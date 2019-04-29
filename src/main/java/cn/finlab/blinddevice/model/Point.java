package cn.finlab.blinddevice.model;

/**
 * @author zsw
 * @date 2019/4/29 11:54
 */
public class Point {

    private String longtitude;
    private String latitude;
    private String time;

    public Point(String longtitude, String latitude, String time) {
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.time = time;
    }


    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
