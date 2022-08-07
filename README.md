# MurFree

这一个基于java的可视化行为编程框架，灵感来自于行为树模式，通过分治的思想，把复杂的业务逻辑拆成一个个最简单逻辑单元，再组合成一个多叉树；

*This visual behavior programming framework based on Java is inspired by the behavior tree mode. Through the idea of divide and conquer, the complex business logic is divided into the simplest logic units and then combined into a multi tree;*

+ 主要是为了解决：
  - 冗长的业务逻辑，在或没有注释的情况下，很难理解；
  - 高深的设计模式导致可读性较差；
  - 复杂多变的业务场景可视化、简单化；
    - 你可以用本框架轻松的构建一个属于你的用户画像模型；
    - 一个智能AI模型；
    - 一个智能的测试模型，配合你的图像识别逻辑，进行测试；
    - 取决于你得想象力；


+ *Mainly to solve:*
  - *Lengthy business logic is difficult to understand with or without comments;*
  - *Advanced design patterns lead to poor readability;*
  - *Visualization and simplification of complex and changeable business scenarios;*
    - *You can use this framework to easily build a user portrait model that belongs to you;*
    - *An intelligent AI model;*
    - *An intelligent test model, with your image recognition logic, to test;*
    - *It depends on your imagination;*

----
本框架通过使用基础节点和自定义节点，构建一颗专属的业务逻辑的树，框架会以前序遍历的方式执行树。

*This framework builds a dedicated business logic tree by using basic nodes and custom nodes, and the framework will execute the tree in the way of previous traversal.*

  + 基础节点介绍：
    - CompositeSequence：会顺序执行子节点，当有子节点全部执行成功，则该节点执行成功，其中一个子节点执行失败，则该节点会终止执行后续子节点，该节点变为失败状态。
    - CompositeRandomSequence：同上，但是不会顺序执行，会随机执行子节点。
    - CompositeSelector：会顺序执行子节点，当某个节点返回成功时，该节点执行成功，并中止子节点的运行，当所有子节点都执行失败时，该节点变为失败。
    - CompositeRandomSelevtor：同上，但是不会顺序执行，会随机执行子节点。
    - todo 后续会增加更多的基础节点、包装节点等


+ *Introduction to basic nodes:*
  - *Compositesequence: the sub nodes will be executed in sequence. When all the sub nodes are executed successfully, the node will be executed successfully. If one of the sub nodes fails, the node will terminate the execution of subsequent sub nodes, and the node will become a failed state.*
  - *Compositerandomsequence: the same as above, but it will not be executed sequentially, and the child nodes will be executed randomly.*
  - *Compositeselector: the child nodes will be executed in sequence. When a node returns success, the node will be executed successfully and the operation of the child nodes will be suspended. When all the child nodes fail to execute, the node will become a failure.*
  - *Compositerandomselevtor: the same as above, but it will not be executed in sequence, and the child nodes will be executed randomly.*
  - *Todo will add more basic nodes, Decorator nodes, etc*

----
例如：我们现在需要做一个智能的测试模型：

*For example, we now need to build an intelligent test model:*

当钱包里的币充足时，就浏览商品，并且随机购买，随机的进行收货、退货、评论，当钱币不足时进行充值，如果APP黑屏闪退，记录日志并且重新打开APP继续测试。

*When there is enough money in the wallet, browse the goods, buy at random, receive, return and comment at random, and recharge when there is insufficient money. If the app flashes back on the black screen, record the log and reopen the app to continue the test.*

规划的树如下：

![test](https://user-images.githubusercontent.com/31950679/183277988-cd4693e4-a4ba-4be8-9baf-2cbba50ce973.gif)

例如：我们现在要做一个游戏内的NPC的AI模型，我们希望该NPC在蓝足够的时候就一直释放技能，直到Mp不足，该NPC会受伤，当他的HP不足时要立刻停止释放技能，进行回血，当MP不足并且HP充足的时候，就进行巡逻。

*For example, we now want to make an AI model of an NPC in the game. We hope that the NPC will release skills when the blue is enough, until the MP is insufficient, the NPC will be injured. When his HP is insufficient, he should immediately stop releasing skills and regenerate blood. When the MP is insufficient and the HP is sufficient, he will patrol.*

规划的树如下：

![npc](https://user-images.githubusercontent.com/31950679/183277985-554b9b5a-3555-42e4-975b-9acba3a5e1d5.gif)

----

使用工程目录下resources/static/index.html 方便的构建一颗业务树，并生成JSON数据，框架会根据反射来创建并组装节点实例。

*Use resources/static/index Html is convenient to build a business tree and generate JSON data. The framework will create and assemble node instances according to reflection.*

----
详细使用方法包含在了测试类中,开始享用吧！~ 如果这个框架帮助到了你，请给我一个start，这对我来说非常重要~谢谢~；

*Detailed usage methods are included in the test class. Start to enjoy~ If this framework helps you, please give me a start, which is very important to me ~ thank you ~;*

如果你有疑问或者想一起参与该框架的迭代，请联系我：252217616@qq.com
*If you have questions or want to participate in the iteration of the framework together, please contact me: 252217616@qq.com*
