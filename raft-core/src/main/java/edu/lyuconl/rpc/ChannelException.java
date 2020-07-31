package edu.lyuconl.rpc;

/**
 *
 * @author lyuconl
 * @date 2020年7月31日10点03分
 */
public class ChannelException extends RuntimeException {

    public ChannelException(Throwable cause) {
        super(cause);
    }

    public ChannelException(String message, Throwable cause) {
        super(message, cause);
    }
}
