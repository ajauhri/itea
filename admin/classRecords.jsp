<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<jsp:include page="/components/style.jsp" />
<script type="text/javascript" src="/ITEA/components/ajax.js"></script>
<script type="text/javascript" src="/ITEA/components/validation.js"></script>
<script type="text/javascript">
function getCourses(){
	var ce=document.getElementById("course");
	var sel=ce.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	ce.innerHTML=sel+"<option value=\"-1\">Loading...</option></select>";
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/ClassRecords?ajax=true&list=course&rand="+Math.random()) %>","displayCourses");
}
function displayCourses(optList){
	var ce=document.getElementById("course");
	var sel=ce.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	ce.innerHTML=sel+"<option value=\"-1\">--Choose One--</option>"+unescape(optList)+"</select>";
}
function getBranches(course_id){
	var be=document.getElementById("branch");
	var sel=be.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	if(course_id==null||course_id==""){
		be.innerHTML=sel+"<option value\"-1\">--Choose a Course--</option></select>";
		return;
	}
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/ClassRecords?ajax=true&list=branch&course_id=\"+course_id+\"&rand="+Math.random()) %>","displayBranches");
}
function displayBranches(optList){
	var be=document.getElementById("branch");
	var sel=be.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	be.innerHTML=sel+"<option value=\"-1\">--Choose One--</option>"+unescape(optList)+"</select>";
}
function getDuration(course_id){
	var durStore=document.getElementById("durationStore");
	var durElem=document.getElementById("duration");
	if(course_id==null|course_id==""){
		durStore.style.display="none";
		durElem.value="0";
		return;
	}
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/ClassRecords?ajax=true&q=duration&course_id=\"+course_id+\"&rand="+Math.random()) %>","displayDuration");
}
function displayDuration(duration){
	duration=unescape(duration);
	var durStore=document.getElementById("durationStore");
	var durElem1=document.getElementById("durationDisplay");
	var durElem2=document.getElementById("duration");
	durStore.style.display="block";
	durElem1.innerHTML=parseInt(duration);
	durElem2.value=parseInt(duration);
}
function check(branch_id){
	var submitButton=document.getElementById("generateButton");
	submitButton.value="Checking...";
	submitButton.disabled="disabled";
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/ClassRecords?ajax=true&q=check&branch_id=\"+branch_id+\"&rand="+Math.random()) %>","checkResponse");
}
function checkResponse(generated){
	generated=unescape(generated);
	var submitButton=document.getElementById("generateButton");
	var errorElem=document.getElementById("class_error");
	if(generated=="notGenerated"){
		submitButton.disabled="";
		submitButton.value="Generate Classes";		
		errorElem.style.display="none";
	}else if(generated=="generated"){
		// already marked
		submitButton.disabled="disabled";
		submitButton.value="Generate Classes";
		errorElem.style.display="inline";
	}
}
</script>
</head>
<body>
<jsp:include page="/components/navbar.jsp" />
<%
	if(request.getParameter("show")!=null && request.getParameter("show").equals("saved")){
%>
<span class="error alert">Classes have been generated successfully.</span>
<%
	}
%>
<form name="generateClassesForm" id="generateClassesForm" method="post" action="<%=response.encodeURL("/ITEA/admin/ClassRecords?do=generate") %>" 
onsubmit="return validateThese('INDIVIDUAL','sessionCount')">
<span class="error alert" id="class_error">Classes for this branch have already been generated.</span>
<table>
	<tr>
		<td>Course: </td>
		<td id="course">
			<select onchange="getBranches(this.options[this.selectedIndex].value); getDuration(this.options[this.selectedIndex].value);">
				<option value="-1">-Choose One--</option>
				<jsp:include page="/components/CourseList" />
			</select>
		</td>
		<td id="durationStore" style="display:none">
			(Duration: <span id="durationDisplay">0</span> years)
			<input type="text" name="duration" id="duration" value="" />
		</td>
	</tr>
	<tr>
		<td>Branch: </td>
		<td id="branch">
			<select name="branch_id" onchange="check(this.options[this.selectedIndex].value)">
				<option value="-1">--Choose a Course--</option>
			</select>
		</td>
		<td></td>
	</tr>
	<tr>
		<td>No. of Academic Sessions in a Year: </td>
		<td><input type="text" name="sessionYearlyCount" id="sessionYearlyCount" /></td>
		<td id="sessionYearlyCount_number_error" class="error">Numerical value required.</td>
	</tr>
	<tr>
		<td></td>
		<td><input type="submit" id="generateButton" value="Generate Classes" /> <input type="reset" value="Reset" /></td>
	</tr>
</table>
</form>
</body>
</html>