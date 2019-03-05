package cn.finlab.blinddevice.mapper;

import cn.finlab.blinddevice.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserInfoMapper {

    @Select("select * from user_info where id=#{id}")
    public UserInfo getUserInfoById(@Param("id") Integer id);

    @Update({"update user_info set sex=#{sex},nickName=#{nickName},region=#{region} where id=#{id}"})
    public void alterUserInfo(UserInfo userInfo);

    @Update("update user_info set eid=#{eid} where id=#{id}")
    public boolean alterEquipmentId(@Param("eid") Integer eid,@Param("id") Integer id);

    @Select("select id from user_info where eid=#{eid}")
    public Integer getUserIdByEid(@Param("eid") Integer eid);

}
