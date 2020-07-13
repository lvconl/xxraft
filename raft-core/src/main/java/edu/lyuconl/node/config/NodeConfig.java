package edu.lyuconl.node.config;

/**
 * 静态配置信息
 *
 * @date 2020年6月26日20点18分
 * @author 吕从雷
 */
public class NodeConfig {
    private int minElectionTimeout = 3000;
    private int maxElectionTimeout = 4000;
    private int logReplicationDelay = 0;
    private int logReplicationInterval = 1000;
    private int logReplicationReadTimeout = 900;

}
