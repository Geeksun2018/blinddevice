package cn.finlab.blinddevice.mapper;

import cn.finlab.blinddevice.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert({"insert into user(username,password,salt) values(#{username},#{password},#{salt})"})
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    public void register(User user);

    @Select("select * from user where username=#{username}")
    public User getUserByUserName(@Param("username") String username);

    @Select("select * from user where id=#{id}")
    public User getUserByUserId(@Param("id") Integer id);

}
