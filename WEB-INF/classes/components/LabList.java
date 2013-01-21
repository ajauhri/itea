package components;
/* LabList Servlet (Component)
 * Working: 
 * 			Returns an <option> list of labs from the database. Only extracts id and name,
 * 			and return format is <option value="id">name</option>
 * Usage:
 * 			1. Either directly call from ajax (but this method is not recommended)
 * 			2. Use your servlet corresponding to the current jsp page. The servlet can use
 * 					request.getRequestDispatcher("LabList").forward(request,response);
 * 				to forward the request to LabList. LabList will then return the ajax response.
 * 			3. Response should be included in a <select> element by using innerHTML.  
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
 * Servlet implementation class LabList
 */
public class LabList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LabList() {
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
		String query="select id,name from pietons.infrastructure where infra_type=?";
		String output="";
		try {
			PreparedStatement ps=conn.prepareStatement(query);
			ps.setInt(1,1);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				output+="<input type=\"checkbox\" name=\"labs_available\" id=\"lab"+rs.getString("id")+"\" value=\""+rs.getString("id")+"\" />";
				output+="<label for=\"lab"+rs.getString("id")+"\">"+rs.getString("name")+"</label><br />";
			}
			PrintWriter out=response.getWriter();
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
