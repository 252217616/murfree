package cn.murfree.dto;

import cn.murfree.contents.NodeStatus;


/**
 * 条件重评估
 *
 * @author lujun
 * @date 2022/7/22
 */

public class ConditionalReevaluate {
    /**
     * 当前节点的索引
     */
    private int index;
    /**
     * 节点状态
     */
    private NodeStatus status;
    /**
     * 父组合节点索引
     */
    private int compositeIndex;

    public ConditionalReevaluate(int index, NodeStatus status, int compositeIndex) {
        this.index = index;
        this.status = status;
        this.compositeIndex = compositeIndex;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public NodeStatus getStatus() {
        return status;
    }

    public void setStatus(NodeStatus status) {
        this.status = status;
    }

    public int getCompositeIndex() {
        return compositeIndex;
    }

    public void setCompositeIndex(int compositeIndex) {
        this.compositeIndex = compositeIndex;
    }
}