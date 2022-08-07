package cn.murfree.node.composite;


import cn.murfree.contents.AbortType;
import cn.murfree.contents.NodeStatus;
import cn.murfree.node.base.INode;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

import static cn.murfree.contents.NodeStatus.*;


/**
 * 选择随机节点，所有子节点随机执行，当有一个返回成功则中止返回成功，全部节点返回失败，则该节点返回失败
 *
 * @author lujun
 * @date 2022/7/22
 */
public class CompositeRandomSelevtor extends ICompositeNode {

    //真正执行的顺序
    private final Stack<Integer> executionOrder = new Stack<Integer>();

    public CompositeRandomSelevtor() {
        super();
    }
    public CompositeRandomSelevtor(List<INode> children) {
        super(children);
    }

    public CompositeRandomSelevtor(List<INode> children, AbortType abortType) {
        super(children, abortType);
    }

    @Override
    public boolean canExecute() {
        return this.executionOrder.size() > 0 && this.getStatus() != Success;
    }

    @Override
    public void onChildExecuted(NodeStatus childStatus, int index) {
        switch (childStatus){
            case Running:
                this.setStatus(Running);
                break;
            case Success:
                this.setStatus(Success);
                break;
            case Failure:
                this.executionOrder.pop();
                if(this.executionOrder.isEmpty()){
                    this.setStatus(Failure);
                }else {
                    this.setStatus(Running);
                }
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