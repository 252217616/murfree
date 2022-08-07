package cn.murfree.node.composite;


import cn.murfree.contents.AbortType;
import cn.murfree.contents.NodeStatus;
import cn.murfree.node.base.INode;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

import static cn.murfree.contents.NodeStatus.*;

/**
 * 顺序随机节点，所有子节点随机执行，当有一个返回失败，则中止并返回失败
 *
 * @author lujun
 * @date 2022/7/22
 */
public class CompositeRandomSequence extends ICompositeNode {
    //真正执行的顺序
    private final Stack<Integer> executionOrder = new Stack<Integer>();


    public CompositeRandomSequence() {
        super();
    }

    public CompositeRandomSequence(List<INode> children) {
        super(children);
    }

    public CompositeRandomSequence(List<INode> children, AbortType abortType) {
        super(children, abortType);
    }

    @Override
    public boolean canExecute() {
        return this.executionOrder.size() > 0 && this.getStatus() != Failure;
    }

    @Override
    public void onChildExecuted(NodeStatus childStatus, int index) {
        switch (childStatus){
            case Running:
                this.setStatus(Running);
                break;
            case Success:
                this.executionOrder.pop();
                if(this.executionOrder.isEmpty()){
                    this.setStatus(Success);
                }else {
                    this.setStatus(Running);
                }
                break;
            case Failure:
                this.setStatus(Success);
                break;
            default:
                break;
        }
    }

    @Override
    public int getIndex(){
        return this.executionOrder.get(this.executionOrder.size() - 1);
    }

    @Override
    public void onStart(){
        super.onStart();
        for (int i = 0; i < this.children.size(); i++) {
            executionOrder.add(i);
        }
        this.shuffle();
    }

    private void shuffle() {
        Collections.shuffle(this.executionOrder);
    }

    @Override
    public void  onConditionalAbort(int childIndex){
        this.shuffle();
        this.setStatus(Inactive);
    }
}