package cn.finlab.blinddevice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Routes {

    private int distance;
    private int duration;
    private List<Steps> steps;
}