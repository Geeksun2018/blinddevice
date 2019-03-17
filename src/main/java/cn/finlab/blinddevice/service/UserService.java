package cn.finlab.blinddevice.service;

import cn.finlab.blinddevice.model.User;
import cn.finlab.blinddevice.model.UserInfo;

public interface UserService {
    /**
     *注册
     * @param user
     */
    public void register(User user);

    public User findUserByUserName(String userName);

    public Boolean login(String userName, String password);

    public User getUserByUserId(Integer valueOf);

    public UserInfo getUserInfo(Integer id);

    public boolean alterUserInfo(UserInfo userInfo);

    public Integer getUserIdByEid(Integer eid);

    public Integer getEidByUid(Integer uid);
}
