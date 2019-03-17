package cn.finlab.blinddevice.service;

import cn.finlab.blinddevice.model.UserRoute;

public interface UserRootService {
    public void insertRoute(UserRoute userRoute);

    public UserRoute[] getRoutes(Integer eid);
}
