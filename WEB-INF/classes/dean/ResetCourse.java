package dean;

import java.io.IOException;
import java.io.PrintWriter;
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
 * Servlet implementation class ResetCourse
 */
public class ResetCourse extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ResetCourse() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("q") == null) {
			/* Show interface */
			request.getRequestDispatcher(response.encodeURL("resetCourse.jsp"))
					.forward(request, response);
		} else {
			/* Ajax call for the works */
			doPost(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		/* Check Login Status */
		request
				.getRequestDispatcher(
						"/components/CLS?user_type=dean&firstTime=true&returnType=redirect")
				.include(request, response);
		if (response.isCommitted())
			return; // CLS preempting

		HttpSession session = request.getSession();
		Connection conn = null;
		synchronized (session) {
			DBConnector dbcon = (DBConnector) session.getAttribute("dbcon");
			if (dbcon == null) {
				dbcon = new DBConnector();
				session.setAttribute("dbcon", dbcon);
			}
			conn = dbcon.getConnection();
		}
		try {
			PrintWriter out = response.getWriter();
			// String gr = session.getAttribute("user_id").toString();
			int course_id = Integer.parseInt(request.getParameter("q"));
			
			/* Promote every student to next session */
			// Get max session for each branch
			String query="select max(l.id) as maxSession, b.id as branch_id " +
					"from pietons.class l, pietons.branch b, pietons.course c " +
					"where l.branch_id=b.id and b.course_id=c.id and c.id=? " +
					"group by b.id";
			PreparedStatement ps=conn.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1,course_id);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				promoteStudents((new DBConnector()).getConnection(), rs.getInt("branch_id"), rs.getInt("maxSession"));
			}
			
			/* Clear out allocation (time table) */
			query="delete from pietons.allocation where class_id in " +
				"(select id from pietons.class where branch_id in " +
				"(select id from pietons.branch where course_id=?))";
			ps=conn.prepareStatement(query);
			ps.setInt(1,course_id);
			ps.execute();
			
			/* Clear out lectures held */
			query="update pietons.subject set lectures_held=0 where class_id in " +
				"(select id from pietons.class where branch_id in " +
				"(select id from pietons.branch where course_id=?))";
			ps=conn.prepareStatement(query);
			ps.setInt(1,course_id);
			ps.execute();
			
			/* Clear out attendance */
			query="delete from pietons.attendance where subject_id in " +
				"(select id from pietons.subject where class_id in " +
				"(select id from pietons.class where branch_id in " +
				"(select id from pietons.branch where course_id=?)))";
			ps=conn.prepareStatement(query);
			ps.setInt(1,course_id);
			ps.execute();
			
			/* Clear out attendance_marked */
			query="delete from pietons.attendance_marked where subject_id in " +
				"(select id from pietons.subject where class_id in " +
				"(select id from pietons.class where branch_id in " +
				"(select id from pietons.branch where course_id=?)))";
			ps=conn.prepareStatement(query);
			ps.setInt(1,course_id);
			ps.execute();
			
			/* Clear out resources/uploads */
			query="delete from pietons.resources where subject_id in " +
				"(select id from pietons.subject where class_id in " +
				"(select id from pietons.class where branch_id in " +
				"(select id from pietons.branch where course_id=?)))";
			ps=conn.prepareStatement(query);
			ps.setInt(1,course_id);
			ps.execute();
			
			out.println("Transaction Complete");
		} catch (SQLException e) {
			System.out.println(e.toString());
			request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=database")).forward(request,response);
			return;
		}

	}

	private void promoteStudents(Connection conn, int branch_id, int maxSession)
			throws SQLException {
		String query1="update pietons.student set class_id=class_id+1 where class_id in " +
		"(select id from pietons.class where branch_id=?) and class_id<?";
		PreparedStatement ps1=conn.prepareStatement(query1,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		ps1.setInt(1,branch_id);
		ps1.setInt(2,maxSession);
		ps1.execute();
		
		query1="update pietons.student set class_id=-class_id where class_id in " +
		"(select id from pietons.class where branch_id=?) and class_id=?";
		ps1=conn.prepareStatement(query1,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		ps1.setInt(1,branch_id);
		ps1.setInt(2,maxSession);
		ps1.execute();
		ps1.close();
	}

}
