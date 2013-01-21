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

public class FacultySubList extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public FacultySubList() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
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
		String query = "select distinct x.subject_id as subject_id, s.name as name " +
				" from pietons.allocation a, pietons.subject s, " +
				" xmltable('$s/schedule/day/slot' passing a.schedule as \"s\" " +
				"	columns subject_id integer path 'subject_id/text()'," +
				"			teacher_id varchar(10) path 'teacher_id/text()') as x " +
				" where x.teacher_id=? and x.subject_id=s.id";
		String output="" ;
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1,session.getAttribute("user_id").toString());
			ResultSet rset = ps.executeQuery();
			
			while(rset.next()){
				output += "<option value=\""+ rset.getString("subject_id") +"\">"
							+rset.getString("name")+"</option>" ;
			}
		}catch(SQLException e) {
			//TODO
			System.out.println(e.toString());
		}
		out.write(output);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
		doGet(request,response);
	}
}
