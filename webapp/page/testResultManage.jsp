<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <link href="../images/favicon.ico" rel="icon" type="image/x-icon"/>
    <title>成绩管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" type="text/css" href="../exts/datatables/css/jquery.dataTables.css">
    <link rel="stylesheet" type="text/css" href="../css/itemBankManage.css">
    <link rel="stylesheet" type="text/css" href="../css/basic.css">

    <script type="text/javascript" charset="utf8" src="../exts/jquery-1.12.3.min.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/jquery-ui.min.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/datatables/js/jquery.dataTables.js"></script>
    <script type="application/javascript" src="../exts/bootstrap-treeview.js"></script>
    <script src="../exts/bootstrap/js/bootstrap-modal.js"></script>
    <script src="../exts/bootstrap/js/bootstrap-modalmanager.js"></script>
    <link href="../exts/bootstrap/css/bootstrap-modal.css" rel="stylesheet" media="screen"/>
    <link rel="stylesheet" type="text/css" href="../css/bootstrap-treeview.css">
    <script src="../exts/util.js"></script>
    <script type="application/javascript" src="../js/testPaper/testPaperPublic.js"></script>
    <style type="text/css">
        #userScoresModal .modal-body {
            max-height: 800px;
        }

        #userScoresModal {
            max-height: 800px;
            height: 800px;
            width: 830px;
            margin-left: -415px;
            top: 450px;
        }

        .showPeople {
            width: 16px;
            height: 16px;
            border: 0;
            background: url("../images/user.png");

        }

        .columnCenter {
            text-align: center;
        }

        .columnLeft {
            text-align: left;
        }

        .showTestResult {
            width: 20px;
            height: 20px;
            border: 0;
            background: url("../images/show.png");
        }

        .showTestResult:hover {
            background: url("../images/show_hover.png");
        }


        #update_password_modal {
            top: 30% !important;
            width: 480px
        }

        input[type="password"] {
            height: 34px !important;
            width: 150px !important;
        }

        .closeTable {
            background: url("../images/close.png");
            float: right;
            width: 50px;
            height: 50px;
            margin-top: -9px !important;
            margin-right: -16px;
            opacity: 1;
            border: 0;
        }

        .closeTable:hover {
            background: url("../images/close_hover.png");
        }

        #peopleNameModal {
            top: 30%;
        }

        .modal-backdrop {
            background: #333 !important;
        }

        tr.odd {
            background-color: #f1f2f2 !important;
        }

        tr.even {
            background-color: #ffffff !important;
        }

        tr.odd > .sorting_1 {
            background-color: #f1f2f2 !important;
        }

        tr.even > .sorting_1 {
            background-color: #ffffff !important;
        }

    </style>

    <script type="text/javascript">
        var startIndex = 0;
        function showUserMark(achievementId, userId, userMark) {
            window.open("achievementDetail.jsp?achievementId=" + achievementId + "&userId=" + userId + "&userMark=" + userMark);
        }
        $(function () {
            table = $('#testResultTable').DataTable(dataTableJson);
            buildDomTree();
        });
        var dataTableJson = {
            "language": {
                "sSearchPlaceholder": "请输入试卷名称"

            },
            "bServerSide": true,                //指定从服务器端获取数据
            "bProcessing": true,                //显示正在处理进度条
            "sAjaxSource": "../rest/testResult/list",    //服务端Rest接口访问路径
            "columns": [
                {
                    "data": "operate",
                    'sClass': 'columnCenter',
                    'width': '5%',
                    "mRender": function (data, type, full) {
                        return '';
                    }
                },
                {"data": "testPaperName", 'sClass': 'columnLeft', 'width': '24%'},
                {"data": "testPaperCategoryName", 'sClass': 'columnLeft', 'width': '8%'},
                {"data": "totalScore", 'sClass': 'columnCenter', 'width': '6%'},
                {"data": "totalTime", 'sClass': 'columnCenter', 'width': '9%'},
                {"data": "startTime", 'sClass': 'columnCenter', 'width': '15%'},
                {"data": "totalUserNum", 'sClass': 'columnCenter', 'width': '10%'},
                {"data": "testUserNum", 'sClass': 'columnCenter', 'width': '8%'},
                {
                    "data": "operate",
                    'sClass': 'columnCenter',
                    'width': '10%',
                    "mRender": function (data, type, full) {
                        return '';
                    }
                }],
            "aoColumnDefs": [                                //指定列附加属性
                {"bSortable": false, "aTargets": [0, 1, 2, 3, 4, 5, 6, 7, 8]},//这句话意思是第1,3（从0开始算）不能排序
                {"bSearchable": false, "aTargets": [1, 2]}, //bSearchable 这个属性表示是否可以全局搜索，其实在服务器端分页中是没用的
            ],
            "aaSorting": false, "aTargets": [0, 1, 2, 3, 4, 5, 6, 7, 8], //默认排序
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {     // 当创建了行，但还未绘制到屏幕上的时候调用，通常用于改变行的class风格
                /*          if (aData.testPaperModel == 1) {
                 $('td:eq(2)', nRow).html("手工组卷");
                 } else if (aData.testPaperModel == 0) {
                 $('td:eq(2)', nRow).html("智能组卷");
                 }*/
                $('td:eq(0)', nRow).html(( iDisplayIndex + 1 ) + startIndex);
                $('td:eq(1)', nRow).html(filterStringReturnSpan(aData.testPaperName,13));
                $('td:eq(5)', nRow).html(new Date(aData.startTime).format("yyyy-MM-dd hh:mm"));
                $('td:eq(6)', nRow).append('&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button title="查看人数" class="showPeople" onclick="getPeopleNames(' + aData.testInfoId + ',\'' + aData.testPaperName + '\')"></button>');
                $('td:eq(8)', nRow).html('<button class="showTestResult" title="查看成绩" onclick="showUserScores(' + aData.testInfoId + ',' + aData.startTime + ')"></button>');
                return nRow;
            },
            "fnServerData": function (sSource, aoData, fnCallback) {
                var searchName = $('#searchValue').val();
                var testInfoSearch = {
                    testInfo: {}
                };
                if (currentNode && currentNode.categoryName) {
                    testInfoSearch.testInfo.testCategory = currentNode.categoryName;
                }
                testInfoSearch.paging = convertToPagingEntity(aoData);
                $.ajax({
                    "type": 'post',
                    "contentType": "application/json",
                    "url": sSource,
                    "dataType": "json",
                    "headers": {userId: 118},
                    "data": JSON.stringify(
                            testInfoSearch
                    ),
                    "success": function (resp) {
                        if (resp.resultCode == "ERROR") {
                            showOkModal("提示信息", "考试结果查询出现异常!")
                            $("#testResultTable_processing").hide();
                        }
                        fnCallback(resp);
                    }
                });
            }
        };

        function showUserScores(testPaperId, startTime) {
            var startTime = new Date(startTime).format("yyyy-MM-dd hh:mm");
            var now = new Date();
            var dStart = new Date(startTime.replace(/\-/g, "\/"));
            if (now < dStart) {
                showOkModal("提示信息", "该场考试还未开始!");
                return false;

            }
            $('#userScoresModal').modal(
                    {
                        show: true,
                        keyboard: false,
                        backdrop: 'static'
                    }
            );
            $('#userScoresTable').DataTable({
                "language": {
                    "sSearchPlaceholder": "请输入用户名"
                },
                "bServerSide": true,                //指定从服务器端获取数据
                "bProcessing": true,                //显示正在处理进度条
                "bDestroy": true,
                "sAjaxSource": "../rest/testResult/userScoreList",    //服务端Rest接口访问路径
                "columns": [
                    {
                        "data": "operate",
                        "mRender": function (data, type, full) {
                            return '';
                        }
                    },
                    {"data": "testPaperName", 'sClass': 'columnLeft','width':'10%',
                        "mRender": function (data, type, full) {
                            return filterStringReturnSpan(data,13);
                        }},
                    {"data": "userName", 'sClass': 'columnCenter'},
                    {"data": "realName", 'sClass': 'columnCenter'},
                    {"data": "gender", 'sClass': 'columnCenter'},
                    {"data": "totalScore", 'sClass': 'columnCenter'},
                    {"data": "score", 'sClass': 'columnCenter'},
                    {
                        "data": "operate",
                        'sClass': 'columnCenter',
                        "mRender": function (data, type, full) {
                            return '';
                        }
                    },
                    {
                        "data": "operate",
                        'sClass': 'columnCenter',
                        "mRender": function (data, type, full) {
                            return '';
                        }
                    }],
                "aoColumnDefs": [                                  //指定列附加属性
                   
                    {"bSortable": false, "aTargets": [0, 1, 3, 4, 5, 8]},   //这句话意思是第0,1,5,7（从0开始算）不能排序
                    {"bSearchable": false, "aTargets": [2, 3]},     //bSearchable 这个属性表示是否可以全局搜索，其实在服务器端分页中是没用的
                ],
                "aaSorting": [[2, "asc"]], //默认排序
                "fnRowCallback": function (nRow, aData, iDisplayIndex) {     // 当创建了行，但还未绘制到屏幕上的时候调用，通常用于改变行的class风格
                    var percentMark = Math.round(aData.score / aData.totalScore * 100);
                    $('td:eq(7)', nRow).html(percentMark);
                    if (aData.gender == 1) {
                        $('td:eq(4)', nRow).html("男");
                    } else if (aData.gender == 2) {
                        $('td:eq(4)', nRow).html("女");
                    }

                    $('td:eq(0)', nRow).html((iDisplayIndex + 1) + startIndex);
                    $('td:eq(8)', nRow).html('<button class="showTestResult" onclick="showUserMark(' + aData.achievementId + ',' + aData.userId + ',' + aData.score + ')"></button>');
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
                            paging: convertToPagingEntity(aoData),
                            id: testPaperId
                        }),
                        "success": function (resp) {
                            if (resp.resultCode == "ERROR") {
                                showOkModal("提示信息", "成绩查询出现异常!")
                                $("#testResultTable_processing").hide();
                            }
                            fnCallback(resp);
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
        function filterStringReturnSpan(targetStr, widthLimit) {
            var tempStr = "";
            if (targetStr.length > widthLimit) {
                tempStr = targetStr.substr(0,widthLimit) + "...";
            } else {
                tempStr = targetStr;
            }

            return "<span title='" + targetStr + "' class='ellipsis' style='float:left;margin-left:10px;' >" + tempStr + "</span>";
        }

        var currentNode = null; //当前选中节点
        var testPaperCategoryMapById = {};
        var topCategories = [];
        var subCategories = [];
        function returnSpan(targetStr) {
            return "<span title='" + targetStr + "' class='ellipsis' >" + targetStr + "</span>";
        }
        function initSelectCategories(strTopDomId, strSubDomId, optionHtml) {
            $('#' + strTopDomId).text('');
            $('#' + strTopDomId).append(optionHtml);
            $('#' + strTopDomId).val("");
            $('#' + strTopDomId).change(function () {
                var categoryId = $(this).val();
                //向后台请求获取该分类下试卷信息
                initTestPaperListSelect(categoryId, "paperSelect");
                $('#' + strSubDomId).empty();
                $('#' + strSubDomId).trigger('change');
                if (testPaperCategoryMapById[categoryId] && testPaperCategoryMapById[categoryId].nodes && testPaperCategoryMapById[categoryId].nodes.length > 0) {
                    var subCategoriesHtml = "";
                    subCategoriesHtml += "<option value=''></option>";
                    $.each(testPaperCategoryMapById[categoryId].nodes, function (index, category) {
                        //添加子类别下拉框
                        subCategoriesHtml += "<option value='" + category.id + "'>" + category.categoryName + "</option>";
                    });
                    $('#' + strSubDomId).append(subCategoriesHtml);
                }

                /*  $('#' + strTopDomId).change(function () {
                 var categoryId = $(this).val();
                 $('#' + strSubDomId).empty();
                 $('#' + strSubDomId).trigger('change');

                 });*/
            });
            $('#' + strSubDomId).change(function () {
                var categoryId = $(this).val();
                $('#paperSelect').empty();
                //向后台请求获取该分类下试卷信息
                if (!categoryId || categoryId == "") {
                    initTestPaperListSelect($('#' + strTopDomId).val(), "paperSelect");
                } else {
                    initTestPaperListSelect(categoryId, "paperSelect");
                }
            });
        }
        function initTestPaperListSelect(categoryId, strPaperListDomId) {
            if (!categoryId || categoryId == "") {
                return;
            }
            $('#' + strPaperListDomId).empty();
            $.ajax({
                "type": 'POST',
                "contentType": "application/json",
                "url": "rest/testPaper/paperList",
                "dataType": "json",
                "headers": {userId: 118},
                "data": JSON.stringify({
                    id: categoryId,
                    paging: {
                        "sEcho": 2,
                        "startIndex": 0,
                        "pageSize": 100,
                        "searchValue": "",
                        "sortColumn": 1,
                        "sortDir": "desc"
                    }
                }),
                "success": function (resultData) {
                    $('#' + strPaperListDomId).empty();
                    if (resultData && resultData.data) {
                        var optionHtml = "";
                        optionHtml = "<option></option>";
                        $.each(resultData.data, function (index, testPaper) {
                            //添加下拉框
                            optionHtml += "<option testTime='" + testPaper.testTime + "' value='" + testPaper.id + "'>" + testPaper.testpaperName + "</option>";
                        });
                        $('#' + strPaperListDomId).append(optionHtml);

                    }
                },
                error: function (xhr, r, e) {
                    warn(e);
                }
            });
        }
        function buildDomTree() {
            var data = [];
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
                    topCategories = [];
                    subCategories = [];
                    $.each(categories, function (index, category) {
                        if (index == 0) {
                            category.state = {
                                selected: true
                            };
                            currentNode = category;
                            table.ajax.reload();
                        }
                        if (!testPaperCategoryMapById[category.id]) {
                            testPaperCategoryMapById[category.id] = category;
                        }
                        if (category.parentId == 0) {
                            topCategories.push(category);
                        } else {
                            subCategories.push(category);
                        }
                    });
                    var optionHtml = "<option value=''></option>";
                    $.each(topCategories, function (index, category) {
                        //添加下拉框
                        optionHtml += "<option value='" + category.id + "'>" + category.categoryName + "</option>";
                        category.text = returnSpan(category.categoryName);
                        category.href = "#";
                        $.each(subCategories, function (index2, subcategory) {
                            if (subcategory.parentId == category.id) {
                                if (!category.nodes) {
                                    category.nodes = []
                                }

                                var subNode = {
                                    id: subcategory.id,
                                    pId: subcategory.parentId,
                                    categoryName: subcategory.categoryName,
                                    text: returnSpan(subcategory.categoryName),
                                    href: "#"
                                };
                                category.nodes.push(subNode);
                                addNodes(subNode);
                            }

                        });
                    });
                    initSelectCategories("parentCategoriesAddSelect", "subCategoriesAddSelect", optionHtml);

                    function addNodes(targetCategory) {
                        $.each(subCategories, function (index2, subcategory) {
                            if (subcategory.parentId == targetCategory.id) {
                                if (!targetCategory.nodes) {
                                    targetCategory.nodes = []
                                }
                                var subNode = {
                                    id: subcategory.id,
                                    pId: subcategory.parentId,
                                    categoryName: subcategory.categoryName,
                                    text: returnSpan(subcategory.categoryName),
                                    href: "#"
                                };
                                targetCategory.nodes.push(subNode);
                                addNodes(subNode);
                            }

                        });
                    }

                    var options = {
                        levels: 2,
                        data: topCategories,
                        expandIcon: 'icon-plus',
                        collapseIcon: 'icon-minus',
                        emptyIcon: 'icon'
                    };
                    $('#treeview').treeview(options);
                    $('#treeview').on('nodeSelected', function (event, data) {
                        currentNode = data;
                        table.ajax.reload();
                        $("#searchValue").val(""); //将页面快速检索框中内容清空
                    });
                    $('#treeview').on('nodeUnselected', function (event, data) {
                        currentNode = null;
                        table.destroy();
                        table = $('#testResultTable').DataTable(dataTableJson);
                        table.ajax.reload();
                        $("#searchValue").val(""); //将页面快速检索框中内容清空

                    });
                },
                error: function (xhr, r, e) {
                    warn(e);
                }
            });
        }
        function getPeopleNames(testInfoId, testPaperName) {
            $.ajax({
                type: "post",
                contentType: "application/json",
                dataType: "json",
                url: "../rest/testInfo/getTestpeopleNames",
                headers: {userId: "118"},
                data: JSON.stringify({
                    testInfo: {
                        id: testInfoId
                    },
                }),
                success: function (resultData) {
                    $("#peopleDiv").html("");
                    var dataList = resultData.data;
                    $("#peopleNameModal").modal(
                            {
                                show: true,
                                keyboard: false,
                                backdrop: 'static'
                            }
                    );
                    $("#paperName").html(testPaperName);
                    var html = '';
                    $.each(dataList, function (index, dataObj) {
                        html += '<span style="margin-left: 20px;display: block">' + dataObj.testpeopleName + '</span>';

                    })
                    $("#peopleDiv").append(html);

                }
            })
        }
    </script>
    <style>
        #treeview {
            /*border-bottom-left-radius: 4px;
            border-top-left-radius: 4px;*/
        }

        li > .expand-icon {
            margin-left: 10px;
        }
    </style>

</head>
<body style="background: #f1f4f8">
<div id="mainDiv">
    <div id="headDiv">
        <jsp:include page="menu.jsp"/>
    </div>
    <div id="contentDiv">
        <%@include file="mymodal.jsp" %>
        <div class="container-fluid" style="">

            <div class="leftDiv">
                <div class="categoryDiv">
                    <div class="treeDiv">
                        <span class="fontSpan" style="font-family:微软雅黑">考试类别</span>

                        <div id="treeview" class="treeview"></div>
                    </div>
                </div>
            </div>

            <div class="rightDiv">
                <div class="testBody">

                    <ol class="breadcrumb">
                        <li class="myMarkLabel" style="text-shadow:none;font-family: 微软雅黑;font-size: 16px;color: #333">
                            成绩管理
                        </li>
                    </ol>

                    <table id="testResultTable" class="display">
                        <thead>
                        <tr>
                            <th style="text-align:center;width:10%;">序号</th>
                            <%-- <th style="text-align:center;">考试ID</th>--%>
                            <th style="text-align:center;width:15%;">试卷名称</th>
                            <%--            <th style="text-align:center;">出题方式</th>--%>
                            <th style="text-align:center;">考试类别</th>
                            <th style="text-align:center;">分数</th>
                            <th style="text-align:center;">时长(分钟)</th>
                            <th style="text-align:center;">开考时间</th>
                            <th style="text-align:center;">应参加</th>
                            <th style="text-align:center;">实参加</th>
                            <th style="text-align:center;">查看成绩</th>
                        </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
            </div>

        </div>

        <!-- 考生成绩查询模态框 -->
        <div class="modal fade" id="userScoresModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
             aria-hidden="true" style="border-radius: 4px">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header" style="background: #333;height: 32px;line-height: 32px">
                        <button type="button" id="closeTable" class="closeTable" data-dismiss="modal"
                                aria-hidden="true">

                        </button>
            <span class="modal-title" style="color: #ffffff;font-size: 18px">
              考试成绩查询
            </span>
                    </div>
                    <div class="modal-body" style="height:658px;width:800px;">
                        <table id="userScoresTable" class="display">
                            <thead>
                            <tr>
                                <th style="text-align:center;">序号</th>
                                <th style="text-align:center;">试卷名称</th>
                                <th style="text-align:center;">用户名</th>
                                <th style="text-align:center;">真实姓名</th>
                                <th style="text-align:center;">性别</th>
                                <th style="text-align:center;">总分</th>
                                <th style="text-align:center;">得分</th>
                                <th style="text-align:center;">百分制得分</th>
                                <th style="text-align:center;">查看成绩</th>
                            </tr>
                            </thead>
                            <tbody>

                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn_cancel"
                                style="width:70px;height:30px;border-radius: 4px;border: 0;" data-dismiss="modal">关闭
                        </button>
                    </div>
                </div>
                <!-- /.modal-content -->
            </div>
            <!-- /.modal -->
        </div>

        <div class="modal fade" id="peopleNameModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
             aria-hidden="true" style="border-radius: 4px">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header" style="background: #333;height: 32px;line-height: 32px">
                        <button type="button" id="close" class="closeTable" data-dismiss="modal" aria-hidden="true">

                        </button>
            <span class="modal-title" style="color: #ffffff;font-size: 18px">
              参考人数查询
            </span>
                    </div>
                    <div class="modal-body">
                        <label style="font-size: 16px;font-weight: bold;">试卷名称：</label>
                        <span id="paperName"></span>
                        <label style="font-size: 16px;font-weight: bold;">参考人员：</label>

                        <div id="peopleDiv" style="width: 100%;max-height: 200px;overflow-y: scroll"></div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn_cancel"
                                style="width:70px;height:30px;border-radius: 4px;border: 0;" data-dismiss="modal">关闭
                        </button>
                    </div>
                </div>
                <!-- /.modal-content -->
            </div>
            <!-- /.modal -->
        </div>

    </div>
</div>
<div id="testResultModalDiv"/>
</body>
</html>