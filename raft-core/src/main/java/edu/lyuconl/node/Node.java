package edu.lyuconl.node;

/**
 * 核心组件Node接口
 *
 * @date 2020年7月12日10点52分
 * @author lyuconl
 */
public interface Node {
    /**
     * 启动方法
     */
    void start();

    /**
     * 停止方法
     */
    void stop() throws Exception;
}
