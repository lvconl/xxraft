package edu.lyuconl.node;

import com.google.common.base.Preconditions;

/**
 * 服务器地址类
 *
 * @date 2020年6月26日20点27分
 * @author 吕从雷
 */
public class Address {
    private final String host;
    private final int port;

    public Address(String host, int port) {
        Preconditions.checkNotNull(host);
        this.host = host;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }
}
