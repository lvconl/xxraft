package edu.lyuconl.node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 集群成员类
 *
 * @date 2020年6月27日21点38分
 * @author 吕从雷
 */
public class NodeGroup {

    private static final Logger logger = LoggerFactory.getLogger(NodeGroup.class);

    private NodeId selfId;
    private Map<NodeId, GroupMember> memberMap;

    NodeGroup(NodeEndpoint nodeEndpoint) {
        this(Collections.singleton(nodeEndpoint), nodeEndpoint.getId());
    }

    NodeGroup(Collection<NodeEndpoint> endpoints, NodeId selfId) {
        this.memberMap = buildMemberMap(endpoints);
        this.selfId = selfId;
    }

    private Map<NodeId, GroupMember> buildMemberMap(Collection<NodeEndpoint> endpoints) {
        Map<NodeId, GroupMember> map = new HashMap<>(16);

        for (NodeEndpoint nodeEndpoint : endpoints) {
            map.put(nodeEndpoint.getId(), new GroupMember(nodeEndpoint));
        }

        if (map.isEmpty()) {
            throw new IllegalArgumentException("endpoints is empty...");
        }
        return map;
    }

    GroupMember findMember(NodeId id) {
        GroupMember member = getMember(id);
        if (member == null) {
            throw new IllegalArgumentException("no such node " + id);
        }
        return member;
    }

    GroupMember getMember(NodeId id) { return memberMap.get(id); }

    /**
     * 列出日志复制的peers节点，除自己以外的节点
     * @return peers节点
     */
    Collection<GroupMember> listReplicationTarget() {
        return memberMap.values().stream().filter(
                m -> !m.idEquals(selfId)).collect(Collectors.toList());
    }

    /**
     * 列出除当前节点以外的其它节点
     *
     * @return 除当前节点以外的其它节点
     */
    Set<NodeEndpoint> listEndpointExceptSelf() {
        Set<NodeEndpoint> endpoints = new HashSet<>();
        for (GroupMember member : memberMap.values()) {
            // 判断是不是当前节点
            if (!member.idEquals(selfId)) {
                endpoints.add(member.getEndpoint());
            }
        }
        return endpoints;
    }

    int getCount() {
        return (int) memberMap.values().stream().filter(GroupMember::isMajor).count();
    }

    boolean isStandalone() {
        return memberMap.size() == 1 && memberMap.containsKey(selfId);
    }

    int getMatchIndexOfMajor() {
        List<NodeMatchIndex> matchIndices = new ArrayList<>();
        for (GroupMember member : memberMap.values()) {
            if (member.isMajor() && !member.idEquals(selfId)) {
                matchIndices.add(new NodeMatchIndex(member.getEndpoint().getId(), member.getMatchIndex()));
            }
        }
        int count = matchIndices.size();
        if (count == 0) {
            throw new IllegalStateException("standalone or no major node");
        }
        Collections.sort(matchIndices);
        logger.debug("match indices {}", matchIndices);
        return matchIndices.get(count / 2).getMatchIndex();
    }

    private static class NodeMatchIndex implements Comparable<NodeMatchIndex> {

        private final NodeId nodeId;
        private final int matchIndex;

        NodeMatchIndex(NodeId nodeId, int matchIndex) {
            this.nodeId = nodeId;
            this.matchIndex = matchIndex;
        }

        public int getMatchIndex() {
            return matchIndex;
        }

        @Override
        public int compareTo(@Nonnull NodeMatchIndex o) {
            return -Integer.compare(o.matchIndex, this.matchIndex);
        }

        @Override
        public String toString() {
            return "NodeMatchIndex{" +
                    "nodeId=" + nodeId +
                    ", matchIndex=" + matchIndex +
                    '}';
        }
    }
}
