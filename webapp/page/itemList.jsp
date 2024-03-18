<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <link href="../images/favicon.ico" rel="icon" type="image/x-icon"/>
    <meta charset="UTF-8">
    <title>在线考试系统--题目管理</title>
    <link rel="stylesheet" type="text/css" href="../css/basic.css">
    <link rel="stylesheet" type="text/css" href="../css/base.css">
    <link rel="stylesheet" type="text/css" href="../exts/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="../exts/bootstrap/css/bootstrap-modal.css">
    <link rel="stylesheet" type="text/css" href="../exts/datatables/css/jquery.dataTables.css">
    <link rel="stylesheet" type="text/css" href="../css/itemList.css">
    <script type="text/javascript" charset="utf8" src="../exts/jquery-1.12.3.min.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/datatables/js/jquery.dataTables.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/bootstrap/js/bootstrap-modal.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/bootstrap/js/bootstrap-modalmanager.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/modalTemplate.js"></script>


    <link rel="stylesheet" type="text/css" href="../css/bootstrap-treeview.css">
    <link rel="stylesheet" type="text/css" href="../css/square/blue.css">
    <link rel="stylesheet" type="text/css" href="../css/itemBankManage.css">
    <link rel="stylesheet" type="text/css" href="../css/testPaper/testPaperManage.css">
    <script type="application/javascript" src="../exts/bootstrap-treeview.js"></script>
    <script type="application/javascript" src="../exts/icheck.min.js"></script>
    <script type="application/javascript" src="../exts/util.js"></script>
    <script type="text/javascript" src="../exts/bootstrap-filestyle.min.js"> </script>

    <script type="application/javascript">
        var optionNames = ['A', 'B', 'C', 'D', 'E', 'F'];
        var userId = <%=request.getSession().getAttribute("userId")%>;//用户ID
        //URL截取
        function getQueryString(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) {
                return unescape(r[2]);
            }
            return null;
        }
        $(function () {
            $("#confirmModal").draggable();
            $('#itemCategoryId').val(getQueryString("itemCategoryId"));
            $('#itemBankId').val(getQueryString("itemBankId"));
            $('#itemBankName').text((getQueryString("itemBankName")));
        });

        function returnSpan(targetStr) {
            var name = "";
            if (targetStr.length > 70) {
                name = targetStr.substr(0,70) + "...";
            } else {
                name = targetStr;
            }
            return '<span style="cursor:pointer;white-space: pre-wrap" title="' + targetStr + '" class="ellipsis" >' + name + '</span>';
        }

        //显示添加题目模态框
        function showItemAddModal() {
            $('#itemTypeSelect').val(1);//初始化题型
            $('#itemTitle').val('');//清空题干
            $('#itemCode').val('');//清空题目代码
            $('#answerSolution').val('');//清空答案解析
            changeType(1);//加载添加题目选项
            //延迟添加focus事件
            setTimeout(function () {
                $('#itemTitle').focus();
            }, 1000);
            setTimeout(function(){
                $('#itemAddModal').modal(
                        {
                            show: true,
                            keyboard: false,
                            backdrop: 'static'
                        });
            },200);
        }

        //添加题目信息
        function addItemInfo() {
            var itemOptions = [];
            var itemId = new Date().getTime();//获取时间戳作为题目编号
            var itemBankId = $('#itemBankId').val();
            var itemType = $('#itemTypeSelect option:selected').val();
            var itemTitle = $('#itemTitle').val().trim();
            if (itemTitle == '') {
                showOkModal('提示框', '请填写题干内容!');
                $('#itemTitle').focus();
                return false;
            }
            var itemCode = $('#itemCode').val().trim();
            var elements = document.getElementsByName("optionRadio");
            var contentWarn;
            var contents = [];//创建内容数组便于重复校验使用
            var flag = false;
            $.each(elements, function (index, element) {
                var optionId = element.value;
                var content = $('#optionInput' + optionId).val();
                content = content.trim();
                if ($.inArray(content, contents) > -1) {
                    flag = true;
                    return;
                } else {
                    contents.push(content);
                }
                if (content == '') {
                    contentWarn = true;
                    return;
                }
                itemOptions.push({
                    "optionId": optionId,
                    "content": content,
                    "itemId": itemId,
                    "creator": userId
                });
            });
            if (contentWarn) {
                showOkModal('提示框', '请填写全部选项内容!');
                return false;
            }
            if (flag) {
                showOkModal('提示框', '选项存在重复!');
                return false;
            }
            var answer = '';
            if (itemType == 2) {
                $('input[type="checkbox"][name="optionRadio"]:checked').each(function (index, element) {
                    answer += $(element).val() + ",";
                });
                if (answer.indexOf(",") > -1) {
                    answer = answer.substr(0, answer.lastIndexOf(","));
                }
                if (answer.indexOf(",") == -1) {
                    showOkModal('提示框', '请选择2个以上答案!');
                    return false;
                }
            } else if (itemType == 4) {
                answer = $('#optionInput1').val();
                if (answer == undefined) {
                    showAlert('addItemWarn', '请填写答案!');
                    return false;
                }
            } else if (itemType == 5) {
                var inputs = $("input[name='optionInput']");
                if (inputs.length == 0) {
                    showAlert('addItemWarn', '请添加填空项!');
                    return false;
                }
                var answerArr = [];
                var tag = true;
                $.each(inputs, function (i, input) {
                    var oneAnswer = $(this).val();
                    if (oneAnswer.trim() == '') {
                        showAlert('addItemWarn', '填空项答案不能为空!');
                        tag = false;
                        return false;
                    }
                    answerArr.push(oneAnswer);
                });
                if (!tag) {
                    return;
                }
                console.log(itemTitle.split("(░░)").length - 1)
                var titleTagLength = itemTitle.split("(░░)").length - 1;
                if (titleTagLength != answerArr.length) {
                    showAlert('addItemWarn', '题干填空标识数量和实际填空项数目不一致，请核实!');
                    return;
                }
                answer = answerArr.join("░");
                console.log(answer);

            } else {
                answer = $('input[type="radio"][name="optionRadio"]:checked').val();
                if (answer == undefined) {
                    showOkModal('提示框', '请选择答案!');
                    return false;
                }
            }
            var answerResolution = $('#answerSolution').val().trim();
            var item = {
                "itemId": itemId,
                "itemTitle": itemTitle,
                "itemCode": itemCode,
                "answer": answer,
                "answerResolution": answerResolution,
                "itemType": itemType,
                "itemBankId": itemBankId,
                "options": itemOptions
            };
            $.ajax({
                url: "/zxks/rest/item/itemAdd",
                type: "post",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: userId},
                data: JSON.stringify({"item": item}),
                success: function (resultData) {
                    showOkModal('提示框', resultData.message);
                    table.ajax.reload(); //刷新表格数据，分页信息重置
                    $('#itemAddModal').modal('hide');
                },
                error: function (xhr, r, e) {
                    showOkModal('提示框', e);
                }
            });
        }


        //显示批量上传模态框
        function showBatchUploadItemInfo() {
            $("#uploadingSpan").hide();
            $("#uploadBtn").removeAttr("disabled");
            $('#uploadModal').modal(
                {
                    show: true,
                    keyboard: false,
                    backdrop: 'static'
                });
        }

        function uploadItemInfo() {
            $("#uploadingSpan").show();
            $("#uploadBtn").attr("disabled", "disabled");
            var file = $('input[name="excelFile"]').get(0).files[0];
            var formData = new FormData();
            formData.append('file', file);
            $.ajax({
                url : '/zxks/rest/item/'+userId+'/'+$('#itemBankId').val()+'/batchUpload',
                type : 'POST',
                data : formData,
                cache : false,
                contentType : false,
                processData : false,
                success: function(data,s, status){
                    $("#uploadingSpan").hide();
                    $("#uploadBtn").removeAttr("disabled");
                    if(data.message == 'success'){
                        showOkModal("提示", "导入成功！");
                        $('#uploadModal').modal('hide');
                        table.ajax.reload();
                    } else {
                        showOkModal("提示", "导入失败！");
                    }
                },error: function (data, status, e){
                    alert("导入异常！");
                }
            });
        }

        var startIndex = 0;//开始页码初始化
        var count1 = 0;
        var count2 = 0;
        var count3 = 0;
        var count4 = 0;
        var count5 = 0;
        //初始化dataTable
        var dataTableJson = {
            "language": {
                "sSearchPlaceholder": "请填写题干内容"
            },
            "bServerSide": true,                    //指定从服务器端获取数据
            "bProcessing": true,                    //显示正在处理进度条
            "sAjaxSource": "../rest/item/itemList",    //服务端Rest接口访问路径
            "columns": [
                {
                    "data": "operate", 'sClass': 'itemListTd',
                    "mRender": function (data, type, full) {
                        return ''
                    }
                },
                {"data": "no", 'sClass': 'itemListTd'},
                {"data": "itemTitle", 'sClass': 'itemListTdLeft'},
                {"data": "itemType", 'sClass': 'itemListTd'},
                {"data": "updateTime", 'sClass': 'itemListTd'},
                {
                    "data": "operate", 'sClass': 'itemListTd',
                    "mRender": function (data, type, full) {
                        return '';
                    }
                }],
            "aoColumnDefs": [                                       //指定列附加属性
                {"bSortable": false, "aTargets": [0, 1, 2, 5]},        //这句话意思是第1,3（从0开始算）不能排序
                {"bSearchable": false, "aTargets": [0, 1, 4, 5]},   //bSearchable 这个属性表示是否可以全局搜索，其实在服务器端分页中是没用的
            ],
            "aaSorting": [[4, "desc"]], //默认排序
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {     // 当创建了行，但还未绘制到屏幕上的时候调用，通常用于改变行的class风格

                if (aData.itemType == 1) {
                    $('td:eq(3)', nRow).html("单选题");
                    count1 += 1;
                } else if (aData.itemType == 2) {
                    $('td:eq(3)', nRow).html("多选题");
                    count2 += 1;
                } else if (aData.itemType == 3) {
                    $('td:eq(3)', nRow).html("判断题");
                    count3 += 1;
                } else if (aData.itemType == 4) {
                    $('td:eq(3)', nRow).html("简答题");
                    count4 += 1;
                } else if (aData.itemType == 5) {
                    $('td:eq(3)', nRow).html("填空题");
                    count5 += 1;
                }
                $('td:eq(0)', nRow).html("<input type='checkbox' name='itemCheck' class='itemCheck' value=" + aData.itemId + ">");
                $('td:eq(1)', nRow).html(iDisplayIndex + startIndex + 1);
                $('td:eq(2)', nRow).html(returnSpan(aData.itemTitle,aData.itemId));
                $('td:eq(5)', nRow).append('<button class="showItemBtn" title="查看题目" data-toggle="modal" onclick="showOrEditItemInfo(1,' + aData.itemId + ')"></button> ');
                $('td:eq(5)', nRow).append('<button class="editItemBtn" title="编辑题目" data-toggle="modal" onclick="showOrEditItemInfo(2,' + aData.itemId + ')"></button> ');
                $('td:eq(5)', nRow).append('<button class="deleteItemBtn" title="删除题目" data-toggle="modal" onclick="showDeleteItemInfo(' + aData.itemId + ')"></button>');

                return nRow;
            },
            "fnServerData": function (sSource, aoData, fnCallback) {
                $.ajax({
                    "type": 'post',
                    "contentType": "application/json",
                    "url": sSource,
                    "dataType": "json",
                    "headers": {userId: userId},
                    "data": JSON.stringify({
                        item: {itemBankId: $('#itemBankId').val()},
                        paging: convertToPagingEntity(aoData)
                    }),
                    "success": function (resp) {
                        fnCallback(resp);
                    }
                });
            },
        }

        var table;
        $(document).ready(function () {
            table = $('#table_id').DataTable(dataTableJson);
            $('#table_id').on('draw.dt', function () {
                $('#reverse').iCheck({
                    checkboxClass: 'icheckbox_square-blue',  // 注意square和blue的对应关系
                    radioClass: 'iradio_square-blue',
                    increaseArea: '20%' // optional
                })

                $('input[name="itemCheck"]').iCheck({
                    checkboxClass: 'icheckbox_square-blue',  // 注意square和blue的对应关系
                    radioClass: 'iradio_square-blue',
                    increaseArea: '20%' // optional
                })

                $("#reverse").on("ifChecked", function (event) {
                    $('input.itemCheck').iCheck("check");
                });
                $("#reverse").on("ifUnchecked", function (event) {
                    $('input.itemCheck').iCheck("uncheck");
                });
                var checkboxes = $("input.itemCheck");
                $('#reverse').iCheck("uncheck");
                checkboxes.on("ifChanged", function (event) {
                    if (checkboxes.filter(':checked').length == checkboxes.length) {
                        $("#reverse").prop("checked", "checked");
                    } else {
                        $("#reverse").removeProp("checked");
                    }
                    $("#reverse").iCheck("update");
                });
            });

            $('#reverse').click(function () {
                $('input[name="itemCheck"]').prop("checked", this.checked);//全选反选
            });

        });

        //分页实体类
        function convertToPagingEntity(aoData) {
            var pagingEntity = new Object();
            $.each(aoData, function (i, val) {
                if (val.name == "iDisplayStart") {
                    pagingEntity.startIndex = val.value;
                    startIndex = val.value;
                } else if (val.name == "iDisplayLength") {
                    pagingEntity.pageSize = val.value;
                } else if (val.name == "sSearch") {
                    pagingEntity.searchValue = val.value.trim();
                } else if (val.name == "iSortCol_0") {
                    pagingEntity.sortColumn = val.value;
                } else if (val.name == "sSortDir_0") {
                    pagingEntity.sortDir = val.value;
                } else if (val.name == "sEcho") {
                    pagingEntity.sEcho = val.value;
                }
            });
            return pagingEntity;
        }

        //删除题目
        function deleteItemInfo(id) {
            id = this;//获取传入Id
            if (id != undefined) {
                $.ajax({
                    url: "/zxks/rest/item/itemDelete",
                    type: "DELETE",
                    contentType: "application/json; charset=UTF-8",
                    async: true,
                    dataType: "json",
                    headers: {userId: userId},
                    data: JSON.stringify({"id": id}),
                    success: function (resultData) {
                        if (resultData != null) {
                            showOkModal('提示框', resultData.message);
                            if (resultData.resultCode == 'SUCCESS') {
                                table.ajax.reload(null, false); //刷新表格数据，分页信息不会重置
                            }
                        }

                    },
                    error: function (xhr, r, e) {
                        showOkModal('提示框', e);
                    }
                });
            }
        }

        //显示确认删除模态框
        function showDeleteItemInfo(id) {
            showConfirmModal('您确定删除吗？', deleteItemInfo, id);
        }

        //显示批量删除确认模态框
        function showBatchDeleteItemInfo() {
            var chk_value = [];
            $('input[name="itemCheck"]:checked').each(function () {
                chk_value.push($(this).val());
            });
            if (chk_value.length > 0) {
                showConfirmModal('您确定删除吗？', batchDeleteItemInfo);
            } else {
                showOkModal('提示框', '请先选择题目!');
            }

        }

        //批量删除题目
        function batchDeleteItemInfo() {
            var chk_value = [];
            $('input[name="itemCheck"]:checked').each(function () {
                chk_value.push($(this).val());
            });
            if (chk_value.length > 0) {
                $.ajax({
                    url: "/zxks/rest/item/itemBatchDelete",
                    type: "DELETE",
                    contentType: "application/json; charset=UTF-8",
                    async: true,
                    dataType: "json",
                    headers: {userId: userId},
                    data: JSON.stringify({"itemIDList": chk_value}),
                    success: function (resultData) {
                        if (resultData != null) {
                            if (resultData.resultCode == 'SUCCESS') {
                                showOkModal('提示框', resultData.message);
                                table.ajax.reload(null, false); //刷新表格数据，分页信息不会重置
                            } else if (resultData.resultCode == 'RELATED') {
                                var orderArr = [];
                                var itemTitles = resultData.data;
                                table.data().each(function (value, index) {
                                    if (value.itemTitle) {
                                        if (itemTitles.indexOf(value.itemTitle) > -1) {
                                            orderArr.push(index + startIndex + 1);
                                        }
                                    }
                                });
                                showOkModal('提示框', "题目序号为 " + orderArr.join('，') + " 的题目已被试卷引用，无法删除！");

                            } else {
                                showOkModal('提示框', resultData.message);
                            }
                        }

                    },
                    error: function (xhr, r, e) {
                        showOkModal('提示框', e);
                    }
                });
            }
        }



        //给指定元素绑定事件（允许带参数)
        function bindEventWithArgs(target, eventName, handler, args) {
            var eventHandler = handler;
            if(args) {
                eventHander = function(e) {
                    handler.call(args, e);
                }
            }
            if(window.attachEvent) {    //IE
                target.attachEvent("on" + eventName, eventHander);
            } else {    //FF
                target.addEventListener(eventName, eventHander, false);
            }
        }

        //弹出消息提醒框
        function showOkModal(label, contennt) {
            $("#okModalLabel")[0].innerHTML = label;
            $("#okModalMessage")[0].innerHTML = contennt;
            $("#okModal").modal({
                show:true,
                keyboard:false,
                backdrop:'static'
            });
        }

        //弹出确认框并对确认按钮绑定事件
        function showConfirmModal(content, fn, param) {
            //$("#confirmModalLabel")[0].innerHTML = label;
            $("#confirmModalMessage")[0].innerHTML = content;
            $("#confirmModal").modal({
                show:true,
                keyboard:false,
                backdrop:'static'
            });
            $("#btnExecute").remove();
            $("#confirmModalFooter").append('<button type="button" id="btnExecute" class="btn btn-primary">确定</button>');
            var btn = document.getElementById("btnExecute");
            if(param==null) {
                $("#btnExecute").bind("click", eval(fn));
            } else {
                bindEventWithArgs(btn, "click", fn, param);
            }
        }

        //反转义特殊字符
        function unCape(str) {
            var c = document.createElement('div');
            c.innerHTML = str;
            str = c.innerText || c.textContent;
            c = null;
            return str
        }


        //查看题目信息type:1查看,2编辑
        function showOrEditItemInfo(type, id) {
            if (id != undefined) {
                var existTestPaper = false;
                if (type == 2) {
                    $.ajax({
                        url: "/zxks/rest/item/getTestPaperByItemId",
                        type: "POST",
                        contentType: "application/json; charset=UTF-8",
                        async: false,
                        dataType: "json",
                        headers: {userId: userId},
                        data: JSON.stringify({"id": id}),
                        success: function (resultData) {
                            if (resultData != null) {
                                if (resultData.resultCode == 'SUCCESS') {
                                    var data = resultData.data;
                                    if (data.length > 0) {
                                        existTestPaper = true;
                                        return;
                                    }
                                }
                            }
                        }
                    })
                }
                if (existTestPaper) {
                    showOkModal('提示框', "该题目已被试卷引用，无法修改，请核实!");
                    return;
                }

                $.ajax({
                    url: "/zxks/rest/item/itemInfo",
                    type: "POST",
                    contentType: "application/json; charset=UTF-8",
                    async: true,
                    dataType: "json",
                    headers: {userId: userId},
                    data: JSON.stringify({"id": id}),
                    success: function (resultData) {
                        if (resultData != null) {
                            var item = resultData.data[0];
                            $('#itemTypeShowSelect').val(item.itemType);
                            $('#itemTitleShow').val(unCape(item.itemTitle));

                            $('#itemCodeShow').val(item.itemCode);
                            var answerResolution = item.answerResolution;
                            if(answerResolution!="null"){
                                $('#answerSolutionShow').val(item.answerResolution);
                            }
                            var answer = item.answer;
                            var options = item.optionList;
                            var optionHtml = '';
                            $.each(options, function (i, option) {
                                var optionType = 'radio';
                                var checkedStr = '';
                                var addOptionBtnDiv = '';
                                var inputLength = '530px';
                                var num = option.optionId;
                                if (type == 2) {
                                    if (item.itemType != 4) {
                                        addOptionBtnDiv = '<a href="#" class="optionA" name="optionShowA" id="optionShowA' + num + '" onclick="changeShowOption(' + num + ')">&times;</a>';
                                    }
                                    inputLength = '500px';
                                    //$('#showDialogName').text('编辑题目');
                                }
                                // itemType为题型，1为单选，2为多选，3为判断 4为简答 5为填空
                                if (item.itemType == 1) {
                                    $('#optionTipShow').text('正确选项个数为1');
                                    if (answer == option.optionId) {
                                        checkedStr = 'checked="checked"';
                                    }
                                    $('#addOptionBtnDivShow').attr('onclick', 'addShowOption(1);');
                                } else if (item.itemType == 2 && answer.concat(",")) {
                                    $('#optionTipShow').text('正确选项个数为2个及以上');
                                    var answers = answer.split(",");
                                    if ($.inArray('' + option.optionId + '', answers) > -1) {
                                        checkedStr = 'checked="checked"';
                                    }
                                    optionType = 'checkbox';
                                    $('#addOptionBtnDivShow').attr('onclick', 'addShowOption(2);');
                                } else if (item.itemType == 4) {
                                    var num = option.optionId;
                                    $('#optionTipShow').text('请填写正确答案');
                                } else if (item.itemType == 5) {
                                    $('#optionTipShow').text('请填写每个填空的正确答案');
                                    $('#addOptionBtnDivShow').attr('onclick', 'addShowOption(5);');
                                }
                                optionHtml += '<div style="margin-top: 0px;" name="optionShowDiv" id="optionShowDiv' + num + '">';
                                if (item.itemType == 5) {
                                    optionHtml += '<label style="margin-left: 10px;margin-right: 20px;width: 40px;line-height:30px;text-align: left;">' +
                                            '<input name="optionShowRadio" id="optionShowRadio' + num + '" type="hidden" value="' + num + '" style="margin-top: -2px;margin-right:10px;">' +
                                            '<span name="optionShowName" id="optionShowName' + num + '">' + '填空' + num + '</span></label>';

                                } else if (item.itemType == 4) {
                                    optionHtml += '<label style="margin-left: 10px;margin-right: 20px;width: 40px;line-height:30px;text-align: left;">' +
                                            '<input name="optionShowRadio" id="optionShowRadio' + num + '" type="hidden" value="' + num + '" ' + ' style="margin-top: -2px;margin-right:10px;">' +
                                            '</label>';
                                } else if (item.itemType != 3) {
                                    if (type == 1) {
                                        optionHtml += '<label style="margin:15px 20px 0 10px;width: 40px;line-height:30px;text-align: left;">' +
                                                '<input name="optionShowRadio" id="optionShowRadio' + num + '" type="' + optionType + '" value="' + num + '" ' + checkedStr + ' style="margin-top: -2px;margin-right:10px;">' +
                                                '<span name="optionShowName" id="optionShowName' + num + '">' + optionNames[num - 1] + '</span></label>';
                                                /*'<div class="col-sm-11" style="padding: 5px 5px 5px 5px;width:554px;margin:-30px 0 0 60px;background: #eee;border-radius: 4px; ">' +
                                                '<span class="form-control option" readonly="readonly" style="word-break:break-all;white-space: pre-wrap;font-family:微软雅黑;">' + option.content + '</span></div>'*/
                                    } else {
                                        optionHtml += '<label style="margin-left: 10px;margin-right: 20px;width: 40px;line-height:30px;text-align: left;">' +
                                                '<input name="optionShowRadio" id="optionShowRadio' + num + '" type="' + optionType + '" value="' + num + '" ' + checkedStr + ' style="margin-top: -2px;margin-right:10px;">' +
                                                '<span name="optionShowName" id="optionShowName' + num + '">' + optionNames[num - 1] + '</span></label>';
                                                /*'<input class="inputItem" maxlength="200" style="width: ' + inputLength + ';margin-left: 70px;margin-top: -35px;" name="optionShowInput" id="optionShowInput' + num + '" placeholder="请填写选项内容" value="' + option.content + '">' +
                                                addOptionBtnDiv + '</div>';*/
                                    }
                                } else {
                                    $('#optionTipShow').text('正确选项个数为1');
                                    if (answer == option.optionId) {
                                        checkedStr = 'checked="checked"';
                                    }
                                    $('#addOptionBtnShow').attr('style', 'margin-top: 20px;display: none');
                                    optionHtml += '<label style="margin-left: 10px;margin-right: 20px;width: 40px;line-height:30px;text-align: left;">' +
                                            '<input name="optionShowRadio" id="optionShowRadio' + num + '" type="radio" value="' + num + '" ' + checkedStr + ' style="margin-top:0;margin-right:10px;">' +
                                            '<span name="optionShowName" id="optionShowName' + num + '">' + optionNames[num - 1] + '</span></label>'
                                }
                                optionHtml += '<input class="inputItem" maxlength="200" style="width: ' + inputLength + ';margin-left: 70px;margin-top: -35px;" name="optionShowInput" id="optionShowInput' + num + '" placeholder="请填写选项内容" value="' + option.content + '">' +
                                    addOptionBtnDiv + '</div>';

                            });
                            var footerHtml = '';
                            if (type == 1) {
                                $('#showDialogName').text('查看题目');
                                $('#addOptionBtnDivShow').attr('style', 'margin-top: 20px;display: none');
                                $('#itemTypeShowSelect').attr("disabled", "disabled");
                                $('#itemTitleShow').attr('disabled', 'disabled');
                                $('#itemCodeShow').attr('disabled', 'disabled');
                                $('#answerSolutionShow').attr('disabled', 'disabled');
                                setTimeout(function () {
                                    $('input[name="optionShowInput"]').attr('disabled', 'disabled');
                                    $('input[name="optionShowRadio"]').attr('disabled', 'disabled');
                                }, 200)
                                footerHtml = '<button class="btCancle" data-dismiss="modal" onclick="hideAlert(\'addItemWarn\')">关闭</button>';
                            } else if (type == 2) {
                                $('#showDialogName').text('编辑题目');
                                $('#itemId').val(item.itemId);
                                if (item.itemType == 4) {
                                    $('#addOptionBtnDivShow').attr('style', 'margin-top: 20px;display: none');
                                } else {
                                    $('#addOptionBtnDivShow').attr('style', 'margin-top: 20px');
                                }
                                $('#itemTitleShow').removeAttr('disabled');
                                $('#itemCodeShow').removeAttr('disabled');
                                $('#answerSolutionShow').removeAttr('disabled');
                                setTimeout(function () {
                                    $('input[name="optionShowInput"]').removeAttr('disabled');
                                    $('input[name="optionShowRadio"]').removeAttr('disabled');
                                }, 200)
                                footerHtml = '<button class="btCancle" data-dismiss="modal" onclick="hideAlert(\'addItemWarn\')">取消</button>' +
                                        '&nbsp;<button class="btEnabled" onclick="updateItemInfo()">确定</button>';
                            }
                            $('#optionShowDiv').text('');
                            $('#optionShowDiv').append(optionHtml);
                            $('#itemShowFooter').text('');
                            $('#itemShowFooter').append(footerHtml);
                            //延迟添加focus事件
                            setTimeout(function () {
                                $('#itemTitleShow').focus();
                            }, 800);
                            $('#itemShowModal').modal(
                                    {
                                        show: true,
                                        keyboard: false,
                                        backdrop: 'static'
                                    }
                            );
                        }
                    },
                    error: function (xhr, r, e) {
                        showOkModal('提示框', e);
                    }
                });
            }
        }

        //更新题目信息
        function updateItemInfo() {
            var itemOptions = [];
            var itemId = $('#itemId').val();
            var itemBankId = $('#itemBankId').val();
            var itemType = $('#itemTypeShowSelect option:selected').val();
            var itemTitle = $('#itemTitleShow').val().trim();
            if (itemTitle == '') {
                showOkModal('提示框', '请填写题干内容!');
                $('#itemTitleShow').focus();
                return false;
            }
            var itemCode = $('#itemCodeShow').val().trim();
            var elements = document.getElementsByName("optionShowRadio");
            var contentWar;
            var contents = [];//创建内容数组便于重复校验使用
            var flag = false;
            $.each(elements, function (index, element) {
                var optionId = element.value;
                var content = $('#optionShowInput' + optionId).val();
                content = content.trim();
                if ($.inArray(content, contents) > -1) {
                    flag = true;
                    return;
                } else {
                    contents.push(content);
                }
                if (content == '') {
                    contentWar = true;
                    return;
                }
                itemOptions.push({
                    "optionId": optionId,
                    "content": content,
                    "itemId": itemId,
                    "creator": userId
                });
            });
            if (contentWar) {
                showOkModal('提示框', '请填写全部选项内容!');
                return false;
            }
            if (flag) {
                showOkModal('提示框', '选项存在重复!');
                return false;
            }
            var answer = '';
            if (itemType == 1 || itemType == 3) {
                answer = $('input[type="radio"][name="optionShowRadio"]:checked').val();
                if (answer == undefined) {
                    showOkModal('提示框', '请选择答案!');
                    return false;
                }
            } else if (itemType == 2) {
                $('input[type="checkbox"][name="optionShowRadio"]:checked').each(function (index, element) {
                    answer += $(element).val() + ",";
                });
                if (answer.indexOf(",") > -1) {
                    answer = answer.substr(0, answer.lastIndexOf(","));
                }
                if (answer.indexOf(",") == -1) {
                    showOkModal('提示框', '请选择2个以上答案!');
                    return false;
                }
            } else if (itemType == 4) {
                answer = $('#optionShowInput1').val();
                if (answer == undefined) {
                    showAlert('addItemWarn', '请填写答案!');
                    return false;
                }
            } else if (itemType == 5) {
                var inputs = $("input[name='optionShowInput']");
                if (inputs.length == 0) {
                    showAlert('addItemWarn', '请添加填空项!');
                    return false;
                }
                var answerArr = [];
                var tag = true;
                $.each(inputs, function (i, input) {
                    var oneAnswer = $(this).val();
                    if (oneAnswer.trim() == '') {
                        showAlert('addItemWarn', '填空项答案不能为空!');
                        tag = false;
                        return false;
                    }
                    answerArr.push(oneAnswer);
                });
                if (!tag) {
                    return;
                }
                console.log(itemTitle.split("(░░)").length - 1)
                var titleTagLength = itemTitle.split("(░░)").length - 1;
                if (titleTagLength != answerArr.length) {
                    showAlert('addItemWarn', '题干填空标识数量和填空项数目不一致!');
                    return;
                }
                answer = answerArr.join("░");
                console.log(answer);
            }
            var answerResolution = $('#answerSolutionShow').val().trim();
            var item = {
                "itemId": itemId,
                "itemTitle": itemTitle,
                "itemCode": itemCode,
                "answer": answer,
                "answerResolution": answerResolution,
                "itemType": itemType,
                "itemBankId": itemBankId,
                "options": itemOptions
            };
            $.ajax({
                url: "/zxks/rest/item/itemUpdate",
                type: "PUT",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: userId},
                data: JSON.stringify({"item": item}),
                success: function (resultData) {
                    if (resultData != null) {
                        showOkModal('提示框', resultData.message);
                        if (resultData.resultCode == 'SUCCESS') {
                            table.ajax.reload(); //刷新表格数据，分页信息不会重置
                            $('#itemShowModal').modal('hide');
                        }
                    }
                },
                error: function (xhr, r, e) {
                    showOkModal('提示框', e);
                }
            });
        }

        //题目类型更改选项
        function changeType(type) {
            var optionHtml = '';
            var nums = [1, 2, 3, 4];
            if (type == 1) {
                $('#optionTip').text('正确选项个数为1');
                $.each(nums, function (index, num) {
                    optionHtml += '<div style="margin-top: 0px;" name="optionDiv" id="optionDiv' + num + '">' +
                            '<label style="margin-left: 10px;margin-right: 20px;width: 40px;line-height:30px;text-align: left;">' +
                            '<input name="optionRadio" id="optionRadio' + num + '" type="radio" value="' + num + '" style="margin-top:0;margin-right:10px;">&nbsp;' +
                            '<span name="optionName" id="optionName' + num + '">' + optionNames[num - 1] + '</span></label>' +
                            '<input class="inputItem" maxlength="200" style="width: 500px;margin-left: 70px;margin-top: -35px;" name="optionInput" id="optionInput' + num + '" placeholder="请填写选项内容"/>' +
                            '<a href="#" class="optionA" name="optionA" id="optionA' + num + '" onclick="changeOption(' + num + ')">&times;</a>' +
                            '</div>';
                });
                $('#addOptionBtnDiv').attr('style', 'margin-top: 20px');
                $('#addOptionBtn').attr('onclick', 'addOption(1)');
                $('#optionDiv').text('');
                $('#optionDiv').append(optionHtml);
            } else if (type == 2) {
                $('#optionTip').text('正确选项个数为2个及以上');
                $.each(nums, function (index, num) {
                    optionHtml += '<div style="margin-top: 0px;" name="optionDiv" id="optionDiv' + num + '">' +
                            '<label style="margin-left: 10px;margin-right: 20px;width: 40px;line-height:30px;text-align: left;">' +
                            '<input name="optionRadio" id="optionRadio' + num + '" type="checkbox" value="' + num + '" style="margin-top: -2px;margin-right:10px;">&nbsp;' +
                            '<span name="optionName" id="optionName' + num + '">' + optionNames[num - 1] + '</span></label>' +
                            '<input class="inputItem" maxlength="200" style="width: 500px;margin-left: 70px;margin-top: -35px;" name="optionInput" id="optionInput' + num + '" placeholder="请填写选项内容"/>' +
                            '<a href="#" class="optionA" name="optionA" id="optionA' + num + '" onclick="changeOption(' + num + ')">&times;</a>' +
                            '</div>';
                });
                $('#addOptionBtnDiv').attr('style', 'margin-top: 20px');
                $('#addOptionBtn').attr('onclick', 'addOption(2)');
                $('#optionDiv').text('');
                $('#optionDiv').append(optionHtml);
            } else if (type == 3) {
                $('#optionTip').text('正确选项个数为1');
                optionHtml += '<div style="margin-top: 0px;">' +
                        '<label style="margin-left: 10px;margin-right: 20px;width: 40px;line-height:30px;text-align: left;">' +
                        '<input name="optionRadio" id="optionRadio1" type="radio" value="1" style="margin-top:0;margin-right:10px;">' +
                        'A</label>' +
                        '<input class="inputItem" maxlength="200" style="width: 530px;margin-left: 70px;margin-top: -35px;" name="optionInput" id="optionInput1" placeholder="请填写选项内容" value="对" disabled="disabled"/>' +
                        '</div>';
                optionHtml += '<div style="margin-top: 0px;">' +
                        '<label style="margin-left: 10px;margin-right: 20px;width: 40px;line-height:30px;text-align: left;">' +
                        '<input name="optionRadio" id="optionRadio2" type="radio" value="2" style="margin-top:0;margin-right:10px;">' +
                        'B</label>' +
                        '<input class="inputItem" maxlength="200" style="width: 530px;margin-left: 70px;margin-top: -35px;" name="optionInput" id="optionInput2" placeholder="请填写选项内容" value="错" disabled="disabled"/>' +
                        '</div>';
                $('#addOptionBtnDiv').attr('style', 'margin-top: 20px;display: none');
                $('#optionDiv').text('');
                $('#optionDiv').append(optionHtml);
            } else if (type == 4) {
                $('#optionTip').text('请填写正确答案');
                optionHtml += '<div style="margin-top: 0px;">' +
                        '<input name="optionRadio" id="optionRadio1" type="hidden" value="1">' +
                        '<textarea class="shortAnswer" id="optionInput1" rows="2" placeholder="请填写答案内容"></textarea>' +
                        '</div>';
                $('#addOptionBtnDiv').attr('style', 'margin-top: 20px;display: none');
                $('#optionDiv').text('');
                $('#optionDiv').append(optionHtml);
            } else if (type == 5) {
                $.each(nums, function (index, num) {
                    optionHtml += '<div style="margin-top: 0px;" name="optionDiv" id="optionDiv' + num + '">' +
                            '<label style="margin-left: 10px;margin-right: 20px;width: 40px;line-height:30px;text-align: left;">' +
                            '<span name="optionName" id="optionName' + num + '">' + optionNames[num - 1] + '</span></label>' +
                            '<input class="inputItem" style="width: 500px;margin-left: 70px;margin-top: -35px;" name="optionInput" id="optionInput' + num + '" maxlength="200" placeholder="请填写选项内容"/>' +
                            '</div>';
                });
                $('#addOptionBtnDiv').attr('style', 'margin-top: 20px');
                $('#addOptionBtn').attr('onclick', 'addOption(5)');
                $('#optionTip').text('请填写每个填空的正确答案');
                $('#optionDiv').text('');
            }
        }
        //删除选项，相应更新其他选项信息，传入选项id信息
        function changeOption(id) {
            var elements = $('input[name=optionRadio]');
            var length = elements.length;
            if (length <= 3) {
                showAlert('addOptionWarn', '最少3个选项!');
                return false;
            }
            $('#optionDiv' + id).remove();
            changeElements(id, 'id', 'optionDiv');
            changeElements(id, 'value', 'optionRadio');
            changeElements(id, 'id', 'optionRadio');
            changeElements(id, 'id', 'optionInput');
            changeElements(id, 'onclick', 'optionA');
            changeElements(id, 'id', 'optionA');
            changeElements(id, 'id', 'optionName');
        }
        //删除选项，相应更新其他选项信息
        function changeElements(value, attName, elementName) {
            var elements = $('[name=' + elementName + ']');
            $.each(elements, function (index, element) {
                var oldId = Number(index + value + 1);
                if (oldId > value) {
                    var newId = Number(oldId - 1);
                    if (attName == 'value') {
                        $('#' + elementName + oldId).val("" + newId + "");
                    } else {
                        if (attName != 'id' && elementName == 'optionA') {
                            $('#' + elementName + oldId).attr(attName, 'changeOption(' + newId + ')');
                        } else {
                            $('#' + elementName + oldId).attr(attName, elementName + newId);
                            if (elementName == 'optionName') {
                                $('#' + elementName + newId).text(optionNames[newId - 1]);
                            }
                        }
                    }
                }
            });
        }
        //添加选项，相应更新选项信息
        function addOption(type) {
            var elements = $('input[name=optionRadio]');
            var length = elements.length;
            if (length >= 6) {
                showAlert('addOptionWarn', '最多添加6个选项!');
                return false;
            }
            //填空题
            if (type == 5) {
                var elements1 = $('input[name=optionRadio]');
                var length1 = elements1.length;
                var newValue1 = length1 + 1;
                var optionHtml1 = '<div style="" name="optionDiv" id="optionDiv' + newValue + '">' +
                        '<label style="margin-left: 10px;margin-right: 20px;width: 40px;line-height:30px;text-align: left;float: left">' +
                        '<span name="optionName" id="optionName' + newValue1 + '">' + '填空' + newValue1 + '</span></label>' +
                        '<input name="optionRadio" id="optionRadio' + newValue1 + '" type="hidden" value="' + newValue1 + '" style="margin-top: -2px;margin-right:10px;">&nbsp;' +
                        '<input class="inputItem" style="width: 520px;margin-left: 70px;margin-top: -35px;" name="optionInput" id="optionInput' + newValue1 + '" placeholder="请填写填空内容"/>' +
                        '</div>';
                $('#optionDiv').append(optionHtml1);

                var titleText = $("#itemTitle").val();
                titleText += "(░░)";
                $("#itemTitle").val(titleText)
                return;
            }
            var newValue = Number($('#optionRadio' + Number(length)).val()) + 1;
            var inputType = 'radio';
            if (type == 2) {
                inputType = 'checkbox';
            }
            var optionHtml = '<div style="margin-top: 0px;" name="optionDiv" id="optionDiv' + newValue + '">' +
                    '<label style="margin-left: 10px;margin-right: 20px;width: 40px;line-height:30px;text-align: left;">' +
                    '<input name="optionRadio" id="optionRadio' + newValue + '" type="' + inputType + '" value="' + newValue + '" style="margin-top:0;margin-right:10px;">&nbsp;' +
                    '<span name="optionName" id="optionName' + newValue + '">' + optionNames[newValue - 1] + '</span></label>' +
                    '<input class="inputItem" maxlength="200" style="width: 500px;margin-left: 70px;margin-top: -35px;" name="optionInput" id="optionInput' + newValue + '" placeholder="请填写选项内容"/>' +
                    '<a href="#" class="optionA" name="optionA" id="optionA' + newValue + '" onclick="changeOption(' + newValue + ')" >&times;</a>' +
                    '</div>';
            $('#optionDiv').append(optionHtml);
        }
        //删除选项，相应更新其他选项信息，传入选项id信息
        function changeShowOption(id) {
            var elements = $('input[name=optionShowRadio]');
            var length = elements.length;
            if (length <= 3) {
                showAlert('showOptionWarn', '最少3个选项!');
                return false;
            }
            $('#optionShowDiv' + id).remove();
            changeShowElements(id, 'id', 'optionShowDiv');
            changeShowElements(id, 'value', 'optionShowRadio');
            changeShowElements(id, 'id', 'optionShowRadio');
            changeShowElements(id, 'id', 'optionShowInput');
            changeShowElements(id, 'onclick', 'optionShowA');
            changeShowElements(id, 'id', 'optionShowA');
            changeShowElements(id, 'id', 'optionShowName');
        }
        //删除选项，相应更新其他选项信息
        function changeShowElements(value, attName, elementName) {
            var elements = $('[name=' + elementName + ']');
            $.each(elements, function (index, element) {
                var oldId = Number(index + value + 1);
                if (oldId > value) {
                    var newId = Number(oldId - 1);
                    if (attName == 'value') {
                        $('#' + elementName + oldId).val("" + newId + "");
                    } else {
                        if (attName != 'id' && elementName == 'optionShowA') {
                            $('#' + elementName + oldId).attr(attName, 'changeShowOption(' + newId + ')');
                        } else {
                            $('#' + elementName + oldId).attr(attName, elementName + newId);
                            if (elementName == 'optionShowName') {
                                $('#' + elementName + newId).text(optionNames[newId - 1]);
                            }
                        }
                    }
                }
            });
        }
        //添加选项，相应更新选项信息
        function addShowOption(type) {
            //填空题
            if (type == 5) {
                var elements1 = $('input[name=optionShowRadio]');
                var length1 = elements1.length;
                var newValue1 = length1 + 1;
                var optionHtml1 = '<div style="margin-top: 0px;" name="optionDiv" id="optionDiv' + newValue + '">' +
                        '<label style="margin-left: 10px;margin-right: 20px;width: 40px;line-height:30px;text-align: left;">' +
                        '<span name="optionShowName" id="optionShowName' + newValue1 + '">' + '填空' + newValue1 + '</span></label>' +
                        '<input name="optionShowRadio" id="optionShowRadio' + newValue1 + '" type="hidden" value="' + newValue1 + '" style="margin-top: -2px;margin-right:10px;">' +
                        '<input class="inputItem" style="width: 520px;margin-left: 70px;margin-top: -35px;" name="optionShowInput" id="optionShowInput' + newValue1 + '" placeholder="请填写填空内容"/>' +
                        '</div>';
                $('#optionShowDiv').append(optionHtml1);

                var titleText = $("#itemTitleShow").val();
                titleText += "(░░)";
                $("#itemTitleShow").val(titleText)
                return;
            }
            var elements = $('input[name=optionShowRadio]');
            var length = elements.length;
            if (length >= 6) {
                showAlert('showOptionWarn', '最多添加6个选项!');
                return false;
            }
            var newValue = Number($('#optionShowRadio' + Number(length)).val()) + 1;
            var inputType = 'radio';
            if (type == 2) {
                inputType = 'checkbox';
            }
            var optionHtml = '<div style="margin-top: 0px;" name="optionShowDiv" id="optionShowDiv' + newValue + '">' +
                    '<label style="margin-left: 10px;margin-right: 20px;width: 40px;line-height:30px;text-align: left;">' +
                    '<input name="optionShowRadio" id="optionShowRadio' + newValue + '" type="' + inputType + '" value="' + newValue + '" style="margin-top:0;margin-right:10px;">' +
                    '<span name="optionShowName" id="optionShowName' + newValue + '">' + optionNames[newValue - 1] + '</span></label>' +
                    '<input class="inputItem" style="width: 500px;margin-left: 70px;margin-top: -35px;" name="optionShowInput" id="optionShowInput' + newValue + '" placeholder="请填写选项内容"/>' +
                    '<a href="#" class="optionA" name="optionShowA" id="optionShowA' + newValue + '" onclick="changeShowOption(' + newValue + ')" >&times;</a>' +
                    '</div>';
            $('#optionShowDiv').append(optionHtml);
        }
    </script>
    <style type="text/css">
        th, button, td, span, input, textarea, select, div {

            font-family: 微软雅黑 !important;
        }

        .modal-backdrop {
            background: #333 !important;
        }

        .deleteBtn {
            background-color: #da4f49;
        }

        .deleteBtn:hover {
            background-color: #bd362f !important;
        }
        #searchValue{
            margin-left: -30px !important;
        }
        input[type="password"]{
            height: 32px !important;
        }
        #update_password_modal{
            top: 30% !important;
            height:270px;
        }
        #fileStyleLabel{
            width: 100px;
            vertical-align: super;
            height: 21px;
        }
    </style>
</head>
<body>
<div id="mainDiv" style="background-color: #f1f4f8">
    <div id="headDiv">
        <jsp:include page="menu.jsp"/>
    </div>
    <div id="contentDiv" style="text-align: left;margin:20px 20px 20px 20px;">
        <%@include file="mymodal.jsp" %>
        <div style="padding:20px 20px 20px 20px;background-color:#ffffff">
            <input id="itemCategoryId" value="" style="display: none"/>
            <input id="itemBankId" value="" style="display: none"/>
            <input id="itemId" value="" style="display: none"/>

            <div id="itemDiv"></div>
            <div class="container-fluid" style="padding: 0;margin: 0;">
                <div class="col-lg-12">
                    <ol class="breadcrumb">
                        <li style="margin: 8px 0 0 20px;float: left;font-size: 16px;text-shadow: none">
                            <a href="itemBankManage.jsp" style="display: inline-block;cursor: pointer">题库</a></li>
                        <li class="active" style="margin-left: 20px;font-size: 16px;text-shadow: none">
                            <label id="" style="height: 20px;margin-left: 0px;">/</label></li>
                        <li class="active" style="margin-left: 20px;font-size: 16px;text-shadow: none">
                            <label id="itemBankName" style="height: 20px;width:300px;margin-left: -200px;"></label></li>
                    </ol>
                    <div class="btn-group" style="position: absolute;margin: 6px 0 0 -10px;z-index: 1">
                        <button type="button" class="itemBtn" onclick="showItemAddModal()">
                            添加题目
                        </button>
                        <button type="button" class="itemBtn" title="文件导入题目" onclick="showBatchUploadItemInfo()"
                                data-toggle="tooltip" data-placement="auto top" data-trigger="hover focus">
                            批量导入
                        </button>
                        <button class="itemBtn deleteBtn" onclick="showBatchDeleteItemInfo()">
                            批量删除
                        </button>
                    </div>
                    <table id="table_id" class="display">
                        <thead>
                        <tr>
                            <th width="5%" style="text-align: center"><input type="checkbox" id="reverse"/></th>
                            <th width="5%" style="text-align: center">序号</th>
                            <th width="50%" style="text-align: center">题干</th>
                            <th width="10%" style="text-align: center">题型</th>
                            <th width="20%" style="text-align: center">更新时间</th>
                            <th width="10%" style="text-align: center">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
            <!-- 添加题目模态框 -->
            <div class="modal fade hide" id="itemAddModal"
                 style="position: absolute;left: 700px;height: 854px;width: 680px;top:50%;"><!-- fade是设置淡入淡出效果 -->
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                            <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true">
                            </button>
                            添加题目
                        </div>
                        <div class="modalBody" id="modalBody"
                             style="height: 700px;overflow-y: scroll;padding: 20px 20px 20px 20px;">
                            <label class=" itemType"
                                   style="font-family: '微软雅黑';font-weight: bold;display: inline-block;width: 40px;margin-right:26px;text-align: right;"><span
                                    style="color:red;">*</span> 题型</label>
                            <select id="itemTypeSelect" class="selectItem"
                                    onchange="changeType(this.options[this.options.selectedIndex].value)">
                                <option value="1">单选题</option>
                                <option value="2">多选题</option>
                                <option value="3">判断题</option>
                                <%--<option value="4">简答题</option>--%>
                                <option value="5">填空题</option>
                            </select><br>
                            <label class="itemTitleLabel"
                                   style="font-family: '微软雅黑';font-weight: bold;display: inline-block;width: 40px;margin-right:26px;text-align: right"><span
                                    style="color:red;">*</span> 题干</label>
                            <textarea id="itemTitle" class="itemTitle" rows="3" placeholder="请填写题干内容" maxlength="400"></textarea><br>

                            <label class="itemCodeLabel"
                                   style="font-family: '微软雅黑';font-weight: bold;display: inline-block;width: 40px;margin-right:26px;text-align: right">代码</label>
                            <textarea id="itemCode" class="itemCode" rows="3" placeholder="请填写题目代码内容" maxlength="400"></textarea><br>
                            <!-- 添加题目模态框中选项说明区域 -->
                            <label class="col-sm-1 control-label"
                                   style="font-family: '微软雅黑';font-weight: bold;display: inline-block;width: 40px;margin-right:26px;margin-bottom: 0px;text-align: right"><span
                                    style="color:red;">*</span> 选项</label>
                <span class="control-label" style="color:orange;"><span id="optionTip"
                                                                                              style="font-weight: 700;">正确选项个数为1</span></span>
                            <!-- 添加题目模态框中添加选项区域 -->
                            <div id="optionDiv" style=""></div>
                            <!--警告框-->
                            <div class="alert alert-block hide" style="color: #ff6c60;margin: 0;width: 89%;"
                                 id="addOptionWarn"></div>
                            <div style="margin-top: 20px;" id='addOptionBtnDiv'>
                                <button class="sure" id="addOptionBtn" style="width:520px;margin-left: 70px;"
                                        onclick="addOption(1);">
                                    <img src="../images/add.png" style="margin-bottom: 4px;"/>&nbsp;添加一个选项</span>
                                </button>
                            </div>
                            <!-- 添加题目模态框中题目解析说明区域 -->
                            <label class="itemExplain"
                                   style="font-family: '微软雅黑';font-weight: bold;display: inline-block;width: 40px;margin-right:26px;text-align: right">解析</label>
                            <span class="labelDetail" style="color:orange;font-weight: 700;">考生/学员查答案时可显示该信息，达到学习的目的</span>
                            <textarea class="explainArea" id="answerSolution" rows="3" placeholder="请填写解析内容" maxlength="500"></textarea>
                        </div>
                        <!--警告框-->
                        <div class="alert alert-block hide"
                             style="color: #ff6c60;margin: 0;width: 66%;float: left;position: absolute" id="addItemWarn"></div>
                        <div class="modal-footer">
                            <button class="cancel" data-dismiss="modal" style="width:70px;margin-right: 0px"
                                    onclick="hideAlert('addItemWarn')">取消
                            </button>
                            <button class="sure" onclick="addItemInfo()">确定</button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 查看或编辑题目模态框 -->
            <div class="modal fade hide" id="itemShowModal"
                 style="position: absolute;left: 700px;height: 856px;width: 680px;top:50%">
                <!-- fade是设置淡入淡出效果 -->
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                            <button type="button" class="closeTable" data-dismiss="modal"
                                    aria-hidden="true">

                            </button>
                            <span id="showDialogName">添加题目</span>
                        </div>
                        <div class="modalBody" id="modalBodyShow"
                             style="height: 700px;overflow-y: scroll;padding: 20px 20px 20px 20px;">
                            <label class=" itemType"
                                   style="font-family: '微软雅黑';font-weight: bold;display: inline-block;width: 40px;margin-right:26px;text-align: right;"><span
                                    style="color:red;">*</span> 题型</label>
                            <select id="itemTypeShowSelect" class="selectItem"
                                    onchange="changeType(this.options[this.options.selectedIndex].value)">
                                <option value="1">单选题</option>
                                <option value="2">多选题</option>
                                <option value="3">判断题</option>
                                <%--<option value="4">简答题</option>--%>
                                <option value="5">填空题</option>
                            </select><br>
                            <label class="itemTitleLabel"
                                   style="font-family: '微软雅黑';font-weight: bold;display: inline-block;width: 40px;margin-right:26px;text-align: right;"><span
                                    style="color:red;">*</span> 题干</label>
                            <textarea id="itemTitleShow" class="itemTitle" rows="3" maxlength="400"
                                      placeholder="请填写题干内容"></textarea><br>

                            <label class="itemCodeLabel"
                                   style="font-family: '微软雅黑';font-weight: bold;display: inline-block;width: 40px;margin-right:26px; text-align: right;">代码</label>
                            <textarea id="itemCodeShow" class="itemCode" rows="3" maxlength="400"
                                      placeholder="请填写题目代码内容"></textarea><br>
                            <!-- 添加题目模态框中选项说明区域 -->
                            <label class="col-sm-1 control-label"
                                   style="font-family: '微软雅黑';font-weight: bold;display: inline-block;width: 40px;margin-right:26px;margin-bottom: 0px;text-align: right;"><span
                                    style="color:red;">*</span> 选项</label>
                            <span class="control-label" style="color:orange;"><span
                                    id="optionTipShow"
                                    style="font-weight: 700;">正确选项个数为1</span></span>
                            <!-- 添加题目模态框中添加选项区域 -->
                            <div id="optionShowDiv"></div>
                            <!--警告框-->
                            <div class="alert alert-block hide" style="color: #ff6c60"
                                 id="showOptionWarn"></div>
                            <div style="margin-top: 20px;" id='addOptionBtnDivShow'>
                                <button class="btEnabled" id="addOptionBtnShow"
                                        style="width:520px;margin-left: 70px;">
                                    <img src="../images/add.png" style="margin-bottom: 4px;"/>&nbsp;添加一个选项</span>
                                </button>
                            </div>
                            <!-- 添加题目模态框中题目解析说明区域 -->
                            <label class="itemExplain"
                                   style="font-family: '微软雅黑';font-weight: bold;display: inline-block;width: 40px;margin-right:26px;text-align: right;">解析</label>
                            <span class="labelDetail" style="color:orange;font-weight: 700;">考生/学员查答案时可显示该信息，达到学习的目的</span>
                            <textarea class="explainArea" id="answerSolutionShow" rows="3" maxlength="500"
                                      placeholder="请填写解析内容"></textarea>
                        </div>
                        <!--警告框-->
                        <div class="alert alert-block hide" style="color: #ff6c60"
                             id="showItemWarn"></div>
                        <!-- 添加题目按钮-->
                        <div class="modal-footer" id="itemShowFooter"></div>
                    </div>
                </div>
            </div>

            <!-- 模态框（Modal） -->
            <div class="modal fade dialog fade hide" id="okModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="top:250px;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                            <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true">
                            </button>
                            <span id="okModalLabel">提示框</span>
                        </div>
                        <div class="modal-body" id="okModalMessage"  style="height:45px;">
                            在这里添加一些文本
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn_cancel" style="width:60px;height:30px;"data-dismiss="modal">关闭</button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal -->
            </div>
        </div>
    </div>
</div>
<div class="modal fade hide" id="uploadModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="position: absolute;left: 45%;height: 150px;top:45%;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true">
                </button>
                <span>上传文件</span>
            </div>
            <div class="modalBody" id="uploadModalMessage"  style="height:70px;width: 300px;">
                <form id="form1" method="post" action="/zxks/rest/item/batchUpload" enctype="multipart/form-data" style="width: 430px;padding: 10px 0 0 30px">
                    <input type="file" class="filestyle" data-classButton="btn btn-success" data-buttonText="请选择文件" name="excelFile" accept=".xls,.xlsx"/>
                </form>
            </div>
            <div class="modal-footer">
                <span id="uploadingSpan" style="display: none;float: left;color: red;">导入中,请稍候...</span>
                <button id="uploadBtn" class="sure" onclick="uploadItemInfo()"> 上 传 </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
</body>
</html>