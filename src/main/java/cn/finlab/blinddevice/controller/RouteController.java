package cn.finlab.blinddevice.controller;

import cn.finlab.blinddevice.exception.TrajectoryException;
import cn.finlab.blinddevice.service.TrajectoryService;
import cn.finlab.blinddevice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.HashMap;
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

    /**
     *
     * @param userId
     * @param pageNo 第几页
     * @param pageSize 每一页有几条
     * @return
     */
    @GetMapping("/getRoute")
    public Map<String,Object> getRoute(@RequestParam("id") Integer userId,
                                       @RequestParam("pageNo")Integer pageNo,
                                       @RequestParam("pageSize") Integer pageSize){
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
    public Map<String,Object> getPosition(@RequestParam("id")Integer userId,
                                          @RequestParam("startTime")Long startTime,
                                          @RequestParam("endTime")Long endTime){
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

}
