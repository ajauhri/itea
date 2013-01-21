package faculty;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.DBConnector;

import com.oreilly.servlet.MultipartRequest;

public class Upload extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		/* Check Login Status */
		request
				.getRequestDispatcher(
						"/components/CLS?user_type=faculty&firstTime=true&returnType=redirect")
				.include(request, response);
		if (response.isCommitted())
			return; // CLS preempting
		/* Show upload page */
		request.getRequestDispatcher(response.encodeURL("upload.jsp?show=form")).forward(request,response);
	}
		
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/* Check Login Status */
		request
				.getRequestDispatcher(
						"/components/CLS?user_type=faculty&firstTime=true&returnType=redirect")
				.include(request, response);
		if (response.isCommitted())
			return; // CLS preempting
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
		String path = this.getServletContext().getRealPath(this.getServletInfo())+"/uploads/";
		PrintWriter out=response.getWriter();
		try {
			/* Use MultipartRequest to grab files */
			MultipartRequest multi = new MultipartRequest(request, path,
					200 * 1024 * 1024,
					new com.oreilly.servlet.multipart.DefaultFileRenamePolicy());
			Enumeration params = multi.getParameterNames();
			String title=request.getParameter("title");
			String description=request.getParameter("description");
			String subject_id=request.getParameter("subject_id");
			while (params.hasMoreElements()) {
				String name = (String) params.nextElement();
				String value = multi.getParameter(name);
				if (name.equals("title"))
					title = value;
				if (name.equals("description"))
					description = value;
				if (name.equals("subject_id"))
					subject_id = value;
			}
			Enumeration files = multi.getFileNames();
			while (files.hasMoreElements()) {
				/* Store file */
				String name = (String) files.nextElement();
				String filename = multi.getFilesystemName(name);
				String original = multi.getOriginalFileName(name);
				File f = multi.getFile(name);
				if (f != null) {
					/*  Update to Database */
					PreparedStatement pstmnt = null;
					Calendar cal = Calendar.getInstance();
					
					String query = "insert into pietons.resources "
							+ "values ( default , ? , ? , ? , ? , "
							+ "xmlserialize(content xmlelement(name \"date\", "
							+ "	xmlelement(name \"dd\", '"
							+ cal.get(Calendar.DATE) + "'), "
							+ "	xmlelement(name \"mm\", '"
							+ cal.get(Calendar.MONTH) + "'), "
							+ "	xmlelement(name \"yyyy\", '"
							+ cal.get(Calendar.YEAR) + "')) as clob(100)), ?)";
					String commit = "commit";
					try{
						pstmnt = conn.prepareStatement(query);

						pstmnt.setString(1, subject_id); // request.getParameter(
														// "subject_id")
						pstmnt.setString(2, title);
						pstmnt.setString(3, description);
						pstmnt.setString(4, filename);
						pstmnt.setString(5, original);
						pstmnt.execute();
						
						pstmnt = conn.prepareStatement(commit);
						pstmnt.execute();
					} catch (SQLException e) {
						// TODO
						System.out.println(e.toString());
					}
					/* Show confirmation */
					request.getRequestDispatcher(response.encodeURL("upload.jsp?show=conf&filename="+filename)).forward(request,response);
				} // if
			}// while
		} catch (Exception e) {
			// TODO
			e.printStackTrace(out);
		}
		return;
	}
}
