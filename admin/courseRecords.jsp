<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="beans.CourseBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ITEA - Admin | Course Records</title>
<jsp:include page="/components/style.jsp" />
<script type="text/javascript" src="/ITEA/components/ajax.js"></script>
<script type="text/javascript" src="/ITEA/components/validation.js"></script>
<script type="text/javascript">
function modify(elem,course_id,field){
	elem.onclick="";
	var editor="<form name='editForm' onsubmit='makeChanges("+course_id+",\""+field+"\",this.editField.value); return false;'>";
	editor+="<input type='text' value='"+elem.innerHTML+"' name='editField' onblur='makeChanges("+course_id+",\""+field+"\",this.value); return false;' />"
	editor+="</form>";
	elem.innerHTML=editor;
	elem.form.editField.focus();
}
function makeChanges(courseId,field,value){
	/* Highly specific validation */
	var e=0;
	var ge=document.getElementById("global_error");
	if(field=="name" && value.length>30) e=1;
	if(field=="duration" && isNaN(value)) e=1;
	if(e==1) ge.style.display="inline";
	else ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/CourseRecords?ajax=true&do=edit&course_id=\"+courseId+\"&field=\"+field+\"&newContent=\"+value+\"&rand="+Math.random()) %>","reloadPage");
}
function confirmDeleteRecord(id){
	if(id!=null && id!=""){
		document.getElementById("deleteConfirmationDiv").style.display="";
		document.getElementById("idStore").value=id;
	}else
		document.getElementById("deleteConfirmationDiv").style.display="none";
}
function deleteRecord(){
	id=document.getElementById("idStore").value;
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/CourseRecords?ajax=true&do=delete&id=\"+id+\"&rand="+Math.random()) %>","reloadPage");
}
function reloadPage(){
	location.reload(location.href);
}
function validate(){
	var valNum=validateNumber("duration");
	if(valNum==false){
		document.getElementById("duration_number_error").style.display="block";
		return false;
	}else document.getElementById("duration_number_error").style.display="none";
	return validateAll("INDIVIDUAL","addCourseForm");
}
</script>
</head>
<body>
<jsp:include page="/components/navbar.jsp" />
<%
	if(request.getParameter("do")!=null && request.getParameter("do").equals("save")){
		/* Course Record Saved */
%>
<h2>Record Saved.</h2>
The new record was added.
Would you like to add another record?
<a href="<%=response.encodeURL("/ITEA/admin/CourseRecords?do=add") %>">Add New Record</a>
Would you like to view course records?
<a href="<%=response.encodeURL("/ITEA/admin/CourseRecords?start=0&perpage=10") %>">View Records</a>
<%
	}else if(request.getParameter("do")!=null && request.getParameter("do").equals("add")){
		/* Add Course Interface */
%>
<form id="addCourseForm" method="post" action="<%=response.encodeURL("/ITEA/admin/CourseRecords?do=save") %>" onsubmit="return validate()">
	<table>
		<tr>
			<td>Course Name: </td>
			<td><input type="text" name="name" id="name" maxlength="30" /></td>
			<td id="name_error" class="error">Cannot be empty.</td>
		</tr>
		<tr>
			<td>Duration: </td>
			<td><input type="text" name="duration" id="duration" /> years</td>
			<td>
				<div id="duration_error" class="error">Cannot be empty.</div><br />
				<div id="duration_number_error" class="error">Numeric value required.</div>
			</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="Submit" /> <input type="reset" value="Reset" /></td>
			<td></td>
		</tr>
	</table>
</form>
<%
	}else{
		/* Display Course Records */

		/* List Record Listing Limits */
		int perpage=10;	// default records per page=10
		if(request.getAttribute("perpage")!=null){
			perpage=(Integer)request.getAttribute("perpage");
			if(perpage!=10 && perpage!=50 && perpage!=100) perpage=0;	// reset perpage to default if value non-standard
		}
		int start=0;	// default zero
		if(request.getAttribute("start")!=null){
			start=(Integer)request.getAttribute("start");
			if(start%perpage!=0) start=0;	// reset start to default if value non-standard
		}
		
		CourseBean cb[]=(CourseBean[])request.getAttribute("cb");
		int noOfCourses=(Integer)request.getAttribute("cb_no");

		int end=start+perpage;
		if(noOfCourses<end) end=noOfCourses;
%>

<!-- Chart for entire course database -->
<div class="options">
	<!-- Displays per page -->
	<center>
		<input type="button" value="Add Course" onclick="location.href='/ITEA/admin/CourseRecords?do=add'" /><br />
		<ul>
			<li>Edit any field by clicking on it.</li>
			<li>Sort the table by clicking on a header.</li>
		</ul>
		<div id="global_error" class="error">Invalid Entry.</div>
		<div id="deleteConfirmationDiv" style="display:none">
			<fieldset>
			Are you sure you want to delete the record? <br />
			<input type="hidden" id="idStore" value="" /> <br />
			<input type="button" value="Yes" onclick="deleteRecord()" /><input type="button" value="No" onclick="confirmDeleteRecord()" />
			</fieldset>
		</div>
	</center>
	Records per page: <a href="<%=response.encodeURL("/ITEA/admin/CourseRecords?start="+start+"&perpage=10") %>">10</a> | <a href="<%=response.encodeURL("/ITEA/admin/CourseRecords?start="+start+"&perpage=50") %>">50</a> | <a href="<%=response.encodeURL("/ITEA/admin/CourseRecords?start="+start+"&perpage=100") %>">100</a>
	<br />
<%
		if(start==0){
%>
	<span style="color:#777">&lt;Previous</span> | <a href="<%=response.encodeURL("/ITEA/admin/CourseRecords?start="+end+"&perpage="+perpage) %>">Next&gt;</a>
<%
		}else if(start==noOfCourses){
			int prevStart=start-perpage;
%>
	<a href="<%=response.encodeURL("/ITEA/admin/CourseRecords?start="+prevStart+"&perpage="+perpage) %>">&lt;Previous</a> | <span style="color:#777">Next&gt;</span>
<%
		}else{
			int prevStart=start-perpage;
%>
	<a href="<%=response.encodeURL("/ITEA/admin/CourseRecords?start="+prevStart+"&perpage="+perpage) %>">&lt;Previous</a> | <a href="<%=response.encodeURL("/ITEA/admin/CourseRecords?start="+end+"&perpage="+perpage) %>">Next&gt;</a>
<%
		}
%>
</div>
<table class="chart">
	<tr>
		<th><a href="<%=response.encodeURL("/ITEA/admin/CourseRecords?start=0&perpage="+perpage+"&order_by=id") %>">Id</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/CourseRecords?start=0&perpage="+perpage+"&order_by=name") %>">Course Name</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/CourseRecords?start=0&perpage="+perpage+"&order_by=duration") %>">Duration</a></th>
		<th></th>
	</tr>
	<%
		/* List All Course Records */
		for(int i=start;i<end;i++){
			%>
				<tr>
					<td><%=cb[i].getId() %></td>
					<td onclick="modify(this,<%=cb[i].getId() %>,'name')"><%=cb[i].getName() %></td>
					<td onclick="modify(this,<%=cb[i].getId() %>,'duration')"><%=cb[i].getDuration() %></td>
					<th><input type="button" value="Delete" onclick="confirmDeleteRecord('<%=cb[i].getId() %>');" /></th>
				</tr>
			<%
		}
	%>
</table>
<div class="options">
<%
		if(start==0){
%>
<span style="color:#777">&lt;Previous</span> | <a href="<%=response.encodeURL("/ITEA/admin/CourseRecords?start="+end+"&perpage="+perpage) %>">Next&gt;</a>
<%
		}else if(start==noOfCourses){
			int prevStart=start-perpage;
%>
<a href="<%=response.encodeURL("/ITEA/admin/CourseRecords?start="+prevStart+"&perpage="+perpage) %>">&lt;Previous</a> | <span style="color:#777">Next&gt;</span>
<%
		}else{
			int prevStart=start-perpage;
%>
<a href="<%=response.encodeURL("/ITEA/admin/CourseRecords?start="+prevStart+"&perpage="+perpage) %>">&lt;Previous</a> | <a href="<%=response.encodeURL("/ITEA/admin/CourseRecords?start="+end+"&perpage="+perpage) %>">Next&gt;</a>
<%
		}
	}
%>
</div>
</body>
</html>
<%
	/* Remove Request Attributes */
	request.removeAttribute("cb");
	request.removeAttribute("cb_no");
	request.removeAttribute("perpage");
	request.removeAttribute("start");
%>