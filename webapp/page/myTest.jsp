<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <link href="../images/favicon.ico" rel="icon" type="image/x-icon"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>在线考试系统--成绩管理</title>
    <link rel="stylesheet" type="text/css" href="../exts/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="../css/myTest.css">
    <link rel="stylesheet" type="text/css" href="../css/base.css">
    <link rel="stylesheet" type="text/css" href="../exts/datatables/css/jquery.dataTables.css">
    <script src="../exts/jquery-1.12.3.min.js"></script>
    <script src="../exts/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" charset="utf8"
            src="../exts/datatables/js/jquery.dataTables.js"></script>
    <script type="application/javascript" src="../js/testPaper/testPaperPublic.js"></script>
    <script>
        var startIndex = 0;
        var infoId = 0;
        //获取考试信息列表
        $(document).ready(function () {
            testInfoList();
        });
        function checkUserAchievement(testInfoId, endTime) {
            $.ajax({
                async:false,
                contentType: "application/json",
                dataType: "json",
                type: "post",
                headers: {"userId": 118},
                url: "/zxks/rest/testInfo/userAchievement",
                data: JSON.stringify({
                    testInfo: {
                        id: testInfoId
                    }
                }),
                success: function (data) {
                    if (data.message == "1") {
                        showOkModal('提示框', '您已参加过该场考试!');
                        return false;
                    } else {
                        sessionStorage.setItem('testInfoId', testInfoId);
                        sessionStorage.setItem('endTime', endTime);
                        //window.location.href = "examing.jsp";
                        var height = window.screen.height;
                        var width = window.screen.width;
                        window.open("examing.jsp", "","top=0,left=0,height="+height+",width="+width+",toolbar=no, menubar=no,scrollbars=yes, resizable=no, location=no, statue=no");
                         //  window.open("examing.jsp", "","top=0,left=0,height=800,width=1600");

                    }
                }
            });

        }
         var userId = <%=request.getSession().getAttribute("userId")%>;
        function showOkModal(label, contennt) {
            $("#okModalLabel")[0].innerHTML = label;
            $("#okModalMessage")[0].innerHTML = contennt;
            $("#okModal").modal(
                    {
                        show: true,
                        keyboard: false,
                        backdrop: 'static'
                    });
        }

        function testInfoList(key) {
            var currentDate = new Date();
            var testStatus = key;
            $('#table_id').DataTable({
                "language": {
                    "sSearchPlaceholder": "请输入试卷名称"
                },
                "bServerSide": true,                //指定从服务器端获取数据
                "bProcessing": true,                //显示正在处理进度条
                "bDestroy": true,
                "sAjaxSource": "/zxks/rest/testInfo/testInfoList",    //服务端Rest接口访问路径
                "columns": [
                    {
                        "data": "no", 'sClass': 'left',
                        "mRender": function (data, type, full) {
                            return '';
                        }
                    },
                    {"data": "testpaperName", 'sClass': 'columnLeft'},
                    {"data": "startTime", 'sClass': 'center'},
                    {"data": "endTime", 'sClass': 'center'},
                    {"data": "testStatus", 'sClass': 'left'},
                    {"data": "testHour", 'sClass': 'left'},
                    {
                        "data": "operate", 'sClass': 'left',
                        "mRender": function (data, type, full) {
                            return '';
                        }
                    }],
                "aoColumnDefs": [                                //指定列附加属性
                    {"bSortable": false, "aTargets": [0, 1, 4, 6]},   //这句话意思是第1,3（从0开始算）不能排序
                    {"bSearchable": false, "aTargets": [1, 2]}, //bSearchable 这个属性表示是否可以全局搜索，其实在服务器端分页中是没用的
                ],
                "aaSorting": [[2, "desc"]], //默认排序
                "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayStart, iDisplayLength) {     // 当创建了行，但还未绘制到屏幕上的时候调用，通常用于改变行的class风格
                    if (aData.endTime && aData.startTime) {
                        var dEnd = new Date(aData.endTime.replace(/\-/g, "\/"));
                        var dStart = new Date(aData.startTime.replace(/\-/g, "\/"));
                        $('td:eq(4)', nRow).html("<span id='testStatus' class='testStatus'  endTime='" + aData.endTime + "' startTime='" + aData.startTime + "'></span>");
                    }

                    $('td:eq(6)', nRow).html((currentDate > dStart && currentDate < dEnd ) ? '<button id="intoTest" title="进入考试" class="intoTest" style="background-image: url(../images/into_test.png);width:20px;height:20px;border:0;"  onclick="checkUserAchievement(' + aData.id + ',\'' + aData.endTime + '\')"></button>' : '<button id="intoTest" class="intoTest" disabled="disabled" style="width:20px;height:20px;border:0;background-image: url(../images/into_complete.png)"></button>');
                    $('td:eq(1)', nRow).html($.testPaperPublic.returnSpan(aData.testpaperName));
                    $('td:eq(0)', nRow).html((iDisplayIndex + 1) + startIndex);
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
                            testInfo: {
                                testStatus: testStatus,
                            },
                            paging: convertToPagingEntity(aoData)
                        }),
                        "success": function (resp) {
                            fnCallback(resp);
                        }
                    });
                }
            });
        }

        //定时检测当前考试状态
        var interval = setInterval(function () {
            var spanList = $("span.testStatus");
            $.each(spanList, function (index, span) {
                var endTime = span.getAttribute("endTime");
                var startTime = span.getAttribute("startTime");
                if (endTime && endTime.length > 0 && startTime && startTime.length > 0) {
                    var dEnd = new Date(endTime.replace(/\-/g, "\/"));
                    var dStart = new Date(startTime.replace(/\-/g, "\/"));
                    var now = new Date();
                    if (now >= dEnd) {
                        span.setAttribute("style", "color:gray");
                        span.innerHTML = "已结束";
                    } else {
                        if (now >= dStart) {
                            span.setAttribute("style", "color:green");
                            span.innerHTML = "开考中";
                        } else {
                            span.setAttribute("style", "color:red");
                            span.innerHTML = "未开考";
                        }
                    }

                }
            });
        }, 500);

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
            return pagingEntity;
        }
    </script>
</head>
<body>
<div id="mainDiv">
    <div id="headDiv">
        <jsp:include page="menu.jsp"/>
    </div>
    <div id="contentDiv" style="text-align: left;">
        <div id="modalDiv">
            <div class="modal fade dialog fade hide" id="okModal" tabindex="-1" role="dialog"
                 aria-labelledby="myModalLabel" aria-hidden="true" style="top:250px;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                            <button type="button" class="closeTable" data-dismiss="modal"
                                    aria-hidden="true" style="color: #fff">
                                &times;
                            </button>
                            <span id="okModalLabel">提示框</span>
                        </div>
                        <div class="modal-body" id="okModalMessage" style="height:45px;">
                            在这里添加一些文本
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn_cancel" style="width:60px;height:30px;"
                                    data-dismiss="modal">关闭
                            </button>
                        </div>
                    </div>
                    <!-- /.modal-content -->
                </div>
                <!-- /.modal -->
            </div>


        </div>
        <div class="container-fluid" style="">
            <div class="testBody">

                <ol class="breadcrumb">
                    <li class="myMarkLabel"
                        style="font-family: 微软雅黑;font-size: 16px;text-shadow:none;">我的考试
                    </li>
                </ol>

                <table id="table_id" class="display">
                    <thead>
                    <tr>
                        <th style="text-align:center;">序号</th>
                        <th style="text-align:center;">试卷名称</th>
                        <th style="text-align:center;">考试开始时间</th>
                        <th style="text-align:center;">考试结束时间</th>
                        <th style="text-align:center;">考试状态</th>
                        <th style="text-align:center;">时长(分钟)</th>
                        <th style="text-align:center;">参加考试</th>
                    </tr>
                    </thead>

                    <tbody>

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>