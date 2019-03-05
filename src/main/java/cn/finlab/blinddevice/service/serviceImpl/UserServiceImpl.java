package cn.finlab.blinddevice.service.serviceImpl;

import cn.finlab.blinddevice.mapper.UserInfoMapper;
import cn.finlab.blinddevice.mapper.UserMapper;
import cn.finlab.blinddevice.model.User;
import cn.finlab.blinddevice.model.UserInfo;
import cn.finlab.blinddevice.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    final char []codeSequence = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
    final int SALT_LENGTH = 8;//盐值长度
    final int ENCRYPT_NUM=1024;//加密次数

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public Boolean login(String userName, String password) {

        Subject currentUser = SecurityUtils.getSubject();

        UsernamePasswordToken token=new UsernamePasswordToken(userName,password);
        try {
            currentUser.login(token);//登入验证
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void register(User user)
    {
        String password = user.getPassword();
        String salt=produceSalt();//生成八位的盐值
        ByteSource byteSource=ByteSource.Util.bytes(salt);
        SimpleHash simpleHash=new SimpleHash("md5",password,byteSource,ENCRYPT_NUM);
        user.setPassword(simpleHash.toHex());
        user.setSalt(salt);
        userMapper.register(user);
    }

    @Override
    public User findUserByUserName(String userName) {
        return userMapper.getUserByUserName(userName);
    }

    public String produceSalt()
    {
        StringBuilder randomString= new StringBuilder();
        Random random = new Random();
        for(int i = 0;i < SALT_LENGTH;i++)
        {
            String strRand = null;
            strRand = String.valueOf(codeSequence[random.nextInt(62)]);
            randomString.append(strRand);
        }
        return randomString.toString();
    }

    @Override
    public User getUserByUserId(Integer valueOf) {
        return userMapper.getUserByUserId(valueOf);
    }

    @Override
    public UserInfo getUserInfo(Integer id) {
        UserInfo userInfo = userInfoMapper.getUserInfoById(id);
        return userInfo;
    }

    @Override
    public boolean alterUserInfo(UserInfo userInfo) {
        UserInfo d=userInfoMapper.getUserInfoById(userInfo.getId());
        if (userInfo.getSex()==null){
            userInfo.setSex(d.getSex());
        }
        if (userInfo.getAge()==null){
            userInfo.setAge(d.getAge());
        }
        if (userInfo.getNickName()==null){
            userInfo.setNickName(d.getNickName());
        }
        if (userInfo.getRegion()==null){
            userInfo.setRegion(d.getRegion());
        }
        userInfoMapper.alterUserInfo(userInfo);
        return true;
    }

    @Override
    public Integer getUserIdByEid(Integer eid) {
        return userInfoMapper.getUserIdByEid(eid);
    }
}
