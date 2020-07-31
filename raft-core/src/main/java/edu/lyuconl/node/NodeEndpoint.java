package edu.lyuconl.node;

import com.google.common.base.Preconditions;
import edu.lyuconl.rpc.Address;

/**
 * 连接节点的基本信息，包括 IP地址以及端口
 *
 * @date 2020年6月26日20点30分
 * @author 吕从雷
 */
public class NodeEndpoint {
    private final NodeId id;
    private final Address address;

    public NodeEndpoint(NodeId id, Address address) {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(address);
        this.id = id;
        this.address = address;
    }

    public NodeEndpoint(String id, String host, int port) {
        this(new NodeId(id), new Address(host, port));
    }

    public Address getAddress() {
        return address;
    }

    public NodeId getId() {
        return id;
    }
}
