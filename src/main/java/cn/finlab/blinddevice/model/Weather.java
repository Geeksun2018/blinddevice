package cn.finlab.blinddevice.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zsw
 */
public class Weather {

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 风力级别
     */
    private String windpower;

    /**
     * 天气现象
     */
    private String weather;

    /**
     * 实时气温
     */
    private String temperature;

    /**
     * 空气湿度
     */
    private String humidity;

    /**
     * 风向描述
     */
    private String winddirection;

    /**
     * 当前时间
     */
    private String nowTime;

    public Weather() {

    }

    public Weather(String province, String city, String windpower, String weather, String temperature, String humidity, String winddirection) {
        this.province = province;
        this.city = city;
        this.windpower = windpower;
        this.weather = weather;
        this.temperature = temperature;
        this.humidity = humidity;
        this.winddirection = winddirection;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        this.nowTime = sdf.format(new Date());
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWindpower() {
        return windpower;
    }

    public void setWindpower(String windpower) {
        this.windpower = windpower;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWinddirection() {
        return winddirection;
    }

    public void setWinddirection(String winddirection) {
        this.winddirection = winddirection;
    }

    public String getNowTime() {
        return nowTime;
    }

    public void setNowTime(String nowTime) {
        this.nowTime = nowTime;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", windpower='" + windpower + '\'' +
                ", weather='" + weather + '\'' +
                ", temperature='" + temperature + '\'' +
                ", humidity='" + humidity + '\'' +
                ", winddirection='" + winddirection + '\'' +
                ", nowTime='" + nowTime + '\'' +
                '}';
    }
}
