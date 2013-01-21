package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.DBConnector;
import beans.SubjectBean;

/**
 * Servlet implementation class SubjectRecords
 */
public class SubjectRecords extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SubjectRecords() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/* Check Login Status */
		request.getRequestDispatcher(
				"/components/CLS?user_type=admin&returnType=redirect").include(
				request, response);
		if (response.isCommitted())
			return; // CLS preempting
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
		} else if (request.getParameter("do") != null
				&& request.getParameter("do").equals("delete")) {
			/* Delete a Record */
			String query = "update pietons.subject set valid=? where id=?";
			try {
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setInt(1, 0);
				ps.setString(2, request.getParameter("id"));
				ps.execute();
				return;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
				request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=database")).forward(request,response);
				return;
			}
		} else if (request.getParameter("do") != null
				&& request.getParameter("do").equals("restore")) {
			/* Restore a Record */
			String query = "update pietons.subject set valid=? where id=?";
			try {
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setInt(1, 1);
				ps.setString(2, request.getParameter("id"));
				ps.execute();
				return;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
				request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=database")).forward(request,response);
				return;
			}
		} else if (request.getParameter("do") != null
				&& request.getParameter("do").equals("edit")) {
			/* Edit a Record */
			String field = request.getParameter("field");
			int subject_id = Integer.parseInt(request
					.getParameter("subject_id"));
			String newContent = request.getParameter("newContent");
			if(field.equals("labs_available")){
				String labs_available = "<labs_available>";
				StringTokenizer st = new StringTokenizer(newContent, ",");
				while(st.hasMoreTokens()) {
					labs_available += "<lab id=\"" + Integer.parseInt(st.nextToken()) + "\"/>";
				}
				labs_available += "</labs_available>";
				newContent=labs_available;
			}
			String query = "update pietons.subject set " + field
					+ "=? where id=?";
			PreparedStatement ps;
			try {
				ps = conn.prepareStatement(query);
				ps.setString(1, newContent);
				ps.setInt(2, subject_id);
				ps.execute();
				return;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
				request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=form")).forward(request,response);
				return;
			}
		} else if (request.getParameter("list") != null
				&& request.getParameter("list").equals("course")) {
			request.getRequestDispatcher("/components/CourseList").forward(
					request, response);
		} else if (request.getParameter("list") != null
				&& request.getParameter("list").equals("branch")) {
			request.getRequestDispatcher(
					"/components/BranchList?course_id="
							+ request.getParameter("course_id")).forward(
					request, response);
		} else if (request.getParameter("list") != null
				&& request.getParameter("list").equals("class")) {
			request.getRequestDispatcher(
					"/components/ClassList?branch_id="
							+ request.getParameter("branch_id")).forward(
					request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/* Check Login Status */
		request.getRequestDispatcher(
				"/components/CLS?user_type=admin&returnType=redirect").include(
				request, response);
		if (response.isCommitted())
			return; // CLS preempting
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
		if (request.getParameter("do") != null
				&& request.getParameter("do").equals("add")) {
			/* Add New Subject */
			request.getRequestDispatcher(
					response.encodeURL("/admin/subjectRecords.jsp?do=add"))
					.forward(request, response);
			return;
		} else if (request.getParameter("do") != null
				&& request.getParameter("do").equals("save")) {
			/* Save New Record */
			
			String query = "insert into pietons.subject(class_id, name) values(?,?)";
			try {
				PreparedStatement ps = conn.prepareStatement(query);
				ps
						.setInt(1, Integer.parseInt(request
								.getParameter("class_id")));
				ps.setString(2, request.getParameter("name"));
				ps.execute();
				request
						.getRequestDispatcher(
								response
										.encodeURL("/admin/subjectRecords.jsp?do=save"))
						.forward(request, response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
				request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=form")).forward(request,response);
				return;
			}
			return;
		}
		/* Show list of subjects */
		if (request.getParameter("start") == null
				|| request.getParameter("start").equals("")
				|| request.getParameter("perpage") == null
				|| request.getParameter("perpage").equals("")) {
			response
					.sendRedirect(response
							.encodeURL("/ITEA/admin/SubjectRecords?start=0&perpage=10"));
			return;
		}
		/* Extract List of Classes */
		String order_by = "";
		if (request.getParameter("order_by") != null) {
			order_by = request.getParameter("order_by");
		} else
			order_by = "id";
		String query = "select id, class_id, name, lectures_held, valid"
				+ " from pietons.subject order by "
				+ order_by;
		Statement st;
		int noOfRows = 0;
		try {
			st = conn.createStatement();
			/* Find No. of Records */
			ResultSet rschk = st.executeQuery(query);
			while (rschk.next()) {
				noOfRows++;
			}
			/* Instantiate noOfRows Number of SubjectBean Objects */
			SubjectBean[] sb = new SubjectBean[noOfRows];
			ResultSet rs = st.executeQuery(query);
			for (int i = 0; i < noOfRows; i++) {
				rs.next();

				sb[i] = new SubjectBean();
				sb[i].setId(rs.getInt("id"));
				sb[i].setClass_id(rs.getInt("class_id"));
				sb[i].setName(rs.getString("name"));
				sb[i].setLectures_held(rs.getInt("lectures_held"));				
				sb[i].setValid(rs.getInt("valid"));
			}

			request.setAttribute("sb", sb);
			request.setAttribute("sb_no", noOfRows);
			request.setAttribute("perpage", Integer.parseInt(request
					.getParameter("perpage").toString()));
			request.setAttribute("start", Integer.parseInt(request
					.getParameter("start").toString()));
			request.getRequestDispatcher(
					response.encodeURL("/admin/subjectRecords.jsp")).forward(
					request, response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=database")).forward(request,response);
			return;
		}
	}
}
