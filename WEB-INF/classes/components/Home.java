package components;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Home
 */
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Home() {
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
		/* Login error */
		if (request.getParameter("error") != null
				&& request.getParameter("error").equals("true")) {
			request.getRequestDispatcher("/components/home.jsp?error=true")
					.forward(request, response);
			return;
		}
		/* Check login status */
		request.getRequestDispatcher(
				response.encodeURL("/components/CLS?returnType=status"))
				.include(request, response);
		if ((Boolean) request.getAttribute("status")) {
			/* User logged in */
			/* Check if first time log in */
			request
					.getRequestDispatcher(
							response
									.encodeURL("/components/CLS?returnType=redirect&firstTime=true"))
					.include(request, response);
			if (!response.isCommitted()) {
				/* No, proceed to home */
				HttpSession session = request.getSession();
				request.getRequestDispatcher(
						response.encodeURL("/"
								+ session.getAttribute("user_type").toString()
								+ "/home.jsp")).forward(request, response);
				return;
			}
		} else	// show generic home
			request.getRequestDispatcher("/components/home.jsp").forward(
					request, response);
		request.removeAttribute("status");
		return;
	}

}
