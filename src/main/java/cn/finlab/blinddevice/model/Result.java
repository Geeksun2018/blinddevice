package cn.finlab.blinddevice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Result {
    private Origin origin;
    private Destination destination;
    private List<Routes> routes;
}