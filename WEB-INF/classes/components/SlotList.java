/* Component: SlotList
 * Displays the list of slots as an <option> tag set. Must be included inside an html <select> using:
 * 		<jsp:include page="/components/SlotList" />
 */

package components;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SlotList
 */
public class SlotList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SlotList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String output="";
		String[] slot_duration={"8:00am - 9:00am","9:00am - 10:00am","10:00am - 11:00am","11:00am - 12:00pm","12:00pm - 1:00pm","1:00pm - 2:00pm","2:00pm - 3:00pm","3:00pm - 4:00pm","4:00pm - 5:00pm","5:00pm - 6:00pm"};
		for(int i=0;i<slot_duration.length;i++){
			output+="<option value='"+(i+1)+"'>";
			output+=slot_duration[i];
			output+="</option>";
		}
		response.getWriter().write(output);
	}

}
