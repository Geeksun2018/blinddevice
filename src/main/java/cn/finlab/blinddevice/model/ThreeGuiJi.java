package cn.finlab.blinddevice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author zsw
 * @date 2019/4/29 21:54
 */
@Data
@AllArgsConstructor
public class ThreeGuiJi {

    private Point startPoint;
    private Point endPoint;
    private List<Point> points;

}
