package components;

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

/**
 * Servlet implementation class ManageAccount
 */
public class ManageAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ManageAccount() {
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
		request.getRequestDispatcher("/components/CLS?returnType=redirect").include(request,
				response);
		if(response.isCommitted()) return;	// Not logged in
		/* Check first time */
		request.getRequestDispatcher("/components/CLS?returnType=status&firstTime=true").include(request,
				response);
		String username = request.getParameter("new_username");
		if((Boolean)request.getAttribute("status")){
			/* First time log in */
			if(username!=null){ // username was passed: we are assured that:
				/* Came here for first-time upgrades, sire. Honest. */
			}else{	/* Must redirect for first time log in things */
				request.getRequestDispatcher("/components/manageAccount.jsp?firstTime=true").include(request,
					response);
				return;
			}

		}
		/* Ordinary Request */
		if (request.getParameter("do") == null) {
			request.getRequestDispatcher(
					response.encodeURL("/components/manageAccount.jsp"))
					.forward(request, response);
		} else if (request.getParameter("do").equals("save")) {
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

			try {
				if(request.getParameter("curr_password")!=null && !request.getParameter("curr_password").equals("") ){
					/* Check Previous Password */
					String curr_password=MD5Hash.generateMD(request.getParameter("curr_password"));
					String queryP="select * from pietons.users where password=? and gr_no=?";
					PreparedStatement psP=conn.prepareStatement(queryP);
					psP.setString(1,curr_password);
					psP.setString(2,session.getAttribute("user_id").toString());
					ResultSet rsP=psP.executeQuery();
					if(rsP==null || !rsP.next()){
						String url="/components/manageAccount.jsp?error=password";
						if(username!=null) url+="&firstTime=true";
						request.getRequestDispatcher(response.encodeURL(url)).forward(request,response);
						return;
					}
				}
				/* Check Username Uniqueness for First Time username Change */
				if(username!=null && !username.equals("")){
					String queryU="select * from pietons.users where username=?";
					PreparedStatement psU=conn.prepareStatement(queryU);
					psU.setString(1,username);
					ResultSet rsU=psU.executeQuery();
					if(rsU!=null && rsU.next()){
						request.getRequestDispatcher(response.encodeURL("/components/manageAccount.jsp?error=username&firstTime=true")).forward(request,response);
						return;
					}
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				System.out.println(e1.toString());
			}

			/* Changing username */
			if(username!=null && !username.equals("")){
				String query1="update pietons.users set username=? where gr_no=?";
				try {
					PreparedStatement ps1=conn.prepareStatement(query1);
					ps1.setString(1,username);
					ps1.setString(2,session.getAttribute("user_id").toString());
					ps1.execute();
					/* Update Session */
					session.setAttribute("username",username);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println(e.toString());
				}
			}
			/* De-setting "first-time" notice */
			if(username!=null && !username.equals("")){
				String query1="update pietons.users set changed=? where gr_no=?";
				try {
					PreparedStatement ps1=conn.prepareStatement(query1);
					ps1.setInt(1,1);
					ps1.setString(2,session.getAttribute("user_id").toString());
					ps1.execute();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println(e.toString());
				}
			}
			/* Changing password */
			if(request.getParameter("curr_password")!=null && !request.getParameter("curr_password").equals("")){
				String password=MD5Hash.generateMD(request.getParameter("new_password"));
				String query2 = "update pietons.users set password=? where gr_no=?";
				try {
					PreparedStatement ps2 = conn.prepareStatement(query2);
					ps2.setString(1,password);
					ps2.setString(2,session.getAttribute("user_id").toString());
					ps2.execute();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println(e.toString());
				}
			}
			/* Changing theme */
			if(request.getParameter("theme")!=null && !request.getParameter("theme").equals("")){
				String query3="update pietons.users set theme=? where gr_no=?";
				PreparedStatement ps3;
				try {
					ps3 = conn.prepareStatement(query3);
					ps3.setInt(1,Integer.parseInt(request.getParameter("theme")));
					ps3.setString(2,session.getAttribute("user_id").toString());
					ps3.execute();
					/* Update Session */
					session.setAttribute("theme",Integer.parseInt(request.getParameter("theme")));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println(e.toString());
				}
			}
			response.sendRedirect(response.encodeURL("/ITEA/components/Home"));
		}
	}
}