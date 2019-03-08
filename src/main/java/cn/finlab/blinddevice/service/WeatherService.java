package cn.finlab.blinddevice.service;

import cn.finlab.blinddevice.exception.GetWeatherException;
import cn.finlab.blinddevice.model.Weather;

/**
 * @author zsw
 */
public interface WeatherService {

    /**
     * 根据经纬度获取当前城市编码
     * @param longitude 小数点后最多六位
     * @param latitude 小数点后最多六位
     * @return
     * @throws GetWeatherException 获取城市编码异常
     */
    public String getAdcode(String longitude, String latitude) throws GetWeatherException;

    /**
     * 根据经纬度获取当前天气
     * @param longitude 经度 小数点后最多六位
     * @param latitude 纬度 小数点后最多六位
     * @return
     * @throws GetWeatherException 天气获取异常
     */
    public Weather getBaseWeather(String longitude, String latitude) throws GetWeatherException;


}
