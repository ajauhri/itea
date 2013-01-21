package components;

/* Messages Servlet
 * Working:
 * 		1. Instantiates MessageBean objects to store messages received (if requested interface is "view messages").
 * 		2. Saves message (when current interface is "send messages").
 * 		3. Provides ajax facility to handle requests for viewing full message bodies.
 * Requirements:
 * 		1. Request parameter "interface" - values: "view" or "send". If none specified, defaults to "view".
 * 		2. Ajax request parameter "ajax" - value: true.
 * 		3. messages.jsp page to display stuff.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.DBConnector;
import beans.DateBean;
import beans.MessageBean;

public class Messages extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Messages() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/* Check Login Status */
		request.getRequestDispatcher("/components/CLS?returnType=redirect&firstTime=true").include(request,
				response);
		if(response.isCommitted()) return;	// CLS preempting.
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
		// Determine ajax call or ordinary hyperlink
		if (request.getParameter("ajax") == null) {
			doPost(request, response);
			return;
		} else if (request.getParameter("do") != null
				&& request.getParameter("do").equals("show")) {
			// For ajax call for message content
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			String content = "";
			content = request.getParameter("store");
			content += "##";
			String query = "select content from pietons.messages where id=?";
			try {
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setInt(1, Integer.parseInt(request.getParameter("id")));
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					content += rs.getString("content");
				}
			} catch (SQLException e) {
				System.out.println(e.toString());
			}
			content = content.replace("'", "\\'");
			content = content.replace("\"", "\\\"");

			out.write(content);
			out.close();
		} else if (request.getParameter("do") != null
				&& request.getParameter("do").equals("delete")) {
			/* Delete a Message */
			String query = "delete from pietons.messages where id=?";
			try {
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setString(1, request.getParameter("id"));
				ps.execute();
				return;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
			}
		} else if (request.getParameter("list") != null) {
			// For ajax call to list or
			// courses/branches/classes/students/faculty
			String list = request.getParameter("list").toString();
			if (list.equals("course"))
				request.getRequestDispatcher("/components/CourseList").forward(
						request, response);
			else if (list.equals("branch"))
				request.getRequestDispatcher("/components/BranchList").forward(
						request, response);
			else if (list.equals("class"))
				request.getRequestDispatcher("/components/ClassList").forward(
						request, response);
			else if (list.equals("student"))
				request.getRequestDispatcher("/components/StudentList")
						.forward(request, response);
			else if (list.equals("faculty"))
				request.getRequestDispatcher("/components/FacultyList")
						.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/* Check Login Status */
		request.getRequestDispatcher("/components/CLS?returnType=redirect&firstTime=true").include(request,
				response);
		if(response.isCommitted()) return; // CLS preempting
		/* Get connection */
		int noOfRows = 0, i = 0;
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
		/* No interface -- default view */
		if (request.getParameter("interface") == null) {
			response
					.sendRedirect(response.encodeURL("Messages?interface=view"));
			return;
		}
		/* Determine interface requested: view/send */
		if (request.getParameter("interface").toString().equals("send")) {
			request
					.getRequestDispatcher(
							response
									.encodeURL("/components/messages.jsp?interface=send&rand="
											+ Math.random())).forward(request,
							response);
			return;
			/* Show Messages */
		} else if (request.getParameter("interface").toString().equals("view")) {
			/* Retrieve messages */
			String query = "select m.id as id, m.sender as sender, m.msg_type as msg_type, m.recipient as recipient, m.rec_type as rec_type,"
					+ " x.dd as dd, x.mm as mm, x.yyyy as yyyy, m.subject as subject, m.content as content"
					+ " from pietons.messages m,"
					+ " xmltable('$d/date' passing m.date as \"d\""
					+ " columns dd varchar(2) path 'dd/text()',"
					+ "		mm varchar(2) path 'mm/text()',"
					+ "		yyyy varchar(4) path 'yyyy/text()') as x";
			/* Add where Clause According to User Type */
			query = specifyQuery(session, query);
			query += " order by x.yyyy desc, x.mm desc, x.dd desc, m.id desc";

			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement(query,
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);

				applyQueryValuesToPreparedStatement(session, ps); // specific
				// where
				// clauses
				// per user

				ResultSet rschk = ps.executeQuery();
				while (rschk.next())
					noOfRows++;
				ps.close();
				ps = conn.prepareStatement(query,
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);

				applyQueryValuesToPreparedStatement(session, ps); // specific
				// where
				// clauses
				// per user

				ResultSet rs = ps.executeQuery();
				/* Create noOfRows instances of MessageBean & one DateBean */
				MessageBean msgb[] = new MessageBean[noOfRows];
				DateBean db = null;
				for (i = 0; i < noOfRows; i++) {
					rs.next();
					/* Get sender */
					String sender = "";
					if (rs.getString("sender").equals("0")) {
						sender = "Administrator";
					} else {
						String query1 = "select x.fname as fname, x.mname as mname, x.lname as lname"
								+ " from pietons.student as m,"
								+ " xmltable('$p/personal_details/name' passing m.personal_details as \"p\""
								+ " columns fname varchar(30) path 'fname/text()',"
								+ "			mname varchar(30) path 'mname/text()',"
								+ "			lname varchar(30) path 'lname/text()') as x"
								+ " where m.gr_no=?";
						PreparedStatement ps1 = conn.prepareStatement(query1,
								ResultSet.TYPE_SCROLL_INSENSITIVE,
								ResultSet.CONCUR_READ_ONLY);
						ps1.setString(1, rs.getString("sender"));
						ResultSet rs1 = ps1.executeQuery();
						if (rs1.next()) {
							sender = rs1.getString("fname")
									+ " "
									+ (rs1.getString("mname")==null? ""
											: rs1.getString("mname")) + " "
									+ rs1.getString("lname");
						} else {
							query1 = "select x.fname as fname, x.mname as mname, x.lname as lname"
									+ " from pietons.faculty as m,"
									+ " xmltable('$p/personal_details/name' passing m.personal_details as \"p\""
									+ " columns fname varchar(30) path 'fname/text()',"
									+ "			mname varchar(30) path 'mname/text()',"
									+ "			lname varchar(30) path 'lname/text()') as x"
									+ " where m.gr_no=?";
							ps1 = conn.prepareStatement(query1);
							ps1.setString(1, rs.getString("sender"));
							rs1 = ps1.executeQuery();
							if (rs1.next()) {
								sender = rs1.getString("fname")
										+ " "
										+ (rs1.getString("mname")==null || rs1.getString("mname").equals("") || rs1.getString("mname").equals("null") ? ""
												: rs1.getString("mname")) + " "
										+ rs1.getString("lname");
							} else
								sender = "Unknown";
						}
					}
					/* Save message in msgb */
					msgb[i] = new MessageBean();
					msgb[i].setId(rs.getInt("id") + "");
					msgb[i].setSender(sender);
					msgb[i].setMsg_type(rs.getInt("msg_type"));
					msgb[i].setRecipient(rs.getString("recipient"));
					msgb[i].setRec_type(rs.getInt("rec_type") + "");
					msgb[i].setSubject(rs.getString("subject"));
					msgb[i].setContent(rs.getString("content"));
					/* Save date in msgb via db */
					db = new DateBean();
					db.setDd(rs.getString("dd"));
					db.setMm(rs.getString("mm"));
					db.setYyyy(rs.getString("yyyy"));
					msgb[i].setDate(db);
				}
				rs.close();
				ps.close();

				request.setAttribute("msgb", msgb);
				request.setAttribute("msgb_no", noOfRows);
				request
						.getRequestDispatcher(
								response
										.encodeURL("/components/messages.jsp?interface=view"))
						.forward(request, response);
			} catch (SQLException e) {
				System.out.println(e.toString());
			}
		} else if (request.getParameter("interface").toString().equals("save")) {
			/* Save New Message */
			try {
				int msg_type = Integer.parseInt(request
						.getParameter("msg_type").toString());
				if (msg_type != 2 && msg_type != 3) { // not a feedback -- store
					// in messages table
					String query = "insert into messages(id,sender,msg_type,recipient,rec_type,date,subject,content) values(default,?,?,?,?,?,?,?)";
					PreparedStatement ps = conn.prepareStatement(query);
					ps.setString(1, request.getParameter("sender"));
					ps.setInt(2, msg_type);
					ps.setString(3, request.getParameter("recipient"));
					ps.setInt(4, Integer.parseInt(request.getParameter(
							"rec_type").toString()));
					Calendar cal = Calendar.getInstance();
					ps.setString(5, "<date><dd>" + cal.get(Calendar.DATE)
							+ "</dd>" + "<mm>" + cal.get(Calendar.MONTH)
							+ "</mm><yyyy>" + cal.get(Calendar.YEAR)
							+ "</yyyy></date>");
					ps.setString(6, request.getParameter("subject"));
					ps.setString(7, request.getParameter("content"));
					ps.execute();
				} else if (msg_type == 2) { // faculty feedback -- store in
					// feedbacks column of the
					// appropriate record in faculty
					// table
					/* Extract original feedbacks xml */
					String fbxml = null;
					String query = "select xml2clob(feedbacks) from pietons.faculty where gr_no=?";
					PreparedStatement ps = conn.prepareStatement(query);
					ps.setString(1, request.getParameter("recipient"));
					ResultSet rs = ps.executeQuery();
					if (rs.next()) {
						fbxml = rs.getString(1);
					}
					/* Get last id */
					int id;
					if (fbxml == null || fbxml.equals("")
							|| fbxml.equals("<feedbacks/>"))
						id = 0;
					else {
						String query1 = "select x.id as id from pietons.faculty f,"
								+ " xmltable('$f/feedbacks' passing f.feedbacks as \"f\""
								+ " columns id integer path 'feedback/id') as x"
								+ " where f.gr_no=? order by x.id desc";
						PreparedStatement ps1 = conn.prepareStatement(query1);
						ps1.setString(1, request.getParameter("recipient"));
						ResultSet rs1 = ps1.executeQuery();
						rs1.next();
						id = rs1.getInt("id") + 1;
					}
					/* Append new entry to XML */
					if (fbxml == null || fbxml.equals("")
							|| fbxml.equals("<feedbacks/>")) {
						fbxml = "<feedbacks><feedback id=\"" + id + "\">"
								+ request.getParameter("content")
								+ "</feedback></feedbacks>";
					} else {
						fbxml = fbxml.substring(0, fbxml
								.indexOf("</feedbacks>"))
								+ "<feedback id=\""
								+ id
								+ "\">"
								+ request.getParameter("content")
								+ "</feedback></feedbacks>";
					}
					/* Update faculty record */
					String query2 = "update pietons.faculty set feedbacks=? where gr_no=?";
					PreparedStatement ps2 = conn.prepareStatement(query2);
					ps2.setString(1, fbxml);
					ps2.setString(2, request.getParameter("recipient"));
					ps2.execute();
				} else if (msg_type == 3) { // student feedback -- store in
					// feedbacks column of the
					// appropriate record in student
					// table
					/* Extract original feedbacks xml */
					String fbxml = null;
					String query = "select xml2clob(feedbacks) from pietons.student where gr_no=?";
					PreparedStatement ps = conn.prepareStatement(query);
					ps.setString(1, request.getParameter("recipient"));
					ResultSet rs = ps.executeQuery();
					if (rs.next()) {
						fbxml = rs.getString(1);
					}
					/* Get last id */
					int id;
					if (fbxml == null || fbxml.equals(""))
						id = 0;
					else {
						String query1 = "select x.id as id from pietons.student s,"
								+ " xmltable('$f/feedbacks' passing s.feedbacks as \"f\""
								+ " columns id integer path 'feedback/id') as x"
								+ " where s.gr_no=? order by x.id desc";
						PreparedStatement ps1 = conn.prepareStatement(query1);
						ps1.setString(1, request.getParameter("recipient"));
						ResultSet rs1 = ps1.executeQuery();
						rs1.next();
						id = rs1.getInt("id") + 1;
					}
					/* Append new entry to XML */
					fbxml = fbxml.substring(0, fbxml.indexOf("</feedbacks>"))
							+ "<feedback id=\"" + id + "\">"
							+ request.getParameter("content")
							+ "</feedback></feedbacks>";
					/* Update student record */
					String query2 = "update pietons.student set feedbacks=? where gr_no=?";
					PreparedStatement ps2 = conn.prepareStatement(query2);
					ps2.setString(1, fbxml);
					ps2.setString(2, request.getParameter("recipient"));
					ps2.execute();
				}
				request
						.getRequestDispatcher(
								response
										.encodeURL("/components/messages.jsp?interface=save"))
						.forward(request, response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
			}

		}
	}

	private void applyQueryValuesToPreparedStatement(HttpSession session,
			PreparedStatement ps) throws SQLException {
		if (session.getAttribute("user_type").toString().equals("student")) {
			/* Student specific search */
			ps.setString(1, session.getAttribute("user_id").toString());
			ps.setString(2, session.getAttribute("class_id").toString());
			ps.setString(3, session.getAttribute("branch_id").toString());
			ps.setString(4, session.getAttribute("course_id").toString());
		} else if (session.getAttribute("user_type").toString().equals(
				"faculty")
				|| session.getAttribute("user_type").toString().equals("hod")
				|| session.getAttribute("user_type").toString().equals("dean")) {
			/* Faculty/HOD/Dean specific search */
			ps.setString(1, session.getAttribute("user_id").toString());
			ps.setString(2, session.getAttribute("branch_id").toString());
		} else if (session.getAttribute("user_type").toString().equals("admin")) {
			/* Data Manager specific search */
		}
	}

	private String specifyQuery(HttpSession session, String query) {
		if (session.getAttribute("user_type").toString().equals("student")) {
			/* Student specific search */
			/* Individual Recipient: user_id */
			query += " where (m.recipient=? and m.rec_type=0)"
			/* Class-wide Students: class_id */
			+ " or (m.recipient=? and m.rec_type=1)"
			/* Branch-wide Students: branch_id */
			+ " or (m.recipient=? and m.rec_type=2)"
			/* Course-wide Students: course_id */
			+ " or (m.recipient=? and m.rec_type=4)"
			/* All Students */
			+ " or (m.rec_type=5)"
			/* Entire College */
			+ " or (m.rec_type=7)" + "";
		} else if (session.getAttribute("user_type").toString().equals(
				"faculty")
				|| session.getAttribute("user_type").toString().equals("hod")
				|| session.getAttribute("user_type").toString().equals("dean")) {
			/* Faculty/HOD/Dean specific search */
			/* Individual Recipient: user_id */
			query += " where (m.recipient=? and m.rec_type=0)"
			/* Branch-wide Faculty: branch_id */
			+ " or (m.recipient=? and m.rec_type=3)"
			/* All faculty */
			+ " or (m.rec_type=6)"
			/* Entire College */
			+ " or (m.rec_type=7)" + "";
		} else if (session.getAttribute("user_type").toString().equals("admin")) {
			/* Data Manager specific search */
			/* Administrator */
			query += " where m.rec_type=7" + " or (m.rec_type=8)" + "";
		}
		return query;
	}
}
