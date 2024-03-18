/**
 * 考试类别和试卷JS
 * @date 2016/6/1 10:59
 * @author 房桂堂
 */
$(function () {
    //初始化icheck
    initCheckBox();
    //构建考试分类树
    buildDomTree();
    var elements = document.getElementsByClassName("node-selected");
    if (elements[0] == null) {
        $('#nodeId').val("");
        $('#categoryNameUpdate').val("");
    }
});

var treeView;

$('a').tooltip({
    delay: {
        show: 500,
        hide: 100
    }
    //placement:left,
});

$('button').tooltip({
    delay: {
        show: 500,
        hide: 100
    },
    container: 'body'
});


function returnSpan(targetStr) {
    var name = "";
    if (targetStr.length > 15) {
        name = targetStr.substr(0, 15) + "...";
    } else if (targetStr.length> 0) {
        name = targetStr;
    }

    return "<span title='" + targetStr + "' class='ellipsis' style='margin-left:5px;' >" + name + " </span>"
}

//构建树型目录
function buildDomTree() {
    var data = [];
    var categories = [];
    $.ajax({
        url: "../../zxks/rest/testPaperCategory/categoryList",
        type: "get",
        contentType: "application/json; charset=UTF-8",
        async: true,
        dataType: "json",
        headers: {userId: 118},
        data: {},
        success: function (resultData) {
            categories = resultData.data;
            var topCategories = [];
            var subCategories = [];
            $.each(categories, function (index, category) {
                if (category.parentId == 0) {
                    topCategories.push(category);
                } else {
                    subCategories.push(category);
                }
            });
            $.each(topCategories, function (index, category) {

                $.each(subCategories, function (index2, subcategory) {
                    if (subcategory.parentId == category.id) {
                        if (!category.nodes) {
                            category.nodes = []
                        }
                        var subNode = {
                            id: subcategory.id,
                            categoryName: subcategory.categoryName
                        };
                        category.nodes.push(subNode);
                        addNodes(subNode);
                    }

                });
            });

            function addNodes(targetCategory) {
                $.each(subCategories, function (index2, subcategory) {
                    if (subcategory.parentId == targetCategory.id) {
                        if (!targetCategory.nodes) {
                            targetCategory.nodes = []
                        }
                        var subNode = {
                            id: subcategory.id,
                            categoryName: subcategory.categoryName
                        };
                        targetCategory.nodes.push(subNode);
                        addNodes(subNode);
                    }

                });
            }

            function walk(topCategories, data) {
                if (!topCategories) {
                    return;
                }
                $.each(topCategories, function (topIndex, topCategory) {
                    if (topIndex == 0) {
                        var obj = {
                            id: topCategory.id,
                            text: returnSpan(topCategory.categoryName),
                            name: topCategory.categoryName,
                            href: "#",
                            state: {
                                selected: false
                            }
                        };
                    } else {
                        obj = {
                            id: topCategory.id,
                            text: returnSpan(topCategory.categoryName),
                            name: topCategory.categoryName,
                            href: "#",
                            state: {
                                selected: false
                            }
                            //node.nodeName + " - " + (node.innerText ? node.innerText : '')
                            //tags: [node.childElementCount > 0 ? node.childElementCount + ' child elements' : '']
                        };
                    }

                    if (topCategory.nodes != null) {
                        obj.nodes = [];
                        walk(topCategory.nodes, obj.nodes);

                    }
                    data.push(obj);
                });
            }

            walk(topCategories, data);
            var options = {
                levels: 2,
                data: data,
                expandIcon: 'icon-plus',
                collapseIcon: 'icon-minus',
                checkedIcon: 'icon-check',
                uncheckedIcon: 'icon-unchecked'
            };
            if (options.data[0]) {
                options.data[0].state.selected = true;
                $('#nodeId').val(options.data[0].id);
                $("#nodeText").val(options.data[0].name);
            }
            /*            var url = location.search;
             var urlVal = 0;
             var urlNodeVal = null;
             if(url.indexOf("nodeId") > 0){
             var  _urlVal = Number(url.split("=")[1]);
             for(var i = 0; i < options.data.length; i++){
             if(options.data[i].id == _urlVal){
             urlVal = i;
             break;
             }

             if(options.data[i].nodes){
             for(var j = 0; j < options.data[i].nodes.length; j++ ){
             if(options.data[i].nodes[j].id == _urlVal){
             urlVal = i;
             urlNodeVal = j;
             break;
             }
             }
             }
             }
             }
             if( urlNodeVal != null){
             if( options.data[urlVal]){
             options.data[urlVal].nodes[urlNodeVal].state.selected = true;
             $('#nodeId').val(options.data[urlVal].nodes[urlNodeVal].id);
             buildTable(options.data[urlVal].nodes[urlNodeVal].id)
             }
             }else{
             if( options.data[urlVal]){
             options.data[urlVal].state.selected = true;
             $('#nodeId').val(options.data[urlVal].id);
             }
             }*/


            treeView = $('#treeview').treeview(options);
            $('#treeview').on('nodeSelected', function (event, data) {
                //$('input[type!=radio]').iCheck("uncheck");
                $('#nodeText').val(data.name);
                var param = {"id": data.id};
                categoryId = data.id;
                DATATABLE.ajax.reload();
                $('#nodeId').val(data.id);
                $('#categoryNameUpdate').val(data.name);
            });
            $('#treeview').on('nodeUnselected', function (event, data) {
                categoryId = 0;
                buildTable();
                $('#nodeId').val("");
                $('#nodeText').val("");
                $('#categoryNameUpdate').val("");

            });

            //默认渲染url参数指定的考试分类没有指定则默认选中第一个考试类别下的试卷
            var urlCategoryId = getUrlParam("categoryId");
            if (urlCategoryId) {
                categoryId = urlCategoryId;
                $('#treeview').treeview('uncheckAll');
                var allNodes = $('#treeview').treeview('getUnchecked');
                //循环所有节点
                $.each(allNodes, function (i, node) {
                    if (node.id == urlCategoryId) {
                        $('#treeview').treeview('selectNode', node.nodeId);//获取·check状态
                    }
                });

            } else {
                if (topCategories.length > 0) {
                    var categoriyId = topCategories[0].id;
                    if (!$.fn.DataTable.isDataTable('#tb')) {//判断表格是否已经完成初始化
                        if (topCategories[0].categoryName != null) {
                            $('#categoryNameUpdate').val(topCategories[0].categoryName);
                        }
                        //默认选中第一个
                        $('#treeview').treeview('selectNode', 0);
                        categoryId = categoriyId;
                    }
                }
            }
            buildTable();

        },
        error: function (xhr, r, e) {
            alert(e);
        }
    });
}

function addTestPaperCategory() {
    var parentNode = $('#nodeText').val();
    if (parentNode == "") {
        $("#preNode").attr("style", "display:none");
        $("#parentNode").attr("style", "display:none");
    }
    $("#parentNode")[0].innerHTML = parentNode;
    $('#categoryName').val('');
    $('#categoryAddModal').modal({
        show: true,
        keyboard: false,
        backdrop: 'static'
    })
    //延迟添加focus时间
    setTimeout(function () {
        $('#categoryName').focus();
    }, 500);
}

function toDelCategory() {
    var nodeId = $('#nodeId').val();
    if (nodeId == 0 || nodeId == "") {
        showOkModal('提示框', '请先选择分类!');
        return false;
    }
    showConfirmModal('确定要删除吗？', delCategory);
}

//删除分类
function delCategory() {
    var nodeId = $('#nodeId').val();
    $.ajax({
        url: "/zxks/rest/testPaperCategory/category",
        type: "DELETE",
        contentType: "application/json; charset=UTF-8",
        async: true,
        dataType: "json",
        headers: {userId: 118},
        data: JSON.stringify({id: nodeId}),
        success: function (resultData) {
            if (resultData != null) {
                showOkModal('提示框', resultData.message);
            }
            buildDomTree();
            $('#nodeId').val("");
        },
        error: function (xhr, r, e) {
            alert(e);
        }
    });
}

//添加分类
function addCategory() {
    var nodeId = $('#nodeId').val();
    var categoryName = $('#categoryName').val();

    if ($.trim(categoryName) == "") {
        showOkModal('提示框', '请填写类别名称!');
        $('#categoryName').focus();
        return false;
    }
    if ($.trim(categoryName).length > 25) {
        showOkModal('提示框', '类别名称长度不能大于25!');
        $('#categoryName').focus();
        return false;
    }
    if (nodeId == "") {//代表其是父类别
        nodeId = "0";
    }
    $.ajax({
        url: "/zxks/rest/testPaperCategory/category",
        type: "POST",
        contentType: "application/json; charset=UTF-8",
        async: true,
        dataType: "json",
        headers: {userId: 118},
        data: JSON.stringify({"testPaperCategory": {"parentId": nodeId, "categoryName": categoryName}}),
        success: function (resultData) {
            if (resultData != null) {
                showOkModal('提示框', resultData.message);
            }
            buildDomTree();//重新加载分类
            if (resultData.resultCode == 'SUCCESS') {
                $('#nodeId').val("");
                $('#categoryName').val("");
                $('#categoryAddModal').modal('hide');
            }
        },
        error: function (xhr, r, e) {
            alert(e);
            $('#nodeId').val("");
            $('#categoryName').val("");
            $('#categoryAddModal').modal('hide');
        }
    });
}

//显示分类更新模态框
function showCategoryUpdateModal() {
    var seletNode = $("#treeview").treeview('getSelected');
    if (seletNode < 1) {
        showOkModal('提示框', '请先选择分类!');
        return false;
    }
    //暂时注释 二期代码 勿删
    /*var parentNode = $("#treeview").treeview('getParent', seletNode[0].nodeId);
    //父节点存在
    if (parentNode.nodeId != null && parentNode.nodeId != undefined) {
        $("#parentNameUpdate").val(parentNode.name);
        $("#parentUpdateDiv").show();
    } else {
        $("#parentNameUpdate").val('');
        $("#parentUpdateDiv").hide();
    }*/
    $("#categoryNameUpdate").val(seletNode[0].name);
    $('#categoryUpdateModal').modal(
        {
            show: true,
            keyboard: false,
            backdrop: 'static'
        }
    );
}

//更新分类
function updateCategory() {
    var seletNode = $("#treeview").treeview('getSelected');
    if (seletNode.length == 1) {
        var nodeId = seletNode[0].id;
        var parentNode = $("#treeview").treeview('getParent', seletNode[0].nodeId);
        var parentNodeId;
        if (parentNode.nodeId == null) {
            parentNodeId = 0;
        } else {
            parentNodeId = parentNode.id;
        }
        var nodeName = $('#nodeText').val();
        var categoryName = $('#categoryNameUpdate').val();
        if ($.trim(categoryName) == "") {
            showOkModal('提示框', '请填写类别名称!');
            $('#categoryNameUpdate').focus();
            return false;
        }
        if ($.trim(categoryName).length > 20) {
            showOkModal('提示框', '类别名称长度不能大于20!');
            $('#categoryNameUpdate').focus();
            return false;
        }
        if (nodeName == categoryName) {
            showOkModal("提示框", "分类名称未做修改!");
            $('#categoryNameUpdate').focus();
            return false;
        }
        $.ajax({
            url: "/zxks/rest/testPaperCategory/category",
            type: "PUT",
            contentType: "application/json; charset=UTF-8",
            async: true,
            dataType: "json",
            headers: {userId: 118},
            data: JSON.stringify({
                "testPaperCategory": {
                    "id": nodeId,
                    "parentId": parentNodeId,
                    "categoryName": categoryName
                }
            }),
            success: function (resultData) {
                if (resultData != null) {
                    showOkModal('提示框', resultData.message);
                    if (resultData.resultCode == 'SUCCESS') {
                        $('#nodeId').val("");
                        $('#categoryUpdateModal').modal('hide');
                        buildDomTree();//重新加载分类
                        setTimeout(function () {
                            $('#categoryNameUpdate').val("");
                        }, 100);
                    }
                }

            },
            error: function (xhr, r, e) {
                alert(e);
                $('#nodeId').val("");
                $('#categoryNameUpdate').val("");
                $('#categoryUpdateModal').modal('hide');
            }
        });
    }

}

/*******************************************试卷相关操作js**************************************************/

//修改试卷信息的id 全局变量
var updateTestpaperId = 0;

var DATATABLE = null;
var table = null;
var categoryId = 0;

function buildTable() {
    $('input[type!=radio]').iCheck("uncheck");//重置icheck为为选中状态
    if ($.fn.DataTable.isDataTable('#tb')) {//判断表格是否已经完成初始化
        DATATABLE.destroy();
    }
    DATATABLE = $('#tb').DataTable({
        "language": {
            "sSearchPlaceholder": "请输入试卷名称"
        },
        "emptyTable": "没有数据",
        "bDestroy": true,
        //"bProcessing" : true,//是否显示等待提示
        "bServerSide": true,//服务器模式（重要）
        "sAjaxSource": "../rest/testPaper/paperList",
        /*"pageLength" : 10,*/
        "columns": [
            {
                "data": "id", "sClass": 'center', 'width': '4%',
                render: function (data, type, row, meta) {
                    return "<input type='checkbox' class='paperCheckbox' name='testPaperCb' value='" + data + "'>";
                }
            },
            {
                "data": "testpaperName",
                'sClass': 'columnLeft',
                'width': '10%',
                render: function (data, type, row, meta) {
                    return returnSpan(data);
                }
            },
            {
                "data": "categoryName",
                'sClass': 'columnLeft',
                'width': '12%',
                render: function (data, type, row, meta) {
                    return returnSpan(data);
                }
            },
            {
                "data": "testpaperDescription",
                'sClass': 'columnLeft',
                'width': '10%',
                render: function (data, type, row, meta) {
                    return returnSpan(data);
                }
            },
            {
                "data": "testpaperExplain",
                'sClass': 'columnLeft',
                'width': '10%',
                render: function (data, type, row, meta) {
                    return returnSpan(data);
                }
            },
            {
                "data": "createTime", 'width': '10%', render: function (data, type, row, meta) {
                return new Date(data).format('yyyy-MM-dd hh:mm:ss');
            }
            },
            {
                "data": "updateTime", 'width': '10%', render: function (data, type, row, meta) {
                return new Date(data).format('yyyy-MM-dd hh:mm:ss');
            }
            }
        ],
        "aoColumnDefs": [
            {
                targets: [0, 1, 2, 3, 4, 7],//禁止排序
                orderable: false
            }, {
                targets: 7,
                'sClass': 'columnLeft', 'width': '7%',
                render: function (data, type, row, meta) {
                    var str = '<button class="showPaperBtn" title="查看试卷" data-toggle="modal" onclick="showPaper(' + meta.row + ')"></button> ';
                    str += '<button class="editPaperBtn"title="修改试卷" data-toggle="modal" onclick="toUpdatePaper(' + meta.row + ')"></button> ';
                    str += '<button class="deletePaperBtn"title="删除试卷" data-toggle="modal" onclick="toDelPaper(' + row.id + ')"></button> ';
                    return str;
                }
            }],
        "aaSorting": [
            [0, null]//第一列排序图标改为默认
        ],
        "fnServerData": function (sSource, aoData, fnCallback) {
            console.log(convertToPagingEntity(aoData));
            $.ajax({
                "type": 'POST',
                "contentType": "application/json",
                "url": sSource,
                "dataType": "json",
                "headers": {userId: 118},
                "data": JSON.stringify({
                    id: categoryId,
                    paging: convertToPagingEntity(aoData)
                }),
                "success": function (resp) {
                    fnCallback(resp);
                }
            });
        },
        "fnRowCallback": function (nRow, aData, iDisplayIndex) {     // 当创建了行，但还未绘制到屏幕上的时候调用，通常用于改变行的class风格
            $('td:eq(0)', nRow).attr('style', 'text-align:center');
            $('td:eq(1)', nRow).html($.testPaperPublic.returnSpan(aData.testpaperName));
            $('td:eq(3)', nRow).html($.testPaperPublic.returnSpan(aData.testpaperDescription));
            $('td:eq(4)', nRow).html($.testPaperPublic.returnSpan(aData.testpaperExplain));
            return nRow;
        }
    });
    $('#tb').on('draw.dt', function () {
        initCheckBox();
        $("#checkBoxAllCheckBox").on("ifChecked", function (event) {
            $('input[type!=radio]').iCheck("check");
        });
        $("#checkBoxAllCheckBox").on("ifUnchecked", function (event) {
            $('input[type!=radio]').iCheck("uncheck");
        });
        $('#checkBoxAllCheckBox').iCheck("uncheck");
        var checkboxes = $("input.paperCheckbox");
        checkboxes.on("ifChanged", function (event) {
            if (checkboxes.filter(':checked').length == checkboxes.length) {
                $("#checkBoxAllCheckBox").prop("checked", "checked");
            } else {
                $("#checkBoxAllCheckBox").removeProp("checked");
            }
            $("#checkBoxAllCheckBox").iCheck("update");
        });
        if ($("#checkBoxAllCheckBox").is(":checked")) {
            $('input[type!=radio]').iCheck("check");
        } else {
            $('input[type!=radio]').iCheck("uncheck");
        }
    });
}

var delPaperId;

function toDelPaper(id) {
    delPaperId = id;
    showConfirmModal('确定要删除吗？', delPaper);
}

function delPaper() {
    $.ajax({
        url: "/zxks/rest/testPaper/paper",
        type: "DELETE",
        contentType: "application/json; charset=UTF-8",
        async: true,
        dataType: "json",
        headers: {userId: 118},
        data: JSON.stringify({id: delPaperId}),
        success: function (resultData) {
            if (resultData != null) {
                showOkModal('提示框', resultData.message);
            }
            $('#tb').DataTable().ajax.reload();
        },
        error: function (xhr, r, e) {
            alert(e);
        }
    });
}

var paperIdList = [];

function toDeletePaperByList() {
    paperIdList = [];
    $("input[class='paperCheckbox']:checkbox").each(function () {
        if (true == $(this).is(':checked')) {
            paperIdList.push(Number($(this).val()));
        }
    });
    if (paperIdList.length == 0) {
        /*$('#warnTip').text("请选择需要删除的试卷！");
         $('#warnModal').modal('show');*/
        showOkModal('提示框', '请选择需要删除的试卷');
        return;
    }

    /*$('#sureTip').text('确定要删除吗？');
     $('#btnExecute').attr('onclick','deletePaperByList()');
     $('#sureModal').modal('show');*/
    showConfirmModal('确定要删除吗?', deletePaperByList);
}

//批量删除试卷
function deletePaperByList() {
    $.ajax({
        url: "/zxks/rest/testPaper/paperBatch",
        type: "DELETE",
        contentType: "application/json; charset=UTF-8",
        async: true,
        dataType: "json",
        headers: {userId: 118},
        data: JSON.stringify({"testPaperIdList": paperIdList}),
        success: function (resultData) {
            if (resultData != null) {
                showOkModal('提示框', resultData.message);
                if (resultData.resultCode == 'SUCCESS') {
                    paperIdList = [];
                    //$("#checkBoxAllCheckBox").iCheck("unCheck");
                    $('#tb').DataTable().ajax.reload();
                }
            }

        },
        error: function (xhr, r, e) {
            alert(e);
        }
    });

}

function showAddTestpaperModal() {
    var _nodeIdVal = $("#nodeId").val();
    window.location.href = "createTestpaper.jsp?nodeId=" + _nodeIdVal;
}

//显示创建试卷模态框
function showAddTestpaperModal2() {
    $.ajax({
        url: "/zxks/rest/testPaperCategory/categoryList",
        type: "get",
        contentType: "application/json; charset=UTF-8",
        async: true,
        dataType: "json",
        headers: {userId: 118},
        data: {},
        success: function (resultData) {
            categories = resultData.data;
            var optionHtml = "";
            $.each(categories, function (index, category) {
                //添加下拉框
                optionHtml += "<option value='" + category.id + "'>" + category.categoryName + "</option>";
            });

            //$('#itemBankCategorySelect').toggleClass('selectpicker show-tick form-control');

            //清空之前的值
            $('#testpaperName').val('');
            $('#testpaperDescription').val('');
            $('#testpaperExplain').val('');
            $('#testpaperModeSelect').val(1);
            $('#testPaperCategorySelect').text('');
            $('#testPaperCategorySelect').append(optionHtml);//考试类别
            $('#testpaperTypeSelect').val(0);
            $('#markTotal').val('');
            $('#testTime').val('');
            $('#itemSequenceSelect').val(1);
            $('#answerSequenceSelect').val(1);
            $('#testPaperAddModal').modal(
                {
                    show: true,
                    keyboard: false,
                    backdrop: 'static'
                }
            );
        },
        error: function (xhr, r, e) {
            alert(e);
        }
    });
}

function addTestPaper() {
    var testpaperName = $('#testpaperName').val();//试卷名称
    var testpaperDescription = $('#testpaperDescription').val();//试卷描述
    var testpaperExplain = $('#testpaperExplain').val();//试卷说明
    var testpaperModeSelect = $('#testpaperModeSelect option:selected').val();//出题方式
    var testPaperCategorySelect = $('#testPaperCategorySelect option:selected').val();//考试类别
    var testpaperTypeSelect = $('#testpaperTypeSelect option:selected').val();//试卷类型
    var markTotal = $('#markTotal').val();//试卷总分值
    var testTime = $('#testTime').val();//考试时长
    var itemSequenceSelect = $('#itemSequenceSelect option:selected').val();//题目顺序
    var answerSequenceSelect = $('#answerSequenceSelect option:selected').val();//选项顺序

    if ($.trim(testpaperName) == "") {
        alert("请输入试卷名称!");
        return false;
    }
    if (markTotal == "") {
        alert("请输入试卷总分值!");
        return false;
    }
    if (testTime == "") {
        alert("请输入考试时长!");
        return false;
    }
    $.ajax({
        url: "/zxks/rest/testPaper/paper",
        type: "POST",
        contentType: "application/json; charset=UTF-8",
        async: true,
        dataType: "json",
        headers: {userId: 118},
        data: JSON.stringify({
            "testPaper": {
                "testpaperName": testpaperName,
                "testpaperDescription": testpaperDescription,
                "testpaperExplain": testpaperExplain,
                "testpaperMode": testpaperModeSelect,
                "categoryId": testPaperCategorySelect,
                "testpaperType": testpaperTypeSelect,
                "markTotal": markTotal,
                "testTime": testTime,
                "itemSequence": itemSequenceSelect,
                "answerSequence": answerSequenceSelect
            }
        }),
        success: function (resultData) {
            if (resultData != null) {
                alert(resultData.message);
            }
            $('#nodeId').val("");
            $('#nodeText').val("");
            $('#testpaperName').val("");
            $('#testPaperAddModal').modal('hide');
            $('#tb').DataTable().ajax.reload();//刷新考试数据表格
        },
        error: function (xhr, r, e) {
            alert(e);
            $('#nodeId').val("");
            $('#nodeText').val("");
            $('#categoryName').val("");
            $('#itemBankInfoAddModal').modal('hide');
        }
    });
}

//修改试卷
function toUpdatePaper(rowNum) {
    var updateRow = $('#tb').DataTable().data()[rowNum];
    //var _nodeIdVal = $("#nodeId").val();
    $.ajax({
        url: "/zxks/rest/testPaper/exist",
        type: "post",
        contentType: "application/json; charset=UTF-8",
        async: true,
        dataType: "json",
        headers: {userId: 118},
        data: JSON.stringify({id: updateRow.id}),
        success: function (resultData) {
            if (resultData.data != null && resultData.data.length > 0) {
                showOkModal('提示框', "存在相应考试，不可修改试卷信息！");
                return false;
            } else {
                window.location.href = "updateTestpaper.jsp?id=" + updateRow.id;
            }

        },
        error: function (xhr, r, e) {
            alert(e);
        }
    });
}

//显示试卷
function showPaper(rowNum) {
    var updateRow = $('#tb').DataTable().data()[rowNum];
    window.location.href = "updateTestpaper.jsp?id=" + updateRow.id + "&onlyShow=" + 1;
}

//打开试卷修改窗口
function toUpdatePaper2(rowNum) {
    var updateRow = $('#tb').DataTable().data()[rowNum];
    var categories = [];
    $.ajax({
        url: "/zxks/rest/testPaperCategory/categoryList",
        type: "get",
        contentType: "application/json; charset=UTF-8",
        async: true,
        dataType: "json",
        headers: {userId: 118},
        data: {},
        success: function (resultData) {
            categories = resultData.data;
            var optionHtml = "";
            $.each(categories, function (index, category) {
                if (category == updateRow.categoryId) {
                    //添加下拉框
                    optionHtml += "<option value='" + category.id + "'selected='selected'>" + category.categoryName + "</option>";
                } else {
                    optionHtml += "<option value='" + category.id + "'>" + category.categoryName + "</option>";
                }
            });
            $('#updateTestpaperName').val(updateRow.testpaperName);
            $('#updateTestpaperDescription').val(updateRow.testpaperDescription);
            $('#updateTestpaperExplain').val(updateRow.testpaperExplain);
            $('#updateTestpaperModeSelect').val(updateRow.testpaperMode);
            $('#updateTestPaperCategorySelect').text('');//先清空
            $('#updateTestPaperCategorySelect').append(optionHtml);//考试类别
            $('#updateTestpaperTypeSelect').val(updateRow.testpaperType);//试卷类型
            $('#updateMarkTotal').val(updateRow.markTotal);//试卷总分
            $('#updateTestTime').val(updateRow.testTime);//考试时长
            $('#updateItemSequenceSelect').val(updateRow.itemSequence);//题目顺序
            $('#updateAnswerSequenceSelect').val(updateRow.answerSequence);//选项顺序

            //$('#itemBankCategorySelect').toggleClass('selectpicker show-tick form-control');
            updateTestpaperId = updateRow.id;
            $('#testPaperUpdateModal').modal(
                {
                    show: true,
                    keyboard: false,
                    backdrop: 'static'
                }
            );
        },
        error: function (xhr, r, e) {
            alert(e);
        }
    });
}

//修改试卷信息
function updateTestPaper() {
    var testpaperId = updateTestpaperId;
    var testpaperName = $('#updateTestpaperName').val();//试卷名称
    var testpaperDescription = $('#updateTestpaperDescription').val();//试卷描述
    var testpaperExplain = $('#updateTestpaperExplain').val();//试卷说明
    var testpaperModeSelect = $('#updateTestpaperModeSelect option:selected').val();//出题方式
    var testPaperCategorySelect = $('#updateTestPaperCategorySelect option:selected').val();//考试类别
    var testpaperTypeSelect = $('#updateTestpaperTypeSelect option:selected').val();//试卷类型
    var markTotal = $('#updateMarkTotal').val();//试卷总分值
    var testTime = $('#updateTestTime').val();//考试时长
    var itemSequenceSelect = $('#updateItemSequenceSelect option:selected').val();//题目顺序
    var answerSequenceSelect = $('#updateAnswerSequenceSelect option:selected').val();//选项顺序

    if (testpaperName == "") {
        alert("请输入试卷名称!");
        return false;
    }
    if (markTotal == "") {
        alert("请输入试卷总分值!");
        return false;
    }
    if (testTime == "") {
        alert("请输入考试时长!");
        return false;
    }
    $.ajax({
        url: "/zxks/rest/testPaper/paper",
        type: "PUT",
        contentType: "application/json; charset=UTF-8",
        async: true,
        dataType: "json",
        headers: {userId: 118},
        data: JSON.stringify({
            "testPaper": {
                "id": testpaperId,
                "testpaperName": testpaperName,
                "testpaperDescription": testpaperDescription,
                "testpaperExplain": testpaperExplain,
                "testpaperMode": testpaperModeSelect,
                "categoryId": testPaperCategorySelect,
                "testpaperType": testpaperTypeSelect,
                "markTotal": markTotal,
                "testTime": testTime,
                "itemSequence": itemSequenceSelect,
                "answerSequence": answerSequenceSelect
            }
        }),
        success: function (resultData) {
            if (resultData != null) {
                alert(resultData.message);
            }
            $('#nodeId').val("");
            updateTestpaperId = 0;
            $('#testPaperUpdateModal').modal('hide');
            $('#tb').DataTable().ajax.reload();//刷新考试数据表格
        },
        error: function (xhr, r, e) {
            alert(e);
            $('#nodeId').val("");
        }
    });
}

function convertToPagingEntity(aoData) {
    var pagingEntity = new Object();
    $.each(aoData, function (i, val) {
        if (val.name == "iDisplayStart") {
            pagingEntity.startIndex = val.value;
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

function initCheckBox() {
    $('input[type!=radio]').iCheck({
        checkboxClass: 'icheckbox_square-blue',  // 注意square和blue的对应关系
        radioClass: 'iradio_square-blue',
        increaseArea: '20%' // optional
    })
}

//获取url参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");//构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);//匹配目标参数
    if (r != null) return unescape(r[2]);
    return null;//返回参数值
}
