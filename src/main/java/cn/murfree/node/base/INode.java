package cn.murfree.node.base;

import cn.murfree.contents.NodeStatus;
import cn.murfree.context.IContext;

/**
 * INode
 * 节点基类
 * @author luJun
 * @date 2022/7/22
 */
public abstract class INode <T extends IContext> {

    private T context;
    /**
     * 节点名称
     */
    private String name;

    /**
     * 节点状态
     */
    private NodeStatus status;

    public NodeStatus getStatus() {
        return status;
    }

    public void setStatus(NodeStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public INode(String name){
        this.name = name;
    }

    protected T getContext() {
        return this.context;
    }

    public void setContext(T context) {
        this.context = context;
    }

    public INode(){
    }

    /**
     * 节点运行前执行
     */
    public void onStart(){
        this.status = NodeStatus.Inactive;
    }


    /**
     * 节点运行时执行
     */
    public NodeStatus onUpdate(T context){
        return NodeStatus.Success;
    }

    /**
     * 节点结束运行时执行
     */
    public void onEnd(){
        this.status = NodeStatus.Inactive;
    }

}
