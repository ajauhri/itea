package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.BranchBean;
import beans.DBConnector;

/**
 * Servlet implementation class BranchRecords
 */
public class BranchRecords extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BranchRecords() {
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
			String query = "delete from pietons.branch where id=?";
			try {
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setString(1, request.getParameter("id"));
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
			int branch_id = Integer.parseInt(request.getParameter("branch_id"));
			String newContent = request.getParameter("newContent");
			String query = "update pietons.branch set " + field
					+ "=? where id=?";
			PreparedStatement ps;
			try {
				ps = conn.prepareStatement(query);
				ps.setString(1, newContent);
				ps.setInt(2, branch_id);
				ps.execute();
				return;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
				request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=form")).forward(request,response);
				return;
			}
		} else if (request.getParameter("q") != null
				&& request.getParameter("q").equals("duration")) {
			/* Ajax call for course duration */
			String query = "select duration from pietons.course where id=?";
			try {
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setInt(1, Integer
						.parseInt(request.getParameter("course_id")));
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					response.getWriter().write(rs.getInt("duration") + "");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
				request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=database")).forward(request,response);
				return;
			}
			return;
		} else if (request.getParameter("list") != null
				&& request.getParameter("list").equals("course")) {
			request.getRequestDispatcher("/components/CourseList").forward(
					request, response);
		} else if (request.getParameter("list") != null
				&& request.getParameter("list").equals("faculty")) {
			request.getRequestDispatcher(
					"/components/FacultyList?fac_type="
							+ request.getParameter("fac_type")).forward(
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
			/* Add New Branch */
			request.getRequestDispatcher(
					response.encodeURL("/admin/branchRecords.jsp?do=add"))
					.forward(request, response);
			return;
		} else if (request.getParameter("do") != null
				&& request.getParameter("do").equals("save")) {
			/* Save New Record */
			try {
				String query = "insert into pietons.branch(id, course_id, name, hod) values(default,?,?,?)";
				int branch_id = 0;
				PreparedStatement ps = conn.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				ps.setInt(1, Integer
						.parseInt(request.getParameter("course_id")));
				ps.setString(2, request.getParameter("name"));
//				if (request.getParameter("hod") != null
//						&& !request.getParameter("hod").equals("-1"))
//					ps.setString(3, request.getParameter("hod"));
//				else
					ps.setString(3, null);
				ps.execute();

				
				/* Get Id */
				String query0="select id from pietons.branch where course_id=? and name=? order by id desc";
				PreparedStatement ps0=conn.prepareStatement(query0);
				ps0.setInt(1,Integer.parseInt(request.getParameter("course_id")));
				ps0.setString(2,request.getParameter("name"));
				ResultSet rs0=ps0.executeQuery();
				rs0.next();
				branch_id=rs0.getInt("id");
				
				/* Generate records for classes */
				int sessionYearlyCount = Integer.parseInt(request
						.getParameter("sessionYearlyCount"));
				int duration = Integer.parseInt(request
						.getParameter("duration"));
				int sessionCount = sessionYearlyCount * duration;
				String query1 = "insert into pietons.class(id, branch_id, session) values(default,?,?)";
				PreparedStatement ps1 = null;
				for (int i = 1; i <= sessionCount; i++) {
					ps1 = conn.prepareStatement(query1);
					ps1.setInt(1, branch_id);
					ps1.setInt(2, i);
					ps1.execute();
				}
				
				request.getRequestDispatcher(
						response.encodeURL("/admin/branchRecords.jsp?do=save"))
						.forward(request, response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
				request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=database")).forward(request,response);
				return;
			}
			return;
		}
		/* Show list of branches */
		if (request.getParameter("start") == null
				|| request.getParameter("start").equals("")
				|| request.getParameter("perpage") == null
				|| request.getParameter("perpage").equals("")) {
			response.sendRedirect(response
					.encodeURL("/ITEA/admin/BranchRecords?start=0&perpage=10"));
			return;
		}

		/* Extract List of Branches */
		String order_by = "";
		if (request.getParameter("order_by") != null) {
			order_by = request.getParameter("order_by");
		} else
			order_by = "id";
		String query = "select id, course_id, name, hod from pietons.branch order by "
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
			/* Instantiate noOfRows Number of BranchBean Objects */
			BranchBean[] bb = new BranchBean[noOfRows];
			ResultSet rs = st.executeQuery(query);
			for (int i = 0; i < noOfRows; i++) {
				rs.next();

				bb[i] = new BranchBean();
				bb[i].setId(rs.getInt("id"));
				bb[i].setCourse_id(rs.getInt("course_id"));
				bb[i].setName(rs.getString("name"));
				bb[i].setHod(rs.getString("hod"));
			}

			request.setAttribute("bb", bb);
			request.setAttribute("bb_no", noOfRows);
			request.setAttribute("perpage", Integer.parseInt(request
					.getParameter("perpage").toString()));
			request.setAttribute("start", Integer.parseInt(request
					.getParameter("start").toString()));
			request.getRequestDispatcher(
					response.encodeURL("/admin/branchRecords.jsp")).forward(
					request, response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=database")).forward(request,response);
			return;
		}
	}
}
