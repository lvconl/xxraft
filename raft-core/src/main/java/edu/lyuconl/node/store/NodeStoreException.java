package edu.lyuconl.node.store;

/**
 * 持久化异常处理类
 *
 * @date 2020年7月11日17点46分
 * @author lyuconl
 */
public class NodeStoreException extends RuntimeException {
    public NodeStoreException(Throwable cause) {
        super(cause);
    }

    public NodeStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
