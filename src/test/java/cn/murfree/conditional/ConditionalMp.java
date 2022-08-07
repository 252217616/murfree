package cn.murfree.conditional;

import cn.murfree.contents.NodeStatus;
import cn.murfree.context.TestContext;
import cn.murfree.node.base.IConditionalNode;

/**
 * ConditionalHp
 *
 * @author lujun
 * @date 2022/7/22
 */
public class ConditionalMp extends IConditionalNode<TestContext> {


    @Override
    public NodeStatus onUpdate(TestContext cxt) {
        System.out.println("cxt.mp : "+cxt.getMp()+", ConditionalMp 判断结果 "+ (cxt.getMp() >= 100));
        if(cxt.getMp() >= 100){
            return NodeStatus.Success;
        }
        return NodeStatus.Failure;
    }
}