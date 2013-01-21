package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.DBConnector;

/**
 * Servlet implementation class ClassRecords
 */
public class ClassRecords extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClassRecords() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Check Login Status */
		request.getRequestDispatcher("/components/CLS?user_type=admin&returnType=redirect").include(request,response);
		if(response.isCommitted()) return;	// CLS preempting
		/* Get connection */
		HttpSession session = request.getSession();
		Connection conn = null;
		synchronized (session) {
			DBConnector dbcon = (DBConnector) session.getAttribute("dbcon"); // fetch
			// an
			// existing
			// instance
			if (dbcon == null) { // if none found, create a new one
				dbcon = new DBConnector();
				session.setAttribute("dbcon", dbcon);
			}
			conn = dbcon.getConnection();
		}
		// Determine if ajax or ordinary hyperlink
		if (request.getParameter("ajax") == null
				|| request.getParameter("ajax").equals("")) {
			doPost(request, response);
			return;
		} else if(request.getParameter("q")!=null && request.getParameter("q").equals("duration")){
			/* Ajax call for course duration */
			String query="select duration from pietons.course where id=?";
			try {
				PreparedStatement ps=conn.prepareStatement(query);
				ps.setInt(1,Integer.parseInt(request.getParameter("course_id")));
				ResultSet rs=ps.executeQuery();
				if(rs.next()){
					response.getWriter().write(rs.getInt("duration")+"");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
			}
			return;
		} else if(request.getParameter("q")!=null && request.getParameter("q").equals("check")){
			/* Ajax call for checking if classes already exist */
			String query="select * from pietons.class where branch_id=?";
			try {
				PreparedStatement ps=conn.prepareStatement(query);
				ps.setInt(1,Integer.parseInt(request.getParameter("branch_id")));
				ResultSet rs=ps.executeQuery();
				if(rs.next())
					response.getWriter().write("generated");
				else
					response.getWriter().write("notGenerated");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
			}
			return;
		} else if (request.getParameter("list") != null
				&& request.getParameter("list").equals("course")) {
			request.getRequestDispatcher("/components/CourseList").forward(
					request, response);
			return;
		} else if (request.getParameter("list") != null
				&& request.getParameter("list").equals("branch")) {
			request.getRequestDispatcher(
					"/components/BranchList?course_id="
							+ request.getParameter("course_id")).forward(
					request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Check Login Status */
		request.getRequestDispatcher("/components/CLS?user_type=admin&returnType=redirect").include(request,response);
		if(response.isCommitted()) return;	// CLS preempting
		/* Get connection */
		HttpSession session = request.getSession();
		Connection conn = null;
		synchronized (session) {
			DBConnector dbcon = (DBConnector) session.getAttribute("dbcon"); // fetch
			// an
			// existing
			// instance
			if (dbcon == null) { // if none found, create a new one
				dbcon = new DBConnector();
				session.setAttribute("dbcon", dbcon);
			}
			conn = dbcon.getConnection();
		}
		if(request.getParameter("do")!=null && request.getParameter("do").equals("generate")){
			/* Generate records for classes */
			int sessionYearlyCount=Integer.parseInt(request.getParameter("sessionYearlyCount"));
			int duration=Integer.parseInt(request.getParameter("duration"));
			int sessionCount=sessionYearlyCount*duration;
			int branch_id=Integer.parseInt(request.getParameter("branch_id"));
			String query="insert into pietons.class(id, branch_id, session) values(default,?,?)";
			PreparedStatement ps=null;
			try {
				for(int i=1;i<=sessionCount;i++){
					ps=conn.prepareStatement(query);
					ps.setInt(1,branch_id);
					ps.setInt(2,i);
					ps.execute();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
			}
			request.getRequestDispatcher(response.encodeURL("/admin/classRecords.jsp")).forward(request,response);
		}else{
			/* Show main page */
			request.getRequestDispatcher(response.encodeURL("/admin/classRecords.jsp")).forward(request,response);
		}
	}

}
