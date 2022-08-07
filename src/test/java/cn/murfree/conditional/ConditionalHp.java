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
public class ConditionalHp extends IConditionalNode<TestContext> {


    @Override
    public NodeStatus onUpdate(TestContext cxt) {
        System.out.println("cxt.hp : "+cxt.getHp()+", ConditionalHp 判断结果 "+ (cxt.getHp() <= 80));
        if(cxt.getHp() <= 80){
            return NodeStatus.Success;
        }
        return NodeStatus.Failure;
    }
}