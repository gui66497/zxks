<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
<head>
    <link href="../images/favicon.ico" rel="icon" type="image/x-icon"/>
    <meta charset="UTF-8">
    <title>在线考试系统--考生管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="../exts/datatables/css/jquery.dataTables.css">
    <link rel="stylesheet" type="text/css" href="../css/chartsManage.css">
    <link rel="stylesheet" type="text/css" href="../css/itemBankManage.css">
    <link href="../exts/bootstrap/css/bootstrap-modal.css" rel="stylesheet" media="screen" />
    <script type="text/javascript" charset="utf8" src="../exts/jquery-1.12.3.min.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/datatables/js/jquery.dataTables.js"></script>
    <script src="../exts/bootstrap/js/bootstrap-modal.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/modalTemplate.js"></script>
    <script type="application/javascript" src="../exts/util.js"></script>
    <script src="../exts/bootstrap/js/bootstrap-modalmanager.js"></script>

    <!-- 引入 echarts.js -->
    <script src="../exts/echarts.min.js"></script>


    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->

    <script type="text/javascript">
        var userId = <%=request.getSession().getAttribute("userId")%>;//用户ID
        function preBarFontCount( title,datas,fontSize,barContainerWidth,xWidth,X2Width,insertContent){
            if(!fontSize){
                fontSize = 12;
            }
            if(isNaN(xWidth)){
                xWidth = 80;
            }
            if(isNaN(X2Width)){
                X2Width = 80;
            }
            if(!insertContent){
                insertContent = "\n"
            }
            var xAxisWidth = parseInt(barContainerWidth) - (parseInt(xWidth) + parseInt(X2Width));
            var barCount = datas;
            var preBarWidth = Math.floor(xAxisWidth/barCount);
            var preBarFontCount = Math.floor(preBarWidth/fontSize)
            if(preBarFontCount > 3 ){
                preBarFontCount -= 2;
            }else if(preBarFontCount <=3 && preBarFontCoun >=2){
                preBarFontCount -=1;
            }
            var newTitle = "";
            var titleSuf = "";
            var rowCount = Math.ceil(title.length/preBarFontCount)
            if(rowCount > 1){
                for(var j = 1;j <= rowCount;j++){
                    if(j ==1) {
                        newTitle += title.substring(0,preBarFontCount) + insertContent;
                        titleSuf = title.substring(preBarFontCount);
                    }else{
                        var startIndex = 0;
                        var endIndex = preBarFontCount;
                        if(titleSuf.length > preBarFontCount){
                            newTitle += titleSuf.substring(startIndex,endIndex) + insertContent
                            titleSuf = titleSuf.substring(endIndex)
                        }else if( titleSuf.length > 0){
                            newTitle += titleSuf.substring(startIndex);
                        }
                    }
                }
            }else {
                newTitle = title;
            }
            return newTitle;
        }
        $(function () {
            getChartsInfo();
        })
        //取得统计数据
        var xdata1 = new Array();
        var xdataChange = new Array();
        var ydataAvg = new Array();
        var ydataPerson = new Array();
        function getChartsInfo() {
            var arr = [];
            var paperId = $("#paperSelect").val();
            var parentCategoriesAddSelect = $("#parentCategoriesAddSelect").val();
            var subCategoriesAddSelect = $("#subCategoriesAddSelect").val();
            var companyVal= $("#companyUserAddSelect").val();
            if(paperId == null && (parentCategoriesAddSelect !=null || subCategoriesAddSelect!= null || companyVal != null)){
                showOkModal('提示框',"试卷不能为空");
                return false;
            }
            var companyId = $("#companyUserAddSelect").val();
            $.ajax({
                url: "/zxks/rest/charts/getChart",
                type: "POST",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: userId},
                data: JSON.stringify({"companyId": companyId, "paperId": paperId}),
                success: function (resultData) {
                    arr = resultData.data;
                    $.each(arr, function (index, category) {
                        var xdata = category.split("▓")
                        /*                        alert(xdata[0])
                         alert(xdata[1])
                         alert(xdata[2])*/
                        xdataChange.push(xdata[0])
                        ydataAvg.push(xdata[1])
                        ydataPerson.push(xdata[2])

                    });
                    if(xdata1.length == 0){
                        for(var index = 0;index<xdataChange.length;index++){
                            xdata1.push(preBarFontCount(xdataChange[index],xdataChange.length,12,1407,150,100,"\n"))
                        }
                        xdataChange = new Array();
                    }
                    var myChart = echarts.init(document.getElementById('main'));
                    // 指定图表的配置项和数据
                    var option = {
                        title: {
                            text: '参考人次/平均分统计',
                            size:'16px',
                            left:'50px'
                        },
                        tooltip: {
                            trigger: 'axis'
                        },
                        legend: {
                            data: ['参考人次', '平均分']
                        },
                        toolbox: {
                            show: true,
                            feature: {
                                mark: {show: true},
                                //dataView: {show: true, readonly: false},
                                magicType: {show: true},
                             //   magicType: {show: true, type: ['line', 'bar']},
                                restore: {show: true},
                                saveAsImage: {show: true}
                            }
                        },
                        calculable: true,
                        grid: {y: 50, y2: 100,x:150,x2: 100},
                        xAxis: { axisLabel:{interval:0,rotate:0,margin:5}},
                        // xAxis: {},
                        yAxis: [{
                            type :'value',

                            splitNumber:10
                        }],
                        series: [
                            {
                                name: '参考人次',
                                type: 'bar',
                                //系列中的数据标注内容
                                markPoint:{
                                    data:[
                                        {type:'max',name:'最大人数'},
                                        /*                                      {type:'min',name:'最小人数'}*/
                                    ]
                                },
                                //系列中的数据标线内容
                                /*                                markLine:{
                                 data:[
                                 {type:'avgPerson',name:'平均人数'}
                                 ]
                                 },*/
                            },
                            {
                                name: '平均分',
                                type: 'bar',
                                //系列中的数据标注内容
                                markPoint:{
                                    data:[
                                        {type:'max',name:'最高分'},
                                        /*                                        {type:'min',name:'最低分'}*/
                                    ]
                                },
                                //系列中的数据标线内容
                                /*                               markLine:{
                                 data:[
                                 {type:'avgCount',name:'平均分数'}
                                 ]
                                 },*/
                            }
                        ]
                    };
                    option.xAxis.data = xdata1
                    option.series[0].data = ydataAvg;
                    option.series[1].data = ydataPerson;
                    myChart.setOption(option);
                    xdata1 = new Array();
                    ydataAvg = new Array();
                    ydataPerson = new Array();

                },
                error: function (xhr, r, e) {
                    showOkModal('提示框', e);
                }
            });
        }
        //弹出消息提醒框
        function showOkModal(label, contennt) {
            $("#okModalLabel")[0].innerHTML = label;
            $("#okModalMessage")[0].innerHTML = contennt;
            $("#okModal").modal({
                show : true
            });
        }
        // 使用刚指定的配置项和数据显示图表。
    </script>
    <%--    <!--加载echart图所需要的option-->
        <script type="text/javascript">
            $(function(){
                alert(11);
                getCharts();
            })
            function getCharts(){
                alert(38);

                var myChart = echarts.init(document.getElementById(mainBar));
                alert(42);
                var options = {
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    legend: {
                        data: ['平均分', '参考人数']
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data: []
                        }
                    ],
                    yAxis: [{
                        type: 'value',
                    }],
                    series: [
                        {
                            name: '平均分',
                            type: 'bar',
                            data: [78, 67, 89]
                        },
                        {
                            name: '参考人数',
                            type: 'bar',
                            data: [178, 167, 189]
                        }
                    ]
                };
                myChart.setOption(options);
            }
        </script>--%>
    <!--加载日期控件-->
    <%--    <script type="text/javascript">
            $(function () {
                var date = new Date();
                date.getDate();
                $(".form_datetime").datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd',
                    minView: 2,
                    autoclose: true,
                    endDate: date
                });

                $(".form_date").datetimepicker({
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd',
                    minView: 2,
                    autoclose: true,
                    endDate: date
                });

            })
        </script>--%>
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
            /* initCheckBox();*/
            loadCompaniesAndDepts();
        });

        /*        $('a').tooltip({
         delay: {
         show: 500,
         hide: 100,
         },
         //placement:left,
         });*/

        /*$('button').tooltip({
            delay: {
                show: 500,
                hide: 100,
            },
            container: 'body'
        });*/
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
                    var optionHtml = "<option value=''></option>";
                    $.each(companies, function (index, company) {
                        if (!companiesMapById[company.companyId]) {
                            companiesMapById[company.companyId] = company;
                        }
                        company.company = JSON.parse(JSON.stringify(company));
                        //添加下拉框
                        optionHtml += "<option value='" + company.companyId + "'>" + company.companyName + "</option>";
                    });
                    $('#companyUserAddSelect').text('');
                    $('#companyUserAddSelect').append(optionHtml);
                },
                error: function (xhr, r, e) {
                    alert(e);
                }
            });
        }
    </script>
    <!--考试类别、考卷列表-->
    <script type="text/javascript">
        var currentNode = null; //当前选中节点
        var testPaperCategoryMapById = {};
        var topCategories = [];
        var subCategories = [];
        var dataTableJson = {
            "language": {
                "sSearchPlaceholder": "请输入试卷名称"
            },
            "bServerSide": true,                    //指定从服务器端获取数据
            "bProcessing": true,                    //显示正在处理进度条
            "sAjaxSource": "rest/testInfo/filterTestInfoList",    //服务端Rest接口访问路径
            "columns": [
                {
                    "data": "operate",
                    'sClass': 'center',
                    "mRender": function (data, type, full) {
                        return '';
                    }
                },
                {"data": "testpaperName", 'sClass': 'center'},
                {"data": "testCategory", 'sClass': 'center'},
                {"data": "startTime", 'sClass': 'center'},
                {"data": "totalUserNo", 'sClass': 'center'},
                {
                    "data": "operate", 'sClass': 'center',
                    "mRender": function (data, type, full) {
                        //alert(aData.itemId);
                        return '';
                    }
                },
                {
                    "data": "operate", 'sClass': 'center',
                    "mRender": function (data, type, full) {
                        //alert(aData.itemId);
                        return '';
                    }
                }],
            "aoColumnDefs": [                                       //指定列附加属性
                {"bSortable": false, "aTargets": [0, 4, 5, 6]},        //这句话意思是第1,3（从0开始算）不能排序
                {"bSearchable": false, "aTargets": [0, 1, 4, 5]},   //bSearchable 这个属性表示是否可以全局搜索，其实在服务器端分页中是没用的
            ],
            "aaSorting": [[3, "desc"]], //默认排序
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {     // 当创建了行，但还未绘制到屏幕上的时候调用，通常用于改变行的class风格
                // $('td:eq(0)', nRow).append("<input class='test' value='" + aData.testId + "' type='checkbox'/>");
                testInfoMap[aData.id] = aData;
                $('td:eq(0)', nRow).append(nRow._DT_RowIndex + 1);
                $('td:eq(3)', nRow).html(aData.startTime + "<br/>至<br/>" + aData.endTime);
                if (aData.endTime && aData.startTime) {
                    var statusSapn = "<span class='testStatus' testId='" + aData.id + "' endTime='" + aData.endTime + "' startTime='" + aData.startTime + "'></span>";
                }
                $('td:eq(5)', nRow).append(statusSapn);
                var options = "";
                options += "<a editId='" + aData.id + "' data-original-title='编辑' style='text-decoration:none;display: none;' href='javascript:void(0);'" +
                        "onclick='editTestInfo(" + aData.id + ");' data-placement='auto top' data-trigger='hover focus'>" +
                        "<i class='glyphicon glyphicon-pencil'></i>" +
                        "</a>&nbsp;&nbsp;";
                options += "<a delId='" + aData.id + "' data-original-title='删除' style='text-decoration:none;display: none;' href='javascript:void(0);'" +
                        "onclick='deleteTestInfoById(" + aData.id + ");' data-placement='auto top' data-trigger='hover focus'>" +
                        "<i class='glyphicon glyphicon-trash'></i>" +
                        "</a>&nbsp;&nbsp;";
                $('td:eq(6)', nRow).append(options);
                return nRow;
            },
            "fnServerData": function (sSource, aoData, fnCallback) {
                var searchName = $('#searchValue').val().trim();
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
                    $('#' + strPaperListDomId).empty();
                    if (resultData && resultData.data) {
                        var optionHtml = "";
                        $.each(resultData.data, function (index, testPaper) {
                            //添加下拉框
                            optionHtml += "<option value='" + testPaper.id + "'>" + testPaper.testpaperName + "</option>";
                        });

                        $('#' + strPaperListDomId).append(optionHtml);
                    }
                },
                error: function (xhr, r, e) {
                    alert(e);
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
                        if (!testPaperCategoryMapById[category.id]) {
                            testPaperCategoryMapById[category.id] = category;
                        }
                        if (category.parentId == 0) {
                            topCategories.push(category);
                        } else {
                            subCategories.push(category);
                        }
                    });
                    var optionHtml = "";
                    $.each(topCategories, function (index, category) {
                        //添加下拉框
                        optionHtml += "<option value='" + category.id + "'>" + category.categoryName + "</option>";
                        category.text = filterStringReturnSpan(category.categoryName, 110);
                        category.href = "#";
                        $.each(subCategories, function (index2, subcategory) {
                            if (subcategory.parentId == category.id) {
                                if (!category.nodes) {
                                    category.nodes = []
                                }

                                var subNode = {
                                    id: subcategory.id,
                                    categoryName: subcategory.categoryName,
                                    text: filterStringReturnSpan(subcategory.categoryName, 82),
                                    href: "#"
                                };
                                category.nodes.push(subNode);
                                addNodes(subNode);
                            }

                        });
                    });
                    initSelectCategories("parentCategoriesAddSelect", "subCategoriesAddSelect", optionHtml);

                    function addNodes(targetCategory) {
                        $.each(subCategories, function (index2, subcategory) {
                            if (subcategory.parentId == targetCategory.id) {
                                if (!targetCategory.nodes) {
                                    targetCategory.nodes = []
                                }
                                var subNode = {
                                    id: subcategory.id,
                                    categoryName: subcategory.categoryName,
                                    text: filterStringReturnSpan(subcategory.categoryName, 82),
                                    href: "#"
                                };
                                targetCategory.nodes.push(subNode);
                                addNodes(subNode);
                            }

                        });
                    }

                    var options = {
                        levels: 2,
                        data: topCategories
                    };
                    console.log(options);
                },
                error: function (xhr, r, e) {
                    alert(e);
                }
            });
        }
        function getSubInfo(){
            setTimeout(function(){
                if($("#subCategoriesAddSelect").val()!=null){
                    $("#subCategoriesAddSelect").attr("style","display:''");
                }
            },100)
        }
        $(function () {
            if($("#subCategoriesAddSelect").val()==null){
                $("#subCategoriesAddSelect").attr("style","display:none");
            }
            buildDomTree();
        });
    </script>
    <style>
        th, button, td, span, input, div, select {

            font-family: 微软雅黑 !important;
        }
        #update_password_modal{
            top: 30% !important;
            z-index: 2000 !important;
            width:480px
        }
        input[type="password"]{
            height: 34px !important;
            width: 150px !important;
        }
        .closeTable {
            background: url("../images/close.png");
            float: right;
            width: 50px;
            height: 50px;
            margin-top: 0px;
            margin-right: -16px;
            opacity: 1;
            border: 0;
        }
        .closeTable:hover {
            background: url("../images/close_hover.png");
        }
        #okModal{
            width: 20%;
        }
        .modal-header{
            height: 50px;
            line-height: 50px;
            background-color: #1c2b36;
            border-top-right-radius: 4px;
            border-top-left-radius: 4px;
            color: #ffffff;
            font-size: 16px;
        }
    </style>

</head>
<body style="background: #f1f4f8">
<%--<%@include file="menu.jsp" %>--%>
<div id="mainDiv" >
    <div id="headDiv">
        <jsp:include page="menu.jsp" />
    </div>
    <div id="contentDiv" style="height: 85%; padding: 0 20px 20px 20px; margin:0 10px 10px 10px ;" >
        <%@include file="mymodal.jsp"%>
        <div class="container-fluid" >
            <div class="col-lg-1">
            </div>
            <div class="col-lg-10"style="font-family:微软雅黑">
                <div style="background: #fff;padding: 20px 20px 10px 20px">

                    <ol class="breadcrumb" style="margin: 0;padding: 0">
                        <li class="myMarkLabel" style="margin:7px 5px 7px 5px;text-shadow: none;font-size: 16px">统计管理</li>
                    </ol>
                    <div class="row">
                        <div class="modal-body" style="text-align: left;margin-left: 20px;">
                            <div style="margin-bottom:10px;font-family:微软雅黑">
                                <span>&nbsp;&nbsp;试卷类别：</span>

                                <div style="display:inline-block;vertical-align:middle;">
                                    <select name="companySelect" id="parentCategoriesAddSelect" class="form-control" onchange="getSubInfo()"
                                            style="width: 230px;float: left;font-family:微软雅黑">
                                    </select>
                                    <select name="companySelect" id="subCategoriesAddSelect" class="form-control"
                                            style="width: 230px;float: left;margin-left: 10px !important;font-family:微软雅黑">
                                    </select>
                                </div>
                            </div>
                            <div style="margin-bottom:10px;font-family:微软雅黑">
                                <span style="color:red;">*</span><span>选择试卷：</span>

                                <div style="display:inline-block;vertical-align:middle;">
                                    <select class="form-control" id="paperSelect" style="width:230px;display:inline-block;font-family:微软雅黑">
                                    </select>
                                </div>
                            </div>
                            <div style="margin-bottom:10px;font-family:微软雅黑">
                                <span style="position: absolute;margin-top: 8px">&nbsp;单&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;位：</span>

                                <div style="display:inline-block;vertical-align:middle;">
                                    <select name="companySelect" id="companyUserAddSelect"
                                            style="width:230px;display:inline-block;float: left;font-family:微软雅黑;margin-left: 80px" class="form-control"
                                            placeholder="请选择单位">
                                    </select>
                                    <div style="margin: 45px 0 0 63px;">
                                        <button type="button" class="sure" data-toggle="modal" onclick="getChartsInfo()"
                                                data-backdrop="static"   style="width: 82px;margin-left: 10px;border-radius: 4px;font-family:微软雅黑">
                                            检&nbsp;&nbsp;&nbsp;&nbsp;索
                                        </button>
                                        <button type="button" class="sure" data-toggle="modal" onclick="location.reload()"
                                                data-backdrop="static"   style="width: 82px;margin-left: 5px;border-radius: 4px;font-family:微软雅黑">
                                            重&nbsp;&nbsp;&nbsp;&nbsp;置
                                        </button>

                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="chartDiv" style="background: #fff">
                    <hr>
                    <div id="main" style="width: 90%;height:60%;padding: 15px 15px 15px 15px"></div>
                </div>

            </div>
        </div>
    </div>
</div>

</body>
</html>