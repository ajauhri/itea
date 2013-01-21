package components;
/*Usage: Specific to ExaminationAdmin.java as it states the whether an allotment of a particular subject has been made or not.
 * 
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
 * Servlet implementation class SubjectList
 */
public class SubjectList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SubjectList() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		/* Get connection */
		Connection conn = null;
		HttpSession session = request.getSession();
		synchronized (session) {
			DBConnector dbcon = (DBConnector) session.getAttribute("dbcon");
			if (dbcon == null) {
				dbcon = new DBConnector();
				session.setAttribute("dbcon", dbcon);
			}
			conn = dbcon.getConnection();
		}
		try {
			String query1 = "";
			String query2 = "";
			int class_id = Integer.parseInt(request.getParameter("class_id"));
			String output = "";
			PreparedStatement ps1 = null;
			query1 = "select id, name from pietons.subject where class_id=?";
			ps1 = conn.prepareStatement(query1,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ps1.setInt(1, class_id);
			ResultSet rs1 = ps1.executeQuery();

			PreparedStatement ps2 = null;
			ResultSet rs2 = null;
			String name = null;
			int id = 0;

			while (rs1.next()) {
				id = rs1.getInt("id");
				name = rs1.getString("name");
				query2 = "select subject_id from pietons.examination where subject_id=?";
				ps2 = conn.prepareStatement(query2,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				ps2.setInt(1, id);
				rs2 = ps2.executeQuery();
				if (rs2.next()) {
					output += "<option value=\"-1\" class='error' style='display:block'>"
							+ name
							+ " - Allotment Done"
							+ "</option>";
				} else
					output += "<option value=\"" + id + "\">" + name
							+ "</option>";
//				rs2.close();
			}
			rs1.close();
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
		// TODO Auto-generated method stub
		super.doPost(request, response);
	}

}
