package edu.lyuconl.proto;

import edu.lyuconl.Protos;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @date 2020年7月29日18点02分
 * @author lyuconl
 */
public class ProtosTest {

    @Test
    public void testProtosBuild() throws IOException {
        Protos.RequestVoteRpc message = Protos.RequestVoteRpc.newBuilder()
                .setTerm(3)
                .setCandidateId("A")
                .setLastLogIndex(10)
                .setLastLogTerm(3)
                .build();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        message.writeTo(buffer);

        Protos.RequestVoteRpc msg = Protos.RequestVoteRpc.parseFrom(buffer.toByteArray());

        System.out.println(msg.getTerm());
        System.out.println(msg.getCandidateId());
        System.out.println(msg.getLastLogIndex());
        System.out.println(msg.getLastLogTerm());
    }
}
