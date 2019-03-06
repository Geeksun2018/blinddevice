package cn.finlab.blinddevice.service.serviceImpl;

import cn.finlab.blinddevice.model.WalkRoute;
import cn.finlab.blinddevice.service.MapService;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

@Service
public class MapServiceImpl implements MapService {
    @Override
    public WalkRoute getWalkRoute(String origin,String destination) {
        URL url = null;
        BufferedReader in = null;
        WalkRoute walkRoute = null;
        try {
            url = new URL("http://api.map.baidu.com/directionlite/v1/walking?origin=" + origin + "&destination=" + destination + "&ak=XKBZNxGb7eRtxBXbnrDjMVEhHiNKu8Xz");
            if(url.openStream()!=null){
                in = new BufferedReader(new InputStreamReader(url.openStream()));
                String res;
                StringBuilder sb = new StringBuilder("");
                while ((res=in.readLine())!=null) {
                    sb.append(res.trim());
                }
                in.close();
                String str = sb.toString();
                walkRoute = JSON.parseObject(str,WalkRoute.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return walkRoute;
    }
}
