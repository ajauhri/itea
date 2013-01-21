<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ITEA - Faculty | Lecture Time Preferences</title>
<jsp:include page="/components/style.jsp" />
</head>
<body>
<jsp:include page="/components/navbar.jsp" />

<%
	if((Integer)session.getAttribute("fac_type")==0){
%>
This facility is only available for visiting faculty. If you'd still like to make a request, you can contact the H.O.D. through <a href="<%=response.encodeURL("/ITEA/components/Messages") %>">messages</a>.<br />
<%		
	}else{
%>

Choose a day and time slot you prefer for conduction of your lectures. Only visiting faculty have this facility.<br />
Enter as many preferences as desired.<br />
You must declare your preferences prior to the fixing of the time table by the respective H.O.D. You can coordinate with the H.O.D. through <a href="<%=response.encodeURL("/ITEA/components/Messages") %>">messages</a>.<br />
Note, the H.O.D. may decide to allot you a slot outside of your preference.<br />
<form name="addPreferenceForm" id="addPreferenceForm" method="post" action="<%=response.encodeURL("/ITEA/faculty/Preferences") %>">
<table>
	<tr>
		<td>Day: </td>
		<td>
			<select name="d_id">
				<option value="m">Monday</option>
				<option value="tu">Tuesday</option>
				<option value="w">Wednesday</option>
				<option value="th">Thursday</option>
				<option value="f">Friday</option>
				<option value="s">Saturday</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>Time Slot: </td>
		<td>
			<select name="slot_no">
				<option value="-1">--Choose One--</option>
				<jsp:include page="/components/SlotList" />
			</select>
		</td>
	</tr>
	<tr>
		<td />
		<td><input type="submit" value="Add" /> <input type="reset" value="Reset" /></td>
	</tr>
</table>
</form>
<%
	if(request.getParameter("interface")!=null && request.getParameter("interface").equals("save")){
%>
<p class="error" style="display:block">Added successfully.</p>
<%
	}
	}
%>
</body>
</html>