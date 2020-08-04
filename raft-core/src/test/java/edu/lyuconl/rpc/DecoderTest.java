package edu.lyuconl.rpc;

import edu.lyuconl.Protos;
import edu.lyuconl.node.NodeId;
import edu.lyuconl.rpc.message.MessageConstants;
import edu.lyuconl.rpc.message.RequestVoteRpc;
import edu.lyuconl.rpc.nio.Decoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * decoder test
 *
 * @date 2020年8月4日09点37分
 * @author lyuconl
 */
public class DecoderTest {

    @Test
    public void testNodeId() throws Exception {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(MessageConstants.MSG_TYPE_NODE_ID);
        buffer.writeInt(1);
        buffer.writeByte((byte) 'A');
        Decoder decoder = new Decoder();
        List<Object> out = new ArrayList<>();
        decoder.decode(null, buffer, out);
        Assert.assertEquals(NodeId.of("A"), out.get(0));
    }

    @Test
    public void testRequestVoteRpc() throws Exception {
        Protos.RequestVoteRpc rpc = Protos.RequestVoteRpc.newBuilder()
                .setLastLogIndex(2)
                .setLastLogTerm(1)
                .setTerm(2)
                .setCandidateId("A")
                .build();
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(MessageConstants.MSG_TYPE_REQUEST_VOTE_RPC);
        byte[] rpcBytes = rpc.toByteArray();
        buffer.writeInt(rpcBytes.length);
        buffer.writeBytes(rpcBytes);
        Decoder decoder = new Decoder();
        List<Object> out = new ArrayList<>();
        decoder.decode(null, buffer, out);
        RequestVoteRpc decodeRpc = (RequestVoteRpc) out.get(0);
        Assert.assertEquals(rpc.getLastLogIndex(), decodeRpc.getLastLogIndex());
        Assert.assertEquals(rpc.getLastLogTerm(), decodeRpc.getLastLogTerm());
        Assert.assertEquals(rpc.getTerm(), decodeRpc.getTerm());
        Assert.assertEquals(NodeId.of(rpc.getCandidateId()), decodeRpc.getCandidateId());
    }
}
