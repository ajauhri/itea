<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="/components/style.jsp" />
<title>ITEA - Dean | Reset Course</title>
<script type="text/javascript" src="/ITEA/components/ajax.js"></script>
<script type="text/javascript">
function resetCourse(){
	document.getElementById("resetConfirmationDiv").style.display="none";
	var ce=document.getElementById("courses");
	var course=ce.options[ce.selectedIndex].value;
	if(course=="-1" || course==null || course=="")
		document.getElementById("confirm").innerHTML="Please select a course.";		
	else{
		document.getElementById("confirm").innerHTML="Loading...";
		ajaxRequestEscaped("<%=response.encodeURL("/ITEA/dean/ResetCourse?q=\"+course+\"&rand="+Math.random()) %>", "displayResult");
	}
}
function displayResult(status){
	document.getElementById("confirm").innerHTML=unescape(status);
}
function confirmResetCourse(x){
	if(x==1) document.getElementById("resetConfirmationDiv").style.display="";
	else document.getElementById("resetConfirmationDiv").style.display="none";
}
</script>
</head>
<body>
	<jsp:include page="/components/navbar.jsp" />
	<p>
	The function will reset the following for a selected course:
	</p>
	<ul>
		<li>Promotes all students to next academic session.</li>
		<li>Clear attendance for the concluded session.</li>
		<li>Reset all class schedules under the selected course.</li>
		<li>Clears the number of lectures held per subject.</li>
		<li>Clears the uploaded resources under the course.</li>
	</ul>
	Select a course: 
	<select id="courses" name="courses">
		<jsp:include page="/components/CourseList" />
	</select>
	<input type="button" id="reset_course" name="reset_course"
		onclick="confirmResetCourse(1)" value="Reset Course">
	<div id="confirm" class="error" style="display:block"></div>
	<div id="resetConfirmationDiv" style="display:none">
		<fieldset>
		Are you sure you want to reset the course? <br />
		<input type="button" value="Yes" onclick="resetCourse()" /><input type="button" value="No" onclick="confirmResetCourse(0)" />
		</fieldset>
	</div>
</body>
</html>