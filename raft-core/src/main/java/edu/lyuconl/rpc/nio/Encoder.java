package edu.lyuconl.rpc.nio;

import com.google.protobuf.MessageLite;
import edu.lyuconl.Protos;
import edu.lyuconl.node.Node;
import edu.lyuconl.node.NodeId;
import edu.lyuconl.rpc.message.MessageConstants;
import edu.lyuconl.rpc.message.RequestVoteRpc;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 编码器
 *
 * @author lyuconl
 * @date 2020年7月31日14点50分
 */
public class Encoder extends MessageToByteEncoder<Object> {
    @Override
    public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        // 判断消息类型
        if (msg instanceof NodeId) {
            this.writeMessage(out, MessageConstants.MSG_TYPE_NODE_ID, ((NodeId) msg).getValue().getBytes() );
        } else if (msg instanceof RequestVoteRpc) {
            RequestVoteRpc rpc = (RequestVoteRpc) msg;
            Protos.RequestVoteRpc protoRpc = Protos.RequestVoteRpc.newBuilder()
                    .setTerm(rpc.getTerm())
                    .setCandidateId(rpc.getCandidateId().getValue())
                    .setLastLogIndex(rpc.getLastLogIndex())
                    .setLastLogTerm(rpc.getLastLogTerm())
                    .build();
            this.writeMessage(out,
                    MessageConstants.MSG_TYPE_REQUEST_VOTE_RPC, protoRpc);
        }
    }

    private void writeMessage(ByteBuf out, int messageType, MessageLite message) throws IOException {
        // 先写入消息类型
        out.writeInt(messageType);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        message.writeTo(byteOutput);
        this.writeBytes(out, byteOutput.toByteArray());
    }

    private void writeMessage(ByteBuf out, int messageType, byte[] bytes) {
        out.writeInt(messageType);
        this.writeBytes(out, bytes);
    }

    private void writeBytes(ByteBuf out, byte[] bytes) {
        // 写入长度
        out.writeInt(bytes.length);
        // 写入负载
        out.writeBytes(bytes);
    }
}
