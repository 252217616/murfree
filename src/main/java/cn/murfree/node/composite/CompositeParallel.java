package cn.murfree.node.composite;

import cn.murfree.contents.AbortType;
import cn.murfree.contents.NodeStatus;
import cn.murfree.node.base.INode;

import java.util.Arrays;
import java.util.List;

/**
 * 并行节点
 * 所有子节点按同时执行，当有一个返回失败，则返回失败，全部成功则成功
 * @author lujun
 * @date 2022/7/22
 */
public class CompositeParallel extends ICompositeNode {

    private NodeStatus[] exectionStatus = new NodeStatus[]{};

    @Override
    public NodeStatus getStatus() {
        boolean childrenComplete = true;
        for (NodeStatus curStatus : this.exectionStatus) {
            if (curStatus == NodeStatus.Running) {
                childrenComplete = false;
            } else if (curStatus == NodeStatus.Failure) {
                return NodeStatus.Failure;
            }
        }
        return childrenComplete? NodeStatus.Success: NodeStatus.Running;
    }

    @Override
    public void setStatus(NodeStatus status) {

    }


    public CompositeParallel() {
        super();
    }

    public CompositeParallel(List<INode> children) {
        super(children);
    }

    public CompositeParallel(List<INode> children, AbortType abortType) {
        super(children, abortType);
    }

    @Override
    public boolean canExecute() {
        return this.getIndex() < this.children.size();
    }

    @Override
    public void onChildExecuted(NodeStatus childStatus, int index) {
        this.exectionStatus[index] = childStatus;
    }

    @Override
    public void onStart(){
        super.onStart();
        this.exectionStatus = new NodeStatus[this.children.size()];
        this.setIndex(0);
        for (int i = 0; i < this.children.size(); i++) {
            this.exectionStatus[i] = NodeStatus.Inactive;
        }
    }

    /**
     * 节点中断时重置状态
     */
    @Override
    public void onConditionalAbort(int childIndex){
        this.setIndex(0);
        Arrays.fill(this.exectionStatus, NodeStatus.Inactive);
    }


    @Override
    public boolean canRunParallelChildren(){
        return true;
    }

    @Override
    public void onChildStarted(){
        this.exectionStatus[this.getIndex()] = NodeStatus.Running;
        this.setIndex(this.getIndex()+1);
    }
}