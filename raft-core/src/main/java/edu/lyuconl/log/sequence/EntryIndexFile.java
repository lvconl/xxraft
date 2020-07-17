package edu.lyuconl.log.sequence;

import edu.lyuconl.support.RandomAccessFileAdapter;
import edu.lyuconl.support.SeekableFile;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * entry索引文件
 *
 * @date 2020年7月16日10点05分
 * @author lyuconl
 */
public class EntryIndexFile implements Iterable<EntryIndexItem> {

    private static final long OFFSET_MAX_ENTRY_INDEX = Integer.BYTES;
    private static final int LENGTH_ENTRY_INDEX_ITEM = 16;

    private final SeekableFile seekableFile;
    private int entryIndexCount;
    private int minEntryIndex;
    private int maxEntryIndex;
    private Map<Integer, EntryIndexItem> entryIndexMap = new HashMap<>();

    public EntryIndexFile(File file) throws IOException {
        this(new RandomAccessFileAdapter(file));
    }

    public EntryIndexFile(SeekableFile seekableFile) throws IOException {
        this.seekableFile = seekableFile;
        load();
    }

    /**
     * 加载所有日志元信息
     *
     * @throws IOException IO异常
     */
    private void load() throws IOException {
        if (seekableFile.size() == 0L) {
            entryIndexCount = 0;
            return;
        }
        minEntryIndex = seekableFile.readInt();
        maxEntryIndex = seekableFile.readInt();
        updateEntryIndexCount();
        // 逐条加载
        long offset;
        int kind;
        int term;
        for (int i = minEntryIndex; i <= maxEntryIndex; i++) {
            offset = seekableFile.readLong();
            kind = seekableFile.readInt();
            term = seekableFile.readInt();
            entryIndexMap.put(i, new EntryIndexItem(i, offset, kind, term));
        }
    }

    /**
     * 追加日志条目元信息
     *
     * @param index 索引
     * @param offset entry file内偏移
     * @param kind entry类型
     * @param term entry term
     * @throws IOException IO异常
     */
    public void appendEntryIndex(int index, long offset, int kind, int term) throws IOException {
        if (seekableFile.size() == 0L) {
            // 文件为空，写入seekableFile
            seekableFile.writeInt(index);
            minEntryIndex = index;
        } else {
            // 索引检查
            if (index != maxEntryIndex + 1) {
                throw new IllegalArgumentException("index must be " + (maxEntryIndex + 1) + ", but was " + index);
            }
            // 跳过minEntryIndex
            seekableFile.seek(OFFSET_MAX_ENTRY_INDEX);
        }
        // 写入maxEntryIndex
        seekableFile.writeInt(index);
        maxEntryIndex = index;
        updateEntryIndexCount();
        // 移动到文件最后
        seekableFile.seek(getOffsetOfEntryIndexItem(index));
        seekableFile.writeLong(offset);
        seekableFile.writeInt(kind);
        seekableFile.writeInt(term);
        entryIndexMap.put(index, new EntryIndexItem(index, offset, kind, term));
    }

    /**
     * 清楚全部
     *
     * @throws IOException IO异常
     */
    public void clear() throws IOException {
        seekableFile.truncate(0L);
        entryIndexCount = 0;
        entryIndexMap.clear();
    }

    public void removeAfter(int newMaxEntryIndex) throws IOException {
        // 合法性校验
        if (isEmpty() || newMaxEntryIndex >= maxEntryIndex) {
            return;
        }
        // 判断新的maxEntryIndex是否比minEntryIndex小，如果是全部删除
        if (newMaxEntryIndex < minEntryIndex) {
            clear();
            return;
        }
        // 修改maxEntryIndex
        seekableFile.seek(OFFSET_MAX_ENTRY_INDEX);
        seekableFile.writeInt(newMaxEntryIndex);
        // 截断
        seekableFile.truncate(getOffsetOfEntryIndexItem(newMaxEntryIndex));
        for (int i = newMaxEntryIndex + 1; i <= maxEntryIndex; i++) {
            entryIndexMap.remove(i);
        }
        maxEntryIndex = newMaxEntryIndex;
        entryIndexCount = newMaxEntryIndex - minEntryIndex + 1;
    }

    /**
     * 更新日志条目数量
     */
    private void updateEntryIndexCount() {
        entryIndexCount = maxEntryIndex - minEntryIndex + 1;
    }

    /**
     * 获取指定索引日志的偏移
     *
     * @param index 指定索引
     * @return 偏移量
     */
    private long getOffsetOfEntryIndexItem(int index) {
        return (index - minEntryIndex) * LENGTH_ENTRY_INDEX_ITEM + Integer.BYTES * 2;
    }

    public boolean isEmpty() {
        return entryIndexCount == 0;
    }

    @Override
    public Iterator<EntryIndexItem> iterator() {
        // 索引文件是否为空
        if (isEmpty()) {
            return Collections.emptyIterator();
        }
        return new EntryIndexIterator(entryIndexCount, minEntryIndex);
    }

    private class EntryIndexIterator implements Iterator<EntryIndexItem> {

        private final int entryIndexCount;
        private int currentEntryIndex;

        EntryIndexIterator(int entryIndexCount, int currentEntryIndex) {
            this.entryIndexCount = entryIndexCount;
            this.currentEntryIndex = currentEntryIndex;
        }

        @Override
        public boolean hasNext() {
            checkModification();
            return currentEntryIndex <= maxEntryIndex;
        }

        @Override
        public EntryIndexItem next() {
            checkModification();
            return entryIndexMap.get(currentEntryIndex++);
        }

        private void checkModification() {
            if (this.entryIndexCount != EntryIndexFile.this.entryIndexCount) {
                throw new IllegalStateException("entry index count changed...");
            }
        }
    }


    public int getEntryIndexCount() {
        return entryIndexCount;
    }

    public void setEntryIndexCount(int entryIndexCount) {
        this.entryIndexCount = entryIndexCount;
    }

    public int getMinEntryIndex() {
        return minEntryIndex;
    }

    public void setMinEntryIndex(int minEntryIndex) {
        this.minEntryIndex = minEntryIndex;
    }

    public int getMaxEntryIndex() {
        return maxEntryIndex;
    }

    public void setMaxEntryIndex(int maxEntryIndex) {
        this.maxEntryIndex = maxEntryIndex;
    }

    public Map<Integer, EntryIndexItem> getEntryIndexMap() {
        return entryIndexMap;
    }

    public void setEntryIndexMap(Map<Integer, EntryIndexItem> entryIndexMap) {
        this.entryIndexMap = entryIndexMap;
    }

    public long getOffset(int entryIndex) {
        return get(entryIndex).getOffset();
    }

    @Nonnull
    public EntryIndexItem get(int entryIndex) {
        checkEmpty();
        if (entryIndex < minEntryIndex || entryIndex > maxEntryIndex) {
            throw new IllegalArgumentException("entryIndex < minEntryIndex or entryIndex > maxEntryIndex");
        }
        return entryIndexMap.get(entryIndex);
    }

    private void checkEmpty() {
        if (isEmpty()) {
            throw new IllegalStateException("no entry index");
        }
    }
}
