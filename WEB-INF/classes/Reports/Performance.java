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
public class Performance extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Performance() {
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
				JasperReport jasperReport= (JasperReport)JasperCompileManager.compileReport(path+"Performance.jrxml");
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
				PreparedStatement ps =conn.prepareStatement("select su.name as subject, sum(m.marks) as tot, max(m.marks) as max, min(m.marks) as min, avg(m.marks) as avg "  
															+"from pietons.examination e,pietons.subject su,pietons.marks m "
															+"where e.subject_id=su.id and m.examination_id=e.id and su.class_id=?" 
															+"group by su.name");
				ps.setInt(1,classid);
				ResultSet rs=ps.executeQuery();
				if(rs!=null)
				{
					//request.getRequestDispatcher(response.encodeURL("../hod/reports.jsp")).forward(request,response);
					//return;
					JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(rs);
					jasperPrint= JasperFillManager.fillReport(jasperReport,new HashMap(), resultSetDataSource);
					JasperExportManager.exportReportToPdfStream(jasperPrint,servletOutputStream);
				}
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
