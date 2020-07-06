package edu.lyuconl.rpc.message;

/**
 * 日志追加条目响应
 *
 * @date 2020年7月6日10点59分
 * @author 吕从雷
 */
public class AppendEntiresResult {
    private final int term;
    private final boolean success;

    public AppendEntiresResult(int term, boolean success) {
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
        return "AppendEntiresResult{" +
                "term=" + term +
                ", success=" + success +
                '}';
    }
}
