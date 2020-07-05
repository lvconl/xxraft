package edu.lyuconl.node.role;

/**
 * 系统角色抽象父类
 *
 * @date 2020年7月5日16点48分
 * @author 吕从雷
 */
public abstract class AbstractNodeRole {
    private final RoleName name;
    protected final int term;

    AbstractNodeRole(RoleName roleName, int term) {
        this.name = roleName;
        this.term = term;
    }

    public RoleName getName() {
        return name;
    }

    /**
     * 取消超时或定时任务
     */
    public abstract void cancelTimeoutOrTask();

    public int getTerm() {
        return term;
    }
}
