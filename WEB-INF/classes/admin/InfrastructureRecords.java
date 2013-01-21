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

import beans.InfrastructureBean;
import beans.DBConnector;

/**
 * Servlet implementation class InfrastructureRecords
 */
public class InfrastructureRecords extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InfrastructureRecords() {
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
			String query = "delete from pietons.infrastructure where id=?";
			try {
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setString(1, request.getParameter("id"));
				ps.execute();
				return;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
				request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=database")).forward(request,response);
				return;
			}
		} else if (request.getParameter("do") != null
				&& request.getParameter("do").equals("edit")) {
			/* Edit a Record */
			String field = request.getParameter("field");
			int infra_id = Integer.parseInt(request.getParameter("infra_id"));
			String newContent = request.getParameter("newContent");
			String query = "update pietons.infrastructure set " + field
					+ "=? where id=?";
			PreparedStatement ps;
			try {
				ps = conn.prepareStatement(query);
				ps.setString(1, newContent);
				ps.setInt(2, infra_id);
				ps.execute();
				return;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
				request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=form")).forward(request,response);
				return;
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
			/* Add New Infrastructure */
			request.getRequestDispatcher(
					response.encodeURL("/admin/infrastructureRecords.jsp?do=add")).forward(
					request, response);
			return;
		} else if (request.getParameter("do") != null
				&& request.getParameter("do").equals("save")) {
			/* Save New Record */
			String query = "insert into pietons.infrastructure(infra_type, capacity, name) values(?,?,?)";
			try {
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setInt(1, Integer.parseInt(request.getParameter("infra_type")));
				ps.setInt(2, Integer.parseInt(request.getParameter("capacity")));
				ps.setString(3, request.getParameter("name"));
				ps.execute();
				request.getRequestDispatcher(
						response.encodeURL("/admin/infrastructureRecords.jsp?do=save"))
						.forward(request, response);
				return;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
				request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=form")).forward(request,response);
				return;
			}
		}
		/* Show list of infrastructures */
		if (request.getParameter("start") == null
				|| request.getParameter("start").equals("")
				|| request.getParameter("perpage") == null
				|| request.getParameter("perpage").equals("")) {
			response.sendRedirect(response
					.encodeURL("/ITEA/admin/InfrastructureRecords?start=0&perpage=10"));
			return;
		}
		/* Extract List of Infrastructures */
		String order_by = "";
		if (request.getParameter("order_by") != null) {
			order_by = request.getParameter("order_by");
		} else
			order_by = "id";
		String query = "select id, infra_type, capacity, name from pietons.infrastructure order by "
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
			/* Instantiate noOfRows Number of InfrastructureBean Objects */
			InfrastructureBean[] ib = new InfrastructureBean[noOfRows];
			ResultSet rs = st.executeQuery(query);
			for (int i = 0; i < noOfRows; i++) {
				rs.next();

				ib[i] = new InfrastructureBean();
				ib[i].setId(rs.getInt("id"));
				ib[i].setInfra_type(rs.getInt("infra_type"));
				ib[i].setCapacity(rs.getInt("capacity"));
				ib[i].setName(rs.getString("name"));
			}

			request.setAttribute("ib", ib);
			request.setAttribute("ib_no", noOfRows);
			request.setAttribute("perpage", Integer.parseInt(request
					.getParameter("perpage").toString()));
			request.setAttribute("start", Integer.parseInt(request
					.getParameter("start").toString()));
			request.getRequestDispatcher(
					response.encodeURL("/admin/infrastructureRecords.jsp")).forward(request,
					response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=database")).forward(request,response);
			return;
		}
	}
}
