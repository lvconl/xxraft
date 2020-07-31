package edu.lyuconl.rpc;

import edu.lyuconl.rpc.message.AppendEntriesResult;
import edu.lyuconl.rpc.message.AppendEntriesRpc;
import edu.lyuconl.rpc.message.RequestVoteResult;
import edu.lyuconl.rpc.message.RequestVoteRpc;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Nonnull;

/**
 * 节点间通道
 *
 * @date 2020年7月20日14点22分
 * @author lyuconl
 */
public interface Channel {

    /**
     * write request vote rpc
     *
     * @param rpc rpc
     */
    void writeRequestVoteRpc(@Nonnull RequestVoteRpc rpc);

    /**
     * write request vote result
     *
     * @param result result
     */
    void writeRequestVoteResult(@Nonnull RequestVoteResult result);

    /**
     * write append entries rpc
     *
     * @param rpc rpc
     */
    void writeAppendEntriesRpc(@NonNull AppendEntriesRpc rpc);

    /**
     * write append entries result
     *
     * @param result result
     */
    void writeAppendEntriesResult(@Nonnull AppendEntriesResult result);

    /**
     * close channel
     *
     */
    void close();
}
