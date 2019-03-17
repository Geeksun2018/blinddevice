package cn.finlab.blinddevice.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    //精度Lng 导航的时候传入目标纬度,经度
    private String lng;
    //纬度lat,导航的时候传入起始纬度,经度
    private String lat;
    //行走的方向
    private String direction;
    //是否为导航模式
    private Integer navigation;
    //导航模式下，当前走到了，第几个阶段
    private Integer step;
    //是否为路口
    private Integer isIntersection;
    //硬件的mac地址
    private String macAddress;


    @Override
    public String toString() {
        try {
            ObjectMapper mapper=new ObjectMapper();
            String json = mapper.writeValueAsString(this);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "{}";
    }
}
