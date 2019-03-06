package cn.finlab.blinddevice.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    private String lng;
    private String lat;
    private String direction;
    private Integer navigation;
    private Integer step;
    private Integer isIntersection;

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
