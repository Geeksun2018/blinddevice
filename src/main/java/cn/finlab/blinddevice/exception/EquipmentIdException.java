package cn.finlab.blinddevice.exception;

/**
 * 设备id不在equipment表中抛出此异常
 * @author zsw
 * @date 2019/3/17 13:56
 */
public class EquipmentIdException extends Exception{

    public EquipmentIdException() {
    }

    public EquipmentIdException(String message) {
        super(message);
    }
}
