package cn.finlab.blinddevice.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author zsw
 */
public class SendShortMsgUtil {

    static Logger logger = LoggerFactory.getLogger(SendShortMsgUtil.class);

    /**
     * 签名
     */
    private static final String SMS_SIGN = "顺手APP";

    /**
     * 申请到的 appkey
     */
    private static final String APPKEY = "c468709ea4fc1d5a945ad6f08decfc05";

    /**
     * appid
     */
    private static final int APPID = 1400181857;

    /**
     * 注册模板id
     */
    private static final int ADD_USER_TEMPLATE_ID = 269510;

    /**
     * 找回密码模板id
     */
    private static final int RESET_PASSWOR_TEMPLATE_ID = 270300;

    /**
     * 更改手机号模板id
     */
    private static final int RESET_PHONE_TEMPLATE_ID = 270302;


    /**
     * 发送注册短信
     * @param phone 手机号
     * @param code 验证码
     * @param timeMax 有效时间 以秒为单位
     * @return
     */
    public static boolean sendRegisterMsg(String phone, String code,int timeMax) {
        return sendMsg(phone, code, timeMax,ADD_USER_TEMPLATE_ID);
    }

    /**
     * 重置密码短信
     * @param phone 手机号
     * @param code 验证码
     * @param timeMax 有效时间 以秒为单位
     * @return
     */
    public static boolean sendResetPsdMsg(String phone,String code,int timeMax){
        return sendMsg(phone, code, timeMax,RESET_PASSWOR_TEMPLATE_ID);
    }

    private static boolean sendMsg(String phone, String code, int timeMax,int templateId) {
        String time = String.valueOf(timeMax);

        String[] params = {code, time};

        SmsSingleSender ssender = new SmsSingleSender(APPID, APPKEY);
        try {
            SmsSingleSenderResult result = ssender.sendWithParam("86", phone, templateId, params, SMS_SIGN, "", "");

            JSONObject jsonObject = JSON.parseObject(String.valueOf(result));

            int status = (int) jsonObject.get("result");
            if(status != 0){
                return false;
            }
            return true;
        } catch (HTTPException | IOException e) {
            logger.warn("发送短信失败,信息:" + e.getMessage());
            return false;
        }
    }

    /**
     * 发送重置手机号短信
     * @param phone
     * @param code
     * @param timeMax 以秒为单位
     * @return
     */
    public static boolean sendResetPhoneMsg(String phone,String code,int timeMax){

        return sendMsg(phone,code,timeMax,RESET_PHONE_TEMPLATE_ID);
    }



    /**
     * 生成六位随机数字验证码
     *
     * @return
     */
    public static String randomCode() {
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        return String.valueOf(code);
    }


}
