package com.scoreproject.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.scoreproject.service.impl.ClassServiceImpl;

/**
 * Servlet implementation class ClassServlet
 */
@WebServlet("/class.do")
public class ClassServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ClassServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  String  classNoX  =   request.getParameter("classNo");
		  String  type  = request.getParameter("type");
		  System.out.println(classNoX+"-----------------------"+type);
		  List<com.scoreproject.po.Class> datas  = null;
		  if(classNoX==null ){
			  datas=  ClassServiceImpl.getInstance().showClass();
			  for(com.scoreproject.po.Class data:datas){
				  System.out.println(data);
				  
			  }
			  request.setAttribute("flag", "1");
			  request.setAttribute("datas", datas);
	          request.getRequestDispatcher("./index.jsp").forward(request, response);   
		  }else{
			  if("add".equals(type)){
				  System.out.println(type);
				  ClassServiceImpl.getInstance().addClass(classNoX);
				  response.sendRedirect("./index.jsp");
			  }
		  }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(request, response);
	}

}
