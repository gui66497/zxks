<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <link href="../images/favicon.ico" rel="icon" type="image/x-icon"/>
    <title>在线考试系统--登录页面</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="/zxks/css/login.css" rel='stylesheet' type='text/css'/>
    <script src="/zxks/exts/jquery-1.12.3.min.js"></script>
    <script src="/zxks/exts/jquery.min.js"></script>
    <script type="text/javascript">

        //回车键查询事件
        function keyDown(){
            //13是键盘上面固定的回车键
            if (event.keyCode == 13) {
               document.getElementById("loginBtn").click();
            }
        }

        function getQueryString(name){
            var reg = new RegExp("(^|&)"+name+"=([^&]*)(&|$)","i");
            var r = window.location.search.substr(1).match(reg);
            if ( r!= null) {
                return unescape(r.input.substr(4));
            }
            return null;

        }
        var randomCode = "";
        $(document).ready(function () {
            getImageAndCode()
            keyDown(event);
            //获取验证码图形
//            getImageAndCode();
            $("#userName").focus();
        });

        //用户登录
        function userLogin() {
            $("#codeSpan")[0].innerHTML="";
            var userName = $("#userName").val();
            var password = $("#password").val();
            var imageCode = $("#imageCode").val();
            $("#codeSpan").show();
            $("#codeSpan").delay(2000).hide("fast");

            if(userName==""){
                $("#codeSpan")[0].innerHTML="用户名不能为空！";
                getImageAndCode();
                $("#userName").focus();
                return false;
            }
            if(password==""){
                $("#codeSpan")[0].innerHTML="密码不能为空！";
                getImageAndCode();
                $("#password").focus();
                return false;
            }
            if(imageCode==""){
                $("#codeSpan")[0].innerHTML="验证码不能为空！";
                getImageAndCode();
                $("#imageCode").focus();
                return false;
            }
            $.ajax({
                contentType: "application/json",
                dataType: "json",
                type: "post",
                url: "/zxks/rest/userLogin/login",
                headers: {'userId': '10'},
                data: JSON.stringify({
                    user:{
                        userName: userName,
                        passWord: password
                    },
                }),

                success: function (data) {
                    var dataList = data.data;
                    if(dataList==null){
                        if(data.message=="1"){
                            $("#codeSpan")[0].innerHTML="用户名不存在！";
                            $("#userName").focus();
                        }else if(data.message=="2"){
                            $("#codeSpan")[0].innerHTML="密码不正确！";
                            $("#password").focus();
                        }

                      getImageAndCode();
                        return false;
                    }

                    //验证码校验
                    if (imageCode.toLowerCase() == randomCode.toLowerCase()) {
                        if (dataList[0].roleId == 0) {
                            //普通用户登录
                                window.location.href= "/zxks/page/myTest.jsp";
                        } else if (dataList[0].roleId == 1) {
                            var url = getQueryString("url");
                            //管理员登录
                            if(url!=null){
                                window.location.href = "/zxks/page"+url;
                            }else{
                                window.location.href= "/zxks/page/testManage.jsp";
                            }

                        }
                    } else {
                        $("#codeSpan")[0].innerHTML="验证码不正确！";
                        $("#imageCode").focus();
                        getImageAndCode();
                        return false;
                    }
                },
            });
        }

        //获取验证码图形
        function getImageAndCode() {
            $.ajax({
                dataType: "json",
                type: "get",
                url: "/zxks/rest/imageCode/execute",
                headers: {'userId': '10'},
                data: {},
                success: function (data) {
                    var dataList = data.data;
                    if (dataList.length == 2) {
                        var randomImg = dataList[0];
                        $("#imagePre")[0].src = randomImg;
                        randomCode = dataList[1];
                    }
                }
            });
        }
    </script>
</head>
<body onkeydown="keyDown()">

<div class="logo">
    <img src="/zxks/images/logo.png"/>

</div>
<div class="login-form">
<input type="text" class="text" name="userName" id="userName" placeholder="输入用户名">
<input type="password" name="password" id="password" placeholder="输入密码">

    <div style="height:38px">
    <input type="text" name="imageCode" id="imageCode" placeholder="输入验证码" >
        <img id="imagePre" src=""/>
    </div>
    <div class="warningDiv" id="warningDiv" ><span class="codeSpan" id="codeSpan"></span></div>
    <p class="codeChange">
        <span><a href="#"  onclick="getImageAndCode()">看不清，换一张</a></span>
    </p>

    <input type="submit" id="loginBtn" onclick="userLogin()"
           value="登     录">
        <p class="register_text">
            没有帐号？立即<a href="/zxks/page/userRegister.jsp" >注册</a>
        </p>
</div>
</body>
</html>