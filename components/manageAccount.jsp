<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>                
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<jsp:include page="/components/style.jsp" />
<title>ITEA | Manage Your Account</title>
<style type="text/css">
#passwordStrengthBarBase{
	width:145px;
	height:10px;
	display: block;
	background:#cccccc;
}
#passwordStrengthBar {
	width:0px;
	height: 10px;
	float: left;
}
</style>
<script type="text/javascript" src="/ITEA/components/ajax.js"></script>
<script type="text/javascript" src="/ITEA/components/validation.js"></script>
<script type="text/javascript">
function passwordStrength(password){
	var psd=document.getElementById("passwordStrengthDescription");
	var psb=document.getElementById("passwordStrengthBar");

	if(password.length==0){
		psd.innerHTML="Password Not Entered";
		psb.style.width="0px";
		return;
	}
	
	var desc = new Array();
	desc[0] = "Very Weak";
	desc[1] = "Weak";
	desc[2] = "Medium";
	desc[3] = "Strong";
	desc[4] = "Very Strong";
	
	var col=new Array();
	col[0]="#B00";
	col[1]="#F00";
	col[2]="#FF0";
	col[3]="#0B0";
	col[4]="#0F0";
	
	var score   = 0;

	//if password bigger than 6 give 1 point
//	if (password.length > 6) score++;
	
	//if password has both lower and uppercase characters give 1 point	
	if ( ( password.match(/[a-z]/) ) && ( password.match(/[A-Z]/) ) ) score++;

	//if password has at least one number give 1 point
	if (password.match(/\d+/)) score++;

	//if password has at least one special character give 1 point
	if ( password.match(/.[!,@,#,$,%,^,&,*,?,_,~,-,(,)]/) )	score++;

	//if password bigger than 12 give another 1 point
	if (password.length > 12) score++;
	
	 psd.innerHTML = desc[score];
	 psb.style.width=(score+1)*29+"px";
	 psb.style.background=col[score];
}
function passwordMatch(){
	var newpwd=document.getElementById("new_password").value;
	var confpwd=document.getElementById("confirm_password").value;
	if(newpwd==confpwd){
		document.getElementById("confirm_password_match_error").style.display="none";
		return true;
	}
	else{
		document.getElementById("confirm_password_match_error").style.display="block";
		return false;
	}
}
function validate(){
	if(validateThese("COMBINED","curr_password")){
		/* Password is to be changed */
		if(document.getElementById('new_password').value.length<6){
			document.getElementById("new_password_length_error").style.display="block";
			return false;
		}else{
			document.getElementById("new_password_length_error").style.display="none";
			if(!passwordMatch())
				return false;
		}
	}
	if(validateThese('COMBINED','theme'))
		return validateThese('INDIVIDUAL','theme');
	return true;
}
</script>
</head>
<body>
<jsp:include page="/components/navbar.jsp" />
<form name="manageAccountForm" id="manageAccountForm" method="post" action="<%=response.encodeURL("/ITEA/components/ManageAccount?do=save") %>" 
onsubmit="return validate()">
<table>
<%
	if(request.getParameter("firstTime")!=null){
%>
	<tr>
		<td colspan="3">
			You have logged in for the first time. Please choose a new username and password. A username once chosen cannot be changed again.
		</td>
	</tr>
	<tr>
		<th colspan="2">Change Username</th>
		<th />
	</tr>
<%
	}
%>
	<tr>
		<td>Current Username: </td>
		<td><input type="text" value="<%=session.getAttribute("username").toString() %>" disabled="disabled" /></td>
		<td />
	</tr>
<%
	if(request.getParameter("firstTime")!=null){
%>
	<tr>
		<td>New Username: </td>
		<td><input type="text" name="new_username" id="new_username" maxlength="30" /></td>
		<td id="new_username_error" class="error">Cannot be empty.</td>
	</tr>
	<tr>
		<td />
		<td colspan="2">
			<div id="new_username_duplicate_error" class="error" style="display:block">
<%
		if(request.getParameter("error")!=null && request.getParameter("error").equals("username")){
%>
			This username is already taken. Please enter another username.
<%
		}
%>
			</div>
		</td>
	</tr>
<%		
	}
%>
	<tr>
		<th colspan="2">Change Password</th>
		<th />
	</tr>
	<tr>
		<td>Current password: </td>
		<td><input type="password" name="curr_password" id="curr_password" maxlength="32" /></td> 
		<td id="curr_password_error" class="error">Cannot be empty.</td>
	</tr>
	<tr>
		<td />
		<td colspan="2">
			<div id="curr_password_match_error" class="error" style="display:block">
<%
		if(request.getParameter("error")!=null && request.getParameter("error").equals("password")){
%>
			The password does not match. Please enter the correct password.
<%
		}
%>
			</div>
		</td>
	</tr>
	<tr>
		<td>New password (minimum 6 characters): </td>
		<td><input type="password" name="new_password" id="new_password" onblur="passwordStrength(this.value)" onkeyup="passwordStrength(this.value);" maxlength="32" /></td> 
		<td>
			<div id="new_password_error" class="error">Cannot be empty.</div>
			<div id="new_password_length_error" class="error">Must be atleast 6 characters long.</div>
		</td>
	</tr>
	<tr>
		<td>Confirm password: </td>
		<td><input type="password" name="confirm_password" id="confirm_password" maxlength="32" /></td> 
		<td id="confirm_password_error" class="error">Cannot be empty.</td>
		<td id="confirm_password_match_error" class="error">Does not match the new password entered.</td>
	</tr>
	<tr>
		<td>Password strength: </td>
		<td>
			<div id="passwordStrengthBarBase"><div id="passwordStrengthBar"></div></div>
			<div id="passwordStrengthDescription">Password not entered</div>
		</td>
		<td></td>
	</tr>
	<tr>
		<th colspan="2">Change Theme</th>
		<th />
	</tr>
	<tr>
		<td>Theme: </td>
		<td>
			<select name="theme" id="theme">
				<option value="0">Gold</option>
				<option value="1">Green</option>
				<option value="2">Twilight</option>
				<option value="3">Suede</option>
				<option value="4">Electric Black</option>
				<option value="5">Sahara</option>
				<option value="6">Ice</option>
			</select>
		</td>
		<td />
	</tr>
	<tr>
		<td></td>
		<td><input type="submit" value="Submit" /> <input type="reset" value="Reset" /></td>
		<td></td>
	</tr>
</table>

</form>
</body>
</html>