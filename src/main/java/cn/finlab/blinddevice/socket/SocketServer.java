package cn.finlab.blinddevice.socket;

import cn.finlab.blinddevice.exception.EquipmentIdException;
import cn.finlab.blinddevice.model.*;
import cn.finlab.blinddevice.service.*;
import cn.finlab.blinddevice.service.serviceImpl.*;
import cn.finlab.blinddevice.utils.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer
{
    private ExecutorService executorService;
    private ServerSocket serverSocket;
    private static Map<String, Socket> socketMap;
    private static Map<String,WalkRoute> stepsMap;
    private static Map<String,String> locationMap;
    private static Map<String,Integer> stepMap;

    public SocketServer()
    {
        Socket client = null;
        try
        {
            socketMap = new HashMap<>();
            stepsMap = new HashMap<>();
            locationMap = new HashMap<>();
            stepMap = new HashMap<>();
            serverSocket = new ServerSocket(8888);
            System.out.println("你的ip为" + serverSocket.getInetAddress().getHostAddress());
            executorService = Executors.newCachedThreadPool();
            while (true)
            {
                client = serverSocket.accept();
                String ip = client.getInetAddress().getHostAddress();
                System.out.println("ip:" + ip);
                socketMap.put(ip, client);
                executorService.execute(new Thread(ip));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                client.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    class Thread implements Runnable{
        private String ip;

        public Thread(String ip){
            this.ip = ip;
        }

        @Override
        public void run()
        {
            Socket client = null;
            InputStream ins = null;
            OutputStream ous = null;
            BufferedReader br = null;
            String resultData = null;
            List<Steps> steps = null;
            Message message = null;
            MapService mapService = SpringUtil.getBean(MapServiceImpl.class);
            TrajectoryService trajectoryService = SpringUtil.getBean(TrajectoryServiceImpl.class);
            EquipmentService equipmentService = SpringUtil.getBean(EquipmentServiceImpl.class);
            UserService userService = SpringUtil.getBean(UserServiceImpl.class);
            UserRootService userRootService = SpringUtil.getBean(UserRouteServiceImpl.class);
            try
            {
                client = socketMap.get(ip);
                ins = client.getInputStream();
                ous = client.getOutputStream();
                br = new BufferedReader(new InputStreamReader(ins));
                Integer uid = null;
                Integer eid = null;
                Date now = null;
                while (true)
                {
                    while(ins.available() > 0)
                    {
                        byte[] data = new byte[ins.available()];
                        ins.read(data);
                        resultData = new String(data);
                        System.out.println(resultData);
                        try {
                            message = JSON.parseObject(resultData, Message.class);
                        }catch (JSONException e){
                            e.printStackTrace();
                            ous.write("json格式错误！\r".getBytes());
                        }
                        //根据mac地址，得到用户的id
                        eid = equipmentService.getEidByMacAddress(message.getMacAddress());
                        uid = userService.getUserIdByEid(eid);
                        if(uid == null){
                            ous.write("用户尚未绑定！".getBytes());
                        }
                        //如果当前为导航模式
                        if(message.getNavigation().equals(0)){
                            now = new Date();
                            System.out.println(message);
                            WalkRoute walkRoute = mapService.getWalkRoute(message.getLat(),message.getLng());
                            steps = walkRoute.getResult().getRoutes().get(0).getSteps();
                            stepsMap.put(ip,walkRoute);
                            locationMap.put(ip,message.getLat());
                            //发送导航总阶段数
                            ous.write(("there are" + steps.size()+ " steps " + "\r").getBytes());
                            //当前时间再加100分钟
                            UserRoute userRoute = new UserRoute(uid,now,new Date(now.getTime() + 6000000),eid);
                            userRootService.insertRoute(userRoute);
                            stepMap.put(ip,1);
                            ous.write(steps.get(0).getInstruction().getBytes());
                        }
                        else {
                            steps = stepsMap.get(ip).getResult().getRoutes().get(0).getSteps();
                            String[] pointBegin = message.getLat().split(",");
                            String lng = pointBegin[1];
                            String lat = pointBegin[0];
                            Start_location start_location = new Start_location(lng,lat);
                            //这个结束定位指的是当前阶段的结束点
                            End_location end_location = steps.get(stepMap.get(ip)).getEnd_location();
                            //添加轨迹到百度鹰眼   这里应该是有一个异常的
                            try{
                                trajectoryService.addUserTrajectory(eid,lng,lat,String.valueOf(new Date().getTime()/1000));
                            }catch (EquipmentIdException e){
                                ous.write("您的设备尚未注册!".getBytes());
                            }
                            //如果离目标点的距离小于0.5km或者人到了路口 就跳到下一个阶段(路口无法检测！！！)
                            if(getDistance(start_location,end_location) < 0.2||message.getIsIntersection()==1){
                                Integer step = stepMap.get(ip);
                                if(step >= steps.size()){
                                    ous.write("您已到达目的地！".getBytes());
                                    return;
                                }
                                stepMap.put(ip,++step);
                                ous.write(steps.get(step).getInstruction().getBytes());
                            }
                        }
                    }
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            } finally
            {
                try
                {
                    br.close();
                    ins.close();
                    client.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void sendMessage(String message,String ip){
        Socket socket = SocketServer.socketMap.get(ip);
        OutputStream ous = null;
        if(socket == null){
            return;
        }
        try {
            ous = socket.getOutputStream();
            ous.write(message.getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public double getDistance(Start_location start, End_location end){
        double lat1 = (Math.PI/180)*(new Double(start.getLat()));
        double lat2 = (Math.PI/180)*(new Double(end.getLat()));

        double lon1 = (Math.PI/180)*(new Double(start.getLng()));
        double lon2 = (Math.PI/180)*(new Double(end.getLng()));

        //地球半径
        double R = 6371.004;

        //两点间距离 km，如果想要米的话，结果*1000就可以了
        double d =  Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1))*R;

        return d;
    }

}
