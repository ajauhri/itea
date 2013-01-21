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
import beans.TranscriptBean;

/**
 * Servlet implementation class Transcripts
 */
public class Transcripts extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Transcripts() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (request.getParameter("ajax") == null) {
			/* Ordinary non-ajax call */
			doPost(request, response);
			return;
		} else if (request.getParameter("list") != null
				&& request.getParameter("list").equals("class")) {
			/* Ajax call for list of classes */
			request.getRequestDispatcher(
					"/components/ClassList?branch_id="
							+ session.getAttribute("branch_id").toString())
					.forward(request, response);
			return;
		}
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
		if (request.getParameter("do") == null) {
			request.getRequestDispatcher(
					response.encodeURL("/student/transcripts.jsp")).forward(
					request, response);
			return;
		} else if (request.getParameter("do").equals("get")) {
			String query = "select session from pietons.class where id=?";
			String query1 = "select s.name as subject_name, m.marks as marks"
					+ " from pietons.marks m , pietons.class c, pietons.examination e, pietons.subject s"
					+ " where c.id=? and c.branch_id=? and m.student_id=?"
					+ " and c.id = s.class_id and s.id = e.subject_id and e.id = m.examination_id";
			try {
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setInt(1, Integer.parseInt(request.getParameter("class_id")));
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					request.setAttribute("session",rs.getInt("session"));
				}
				
				PreparedStatement ps1 = conn.prepareStatement(query1);
				ps1.setInt(1, Integer.parseInt(request.getParameter("class_id")));
				ps1.setInt(2, (Integer) session.getAttribute("branch_id"));
				ps1.setString(3, session.getAttribute("user_id").toString());
				
				/* Find No. of Records */
/*				String path=this.getServletContext().getRealPath(this.getServletInfo())+"/student/";
				ServletOutputStream servletOutputStream = response.getOutputStream();
				
				try{
					JasperReport jasperReport= (JasperReport)JasperCompileManager.compileReport(path+"Transcript.jrxml");
					JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(rs);
					JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport,new HashMap(), resultSetDataSource);
					JasperExportManager.exportReportToPdfStream(jasperPrint,servletOutputStream);
				}
				catch(Exception e)
				{
					System.out.println(e.toString());
				}
*/						
				/*Zubeen has temporarily removed this Restore if jasper reports do not work
*/
				int noOfRows=0;
				ResultSet rschk=ps1.executeQuery();
				while (rschk.next()) {
					noOfRows++;
				}
				// Instantiate noOfRows Number of StudentBean Objects 
				TranscriptBean[] tb = new TranscriptBean[noOfRows];
				ResultSet rs1 = ps1.executeQuery();
				for (int i = 0; i < noOfRows; i++) {
					rs1.next();
					
					tb[i]=new TranscriptBean();
					tb[i].setSubject(rs1.getString("subject_name"));
					tb[i].setMarks(rs1.getInt("marks"));
				}
				
				request.setAttribute("tb", tb);
				request.setAttribute("tb_no", noOfRows);
				request.getRequestDispatcher(
						response.encodeURL("/student/transcripts.jsp?do=show")).forward(request,
						response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
			}
		}
	}

}
