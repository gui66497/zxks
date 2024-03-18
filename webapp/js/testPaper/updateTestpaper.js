/**
 * 修改试卷js
 * @date 2016/6/15 14:59
 * @author 房桂堂
 */
$(function () {

    //初始化icheck
    initCheckBox();

    var fromId = getUrlParam("id");
    TESTPAPERID = fromId;
    setParam(fromId);
    var onlyShow = getUrlParam("onlyShow");//查看考试 隐藏一些按钮

    //判断如果是查看试卷的话就隐藏部分操作按钮
    if (onlyShow == 1) {
        document.title = '查看试卷';
        $("#actionLi").text('查看试卷');
        $('#savePaperBtn').hide();
        $('button[name="batchDelItem"]').hide();
        $("#testpaperName").attr("disabled", "disabled");
        $("#testPaperCategorySelect").attr("disabled", "disabled");
        $("#totalTimeInput").attr("disabled", "disabled");
        $("#testpaperDescription").attr("disabled", "disabled");
        $("#testpaperExplain").attr("disabled", "disabled");
        $('textarea[name="partExplainArea"]').attr("disabled", "disabled");
        $('select[name="testpaperModeSelect"]').attr("disabled", "disabled");
        $('button[name="continuePersonalAdd"]').hide();
        $('button[name="continueAdd"]').hide();
        $('button[name="selectItemBtn"]').hide();
        $('button[name="itemPersonalBtn"]').hide();
        $('button[name="delPartBtn"]').hide();

        //延迟加载删除按钮disabled样式
        setTimeout(function () {
            $('button[class="deleteItem"]').attr("disabled", "disabled");
        }, 100)
    }

    $("button[name='selectItemBtn']").click(function () {
        var sectionDiv = $(this).closest("div[name='partPaper']");//父元素 用来判断是第几个按钮
        itemTypeSelect = sectionDiv.find("select[name='testpaperModeSelect']");
        var itemType = itemTypeSelect.val();//题目类型
        var lastIndex = $.testPaperPublic.getIndexByDiv(sectionDiv);//这是第lastIndex个模块
        overallIndex = lastIndex;
        showItemSelectModal(itemType);
    });

    //手动添加题目
    $("button[name='itemPersonalBtn']").click(function () {
        var sectionDiv = $(this).closest("div[name='partPaper']");//父元素 用来判断是第几个按钮
        itemTypeSelect = sectionDiv.find("select[name='testpaperModeSelect']");
        var itemType = itemTypeSelect.val();//题目类型

        var lastIndex = $.testPaperPublic.getIndexByDiv(sectionDiv);//如section-0 结果就是0
        overallIndex = lastIndex;

        showItemPersonalModal(itemType);
    });

    $("button[name='delPartBtn']").click(function () {
        var sectionDiv = $(this).closest("div[name='partPaper']");
        delPartSectionDiv = sectionDiv;
        showConfirmModal('确定要删除这个模块吗?', delPart);
        /*$('#sureTip').text('确定要删除这个模块吗？');
         $('#btnExecute').attr('onclick','delPart()');
         $('#sureModal').modal('show');*/
    });

    $("button[name='batchDelItem']").click(function () {
        var sectionDiv = $(this).closest("div[name='partPaper']");//父元素 用来判断是第几个按钮
        var index = $.testPaperPublic.getIndexByDiv(sectionDiv);//当前操作的部分序列号 如section-0 结果就是0
        var paperIdList = [];
        $("input[name='itemCheckbox"+index+"']:checkbox").each(function () {
            if (true == $(this).is(':checked')) {
                paperIdList.push(Number($(this).val()));
            }
        });
        if (paperIdList.length == 0) {
            showOkModal('提示框', '请选择需要删除的题目');
            return false;
        }
        $("input[name='itemCheckbox" + index + "']:checkbox").each(function () {
            if (true == $(this).is(':checked')) {
                delItem(Number($(this).val()), index);
            }
        });
    });

    $("button[name='continueAdd']").click(function () {
        var sectionDiv = $(this).closest("div[name='partPaper']");
        var drawIndex = $.testPaperPublic.getIndexByDiv(sectionDiv);
        itemTypeSelect = sectionDiv.find("select[name='testpaperModeSelect']");
        var itemType = itemTypeSelect.val();//题目类型

        showItemAddModal(itemType, drawIndex);
    });
    $("button[name='continuePersonalAdd']").click(function () {
        var sectionDiv = $(this).closest("div[name='partPaper']");
        var drawIndex = $.testPaperPublic.getIndexByDiv(sectionDiv);
        overallIndex = drawIndex;
        itemTypeSelect = sectionDiv.find("select[name='testpaperModeSelect']");
        var itemType = itemTypeSelect.val();//题目类型
        showItemPersonalAddModal(itemType, drawIndex);
    });

    $("#testpaperName").focus();
});


var TESTPAPERID;
var ITEMTYPE = 0;//试卷类型
var itemScore;//点击抽题 每题分数
var overallIndex = -1;//标记当前点击的是第几个table 第一个是0
var currentDataTable;//当前dataTable
var allTableMsg = [];//存放所有table和题数 每题分值
var addedIndex = -1;//继续添加的全局index
var delPartSectionDiv;
var itemList = {};
var answerSection = ['A', 'B', 'C', 'D', 'E', 'F', 'G'];


//设置初始值
function setParam(id) {
    if (id == null || id == "") {
        return false;
    }
    $.ajax({
        url: "/zxks/rest/testPaper/getTestPaperWithPart",
        type: "post",
        contentType: "application/json; charset=UTF-8",
        async: false,
        dataType: "json",
        headers: {userId: 118},
        data: JSON.stringify({id: id}),
        success: function (resultData) {
            var testPaper = resultData.data[0];//试卷
            setTestPaperCategorySelect(testPaper);//初始化和设置select框

            $('#testpaperName').val(testPaper.testpaperName);
            $('#testpaperDescription').val(testPaper.testpaperDescription);
            $('#testpaperExplain').val(testPaper.testpaperExplain);
            $('#finalScore').html(testPaper.markTotal);//试卷总分
            $('#totalTimeInput').val(testPaper.testTime);//考试时长
            if (testPaper.itemSequence == 1) {
                $('#itemSequenceInput').iCheck("check");
            }
            if (testPaper.answerSequence == 1) {
                $('#answerSequenceInput').iCheck("check");
            }

            //有值的模块order
            var allOrder = [];
            var testPaperPartList = testPaper.testPaperPartList;//试卷的模块
            console.log(testPaperPartList);
            $.each(testPaperPartList, function (i, testPaperPart) {
                //模块改为按顺序依次填充
                var partName = testPaperPart.partName;
                var partOrder = testPaperPart.partOrder;
                allOrder.push(partOrder);

                $("#dataIndexSpan")[0].innerText = partOrder + "";
                setTestPaperPart(partOrder, testPaperPart);

            });
            //初始化其余table（本来应该先初始化所有数组的，但是那样有问题）
            $.testPaperPublic.initOtherTable(allOrder);


        },
        error: function (xhr, r, e) {
            alert(e);
        }
    });
}

//设置各个模块的题目
function setTestPaperPart(partOrder, testPaperPart) {
    var partExplain = testPaperPart.partExplain;
    var itemArr = testPaperPart.item;
    if (itemArr.length < 1) {
        return false;
    }
    var itemCount = itemArr.length;//当前模块的题目数
    var partMark = testPaperPart.partMark;//该模块的总分值
    var oneItemScore = partMark / itemCount;//模块总分/模块数
    var allItem = [];
    $.each(itemArr, function (j, item) {
        console.log($("#dataIndexSpan")[0].innerText);
        $("#section-" + $("#dataIndexSpan")[0].innerText).find("select[name='testpaperModeSelect']").val(item.itemType);//设置题目类型
        var item = {
            "itemId": item.itemId,
            "itemTitle": returnSpan(item.itemTitle,item.itemId),
            "itemType": item.itemType,
            "itemBankName": item.itemBankName,
            "itemBankId": item.itemBankId
        };
        allItem.push(item);
    });

    var button = $("button[name='selectItemBtn']:eq(" + $("#dataIndexSpan")[0].innerText + ")");
    var itemTypeSelect = $("#section-" + $("#dataIndexSpan")[0].innerText).find("select[name='testpaperModeSelect']");
    var partExplainArea = $("#section-" + $("#dataIndexSpan")[0].innerText).find("textarea[name='partExplainArea']");
    itemTypeSelect.val();
    partExplainArea.val(testPaperPart.partExplain);

    console.log($("#dataIndexSpan")[0].innerText);
    var currentTb = $('#tb' + $("#dataIndexSpan")[0].innerText).DataTable({
        "data": allItem,
        "searching": false,//关闭搜索
        "paging": false,//关闭分页
        "bDestroy": true,
        "ordering": false,//关闭排序
        "id": "itemId",
        "columns": [
            {"data": "itemId", 'sClass': 'center'},
            {"data": "itemTitle"},
            {
                "data": "itemTitle", render: function (data, type, row, meta) {
                return '<a href="javascript:void(0);" style="cursor:pointer" onclick="$.testPaperPublic.showItemDetail(' + row.itemId + ')" >' + data + '</a>'
                }
            },
            {
                "data": "itemType", render: function (data, type, row, meta) {
                    if (data == 1) {
                        return "单选题";
                    } else if (data == 2) {
                        return "多选题";
                    } else if (data == 3) {
                        return "判断题";
                    } else if (data == 4) {
                        return "简答题";
                    } else if (data == 5) {
                        return "填空题";
                    }
                }
            },
            {"data": "itemBankName"},
            {"data": "score"}

        ],
        "aoColumnDefs": [
            {
                targets: 6,
                render: function (data, type, row, meta) {
                    var str = "<button data-original-title='删除'class='deleteItem' style='text-decoration:none' href='javascript:void(0);'" +
                        "onclick='delItem(" + row.itemId + "," + $("#dataIndexSpan")[0].innerText + ");' data-placement='auto top' data-trigger='hover focus'>" +
                        "</button>";
                    return str;
                }
            }, {
                targets: 5,//
                render: function (data, type, row, meta) {
                    return oneItemScore;
                }
            }],
        "fnRowCallback": function (nRow, aData, iDisplayIndex) {     // 当创建了行，但还未绘制到屏幕上的时候调用，通常用于改变行的class风格
            $('td:eq(0)', nRow).html("<input type='checkbox' class='itemCheckbox' name='itemCheckbox" + partOrder + "' value=" + aData.itemId + ">");
            $('td:eq(1)', nRow).html(iDisplayIndex + 1);
            return nRow;
        },
        "drawCallback": function (settings) {
            checkBoxReDraw(partOrder);
        }
    });

    var obj = {
        "partId": testPaperPart.partId,
        "table": currentTb,
        "itemCount": itemCount,
        "oneItemScore": oneItemScore,
        expandIcon: 'icon-plus',
        collapseIcon: 'icon-minus',
        checkedIcon: 'icon-check',
        uncheckedIcon: 'icon-retweet'
    };
    allTableMsg[$("#dataIndexSpan")[0].innerText] = obj;//存放题目数和每题分数
    $("#section-" + $("#dataIndexSpan")[0].innerText).find("button[name=continueAdd]").show();//显示继续添加按钮
    updateCountAndScore();//更新分数

    if ($('#tb' + $("#dataIndexSpan")[0].innerText).DataTable().data().length > 0) {
        $("#section-" + $("#dataIndexSpan")[0].innerText).find("button[name=continueAdd]").show();//显示继续智能添加按钮
        $("#section-" + $("#dataIndexSpan")[0].innerText).find("button[name=continuePersonalAdd]").show();//显示继续人工添加按钮
        $("#section-" + $("#dataIndexSpan")[0].innerText).find("input[type='text']").attr("disabled", "false");//显示题目数输入框不可操作
        $("#section-" + $("#dataIndexSpan")[0].innerText).find("select[name='testpaperModeSelect']").attr("disabled", "false");//显示题目类型输入框不可操作
        $("#section-" + $("#dataIndexSpan")[0].innerText).find("button[name=itemPersonalBtn]").hide();//隐藏人工添加按钮
        $("#section-" + $("#dataIndexSpan")[0].innerText).find("button[name=selectItemBtn]").hide();//隐藏智能添加按钮
    }


}

//删除模块
function delPart() {
    var index = $.testPaperPublic.getIndexByDiv(delPartSectionDiv);
    if (allTableMsg[index] != null) {
        allTableMsg[index] = null;
    }
    delPartSectionDiv.remove();
    updateCountAndScore();
}

/**
 * 删除一个考题
 * @param itemId itemId
 * @param i   第几个dataTable
 */
function delItem(itemId, i) {
    var table = allTableMsg[i].table;
    if (table == null) {//判断表格是否已经完成初始化
        return false;
    }
    var removeIndex = -1;
    var datas = table.data();
    $.each(datas, function (j, da) {
        if (da.itemId == itemId) {
            removeIndex = j;
        }
    });
    table.row(removeIndex).remove().draw(false);
    var nowCount = allTableMsg[i].itemCount - 1;
    //删除最后一道题目
    if (nowCount < 1) {
        $("#section-" + i).find("button[name='continueAdd']").hide();
        $("#section-" + i).find("button[name='continuePersonalAdd']").hide();
        $("#section-" + i).find("button[name='itemPersonalBtn']").show();
        $("#section-" + i).find("button[name='selectItemBtn']").show();

        $("#section-" + i).find("select[name='testpaperModeSelect']").prop("disabled", false);
        allTableMsg[i] = null;
        $("#tb" + i).find("input[name='checkAll']").iCheck("unCheck");
    } else {
        var oneItemScore = allTableMsg[i].oneItemScore;
        var nowPartId = allTableMsg[i].partId;
        allTableMsg[i] = {
            "partId": nowPartId,
            "table": table,
            "itemCount": nowCount,
            "oneItemScore": oneItemScore
        };
    }
    updateCountAndScore();
}

//打开题目选择模态框
function showItemSelectModal(itemType) {
    ITEMTYPE = itemType;

    buildLeftDom();//初始化左侧treeView
    buildSelectItemTable();//初始化题库选择dataTable
    hideAlert('addItemWarn');

    //显示分值选项框 隐藏题目数选项框
    $("#scoreP").show();
    $("#addItemP").hide();
    //隐藏继续抽提按钮 显示抽取题目按钮
    $("#continueAddBtn").hide();
    $("#addBtn").show();
    $('#itemSelectModal').modal(
        {
            show: true,
            keyboard: false,
            backdrop: 'static'
        }
    );
}

function showItemPersonalModal(itemType) {
    ITEMTYPE = itemType;

    initPersonalDom();//初始化左侧treeView
    selectPersonalItemTable();//初始化题库选择dataTable
    //显示分值选项框 隐藏题目数选项框
    $("#scoreD").show();
    $("#addItemD").hide();
    //隐藏继续抽提按钮 显示抽取题目按钮
    $("#uptoAddBtn").hide();
    $("#addItemBtn").show();
    //hideAlert('addItemWarn');
    $('#itemPersonalModal').modal(
        {
            show: true,
            keyboard: false,
            backdrop: 'static'
        }
    );
}

//初始化左边tree
function buildLeftDom() {
    //获取所有itemId
    var itemIds = [];
    $.each(allTableMsg, function (i, msg) {
        if (msg) {
            var datas = msg.table.data();
            $.each(datas, function(j,data) {
                itemIds.push(data.itemId);
            });
        }
    });
    var data = [];
    var categories = [];
    $.ajax({
        url: "/zxks/rest/itemBank/categoryList",
        type: "get",
        contentType: "application/json; charset=UTF-8",
        async: true,
        dataType: "json",
        headers: {userId: 118},
        data: {},
        success: function (resultData) {
            categories = resultData.data;
            var subCategories = [];
            $.each(categories, function (index, category) {//循环每个top
                var itemList = getItemByCategoryId(category.categoryId);
                var node = {
                    id: category.categoryId,
                    text: category.categoryName,
                    selectable: false,
                };
                if (itemList != null) {//分类下有题库
                    var subNode = [];
                    $.each(itemList, function (index, item) {
                        var subNodess = {
                            id: item.itemBankId,
                            text: item.itemBankName,
                            href: "#",
                            selectable: false
                        };
                        //过滤分类下面有题库没题目的题库
                        //var count = $.testPaperPublic.countItemByItemTypeAndItemBank(subNodess.id, ITEMTYPE);
                        var items = $.testPaperPublic.getItemByItemTypeAndItemBank(subNodess.id, ITEMTYPE);
                        var count = $.testPaperPublic.getLastCountExceptIds(items, itemIds);//题目数量(排除已选题目id)
                        if (count > 0) {
                            subNode.push(subNodess);
                        }
                    });
                    //过滤分类下面有题库没题目的分类
                    if (subNode.length > 0) {
                        node.nodes = subNode;
                    }
                }
                //过滤分类下面没题库的分类
                if (node.nodes != null) {
                    data.push(node);
                }
            });

            var options = {
                showCheckbox: true,
                levels: 2,
                data: data,
                expandIcon: 'icon-plus',
                collapseIcon: 'icon-minus',
                checkedIcon: 'icon-check',
                uncheckedIcon: 'icon-retweet'
            };
            var $checkableTree = $('#treeview-checkable').treeview(options);

            /*$checkableTree.on('nodeSelected',function(event,data) {
             itemBankSelectId = data.id;

             var param = itemBankSelectId + "_" + ITEMTYPE;
             $.ajax({
             url: "/zxks/rest/item/countItemByItemTypeAndItemBank/"+param,
             type: "get",
             async: false,//同步
             contentType: "application/json; charset=UTF-8",
             dataType: "json",
             headers: {userId: 118},
             success: function (resultData) {
             console.log(resultData);
             var countList = resultData.data;
             var count = countList[0];
             alert("数量为"+count);

             }
             });
             });
             $checkableTree.on('nodeUnselected',function(event,data) {
             itemBankSelectId = 0;
             });*/
            $checkableTree.on('nodeChecked', function (event, data) {
                var parentNodeId = $('#treeview-checkable').treeview('getParent', data.nodeId).nodeId;
                if (parentNodeId == undefined) {//父节点
                    var unCheckedNodes = $('#treeview-checkable').treeview('getUnchecked');//获取所有未选中的节点
                    $.each(unCheckedNodes, function (i, node) {
                        if (node.parentId == data.nodeId) {//筛选子节点
                            $('#treeview-checkable').treeview('checkNode', node.nodeId);//取消check状态
                        }
                    });

                } else {//子节点 单个添加
                    console.log(data);
                    var itemType = "";//题目类型
                    switch (ITEMTYPE) {
                        case '1':
                            itemType = "单选题";break;
                        case '2':
                            itemType = "多选题";break;
                        case '3':
                            itemType = "判断题";break;
                        case '4':
                            itemType = "简答题";break;
                        case '5':
                            itemType = "填空题";break;
                    }
                    var items = $.testPaperPublic.getItemByItemTypeAndItemBank(data.id, ITEMTYPE);
                    var count = $.testPaperPublic.getLastCountExceptIds(items, itemIds);//题目数量(排除已选题目id)
                    if (count < 1) {
                        //showAlertNoHide('addItemWarn', data.text + '题库下没有题目!');
                        showOkModal("提示",data.text + '题库下没有题目!');
                        $('#treeview-checkable').treeview('uncheckNode', data.nodeId);//取消check状态
                        return false;
                    }
                    var proportion = '<input type="text" name="proportionInput" style="width: 60px" onkeypress="return (/[1,2,3,4,5,6,7,8,9,0]/.test(String.fromCharCode(event.keyCode)))" class="form-control input-sm-1">';
                    var row = [data.id, itemType, data.text, count, proportion];
                    selectItemTb.row.add(row).draw();
                }
            });
            $checkableTree.on('nodeUnchecked', function (event, data) {
                var parentNodeId = $('#treeview-checkable').treeview('getParent', data.nodeId).nodeId;
                if (parentNodeId == undefined) {//父节点
                    var checkedNodes = $('#treeview-checkable').treeview('getChecked');//获取所有选中的节点
                    $.each(checkedNodes, function (i, node) {
                        if (node.parentId == data.nodeId) {//筛选子节点
                            $('#treeview-checkable').treeview('uncheckNode', node.nodeId);//取消check状态
                        }
                    });

                } else {//子节点
                    var datas = selectItemTb.data();
                    var removeIndex = -1;
                    $.each(datas, function (i, d) {
                        if (d[0] == data.id) {
                            removeIndex = i;
                        }
                    });
                    if (removeIndex > -1) {
                        selectItemTb.row(removeIndex).remove().draw(false);//删除一列
                    }
                }
            });

        }
    });
}

function convertToPagingEntity(aoData) {
    var pagingEntity = new Object();
    $.each(aoData, function (i, val) {
        if (val.name == "iDisplayStart") {
            pagingEntity.startIndex = val.value;
            startIndex = val.value;
        } else if (val.name == "iDisplayLength") {
            pagingEntity.pageSize = val.value;
        } else if (val.name == "sSearch") {
            pagingEntity.searchValue = val.value;
        } else if (val.name == "iSortCol_0") {
            pagingEntity.sortColumn = val.value;
        } else if (val.name == "sSortDir_0") {
            pagingEntity.sortDir = val.value;
        } else if (val.name == "sEcho") {
            pagingEntity.sEcho = val.value;
        }
    });
    //var jsonObj = JSON.stringify(pagingEntity);
    return pagingEntity;
}

//判断题目是否已经添加，添加过了做checked处理
function checkedInfo() {
    var selectTab1 = null;
    var selectTab2 = null;
    if (selectItemTab1) {
        selectTab1 = selectItemTab1.data();
    }
    if (selectItemTab) {
        selectTab2 = selectItemTab.data();
    }
    var currentTab = $('#tb' + overallIndex).DataTable().data();//获取已选定的题目
    if (selectTab1 && selectTab1.length > 0) {
        for (var i = 0; i < selectTab1.length; i++) {
            itemData.push(itemList[Number(selectTab1[i][0])]);
            for (var j = 0; j < selectTab2.length; j++) {
                if (selectTab1[i][0] == selectTab2[j].itemId) {
                    $("#itemCheck" + selectTab1[i][0] + "").prop("checked", "checked");
                    $("#itemCheck" + selectTab1[i][0] + "").iCheck("update");
                }
            }
        }
    }
    if (currentTab && currentTab.length > 0) {
        for (var i = 0; i < currentTab.length; i++) {
            for (var j = 0; j < selectTab2.length; j++) {
                if (currentTab[i].itemId == selectTab2[j].itemId) {
                    $("#itemCheck" + selectTab2[j].itemId + "").iCheck("destroy");
                    $("#itemCheck" + selectTab2[j].itemId + "").attr("style","display:none");
                    $("#itemCheck" + selectTab2[j].itemId + "").iCheck("update");
                }
            }
        }
    }
}

//去除销毁iCheck样式
function reInitCheckbox(){
    $("#personalCheckBox").on("ifCreated", function (event) {
        $('input[name="personalCheckBox"]').iCheck("destroy");
    });
    $("#personalCheckBox").on("ifCreated", function (event) {
        $('input[name="itemPersonalCheck"]').iCheck("destroy");
    });
}

//手工添加初始化左边tree
var itemBankIds = ["0"];
var itemData = [];
function initPersonalDom() {

    var data = [];
    var categories = [];
    $.ajax({
        url: "/zxks/rest/itemBank/categoryList",
        type: "get",
        contentType: "application/json; charset=UTF-8",
        async: true,
        dataType: "json",
        headers: {userId: 118},
        data: {},
        success: function (resultData) {
            categories = resultData.data;
            var subCategories = [];
            $.each(categories, function (index, category) {//循环每个top
                var itemList = getItemByCategoryId(category.categoryId);
                var node = {
                    id: category.categoryId,
                    text: category.categoryName,
                    selectable: false,
                };
                if (itemList != null) {//分类下有题库
                    var subNode = [];
                    $.each(itemList, function (index, item) {
                        var subNodess = {
                            id: item.itemBankId,
                            text: item.itemBankName,
                            href: "#",
                            selectable: false
                        };
                        //过滤分类下面有题库没题目的题库
                        var count = $.testPaperPublic.countItemByItemTypeAndItemBank(subNodess.id, ITEMTYPE);
                        if (count > 0) {
                            subNode.push(subNodess);
                        }
                    });
                    //过滤分类下面有题库没题目的分类
                    if (subNode.length > 0) {
                        node.nodes = subNode;
                    }
                }
                //过滤分类下面没题库的分类
                if (node.nodes != null) {
                    data.push(node);
                }

            });

            var options = {
                showCheckbox: true,
                levels: 2,
                data: data,
                expandIcon: 'icon-plus',
                collapseIcon: 'icon-minus',
                checkedIcon: 'icon-check',
                uncheckedIcon: 'icon-retweet'
            };
            var $checkableTree = $('#treeview-checkable2').treeview(options);

            //节点checked状态
            $checkableTree.on('nodeChecked', function (event, data) {
                itemBankIds = ["0"];
                var arrayCheck = $checkableTree.treeview("getChecked", data.nodeId);
                $.each(arrayCheck, function (index, obj) {
                    itemBankIds.push(obj.id);
                });
                var parentNodeId = $('#treeview-checkable2').treeview('getParent', data.nodeId).nodeId;
                if (parentNodeId == undefined) {//父节点
                    var unCheckedNodes = $('#treeview-checkable2').treeview('getUnchecked');//获取所有未选中的节点
                    $.each(unCheckedNodes, function (i, node) {
                        if (node.parentId == data.nodeId) {//筛选子节点
                            $('#treeview-checkable2').treeview('checkNode', node.nodeId);//获取·check状态
                        }
                    });

                } else {//子节点 单个添加
                    setTimeout(function(){
                        reInitCheckbox();
                        var itemType = "";//题目类型
                        switch (ITEMTYPE) {
                            case '1':
                                itemType = "单选";break;
                            case '2':
                                itemType = "多选";break;
                            case '3':
                                itemType = "判断";break;
                            case '4':
                                itemType = "简答";break;
                            case '5':
                                itemType = "填空";break;
                        }

                        itemBankIds.push(data.id);
                        var count = $.testPaperPublic.countItemByItemTypeAndItemBank(data.id, ITEMTYPE);//题目数量
                        if (count < 1) {
                            //showAlertNoHide('addItemWarn', data.text + '题库下没有题目!');
                            showOkModal("提示", data.text + '题库下没有题目!');
                            $('#treeview-checkable2').treeview('checkNode', data.nodeId);//取消check状态
                        }


                        var dataList = null;
                        var item = {
                            itemBankIds: itemBankIds.join(","),
                            itemType: ITEMTYPE
                        }

                        var itemObject = [];

                        selectItemTab = $("#selectItemTab").DataTable({
                            "language": {
                                "sSearchPlaceholder": "请输入题目名称"
                            },
                            "bServerSide": true,                //指定从服务器端获取数据
                            "bProcessing": true,
                            "ordering": false,
                            "bDestroy": true,
                            "sAjaxSource": "/zxks/rest/item/getItemByItemTypeAndItemBankIds",
                            "columns": [
                                {
                                    "data": "no", 'sClass': 'left',
                                    "mRender": function (data, type, full) {
                                        return '';
                                    }
                                },
                                {
                                    "data": "no", 'sClass': 'left',
                                    "mRender": function (data, type, full) {
                                        return '';
                                    }
                                },
                                {"data": "itemTitle", 'sClass': 'columnLeft'},
                                {"data": "itemType", 'sClass': 'center', render: function (data, type, row, meta) {
                                        if (data == 1) {
                                            return "单选";
                                        } else if (data == 2) {
                                            return "多选";
                                        } else if (data == 3) {
                                            return "判断";
                                        }
                                    }
                                },
                                {"data": "answer", 'sClass': 'center',render: function (data, type, row, meta) {
                                    return $.testPaperPublic.getAnswer(row.itemType, data);
                                }}
                            ],
                            "columnDefs" : [
                                {

                                    targets : 0,
                                    visible : false,
                                    searchable : true
                                }


                        ],
                            "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayStart, iDisplayLength) {     // 当创建了行，但还未绘制到屏幕上的时候调用，通常用于改变行的class风格

                                if (!itemList[aData.itemId]) {
                                    itemList[aData.itemId] = aData;
                                    console.log(">>>>>>>>>>>>>>>>" + itemList);
                                }
                                $('td:eq(0)', nRow).html('<input type="checkbox" id="itemCheck' + aData.itemId + '" name="itemPersonalCheck" class="itemPersonalCheck" value="' + aData.itemId + '" >');
                                $('td:eq(1)', nRow).html(returnSpan(aData.itemTitle,aData.itemId));
                                return nRow;

                            },

                            "fnServerData": function (sSource, aoData, fnCallback) {
                                $.ajax({
                                    "type": 'post',
                                    "contentType": "application/json",
                                    "url": sSource,
                                    "dataType": "json",
                                    "headers": {userId: 118},
                                    "data": JSON.stringify({
                                        item: item,
                                        paging: convertToPagingEntity(aoData)
                                    }),
                                    "success": function (resp) {
                                        fnCallback(resp);
                                        checkedInfo();
                                    }
                                });
                            }
                        });
                        exchangeCheckBox();
                    },300)

                }

            });


            //节点unchecked状态
            $checkableTree.on('nodeUnchecked', function (event, data) {
                itemList = {};
                itemBankIds = ["0"];
                var arrayCheck = $checkableTree.treeview("getChecked", data.nodeId);
                $.each(arrayCheck, function (index, obj) {
                    itemBankIds.push(obj.id);
                })

                var parentNodeId = $('#treeview-checkable2').treeview('getParent', data.nodeId).nodeId;
                if (parentNodeId == undefined) {//父节点
                    var checkedNodes = $('#treeview-checkable2').treeview('getChecked');//获取所有选中的节点
                    $.each(checkedNodes, function (i, node) {
                        if (node.parentId == data.nodeId) {//筛选子节点
                            $('#treeview-checkable2').treeview('uncheckNode', node.nodeId);//取消check状态

                        }
                    });

                } else {

                    //添加延迟事件模拟异步加载
                    setTimeout(function () {
                        reInitCheckbox();
                        var item = {
                            itemBankIds: itemBankIds.join(","),
                            itemType: ITEMTYPE
                        }
                        selectItemTab = $("#selectItemTab").DataTable({
                            "language": {
                                "sSearchPlaceholder": "请输入题目名称"
                            },
                            "bServerSide": true,                //指定从服务器端获取数据
                            "bProcessing": true,
                            "ordering": false,
                            "bDestroy": true,
                            "sAjaxSource": "/zxks/rest/item/getItemByItemTypeAndItemBankIds",
                            "columns": [
                                {
                                    "data": "no", 'sClass': 'left',
                                    "mRender": function (data, type, full) {
                                        return '';
                                    }
                                },
                                {
                                    "data": "no", 'sClass': 'left',
                                    "mRender": function (data, type, full) {
                                        return '';
                                    }
                                },
                                {"data": "itemTitle", 'sClass': 'columnLeft'},
                                {"data": "itemType", 'sClass': 'center', render: function (data, type, row, meta) {
                                    if (data == 1) {
                                        return "单选";
                                    } else if (data == 2) {
                                        return "多选";
                                    } else if (data == 3) {
                                        return "判断";
                                    }
                                }
                                },
                                {"data": "answer", 'sClass': 'center', render: function (data, type, row, meta) {
                                    return $.testPaperPublic.getAnswer(row.itemType, data);
                                }
                                }
                            ],
                            "columnDefs" : [
                                {
                                    targets : 0,
                                    visible : false,
                                    searchable : true
                                }
                            ],
                            "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayStart, iDisplayLength) {     // 当创建了行，但还未绘制到屏幕上的时候调用，通常用于改变行的class风格

                                if (!itemList[aData.itemId]) {
                                    itemList[aData.itemId] = aData;
                                }
                                $('td:eq(0)', nRow).html('<input type="checkbox" id="itemCheck' + aData.itemId + '" name="itemPersonalCheck" class="itemPersonalCheck" value="' + aData.itemId + '" >');
                                $('td:eq(1)', nRow).html(returnSpan(aData.itemTitle, aData.itemId));
                                var selectTab1 = selectItemTab1.data();
                                for (var i = 0; i < selectTab1.length; i++) {
                                    if (selectTab1[i].itemId == aData.itemId) {
                                        $("#itemCheck" + aData.itemId + "").prop("checked", "checked");
                                    }
                                }
                                return nRow;
                            },
                            "fnServerData": function (sSource, aoData, fnCallback) {
                                $.ajax({
                                    "type": 'post',
                                    "contentType": "application/json",
                                    "url": sSource,
                                    "dataType": "json",
                                    "headers": {userId: 118},
                                    "data": JSON.stringify({
                                        item: item,
                                        paging: convertToPagingEntity(aoData)
                                    }),
                                    "success": function (resp) {
                                        fnCallback(resp);
                                    }
                                });
                            },
                            "drawCallback": function (settings) {
                                exchangeCheckBox(); //checkbox初始化并监听其事件
                            }
                        });
                    }, 300);
                }
            });

        }
    });
}

function returnSpan(targetStr,itemId) {
    var name = "";
    if (targetStr.length > 15) {
        name = targetStr.substr(0,15) + "...";
    } else {
        name = targetStr;
    }

    return '<a style="cursor:pointer" title="' + targetStr + '" class="ellipsis" onclick="$.testPaperPublic.showItemDetail(' + itemId + ')" >' + name + '</a>';

}

//初始化题库选择dataTable
function buildSelectItemTable() {
    if ($.fn.DataTable.isDataTable('#selectItemTb')) {//判断表格是否已经完成初始化
        selectItemTb.rows().remove().draw(false);
    } else {
        selectItemTb = $("#selectItemTb").DataTable({
            "searching": false,//关闭搜索
            "paging": false,//关闭分页
            "ordering": false,
            "rowId": 'id',
            "columnDefs" : [
                {"bSortable": false, "aTargets": [0,1,2,3,4]},
                {

                    targets : 0,
                    visible : false,
                    searchable : true
                }
            ],
        });
    }
}

//手工组卷 初始化题库选择dataTable
function selectPersonalItemTable() {

    if ($.fn.DataTable.isDataTable('#selectItemTab')) {//判断已选列表是否已经完成初始化
        $('#selectItemTab').DataTable().clear().draw();
    }

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
        ],

    });

    if ($.fn.DataTable.isDataTable('#selectItemTab1')) {//判断已选列表是否已经完成初始化
        selectItemTab1.clear().draw();
    } else {
        selectItemTab1 = $("#selectItemTab1").DataTable({
            "searching": false,//关闭搜索
            "paging": false,//关闭分页
            "ordering" : false,
            "bDestroy":true,
            "rowId": 'id',
            "columnDefs" : [
                {
                    targets : 0,
                    visible : false,
                    searchable : true
                }
            ],
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {     // 当创建了行，但还未绘制到屏幕上的时候调用，通常用于改变行的class风格
                $('td:eq(0)', nRow).html(iDisplayIndex+1);
            }
        });
    }

}

//根据分类ID查询题库信息
function getItemByCategoryId(categoryId) {
    var itemList = null;
    $.ajax({
        url: "/zxks/rest/itemBank/infoList",
        type: "get",
        async: false,//同步
        contentType: "application/json; charset=UTF-8",
        dataType: "json",
        headers: {userId: 118},
        data: {"id": categoryId},
        success: function (resultData) {
            itemList = resultData.data;
        }
    });
    return itemList;
}

//准备抽题 前置判断
function toExtractItem() {
    var datas = selectItemTb.data();
    var typeCount = datas.length;//选择的题库个数
    if (typeCount == 0) {
        showOkModal("提示",'请先选择题库!');
        return false;
    }
    var score = $("#itemScoreInput").val();
    if (score == "") {
        showOkModal("提示",'请先输入每题分值!');
        return false;
    } else if (!(/^[0-9]{1,99}$/.test(score)) || Number(score) > 99 || Number(score) <= 0) {
        showOkModal(" 提示", '请输入正确的分值数(小于100的正整数)!');
        return false;
    }
    itemScore = Number(score);

    var proportionInputs = $("input[name='proportionInput']");//占比input集合

    var extractArr = [];
    var totalQuantity = 0;
    var judge = true;
    $.each(datas, function (i, data) {
        var obj;
        var quantity = proportionInputs[i].value;

        if (quantity == "" || quantity == undefined) {
            showOkModal(" 提示", '抽题数不能为空!');
            judge = false;
            return false;
        }
        if (quantity == 0) {
            return true;
        }
        if (!(/^[0-9]*[1-9][0-9]*$/.test(quantity)) || Number(quantity) < 0 || Number(quantity) > 999) {
            judge = false;
            showOkModal(" 提示", '请输入正确的抽题数(0或小于1000的正整数)!');
            return false;
        }
        if (quantity > data[3]) {
            showOkModal("提示", data[2] + " 题目数量不足以抽取！");
            judge = false;
            return false;
        }
        totalQuantity += parseInt(quantity);
        obj = {
            itemBankId: data[0],//题库id
            itemType: parseInt(ITEMTYPE),//题目类型
            quantity: parseInt(quantity)//题目数
        };
        extractArr.push(obj);
    });
    if (!judge) {
        return;
    }
    if (totalQuantity < 1) {
        showOkModal(" 提示", '总的抽题数不能为0！');
        return;
    }
    console.log(extractArr);
    //return;
    extractItem(extractArr);
}

//手工组卷，添加题目前置判断
function toExtractPersonalItem() {
    var datas = selectItemTab1.data();
    var typeCount = datas.length;//选择的题库个数
    if (typeCount == 0) {
        showOkModal('提示', '没有选择题目!');
        return false;
    }
    var score = $("#scoreInput").val();
    if (score == "") {
        showOkModal("提示", '请先输入每题分值!');
        return false;
    } else if (!(/^[0-9]{1,99}$/.test(score)) || Number(score) > 99 || Number(score) <= 0) {
        showOkModal(" 提示", '请输入正确的分值数(小于99的正整数)!');
        return false;
    }
    itemScore = Number(score);

    var itemCountList = [];
    for (var i = 0; i < datas.length; i++) {
        var itemToAdd = itemList[Number(datas[i][0])];
        if (itemToAdd) {
            itemCountList.push(itemToAdd);
        }
    }
    if (itemCountList.length < 1) {
        showOkModal('提示', "请选择题目!");
        return false;
    }
    organizePersonalItem();
}

//抽题
function extractItem(extractArr) {
    $.ajax({
        url: "/zxks/rest/testPaper/extractItem",
        type: "POST",
        contentType: "application/json; charset=UTF-8",
        dataType: "json",
        headers: {userId: 118},
        data: JSON.stringify({
            "objList": extractArr
        }),
        success: function (resultData) {
            showOkModal('提示框', resultData.message);
            if (resultData.resultCode == 'SUCCESS') {
                var itemList = resultData.data;
                organizeItem(itemList);
                $('#itemSelectModal').modal('hide');
            }
        }
    });

}

//组织题目
function organizeItem(itemList) {
    if ($.fn.DataTable.isDataTable('#tb' + overallIndex)) {//判断表格是否已经完成初始化
        $('#tb' + overallIndex).DataTable().destroy();
    }
    var currentTb = $('#tb' + overallIndex).DataTable({
        "data": itemList,
        "searching": false,//关闭搜索
        "paging": false,//关闭分页
        "id": "itemId",
        "ordering": false,
        "columns": [
            {
                "data": "itemId", render: function (data, type, row, meta) {
                return "<input type='checkbox' class='paperCheckbox' name='itemCheckbox"+ overallIndex +"' value=" + data + ">";
            }
            },
            {"data": "itemTitle"},
            {
                "data": "itemTitle", render: function (data, type, row, meta) {
                return '<a href="javascript:void(0);" style="cursor:pointer" onclick="$.testPaperPublic.showItemDetail(' + row.itemId + ')" >' + returnSpan(data,row.itemId) + '</a>'
            }
            },
            {
                "data": "itemType", render: function (data, type, row, meta) {
                if (data == 1) {
                    return "单选题";
                } else if (data == 2) {
                    return "多选题";
                } else if (data == 3) {
                    return "判断题";
                } else if (data == 4) {
                    return "简答题";
                } else if (data == 5) {
                    return "填空题";
                }
            }
            },
            {"data": "itemBankName"},
            {"data": "score"}

        ],
        "aoColumnDefs": [
            {
                targets: 6,
                render: function (data, type, row, meta) {
                    var str = "<button data-original-title='删除' class='deleteItem' style='text-decoration:none' href='javascript:void(0);'" +
                        "onclick='delItem(" + row.itemId + "," + overallIndex + ");' data-placement='auto top' data-trigger='hover focus'>" +
                        "</button>";

                    return str;
                }
            }, {
                targets: 5,//分值
                render: function (data, type, row, meta) {
                    return itemScore;
                }
            }],
        "fnRowCallback": function (nRow, aData, iDisplayIndex) {     // 当创建了行，但还未绘制到屏幕上的时候调用，通常用于改变行的class风格
            $('td:eq(1)', nRow).html(iDisplayIndex + 1);
            return nRow;
        },
        "drawCallback": function (settings) {
            //checkbox重绘
            checkBoxReDraw(overallIndex);
        }

    });

    currentDataTable = currentTb;
    var nowTableMsg = allTableMsg[overallIndex];
    var nowPartId = null;
    if (nowTableMsg != null) {
        nowPartId = allTableMsg[overallIndex].partId;
    }
    var updateItemCount = itemList.length;//修改过后的题目个数
    allTableMsg[overallIndex] = {//修改相关信息
        "partId": nowPartId,
        "table": currentTb,
        "itemCount": updateItemCount,
        "oneItemScore": itemScore
    };

    checkBoxReDraw(overallIndex);
    $("#section-"+overallIndex).find("button[name=continueAdd]").show();//显示继续智能添加按钮
    $("#section-"+overallIndex).find("button[name=continuePersonalAdd]").show();//显示继续人工添加按钮
    $("#section-"+overallIndex).find("select[name='testpaperModeSelect']").attr("disabled","false");//显示题目类型选择框不可操作
    $("#section-"+overallIndex).find("button[name=itemPersonalBtn]").hide();//隐藏人工添加按钮
    $("#section-"+overallIndex).find("button[name=selectItemBtn]").hide();//隐藏智能添加按钮
    updateCountAndScore();
}

//手工组卷，组织题目显示在单选题dataTable中
function organizePersonalItem() {
    var tables = selectItemTab1.data();
    var itemCountList = [];
    for(var i=0;i<tables.length;i++){
        var itemToAdd = itemList[Number(tables[i][0])];
        if (itemToAdd) {
            itemCountList.push(itemToAdd);
        }
    }
    var currentTb = $('#tb' + overallIndex).DataTable({
        "data": itemCountList,
        "searching": false,//关闭搜索
        "bDestroy": true,
        "paging": false,//关闭分页
        "id": "itemId",
        "ordering": false,//关闭排序
        "columns": [
            {
                "data": "itemId", render: function (data, type, row, meta) {
                return "<input type='checkbox' class='paperCheckbox' name='itemCheckbox" + overallIndex + "' value=" + data + ">";
                }
            },
            {"data": "itemTitle", 'sClass': 'itemTitle'},
            {
                "data": "itemTitle", render: function (data, type, row, meta) {
                return '<a href="javascript:void(0);" style="cursor:pointer" onclick="$.testPaperPublic.showItemDetail(' + row.itemId + ')" >' + returnSpan(data,row.itemId) + '</a>'
            }
            },
            {
                "data": "itemType", render: function (data, type, row, meta) {
                if (data == 1) {
                    return "单选题";
                } else if (data == 2) {
                    return "多选题";
                } else if (data == 3) {
                    return "判断题";
                } else if (data == 4) {
                    return "简答题";
                } else if (data == 5) {
                    return "填空题";
                }
            }
            },
            {"data": "itemBankName"},
            {"data": "score"}

        ],
        "aoColumnDefs": [
            {
                targets: 6,
                render: function (data, type, row, meta) {
                    var str = "<button data-original-title='删除' class='deleteItem' style='text-decoration:none' href='javascript:void(0);'" +
                        "onclick='delItem(" + row.itemId + "," + overallIndex + ");' data-placement='auto top' data-trigger='hover focus'>" +
                        "</button>";

                    return str;
                }
            }, {
                targets: 5,//分值
                render: function (data, type, row, meta) {
                    return itemScore;
                }
            }],
        "fnRowCallback": function (nRow, aData, iDisplayIndex) {     // 当创建了行，但还未绘制到屏幕上的时候调用，通常用于改变行的class风格
            $('td:eq(1)', nRow).html(iDisplayIndex + 1);
            return nRow;
        },
        "drawCallback": function (settings) {
            //重绘checkbox
            checkBoxReDraw(overallIndex);
        }

    });
    currentDataTable = currentTb;
    var nowTableMsg = allTableMsg[overallIndex];
    var nowPartId = null;
    if (nowTableMsg != null) {
        nowPartId = allTableMsg[overallIndex].partId;
    }
    var updateItemCount = itemCountList.length;//修改过后的题目个数
    allTableMsg[overallIndex] = {//修改相关信息
        "partId": nowPartId,
        "table": currentTb,
        "itemCount": updateItemCount,
        "oneItemScore": itemScore
    };

    $("#section-" + overallIndex).find("button[name=continueAdd]").show();//显示继续智能添加按钮
    $("#section-" + overallIndex).find("button[name=continuePersonalAdd]").show();//显示继续人工添加按钮
    $("#section-" + overallIndex).find("select[name='testpaperModeSelect']").attr("disabled","false");//显示题目类型选择框不可操作
    $("#section-" + overallIndex).find("button[name=itemPersonalBtn]").hide();//隐藏人工添加按钮
    $("#section-" + overallIndex).find("button[name=selectItemBtn]").hide();//隐藏智能添加按钮

    updateCountAndScore();

    $.testPaperPublic.clearTable();
    $('#itemPersonalModal').modal('hide');
    showOkModal('提示框', "手工抽取题目成功!");
}


//显示继续添加模态框**********************************
function showItemAddModal(itemType, drawIndex) {
    ITEMTYPE = itemType;
    buildLeftDom();//初始化左侧treeView
    buildSelectItemTable();//初始化题库选择dataTable

    addedIndex = drawIndex;//设置当前继续添加的时第几个
    //隐藏分值选项框 显示题目数选项框
    $("#scoreP").hide();
    $("#addItemP").show();
    //显示继续抽提按钮 隐藏抽取题目按钮
    $("#continueAddBtn").show();
    $("#addBtn").hide();
    $('#itemSelectModal').modal(
        {
            show: true,
            keyboard: false,
            backdrop: 'static'
        }
    );
}

//显示继续手工添加模态框
function showItemPersonalAddModal(itemType, drawIndex) {
    ITEMTYPE = itemType;
    initPersonalDom();//初始化左侧treeView
    selectPersonalItemTable();//初始化题库选择dataTable

    addedIndex = drawIndex;//设置当前继续添加的时第几个
    //隐藏分值选项框 显示题目数选项框
    $("#scoreD").hide();
    $("#addItemD").show();
    //显示继续抽提按钮 隐藏抽取题目按钮
    $("#uptoAddBtn").show();
    $("#addItemBtn").hide();
    $('#itemPersonalModal').modal(
        {
            show: true,
            keyboard: false,
            backdrop: 'static'
        }
    );
}

function toContinueExtractItem() {
    var datas = selectItemTb.data();
    var typeCount = datas.length;//选择的题库个数
    if (typeCount == 0) {
        //showAlertNoHide('addItemWarn', "请先选择题库!");
        showOkModal("提示", "请先选择题库!");
        return false;
    }
    //var itemCount = $("#itemContinueAddInput").val();//继续抽的题目数
    /*if (itemCount == "") {
        showOkModal("提示", "请输入题目数!");
        return false;
    }*/
    var proportionInputs = $("input[name='proportionInput']");//占比input集合

    var extractArr = [];
    var totalQuantity = 0;
    var judge = true;
    $.each(datas, function (i, data) {
        var obj;
        var quantity = proportionInputs[i].value;
        if (quantity == "" || quantity == undefined) {
            showOkModal(" 提示", '抽题数不能为空!');
            judge = false;
            return false;
        }
        if (quantity == 0) {
            return true;
        }
        if (!(/^[0-9]*[1-9][0-9]*$/.test(quantity)) || Number(quantity) < 0 || Number(quantity) > 999) {
            judge = false;
            showOkModal(" 提示", '请输入正确的抽题数(0或小于1000的正整数)!');
            return false;
        }
        if (quantity > data[3]) {
            showOkModal("提示", data[2] + " 题目数量不足以抽取！");
            judge = false;
            return false;
        }
        totalQuantity += parseInt(quantity);
        obj = {
            itemBankId: data[0],//题库id
            itemType: parseInt(ITEMTYPE),//题目类型
            quantity: parseInt(quantity)//题目数
        };
        extractArr.push(obj);
    });
    if (!judge) {
        return;
    }
    if (totalQuantity < 1) {
        showOkModal(" 提示", '总的抽题数不能为0！');
        return;
    }
    continusExtractItem(extractArr);
}



//手工组卷，继续添加前置判断
function toContinuePersonalExtractItem() {
    var datas = selectItemTab1.data();
    var typeCount = datas.length;//选择的题库个数
    if (typeCount == 0) {
        showOkModal("提示", "请选择题目!");
        return false;
    }

    var itemCountList = [];
    for (var i = 0; i < datas.length; i++) {
        var itemToAdd = itemList[Number(datas[i][0])];
        if (itemToAdd) {
            itemCountList.push(itemToAdd);
        }
    }
    if (itemCountList.length < 1) {
        showOkModal('提示', "请勾选题目!");
        return false;
    }
    organizePersonalAddedItem();
}


function continusExtractItem(extractArr) {
    var existItemIdS = [];
    $.each(allTableMsg[addedIndex].table.data(), function (i, data) {
        existItemIdS.push(data.itemId);
    });
    $.ajax({
        url: "/zxks/rest/testPaper/extractItem",
        type: "POST",
        contentType: "application/json; charset=UTF-8",
        dataType: "json",
        headers: {userId: 118},
        data: JSON.stringify({
            "objList": extractArr,
            "existItemIds": existItemIdS
        }),
        success: function (resultData) {
            if (resultData.resultCode == "SUCCESS") {
                var itemList = resultData.data;
                organizeAddedItem(itemList);
                showOkModal('提示框', resultData.message);
                $('#itemSelectModal').modal('hide');
            } else {
                showOkModal('提示框', resultData.message);
            }
        }
    });
}

//组织后来添加的题目
function organizeAddedItem(itemList) {
    var table = allTableMsg[addedIndex].table;
    var itemCount = allTableMsg[addedIndex].itemCount;
    $.each(itemList, function (i, item) {
        table.row.add({
            "itemId": item.itemId,
            "itemTitle": returnSpan(item.itemTitle),
            "itemType": item.itemType,
            "itemBankName": item.itemBankName,
            "itemBankId": item.itemBankId
        }).draw(true);
        itemCount += 1;
    });
    allTableMsg[addedIndex] = {
        "itemCount": itemCount,
        "oneItemScore": allTableMsg[addedIndex].oneItemScore,
        "table": table,
        "partId": allTableMsg[addedIndex].partId
    };
    updateCountAndScore();
}

//组织手工组卷后来添加的题目
function organizePersonalAddedItem() {
    var itemCountList = [];
    var datas = selectItemTab1.data();
    for(var i=0;i<datas.length;i++){
        var itemToAdd = itemList[Number(datas[i][0])];
        if (itemToAdd ) {
            itemCountList.push(itemToAdd);
        }
    }

    var table = allTableMsg[addedIndex].table;
    var itemCount = allTableMsg[addedIndex].itemCount;
    $.each(itemCountList, function (i, item) {
        table.row.add({
            "itemId": item.itemId,
            "itemTitle": returnSpan(item.itemTitle),
            "itemType": item.itemType,
            "itemBankName": item.itemBankName,
            "itemBankId": item.itemBankId
        }).draw(true);
        itemCount += 1;
    });
    allTableMsg[addedIndex] = {
        "itemCount": itemCount,
        "oneItemScore": allTableMsg[addedIndex].oneItemScore,
        "table": table,
        "partId": allTableMsg[addedIndex].partId
    };

    updateCountAndScore();

    $.testPaperPublic.clearTable();
    $('#itemPersonalModal').modal('hide');
    showOkModal("提示框", "手工抽取题目成功!");
}

//给checkBox添加iCheck样式
function exchangeCheckBox() {
    $('#selectItemTab').on('draw.dt', function () {
        initTargetCheckBox('itemPersonalCheck');
        initTargetCheckBox('personalCheckBox');
        var itemIds = [];
        var checkboxes = $("input.itemPersonalCheck");
        $("#personalCheckBox").on("ifChecked", function (event) {
            itemIds = [];
            $('input[name="itemPersonalCheck"]').iCheck("check");
        });
        $("#personalCheckBox").on("ifUnchecked", function (event) {
            itemIds = [];
            $('input[name="itemPersonalCheck"]').iCheck("uncheck");
            $("input[name='itemPersonalCheck']:checkbox").each(function () {
                if (false == $(this).is(':checked')) {
                    var itemId = $(this).val();
                    $("#deleteItem" + itemId).click();
                    $("#countSpan").html(selectItemTab1.data().length);
                    $("#continueAddInput").html(selectItemTab1.data().length);
                }
            });
        });

        //$('#personalCheckBox').iCheck("uncheck");
        checkboxes.on("ifChanged", function (event) {
            if (checkboxes.filter(':checked').length == checkboxes.length) {
                $("#personalCheckBox").prop("checked", "checked");
            } else {
                $("#personalCheckBox").removeProp("checked");
            }
            $("#personalCheckBox").iCheck("update");
        });

        checkboxes.on("ifChecked", function (event) {
            itemIds = [];
            itemIds.push(Number($(this).val()));
            getCheckInfo(itemIds);
        })

        checkedInfo(); //分页勾选方法
        checkboxes.on("ifUnchecked", function (event) {
            var itemId = $(this).val();
            $("#deleteItem" + itemId).click();
            $("#countSpan").html(selectItemTab1.data().length);
            $("#continueAddInput").html(selectItemTab1.data().length);
        })


        if (checkboxes.length>0 && checkboxes.filter(':checked').length == checkboxes.length) {
            $("#personalCheckBox").prop("checked", "checked");
        } else {
            $("#personalCheckBox").removeProp("checked");
        }
        $("#personalCheckBox").iCheck("update");
    });
}

//将已勾选的checkbox中的数据加载在已选列表中
//将取消勾选的checkbox中的数据从已选列表中删除
var itemIndex = 0;
function getCheckInfo(itemIds) {
    var itemToAdd = null;
    var selectTab1 = selectItemTab1.data();
    var currentTab = $('#tb' + overallIndex).DataTable().data();
    $.each(itemIds, function (index, obj) {
        if (selectTab1 && selectTab1.length > 0) {
            for (var i = 0; i < selectTab1.length; i++) {
                if (obj != selectTab1[i][0]) {
                    itemToAdd = itemList[obj];
                }
            }
        } else {
            itemToAdd = itemList[obj];
        }
        if(currentTab&&currentTab.length>0){
            for(var i=0;i<currentTab.length;i++){
                if(itemToAdd!=null){
                    if(currentTab[i].itemId ==itemToAdd.itemId){
                        itemToAdd = null;
                    }
                }
            }
        }
        if(itemToAdd!=null){
            var itemType = "";
            switch (itemToAdd.itemType) {
                case 1 :
                    itemType = "单选";break;
                case 2 :
                    itemType = "多选";break;
                case 3 :
                    itemType = "判断";break;
                case 4 :
                    itemType = "简答";break;
                case 5 :
                    itemType = "填空";break;
            }
            var answer = $.testPaperPublic.getAnswer(itemToAdd.itemType, itemToAdd.answer);
            var itemTitle = returnSpan(itemToAdd.itemTitle,itemToAdd.itemId);
            var operation = "<button id='deleteItem" + itemToAdd.itemId + "' data-original-title='删除' class='deleteItem' style='text-decoration:none' href='javascript:void(0);'" +
                "onclick='deletePersonItem(" + itemToAdd.itemId + ");' data-placement='auto top' data-trigger='hover focus'>" +
                "</button>";
            console.log(itemToAdd.itemId);
            var row = [itemToAdd.itemId, itemIndex, itemTitle, itemType, answer, operation];
            selectItemTab1.row.add(row).draw();
            $("#countSpan").html(selectItemTab1.data().length);
            allItemCount = parseInt(selectItemTab1.data().length);
            $("#continueAddInput").html(selectItemTab1.data().length);
        }

    })
}

//重绘checkbox
function checkBoxReDraw(index) {
    initTargetCheckBox('itemCheckbox' + index);
    $("#section-" + index).find("input[name='checkAll']").on("ifChecked", function (event) {
        var checkName = 'itemCheckbox' + index;
        $('input[name="'+checkName+'"]').iCheck("check");
    });
    $("#section-" + index).find("input[name='checkAll']").on("ifUnchecked", function (event) {
        var checkName = 'itemCheckbox' + index;
        $('input[name="'+checkName+'"]').iCheck("uncheck");
    });

    var checkboxs0 = $("input[name='itemCheckbox"+index+"']");
    checkboxs0.on("ifChanged", function (event) {
        if (checkboxs0.filter(':checked').length == checkboxs0.length) {
            $("#checkBoxAllCheckBox" + index).prop("checked", "checked");
        } else {
            $("#checkBoxAllCheckBox" + index).removeProp("checked");
        }
        $("#checkBoxAllCheckBox" + index).iCheck("update");
    });
}

function updateCountAndScore() {
    var finalItemCount = 0;
    var finalScore = 0;
    $.each(allTableMsg, function (i, obj) {
        if (obj != null) {
            finalItemCount += obj.itemCount;
            finalScore += obj.oneItemScore * obj.itemCount;
        }
    });
    $("#finalItemCount").html(finalItemCount + '');
    $("#finalScore").html(finalScore + '');
}

//修改试卷
function toUpdatePaper() {
    var testpaperName = $('#testpaperName').val();//试卷名称
    var testpaperDescription = $('#testpaperDescription').val();//试卷描述
    var testpaperExplain = $('#testpaperExplain').val();//试卷说明
    var totalTime = $('#totalTimeInput').val();//考试时长
    var testPaperCategorySelect = $('#testPaperCategorySelect option:selected').val();//考试类别
    var itemSequenceSelect = $('#itemSequenceInput').is(':checked') ? 1 : 2;//题目顺序
    var answerSequenceSelect = $('#answerSequenceInput').is(':checked') ? 1 : 2;//选项顺序
    var totalScore = $('#finalScore').html();//试卷总分值

    if ($.trim(testpaperName) == "") {
        showOkModal("提示",'请输入试卷名称!');
        return false;
    }
    if ($.trim(testpaperName).length > 50) {
        showOkModal("提示",'试卷名称长度应小于50!');
        return false;
    }
    if ($.trim(totalTime) == "") {
        showOkModal("提示",'请输入考试时长!');
        return false;
    }
    if (!(/^[0-9]{1,9999}$/.test(totalTime)) || Number(totalTime) > 1440 || Number(totalTime) <= 0) {
        showOkModal(" 提示", '请输入正确的考试时长(不大于1440分钟的正整数)!');
        $("#totalTimeInput").focus();
        return false;
    }

    //循环所有添加的part
    var testPaperParts = [];//模块数组
    var partTypeArr = [];//存放模块名 用于判断是否重复
    $.each(allTableMsg, function (i, obj) {
        if (obj) {
            var partType = $("#section-" + i).find("select[name='testpaperModeSelect']").val();//模块类型
            partTypeArr.push(partType);
        }
    });
    console.log(partTypeArr);
    if (isRepeat(partTypeArr)) {
        showOkModal('提示框', '模块的题目类型不能相同,请核实!');
        return false;
    }
    $.each(allTableMsg, function (i, obj) {
        if (obj) {
            var datas = obj.table.data();
            var partStr = "";
            var itemIds = [];//考题id数组
            $.each(datas, function (j, data) {
                itemIds.push(data.itemId);
            });
            var sectionDiv = $("#section-" + i);//顶级父模块
            var partName = sectionDiv.find("select[name='testpaperModeSelect']").find("option:selected").text();//模块名
            var partExplain = sectionDiv.find("textarea[name='partExplainArea']").val();//模块说明
            var partScore = obj.itemCount * obj.oneItemScore;//当前part分数
            testPaperParts.push({
                partId: obj.partId,
                partName: partName,
                partExplain: partExplain,
                partMark: partScore,
                partOrder: i,
                itemIds: itemIds
            });
        }
    });
    console.log(testPaperParts);
    //return;
    $.ajax({
        url: "/zxks/rest/testPaper/updateTestPaper",
        type: "PUT",
        contentType: "application/json; charset=UTF-8",
        async: true,
        dataType: "json",
        headers: {userId: 118},
        data: JSON.stringify({
            "testPaper": {
                "id": TESTPAPERID,
                "testpaperName": testpaperName,
                "testpaperDescription": testpaperDescription,
                "testpaperExplain": testpaperExplain,
                "categoryId": testPaperCategorySelect,
                "markTotal": totalScore,
                "testTime": totalTime,
                "itemSequence": itemSequenceSelect,
                "answerSequence": answerSequenceSelect,
                "testPaperParts": testPaperParts
            }
        }),
        success: function (resultData) {
            if (resultData != null) {
                showOkModal('提示框', resultData.message);
            }
            if (resultData.resultCode == "SUCCESS") {
                var categoryId = $("#testPaperCategorySelect").val();
                showOkModal('提示框', "试卷修改成功，即将调转到试卷列表页面");
                setTimeout(function () {
                    window.location.replace("testPaperManage.jsp?categoryId=" + categoryId);
                }, 2000);
            }
        },
        error: function (xhr, r, e) {
            alert(e);
        }
    });

}

//初始化和设置select框
function setTestPaperCategorySelect(testPaper) {
    $.ajax({
        url: "/zxks/rest/testPaperCategory/categoryList",
        type: "get",
        contentType: "application/json; charset=UTF-8",
        async: true,
        dataType: "json",
        headers: {userId: 118},
        success: function (resultData) {
            var categories = resultData.data;
            var fatherCategories = [];
            $.each(categories, function (j, cate) {
                if (cate.parentId == 0) {
                    fatherCategories.push(cate);
                }
            });
            var optionHtml = "";
            $.each(fatherCategories, function (index, category) {
                if (category.id == testPaper.categoryId) {
                    //添加下拉框
                    optionHtml += "<option value='" + category.id + "'selected='selected'>" + category.categoryName + "</option>";
                } else {
                    optionHtml += "<option value='" + category.id + "'>" + category.categoryName + "</option>";
                }
                $.each(categories, function (k, cate) {
                    if (cate.parentId == category.id) {//是它的子元素
                        if (cate.id == testPaper.categoryId) {
                            optionHtml += "<option value='" + cate.id + "'selected='selected'>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + cate.categoryName + "</option>";
                        } else {
                            optionHtml += "<option value='" + cate.id + "'>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + cate.categoryName + "</option>";
                        }


                    }
                });
            });

            $('#testPaperCategorySelect').text('');
            $('#testPaperCategorySelect').append(optionHtml);//考试类别
        },
        error: function (xhr, r, e) {
            alert(e);
        }
    });
}

function initCheckBox() {
    $('input[type!=radio]').iCheck({
        checkboxClass: 'icheckbox_square-blue',  // 注意square和blue的对应关系
        radioClass: 'iradio_square-blue',
        increaseArea: '20%' // optional
    })
}

//初始化指定name的checkbox
function initTargetCheckBox(name) {
    $('input[name='+name+']').iCheck({
        checkboxClass: 'icheckbox_square-blue',  // 注意square和blue的对应关系
        radioClass: 'iradio_square-blue',
        increaseArea: '20%'
    })
}

//删除手工选题列表中的一个考题
function deletePersonItem(itemId) {
    var table = selectItemTab1.data();

    var removeIndex = -1;

    for (var i = 0; i < table.length; i++) {
        if (table[i][0] == itemId) {
            removeIndex = i;
            table.row(removeIndex).remove().draw(false);
        }
    }
    $("#itemCheck" + itemId).removeProp("checked");
    $("#itemCheck" + itemId).iCheck("update");
    $("#personalCheckBox").removeProp("checked");
    $("#personalCheckBox").iCheck("update");
}

//获取url参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");//构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);//匹配目标参数
    if (r != null) return unescape(r[2]);
    return null;//返回参数值
}

