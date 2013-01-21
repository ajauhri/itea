package components;
/* InfraList Servlet (Component)
 * Working: 
 * 			Returns an <option> list of infrastructure from the database. Return format is <option value="id">name (Capacity: capacity)</option>
 * Usage:
 * 			1. Either directly call from ajax (but this method is not recommended)
 * 			2. Use your servlet corresponding to the current jsp page. The servlet can use
 * 					request.getRequestDispatcher("InfraList").forward(request,response);
 * 				to forward the request to InfraList. InfraList will then return the ajax response.
 * 			3. Response should be included in a <select> element by using innerHTML.  
 */


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
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
 * Servlet implementation class CourseList
 */
public class InfraList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InfraList() {
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
		String query="select id, name, capacity from pietons.infrastructure";
		String output="";
		try {
			Statement st=conn.createStatement();
			ResultSet rs=st.executeQuery(query);
			while(rs.next()){
				output+="<option value=\""+rs.getString("id")+"\">"+rs.getString("name")+" (Capacity: "+rs.getInt("capacity")+")</option>";
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
