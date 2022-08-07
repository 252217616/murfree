package cn.murfree.action;

import cn.murfree.contents.NodeStatus;
import cn.murfree.context.TestContext;
import cn.murfree.node.base.IActionNode;

/**
 * ActionSkill
 *
 * @author lujun
 * @date 2022/8/5
 */
public class ActionAddHp extends IActionNode<TestContext> {

    /**
     * 节点运行前执行
     */
    public void onStart() {
        super.onStart();
        System.out.println(this.getClass().getSimpleName() + " is start");
    }


    /**
     * 节点结束运行时执行
     */
    public void onEnd() {
        super.onEnd();
        System.out.println(this.getClass().getSimpleName() + " is end");
    }

    public NodeStatus onUpdate(TestContext context) {
        context.setHp(context.getHp()+20);
        System.out.println("add Hp");
        return NodeStatus.Success;
    }
}