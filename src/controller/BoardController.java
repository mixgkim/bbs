package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import model.BBSVO;
import model.BoardDAO;


@WebServlet(value={"/bbs/list","/bbs/list.json","/bbs/insert","/bbs/read","/bbs/update","/bbs/delete"})
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    BoardDAO dao=new BoardDAO();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		RequestDispatcher dis=request.getRequestDispatcher("/home.jsp");
		
		switch(request.getServletPath()) {
		case "/bbs/delete":
			int no=Integer.parseInt(request.getParameter("no"));
			dao.delete(no);
			response.sendRedirect("/bbs/list");
			break;
		case "/bbs/update":
			no=Integer.parseInt(request.getParameter("no"));
			request.setAttribute("vo", dao.read(no));
			request.setAttribute("pageName", "/bbs/update.jsp");
			dis.forward(request, response);
			break;
		case "/bbs/read":
			no=Integer.parseInt(request.getParameter("no"));
			request.setAttribute("vo", dao.read(no));
			request.setAttribute("pageName", "/bbs/read.jsp");
			dis.forward(request, response);
			break;
		case "/bbs/list.json":
			int page=Integer.parseInt(request.getParameter("page"));
			String type=request.getParameter("type");
			String query=request.getParameter("query");
			
			ArrayList<BBSVO> array=dao.list(page, type, query);
			
			JSONArray jArray=new JSONArray();
			for(BBSVO vo:array) {
				JSONObject obj=new JSONObject();
				obj.put("no", vo.getNo());
				obj.put("title", vo.getTitle());
				obj.put("content", vo.getContent());
				obj.put("writer", vo.getWriter());
				obj.put("wdate", vo.getWdate());
				jArray.add(obj);
			}
			
			JSONObject jObject=new JSONObject();
			jObject.put("count", dao.count(type, query));
			jObject.put("array", jArray);
			
			out.print(jObject);
			break;
		case "/bbs/list":
			request.setAttribute("pageName", "/bbs/list.jsp");
			dis.forward(request, response);
			break;
		case "/bbs/insert":
			request.setAttribute("pageName", "/bbs/insert.jsp");
			dis.forward(request, response);
			break;
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		BBSVO vo=new BBSVO();
		vo.setTitle(request.getParameter("title"));
		vo.setContent(request.getParameter("content"));
		vo.setWriter("batman");
		
		switch(request.getServletPath()) {
		case "/bbs/insert":
			System.out.println(vo.toString());
			dao.insert(vo);
			response.sendRedirect("/bbs/list");
			break;
		case "/bbs/update":
			int no=Integer.parseInt(request.getParameter("no"));
			vo.setNo(no);
			System.out.println(vo.toString());
			dao.update(vo);
			response.sendRedirect("/bbs/list");
			break;
		}
	}

}
