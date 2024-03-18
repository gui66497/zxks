<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="../images/favicon.ico" rel="icon" type="image/x-icon"/>
    <title>在线考试系统--考生管理</title>
    <link rel="stylesheet" type="text/css" href="../exts/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="../css/base.css">
    <link rel="stylesheet" type="text/css" href="../css/square/blue.css">
    <link rel="stylesheet" type="text/css" href="../css/itemBankManage.css">
    <link rel="stylesheet" type="text/css" href="../css/userManage.css">
    <link rel="stylesheet" type="text/css" href="../css/basic.css">
    <link rel="stylesheet" type="text/css" href="../exts/datatables/css/jquery.dataTables.css">
    <link rel="stylesheet" type="text/css" href="../exts/bootstrap/css/bootstrap-modal.css">
    <link rel="stylesheet" type="text/css" href="../css/bootstrap-treeview.css">
    <link href="../css/bootstrap-datetimepicker.css" rel='stylesheet' type='text/css'/>
    <link href="../css/bootstrap-datetimepicker.min.css" rel='stylesheet' type='text/css'/>
    <script type="application/javascript" src="../exts/jquery-1.12.3.min.js"></script>
    <script type="application/javascript" src="../exts/bootstrap-treeview.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/datatables/js/jquery.dataTables.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/bootstrap/js/bootstrap-modal.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/bootstrap/js/bootstrap-modalmanager.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/modalTemplate.js"></script>
    <script type="application/javascript" src="../exts/icheck.min.js"></script>
    <script src="../exts/bootstrap-datetimepicker.js"></script>
    <script src="../exts/bootstrap-datetimepicker.min.js"></script>
    <script src="../exts/bootstrap-datetimepicker.zh-CN.js"></script>
    <!--加载日期控件-->
    <script type="text/javascript">
        $(function () {
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

        })
    </script>
    <!--初始化checkbox-->
    <script type="text/javascript">
        function initCheckBox() {
            $('input[type!=radio]').iCheck({
                checkboxClass: 'icheckbox_square-blue',  // 注意square和blue的对应关系
                radioClass: 'iradio_square-blue',
                increaseArea: '20%' // optional
            })
        }
        $(document).ready(function () {
            initCheckBox();
        });

        $('a').tooltip({
            delay: {
                show: 500,
                hide: 100,
            },
            //placement:left,
        });

        $('button').tooltip({
            delay: {
                show: 500,
                hide: 100,
            },
            container: 'body'
        });
    </script>
    <!--提示框和确认提示框-->
    <script type="text/javascript">
        function warn(strMessage) {
            $('#warnTip').html(strMessage);
            $('#warnModal').modal(
                    {
                        show:true,
                        keyboard:false,
                        backdrop:'static'
                    }
            );
        }
        function confirm(strMessage, strfunc) {
            $('#sureTip').text(strMessage);
            $('#btnExecute').attr('onclick', strfunc);
            $('#sureModal').modal(
                    {
                        show:true,
                        keyboard:false,
                        backdrop:'static'
                    }
            );
        }
        function warnWithAlert(id, strMessage) {
            showAlert(id, strMessage);
        }
        function showAlert(id, message) {
            //$("#"+id).removeClass("hide");
            $("#" + id).text('');
            $("#" + id).append('<button type="button"class="closeTable" onclick="hideAlert(\'' + id + '\')"></button>');
            $("#" + id).append(message);
            $("#" + id).show();
            $("#" + id).delay(2000).hide("fast");
            //$("#"+id).removeClass("hide");
        }

        function hideAlert(id) {
            //$("#"+id).addClass("hide");
            $("#" + id).hide();
        }
    </script>
    <!--单位部门管理-->
    <script type="text/javascript">
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
                        topDeptsHtml += "<option value='" + dept.deptId + "'>" + dept.deptName.replace(new RegExp(" ","g"),"&nbsp;") + "</option>";
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
                                    subDeptsHtml += "<option value='" + dept.deptId + "'>" + dept.deptName.replace(new RegExp(" ","g"),"&nbsp;") + "</option>";
                                });
                                $('#' + strSubDeptDomId).append(subDeptsHtml);
                            }
                        });
                    }
                }
            });
        }
        function filterString(targetStr, widthLimit) {
            var tempStr = "";
            for (var j = 0; j < targetStr.length; j++) {
                tempStr += targetStr[j];
                $("#word-cal").text(tempStr);
                if ($("#word-cal").width() > widthLimit) {
                    if (j < targetStr.length - 1) {
                        tempStr += "...";
                    }
                    break;
                }

            }
            return tempStr;
        }
        function filterStringReturnSpan(targetStr, widthLimit) {
            var tempStr = "";
            for (var j = 0; j < targetStr.length; j++) {
                tempStr += targetStr[j];
                $("#word-cal").text(tempStr);
                if ($("#word-cal").width() > widthLimit) {
                    if (j < targetStr.length - 1) {
                        tempStr += "...";
                        tempStr = "<span title='" + targetStr + "' class='ellipsis' style='white-space:pre'>" + tempStr + "</span>";
                    }
                    break;
                }

            }
            tempStr = "<span title='" + targetStr + "' class='ellipsis' style='white-space:pre'>" + tempStr + "</span>";
            return tempStr;
        }
        function returnSpan(targetStr,num) {
            var name = "";
            if (targetStr.length > num) {
                name = targetStr.substr(0, num) + "...";
            } else if (targetStr.length> 0) {
                name = targetStr;
            }

            return "<span title='" + targetStr + "' class='ellipsis' style='margin-left:5px;' >" + name + " </span>"
        }
        function loadCompaniesAndDepts() {
            data = [];
            companies = [];
            allDepts = [];
            topDepts = [];
            subDepts = [];
            currentNode = null; //当前选中节点
            options = {};
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
                   // var optionHtml = "<option value=''></option>";
                    var optionHtml = "";
                    $.each(companies, function (index, company) {
                        if (!companiesMapById[company.companyId]) {
                            companiesMapById[company.companyId] = company;
                        }
                        company.company = JSON.parse(JSON.stringify(company));
                        //添加下拉框
                        optionHtml += "<option value='" + company.companyId + "'>" + company.companyName.replace(new RegExp(" ","g"),"&nbsp;") + "</option>";
                    });
                    initSelectCompaniesAndDepts("companyAddSelect", "parentDeptAddSelect", "", optionHtml);
                    initSelectCompaniesAndDepts("companyUpdateSelect", "parentDeptUpdateSelect", "", optionHtml);
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
                                    /*$.each(topDepts, function (index, dept) {
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
                                     });*/
                                }
                                $.each(companies, function (index, company) {
                                    if(index==0){
                                        company.state = {
                                            selected: true
                                        };
                                        currentNode = company;

                                            getUserList();

                                    }
                                    company.id = company.companyId;
                                    company.text = returnSpan(company.companyName,12);
                                    company.href = "#";

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
                                                text: returnSpan(topDept.deptName,12),
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
                                options = {
                                    levels: 2,
                                    data: companies,
                                    expandIcon: 'icon-plus',
                                    collapseIcon: 'icon-minus',
                                    emptyIcon: 'icon',
                                };
                                $('#treeview').treeview(options);
                                $('#treeview').on('nodeSelected', function (event, data) {
                                    currentNode = data;
                                    $("#searchValue").val(""); //将页面快速检索框中内容清空
                                    getUserList();
                                });
                                $('#treeview').on('nodeUnselected', function (event, data) {
                                    currentNode = null;
                                    table.destroy();
                                    table = $('#userListTable').DataTable(dataTableJson);
                                    $("#searchValue").val(""); //将页面快速检索框中内容清空
                                    getUserList();
                                });
                            },
                            error: function (xhr, r, e) {
                                warn(e);
                            }
                        });
                    }
                },
                error: function (xhr, r, e) {
                    warn(e);
                }
            });
        }
        $(function () {
            loadCompaniesAndDepts();
        });
        function beforeCreateCompany(){
            $('#companyName').val("");
            $('#new_company_modal').modal(
                    {
                        show:true,
                        keyboard:false,
                        backdrop:'static'
                    }
            );
            setTimeout(function () {
                $('#companyName').focus();
            }, 800);
        }
        //添加部门之前验证并初始化单位和部门
        function beforeCreateDept() {
            $('#deptNameAdd').val("");
            //new_department_modal
            if (!companies || companies.length == 0) {
                warn("请先创建单位！");
                return;
            }
            //如果用户选中了单位和部门导航节点，则默认选中当前节点的单位
            if (currentNode) {
                if (currentNode.company) {
                    $("#companyAddSelect").val(currentNode.company.companyId);
                } else if (currentNode.dept) {
                    $('#companyAddSelect').val(currentNode.dept.companyId);
                }
            }
            $('#new_department_modal').modal(
                    {
                        show:true,
                        keyboard:false,
                        backdrop:'static'
                    }
            );
            setTimeout(function () {
                $('#deptNameAdd').focus();
            }, 800);
        }
        //更新部门或单位前根据选中的节点来展示更新部门或更新单位模态框
        function beforeUpdate() {
            if (currentNode) {
                if (currentNode.company) {
                    $("#companyNameUpdate").val(currentNode.company.companyName);
                    $('#update_company_modal').modal(
                            {
                                show:true,
                                keyboard:false,
                                backdrop:'static'
                            }
                    );
                    setTimeout(function () {
                        $('#companyNameUpdate').focus();
                    }, 800);
                } else if (currentNode.dept) {
                    $('#companyUpdateSelect').val(currentNode.dept.companyId);
                    $('#companyUpdateSelect').trigger('change');
                    $('#deptNameUpdate').val(currentNode.dept.deptName);
                    var parent = currentNode.dept.parentId == 0 ? "" : currentNode.dept.parentId;
                    $('#parentDeptUpdateSelect').val(parent);
                    $('#update_department_modal').modal(
                            {
                                show:true,
                                keyboard:false,
                                backdrop:'static'
                            }
                    );
                    setTimeout(function () {
                        $('#deptNameUpdate').focus();
                    }, 800);
                }
            } else {
                warn("请先选择单位或部门！");
                return;
            }
        }
        //删除部门或单位前根据选中的节点来对应删除单位或部门
        function beforeDelete() {
            if (currentNode) {
                if (currentNode.company) {
                    deleteCompany();
                } else if (currentNode.dept) {
                    deleteDept();
                }
            } else {
                warn("请先选择单位或部门！");
                return;
            }
        }
        //添加单位
        function addCompany() {
            var companyName = $('#companyName').val().trim();
            if (companyName.length == 0) {
                warn( "单位名称不能为空！");
                $("#companyName").focus();
                return;
            }
            $.ajax({
                url: "/zxks/rest/company/add",
                type: "post",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: 118},
                data: JSON.stringify({"company": {"companyName": companyName}}),
                success: function (resultData) {
                    if (resultData != null && resultData.resultCode == "SUCCESS") {
                        warn("单位添加成功！");
                        loadCompaniesAndDepts();
                        $('#new_company_modal').modal('hide');
                    } else {
                        warn(resultData.message);
                    }

                },
                error: function (xhr, r, e) {
                    warn(e);
                }
            });
        }
        //修改单位
        function updateCompany() {
            var currentName = currentNode.company.companyName.trim();
            var companyName = $('#companyNameUpdate').val().trim();
            if (companyName.length == 0) {
                warn("单位名称不能为空！");
                return;
            }
            if(currentName == companyName){
                warn("单位名称未做修改！");
                return;
            }
            var company = JSON.parse(JSON.stringify(currentNode.company));
            company.companyName = companyName;
            $.ajax({
                url: "/zxks/rest/company/update",
                type: "put",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: 118},
                data: JSON.stringify({"company": company}),
                success: function (resultData) {
                    if (resultData != null && resultData.resultCode == "SUCCESS") {
                        warn("单位修改成功！");
                        loadCompaniesAndDepts();
                        $('#update_company_modal').modal('hide');
                    } else {
                        warn(resultData.message);

                    }
                },
                error: function (xhr, r, e) {
                    warn(e);
                    //resetUserModalAndHide();
                }
            });
        }
        //删除单位前验证和确认
        function deleteCompany() {

            if (currentNode.nodes && currentNode.nodes.length > 0) {
                warn("当前单位下存在部门！");
                return;
            }
            confirm("确定要删除单位：【" + currentNode.company.companyName + "】吗？", 'deleteCompanyComfirmed()');

        }
        function deleteCompanyComfirmed() {
            $.ajax({
                url: "/zxks/rest/company/delete",
                type: "DELETE",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: 118},
                data: JSON.stringify({"company": currentNode.company}),
                success: function (resultData) {
                    if (resultData != null) {
                        warn(resultData.message);
                    }
                    if (resultData != null && resultData.resultCode == "SUCCESS") {
                        currentNode = null;
                        loadCompaniesAndDepts();
                        getUserList();
                    }
                },
                error: function (xhr, r, e) {
                    warn(e);
                }
            });
        }

        //添加部门
        function addDept() {
            var deptName = $('#deptNameAdd').val().trim();
          /*  var companyName =$('#companyAddSelect').val().trim();*/
            var companyId = $('#companyAddSelect option:selected').val();
            var parentDeptId = $('#parentDeptAddSelect option:selected').val();
            if (!companyId || companyId == "") {
                warn( "请选择单位！");
                return false;
            }

            if (!parentDeptId || parentDeptId == "") {
                parentDeptId = "0";
            }
            if (deptName.length == 0) {
                warn( "请输入部门名称！");
                $("#deptNameAdd").focus();
                return false;
            }
            $.ajax({
                url: "/zxks/rest/dept/add",
                type: "post",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: 118},
                data: JSON.stringify({
                    "dept": {
                        "deptName": deptName,
                        "parentId": Number(parentDeptId),
                        "companyId": companyId
                    }
                }),
                success: function (resultData) {
                    if (resultData != null && resultData.resultCode == "SUCCESS") {
                        warn("部门添加成功！");
                        $('#companyAddSelect').val("");
                        $('#companyAddSelect').trigger("change");
                        $('#parentDeptAddSelect').val("");
                        loadCompaniesAndDepts();
                        $('#new_department_modal').modal('hide');
                    } else {
                        warn(resultData.message);
                    }
                },
                error: function (xhr, r, e) {
                    warn(e);
                    //resetUserModalAndHide();
                }
            });
        }
        //修改部门
        function updateDept() {
            var currentName = currentNode.dept.deptName.trim();
            var deptName = $('#deptNameUpdate').val().trim();
            var companyId = $('#companyUpdateSelect option:selected').val();
            var parentDeptId = $('#parentDeptUpdateSelect option:selected').val();
            if (!companyId || companyId == "") {
                warn( "请选择单位！");
                return false;
            }

            if (!parentDeptId || parentDeptId == "") {
                parentDeptId = "0";
            } else if (parentDeptId == currentNode.dept.deptId) {
                warn( "上级部门不能为部门本身！");
                return false;
            } else if (currentNode.nodes && currentNode.nodes.length > 0) {
                warn( "当前部门包含子部门时，不支持设置上级部门！");
                return false;
            }
            if (deptName.length == 0) {
                warn( "请输入部门名称！");
                return false;
            }
            if(currentName == deptName){
                warn("部门名称未做修改！");
                return false;
            }
            var dept = JSON.parse(JSON.stringify(currentNode.dept));
            dept.deptName = deptName;
            dept.parentId = Number(parentDeptId);
            dept.companyId = companyId;
            $.ajax({
                url: "/zxks/rest/dept/update",
                type: "put",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: 118},
                data: JSON.stringify({
                    "dept": dept
                }),
                success: function (resultData) {
                    if (resultData != null && resultData.resultCode == "SUCCESS") {
                        warn("部门修改成功！");
                        $('#deptNameUpdate').val("");
                        $('#companyUpdateSelect').val("");
                        $('#companyUpdateSelect').trigger("change");
                        $('#parentDeptUpdateSelect').val("");
                        loadCompaniesAndDepts();
                        $('#update_department_modal').modal('hide');
                    } else {
                        warn(resultData.message);
                    }
                },
                error: function (xhr, r, e) {
                    warn(e);
                    //resetUserModalAndHide();
                }
            });
        }
        //删除部门
        function deleteDept() {
            /* if (currentNode.nodes && currentNode.nodes.length > 0) {
             warn("当前部门存在子部门！");
             return;
             }*/
            confirm("确定要删除部门：【" + currentNode.dept.deptName + "】吗？", 'deleteDeptConfirmed()');

        }
        function deleteDeptConfirmed() {
            $.ajax({
                url: "/zxks/rest/dept/delete",
                type: "DELETE",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: 118},
                data: JSON.stringify({"dept": currentNode.dept}),
                success: function (resultData) {
                    if (resultData != null) {
                        warn(resultData.message);
                    }
                    if (resultData != null && resultData.resultCode == "SUCCESS") {
                        currentNode = null;
                        loadCompaniesAndDepts();
                        getUserList();
                    }
                },
                error: function (xhr, r, e) {
                    warn(e);
                }
            });
        }
    </script>

    <!--用户管理-->
    <script type="text/javascript">
        var dataTableJson = {
                "bAutoWidth": false,
                "language": {
                    "sSearchPlaceholder": "用户名或真实姓名"
                },
                "bServerSide": true,                    //指定从服务器端获取数据
                "bProcessing": true,
                 "bDestroy": true,//显示正在处理进度条
                "sAjaxSource": "../rest/user/searchByName",    //服务端Rest接口访问路径
                "columns": [
                    {
                        "data": "operate",
                        'sClass': 'center','width':'5%',
                        "mRender": function (data, type, full) {
                            return '';
                        }
                    },
                    {"data": "userName", 'sClass': 'columnLeft','width':'7%'},
                    {"data": "realName", 'sClass': 'center','width':'8%'},
                    {"data": "gender", 'sClass': 'center','width':'5%'},
                    {"data": "idCardNo", 'sClass': 'center','width':'10%'},
                    {"data": "company", 'sClass': 'center','width':'10%'},
                    {"data": "dept", 'sClass': 'center','width':'10%'},
                    {"data": "tel", 'sClass': 'center','width':'10%'},
                    {
                        "data": "operate", 'sClass': 'center','width':'10%',
                        "mRender": function (data, type, full) {
                            //warn(aData.itemId);
                            return '';
                        }
                    }],
                "aoColumnDefs": [                                       //指定列附加属性
                    {"bSortable": false, "aTargets": [0, 4, 5, 6, 7, 8]},        //这句话意思是第1,3（从0开始算）不能排序
                    {"bSearchable": false, "aTargets": [0, 1, 4, 5]},   //bSearchable 这个属性表示是否可以全局搜索，其实在服务器端分页中是没用的
                ],
                "aaSorting": [[1, "asc"]], //默认排序
                "fnRowCallback": function (nRow, aData, iDisplayIndex) {     // 当创建了行，但还未绘制到屏幕上的时候调用，通常用于改变行的class风格
                    if (!userListMap[aData.userId]) {
                        userListMap[aData.userId] = aData;
                    }
                    if (aData.gender == 2) {
                        $('td:eq(3)', nRow).html("女");
                    } else {
                        $('td:eq(3)', nRow).html("男");
                    }
                    if (companiesMapById[aData.company]) {
                        $('td:eq(5)', nRow).html(filterStringReturnSpan(companiesMapById[aData.company].companyName, 150));
                    } else {
                        $('td:eq(5)', nRow).html("");
                    }

                    if (deptsMapById[aData.dept]) {
                        $('td:eq(6)', nRow).html(filterStringReturnSpan(deptsMapById[aData.dept].deptName, 150));
                    } else {
                        $('td:eq(6)', nRow).html("");
                    }
                    $('td:eq(7)', nRow).html(filterStringReturnSpan(aData.tel, 122));
                    var delAndUpdateHtml = "";
                    if (aData.userName != "admin") {
                        $('td:eq(0)', nRow).append("<input class='user' value='" + aData.userId + "' type='checkbox'/>");
                        delAndUpdateHtml = "<button class='editUser' data-original-title='编辑' title='编辑考生' href='javascript:void(0);' onclick='initUpdateUser(" + aData.userId + ");'" +
                                "style='text-decoration:none;background:url(../images/edit.png);width:20px;height:20px;border:0' data-placement='auto top' data-trigger='hover focus'>" +
                                "</button>&nbsp;&nbsp;" +
                                "<button class='delUser' data-original-title='删除' title='删除考生' style='text-decoration:none;background:url(../images/delete.png);width:20px;height:20px;border:0' href='javascript:void(0);'" +
                                "onclick='deleteUserById(" + aData.userId + ");' data-placement='auto top' data-trigger='hover focus'>" +

                                "</button>&nbsp;&nbsp;";
                    }

                    var optionButtons = delAndUpdateHtml +
                            "<button class='resetUser' data-original-title='重置密码'  title='重置密码'  style='text-decoration:none;background:url(../images/reset.png);width:20px;height:20px;border:0' href='javascript:void(0);'" +
                            "onclick='resetPassword(" + aData.userId + ");' data-placement='auto top' data-trigger='hover focus'>" +
                            "</button>"
                    $('td:eq(8)', nRow).append(optionButtons);
                    return nRow;
                },
                "fnServerData": function (sSource, aoData, fnCallback) {
                    var userForSearch = JSON.parse(JSON.stringify(searchUser));
                    if (currentNode && currentNode.company) {
                        userForSearch.user.company = currentNode.company.companyId;
                    } else if (currentNode && currentNode.dept) {
                        userForSearch.user.dept = currentNode.dept.deptId;
                    }
                    userForSearch.paging = convertToPagingEntity(aoData);
                    userForSearch.user.userName = userForSearch.paging.searchValue.trim();
                    $.ajax({
                        "type": 'post',
                        "contentType": "application/json",
                        "url": sSource,
                        "dataType": "json",
                        "headers": {userId: 118},
                        data: JSON.stringify(userForSearch),
                        "success": function (resp) {
                            fnCallback(resp);
                        }
                    });
                }
            };

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
        var allUserList = [];
        var userListMap = {};
        var currentUser = {}; //用于添加和更新用户
        var table;//用于刷新列表
        var searchUser = {
            "user": {
                "userName": ""
            }
        }; //用于检索用户
        $(function () {
                table = $('#userListTable').DataTable(dataTableJson);
                $('#userListTable').on('draw.dt', function () {
                    initCheckBox();
                    $("#checkBoxAllCheckBox").on("ifChecked", function (event) {
                        $('input[type!=radio]').iCheck("check");
                    });
                    $("#checkBoxAllCheckBox").on("ifUnchecked", function (event) {
                        $('input[type!=radio]').iCheck("uncheck");
                    });
                    $('#checkBoxAllCheckBox').iCheck("uncheck");
                    var checkboxes = $("input.user");
                    checkboxes.on("ifChanged", function (event) {
                        if (checkboxes.filter(':checked').length == checkboxes.length) {
                            $("#checkBoxAllCheckBox").prop("checked", "checked");
                        } else {
                            $("#checkBoxAllCheckBox").removeProp("checked");
                        }
                        $("#checkBoxAllCheckBox").iCheck("update");
                    });
                    if ($("#checkBoxAllCheckBox").is(":checked")) {
                        $('input[type!=radio]').iCheck("check");
                    } else {
                        $('input[type!=radio]').iCheck("uncheck");
                    }
                });
        });

        //加载用户列表
        function getUserList() {
            userListMap = {};
            table.ajax.reload();
            return;
        }

        //userTitle 添加和修改用户时模态框标题
        //初始化编辑用户对象
        function initUpdateUser(userId) {
            if (userId && userListMap && userListMap[userId]) {
                var user = userListMap[userId];
                currentUser.user = JSON.parse(JSON.stringify(user));
                $("#userTitle")[0].innerText = "修改考生";
                $('#userName').val(user.userName);
                $('#realName').val(user.realName);
                $('#idCardNo').val(user.idCardNo);
                // $('#passWord').val(user.passWord);
                $('#passWordConfirm').val("");
                $('#companyUserAddSelect').val(user.company);
                $('#companyUserAddSelect').trigger('change');
                $('#parentDeptUserAddSelect').val("");
                $('#subDeptUserAddSelect').val("");
                if (deptsMapById[user.dept]) {
                    $('#parentDeptUserAddSelect').val(user.dept);
                    $('#parentDeptUserAddSelect').trigger('change');

                }
                $('#company').val(user.company);
                $('#dept').val(user.dept);
                $("#pass1").hide();
                $("#pass2").hide();
                if (user.gender == 1) {
                    $("#male").click();
                } else {
                    $("#female").click();
                }
                $("#emailInput").val(user.email);
                $("#identityInput").val(user.idCardNo);
                $("#birthInput").val(user.birth);
                $("#jobInput").val(user.position);
                $("#educationInput").val(user.education);
                $("#universityInput").val(user.graduateSchool);
                $("#graducateTime").val(user.graduateDate);
                $("#majorInput").val(user.major);
                $("#resumeUrl").val(user.resumeUrl);
                $("#telInput").val(user.tel);
            } else {
                $("#userTitle")[0].innerText = "创建考生";
                $("#pass1").show();
                $("#pass2").show();
                $('#companyUserAddSelect').val("");
                $('#companyUserAddSelect').trigger('change');
                resetUserModal();
            }
            $('#new_user_modal2').modal(
                    {
                        show:true,
                        keyboard:false,
                        backdrop:'static'
                    }
            );
            setTimeout(function () {
                $('#userName').focus();
            }, 800);
        }
        //初始化修改密码用户对象
        function initUpdatePasswordUser(userId) {
            if (userId && userListMap && userListMap[userId]) {
                var user = userListMap[userId];
                currentUser.user = JSON.parse(JSON.stringify(user));
                $('#change_password_modal').modal(
                        {
                            show:true,
                            keyboard:false,
                            backdrop:'static'
                        }
                );
            }
        }
        //修改用户密码
        function updatePassword() {
            if (!currentUser.user) {
                warn("暂无此用户信息！请刷新页面后重试");
                return;
            }

            var pwdUser = JSON.parse(JSON.stringify(currentUser));
            var password = $('#oldPassword').val().trim();
            var password1 = $('#newPassword1').val().trim();
            var password2 = $('#newPassword2').val().trim();
            if (password.length == 0) {
                warn("请输入原始密码！");
                return;
            }
            if (password1.length == 0) {
                warn("请输入新密码！");
                return;
            }
            if (password2.length == 0) {
                warn("请输入确认密码！");
                return;
            }
            if (password2 != password1) {
                warn("两次新密码输入不一致！");
                return;
            }
            if (!(/^[a-zA-Z0-9_]{6,18}$/.test(password1))) {
                warn("不支持的密码格式！");
                return;
            }
            pwdUser.user.passWord = password;
            //确认原始密码
            $.ajax({
                url: "/zxks/rest/user/confirmPassword",
                type: "post",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: 118},
                data: JSON.stringify(pwdUser),
                success: function (resultData) {

                    if (resultData && resultData.resultCode == "SUCCESS") {
                        pwdUser.user.passWord = password1;
                        //修改密码
                        $.ajax({
                            url: "/zxks/rest/user/updatePassword",
                            type: "put",
                            contentType: "application/json; charset=UTF-8",
                            async: true,
                            dataType: "json",
                            headers: {userId: 118},
                            data: JSON.stringify(pwdUser),
                            success: function (resultData) {
                                if (resultData != null && resultData.resultCode != "SUCCESS") {
                                    warn(resultData.message);
                                }
                                if (resultData.resultCode == "SUCCESS") {
                                    warn("密码修改成功!");
                                    resetPasswordModalAndHide();
                                }
                            },
                            error: function (xhr, r, e) {
                                warn(e);
                                //resetUserModalAndHide();
                            }
                        });
                    } else if (resultData && resultData.message) {
                        warn(resultData.message);
                    }
                },
                error: function (xhr, r, e) {
                    warn(e);
                    //resetUserModalAndHide();
                }
            });
        }
        //重设用户密码
        function resetPassword(userId) {
            confirm("确定要重设该用户的密码吗？", 'resetPasswordConfirmed(' + userId + ')');
        }
        function resetPasswordConfirmed(userId) {
            var pwdUser = {
                "user": {
                    "userId": userId
                }
            };
            //重设密码
            $.ajax({
                url: "/zxks/rest/user/resetPassword",
                type: "put",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: 118},
                data: JSON.stringify(pwdUser),
                success: function (resultData) {
                    if (resultData != null && resultData.resultCode != "SUCCESS") {
                        warn(resultData.message);
                    }
                    if (resultData.resultCode == "SUCCESS") {
                        warn("密码重设成功!");
                        resetPasswordModalAndHide();
                    }
                },
                error: function (xhr, r, e) {
                    warn(e);
                    //resetUserModalAndHide();
                }
            });
        }
        //添加和修改用户至后台
        function addOrUpdateUser() {
            var actionType = "add"; //add 添加 update修改
            var postType = "POST";
            var userTemp;
            if (currentUser.user) {
                actionType = "update";
                postType = "PUT";
                userTemp = JSON.parse(JSON.stringify(currentUser))
            } else {
                userTemp = {"user": {}};
            }
            var userName = $('#userName').val().trim();
            var realName = $('#realName').val().trim();
            var passWord = $('#passWord').val();
            var passWordConfirm = $('#passWordConfirm').val();
            var companyId = $('#companyUserAddSelect').val();
            var deptId = $('#subDeptUserAddSelect').val();
            var gender = $(' input[type="radio"]:checked').val();
            var email = $("#emailInput").val().trim();
            var idCardNo = $("#identityInput").val().trim();
            var birth = $("#birthInput").val().trim();
            var position = $("#jobInput").val().trim();
            var education = $("#educationInput").val();
            var graduateSchool = $("#universityInput").val().trim();
            var graduateDate = $("#graducateTime").val();
            var major = $("#majorInput").val().trim();
            var resumeUrl = $("#resumeUrl").val().trim();
            var tel = $("#telInput").val().trim();
            var userNameReg = /(^[A-Za-z0-9]{6,15}$)/;//用户名正则
            var realNameReg =/^[\u4e00-\u9fa5]{2,5}$|^[a-zA-Z]{2,10}$/;//真实姓名正则
            var mailReg = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;//邮箱正则
            var identityReg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;//身份证号正则
            var telReg = /^1[3|4|5|7|8][0-9]\d{4,8}$/;//电话号码正则
            //非空验证
            if (userName.length == 0) {
                warn( "请输入用户名！");
                $("#userName").focus();
                return;
            } else if (userName != "" && userNameReg.test(userName) === false) {
                warn( "用户名格式不正确！");
                $("#userName").focus();
                return;
            }
            if (realName.length == 0) {
                warn( "请输入真实姓名！");
                $("#realName").focus();
                return;
            }
            //真实姓名校验
            if(realNameReg.test(realName) === false) {
                warn( "真实姓名格式不正确！");
                $("#realName").focus();
                return false;
            }
            if (postType == "POST" && passWord.length == 0) {
                warn( "请输入密码！");
                $("#passWord").focus();
                return;
            }
            if (postType == "POST" && passWordConfirm.length == 0) {
                warn( "请输入确认密码！");
                $("#passWordConfirm").focus();
                return;
            }

            //非法验证
            if (postType == "POST" && passWord != passWordConfirm) {
                warn( "两次密码输入不一致！");
                $("#passWordConfirm").focus();
                return;
            }

            if (postType == "POST" && !(/^[a-zA-Z0-9_]{6,18}$/.test(passWord))) {
                warn( "不支持的密码格式！");
                $("#passWord").focus();
                return;
            }

/*            if (!companyId || companyId == "") {
                warnWithAlert("userWarn", "请选择单位！");
                return false;
            }*/

            //邮箱校验
            if (email.length > 0 && mailReg.test(email) === false) {
                warn( "邮箱格式不正确！");
                $("#emailInput").focus();
                return;
            }
            //身份证号校验
            if (idCardNo.length > 0 && identityReg.test(idCardNo) === false) {
                warn( "不支持的身份证号格式！");
                $("#identityInput").focus();
                return;
            }
            //电话号码校验
            if (tel.length > 0 && telReg.test(tel) === false) {
                warn( "电话号码格式不正确！");
                $("#telInput").focus();
                return;
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
                    warn( "出生时间应小于毕业时间！");
                    return;
                }
            }

            //设置未选择部门时为默认部门ID
            if (!deptId || deptId == "") {
                deptId = $('#parentDeptUserAddSelect').val();
            }
            if (!deptId || deptId == "") {
                deptId = 0;
            }

            userTemp.user.userName = userName;
            userTemp.user.passWord = passWord.trim();
            userTemp.user.realName = realName;
            userTemp.user.idCardNo = idCardNo;
            userTemp.user.company = companyId;
            userTemp.user.dept = deptId;
            userTemp.user.gender = gender;
            userTemp.user.email = email;
            userTemp.user.birth = birth;
            userTemp.user.position = position;
            userTemp.user.education = education;
            userTemp.user.graduateSchool = graduateSchool;
            userTemp.user.graduateDate = graduateDate;
            userTemp.user.major = major;
            userTemp.user.resumeUrl = resumeUrl;
            userTemp.user.tel = tel;
            $.ajax({
                url: "/zxks/rest/user/" + actionType,
                type: postType,
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: 118},
                data: JSON.stringify(userTemp),
                success: function (resultData) {
                    if (resultData != null) {
                        warn(resultData.message);
                    }
                    if (resultData.resultCode == "SUCCESS") {
                        getUserList();//重新加载用户列表
                        resetUserModalAndHide();
                    }
                },
                error: function (xhr, r, e) {
                    warn(e);
                    //resetUserModalAndHide();
                }
            });
        }
        //删除用户
        function deleteUserById(userId) {
            confirm("确定要删除吗？", 'deleteUserByIdConfirmed(' + userId + ')');
        }
        function deleteUserByIdConfirmed(userId) {
            //验证是否有考试计划的考生
            $.ajax({
                url: "/zxks/rest/user/confirmDelete",
                type: "post",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: 118},
                data: JSON.stringify({"userIdList": [userId]}),
                success: function (resultData) {
                    if (resultData != null) {
                        if (resultData.resultCode == "SUCCESS") {
                            //说明有不可以删除的考生
                            if (resultData.data && resultData.data.length > 0) {
                                var strwarn = "该考生已被邀请过考试，不可删除！";
                                warn(strwarn);
                            } else {
                                //开始删除用户
                                $.ajax({
                                    url: "/zxks/rest/user/delete",
                                    type: "DELETE",
                                    contentType: "application/json; charset=UTF-8",
                                    async: true,
                                    dataType: "json",
                                    headers: {userId: 118},
                                    data: JSON.stringify({"user": {"userId": userId}}),
                                    success: function (resultData) {
                                        if (resultData != null) {
                                            warn(resultData.message);
                                        }
                                        getUserList();
                                    },
                                    error: function (xhr, r, e) {
                                        warn(e);
                                    }
                                });
                            }
                        } else {
                            warn(resultData.message);
                        }
                    }
                    console.log(resultData);
                    //getUserList();
                },
                error: function (xhr, r, e) {
                    warn(e);
                }
            });

        }
        //批量删除用户
        function deleteUserByList() {
            var userIdList = [];
            $("input[class='user']:checkbox").each(function () {
                if (true == $(this).is(':checked')) {
                    userIdList.push(Number($(this).val()));
                }
            });
            if (userIdList.length == 0) {
                warn("请选择需要删除的用户！");
                return;
            }
            confirm("确定要删除吗？", 'deleteUserByListConfirmed()');
        }

        //批量删除用户
        function deleteUserByListConfirmed() {
            var userIdList = [];
            $("input[class='user']:checkbox").each(function () {
                if (true == $(this).is(':checked')) {
                    userIdList.push(Number($(this).val()));
                }
            });
            if (userIdList.length == 0) {
                warn("请选择需要删除的用户！");
                return;
            }
            //验证是否有考试计划的考生
            $.ajax({
                url: "/zxks/rest/user/confirmDelete",
                type: "post",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: 118},
                data: JSON.stringify({"userIdList": userIdList}),
                success: function (resultData) {
                    if (resultData != null) {
                        if (resultData.resultCode == "SUCCESS") {
                            //说明有不可以删除的考生
                            if (resultData.data && resultData.data.length > 0) {
                                var strwarn = "以下考生已被邀请过考试，不可删除！<br>";
                                var i = 0;
                                $.each(resultData.data, function (index, user) {
                                    strwarn += user.userName + " , ";
                                    i++;
                                    if(i == 3){
                                        strwarn += "<br>";
                                        i = 0;
                                    }
                                });
                                //strwarn = strwarn.substring(0, strwarn.length - 1);
                                warn(strwarn);
                            } else {
                                //开始删除考生
                                $.ajax({
                                    url: "/zxks/rest/user/deleteUsers",
                                    type: "DELETE",
                                    contentType: "application/json; charset=UTF-8",
                                    async: true,
                                    dataType: "json",
                                    headers: {userId: 118},
                                    data: JSON.stringify({"userIdList": userIdList}),
                                    success: function (resultData) {
                                        if (resultData != null) {
                                            warn(resultData.message);
                                        }
                                        getUserList();
                                    },
                                    error: function (xhr, r, e) {
                                        warn(e);
                                    }
                                });
                            }
                        } else {
                            warn(resultData.message);
                        }
                    }
                    console.log(resultData);
                    //getUserList();
                },
                error: function (xhr, r, e) {
                    warn(e);
                }
            });
            return;
        }

        //清空添加和更新用户输入框内容并隐藏
        function resetUserModalAndHide() {
            resetUserModal();
            $('#new_user_modal2').modal('hide');
        }
        //清空添加和更新用户输入框内容
        function resetUserModal() {
            $('#userName').val("");
            $('#realName').val("");
            $("#male").click();
            $('#idCardNo').val("");
            $('#passWord').val("");
            $('#passWordConfirm').val("");
            $("#emailInput").val("");
            $("#identityInput").val("");
            $("#birthInput").val("");
            $("#jobInput").val("");
            $("#educationInput").val("");
            $("#universityInput").val("");
            $("#graducateTime").val("");
            $("#majorInput").val("");
            $("#resumeUrl").val("");
            $("#telInput").val("");
            $('#companyUserAddSelect').val("");
            $('#companyUserAddSelect').trigger('change');
            $('#parentDeptUserAddSelect').val("");
            $('#parentDeptUserAddSelect').trigger('change');
            $('#subDeptUserAddSelect').val("");
            currentUser = {};
        }
        //清空重置密码输入框内容
        function resetPasswordModalAndHide() {
            $('#oldPassword').val("");
            $('#newPassword1').val("");
            $('#newPassword2').val("");
            currentUser = {};
            $('#change_password_modal').modal('hide');
        }

    </script>
    <style>

        /*.treeview ul{*/
            /*overflow-y:scroll;*/
        /*}*/

        /*.treeview ul{*/
            /*overflow-y:scroll;*/
            /*height: 90%;*/
        /*}*/

        td,th{
            font-family:微软雅黑 !important;
        }
        #userListTable_info{
            font-family:微软雅黑;
        }
        #update_password_modal{
            top: 30% !important;
            z-index: 2000;
        }
        .modal-backdrop{
            background: #333 !important;
        }
        .editUser:hover{
            background: url("../images/edit_hover.png") !important;
        }
        .delUser:hover{
            background: url("../images/delete_hover.png") !important;
        }
        .resetUser:hover{
            background: url("../images/reset_hover.png") !important;
        }
        .columnLeft{
            text-align: left;
        }
        .btn-md1:hover{
            background-color:  #bd362f !important;
        }
        #searchValue{
            margin-top: 10px !important;
        }
        #btnAddUser{
            width: 100px;
        }
    </style>
</head>
<body style="background-color: #f1f4f8">
<div id="mainDiv">
    <div id="headDiv">
        <jsp:include page="menu.jsp"/>
    </div>
    <div id="contentDiv" style="text-align: left;">
        <div class="container-fluid">


            <div class="leftDiv">
                <div class="categoryDiv">
                    <div class="treeDiv">
                        <span class="fontSpan" style="font-family:微软雅黑">单位部门</span>
                        <div class="btn-group" style="margin:0 0 3px 0;float: right">
                            <button class="btn btn-default"
                                    title="新增单位或部门"
                                    data-toggle="dropdown"
                                    data-placement="auto left"
                                    data-trigger="hover focus"
                                    >
                                <span class="icon-plus"></span>
                            </button>
                            <button class="btn btn-default"
                                    title="删除单位或部门"
                                    onclick="beforeDelete()"
                                    data-toggle="tooltip"
                                    data-placement="auto bottom"
                                    data-trigger="hover focus"
                                    >
                                <span class="icon-minus"></span>
                            </button>
                            <button class="btn btn-default" style="border-top-right-radius: 4px;border-bottom-right-radius: 4px"
                                    title="编辑单位或部门"
                                    onclick="beforeUpdate()"
                                    data-toggle="tooltip"
                                    data-placement="auto right"
                                    data-trigger="hover focus"
                                    >
                                <span class="icon-pencil"></span>
                            </button>
                            <ul class="dropdown-menu">
                                <li><a href="#" data-toggle="modal" onclick="beforeCreateCompany()"
                                       data-backdrop="static">创建单位</a></li>
                                <li><a href="#" data-toggle="modal" onclick="beforeCreateDept()"
                                       data-backdrop="static">创建部门</a></li>
                            </ul>
                        </div>
                        <div id="treeview" class="treeview"></div>
                    </div>
                </div>
            </div>

            <div class="rightDiv">
                <div class="rightHeader" style=" margin-bottom: 10px;"><span style="margin-left: 15px;font-size: 16px;font-family:微软雅黑">考生管理</span></div>
                <div class="btn-group" style="width: 50%;position: absolute;top:162px;z-index:1;display: inherit;">
                    <button class="sure" id="btnAddUser" style="width: 100px" data-backdrop="static" onclick="initUpdateUser()"
                            type="button"
                            data-original-title="" title="" aria-describedby="null">
                        <span class="icon-plus icon-white" style="margin-top: 2px"></span>&nbsp添加考生
                    </button>
                    <!-- <button class="btn btn-default btn-md">
                         <span class="glyphicon glyphicon-paste"></span> 移动到
                     </button>-->
                    <button class="sure btn-md1" onclick="deleteUserByList()" style="width: 100px;border-radius: 4px;font-family:微软雅黑;background-color: #da4f49">
                        <span class="icon-remove icon-white" style="margin-top: 3px"></span> 批量删除
                    </button>
                </div>
                <div class="table-responsive">
                    <table id="userListTable" class="display">
                        <thead>
                        <tr>
                            <th style="text-align:center;" id="checkBoxAllTh"><input type="checkbox" id="checkBoxAllCheckBox"/></th>
                            <th style="text-align:center;">用户名</th>
                            <th style="text-align:center;">真实姓名</th>
                            <th style="text-align:center;">性别</th>
                            <th style="text-align:center;">身份证</th>
                            <th style="text-align:center;">所属单位</th>
                            <th style="text-align:center;">所属部门</th>
                            <th style="text-align:center;">手机号码</th>
                            <th style="text-align:center;width:10%">操作</th>
                        </tr>
                        </thead>
                        <tbody id="targetBody">
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    </div>

    <!-- 创建单位的模态框 -->
    <div class="modal fade hide" id="new_company_modal" style=" top: 30%; left: 45%;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                    <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true">
                    </button>
                    创建单位
                </div>
                <div class="modal-body" style=" width: 600px;">
                    <table style="width: 94%;border: none;">
                        <tr>
                            <td width="25%" style="text-align: center"><label><span
                                    style="color:red;">*</span> 单位名称</label>
                            </td>
                            <td width="75%"><input type="text" id="companyName" class="form-control" style="height: 32px;margin-left: 5px"  maxlength="30">
                            </td>
                        </tr>
                    </table>
                </div>
                <!--警告框-->
                <div class="alert alert-block" style="color: #ff6c60;display: none;" id="addCompanyWarn"></div>
                <div class="modal-footer">
                    <button class="cancel" data-dismiss="modal">取消</button>
                    <button class="sure" onclick="addCompany();">确定</button>
                </div>
            </div>
        </div>
    </div>

    <!-- 修改单位的模态框 -->
    <div class="modal fade hide" id="update_company_modal" style=" top: 30%; left: 45%;">
        <div class="modal-dialog" style=" width: 600px;">
            <div class="modal-content">
                <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                    <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true">
                    </button>
                    修改单位
                </div>
                <div class="modal-body">
                    <table style="width: 94%;border: none;">
                        <tr>
                            <td width="25%" style="text-align: right"><label><span
                                    style="color:red;">*</span> 单位名称</label>
                            </td>
                            <td width="75%"><input type="text" id="companyNameUpdate" class="form-control" style="height: 32px;margin-left: 5px"  maxlength="30">
                            </td>
                        </tr>
                    </table>
                </div>
                <!--警告框-->
                <div class="alert alert-block" style="color: #ff6c60;display: none;" id="updateCompanyWarn"></div>
                <div class="modal-footer">
                    <button class="cancel" data-dismiss="modal">取消</button>
                    <button class="sure" onclick="updateCompany();">确定</button>
                </div>
            </div>
        </div>
    </div>

    <!-- 创建部门的模态框 -->
    <div class="modal fade hide" id="new_department_modal" style=" top: 30%; left: 45%;">
        <div class="modal-dialog" style=" width: 600px;">
            <div class="modal-content">
                <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                    <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true">
                    </button>
                    创建部门
                </div>
                <div class="modal-body">

                    <table style="width: 94%;border: none;">
                        <tr>
                            <td width="25%" style="text-align: right"><label><span
                                    style="color:red;">*</span> 选择单位</label>
                            </td>
                            <td width="75%"><select name="companySelect" id="companyAddSelect" class="form-control"
                                                    style="height: 32px;margin-left: 5px;margin-bottom: 10px;white-space: pre-wrap"></select>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" style="text-align: right"><label><span
                                    style="color:red;">*</span> 部门名称</label>
                            </td>
                            <td width="75%"><input type="text" id="deptNameAdd" class="form-control" maxlength="20"
                                                    style="height: 32px;margin-left: 5px"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <!--警告框-->
                <div class="alert alert-block" style="color: #ff6c60;display: none;" id="addDeptWarn"></div>
                <div class="modal-footer">
                    <button class="cancel" data-dismiss="modal">取消</button>
                    <button class="sure" onclick="addDept();">确定</button>
                </div>
            </div>
        </div>
    </div>

    <!-- 修改部门的模态框 -->
    <div class="modal fade hide" id="update_department_modal" style=" top: 30%; left: 45%;">
        <div class="modal-dialog" style=" width: 600px;">
            <div class="modal-content">
                <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                    <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true">
                    </button>
                    修改部门
                </div>
                <div class="modal-body">
                    <table style="width: 94%;border: none;">
                        <tr>
                            <td width="25%" style="text-align: right"><label><span
                                    style="color:red;">*</span> 选择单位</label>
                            </td>
                            <td width="75%"><select name="companyUpdateSelect" id="companyUpdateSelect" class="form-control"
                                                    style="height: 32px;margin-left: 5px;margin-bottom: 10px;"  maxlength="30" ></select>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" style="text-align: right"><label><span
                                    style="color:red;">*</span> 部门名称</label>
                            </td>
                            <td width="75%"><input type="text" id="deptNameUpdate" class="form-control" maxlength="20"
                                                   style="height: 32px;margin-left: 5px"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <!--警告框-->
                <div class="alert alert-block" style="color: #ff6c60;display: none;" id="updateDeptWarn"></div>
                <div class="modal-footer">
                    <button class="cancel" data-dismiss="modal">取消</button>
                    <button class="sure" onclick="updateDept();">确定</button>
                </div>
            </div>
        </div>
    </div>

    <!-- 创建考生模态框 -->
    <div class="modal fade hide" id="new_user_modal2" style=" top: 40%; left: 35%;"><!-- fade是设置淡入淡出效果 -->
        <div class="modal-dialog " style="width: 780px;">
            <div class="modal-content">
                <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                    <button type="button" class="closeTable"  data-dismiss="modal" aria-hidden="true">

                    </button>
                    <span id="userTitle">创建考生</span>

                </div>
                <div class="modal-body">
                    <div class="registerTab">

                        <div class="requiredField">
                            <form class="form-horizontal">

                                <div class="formGroup">
                                    <label class="col-sm-4" style="text-align: right"><span
                                            style="color:red;">*</span>用户名:</label>

                                    <div class="col-sm-8">
                                        <input type="text" maxlength="15" id="userName" class="form-control"
                                               placeholder="6-15位字母或数字">
                                    </div>
                                </div>
                                <div class="formGroup">
                                    <label class="col-sm-4" style="text-align: right"><span
                                            style="color:red;">*</span>真实姓名:</label>

                                    <div class="col-sm-8">
                                        <input type="text" maxlength="5" id="realName" class="form-control"
                                               placeholder="2-5位中文或2-10位英文">
                                    </div>
                                </div>
                                <div class="formGroup">
                                    <label class="col-sm-4" style="text-align: right"><span
                                            style="color:red;">*</span>性别:</label>

                                    <div class="col-sm-8" style="margin-top: -10px;">
                                        <input type="radio" id="male" style="margin-bottom: 20px;" class="genderInput" name="genderInput"
                                               value="1">男
                                        <input type="radio" id="female" style="margin-bottom: 20px;"  class="genderInput" name="genderInput"
                                               value="2">女
                                    </div>
                                </div>
                                <div class="formGroup" id="pass1">
                                    <label class="col-sm-4" style="text-align: right"><span
                                            style="color:red;">*</span>密码:</label>

                                    <div class="col-sm-8">
                                        <input type="password" id="passWord" maxlength="18" class="form-control"
                                               placeholder="6-18位数字、字母、下划线">
                                    </div>
                                </div>
                                <div class="formGroup" id="pass2">
                                    <label class="col-sm-4" style="text-align: right"><span
                                            style="color:red;">*</span>确认密码:</label>

                                    <div class="col-sm-8">
                                        <input type="password" id="passWordConfirm" class="form-control"
                                               maxlength="18"
                                               placeholder="6-18位数字、字母、下划线">
                                    </div>
                                </div>
                                <div class="formGroup">
                                    <label class="col-sm-4" style="text-align: right">所属单位:</label>

                                    <div class="col-sm-8">
                                        <select name="companySelect" id="companyUserAddSelect" class="form-control"
                                         style="margin-left: 5px;"       placeholder="请选择单位">
                                        </select>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <span class="separateLine"></span>

                        <div class="optionalField">
                            <form id="userForm" class="form-horizontal">
                                <div class="formGroup">
                                    <label class="col-sm-4" style="text-align: right"> 所属部门:</label>

                                    <div class="col-sm-8">
                                        <select name="companySelect" id="parentDeptUserAddSelect"
                                                class="form-control"
                                                style="margin-left: 5px;"
                                                placeholder="请选择部门">
                                        </select>
                                        <select name="companySelect" id="subDeptUserAddSelect" class="form-control"
                                                style="margin-top: 10px;display:none;"
                                                placeholder="请选择部门">
                                        </select>
                                    </div>
                                </div>
                                <div class="formGroup" <%--style="margin-top: 45px;"--%>>
                                    <label class="col-sm-4" style="text-align: right">邮箱:</label>

                                    <div class="col-sm-8">
                                        <input type="text" class="form-control" id="emailInput" maxlength="30"
                                               placeholder="例如：12345abc@163.com">
                                    </div>
                                </div>
                                <div class="formGroup">
                                    <label class="col-sm-4" style="text-align: right">身份证号:</label>

                                    <div class="col-sm-8">
                                        <input type="text" class="form-control" id="identityInput" maxlength="18"
                                               placeholder="15位或18位或17X(数字)">
                                    </div>
                                </div>
                                <div class="formGroup">
                                    <label class="col-sm-4" style="text-align: right">出生年月:</label>

                                    <div class="col-sm-8">
                                        <div class="input-group date form_datetime col-md-6">
                                            <input type="text" id="birthInput" maxlength="12"
                                                   style="cursor: pointer;height: 34px;width: 100%;margin-left: 5px;" class="inputTime"
                                                   >
                    <span class="input-group-addon" style="display: inline-block;float: right;margin-top: -32px;
                          margin-right: 10px;">
                        <span class="glyphicon glyphicon-th"><i class='icon-th'></i></span>
                    </span>
                                        </div>
                                    </div>
                                </div>
                                <div class="formGroup">
                                    <label class="col-sm-4" style="text-align: right">职位:</label>

                                    <div class="col-sm-8">
                                        <input type="text" class="form-control" id="jobInput" maxlength="15">
                                    </div>
                                </div>
                                <div class="formGroup">
                                    <label class="col-sm-4" style="text-align: right">学历:</label>

                                    <div class="col-sm-8">
                                        <select  style="margin-left: 5px;" class="form-control" id="educationInput">
                                            <option>专科</option>
                                            <option>本科</option>
                                            <option>硕士</option>
                                            <option>博士</option>
                                            <option>其它</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="formGroup">
                                    <label class="col-sm-4" style="text-align: right">毕业院校:</label>

                                    <div class="col-sm-8">
                                        <input type="text" class="form-control" id="universityInput" maxlength="15">
                                    </div>
                                </div>
                                <div class="formGroup">
                                    <label class="col-sm-4" style="text-align: right">毕业时间:</label>

                                    <div class="col-sm-8">
                                        <div class="input-group date form_datetime col-md-6">
                                            <input type="text" class="inputTime" id="graducateTime" maxlength="12"
                                                   style="cursor: pointer;height: 34px;width: 100%;margin-left: 5px;" >
                    <span class="input-group-addon" style="display: inline-block;float: right;margin-top: -32px;
                          margin-right: 10px;">
                        <span class="glyphicon glyphicon-th"><i class='icon-th'></i></span>
                    </span>
                                        </div>
                                    </div>
                                </div>
                                <div class="formGroup">
                                    <label class="col-sm-4" style="text-align: right">专业:</label>

                                    <div class="col-sm-8">
                                        <input type="text" class="form-control" id="majorInput" maxlength="15">
                                    </div>
                                </div>
                                <div class="formGroup">
                                    <label class="col-sm-4" style="text-align: right">简历地址:</label>

                                    <div class="col-sm-8">
                                        <input type="text" class="form-control" id="resumeUrl" maxlength="30">
                                    </div>
                                </div>
                                <div class="formGroup">
                                    <label class="col-sm-4" style="text-align: right">手机号码:</label>

                                    <div class="col-sm-8">
                                        <input type="text" class="form-control" id="telInput" maxlength="11"
                                               placeholder="11位数字">
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <!--警告框-->
                <div class="alert alert-block" style="color: #ff6c60;display: none;" id="userWarn"></div>
                <div class="modal-footer">
                    <button class="cancel" data-dismiss="modal">取消</button>
                    <button class="sure" onclick="addOrUpdateUser();">确定</button>
                </div>

            </div>
        </div>
    </div>
    <!-- 弹出框模态框 -->
    <div class="modal fade dialog hide" id="warnModal" style="top:250px;"><!-- fade是设置淡入淡出效果 -->
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                    <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true" >
                    </button>
                    提示框
                </div>
                <div class="modal-body" style="padding:20px 20px;">
                    <span id="warnTip"></span>
                </div>
                <div class="modal-footer">
                    <button class="cancel" data-dismiss="modal" style="width:70px;margin-right: 15px">关闭</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 弹出框确认模态框 -->
    <div class="modal fade dialog hide" id="sureModal" style="top:250px;"><!-- fade是设置淡入淡出效果 -->
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                    <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true">

                    </button>
                    提示框
                </div>
                <div class="modal-body" style="padding:20px 20px;">
                    <span id="sureTip"></span>
                </div>
                <div class="modal-footer">
                    <button class="cancel" data-dismiss="modal" style="width:70px;margin-right: 15px">取消</button>
                    <button class="sure" id="btnExecute" data-dismiss="modal" style="width: 70px;">确定</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!--用来判断字符串实际长度-->
<span id="word-cal" style="visibility: hidden;white-space: nowrap;"></span>

</body>
</html>
