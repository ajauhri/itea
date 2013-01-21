package student;

/* Functions:
 * - Corresponds to servlets for retrieval of values needed for allotting examination schedule
 * - Makes an entry into the examination table
 */

import java.io.IOException;
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
import beans.DateBean;
import beans.ExaminationBean;
import beans.TimeBean;

public class ExaminationStudent extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ExaminationStudent() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/* Check Login Status */
		request.getRequestDispatcher("/components/CLS?user_type=student")
				.include(request, response);
		if (!(Boolean) request.getAttribute("status")) {
			// unauthorized access - login first
			request.getRequestDispatcher(
					"/components/error.jsp?type=unauthorized").forward(request,
					response);
			return;
		}
		HttpSession session = request.getSession();
		int noOfRows = 0, i = 0;
		Connection conn = null;
		synchronized (session) {
			DBConnector dbcon = (DBConnector) session.getAttribute("dbcon"); // fetch

			if (dbcon == null) { // if none found, create a new one
				dbcon = new DBConnector();
				session.setAttribute("dbcon", dbcon);
			}
			conn = dbcon.getConnection();
		}
		/* Retrieve messages: xml to relational table conversion */
		String query = "select h.id as id, h.subject_id as subject_id, h.duration as duration,"
				+ " d.dd as dd, d.mm as mm, d.yyyy as yyyy,"
				+ " t.hh as hh, t.mm as min, t.ss as ss"
				+ " from pietons.examination h,"
				+ " xmltable('$c/date' passing h.date as \"c\""
				+ " columns dd varchar(10) path 'dd/text()',"
				+ "		mm varchar(10) path 'mm/text()',"
				+ "		yyyy varchar(10) path 'yyyy/text()')as d,"
				+ " xmltable('$d/time' passing h.time as \"d\""
				+ " columns hh varchar(10) path 'hh/text()',"
				+ "		mm varchar(10) path 'mm/text()',"
				+ "		ss varchar(10) path 'ss/text()') as t"
				+ " where h.subject_id in (select s.id from pietons.subject s where class_id=?)";

		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(query);
			session.setAttribute("class_id", 1);
			ps.setString(1, session.getAttribute("class_id").toString());
			ResultSet rschk = ps.executeQuery();
			while (rschk.next())
				noOfRows++;
			ps = conn.prepareStatement(query);
			ps.setString(1, session.getAttribute("class_id").toString());

			ResultSet rs = ps.executeQuery();

			// Instances for ExaminationBean, DateBean, TimeBean
			ExaminationBean eb[] = new ExaminationBean[noOfRows];

			DateBean db = null;
			TimeBean tb = null;

			for (i = 0; i < noOfRows; i++) {
				rs.next();

				eb[i] = new ExaminationBean();
				eb[i].setId(rs.getString("id"));
				eb[i].setSubjectId(rs.getString("subject_id"));
				eb[i].setDuration(rs.getString("duration"));
				db = new DateBean();
				db.setDd(rs.getString("dd"));
				db.setMm(rs.getString("mm"));
				db.setYyyy(rs.getString("yyyy"));
				eb[i].setDateBean(db);
				tb = new TimeBean();
				tb.setHh(rs.getInt("hh"));
				tb.setMm(rs.getInt("min"));
				tb.setSs(rs.getInt("ss"));
				eb[i].setTimeBean(tb);
			}
			request.setAttribute("eb", eb);
			request.setAttribute("eb_no", noOfRows);

			request.getRequestDispatcher(
					response.encodeURL("/student/examinationStudent.jsp"))
					.forward(request, response);
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
	}

}
