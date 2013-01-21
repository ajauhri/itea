package faculty;

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

/**
 * Servlet implementation class TimeTablePersonal
 */
public class TimeTablePersonal extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TimeTablePersonal() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Check Login Status */
		request
				.getRequestDispatcher(
						"/components/CLS?user_type=faculty&firstTime=true&returnType=redirect")
				.include(request, response);
		if (response.isCommitted())
			return; // CLS preempting
		/* Get Connection */
		HttpSession session = request.getSession();
		Connection conn = null;
		synchronized (session) {
			DBConnector dbcon = (DBConnector) session.getAttribute("dbcon"); // fetch

			if (dbcon == null) {
				dbcon = new DBConnector();
				session.setAttribute("dbcon", dbcon);
			}
			conn = dbcon.getConnection();
		}

		String query = "select B.class_id, "
				+ "xmlserialize(content xmlelement (name \"day\", "
				+ "				xmlattributes(A.d_id as \"d_id\"), "
				+ "				xmlagg(xmlelement(name \"slot\", "
				+ "						xmlattributes(A.no as \"no\"), "
				+ "						xmlelement(name \"subject_name\", su.name), "
				+ "						xmlelement(name \"teacher_name\", na.fname||' '|| na.mname||' '||na.lname), "
				+ "						xmlelement(name \"infra_name\", in.name) ))) as clob) "
				+ "from pietons.faculty fa, "
				+ "xmltable('$c/personal_details' passing fa.personal_details as \"c\" "
				+ "columns fname varchar(20) path './name/fname', "
				+ "		mname varchar(20) path './name/mname', "
				+ "		lname varchar(20) path './name/lname') as na, "
				+ "pietons.subject su, pietons.infrastructure in, pietons.allocation B, "
				+ "xmltable ('$d/schedule/day/slot' passing B.schedule as \"d\" "
				+ "columns d_id varchar(2) path '../@d_id', "
				+ "		subject_id int path './subject_id', "
				+ "		no varchar(2) path './@no', "
				+ "		teacher_id varchar(10) path './teacher_id', "
				+ "		infra_id int path './infra_id') as A "
				+ "where A.teacher_id = fa.gr_no and A.subject_id = su.id and A.infra_id=in.id and fa.gr_no=?"
				+ "group by B.class_id, A.d_id";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, session.getAttribute("user_id").toString());
			ResultSet rs = ps.executeQuery();
			PrintWriter out = response.getWriter();
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
					+ "<?xml-stylesheet type=\"text/xsl\" href=\"/ITEA/components/tt.xsl\" ?>"
					+ "<schedule>");
			while (rs.next())
				out.println(rs.getString(2));
			out.println("</schedule>");
		} catch (SQLException e) {
			System.err.println(e.toString());
		}
	}

}
