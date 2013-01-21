<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>ITEA - Dean | Reports</title>
		<jsp:include page="/components/style.jsp" />
<script type="text/javascript" src="/ITEA/components/ajax.js"></script>
<script type="text/javascript">

function getCourses(){
	var ce=document.getElementById("course");
	document.getElementById("coursewiseForm").style.display="block";
	var sel=ce.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	ce.innerHTML=sel+"<option value=\"-1\">Loading...</option></select>";
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/Reports/Reports?q=course&rand="+Math.random()) %>","displayCourses");
}

function displayCourses(optList){
	var ce=document.getElementById("course");
	var sel=ce.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	ce.innerHTML=sel+"<option value=\"-1\">--Choose One--</option>"+unescape(optList)+"</select>";
}

function getCourses1(){
	var ce=document.getElementById("course1");
	document.getElementById("attendanceForm").style.display="block";
	var sel=ce.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	ce.innerHTML=sel+"<option value=\"-1\">Loading...</option></select>";
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/Reports/Reports?q=course&rand="+Math.random()) %>","displayCourses1");
}

function displayCourses1(optList){
	var ce=document.getElementById("course1");
	var sel=ce.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	ce.innerHTML=sel+"<option value=\"-1\">--Choose One--</option>"+unescape(optList)+"</select>";
}

function getBranches1(course_id){
	var ce=document.getElementById("branch1");
	var sel=ce.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	ce.innerHTML=sel+"<option value=\"-1\">Loading...</option></select>";
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/Reports/Reports?q=branch&course_id=\"+course_id+\"&rand="+Math.random()) %>","displayBranches1");
}

function displayBranches1(optList){
	var ce=document.getElementById("branch1");
	var sel=ce.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	ce.innerHTML=sel+"<option value=\"-1\">--Choose One--</option>"+unescape(optList)+"</select>";
}

function getClasses1(branch_id){
	var ce=document.getElementById("class1");
	var sel=ce.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	ce.innerHTML=sel+"<option value=\"-1\">Loading...</option></select>";
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/Reports/Reports?q=class&branch_id=\"+branch_id+\"&rand="+Math.random()) %>","displayClasses1");
}

function displayClasses1(optList){
	var ce=document.getElementById("class1");
	var sel=ce.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	ce.innerHTML=sel+"<option value=\"-1\">--Choose One--</option>"+unescape(optList)+"</select>";
}

function getCourses2(){
	var ce=document.getElementById("course2");
	document.getElementById("performanceForm").style.display="block";
	var sel=ce.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	ce.innerHTML=sel+"<option value=\"-1\">Loading...</option></select>";
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/Reports/Reports?q=course&rand="+Math.random()) %>","displayCourses2");
}

function displayCourses2(optList){
	var ce=document.getElementById("course2");
	var sel=ce.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	ce.innerHTML=sel+"<option value=\"-1\">--Choose One--</option>"+unescape(optList)+"</select>";
}

function getBranches2(course_id){
	var ce=document.getElementById("branch2");
	var sel=ce.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	ce.innerHTML=sel+"<option value=\"-1\">Loading...</option></select>";
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/Reports/Reports?q=branch&course_id=\"+course_id+\"&rand="+Math.random()) %>","displayBranches2");
}

function displayBranches2(optList){
	var ce=document.getElementById("branch2");
	var sel=ce.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	ce.innerHTML=sel+"<option value=\"-1\">--Choose One--</option>"+unescape(optList)+"</select>";
}

function getClasses2(branch_id){
	var ce=document.getElementById("class2");
	var sel=ce.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	ce.innerHTML=sel+"<option value=\"-1\">Loading...</option></select>";
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/Reports/Reports?q=class&branch_id=\"+branch_id+\"&rand="+Math.random()) %>","displayClasses2");
}

function displayClasses2(optList){
	var ce=document.getElementById("class2");
	var sel=ce.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	ce.innerHTML=sel+"<option value=\"-1\">--Choose One--</option>"+unescape(optList)+"</select>";
}
function displayDays(){
	document.getElementById("infraForm").style.display="block";
}


</script>
	</head>
	<body>
		<jsp:include page="/components/navbar.jsp" />
		<br>
		<a href="javascript:void(0)" onclick="getCourses()">CourseWise Report</a>
		<form id="coursewiseForm" method="get" action="<%=response.encodeURL("/ITEA/Reports/CourseWise") %>" style="display:none">
			<div id="course">
				<select name="course_id">
					<option value="-1">--Choose One--</option>
				</select>
			</div>
		<input type="submit" value="Generate" />
		</form>
		<br>
		<!--a href="javascript:void(0)" onclick="displayDays()">Infrastructure Report</a>
		<form id="infraForm" method="get" action="<%=response.encodeURL("infraGraph.jsp") %>" style="display:none">
			<div id="day">
				<select name="day">
					<option value="-1">--Choose One--</option>
					<option value="m">Monday</option>
					<option value="tu">Tuesday</option>
					<option value="w">Wednesday</option>
					<option value="th">Thursday</option>
					<option value="f">Friday</option>
					<option value="s">Saturday</option>
				</select>
			</div>
		<input type="submit" value="Generate" />
		</form>
		<br-->
		<a href="<%=response.encodeURL("/ITEA/Reports/Daily") %>">Daily Report</a>
		<br>
		<a href="javascript:void(0)" onclick="getCourses1()">Attendance Report</a>
		<form id="attendanceForm" method="get" action="<%=response.encodeURL("/ITEA/Reports/DefaultersList") %>" style="display:none">
			<div id="course1">
				<select name="course_id" onchange="getBranches1(this.options[this.selectedIndex].value)">
					<option value="-1">--Choose One--</option>
				</select>
			</div>
			<div id="branch1">
				<select name="branch_id" onchange="getClasses1(this.options[this.selectedIndex].value)">
					<option value="-1">--Choose One--</option>
				</select>
			</div>
			<div id="class1">
				<select name="class_id">
					<option value="-1">--Choose One--</option>
				</select>
			</div>
		<input type="submit" value="Generate" />
		</form>
		<br>
		<a href="javascript:void(0)" onclick="getCourses2()">Student Performance Report</a>
		<form id="performanceForm" method="get" action="<%=response.encodeURL("/ITEA/Reports/Performance") %>" style="display:none">
			<div id="course2">
				<select name="course_id" onchange="getBranches2(this.options[this.selectedIndex].value)">
					<option value="-1">--Choose One--</option>
				</select>
			</div>
			<div id="branch2">
				<select name="branch_id" onchange="getClasses2(this.options[this.selectedIndex].value)">
					<option value="-1">--Choose One--</option>
				</select>
			</div>
			<div id="class2">
				<select name="class_id">
					<option value="-1">--Choose One--</option>
				</select>
			</div>
		<input type="submit" value="Generate" />
		</form>
		<br>
	</body>
</html>
