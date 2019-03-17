package cn.finlab.blinddevice.exception;

/**
 * 硬件id不存在
 * @author zsw
 * @date 2019/3/17 14:15
 */
public class EquipIdException extends Exception {
    public EquipIdException() {
    }

    public EquipIdException(String message) {
        super(message);
    }
}
