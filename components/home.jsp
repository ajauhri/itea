<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" session="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ITEA | Home</title>
<jsp:include page="/components/style.jsp" />
<style type="text/css">
#leftContent{ position:relative; float:left; width:60%; }
#leftContent p{ margin-left:5em; }
#leftContent table{ margin-left:7em; }
#rightContent{ position:relative; width:20%; }
.class{ font-size:0.8em; }
</style>
<script type="text/javascript" src="/ITEA/components/validation.js"></script>
</head>
<body>
<jsp:include page="/components/navbar.jsp" />
<div id="contentContainer">
	<div id="leftContent">
		<p>About ITEA, and about a generic college.</p>
<br />
<center><b>Quick Start</b></center>
<p>Login as the following users for direct access: </p>
<p>
	Data Manager:
</p>
	<table>
		<tr>
			<td>Username: </td>
			<td>admin</td>
		</tr>
		<tr>
			<td>Username: </td>
			<td>admin</td>
		</tr>
	</table>
<p>
	Dean:
</p>
	<table>
		<tr>
			<td>Username: </td>
			<td>dj</td>
		</tr>
		<tr>
			<td>Username: </td>
			<td>djshah</td>
		</tr>
	</table>
<p>
	H.O.D:
</p>
	<table>
		<tr>
			<td>Username: </td>
			<td>sri</td>
		</tr>
		<tr>
			<td>Username: </td>
			<td>kantharao</td>
		</tr>
	</table>
<p>
	H.O.D:
</p>
	<table>
		<tr>
			<td>Username: </td>
			<td>ketan</td>
		</tr>
		<tr>
			<td>Username: </td>
			<td>shah</td>
		</tr>
	</table>
<p>
	Faculty:
</p>
	<table>
		<tr>
			<td>Username: </td>
			<td>st</td>
		</tr>
		<tr>
			<td>Username: </td>
			<td>gandhe</td>
		</tr>
	</table>
<p>
	Faculty:
</p>
	<table>
		<tr>
			<td>Username: </td>
			<td>dimple</td>
		</tr>
		<tr>
			<td>Username: </td>
			<td>parekh</td>
		</tr>
	</table>
<p>
	Student:
</p>
	<table>
		<tr>
			<td>Username: </td>
			<td>pulkit</td>
		</tr>
		<tr>
			<td>Username: </td>
			<td>karwal</td>
		</tr>
	</table>
<p>
	Student:
</p>
	<table>
		<tr>
			<td>Username: </td>
			<td>abhinav</td>
		</tr>
		<tr>
			<td>Username: </td>
			<td>jauhri</td>
		</tr>
	</table>


	</div>
	<div id="rightContent">
		<!-- Login Form -->
		<form name="loginForm" id="loginForm" method="post" action="/ITEA/components/Login" onsubmit="return validateThese('INDIVIDUAL','username','password')">
			<table>
				<tr>
					<th colspan="2">Login</th>
				</tr>
<%
	if(request.getParameter("error")!=null && request.getParameter("error").equals("true")){
%>
				<tr>
					<td />
					<td class="error" style="display:block">Invalid Username or Password.</td>
				</tr>
<%
	}
%>				
				<tr>
					<td>Username: </td>
					<td><input type="text" name="username" id="username" maxlength="30" /></td>
				</tr>
				<tr>
					<td></td>
					<td id="username_error" class="error">Cannot be empty.</td>
				</tr>
				<tr>
					<td>Password: </td>
					<td><input type="password" name="password" id="password" maxlength="32" /></td>
				</tr>
				<tr>
					<td></td>
					<td id="password_error" class="error">Cannot be empty.</td>
				</tr>
				<tr>
					<td colspan="2">
						If you are logging in for the first time, provide your GR number (unique identification number provided by the college)
						for username, and your date of birth ("dd/mm/yyyy") as password.
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<a href="javascript:void(0)" onclick="if(validateThese('INDIVIDUAL','username','password')) document.getElementById('loginForm').submit()">Sign In</a>
						<input type="submit" style="display:none" />
					</td>
			</table>
		</form>
	</div>
</div>
</body>
</html>