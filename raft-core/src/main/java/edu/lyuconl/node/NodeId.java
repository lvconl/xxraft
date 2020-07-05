package edu.lyuconl.node;

import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.Objects;

/**
 * 存储服务器的ID工具类
 *
 * @date 2020年6月26日，19点58分
 * @author 吕从雷
 */
public class NodeId implements Serializable {
    private final String value;

    public NodeId (String value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }

    public static NodeId of (String value) {
        return new NodeId(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
        if (!(o instanceof  NodeId)) {
            return false;
        }
        NodeId nodeId = (NodeId) o;
        return Objects.equals(value, nodeId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String getValue() {
        return value;
    }
}
