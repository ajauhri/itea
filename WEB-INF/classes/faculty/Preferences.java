package faculty;

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
 * Servlet implementation class Preferences
 */
public class Preferences extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Preferences() {
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
		/* Check Login Status */
		request
				.getRequestDispatcher(
						response
								.encodeURL("/components/CLS?firstTime=true&user_type=faculty&returnType=redirect"))
				.include(request, response);
		if (response.isCommitted())
			return; // CLS preempting
		/* Save Preference */
		if (request.getParameter("d_id") != null) {
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
			/* Get original xml */
			String xml = "";
			try {
				String query = "select xml2clob(preferences) as xml from pietons.faculty where gr_no=?";
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setString(1,session.getAttribute("user_id").toString());
				ResultSet rs = ps.executeQuery();
				rs.next();
				xml = rs.getString("xml");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
			}
			/* Check if xml is filled */
			if (xml.indexOf("</preferences>") == -1) { // not filled
				xml = "<preferences><day d_id=\""
						+ request.getParameter("d_id") + "\"><slot no=\""
						+ request.getParameter("slot_no")
						+ "\"/></day></preferences>";
			} else { // filled
				/* Check if day exists */
				if (xml
						.indexOf("d_id=\"" + request.getParameter("d_id")
								+ "\"") == -1) { // no
					xml = xml.substring(0, xml.indexOf("</preferences>"));
					xml += "<day d_id=\"" + request.getParameter("d_id")
							+ "\"><slot no=\""
							+ request.getParameter("slot_no")
							+ "\"/></day></preferences>";
				} else { // yes
					String temp = xml.substring(xml.indexOf("d_id=\""
							+ request.getParameter("d_id") + "\""), xml
							.length());
					xml = xml.substring(0, xml.indexOf("d_id=\""
							+ request.getParameter("d_id") + "\"")
							+ temp.indexOf("</day>"))
							+ "<slot no=\""
							+ request.getParameter("slot_no")
							+ "\"/>"
							+ xml.substring(xml.indexOf("d_id=\""
									+ request.getParameter("d_id") + "\"")
									+ temp.indexOf("</day>"), xml.length());
				}
			}
			/* Update xml field */
			String query1="update pietons.faculty set preferences=? where gr_no=?";
			try {
				PreparedStatement ps1=conn.prepareStatement(query1);
				ps1.setString(1,xml);
				ps1.setString(2,session.getAttribute("user_id").toString());
				ps1.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
			}
			request.getRequestDispatcher(response.encodeURL("/faculty/preferences.jsp?interface=save")).forward(request,response);
		}else{
			/* Ordinary request */
			request.getRequestDispatcher(response.encodeURL("/faculty/preferences.jsp")).forward(request,response);
		}
	}
}
