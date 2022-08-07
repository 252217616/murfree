package cn.murfree.node.base;


import cn.murfree.contents.AbortType;
import cn.murfree.contents.NodeStatus;
import cn.murfree.context.IContext;
import cn.murfree.dto.ConditionalReevaluate;
import cn.murfree.node.composite.ICompositeNode;
import cn.murfree.node.decorator.IDecoratorNode;

import java.util.*;

/**
 * 基础树
 *
 * @author lujun
 * @date 2022/7/22
 */
public abstract class ITree<T extends IContext>  {
    /**
     * 根节点
     */
    private final INode root;

    /**
     *     上下文对象
     */
    private T  context;
    /**
     * 是否允许此行为树重新开始
     */
    private boolean isCanRestart = true;

    /**
     * 通过前序遍历保存行为树所有节点
     */
    private final List<INode> nodeList = new ArrayList<INode>();
    /**
     * 运行栈
     */
    private final Stack<Stack<Integer>> activeStack = new Stack<Stack<Integer>>();
    /**
     * 每个节点的父节点再nodeList上的索引
     */
    private final List<Integer> parentIndex = new ArrayList<Integer>();
    /**
     * 每个节点的子元素在nodeList上的索引
     */
    private final List<List<Integer>> childrenIndex = new ArrayList<List<Integer>>();
    /**
     * 当前元素是爸爸的第几个孩子
     */
    private final List<Integer> relativeChildIndex = new ArrayList<Integer>();
    /**
     * 第一个爸爸组合节点的index
     */
    private final List<Integer> parentCompositeIndex = new ArrayList<Integer>();
    /**
     * 孩子是条件节点的索引数组
     */
    private final List<List<Integer>> childConditionalIndex = new ArrayList<List<Integer>>();
    /**
     * 条件重评估对象保存
     */
    private final List<ConditionalReevaluate> conditionalReevaluateList = new ArrayList<ConditionalReevaluate>();
    private final Map<Integer, ConditionalReevaluate> conditionalReevaluateMap = new HashMap<Integer, ConditionalReevaluate>();

    public boolean isCanRestart() {
        return isCanRestart;
    }

    public void setCanRestart(boolean canRestart) {
        isCanRestart = canRestart;
    }


    public ITree(INode root, T context) {
        this.root = root;
        this.context = context;
        //将上下文传递进去
        enableBahacvior();
    }

    //启动行为树
    private void enableBahacvior() {
        //初始化运行栈
        this.activeStack.push(new Stack<Integer>());
        this.parentIndex.add(-1);
        this.relativeChildIndex.add(-1);
        this.parentCompositeIndex.add(-1);
        this.addToNodeList(this.root, -1);
        this.pushNode(0, 0);
    }


    private void addToNodeList(INode node, int parentCompositeIndex) {
        //保存当前节点
        this.nodeList.add(node);
        node.setContext(this.context);
        //当前元素在nodeLits的索引
        int index = this.nodeList.size() - 1;
        //拥有子元素
        if (node instanceof IParentNode) {
            IParentNode parentNode = (IParentNode) node;

            //如果是父节点类型，给孩子索引增加空数组
            this.childrenIndex.add(new ArrayList<Integer>());
            this.childConditionalIndex.add(new ArrayList<Integer>());
            for (int i = 0; i < parentNode.children.size(); i++) {
                //前序遍历 ...
                //给每个孩子保存父节点内容
                this.parentIndex.add(index);
                //保存当前父节点的孩子索引
                this.childrenIndex.get(index).add(this.nodeList.size());
                //保存当前节点的是父亲第几个孩子索引
                this.relativeChildIndex.add(i);
                //防止装入装饰节点
                if (node instanceof ICompositeNode) {
                    parentCompositeIndex = index;
                }
                //该节点的上层组合节点索引
                this.parentCompositeIndex.add(parentCompositeIndex);
                //前序遍历 ...
                this.addToNodeList(parentNode.children.get(i), parentCompositeIndex);
            }
        } else {
            //没有子元素的节点
            this.childrenIndex.add(null);
            this.childConditionalIndex.add(null);
            if (node instanceof IConditionalNode) {
                //如果是条件节点 找出上层的组合节点
                parentCompositeIndex = this.parentCompositeIndex.get(index);
                if (parentCompositeIndex != -1) {
                    //给上层组合节点增加孩子是条件节点的索引
                    this.childConditionalIndex.get(parentCompositeIndex).add(index);
                }
            }
        }
    }

    private void pushNode(int index, int stackIndex) {
        Stack<Integer> stack = this.activeStack.get(stackIndex);
        if (stack.size() == 0 || stack.get(stack.size() - 1) != index) {
            stack.push(index);
            INode<T> node = this.nodeList.get(index);
//            if (isOpenLog) {
//                log.info("pushNode {}", node);
//            }
            //执行初始化方法
            node.onStart();
        }
    }

    //运行行为树
    public void tick() {
        //条件判断 并回滚
//        this.reevaluateConftionalNode()
        //遍历所有的运行栈
        for (int i = this.activeStack.size() - 1; i >= 0; i--) {
            //拿到运行栈
            Stack<Integer> stack = this.activeStack.get(i);
            //上一次执行的节点索引
            int preIndex = -1;
            //上一次执行的节点状态
            NodeStatus preStatus = NodeStatus.Inactive;
            //当运行栈中有节点时执行
            while (preStatus != NodeStatus.Running && i < this.activeStack.size() && !stack.isEmpty()) {
                //找出当前运行节点的索引
                int curIndex = stack.get(stack.size() - 1);
                //如果本次和上次一样则跳过（防止重复执行）
                if (preIndex == curIndex) {
                    break;
                }
                //更新前置节点
                preIndex = curIndex;
                //执行节点，并更新前置节点状态
                preStatus = this.runNode(curIndex, i, preStatus);
            }

        }

    }

    public NodeStatus runNode(int index, int stackIndex, NodeStatus preStatus) {
        //将节点推入运行栈，并执行节点的onStart方法（内部有判断，如果当前节点已经在运行栈中就不执行）
        this.pushNode(index, stackIndex);
        //拿到当前节点
        INode node = this.nodeList.get(index);
        NodeStatus status = preStatus;
        if (node instanceof IParentNode) {
            IParentNode parentNode = (IParentNode) node;
            //如果是父节点，执行父节点逻辑
            status = this.runParentNode(index, stackIndex, preStatus);
            if (parentNode.canRunParallelChildren()) {
                status = node.getStatus();
            }
        } else {
            //普通节点执行节点的onUpdate方法
            status = node.onUpdate(this.context);
        }
        //如果节点已经执行完毕，非running状态，则推出运行栈，并执行onEnd方法，和父节点的onChildExecuted方法，改变运行孩子索引和父节点的状态
        if (status != NodeStatus.Running) {
            status = this.popNode(index, stackIndex, status, true);
        }
        //将本次节点运行的状态返回
        return status;
    }

    public NodeStatus runParentNode(int index, int stackIndex, NodeStatus preStatus) {
        //强转为父节点
        IParentNode parentNode = (IParentNode) this.nodeList.get(index);
        if (!parentNode.canRunParallelChildren() || parentNode.getStatus() != NodeStatus.Running) {
            NodeStatus childStatus = NodeStatus.Inactive;
            //如果父节点可执行，并且状态非running 则继续执行
            while (parentNode.canExecute() && (childStatus != NodeStatus.Running || parentNode.canRunParallelChildren())) {
                int childIndex = parentNode.getIndex();
                //如果是并行节点，增加运行栈将任务都推送进去
                if (parentNode.canRunParallelChildren()) {
                    this.activeStack.push(new Stack<Integer>());
                    stackIndex = this.activeStack.size() - 1;
                    //增加parentNode index
                    parentNode.onChildStarted();
                }
                //执行孩子节点
                childStatus = preStatus = this.runNode(this.childrenIndex.get(index).get(childIndex), stackIndex, preStatus);
            }
        }

        return preStatus;
    }

    public NodeStatus popNode(int index, int stackIndex, NodeStatus status, boolean popChildren) {
        Stack<Integer> stack = this.activeStack.get(stackIndex);
        //当前运行节点出栈
        stack.pop();
        //拿到当前节点
        INode node = this.nodeList.get(index);
        //执行onEnd方法
        node.onEnd();
//        if (isOpenLog) {
//            log.info("popNode {}", node);
//        }
        //拿到当前节点的父节点索引
        int parentIndex = this.parentIndex.get(index);
        //如果有父节点
        if (parentIndex != -1) {
            //当前节点是条件节点，生成条件重新评估对象
            if (node instanceof IConditionalNode) {
                //拿到当前节点的父组合节点
                int parentCompositeIndex = this.parentCompositeIndex.get(index);
                //如果有
                if (parentCompositeIndex != -1) {
                    //拿到父组合节点
                    ICompositeNode compositeNode = (ICompositeNode) this.nodeList.get(parentCompositeIndex);
                    //如果父组合节点有重新评估类型 则生成
                    if (compositeNode.getAbortType() != AbortType.None) {
                        if (this.conditionalReevaluateMap.containsKey(index)) {
                            //如果有先不要执行
                            ConditionalReevaluate conditionalReevaluate = this.conditionalReevaluateMap.get(index);

                            conditionalReevaluate.setCompositeIndex(-1);
                            conditionalReevaluate.setStatus(status);
                        } else {
                            //如果上级组合节点是低优先级，则本次不执行，等到执行到行为树右边时再执行
                            ConditionalReevaluate conditionalReevaluate = new ConditionalReevaluate(index, status, compositeNode.getAbortType() == AbortType.LowPriority ? -1 : parentCompositeIndex);
                            this.conditionalReevaluateList.add(conditionalReevaluate);
                            this.conditionalReevaluateMap.put(index, conditionalReevaluate);
                        }
                    }

                }
            }

            IParentNode parentNode = (IParentNode) this.nodeList.get(parentIndex);
            if (node instanceof IDecoratorNode) {
                //如果是装饰节点 则将状态装饰
                status = ((IDecoratorNode) node).decorator(status);
            }
            //运行子状态影响父状态的方法
            parentNode.onChildExecuted(status, this.relativeChildIndex.get(index));
        }

        //如果当前节点是组合节点退出
        if (node instanceof ICompositeNode) {
            ICompositeNode compositeNode = (ICompositeNode) node;
            //如果条件重新评估 只影响自己 或者 没有，或者当前运行栈已经没有运行节点了，则清空以当前节点为父节点的的条件重评估
            if (compositeNode.getAbortType() == AbortType.Self || compositeNode.getAbortType() == AbortType.None || stack.isEmpty()) {
                this.removeChildConditionalReevalute(index);
                //如果当前节点的重评估状态为低优先级，或者Both，则要将当前节点为父节点的条件重评估，父节点向上移动
            } else if (compositeNode.getAbortType() == AbortType.LowPriority || compositeNode.getAbortType() == AbortType.Both) {
                //遍历该节点下所有的条件节点（必须遍历 有可能当前节点下的条件重新评估指向-1，先不执行，当父节点退出时，指向上级，开始执行）
                for (int i = 0; i < this.childConditionalIndex.get(index).size(); i++) {
                    //拿到条件节点的索引
                    int curNodeIndex = this.childConditionalIndex.get(index).get(i);
                    //如果有条件重评估
                    if (this.conditionalReevaluateMap.containsKey(curNodeIndex)) {
                        //拿到条件重评估
                        ConditionalReevaluate conditionalReevaluate = this.conditionalReevaluateMap.get(curNodeIndex);
                        //将父节点指向当前节点的父组合节点
                        conditionalReevaluate.setCompositeIndex(this.parentCompositeIndex.get(index));
                    }
                }
                //遍历所有的条件重评估对象  把有可能是下级传递上来的条件重评估再往上传递
                for (ConditionalReevaluate conditionalReevaluate : this.conditionalReevaluateList) {
                    if (conditionalReevaluate.getCompositeIndex() == index) {
                        conditionalReevaluate.setCompositeIndex(this.parentCompositeIndex.get(index));
                    }
                }
            }
        }
        //并行节点返回失败将其他节点pop出去
        if (popChildren) {
            //拿到右边的运行栈
            for (int i = this.activeStack.size() - 1; i < stackIndex; i--) {
                Stack<Integer> curStack = this.activeStack.get(i);
                if (!curStack.isEmpty() && this.isParentNode(index, curStack.get(curStack.size() - 1))) {
                    for (int j = curStack.size() - 1; j >= 0; j--) {
                        this.popNode(curStack.get(curStack.size() - 1), i, NodeStatus.Failure, false);

                    }
                }

            }
        }


        //如果当前没有可以运行的节点了则重新开始行为树
        if (stack.isEmpty()) {
            if (stackIndex == 0) {
                //所有运行栈都运行完成了
                this.restart();
            } else {
                //当前栈运行完毕，其他栈还有 删除当前栈
                this.activeStack.remove(stackIndex);
            }
        }
        //返回状态
        return status;
    }

    private void removeChildConditionalReevalute(int index) {
        for (int i = this.conditionalReevaluateList.size() - 1; i >= 0; i--) {
            ConditionalReevaluate cur = this.conditionalReevaluateList.get(i);
            if (cur.getCompositeIndex() == index) {
                this.conditionalReevaluateMap.remove(cur.getIndex());
                this.conditionalReevaluateList.remove(i);
            }

        }
    }

    private boolean isParentNode(int parentIndex, int childIndex) {
        for (int i = childIndex; i != -1; i = this.parentIndex.get(i)) {
            if (i == parentIndex) {
                return true;
            }
        }
        return false;

    }

    private void restart() {
        if (this.isCanRestart) {
            this.removeChildConditionalReevalute(-1);
            this.pushNode(0, 0);
        }
    }

    private void reevaluateConftionalNode() {
        //倒序遍历收集到的重评估对象
        for (int i = this.conditionalReevaluateList.size() - 1; i >= 0; i--) {
            final ConditionalReevaluate conditionalReevaluate = this.conditionalReevaluateList.get(i);
            int index = conditionalReevaluate.getIndex();
            NodeStatus preStatus = conditionalReevaluate.getStatus();
            int compositeIndex = conditionalReevaluate.getCompositeIndex();
            //组合节点是-1的时候跳过
            if (compositeIndex == -1) {
                continue;
            }
            NodeStatus status = this.nodeList.get(index).onUpdate(this.context);
            //状态没有变化时跳过
            if (status == preStatus) {
                continue;
            }

            for (int j = this.activeStack.size() - 1; j >= 0; j--) {
                Stack<Integer> stack = this.activeStack.get(j);
                //首先找到当前节点和条件变化的节点的共同父节点
                int curNodeIndex = stack.get(stack.size() - 1);
                int commonParentIndex = this.findCommonParentIndex(curNodeIndex, index);
                if (this.isParentNode(compositeIndex, commonParentIndex)) {
                    int stackLen = this.activeStack.size();
                    //1、把当前节点的所有父节点退出运行栈
                    while (curNodeIndex != -1 && curNodeIndex != commonParentIndex && stackLen == this.activeStack.size()) {
                        this.popNode(curNodeIndex, j, NodeStatus.Failure, false);
                        curNodeIndex = this.parentIndex.get(curNodeIndex);
                    }
                }

            }

            //2、把公共节点下最顶级的父节点的右侧的条件重评估移除
            //倒序遍历 j -》 i的所有重评估对象都要删掉
            for (int j = this.conditionalReevaluateList.size() - 1; j >= i; j--) {
                ConditionalReevaluate curCR = this.conditionalReevaluateList.get(j);
                //只有当前重评估节点的父组合节点是重评估的父节点才可以删除
                if (this.isParentNode(compositeIndex, curCR.getIndex())) {
                    this.conditionalReevaluateMap.remove(curCR.getIndex());
                    this.conditionalReevaluateList.remove(j);
                }
            }
            //3、当前生效的条件重评估对象同一组合下的条件重评估对象停止并删除
            //当前重评估对象的父组合节点
            ICompositeNode compositeNode = (ICompositeNode) this.nodeList.get(this.parentCompositeIndex.get(index));
            //遍历左边的元素
            for (int j = i - 1; j >= 0; j--) {
                ConditionalReevaluate curCR = this.conditionalReevaluateList.get(j);
                //如果有相同的父组合节点
                if (this.parentCompositeIndex.get(curCR.getIndex()).equals(this.parentCompositeIndex.get(index))) {
                    //如果父组合重评估类型是低优先级
                    if (compositeNode.getAbortType() == AbortType.LowPriority) {
                        //不执行
                        curCR.setCompositeIndex(-1);
                    }
                }
            }
            //4、当前重评估的父节点到公共的父节点要重置内部的执行索引
            List<Integer> conditionalParentIndex = new ArrayList<Integer>();
            for (int j = this.parentIndex.get(index); j != compositeIndex; j = this.parentIndex.get(j)) {
                conditionalParentIndex.add(j);
            }
            conditionalParentIndex.add(compositeIndex);
            for (int j = conditionalParentIndex.size() - 1; j >= 0; j--) {
                IParentNode parentNode = (IParentNode) this.nodeList.get(conditionalParentIndex.get(j));
                if (j == 0) {
                    parentNode.onConditionalAbort(this.relativeChildIndex.get(index));
                } else {
                    parentNode.onConditionalAbort(this.relativeChildIndex.get(conditionalParentIndex.get(j-1)));
                }
            }
        }
    }

    private int findCommonParentIndex(int index1, int index2) {
        //收集index1 所有的父节点
        Set<Integer> set = new HashSet<Integer>();
        int num = index1;
        while (num != -1) {
            set.add(num);
            num = this.parentIndex.get(num);
        }

        num = index2;
        while (!set.contains(num)) {
            num = this.parentIndex.get(num);
        }

        return num;
    }

    protected  T getContext(){
        return this.context;
    }

}