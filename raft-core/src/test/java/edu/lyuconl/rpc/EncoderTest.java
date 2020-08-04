package edu.lyuconl.rpc;

import edu.lyuconl.Protos;
import edu.lyuconl.node.NodeId;
import edu.lyuconl.rpc.message.MessageConstants;
import edu.lyuconl.rpc.message.RequestVoteRpc;
import edu.lyuconl.rpc.nio.Encoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 *
 * encoder test
 *
 * @date 2020年8月3日17点03分
 * @author lyuconl
 */
public class EncoderTest {

    @Test
    public void testNodeId() throws Exception {
        Encoder encoder = new Encoder();
        ByteBuf buffer = Unpooled.buffer();
        encoder.encode(null, NodeId.of("A"), buffer);
        Assert.assertEquals(MessageConstants.MSG_TYPE_NODE_ID, buffer.readInt());
        Assert.assertEquals(1, buffer.readInt());
        Assert.assertEquals((byte) 'A', buffer.readByte());
    }

    @Test
    public void testRequestVoteRpc() throws Exception {
        Encoder encoder = new Encoder();
        ByteBuf buffer = Unpooled.buffer();
        RequestVoteRpc rpc = new RequestVoteRpc();
        rpc.setLastLogIndex(2);
        rpc.setLastLogTerm(1);
        rpc.setTerm(2);
        rpc.setCandidateId(NodeId.of("A"));
        encoder.encode(null, rpc, buffer);
        Assert.assertEquals(MessageConstants.MSG_TYPE_REQUEST_VOTE_RPC, buffer.readInt());
        buffer.readInt();
        Protos.RequestVoteRpc decodeRpc = Protos.RequestVoteRpc.parseFrom(new ByteBufInputStream(buffer));
        Assert.assertEquals(rpc.getLastLogIndex(), decodeRpc.getLastLogIndex());
        Assert.assertEquals(rpc.getLastLogTerm(), decodeRpc.getLastLogTerm());
        Assert.assertEquals(rpc.getTerm(), decodeRpc.getTerm());
        Assert.assertEquals(rpc.getCandidateId().getValue(), decodeRpc.getCandidateId());
    }
}
