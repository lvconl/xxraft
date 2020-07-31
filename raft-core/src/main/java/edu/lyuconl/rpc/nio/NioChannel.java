package edu.lyuconl.rpc.nio;

import edu.lyuconl.rpc.Channel;
import edu.lyuconl.rpc.ChannelException;
import edu.lyuconl.rpc.message.AppendEntriesResult;
import edu.lyuconl.rpc.message.AppendEntriesRpc;
import edu.lyuconl.rpc.message.RequestVoteResult;
import edu.lyuconl.rpc.message.RequestVoteRpc;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Nonnull;

/**
 * Nio channel
 *
 * @date 2020年7月31日09点47分
 * @author lyuconl
 */
public class NioChannel implements Channel {

    private final io.netty.channel.Channel nettyChannel;

    NioChannel(io.netty.channel.Channel nettyChannel) {
        this.nettyChannel = nettyChannel;
    }

    @Override
    public void writeRequestVoteRpc(@Nonnull RequestVoteRpc rpc) {
        nettyChannel.writeAndFlush(rpc);
    }

    @Override
    public void writeRequestVoteResult(@Nonnull RequestVoteResult result) {
        nettyChannel.writeAndFlush(result);
    }

    @Override
    public void writeAppendEntriesRpc(@NonNull AppendEntriesRpc rpc) {
        nettyChannel.writeAndFlush(rpc);
    }

    @Override
    public void writeAppendEntriesResult(@Nonnull AppendEntriesResult result) {
        nettyChannel.writeAndFlush(result);
    }

    @Override
    public void close() {
        try {
            nettyChannel.close().sync();
        } catch (InterruptedException ex) {
            throw new ChannelException("failed to close", ex);
        }
    }

    io.netty.channel.Channel getDelegate() {
        return nettyChannel;
    }
}
