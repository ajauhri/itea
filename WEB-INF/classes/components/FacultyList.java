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
public class FacultyList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FacultyList() {
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

		String query = "select m.gr_no as gr_no, m.branch_id as branch_id, m.fac_type as fac_type,"
				+ " x.fname as fname, x.mname as mname, x.lname as lname"
				+ " from pietons.faculty m,"
				+ " xmltable('$c/personal_details/name' passing m.personal_details as \"c\""
				+ " columns fname varchar(30) path 'fname/text()',"
				+ "			mname varchar(30) path 'mname/text()',"
				+ "			lname varchar(30) path 'lname/text()') as x";
		String query2 = query + " where m.branch_id=?";
		String query3 = query + " where m.fac_type=?";
		String query4 = "select name from pietons.branch where id=?";
		String output = "";
		PreparedStatement ps = null,ps2=null;
		try {
			if (request.getParameter("branch_id") == null
					&& request.getParameter("fac_type") == null) {
				ps = conn.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			} else if (request.getParameter("fac_type") == null) {
				ps = conn.prepareStatement(query2,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				ps.setInt(1, Integer.parseInt(request.getParameter("branch_id")));
			} else if (request.getParameter("branch_id") == null) {
				ps = conn.prepareStatement(query3,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				ps.setInt(1, Integer.parseInt(request.getParameter("fac_type")));
			}
			ResultSet rs = ps.executeQuery();
			ResultSet rs2=null;
			String fac_type = null;
			while (rs.next()) {
				switch (rs.getInt("fac_type")) {
				case 0:
					fac_type = "Internal Faculty";
					break;
				case 1:
					fac_type = "Visiting Faculty";
					break;
				case 2:
					fac_type = "H.O.D.";
					break;
				case 3:
					fac_type = "Dean";
					break;
				}
				ps2 = conn.prepareStatement(query4,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				ps2.setInt(1, Integer.parseInt(rs.getString("branch_id")));
				rs2 = ps2.executeQuery();
				rs2.next();
				output += "<option value=\"" + rs.getString("gr_no") + "\">"
						+ rs.getString("fname") + " " + (rs.getString("mname")==null?"":rs.getString("mname"))
						+ " " + rs.getString("lname") + " - " + fac_type + " ("
						+ rs2.getString("name") + ")</option>";
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
