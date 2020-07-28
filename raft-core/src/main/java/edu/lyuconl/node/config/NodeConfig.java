package edu.lyuconl.node.config;

import edu.lyuconl.log.Log;

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
    private int maxReplicationEntries = Log.ALL_ENTRIES;

    public int getMinElectionTimeout() {
        return minElectionTimeout;
    }

    public void setMinElectionTimeout(int minElectionTimeout) {
        this.minElectionTimeout = minElectionTimeout;
    }

    public int getMaxElectionTimeout() {
        return maxElectionTimeout;
    }

    public void setMaxElectionTimeout(int maxElectionTimeout) {
        this.maxElectionTimeout = maxElectionTimeout;
    }

    public int getLogReplicationDelay() {
        return logReplicationDelay;
    }

    public void setLogReplicationDelay(int logReplicationDelay) {
        this.logReplicationDelay = logReplicationDelay;
    }

    public int getLogReplicationInterval() {
        return logReplicationInterval;
    }

    public void setLogReplicationInterval(int logReplicationInterval) {
        this.logReplicationInterval = logReplicationInterval;
    }

    public int getLogReplicationReadTimeout() {
        return logReplicationReadTimeout;
    }

    public void setLogReplicationReadTimeout(int logReplicationReadTimeout) {
        this.logReplicationReadTimeout = logReplicationReadTimeout;
    }

    public int getMaxReplicationEntries() {
        return maxReplicationEntries;
    }

    public void setMaxReplicationEntries(int maxReplicationEntries) {
        this.maxReplicationEntries = maxReplicationEntries;
    }
}
