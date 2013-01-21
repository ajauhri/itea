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
	var class_id

	function showCourse(){
		ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/ExaminationAdmin?q=course&rand="+Math.random()) %>","displayCourses");
	}
	function displayCourses(optList){
		var courseElem=document.getElementById("courses");
		courseElem.innerHTML="<option id=\"-1\">--Choose One--</option>"+unescape(optList);
	}
	function showBranch() {
		document.getElementById("branches").style.display = 'block'
		course_id = document.getElementById("courses")
		course_id = course_id.options[course_id.selectedIndex].value
		ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/ExaminationAdmin?q=branch&course_id=\" + course_id + \"&rand=" + Math.random()) %>", "displayBranches")
	}
	function displayBranches(optList){
		var branchElem=document.getElementById("branches");
		branchElem.innerHTML="<option id=\"-1\">--Choose One--</option>"+unescape(optList);
	}
	function showClass() {
		document.getElementById("classes").style.display = 'block'
		branch_id = document.getElementById("branches")
		branch_id = branch_id.options[branch_id.selectedIndex].value
		ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/ExaminationAdmin?q=class&branch_id=\" + branch_id+\"&rand=" + Math.random()) %>", "displayClasses")
	}
	function displayClasses(optList){
		var classElem=document.getElementById("classes");
		classElem.innerHTML="<option id=\"-1\">--Choose One--</option>"+unescape(optList);
	}
	function showSubject() {
		document.getElementById("subjects").style.display = 'block'
		document.getElementById("schedulediv").style.display = 'block'
		class_id = document.getElementById("classes")
		class_id = class_id.options[class_id.selectedIndex].value
		ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/ExaminationAdmin?q=subject&class_id=\" + class_id + \"&rand="+ Math.random()) %>", "displaySubjects")
	}
	function displaySubjects(optList){
		var subjectElem=document.getElementById("subjects");
		subjectElem.innerHTML=unescape(optList);
	}
	function validate(){
		var numElems=new Array("dd","mm","yyyy","dur","hr","min");
		for(var i=0;i<numElems.length;i++)
			if(!validateNumber(numElems[i])){
				document.getElementById(numElems[i]+"_error").style.display="block";
				return false;
			}
		return validateAll('INDIVIDUAL',"examForm");
	}
	function allotSlot() {
		if(!validate()) return;
		subject_id = document.getElementById("subjects")
		subject_id = subject_id.options[subject_id.selectedIndex].value
		dd = document.getElementById("dd").value
		mm = document.getElementById("mm").value
		yyyy = document.getElementById("yyyy").value
		hr = document.getElementById("hr").value
		min = document.getElementById("min").value
		dur = document.getElementById("dur").value
		ss = 0
		ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/ExaminationAdmin?q=allot"
				+ "&subject_id=\" + subject_id + \""
				+ "&dd=\" + dd + \"&mm=\" + mm + \"&yyyy=\" + yyyy + \""
				+ "&hr=\" + hr + \"&min=\" + min + \"&dur=\" + dur + \"&rand=" + Math.random()) %>",
				"updateAllotdiv")
	}
	function updateAllotdiv(content){
		document.getElementById("allotdiv").style.display="inline";
		document.getElementById("allotdiv").innerHTML=unescape(content);
	}
</script>

</head>
<body onload="showCourse()">
<jsp:include page="/components/navbar.jsp" />
<form name="examForm" id="examForm" method="post" onsubmit="return false">
<table>
	<tr>
		<td>Select a Course: </td>
		<td>
			<select name="courses" id="courses" size="1" onchange="showBranch()">
				<option value="-1">--Choose One--</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>Select a Branch: </td>
		<td>
			<select name="branches" id="branches" size="1" style="display: none"
				onchange="showClass()">
				<option value="-1">--Choose a Course--</option>
			</select>
		</td>
		<td />
	</tr>
	<tr>
		<td>Select a Class: </td>	
		<td>
			<select name="classes" id="classes" size="1" style="display: none"
				onchange="showSubject()">
				<option value="-1">--Choose a Branch--</option>
			</select>
		</td>
		<td />
	</tr>
	<tr>
		<td>Select a Subject: </td>
		<td>
			<select name="subjects" id="subjects" size="1" style="display: none">
				<option value="-1">--Choose a Class--</option>
			</select>
		</td>
		<td class="error" id="subjects_error">Please select a subject.</td>
	</tr>
</table>
<div id="schedulediv" style="display: none">
	<table>
		<tr>
			<td>Date: </td>
			<td>
				<table>
					<tr>
						<td><input type="text" name="dd" id="dd" maxlength="2" size="2" />/</td>
						<td><input type="text" name="mm" id="mm" maxlength="2" size="2" />/</td>
						<td><input type="text" name="yyyy" id="yyyy" maxlength="4" size="4" /></td>
						<td>
							<span class="error" id="dd_error">Numerical value required for dd.</span>
							<span class="error" id="mm_error">Numerical value required for mm.</span>
							<span class="error" id="yyyy_error">Numerical value required for yyyy.</span>
						</td>
					</tr>
					<tr>
						<td><small>dd</small></td>
						<td><small>mm</small></td>
						<td><small>yyyy</small></td>
						<td />
					</tr>
				</table>
			</td>
			<td />
		</tr>
		<tr>
			<td>Time of Examination: </td>
			<td>
				<table>
					<tr>
						<td>Hours: </td>
						<td><input type="text" id="hr" maxlength="2" /></td>
						<td class="error" id="hr_error">Numeric value required.</td>
					</tr>
					<tr>
						<td>Minutes: </td>
						<td><input type="text" id="min" maxlength="2" /></td>
						<td class="error" id="min_error">Numeric value required.</td>
					</tr>
					<tr>
						<td>Duration: </td>
						<td><input type="text" id="dur" /> hours</td>
						<td class="error" id="dur_error">Numeric value required.</td>
					</tr>
				</table>
			</td>
			<td />
		</tr>
		<tr>
			<td colspan="2">
				<input type="submit" onclick="allotSlot()" value="Submit" />
				<input type="reset" value="Reset" />
			</td>
			<td />
		</tr>
	</table>
</div>
</form>
<div id="allotdiv" class="alert"></div>
</body>
</html>