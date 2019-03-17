package cn.finlab.blinddevice.mapper;

import cn.finlab.blinddevice.model.UserRoute;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserRouteMapper {

    @Insert("insert into route(start_time,end_time,eid) values(#{startTime},#{endTime},#{eid})")
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    public void insertRoute(UserRoute userRoute);

    @Select("select * from route where eid=#{eid}")
    @Results({
            @Result(column = "start_time",property = "startTime"),
            @Result(column = "end_time",property = "endTime"),
    })
    public UserRoute[] getRoutes(Integer eid);

}
