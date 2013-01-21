<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"  import="beans.ExaminationBean, beans.StatusBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%!int i = 0;%>
<%
/*	RequestDispatcher rd = request.getRequestDispatcher("/CLS");
	rd.include(request, response);
	StatusBean sb = (StatusBean) session.getAttribute("sb");
	String status = sb.getStatus();
*/ String status="a";
	if (status != null) {

		/* Retrieve ExaminationStudentBean instances */
		ExaminationBean eb[] = (ExaminationBean[]) request.getAttribute("eb");
		int noOfMsgs = (Integer) request.getAttribute("eb_no");
%>
<table>
	<tr>
		<th>S No.</th>
		<th>Subject ID</th>
		<th>Duration</th>
		<th>Date</th>
		<th>Time</th>

	</tr>
	<%
		for (i = 0; i < noOfMsgs; i++) {
	%>
	<tr>
		<td><%=(i + 1)%></td>
		<td><%=eb[i].getSubjectId()%></td>
		<td><%=eb[i].getDuration()%></td>
		<td><%=eb[i].getDateBean().getDd() + "/"
							+ eb[i].getDateBean().getMm() + "/"
							+ eb[i].getDateBean().getYyyy()%></td>
		<td><%=eb[i].getTimeBean().getHh() + ":"
							+ eb[i].getTimeBean().getMm()%></td>
	</tr>
	<%
	}
}
	%>
</table>
</body>
</html>