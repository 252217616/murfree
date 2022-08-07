package cn.murfree.node.decorator;

import cn.murfree.contents.NodeStatus;
import cn.murfree.node.base.INode;
import cn.murfree.node.base.IParentNode;

import java.util.List;

/**
 * 基础包装节点
 *
 * @author lujun
 * @date 2022/7/22
 */
public abstract class IDecoratorNode extends IParentNode {


    public IDecoratorNode() {
        super();
    }

    public IDecoratorNode(List<INode> children) {
        super(children);
    }

    public NodeStatus decorator(NodeStatus status){
        return this.getStatus();
    }


}