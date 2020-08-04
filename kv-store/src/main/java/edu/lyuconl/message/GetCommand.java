package edu.lyuconl.message;

/**
 * GET请求
 *
 * @date 2020年8月4日15点51分
 * @author lyuconl
 */
public class GetCommand {

    private final String key;

    public GetCommand(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "GetCommand{" +
                "key='" + key + '\'' +
                '}';
    }
}
