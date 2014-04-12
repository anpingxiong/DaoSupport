<%@page import="com.scoreproject.po.Course"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
if(request.getAttribute("flag")==null){
	
	response.sendRedirect("score.do?studentId="+request.getParameter("studentId"));
	return;
}   


List<com.scoreproject.po.Score> scores=(	List<com.scoreproject.po.Score>) request.getAttribute("scores");
List<Course> course= (List<com.scoreproject.po.Course>)request.getAttribute("courses");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<% 
   if(scores!=null){
	   
  %>
   
  姓名： <%=request.getAttribute("studentName") %>
  <table>
   
   
   
   <tr><td>科目</td><td>成绩</td></tr>
   <%
   
   for(com.scoreproject.po.Score score:scores){
	 %>
	   <tr><td><%=score.getCourse().getName() %></td><td><%=score.getScore()%></td></tr>	   
<%
   }
   %>
   
   <% }%>
  </table>
  
  
 录入分数：
 <form action="./score.do" method="post">
 <input type="hidden" name="type" value="add">
  <input type="hidden" name="studentId" value="<%=request.getAttribute("studentId") %>">
 <select name="courseId">
   <% 
   for(Course course2:course){
   %>
   <option value="<%=course2.getId()%>"><%=course2.getName()%></option>
   <% }%>
 </select>
 分数：<input type="text" name="score">
 
 <input type="submit" value="录入分数">
 </form>
  
  
</body>
</html>