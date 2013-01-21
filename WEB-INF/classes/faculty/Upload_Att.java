package faculty;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import components.Barcode_attendance;

public class Upload_Att extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		/* Check Login Status */
		request
				.getRequestDispatcher(
						"/components/CLS?user_type=faculty&returnType=redirect&firstTime=true")
				.include(request, response);
		if (response.isCommitted())
			return; // CLS preempt
		/* Show interface */
		request.getRequestDispatcher(response.encodeURL("upload_attendance.jsp")).forward(request,response);
	}
	
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/* Check Login Status */
		request
				.getRequestDispatcher(
						"/components/CLS?user_type=faculty&returnType=redirect&firstTime=true")
				.include(request, response);
		if (response.isCommitted())
			return; // CLS preempt
		String path = this.getServletContext().getRealPath(
				this.getServletInfo())
				+ "/uploads/";
		try {
			MultipartRequest multi = new MultipartRequest(request, path,
					2000 * 1024 * 1024,
					new com.oreilly.servlet.multipart.DefaultFileRenamePolicy());

			Enumeration params = multi.getParameterNames();
			String day = "", month = "", year = "", slot = "", subject_id = "";
			while (params.hasMoreElements()) {
				String name = (String) params.nextElement();
				String value = multi.getParameter(name);
				if (name.equals("dd"))
					day = value;
				if (name.equals("mm"))
					month = value;
				if (name.equals("yy"))
					year = value;
				if (name.equals("slot"))
					slot = value;
				if (name.equals("subject"))
					subject_id = value;
			}

			String separator = "/";
			String date = day + separator + month + separator + year;

			Enumeration files = multi.getFileNames();

			String name = (String) files.nextElement();
			String filename = multi.getFilesystemName(name);
//			String original = multi.getOriginalFileName(name);
			String type = multi.getContentType(name);

			File f = multi.getFile(name);

			if (f != null) {
				if (type.indexOf("text/") == -1) {
					f.delete();
					request
							.getRequestDispatcher(
									response
											.encodeURL("upload_attendance.jsp?error=filetype"))
							.forward(request, response);
					return;
				}

				Barcode_attendance bar = new Barcode_attendance(date,
						subject_id, slot, path, filename);
				request.setAttribute("date", date);
				request.setAttribute("slot", slot);
				request.setAttribute("attenders", bar.gr_nos);
				request.setAttribute("subject_id", subject_id);

				f.delete();
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		request.getRequestDispatcher(response.encodeURL("Attendance?do=save")).forward(request,response);
	}
}
