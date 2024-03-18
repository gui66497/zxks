<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>模态框例子</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%--<script type="text/javascript" src="js/jquery-1.12.3.min.js" />--%>
    <%--<script type="application/javascript" src="js/bootstrap.min.js"></script>--%>
    <%--<script src="js/bootstrap/js/bootstrap-modal.js"></script>
    <script src="js/bootstrap/js/bootstrap-modalmanager.js"></script>--%>
    <%--<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">--%>
    <%--<link href="js/bootstrap/css/bootstrap-modal.css" rel="stylesheet" media="screen" />--%>
    <style type="text/css">
        #myModal
        {
            top:200px;
        }
        .btn_cancel{
            border: 0 !important;
            border-radius: 4px !important;
        }
        .cancel{
            width:70px;
            height:34px;
            background-color: #bec3c7;
            border:0;
            border-radius: 4px;
            color: #ffffff;
            font-size: 14px;
            font-family: 微软雅黑;
        }
        .cancel:hover{
            background-color: #b0b5b9;
        }
        .closeTable{
            background: url("../images/close.png");
            float: right;
            width: 50px;
            height: 50px;
            margin-top: 0px;
            margin-right: -16px;
            opacity:1;
            border: 0;
        }
        .closeTable:hover{
            background: url("../images/close_hover.png");
        }
    </style>
    <script type="text/javascript">

        //给指定元素绑定事件（允许带参数)
        function bindEventWithArgs(target, eventName, handler, args) {
            var eventHandler = handler;
            if(args) {
                eventHander = function(e) {
                    handler.call(args, e);
                }
            }
            if(window.attachEvent) {    //IE
                target.attachEvent("on" + eventName, eventHander);
            } else {    //FF
                target.addEventListener(eventName, eventHander, false);
            }
        }

        //弹出消息提醒框
        function showOkModal(label, contennt) {
            $("#okModalLabel")[0].innerHTML = label;
            $("#okModalMessage")[0].innerHTML = contennt;
            $("#okModal").modal({
                show:true,
                keyboard:false,
                backdrop:'static'
            });
        }

        //弹出确认框并对确认按钮绑定事件
        function showConfirmModal(content, fn, param) {
            //$("#confirmModalLabel")[0].innerHTML = label;
            $("#confirmModalMessage")[0].innerHTML = content;
            $("#confirmModal").modal({
                show:true,
                keyboard:false,
                backdrop:'static'
            });
            $("#btnExecute").remove();
            $("#confirmModalFooter").append('<button type="button" id="btnExecute" class="sure">确定</button>');
            var btn = document.getElementById("btnExecute");
            if(param==null) {
                $("#btnExecute").bind("click", eval(fn));
            } else {
                bindEventWithArgs(btn, "click", fn, param);
            }
            $("#btnExecute").bind("click", eval(hideConfirmModal));
        }

        //展示模态框
        function showModal(modalName) {
            $("#"+modalName).modal(
                    {
                        show:true,
                        keyboard:false,
                        backdrop:'static'
                    }
            );
        }

        //隐藏模态框
        function hideModal(modalName) {
            $("#"+modalName).modal('hide');
        }

        //隐藏确认模态框
        function hideConfirmModal() {
            $("#confirmModal").modal('hide');
        }
    </script>

</head>
<body>

<!-- 模态框（Modal） -->
<div class="modal fade dialog fade hide" id="okModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="top:250px;">
    <div class="modal-dialog">
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
                <button type="button" class="btn_cancel" style="width:60px;height:30px;"data-dismiss="modal">关闭</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

<!-- 模态框（Modal） -->
<div class="modal fade dialog fade hide" id="confirmModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="top:250px;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true">
                </button>
                提示框
            </div>
            <div class="modal-body" id="confirmModalMessage" style="height:45px;">
                在这里添加一些文本
            </div>
            <div class="modal-footer" id="confirmModalFooter">
                <button type="button" class="cancel" data-dismiss="modal">取消</button>
                <button type="button" class="btn sure" id="btnExecute" >确定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
</body>
</html>
