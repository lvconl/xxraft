package edu.lyuconl.log;

/**
 * 日志相关异常
 *
 * @date 2020年7月15日11点04分
 * @author lyuconl
 */
public class LogException extends RuntimeException {

    public LogException() {}

    public LogException(String message) {
        super(message);
    }

    public LogException(Throwable cause) {
        super(cause);
    }

    public LogException(String message, Throwable cause) {
        super(message, cause);
    }
}
