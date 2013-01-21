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
public class CourseWise extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CourseWise() {
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
				int courseid=Integer.parseInt(request.getParameter("course_id"));
				String path = this.getServletContext().getRealPath(this.getServletInfo())+"/Reports/";
				JasperReport jasperReport= (JasperReport)JasperCompileManager.compileReport(path+"CourseWise.jrxml");
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
				PreparedStatement ps =conn.prepareStatement("select distinct su.lectures_held as lecturesheld,T.fname ||' '|| T.lname as faculty,br.name as branch,su.name as subject ,cl.session as session " 
							+"from pietons.course co,pietons.branch br,pietons.class cl,pietons.subject su,"
							+"pietons.faculty fa,xmltable('$e/personal_details/name' passing fa.personal_details as \"e\" columns fname varchar(10) path 'fname', lname varchar(10) path 'lname') as T,"
							+"pietons.allocation B,xmltable('$d/schedule/day/slot' passing B.schedule as \"d\" columns d_id varchar(2) path '../@d_id', no varchar(2) path '@no', subject_id integer path 'subject_id', teacher_id varchar(10) path 'teacher_id', infra_id integer path 'infra_id') as A "  
							+"where co.id=? and br.id=cl.branch_id and cl.id=su.class_id and co.id=br.course_id and cl.id=B.class_id and A.teacher_id=fa.gr_no and A.subject_id=su.id");
				ps.setInt(1,courseid);
				ResultSet rs=ps.executeQuery();
				JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(rs);
				jasperPrint= JasperFillManager.fillReport(jasperReport,new HashMap(), resultSetDataSource);
				JasperExportManager.exportReportToPdfStream(jasperPrint,servletOutputStream);
				
				
			}
			catch(Exception e)
			{
				System.out.println(e.toString());
//				request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=server")).forward(request,response);
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

