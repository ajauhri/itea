<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="beans.SubjectBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ITEA - Admin | Subject Records</title>
<jsp:include page="/components/style.jsp" />
<script type="text/javascript" src="/ITEA/components/ajax.js"></script>
<script type="text/javascript" src="/ITEA/components/validation.js"></script>
<script type="text/javascript">
function modify(elem,class_id,field,list){
	if(list){
		makeChanges(class_id,field,elem.value);
		return;
	}
	elem.onclick="";
	var editor="<form name='editForm' onsubmit='makeChanges("+class_id+",\""+field+"\",this.editField.value); return false;'>";
	editor+="<input type='text' value='"+elem.innerHTML+"' name='editField' onblur='makeChanges("+class_id+",\""+field+"\",this.value); return false;' />"
	editor+="</form>";
	elem.innerHTML=editor;
}
function makeChanges(subjectId,field,value){
	/* Highly specific validation */
	var e=0;
	var ge=document.getElementById("global_error");
	if(field=="class_id" && isNaN(value)) e=1;
	if(e==1) ge.style.display="inline";
	else ajaxRequest("<%=response.encodeURL("/ITEA/admin/SubjectRecords?ajax=true&do=edit&subject_id=\"+subjectId+\"&field=\"+field+\"&newContent=\"+value+\"&rand="+Math.random()) %>","reloadPage");
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
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/SubjectRecords?ajax=true&do=delete&id=\"+id+\"&rand="+Math.random()) %>","reloadPage");
}
function restoreRecord(id){
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/SubjectRecords?ajax=true&do=restore&id=\"+id+\"&rand="+Math.random()) %>","reloadPage");
}
function reloadPage(){
	location.reload(location.href);
}
function getCourses(){
	var ce=document.getElementById("course");
	var sel=ce.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	ce.innerHTML=sel+"<option value=\"-1\">Loading...</option></select>";
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/SubjectRecords?ajax=true&list=course&rand="+Math.random()) %>","displayCourses");
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
	if(course_id=="-1"){
		be.innerHTML=sel+"<option value\"-1\">--Choose a Course--</option></select>";
		return 0;
	}
	be.innerHTML=sel+"<option value=\"-1\">Loading...</option></select>";
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/SubjectRecords?ajax=true&list=branch&course_id=\"+course_id+\"&rand="+Math.random()) %>","displayBranches");	
}
function displayBranches(optList){
	var be=document.getElementById("branch");
	var sel=be.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	be.innerHTML=sel+"<option value=\"-1\">--Choose One--</option>"+unescape(optList)+"</select>";
}
function getClasses(branch_id){
	var le=document.getElementById("class");
	var sel=le.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	if(branch_id=="-1"){
		le.innerHTML=sel+"<option value=\"-1\">--Choose a Course and Branch--</option></select>";
		return 0;
	}
	le.innerHTML=sel+"<option value=\"-1\">Loading...</option></select>";
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/SubjectRecords?ajax=true&list=class&branch_id=\"+branch_id+\"&rand="+Math.random()) %>","displayClasses");	
}
function displayClasses(optList){
	var le=document.getElementById("class");
	var sel=le.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	le.innerHTML=sel+"<option value=\"-1\">--Choose One--</option>"+unescape(optList)+"</select>";
}
function selectClass(classStore,class_id){
	var le=document.getElementById(classStore);
	var classes=le.options;
	for(var i=0;i<classes.length;i++)
		if(classes[i].value==class_id){
			classes[i].selected="selected";
			le.selectedIndex=i;
		}
}
</script>
</head>
<%
	if(request.getParameter("do")!=null && request.getParameter("do").equals("add")){
		/* Add Subject body preload */
%>
<body onload="getCourses(); getLabs()">
<%
	}else{
%>
<body>
<%
	}
%>
<jsp:include page="/components/navbar.jsp" />
<%
	if(request.getParameter("do")!=null && request.getParameter("do").equals("save")){
		/* Subject Record Saved */
%>
<h2>Record Saved.</h2>
The new record was added.
Would you like to add another record?
<a href="<%=response.encodeURL("/ITEA/admin/SubjectRecords?do=add") %>">Add New Record</a>
Would you like to view subject records?
<a href="<%=response.encodeURL("/ITEA/admin/SubjectRecords?start=0&perpage=10") %>">View Records</a>
<%
	}else if(request.getParameter("do")!=null && request.getParameter("do").equals("add")){
		/* Add Subject Interface */
%>
<form id="addSubjectForm" method="post" action="<%=response.encodeURL("/ITEA/admin/SubjectRecords?do=save") %>" 
onsubmit="return validateAllExcept('INDIVIDUAL','addSubjectForm')">
	<table>
		<tr>
			<td>Class: </td>
			<td>
				<table>
					<tr>
						<td>Select a Course: </td>
						<td id="course">
							<select onchange="getBranches(this.options[this.selectedIndex].value)">
								<option value="-1" selected="selected">--Choose One--</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>Select a Branch: </td>
						<td id="branch">
							<select onchange="getClasses(this.options[this.selectedIndex].value)">
								<option value="-1" selected="selected">--Choose a Course--</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>Select a Class: </td>
						<td id="class">
							<select name="class_id">
								<option value="-1" selected="selected">--Choose a Course and Branch--</option>
							</select>
						</td>
					</tr>
				</table>
			</td>
			<td id="class_id_error" class="error">Must select an option.</td>
		</tr>
		<tr>
			<td>Name: </td>
			<td><input type="text" name="name" id="name" maxlength="30" /></td>
			<td id="name_error" class="error">Cannot be empty.</td>
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
		/* Display Subject Records */

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
		
		SubjectBean sb[]=(SubjectBean[])request.getAttribute("sb");
		int noOfSubjects=(Integer)request.getAttribute("sb_no");

		int end=start+perpage;
		if(noOfSubjects<end) end=noOfSubjects;
%>

<!-- Chart for entire subject database -->
<div class="options">
	<!-- Displays per page -->
	<center>
		<input type="button" value="Add Subject" onclick="location.href='<%=response.encodeURL("/ITEA/admin/SubjectRecords?do=add") %>'" /><br />
		<ul>
			<li>Edit any field by clicking on it.</li>
			<li>Sort the table by clicking on a header.</li>
		</ul>
		<div id="global_error" class="error">Invalid Entry.</div>
		<div id="deleteConfirmationDiv" style="display:none">
			<fieldset>
			Discarding a subject does not remove it from the database, in order that transcripts for alumni students be still valid.
			Discarded subjects can be restored to make them available again. Are you sure you want to discard the record? <br />
			<input type="hidden" id="idStore" value="" /> <br />
			<input type="button" value="Yes" onclick="deleteRecord()" /><input type="button" value="No" onclick="confirmDeleteRecord()" />
			</fieldset>
		</div>
	</center>
	Records per page: <a href="<%=response.encodeURL("/ITEA/admin/SubjectRecords?start="+start+"&perpage=10") %>">10</a> | <a href="<%=response.encodeURL("/ITEA/admin/SubjectRecords?start="+start+"&perpage=50") %>">50</a> | <a href="<%=response.encodeURL("/ITEA/admin/SubjectRecords?start="+start+"&perpage=100") %>">100</a>
	<br />
<%
		if(start==0){
%>
	<span style="color:#777">&lt;Previous</span> | <a href="<%=response.encodeURL("/ITEA/admin/SubjectRecords?start="+end+"&perpage="+perpage) %>">Next&gt;</a>
<%
		}else if(start==noOfSubjects){
			int prevStart=start-perpage;
%>
	<a href="<%=response.encodeURL("/ITEA/admin/SubjectRecords?start="+prevStart+"&perpage="+perpage) %>">&lt;Previous</a> | <span style="color:#777">Next&gt;</span>
<%
		}else{
			int prevStart=start-perpage;
%>
	<a href="<%=response.encodeURL("/ITEA/admin/SubjectRecords?start="+prevStart+"&perpage="+perpage) %>">&lt;Previous</a> | <a href="<%=response.encodeURL("/ITEA/admin/SubjectRecords?start="+end+"&perpage="+perpage) %>">Next&gt;</a>
<%
			}
%>

</div>
<table class="chart">
	<tr>
		<th><a href="<%=response.encodeURL("/ITEA/admin/SubjectRecords?start=0&perpage="+perpage+"&order_by=id") %>">Id</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/SubjectRecords?start=0&perpage="+perpage+"&order_by=class_id") %>">Class</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/SubjectRecords?start=0&perpage="+perpage+"&order_by=name") %>">Name</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/SubjectRecords?start=0&perpage="+perpage+"&order_by=lectures_held") %>">Lectures Held</a></th>
		<th></th>
	</tr>
	<%
		/* List All Class Records */
		for(int i=start;i<end;i++){
			if(sb[i].getValid()==0){
			%>
				<tr>
					<td style="color:#777;"><%=sb[i].getId() %></td>
					<td style="color:#777;">
						<select id="class<%=sb[i].getId() %>">
							<jsp:include page="/components/ClassList" />
						</select>
						<script type="text/javascript">
						selectClass("class<%=sb[i].getId() %>","<%=sb[i].getClass_id() %>");
						</script>
					</td>
					<td style="color:#777;"><%=sb[i].getName() %></td>
					<td style="color:#777;"><%=sb[i].getLectures_held() %></td>
					<th><input type="button" value="Restore" onclick="restoreRecord('<%=sb[i].getId() %>');" /></th>
				</tr>
			<%
			}else{
			%>
				<tr>
					<td><%=sb[i].getId() %></td>
					<td>
						<select id="class<%=sb[i].getId() %>" onchange="modify(this,<%=sb[i].getId() %>,'class_id',true)">
							<jsp:include page="/components/ClassList" />
						</select>
						<script type="text/javascript">
						selectClass("class<%=sb[i].getId() %>","<%=sb[i].getClass_id() %>");
						</script>
					</td>
					<td onclick="modify(this,<%=sb[i].getId() %>,'name')"><%=sb[i].getName() %></td>
					<td><%=sb[i].getLectures_held() %></td>
					<th><input type="button" value="Discard" onclick="confirmDeleteRecord('<%=sb[i].getId() %>');" /></th>
				</tr>
			<%
			}
		}
	%>
</table>
<div class="options">
<%
		if(start==0){
%>
<span style="color:#777">&lt;Previous</span> | <a href="<%=response.encodeURL("/ITEA/admin/SubjectRecords?start="+end+"&perpage="+perpage) %>">Next&gt;</a>
<%
		}else if(start==noOfSubjects){
			int prevStart=start-perpage;
%>
<a href="<%=response.encodeURL("/ITEA/admin/SubjectRecords?start="+prevStart+"&perpage="+perpage) %>">&lt;Previous</a> | <span style="color:#777">Next&gt;</span>
<%
		}else{
			int prevStart=start-perpage;
%>
<a href="<%=response.encodeURL("/ITEA/admin/SubjectRecords?start="+prevStart+"&perpage="+perpage) %>">&lt;Previous</a> | <a href="<%=response.encodeURL("/ITEA/admin/SubjectRecords?start="+end+"&perpage="+perpage) %>">Next&gt;</a>
<%
			}
	}
%>
</div>
</body>
</html>
<%
	/* Remove Request Attributes */
	request.removeAttribute("sb");
	request.removeAttribute("sb_no");
	request.removeAttribute("perpage");
	request.removeAttribute("start");
%>