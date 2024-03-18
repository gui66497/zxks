<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <link href="../images/favicon.ico" rel="icon" type="image/x-icon"/>
    <title>在线考试系统--个人中心</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="../exts/bootstrap/css/bootstrap.min.css" rel='stylesheet' type='text/css'/>
    <link href="../exts/bootstrap/css/bootstrap.css" rel='stylesheet' type='text/css'/>
    <link rel="stylesheet" type="text/css" href="../css/base.css">
    <link href="../css/userCenter.css" rel='stylesheet' type='text/css'/>

    <link href="../css/bootstrap-datetimepicker.css" rel='stylesheet' type='text/css'/>
    <link href="../css/bootstrap-datetimepicker.min.css" rel='stylesheet' type='text/css'/>
    <script src="../exts/jquery-1.12.3.min.js"></script>
    <script src="../exts/bootstrap/js/bootstrap.min.js"></script>
    <script src="../exts/bootstrap-datetimepicker.js"></script>
    <script src="../exts/bootstrap-datetimepicker.min.js"></script>
    <script src="../exts/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="application/javascript">

        var data = [];
        var companies = [];
        var allDepts = [];
        var topDepts = [];
        var subDepts = [];
        var currentNode = null; //当前选中节点
        var options;
        var deptsGroupByCompany = {};
        var deptsMapById = {};
        var companiesMapById = {};
        function addNodes(targetDept) {
            $.each(subDepts, function (index2, subDept) {
                if (subDept.parentId == targetDept.deptId) {
                    if (!targetDept.nodes) {
                        targetDept.nodes = []
                    }
                    var subNode = {
                        id: subDept.deptId,
                        text: subDept.deptName,
                        href: "#",
                        deptId: subDept.deptId,
                        deptName: subDept.deptName,
                        dept: subDept,
                    };
                    targetDept.nodes.push(subNode);
                    addNodes(subNode);
                }
            });
        }
        function filterStringReturnSpan(targetStr, widthLimit) {
            var tempStr = "";
            for (var j = 0; j < targetStr.length; j++) {
                tempStr += targetStr[j];
                $("#word-cal").text(tempStr);
                if ($("#word-cal").width() > widthLimit) {
                    if (j < targetStr.length - 1) {
                        tempStr += "...";
                        tempStr = "<span title='" + targetStr + "' >" + tempStr + "</span>";
                    }
                    break;
                }

            }
            return tempStr;
        }

        function initSelectCompaniesAndDepts(strCompanyDomId, strDeptDomId, strSubDeptDomId, optionHtml) {
            $('#' + strCompanyDomId).text('');
            $('#' + strCompanyDomId).append(optionHtml);
            $('#' + strCompanyDomId).val("");
            $('#' + strCompanyDomId).change(function () {
                var companyId = $(this).val();
                $('#' + strDeptDomId).empty();
                $('#' + strDeptDomId).trigger('change');
                if (deptsGroupByCompany[companyId]) {
                    var topDeptsHtml = "";
                    topDeptsHtml += "<option value=''></option>";
                    $.each(deptsGroupByCompany[companyId], function (index, dept) {
                        //添加部门下拉框
                        topDeptsHtml += "<option value='" + dept.deptId + "'>" + dept.deptName + "</option>";
                    });
                    $('#' + strDeptDomId).append(topDeptsHtml);
                    if (strSubDeptDomId && strSubDeptDomId.length > 0) {
                        $('#' + strDeptDomId).change(function () {
                            var parentDeptId = $(this).val();
                            $('#' + strSubDeptDomId).empty();
                            if (deptsMapById[parentDeptId] && deptsMapById[parentDeptId].nodes && deptsMapById[parentDeptId].nodes.length > 0) {
                                var subDeptsHtml = "";
                                subDeptsHtml += "<option value=''></option>";
                                $.each(deptsMapById[parentDeptId].nodes, function (index, dept) {
                                    //添加部门下拉框
                                    subDeptsHtml += "<option value='" + dept.deptId + "'>" + dept.deptName + "</option>";
                                });
                                $('#' + strSubDeptDomId).append(subDeptsHtml);
                            }
                        });
                    }
                }
            });
        }

        function loadCompaniesAndDepts() {
            data = [];
            companies = [];
            allDepts = [];
            topDepts = [];
            subDepts = [];
            currentNode = null; //当前选中节点
            options;
            deptsGroupByCompany = {};
            //加载单位和部门
            $.ajax({
                url: "/zxks/rest/company/list",
                type: "get",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: 118},
                data: {},
                success: function (resultData) {
                    if (resultData && resultData.data)
                        companies = resultData.data;
                    var optionHtml = "";
                    $.each(companies, function (index, company) {
                        if (!companiesMapById[company.companyId]) {
                            companiesMapById[company.companyId] = company;
                        }
                        company.company = JSON.parse(JSON.stringify(company));
                        //添加下拉框
                        optionHtml += "<option value='" + company.companyId + "'>" + company.companyName + "</option>";
                    });
                    //initSelectCompaniesAndDepts("companyAddSelect", "parentDeptAddSelect", "", optionHtml);
                    //initSelectCompaniesAndDepts("companyUpdateSelect", "parentDeptUpdateSelect", "", optionHtml);
                    initSelectCompaniesAndDepts("companyUserAddSelect", "parentDeptUserAddSelect", "subDeptUserAddSelect", "<option value=''></option>"+optionHtml);

                    if (companies.length > 0) {
                        //获取部门数据
                        $.ajax({
                            url: "/zxks/rest/dept/list",
                            type: "get",
                            contentType: "application/json; charset=UTF-8",
                            async: true,
                            dataType: "json",
                            headers: {userId: 118},
                            data: {},
                            success: function (resultData) {
                                if (resultData && resultData.data)
                                    allDepts = resultData.data;
                                deptsMapById = {};
                                if (allDepts.length > 0) {
                                    $.each(allDepts, function (index, dept) {
                                        if (!deptsMapById[dept.deptId]) {
                                            deptsMapById[dept.deptId] = dept;
                                        }
                                        if (dept.parentId == 0) {
                                            topDepts.push(dept);
                                        } else {
                                            subDepts.push(dept);
                                        }
                                    });
                                    $.each(topDepts, function (index, dept) {
                                        $.each(subDepts, function (index2, subDept) {
                                            if (subDept.parentId == dept.deptId) {
                                                if (!dept.nodes) {
                                                    dept.nodes = []
                                                }
                                                var subNode = {
                                                    id: subDept.deptId,
                                                    text: filterStringReturnSpan(subDept.deptName, 82),
                                                    href: "#",
                                                    deptId: subDept.deptId,
                                                    deptName: subDept.deptName,
                                                    dept: subDept
                                                };
                                                dept.nodes.push(subNode);
                                                addNodes(subNode);
                                            }
                                        });
                                    });
                                    $.each(companies, function (index, company) {
                                        company.id = company.companyId;
                                        company.text = filterStringReturnSpan(company.companyName, 115);
                                        company.href = "#";
                                        company.state = {
                                            selected: false
                                        };
                                        if (!deptsGroupByCompany[company.companyId]) {
                                            deptsGroupByCompany[company.companyId] = [];
                                        }
                                        $.each(topDepts, function (index2, topDept) {
                                            if (topDept.companyId == company.companyId) {
                                                deptsGroupByCompany[company.companyId].push(topDept);
                                                if (!company.nodes) {
                                                    company.nodes = []
                                                }
                                                var subNode = {
                                                    id: topDept.deptId,
                                                    text: filterStringReturnSpan(topDept.deptName, 110),
                                                    href: "#",
                                                    deptId: topDept.deptId,
                                                    deptName: topDept.deptName,
                                                    dept: topDept,
                                                };
                                                if (topDept.nodes && topDept.nodes.length > 0)
                                                    subNode.nodes = topDept.nodes;
                                                company.nodes.push(subNode);
                                            }
                                        });
                                    });

                                }
                            },
                            error: function (xhr, r, e) {
                                alert(e);
                            }
                        });
                    }
                },
                error: function (xhr, r, e) {
                    alert(e);
                }
            });
        }

        //获取用户信息
        $(function () {

            //加载日期控件
            var date = new Date();
            date.getDate();
            $(".form_datetime").datetimepicker({
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                minView: 2,
                autoclose: true,
                endDate: date,
                pickerPosition: "bottom-left",
            });

            $(".form_date").datetimepicker({
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                minView: 2,
                autoclose: true,
                endDate: date,
                pickerPosition: "bottom-left",
            });

            //获取单位部门信息
            loadCompaniesAndDepts();

            //获取用户信息
            setTimeout(function () {
                $.ajax({
                    contentType: "application/json",
                    type: "post",
                    dataType: "json",
                    url: "../rest/userLogin/getUser",
                    headers: {userId: 118},
                    data: {},
                    success: function (data) {
                        var dataList = data.data;
                        if ((dataList[0].gender.toString()) == $(' input[type="radio"]')[0].value) {
                            $(' input[type="radio"]')[0].checked = true;
                        } else {
                            $(' input[type="radio"]')[1].checked = true;
                        }
                        $("#userName")[0].innerHTML = dataList[0].userName.trim();
                        $("#realName").val(dataList[0].realName.trim());

                        $("#identityNo").val(dataList[0].idCardNo.trim());
                        $("#userMail").val(dataList[0].email.trim());
                        $("#birthDate").val(dataList[0].birth);
                        $("#educationSelect").val(dataList[0].education.trim());
                        $("#graduateSchool").val(dataList[0].graduateSchool.trim());
                        $("#graduateDate").val(dataList[0].graduateDate);
                        $("#major").val(dataList[0].major.trim());
                        $("#resumeUrl").val(dataList[0].resumeUrl.trim());
                        $("#userTel").val(dataList[0].tel.trim());
                        $("#position").val(dataList[0].position.trim());
                        $('#companyUserAddSelect').val(dataList[0].company);
                        $('#companyUserAddSelect').trigger("change");
                        $('#parentDeptUserAddSelect').val(dataList[0].dept);


                    }
                })
            }, 100);

        });

        function userUpdate() {
            var userId = <%=request.getSession().getAttribute("userId")%>;
            var userName = $("#userName")[0].innerHTML;
            var realName = $("#realName").val();
            var userGender = $(' input[type="radio"]:checked').val();
            $("#warningSpan").show();
            $("#warningSpan").delay(2000).hide("fast");

//            if(gender=="男"){
//
//                var userGender = 1;
//            }else{
//                userGender = 2;
//            }
            var companyId = $('#companyUserAddSelect').val();
            var deptId = $('#parentDeptUserAddSelect').val();
            var email = $("#userMail").val().trim();
            var identity = $("#identityNo").val().trim();
            var birth = $("#birthDate").val();
            var position = $("#position").val().trim();
            var education = $("#educationSelect").val();
            var graduateSchool = $("#graduateSchool").val().trim();
            var graduateDate = $("#graduateDate").val();
            var major = $("#major").val().trim();
            var resumeUrl = $("#resumeUrl").val().trim();
            var tel = $("#userTel").val().trim();
            var realNameReg =/^[\u4e00-\u9fa5]{2,5}$|^[a-zA-Z]{2,10}$/;//真实姓名正则
            var mailReg = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;//邮箱正则
            var identityReg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;//身份证号正则
            var telReg = /^1[3|4|5|7|8][0-9]\d{4,8}$/;//电话号码正则

            //用户名非空校验
            if(realName==""){
                //$("#warningSpan")[0].innerHTML = "真实姓名能为空！";
                showOkModal("提示框","真实姓名不能为空！") ;
                return false;
            }else if(realName!=""&&realNameReg.test(realName)===false){
                showOkModal("提示框","真实姓名格式不正确!");
                $("#realName").focus();
                return false;
            }
            //设置未选择部门时为默认部门ID
            if (!deptId || deptId == "") {
                deptId = $('#parentDeptUserAddSelect').val();
            }
            /*            if (!deptId || deptId == "") {
             deptId = 0;
             }*/

            //邮箱校验
            if (email != "" && mailReg.test(email) === false) {
                //$("#warningSpan")[0].innerHTML = "邮箱格式不正确(" + $("#userMail").attr("placeholder") + ")!";
                showOkModal("提示框","邮箱格式不正确(" + $("#userMail").attr("placeholder") + ")!") ;
                $("#userMail").focus();
                return false;
            }
            //身份证号校验
            if (identity != "" && identityReg.test(identity) === false) {
                //$("#warningSpan")[0].innerHTML = "身份证号格式不正确(" + $("#identityNo").attr("placeholder") + ")!";
                showOkModal("提示框","身份证号格式不正确(" + $("#identityNo").attr("placeholder") + ")!") ;
                $("#identityNo").focus();
                return false;
            }
            //电话号码校验

            if (tel != "" && telReg.test(tel) === false) {
                showOkModal("提示框","电话号码格式不正确(" + $("#userTel").attr("placeholder") + ")!") ;
                $("#userTel").focus();
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
                    showOkModal("提示框","出生时间应小于毕业时间！") ;
                    return;
                }
            }
            $.ajax({
                contentType: "application/json",
                dataType: "json",
                type: "put",
                url: "/zxks/rest/user/update",
                headers: {'userId': '10'},
                data: JSON.stringify({
                    user: {
                        userId: userId,
                        userName: userName,
                        realName: realName,
                        gender: userGender,
                        company: companyId,
                        dept: deptId,
                        email: email,
                        idCardNo: identity,
                        birth: birth,
                        position: position,
                        education: education,
                        graduateSchool: graduateSchool,
                        graduateDate: graduateDate,
                        major: major,
                        resumeUrl: resumeUrl,
                        tel: tel
                    }

                }),
                success: function (resultData) {
                    showOkModal("提示框", resultData.message);
                    if (resultData.resultCode == "SUCCESS") {
                        setTimeout(function(){
                            window.location.href = "myTest.jsp";
                        },2000);
                    }
                }
            })
        }

    </script>
    <style type="text/css">

    </style>

</head>
<body style="background-color: #f1f4f8">
<div id="mainDiv">
    <div id="headDiv">
        <jsp:include page="menu.jsp"/>
    </div>
    <div id="contentDiv" style="text-align: left">
        <%@include file="mymodal.jsp"%>
        <div class="header" style="background: #fff">
            <p class="header_p1"></p>

            <p class="header_font">个人中心</p>
        </div>

        <div>

            <div class="userUpdate1">
                <span style="color:red;margin-left: 5px;position: absolute;margin-top: 26px">*</span><label name="userLabel" style="display: inline-block;position: absolute;">用户名:</label>
                <label style="margin:25px 0 0 100px" id="userName"></label>
                <span style="color:red;margin-left: 5px;position: absolute;margin-top: 26px">*</span><label name="userLabel" style="display: inline-block;position: absolute">真实姓名:</label>
                <input style="width: 260px;padding-left:10px;margin: 20px 0 0 90px;border-radius: 4px;border: 1px solid #dddddd;height: 30px;font-family:微软雅黑" placeholder="2-5位中文或2-10位英文"
                       id="realName" maxlength="5"><br>
                <span style="color:red;margin-left: 5px;position: absolute;margin-top: 19px">*</span><label name="userLabel" style="display: inline-block;position: absolute;margin-top: 15px">性别:</label>
                <input type="radio" name="gender" class="gender" value="1"
                       style="margin-left: 90px;margin-top: 20px"><span
                    style="margin-top: 15px;float:left;position: absolute;font-family:微软雅黑">男</span>
                <input type="radio" name="gender" value="2" class="gender"
                       style="margin-left:50px;margin-top: 20px"><span
                    style="margin-top: 15px;float:left;position: absolute;font-family:微软雅黑">女</span><br>
                <label name="userLabel" style="display: inline-block;position: absolute;margin-top: 20px">所属单位:</label>
                <select name="companySelect" id="companyUserAddSelect" class="companySelect"
                        placeholder="请选择单位"><option></option></select><br>
                <label name="userLabel" style="display: inline-block;position: absolute;margin-top: 20px">部门:</label>
                <select name="companySelect" id="parentDeptUserAddSelect" class="companySelect" placeholder="请选择部门">
                </select><br>
                <%--            <select name="companySelect" id="subDeptUserAddSelect" class="companySelect"  placeholder="请选择部门">
                            </select><br>--%>
                <label name="userLabel" style="display: inline-block">身份证号:</label>
                <input type="text" name="userInput" id="identityNo" class="userInput" maxlength="18"
                       placeholder="15位或18位或17X(数字)"/><br>
                <label name="userLabel" style="display: inline-block">邮箱:</label>
                <input type="text" name="userInput" maxlength="30" id="userMail" class="userInput " placeholder="例如：12345abc@163.com"/><br>
                <label name="userLabel" style="display: inline-block;float: left">出生年月:</label>

                <div class="input-group date form_datetime col-md-6"
                     style="float:left;margin-left:90px;margin-top:20px;">
                    <input type="text" name="userInput" id="birthDate" maxlength="12"
                           style="cursor: pointer;margin: 0" class="inputTime"
                            >
                    <span class="input-group-addon" style="display: inline-block;margin-left: -25px">
                        <span class="glyphicon glyphicon-th icon-th"></span>
                    </span>
                </div>

            </div>

            <div class="userUpdate2">

                <label name="userLabel" style="display: inline-block;position: absolute;margin-top: 20px">学历:</label>
                <select class="educationSelect" id="educationSelect">
                    <option>专科</option>
                    <option>本科</option>
                    <option>硕士</option>
                    <option>博士</option>
                    <option>其它</option>
                </select><br>
                <label name="userLabel" style="display: inline-block">毕业院校:</label>
                <input type="text" name="userInput" maxlength="15" class="userInput" id="graduateSchool"/><br>
                <label name="userLabel" style="display: inline-block">职位:</label>
                <input type="text" name="userInput" class="userInput" maxlength="15" id="position"/><br>
                <label name="userLabel" style="display: inline-block;float: left;">毕业时间:</label>

                <div class="input-group date form_date col-md-5" style="float:left;margin-left:90px;margin-top:20px;">
                    <input type="text" name="userInput" class="graduateDate" id="graduateDate" maxlength="12"
                           style="cursor: pointer;margin: 0">
                    <span class="input-group-addon" style="display: inline-block;margin-left: -25px">
                        <span class="glyphicon glyphicon-th icon-th"></span>
                    </span>
                </div>
                <label name="userLabel" style="display: inline-block;position: absolute;margin-top: 90px">专业:</label>
                <input type="text" name="userInput" maxlength="15" class="userInput" id="major" style="margin-left: 90px"/><br>
                <label name="userLabel" style="display: inline-block">简历地址:</label>
                <input type="text" name="userInput" maxlength="30" class="userInput" id="resumeUrl"/><br>
                <label name="userLabel" style="display: inline-block">手机号码:</label>
                <input type="text" name="userInput" class="userInput" maxlength="11" id="userTel" placeholder="11位数字"/><br>

<%--                <div style="padding-left: 10px;float: left;margin-left: 90px; margin-top: 10px;background-color: #fcf8e3;">
            <span class="warningSpan" id="warningSpan" style="color: #ff6c60;height: 25px;font-size: 14px;line-height: 25px;
            font-family: 微软雅黑;"></span>
                </div>--%>
                <div id="userOperation" class="userOperation">
                    <input type="button" value="提交" onclick="userUpdate()" style="width: 70px;">

                </div>
            </div>
        </div>

    </div>


</div>
</body>
</html>