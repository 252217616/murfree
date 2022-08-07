package cn.murfree.action;

import cn.murfree.contents.NodeStatus;
import cn.murfree.context.TestContext;
import cn.murfree.node.base.IActionNode;

/**
 * 巡逻
 *
 * @author lujun
 * @date 2022/8/5
 */
public class ActionPatrol extends IActionNode<TestContext> {

    //持续时间
    private final int duration = 2000;
    //当前时间
    private long startTime = 0;

    @Override
    public void onStart() {
        super.onStart();
        this.startTime = System.currentTimeMillis();
    }
    public NodeStatus onUpdate(TestContext context) {
        if (System.currentTimeMillis() - this.startTime >= this.duration) {
            System.out.println("巡逻完毕");
            return NodeStatus.Success;
        }
        System.out.println("巡逻中");
        return NodeStatus.Running;
    }
}