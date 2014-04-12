package com.scoreproject.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.scoreproject.po.Student;
import com.scoreproject.service.impl.StudentServiceImpl;

/**
 * Servlet implementation class StudentServlet
 */
@WebServlet("/student.do")
public class StudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
        String	classNo = request.getParameter("classNoX");
        String type   = request.getParameter("type");
        //type==null则为显示班级的所有学生==add表示添加学生==1
        System.out.println(classNo+type);
        List<Student> students =null;
        if(type==null){
        	students=StudentServiceImpl.getInstance().showStudent(Integer.parseInt(classNo));
          }else if(type.equals("add")){
          String studentNo= request.getParameter("studentNo");  
          String studentName = request.getParameter("studentName");
          StudentServiceImpl.getInstance().addStudent(studentNo, studentName, Integer.parseInt(classNo));
           students = StudentServiceImpl.getInstance().showStudent(Integer.parseInt(classNo));
      
         }
        request.setAttribute("datas", students);

        request.setAttribute("classNo", classNo);
        request.getRequestDispatcher("./student.jsp").forward(request, response);
        
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(request, response);
	}
	

}
