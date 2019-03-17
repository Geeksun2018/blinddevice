package cn.finlab.blinddevice.service.serviceImpl;

import cn.finlab.blinddevice.exception.EquipmentIdException;
import cn.finlab.blinddevice.exception.TrajectoryException;
import cn.finlab.blinddevice.mapper.TrajectoryMapper;
import cn.finlab.blinddevice.service.TrajectoryService;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zsw
 * @date 2019/3/16 19:48
 */
@Service("trajectoryService")
public class TrajectoryServiceImpl implements TrajectoryService {

    @Autowired
    TrajectoryMapper trajectoryMapper;

    private static Logger logger = LoggerFactory.getLogger(TrajectoryServiceImpl.class);

    private static final String AK = "9emjMyBccbpTQC0SgdoyXsarumrb2ZX0";
    private static final String SERVICE_ID = "210242";


    private boolean addUserForTrajectory(Integer id) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("ak", AK)
                .add("service_id", SERVICE_ID)
                .add("entity_name", String.valueOf(id))
                .build();
        Request request = new Request.Builder().post(formBody).url("http://yingyan.baidu.com/api/v3/entity/add").build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            String result = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(result);
            String status = jsonObject.getString("status");
            if (!"0".equals(status)) {
                return false;
            } else {
                return true;
            }
        } catch (IOException e) {
            logger.error(e.toString());
            return false;
        }

    }

    @Override
    public boolean addUserTrajectory(Integer id, String longitude, String latitude, String locTime) throws EquipmentIdException {

        if(trajectoryMapper.idExist(id) != 1){
            throw new EquipmentIdException("设备id不存在");
        }

        if(!trajectoryMapper.hasTrajectoryInfo(id)){
            boolean b = addUserForTrajectory(id);
            if(!b){
                return false;
            }
            trajectoryMapper.setTrajectoryInfo(id);
        }

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("ak", AK)
                .add("service_id", SERVICE_ID)
                .add("entity_name", String.valueOf(id))
                .add("latitude", latitude)
                .add("longitude", longitude)
                .add("loc_time", locTime)
                .add("coord_type_input", "wgs84")
                .build();
        Request request = new Request.Builder().post(formBody).url("http://yingyan.baidu.com/api/v3/track/addpoint").build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            String result = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(result);
            String status = jsonObject.getString("status");
            if (!"0".equals(status)) {
                return false;
            } else {
                return true;
            }
        } catch (IOException e) {
            logger.error(e.toString());
            return false;
        }

    }

    @Override
    public Map<String, Object> getUserTrajectory(Integer id, String startTime, String endTime) throws TrajectoryException {

        Map<String, Object> map = new HashMap<>(16);

        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder reqBuild = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://yingyan.baidu.com/api/v3/track/gettrack").newBuilder();
        urlBuilder.addQueryParameter("ak", AK);
        urlBuilder.addQueryParameter("service_id", SERVICE_ID);
        urlBuilder.addQueryParameter("start_time", startTime);
        urlBuilder.addQueryParameter("end_time", endTime);
        urlBuilder.addQueryParameter("entity_name", String.valueOf(id));

        reqBuild.url(urlBuilder.build());
        Request request = reqBuild.build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            String text = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(text);
            String status = jsonObject.getString("status");
            if (!"0".equals(status)) {
                throw new TrajectoryException("轨迹获取失败");
            } else {
                String startPoint = jsonObject.getString("start_point");
                String endPonint = jsonObject.getString("end_point");
                String points = jsonObject.getString("points");

                map.put("start_point", startPoint);
                map.put("end_point", endPonint);
                map.put("points", points);


                return map;
            }

        } catch (IOException e) {
            throw new TrajectoryException("轨迹获取失败");
        }


    }


    public Map<String, Object> getEntity(Integer id) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder reqBuild = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://yingyan.baidu.com/api/v3/entity/list").newBuilder();
        urlBuilder.addQueryParameter("ak", AK);
        urlBuilder.addQueryParameter("service_id", SERVICE_ID);
        String prefix = "entity_names:";
        urlBuilder.addQueryParameter("filter", prefix + id);
        reqBuild.url(urlBuilder.build());
        Request request = reqBuild.build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            String text = response.body().string();
            System.out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
