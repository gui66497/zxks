<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<link href="../images/favicon.ico" rel="icon" type="image/x-icon"/>
<title>成绩详情界面</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8;IE=edge"/>
<title>在线考试系统--试卷管理</title>
<link rel="stylesheet" type="text/css" href="../exts/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="../css/examing.css">
<script src="../exts/jquery.min.js"></script>
<script src="../exts/bootstrap/js/bootstrap.min.js"></script>
<script src="../exts/bootstrap-hover-dropdown.min.js"></script>

<script src="../exts/icheck.min.js"></script>

<script src="../exts/bootstrap-treeview.js"></script>

<script type="text/javascript">

    function getSection(id) {
        window.location.href = "#section-" + id;
        var elements = $('li[name="myAffixLi"]');
        $.each(elements, function (index, element) {
            if (id == index) {
                $('#myAffixLi' + index).attr('class', 'active');
            } else {
                $('#myAffixLi' + index).removeClass();
            }

        });
    }
    /*        //附加导航随滚动条变化
     $(window.document).scroll(function () {
     var elements = $("div[name='partPaper']");
     var tempList = [];
     var tempMap = {};
     $("div[name='partPaper']").each(function (index) {
     var height =$(this).offset().top-$(window).scrollTop();
     if(height>=0){
     tempList.push(height);
     }
     if(!tempMap[height]){
     tempMap[height] = $(this).attr("id");
     }
     });
     var minDiv = tempMap[Math.min.apply(null, tempList)]+"";
     var affixLi = $("#myAffixLi"+minDiv.substring(8,9));
     $("li[name='myAffixLi']").each(function(){
     if(affixLi.attr("id")==$(this).attr("id")){
     $(this).attr('class','active');
     }else{
     $(this).removeClass();
     }
     })
     });*/

    var userRoleId = 0;
    function getUserInfo() {
        $.ajax({
            contentType: 'application/json',
            dataType: 'json',
            url: '/zxks/rest/user/getUser',
            type: 'post',
            headers: {userId: 118},
            data: JSON.stringify({
                user: {
                    userId: userObjId
                }
            }),
            success: function (data) {
                if (data.resultCode === "SUCCESS") {
                    $('#userName').text(data.data[0].userName);
                    $('#realName').text(data.data[0].realName);
                    $('#identityNo').text(data.data[0].idCardNo);
                    userRoleId = data.data[0].roleId;
                } else {
                    showOkModal("提示", data.message);
                    setTimeout(function () {
                        window.location.href = "login.jsp";
                    }, 300);
                }

            }
        })
    }
    function finishBack() {
        window.location.href = "myPoint.jsp";
    }


    var itemIDList = [];
    var userObjId = '<%=request.getParameter("userId")%>';//用户ID
    var achievementId = <%=request.getParameter("achievementId")%>;//考试信息ID
    var userMark = <%=request.getParameter("userMark")%>;//考试分数
    var userId = <%=request.getSession().getAttribute("userId")%>;//用户ID
    var resulCount = [];//考生答题数数组
    //            var userName = '<%=request.getSession().getAttribute("userName")%>';//用户名
    //           $('#userName').text(userName);

    var optionNames = ['A', 'B', 'C', 'D', 'E', 'F'];
    $(document).ready(function () {

        getUserInfo();
        $('input').iCheck({
            checkboxClass: 'icheckbox_square-blue',  // 注意square和blue的对应关系
            radioClass: 'iradio_square-blue',
            increaseArea: '20%' // optional
        });
        $.ajax({
            url: "/zxks/rest/achievement/detail",
            type: "post",
            contentType: "application/json; charset=UTF-8",
            async: true,
            dataType: "json",
            headers: {userId: userId},
            data: JSON.stringify({"id": achievementId}),
            success: function (resultData) {
                console.log(resultData);
                if (resultData != null) {
                    var testPaperPartList = resultData.data;
                    if (testPaperPartList == null || testPaperPartList.length == 0) {
                        return;
                    }
                    var myAffixHtml = '<li class="active" id="myAffixLi0" name="myAffixLi" style="margin: 20px 20px 0 20px"><a style="height: 21px;line-height: 21px;cursor: pointer" onclick="getSection(0)">基本信息</a></li>';
                    var itemHtml = '';
                    var testTime = 0;
                    $.each(testPaperPartList, function (index, testPaperPart) {
                        var partNo = Number(index + 1);
                        myAffixHtml += '<li id="myAffixLi' + Number(index + 1) + '" name="myAffixLi" style="margin: 6px 20px 0 20px"><a style="height: 21px;line-height: 21px;cursor:pointer" onclick="getSection(' + Number(index + 1) + ')" >第' + partNo + '部分：' + testPaperPart.partName + '</a></li>';
                        var resultInfoList = testPaperPart.resultInfoList;
                        itemHtml += '<div class="col-lg-12 right_details" style="margin-top:20px;padding:0" id="section-' + partNo + '">';
                        itemHtml += '<form class="form-horizontal" style="width:95%;margin:0 auto;">';
                        itemHtml += '<ol class="breadcrumb itemOl" style="width: 676px;margin-top: 15px">';
                        var counts = testPaperPart.partItemCounts;

                        if (counts == 0) {
                            itemHtml += '<li class="active" style="font-size: 16px;color: #4dbd73">第' + partNo + '部分：' + testPaperPart.partName + '（共' + testPaperPart.partItemCounts + '道,每道' + 0 + '分,共' + testPaperPart.partMark + '分）</li>';
                        } else {
                            itemHtml += '<li class="active" style="font-size: 16px;color: #4dbd73">第' + partNo + '部分：' + testPaperPart.partName + '（共' + testPaperPart.partItemCounts + '道,每道' + Number(testPaperPart.partMark / testPaperPart.partItemCounts) + '分,共' + testPaperPart.partMark + '分）</li>';
                        }
                        itemHtml += '</ol>';
                        $.each(resultInfoList, function (resultIndex, result) {
                            if (result.result != '') {
                                resulCount.push(result.result);
                            }
                            var item = result.item;
                            itemIDList.push(item.itemId);
                            itemHtml += '<div class="form-group" style="margin-bottom: 20px;border: 1px solid #ccc;border-radius: 4px">';
                            itemHtml += '<div class="panel-group">';
                            itemHtml += '<div class="panel panel-default">';

                            if (result.item.itemType == 4 || result.item.itemType == 5) {
                                //每题分值
                                var oneMark = Number(testPaperPart.partMark / testPaperPart.partItemCounts);
                                var score = result.score;
                                var ratio = score / oneMark;
                                var colorStyle;
                                if (ratio < 0.6) {
                                    colorStyle = 'color:red;';
                                } else if (ratio >= 0.6 && score / oneMark < 0.8) {
                                    colorStyle = 'color:orange;';
                                } else if (ratio >= 0.8) {
                                    colorStyle = 'color:green;';
                                }
                                itemHtml += '<span style="float: right;margin:5px 5px 0 0;' + colorStyle + '">' + result.score.toFixed(2) + " 分" + '</span>'
                            } else {
                                if (result.result == item.answer) {
                                    itemHtml += '<img style="float: right;margin:5px 5px 0 0" id="imgObj" src="../images/true.png"/>'
                                } else {
                                    itemHtml += '<img style="float: right;margin:5px 5px 0 0" id="imgObj" src="../images/false.png"/>'
                                }
                            }
                            itemHtml += '<fieldset class="panel-heading" style="padding: 0px 10px 10px 10px">';
                            itemHtml += '<legend class="panel-title" id="itemTitle' + item.itemId + '" style="width:100%;font-family:微软雅黑;font-size:14px;font-weight:bold;margin-bottom:0;border-bottom:0">第' + Number(resultIndex + 1) + '题：</legend>' + item.itemTitle + '';

                            itemHtml += '<input id="item' + item.itemId + '" value=' + item.itemId + ' style="display: none">';//隐藏题目ID
                            itemHtml += '<input id="mark' + item.itemId + '" value=' + Number(testPaperPart.partMark / testPaperPart.partItemCounts) + ' style="display: none">';//隐藏每题的分数
                            itemHtml += '<div style="padding: 10px 20px 10px 20px">';
                            if (item.itemCode.trim() != '') {
                                itemHtml += '<pre style="font-family: 微软雅黑">' + item.itemCode + '</pre>';
                            }
                            itemHtml += '</div>';
                            itemHtml += '</fieldset>';
                            itemHtml += '<div id="collapseOne" class="panel-collapse collapse fade in">';
                            itemHtml += '<div class="panel-body">';
                            var optionList = item.optionList;
                            $.each(optionList, function (optionIndex, option) {
                                console.log(option);
                                if (item.itemType != 4 && item.itemType != 5) {
                                    itemHtml += '<div class="form-group" style="margin-top: 10px">';
                                    itemHtml += '<div class="radio col-sm-1" style="text-align:left;padding-left: 0px;padding-right: 0px;width: 43px;float: left;">';
                                    if (item.itemType == 2) {
                                        var resultArr = result.result.split(",");
                                        if (resultArr.indexOf(option.optionId + "") != -1) {
                                            itemHtml += '<input type="checkbox" style="margin-left: 30px;margin-right: 20px;" checked="checked" name="option' + item.itemId + '" value="' + option.optionId + '"/>';
                                        } else {
                                            itemHtml += '<input type="checkbox" style="margin-left: 30px;margin-right: 20px;" name="option' + item.itemId + '" value="' + option.optionId + '"/>';
                                        }
                                    } else {
                                        if (Number(result.result) == option.optionId) {
                                            itemHtml += '<input type="radio" style="margin-left: 30px;margin-right: 20px;" checked="checked" name="option' + item.itemId + '" value="' + option.optionId + '"/>';
                                        } else {
                                            itemHtml += '<input type="radio" style="margin-left: 30px;margin-right: 20px;" name="option' + item.itemId + '" value="' + option.optionId + '"/>';
                                        }
                                    }
                                    itemHtml += '</div>';
                                    itemHtml += '<div class="col-sm-11" style="padding-left: 0px;margin-left: 20px;width: 602px;height: 30px;line-height:30px;margin-right: 20px;font-family:微软雅黑;margin-left: 60px;background: #eee;border-radius: 4px;/*border: 1px solid #ccc*/; ">';
                                    itemHtml += '<span class="form-control option" readonly="readonly" style="font-family:微软雅黑;margin-left: 10px" >' + optionNames[Number(optionIndex)] + '：' + option.content + '</span>';
                                    itemHtml += '</div>';
                                    itemHtml += '</div>';
                                } else if (item.itemType == 4) {
                                    itemHtml += '<div class="form-group" style="margin-top: 10px">';
                                    itemHtml += '<span style="vertical-align: top;padding:0 10px;">考生答案:</span>' + '<textarea  readonly="readonly" style="width: 590px;height: 54px;">' + result.result + '</textarea>';
                                    itemHtml += '</div>';
                                    itemHtml += '<div class="form-group" style="margin-top: 10px">';
                                    itemHtml += '<span style="vertical-align: top;padding:0 10px;">正确答案:</span>' + '<textarea  readonly="readonly" style="width: 590px;height: 54px;">' + item.answer + '</textarea>';
                                    itemHtml += '</div>';
                                } else if (item.itemType == 5) {
                                    //该填空题的所有回答数组
                                    var results = result.result.split('░');
                                    itemHtml += '<div class="form-group" style="margin-top: 10px;">';
                                    itemHtml += '<div class="radio col-sm-1" style="text-align:left;padding-left: 0px;padding-right: 0px;width: 45px;float: left;margin-top: 4px">';
                                    itemHtml += '<span style="margin-left: 30px;margin-right: 20px;" name="option' + item.itemId + '" value="' + option.optionId + '"/>';
                                    itemHtml += '<span class="form-control option" readonly="readonly" style="font-family:微软雅黑;margin-left: 10px;white-space: nowrap;">' + '填空' + (optionIndex + 1) + '</span>';
                                    itemHtml += '</div>';
                                    itemHtml += '<div class="col-sm-11" style="padding-left: 0px;margin-left: 20px;width: 602px;height: 30px;line-height:30px;margin-right: 20px;font-family:微软雅黑;margin-left: 60px;border-radius: 4px;">';
                                    itemHtml += '<input type="text" readonly="readonly" class="form-control option" name="option' + item.itemId + '" value="' + results[Number(optionIndex)] + '" style="font-family:微软雅黑;margin-left: 10px;width: 86%;" >' + '</input>';
                                    itemHtml += '</div>';
                                    itemHtml += '<div style="height: 4px;display: block"></div>'
                                    itemHtml += '<div class="radio col-sm-1" style="text-align:left;padding-left: 0px;padding-right: 0px;width: 45px;float: left;margin-top: 4px">';
                                    itemHtml += '<span class="form-control option" readonly="readonly" style="font-family:微软雅黑;float: right;white-space: nowrap;">' + '答案</span>';
                                    itemHtml += '</div>';
                                    itemHtml += '<div class="col-sm-11" style="padding-left: 0px;margin-left: 20px;width: 602px;height: 30px;line-height:30px;margin-right: 20px;font-family:微软雅黑;margin-left: 60px;border-radius: 4px;">';
                                    itemHtml += '<input type="text" readonly="readonly" class="form-control option" name="option' + item.itemId + '" value="' + option.content + '" style="font-family:微软雅黑;margin-left: 10px;width: 86%;" >' + '</input>';
                                    itemHtml += '</div>';
                                    itemHtml += '</div>';
                                }
                            });
                            var showResult = '';
                            var showAnswer = "";
                            if (item.itemType == 4) { //简答题
                                showAnswer = item.answer;
                            } else if (item.itemType == 2) {
                                var resultArr = result.result.split(",");
                                var answerArr = item.answer.split(",");
                                $.each(resultArr, function (index, singleResult) {
                                    optionNames[singleResult - 1];
                                    showResult += optionNames[singleResult - 1];
                                });
                                if (showResult == undefined) {
                                    showResult = "未选择";
                                }
                                $.each(answerArr, function (index, result) {
                                    optionNames[result - 1];
                                    showAnswer += optionNames[result - 1];
                                });

                            } else {
                                showResult = optionNames[result.result - 1];
                                showAnswer = optionNames[item.answer - 1];
                                if (showResult == undefined) {
                                    showResult = "未选择";
                                }
                            }

                            if (item.answerResolution == "null") {
                                item.answerResolution = "";
                            }
                            if (item.itemType != 4 && item.itemType != 5) {
                                itemHtml += '<div style="background:#eee;height: 30px;line-height:34px;margin-top: 15px"><span id="answer' + item.itemId + '" style="margin-left:50px;margin-bottom:10px;font-family:微软雅黑;">正确答案:&nbsp' + showAnswer + '</span>';//隐藏题目答案
                                itemHtml += '<span id="answer' + item.itemId + '" style="margin-left:50px;font-family:微软雅黑;">考生答案:&nbsp' + showResult + '</span></div>';
                                /*itemHtml += '<fieldset style="background:#eee;font-size: 14px;padding: 0px 10px 10px 10px;word-wrap: break-word"><legend style="font-size: 14px;margin-bottom: 0;border-bottom: 0" id="explain' + item.itemId + '">题目解析:&nbsp</legend>' + item.answerResolution + '';//隐藏题目答案
                                itemHtml += '</fieldset>';*/
                                itemHtml += '<div style="background:#eee;height: 35px;line-height:34px;word-wrap: break-word"><span id="explain' + item.itemId + '" style="padding: 0 10px;font-family:微软雅黑;">题目解析:&nbsp' + item.answerResolution + '</span>';//隐藏题目答案
                                itemHtml += '</div>';
                            } else {
                                itemHtml += '<div style="height: 11px" />'
                                itemHtml += '<div style="background:#eee;height: 35px;line-height:34px;word-wrap: break-word"><span id="explain' + item.itemId + '" style="padding: 0 10px;font-family:微软雅黑;">题目解析:&nbsp' + item.answerResolution + '</span>';//隐藏题目答案
                                itemHtml += '</div>';
                            }
                            itemHtml += '</div>';
                            itemHtml += '</div>';
                            itemHtml += '</div>';
                            itemHtml += '</div>';
                            itemHtml += '</div>';
                            /*itemHtml += '</div>';*/

                        });
                        itemHtml += '</form>';
                        itemHtml += '</div>';
                        var testPaper = testPaperPart.testPaper;
                        if (testPaper != null) {
                            $('#testTime').text(testPaper.testTime);
                            testTime = testPaper.testTime;
                            $('#markTotal').text(testPaper.markTotal);
                            $('#overMark').text(Number(testPaper.markTotal) * 0.6);
                            $('#testPaperName').text(testPaper.testpaperName);

                            var userName = '<%=request.getSession().getAttribute("userName")%>';
                            if (userName == "admin") {
                                if (testPaper.testpaperDescription) {
                                    $("#testPaperDescriptionObj").show();
                                    $("#testPaperDescription").text(testPaper.testpaperDescription);
                                }
                            } else {
                                $("#testPaperDescriptionObj").hide();
                            }
                            if (testPaper.testpaperExplain) {
                                $("#testPaperExplainOl").show();
                                $('#testPaperExplain').text(testPaper.testpaperExplain);
                            }
                            $("#userMark").text(userMark);
                            $("#testNo").text(itemIDList.length);
                            $("#userTestNo").text(resulCount.length);
                            $("#noAnswer").text(itemIDList.length - resulCount.length);

                        }

                    });

                    /*if(userId!=1465201138486){
                     myAffixHtml += '<div class="col-sm-6"><button class="btn btn-success" onclick="finishBack()" style="font-family:微软雅黑;border: 0;height: 34px;width: 220px;text-shadow: none">' +
                     '返回我的成绩</button></div>';
                     }*/
                    $('#myAffix').text('');
                    $('#myAffix').append(myAffixHtml);//左边导航树加载
                    $('#itemDiv').text('');
                    $('#itemDiv').append(itemHtml);//添加题目信息
                    /* timer(Number(testTime*60));*/

                }
            },
            error: function (xhr, r, e) {
                showOkModal('提示框', e);
            }
        });
    });
    $('#myAffix').affix({
        offset: {
            top: 25,
        }
    });


    $('#userInfo').affix({
        offset: {
            top: 50,
        }
    });


</script>
<style>
    td, th, select, input, textarea, span, li, div, button, a {
        font-family: 微软雅黑 !important;
    }
</style>
</head>
<body data-spy="scroll" data-target="#myScrollspy" style="background: #f1f4f8">

<%--        <div id="headDiv">
            <jsp:include page="menu.jsp" />
        </div>--%>
<%@include file="mymodal.jsp" %>
<div id="contentDiv" style="text-align: left">

    <div class="container">

        <div class="row" style="margin-left: 0px">
            <div class="col-xs-3" id="myScrollspy">
                <!-- 附加导航区域 -->
                <ul id="myAffix" class="nav nav-pills nav-stacked" style="text-align:center;padding-bottom: 20px">
                </ul>

                <div id="userInfo" class="nav nav-pills "
                     style="text-align:left;padding-bottom: 20px;padding-top: 20px;font-family:微软雅黑;color: #333333;background: #ffffff; position: fixed;top: 200px;">
                    <div style="overflow: hidden"><label style="margin-left: 20px;float: left;">用户名:</label><span
                            id="userName" style="color:#99c7c9;margin-left: 5px;float: left;"></span></div>
                    <div style="overflow: hidden"><label style="margin-left: 20px;float: left;">真实姓名:</label><span
                            id="realName" style="color:#99c7c9;margin-left: 5px;float: left;"></span></div>
                    <div style="overflow: hidden"><label style="margin-left: 20px;float: left;">身份证号:</label><span
                            id="identityNo" style="color:#99c7c9;margin-left: 5px;float: left;"></span></div>
                    <div style="overflow: hidden"><label style="margin-left: 20px;float: left;">考试时长:</label><span
                            id="testTime" style="color:#99c7c9;margin-left: 5px;float: left;"></span></div>
                    <div style="overflow: hidden"><label style="margin-left: 20px;float: left;">试卷题数:</label><span
                            id="testNo" style="color:#99c7c9;margin-left: 5px" ;float: left;></span></div>
                    <div style="overflow: hidden"><label style="margin-left: 20px;float: left;">考生答题:</label><span
                            id="userTestNo" style="color:#ffa500;margin-left: 5px;float: left;"></span></div>
                    <div style="overflow: hidden"><label style="margin-left: 20px;float: left;">考生漏答:</label><span
                            id="noAnswer" style="color:#ffa500;margin-left: 5px;float: left;"></span></div>
                    <div style="overflow: hidden"><label style="margin-left: 20px;float: left;">试卷总分:</label><span
                            id="markTotal" style="color:#99c7c9;margin-left: 5px;float: left;"></span></div>
                    <div style="overflow: hidden"><label style="margin-left: 20px;float: left;">考生得分:</label><span
                            id="userMark" style="color:#4dbd73;margin-left: 5px;float: left;"></span></div>
                </div>
            </div>

            <div class="col-lg-9">
                <!-- 基本信息 -->
                <div class="col-lg-12 right_details" style="margin-top:30px;margin-left: 260px;width: 100%;padding: 0;"
                     id="section-0">
                    <form class="form-horizontal" style="width:95%;margin:0 auto;">
                        <ol class="breadcrumb itemOl"
                            style="width: 676px;height: 30px;margin-top: 15px;font-size: 18px;font-weight: bold;">
                            <li class="active"><span id="testPaperName" class="baseInfo"></span></li>
                        </ol>
                        <ol class="breadcrumb itemOl" id="testPaperDescriptionObj"
                            style="width: 676px;height: 100px;margin-bottom: 20px;display: none">
                            <li class="active"><span id="testPaperDescription" class="baseInfo"></span></li>
                        </ol>
                        <ol class="breadcrumb itemOl" id="testPaperExplainOl"
                            style="width: 676px;height: 100px;margin-bottom: 20px;display: none">
                            <li class="active"><span id="testPaperExplain" class="baseInfo"></span></li>
                        </ol>
                    </form>
                </div>
                <!-- 题目信息 -->
                <div id="itemDiv" style="width: 100%;margin-left: 260px"></div>
            </div>
        </div>
    </div>

    <!-- 弹出框模态框 -->
    <div class="modal fade hide" id="warnModal"><!-- fade是设置淡入淡出效果 -->
        <div class="modal-dialog" style="width: 360px;height:186px; margin-top: 150px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button class="btn btn-default close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title" style="font-weight:bold">提示框</h4>
                </div>
                <div class="modal-body" style="height: 75px;">
                    <form class="form-horizontal">
                        <span id="warnTip"></span>
                    </form>
                </div>
                <div class="modal-footer" id="modal-footer">
                    <button class="btn btn-primary" data-dismiss="modal"
                            style="background-color: #4dbd73;border:0;width: 60px;height: 30px">确定
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>