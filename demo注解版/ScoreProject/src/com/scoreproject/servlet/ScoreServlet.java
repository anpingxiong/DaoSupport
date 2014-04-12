package com.scoreproject.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.scoreproject.po.Course;
import com.scoreproject.po.Score;
import com.scoreproject.service.impl.CourseServiceImpl;
import com.scoreproject.service.impl.ScoreServiceImpl;

/**
 * Servlet implementation class ScoreServlet
 */
@WebServlet("/score.do")
public class ScoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScoreServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String type =  request.getParameter("type");
		 List<Score> scores =null;
		 String studentId=request.getParameter("studentId");
		 if(type==null){
		}else if(type.equals("add")){
			String scoreId = request.getParameter("courseId");
			float score   = Float.parseFloat(request.getParameter("score"));
		   	 ScoreServiceImpl.getInstance().addScore(Integer.parseInt(studentId), Integer.parseInt(scoreId), score);
	 	}
	      scores =   ScoreServiceImpl.getInstance().showScore(Integer.parseInt(studentId), -1);
	  	  List<Course> courses =  CourseServiceImpl.getInstance().showCourse();
		  request.setAttribute("flag",1);
		  request.setAttribute("scores",scores);
		  request.setAttribute("courses",courses);
		  request.setAttribute("studentId", studentId);
		  request.setAttribute("studentName", request.getParameter("studentName"));
		  request.getRequestDispatcher("showScore.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(request, response);
	}

}
