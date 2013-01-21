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
 * Servlet implementation class BranchWise
 */
public class BranchWise extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BranchWise() {
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
			String path = this.getServletContext().getRealPath(this.getServletInfo())+"/Reports/";
			JasperReport jasperReport= (JasperReport)JasperCompileManager.compileReport(path+"BranchWise.jrxml");
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
			ResultSet rs=null;
			PreparedStatement ps=null;
			JasperPrint jasperPrint= new JasperPrint();
			
			
			int branchid=(Integer)session.getAttribute("branch_id");
			
			String query="select distinct su.lectures_held as lecturesheld, T.fname ||' '|| T.lname as faculty, su.name as subject, cl.session as session " +
					"from pietons.branch br, pietons.class cl, pietons.subject su, pietons.faculty fa, " +
					"xmltable('$e/personal_details/name' passing fa.personal_details as \"e\" " +
					"columns fname varchar(10) path 'fname', " +
					"		lname varchar(10) path 'lname') as T, " +
					"pietons.allocation B, " +
					"xmltable('$d/schedule/day/slot' passing B.schedule as \"d\" " +
					"columns d_id varchar(2) path '../@d_id', " +
					"		no varchar(2) path './@no', " +
					"		subject_id integer path './subject_id', " +
					"		teacher_id varchar (10) path './teacher_id', " +
					"		infra_id integer path './infra_id') as A " +
					"where br.id=? and cl.id=su.class_id and cl.id=B.class_id and su.id=A.subject_id and A.teacher_id=fa.gr_no";
			ps=conn.prepareStatement(query);
			ps.setInt(1,branchid);
			rs=ps.executeQuery();
			JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(rs);
			jasperPrint= JasperFillManager.fillReport(jasperReport,new HashMap(), resultSetDataSource);
			JasperExportManager.exportReportToPdfStream(jasperPrint,servletOutputStream);
		}
		catch(Exception e)
		{
			// TODO
//			System.out.println(e.toString());
//			request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=server")).forward(request,response);
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
