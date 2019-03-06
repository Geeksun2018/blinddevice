package cn.finlab.blinddevice.exception;

/**
 * 天气获取异常
 * @author zsw
 */
public class GetWeatherException extends Exception {
    public GetWeatherException() {
    }

    public GetWeatherException(String message) {
        super(message);
    }
}

