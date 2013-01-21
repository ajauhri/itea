package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.DBConnector;
import beans.DateBean;
import beans.PersonalDetailBean;
import beans.StudentBean;

import components.GrNo;
import components.Users;

/**
 * Servlet implementation class StudentRecords
 */
public class StudentRecords extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StudentRecords() {
		super();
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
		// Determine if ajax or ordinary hyperlink
		if (request.getParameter("ajax") == null
				|| request.getParameter("ajax").equals("")) {
			doPost(request, response);
			return;
		} else if (request.getParameter("do") != null
				&& request.getParameter("do").equals("delete")) {
			/* Delete a Record */
			String query = "delete from pietons.student where gr_no=?";
			try {
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setString(1, request.getParameter("gr_no"));
				ps.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
				request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=database")).forward(request,response);
				return;
			}
			String query1 = "delete from pietons.users where gr_no=?";
			try {
				PreparedStatement ps1 = conn.prepareStatement(query1);
				ps1.setString(1, request.getParameter("gr_no"));
				ps1.execute();
				return;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
				request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=server")).forward(request,response);
				return;
			}
		} else if (request.getParameter("do") != null
				&& request.getParameter("do").equals("edit")) {
			/* Edit a Record */
			String field = request.getParameter("field");
			String gr_no = request.getParameter("gr_no");
			String newContent = request.getParameter("newContent");
			/*
			 * Update users table - the student table will be updated after that
			 */
			if (field.equals("gr_no")) { // Check if new gr_no entered is unique
				if (!GrNo.isNew(newContent)) // not unique
					return;
				Users.updateGrNo(gr_no, newContent);
			}
			if (field.equals("dd") || field.equals("mm")
					|| field.equals("yyyy")) {
				Users.updateDoB(gr_no, field, newContent);
			}
			/* Update student table */
			if (field.equals("gr_no") || field.equals("roll_no")
					|| field.equals("class_id")
					|| field.equals("year_of_admission")) {
				/* Direct SQL Updates */
				String query = "update pietons.student set " + field
						+ "=? where gr_no=?";
				PreparedStatement ps;
				try {
					ps = conn.prepareStatement(query);
					ps.setString(1, newContent);
					ps.setString(2, gr_no);
					ps.execute();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println(e.toString());
					request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=form")).forward(request,response);
					return;
				}
			} else {
				try {
					/* Extract personal details xml */
					String query = "select personal_details from pietons.student where gr_no=?";
					PreparedStatement ps = conn.prepareStatement(query);
					ps.setString(1, gr_no);
					ResultSet rs = ps.executeQuery();
					rs.next();
					String pdxml = rs.getString(1);
					/* Update xml */
					// Check if original value is null
					if (pdxml.indexOf("</" + field + ">") != -1) {
						pdxml = pdxml.substring(0, pdxml.indexOf("<" + field
								+ ">")
								+ field.length() + 2)
								+ newContent
								+ pdxml.substring(pdxml.indexOf("</" + field
										+ ">"), pdxml.length());
					} else { // Xml is automatically self-closed: <field/>
						pdxml = pdxml.substring(0, pdxml.indexOf("<" + field
								+ "/>")
								+ field.length() + 1)
								+ ">"
								+ newContent
								+ "</"
								+ field
								+ ">"
								+ pdxml.substring(pdxml.indexOf("<" + field
										+ "/>")
										+ field.length() + 3, pdxml.length());
					}
					/* Save update */
					String query1 = "update pietons.student set personal_details=? where gr_no=?";
					PreparedStatement ps1 = conn.prepareStatement(query1);
					ps1.setString(1, pdxml);
					ps1.setString(2, gr_no);
					ps1.execute();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println(e.toString());
					request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=form")).forward(request,response);
					return;
				}
			}
		} else if (request.getParameter("list") != null
				&& request.getParameter("list").equals("branch")) {
			request.getRequestDispatcher("/components/BranchList?course_id="+request.getParameter("course_id")).forward(
					request, response);
		} else if (request.getParameter("list") != null
				&& request.getParameter("list").equals("class")) {
			request.getRequestDispatcher(
					"/components/ClassList?branch_id="
							+ request.getParameter("branch_id")).forward(
					request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/* Check Login Status */
		request.getRequestDispatcher("/components/CLS?user_type=admin&returnType=redirect").include(request,response);
		if(response.isCommitted()) return;	// CLS preempting
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
				&& request.getParameter("do").equals("add")) {
			/* Add New Student */
			request.getRequestDispatcher(
					response.encodeURL("/admin/studentRecords.jsp?do=add")).forward(
					request, response);
			return;
		} else if (request.getParameter("do") != null
				&& request.getParameter("do").equals("save")) {
			/* Save New Record */
			String gr_no = request.getParameter("gr_no");
			if (gr_no == null)
				gr_no = GrNo.generateStudent(Integer.parseInt(request
						.getParameter("class_id")), Integer.parseInt(request
						.getParameter("year_of_admission")));
			else if(!GrNo.isNew(gr_no)) {
				// TODO error page -- duplicate gr no
				request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=duplicate")).forward(request,response);
				return;
			}
			String query = "insert into pietons.student(gr_no, roll_no, class_id, year_of_admission, personal_details, feedbacks) values(?,?,?,?,?,?)";
			String personal_details = "<personal_details>" + "	<name>"
					+ "	<fname>"
					+ request.getParameter("fname")
					+ "</fname>"
					+ "<mname>"
					+ request.getParameter("mname")
					+ "</mname>"
					+ "<lname>"
					+ request.getParameter("lname")
					+ "</lname>"
					+ "</name>"
					+ "<address>"
					+ "<house_no>"
					+ request.getParameter("house_no")
					+ "</house_no>"
					+ "<street>"
					+ request.getParameter("street")
					+ "</street>"
					+ "<city>"
					+ request.getParameter("city")
					+ "</city>"
					+ "<pincode>"
					+ request.getParameter("pincode")
					+ "</pincode>"
					+ "<state>"
					+ request.getParameter("state")
					+ "</state>"
					+ "</address>"
					+ "<contact_details>"
					+ "<phone_no>"
					+ request.getParameter("phone_no")
					+ "</phone_no>"
					+ "<email>"
					+ request.getParameter("email")
					+ "</email>"
					+ "</contact_details>"
					+ "<dob>"
					+ "<dd>"
					+ request.getParameter("dd")
					+ "</dd>"
					+ "<mm>"
					+ request.getParameter("mm")
					+ "</mm>"
					+ "<yyyy>"
					+ request.getParameter("yyyy")
					+ "</yyyy>"
					+ "</dob>"
					+ "</personal_details>";
			try {
				/* Insert into student database */
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setString(1, gr_no);
				ps.setInt(2, Integer.parseInt(request.getParameter("roll_no")));
				ps
						.setInt(3, Integer.parseInt(request
								.getParameter("class_id")));
				ps.setInt(4, Integer.parseInt(request
						.getParameter("year_of_admission")));
				ps.setString(5, personal_details);
				ps.setString(6, "<feedbacks></feedbacks>");
				ps.execute();
				/* Insert into users table */
				DateBean db = new DateBean();
				db.setDd(request.getParameter("dd"));
				db.setMm(request.getParameter("mm"));
				db.setYyyy(request.getParameter("yyyy"));
				Users.insert(gr_no, "student", db);
				/* Redirect */
				request.getRequestDispatcher(
						response.encodeURL("/admin/studentRecords.jsp?do=save"))
						.forward(request, response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
				request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=form")).forward(request,response);
				return;
			}
			return;
		}
		/* Show list of student */
		if (request.getParameter("start") == null
				|| request.getParameter("start").equals("")
				|| request.getParameter("perpage") == null
				|| request.getParameter("perpage").equals("")) {
			response.sendRedirect(response
					.encodeURL("/ITEA/admin/StudentRecords?start=0&perpage=10"));
			return;
		}
		/* Extract List of Students */
		String order_by = "";
		if (request.getParameter("order_by") != null) {
			String req_ob = request.getParameter("order_by");
			if (req_ob.equals("gr_no") || req_ob.equals("roll_no")
					|| req_ob.equals("class_id")
					|| req_ob.equals("year_of_admission"))
				order_by = "s." + req_ob;
			else
				order_by = "x." + req_ob;
		} else
			order_by = "s.gr_no";
		String query = "select s.gr_no as gr_no, s.roll_no roll_no, s.class_id as class_id, s.year_of_admission as year_of_admission,"
				+ " x.fname as fname, x.mname as mname, x.lname as lname,"
				+ " x.house_no as house_no, x.street as street, x.city as city, x.pincode as pincode, x.state as state,"
				+ " x.phone_no as phone_no, x.email as email,"
				+ " x.dd as dd, x.mm as mm, x.yyyy as yyyy from pietons.student s,"
				+ " xmltable('$p/personal_details' passing s.personal_details as \"p\""
				+ " columns fname varchar(30) path 'name/fname/text()',"
				+ "		mname varchar(30) path 'name/mname/text()',"
				+ "		lname varchar(30) path 'name/lname/text()',"
				+ "		house_no varchar(30) path 'address/house_no/text()',"
				+ "		street varchar(30) path 'address/street/text()',"
				+ "		city varchar(30) path 'address/city/text()',"
				+ "		pincode varchar(30) path 'address/pincode/text()',"
				+ "		state varchar(30) path 'address/state/text()',"
				+ "		phone_no varchar(30) path 'contact_details/phone_no/text()',"
				+ "		email varchar(30) path 'contact_details/email/text()',"
				+ "		dd varchar(2) path 'dob/dd/text()',"
				+ "		mm varchar(2) path 'dob/mm/text()',"
				+ "		yyyy varchar(4) path 'dob/yyyy/text()') as x"
				+ " order by "
				+ order_by;
		Statement st;
		int noOfRows = 0;
		try {
			st = conn.createStatement();
			/* Find No. of Records */
			ResultSet rschk = st.executeQuery(query);
			while (rschk.next()) {
				noOfRows++;
			}
			/* Instantiate noOfRows Number of StudentBean Objects */
			StudentBean[] sb = new StudentBean[noOfRows];
			PersonalDetailBean pb = null;
			DateBean db = null;
			ResultSet rs = st.executeQuery(query);
			for (int i = 0; i < noOfRows; i++) {
				rs.next();

				sb[i] = new StudentBean();
				sb[i].setGr_no(rs.getString("gr_no"));
				sb[i].setRoll_no(rs.getInt("roll_no"));
				sb[i].setClass_id(rs.getInt("class_id"));
				sb[i].setYear_of_admission(rs.getInt("year_of_admission"));

				db = new DateBean();
				db.setDd(rs.getString("dd"));
				db.setMm(rs.getString("mm"));
				db.setYyyy(rs.getString("yyyy"));

				pb = new PersonalDetailBean();
				pb.setFname(rs.getString("fname"));
				pb.setMname(rs.getString("mname"));
				pb.setLname(rs.getString("lname"));
				pb.setHouse_no(rs.getString("house_no"));
				pb.setStreet(rs.getString("street"));
				pb.setCity(rs.getString("city"));
				pb.setPincode(rs.getString("pincode"));
				pb.setState(rs.getString("state"));
				pb.setPhone_no(rs.getString("phone_no"));
				pb.setEmail(rs.getString("email"));
				pb.setDob(db);

				sb[i].setPersonal_details(pb);
			}

			request.setAttribute("sb", sb);
			request.setAttribute("sb_no", noOfRows);
			request.setAttribute("perpage", Integer.parseInt(request
					.getParameter("perpage").toString()));
			request.setAttribute("start", Integer.parseInt(request
					.getParameter("start").toString()));
			request.getRequestDispatcher(
					response.encodeURL("/admin/studentRecords.jsp")).forward(request,
					response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=database")).forward(request,response);
			return;
		}
	}
}
