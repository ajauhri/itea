package components;

/* FacultyList Servlet (Component)
 * Working: 
 * 			Returns an <option> list of students from the database. Extracts either all the students in the college,
 * 			or all the students in a class. Return format is either <option value="gr_no">full name - facType (branch name)</option>
 * 			or just <option value="gr_no">full name</option>
 * Usage:
 * 			1. Optional, provide the "branch_id" and/or "fac_type" parameter to the request.
 * 			2. Either directly call from ajax (but this method is not recommended)
 * 			3. Use your servlet corresponding to the current jsp page. The servlet can use
 * 					request.getRequestDispatcher("FacultyList").forward(request,response);
 * 				to forward the request to FacultyList. FacultyList will then return the ajax response.
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
 * Servlet implementation class FacultyList
 */
public class SemList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SemList() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
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
		String query = "select session from pietons.class where class_id=?";
		PrintWriter out = response.getWriter();
		out.println("<select name=\"semester\">");
		String output = "";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);
			if (rs.next()) {
				int i = 1;
				int sems = rs.getInt("session");
				while (i <= sems) {
					output += "<option value=\"" + i + "\">Semester " + i
							+ "</option>";
					i++;
				}
				out.write(output);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}
		out.println("</select>");
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
