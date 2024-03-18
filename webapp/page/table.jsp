<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
  <title>DataTable例子</title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <!-- DataTables CSS -->
  <link rel="stylesheet" type="text/css" href="/exts/datatables/css/jquery.dataTables.css">
  <!-- jQuery -->
  <script type="text/javascript" charset="utf8" src="/exts/jquery-1.12.3.min.js"></script>
  <!-- DataTables -->
  <script type="text/javascript" charset="utf8" src="/exts/datatables/js/jquery.dataTables.js"></script>
  <script type="text/javascript">
    $(document).ready( function () {
      $('#table_id').DataTable({
        "language" : {
          "sSearchPlaceholder": "请输入试卷名称"
        },
        "bServerSide": true,                //指定从服务器端获取数据
        "bProcessing": true,                //显示正在处理进度条
        "sAjaxSource": "rest/user/list",    //服务端Rest接口访问路径
        "columns" : [
          {"data": "operate",
            "mRender": function(data, type, full) {
              return '<A href="../OrderdetailsServlet?customerID='+data+'" target="_blank">删除</A>';}
          },
          {"data" : "userId", 'sClass':'center'},
          {"data" : "userName", 'sClass':'center'},
          {"data" : "sex", 'sClass':'center'},
          {"data" : "password", 'sClass':'left'},
          {"data" : "email", 'sClass':'left'},
          {"data": "operate",
            "mRender": function(data, type, full) {
              return '<A href="../OrderdetailsServlet?customerID='+data+'" target="_blank">删除</A>';}
          }],
        "aoColumnDefs": [                                //指定列附加属性
          {"bSortable": false, "aTargets": [1, 3]},   //这句话意思是第1,3（从0开始算）不能排序
          {"bSearchable": false, "aTargets": [1, 2]}, //bSearchable 这个属性表示是否可以全局搜索，其实在服务器端分页中是没用的
        ],
        "aaSorting": [[2, "desc"]], //默认排序
        "fnRowCallback": function(nRow, aData, iDisplayIndex) {     // 当创建了行，但还未绘制到屏幕上的时候调用，通常用于改变行的class风格
          if (aData.sex == 1) {
            $('td:eq(2)', nRow).html("男");
          } else if (aData.sex == 0) {
            $('td:eq(2)', nRow).html("女");
          }
          return nRow;
        },
        "fnServerData" : function(sSource, aoData, fnCallback) {
          $.ajax({
            "type" : 'get',
            "contentType": "application/json",
            "url" : sSource,
            "dataType" : "json",
            "headers" : {userId: 118},
            "data" : JSON.stringify({
              paging:convertToPagingEntity(aoData)
            }),
            "success" : function(resp) {
              fnCallback(resp);
            }
          });
        }
      });
    } );

    function convertToPagingEntity(aoData) {
      var pagingEntity = new Object();
      $.each(aoData, function(i,val){
        if(val.name=="iDisplayStart") {
          pagingEntity.startIndex = val.value;
        } else if(val.name=="iDisplayLength") {
          pagingEntity.pageSize = val.value;
        } else if(val.name=="sSearch") {
          pagingEntity.searchValue = val.value;
        } else if(val.name=="iSortCol_0") {
          pagingEntity.sortColumn = val.value;
        } else if(val.name=="sSortDir_0") {
          pagingEntity.sortDir = val.value;
        } else if(val.name=="sEcho") {
          pagingEntity.sEcho = val.value;
        }
      });
      //var jsonObj = JSON.stringify(pagingEntity);
      return pagingEntity;
    }
  </script>
</head>
<body>
<div id="mainDiv">
  <div id="headDiv">
    <jsp:include page="menu.jsp" />
  </div>
  <div id="contentDiv">
    <table id="table_id" class="display">
      <thead>
      <tr>
        <th>全选</th>
        <th>ID</th>
        <th>用户名</th>
        <th>性别</th>
        <th>密码</th>
        <th>邮箱</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>

      </tbody>
    </table>
  </div>
</div>
</body>
</html>