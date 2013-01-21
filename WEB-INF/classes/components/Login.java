package components;

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
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
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
		/* Check login status */
		request.getRequestDispatcher(response.encodeURL("/components/CLS?returnType=status")).include(
				request, response);
		if ((Boolean) request.getAttribute("status")) {
			/* User logged in */
			// redirect to home
			request
					.getRequestDispatcher(
							response.encodeURL("/components/Home")).forward(
							request, response);
			return;
		}
		request.removeAttribute("status");
		/* Else - login new user */
		/* Gather login credentials */
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		/* Convert password to md5 hash */
		password = MD5Hash.generateMD(password);
		/* Fetch from database */
		String user_type = null;
		String query = "select user_type, theme, gr_no, changed from pietons.users where username=? and password=?";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			/* User is authentic */
			if (rs.next()) {
				user_type = rs.getString("user_type");
				/* Set session */
				session.setAttribute("username", username);
				session.setAttribute("user_type", user_type);
				session.setAttribute("theme", rs.getInt("theme"));
				session.setAttribute("user_id", rs.getString("gr_no"));
				session.setAttribute("changed", rs.getInt("changed"));
				/* User specific attributes */
				if (user_type.equals("student")) {
					/* Student specific attributes */
					String query1 = "select s.class_id as class_id, c.branch_id as branch_id, b.course_id as course_id"
							+ " from pietons.student s, pietons.class c, pietons.branch b"
							+ " where s.gr_no=? and s.class_id=c.id and c.branch_id=b.id";
					PreparedStatement ps1 = conn.prepareStatement(query1);
					ps1.setString(1, rs.getString("gr_no"));
					ResultSet rs1 = ps1.executeQuery();
					if (rs1.next()) {
						session
								.setAttribute("class_id", rs1
										.getInt("class_id"));
						session.setAttribute("branch_id", rs1
								.getInt("branch_id"));
						session.setAttribute("course_id", rs1
								.getInt("course_id"));
					}
				} else if (user_type.equals("admin")) {
					/* Data Manager specific attributes */
				} else {
					/* Faculty/hod/dean specific attributes */
					String query1 = "select branch_id, fac_type from pietons.faculty where gr_no=?";
					PreparedStatement ps1 = conn.prepareStatement(query1);
					ps1.setString(1, rs.getString("gr_no"));
					ResultSet rs1 = ps1.executeQuery();
					if (rs1.next()) {
						session.setAttribute("branch_id", rs1
								.getInt("branch_id"));
						session
								.setAttribute("fac_type", rs1
										.getInt("fac_type"));
					}
				}
				/* Redirect to specific home */
				response.sendRedirect(response.encodeURL("/ITEA/components/Home"));
			} else
				/* User is not authentic */
				response.sendRedirect("/ITEA/components/Home?error=true");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}catch (Exception e){
			System.out.println(e.toString());
		}
	}

}
