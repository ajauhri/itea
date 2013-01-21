package Reports;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import beans.DBConnector;


/**
 * Servlet implementation class CourseWise
 */
public class DefaultersList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DefaultersList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
			ServletOutputStream servletOutputStream = response.getOutputStream();
			try
			{
				int classid=Integer.parseInt(request.getParameter("class_id"));
				String path = this.getServletContext().getRealPath(this.getServletInfo())+"/Reports/";
				JasperReport jasperReport= (JasperReport)JasperCompileManager.compileReport(path+"Attendance.jrxml");
				/* Get connection */
				HttpSession session = request.getSession();
				Connection conn = null;
				synchronized (session) 
				{
					DBConnector dbcon = (DBConnector) session.getAttribute("dbcon"); // fetch an existing instance
					if (dbcon == null)// if none found, create a new one 
					{ 
						dbcon = new DBConnector();
						session.setAttribute("dbcon", dbcon);
					}
					conn = dbcon.getConnection();
				}
				JasperPrint jasperPrint= new JasperPrint();
				PreparedStatement ps =conn.prepareStatement("select s.roll_no as rollno,co.name || ' ' || br.name as stuff,cl.session as session,su.name as subject ,att.attendance as attendance ,su.lectures_held as lecturesheld, x.fname || ' ' || x.lname as name " 
															+"from pietons.student s ,pietons.subject su, pietons.attendance att,pietons.class cl,pietons.branch br,pietons.course co," 
															+"xmltable('$p/personal_details/name' passing s.personal_details as \"p\" "
															+"columns fname varchar(30) path 'fname/text()',"
															+"lname varchar(30) path 'lname/text()') as x " 
															+"where co.id=br.course_id and br.id=cl.branch_id and cl.id=? and att.subject_id=su.id and s.gr_no=att.student_id and ((att.attendance)<(0.6*su.lectures_held))");
				ps.setInt(1,classid);
				ResultSet rs=ps.executeQuery();
				JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(rs);
				jasperPrint= JasperFillManager.fillReport(jasperReport,new HashMap(), resultSetDataSource);
				JasperExportManager.exportReportToPdfStream(jasperPrint,servletOutputStream);
			}
			catch(Exception e)
			{
				request.getRequestDispatcher(response.encodeURL("error.jsp?type=server")).forward(request,response);
				return;
			}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
