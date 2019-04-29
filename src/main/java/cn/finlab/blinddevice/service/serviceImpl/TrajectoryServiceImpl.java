package cn.finlab.blinddevice.service.serviceImpl;

import cn.finlab.blinddevice.exception.EquipmentIdException;
import cn.finlab.blinddevice.exception.TrajectoryException;
import cn.finlab.blinddevice.mapper.RouteRecordMapper;
import cn.finlab.blinddevice.mapper.TrajectoryMapper;
import cn.finlab.blinddevice.model.Point;
import cn.finlab.blinddevice.model.RouteRecord;
import cn.finlab.blinddevice.service.TrajectoryService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zsw
 * @date 2019/3/16 19:48
 */
@Service("trajectoryService")
public class TrajectoryServiceImpl implements TrajectoryService {

    @Autowired
    TrajectoryMapper trajectoryMapper;

    @Autowired
    RouteRecordMapper recordMapper;

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addUserTrajectory(Integer id, String longitude, String latitude, String locTime) throws EquipmentIdException {

        if (trajectoryMapper.idExist(id) != 1) {
            throw new EquipmentIdException("设备id不存在");
        }

        if (!trajectoryMapper.hasTrajectoryInfo(id)) {
            boolean b = addUserForTrajectory(id);
            if (!b) {
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

        if (!trajectoryMapper.hasTrajectoryInfo(id)) {
            map.put("code", "1");
            map.put("msg", "未添加过轨迹");
            return map;
        }

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
                JSONObject start_point = jsonObject.getJSONObject("start_point");
                String latitude = start_point.getString("latitude");
                String longitude = start_point.getString("longitude");
                String loc_time = start_point.getString("loc_time");
                Point startPoint = new Point(longitude, latitude, loc_time);
                map.put("startPoint", startPoint);

                JSONObject end_point = jsonObject.getJSONObject("end_point");
                latitude = end_point.getString("latitude");
                longitude = end_point.getString("longitude");
                loc_time = end_point.getString("loc_time");
                Point endPoint = new Point(longitude, latitude, loc_time);
                map.put("endPoint", endPoint);

                JSONArray jsonArray = jsonObject.getJSONArray("points");
                List<Point> positions = new ArrayList<>();
                for (Object o : jsonArray) {
                    JSONObject object =(JSONObject) o;
                    String longitude1 = object.getString("longitude");
                    String latitude1 = object.getString("latitude");
                    String time = object.getString("loc_time");
                    Point point = new Point(longitude1,latitude1,time);
                    positions.add(point);
                }
                map.put("points",positions);
                return map;
            }

        } catch (IOException e) {
            throw new TrajectoryException("轨迹获取失败");
        }


    }


    @Override
    public Map<String, Object> getUserNavigationRecord(Integer id, @RequestParam(defaultValue = "1") Integer pageNo,
                                                       @RequestParam(defaultValue = "3") Integer pageSize) throws ParseException {
        //逆序排序
        String orderBy = "id desc";
        Map<String, Object> map = new HashMap<>(16);
        PageHelper.startPage(pageNo, pageSize, orderBy);
        Page<RouteRecord> records = recordMapper.getRouteRecord(id);
        for (RouteRecord record : records) {
            record.setStartTime(dateToStamp(record.getStartTime()));
            record.setEndTime(dateToStamp(record.getEndTime()));
        }
        long total = records.getTotal();
        map.put("routeRecord", records);
        map.put("total", total);
        return map;
    }


    private Map<String, Object> getEntity(Integer id) {
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

    /**
     * date转时间戳
     *
     * @param s
     * @return
     */
    private static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts / 1000);
        return res;
    }

}
