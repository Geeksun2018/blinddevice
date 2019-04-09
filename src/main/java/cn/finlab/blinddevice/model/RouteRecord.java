package cn.finlab.blinddevice.model;

/**
 * @author zsw
 * @date 2019/3/17 15:28
 */
public class RouteRecord {

    private Integer id;
    private String startTime;
    private String endTime;



    public RouteRecord() {
    }

    @Override
    public String toString() {
        return "RouteRecord{" +
                "id=" + id +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}
