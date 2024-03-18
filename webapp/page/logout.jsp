<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="../images/favicon.ico" rel="icon" type="image/x-icon"/>
    <title>用户注销</title>
</head>
<script type="text/javascript">
  var logoutUserId = <%=request.getParameter("userId")%>;
  var beforeUserId = <%=request.getSession().getAttribute("userId")%>;
  <% request.getSession().invalidate();%>
  var afterUserId = <%=request.getSession().getAttribute("afterUserId")%>;
  window.location.href="login.jsp";
</script>
<body>
</body>
</html>
