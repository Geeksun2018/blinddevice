package cn.finlab.blinddevice.service.serviceImpl;

import cn.finlab.blinddevice.mapper.UserRouteMapper;
import cn.finlab.blinddevice.model.UserRoute;
import cn.finlab.blinddevice.service.UserRootService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRouteServiceImpl implements UserRootService {

    @Autowired
    private UserRouteMapper userRouteMapper;

    @Override
    public void insertRoute(UserRoute userRoute) {
        userRouteMapper.insertRoute(userRoute);
    }

    @Override
    public UserRoute[] getRoutes(Integer eid) {
        return userRouteMapper.getRoutes(eid);
    }
}
