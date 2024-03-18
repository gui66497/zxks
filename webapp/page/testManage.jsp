<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="../images/favicon.ico" rel="icon" type="image/x-icon"/>
    <title>在线考试系统--考试计划</title>
    <link rel="stylesheet" type="text/css" href="../exts/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="../css/base.css">
    <link rel="stylesheet" type="text/css" href="../css/square/blue.css">
    <link rel="stylesheet" type="text/css" href="../css/itemBankManage.css">
    <link rel="stylesheet" type="text/css" href="../css/userManage.css">
    <link rel="stylesheet" type="text/css" href="../css/testManage.css">
    <link rel="stylesheet" type="text/css" href="../exts/datatables/css/jquery.dataTables.css">
    <link rel="stylesheet" type="text/css" href="../exts/bootstrap/css/bootstrap-modal.css">
    <link rel="stylesheet" type="text/css" href="../css/bootstrap-treeview.css">
    <link rel="stylesheet" type="text/css" href="../css/basic.css">
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
    <!--日期控件-->
    <script src="../exts/bootstrap-datetimepicker.js"></script>
    <script src="../exts/bootstrap-datetimepicker.min.js"></script>
    <script src="../exts/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="application/javascript" src="../js/testPaper/testPaperPublic.js"></script>
    <!--加载日期控件-->
    <script type="text/javascript">
        $(function () {
            var date = new Date();
            date.getDate();
            //日期插件
            $(".form_datetime").datetimepicker({
                format: "yyyy-mm-dd hh:ii",
                autoclose: true,
                todayBtn: true,
                todayHighlight: true,
                weekStart: 1,
                showMeridian: true,
                startDate: date,
                pickerPosition: "bottom-left",
                language: 'zh-CN',//中文，需要引用zh-CN.js包
                startView: 2,//月视图
                minView: 0,//日期时间选择器所能够提供的最精确的时间选择视图
            });
            $(".form_date").datetimepicker({
                format: "yyyy-mm-dd hh:ii",
                autoclose: true,
                todayBtn: true,
                todayHighlight: true,
                weekStart: 1,
                showMeridian: true,
                startDate: date,
                pickerPosition: "bottom-left",
                language: 'zh-CN',//中文，需要引用zh-CN.js包
                startView: 2,//月视图
                minView: 0,//日期时间选择器所能够提供的最精确的时间选择视图
            });

        })

    function getTestTime(){
      if($("#paperSelect option:selected") && $("#paperSelect option:selected").attr("testTime")){
        $("#testTime")[0].innerHTML="(试卷时长为:"+$("#paperSelect option:selected").attr("testTime")+")";
      }else{
        $("#testTime")[0].innerHTML="";
      }
    }
  </script>

    <!--初始化check-->
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
                hide: 100
            }
            //placement:left,
        });

        $('button').tooltip({
            delay: {
                show: 500,
                hide: 100
            },
            container: 'body'
        });

    </script>
    <style type="text/css">
        .node-treeview ::selection {
            background-color: #28d6d7;
        }

        td, th {
            font-family: 微软雅黑 !important;
        }

        .columnLeft {
            text-align: left;
        }

        .editTest {
            width: 20px;
            height: 20px;
            border: 0;
            background: url("../images/edit.png");
        }

        .editTest:hover {
            background: url("../images/edit_hover.png");
        }

        .deleteTest {
            width: 20px;
            height: 20px;
            border: 0;
            background: url("../images/delete.png");
        }

        .deleteTest:hover {
            background: url("../images/delete_hover.png");
        }
    </style>
    <!--提示框和确认提示框-->
    <script type="text/javascript">
        function warn(strMessage) {
            $('#warnTip').text(strMessage);
            $('#warnModal').modal(
                    {
                        show: true,
                        keyboard: false,
                        backdrop: 'static'
                    }
            );
        }
        function confirm(strMessage, strfunc) {
            $('#sureTip').text(strMessage);
            $('#btnExecute').attr('onclick', strfunc);
            $('#sureModal').modal(
                    {
                        show: true,
                        keyboard: false,
                        backdrop: 'static'
                    }
            );
        }

        function warnWithAlert(id, strMessage) {
            showAlert(id, strMessage);
        }
        function showAlert(id, message) {
            //$("#"+id).removeClass("hide");
            $("#" + id).text('');
            $("#" + id).append('<button type="button"class="close" onclick="hideAlert(\'' + id + '\')">&times;</button>');
            $("#" + id).append(message);
            $("#" + id).show();
            $("#" + id).delay(2000).hide("fast");
            //$("#"+id).removeClass("hide");
        }

    </script>
    <!--考试类别、考卷列表-->
    <script type="text/javascript">
        var currentNode = null; //当前选中节点
        var testPaperCategoryMapById = {};
        var topCategories = [];
        var subCategories = [];
        var dataTableJson = {
            "bAutoWidth": false,
            "language": {
                "sSearchPlaceholder": "请输入试卷名称"
            },
            "bServerSide": true,                    //指定从服务器端获取数据
            "bProcessing": true,                    //显示正在处理进度条
            "sAjaxSource": "../rest/testInfo/filterTestInfoList",    //服务端Rest接口访问路径
            "columns": [
                {
                    "data": "operate",
                    'sClass': 'center',
                    'width': '5%',
                    "mRender": function (data, type, full) {
                        return '';
                    }
                },
                {"data": "testpaperName", 'sClass': 'columnLeft', 'width': '24%'},
                {"data": "testCategory", 'sClass': 'columnLeft', 'width': '10%'},
                {"data": "startTime", 'sClass': 'center', 'width': '22%'},
                {"data": "totalUserNo", 'sClass': 'right', 'width': '7%'},
                {"data": "testUserNo", 'sClass': 'center', 'width': '7%'},
                {
                    "data": "operate", 'sClass': 'center',
                    'width': '8%',
                    "mRender": function (data, type, full) {
                        //warn(aData.itemId);
                        return '';
                    }
                },
                {
                    "data": "operate", 'sClass': 'center',
                    'width': '8%',
                    "mRender": function (data, type, full) {
                        //warn(aData.itemId);
                        return '';
                    }
                }],
            "aoColumnDefs": [                                       //指定列附加属性
                {"bSortable": false, "aTargets": [0, 1, 2, 4, 5, 6, 7]},        //这句话意思是第1,3（从0开始算）不能排序
                {"bSearchable": false, "aTargets": [0, 1, 4, 5, 7]},   //bSearchable 这个属性表示是否可以全局搜索，其实在服务器端分页中是没用的
            ],
            "aaSorting": [[3, "desc"]], //默认排序
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {     // 当创建了行，但还未绘制到屏幕上的时候调用，通常用于改变行的class风格
                // $('td:eq(0)', nRow).append("<input class='test' value='" + aData.testId + "' type='checkbox'/>");
                testInfoMap[aData.id] = aData;
                $('td:eq(0)', nRow).append(nRow._DT_RowIndex + 1);
                $('td:eq(1)', nRow).html($.testPaperPublic.returnSpan(aData.testpaperName));
                $('td:eq(3)', nRow).html(aData.startTime + "&nbsp;～&nbsp;" + aData.endTime);
                $('td:eq(4)', nRow).append('&nbsp;&nbsp;&nbsp;&nbsp;<button title="查看人数" class="showPeople" onclick="getPeopleNames(' +aData.id + ',\'' +aData.testpaperName +'\')"></button>');

                if (aData.endTime && aData.startTime) {
                    var statusSapn = "<span class='testStatus' testId='" + aData.id + "' endTime='" + aData.endTime + "' startTime='" + aData.startTime + "'></span>";
                }
                $('td:eq(6)', nRow).append(statusSapn);
                var options = "";
                options += "<button editId='" + aData.id + "' data-original-title='编辑' title='编辑考试' class='editTest' style='text-decoration:none;' href='javascript:void(0);'" +
                        "onclick='editTestInfo(" + aData.id + ",\"" + aData.startTime + "\",\"" + aData.endTime + "\");' data-placement='auto top' data-trigger='hover focus'>" +
                        "</button>&nbsp;&nbsp;";
                options += "<button delId='" + aData.id + "' data-original-title='删除' title='删除考试' class='deleteTest'  style='text-decoration:none;' href='javascript:void(0);'" +
                        "onclick='deleteTestInfoById(" + aData.id + ");' data-placement='auto top' data-trigger='hover focus'>" +
                        "</button>&nbsp;&nbsp;";
                $('td:eq(7)', nRow).append(options);
                return nRow;
            },
            "fnServerData": function (sSource, aoData, fnCallback) {
                //var searchName = $('#searchValue').val().trim();
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
                    data: JSON.stringify(testInfoSearch),
                    "success": function (resp) {
                        fnCallback(resp);
                    }
                });
            }
        };
        var testInfoMap = {};
        $(function () {
            table = $('#testListTable').DataTable(dataTableJson);
            buildDomTree();

            $('#testListTable').on('draw.dt', function () {
                $('#checkBoxAllCheckBox').iCheck({
                    checkboxClass: 'icheckbox_square-blue',  // 注意square和blue的对应关系
                    radioClass: 'iradio_square-blue',
                    increaseArea: '20%' // optional
                });
                $('input.test').iCheck({
                    checkboxClass: 'icheckbox_square-blue',  // 注意square和blue的对应关系
                    radioClass: 'iradio_square-blue',
                    increaseArea: '20%' // optional
                })
                $("#checkBoxAllCheckBox").on("ifChecked", function (event) {
                    $('input.test').iCheck("check");
                });
                $("#checkBoxAllCheckBox").on("ifUnchecked", function (event) {
                    $('input.test').iCheck("uncheck");
                });
                var checkboxes = $("input.test");
                checkboxes.on("ifChanged", function (event) {
                    if (checkboxes.filter(':checked').length == checkboxes.length) {
                        $("#checkBoxAllCheckBox").prop("checked", "checked");
                    } else {
                        $("#checkBoxAllCheckBox").removeProp("checked");
                    }
                    $("#checkBoxAllCheckBox").iCheck("update");
                });
                if ($("#checkBoxAllCheckBox").is(":checked")) {
                    $('input.test').iCheck("check");
                } else {
                    $('input.test').iCheck("uncheck");
                }
            });
        });
        table = $('#testListTable').DataTable(dataTableJson);

        function convertToPagingEntity(aoData) {
            var pagingEntity = new Object();
            $.each(aoData, function (i, val) {
                if (val.name == "iDisplayStart") {
                    pagingEntity.startIndex = val.value;
                } else if (val.name == "iDisplayLength") {
                    pagingEntity.pageSize = val.value;
                } else if (val.name == "sSearch") {
                    pagingEntity.searchValue = val.value.trim();
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
            $('#' + strPaperListDomId).trigger('change');
            $.ajax({
                "type": 'POST',
                "contentType": "application/json",
                "url": "../rest/testPaper/paperList",
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
                    console.log("试卷信息");
                    console.log(resultData);
                    if (resultData.data != null) {
                        if (resultData.data.length == 0) {
                            warnWithAlert("testWarn", "该试卷类别下无考卷！");
                            return;
                        }
                    }
                    $('#' + strPaperListDomId).empty();
                    if (resultData && resultData.data) {
                        var optionHtml = "";
                        //optionHtml="<option></option>";
                        $.each(resultData.data, function (index, testPaper) {
                            //添加下拉框
                            optionHtml += "<option testTime='" + testPaper.testTime + "' value='" + testPaper.id + "'>" + testPaper.testpaperName + "</option>";
                        });
                        $('#' + strPaperListDomId).append(optionHtml);

                    }
                    $('#' + strPaperListDomId).trigger('change');
                },
                error: function (xhr, r, e) {
                    warn(e);
                }
            });
        }
        //构建树型目录
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
                    var optionHtml = "";//"<option value=''></option>"
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
                    initSelectCategories("parentCategoriesAddSelect", "subCategoriesAddSelect", '<option value="-1"></option>' + optionHtml);

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
                        table.search('');//将页面快速检索框中内容清空
                        table.ajax.reload();
                    });
                    $('#treeview').on('nodeUnselected', function (event, data) {
                        currentNode = null;
                        table.destroy();
                        table = $('#testListTable').DataTable(dataTableJson);
                        table.search('');//将页面快速检索框中内容清空
                        table.ajax.reload();
                    });
                },
                error: function (xhr, r, e) {
                    warn(e);
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
                        $("button[editId=" + span.getAttribute("testId") + "]").attr("style","background:url('../images/edit_disabled.png');cursor:default");
                        $("button[delId=" + span.getAttribute("testId") + "]").attr("style","background:url('../images/bin_disabled.png');cursor:default");
                        $("button[editId=" + span.getAttribute("testId") + "]").attr("disabled","disabled");
                        $("button[delId=" + span.getAttribute("testId") + "]").attr("disabled","disabled");
                    } else {
                        if (now >= dStart) {
                            span.setAttribute("style", "color:green");
                            span.innerHTML = "开考中";
                            $("button[editId=" + span.getAttribute("testId") + "]").attr("style","background:url('../images/edit_disabled.png');cursor:default");
                            $("button[delId=" + span.getAttribute("testId") + "]").attr("style","background:url('../images/bin_disabled.png');cursor:default");
                            $("button[editId=" + span.getAttribute("testId") + "]").attr("disabled","disabled");
                            $("button[delId=" + span.getAttribute("testId") + "]").attr("disabled","disabled");
                        } else {
                            span.setAttribute("style", "color:red");
                            span.innerHTML = "未开考";
                            $("button[editId=" + span.getAttribute("testId") + "]").show();
                            $("button[delId=" + span.getAttribute("testId") + "]").show();
                        }
                    }

                }
            });
        }, 100);
    </script>
    <!--单位部门-->
    <script type="text/javascript">
        var data = [];
        var companies = [];
        var allDepts = [];
        var topDepts = [];
        var subDepts = [];
        var currentCompanyAndDeptNode = null; //当前选中节点
        var companyAndDeptoptions;
        var deptsGroupByCompany = {};
        var deptsMapById = {};
        var companiesMapById = {};
        function addNodes2(targetDept) {
            $.each(subDepts, function (index2, subDept) {
                if (subDept.parentId == targetDept.deptId) {
                    if (!targetDept.nodes) {
                        targetDept.nodes = []
                    }
                    var subNode = {
                        id: subDept.deptId,
                        text: returnSpan(subDept.deptName),
                        href: "#",
                        deptId: subDept.deptId,
                        deptName: subDept.deptName,
                        dept: subDept,
                    };
                    targetDept.nodes.push(subNode);
                    addNodes2(subNode);
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
        function loadCompaniesAndDepts() {
            companies = [];
            allDepts = [];
            topDepts = [];
            subDepts = [];
            currentCompanyAndDeptNode = null; //当前选中节点
            companyAndDeptoptions = {};
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
                    $.each(companies, function (index, company) {
                        if (!companiesMapById[company.companyId]) {
                            companiesMapById[company.companyId] = company;
                        }
                        company.company = JSON.parse(JSON.stringify(company));
                    });

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
                                    /*  $.each(topDepts, function (index, dept) {
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
                                     addNodes2(subNode);
                                     }
                                     });
                                     });*/

                                }
                                $.each(companies, function (index, company) {
                                    company.id = company.companyId;
                                    company.text = returnSpan(company.companyName);
                                    company.href = "#";
                                    company.state = {
                                        selected: false
                                    };
                                    $.each(topDepts, function (index2, topDept) {
                                        if (topDept.companyId == company.companyId) {
                                            if (!company.nodes) {
                                                company.nodes = []
                                            }
                                            var subNode = {
                                                id: topDept.deptId,
                                                text: returnSpan(topDept.deptName),
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
                                companyAndDeptoptions = {
                                    levels: 2,
                                    data: companies,
                                    expandIcon: 'icon-plus',
                                    collapseIcon: 'icon-minus',
                                    emptyIcon: 'icon'
                                };
                                $('#treeview1').treeview(companyAndDeptoptions);
                                $('#treeview1').on('nodeSelected', function (event, data) {
                                    currentCompanyAndDeptNode = data;
                                    setTimeout(function () {
                                        getUserList();
                                    }, 200);
                                });
                                $('#treeview1').on('nodeUnselected', function (event, data) {
                                    currentCompanyAndDeptNode = null;
                                    setTimeout(function () {
                                        getUserList();
                                    }, 200);
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
            setTimeout(function () {
                loadCompaniesAndDepts();//加载单位部门分类
            }, 200)
        });

    </script>
    <!--邀请用户-->
    <script type="text/javascript">
        var selectedUsers;
        var addedUsers = {};
        var toAddUsers = [];
        var toRemoveUsers = [];
        var dataTableJsonAddedUser = {
            "bAutoWidth": false,
            "language": {
                "sSearchPlaceholder": "用户名或真实姓名"
            },
            "bDestroy":true,
            "searching": false,
            "bServerSide": false,                    //指定从服务器端获取数据
            "bProcessing": true,                    //显示正在处理进度条
            "columns": [
                {
                    "data": "operate",
                    'sClass': 'center',
                    "mRender": function (data, type, full) {
                        return '';
                    }
                },
                {"data": "userName", 'sClass': 'center'},
                {"data": "realName", 'sClass': 'center'},
                {"data": "gender", 'sClass': 'center'},
                {"data": "idCardNo", 'sClass': 'center'},
                {"data": "company", 'sClass': 'center'},
                {"data": "dept", 'sClass': 'center'},
            ],
            "aoColumnDefs": [                                       //指定列附加属性
                {"bSortable": false, "aTargets": [0, 2, 4, 5, 6]},        //这句话意思是第1,3（从0开始算）不能排序
                {"bSearchable": false, "aTargets": [0, 1, 2, 3, 4, 5]},   //bSearchable 这个属性表示是否可以全局搜索，其实在服务器端分页中是没用的
            ],
            "aaSorting": [[1, "asc"]], //默认排序
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {     // 当创建了行，但还未绘制到屏幕上的时候调用，通常用于改变行的class风格
                $('td:eq(0)', nRow).html("<input class='userAdded' name='" + nRow._DT_RowIndex + "'  value='" + aData.userId + "' type='checkbox'/>");

                if (aData.gender == 2) {
                    $('td:eq(3)', nRow).html("女");
                } else {
                    $('td:eq(3)', nRow).html("男");
                }

                if (companiesMapById[aData.company]) {
                    $('td:eq(5)', nRow).html(filterStringReturnSpan(companiesMapById[aData.company].companyName, 115));
                } else {
                    $('td:eq(5)', nRow).html("");
                }

                if (deptsMapById[aData.dept]) {
                    $('td:eq(6)', nRow).html(filterStringReturnSpan(deptsMapById[aData.dept].deptName, 115));
                } else {
                    $('td:eq(6)', nRow).html("");
                }
                return nRow;
            }
        };
        $(function () {
            setTimeout(function () {
                selectedUsers = $('#selectedUserList').DataTable(dataTableJsonAddedUser);
                $('#selectedUserList').on('draw.dt', function () {
                    $('#checkBoxAllSelectedUsers').iCheck({
                        checkboxClass: 'icheckbox_square-blue',  // 注意square和blue的对应关系
                        radioClass: 'iradio_square-blue',
                        increaseArea: '20%' // optional
                    });
                    $('input.userAdded').iCheck({
                        checkboxClass: 'icheckbox_square-blue',  // 注意square和blue的对应关系
                        radioClass: 'iradio_square-blue',
                        increaseArea: '20%' // optional
                    })
                    $("#checkBoxAllSelectedUsers").on("ifChecked", function (event) {
                        $('input.userAdded').iCheck("check");
                    });
                    $("#checkBoxAllSelectedUsers").on("ifUnchecked", function (event) {
                        $('input.userAdded').iCheck("uncheck");
                    });
                    var checkboxes = $("input.userAdded");
                    checkboxes.on("ifChanged", function (event) {
                        if (checkboxes.filter(':checked').length == checkboxes.length) {
                            $("#checkBoxAllSelectedUsers").prop("checked", "checked");
                        } else {
                            $("#checkBoxAllSelectedUsers").removeProp("checked");
                        }
                        $("#checkBoxAllSelectedUsers").iCheck("update");
                    });
                    $('#checkBoxAllSelectedUsers').iCheck("uncheck");
                    if ($("#checkBoxAllSelectedUsers").is(":checked")) {
                        $('input.userAdded').iCheck("check");
                    } else {
                        $('input.userAdded').iCheck("uncheck");
                    }
                });
            }, 200);
        });
        function initAllUserList() {
            $("#addUser").one("click", addUser);
            //备份已经选择的用户
            //bakSelectedUserIds = [];
            toAddUsers = [];
            toRemoveUsers = [];

            $("#userListTable_paginate").click(function () {
            });
            initSearchUser();

/*            var checkboxes = $("input.user");
            if (checkboxes.filter(':checked').length == checkboxes.length) {
                $("#checkBoxAllCheckBoxUser").prop("checked", "checked");
            } else {
                $("#checkBoxAllCheckBoxUser").removeProp("checked");
            }
            $("#checkBoxAllCheckBoxUser").iCheck("update");*/

        }

        var addUserDisabled = false;
        function addUser() {
            var selectedData1 = null;
            if(selectedUsers){
                selectedData1 = selectedUsers.data();
            }
            $("input[class='user']:checkbox").each(function () {

                if (true == $(this).is(':checked')) {
                    var userToAdd = userListMap[Number($(this).val())];
                    for(var i = 0;i<selectedData1.length;i++){
                        if(userToAdd!=null){
                            if(userToAdd.userId ==selectedData1[i].userId ){
                                userToAdd = null;
                            }
                        }
                    }
                    if(userToAdd!=null){
                        $('#selectedUserList').dataTable().fnAddData(userToAdd);
                    }
                }
            })

            $('#students_select_modal').modal('hide');
            initSearchUser();
            // $("#addUser").off("click");

        }

        function getIndexInSelectUser(userId) {
            var result = -1;
            $("input[class='userAdded']:checkbox").each(function (i) {
                if ($(this).val() == userId) {
                    result = i;
                    return true;
                }
            });
            return result;
        }
        function removeUsers() {
            var checkboxes = $("input.userAdded");
            if (checkboxes.filter(':checked').length < 1) {
                warn("请选择需要取消邀请的考生！");
                return;
            }
            var rowIndexToRemove = [];
            $("input[class='userAdded']:checkbox").each(function () {
                if (true == $(this).is(':checked')) {
                    addedUsers[Number($(this).val())] = false;
                    rowIndexToRemove.push(Number($(this).attr("name")));
                    var removeIndex = $.inArray(Number($(this).val()) + "", bakSelectedUserIds);
                    if (removeIndex > -1) {
                        bakSelectedUserIds.splice(removeIndex, 1);
                    }
                }
            });
            rowIndexToRemove = rowIndexToRemove.sort(function (a, b) {
                return b - a;
            });
            $.each(rowIndexToRemove, function (i, rowIndex) {
                $('#selectedUserList').dataTable().fnDeleteRow(rowIndex);
            });
        }
    </script>
    <!--用户列表-->
    <script type="text/javascript">
        function exchangeCheckBox(){
            var selectedData1 = null;
            var selectedData2 = null;
            if(selectedUsers){
                selectedData1 = selectedUsers.data();
            }
            if(userTable){
                selectedData2 = userTable.data();
            }
            if (selectedData1 && selectedData1.length > 0) {
                for (var i = 0; i < selectedData1.length; i++) {
                    if (selectedData2 && selectedData2.length > 0){
                        for (var j = 0; j < selectedData2.length; j++) {
                            if (selectedData1[i].userId == selectedData2[j].userId) {
                                $("#userCheck" + selectedData2[j].userId + "").iCheck("destroy");
                                $("#userCheck" + selectedData2[j].userId + "").attr("style","display:none");
                                $("#userCheck" + selectedData2[j].userId + "").iCheck("update");
                                $("#userCheck" + selectedData2[j].userId + "").removeClass("user");

                            }
                        }
                    }
                }
            }
        }

        var bakSelectedUserIds = [];
        var dataTableJsonUser = {
            "bAutoWidth": false,
            "language": {
                "sSearchPlaceholder": "用户名或真实姓名"
            },
            "bDestroy": true,
            "bServerSide": true,                    //指定从服务器端获取数据
            "bProcessing": true,                    //显示正在处理进度条
            "sAjaxSource": "../rest/user/searchByName",    //服务端Rest接口访问路径
            "columns": [
                {
                    "data": "operate",
                    'sClass': 'center',
                    "mRender": function (data, type, full) {
                        return '';
                    }
                },
                {"data": "userName", 'sClass': 'center'},
                {"data": "realName", 'sClass': 'center'},
                {"data": "gender", 'sClass': 'center'},
                {"data": "idCardNo", 'sClass': 'left'}],
            "aoColumnDefs": [                                       //指定列附加属性
                {"bSortable": false, "aTargets": [0, 4]},        //这句话意思是第1,3（从0开始算）不能排序
                {"bSearchable": false, "aTargets": [0, 1, 2, 3]},   //bSearchable 这个属性表示是否可以全局搜索，其实在服务器端分页中是没用的
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
                $('td:eq(0)', nRow).append("<input class='user' id='userCheck"+aData.userId+"' value='" + aData.userId + "' type='checkbox'/>");
                return nRow;
            },
            "fnServerData": function (sSource, aoData, fnCallback) {
                var userForSearch = JSON.parse(JSON.stringify(searchUser));
                if (currentCompanyAndDeptNode && currentCompanyAndDeptNode.company) {
                    userForSearch.user.company = currentCompanyAndDeptNode.company.companyId;
                } else if (currentCompanyAndDeptNode && currentCompanyAndDeptNode.dept) {
                    userForSearch.user.dept = currentCompanyAndDeptNode.dept.deptId;
                }
                userForSearch.paging = convertToPagingEntity(aoData);
                userForSearch.user.userName = userForSearch.paging.searchValue.trim();
                $.ajax({
                    "type": 'post',
                    "contentType": "application/json",
                    "url": sSource,
                    "dataType": "json",
                    "headers": {userId: 118},
                    "async": false,
                    data: JSON.stringify(userForSearch),
                    "success": function (resp) {
                        console.log(resp);
                        fnCallback(resp);
                    }
                });
            },
            "drawCallback": function () {
                initUserCheckbox();

            }
        };
        var allUserList = [];
        var userListMap = {};
        var currentUser = {}; //用于添加和更新用户
        var userTable;//用于刷新列表
        var searchUser = {
            "user": {
                "userName": ""
            }
        }; //用于检索用户
        var initSearchUser = function () {
            userTable = $('#userListTable').DataTable(dataTableJsonUser);
        };
        $(function () {
            initSearchUser();
        });

        //表格初始化完后初始化checkbox
        function initUserCheckbox() {
            $('#userListTable').on('draw.dt', function () {
                $('#checkBoxAllCheckBoxUser').iCheck({
                    checkboxClass: 'icheckbox_square-blue',  // 注意square和blue的对应关系
                    radioClass: 'iradio_square-blue',
                    increaseArea: '20%' // optional
                });
                $('input.user').iCheck({
                    checkboxClass: 'icheckbox_square-blue',  // 注意square和blue的对应关系
                    radioClass: 'iradio_square-blue',
                    increaseArea: '20%' // optional
                })
                var checkboxes = $("input.user");
                $("#checkBoxAllCheckBoxUser").on("ifChecked", function (event) {
                    $('input.user').iCheck("check");
                });
                $("#checkBoxAllCheckBoxUser").on("ifUnchecked", function (event) {
                    $('input.user').iCheck("uncheck");
                });

                checkboxes.on("ifChanged", function (event) {
                    if (checkboxes.length>0 && checkboxes.filter(':checked').length == checkboxes.length) {
                        $("#checkBoxAllCheckBoxUser").prop("checked", "checked");
                    } else {
                        $("#checkBoxAllCheckBoxUser").removeProp("checked");
                    }
                    $("#checkBoxAllCheckBoxUser").iCheck("update");
                });
                exchangeCheckBox(); //checkbox初始化并监听其事件
                if (checkboxes.length>0 && checkboxes.filter(':checked').length == checkboxes.length) {
                    $("#checkBoxAllCheckBoxUser").prop("checked", "checked");
                } else {
                    $("#checkBoxAllCheckBoxUser").removeProp("checked");
                }

            });
        }

        //加载用户列表
        function getUserList() {
            //userListMap = {};
            userTable.ajax.reload();
            return;
        }

    </script>
    <!--考试管理-->
    <script type="text/javascript">
        function resetTestModelInput() {
            addedUsers = {};
            $('#parentCategoriesAddSelect').val("");
            $('#parentCategoriesAddSelect').trigger('change');
            $('#subCategoriesAddSelect').val("");
            $('#subCategoriesAddSelect').trigger('change');
            $('#startTime').val("");
            $('#endTime').val("");
            $('#selectedUserList').dataTable().fnClearTable();
        }
        function resetTestModelInputAndHide() {
            resetTestModelInput();
            $('#paper_add_modal').modal('hide');
        }
        var currentState = true; //true 创建考试， false 编辑考试
        var currentTestId = 0;
        //取消创建或者编辑考试
        function cancelCreateOrUpdateTest() {
            bakSelectedUserIds = [];
        }
        //创建考试
        function createOrUpdateTest() {
            var userIdList = [];
            var nTrs = $('#selectedUserList').dataTable().fnGetNodes();
            var testId = $('#paperSelect').val();
            var startTime = $('#startTime').val().trim();
            var endTime = $('#endTime').val().trim();
            var testTime = $("#paperSelect option:selected").attr("testTime");
            if (!testId || testId == "") {
                warnWithAlert("testWarn", "请选择考卷！");
                return;
            }
            if (startTime.length == 0) {
                warnWithAlert("testWarn", "请选择开始时间！");
                return;
            }
            if (endTime.length == 0) {
                warnWithAlert("testWarn", "请选择结束时间！");
                return;
            }
            var dStart = new Date(startTime.replace(/\-/g, "\/"));
            var dEnd = new Date(endTime.replace(/\-/g, "\/"));


            if (dStart >= dEnd) {
                warnWithAlert("testWarn", "开始时间应小于结束时间！");
                return;
            }

            var currentTime = (dEnd - dStart) / 1000 / 60;
            if (currentTime < testTime) {
                warnWithAlert("testWarn", "时间区间应大于试卷时长！");
                return;
            }
            /*      var now = new Date();
             if (dStart <= now) {
             warnWithAlert("testWarn", "开始时间应大于当前时间！");
             return;
             }*/


            if (nTrs.length == 0) {
                warnWithAlert("testWarn", "请添加考生！");
                return;
            }
            $.each(nTrs, function (index, tr) {
                var tempUser = $('#selectedUserList').dataTable().fnGetData(tr);
                if (tempUser && tempUser.userId) {
                    userIdList.push(tempUser.userId);
                }
            });
            var testInfoTemp = {
                "testInfo": {
                    "testId": testId.toString(),
                    "userIdList": userIdList,
                    "totalUserNo": userIdList.length,
                    "noTestUserNo": userIdList.length,
                    "completeUserNo": 0,
                    "startTime": startTime,
                    "endTime": endTime,
                }
            };
            var url = "../rest/testInfo/add";
            var postType = "post";
            var strwarn = "添加";
            if (!currentState) {
                testInfoTemp.testInfo.id = currentTestId;
                url = "../rest/testInfo/update";
                postType = "put";
                strwarn = "修改";
            }
            console.log(testInfoTemp);


            $.ajax({
                url: url,
                type: postType,
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: 118},
                data: JSON.stringify(testInfoTemp),
                success: function (resultData) {
                    if (resultData == null) {
                        warn(strwarn + "失败！");
                    }
                    if (resultData.resultCode == "SUCCESS") {
                        warn(strwarn + "成功！");
                        resetTestModelInputAndHide();
                        cancelCreateOrUpdateTest();
                        //重新加载试卷列表
                        table.ajax.reload();
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
        //删除考试
        function deleteTestInfoById(id) {
            confirm("确定要删除吗？", 'deleteTestInfoByIdConfirmed(' + id + ')');
        }
        function deleteTestInfoByIdConfirmed(id) {
            var testInfoTemp = {
                "testInfo": {
                    "id": id
                }
            };
            console.log(testInfoTemp);
            $.ajax({
                url: "../rest/testInfo/delete",
                type: "delete",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: 118},
                data: JSON.stringify(testInfoTemp),
                success: function (resultData) {
                    if (resultData == null) {
                        warn("删除失败！");
                    }
                    if (resultData.resultCode == "SUCCESS") {
                        warn("删除成功！");
                        //重新加载试卷列表
                        table.ajax.reload();
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
        //编辑考试
        function editTestInfo(id, startTime, endTime) {
            var testInfo = testInfoMap[id];
            var startTime = startTime;
            var endTime = endTime;
            var dEnd = new Date(endTime.replace(/\-/g, "\/"));
            var dStart = new Date(startTime.replace(/\-/g, "\/"));
            var now = new Date();

            //获取考试用户列表信息
            $.ajax({
                url: "../rest/testInfo/getTestInfo",
                type: "post",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: 118},
                data: JSON.stringify({"testInfo": testInfo}),
                success: function (resultData) {
                    if (resultData == null) {
                        warn("获取考试信息失败！");
                        return;
                    }
                    if (resultData.resultCode == "SUCCESS") {
                        if (resultData.data && resultData.data.length > 0) {
                            testInfo.userList = resultData.data[0].userList;
                            $.each(testInfo.userList, function (index, userAdded) {
                                bakSelectedUserIds.push(userAdded.userId + "");
                            });
                        }
                        if (now < dStart) {
                            showEditModal(testInfo);
                        } else if (dStart < now && now < dEnd) {
                            showEditModal(testInfo);
                            $('#parentCategoriesAddSelect').attr("disabled", "disabled");
                            $('#subCategoriesAddSelect').attr("disabled", "disabled");
                            $('#paperSelect').attr("disabled", "disabled");
                            $('#startTime').attr("disabled", "disabled");
                            $('#endTime').attr("disabled", "disabled");
                            /*                $('.input-group-addon').attr("style","display:none");*/
                        } else {
                            warn("考试已结束，不可修改");
                        }

                    } else {
                        warn("获取考试信息失败！");
                    }
                },
                error: function (xhr, r, e) {
                    warn(e);
                    //resetUserModalAndHide();
                }
            });


        }
        //显示考试编辑模态框
        function showEditModal(testInfo) {
            console.log("编辑考试");
            console.log(testInfo);
            initAddOrUpdateModal(testInfo);
        }
        //初始化考试创建和编辑模态框
        function initAddOrUpdateModal(testInfo) {
            $("#testTime").text('');
            if (!testInfo) {
                resetTestModelInput();
                $("#testTitle").html("创建考试");
                currentState = true;
                if (currentNode) {
                    if (!currentNode.pId) {
                        //直接选中父类
                        $('#parentCategoriesAddSelect').val(currentNode.id.toString());
                        $('#parentCategoriesAddSelect').trigger('change');
                    } else {
                        //选中父类后再选子类
                        $('#parentCategoriesAddSelect').val(currentNode.pId.toString());
                        $('#parentCategoriesAddSelect').trigger('change');
                        setTimeout(function () {
                            $('#subCategoriesAddSelect').val(currentNode.id.toString());
                            $('#subCategoriesAddSelect').trigger('change');
                        }, 300);
                    }
                } else {
                    $('#parentCategoriesAddSelect').val('');
                }
            } else {
                resetTestModelInput();
                currentTestId = testInfo.id;
                $("#testTitle").html("编辑考试");
                currentState = false;
                //加载考试信息至编辑项
                //var testId = $('#paperSelect').val();
                var ca = testPaperCategoryMapById[testInfo.testCategoryId];
                if (ca) {
                    if (ca.parentId == 0) {
                        //直接选中父类
                        $('#parentCategoriesAddSelect').val(ca.id.toString());
                        $('#parentCategoriesAddSelect').trigger('change');
                        setTimeout(function () {
                            $('#paperSelect').val(testInfo.testId.toString());
                        }, 300);
                    } else {
                        //选中父类后再选子类
                        $('#parentCategoriesAddSelect').val(ca.parentId.toString());
                        $('#parentCategoriesAddSelect').trigger('change');
                        setTimeout(function () {
                            $('#subCategoriesAddSelect').val(ca.id.toString());
                            $('#subCategoriesAddSelect').trigger('change');
                            setTimeout(function () {
                                $('#paperSelect').val(testInfo.testId.toString());
                            }, 300);
                        }, 300);

                    }
                }
                $('#startTime').val(testInfo.startTime);
                $('#endTime').val(testInfo.endTime);
                //加载考试用户列表至编辑项
                if (testInfo.userList) {
                    $.each(testInfo.userList, function (index, userToAdd) {
                        if (userToAdd && !addedUsers[userToAdd.userId]) {
                            $('#selectedUserList').dataTable().fnAddData(userToAdd);
                            addedUsers[userToAdd.userId] = true;
                        }
                    });
                }
            }
            $('#paper_add_modal').modal(
                    {
                        show: true,
                        keyboard: false,
                        backdrop: 'static'
                    }
            );
        }

        //获取参加考试人数
        function getPeopleNames(testInfoId,testPaperName){

            $.ajax({
                type:"post",
                contentType:"application/json",
                dataType:"json",
                url:"../rest/testInfo/getTestpeopleNames",
                headers:{userId:"118"},
                data:JSON.stringify({
                    testInfo:{
                        id:testInfoId
                    },


                }),
                success:function(resultData){
                    $("#peopleDiv").html("");
                    var dataList = resultData.data;
                    $("#peopleNameModal").modal(
                            {
                                show:true,
                                keyboard:false,
                                backdrop:'static'
                            }
                    );
                    $("#paperName").html(testPaperName);
                    var html ='';
                    $.each(dataList,function(index,dataObj){
                        html+='<span style="margin-left: 20px;display: block">'+dataObj.testpeopleName+'</span>';

                    })
                    $("#peopleDiv").append(html);

                }
            })
        }
    </script>
    <style>
        #peopleNameModal{
            top: 30%;
        }
        th, button, td, span, input, div, select {

            font-family: 微软雅黑 !important;
        }
        .showPeople{
            width: 16px;
            height: 16px;
            border: 0;
            background: url("../images/user.png");

        }
        #update_password_modal {
            top: 30% !important;
            width: 480px
        }

        input[type="password"] {
            height: 34px !important;
            width: 150px !important;
        }

        .modal-backdrop {
            background: #333 !important;
        }
</style>
</head>
<body style="background-color: #f1f4f8">
<div id="mainDiv">
    <div id="headDiv">
        <jsp:include page="menu.jsp"/>
    </div>
    <div id="contentDiv" style="text-align: left;">
        <input id="nodeId" value="" style="display: none"/><!--考试类别id-->
        <input id="testpaperId" value="" style="display: none"><!--试卷id-->
        <div id="itemDiv"></div>
        <div class="container-fluid">
            <div class="leftDiv">
                <div class="categoryDiv">
                    <div class="treeDiv">
                        <span class="fontSpan" style="font-family:微软雅黑">考试类别</span>

                        <div id="treeview" class="treeview"></div>
                    </div>
                </div>
            </div>
            <div class="rightDiv">
                <div class="rightHeader" style=" margin-bottom: 10px;"><span style="margin-left: 15px;font-size: 16px">考试管理</span>
                </div>
                <div class="btn-group" style="width: 50%;position: absolute;top:162px;z-index:1;display: inherit;">
                    <button class="sure" data-backdrop="static"
                            style="border-radius: 4px;font-family:微软雅黑;width: 100px;"
                            onclick="initAddOrUpdateModal(null)"
                            type="button"
                            data-original-title="" title="" aria-describedby="null">
                        <span class="icon-plus icon-white" style="margin-top: 2px"></span>&nbsp创建考试
                    </button>
                </div>
                <div class="table-responsive">
                    <table id="testListTable">
                        <thead>
                        <tr>
                            <%--<th id="checkBoxAllTh" style="text-align:center;"><input type="checkbox"
                                                                                     id="checkBoxAllCheckBox"/></th>--%>
                            <th style="text-align:center;">序号</th>
                            <th style="text-align:center;">试卷名称</th>
                            <th style="text-align:center;">考试类别</th>
                            <th style="text-align:center;">考试时间</th>
                            <th style="text-align:center;">考生人数</th>
                            <th style="text-align:center;">参考人数</th>
                            <th style="text-align:center;">考试状态</th>
                            <th style="text-align:center;">操作</th>
                        </tr>
                        <tbody>
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    </div>

    <!-- 创建考试的模态框 -->
    <div class="modal fade hide" id="paper_add_modal"><!-- fade是设置淡入淡出效果 -->
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                    <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true"
                            onclick="cancelCreateOrUpdateTest()">
                    </button>
                    <span id="testTitle">创建考试</span>
                </div>
            </div>
            <div class="clearfix"></div>
            <div class="modal-body">
                <div style="margin-bottom:15px;">
                    <span>试卷类别：</span>

                    <div style="display:inline-block;vertical-align:middle;">
                        <select name="companySelect" id="parentCategoriesAddSelect" class="form-control"
                                style="width: 230px;float: left">
                        </select>
                        <select name="companySelect" id="subCategoriesAddSelect" class="form-control"
                                style="width: 230px;float: left;margin-left: 10px;">
                        </select>
                    </div>
                </div>
                <div style="margin-bottom:15px;">
                    <span>选择试卷：</span>

                    <div style="display:inline-block;vertical-align:middle;">
                        <select class="form-control" id="paperSelect" onchange="getTestTime()"
                                style="width:230px;display:inline-block;">
                        </select>
                        <span id="testTime" style="color: #0f0f0f;"></span>
                    </div>
                </div>
                <div style="margin-bottom:15px;">
                    <span>考试时间：</span>

                    <div style="display:inline-block;vertical-align:middle;">
                        <div class="input-group date form_datetime">
                            <input type="text" id="startTime"
                                   style="cursor: pointer;height: 34px;width: 100%;" class="inputTime"
                                    >
                    <span class="input-group-addon" style="display: inline-block;float: right;margin-top: -41px;
                          margin-right: 10px;">
                        <span class="glyphicon glyphicon-th"><i class='icon-th'></i></span>
                    </span>
                        </div>
                    </div>
                    <span>&nbsp;至&nbsp;</span>

                    <div style="display:inline-block;vertical-align:middle;">
                        <div class="input-group date form_datetime">
                            <input type="text" id="endTime"
                                   style="cursor: pointer;height: 34px;width: 100%;" class="inputTime"
                                    >
                    <span class="input-group-addon" style="display: inline-block;float: right;margin-top: -41px;
                          margin-right: 10px;">
                        <span class="glyphicon glyphicon-th"><i class='icon-th'></i></span>
                    </span>
                        </div>
                    </div>
                </div>
                <div style="margin-bottom:15px;">
                    <button class="sure" data-toggle="modal" data-target="#students_select_modal"
                            data-backdrop="static" onclick="initAllUserList()" style="border-radius: 4px;"> 邀请考生
                    </button>
                    <button class="sure" onclick="removeUsers()" style="border-radius: 4px;">取消邀请
                    </button>
                </div>
                <div class="table-responsive">
                    <table id="selectedUserList" class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>
                            <th style="text-align:center;" id="thSelectedUsers"><input type="checkbox"
                                                                                       id="checkBoxAllSelectedUsers"/>
                            </th>
                            <th style="text-align:center;">用户名</th>
                            <th style="text-align:center;">真实姓名</th>
                            <th style="text-align:center;">性别</th>
                            <th style="text-align:center;">身份证号</th>
                            <th style="text-align:center;">所属单位</th>
                            <th style="text-align:center;">所属部门</th>
                        </tr>
                        </thead>

                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
            <!--警告框-->
            <div class="alert alert-block" style="color: #ff6c60;display: none;" id="testWarn"></div>
            <div class="modal-footer">
                <button class="cancel" data-dismiss="modal" onclick="cancelCreateOrUpdateTest()">取消</button>
                <button class="sure" data-toggle="modal" data-backdrop="static" id="testBtn"
                        onclick="createOrUpdateTest()">确定
                </button>
            </div>
        </div>
    </div>
</div>
<!-- 选择考生的模态框（二级弹框） -->
<div class="modal fade hide" id="students_select_modal"><!-- fade是设置淡入淡出效果 -->
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true">
                </button>
                邀请考生
            </div>
            <div class="clearfix"></div>
            <div class="modal-body">
                <div class="row">
                    <div class="leftDiv" style="width: 30%;padding-right: 0px;">
                        <div class="panel panel-default"
                             style="padding-top: 10px;;background-color: #ffffff;height: 100%;">
                            <div class="panel-heading">
                                <span class="fontSpan" style="margin-left: 10px;">单位部门</span>
                            </div>
                            <div id="collapseOne1" style="width: 100%;">
                                <div class="panel-body" style=" border-top: 0px;padding: 0px;border-radius: 4px;">
                                    <div class="list-group">
                                        <label for="treeview"></label>

                                        <div id="treeview1"
                                             style="overflow-y: auto;width: 100%; margin: 0 auto;border: none;"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="rightDiv" style="width: 70%;padding-right: 5px;">
                        <div class="table-responsive">
                            <table width="100%" id="userListTable"
                                   class="table table-striped table-bordered table-hover">
                                <thead>
                                <tr>
                                    <th style="text-align:center;" id="checkBoxAllThUser"><input
                                            type="checkbox"
                                            id="checkBoxAllCheckBoxUser"/>
                                    </th>
                                    <th style="text-align:center;">用户名</th>
                                    <th style="text-align:center;">真实姓名</th>
                                    <th style="text-align:center;">性别</th>
                                    <th style="text-align:center;">身份证号</th>
                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>

                    </div>
                </div>

            </div>
            <div class="modal-footer">
                <button class="cancel" data-dismiss="modal">取消</button>
                <button id="addUser" class="sure">确定</button>
            </div>
        </div>
    </div>
</div>
<!-- 弹出框模态框 -->
<div class="modal fade dialog hide" id="warnModal" style="top:250px;"><!-- fade是设置淡入淡出效果 -->
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true">
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

<div class="modal fade" id="peopleNameModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="border-radius: 4px">
    <div class="modal-dialog">
        <div class="modal-content" >
            <div class="modal-header" style="background: #333;height: 32px;line-height: 32px">
                <button type="button" id="close" class="closeTable" style="margin-top: -9px;" data-dismiss="modal" aria-hidden="true">

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
                <button type="button" class="btn_cancel" style="width:70px;height:30px;border-radius: 4px;border: 0;"data-dismiss="modal">关闭</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
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
