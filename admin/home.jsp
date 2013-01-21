<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ITEA - Admin | Home</title>
<jsp:include page="/components/style.jsp" />
</head>
<body>
<jsp:include page="/components/navbar.jsp" />
<p>
Welcome Data Manager to the HomePage of <b>I.T.E.A</b>.
</p>
<p>
This will be page your guide to use this software.
</p>
<p>
<b>Messages</b> - This feature will help you to communicate with other users via messages. 
One can send messages to individual or even groups of users. Alerts are special type of messages
which are displayed separately since they have a greater privilege.
</p>
<p>
<b>Manage Database</b> - This feature will allow you to make changes to the database such as
add / modify / delete the users, data and infrastructure. You can click on the link to see further
options.
</p>
<p>
<b>Manage Account</b> - This feature will allow you to change your account password and the
theme.
</p>
<br />
Manage Events Calendar:
<table>
	<tr>
		<td>Web Service Provider:</td>
		<td><a href="https://www.google.com/accounts/ServiceLogin?service=cl&passive=true&nui=1&continue=http%3A%2F%2Fwww.google.com%2Fcalendar%2Frender%3Fhl%3Den%26tab%3Dwc&followup=http%3A%2F%2Fwww.google.com%2Fcalendar%2Frender%3Fhl%3Den%26tab%3Dwc&hl=en">Google</a></td>
	</tr>
	<tr>
		<td>User Name: </td>
		<td>itea.ibm@gmail.com</td>
	</tr>
	<tr>
		<td>Password: </td>
		<td>1234567890abc</td>
	</tr>
</table>
<br />
Events Calendar: <br />
<iframe src="//www.google.com/calendar/embed?title=ITEA%20College%20Events&amp;height=500&amp;wkst=2&amp;bgcolor=%23FFFFFF&amp;src=4s2k1nv1iks87fj7cp3fu0m5u8%40group.calendar.google.com&amp;color=%2388880E&amp;ctz=Asia%2FCalcutta" style=" border-width:0 " width="700" height="500" frameborder="0" scrolling="no"></iframe>
</body>
</html>