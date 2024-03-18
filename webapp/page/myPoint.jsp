<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<html lang="en">
<head>
    <link href="../images/favicon.ico" rel="icon" type="image/x-icon"/>
    <meta charset="UTF-8">
    <title>在线考试系统--成绩管理</title>
    <link rel="stylesheet" type="text/css" href="../css/base.css">
    <link rel="stylesheet" type="text/css" href="../exts/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="../css/myPoint.css">
    <link rel="stylesheet" type="text/css" href="../exts/datatables/css/jquery.dataTables.css">
    <script src="../exts/jquery-1.12.3.min.js"></script>
    <script src="../exts/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/datatables/js/jquery.dataTables.js"></script>
    <script type="application/javascript" src="../js/testPaper/testPaperPublic.js"></script>
    <script>
        $(document).ready(function () {
            pointInfoList();
        });

        function intoMyPoint(achievementId,userMark,userId){
            window.open("achievementDetail.jsp?achievementId="+achievementId+"&userMark="+userMark+"&userId="+userId);
        }

        function pointInfoList(){
            $('#table_id').DataTable({
                "language" : {
                    "sSearchPlaceholder": "请输入试卷名称"
                },
                "bServerSide": true,                //指定从服务器端获取数据
                "bProcessing": true,                //显示正在处理进度条
                "sAjaxSource": "/zxks/rest/testInfo/pointInfoList",    //服务端Rest接口访问路径
                "columns" : [
                    {"data": "no","mRender": function(data, type, full) {
                        return '';}
                    },
                    {"data" : "testpaperName", 'sClass':'columnLeft'},
                    {"data" : "startTime", 'sClass':'center'},
                    {"data" : "endTime", 'sClass':'center'},
                    {"data" : "testHour", 'sClass':'left'},
                    {"data" : "totalMark", 'sClass':'left'},
                    {"data" : "userMark", 'sClass':'left'},
                    {"data" : "percentMark", 'sClass':'left'},
                    {"data" : "operate", 'sClass':'left',"mRender": function(data, type, full) {
                        return '';}},
                    {"data": "operate", 'sClass':'left',
                        "mRender": function(data, type, full) {
                            return '';}
                    }],
                "aoColumnDefs": [                                //指定列附加属性
                    {"bSortable": false, "aTargets": [0,1,4,6,7,8,9]},   //这句话意思是第1,3（从0开始算）不能排序
                    {"bSearchable": false, "aTargets": [1, 2]}, //bSearchable 这个属性表示是否可以全局搜索，其实在服务器端分页中是没用的
                ],
                "aaSorting": [[2, "desc"]], //默认排序
                "fnRowCallback": function(nRow, aData, iDisplayIndex, iDisplayStart, iDisplayLength) {     // 当创建了行，但还未绘制到屏幕上的时候调用，通常用于改变行的class风格
                    var whetherPass = "";
                    if(aData.percentMark>=60){
                        whetherPass ="通过";
                        $('td:eq(8)', nRow).html(whetherPass);
                    }else{
                        whetherPass ="不通过";
                        $('td:eq(8)', nRow).html(whetherPass);
                    }
                    var userId = <%=request.getSession().getAttribute("userId")%>;
                       $('td:eq(9)', nRow).html("<button class='showTest' title='查看成绩' onclick='intoMyPoint("+aData.achievementId+","+aData.userMark+","+userId+")'></button>");

                    $('td:eq(0)', nRow).html((iDisplayIndex+1)+startIndex);
                    $('td:eq(1)', nRow).html($.testPaperPublic.returnSpan(aData.testpaperName));
                    return nRow;
                },
                "fnServerData" : function(sSource, aoData, fnCallback) {
                    $.ajax({
                        "type" : 'post',
                        "contentType": "application/json",
                        "url" : sSource,
                        "dataType" : "json",
                        "headers" : {userId: 118},
                        "data" : JSON.stringify({
                            testInfo:{
                            },
                            paging:convertToPagingEntity(aoData)
                        }),
                        "success" : function(resp) {
                            fnCallback(resp);
                        }
                    });
                }
            });
        }

        function convertToPagingEntity(aoData) {
            var pagingEntity = new Object();
            $.each(aoData, function(i,val){
                if(val.name=="iDisplayStart") {
                    pagingEntity.startIndex = val.value;
                    startIndex = val.value;
                } else if(val.name=="iDisplayLength") {
                    pagingEntity.pageSize = val.value;
                } else if(val.name=="sSearch") {
                    pagingEntity.searchValue = val.value;
                } else if(val.name=="iSortCol_0") {
                    pagingEntity.sortColumn = val.value;
                } else if(val.name=="sSortDir_0") {
                    pagingEntity.sortDir = val.value;
                } else if(val.name=="sEcho") {
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
        <jsp:include page="menu.jsp" />
    </div>
    <div id="contentDiv" style="text-align: left">
<div class="container-fluid">
    <div class="col-lg-10">
        <ol class="breadcrumb">
            <li class="myMarkLabel" style="text-shadow:none;">我的成绩</li>
        </ol>

            <table id="table_id" class="display">
                <thead>
                <tr>
                    <th style="text-align:center;">序号</th>
                    <th style="text-align:center;">试卷名称</th>
                    <th style="text-align:center;">考试开始时间</th>
                    <th style="text-align:center;">考试结束时间</th>
                    <th style="text-align:center;">时长(分钟)</th>
                    <th style="text-align:center;">试卷总分</th>
                    <th style="text-align:center;">我的成绩</th>
                    <th style="text-align:center;">百分制得分</th>
                    <th style="text-align:center;">是否通过</th>
                    <th style="text-align:center;">查阅考试</th>
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