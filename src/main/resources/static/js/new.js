'use strict';

(function ($) {
    $(function () {
        var getdatasource = function () {

            //调用后台接口获得数据
            return {"id":"1659672105459487","name":"root","content":"cn.murfree.node.composite.CompositeSequence","children":[{"id":"1659672105460940","name":"选择执行","content":"cn.murfree.node.composite.CompositeSelector","children":[{"id":"1659672105461766","name":"释放技能【顺序】","content":"cn.murfree.node.composite.CompositeSequence","children":[{"id":"1659672105462229","name":"蓝是否充足","content":"cn.murfree.conditional.ConditionalMp"},{"id":"1659672105462452","name":"执行释放技能","content":"cn.murfree.action.ActionSkill"}]},{"id":"1659672105463028","name":"加血【顺序】","content":"cn.murfree.node.composite.CompositeSequence","children":[{"id":"1659672105464032","name":"是否血未满","content":"cn.murfree.conditional.ConditionalHp"},{"id":"1659672105464677","name":"执行加血","content":"cn.murfree.action.ActionAddHp"}]},{"id":"1659672105465892","name":"巡逻【持续2秒】","content":"cn.murfree.action.ActionPatrol"}]}]}
        };

        var getId = function () {
            return (new Date().getTime()) * 1000 + Math.floor(Math.random() * 1001);
        };

        var getdata = function () {
            if ($('#chart-container').find('.node:first').length === 0) return '';
            var hierarchy = $('#chart-container').orgchart('getHierarchy');
            if (hierarchy) {
                return hierarchy;
            }
            return '';
        }

        var addnode = function (name, content, cid, pid) {
            var $chartContainer = $('#chart-container')
            var nodeVals = []
            var obj = {name: '', content: ''}
            obj.name = name ? name : ''
            obj.content = content ? content : ''
            obj.id = cid ? cid : ''
            nodeVals.push(obj)
            if (pid) var $node = $('#' + pid);
            var hasChild = $node.parent().attr('colspan') > 0 ? true : false
            if (!hasChild) {
                var rel = nodeVals.length > 1 ? '110' : '100'
                $chartContainer.orgchart('addChildren', $node, {
                    'children': nodeVals.map(function (item) {
                        return {'name': item.name, 'content': item.content, 'relationship': rel, 'Id': item.id}
                    })
                }, $.extend({}, $chartContainer.find('.orgchart').data('options'), {depth: 0}))
            } else {
                $chartContainer.orgchart('addSiblings', $node.closest('tr').siblings('.nodes').find('.node:first'),
                    {
                        'siblings': nodeVals.map(function (item) {
                            return {'name': item.name, 'content': item.content, 'relationship': '110', 'Id': item.id};
                        })
                    })
            }
        }

        var deletenode = function (pid) {
            var $node = $('#' + pid)
            if ($node[0] === $('.orgchart').find('.node:first')[0]) {
                alert('不能删除根节点')
                return getdata();
            }
            $('#chart-container').orgchart('removeNodes', $node)
            $('#selected-node').val('').data('node', null)
            return getdata();
        }

        var renderpic = function (datascource) {
            $('#chart-container').empty();//渲染之前先全部清空
            $('#chart-container').orgchart({
                'data': datascource,
                'parentNodeSymbol': '',
                'nodeContent': "content",
                'createNode': function ($node, data) {
                    $node[0].id = getId();
                },
                'draggable': true,
                'dropCriteria': function ($draggedNode, $dragZone, $dropZone) {
                    if ($draggedNode.find('.content').text().indexOf('manager') > -1 && $dropZone.find('.content').text().indexOf('engineer') > -1) {
                        return false;
                    }
                    return true;
                }
            })
                .on('contextmenu', '.node', function (ev) {
                    var oEvent = ev || event;
                    var oUl = document.getElementById("contextDiv");
                    oUl.style.display = "block";
                    oUl.style.left = oEvent.clientX + 'px';
                    oUl.style.top = oEvent.clientY + 'px';
                    clicknode = $(this)[0].id
                    // clicknode = $(this)
                    return false;
                })
                .children('.orgchart').on('nodedropped.orgchart', function (event) {
                console.log('draggedNode:' + event.draggedNode.children('.title').text()
                    + ', dragZone:' + event.dragZone.children('.title').text()
                    + ', dropZone:' + event.dropZone.children('.title').text()
                );
            })

        }

        var disnone = function () {
            var oUl = document.getElementById("contextDiv");
            oUl.style.display = "none";
        }

        var erg = function (obj, id) {
            var fobj
            findobj(obj, id)

            function findobj(obj, id) {
                if (Object.prototype.toString.call(obj) === "[object Object]") {
                    if (obj.id === id) {
                        fobj = obj;
                    } else if (obj.children) {
                        findobj(obj.children, id);
                    }
                } else {
                    for (var i in obj) {
                        findobj(obj[i], id);
                    }
                }
            }

            return fobj;
        }

        // 以上事封装的方法，下面是具体执行的顺序
        // 设置一些常用量
        var data = getdatasource()
        var clicknode = '';
        renderpic(data)
        // 给标签绑定增加事件
        $('#add-node').on('click', function () {
            disnone();
            var name = '新的节点' //默认name
            var content = '节点描述' // 默认content
            // console.log(clicknode +','+ name +','+ content)
            //调用接口返回新的id,cid

            //调用接口返回新的id,cid
            addnode(name, content, getId(), clicknode)//此处先用getId()代替
        })
        // 给标签绑定增加事件

        // 绑定删除事件
        $('#delete-node').on('click', function () {
            disnone()
            //因为可以修改节点内容，不允许删除根节点
            //调用接口通知后台

            //调用接口通知后台
            data = deletenode(clicknode);
        });
        // 绑定删除事件

        // body绑定disnone事件，用于隐藏contextnemu
        $('body').on('click', disnone)
        // body绑定disnone事件，用于隐藏contextnemu

        // 绑定修改属性事件
        $('#change-props').on('click', function () {
            $('#itemid').val(clicknode);
            data = getdata();
            // console.log(erg(data,clicknode))
            $('#itemname').val(erg(data, clicknode).name)
            $('#itemcontent').val(erg(data, clicknode).content)
            //console.log(getdata())
            //调用接口，返回数据
            $('#myModal').modal({show: true})

        })
        // 绑定修改属性事件


        $('#savechange').on('click', function () {
            //保存之后重新渲染
            erg(data, clicknode).type = $("input[name='optionsRadios']:checked").val()
            console.log(erg(data, clicknode))
            erg(data, clicknode).name = $('#itemname').val()
            erg(data, clicknode).content = $('#itemcontent').val()
            // console.log(data)
            renderpic(data);
        })


        $('#log').on('click', function () {
            console.log(JSON.stringify(getdata()))
            alert("生成json成功，请在控制台查看！")
        })

        $('#loadJson').on('click', function () {
            let json = prompt();
            if (json !== undefined && json !== null && json !== '') {
                data = JSON.parse(json);
                clicknode = '';
                renderpic(data)
            }


        })

    })
})(jQuery);