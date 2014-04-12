<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 <% 
 
 List<com.scoreproject.po.Student> params =  (ArrayList<com.scoreproject.po.Student>)request.getAttribute("datas");
 %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<table>
 <% 

 if(params!=null){
	 %>
	 <tr><td>编号</td><td>姓名</td><td>录成绩</td></tr>
	 
	 <%

for(com.scoreproject.po.Student data:params){
	%>
	<tr><td><%=data.getStudentNo()%></td><td><%=data.getStudentName()%></td><td><a href="./showScore.jsp?studentId=<%=data.getId()%>&studentName=<%=data.getStudentName()%>">录入成绩</a></td></tr>
<% 
}
 }
%>
 
</table>

添加学生:<form action="student.do" method="post"/>
<input type="hidden" value="add" name="type">
<input type="hidden" value="<%=request.getAttribute("classNo")%>" name="classNoX">

姓名：<input type="text" name="studentNo" >
编号：<input type="text" name="studentName" >
<input type="submit" value="增加">
</form>
</body>
</html>