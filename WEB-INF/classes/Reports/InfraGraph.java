package Reports;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.DBConnector;

/**
 * Servlet implementation class InfraGraph
 */
public class InfraGraph extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InfraGraph() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Get connection */
		Connection conn = null;
		HttpSession session=request.getSession();
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

		Statement st;
		try {
			st = conn.createStatement();
		int slots =9;
		String day=request.getParameter("day");
		PrintWriter out=response.getWriter();
		out.println("<table border=1>");
		
		for(int i=0;i<=slots;i++)
		{
			out.println("<tr><td>");
				if(i!=0)
				{
					out.println((7+i)+":00 hrs to "+(8+i)+":00 hrs");
				}
				out.println("</td>");
				ResultSet rs=st.executeQuery("select id,infra_type,capacity,name from pietons.infrastructure");
				while(rs.next())
				{
					if(i==0)
					{
						out.println("<th style=\"width:20px; height:10px;border:1px solid black\">"+rs.getString("name")+"</th>");
					}
					else
					{
						out.println(printStats((new DBConnector()).getConnection(),rs.getString("id"),i,day));
					}
				}
			out.println("</tr>");
		}
		out.println("</table>");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}
	}

	public String printStats(Connection conn,String id,int i,String day){
		String output="";
		try{
			PreparedStatement ps =conn.prepareStatement("select a.class_id as class_id, x.d_id as d_id, x.slot_no as slot_no, x.infra_id as infra_id "
					 +"from pietons.allocation a, "
					 +"xmltable('$s/schedule/day/slot' passing a.schedule as \"s\" "
					 +"columns d_id varchar(2) path '../@d_id', "
					 +"slot_no integer path '@no', "
					 +"infra_id integer path 'infra_id/text()') as x "
					 +"where x.infra_id=? and x.slot_no=? and x.d_id=?");
			ps.setString(1,id);
			ps.setInt(2,i);
			ps.setString(3,day);
			ResultSet rs1=ps.executeQuery();
			if(rs1!=null && rs1.next())
				output+="<td style=\"width:20px; height:10px;border:1px solid black;background-color: red\"> </td>";
			else
				output+="<td style=\"width:20px; height:10px;border:1px solid black;\"> </td>";
		}catch(Exception e)
		{
			System.out.println("Error: "+e.toString());
		}
		return output;
	}

}
