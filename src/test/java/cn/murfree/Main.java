package cn.murfree;

import cn.murfree.context.TestContext;
import cn.murfree.node.base.INode;
import cn.murfree.tree.TestTree;
import cn.murfree.utils.MurFreeBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Main
 *
 * @author lujun
 * @date 2022/8/5
 */
public class Main {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        System.out.println("顺序节点测试");
        testSeq();
        System.out.println("选择节点测试");
        testSelector();
    }

    private static void testSeq() throws Exception {
        String json = "{\"id\":\"1659672105459487\",\"name\":\"root\",\"content\":\"cn.murfree.node.composite.CompositeSequence\",\"children\":[{\"id\":\"1659672105460940\",\"name\":\"顺序执行\",\"content\":\"cn.murfree.node.composite.CompositeSequence\",\"children\":[{\"id\":\"1659672105461766\",\"name\":\"释放技能【顺序】\",\"content\":\"cn.murfree.node.composite.CompositeSequence\",\"children\":[{\"id\":\"1659672105462229\",\"name\":\"蓝是否充足\",\"content\":\"cn.murfree.conditional.ConditionalMp\"},{\"id\":\"1659672105462452\",\"name\":\"执行释放技能\",\"content\":\"cn.murfree.action.ActionSkill\"}]},{\"id\":\"1659672105463028\",\"name\":\"加血【顺序】\",\"content\":\"cn.murfree.node.composite.CompositeSequence\",\"children\":[{\"id\":\"1659672105464032\",\"name\":\"是否血未满\",\"content\":\"cn.murfree.conditional.ConditionalHp\"},{\"id\":\"1659672105464677\",\"name\":\"执行加血\",\"content\":\"cn.murfree.action.ActionAddHp\"}]},{\"id\":\"1659672105465892\",\"name\":\"巡逻【持续2秒】\",\"content\":\"cn.murfree.action.ActionPatrol\"}]}]}";
        final JsonNode jsonNode = mapper.readTree(json);
        final INode root = MurFreeBuilder.build(jsonNode);
        final TestTree tree = new TestTree(root, new TestContext());
        //顺序节点 每次执行tick 会全部执行
        tree.tick();
    }

    private static void testSelector() throws Exception {
        String json = "{\"id\":\"1659672105459487\",\"name\":\"root\",\"content\":\"cn.murfree.node.composite.CompositeSequence\",\"children\":[{\"id\":\"1659672105460940\",\"name\":\"选择执行\",\"content\":\"cn.murfree.node.composite.CompositeSelector\",\"children\":[{\"id\":\"1659672105461766\",\"name\":\"释放技能【顺序】\",\"content\":\"cn.murfree.node.composite.CompositeSequence\",\"children\":[{\"id\":\"1659672105462229\",\"name\":\"蓝是否充足\",\"content\":\"cn.murfree.conditional.ConditionalMp\"},{\"id\":\"1659672105462452\",\"name\":\"执行释放技能\",\"content\":\"cn.murfree.action.ActionSkill\"}]},{\"id\":\"1659672105463028\",\"name\":\"加血【顺序】\",\"content\":\"cn.murfree.node.composite.CompositeSequence\",\"children\":[{\"id\":\"1659672105464032\",\"name\":\"是否血未满\",\"content\":\"cn.murfree.conditional.ConditionalHp\"},{\"id\":\"1659672105464677\",\"name\":\"执行加血\",\"content\":\"cn.murfree.action.ActionAddHp\"}]},{\"id\":\"1659672105465892\",\"name\":\"巡逻【持续2秒】\",\"content\":\"cn.murfree.action.ActionPatrol\"}]}]}";
        final JsonNode jsonNode = mapper.readTree(json);
        final INode root = MurFreeBuilder.build(jsonNode);
        final TestTree tree = new TestTree(root, new TestContext());
        //选择节点 每次执行tick 当有子节点执行成功时结束
        for (int i = 0; i < 4; i++) {
            tree.tick();
            System.out.println("tick over");
            Thread.sleep(3000);
        }
    }
}