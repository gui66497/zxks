/**
 * Created by guzhenggen on 2016/6/8.
 */

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
    $("#confirmModalFooter").append('<button type="button" id="btnExecute" class="btn btn-primary">确定</button>');
    var btn = document.getElementById("btnExecute");
    if(param==null) {
        $("#btnExecute").bind("click", eval(fn));
    } else {
        bindEventWithArgs(btn, "click", fn, param);
    }
}

function showAlert(id,message) {
    //$("#"+id).removeClass("hide");
    $("#"+id).text('');
    $("#"+id).append('<button type="button"class="close" onclick="hideAlert(\''+id+'\')">&times;</button>');
    $("#"+id).append(message);
    $("#"+id).show();
    $("#"+id).delay(2000).hide("fast");
    //$("#"+id).removeClass("hide");
}

function showAlertNoHide(id,message) {
    $("#"+id).removeClass("hide");
    $("#"+id).text('');
    $("#"+id).append('<button type="button"class="close" onclick="hideAlert(\''+id+'\')">&times;</button>');
    $("#"+id).append(message);
    $("#"+id).show();
    $("#"+id).delay(2000).hide("fast");
    //$("#"+id).delay(2000).hide("fast");
    //$("#"+id).removeClass("hide");
}

function hideAlert(id) {
    //$("#"+id).addClass("hide");
    $("#"+id).hide();
}

function doSomething(id) {
    alert("执行删除操作1！" + id);
}