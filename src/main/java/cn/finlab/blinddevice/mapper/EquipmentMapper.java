package cn.finlab.blinddevice.mapper;

import cn.finlab.blinddevice.model.Equipment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EquipmentMapper {

    @Insert("insert into equipment(mac_address) values(#{macAddress})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    public void insertEquipment(Equipment equipment);

    @Select("select id from equipment where mac_address=#{macAddress}")
    public Integer getEidByMacAddress(String macAddress);

}
