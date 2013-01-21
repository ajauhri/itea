package components;
/* TimePreferences Servlet (Component)
 * Working: 
 * 			Returns an xml document for faculty's lecture time preferences from the database.
 * Usage:
 * 			1. Requires "gr_no" as parameter.
 * 			1. Either directly call from ajax (but this method is not recommended)
 * 			2. Use your servlet corresponding to the current jsp page. The servlet can use
 * 					request.getRequestDispatcher("TimePreferences").forward(request,response);
 * 				to forward the request to TimePreferences. TimePreferences will then return the ajax response.
 */


import java.io.IOException;
import java.io.PrintWriter;
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
 * Servlet implementation class TimePreferences
 */
public class TimePreferences extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TimePreferences() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Get connection */
		HttpSession session = request.getSession();
		Connection conn = null;
		synchronized (session) {
			DBConnector dbcon = (DBConnector) session.getAttribute("dbcon"); // fetch an existing instance
			if (dbcon == null) { // if none found, create a new one
				dbcon = new DBConnector();
				session.setAttribute("dbcon", dbcon);
			}
			conn = dbcon.getConnection();
		}			
//		String query="select xml2clob(preferences) as preferences from pietons.faculty where gr_no=?";
		String query="select x.day as day, x.slot as slot from pietons.faculty f, " +
				"xmltable('$p/preferences/day/slot' passing f.preferences as \"p\" " +
				"columns day varchar(2) path '../@d_id', " +
				"		slot integer path '@no') as x " +
				"where f.gr_no=?";
		PrintWriter out=response.getWriter();
		String output="";
		String day="";
		try {
			PreparedStatement ps=conn.prepareStatement(query);
			ps.setString(1,request.getParameter("gr_no"));
			ResultSet rs=ps.executeQuery();
			while(rs.next() && rs.getString("day")!=null){
				if(rs.getString("day").equals("m")) day="Monday";
				if(rs.getString("day").equals("tu")) day="Tuesday";
				if(rs.getString("day").equals("w")) day="Wednesday";
				if(rs.getString("day").equals("th")) day="Thursday";
				if(rs.getString("day").equals("f")) day="Friday";
				if(rs.getString("day").equals("s")) day="Saturday";
				output+="Day: "+day+" Slot: "+rs.getInt("slot")+"<br />"; 
/*				output=rs.getString("preferences");
				if(output.indexOf("?>")!=-1){
					output=output.substring(0,output.indexOf("?>"))
						+ "<?xml-stylesheet type=\"text/xsl\" href=\"../Database.xsl\" ?>"
						+ output.substring(output.indexOf("?>")+2,output.length());
				}else{
					output="<?xml version=\"1.0\" encoding=\"UTF-1\" ?>"
						+ "<?xml-stylesheet type=\"text/xsl\" href=\"../Database.xsl\" ?>"
						+ output;
				}
*/			}
			out.write(output);
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
}
