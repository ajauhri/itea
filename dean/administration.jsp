<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ITEA - Dean | Administration</title>
<jsp:include page="/components/style.jsp" />
</head>
<body>
<jsp:include page="/components/navbar.jsp" />

<table>
	<tr>
		<th colspan="2">Perform Administrative Activities</th>
	</tr>
	<tr>
		<td>Declare end of academic session. All students will be promoted to the next session. Class-specific data such as uploaded resources and time-tables are reset.</td>
		<td><a href="<%=response.encodeURL("/ITEA/dean/ResetCourse") %>">Declare End of Session</a></td>
	</tr>
	<tr>
		<td>Declare failed students. Students who fail to achieve marks below minimum, can be demoted.</td>
		<td><a href="<%=response.encodeURL("/ITEA/dean/Failures") %>">Declare Failures</a></td>
	</tr>
</table>
</body>
</html>