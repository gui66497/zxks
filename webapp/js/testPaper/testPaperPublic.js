/**
 * 创建试卷和修改试卷公用函数
 * @Author guitang.fang
 * @Date 2016/6/22 14:32
 */
(function($) {
    $.testPaperPublic = {
        answerSection :  ['A', 'B', 'C', 'D', 'E', 'F', 'G'],
        allOrder: [0, 1, 2],
        //查看单个考题详情
        showItemDetail : function (itemId) {
            $.ajax({
                url: "/zxks/rest/item/itemInfo",
                type: "post",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: 118},
                data: JSON.stringify({"id":itemId}),
                success: function (resultData) {
                    if (resultData != null) {
                        var items = resultData.data;
                        if(items == null || items.length == 0){
                            return false;
                        }
                        var item = items[0];//考题信息
                        console.log(item);
                        $("#itemCode").html(item.itemCode);
                        var innerItem = "";
                        $("#itemDetailTitle").html("<b>题干: </b>" + item.itemTitle);

                        var characters = ['A','B','C','D','E','F','G'];
                        var options = item.optionList;
                        console.log(options);

                        if (item.itemType == 5) {//填空题
                            var blankOptions = item.optionList;
                            $.each(blankOptions, function (i, option) {
                                innerItem += '<div class="control-group">';
                                innerItem += '<div class="span5" style="padding-top: 10px;margin-left: 0px;width: 502px;margin-right: 20px;">';
                                innerItem += '<input type="text" style="width:75%" readonly="readonly" value=填空'+(++i)+'：'+option.content+'></input>';
                                innerItem += '</div>';
                                innerItem += '</div>';
                            });
                        } else if (item.itemType == 4) {//简答题
                            var option = item.optionList[0];
                            innerItem += '<div class="control-group">';
                            innerItem += '<div class="span5" style="padding-top: 10px;margin-left: 0px;width: 502px;margin-right: 20px;">';
                            innerItem += '<textarea class="answerTextarea" readonly="readonly" >'+option.content+'</textarea>';
                            innerItem += '</div>';
                            innerItem += '</div>';

                        } else if (item.itemType == 2) { //多选
                            var itemAnswer = item.answer.split(',');
                            $.each(options, function (i, option) {
                                innerItem += '<div class="control-group">';
                                innerItem += '<div class="span5" style="padding-top: 10px;margin-left: 0px;width: 502px;margin-right: 20px;">';
                                if (itemAnswer.indexOf(i+1 + '') > -1) {
                                    innerItem += '<div class="input-append" style="width: 100%">';
                                }
                                var content = (option.content).replace(new RegExp("\"","gm"), "&quot;");
                                innerItem += '<input type="text" style="width:75%" readonly="readonly" value="'+characters[i]+'：' + content + '"></input>';
                                if (itemAnswer.indexOf(i+1 + '') > -1) {
                                    innerItem += '<span class="add-on">☆</span>';
                                    innerItem += '</div>';
                                }
                                innerItem += '</div>';
                                innerItem += '</div>';
                            });
                            //单选和判断
                        } else {
                            $.each(options, function (i, option) {
                                innerItem += '<div class="control-group">';
                                innerItem += '<div class="span5" style="padding-top: 10px;margin-left: 0px;width: 502px;margin-right: 20px;">';
                                if (item.answer == i+1) {
                                    innerItem += '<div class="input-append" style="width: 100%">';
                                }
                                var content = (option.content).replace(new RegExp("\"","gm"), "&quot;");
                                innerItem += '<input type="text" style="width:75%" readonly="readonly" value="'+characters[i]+'：'+ content +'"></input>';
                                if (item.answer == i+1) {
                                    innerItem += '<span class="add-on">☆</span>';
                                    innerItem += '</div>';
                                }
                                innerItem += '</div>';
                                innerItem += '</div>';
                            });
                        }




                        $("#itemBody").text('').append(innerItem);
                        $('#itemDetailModal').modal(
                            {
                                show:true,
                                keyboard:false,
                                backdrop:'static'
                            }
                        );
                    }
                }
            });
        },
        //根据题目类型和题库id获取题目数量
        countItemByItemTypeAndItemBank : function (itemBankId, itemType) {
            var param = itemBankId + "_" + itemType;
            var resCount = null;
            $.ajax({
                url: "/zxks/rest/item/countItemByItemTypeAndItemBank/"+param,
                type: "get",
                async: false,//同步
                contentType: "application/json; charset=UTF-8",
                dataType: "json",
                headers: {userId: 118},
                success: function (resultData) {
                    var countList = resultData.data;
                    resCount = countList[0];
                }
            });
            return resCount;
        },
        //根据题目类型和题库id获取题目id数组
        getItemByItemTypeAndItemBank : function (itemBankId, itemType) {
            var param = itemBankId + "_" + itemType;
            var resCount = null;
            $.ajax({
                url: "/zxks/rest/item/getItemByItemTypeAndItemBank/"+param,
                type: "get",
                async: false,//同步
                contentType: "application/json; charset=UTF-8",
                dataType: "json",
                headers: {userId: 118},
                success: function (resultData) {
                    resCount = resultData.data;;
                }
            });
            return resCount;
        },
        //
        getLastCountExceptIds : function (items, itemIds) {
            var resItems = [];
            $.each(items, function (i, item) {
                if (itemIds.indexOf(item.itemId) == -1) {
                    resItems.push(item)
                }
            });
            return resItems.length;
        },
        //根据div的id判断是第几个模块
        getIndexByDiv : function(sectionDiv) {
            var index = sectionDiv.attr("id");
            return index.substr(index.length-1);//通过div的id获取这是第几个模块 三个模块默认0 1 2
        },
        //根据题目类型和答案数字获取显示答案 如 1 -> A   2,3 -> B,C
        getAnswer: function (itemType, answerStr) {
            if (itemType == 1) {
                return this.answerSection[answerStr-1];
            } else if (itemType == 2) {
                var numArr = answerStr.split(",");
                var sectionArr = [];
                $.each(numArr, function (i, num) {
                    sectionArr.push(answerSection[num-1]);
                });
                return sectionArr.join(",");
            } else if (itemType == 3) {
                if (answerStr == 1) {
                    return "对";
                } else if (answerStr == 2) {
                    return "错";
                }
            } else if (itemType == 4) {
                return answerStr
            } else if (itemType == 5) {
                if (answerStr) {
                    return answerStr.replace(/░/g, ',');
                }
            }
        },
        initModuleTable: function (n) {
            for (var i = 0;i < n;i ++) {
                $('#tb' + i).DataTable({
                    "bDestroy": true,
                    "searching": false,//关闭搜索
                    "paging": false,//关闭分页
                    "ordering": false,//关闭排序
                    "drawCallback" : function () {
                        $.testPaperPublic.initCheckBox();
                    }
                });
            }
        },
        //初始化其他datatable
        initOtherTable: function (orders) {
            var otherOrders = [];
            console.log(this.allOrder);
            $.each($.testPaperPublic.allOrder, function (i, order) {
                if (orders.indexOf(order) == -1) {
                    otherOrders.push(order);
                }
            });
            $.each(otherOrders, function (j, order) {
                $('#tb' + order).DataTable({
                    "bDestroy": true,
                    "searching": false,//关闭搜索
                    "paging": false,//关闭分页
                    "ordering": false//关闭排序
                });
            });

        },
        initCheckBox: function () {
            $('input[type!=radio]').iCheck({
                checkboxClass: 'icheckbox_square-blue',  // 注意square和blue的对应关系
                radioClass: 'iradio_square-blue',
                increaseArea: '20%' // optional
            });
        },
        //清空表格
        clearTable: function () {
            $('input[name="itemPersonalCheck"]').iCheck("uncheck");
            $("#personalCheckBox").iCheck("uncheck");

            if ($.fn.DataTable.isDataTable('#selectItemTab')) {
                $('#selectItemTab').DataTable().clear().draw();

                selectItemTab = $("#selectItemTab").DataTable({
                    "language": {
                        "sSearchPlaceholder": "请输入题目名称"
                    },
                    "ordering": false,
                    "bDestroy": true,
                    "columnDefs": [
                        {
                            targets: 0,
                            visible: false,
                            searchable: true
                        }
                    ]
                });
            }
            if ($.fn.DataTable.isDataTable('#selectItemTab1')) {
                $('#selectItemTab1').DataTable().clear().draw();
            }
        },
        //面包屑导航跳转
        breadHref: function (hrefString) {
            var url = location.search;
            window.location.href = hrefString + url;
        },
        //格式化段落,超过15个字就省略号
        returnSpan: function (targetStr) {
            var name = "";
            if (targetStr.length > 15) {
                name = targetStr.substr(0,15) + "...";
            } else {
                name = targetStr;
            }
            return "<span title='" + targetStr + "' class='ellipsis' style='float:left;margin-left:10px;white-space: pre-wrap' >" + name + " </span>"
        }
        
    }
})(jQuery);