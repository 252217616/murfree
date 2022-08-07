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
public class ActionSkill extends IActionNode<TestContext> {

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
        //获得上下文
        final TestContext context = getContext();
        //技能释放完毕扣蓝
        context.setMp(context.getMp()-20);
        System.out.println(this.getClass().getSimpleName() + " is end");
    }

    public NodeStatus onUpdate(TestContext context) {
        System.out.println("release skills");
        return NodeStatus.Success;
    }
}