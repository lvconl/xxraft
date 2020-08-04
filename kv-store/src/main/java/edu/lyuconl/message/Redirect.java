package edu.lyuconl.message;

import edu.lyuconl.node.NodeId;

/**
 *
 * 重定向请求
 *
 * @date 2020年8月4日16点05分
 * @author lyuconl
 */
public class Redirect {

    private final String leaderId;

    public Redirect(String leaderId) {
        this.leaderId = leaderId;
    }

    public Redirect(NodeId leaderId) {
        this(leaderId != null ? leaderId.getValue() : null);
    }

    public String getLeaderId() {
        return leaderId;
    }

    @Override
    public String toString() {
        return "Redirect{" +
                "leaderId='" + leaderId + '\'' +
                '}';
    }
}
