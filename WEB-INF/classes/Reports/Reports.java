package Reports;




import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Reports
 */
public class Reports extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Reports() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(request.getParameter("q")==null){
			doPost(request, response);
		}else if(request.getParameter("q").equals("course")){
			/* Ajax request has been made -- q parameter is passed */
			request.getRequestDispatcher("/components/CourseList").forward(request,response);
		}else if(request.getParameter("q").equals("branch")){
			/* Ajax request has been made -- q parameter is passed */
			request.getRequestDispatcher("/components/BranchList?course_id="+request.getParameter("course_id")).forward(request,response);
		}else if(request.getParameter("q").equals("class")){
			/* Ajax request has been made -- q parameter is passed */
			request.getRequestDispatcher("/components/ClassList?branch_id="+request.getParameter("branch_id")).forward(request,response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session=request.getSession();
		if(session.getAttribute("user_type").toString().equals("dean")){
			request.getRequestDispatcher(response.encodeURL("../dean/reports.jsp")).forward(request,response);
			return;
		}
		if(session.getAttribute("user_type").toString().equals("hod")){
			request.getRequestDispatcher(response.encodeURL("../hod/reports.jsp")).forward(request,response);
			return;
		}
	}
}
