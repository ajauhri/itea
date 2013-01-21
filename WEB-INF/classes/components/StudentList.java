package components;
/* StudentList Servlet (Component)
 * Working: 
 * 			Returns an <option> list of students from the database. Extracts either all the students in the college,
 * 			or all the students in a class. Return format is <option value="gr_no">full name (roll no)</option>
 * Usage:
 * 			1. Optional, provide the "class_id" parameter to the request.
 * 			2. Either directly call from ajax (but this method is not recommended)
 * 			3. Use your servlet corresponding to the current jsp page. The servlet can use
 * 					request.getRequestDispatcher("StudentList").forward(request,response);
 * 				to forward the request to StudentList. StudentList will then return the ajax response.
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
 * Servlet implementation class StudentList
 */
public class StudentList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StudentList() {
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
		String query = "select m.gr_no as gr_no,m.roll_no as roll_no,m.class_id as class_id,"
				+ " x.fname as fname, x.mname as mname, x.lname as lname"
				+ " from pietons.student as m,"
				+ " xmltable('$c/personal_details/name' passing m.personal_details as \"c\""
				+ " columns fname varchar(30) path 'fname/text()',"
				+ "			mname varchar(30) path 'mname/text()',"
				+ "			lname varchar(30) path 'lname/text()') as x";
		String query2 = query + " where m.class_id=?";
		String output = "";
		PreparedStatement ps = null;
		try {
			if (request.getParameter("class_id") == null) {
				ps = conn.prepareStatement(query);
			} else {
				ps = conn.prepareStatement(query2);
				ps.setString(1, request.getParameter("class_id").toString());
			}
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				output += "<option value=\"" + rs.getString("gr_no") + "\">"
						+ rs.getString("fname") + " " + (rs.getString("mname")==null?"":rs.getString("mname"))
						+ " " + rs.getString("lname") + " ("
						+ rs.getString("roll_no") + ")</option>";
			}
			PrintWriter out=response.getWriter();
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
