<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<h2>欢迎来到易直购9001</h2>

<%= request.getRemoteUser()%>
<a href="http://localhost:9100/cas/logout?service=http://www.baidu.com">退出</a>
</body>
</html>
