package components;

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

public class StudentSubList extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public StudentSubList() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
		/* Check Login Status */
		request
				.getRequestDispatcher(
						"/components/CLS?user_type=student&returnType=redirect&firstTime=true")
				.include(request, response);
		if (response.isCommitted())
			return; // CLS preempt
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
		PrintWriter out=response.getWriter();
		
		String query = "select id, name from pietons.subject where class_id = ?" ;// and valid = true
		
		out.println("<select name=\"subject\">") ;
		String output="" ;
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1,session.getAttribute("class_id").toString());
			ResultSet rset = ps.executeQuery();
			
			while(rset.next()){
				output += "<option value=\""+ rset.getString("id") +"\">"
							+rset.getString("name")+"</option>" ;
			}
		}catch(SQLException e) {
			//TODO
			System.out.println(e.toString());
		}
		out.write(output);
		out.println("</select>") ;
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
		super.doPost(request, response);
	}
}
