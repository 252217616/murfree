package cn.murfree.contents;

/**
 * 中断类型
 *
 * @author luJun
 * @date 2022/7/22
 */
public enum AbortType {
    //不中断节点运行
    None,
    //中断低优先级的节点运行
    LowPriority,
    //中断同一组合下的节点运行
    Self,
    //同时具有LowPri 和Self
    Both
}
