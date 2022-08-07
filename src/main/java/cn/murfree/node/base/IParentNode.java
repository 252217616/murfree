package cn.murfree.node.base;

import cn.murfree.contents.NodeStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础父节点
 *
 * @author lujun
 * @date 2022/7/22
 */
public abstract class IParentNode extends INode {

    protected List<INode> children;

    private int index = 0;

    public IParentNode(List<INode> children){
        this.children = children;
    }

    public IParentNode(){
        this.children = new ArrayList<INode>();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    /**
     * 该节点是否能够执行
     */
    public abstract boolean canExecute();

    /**
     * 子节点执行对父节点的影响
     */
    public abstract void onChildExecuted(NodeStatus childStatus, int index);

    //条件中止
    public void onConditionalAbort(int childIndex){
    }

    /**
     * 能否运行的并行节点
     */
    public boolean canRunParallelChildren(){
        return false;
    }

    /**
     * 并发执行时运行
     */
    public void onChildStarted(){

    }

    public List<INode> getChildren() {
        return children;
    }

    public void setChildren(List<INode> children) {
        this.children = children;
    }

    public void addChild(INode child){
        this.children.add(child);
    }
}