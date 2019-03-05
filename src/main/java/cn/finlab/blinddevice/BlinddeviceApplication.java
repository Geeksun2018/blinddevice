package cn.finlab.blinddevice;

import cn.finlab.blinddevice.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BlinddeviceApplication{

    public static void main(String[] args) {
        ApplicationContext app=SpringApplication.run(BlinddeviceApplication.class, args);
        SpringUtil.setApplicationContext(app);
    }

}
