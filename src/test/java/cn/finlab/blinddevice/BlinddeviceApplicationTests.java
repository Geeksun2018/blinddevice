package cn.finlab.blinddevice;

import cn.finlab.blinddevice.model.WalkRoute;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlinddeviceApplicationTests {

    @Test
    public void contextLoads() {
        URL url = null;
        BufferedReader in = null;
        try {
            url = new URL("http://api.map.baidu.com/directionlite/v1/walking?origin=40.01116,116.339303&destination=39.936404,116.452562&ak=XKBZNxGb7eRtxBXbnrDjMVEhHiNKu8Xz");
            if(url.openStream()!=null){
                in = new BufferedReader(new InputStreamReader(url.openStream()));
                String res;
                StringBuilder sb = new StringBuilder("");
                while ((res=in.readLine())!=null) {

                    sb.append(res.trim());
                }
                in.close();
                String str = sb.toString();
                WalkRoute walkRoute = JSON.parseObject(str,WalkRoute.class);
                System.out.println(walkRoute);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }



}
