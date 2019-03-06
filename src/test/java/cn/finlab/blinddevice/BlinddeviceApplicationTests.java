package cn.finlab.blinddevice;

import cn.finlab.blinddevice.model.End_location;
import cn.finlab.blinddevice.model.Start_location;
import cn.finlab.blinddevice.model.WalkRoute;
import cn.finlab.blinddevice.service.MapService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BlinddeviceApplicationTests {

    @Autowired
    MapService mapService;

    @Test
    public void contextLoads() {
        //精度Lng
        //纬度lat
        //34.269545, 108.922495
        //34.269917, 108.947230
        //2.27公里
//        Start_location start_location = new Start_location("34.269545","108.922495");
//        End_location end_location = new End_location("34.269917","108.947230");
        Start_location start_location = new Start_location("34.341009","108.929663");
        End_location end_location = new End_location("34.341044","108.937720");
        WalkRoute walkRoute = mapService.getWalkRoute("40.01116,116.339303","39.936404,116.452562");
//        System.out.println(getDistance(start_location,end_location));
    }
    @Test
    public void test(){
        System.out.println("test");
    }




}
