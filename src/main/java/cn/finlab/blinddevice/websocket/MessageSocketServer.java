package cn.finlab.blinddevice.websocket;

import cn.finlab.blinddevice.service.RedisService;
import cn.finlab.blinddevice.service.UserService;
import cn.finlab.blinddevice.service.serviceImpl.UserServiceImpl;
import cn.finlab.blinddevice.utils.JwtUtils;
import cn.finlab.blinddevice.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;

@Component
@ServerEndpoint("/getMessageServer/{token}/{id}")
public class MessageSocketServer {

    @Autowired
    RedisService redisService;

    public static HashMap<Integer,MessageSocketServer> webSocketMap = new HashMap<>();

    private Session session;
    private Integer id;
    private Integer eid;

    //接受新的连接，并判断权限是否足够
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token, @PathParam("id")Integer id, @PathParam("macAddress")String macAddress) throws IOException {
        this.session = session;
        id= JwtUtils.getId(token);
        if(id == null){
            session.close();
            return;
        }
        webSocketMap.put(id,this);
        UserService userService = SpringUtil.getBean(UserServiceImpl.class);
        eid = userService.getEidByUid(id);
    }

    @OnClose
    public void onClose() throws IOException {
        webSocketMap.remove(this.id);//连接断开，移除session
        this.session.close();
        System.out.println("一个连接断开"+this.id);
    }

    //接受来自客户端的消息
    @OnMessage
    public void onMessage(String message) throws IOException {
        this.session.getAsyncRemote().sendText(message);

    }

    //发送消息
    public void sendMessage(Integer id,String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
}
