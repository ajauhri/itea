<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ITEA - Faculty | Attendance</title>
<jsp:include page="/components/style.jsp" />
<style type="text/css">
#chart{ display:none; }
.dark{ background-color:; }
.alert{ position:relative; left:30%; padding:2em 3em; background-color:#ffc; }
</style>
<script type="text/javascript" src="/ITEA/components/ajax.js"></script>
<script type="text/javascript">
function getSubjects(){
	var se=document.getElementById("subjectContainer");
	var sel=se.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	se.innerHTML=sel+"<option value=\"-1\">Loading...</option></select>";
	ajaxRequest("<%=response.encodeURL("/ITEA/faculty/Attendance?ajax=true&list=subject&rand="+Math.random()) %>","displaySubjects");
}
function displaySubjects(optList){
	var se=document.getElementById("subjectContainer");
	var sel=se.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	se.innerHTML=sel+"<option value=\"-1\">--Choose One--</option>"+optList+"</select>";
}
function getChart(){
	var se=document.getElementById("subject");
	var subject_id=se.options[se.selectedIndex].value;
	var chartStore=document.getElementById("chart");
	chartStore.innerHTML+="<tr><td colspan='3'>Loading...</td></tr>";
	ajaxRequest("<%=response.encodeURL("/ITEA/faculty/Attendance?ajax=true&list=attendance&subject_id=\"+subject_id+\"&rand="+Math.random()) %>","displayChart");
}
function displayChart(content){
	var chartStore=document.getElementById("chart");
	chartStore.style.display="block";
	var chartContent=chartStore.innerHTML;
	chartContent=chartContent.substring(0,chartContent.indexOf("Loading..."));
	chartStore.innerHTML=chartContent+content;
}
function getStudents(){
	var se=document.getElementById("subject");
	var subject_id=se.options[se.selectedIndex].value;
	var chartStore=document.getElementById("chart");
	chartStore.innerHTML+="<tr><td colspan='3'>Loading...</td></tr>";
	ajaxRequest("<%=response.encodeURL("/ITEA/faculty/Attendance?ajax=true&list=students&subject_id=\"+subject_id+\"&rand="+Math.random()) %>","displayStudents");
}
function displayStudents(content){
	var chartStore=document.getElementById("chart");
	chartStore.style.display="block";
	var chartContent=chartStore.innerHTML;
	chartContent=chartContent.substring(0,chartContent.indexOf("Loading..."));
	chartStore.innerHTML=chartContent+content;
}
function gatherAttendance(){
	/* Convert attendance to CSV format */
	var form=document.getElementById("markAttendanceForm");
	var attendanceList="";
	var checkboxElem=null;
	for(var i=0;i<form.elements.length;i++){
		checkboxElem=form.elements[i];
		if(checkboxElem.tagName.toUpperCase()!="input".toUpperCase() || checkboxElem.type.toUpperCase()!="checkbox".toUpperCase())
			continue;
		// else
		if(checkboxElem.checked=="true" || checkboxElem.checked=="TRUE" || checkboxElem.checked==true)
			attendanceList+=checkboxElem.value+",";
	}
	// strip the last comma, if any
	if(attendanceList.lastIndexOf(",")!=-1)
		attendanceList=attendanceList.substring(0,attendanceList.length-1);
	// store list in form
	document.getElementById("attenders").value=attendanceList;
}
function check(){
	// disable submission of form & clear any error notices
	var submitButton=document.getElementById("submit");
	submitButton.disabled="disabled";
	submitButton.value="Checking...";
	document.getElementById("lecture_error").style.display="none";
	// check whether attendance has already been marked for the lecture
	var subject_id=document.getElementById("subject").value;
	var dd=document.getElementById("dd").value;
	var mm=document.getElementById("mm").value;
	var yyyy=document.getElementById("yyyy").value;
	var slot=document.getElementById("slot").value;
	if(dd=="" || dd==null || mm=="" || mm==null || yyyy=="" || yyyy==null || slot=="-1"){
		// wait for user to enter the values
		return;
	}
	ajaxRequest("<%=response.encodeURL("/ITEA/faculty/Attendance?ajax=true&do=check&subject_id=\"+subject_id+\"&dd=\"+dd+\"&mm=\"+mm+\"&yyyy=\"+yyyy+\"&slot=\"+slot+\"&rand="+Math.random()) %>","checkResponse");
}
function checkResponse(marked){
	var submitButton=document.getElementById("submit");
	if(marked=="unmarked"){
		submitButton.disabled="";
		submitButton.value="Submit";		
	}else if(marked=="marked"){
		// already marked
		submitButton.disabled="disabled";
		submitButton.value="Submit";
		var errorElem=document.getElementById("lecture_error");
		errorElem.style.display="inline";
	}
}
</script>
</head>
<%
/*
	Get:		
		1. List all subjects taught by the faculty.
		2. User selects a subject.
		3. Chart displaying students and their total attendance.
	Set:
		1. List all the subjects taught by the faculty.
		2. User selects a subject.
		3. User enters the date of lecture.
		4. User selects a slot for the lecture.
		5. Chart displaying students.
		6. User marks present students.
		7. User submits.
		8. Attendance total for each student is updated.
		9. Attendance_marked is updated for the lecture.
*/
%>
<%
	if(request.getParameter("do")!=null && request.getParameter("do").equals("save")){
%>
<body>
<% 
	}else{
%>
<body onload="getSubjects()">
<%
	}
%>
<jsp:include page="/components/navbar.jsp" />
<%
	if(request.getParameter("do")!=null && request.getParameter("do").equals("save")){
%>
Attendance submitted.<br />
<a href="javascript:void(0)" onclick="location.href='/ITEA/faculty/Attendance?do=mark'">Mark Attendance</a><br />
<a href="javascript:void(0)" onclick="location.href='/ITEA/faculty/Attendance'">View Attendance</a><br />
<%
	}else if(request.getParameter("do")!=null && request.getParameter("do").equals("mark")){
%>
<form name="markAttendanceForm" id="markAttendanceForm" method="post" action="<%=response.encodeURL("/ITEA/faculty/Attendance?do=save") %>" 
onsubmit="gatherAttendance(); return true;">
<span class="error alert" id="lecture_error">Attendance has already been marked for this lecture. Please select another date/slot.</span>
<table>
	<tr>
		<td>Select a Subject: </td>
		<td>
			<div id="subjectContainer">
				<select name="subject_id" id="subject" onchange="getStudents()">
					<option value="-1">Choose One</option>
				</select>
			</div>
		</td>
	</tr>
	<tr>
		<td>Date of the Lecture: </td>
		<td>
			<table>
				<tr>
					<td><input type="text" name="dd" id="dd" size="2" maxlength="2" onblur="check()" /></td>
					<td> / </td>
					<td><input type="text" name="mm" id="mm" size="2" maxlength="2" onblur="check()" /></td>
					<td> / </td>
					<td><input type="text" name="yyyy" id="yyyy" size="4" maxlength="4" onblur="check()" /></td>
				</tr>
				<tr style="font-size:0.8em">
					<td>dd</td>
					<td />
					<td>mm</td>
					<td />
					<td>yyyy</td>
					<td />
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>Select a Slot: </td>
		<td>
			<select name="slot" id="slot" onchange="check()">
				<option value="-1">--Choose One--</option>
				<jsp:include page="/components/SlotList" />
			</select>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<p>Mark the students that are present.</p>
			<table id="chart">
				<tr>
					<th>Roll No.</th>
					<th>Student Name</th>
					<th>Present</th>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td></td>
		<td><input type="submit" id="submit" value="Submit" /> <input type="reset" value="Reset" /></td>
	</tr>
</table>
<input type="hidden" name="attenders" id="attenders" value="" />
</form>
<%
	}else{
%>
<a href="javascript:void(0)" onclick="location.href='/ITEA/faculty/Attendance?do=mark'">Mark Attendance</a><br />
<a href="javascript:void(0)" onclick="location.href='/ITEA/faculty/Upload_Att'">Mark Attendance From Bar Code Reader</a><br />
<br />
Select a Subject that you currently teach:
<div id="subjectContainer">
	<select name="subject" id="subject" onchange="getChart()">
		<option value="-1">Choose One</option>
	</select>
</div>
<br />
<table id="chart">
	<tr>
		<th>Roll No.</th>
		<th>Student Name</th>
		<th>Total Attendance</th>
	</tr>
</table>
<%
	}
%>
</body>
</html>