package cn.finlab.blinddevice.service;

import cn.finlab.blinddevice.model.Equipment;

public interface EquipmentService {

    public void insertEquipment(Equipment equipment);

    public Integer getEidByMacAddress(String macAddress);


}
