<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 <% 
 if(request.getAttribute("flag")==null){
	 %>
	 <jsp:forward page="/class.do"></jsp:forward>
	  <%
	 return ;
 }
 List<com.scoreproject.po.Class> params =  (ArrayList<com.scoreproject.po.Class>)request.getAttribute("datas");
 %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

""""""""""*************************"""""""""""""
""""""""""  简单成绩管理""""""""""""""""

<table>
<tr><td>序号</td><td>班级</td></tr>
<% 

for(com.scoreproject.po.Class data:params){
	%>
	<tr><td><%=data.getId()%></td><td><a href="student.do?classNoX=<%=data.getId()%>"><%=data.getClassNo()%></a></td></tr>
<% 
}
%>

</table>


添加班级:
<form action="class.do" method="post"/>
<input type="hidden" name="type" value="add">
班级：<input type="text" name="classNo" >
<input type="submit" value="增加">
</form>
</body>
</html>