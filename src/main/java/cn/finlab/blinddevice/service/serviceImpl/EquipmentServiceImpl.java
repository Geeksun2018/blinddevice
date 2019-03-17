package cn.finlab.blinddevice.service.serviceImpl;

import cn.finlab.blinddevice.mapper.EquipmentMapper;
import cn.finlab.blinddevice.model.Equipment;
import cn.finlab.blinddevice.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Override
    public void insertEquipment(Equipment equipment) {
        equipmentMapper.insertEquipment(equipment);
    }

    @Override
    public Integer getEidByMacAddress(String macAddress) {
        return null;
    }
}
