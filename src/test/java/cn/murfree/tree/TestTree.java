package cn.murfree.tree;

import cn.murfree.context.TestContext;
import cn.murfree.node.base.INode;
import cn.murfree.node.base.ITree;

/**
 * TestTree
 *
 * @author lujun
 * @date 2022/8/5
 */
public class TestTree extends ITree<TestContext> {

    public TestTree(INode root, TestContext context) {
        super(root, context);
    }
}