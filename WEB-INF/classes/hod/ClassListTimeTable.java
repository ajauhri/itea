/* ClassList Servlet (Component)
 * Working: 
 * 			Returns an <option> list of branches from the database. Extracts all the classes under a branch,
 * 			Return format is <option value="id">name session</option> where name is branch name.
 * Usage:
 * 			1. Provide the "branch_id" parameter to the request.
 * 			2. Either directly call from ajax (but this method is not recommended)
 * 			3. Use your servlet corresponding to the current jsp page. The servlet can use
 * 					request.getRequestDispatcher("ClassList").forward(request,response);
 * 				to forward the request to ClassList. ClassList will then return the ajax response.
 * 			4. Response should be included in a <select> element by using innerHTML.
 */

package hod;

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
 * Servlet implementation class ClassList
 */
public class ClassListTimeTable extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ClassListTimeTable() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String branch_id = request.getParameter("branch_id").toString();
		/* Check Login Status */
		request.getRequestDispatcher(
						"/components/CLS?user_type=hod&firstTime=true&returnType=redirect")
				.include(request, response);
		if (response.isCommitted())
			return; // CLS preempting
		/* Get connection */
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
		String query = "select id,session from pietons.class where branch_id=? and id not in(select class_id from pietons.allocation)";
		String query2 = "select name from pietons.branch where id=?";
		String output = "";
		try {
			PreparedStatement ps2 = conn.prepareStatement(query2,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ps2.setString(1, branch_id);
			ResultSet rs2 = ps2.executeQuery();
			PreparedStatement ps = conn.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ps.setString(1, branch_id);
			ResultSet rs = ps.executeQuery();
			if (rs2.next()) {
				while (rs.next()) {
					output += "<option value=\"" + rs.getString("id") + "\">"
							+ rs2.getString("name") + " - "
							+ rs.getString("session") + "</option>";
				}
			}
			rs.close();
			rs2.close();
			ps.close();
			ps2.close();
			PrintWriter out = response.getWriter();
			out.write(output);
			out.close();
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
