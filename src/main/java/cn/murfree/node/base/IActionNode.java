package cn.murfree.node.base;

import cn.murfree.contents.NodeStatus;
import cn.murfree.context.IContext;


/**
 * 基础行动节点
 *
 * @author lujun
 * @date 2022/7/22
 */
public abstract class IActionNode<T extends IContext> extends INode<T> {


    /**
     * 节点运行时执行
     */
    @Override
    public abstract NodeStatus onUpdate(T context);
}