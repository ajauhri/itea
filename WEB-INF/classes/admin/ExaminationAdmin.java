package admin;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.DBConnector;

/**
 * Servlet implementation class InitCourse
 */
public class ExaminationAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ExaminationAdmin() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/* Check Login Status */
		request.getRequestDispatcher("/components/CLS?user_type=admin&returnType=redirect").include(request,response);
		if(response.isCommitted()) return;	// CLS preempting
		if(request.getParameter("q")!=null){
			if (request.getParameter("q").equals("course")) {
				request.getRequestDispatcher("/components/CourseList").forward(request,
						response);
			}else if (request.getParameter("q").equals("branch")) {
				request.getRequestDispatcher("/components/BranchList").forward(request,
						response);
			}else if (request.getParameter("q").equals("class")) {
				request.getRequestDispatcher("/components/ClassList")
						.forward(request, response);
			}else if (request.getParameter("q").equals("subject")) {
				request.getRequestDispatcher("/components/SubjectList?class_id="+request.getParameter("class_id")).forward(request,
						response);
			}else if (request.getParameter("q").equals("allot")) {
				HttpSession session = request.getSession();
				Connection conn = null;
				synchronized (session) {
					DBConnector dbcon = (DBConnector) session.getAttribute("dbcon");
					if (dbcon == null) {
						dbcon = new DBConnector();
						session.setAttribute("dbcon", dbcon);
					}
					conn = dbcon.getConnection();
				}
	
				int subject_id = Integer.parseInt(request
						.getParameter("subject_id"));
				int dd = Integer.parseInt(request.getParameter("dd"));
				int mm = Integer.parseInt(request.getParameter("mm"));
				int yyyy = Integer.parseInt(request.getParameter("yyyy"));
				int hr = Integer.parseInt(request.getParameter("hr"));
				int min = Integer.parseInt(request.getParameter("min"));
				int dur = Integer.parseInt(request.getParameter("dur"));
	
				String query = "insert into pietons.examination"
						+ " values(default, ?, "
						+ " xmlserialize(content xmlelement(name \"date\", "
						+ "		xmlelement(name \"dd\", '"+dd+"'),"
						+ "		xmlelement(name \"mm\", '"+mm+"'),"
						+ "		xmlelement(name \"yyyy\", '"+yyyy+"')) as clob(100)),"
						+ " xmlserialize(content xmlelement(name \"time\", "
						+ "		xmlelement(name \"hh\", '"+hr+"'),"
						+ "		xmlelement(name \"mm\", '"+min+"'),"
						+ "		xmlelement(name \"ss\", '0')) as clob(100)), ?)";
				PreparedStatement ps;
				try {
					ps = conn.prepareStatement(query);
					ps.setInt(1, subject_id);
					ps.setInt(2, dur);
					ps.execute();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println(e.toString());
					request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=database")).forward(request,response);
					return;
				}
				PrintWriter out = response.getWriter();
				out.write("Update Successful");
				out.close();
			}
		}else{
			request.getRequestDispatcher(response.encodeURL("/admin/examinationAdmin.jsp")).forward(request,response);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
