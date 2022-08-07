package cn.murfree.utils;

import com.fasterxml.jackson.databind.JsonNode;
import cn.murfree.node.base.INode;
import cn.murfree.node.base.IParentNode;

/**
 * TreeBuild
 *
 * @author lujun
 * @date 2022/7/27
 */
public class MurFreeBuilder {



    public static INode build(JsonNode jsonNode) throws Exception {
        final String nodeName = jsonNode.get("name").textValue();
        final String className = jsonNode.get("content").textValue();
        //创建该类
        final INode node = (INode) Class.forName(className).newInstance();
        node.setName(nodeName);
        if(jsonNode.has("children")){
            final IParentNode parentNode = (IParentNode) node;
            final JsonNode children = jsonNode.get("children");
            for (JsonNode child : children) {
                parentNode.addChild(build(child));
            }
        }
        return node;
    }
}