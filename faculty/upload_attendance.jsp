<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ITEA - Faculty | Attendance</title>
<jsp:include page="/components/style.jsp" />
<script type="text/javascript" src="/ITEA/components/validation.js"></script>
<script type="text/javascript">
function validate(){
	var numElems=new Array("dd","mm","yyyy");
	for(var i=0;i<numElems.length;i++)
		if(!validateNumber(numElems[i])){
			document.getElementById(numElems[i]+"_error").style.display="block";
			return false;
		}
	return validateAll('INDIVIDUAL',"attForm");
}
</script>
</head>
<body>
<jsp:include page="/components/navbar.jsp" />
<form name="attForm" id="attForm" method="post" action="Upload_Att" enctype="multipart/form-data" onsubmit="return validate()">	
	<table>
		<tr>
			<td>Select a Subject: </td>
			<td>
				<select name="subject" id="subject">
					<jsp:include page="/components/FacultySubList" />
				</select>
			</td>
			<td class="error" id="subject_error">Please select a subject.</td>
		</tr>
		<tr>
			<td>Date: </td>
			<td>
				<table>
					<tr>
						<td><input type="text" name="dd" id="dd" size="2" maxlength="2" /> | </td>
						<td><input type="text" name="mm" id="mm" size="2" maxlength="2" /> | </td>
						<td><input type="text" name="yy" id="yy" size="2" maxlength="2" /></td>
						<td>
							<span class="error" id="dd_error">Numeric value required for dd.</span>
							<span class="error" id="mm_error">Numeric value required for mm.</span>
							<span class="error" id="yy_error">Numeric value required for yyyy.</span>
						</td>
					</tr>
					<tr>
						<td>dd</td>
						<td>mm</td>
						<td>yy</td>
						<td />
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>Slot: </td>
			<td>
				<select name="slot" id="slot">
					<jsp:include page="/components/SlotList" />
				</select>
			</td>
			<td class="error" id="slot_error">Please select a slot.</td>
		</tr>
		<tr>
			<td>File: </td>
			<td><input type="file" name="uploadfile" size="30" /></td>
			<td />
		</tr>
		<tr>	
			<td colspan="2">Upload the file obtained from the bar code reader.</td>
			<td />
		</tr>
		<tr>
			<td colspan="2">
				<input type="submit" value="Mark Attendance" />
				<input type="reset" value="Reset" />
			</td>
			<td />
		</tr>
	</table>
</form>		
</body>
</html>