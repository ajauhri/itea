<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ITEA - Faculty | Manage Lectures</title>
<jsp:include page="/components/style.jsp" />
</head>
<body>
<jsp:include page="/components/navbar.jsp" />

<table>
	<tr>
		<th colspan="2">Perform Teaching-Related Activities</th>
	</tr>
	<tr>
		<td>Declare lecture time preferences. These preferences will be taken into account during timetable generation and faculty allotment.</td>
		<td><a href="<%=response.encodeURL("/ITEA/faculty/Preferences") %>">Declare Preferences</a></td>
	</tr>
	<tr>
		<td>Mark Daily Attendance for Lectures. Attendance reports are given to the Dean on a daily basis. It is important to mark the attendance for every lecture. This is the way the system tracks the number of lectures held.</td>
		<td><a href="<%=response.encodeURL("/ITEA/faculty/Attendance") %>">Mark Attendance</a></td>
	</tr>
	<tr>
		<td>Upload resources. Resources include lecture schedules, plans, notes, test dates, assignment notices and other notices and study material. These resources will be available to the students to download.</td>
		<td><a href="<%=response.encodeURL("/ITEA/faculty/Upload") %>">Upload Resources</a></td>
	</tr>
	<tr>
		<td>View your current weekly lecture schedule.</td>
		<td><a href="<%=response.encodeURL("/ITEA/faculty/TimeTablePersonal") %>">View Time Table</a></td>
	</tr>
</table>
</body>
</html>