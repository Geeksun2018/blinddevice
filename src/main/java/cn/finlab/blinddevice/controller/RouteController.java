package cn.finlab.blinddevice.controller;

import cn.finlab.blinddevice.common.RetJson;
import cn.finlab.blinddevice.exception.TrajectoryException;
import cn.finlab.blinddevice.model.Steps;
import cn.finlab.blinddevice.model.User;
import cn.finlab.blinddevice.model.UserRoute;
import cn.finlab.blinddevice.model.WalkRoute;
import cn.finlab.blinddevice.service.MapService;
import cn.finlab.blinddevice.service.TrajectoryService;
import cn.finlab.blinddevice.service.UserRootService;
import cn.finlab.blinddevice.service.UserService;
import cn.finlab.blinddevice.socket.SocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zsw
 * @date 2019/3/17 15:53
 */
@CrossOrigin
@RestController
public class RouteController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserService userService;

    @Autowired
    TrajectoryService trajectoryService;

    @Autowired
    MapService mapService;

    @Autowired
    UserRootService userRootService;

    /**
     * @param pageNo 第几页
     * @param pageSize 每一页有几条
     * @return
     */
    @GetMapping("/getRoute")
    public Map<String,Object> getRoute(HttpServletRequest request,
                                       @RequestParam("pageNo")Integer pageNo,
                                       @RequestParam("pageSize") Integer pageSize){
//        Integer eid = 1;
        User user = (User) request.getAttribute("user");
        int userId = user.getId();
        Integer eid = userService.getEidByUid(userId);
        try {
            Map<String, Object> map = trajectoryService.getUserNavigationRecord(eid, pageNo, pageSize);
            //请求成功
            map.put("code","0");
            return map;
        } catch (ParseException e) {
            logger.error("数据库时间存储格式错误");
            logger.error(e.toString());
            Map<String,Object> map = new HashMap<>();
            map.put("code","-1");
            map.put("msg","服务器错误");
            return map;
        }
    }

    @GetMapping("/getPosition")
    public Map<String,Object> getPosition(HttpServletRequest request,
                                          @RequestParam("startTime")Long startTime,
                                          @RequestParam("endTime")Long endTime){
        User user= (User) request.getAttribute("user");
        int userId = user.getId();
        Integer eid = userService.getEidByUid(userId);
        try {
            Map<String, Object> userTrajectory = trajectoryService.getUserTrajectory(eid, String.valueOf(startTime), String.valueOf(endTime));
            return userTrajectory;
        } catch (TrajectoryException e) {
            Map<String, Object> map = new HashMap<>(16);
            map.put("msg","轨迹获取失败");
            logger.error(e.toString());
            return map;
        }
    }

    @GetMapping("/navigation")
    public RetJson navigation(@RequestParam("start")String start,
                              @RequestParam("end")String end,
                              @RequestParam("uid")Integer uid){
        Integer eid = userService.getEidByUid(uid);
        List<Steps> steps = null;
        Map<Integer, WalkRoute> stepsMap = SocketServer.getStepsMap();
        Map<Integer,Integer> stepMap = SocketServer.getStepMap();
        Date now = new Date();
        WalkRoute walkRoute = mapService.getWalkRoute(start,end);
        steps = walkRoute.getResult().getRoutes().get(0).getSteps();
        for(int i = 0;i < steps.size();i++){
            Steps steps1 = steps.get(i);
            steps1.setInstruction(steps1.getInstruction().replace("<b>",""));
            steps1.setInstruction(steps1.getInstruction().replace("</b>",""));
            steps.set(i,steps1);
        }
        stepsMap.put(uid,walkRoute);
        //当前时间再加100分钟
        UserRoute userRoute = new UserRoute(uid,now,new Date(now.getTime() + 6000000),eid);
        userRootService.insertRoute(userRoute);
        stepMap.put(uid,0);
        return RetJson.succcess(null);
    }

    @GetMapping("/getLocation")
    public RetJson navigation(@RequestParam("uid")Integer uid){
        if(SocketServer.locationMap.get(uid) == null){
            return RetJson.succcess("location","113.017001,28.070051");
        }
        return RetJson.succcess("location",SocketServer.locationMap.get(uid));
    }

}
