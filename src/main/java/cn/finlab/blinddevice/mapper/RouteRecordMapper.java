package cn.finlab.blinddevice.mapper;

import cn.finlab.blinddevice.model.RouteRecord;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author zsw
 * @date 2019/3/17 15:31
 */
@Mapper
public interface RouteRecordMapper {

    /**
     * 获取历史导航起始时间
     * @param id
     * @return
     */
    @Select("select * from route where eid = #{id}")
    Page<RouteRecord> getRouteRecord(Integer id);

}
