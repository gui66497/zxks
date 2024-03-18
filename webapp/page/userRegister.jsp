<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <link href="../images/favicon.ico" rel="icon" type="image/x-icon"/>
    <title>在线考试系统--注册页面</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="../css/userRegister.css" rel='stylesheet' type='text/css'/>
    <link href="../exts/bootstrap/css/bootstrap.css" rel='stylesheet' type='text/css'/>
    <link rel="stylesheet" type="text/css" href="../exts/bootstrap/css/bootstrap-modal.css">
    <link href="../css/bootstrap-datetimepicker.css" rel='stylesheet' type='text/css'/>
    <link href="../css/bootstrap-datetimepicker.min.css" rel='stylesheet' type='text/css'/>
    <script src="../exts/jquery-1.12.3.min.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/jquery-ui.min.js"></script>
    <script src="../exts/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/bootstrap/js/bootstrap-modal.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/bootstrap/js/bootstrap-modalmanager.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/modalTemplate.js"></script>
    <script src="../exts/bootstrap-datetimepicker.js"></script>
    <script src="../exts/bootstrap-datetimepicker.min.js"></script>
    <script src="../exts/bootstrap-datetimepicker.zh-CN.js"></script>

    <script type="text/javascript">

        //加载日期控件
        $(function(){
            var date = new Date();
            date.getDate();
            $(".form_datetime").datetimepicker({
                language:'zh-CN',
                format:'yyyy-mm-dd',
                minView:2,
                autoclose:true,
                endDate:date,
                forceParse:true
            });

            $(".form_date").datetimepicker({
                language:'zh-CN',
                format:'yyyy-mm-dd',
                minView:2,
                autoclose:true,
                endDate:date,
                forceParse:true
            });

            $("#okModal").draggable({
                containment: '.registerTab'
            });

        })

        //必填项校验
        function validCheck(){
            var userName = $("#nameLabel").val();
            var password = $("#pwdInput").val();
            var comPassword = $("#confirmInput").val();
            var realName = $("#rNameInput").val();
            var gender = $(' input[type="radio"]:checked').val();
            var userNameReg =/(^[A-Za-z0-9]{6,15}$)/;//用户名正则
            var realNameReg =/^[\u4e00-\u9fa5]{2,5}$|^[a-zA-Z]{2,10}$/;//真实姓名正则
            var pwdReg = /^[A-Za-z0-9_]{6,18}$///密码正则
            $("#warningReg").show();
            $("#warningReg").delay(2000).hide("fast");
            var dataList =null;
/*            if(userName==""||password==""||comPassword==""||gender==""||realName==""){
                $("#registerBtn").attr("disabled",'disabled');
                $("#registerBtn").style.background='#b0b5b9'
            }*/
            //用户名校验
            $.ajax({
                type:"post",
                contentType:"application/json",
                dataType:"json",
                headers:{"userId":118},
                url:"/zxks/rest/userLogin/getUserByUserName",
                data:JSON.stringify({
                    user:{
                        userName:userName,
                    }
                }),
                success:function(data){
                     dataList = data.data;
                    $("#warningReg")[0].innerHTML="";
                        if(dataList!=null&&dataList.length>0){
                            showOkModal("提示框","用户名已存在");
                        //$("#warningReg")[0].innerHTML="用户名已存在!";
                        $("#nameLabel").focus();
                        return false;
                    }else{
                        if(userName==""){
                            showOkModal("提示框","用户名不能为空("+$("#nameLabel").attr("placeholder")+")!");
                            //$("#warningReg")[0].innerHTML="用户名不能为空("+$("#nameLabel").attr("placeholder")+")!";
                            $("#nameLabel").focus();
                            return false;
                        }else if(userName!=""&&userNameReg.test(userName)===false){
                            showOkModal("提示框","用户名格式不正确("+$("#nameLabel").attr("placeholder")+")!");
                            //$("#warningReg")[0].innerHTML="用户名格式不正确("+$("#nameLabel").attr("placeholder")+")!";
                            $("#nameLabel").focus();
                            return false;
                        }
                            //真实姓名校验
                            if(realName==""){
                                showOkModal("提示框","真实姓名不能为空");
                                //$("#warningReg")[0].innerHTML="真实姓名不能为空!";
                                $("#rNameInput").focus();
                                return false;
                            }else if(realName!=""&&realNameReg.test(realName)===false){
                                showOkModal("提示框","真实姓名格式不正确");
                                //$("#warningReg")[0].innerHTML="真实姓名格式不正确!";
                                $("#rNameInput").focus();
                                return false;
                            }
                            if(!gender || gender==""){
                                showOkModal("提示框","性别不能为空");
                                //$("#warningReg")[0].innerHTML="性别不能为空!";
                                $(' input[type="radio"]:checked').focus();
                                return false;
                            }

                        //密码校验
                        if(password==""){
                            showOkModal("提示框","密码不能为空("+$("#pwdInput").attr("placeholder")+")!");
                            //$("#warningReg")[0].innerHTML="密码不能为空("+$("#pwdInput").attr("placeholder")+")!";
                            $("#pwdInput").focus();
                            return false;
                        }else if(password!=""&&pwdReg.test(password)===false){
                            showOkModal("提示框","密码格式不正确("+$("#pwdInput").attr("placeholder")+")!");
                            //$("#warningReg")[0].innerHTML="密码格式不正确("+$("#pwdInput").attr("placeholder")+")!";
                            $("#pwdInput").focus();
                            return false;
                        }
                        if(comPassword==""){
                            showOkModal("提示框","确认密码不能为空");
                            //$("#warningReg")[0].innerHTML="确认密码不能为空!";
                            $("#confirmInput").focus();
                            return false;
                        }
                        if(password!=comPassword){
                            showOkModal("提示框","两次输入密码不匹配");
                            //$("#warningReg")[0].innerHTML="两次输入密码不匹配!";
                            $("#confirmInput").focus();
                            return false;
                        }

                        userRegister();

                    }
                }
            })




        }

/*        //弹出消息提醒框
        function showOkModal(label, content) {
            $("#okModalLabel")[0].innerHTML = label;
            $("#okModalMessage")[0].innerHTML = content;
            $("#confirmModal").modal({
                show : true
            });
        }*/

        //用户注册
        function userRegister(){
            var userName = $("#nameLabel").val();
            var password = $("#pwdInput").val();
            var realName = $("#rNameInput").val();
            var gender = $(' input[type="radio"]:checked').val();
            var company = "";
            var dept = "";
            var email = $("#emailInput").val();
            var identity = $("#identityInput").val();
            var birth = $("#birthInput").val();
            var position = $("#jobInput").val();
            var education = $("#educationInput").val();
            var graduateSchool = $("#universityInput").val();
            var graduateDate =$("#graducateTime").val();
            var major = $("#majorInput").val();
            var resumeUrl = $("#resumeUrl").val();
            var tel = $("#telInput").val();
            var mailReg = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;//邮箱正则
            var identityReg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;//身份证号正则
            var telReg = /^1[3|4|5|7|8][0-9]\d{4,8}$/;//电话号码正则



            //邮箱校验
            if(email!=""&& mailReg.test(email)===false){
                showOkModal("提示框","邮箱格式不正确("+ $("#emailInput").attr("placeholder")+")!");
               //$("#warningMail")[0].innerHTML="邮箱格式不正确("+ $("#emailInput").attr("placeholder")+")!";
                $("#emailInput").focus();
                return false;
            }
            //身份证号校验
            if(identity!=""&& identityReg.test(identity)===false){
                showOkModal("提示框","身份证号格式不正确("+ $("#identityInput").attr("placeholder")+")!");
               //$("#warningIdentity")[0].innerHTML="身份证号格式不正确("+ $("#identityInput").attr("placeholder")+")!";
                return false;
            }
            //电话号码校验
            if(tel!=""&&telReg.test(tel)===false){
                showOkModal("提示框","电话号码格式不正确("+ $("#telInput").attr("placeholder")+")!");
                return false;
            }

            if (birth.trim().length == 0) {
                birth = undefined;
            }
            if (graduateDate.trim().length == 0) {
                graduateDate = undefined;
            }
            if (graduateDate && birth) {
                var dStart = new Date(birth.replace(/\-/g, "\/"));
                var dEnd = new Date(graduateDate.replace(/\-/g, "\/"));
                if (dStart >= dEnd) {
                    //$("#warningReg")[0].innerHTML =  "出生时间应小于毕业时间！";
                    showOkModal("提示框","出生时间应小于毕业时间！");
                    return;
                }
            }
            $.ajax({
                contentType:"application/json",
                dataType: "json",
                type: "post",
                url: "/zxks/rest/user/add",
                headers: {'userId': '10'},
                data: JSON.stringify({
                    user:{
                        userName: userName,
                        passWord: password,
                        realName: realName,
                         gender: gender,
                         company: company,
                         dept: dept,
                         email: email,
                         idCardNo: identity,
                         birth: birth,
                         position: position,
                         education: education,
                         graduateSchool:graduateSchool,
                         graduateDate:graduateDate,
                         major:major,
                         resumeUrl:resumeUrl,
                         tel:tel
                    }

                }),
                success:function(resultData){
                    if (resultData != null) {
                        if (resultData.resultCode == 'SUCCESS') {
                            //跳转至登录界面
                            showOkModal("提示框","注册成功，即将跳转到登录界面！");
                            setTimeout(function(){
                                window.location.href ="/zxks/page/login.jsp";
                            },2000);
                        } else {
                            showOkModal("提示框",resultData.message);
                        }
                    }
                }
            })
        }
    </script>
    <style>
.modal-backdrop, .modal-backdrop.fade.in{
    background: #333;
}
span,th,td,input,select,label,div,button{
    font-family:微软雅黑 !important;
}

    </style>
</head>

<body>
<%@include file="mymodal.jsp"%>
    <div class="registerTab">
        <div class="requiredField">
            <div class="title" >用户注册&nbsp(<span class="requiredLb">*</span>为必填项)</div>
            <span class="requiredLb">*</span><label class="nameLabel">用户名:</label>
            <input type="text" class="userName" id="nameLabel" maxlength="15" placeholder="6-15位字母或数字"><br>
            <span class="requiredLb">*</span><label class="realName">真实姓名:</label>
            <input type="text" class="rNameInput" id="rNameInput" maxlength="5" placeholder="2-5位中文或2-10位英文"><br>
            <span class="requiredLb">*</span><label class="genderLabel">性别:</label>
            <input type="radio" checked class="genderInput" name="genderInput" value="1" style="margin: 0px 0 0 20px;"><label style="float: left;position:absolute;width: 20px">男</label>
            <input type="radio" class="genderInput" name="genderInput" value="2" style="margin: 0px 0 0 30px;"><label style="float: left;position:absolute;width: 20px">女</label><br>
            <span class="requiredLb">*</span><label class="pwdLabel">密码:</label>
            <input type="password" class="pwdInput" id="pwdInput" maxlength="18" placeholder="6-18位数字、字母、下划线"><br>
            <span class="requiredLb">*</span><label class="confirmLabel">确认密码:</label>
            <input type="password" class="confirmInput" id="confirmInput" maxlength="18">
            <span class="warningSpan requiredSpan" id="warningReg"></span>
            <div id="userOperation">
                <input type="button" id="registerBtn" value="注册"  onclick="validCheck()">
                <!--   <input type="button" class="btBack" value="返回" onclick="history.go(-1)">-->
            </div>
        </div>
        <span class="separateLine"></span>
        <div class="optionalField">
            <div class="loginNext">已经注册了，请直接<a href="login.jsp">登录</a></div>
            <%--<label class="">单位:</label>
            <input type="text" class="companyInput" id="companyInput" maxlength="15"><br>
            <label class="">部门:</label>
            <input type="text" class="departmentInput" id="departmentInput" maxlength="10"><br>--%>
            <label class="">邮箱:</label>
            <input type="text" class="emailInput" id="emailInput" maxlength="30" placeholder="例如：12345abc@163.com">
            <span class="warningSpan" id="warningMail"></span><br>
            <label class="">身份证号:</label>
            <input type="text" class="identityInput" id="identityInput" maxlength="18" placeholder="15位或18位或17X(数字)" >
            <span class="warningSpan" id="warningIdentity"></span><br>
            <label class="">出生年月:</label>
            <div class="input-group date form_datetime col-md-2" style="margin-left: 48px;position: relative">
                <input type="text" id="birthInput" maxlength="12">
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-th icon-th"></span>
                    </span>
            </div>

            <label class="">职位:</label>
            <input type="text" class="jobInput" id="jobInput" maxlength="15" ><br>
            <label class="">学历:</label>
            <select class="educationInput" id="educationInput">
                <option>专科</option>
                <option>本科</option>
                <option>硕士</option>
                <option>博士</option>
                <option>其它</option>
            </select><br>
            <label class="">毕业院校:</label>
            <input type="text" class="universityInput" id="universityInput" maxlength="15"><br>
            <label class="">毕业时间:</label>
            <div class="input-group date form_date col-md-5" style="margin-left: 48px;position: relative">
                <input type="text" class="graducateTime" id="graducateTime" maxlength="12" style="cursor: pointer">
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-th icon-th"></span>
                    </span>
            </div>

            <label class="" >专业:</label>
            <input type="text" class="majorInput" id="majorInput" maxlength="15"><br>
            <label class="">简历地址:</label>
            <input type="text" class="resumeUrl" id="resumeUrl" maxlength="30"><br>
            <label class="">手机号码:</label>
            <input type="text" class="telInput" id="telInput" maxlength="11" placeholder="11位数字" >
            <span class="warningSpan" id="warningTel"></span><br>
        </div>
    </div>


<!-- 弹出框模态框 -->
<%--<div class="modal fade dialog hide" id="confirmModal" style="top:250px;"><!-- fade是设置淡入淡出效果 -->
    <div class="modal-dialog category">
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
                <button class="cancel" data-dismiss="modal" style="width:70px;margin-right: 15px">关闭</button>
            </div>
        </div>
    </div>
</div>--%>

</body>
</html>