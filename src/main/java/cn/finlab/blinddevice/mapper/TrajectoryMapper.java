package cn.finlab.blinddevice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author zsw
 * @date 2019/3/17 13:51
 */
@Mapper
public interface TrajectoryMapper {

    /**
     * 是否已在百度鹰眼轨迹创建了信息
     * @param id
     * @return
     */
    @Select("select has_trajectoty_info from equipment where id = #{id}")
    boolean hasTrajectoryInfo(Integer id);

    /**
     * 设备id是否存在
     * @param id
     * @return
     */
    @Select("select count(*) from equipment where id = #{id}")
    int idExist(Integer id);

    /**
     * 设备在鹰眼轨迹创建信息后调用
     * @param id
     */
    @Update("update equipment set has_trajectoty_info = 1 where id = #{id}")
    void setTrajectoryInfo(Integer id);

}
