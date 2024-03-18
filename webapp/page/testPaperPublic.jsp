<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--抽题公共模态框--%>
<div class="modal hide fade" id="itemSelectModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="width: 80%">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header" style="border-top-radius: 4px;height: 32px;line-height: 32px">
          <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true" style=" margin-top: -9px">
          </button>
        <span class="modal-title" style="font-size: 18px;font-family:微软雅黑">
          抽取题目
        </span>
      </div>
      <div class="modal-body" style="height:638px;">
        <form class="form-horizontal">

          <div class="container" style="margin-top:20px">
            <div class="row">
              <div class="span3" style="margin-left: -3%">
                <div class="panel-group">
                  <div class="panel panel-default">
                    <div class="panel-heading">
                      <span class="panel-title" style="position:relative;font-family:微软雅黑;font-size: 14px;font-weight: bold">选择题库</span>
                      <div style="border:1px solid #eee;margin-top:20px;position:absolute;right:30px;top:-16px;">
                      </div>
                    </div>
                    <div id="collapseOne" class="panel-collapse collapse fade in">
                      <div class="panel-body">
                        <div id="treeview-checkable" style="overflow-y: scroll;max-height: 600px;"></div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="span9">
                <div class="row">
                  <!-- 第一部分 -->
                  <div class="span10 right_details">
                    <form class="form-horizontal" style="width:95%;margin:0 auto;padding-top:15px;">
                      <div class="control-group" style="margin-top: 15px;margin-left: 5px;font-family:微软雅黑">
                        <blockquote style="border-left:5px solid #337AB7;display:inline;float: left;padding: 4px 0 0 16px">定义试卷</blockquote>
                        <div id="scoreP" style="margin-left:70px;float: left" >
                          &nbsp;&nbsp;
                            每题分值：<input type="text" id="itemScoreInput" onkeypress="return (/[1,2,3,4,5,6,7,8,9,0]/.test(String.fromCharCode(event.keyCode)))" value="5" style="width: 50px;" >
                        </div>

                        <div class="col-sm-2" style="padding-right: 20px;;float:right">
                          <a href="#" id="continueAddBtn" onclick="toContinueExtractItem()" class="btn btn-success hide"><i class="glyphicon glyphicon-ok"></i> 继续抽题</a>
                          <a href="#" id="addBtn" onclick="toExtractItem()" class="btn btn-success"><i class="glyphicon glyphicon-ok"></i> 抽取题目</a>
                        </div>
                      </div>
                      <!-- 题目表格区域 -->
                      <div class="control-group" style="margin-left: 5px;margin-right: 2px;">
                        <div class="table-responsive">
                          <table id="selectItemTb" style="width: 100%" class="table table-striped table-bordered table-hover">
                            <thead>
                            <tr >
                              <th style="text-align:center;">id</th>
                              <th style="text-align:center;">题型</th>
                              <th style="text-align:center;">题库名称</th>
                              <th style="text-align:center;">题目数</th>
                              <th style="text-align:center;">抽题数</th>
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
                      <div class="span10 right_details" style="border: 0px;padding-top: 4px">
                          <div class="alert fade in hide" style="font-family: 微软雅黑" id="addItemWarn"></div>
                      </div>
                  </div>

              </div>
            </div>
          </div>

        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn_cancel" style="width:70px;height:30px;border: 0;border-radius: 4px;font-family:微软雅黑"data-dismiss="modal">关闭</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal -->
</div>

<div class="modal hide fade" id="itemPersonalModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="width: 80%">
    <div class="modal-dialog" style="margin-top:20px;top: 10%">
        <div class="modal-content">
            <div class="modal-header" style="margin-top: -20px;font-family:微软雅黑;height: 32px;">

                <button type="button" onclick="$.testPaperPublic.clearTable()" class="closeTable" data-dismiss="modal" aria-hidden="true" style=" margin-top: -9px">
                </button>
                <span class="modal-title" style="font-size: 18px">
                    添加题目
                </span>
            </div>
            <div class="modal-body" style="min-height:730px">
                <form class="form-horizontal">

                    <div class="container" style="margin-top:20px">
                        <div class="row">
                            <div class="span3" style="margin-left: -3%">
                                <div class="panel-group">
                                    <div class="panel panel-default">
                                        <div class="panel-heading" style="font-family:微软雅黑">
                                            <h5 class="panel-title" style="position:relative">选择题库</h5>
                                            <div style="border:1px solid #eee;margin-top:20px;position:absolute;right:30px;top:-16px;">
                                            </div>
                                        </div>
                                        <div id="collapseTwo" class="panel-collapse collapse fade in">
                                            <div class="panel-body">
                                                <div id="treeview-checkable2" style="overflow-y: scroll;max-height: 600px;"></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="span9">
                                <div class="row">
                                    <!-- 第一部分 -->
                                    <div class="span11 right_details" style="width: 780px;height: 220px;">
                                        <form class="form-horizontal" style="width:95%;margin:0 auto;padding-top:15px;">
                                            <div class="control-group" style="margin-top: 15px;margin-left: 5px;font-family:微软雅黑">
                                                <blockquote style="border-left:5px solid #337AB7;display:inline;float: left;padding: 4px 0 0 16px">已选列表</blockquote>
                                               <div id="scoreD" style="margin-left:70px;float: left;font-family:微软雅黑" >
                                                    总题数：<span id="countSpan" style="color:orange"></span><span style="color:gray"> 道</span>&nbsp;&nbsp;
                                                   每题分值：<input type="text" id="scoreInput" onkeypress="return (/[1,2,3,4,5,6,7,8,9,0]/.test(String.fromCharCode(event.keyCode)))" style="width: 50px;" value="5" >
                                                </div>
                                                <div id="addItemD" class="hide" style="margin-left:70px;float: left">
                                                    添加题数：<span type="text" id="continueAddInput" style="width: 50px;"></span><span style="color:gray"> 道</span>
                                                </div>

                                                <div class="col-sm-2" style="padding-right: 20px;;float:right">
                                                    <a href="#" id="uptoAddBtn" onclick="toContinuePersonalExtractItem()" class="btn btn-success hide"><i class="glyphicon glyphicon-ok"></i> 继续添加</a>
                                                    <a href="#" id="addItemBtn" onclick="toExtractPersonalItem()" class="btn btn-success"><i class="glyphicon glyphicon-ok"></i> 添加题目</a>
                                                </div>
                                            </div>
                                            <!-- 题目表格区域 -->
                                            <div class="control-group" style="height:120px;margin-left: 5px;margin-right: 2px;overflow-y: scroll">
                                                <div class="table-responsive">
                                                    <table id="selectItemTab1" style="width: 100%" class="table table-striped table-bordered table-hover">
                                                        <thead>
                                                        <tr >
                                                            <th style="text-align:center;">#</th>
                                                            <th style="text-align:center;">序号</th>
                                                            <th style="text-align:center;width: 30% !important;">题干</th>
                                                            <th style="text-align:center;">题型</th>
                                                            <th style="text-align:center;">答案</th>
                                                            <th style="text-align: center">操作</th>
                                                        </tr>
                                                        </thead>

                                                        <tbody>

                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="span10 right_details" style="margin-top: 10px;min-height: 400px;height:450px;overflow-y: scroll">
                                        <form class="form-horizontal" style="width:95%;margin:0 auto;padding-top:15px;max-height: 640px">
                                            <div class="control-group" style="position: absolute;font-family:微软雅黑">
                                                <blockquote style="border-left:5px solid #337AB7;display:inline;float: left;padding: 4px 0 0 16px">题目列表</blockquote>

                                            </div>
                                            <!-- 题目表格区域 -->
                                            <div class="control-group" style="margin-left: 5px;margin-right: 2px;">
                                                <div class="table-responsive">
                                                    <table id="selectItemTab" style="width: 100%" class="table table-striped table-bordered table-hover">
                                                        <thead>
                                                        <tr >
                                                            <th style="text-align:center;">id</th>
                                                            <th style="text-align:center;"><input type="checkbox" id="personalCheckBox" name="personalCheckBox"></th>
                                                            <th style="text-align:center;">题干</th>
                                                            <th style="text-align:center;">题型</th>
                                                            <th style="text-align:center;">答案</th>
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
                                    <div class="span10 right_details" style="border: 0px;padding-top: 4px;">
                                        <div class="alert fade in hide" style="font-family: 微软雅黑" id="addPersonalItemWarn"></div>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>

                </form>
            </div>

        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>


<%--显示题目详情公共模态框--%>
<div class="modal hide fade" id="itemDetailModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="top: 40%;min-width: 33%;left: 44%">
  <div class="modal-header" style="height: 32px;">
    <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true" style=" margin-top: -9px">

    </button>
    <span class="modal-title" style="font-family:微软雅黑;font-size: 18px">
      题目详情
    </span>
  </div>
  <div class="modal-body" style="padding-left:35px;overflow-x: hidden;font-family:微软雅黑">
    <form class="control-group">
      <div class="panel panel-default">
        <div class="panel-heading">
          <span style="width: auto" id="itemDetailTitle"></span>
            <div style="width: auto" id="itemCode"></div>
        </div>
        <div class="panel-body" style="padding-bottom: 15px;height: 220px;font-family:微软雅黑" id="itemBody">

        </div>
      </div>
    </form>
  </div>
  <div class="modal-footer" style="font-family:微软雅黑">
    <button type="button" class="btn_cancel" style="width:70px;height:30px;border-radius: 4px;border: 0;"data-dismiss="modal">关闭</button>
  </div>
</div>