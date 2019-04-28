package cn.finlab.blinddevice.service.serviceImpl;

import cn.finlab.blinddevice.exception.GetWeatherException;
import cn.finlab.blinddevice.model.Weather;
import cn.finlab.blinddevice.service.WeatherService;
import com.alibaba.fastjson.JSONObject;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author zsw
 */
@Service("weatherService")
public class WeatherServiceImpl implements WeatherService {

    private static final String APP_KEY = "b631bc6d5021c776f82ffafdd4be857b";

    /**
     * 获取当前天气的
     */
    private static final String BASE_WEATHER = "base";

    private static final String ALL_WEATHER = "all";

    /**
     * 对经纬度参数进行处理
     *
     * @param location
     * @return
     */
    private String locationParamHandle(String location) {
        String[] strings = location.split("\\.");
        String smallNum = strings[1];

        if (smallNum != null && smallNum.length() > 6) {
            smallNum = smallNum.substring(0, 6);
        }
        String handleLocation = strings[0];
        if (smallNum != null) {
            handleLocation = handleLocation + "." + smallNum;
        }
        return handleLocation;
    }

    @Override
    public String getAdcode(String longitude, String latitude) throws GetWeatherException {

        longitude = locationParamHandle(longitude);
        latitude = locationParamHandle(latitude);


        String location = longitude + "," + latitude;

        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder reqBuild = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://restapi.amap.com/v3/geocode/regeo").newBuilder();
        urlBuilder.addQueryParameter("key", APP_KEY);
        urlBuilder.addQueryParameter("location", location);

        reqBuild.url(urlBuilder.build());
        Request request = reqBuild.build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            String text = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(text);
            String regeocode = jsonObject.getString("regeocode");
            JSONObject jsonObject1 = JSONObject.parseObject(regeocode);
            String addressComponent = jsonObject1.getString("addressComponent");
            JSONObject jsonObject2 = JSONObject.parseObject(addressComponent);
            String adcode = jsonObject2.getString("adcode");
            return adcode;
        } catch (IOException e) {
            throw new GetWeatherException();
        }
    }

    @Override
    public Weather getBaseWeather(String longitude, String latitude) throws GetWeatherException {
        longitude = locationParamHandle(longitude);
        latitude = locationParamHandle(latitude);

        String adcode = null;
        adcode = getAdcode(longitude, latitude);
        OkHttpClient okHttpClient = new OkHttpClient();

        Request.Builder reqBuild = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://restapi.amap.com/v3/weather/weatherInfo?parameters").newBuilder();

        urlBuilder.addQueryParameter("key", APP_KEY);
        urlBuilder.addQueryParameter("city", adcode);
        urlBuilder.addQueryParameter("extensions", BASE_WEATHER);
        urlBuilder.addQueryParameter("output", "JSON");

        reqBuild.url(urlBuilder.build());
        Request request = reqBuild.build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            String text = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(text);
            String lives = jsonObject.getString("lives");
            lives = lives.substring(1, lives.length() - 1);

            JSONObject livesObject = JSONObject.parseObject(lives);

            String province = livesObject.getString("province");
            String city = livesObject.getString("city");
            String windpower = livesObject.getString("windpower");
            String weathers = livesObject.getString("weather");
            String temperature = livesObject.getString("temperature");
            String humidity = livesObject.getString("humidity");
            String winddirection = livesObject.getString("winddirection");

            Weather weather = new Weather(province, city, windpower, weathers, temperature, humidity, winddirection);
            return weather;
        } catch (IOException e) {
            throw new GetWeatherException();
        }

    }

}
