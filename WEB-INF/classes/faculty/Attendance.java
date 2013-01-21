package faculty;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.DBConnector;

/**
 * Servlet implementation class Attendance
 */
public class Attendance extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Attendance() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/* Check Login Status */
		request
				.getRequestDispatcher(
						"/components/CLS?user_type=faculty&returnType=redirect&firstTime=true")
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
		if (request.getParameter("ajax") == null) {
			/* Non-ajax get call */
			doPost(request, response);
		} else if (request.getParameter("list") != null
				&& request.getParameter("list").equals("attendance")) {
			/* Ajax call for attendance */
			/* Extract Student info */
			String query = "select a.attendance as total,"
					+ " x.fname as fname, x.mname as mname, x.lname as lname, s.roll_no as roll_no"
					+ " from pietons.attendance a, pietons.student s,"
					+ " xmltable('$p/personal_details/name' passing s.personal_details as \"p\""
					+ " columns fname varchar(30) path 'fname/text()',"
					+ "		mname varchar(30) path 'mname/text()',"
					+ "		lname varchar(30) path 'lname/text()') as x"
					+ " where a.subject_id=? and a.student_id=s.gr_no order by s.roll_no";
			String output = "";
			String studentName = "";
			boolean count = false;
			try {
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setString(1, request.getParameter("subject_id"));
				ResultSet rs = ps.executeQuery();
				// if (rs.absolute(Integer.parseInt(request
				// .getParameter("count")))) {
				while (rs.next()) {
					studentName = rs.getString("fname")
							+ " "
							+ ((rs.getString("mname") == null
									|| rs.getString("mname").equals("") || rs
									.getString("mname").equals("null")) ? ""
									: rs.getString("mname")) + " "
							+ rs.getString("lname");
					output += "<tr" + (count ? "" : " class=\"menu navText\"")
							+ "><td>" + rs.getInt("roll_no") + "</td><td>"
							+ studentName + "</td><td>" + rs.getInt("total")
							+ "</td></tr>";
					count = !count;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
			}
			response.getWriter().write(output);
			return;
		} else if (request.getParameter("list") != null
				&& request.getParameter("list").equals("students")) {
			/* Ajax call for student chart */
			/* Extract Student info */
			// String query =
			// "select a.student_id as student_id from pietons.attendance a, pietons.student s"
			// +
			// " where a.subject_id=? and a.student_id=s.gr_no order by s.roll_no"
			// ;
			String query1 = "select x.fname as fname, x.mname as mname, x.lname as lname, s.roll_no as roll_no, s.gr_no as gr_no"
					+ " from pietons.student s, pietons.subject j,"
					+ " xmltable('$p/personal_details/name' passing s.personal_details as \"p\""
					+ " columns fname varchar(30) path 'fname/text()',"
					+ "		mname varchar(30) path 'mname/text()',"
					+ "		lname varchar(30) path 'lname/text()') as x"
					+ " where j.id=? and j.class_id=s.class_id order by s.roll_no";
			String output = "";
			String studentName = "";
			boolean count = false;
			try {
				// PreparedStatement ps = conn.prepareStatement(query,
				// ResultSet.TYPE_SCROLL_INSENSITIVE,
				// ResultSet.CONCUR_READ_ONLY);
				// ps.setString(1, request.getParameter("subject_id"));
				PreparedStatement ps1 = null;
				// ResultSet rs = ps.executeQuery();
				ResultSet rs1 = null;
				// // if (rs.absolute(Integer.parseInt(request
				// // .getParameter("count")))) {
				// while (rs.next()) {
				ps1 = conn.prepareStatement(query1);
				// ps1.setString(1, rs.getString("student_id"));
				ps1.setInt(1, Integer.parseInt(request
						.getParameter("subject_id")));
				rs1 = ps1.executeQuery();
				while (rs1.next()) {
					studentName = rs1.getString("fname")
							+ " "
							+ ((rs1.getString("mname") == null
									|| rs1.getString("mname").equals("") || rs1
									.getString("mname").equals("null")) ? ""
									: rs1.getString("mname")) + " "
							+ rs1.getString("lname");
					output += "<tr" + (count ? "" : " class=\"menu navText\"")
							+ "><td>" + rs1.getInt("roll_no") + "</td><td>"
							+ studentName + "</td><td>"
							+ "<input type=\"checkbox\" value=\""
							+ rs1.getString("gr_no") + "\" />" + "</td></tr>";
					count = !count;
				}
				// }
				// rs.close();
				// ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
			}
			response.getWriter().write(output);
			return;
		} else if (request.getParameter("list") != null
				&& request.getParameter("list").equals("subject")) {
			/* Ajax call for subject list */
			/* Extract list of subjects belonging to faculty */
			String gr_no = session.getAttribute("user_id").toString(); // faculty
			// gr_no
			String query = "select distinct x.subject_id as id, s.name as name"
					+ " from pietons.allocation a, pietons.subject s,"
					+ " xmltable( '$s/schedule/day/slot' passing a.schedule as \"s\""
					+ " columns subject_id integer path 'subject_id/text()',"
					+ "		teacher_id varchar(10) path 'teacher_id/text()') as x"
					+ " where x.teacher_id=? and x.subject_id=s.id";
			PreparedStatement ps;
			String output = "";
			try {
				ps = conn.prepareStatement(query);
				ps.setString(1, gr_no);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					output += "<option value=\"" + rs.getInt("id") + "\">"
							+ rs.getString("name") + "</option>";
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
			}
			response.getWriter().write(output);
			return;
		} else if (request.getParameter("do") != null
				&& request.getParameter("do").equals("check")) {
			/* Check if attendance already marked */
			String query = "select * from pietons.attendance_marked where subject_id=? and slot=?"
					+ " and xmlexists('$d/date[dd=\""
					+ request.getParameter("dd")
					+ "\"]' passing date as \"d\")"
					+ " and xmlexists('$d/date[mm=\""
					+ request.getParameter("mm")
					+ "\"]' passing date as \"d\")"
					+ " and xmlexists('$d/date[yyyy=\""
					+ request.getParameter("yyyy")
					+ "\"]' passing date as \"d\")";
			try {
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setInt(1, Integer.parseInt(request
						.getParameter("subject_id")));
				ps.setInt(2, Integer.parseInt(request.getParameter("slot")));
				ResultSet rs = ps.executeQuery();
				if (rs != null && rs.next())
					response.getWriter().write("marked");
				else
					response.getWriter().write("unmarked");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/* Check Login Status */
		request
				.getRequestDispatcher(
						"/components/CLS?user_type=faculty&firstTime=true&returnType=redirect")
				.include(request, response);
		if (response.isCommitted())
			return; // CLS preempting
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
		if (request.getParameter("do") != null
				&& request.getParameter("do").equals("mark")) {
			/* Show Mark-Attendance Page */
			request.getRequestDispatcher("/faculty/attendance.jsp?do=mark")
					.forward(request, response);
		} else if (request.getParameter("do") != null
				&& request.getParameter("do").equals("save")) {
			/* Save Attendance Marked */
			/* Get date, subject_id, slot no, list of attenders ' gr no. */
			String dd="",mm="",yyyy="",attenders="";
			int subject_id=0,slot=0;
			
			if(request.getParameter("dd")!=null) dd=request.getParameter("dd");
			else if(request.getAttribute("dd")!=null) dd=request.getAttribute("dd").toString();
			if(request.getParameter("mm")!=null) mm=request.getParameter("mm");
			else if(request.getAttribute("mm")!=null) mm=request.getAttribute("mm").toString();
			if(request.getParameter("yyyy")!=null) yyyy=request.getParameter("yyyy");
			else if(request.getAttribute("yyyy")!=null) yyyy=request.getAttribute("yyyy").toString();
			if(request.getParameter("subject_id")!=null) subject_id=Integer.parseInt(request.getParameter("subject_id"));
			else if(request.getAttribute("subject_id")!=null) subject_id=Integer.parseInt(request.getAttribute("subject_id").toString());
			if(request.getParameter("slot")!=null) slot=Integer.parseInt(request.getParameter("slot"));
			else if(request.getAttribute("slot")!=null) slot=Integer.parseInt(request.getAttribute("slot").toString());
			if(request.getParameter("attenders")!=null) attenders=request.getParameter("attenders");
			else if(request.getAttribute("attenders")!=null) attenders=request.getAttribute("attenders").toString();
			
			/* Increment lectures held */
			try{
			String query01="update pietons.subject set lectures_held=lectures_held+1 where id=?";
			PreparedStatement ps0=conn.prepareStatement(query01);
			ps0.setInt(1,subject_id);
			ps0.execute();
			}catch(SQLException e){
				// TODO Auto-generated catch block
				System.out.println(e.toString());				
			}
			
			/* Insert into attendance_marked */
			String query = "insert into pietons.attendance_marked(subject_id, date, slot) values(?,"
					+ " xmlserialize(content xmlelement(name \"date\","
					+ "	xmlelement(name \"dd\", '"
					+ dd
					+ "'),"
					+ "	xmlelement(name \"mm\", '"
					+ mm
					+ "'),"
					+ "	xmlelement(name \"yyyy\", '"
					+ yyyy + "')) as clob(100)),?)";
			try {
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setInt(1, subject_id);
				ps.setInt(2, slot);
				ps.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
			}
			/* Update each student's marks */
			String query0 = "select * from pietons.attendance where student_id=? and subject_id=?";
			String query1 = "update pietons.attendance set attendance=attendance+1 where student_id=? and subject_id=?";
			String query2 = "insert into pietons.attendance(student_id,subject_id,attendance) values (?,?,?)";
			PreparedStatement ps1 = null;
			StringTokenizer st = new StringTokenizer(attenders, ",");
			while (st.hasMoreTokens()) {
				String student_id = st.nextToken();
				try {
					// check if user exists;
					ps1 = conn.prepareStatement(query0);
					ps1.setString(1, student_id);
					ps1.setInt(2, subject_id);
					ResultSet rs1 = ps1.executeQuery();
					if (rs1 != null && rs1.next()) {
						ps1 = conn.prepareStatement(query1);
						ps1.setString(1, student_id);
						ps1.setInt(2, subject_id);
						ps1.execute();
					} else { // record doesn't exist
						ps1 = conn.prepareStatement(query2);
						ps1.setString(1, student_id);
						ps1.setInt(2, subject_id);
						ps1.setInt(3, 1);
						ps1.execute();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println(e.toString());
				}
			}
			request.getRequestDispatcher("/faculty/attendance.jsp?do=save")
					.forward(request, response);
		} else {
			/* Ordinary Request */
			request.getRequestDispatcher("/faculty/attendance.jsp").forward(
					request, response);
		}
	}

}
