package admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.DBConnector;

/**
 * Servlet implementation class EnterMarks
 */
public class EnterMarks extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EnterMarks() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException 
	{
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
		if(request.getParameter("list")==null){
			request.getRequestDispatcher(response.encodeURL("enter_marks.jsp")).forward(request,response);
		}
		 if (request.getParameter("list") != null && request.getParameter("list").equals("subject")) 
		 {
				/* Ajax call for subject list */
				/* Extract list of subjects belonging to faculty */
		
			 PrintWriter out=response.getWriter();
			 			 
			 String class_id = request.getParameter("class_id") ;//session.getAttribute("class_id") ;
			 
			 String query = "select id,name from pietons.subject where class_id = ?";// and valid = true
			 
			 String output="" ;
			 try 
			 {
				 PreparedStatement stmt = conn.prepareStatement(query);
				 stmt.setInt(1,Integer.parseInt(class_id));
				 ResultSet rset = stmt.executeQuery();
				 
				 while(rset.next()) 
				 {
					 output += "<option value=\""+ rset.getString("id") +"\">"
					 +rset.getString("name")+"</option>" ;
				 }
				 out.write(output);
			 }
			 catch(SQLException e) {
				 // TODO
				 System.out.println(e.toString());
				request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=database")).forward(request,response);
				return;
			 }
			 return;
		 }
		 
		 else if (request.getParameter("list") != null && request.getParameter("list").equals("students")) 
		 {
				/* Ajax call for student chart */
				/* Extract Student info */
			 
			 String class_id = request.getParameter("class_id") ;
			 String sub_id = request.getParameter("subject_id") ;
			 
			 ResultSet rset = null;
			 String query =	" select s.gr_no as gr_no, e.id as examination_id " +
			 		"from pietons.student s, pietons.examination e "+
			 			" where s.class_id = ? and e.subject_id=? " ;
			
			 String output="<table border='0'>" +
				"<tr>" +
				"<td>Student</td>" +
				"<td>Marks</td>" +
				"</tr>";
			 
			 try 
			 {
				 PreparedStatement stmt = conn.prepareStatement(query);
				 stmt.setInt(1,Integer.parseInt(class_id));
				 stmt.setInt(2,Integer.parseInt(sub_id));
					 
				 rset = stmt.executeQuery();
				 
				 String exam_id="" ;
				 int i=1;
				 while( rset.next() ) 
				 {	
					 int marks=0 ;
					 String gr_no = rset.getString("gr_no");
					 exam_id = rset.getString("examination_id") ;
			output+= "<tr>" +
							 "<td>"+gr_no+"</td>" +
							 "<td><input type=text name=m"+i+" value="+marks+" maxlength=3 /> </td>" +
							 "<td><input type=hidden name=g"+i+" value="+gr_no+" /></td>" +
					 "</tr>" ;	
					i++;
				 }//if
				 
				 output+="</table><input type=\"hidden\" id=\"exam_id\" name=\"exam_id\" value=\""+exam_id+"\">" ;
			 }
			 catch(SQLException e) 
			 {
				 // TODO
				 System.out.println(e.toString());
				request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=database")).forward(request,response);
				return;
			 }
			 response.getWriter().write(output);
			 return ;
		 }
	}	 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException 
	{
		/* Check Login Status */
		request
				.getRequestDispatcher(
						"/components/CLS?user_type=admin&returnType=redirect&firstTime=true")
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

		RequestDispatcher rd = request.getRequestDispatcher("enter_marks.jsp") ;
		
		String exam_id = request.getParameter("exam_id") ;
		int i=1 ;
		
		while(request.getParameter("g"+i)!=null)	
		{
			String query ="insert into pietons.marks values (?,?,?)";
			
			//		"("+exam_id+","+ request.getParameter("g"+i) +" , "+ request.getParameter("m"+i) +" )" ;
			
			String commit = "commit" ; 

			try
			{
				PreparedStatement pstmnt = conn.prepareStatement(query);
				pstmnt.setString(1,request.getParameter("g"+i));
				pstmnt.setInt(2,Integer.parseInt(exam_id));
				pstmnt.setDouble(3,Double.parseDouble(request.getParameter("m"+i)));
				try
				{
					pstmnt.execute();
				}
				catch(SQLException e)
				{				
					// TODO
					System.out.println(e.toString());
					request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=form")).forward(request,response);
					return;
				}
				pstmnt=conn.prepareStatement(commit);
				pstmnt.execute();
			}
			catch(SQLException e)
			{
				// TODO
				System.out.println(e.toString());
				request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=database")).forward(request,response);
				return;
			}
			i++ ;
		}
		rd.include(request, response) ;
		return ;
		
	}
		
}