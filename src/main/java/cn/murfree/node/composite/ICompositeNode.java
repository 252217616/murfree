package cn.murfree.node.composite;

import cn.murfree.contents.AbortType;
import cn.murfree.node.base.INode;
import cn.murfree.node.base.IParentNode;

import java.util.List;


/**
 * 基础组合节点
 *
 * @author lujun
 * @date 2022/7/22
 */
public abstract class ICompositeNode extends IParentNode {
    /**
     * 中断类型
     */
    private AbortType abortType;

    public AbortType getAbortType() {
        return abortType;
    }

    public void setAbortType(AbortType abortType) {
        this.abortType = abortType;
    }

    public ICompositeNode() {
        super();
        this.abortType = AbortType.None;
    }
    public ICompositeNode(List<INode> children) {
        super(children);
        this.abortType = AbortType.None;
    }

    public ICompositeNode(List<INode> children, AbortType abortType) {
        super(children);
        this.abortType = abortType == null ? AbortType.None : abortType;
    }

    /**
     * 节点中断时重置状态
     */
    @Override
    public abstract void onConditionalAbort(int childIndex);
}