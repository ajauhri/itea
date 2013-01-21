<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="beans.BranchBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ITEA - Admin | Branch Records</title>
<jsp:include page="/components/style.jsp" />
<script type="text/javascript" src="/ITEA/components/ajax.js"></script>
<script type="text/javascript" src="/ITEA/components/validation.js"></script>
<script type="text/javascript">
function modify(elem,branch_id,field,list){
	if(list){
		makeChanges(branch_id,field,elem.value);
		return;
	}
	elem.onclick="";
	var editor="<form name='editForm' onsubmit='makeChanges("+branch_id+",\""+field+"\",this.editField.value); return false;'>";
	editor+="<input type='text' value='"+elem.innerHTML+"' name='editField' onblur='makeChanges("+branch_id+",\""+field+"\",this.value); return false;' />"
	editor+="</form>";
	elem.innerHTML=editor;
}
function makeChanges(branchId,field,value){
	/* Highly specific validation */
	var e=0;
	var ge=document.getElementById("global_error");
	if(field=="course_id" && isNaN(value)) e=1;
	if(field=="name" && value.length>30) e=1;
	if(field=="hod" && value.length>10) e=1;
	if(e==1) ge.style.display="inline";
	else ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/BranchRecords?ajax=true&do=edit&branch_id=\"+branchId+\"&field=\"+field+\"&newContent=\"+value+\"&rand="+Math.random()) %>","reloadPage");
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
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/BranchRecords?ajax=true&do=delete&id=\"+id+\"&rand="+Math.random()) %>","reloadPage");
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
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/BranchRecords?ajax=true&list=course&rand="+Math.random()) %>","displayCourses");
}
function displayCourses(optList){
	var ce=document.getElementById("course");
	var sel=ce.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	ce.innerHTML=sel+"<option value=\"-1\">--Choose One--</option>"+unescape(optList)+"</select>";
}
function selectCourse(courseStore,course_id){
	var ce=document.getElementById(courseStore);
	var courses=ce.options;
	for(var i=0;i<courses.length;i++)
		if(courses[i].value==course_id){
			courses[i].selected="selected";
			ce.selectedIndex=i;
		}
}
// HOD list is no longer required 
//function getHods(){ 
//	var he=document.getElementById("hod");
//	var sel=he.innerHTML;
//	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
//	else sel=sel.substring(0,sel.indexOf("<option"));
//	he.innerHTML=sel+"<option value=\"-1\">Loading...</option></select>";
//	ajaxRequest("<%=response.encodeURL("/ITEA/admin/BranchRecords?ajax=true&list=faculty&fac_type=2&rand="+Math.random()) %>","displayHods");
//}
//function displayHods(optList){
//	var he=document.getElementById("hod");
//	var sel=he.innerHTML;
//	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
//	else sel=sel.substring(0,sel.indexOf("<option"));
//	he.innerHTML=sel+"<option value=\"-1\">--Choose One--</option>"+optList+"</select>";
//}
function getDuration(course_id){
	var durStore=document.getElementById("durationStore");
	var durElem=document.getElementById("duration");
	if(course_id==null||course_id==""||course_id=="-1"){
		durStore.style.display="none";
		durElem.value="-1";
		return 0;
	}
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/BranchRecords?ajax=true&q=duration&course_id=\"+course_id+\"&rand="+Math.random()) %>","displayDuration");
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
</script>
</head>
<body>
<jsp:include page="/components/navbar.jsp" />
<%
	if(request.getParameter("do")!=null && request.getParameter("do").equals("save")){
		/* Branch Record Saved */
%>
<h2>Record Saved.</h2>
The new record was added.
Would you like to add another record?
<a href="<%=response.encodeURL("/ITEA/admin/BranchRecords?do=add") %>">Add New Record</a>
Would you like to view branch records?
<a href="<%=response.encodeURL("/ITEA/admin/BranchRecords?start=0&perpage=10") %>">View Records</a>
<%
	}else if(request.getParameter("do")!=null && request.getParameter("do").equals("add")){
		/* Add Branch Interface */
%>
<form id="addBranchForm" method="post" action="<%=response.encodeURL("/ITEA/admin/BranchRecords?do=save") %>" 
onsubmit="return validateAll('INDIVIDUAL','addBranchForm')">
	<table>
		<tr>
			<td>Course: </td>
			<td id="course">
				<select name="course_id" id="course_id" onchange="getDuration(this.options[this.selectedIndex].value)" onclick="getDuration(this.options[this.selectedIndex].value)">
					<option value="-1">--Choose One--</option>
					<jsp:include page="/components/CourseList" />
				</select>
			</td>
			<td id="durationStore" style="display:none">
				(Duration: <span id="durationDisplay">0</span> years)
				<input type="hidden" name="duration" id="duration" value="" />
			</td>
		</tr>
		<tr>
			<td>Branch Name: </td>
			<td><input type="text" name="name" id="name" maxlength="30" /></td>
			<td id="name_error" class="error">Cannot be empty.</td>
		</tr>
<!--	HODs are entered automatically when faculty is created.
		<tr>
			<td>
				H.O.D: <br />
				(Not mandatory)
			</td>
			<td id="hod">
				<select name="hod" id="hod_gr_no">
					<option value="-1">--Choose One--</option>
				</select>
			</td>
		</tr>
-->
		<tr>
			<td colspan="2">H.O.Ds will be automatically set when they are added.</td>
			<td></td>
		</tr>
		<tr>
			<td>No. of Academic Sessions in a Year: </td>
			<td><input type="text" name="sessionYearlyCount" id="sessionYearlyCount" /></td>
			<td id="sessionYearlyCount_number_error" class="error">Numerical value required.</td>
		</tr>
		<tr>
			<td colspan="2">Classes will be automatically added, one for each academic session in the selected course and branch.</td>
			<td></td>
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
		/* Display Branch Records */

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
		
		BranchBean bb[]=(BranchBean[])request.getAttribute("bb");
		int noOfBranches=(Integer)request.getAttribute("bb_no");

		int end=start+perpage;
		if(noOfBranches<end) end=noOfBranches;
%>

<!-- Chart for entire branch database -->
<div class="options">
	<!-- Displays per page -->
	<center>
		<input type="button" value="Add Branch" onclick="location.href='<%=response.encodeURL("/ITEA/admin/BranchRecords?do=add") %>'" /><br />
		<br /><br />
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
	Records per page: <a href="<%=response.encodeURL("/ITEA/admin/BranchRecords?start="+start+"&perpage=10") %>">10</a> | <a href="<%=response.encodeURL("BranchRecords?start="+start+"&perpage=50") %>">50</a> | <a href="<%=response.encodeURL("/ITEA/admin/BranchRecords?start="+start+"&perpage=100") %>">100</a>
	<br />
<%
		if(start==0){
%>
	<span style="color:#777">&lt;Previous</span> | <a href="<%=response.encodeURL("/ITEA/admin/BranchRecords?start="+end+"&perpage="+perpage) %>">Next&gt;</a>
<%
		}else if(start==noOfBranches){
			int prevStart=start-perpage;
%>
	<a href="<%=response.encodeURL("/ITEA/admin/BranchRecords?start="+prevStart+"&perpage="+perpage) %>">&lt;Previous</a> | <span style="color:#777">Next&gt;</span>
<%
		}else{
			int prevStart=start-perpage;
%>
	<a href="<%=response.encodeURL("/ITEA/admin/BranchRecords?start="+prevStart+"&perpage="+perpage) %>">&lt;Previous</a> | <a href="<%=response.encodeURL("/ITEA/admin/BranchRecords?start="+end+"&perpage="+perpage) %>">Next&gt;</a>
<%
		}
%></div>
<table class="chart">
	<tr>
		<th><a href="<%=response.encodeURL("/ITEA/admin/BranchRecords?start=0&perpage="+perpage+"&order_by=id") %>">Id</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/BranchRecords?start=0&perpage="+perpage+"&order_by=course_id") %>">Course</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/BranchRecords?start=0&perpage="+perpage+"&order_by=name") %>">Branch Name</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/BranchRecords?start=0&perpage="+perpage+"&order_by=hod") %>">H.O.D.</a></th>
		<th></th>
	</tr>
	<%
		/* List All Branch Records */
		String branchList="";
		String courseList="";
		for(int i=start;i<end;i++){
			%>
				<tr>
					<td><%=bb[i].getId() %></td>
					<td>
						<select id="course<%=bb[i].getId() %>" onchange="modify(this,<%=bb[i].getId() %>,'course_id',true)">
							<jsp:include page="/components/CourseList" />
						</select>
						<script type="text/javascript">
						selectCourse("course<%=bb[i].getId() %>","<%=bb[i].getCourse_id() %>");
						</script>
					</td>
					<td onclick="modify(this,<%=bb[i].getId() %>,'name')"><%=bb[i].getName() %></td>
					<td onclick="modify(this,<%=bb[i].getId() %>,'hod')"><%=bb[i].getHod() %></td>
					<th><input type="button" value="Delete" onclick="confirmDeleteRecord('<%=bb[i].getId() %>');" /></th>
				</tr>
			<%
		}
	%>
</table>
<div class="options">
<%
		if(start==0){
%>
<span style="color:#777">&lt;Previous</span> | <a href="<%=response.encodeURL("/ITEA/admin/BranchRecords?start="+end+"&perpage="+perpage) %>">Next&gt;</a>
<%
		}else if(start==noOfBranches){
			int prevStart=start-perpage;
%>
<a href="<%=response.encodeURL("/ITEA/admin/BranchRecords?start="+prevStart+"&perpage="+perpage) %>">&lt;Previous</a> | <span style="color:#777">Next&gt;</span>
<%
		}else{
			int prevStart=start-perpage;
%>
<a href="<%=response.encodeURL("/ITEA/admin/BranchRecords?start="+prevStart+"&perpage="+perpage) %>">&lt;Previous</a> | <a href="<%=response.encodeURL("/ITEA/admin/BranchRecords?start="+end+"&perpage="+perpage) %>">Next&gt;</a>
<%
		}
	}
%>
</div>
</body>
</html>
<%
	/* Remove Request Attributes */
	request.removeAttribute("bb");
	request.removeAttribute("bb_no");
	request.removeAttribute("perpage");
	request.removeAttribute("start");
%>