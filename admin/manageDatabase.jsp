<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ITEA - Admin | Manage Database</title>
<jsp:include page="/components/style.jsp" />
</head>
<body>
<jsp:include page="/components/navbar.jsp" />

<table>
	<tr>
		<th colspan="2">View, Add, Modify or Delete Records</th>
	</tr>
	<tr>
		<td colspan="2"><strong>To setup the college database, begin by adding infrastructure details, courses, branches and classes available, and then the students and faculty.</strong></td>
	</tr>
	<tr>
		<td colspan="2">Setup the college database in the following order: </td>
	</tr>
	<tr>
		<td>Infrastructure, such as class rooms, laboratories and various halls.</td>
		<td><a href="<%=response.encodeURL("/ITEA/admin/InfrastructureRecords") %>">Manage Infrastructure</a></td>
	</tr>
	<tr>
		<td>Courses.</td>
		<td><a href="<%=response.encodeURL("/ITEA/admin/CourseRecords") %>">Manage Courses</a></td>
	</tr>
	<tr>
		<td>Branches and individual classes - one per academic session - in each course.</td>
		<td><a href="<%=response.encodeURL("/ITEA/admin/BranchRecords") %>">Manage Branches</a></td>
	</tr>
	<tr>
		<td>Subjects in each class (academic session). Subjects available to multiple classes must be added separately, once per class.</td>
		<td><a href="<%=response.encodeURL("/ITEA/admin/SubjectRecords") %>">Manage Subjects</a></td>
	</tr>
	<tr>
		<td colspan="2"><strong>To begin adding student records, you must have classes available in the database.</strong></td>
	</tr>
	<tr>
		<td>Students enrolled in the college.</td>
		<td><a href="<%=response.encodeURL("/ITEA/admin/StudentRecords") %>">Manage Students</a></td>
	</tr>
	<tr>
		<td colspan="2"><strong>To begin adding faculty records, you must have course branches available in the database.</strong></td>
	</tr>
	<tr>
		<td>Faculty (include internal faculty, visiting faculty, heads of departments and dean(s)).</td>
		<td><a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords") %>">Manage Faculty</a></td>
	</tr>
	<tr>
		<td colspan="2"><strong>Examination schedules can only be added when subjects have been listed.</strong></td>
	</tr>
	<tr>
		<td>Examination schedules for the current academic session.</td>
		<td><a href="<%=response.encodeURL("/ITEA/admin/ExaminationAdmin") %>">Manage Examinations</a></td>
	</tr>
	<tr>
		<td>Enter marks achieved by students in various examinations.</td>
		<td><a href="<%=response.encodeURL("/ITEA/admin/EnterMarks") %>">Enter Marks</a></td>
	</tr>
</table>

</body>
</html>