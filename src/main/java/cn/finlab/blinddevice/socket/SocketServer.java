package cn.finlab.blinddevice.socket;

import cn.finlab.blinddevice.model.*;
import cn.finlab.blinddevice.service.MapService;
import cn.finlab.blinddevice.service.serviceImpl.MapServiceImpl;
import cn.finlab.blinddevice.utils.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer
{
    private ExecutorService executorService;
    private ServerSocket serverSocket;
    public static Map<String, Socket> socketMap;
    public static Map<String,WalkRoute> stepsMap;

    public SocketServer()
    {
        Socket client = null;
        try
        {
            socketMap = new HashMap<>();
            stepsMap = new HashMap<>();
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
            try
            {
                client = socketMap.get(ip);
                ins = client.getInputStream();
                ous = client.getOutputStream();
                br = new BufferedReader(new InputStreamReader(ins));
                while (true)
                {
                    while(ins.available() > 0)
                    {
                        byte[] data = new byte[ins.available()];
                        ins.read(data);
                        resultData = new String(data);
                        System.out.println(resultData);
//                        resultData = resultData.replace('\'','\"');
//                        resultData = resultData.replaceAll("None","null");
                        try {
                            message = JSON.parseObject(resultData, Message.class);
                        }catch (JSONException e){
                            e.printStackTrace();
                            ous.write("json格式错误！\r".getBytes());
                        }
                        //如果当前为导航模式
                        if(message.getNavigation().equals(0)){
                            System.out.println(message);
                            WalkRoute walkRoute = mapService.getWalkRoute(message.getLat(),message.getLng());
                            steps = walkRoute.getResult().getRoutes().get(0).getSteps();
                            stepsMap.put(ip,walkRoute);
                            ous.write(("there are" + steps.size()+ " steps " + "\r").getBytes());
                        }
                        else {
                            steps = stepsMap.get(ip).getResult().getRoutes().get(0).getSteps();
                            String lat = message.getLat();
                            String lng = message.getLng();
                            Start_location start_location = new Start_location(lng,lat);
                            End_location end_location = steps.get(message.getStep()).getEnd_location();
                            if(getDistance(start_location,end_location) < 0.5||message.getIsIntersection().equals(1)){
                                ous.write(steps.get(message.getStep()).getInstruction().getBytes());
                            }
                        }
                    }
//					while ((resultData = br.readLine()) != null)
//					{
//						if (resultData.equals("{beat}"))
//						{
//							ous.write("{beat}".getBytes());
//							ous.flush();
//						}
//						MessageQueue.addToQueue(resultData);
//						System.out.println("receive:" + resultData);
//					}
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
