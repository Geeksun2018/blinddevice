package cn.finlab.blinddevice.controller;

import cn.finlab.blinddevice.common.RetJson;
import cn.finlab.blinddevice.model.User;
import cn.finlab.blinddevice.model.UserInfo;
import cn.finlab.blinddevice.service.RedisService;
import cn.finlab.blinddevice.service.UserService;
import cn.finlab.blinddevice.utils.GenerateVerificationCode;
import cn.finlab.blinddevice.utils.JwtUtils;
import cn.finlab.blinddevice.utils.SendShortMsgUtil;
import cn.finlab.blinddevice.utils.ValidatedUtil;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    //登入
    @RequestMapping("/login")
    public RetJson login(@Valid User user, HttpServletRequest request){
        if (!ValidatedUtil.validate(user)){
            return RetJson.fail(-1,"登入失败，请检查用户名或密码");
        }
        Boolean b=userService.login(user.getUsername(),user.getPassword());
        if (b==true){
            user=userService.findUserByUserName(user.getUsername());
            request.setAttribute("id",user.getId()+"");
            //登入成功,并且设置了记住我,则发放token
            if (true){
                //手机app
                try {
                    //生成一个随机的不重复的uuid
                    UUID uuid=UUID.randomUUID();
                    request.setAttribute("uuid",uuid.toString());
                    String token= JwtUtils.createToken(uuid,user.getId().toString());
                    //将uuid和user以键值对的形式存放在redis中
                    user.setPassword(null);
                    user.setSalt(null);
                    redisService.set("user:"+user.getId(),uuid.toString(),60*60*24*7);

                    Map map = new LinkedHashMap();
                    map.put("token",token);
                    map.put("id",user.getId());
                    return RetJson.succcess(map);
                }catch (Exception e){
                    System.out.println("token获取失败");
                }
            }
            Map<String,Object> map=new LinkedHashMap<>();
            map.put("id",user.getId());
            return RetJson.succcess(map);
        }else {
            return RetJson.fail(-1,"登入失败,请检查用户名或密码");
        }
    }

    @RequestMapping("/register")
    public RetJson userRegister(@Valid User user, String code) {
        if (redisService.exists(user.getUsername()) && redisService.get(user.getUsername()).equals(code)) {
            if (true) {
                if (userService.findUserByUserName(user.getUsername()) == null) {
                    return RetJson.succcess(null);
                }
                return RetJson.fail(-1, "用户已存在！");
            }
        }
        return RetJson.fail(-1, "验证码不正确！");
    }

    /**
     * 获取用户信息
     * @param id 用户id
     * @param request
     * @return
     */
    @RequestMapping("/getUserInfo")
    public RetJson getUserInfo(Integer id, HttpServletRequest request){
        UserInfo userInfo=userService.getUserInfo(id);
        if (userInfo==null){
            return RetJson.fail(-1,"获取用户信息失败");
        }else{

            return RetJson.succcess("userInfo",userInfo);
        }
    }

    /**
     * 修改用户信息
     * @param userInfo 用户信息，字段为：
     * @param request
     * @return
     */
    @RequestMapping("/alterUserInfo")
    public RetJson alterUserInfo(@Valid UserInfo userInfo, HttpServletRequest request){
        if (!ValidatedUtil.validate(userInfo)){
            return RetJson.fail(-1,"请检查参数");
        }
        Integer id = ((User)request.getAttribute("user")).getId();
        userInfo.setId(id);
        UserInfo pastUserInfo = userService.getUserInfo(id);
        copyFieldValue(userInfo,pastUserInfo);
        userService.alterUserInfo(userInfo);
        return RetJson.succcess(null);
    }

    /**
     * 绑定邮箱
     * @param email 邮箱地址
     * @param code 邮箱验证码
     * @return
     */
    @RequestMapping("/bindMailbox")
    public RetJson bindMailbox(@Validated @Email String email, String code){
        String redisCode=(String) redisService.get(email);
        if (code.equals(redisCode)){
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,"邮箱验证码错误");
    }

    /**
     * 获取邮箱验证码
     * @param email 邮箱地址
     */
    @RequestMapping("/getEmailCode")
    public RetJson getEmailCode(@Validated @Email String email){
//        emailService.sentVerificationCode(email);
        return RetJson.succcess(null);
    }

    @RequestMapping("/getCode")
    public RetJson getCode(@Validated @Length(max = 11,min = 11, message = "手机号的长度必须是11位.") String phoneNum){
//        if (userService.findUserByUserName(phoneNum)!=null){
//            return RetJson.fail(-1,"该用户已经注册");
//        }
        String verificationCode = GenerateVerificationCode.generateVerificationCode(4);
        if(!SendShortMsgUtil.sendRegisterMsg(phoneNum,verificationCode,3)){
            return RetJson.fail(-1,"发送失败！");
        }
        redisService.set(phoneNum,verificationCode,300);
        return RetJson.succcess(null);
    }



    public  void copyFieldValue(UserInfo userInfo,UserInfo pastUserInfo){
        for(Field f : userInfo.getClass().getDeclaredFields()){
            f.setAccessible(true);
            try {
                if(f.get(userInfo) == null&&f.get(pastUserInfo) != null){
                    f.set(userInfo,f.get(pastUserInfo));
                }
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }
        }
    }

}
