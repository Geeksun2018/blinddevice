package cn.finlab.blinddevice.mapper;

import cn.finlab.blinddevice.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserInfoMapper {

    @Select("select * from userinfo where id=#{id}")
    public UserInfo getUserInfoById(@Param("id") Integer id);

    @Update({"update userinfo set sex=#{sex},nickName=#{nickName},region=#{region} where id=#{id}"})
    public void alterUserInfo(UserInfo userInfo);

    @Update("update userinfo set eid=#{eid} where id=#{id}")
    public boolean alterEquipmentId(@Param("eid") Integer eid,@Param("id") Integer id);

    @Select("select id from userinfo where eid=#{eid}")
    public Integer getUserIdByEid(@Param("eid") Integer eid);

    @Select("select eid from userinfo where id=#{uid}")
    public Integer getEidByUid(Integer uid);

}
