/*Servlet used for displaying Courses, Branches, Classes.

 */

package hod;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Enumeration;

import beans.DBConnector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class TimeTableAdmin
 */
public class TimeTableAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TimeTableAdmin() {
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
		request.getRequestDispatcher(
						"/components/CLS?user_type=hod&firstTime=true&returnType=redirect")
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
		PrintWriter out = response.getWriter();
		if(request.getParameter("q")==null){
				request.getRequestDispatcher(response.encodeURL("timeTableAdmin.jsp")).forward(request,response);
		}else if (request.getParameter("q").equals("course")) {
			request.getRequestDispatcher("/components/CourseList").forward(
					request, response);
			return;
		}else if (request.getParameter("q").equals("branch")) {
			request.getRequestDispatcher("/components/BranchList").forward(
					request, response);
			return;
		}else if (request.getParameter("q").equals("class")) {
			request.getRequestDispatcher("/hod/ClassListTimeTable").forward(
					request, response);
			return;
		}else if (request.getParameter("q").equals("preferences")) {
			request.getRequestDispatcher("/components/TimePreferences?gr_no"+request.getParameter("gr_no")).forward(
					request, response);
			return;
		}else if (request.getParameter("q").equals("opts")) {
			String text = "";
			String class_id = request.getParameter("class_id").toString();
			session.setAttribute("class_id", class_id);
			String query1 = "select id, name from pietons.subject where class_id=?";
			String query2 = "select A.gr_no as id, B.fname as fname,B.mname as mname,B.lname as lname from "
					+ "pietons.faculty A, xmltable('$d/personal_details/name' passing  A.personal_details as \"d\" columns fname"
					+ " varchar(20) path 'fname/text()', mname varchar(20) path 'mname/text()', lname varchar(20) path 'lname/text()')"
					+ " as B";
			String query3 = "select * from pietons.infrastructure";
			try {
				PreparedStatement ps = conn.prepareStatement(query1,
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				ps.setString(1, class_id);
				ResultSet rs1 = ps.executeQuery();
				Statement st2 = conn.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				Statement st3 = conn.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				ResultSet rs2 = st2.executeQuery(query2);
				ResultSet rs3 = st3.executeQuery(query3);

				while (rs1.next()) {
					text += "<option value=\"" + rs1.getString("id") + "\">"
							+ rs1.getString("name") + "</option>";
				}
				text += "#";
				while (rs2.next()) {
					text += "<option value=\""
							+ rs2.getString("id")
							+ "\">"
							+ rs2.getString("fname")
							+ " "
							+ (rs2.getString("mname")==null? "" : rs2
									.getString("mname")) + " "
							+ rs2.getString("lname") + "</option>";
				}
				text += "#";
				while (rs3.next()) {
					text += "<option value=\"" + rs3.getString("id") + "\">"
							+ rs3.getString("name") + " (Capacity: "
							+ rs3.getString("capacity") + ")</option>";
				}
				rs1.close();
				rs2.close();
				rs3.close();
				ps.close();
				st2.close();
				st3.close();

				out.write(text);
			} catch (SQLException e) {
				// TODO
				System.out.println(e.toString());
			}

			return;
		}else if (request.getParameter("q").equals("check")) {
			// String subject_id=request.getParameter("subject_id");
			int flag = 0;
			String output = request.getParameter("opId") + "#";
//			String query = "select cl.session as session,su.name as name,in.name as in_name,B.class_id as class_id, A.d_id as d_id,"
//					+ "A.no as slot,"
//					+ "A.subject_id as subject_id,"
//					+ "A.teacher_id as teacher_id,"
//					+ "A.infra_id as infra_id"
//					+ " from pietons.infrastructure in,pietons.subject su,pietons.class cl, pietons.allocation B, pietons.faculty f,"
//					+ "xmltable('$d/schedule/day/slot' passing B.schedule as \"d\""
//					+ " columns d_id varchar(2) path '../@d_id',"
//					+ " no varchar(2) path './@no',"
//					+ " subject_id integer path './subject_id/text()',"
//					+ " teacher_id varchar(10) path './teacher_id/text()',"
//					+ " infra_id integer path './infra_id/text()') as A,"
//					+ "xmltable('$p/personal_details/name' passing f.personal_details as \"p\""
//					+ " columns fname varchar(30) path 'fname/text()',"
//					+ " mname varchar(30) path 'mname/text()',"
//					+ " lname varchar(30) path 'lname/text()') as F"
//					+ " where cl.id=B.class_id and in.id=A.infra_id and su.id=A.subject_id";
			String query="select c.id as class_id, c.session as session, s.name as subject_name, i.name as in_name,"
				+ " xs.d_id as d_id, xs.slot_no as slot, xs.subject_id as subject_id, xs.teacher_id as teacher_id, xs.infra_id as infra_id"
				+ " from pietons.class c, pietons.subject s, pietons.faculty f, pietons.infrastructure i, pietons.allocation a,"
				+ " xmltable('$p/personal_details/name' passing f.personal_details as \"p\""
				+ " columns fname varchar(30) path 'fname/text()',"
				+ "			mname varchar(30) path 'mname/text()',"
				+ "			lname varchar(30) path 'lname/text()') as xfn,"
				+ " xmltable('$s/schedule/day/slot' passing a.schedule as \"s\""
				+ " columns d_id varchar(2) path '../@d_id',"
				+ "			slot_no integer path '@no',"
				+ "			subject_id integer path 'subject_id/text()',"
				+ "			teacher_id varchar(10) path 'teacher_id/text()',"
				+ "			infra_id integer path 'infra_id/text()') as xs"
				+ " where c.id=a.class_id and i.id=xs.infra_id and s.id=xs.subject_id and f.gr_no=xs.teacher_id"
				+ " and xs.slot_no=? and xs.d_id=? and (xs.infra_id=? or xs.teacher_id=?) and s.valid=1";
			String teacher_id = request.getParameter("teacher_id");
			int infra_id = Integer.parseInt(request.getParameter("infra_id"));
			String d_id = request.getParameter("d_id");
			int slot = Integer.parseInt(request.getParameter("slot"));
			try {
				PreparedStatement ps=conn.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				ps.setInt(1,slot);
				ps.setString(2,d_id);
				ps.setInt(3,infra_id);
				ps.setString(4,teacher_id);
				ResultSet rs=ps.executeQuery();
//				Statement st = conn.createStatement(
//						ResultSet.TYPE_SCROLL_INSENSITIVE,
//						ResultSet.CONCUR_READ_ONLY);
//				ResultSet rs = st.executeQuery(query);
//				while (rs.next()) {
//					if (rs.getString("slot").equals(slot)
//							&& rs.getString("d_id").equals(d_id)
//							&& (rs.getString("infra_id").equals(infra_id) || rs
//									.getString("teacher_id").equals(teacher_id))) {
				while(rs.next()){
					output += "<span class='error' style='display:block'>Allocation coincides with class:"
							+ rs.getString("class_id")
							+ "<br />Subject:"
							+ rs.getString("subject_name")+"<br>Session: "+rs.getString("session")
							+ "<br />Teacher: "
							+ rs.getString("teacher_id")
							+ "<br />Infrastructure: "
							+ rs.getString("in_name");
					flag = -1;
					break;
//					}
				}
				if (flag == -1)
					out.write(output + "</span>");
				else
					out.write(output);
			} catch (SQLException e) {
				// TODO
				System.err.println(e.toString());
			}
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/* Check Login Status */
		request.getRequestDispatcher(
						"/components/CLS?user_type=hod&firstTime=true&returnType=redirect")
				.include(request, response);
		if (response.isCommitted())
			return; // CLS preempting
		/* Get Connection */
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
		String mq = "", tuq = "", wq = "", thq = "", fq = "", sq = "";
		int sl[][] = new int[6][9];
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 9; j++)
				sl[i][j] = 0;
		}
		for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
			String temp = e.nextElement().toString();
			if (temp.charAt(0) == 'c') {
				int i;
				if (temp.substring(1, 3).equals("tu")) {
					i = temp.charAt(3) - 49;
					sl[1][i] = 1;
					tuq += ",xmlelement(name \"slot\", xmlattributes('"
							+ temp.charAt(3) + "' as \"no\"),"
							+ "xmlelement(name \"subject_id\",'"
							+ request.getParameter("stu" + temp.charAt(3))
							+ "')," + "xmlelement(name \"teacher_id\",'"
							+ request.getParameter("ttu" + temp.charAt(3))
							+ "')," + "xmlelement(name \"infra_id\", '"
							+ request.getParameter("itu" + temp.charAt(3))
							+ "'))";
				}
				if (temp.substring(1, 3).equals("th")) {
					i = temp.charAt(3) - 49;
					sl[3][i] = 1;
					thq += ",xmlelement(name \"slot\", xmlattributes('"
							+ temp.charAt(3) + "' as \"no\"),"
							+ "xmlelement(name \"subject_id\",'"
							+ request.getParameter("sth" + temp.charAt(3))
							+ "')," + "xmlelement(name \"teacher_id\",'"
							+ request.getParameter("tth" + temp.charAt(3))
							+ "')," + "xmlelement(name \"infra_id\", '"
							+ request.getParameter("ith" + temp.charAt(3))
							+ "'))";
				}
				switch (temp.charAt(1)) {
				case 'm':
					i = temp.charAt(2) - 49;
					sl[0][i] = 1;
					mq += ",xmlelement(name \"slot\", xmlattributes('"
							+ temp.charAt(2) + "' as \"no\"),"
							+ "xmlelement(name \"subject_id\",'"
							+ request.getParameter("sm" + temp.charAt(2))
							+ "')," + "xmlelement(name \"teacher_id\",'"
							+ request.getParameter("tm" + temp.charAt(2))
							+ "')," + "xmlelement(name \"infra_id\", '"
							+ request.getParameter("im" + temp.charAt(2))
							+ "'))";

					break;
				case 'w':
					i = temp.charAt(2) - 49;
					sl[2][i] = 1;
					wq += ",xmlelement(name \"slot\", xmlattributes('"
							+ temp.charAt(2) + "' as \"no\"),"
							+ "xmlelement(name \"subject_id\",'"
							+ request.getParameter("sw" + temp.charAt(2))
							+ "')," + "xmlelement(name \"teacher_id\",'"
							+ request.getParameter("tw" + temp.charAt(2))
							+ "')," + "xmlelement(name \"infra_id\", '"
							+ request.getParameter("iw" + temp.charAt(2))
							+ "'))";
					break;
				case 'f':
					i = temp.charAt(2) - 49;
					sl[4][i] = 1;
					fq += ",xmlelement(name \"slot\", xmlattributes('"
							+ temp.charAt(2) + "' as \"no\"),"
							+ "xmlelement(name \"subject_id\",'"
							+ request.getParameter("sf" + temp.charAt(2))
							+ "')," + "xmlelement(name \"teacher_id\",'"
							+ request.getParameter("sf" + temp.charAt(2))
							+ "')," + "xmlelement(name \"infra_id\", '"
							+ request.getParameter("if" + temp.charAt(2))
							+ "'))";
					break;
				case 's':
					i = temp.charAt(2) - 49;
					sl[5][i] = 1;
					sq += ",xmlelement(name \"slot\", xmlattributes('"
							+ temp.charAt(2) + "' as \"no\"),"
							+ "xmlelement(name \"subject_id\",'"
							+ request.getParameter("ss" + temp.charAt(2))
							+ "')," + "xmlelement(name \"teacher_id\",'"
							+ request.getParameter("ts" + temp.charAt(2))
							+ "')," + "xmlelement(name \"infra_id\", '"
							+ request.getParameter("is" + temp.charAt(2))
							+ "'))";
					break;
				default:
					break;
				}
			}
		}
		String query = "insert into pietons.allocation values(?, xmlserialize(content xmlelement(name"
				+ "\"schedule\", xmlelement(name \"day\", xmlattributes('m' as \"d_id\") "
				+ mq
				+ "),xmlelement(name \"day\",xmlattributes('tu' as \"d_id\") "
				+ tuq
				+ "),xmlelement(name \"day\",xmlattributes('w' as \"d_id\") "
				+ wq
				+ "),xmlelement(name \"day\",xmlattributes('th' as \"d_id\") "
				+ thq
				+ "),xmlelement(name \"day\",xmlattributes('f' as \"d_id\") "
				+ fq
				+ "),xmlelement(name \"day\",xmlattributes('s' as \"d_id\") "
				+ sq + ")) as clob))";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, request.getParameter("classes"));
			ps.executeUpdate();
			request.getRequestDispatcher(response.encodeURL("timeTableAdmin.jsp?show=success")).forward(request,response);
		} catch (SQLException e) {
			System.err.println(e.toString());
		}
	}
}
