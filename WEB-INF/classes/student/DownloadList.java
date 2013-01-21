package student;

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
import beans.DateBean;
import beans.ResourceBean;

public class DownloadList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DownloadList() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/* Check Login Status */
		request
				.getRequestDispatcher(
						"/components/CLS?user_type=student&returnType=redirect&firstTime=true")
				.include(request, response);
		if (response.isCommitted())
			return; // CLS preempt
		/* Show interface */
		request.getRequestDispatcher(response.encodeURL("download.jsp"))
				.forward(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/* Check Login Status */
		request
				.getRequestDispatcher(
						"/components/CLS?user_type=student&returnType=redirect&firstTime=true")
				.include(request, response);
		if (response.isCommitted())
			return; // CLS preempt
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
		String query = "select r.id as id, r.subject_id as subject_id, r.title as title, r.description as description, "
				+ " r.filename as filename, r.cl_filename as original, "
				+ " x.dd as dd, x.mm as mm, x.yyyy as yyyy "
				+ " from pietons.resources r, "
				+ " xmltable ( '$d/date' passing r.date as \"d\" "
				+ " columns dd varchar(2) path 'dd/text()', "
				+ "			mm varchar(2) path 'mm/text()', "
				+ "			yyyy varchar(4) path 'yyyy/text()') as x "
				+ " where subject_id = ? order by x.yyyy, x.mm, x.dd"; // order
		// by
		// date
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, request.getParameter("subject")); // TODO
			ResultSet rset = ps.executeQuery();

			int noOfRows = 0;
			/* Count no. of files */
			while (rset.next()) {
				noOfRows++;
			}
			if (noOfRows == 0) {
				request.getRequestDispatcher(
						response.encodeURL("download.jsp?show=nofiles"))
						.forward(request, response);
				return;
			}
			
			/* Re-query for filling beans */
			ps = conn.prepareStatement(query);
			ps.setString(1, request.getParameter("subject")); // TODO
			rset = ps.executeQuery();

			ResourceBean rb[] = new ResourceBean[noOfRows];
			for (int i = 0; rset.next(); i++) {
				/* Fill Beans */
				rb[i] = new ResourceBean();
				rb[i].setId(rset.getInt("id"));
				rb[i].setSubject_id(rset.getInt("subject_id"));
				rb[i].setTitle(rset.getString("title"));
				rb[i].setDescription(rset.getString("description"));
				rb[i].setFilename(rset.getString("filename"));
				rb[i].setCl_filename(rset.getString("original"));
				DateBean db = new DateBean();
				db.setDd(rset.getString("dd"));
				db.setMm(rset.getString("mm"));
				db.setYyyy(rset.getString("yyyy"));
				rb[i].setDate(db);
			}

			request.setAttribute("rb", rb);
			request.setAttribute("rb_no", noOfRows);
		} catch (SQLException e) {
			// TODO
			System.out.println("Sdfdsf: " + e.toString());
		}
		request.getRequestDispatcher(
				response.encodeURL("download.jsp?show=files")).forward(request,
				response);
	}
}
