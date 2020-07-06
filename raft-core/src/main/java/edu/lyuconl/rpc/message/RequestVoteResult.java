package edu.lyuconl.rpc.message;

/**
 * 投票请求结果实体
 *
 * @date 2020年7月6日10点43分
 * @author 吕从雷
 */
public class RequestVoteResult {
    /**
     * 选举term
     */
    private final int term;
    /**
     * 是否投票
     */
    private final boolean voteGranted;

    public RequestVoteResult(int term, boolean voteGranted) {
        this.term = term;
        this.voteGranted = voteGranted;
    }

    public int getTerm() {
        return term;
    }

    public boolean isVoteGranted() {
        return voteGranted;
    }

    @Override
    public String toString() {
        return "RequestVoteResult{" +
                "term=" + term +
                ", voteGranted=" + voteGranted +
                '}';
    }
}
