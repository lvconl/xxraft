package edu.lyuconl.log.snapshot;

import edu.lyuconl.node.NodeEndpoint;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.util.Set;

/**
 * 快照接口
 *
 * @date 2020年7月17日17点32分
 * @author lyuconl
 */
public interface Snapshot {
    int getLastIncludedIndex();

    int getLastIncludedTerm();

    @Nonnull
    Set<NodeEndpoint> getLastConfig();

    long getDataSize();

    @Nonnull
    SnapshotChunk readData(int offset, int length);

    @Nonnull
    InputStream getDataStream();

    void close();
}
