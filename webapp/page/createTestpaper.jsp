<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link href="../images/favicon.ico" rel="icon" type="image/x-icon"/>
    <meta charset="UTF-8">
    <title>在线考试系统--创建试卷</title>
    <link rel="stylesheet" type="text/css" href="../css/bootstrap-treeview.css">
    <link rel="stylesheet" type="text/css" href="../exts/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="../exts/bootstrap/css/bootstrap-modal.css">
    <!-- DataTables CSS -->
    <link rel="stylesheet" type="text/css" href="../exts/datatables/css/jquery.dataTables.css">
    <link rel="stylesheet" type="text/css" href="../css/square/blue.css">
    <link rel="stylesheet" type="text/css" href="../css/testPaper/createTestpaper.css">
    <link rel="stylesheet" type="text/css" href="../css/itemBankManage.css">

    <script type="application/javascript" src="../exts/jquery-1.12.3.min.js"></script>
    <script type="application/javascript" src="../exts/bootstrap-treeview.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/bootstrap/js/bootstrap-modal.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/bootstrap/js/bootstrap-modalmanager.js"></script>
    <script type="application/javascript" src="../exts/icheck.min.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/modalTemplate.js"></script>
    <script type="application/javascript" src="../exts/util.js"></script>
    <!-- DataTables JS-->
    <script type="text/javascript" charset="utf8" src="../exts/datatables/js/jquery.dataTables.js"></script>
    <script type="application/javascript" src="../js/testPaper/testPaperPublic.js"></script>
    <script type="application/javascript" src="../js/testPaper/createTestpaper.js"></script>

    <style type="text/css">
        input[placeholder]{
            font-family: 微软雅黑;
        }
        textarea[placeholder]{
            font-family: 微软雅黑;
        }
        .node-treeview ::selection{
            background-color: #28d6d7;
        }
        table.dataTable tbody td{
            text-align: center;
        }
        #itemSelectModal .modal-body {
            max-height: 800px;
        }
        #itemSelectModal {
            max-height: 800px;
            height:800px;
            width:68%;
            margin-left:-33%;
            top: 47%
        }
        th,button,td,span,input,div,ul{

            font-family:微软雅黑 !important;
        }
        .btn-cancel{
            border: 0 !important;
            border-radius: 4px !important;
        }
        #update_password_modal{
            top: 30% !important;
            width:480px;
            z-index: 3000;
        }
        input[type="password"]{
            height: 32px !important;
            width: 150px !important;
        }
        .modal-backdrop{
            background: #333 !important;
        }
        .btn-success{
            text-shadow: none !important;
            border: 0;
            box-shadow: none;
        }
    </style>
</head>
<body data-spy="scroll" data-target="#myScrollspy" style="background: #f1f4f8">

<div id="mainDiv" >
    <div id="headDiv">
        <jsp:include page="menu.jsp"/>
    </div>
    <div id="contentDiv">
        <div id="itemDiv"></div>
        <%@include file="mymodal.jsp"%>
        <div class="container" style="width: 80%">
            <div style="margin-top:9px;position:relative">
                <ul class="breadcrumb" style="font-family:微软雅黑;font-size: 15px">
                    <li><a href="javascript:void(0)" onclick="$.testPaperPublic.breadHref('testPaperManage.jsp')">试卷管理</a></li>
                    <li class="active" style="margin: 0 20px 0 20px"> /</li>
                    <li class="active" style="font-size: 14px"> 创建试卷</li>
                </ul>
                <!--<a href="#" onclick="showItemSelectModal()" class="btn btn-primary" style="position:absolute;top:0px;right:0px;"><i class="glyphicon glyphicon-arrow-left"></i> 开始选题</a>-->
            </div>
            <div class="row">
                <div class="span3" id="myScrollspy" style="width: 20%">
                    <!-- 附加导航区域 -->
                    <ul id="myAffix" class="nav nav-pills nav-stacked" style="position:fixed;text-align:center;font-family:微软雅黑">

                        <li class="active" onclick="getSection('b')" name="myAffixLi" id="myAffixLib"><a href="javascript:void(0)" name="totu">基本信息</a></li>
                        <li onclick="getSection('0')" name="myAffixLi" id="myAffixLi0"><a href="javascript:void(0)" name="totu">第一部分</a></li>
                        <li onclick="getSection('1')" name="myAffixLi" id="myAffixLi1"><a href="javascript:void(0)" name="totu">第二部分</a></li>
                        <li onclick="getSection('2')" name="myAffixLi" id="myAffixLi2"><a href="javascript:void(0)" name="totu">第三部分</a></li>
                        <%--<li onclick="getSection('2')" name="myAffixLi" id="myAffixLi2"><a href="javascript:void(0)" name="totu">第四部分</a></li>--%>
                        <%--<li><a href="#" onclick="appendSection()"><i class="glyphicon glyphicon-plus"></i> 继续添加部分</a></li>--%>

                        <%--<div style="margin:20px 0;">
                            <label for="1" class="col-sm-6">
                                <input type="checkbox" id="itemSequenceInput" name="" value="题目乱序"><span style="color:777;font-weight:normal;vertical-align:middle;">&nbsp;&nbsp;题目乱序</span>
                            </label>
                            <label for="2" class="col-sm-6">
                                <input type="checkbox" id="answerSequenceInput" name="" value="选项乱序"><span style="color:777;font-weight:normal;vertical-align:middle;">&nbsp;&nbsp;选项乱序</span>
                            </label>
                        </div>--%>

                        <div class="row" style="margin:20px 0;">
                            <div style="">
                                <div class="span4" style="text-align:left;">
                                    <p>
                                        <span>总题数： </span>
                                        <span id="finalItemCount" style="color:orange"></span>
                                        <span>道</span>
                                    </p>
                                </div>
                                <div class="span4" style="text-align:left;">
                                    <p>
                                        <span>总分值： </span>
                                        <span id="finalScore" style="color:orange"></span>
                                        <span>分</span>
                                    </p>
                                </div>
                                <div class="span4" style="text-align:left;">
                                    <p>
                                        <span>考试时长： </span>
                                        <input id="totalTimeInput" type="text" onkeypress="return (/[1,2,3,4,5,6,7,8,9,0]/.test(String.fromCharCode(event.keyCode)))" class="controls" style="width:50px;display:inline-block;ime-mode: Disabled" required>
                                        <span>分钟</span>
                                    </p>
                                </div>
                            </div>
                        </div>

                        <div style="margin:20px 0;">
                            <a href="#" style="text-decoration:none;">
                                <button type="button" class="btn btn-warning btn-block" onclick="toSavePaper()" style="font-family:微软雅黑;height:30px;border: 0;text-shadow: none;box-shadow: none">保存试卷</button>
                            </a>
                        </div>
                    </ul>
                </div>

                <div class="span9" style="width: 70%">
                    <div  style="padding-left: 8%">
                        <div class="row">
                            <!-- 基础部分 -->
                            <div class="span11 right_details" name="partPaper" id="section-b">
                                <form class="form-horizontal" style="width:95%;margin:0 auto;padding-top:15px;">
                                    <label name="partNameNo" style="font-size:16px;color:#777;padding: 5px 0 0 6px;font-family:微软雅黑">基本信息</label>
                                    </br>
                                    <div class="control-group">
                                        <input type="text" style="display: none">
                                        <input type="text" class="form-control dash_input" style="width: 384px" id="testpaperName" maxlength="50" placeholder="试卷名称(必填)" required>
                                    </div>

                                    <div class="control-group">
                                        <select name="testPaperCategorySelect" id="testPaperCategorySelect" style="width: 400px;font-family:微软雅黑" class="form-control" placeholder="请选择分类">
                                        </select>
                                    </div>

                                    <div class="control-group">
                                        <textarea class="dash_input" style="width: 95%;max-width: 95%" rows="2" id="testpaperDescription" placeholder="试卷描述，该信息考生不可见(选填)" maxlength="200"></textarea>
                                    </div>
                                    <div class="control-group">
                                        <textarea class="dash_input" style="width: 95%;max-width: 95%" rows="2" id="testpaperExplain" placeholder="试卷说明，该信息考生可见(选填)" maxlength="200"></textarea>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <div class="row">
                            <!-- 第一部分 -->
                            <div class="span11 right_details" style="margin-top:20px;" id="section-0" name="partPaper">
                                <form class="form-horizontal" style="width:95%;margin:0 auto;padding-top:15px;">

                                    <div class="control-group">
                                        <div class="row">
                                            <label class="span1" name="partNameNo" style="white-space: nowrap;font-size:16px;color:#777;padding: 5px 0 0 6px;font-family:微软雅黑">第一部分</label>

                                            <div class="span3" style="padding-left:20px;width: 200px">
                                                <select name="testpaperModeSelect" style="width: 150px;font-family:微软雅黑" class="form-control" placeholder="请选择分类">
                                                    <option VALUE=1 SELECTED>单选题</option>
                                                    <option VALUE=2>多选题</option>
                                                    <option VALUE=3>判断题</option>
                                                    <%--<option VALUE=4>简答题</option>--%>
                                                    <option VALUE=5>填空题</option>
                                                </select>
                                            </div>
                                            <div class="span2" style="width: 120px">
                                            </div>
                                            <!--<div class="btn-group" style="margin-left:0px;">
                                                <button class="btn btn-success" data-target="#itemSelectModal"><i class="glyphicon glyphicon-ok"></i> 选择题目</button>
                                            </div>
                                            <div class="btn-group" style="margin-left:0px;">
                                                <button class="btn btn-danger">删除该部分</button>
                                            </div>-->
                                            <div class="span1" style="padding-right:20px">
                                                <button type="button" name="selectItemBtn" class="btn btn-success" style="width: 100px;border: 0;font-family:微软雅黑">
                                                    <span class="icon-plus icon-white" style="margin-top: 2px"></span>&nbsp智能添加</button>
                                            </div>
                                            <div class="span1" style="padding-right:20px">
                                                <button type="button" name="itemPersonalBtn" class="btn btn-success " style="width: 100px;border: 0;font-family:微软雅黑;margin-left: 4px">
                                                    <span class="icon-plus icon-white" style="margin-top: 2px"></span>&nbsp手工添加</button>
                                            </div>
                                            <div class="span1" style="margin-left:28px;display: none">
                                                <button type="button" class="btn btn-danger" name="delPartBtn" style="width: 120px;border: 0;font-family:微软雅黑">
                                                    <span class="icon-remove icon-white" style="margin-top: 3px"></span>&nbsp删除该部分</button>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="control-group">
                                        <div class="row">
                                            <textarea id="partExplain0" name="partExplainArea" style="width: 95%;max-width:95%;margin-left: 2%" class="form-control span11 dash_input" rows="2" placeholder="部分答题说明，该信息考生可见(选填，无题目将不会保存此说明)" maxlength="400"></textarea>
                                        </div>
                                    </div>
                                    <div class="control-g roup" style="margin-bottom: 20px">
                                        <blockquote style="border-left:5px solid #337AB7;display:inline;font-family:微软雅黑">题目详情</blockquote>
                                        <div class="btn-group" style="margin-left:60px;">
                                            <button type="button" name="batchDelItem" style="font-family:微软雅黑;">
                                                <span class="icon-remove icon-white" style="margin-top: 3px"></span> 批量删除
                                            </button>
                                        </div>
                                        <div class="btn-group" style="margin-left:0px;">
                                            <button type="button" name="continueAdd" class="btn btn-default btn-md hide" style="font-family:微软雅黑">
                                                <span class="icon-plus icon-white" style="margin-top: 2px"></span> 继续智能添加
                                            </button>
                                        </div>
                                        <div class="btn-group" style="margin-left:0px;">
                                            <button type="button" name="continuePersonalAdd" class="btn btn-default btn-md hide" style="font-family:微软雅黑">
                                                <span class="icon-plus icon-white" style="margin-top: 2px"></span> 继续手工添加
                                            </button>
                                        </div>
                                        <%--<div class="btn-group">
                                            <button type="button" class="btn btn-default btn-md">
                                                <span class="glyphicon glyphicon-arrow-up"></span> 上移
                                            </button>
                                            <button type="button" class="btn btn-default btn-md">
                                                <span class="glyphicon glyphicon-arrow-down"></span> 下移
                                            </button>
                                        </div>--%>
                                        <%--<p style="display:inline;margin-left:60px;">
                                            总题数：<span style="color:orange">8</span><span style="color:gray">道</span>&nbsp;&nbsp;
                                            总分数：<span style="color:orange">16</span><span style="color:gray">分</span>
                                        </p>--%>
                                        <!--<div class="col-sm-2" style="padding-left:18px;float:right">
                                            <button class="btn btn-success"><i class="glyphicon glyphicon-ok"></i> 选择题目</button>
                                        </div>-->
                                    </div>
                                    <!-- 题目表格区域 1-->
                                    <div class="control-group">
                                        <div class="table-responsive">
                                            <table id="tb0" name="partTable" class="table table-striped table-bordered table-hover" style="font-family:微软雅黑">
                                                <thead style="font-family:微软雅黑" >
                                                <tr>
                                                    <th style="text-align:center;"><input type="checkbox" id="checkBoxAllCheckBox0" name="checkAll" /></th>
                                                    <th style="text-align:center;">序号</th>
                                                    <th style="text-align:center;width: 40%;">题干</th>
                                                    <th style="text-align:center;">题型</th>
                                                    <th style="text-align:center;">来自题库</th>
                                                    <th style="text-align:center;">分值</th>
                                                    <th style="text-align:center;">操作</th>
                                                </tr>
                                                </thead>

                                                <tbody>

                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>

                        <div class="row">
                            <!-- 第二部分 -->
                            <div class="span11 right_details" style="margin-top:20px;" id="section-1" name="partPaper">
                                <form class="form-horizontal" style="width:95%;margin:0 auto;padding-top:15px;">

                                    <div class="control-group">
                                        <div class="row">
                                            <label class="span1"name="partNameNo" style="white-space: nowrap;font-size:16px;color:#777;padding: 5px 0 0 6px;font-family:微软雅黑">第二部分</label>

                                            <div class="span3" style="padding-left:20px;width: 200px">
                                                <select name="testpaperModeSelect" style="width: 150px;font-family:微软雅黑" class="form-control" placeholder="请选择分类">
                                                    <option VALUE=1>单选题</option>
                                                    <option VALUE=2 SELECTED>多选题</option>
                                                    <option VALUE=3>判断题</option>
                                                    <%--<option VALUE=4>简答题</option>--%>
                                                    <option VALUE=5>填空题</option>
                                                </select>
                                            </div>
                                            <div class="span2" style="width: 120px">
                                            </div>
                                            <!--<div class="btn-group" style="margin-left:0px;">
                                                <button class="btn btn-success" data-target="#itemSelectModal"><i class="glyphicon glyphicon-ok"></i> 选择题目</button>
                                            </div>
                                            <div class="btn-group" style="margin-left:0px;">
                                                <button class="btn btn-danger">删除该部分</button>
                                            </div>-->
                                            <div class="span1" style="padding-right:20px">
                                                <button type="button" name="selectItemBtn" class="btn btn-success" style="width: 100px;border: 0;font-family:微软雅黑">
                                                    <span class="icon-plus icon-white" style="margin-top: 2px"></span>&nbsp智能添加</button>
                                            </div>
                                            <div class="span1" style="padding-right:20px">
                                                <button type="button" name="itemPersonalBtn" class="btn btn-success" style="width: 100px;border: 0;font-family:微软雅黑;margin-left: 4px">
                                                    <span class="icon-plus icon-white" style="margin-top: 2px"></span>&nbsp手工添加</button>
                                            </div>
                                            <div class="span1" style="margin-left:28px;display: none">
                                                <button type="button" class="btn btn-danger" name="delPartBtn" style="width: 120px;border: 0;font-family:微软雅黑">
                                                    <span class="icon-remove icon-white" style="margin-top: 3px"></span>&nbsp删除该部分</button>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="control-group">
                                        <div class="row">
                                            <textarea id="partExplain1" name="partExplainArea" style="max-width:95%;width: 95%;margin-left: 2%" class="form-control span11 dash_input" rows="2" placeholder="部分答题说明，该信息考生可见(选填，无题目将不会保存此说明)" maxlength="400"></textarea>
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <blockquote style="border-left:5px solid #337AB7;display:inline;font-family:微软雅黑">题目详情</blockquote>
                                        <div class="btn-group" style="margin-left:60px;">
                                            <button type="button" name="batchDelItem" style="font-family:微软雅黑">
                                                <span class="icon-remove icon-white" style="margin-top: 3px"></span>&nbsp批量删除
                                            </button>
                                        </div>
                                        <div class="btn-group" style="margin-left:0px;">
                                            <button type="button" name="continueAdd" class="btn btn-default btn-md hide"style="font-family:微软雅黑">
                                                <span class="icon-plus icon-white" style="margin-top: 2px"></span>&nbsp继续智能添加
                                            </button>
                                        </div>
                                        <div class="btn-group" style="margin-left:0px;">
                                            <button type="button" name="continuePersonalAdd" class="btn btn-default btn-md hide"style="font-family:微软雅黑">
                                                <span class="icon-plus icon-white" style="margin-top: 2px"></span>&nbsp继续手工添加
                                            </button>
                                        </div>
                                        <%--<div class="btn-group">
                                            <button type="button" class="btn btn-default btn-md">
                                                <span class="glyphicon glyphicon-arrow-up"></span> 上移
                                            </button>
                                            <button type="button" class="btn btn-default btn-md">
                                                <span class="glyphicon glyphicon-arrow-down"></span> 下移
                                            </button>
                                        </div>--%>
                                        <%--<p style="display:inline;margin-left:60px;">
                                            总题数：<span style="color:orange">8</span><span style="color:gray">道</span>&nbsp;&nbsp;
                                            总分数：<span style="color:orange">16</span><span style="color:gray">分</span>
                                        </p>--%>
                                        <!--<div class="col-sm-2" style="padding-left:18px;float:right">
                                            <button class="btn btn-success"><i class="glyphicon glyphicon-ok"></i> 选择题目</button>
                                        </div>-->
                                    </div>
                                    <!-- 题目表格区域 2-->
                                    <div class="control-group">
                                        <div class="table-responsive">
                                            <table id="tb1" name="partTable" class="table table-striped table-bordered table-hover"style="font-family:微软雅黑">
                                                <thead>
                                                <tr>
                                                    <th style="text-align:center;"><input type="checkbox" id="checkBoxAllCheckBox1" name="checkAll" /></th>
                                                    <th style="text-align:center;">序号</th>
                                                    <th style="text-align:center;width: 40%">题干</th>
                                                    <th style="text-align:center;">题型</th>
                                                    <th style="text-align:center;">来自题库</th>
                                                    <th style="text-align:center;">分值</th>
                                                    <th style="text-align:center;">操作</th>
                                                 </tr>
                                                </thead>

                                                <tbody>

                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>

                        <div class="row">
                            <!-- 第三部分 -->
                            <div class="span11 right_details" style="margin-top:20px;" id="section-2" name="partPaper">
                                <form class="form-horizontal" style="width:95%;margin:0 auto;padding-top:15px;">

                                    <div class="control-group">
                                        <div class="row">
                                            <label class="span1"name="partNameNo" style="white-space: nowrap;font-size:16px;color:#777;padding: 5px 0 0 6px;font-family:微软雅黑">第三部分</label>

                                            <div class="span3" style="width: 200px;padding-left: 20px">
                                                <select name="testpaperModeSelect" style="width: 150px;font-family:微软雅黑" class="form-control" placeholder="请选择分类">
                                                    <option VALUE=1>单选题</option>
                                                    <option VALUE=2>多选题</option>
                                                    <option VALUE=3 SELECTED>判断题</option>
                                                    <%--<option VALUE=4>简答题</option>--%>
                                                    <option VALUE=5>填空题</option>
                                                </select>
                                            </div>
                                            <div class="span2" style="width: 120px">
                                            </div>
                                            <!--<div class="btn-group" style="margin-left:0px;">
                                                <button class="btn btn-success" data-target="#itemSelectModal"><i class="glyphicon glyphicon-ok"></i> 选择题目</button>
                                            </div>
                                            <div class="btn-group" style="margin-left:0px;">
                                                <button class="btn btn-danger">删除该部分</button>
                                            </div>-->
                                            <div class="span1" style="padding-right:20px">
                                                <button type="button" name="selectItemBtn" class="btn btn-success" style="width: 100px;border: 0;font-family:微软雅黑">
                                                    <span class="icon-plus icon-white" style="margin-top: 2px"></span>&nbsp智能添加</button>
                                            </div>
                                            <div class="span1" style="padding-right:20px">
                                                <button type="button" name="itemPersonalBtn" class="btn btn-success" style="width: 100px;border: 0;font-family:微软雅黑;margin-left: 4px">
                                                    <span class="icon-plus icon-white" style="margin-top: 2px"></span>&nbsp手工添加</button>
                                            </div>
                                            <div class="span1" style="margin-left:28px;display: none">
                                                <button type="button" class="btn btn-danger" name="delPartBtn" style="width: 120px;border: 0;font-family:微软雅黑">
                                                    <span class="icon-remove icon-white" style="margin-top: 3px"></span>&nbsp删除该部分</button>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="control-group">
                                        <div class="row">
                                            <textarea id="partExplain2" name="partExplainArea" style="max-width:95%;width: 95%;margin-left: 2%" class="form-control span11 dash_input" rows="2" placeholder="部分答题说明，该信息考生可见(选填，无题目将不会保存此说明)" maxlength="400"></textarea>
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <blockquote style="border-left:5px solid #337AB7;display:inline;font-family:微软雅黑">题目详情</blockquote>
                                        <div class="btn-group" style="margin-left:60px;">
                                            <button type="button" name="batchDelItem"  style="font-family:微软雅黑">
                                                <span class="icon-remove icon-white" style="margin-top: 3px"></span>&nbsp批量删除
                                            </button>
                                        </div>
                                        <div class="btn-group" style="margin-left:0px;">
                                            <button type="button" name="continueAdd" class="btn btn-default btn-md hide"style="font-family:微软雅黑">
                                                <span class="icon-plus icon-white" style="margin-top: 2px"></span>&nbsp继续智能添加
                                            </button>
                                        </div>
                                        <div class="btn-group" style="margin-left:0px;">
                                            <button type="button" name="continuePersonalAdd" class="btn btn-default btn-md hide"style="font-family:微软雅黑">
                                                <span class="icon-plus icon-white" style="margin-top: 2px"></span>&nbsp继续手工添加
                                            </button>
                                        </div>
                                        <%--<div class="btn-group">
                                            <button type="button" class="btn btn-default btn-md">
                                                <span class="glyphicon glyphicon-arrow-up"></span> 上移
                                            </button>
                                            <button type="button" class="btn btn-default btn-md">
                                                <span class="glyphicon glyphicon-arrow-down"></span> 下移
                                            </button>
                                        </div>--%>
                                        <%--<p style="display:inline;margin-left:60px;">
                                            总题数：<span style="color:orange">8</span><span style="color:gray">道</span>&nbsp;&nbsp;
                                            总分数：<span style="color:orange">16</span><span style="color:gray">分</span>
                                        </p>--%>
                                        <!--<div class="col-sm-2" style="padding-left:18px;float:right">
                                            <button class="btn btn-success"><i class="glyphicon glyphicon-ok"></i> 选择题目</button>
                                        </div>-->
                                    </div>
                                    <!-- 题目表格区域 3-->
                                    <div class="control-group">
                                        <div class="table-responsive">
                                            <table id="tb2" name="partTable" class="table table-striped table-bordered table-hover" style="font-family:微软雅黑">
                                                <thead>
                                                <tr>
                                                    <th style="text-align:center;"><input type="checkbox" id="checkBoxAllCheckBox2" name="checkAll" /></th>
                                                    <th style="text-align:center;">序号</th>
                                                    <th style="text-align:center;width: 40%">题干</th>
                                                    <th style="text-align:center;">题型</th>
                                                    <th style="text-align:center;">来自题库</th>
                                                    <th style="text-align:center;">分值</th>
                                                    <th style="text-align:center;">操作</th>
                                                </tr>
                                                </thead>

                                                <tbody>

                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <%@include file="testPaperPublic.jsp"%><%--公共模态框--%>
    </div>
</div>

<script type="text/javascript">
    //全屏显示
    function fullScreen(){
        var el = document.documentElement;
        var rts = el.requestFullScreen || el.webkitRequestFullScreen || el.mozRequestFullScreen || el.msRequestFullScreen;
        if(typeof rts !="undefined" && rts){
            rts.call(el);
        }else if(typeof window.ActiveXObject != "undefined"){
            var wscript = new ActiveXObject("WScript.Shell");
            if(wscript != null){
                wscript.SendKeys("{F11}");
            }
        }
    }

    $(document).ready(function(){
        $('input').iCheck({
            checkboxClass: 'icheckbox_square-blue',  // 注意square和blue的对应关系
            radioClass: 'iradio_square-blue',
            increaseArea: '20%' // optional
        });
    });
/*
    $('#myAffix').affix({
        offset : {
            top : 105,
        }
    });
*/

    function getSection(id){
        window.location.href="#section-"+id;
        var myAffix = "myAffixLi"+id;
         $('li[name="myAffixLi"]').each(function(index){
            if(myAffix == $(this).attr("id")){
                $(this).attr('class','active');
            }else{
                $(this).removeClass();
            }

        });
    }

    //附加导航随滚动条变化
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
    });

</script>

</body>
</html>