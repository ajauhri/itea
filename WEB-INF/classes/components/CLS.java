/* Component: Check Login Status (CLS)
 * Checks whether user is logged in, which user is logged in, etc.
 * Also automatically checks if the user has logged in for the first time, if any user is at all logged in. Automatically 
 *  redirects to Manage Accounts page if so.
 * 
 * Check if logged in or not - redirect to login page if not
 * 		request.getRequestDispatcher(response.encodeURL("components/CLS?returnType=redirect")).forward(request,response);
 * Autodirects to site home/login page if not logged in.
 * Check using:
 * 		response.isCommitted();
 * 
 * Check if logged in or not - return attribute "status" true/false
 * 		request.getRequestDispatcher(response.encodeURL("components/CLS?returnType=status")).forward(request,response);
 * Simply sets a status flag saying if user is logged in or not, leaving all the handling to the caller.
 * Return value can be accessed by request attribute "status":
 * 		(Boolean)request.getAttribute("status");
 * Don't forget to remove the attribute after usage:
 * 		request.removeAttribute("status");
 * 
 * Check if user logged in is of user_type
 * 		request.getRequestDispatcher(response.encodeURL("components/CLS?user_type=xyz&returnType=redirect")).forward(request,response); * 
 * Use: call this on beginning of every servlet's doGet and doPost methods specific to a particular user type. 
 * 
 * Check if user is logging in for the first time
 * 		request.getRequestDispatcher(response.encodeURL("components/CLS?firstTime=true&user_type=xyz&returnType=status")).forward(request,response); * 
 * 		request.getRequestDispatcher(response.encodeURL("components/CLS?firstTime=true&returnType=redirect")).forward(request,response); * 
 * Use: call this on beginning of every servlet's doGet and doPost methods specific to a particular user type. 
 * 
 */

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
 * Servlet implementation class CLS
 */
public class CLS extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CLS() {
		super();
		// TODO Auto-generated constructor stub
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
		/* Gather session */
		HttpSession session = request.getSession();
		// returnType: 0 - auto redirect, 1 - status attribute
		int returnType=0;
		if(request.getParameter("returnType")!=null && request.getParameter("returnType").equals("redirect"))
			returnType=0;
		else if(request.getParameter("returnType")!=null && request.getParameter("returnType").equals("status"))
			returnType=1;
		else{
			request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=server")).forward(request,response);
			return;
		}
		String user_type=request.getParameter("user_type");
		boolean firstTime=(request.getParameter("firstTime")!=null)?true:false;
		
		if(returnType==1){
			/* Request: login + status */
			if(isLoggedIn(request, session)){
				if(!firstTime && user_type==null) 
					request.setAttribute("status",true);
				else if(firstTime && user_type==null){	// first time check requested only
					if(isFirstTimeLogin(request, response, session))	// yes, first time log in
						request.setAttribute("status",true);
					else request.setAttribute("status",false);
				}else if(!firstTime && user_type!=null){	// user type check requested only
					if(user_type.equals(session.getAttribute("user_type").toString()))
						request.setAttribute("status",true);
					else request.setAttribute("status",false);
				}else if(firstTime && user_type!=null){	// both first time & user type checks requested
					if(isFirstTimeLogin(request, response, session) && user_type.equals(session.getAttribute("user_type").toString()))
						request.setAttribute("status",true);
					else request.setAttribute("status",false);
				}
			}else request.setAttribute("status",false);
			return;
		}else if(returnType==0){
			/* Request: login + redirect */
			if(isLoggedIn(request, session)){
				if(!firstTime && user_type==null)
					return;
				else if(firstTime && user_type==null){	// first time check requested only
					if(isFirstTimeLogin(request, response, session)){	// yes, first time log in
						request.getRequestDispatcher(response.encodeURL("/components/manageAccount.jsp?firstTime=true")).forward(request,response);
						return;
					}else return;
				}else if(!firstTime && user_type!=null){	// user type check requested only
					if(user_type.equals(session.getAttribute("user_type").toString()))
						return;
					else{
						request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=unauthorized")).forward(request,response);
						return;
					}
				}else if(firstTime && user_type!=null){	// both first time & user type checks requested
					if(isFirstTimeLogin(request, response, session) && user_type.equals(session.getAttribute("user_type").toString())){
						request.getRequestDispatcher(response.encodeURL("/components/manageAccount.jsp?firstTime=true")).forward(request,response);
						return;
					}else if(!isFirstTimeLogin(request, response, session) && user_type.equals(session.getAttribute("user_type").toString())){
							return;
					}else{	// user type doesnt match
						request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=unauthorized")).forward(request,response);
						return;
					}
				}
			}else request.getRequestDispatcher(response.encodeURL("/components/error.jsp?type=unauthorized")).forward(request,response);
			return;
		}
		
	}
	
	private boolean isFirstTimeLogin(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		/* Get connection */
		// HttpSession session = request.getSession();
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
		String query = "select changed from pietons.users where gr_no=?";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, session.getAttribute("user_id").toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next() && rs.getInt("changed") == 0) {
				/* First Time Login */
				return true;
			}else return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}
		return false;
	}

	private boolean isLoggedIn(HttpServletRequest request, HttpSession session) {
		/* Vanilla check */
		if (session != null) {
			if (session.getAttribute("user_id") != null) {
				/* User is logged in */
				return true;
				/* Else user is not logged in */
			} else
				return false;
		} else
			return false;
	}
}
