package cn.murfree.node.base;

import cn.murfree.contents.NodeStatus;
import cn.murfree.context.IContext;

/**
 * 基础条件节点
 *
 * @author lujun
 * @date 2022/7/22
 */
public abstract class IConditionalNode<T extends IContext> extends INode<T> {
    @Override
    public abstract NodeStatus onUpdate(T context);
}