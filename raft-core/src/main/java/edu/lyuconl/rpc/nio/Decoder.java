package edu.lyuconl.rpc.nio;

import edu.lyuconl.Protos;
import edu.lyuconl.node.NodeId;
import edu.lyuconl.rpc.message.MessageConstants;
import edu.lyuconl.rpc.message.RequestVoteRpc;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 编码器
 *
 * @date 2020年7月31日15点35分
 * @author lyuconl
 */
class Decoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 预读8字节, type + len
        int typeAndLen = 8;
        int availableBytes = in.readableBytes();
        if (availableBytes < typeAndLen) {
            return;
        }
        // 记录当前的位置
        in.markReaderIndex();
        int messageType = in.readInt();
        int payloadLength = in.readInt();
        // 消息尚未完全可读
        if (in.readableBytes() < payloadLength) {
            // 回到起始位置
            in.resetReaderIndex();
            return;
        }

        // 消息可读
        byte[] payload = new byte[payloadLength];
        in.readBytes(payload);
        switch (messageType) {
            case MessageConstants.MSG_TYPE_NODE_ID:
                out.add(new NodeId(new String(payload)));
                break;
            case MessageConstants.MSG_TYPE_REQUEST_VOTE_RPC:
                Protos.RequestVoteRpc protoRpc = Protos.RequestVoteRpc.parseFrom(payload);
                RequestVoteRpc rpc = new RequestVoteRpc();
                rpc.setTerm(protoRpc.getTerm());
                rpc.setCandidateId(new NodeId(protoRpc.getCandidateId()));
                rpc.setLastLogIndex(protoRpc.getLastLogIndex());
                rpc.setLastLogTerm(protoRpc.getLastLogTerm());
                out.add(rpc);
                break;
            default:
                throw new IllegalStateException("illegal message type");
        }
    }
}
