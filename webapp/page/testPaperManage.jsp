<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="../images/favicon.ico" rel="icon" type="image/x-icon"/>
    <title>试卷管理</title>
    <link rel="stylesheet" type="text/css" href="../css/bootstrap-treeview.css">
    <link rel="stylesheet" type="text/css" href="../exts/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="../exts/bootstrap/css/bootstrap-modal.css">
    <link rel="stylesheet" type="text/css" href="../css/square/blue.css">
    <link rel="stylesheet" type="text/css" href="../css/basic.css">
    <!-- DataTables CSS -->
    <link rel="stylesheet" type="text/css" href="../exts/datatables/css/jquery.dataTables.css">
    <link rel="stylesheet" type="text/css" href="../css/itemBankManage.css">
    <link rel="stylesheet" type="text/css" href="../css/testPaper/testPaperManage.css">

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
    <script type="application/javascript" src="../js/testPaper/testPaper.js"></script>
    <script type="application/javascript" src="../js/testPaper/testPaperPublic.js"></script>
    <style>
        th,button,td,span,input,div{

            font-family:微软雅黑 !important;
        }
        .treeview ul{
            font-family:微软雅黑 !important;
        }
        #update_password_modal{
            top: 30% !important;
            width:480px;
            z-index: 3000;
        }
        input[type="password"]{
            height: 34px !important;
            width: 150px !important;
        }
        .modal-backdrop{
            background: #333 !important;
        }
        .columnLeft{
            text-align: left;
        }
        .btn-md1:hover{
            background-color:  #bd362f !important;
                 }
    </style>

</head>
<body style="background-color: #f1f4f8">
<input id="nodeId" value="" style="display: none"/>
<input id="nodeText" value="" style="display: none"/>
<input id="itemBankId" value="" style="display: none"/>

<div id="mainDiv" >
    <div id="headDiv">
        <jsp:include page="menu.jsp"/>
    </div>
    <div id="contentDiv" style="text-align: left;">
        <div class="container-fluid">
            <%@include file="mymodal.jsp"%>
            <div class="leftDiv">
               <div class="categoryDiv">
                   <div class="treeDiv">
                       <span class="fontSpan" style="font-family:微软雅黑">考试类别</span>
                       <div class="btn-group" style="margin:0 0 3px 0;float: right">
                           <button class="btn btn-default btn-xs"
                                   title="新增类别"
                                   data-placement="auto left"
                                   data-trigger="hover focus"
                                   onclick="addTestPaperCategory()">
                               <span class="icon-plus"></span>
                           </button>
                           <button class="btn btn-default btn-xs"
                                   title="删除类别"
                                   data-toggle="tooltip"
                                   data-placement="auto bottom"
                                   data-trigger="hover focus"
                                   onclick="toDelCategory()">
                               <span class="icon-minus"></span>
                           </button>
                           <button class="btn btn-default btn-xs"
                                   title="编辑类别"
                                   data-placement="auto right"
                                   data-trigger="hover focus"
                                   data-toggle="modal"
                                   onclick="showCategoryUpdateModal()"
                                   >
                               <span class="icon-pencil"></span>
                           </button>
                       </div>
                       <div id="treeview" class="treeview"></div>
                   </div>
               </div>
            </div>
            <div class="rightDiv">
                <div class="rightHeader"><span style="margin-left: 15px;font-size: 16px;color: #333;font-family:微软雅黑">试卷管理</span></div>

                <div class="btn-group" style="width: 50%;position: absolute;top:162px;z-index:1;display: inherit;">
                    <!-- <div class="btn-group"> -->
                    <button type="button" class="sure btn-md" style="width: 100px" data-toggle="modal" onclick="showAddTestpaperModal()" data-backdrop="static">
                        <span class="icon-plus icon-white" style="margin-top: 2px"></span>&nbsp创建试卷
                    </button>

                    <button class="sure btn-md1" style="width:100px;margin-left: -2px;background-color:#da4f49" onclick="toDeletePaperByList()">
                        <span class="icon-remove icon-white" style="margin-top: 3px"></span> 批量删除
                    </button>
                </div>

                <div class="table-responsive">
                    <table id="tb" >
                        <thead>
                        <tr>
                            <th style="text-align: center;"><input type="checkbox" id="checkBoxAllCheckBox"></th>
                            <th style="text-align: center">试卷名称</th>
                            <th style="text-align: center">考试类别</th>
                            <th style="text-align: center">试卷描述</th>
                            <th style="text-align: center">试卷说明</th>
                            <th style="text-align: center">创建时间</th>
                            <th style="text-align: center;">更新时间</th>
                            <th style="text-align: center; min-width: 70px;">操作</th>
                        </tr>
                        <tbody style="text-align: center">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>


<!-- 新增考试类别模态框 -->
<div class="modal hide fade" id="categoryAddModal" style="top:250px;"><!-- fade是设置淡入淡出效果 -->
    <div class="modal-dialog category">
        <div class="modal-content">
            <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                <button type="button" class="closeTable"  data-dismiss="modal" aria-hidden="true" style="color: #fff">
                </button>
                新增考试类别
            </div>
            <div class="modal-body" style="padding:20px 20px;">
                <table>
                    <tr>
                        <td><label class="moda-label" id="preNode"style="margin-left: 10px;" >上级类别:&nbsp;&nbsp;</label></td>
                        <td><span id="parentNode" style="margin-left: 30px;"></span></td>
                    </tr>
                    <tr>
                        <td><label class="moda-label"><span style="color:red;">*</span>类别名称</label></td>
                        <td><input type="text" id="categoryName" class="form-control" style="width: 280px;margin-left: 30px;margin-bottom: 0px;height: 34px;" maxlength="20"
                                   placeholder="请填写类别名称"></td>
                    </tr>
                </table>
            </div>
            <!--警告框-->
            <div class="alert alert-block hide" style="color: #ff6c60;margin: 0;width: 89%;" id="addCategoryWarn"></div>
            <div class="modal-footer">
                <button class="cancel" data-dismiss="modal" style="width:70px;margin-right: 15px">取消</button>
                <button class="sure" onclick="addCategory();">确定</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改考试类别模态框 -->
<div class="modal hide fade" id="categoryUpdateModal" style="top:250px;"><!-- fade是设置淡入淡出效果 -->
    <div class="modal-dialog category">
        <div class="modal-content">
            <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                <button type="button" class="closeTable"  data-dismiss="modal" aria-hidden="true" style="color: #fff">
                </button>
                修改考试类别
            </div>
            <%--<div class="modal-body" id="parentUpdateDiv" style="padding:20px 20px 0 20px;display: none">
                <label class="moda-label"><span style="color:red;">*</span>父类别</label>
                <input type="text" id="parentNameUpdate" readonly="readonly" class="form-control" style="width: 280px;margin-left: 30px;margin-bottom: 0px;height: 34px;" maxlength="25"
                       placeholder="请填写类别名称">
            </div>--%>
            <div class="modal-body" style="padding:20px 20px;">
                <label class="moda-label"><span style="color:red;">*</span>类别名称</label>
                <input type="text" id="categoryNameUpdate" class="form-control" style="width: 280px;margin-left: 30px;margin-bottom: 0px;height: 34px;" maxlength="20"
                       placeholder="请填写类别名称">
            </div>
            <!--警告框-->
            <div class="alert alert-block hide" style="color: #ff6c60;margin: 0;width: 89%;" id="updateCategoryWarn"></div>
            <div class="modal-footer">
                <button class="cancel" data-dismiss="modal" style="width:70px;margin-right: 15px">取消</button>
                <button class="sure" onclick="updateCategory();">确定</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
