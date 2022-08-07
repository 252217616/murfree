package cn.murfree.node.decorator;


import cn.murfree.contents.NodeStatus;
import cn.murfree.node.base.INode;

import java.util.List;
import static cn.murfree.contents.NodeStatus.*;

/**
 * 反转装饰节点
 *
 * @author lujun
 * @date 2022/7/22
 */
public class DecoratorInverter extends IDecoratorNode {

    public DecoratorInverter() {
        super();
    }

    public DecoratorInverter(List<INode> children) {
        super(children);
    }

    @Override
    public boolean canExecute() {
        return this.getStatus() == NodeStatus.Inactive || this.getStatus() == NodeStatus.Running;
    }

    @Override
    public void onChildExecuted(NodeStatus childStatus, int index) {
        this.setStatus(childStatus);
    }

    @Override
    public NodeStatus decorator(NodeStatus status) {
        switch (status) {
            case Failure:
                return Success;
            case Success:
                return Failure;
            default:
                return status;
        }
    }
}