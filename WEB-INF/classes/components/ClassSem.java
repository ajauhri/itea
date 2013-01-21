package components;

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

public class ClassSem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ClassSem() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {

		//HttpSession session=request.getSession();
		PrintWriter out=response.getWriter();
		
		String query = "select distinct s.class_id as class_id, l.session as name, b.name as branch " +
				"from pietons.student s, pietons.class l, pietons.branch b " +
				"where s.class_id=l.id and l.branch_id=b.id";
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

		out.println("<select name=\"class_id\" id=\"class_id\" onchange=\"getSubjects(this.value)\">") ;
		String output="<option value=\"-1\"><i>--Choose One--</i></option>" ;
		
		try 
		{
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			
			while(rset.next()) 
			{
				output += "<option value=\""+ rset.getString("class_id") +"\">"
							+rset.getString("branch") + "-" + rset.getString("name")+"</option>" ;
			}
			out.write(output);
		}
		catch(SQLException e) {
			System.out.println(e.toString());
		}
		out.println("</select>") ;
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException 
	{
		doGet(request,response);
	}

}
