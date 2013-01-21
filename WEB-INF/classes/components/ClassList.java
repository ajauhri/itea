package components;
/* ClassList Servlet (Component)
 * Working: 
 * 			Returns an <option> list of branches from the database. Extracts all the classes under a branch,
 * 			Return format is <option value="id">name session</option> where name is branch name.
 * Usage:
 * 			1. Optional, provide the "branch_id" parameter to the request.
 * 			2. Either directly call from ajax (but this method is not recommended)
 * 			3. Use your servlet corresponding to the current jsp page. The servlet can use
 * 					request.getRequestDispatcher("ClassList").forward(request,response);
 * 				to forward the request to ClassList. ClassList will then return the ajax response.
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
 * Servlet implementation class ClassList
 */
public class ClassList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ClassList() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/* Get parameter */
		String branch_id = request.getParameter("branch_id");
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
		String query = "select l.id as id,l.session as session,b.name as name" +
				" from pietons.class l, pietons.branch b " +
				" where l.branch_id=b.id";
		if(branch_id!=null) query+=" and b.id=?";
		String output = "";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			if(branch_id!=null)	ps.setString(1, branch_id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
					output += "<option value=\"" + rs.getString("id") + "\">"
							+ rs.getString("name") + " - "
							+ rs.getString("session") + "</option>";
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
