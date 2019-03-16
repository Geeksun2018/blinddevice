package cn.finlab.blinddevice.service;

import cn.finlab.blinddevice.exception.TrajectoryException;

import java.util.Map;

/**
 * @author zsw
 * @date 2019/3/16 22:03
 */
public interface TrajectoryService {

    /**
     * 在鹰眼轨迹创建硬件信息
     * @param id 硬件id
     * @return
     */
    boolean addUserForTrajectory(Integer id);

    /**
     * 添加轨迹点
     * @param id 硬件id
     * @param longitude 经度
     * @param latitude 纬度
     * @param locTime 定位时的时间戳！时间戳！
     * @return
     */
    boolean addUserTrajectory(Integer id,String longitude,String latitude,String locTime);

    /**
     * 获取一段时间内用户的轨迹
     * @param id 硬件id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @throws  TrajectoryException 获取轨迹失败
     */
    Map<String,Object> getUserTrajectory(Integer id,String startTime,String endTime) throws TrajectoryException;

}