package cn.finlab.blinddevice.service;

import cn.finlab.blinddevice.model.WalkRoute;

public interface MapService {

    public WalkRoute getWalkRoute(String origin,String destination);

}
