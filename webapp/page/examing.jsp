<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <link href="../images/favicon.ico" rel="icon" type="image/x-icon"/>
    <title>考试界面</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8;IE=edge" />
    <title>在线考试系统--试卷管理</title>
    <link rel="stylesheet" type="text/css" href="../exts/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="../css/examing.css">
    <%--<link rel="stylesheet" type="text/css" href="css/base.css">--%>
    <script src="../exts/jquery.min.js"></script>

    <script src="../exts/bootstrap/js/bootstrap.min.js"></script>
    <script src="../exts/bootstrap-hover-dropdown.min.js"></script>

    <script src="../exts/icheck.min.js"></script>

    <script src="../exts/bootstrap-treeview.js"></script>

    <script type="text/javascript">
        var itemIDList = [];
        var allItemList = [];
        /*var testInfoId = <%=request.getParameter("testInfoId")%>;//考试信息ID
        var endTime ="<%=request.getParameter("endTime")%>";//考试结束时间*/

        var testInfoId = sessionStorage.getItem('testInfoId');//考试信息ID
        var endTime = sessionStorage.getItem('endTime');//考试结束时间
        var userId = <%=request.getSession().getAttribute("userId")%>;//用户ID
        var userName = '<%=request.getSession().getAttribute("userName")%>';//用户名
        var optionNames = ['A','B','C','D','E','F'];
        $(document).ready(function(){
            $('input').iCheck({
                checkboxClass: 'icheckbox_square-blue',  // 注意square和blue的对应关系
                radioClass: 'iradio_square-blue',
                increaseArea: '20%' // optional
            });
            $.ajax({
                url: "/zxks/rest/testPaper/allTestPaperInfo",
                type: "post",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: userId},
                data: JSON.stringify({"id":testInfoId}),
                success: function (resultData) {
                    if(resultData != null){
                        var testPaperPartList = resultData.data;
                        if(testPaperPartList == null || testPaperPartList.length == 0){
                            return;
                        }
                        var myAffixHtml='<li class="active" id="myAffixLi0" name="myAffixLi" style="margin: 20px 20px 0 20px"><a style="height: 21px;line-height: 21px" onclick="getSection(0)">基本信息</a></li>';
                        var itemHtml = '';
                        var testTime = 0;
                        $.each(testPaperPartList,function(index,testPaperPart){
                            var partNo = Number(index+1);
                            myAffixHtml += '<li id="myAffixLi'+Number(index+1)+'" name="myAffixLi" style="margin: 6px 20px 0 20px"><a style="height: 21px;line-height: 21px" onclick="getSection('+Number(index+1)+')" ">第'+partNo+'部分：'+testPaperPart.partName+'</a></li>';
                            var itemList = testPaperPart.item;
                            itemHtml += '<div class="col-lg-12 right_details" style="margin:20px 0 20px 0;padding:0" id="section-'+partNo+'">';
                            itemHtml += '<form class="form-horizontal" style="width:95%;margin:0 auto;">';
                            itemHtml += '<ol class="breadcrumb itemOl" style="width: 676px;margin-top: 15px;margin-left: 2px">';
                            itemHtml += '<li class="active" style="font-size: 16px;color: #4dbd73">第'+partNo+'部分：'+testPaperPart.partName+'（共'+testPaperPart.partItemCounts+'道,每道'+Number(testPaperPart.partMark/testPaperPart.partItemCounts)+'分,共'+testPaperPart.partMark+'分）</li>';
                            itemHtml += '</ol>';
                            $.each(itemList,function(itemIndex,item){
                                itemIDList.push(item.itemId);
                                allItemList.push(item);
                                itemHtml += '<div class="form-group" style="margin-bottom: 20px;border: 1px solid #ccc;border-radius: 4px;padding-bottom: 15px">';
                                itemHtml += '<div class="panel-group">';
                                itemHtml += '<div class="panel panel-default">';
                                itemHtml += '<fieldset class="panel-heading" style="font-family:微软雅黑;width: 683px;padding: 0px 10px 10px 10px ">';
                                itemHtml += '<legend class="panel-title" id="itemTitle'+item.itemId+'" style="width:100%;font-family:微软雅黑;font-size:14px;font-weight:bold;border-bottom:0;margin-bottom:0">第'+Number(itemIndex+1)+'题：</legend>'+item.itemTitle+'';
                                itemHtml += '<input id="item'+item.itemId+'" value='+item.itemId+' style="display: none">';//隐藏题目ID
                                itemHtml += '<input id="answer'+item.itemId+'" value='+item.answer+' style="display: none">';//隐藏题目答案
                                itemHtml += '<input id="mark'+item.itemId+'" value='+Number(testPaperPart.partMark/testPaperPart.partItemCounts)+' style="display: none">';//隐藏每题的分数
                                if(item.itemCode.trim() != ""){
                                    itemHtml += '<div id="itemCodeDiv" style="padding: 10px 20px 10px 20px"><pre>'+item.itemCode+'</pre></div>';
                                }
                                itemHtml += '</fieldset>';
                                itemHtml += '<div id="collapseOne" class="panel-collapse collapse fade in">';
                                itemHtml += '<div class="panel-body">';
                                var optionList = item.optionList;
                                if (item.itemType == 5) {//填空题
                                    $.each(optionList,function(optionIndex,option){
                                        itemHtml += '<div class="form-group" style="margin-top: 10px;">';
                                        itemHtml += '<div class="radio col-sm-1" style="text-align:left;padding-left: 0px;padding-right: 0px;width: 45px;float: left;margin-top: 4px">';
                                        itemHtml += '<span style="margin-left: 30px;margin-right: 20px;" name="option'+item.itemId+'" value="'+option.optionId+'"/>';
                                        itemHtml += '<span class="form-control option" readonly="readonly" style="font-family:微软雅黑;margin-left: 10px;white-space: nowrap;">'+'填空'+(++optionIndex)+'</span>';
                                        itemHtml += '</div>';
                                        itemHtml += '<div class="col-sm-11" style="padding-left: 0px;margin-left: 20px;width: 602px;height: 30px;line-height:30px;margin-right: 20px;font-family:微软雅黑;margin-left: 60px;border-radius: 4px;">';
                                        itemHtml += '<input type="text" class="form-control option" name="option'+item.itemId+'" style="font-family:微软雅黑;margin-left: 10px;width: 86%;">'+'</input>';
                                        itemHtml += '</div>';
                                        itemHtml += '</div>';
                                    });
                                } else if (item.itemType == 4) { //简答题
                                    var option = optionList[0];
                                    itemHtml += '<div class="form-group" style="margin-top: 10px;">';
                                    itemHtml += '<div class="col-sm-11" style="margin-left: 40px;width: 602px;height: 60px;line-height:30px;margin-right: 20px;font-family:微软雅黑;border-radius: 4px;">';
                                    itemHtml += '<textarea class="form-control option" id="textarea'+item.itemId+'" style="font-family:微软雅黑;width: 578px;height: 49px"></textarea>';
                                    itemHtml += '</div>';
                                    itemHtml += '</div>';
                                } else {
                                    $.each(optionList,function(optionIndex,option){
                                        itemHtml += '<div class="form-group" style="margin-top: 10px;">';
                                        itemHtml += '<div class="radio col-sm-1" style="text-align:left;padding-left: 0px;padding-right: 0px;width: 43px;float: left;">';
                                        if(item.itemType == 2){
                                            itemHtml += '<input type="checkbox" style="margin-left: 30px;margin-right: 20px;" name="option'+item.itemId+'" value="'+option.optionId+'"/>';
                                        }else{
                                            itemHtml += '<input type="radio" style="margin-left: 30px;margin-right: 20px;" name="option'+item.itemId+'" value="'+option.optionId+'"/>';
                                        }
                                        itemHtml += '</div>';
                                        itemHtml += '<div class="col-sm-11" style="padding-left: 0px;margin-left: 20px;width: 602px;height: 30px;line-height:30px;margin-right: 20px;font-family:微软雅黑;margin-left: 60px;background: #f7f7f7;border-radius: 4px;">';
                                        itemHtml += '<span class="form-control option" readonly="readonly" style="font-family:微软雅黑;margin-left: 10px">'+optionNames[Number(optionIndex)]+'：'+option.content+'</span>';
                                        itemHtml += '</div>';
                                        itemHtml += '</div>';
                                    });
                                }
                                itemHtml += '</div>';
                                itemHtml += '</div>';
                                itemHtml += '</div>';
                                itemHtml += '</div>';
                                itemHtml += '</div>';
                            });
                            itemHtml += '</form>';
                            itemHtml += '</div>';
                            var testPaper = testPaperPart.testPaper;
                            if(testPaper != null){
                                $('#testTime').text(testPaper.testTime);
                                testTime = testPaper.testTime;
                                $('#markTotal').text(testPaper.markTotal);
                                $('#overMark').text(Number(testPaper.markTotal)*0.6);
                                $('#testPaperName').text(testPaper.testpaperName);
                                if (testPaper.testpaperExplain) {
                                    $('#testPaperExplainOl').show();
                                    $('#testPaperExplain').text(testPaper.testpaperExplain);
                                }

                            }

                        });
                        myAffixHtml += '<div class="col-sm-6"><button class="btn btn-warning" data-toggle="modal" onclick="finishTest(0)" data-backdrop="static" style="font-family:微软雅黑;border: 0;height: 34px;width: 220px;text-shadow: none">检测答卷</button></div>';
                        myAffixHtml += '<div class="col-sm-6"><button class="btn btn-success" onclick="finishTest(1)" style="font-family:微软雅黑;border: 0;height: 34px;width: 220px;text-shadow: none">提交试卷</button></div>';
                        myAffixHtml += '<div class="col-sm-12" style="float:left;margin:20px 0 0 50px;font-family:微软雅黑;">';
                        myAffixHtml += '<p style="color:#ff6c60;font-size: 16px;font-weight: bold;"><span id="timeShow"></span></p></div>';
                        $('#myAffix').text('');
                        $('#myAffix').append(myAffixHtml);//左边导航树加载
                        $('#itemDiv').text('');
                        $('#itemDiv').append(itemHtml);//添加题目信息
                        timer(testTime);
                    }
                },
                error: function (xhr, r, e) {
                    showOkModal('提示框',e);
                }
            });
        });
        function getSection(id){
            window.location.href="#section-"+id;
            var elements = $('li[name="myAffixLi"]');
            $.each(elements,function(index,element){
                if(id == index){
                    $('#myAffixLi'+index).attr('class','active');
                }else{
                    $('#myAffixLi'+index).removeClass();
                }

            });
        }
        // type代表按钮方法，0代表检测试卷，1代表提交试卷
        function finishTest(type){
            //var testInfoId = $('#testInfoID').val();//考试信息ID
            var achievementId = new Date().getTime();//生成成绩ID
            var totalMark = 0;//总分
            var count = 0;//未完成条数
            var answers = [];
            var resultInfo = [];//回答结果信息列表
            var resultAndAnswerInfo = [];//回答结果信息列表
            var achievementInfo;//分数信息列表
            var achievement = {};
            var optionNames = ['A','B','C','D','E','F'];


            $.each(allItemList,function(index,item){
                var itemID = item.itemId;
                var result;
                if (item.itemType == 5) {
                    var inputs = $("input[name='option"+item.itemId+"']");
                    console.log(inputs);
                    var answerArr = [];
                    var isBlank = false;
                    $.each(inputs, function (i, input) {
                        var value = input.value.trim();
                        if (value == '' || value == undefined) {
                            result = '';
                            answerArr = [];
                            return false;
                        } else {
                            answerArr.push(value);
                        }
                    });
                    result = answerArr.join('░');


                } else if (item.itemType == 4) {   //简答题
                    result = $('#textarea'+item.itemId).val();
                } else {
                    result = $('input[type="radio"][name="option'+itemID+'"]:checked').val();
                }

                if(result == undefined){
                    result = '';
                    $('input[type="checkbox"][name="option'+itemID+'"]:checked').each(function(index,element){
                        result += $(element).val()+",";
                    });
                    if(result != '' && result.indexOf(",") != -1){
                        result = result.substr(0,result.lastIndexOf(","));
                    }
                }
                if(result == ''){
                    count ++ ;
                    if(type != 2){  //到时间
                        $('#itemTitle'+itemID).css("color", "red")
                    }
                } else {
                    $('#itemTitle'+itemID).css("color", "#333")
                }
                var itemInfo = {};
                itemInfo.answer = item.answer;
                resultAndAnswerInfo.push({
                    "result":result,
                    "item":itemInfo
                });
                //$('#itemTitle'+itemID).attr("style","color:#333");
                answers.push({
                    "itemId":itemID,
                    "type":item.itemType,
                    "result":result,
                    "answer":item.answer,
                    "value":$('#mark'+itemID).val()
                });
                resultInfo.push({
                    "achievementId":achievementId,
                    "itemId":itemID,
                    "result":result
                });
                //改为后台计算每题分数
                //计算分数
                var answer = item.answer;
                /*var mark = $('#mark'+itemID).val();
                 if(answer.toLowerCase() == result.toLowerCase()){
                 totalMark = Number(totalMark) + Number(mark);
                 }*/

            });

            console.log(answers);
            if(type != 2){
                if(count > 0){
                    $('#warnTip').text('还有'+count+'题未完成');
                    $('#warnModal').modal(
                            {
                                show:true,
                                keyboard:false,
                                backdrop:'static'
                            }
                    );
                    return false;
                }
            }
            achievement.answers = answers;

            //后台计算成绩
            $.ajax({
                url: "/zxks/rest/achievement/mark",
                type: "POST",
                contentType: "application/json; charset=UTF-8",
                async: false,   //同步
                dataType: "json",
                headers: {userId: userId},
                data: JSON.stringify({"achievement":achievement}),
                success: function (resultData) {
                    console.log(resultData);
                    if (resultData.resultCode == "SUCCESS") {
                        var datas = resultData.data;
                        $.each(datas, function (i, data) {
                            totalMark += data.score;
                            $.each(resultInfo, function (j, result) {
                                if (result.itemId == data.itemId) {
                                    result.score = data.score;
                                    return false;
                                }
                            });
                        });
                        console.log("总分" + totalMark);
                        console.log(resultInfo);

                    } else {
                        alert("计算总分出错！");
                    }
                },
                error: function (xhr, r, e) {

                }
            });

            // type代表按钮方法，0代表检测试卷，1代表提交试卷
            if(type == 0){
                $('#warnTip').text('已做完所有题目，可提交考试！');
                $('#warnModal').modal(
                        {
                            show:true,
                            keyboard:false,
                            backdrop:'static'
                        }
                );
                return true;
            }


            achievementInfo = {
                "testInfoId":testInfoId,
                "achievementId":achievementId,
                "userMark":totalMark,
                "results":resultInfo
            };
            console.log(achievementInfo);
            //return;
            //alert(totalMark);
            //alert(JSON.stringify(achievementInfo));
            $.ajax({
                url: "/zxks/rest/achievement/add",
                type: "POST",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: userId},
                data: JSON.stringify({"achievement":achievementInfo}),
                success: function (resultData) {
                    var message = '';
                    if(resultData != null){
                        if(type == 2){
                            message = "考试时间结束,感谢您参加本次考试!";
//                            window.location.href="myTest.jsp";


                        }else{

                            message = "提交成功，感谢您参加本次考试!";
//                            window.location.href="myTest.jsp";

                        }
                    }else{
                        message = '系统错误!请联系管理员';

                    }
                    $('#modal-footer').text('');
                    $('#modal-footer').append('<button class="btn btn-primary" data-dismiss="modal" style="background-color: #4dbd73;border:0;width: 60px;height: 30px" onclick="toMyTest()">确定</button>');
                    $('#warnTip').text(message);
                    $('#warnModal').modal(
                            {
                                show:true,
                                keyboard:false,
                                backdrop:'static'
                            }
                    );
                   setTimeout(function(){
                       window.close();
                   },2000)

                },
                error: function (xhr, r, e) {
                    $('#modal-footer').text('');
                    $('#modal-footer').append('<button class="btn btn-primary" data-dismiss="modal" style="background-color: #4dbd73;border:0;width: 60px;height: 30px" onclick="toMyTest()">确定</button>');
                    $('#warnTip').text('系统错误!请联系管理员');
                    $('#warnModal').modal(
                            {
                                show:true,
                                keyboard:false,
                                backdrop:'static'
                            }
                    );
                    window.location.href="myTest.jsp";

                }
            });

        }
        //计算并取得用户答题时间,采用定时器
        function timer(intDiff) {
            var date = new Date().getTime()/1000/60;
            var dEnd = new Date(endTime.replace(/\-/g, "\/")).getTime()/1000/60;
            var currentTime = 0;
            if(dEnd-date >intDiff){
                 currentTime = intDiff;
            }else{
                currentTime = dEnd-date;
            }
            currentTime = Number(currentTime*60);
           // var timeV= $.cookie("time");

//            if(timeV!=null) {
//                intDiff=timeV;
//            }
            var timer = window.setInterval(function(){
                var day = 0;
                var hour = 0;
                var minute= 0;
                var second=0;//时间默认值
                if(currentTime > 0){

                    day = Math.floor(currentTime/(60*60*24));
                    hour = Math.floor(currentTime/(60*60)) - (day*24);
                    minute = Math.floor(currentTime/60) - (day*24*60) - (hour*60);
                    second = Math.floor(currentTime) - (day*24*60*60) - (hour*60*60) - (minute*60);
                }
                if(minute <= 9) minute = '0' + minute;
                if(second <=9) second = '0' + second;



                $('#timeShow').html('倒计时：'+hour+'时'+minute+'分'+second+'秒');
                currentTime--;
                if(currentTime < 0){
                    //alert('考试时间结束');
                    finishTest(2);//计算时间结束,自动提交试卷
                    clearInterval(timer);//清除定时器
                    return;
                }
            },1000);


        }





        document.onkeydown = function(e) { //IE 谷歌
            var code, type;
            if (!e) {
                var e = window.event;
            }
            if (e.keyCode) {
                code = e.keyCode;
            } else if (e.which) {
                code = e.which;
            }
            type = e.srcElement.type;
            if ((code == 116) //F5刷新
                    || (code == 8) //屏蔽退格删除键
                    || (code == 112) //屏蔽F1
                    || (code == 122)//屏蔽F11
            ) {
                e.keyCode = 0;
                e.returnValue = false;
            }
            if (e.altKey && ((code == 37) //屏蔽Alt+方向键←
                    || (code == 39) //屏蔽Alt+方向键→
                    || (code == 115))) { //屏蔽Alt+F4
                e.returnValue = false;
            }
            if (e.ctrlKey && ((code == 82) //Ctrl+R
                    || (code == 78)) //屏蔽Ctrl+n
            ) {
                e.returnValue = false;
            }
            if (e.shiftKey && ((code == 121) //屏蔽shift+F10
                    || (e.srcElement.tagName == "A"))//屏蔽shift加鼠标左键新开一网页
            ) {
                e.returnValue = false;
            }
            return false;
        }
        document.onkeypress = function(e) { //火狐
            var code = e.keyCode;
            if ((code == 116) //F5刷新
                    || (code == 8) //屏蔽退格删除键
                    || (code == 112) //屏蔽F1
                    || (code == 122) //屏蔽F12
            ) {
                return false;
            }
            if (e.altKey && ((code == 37) //屏蔽Alt+方向键←
                    || (code == 39) //屏蔽Alt+方向键→
                    || (code == 115))) { //屏蔽Alt+F4
                return false;
            }
            if (e.ctrlKey && (code == 0) //Ctrl+R
            ) {
                return false;
            }
            if (e.shiftKey && ((code == 121) //屏蔽shift+F10
                    || (e.srcElement.tagName == "A"))//屏蔽shift加鼠标左键新开一网页
            ) {
                return false;
            }
        }
        document.oncontextmenu = function(event) {
            event = event ? event : (window.event ? window.event : null);// ie firefox都可以使用的事件
            event.returnValue = false;
            return false;
        }





        function toMyTest(){
            window.location.href="myTest.jsp";
        }

    </script>
</head>
<body data-spy="scroll" data-target="#myScrollspy">
<div class="container">
    <div style="margin-top:20px;width: 1024px;">
        <ol class="breadcrumb" style="background: #ffffff;padding: 8px 0 8px 0;font-weight: bold">
            <li class="active" style="width: 1024px;">
                <label class="topLabel" style="width: 300px;margin-left: 15px;float: left;position: absolute;font-weight: bold">考试时间：<span id="testTime"></span>分钟</label>
                <label class="topLabel" style="width: 250px;text-align: center;float: left;margin-left: 250px;font-weight: bold">考生：<span id="userName"><%=request.getSession().getAttribute("userName")%></span></label>
                <label class="topLabel" style="width: 220px;text-align: center;margin-right: 15px;float: left;font-weight: bold">总分：<span id="markTotal"></span>分</label>
                <label class="topLabel" style="width: 150px;text-align: right; margin-right: 15px;float: left;font-weight: bold">通过分：<span id="overMark"></span>分</label>
            </li>
        </ol>
    </div>
    <div class="row" style="margin-left: 0px">
        <div class="col-xs-3" id="myScrollspy" >
            <!-- 附加导航区域 -->
            <ul id="myAffix" class="nav nav-pills nav-stacked" style="text-align:center;">
            </ul>
        </div>

        <div class="col-lg-9">
            <!-- 基本信息 -->
            <div class="col-lg-12 right_details" style="margin-top:20px;margin-left: 260px;width: 100%;padding: 0;" id="section-0">
                <form class="form-horizontal" style="width:95%;margin:0 auto;">
                    <ol class="breadcrumb itemOl" style="width: 676px;height: 30px;margin-top: 15px;margin-left: 2px;font-size: 18px;font-weight: bold;">
                        <li class="active"><span id="testPaperName" class="baseInfo">此处是考试名称</span></li>
                    </ol>
                    <ol class="breadcrumb itemOl" id="testPaperExplainOl" style="width: 676px;height: 100px;margin-bottom: 20px;margin-left: 2px;display: none">
                        <li class="active"><span id="testPaperExplain" class="baseInfo">此处是考试说明</span></li>
                    </ol>
                </form>
            </div>
            <!-- 题目信息 -->
            <div id="itemDiv" style="width: 100%;margin-left: 260px"></div>
        </div>
    </div>
</div>

<!-- 弹出框模态框 -->
<div class="modal fade dialog hide" id="warnModal" style="top:250px;"><!-- fade是设置淡入淡出效果 -->
    <div class="modal-dialog category">
        <div class="modal-content">
            <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true" style="color: #fff">
                </button>
                提示框
            </div>
            <div class="modal-body" style="padding:20px 20px;">
                <span id="warnTip"></span>
            </div>
            <div class="modal-footer">
                <button class="cancel" data-dismiss="modal" style="width:70px;margin-right: 15px" >关闭</button>
            </div>
        </div>
    </div>
</div>
</body>
    <script type="text/javascript">
        $('#myAffix').affix({
            offset : {
                top : 25,
            }
        });
    </script>
</html>