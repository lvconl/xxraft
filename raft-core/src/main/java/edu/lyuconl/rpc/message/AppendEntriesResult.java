package edu.lyuconl.rpc.message;

import java.io.Serializable;

/**
 * 日志追加条目响应
 *
 * @date 2020年7月6日10点59分
 * @author 吕从雷
 */
public class AppendEntriesResult implements Serializable {
    private final int term;
    private final boolean success;

//    public AppendEntriesResult(String rpcMessageId, int term, boolean success) {
//        this.rpcMessageId = rpcMessageId;
//        this.term = term;
//        this.success = success;
//    }

    public AppendEntriesResult(int term, boolean success) {
        this.term = term;
        this.success = success;
    }

    public int getTerm() {
        return term;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "AppendEntriesResult{" +
                "term=" + term +
                ", success=" + success +
                '}';
    }
}
