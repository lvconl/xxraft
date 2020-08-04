package edu.lyuconl.message;

/**
 *
 * GET 请求响应
 *
 * @date 2020年8月4日15点53分
 * @author lyuconl
 */
public class GetCommandResponse {

    private final boolean found;
    private final byte[] value;

    public GetCommandResponse(byte[] value) {
        this(value != null, value);
    }

    public GetCommandResponse(boolean found, byte[] value) {
        this.found = found;
        this.value = value;
    }
}
