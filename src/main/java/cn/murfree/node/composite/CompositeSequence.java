package cn.murfree.node.composite;

import cn.murfree.contents.AbortType;
import cn.murfree.contents.NodeStatus;
import cn.murfree.node.base.INode;

import java.util.List;

import static cn.murfree.contents.NodeStatus.*;
/**
 * 顺序节点，所有子节点按顺序执行，当有一个返回失败，则中止并返回失败
 *
 * @author lujun
 * @date 2022/7/22
 */
public class CompositeSequence extends ICompositeNode {

    public CompositeSequence() {
        super();
    }

    public CompositeSequence(List<INode> children) {
        super(children);
    }

    public CompositeSequence(List<INode> children, AbortType abortType) {
        super(children, abortType);
    }


    @Override
    public void onStart(){
        super.onStart();
        this.setIndex(0);
    }

    @Override
    public void onConditionalAbort(int childIndex) {
        this.setIndex(childIndex);
        this.setStatus(Inactive);
    }

    @Override
    public boolean canExecute() {
        return this.getIndex() < this.children.size() && this.getStatus() != Failure;
    }

    @Override
    public void onChildExecuted(NodeStatus childStatus, int index) {
        switch (childStatus){
            case Inactive:
                break;
            case Running:
                this.setStatus((Running));
                break;
            case Success:
                this.setIndex(this.getIndex()+1);
                if(this.getIndex() >= this.children.size()){
                    this.setStatus((Success));
                }else {
                    this.setStatus((Running));
                }
                break;
            case Failure:
                this.setStatus((Failure));
                break;
            default:
                break;
        }

    }
}