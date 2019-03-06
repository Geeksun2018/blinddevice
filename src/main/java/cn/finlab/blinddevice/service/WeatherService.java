package cn.finlab.blinddevice.service;

import cn.finlab.blinddevice.exception.GetWeatherException;
import cn.finlab.blinddevice.model.Weather;

/**
 * @author zsw
 */
public interface WeatherService {

    /**
     * 根据经纬度获取当前城市编码
     * @param longitude 经度
     * @param latitude 纬度
     * @return
     */
    public String getAdcode(String longitude, String latitude) throws GetWeatherException;

    /**
     * 根据经纬度获取当前天气
     * @param longitude 经度
     * @param latitude 纬度
     * @return
     */
    public Weather getBaseWeather(String longitude, String latitude) throws GetWeatherException;

    /**
     * 根据经纬度获取天气预报
     * @param longitude
     * @param latitude
     * @return
     */
    public Weather getAllWeather(String longitude, String latitude) throws GetWeatherException;

}
