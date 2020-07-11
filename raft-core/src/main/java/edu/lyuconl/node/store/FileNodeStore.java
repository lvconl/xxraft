package edu.lyuconl.node.store;

import com.google.common.io.Files;
import edu.lyuconl.node.NodeId;
import edu.lyuconl.support.RandomAccessFileAdapter;
import edu.lyuconl.support.SeekableFile;

import java.io.File;
import java.io.IOException;

/**
 * 角色状态持久化，文件版本
 *
 * @date 2020年7月11日11点57分
 * @author lyuconl
 */
public class FileNodeStore implements NodeStore {
    public static final String FILE_NAME = "node.bin";
    private static final long OFFSET_TERM = 0;
    private static final long OFFSET_VOTED_FOR = 4;
    private final SeekableFile seekableFile;
    private int term = 0;
    private NodeId votedFor = null;

    /**
     * 从文件读取
     * @param file 欲操作的文件
     */
    public FileNodeStore(File file) {
        try {
            if (!file.exists()) {
                Files.touch(file);
            }
            seekableFile = new RandomAccessFileAdapter(file);
            initializeOrLoad();
        } catch (IOException e) {
            throw new NodeStoreException(e);
        }
    }

    private void initializeOrLoad() throws IOException {
        if (seekableFile.size() == 0) {
            // 初始化
            seekableFile.truncate(8L);
            seekableFile.seek(0);
            seekableFile.writeInt(0);
            seekableFile.writeInt(0);
        } else {
            // 读取term和votedFor
            term = seekableFile.readInt();
            int length = seekableFile.readInt();
            if (length > 0) {
                byte[] bs = new byte[length];
                seekableFile.read(bs);
                votedFor = new NodeId(new String(bs));
            }
        }
    }

    @Override
    public int getTerm() {
        return term;
    }

    @Override
    public void setTerm(int term) {
        try {
            seekableFile.seek(OFFSET_TERM);
            seekableFile.writeInt(term);
        } catch (IOException e) {
            throw new NodeStoreException(e);
        }
        this.term = term;
    }

    @Override
    public NodeId getVotedFor() {
        return votedFor;
    }

    @Override
    public void setVotedFor(NodeId votedFor) {
        try {
            seekableFile.seek(OFFSET_VOTED_FOR);
            if (votedFor == null) {
                seekableFile.writeInt(0);
                seekableFile.truncate(8L);
            } else {
                byte[] bs = votedFor.getValue().getBytes();
                seekableFile.writeInt(bs.length);
                seekableFile.write(bs);
            }
        } catch (IOException e) {
            throw new NodeStoreException(e);
        }
        this.votedFor = votedFor;
    }

    @Override
    public void close() {
        try {
            seekableFile.close();
        } catch (IOException e) {
            throw new NodeStoreException(e);
        }
    }
}
