package components;
/* BranchList Servlet (Component)
 * Working: 
 * 			Returns an <option> list of branches from the database. Extracts either all the branches under a course,
 * 			or all the branches under an hod. Return format is <option value="id">name</option>
 * Usage:
 * 			1. Optional, provide the "course_id" and/or "hod" (gr_no) parameters to the request.
 * 			2. Either directly call from ajax (but this method is not recommended)
 * 			3. Use your servlet corresponding to the current jsp page. The servlet can use
 * 					request.getRequestDispatcher("BranchList").forward(request,response);
 * 				to forward the request to BranchList. BranchList will then return the ajax response.
 * 			4. Response should be included in a <select> element by using innerHTML.
 */

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
 * Servlet implementation class BranchList
 */
public class BranchList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BranchList() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/* Get course id */
		String course_id = null;
		if (request.getParameter("course_id") != null)
			course_id = request.getParameter("course_id");
		String hod = null;
		if (request.getParameter("hod") != null)
			hod = request.getParameter("hod");
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
		try {
			String query = null;
			String output = "";
			PreparedStatement ps = null;
			if (hod == null && course_id == null) {
				query = "select id,name from pietons.branch";
				ps = conn.prepareStatement(query);
			} else if (hod == null && course_id != null) {
				query = "select id,name from pietons.branch where course_id=?";
				ps = conn.prepareStatement(query);
				ps.setString(1, course_id);
			} else if (hod != null && course_id == null) {
				query = "select id,name from pietons.branch where hod=?";
				ps = conn.prepareStatement(query);
				ps.setString(1, hod);
			} else if (hod != null && course_id != null) {
				query = "select id,name from pietons.branch where hod=? and course_id=?";
				ps = conn.prepareStatement(query);
				ps.setString(1, hod);
				ps.setString(2, course_id);
			}
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				output += "<option value=\"" + rs.getString("id") + "\">"
						+ rs.getString("name") + "</option>";
			}
			PrintWriter out = response.getWriter();
			out.write(output);
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
