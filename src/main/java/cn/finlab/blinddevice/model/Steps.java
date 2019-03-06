package cn.finlab.blinddevice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Steps {

    private int distance;
    private int duration;
    private int direction;
    private String instruction;
    private String path;
    private Start_location start_location;
    private End_location end_location;
}