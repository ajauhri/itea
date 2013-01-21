package Reports;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
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
 * Servlet implementation class Daily
 */
public class Daily extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Daily() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ServletOutputStream servletOutputStream = response.getOutputStream();
		Date d=new Date();
		int day= d.getDay();
		String dbday="";
		System.out.println(day);
		if(day==1)
		{
			dbday="m";
		}
		else if(day==2)
		{
			dbday="tu";
		}
		else if(day==3)
		{
			dbday="w";
		}
		else if(day==4)
		{
			dbday="th";
		}
		else if(day==5)
		{
			dbday="f";
		}
		else if(day==6)
		{
			dbday="s";	
		}
		System.out.println(dbday);
		String path = this.getServletContext().getRealPath(this.getServletInfo())+"/Reports/";
		try
		{
			JasperReport jasperReport= (JasperReport)JasperCompileManager.compileReport(path+"Daily.jrxml");
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
			PreparedStatement ps=conn.prepareStatement("select co.name as course,br.name as branch,cl.session as session,su.name as subject,su.lectures_held as lecturesheld,T.fname || ' ' || T.lname as faculty "
					+"from pietons.class cl,pietons.branch br,pietons.course co,pietons.subject su,pietons.faculty fa,"
					+"xmltable('$e/personal_details/name' passing fa.personal_details as \"e\" " 
					+"columns fname varchar(10) path 'fname', lname varchar(10) " 
					+"path 'lname') as T, " 
					+"pietons.allocation B,xmltable('$d/schedule/day/slot' passing B.schedule as \"d\" columns d_id varchar(10) path '../@d_id' ,subject_id integer path './subject_id',teacher_id varchar "
					+"(10) path './teacher_id') as A "
					+"where cl.branch_id=br.id and br.course_id=co.id and cl.id=su.class_id and su.id=A.subject_id and A.teacher_id=fa.gr_no and A.d_id=?");
			ps.setString(1, dbday);
			ResultSet rs = ps.executeQuery();
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
