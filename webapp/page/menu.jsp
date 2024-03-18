<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="../images/favicon.ico" rel="icon" type="image/x-icon"/>
    <title>导航条</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script src="../exts/bootstrap/js/bootstrap-dropdown.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/jquery-ui.min.js"></script>
    <link href="../exts/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen"/>
    <link href="../images/favicon.ico" rel="shortcut icon" type="image/x-icon"/>
    <script type="text/javascript">
        var userName = '<%=request.getSession().getAttribute("userName")%>';
        var userId = <%=request.getSession().getAttribute("userId")%>;
        $(function () {
            //模态框拖动效果
            var strObj = "#userScoresModal,#peopleNameModal,#categoryAddModal,#categoryUpdateModal,#itemBankInfoAddModal"
                    + ",#itemBankInfoUpdateModal,#itemAddModal,#warnModal,#sureModal,#itemShowModal,#okModal,#confirmModal"
                    + ",#itemPersonalModal,#itemSelectModal,#itemDetailModal,#paper_add_modal,#students_select_modal"
                    + ",#new_company_modal,#update_company_modal,#new_department_modal,#update_department_modal,#new_user_modal2"
                    + ",#update_password_modal";
            $(strObj).draggable({containment: '#mainDiv'});


            //防止普通用户通过url进入管理员界面并设置权限
            var url = window.location.href;
            $("#navUl > li").hide();
            if (userName == "null" || userName == ""){
                window.location.href = "login.jsp";
            } else if (userName != 'admin') {
                $("#myTest").show();
                $("#myPoint").show();

                //防止用户进入管理员界面
                if (url.indexOf("page/myTest") > 0) {
                    return true;
                } else if (url.indexOf("page/myPoint") > 0) {
                    return true;
                } else if (url.indexOf("page/achievementDetail") > 0) {
                    return true;
                } else if (url.indexOf("page/examing") > 0) {
                    return true;
                } else if (url.indexOf("page/userCenter") > 0) {
                    return true;
                } else {
                    window.location.href = "login.jsp";
                }
            } else{
                //防止管理员进入用户界面
                if (url.indexOf("page/myTest") > 0) {
                    window.location.href = "login.jsp";
                    return false;
                } else if (url.indexOf("page/myPoint") > 0) {
                    window.location.href = "login.jsp";
                    return false;
                } else if (url.indexOf("page/examing") > 0) {
                    window.location.href = "login.jsp";
                    return false;
                } else if (url.indexOf("page/userCenter") > 0) {
                    window.location.href = "login.jsp";
                    return false;
                }
                $("#navUl > li").show();
                $("#liUserCenter").hide();
                $("#myTest").hide();
                $("#myPoint").hide();
                $("#editPasswordLi").hide();
            }

            //截取url以记住上次访问过的页面
            var otherUrl = url.substring(url.lastIndexOf('/'));
            if (userId == null) {
                window.location.href = "login.jsp?url=" + otherUrl;
            }


        });
    </script>
    <script type="text/javascript">

        //导航菜单高亮显示
        window.onload = function highThis() {
            var pathname = window.location.pathname;
            if (pathname.indexOf("myTest") > 0) {
                $("#myTest").addClass("active");
            } else if (pathname.indexOf("myPoint") > 0) {
                $("#myPoint").addClass("active");
            } else if (pathname.indexOf("itemBankManage") > 0) {
                $("#itemBankManage").addClass("active");
            } else if (pathname.indexOf("testPaperManage") > 0) {
                $("#testPaperManage").addClass("active");
            } else if (pathname.indexOf("testManage") > 0) {
                $("#testManage").addClass("active");
            } else if (pathname.indexOf("testResultManage") > 0) {
                $("#testResultManage").addClass("active");
            } else if (pathname.indexOf("chartsManage") > 0) {
                $("#chartsManage").addClass("active");
            } else if (pathname.indexOf("userManage") > 0) {
                $("#userManage").addClass("active");
            }
        }

        //显示更新密码模态框
        function showUpdatePwdModal() {
            $("#oldPassword").val('');
            $("#newPassword1").val('');
            $("#newPassword2").val('');
            $("#update_password_modal").modal(
                    {
                        show: true,
                        keyboard: false,
                        backdrop: 'static'
                    }
            );
        }

        //更新密码
        function updatePassword() {

            var userId = <%=request.getSession().getAttribute("userId")%>;
            var userName = "<%=request.getSession().getAttribute("userName")%>";
            var password = $('#oldPassword').val();
            var password1 = $('#newPassword1').val();
            var password2 = $('#newPassword2').val();
            $("#warningSpan1")[0].innerHTML = '';
            $("#warningSpan1").show();
            $("#warningSpan1").delay(2000).hide("fast");
            if (password.length == 0) {
                $("#warningSpan1")[0].innerHTML = "请输入原始密码！";
                return false;
            }
            if (password1.length == 0) {
                $("#warningSpan1")[0].innerHTML = "请输入新密码！";
                return false;
            }
            if (password2.length == 0) {
                $("#warningSpan1")[0].innerHTML = "请输入确认密码！";
                return false;
            }
            if (password2 != password1) {
                $("#warningSpan1")[0].innerHTML = "两次新密码输入不一致！";
                return false;
            }
            if (password == password1) {
                $("#warningSpan1")[0].innerHTML = "新密码与原始密码应不一致！";
                return false;
            }
            if (!(/^[a-zA-Z0-9_]{6,18}$/.test(password1))) {
                $("#warningSpan1")[0].innerHTML = "不支持的密码格式！";
                return false;
            }
            //确认原始密码
            $.ajax({
                url: "/zxks/rest/user/confirmPassword",
                type: "post",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: 118},
                data: JSON.stringify({
                            user: {
                                userName: userName,
                                passWord: password.trim(),
                            }
                        }
                ),
                success: function (resultData) {

                    if (resultData && resultData.resultCode == "SUCCESS") {
                        //修改密码
                        $.ajax({
                            url: "/zxks/rest/user/updatePassword",
                            type: "put",
                            contentType: "application/json; charset=UTF-8",
                            async: true,
                            dataType: "json",
                            headers: {userId: 118},
                            data: JSON.stringify({
                                user: {
                                    userId: userId,
                                    passWord: password1.trim()
                                },
                            }),
                            success: function (resultData) {
                                if (resultData != null && resultData.resultCode != "SUCCESS") {
                                    $("#warningSpan1").show();
                                    $("#warningSpan1").delay(2000).hide("fast");
                                    $("#warningSpan1")[0].innerHTML = resultData.message;
                                }
                                if (resultData.resultCode == "SUCCESS") {
                                    $("#update_password_modal").modal('hide');
                                    window.location.href = "/zxks/page/login.jsp";
                                }
                            },
                            error: function (xhr, r, e) {
                                alert(e);
                            }
                        });
                    } else if (resultData && resultData.message) {
                        $("#warningSpan1").show();
                        $("#warningSpan1").delay(2000).hide("fast");
                        $("#warningSpan1")[0].innerHTML = resultData.message;
                    }
                },
                error: function (xhr, r, e) {
                    alert(e);
                    //resetUserModalAndHide();
                }
            });
        }
    </script>
    <style>
        #update_password_modal {
            margin-left: -250px;
        }

        input[placeholder] {
            font-family: 微软雅黑;
        }

        .closeModal {
            border-radius: 0px;
            box-shadow: none;
            padding: 0;
            float: right;
            background: url("../images/close.png");
        }

        .closeModal:hover {
            background: url("../images/close_hover.png");
        }

        a.brand:hover {
            background: #1c2b36;
        }

        .modal-backdrop,
        .modal-backdrop.fade.in {
            opacity: 0.7;
            filter: alpha(opacity=70);
            background: #333;
        }
        .nav>li>a{
            cursor: pointer;
        }
    </style>
</head>
<body>

<%--重置密码模态框--%>
<div class="modal fade hide" id="update_password_modal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="background-color: #333;margin: 0;height: 32px;line-height: 32px">
                <button class="btn btn-default closeModal" data-dismiss="modal"
                        style="width: 50px;height: 50px;margin-top: -9px;margin-right: -15px; border: 0;"></button>
                <span class="modal-title"
                      style="color: #ffffff;font-family: '微软雅黑';font-size: 16px;margin: 0;height: 31px;">修改密码</span>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-3 control-label"
                               style="text-align:center;font-family: '微软雅黑';margin-left: 5px;font-size: 16px;margin-top: 0px"><span
                                style="color:red;">*</span> 输入旧密码</label>

                        <div class="col-sm-9" style="width: 100% !important;">
                            <input type="password" id="oldPassword" class="form-control" maxlength="18"
                                   style="width: 220px !important;padding: 0 0 0 5px;margin-top: 0px"
                                   placeholder="6-18位数字、字母、下划线组成">
                        </div>
                    </div>
                    <div class="form-group" style="margin-top: 10px">
                        <label class="col-sm-3 control-label"
                               style="text-align:center;font-family: '微软雅黑';margin-left: 5px;font-size: 16px;margin-top: 0px"><span
                                style="color:red;">*</span> 输入新密码</label>

                        <div class="col-sm-9" style="width: 100% !important;">
                            <input type="password" id="newPassword1" class="form-control" maxlength="18"
                                   style="width: 220px !important;padding: 0 0 0 5px;margin-top: 0px"
                                   placeholder="6-18位数字、字母、下划线组成">
                        </div>
                    </div>
                    <div class="form-group" style="margin-top: 10px">
                        <label class="col-sm-3 control-label"
                               style="text-align:center;font-family: '微软雅黑';font-size: 16px;margin-left: 5px;margin-top: 0px"><span
                                style="color:red;">*</span> 确认新密码</label>

                        <div class="col-sm-9" style="width: 100% !important;">
                            <input type="password" id="newPassword2" class="form-control" maxlength="18"
                                   style="width: 220px !important;padding: 0 0 0 5px;margin-top: 0px"
                                   placeholder="6-18位数字、字母、下划线组成">

                        </div>
                    </div>
                    <span class="warningSpan1" id="warningSpan1"
                          style="margin-left: 33px;margin-top: 10px;float: left;color: #ff6c60;"></span>
                </form>
            </div>
            <div class="modal-footer">
                <button class="btn btn-default" data-dismiss="modal"
                        style="color: #ffffff;border: 0;width: 70px;height: 34px;font-family: 微软雅黑;background: #b0b5b9;text-shadow: none">
                    取消
                </button>
                <button class="btn btn-primary" onclick="updatePassword();"
                        style="border: 0;width: 70px;height: 34px;font-family: 微软雅黑;background:#4dbd73">确定
                </button>
            </div>
        </div>
    </div>
</div>



<div class="navbar navbar-inverse" style="font-size:14px;font-family: Arial, 微软雅黑, 宋体;border:0px solid blue;padding: 0px;">
  <div class="navbar-inner">
    <div class="container" style="height:48px;margin-top:15px;">
      <a class="btn btn-navbar" data-toggle="collapse" data-target=".navbar-responsive-collapse">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </a>
      <a class="brand" style="margin-top:-2px;"><img src="../images/logo_menu.png" style="height:42px;margin-top:-10px;"/></a>
      <div class="nav-collapse collapse navbar-responsive-collapse">
        <ul class="nav" id="navUl">
          <li class="menu" id="myTest"><a onclick="window.location.href = '/zxks/page/myTest.jsp'; return false;"><img src="../images/test.png" style="margin-right: 5px;margin-top: -3px">我的考试</a></li>
          <li class="menu" id="myPoint"><a onclick="window.location.href = '/zxks/page/myPoint.jsp'; return false;"><img src="../images/chengji.png" style="margin-right: 5px;margin-top: -3px">我的成绩</a></li>
          <li class="menu" id="itemBankManage"><a onclick="window.location.href = '/zxks/page/itemBankManage.jsp'; return false;"><img src="../images/tiku.png" style="margin-right: 5px;margin-top: -3px">题库管理</a></li>
          <li class="menu" id="testPaperManage"><a onclick="window.location.href = '/zxks/page/testPaperManage.jsp'; return false;"><img src="../images/shijuan.png" style="margin-right: 5px;margin-top: -3px">试卷管理</a></li>
          <li class="menu" id="testManage"><a onclick="window.location.href = '/zxks/page/testManage.jsp'; return false;"><img src="../images/test.png" style="margin-right: 5px;margin-top: -3px">考试管理</a></li>
          <li class="menu" id="testResultManage"><a onclick="window.location.href = '/zxks/page/testResultManage.jsp'; return false;"><img src="../images/chengji.png" style="margin-right: 5px;margin-top: -3px">成绩管理</a></li>
          <li class="menu" id="chartsManage"><a  onclick="window.location.href = '/zxks/page/chartsManage.jsp'; return false;"><img src="../images/tongji.png" style="margin-right: 5px;margin-top: -3px">统计管理</a></li>
          <li class="menu" id="userManage"><a  onclick="window.location.href = '/zxks/page/userManage.jsp'; return false;"><img src="../images/yonghu.png" style="margin-right: 5px;margin-top: -3px">考生管理</a></li>
          <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown" style="display: none">系统设置<b class="caret"></b></a>
            <ul class="dropdown-menu">
              <li><a onclick="window.location.href = '/zxks/page/systemSetting.jsp'; return false;" >基础设置</a></li>
              <li><a onclick="window.location.href = '/zxks/page/noticeManage.jsp'; return false;" >公告管理</a></li>
            </ul>
          </li>
        </ul>
        <ul class="nav" style="margin-right:-20px;float:right;">
          <li class="dropdown" style="float:right;" id="userCenterLi">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown"><%=request.getSession().getAttribute("userName")==null?"匿名用户":request.getSession().getAttribute("userName")%> <b class="caret"></b></a>
            <ul class="dropdown-menu">
              <li id="liUserCenter"><a href="userCenter.jsp">个人中心</a></li>
              <li id="editPasswordLi"><a href="#" onclick="showUpdatePwdModal()">修改密码</a></li>
              <li><a href="logout.jsp?userId=<%=request.getSession().getAttribute("userId")%>">注销</a></li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
  </div>
</div>
</body>

</html>
