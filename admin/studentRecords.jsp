<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="beans.StudentBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ITEA - Admin | Student Records</title>
<jsp:include page="/components/style.jsp" />
<script type="text/javascript" src="/ITEA/components/ajax.js"></script>
<script type="text/javascript" src="/ITEA/components/validation.js"></script>
<script type="text/javascript">
function validate(){
	var form=document.getElementById("addStudentForm");
	var valNum=true;
	var numElems=new Array("roll_no","year_of_admission","dd","mm","yyyy","phone_no");
	for(var i=0;i<numElems.length;i++){
		valNum=validateNumber(numElems[i]);
		if(valNum==false){
			document.getElementById(numElems[i]+"_number_error").style.display="block";
			return false;
		}else  document.getElementById(numElems[i]+"_number_error").style.display="none";
	}
	valNum=validateEmail("email");
	if(valNum==false){
		document.getElementById("email_email_error").style.display="block";
		return false;
	}else  document.getElementById("email_email_error").style.display="none";
	if(document.getElementById("dd").value.length<2) document.getElementById("dd").value="0"+document.getElementById("dd").value;	// fix length of dd
	if(document.getElementById("mm").value.length<2) document.getElementById("mm").value="0"+document.getElementById("mm").value;	// fix length of mm
	return validateAllExcept("INDIVIDUAL","addStudentForm","mname");
}
function modify(elem,gr_no,field,list){
	if(list){
		makeChanges(gr_no,field,elem.value);
		return;
	}
	elem.onclick="";
	var editor="<form name='editForm' onsubmit='makeChanges(\""+gr_no+"\",\""+field+"\",this.editField.value); return false;'>";
	editor+="<input type='text' value='"+elem.innerHTML+"' name='editField' onblur='makeChanges(\""+gr_no+"\",\""+field+"\",this.value); return false;'/>"
	editor+="</form>";
	elem.innerHTML=editor;
}
function makeChanges(gr_no,field,value){
	/* Highly specific validation */
	var e=0;
	var ge=document.getElementById("global_error");
	if(field=="roll_no" && isNaN(value)) e=1;
	if(field=="class_id" && isNaN(value)) e=1;
	if(field=="year_of_joining" && (isNaN(value) || value.length!=4)) e=1;
	if(field=="phone_no" && isNaN(value)) e=1;
	if(field=="dd" && (isNaN(value) || value.length!=2)) e=1;
	if(field=="mm" && (isNaN(value) || value.length!=2)) e=1;
	if(field=="yyyy" && (isNaN(value) || value.length!=4)) e=1;
	if(field=="email" && !validateEmailDirect(value)) e=1;
	if(e==1) ge.style.display="inline";
	else ajaxRequest("<%=response.encodeURL("/ITEA/admin/StudentRecords?ajax=true&do=edit&gr_no=\"+gr_no+\"&field=\"+field+\"&newContent=\"+value+\"&rand="+Math.random()) %>","reloadPage");
}
function validateEmailDirect(value){
	if(value==null || value=="") return false;
	var at_pos=value.indexOf("@");
	var dot_pos=value.indexOf(".");
	if(at_pos==-1 || dot_pos==-1) return false;
	if(!(0<at_pos-1 && at_pos<dot_pos-1 && dot_pos<value.length-1))
		return false;
	return true;
}
function manGr_no(){
	ge=document.getElementById("gr_no");
	ge.disabled="";
	ge.value="";
	gs=document.getElementById("gr_noSwitcher");
	gs.innerHTML="Set Automatically";
	gs.onclick=automateGr_no;
}
function automateGr_no(){
	ge=document.getElementById("gr_no");
	ge.disabled="disabled";
	ge.value="(Automatically Generated)";
	gs=document.getElementById("gr_noSwitcher");
	gs.innerHTML="Set Manually";
	gs.onclick=manGr_no;
}
function confirmDeleteRecord(id){
	if(id!=null && id!=""){
		document.getElementById("deleteConfirmationDiv").style.display="";
		document.getElementById("gr_noStore").value=id;
	}else
		document.getElementById("deleteConfirmationDiv").style.display="none";
}
function deleteRecord(){
	id=document.getElementById("gr_noStore").value;
	ajaxRequest("<%=response.encodeURL("/ITEA/admin/StudentRecords?ajax=true&do=delete&gr_no=\"+id+\"&rand="+Math.random()) %>","reloadPage");
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
	be=document.getElementById("branch");
	if(be==null) return;	// for non do=add requests
	be.disabled="disabled";
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/StudentRecords?ajax=true&list=branch&course_id=\"+course_id+\"&rand="+Math.random()) %>","displayBranches");
}
function displayBranches(optList){
	be=document.getElementById("branch");
	be.disabled="";
	var sel=be.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION")); // IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
//	var sel="<select onchange=\"getClasses(this.options[this.selectedIndex].value)\">";
	sel+="<option value=\"-1\" selected=\"selected\">--Choose One--</option>"+unescape(optList);
	sel+="</select>";
	be.innerHTML=sel;
}
function getClasses(branch_id){
	le=document.getElementById("class");
	le.disabled="disabled";
	if(branch_id=="-1"){
		le.innerHTML="<select name=\"class_id\" disabled=\"disabled\"><option value=\"-1\">--Choose a Branch--</option></select>";
		return;
	}
	le.innerHTML="<select name=\"class_id\"><option value=\"-1\" selected=\"selected\">Loading...</option></select>";
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/StudentRecords?ajax=true&list=class&branch_id=\"+branch_id+\"&rand="+Math.random()) %>","displayClasses");
}
function displayClasses(optList){
	le=document.getElementById("class");
	le.disabled="";
	var sel=le.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION")); // IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
//	var sel="<select name=\"class_id\">";
	sel+="<option value=\"-1\" selected=\"selected\">--Choose One--</option>"+unescape(optList);
	sel+="</select>";
	le.innerHTML=sel;
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
<body>
<jsp:include page="/components/navbar.jsp" />
<%
	if(request.getParameter("do")!=null && request.getParameter("do").equals("save")){
		/* Student Record Saved */
%>
<h2>Record Saved.</h2>
The new record was added.
Would you like to add another record?
<a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?do=add") %>">Add New Record</a>
Would you like to view student records?
<a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start=0&perpage=10") %>">View Records</a>
<%
	}else if(request.getParameter("do")!=null && request.getParameter("do").equals("add")){
		/* Add Student Interface */
%>
<form name="addStudentForm" id="addStudentForm" method="post" action="<%=response.encodeURL("/ITEA/admin/StudentRecords?do=save") %>" 
onsubmit="return validate()">
	<table width="100%">
		<tr>
			<td>Gr No: </td>
			<td>
				<input type="text" id="gr_no" disabled="disabled" name="gr_no" value="(Automatically Generated)" size="22" maxlength="10" />
				<a href="javascript:void(0)" onclick="manGr_no()" id="gr_noSwitcher">Set Manually</a>
			</td>
			<td id="gr_no_error" class="error">Cannot be empty.</td>
		</tr>
		<tr>
			<td>Roll No: </td>
			<td><input type="text" name="roll_no" id="roll_no" /></td>
			<td>
				<div id="roll_no_error" class="error">Cannot be empty.</div>
				<div id="roll_no_number_error" class="error">Numeric value required.</div>
			</td>
		</tr>
		<tr>
			<td>Class Id: </td>
			<td>
				<table>
					<tr>
						<td>Select a Course: </td>
						<td>
							<div id="course">
								<select onchange="getBranches(this.options[this.selectedIndex].value)">
									<option value="-1">--Choose One--</option>
									<jsp:include page="/components/CourseList" />
								</select>
							</div>
						</td>
					</tr>
					<tr>
						<td>Select a Branch: </td>
						<td>
							<div id="branch">
								<select onchange="getClasses(this.options[this.selectedIndex].value)">
									<option value="-1" selected="selected">--Choose a Course--</option>
								</select>
							</div>
						</td>
					</tr>
					<tr>
						<td>Select a Class: </td>
						<td>
							<div id="class">
								<select name="class_id">
									<option value="-1" selected="selected">--Choose a Branch--</option>
								</select>
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>Year of Admission: </td>
			<td><input type="text" name="year_of_admission" id="year_of_admission" maxlength="4" /></td>
			<td>
				<div id="year_of_admission_error" class="error">Cannot be empty.</div>
				<div id="year_of_admission_number_error" class="error">Numeric value required.</div>
			</td>
		</tr>
		<tr>
			<td>First Name: </td>
			<td><input type="text" name="fname" id="fname" /></td>
			<td id="fname_error" class="error">Cannot be empty.</td>
		</tr>
		<tr>
			<td>Middle Name: </td>
			<td><input type="text" name="mname" id="mname" /></td>
		</tr>
		<tr>
			<td>Last Name: </td>
			<td><input type="text" name="lname" id="lname" /></td>
			<td id="lname_error" class="error">Cannot be empty.</td>
		</tr>
		<tr>
			<td>Date of Birth: </td>
			<td>
				<table>
					<tr>
						<td><input type="text" name="dd" size="2" maxlength="2" id="dd" /></td>
						<td> / </td>
						<td><input type="text" name="mm" size="2" maxlength="2" id="mm" /></td>
						<td> / </td>
						<td><input type="text" name="yyyy" size="4" maxlength="4" id="yyyy" /></td>
					</tr>
					<tr style="font-size:small">
						<td align="center">dd</td>
						<td> / </td>
						<td align="center">mm</td>
						<td> / </td>
						<td align="center">yyyy</td>
					</tr>
				</table>
			</td>
			<td>
				<div id="dd_error" class="error">Day ('dd') cannot be empty.</div>
				<div id="dd_number_error" class="error">Numerical value required for day ('dd').</div>
				<div id="mm_error" class="error">Month ('mm') cannot be empty.</div>
				<div id="mm_number_error" class="error">Numerical value required for month ('mm').</div>
				<div id="yyyy_error" class="error">Year ('yyyy') cannot be empty.</div>
				<div id="yyyy_number_error" class="error">Numerical value required for year ('yyyy').</div>
			</td>
		<tr>
			<td>House No: </td>
			<td><input type="text" name="house_no" id="house_no" /></td>
			<td id="house_no_error" class="error">Cannot be empty.</td>
		</tr>
		<tr>
			<td>Street: </td>
			<td><input type="text" name="street" id="street" /></td>
			<td id="street_error" class="error">Cannot be empty.</td>
		</tr>
		<tr>
			<td>City: </td>
			<td><input type="text" name="city" id="city" /></td>
			<td id="city_error" class="error">Cannot be empty.</td>
		</tr>
		<tr>
			<td>Pincode: </td>
			<td><input type="text" name="pincode" id="pincode" /></td>
			<td id="pincode_error" class="error">Cannot be empty.</td>
		</tr>
		<tr>
			<td>State: </td>
			<td><input type="text" name="state" id="state" /></td>
			<td id="state_error" class="error">Cannot be empty.</td>
		</tr>
		<tr>
			<td>Phone No: </td>
			<td><input type="text" name="phone_no" id="phone_no" /></td>
			<td>
				<div id="phone_no_error" class="error">Cannot be empty.</div>
				<div id="phone_no_number_error" class="error">Numerical value required.</div>
			</td>
		</tr>
		<tr>
			<td>Email: </td>
			<td><input type="text" name="email" id="email" /></td>
			<td>
				<div id="email_error" class="error">Cannot be empty.</div>
				<div id="email_email_error" class="error">Must be a valid email address.</div>
			</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="Submit" /> <input type="reset" value="Reset" /></td>
		</tr>
	</table>
</form>
<%
	}else{
		/* Display Student Records */

		/* List Record Listing Limits */
		int perpage=10;	// default records per page=10
		if(request.getAttribute("perpage")!=null){
			perpage=(Integer)request.getAttribute("perpage");
			if(perpage!=10 && perpage!=50 && perpage!=100) perpage=10;	// reset perpage to default if value non-standard
		}
		int start=0;	// default zero
		if(request.getAttribute("start")!=null){
			start=(Integer)request.getAttribute("start");
			if(start%perpage!=0) start=0;	// reset start to default if value non-standard
		}
		
		StudentBean sb[]=(StudentBean[])request.getAttribute("sb");
		int noOfStudents=(Integer)request.getAttribute("sb_no");

		int end=start+perpage;
		if(noOfStudents<end) end=noOfStudents;
%>

<!-- Chart for entire students database -->
<div class="options">
	<!-- Displays per page -->
	<center>
		<input type="button" value="Add Student" onclick="location.href='<%=response.encodeURL("/ITEA/admin/StudentRecords?do=add") %>'" /><br />
		<ul>
			<li>Edit any field by clicking on it.</li>
			<li>Sort the table by clicking on a header.</li>
		</ul>
		<div id="global_error" class="error">Invalid Entry.</div>
		<div id="deleteConfirmationDiv" style="display:none">
			<fieldset>
			Are you sure you want to delete the record? <br />
			<input type="hidden" id="gr_noStore" value="" /> <br />
			<input type="button" value="Yes" onclick="deleteRecord()" /><input type="button" value="No" onclick="confirmDeleteRecord()" />
			</fieldset>
		</div>
	</center>
	Records per page: <a href="/ITEA/admin/StudentRecords?start=<%=start %>&perpage=10">10</a> | <a href="/ITEA/admin/StudentRecords?start=<%=start %>&perpage=50">50</a> | <a href="/ITEA/admin/StudentRecords?start=<%=start %>&perpage=100">100</a>
	<br />
<%
		if(start==0){
%>
	<span style="color:#777">&lt;Previous</span> | <a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start="+end+"&perpage="+perpage) %>">Next&gt;</a>
<%
		}else if(end==noOfStudents){
			int prevStart=start-perpage;
%>
	<a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start="+prevStart+"&perpage="+perpage) %>">&lt;Previous</a> | <span style="color:#777">Next&gt;</span>
<%
		}else{
			int prevStart=start-perpage;
%>
	<a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start="+prevStart+"&perpage="+perpage) %>">&lt;Previous</a> | <a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start="+end+"&perpage="+perpage) %>">Next&gt;</a>
<%
		}
%>
</div>
<table class="chart">
	<tr>
		<th rowspan="2"><a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start=0&perpage="+perpage+"&order_by=gr_no") %>">GR No.</a></th>
		<th rowspan="2"><a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start=0&perpage="+perpage+"&order_by=roll_no") %>">Roll No.</a></th>
		<th rowspan="2"><a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start=0&perpage="+perpage+"&order_by=class_id") %>">Class</a></th>
		<th rowspan="2"><a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start=0&perpage="+perpage+"&order_by=year_of_admission") %>">Year of Admission</a></th>
		<th colspan="3">Name</th>
		<th colspan="5">Address</th>
		<th colspan="2">Contact</th>
		<th colspan="3">Date of Birth</th>
		<th></th>
	</tr>
	<tr>
		<th><a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start=0&perpage="+perpage+"&order_by=fname") %>">First Name</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start=0&perpage="+perpage+"&order_by=mname") %>">Middle Name</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start=0&perpage="+perpage+"&order_by=lname") %>">Last Name</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start=0&perpage="+perpage+"&order_by=house_no") %>">House No.</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start=0&perpage="+perpage+"&order_by=street") %>">Street</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start=0&perpage="+perpage+"&order_by=city") %>">City</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start=0&perpage="+perpage+"&order_by=pincode") %>">Pincode</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start=0&perpage="+perpage+"&order_by=state") %>">State</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start=0&perpage="+perpage+"&order_by=phone_no") %>">Phone No.</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start=0&perpage="+perpage+"&order_by=email") %>">Email</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start=0&perpage="+perpage+"&order_by=dd") %>">dd</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start=0&perpage="+perpage+"&order_by=mm") %>">mm</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start=0&perpage="+perpage+"&order_by=yyyy") %>">yyyy</a></th>
		<th></th>
	</tr>
	<%
		/* List All Student Records */
		for(int i=start;i<end;i++){
			%>
				<tr>
					<td onclick="modify(this,'<%=sb[i].getGr_no() %>','gr_no')"><%=sb[i].getGr_no() %></td>
					<td onclick="modify(this,'<%=sb[i].getGr_no() %>','roll_no')"><%=sb[i].getRoll_no() %></td>
					<td>
						<select id="class<%=sb[i].getGr_no() %>" onchange="modify(this,'<%=sb[i].getGr_no() %>','class_id',true)">
							<jsp:include page="/components/ClassList" />
						</select>
						<script type="text/javascript">
						selectClass("class<%=sb[i].getGr_no() %>","<%=sb[i].getClass_id() %>");
						</script>
					</td>
					<td onclick="modify(this,'<%=sb[i].getGr_no() %>','year_of_admission')"><%=sb[i].getYear_of_admission() %></td>
					<td onclick="modify(this,'<%=sb[i].getGr_no() %>','fname')"><%=sb[i].getPersonal_details().getFname() %></td>
					<td onclick="modify(this,'<%=sb[i].getGr_no() %>','mname')"><%=sb[i].getPersonal_details().getMname() %></td>
					<td onclick="modify(this,'<%=sb[i].getGr_no() %>','lname')"><%=sb[i].getPersonal_details().getLname() %></td>
					<td onclick="modify(this,'<%=sb[i].getGr_no() %>','house_no')"><%=sb[i].getPersonal_details().getHouse_no() %></td>
					<td onclick="modify(this,'<%=sb[i].getGr_no() %>','street')"><%=sb[i].getPersonal_details().getStreet() %></td>
					<td onclick="modify(this,'<%=sb[i].getGr_no() %>','city')"><%=sb[i].getPersonal_details().getCity() %></td>
					<td onclick="modify(this,'<%=sb[i].getGr_no() %>','pincode')"><%=sb[i].getPersonal_details().getPincode() %></td>
					<td onclick="modify(this,'<%=sb[i].getGr_no() %>','state')"><%=sb[i].getPersonal_details().getState() %></td>
					<td onclick="modify(this,'<%=sb[i].getGr_no() %>','phone_no')"><%=sb[i].getPersonal_details().getPhone_no() %></td>
					<td onclick="modify(this,'<%=sb[i].getGr_no() %>','email')"><%=sb[i].getPersonal_details().getEmail() %></td>
					<td onclick="modify(this,'<%=sb[i].getGr_no() %>','dd')"><%=sb[i].getPersonal_details().getDob().getDd() %></td>
					<td onclick="modify(this,'<%=sb[i].getGr_no() %>','mm')"><%=sb[i].getPersonal_details().getDob().getMm() %></td>
					<td onclick="modify(this,'<%=sb[i].getGr_no() %>','yyyy')"><%=sb[i].getPersonal_details().getDob().getYyyy() %></td>
					<th><input type="button" value="Delete" onclick="confirmDeleteRecord('<%=sb[i].getGr_no() %>');" /></th>
				</tr>
			<%
		}
	%>
</table>
<div class="options">
<%
		if(start==0){
%>
<span style="color:#777">&lt;Previous</span> | <a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start="+end+"&perpage="+perpage) %>">Next&gt;</a>
<%
		}else if(end==noOfStudents){
			int prevStart=start-perpage;
%>
<a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start="+prevStart+"&perpage="+perpage) %>">&lt;Previous</a> | <span style="color:#777">Next&gt;</span>
<%
		}else{
			int prevStart=start-perpage;
%>
<a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start="+prevStart+"&perpage="+perpage) %>">&lt;Previous</a> | <a href="<%=response.encodeURL("/ITEA/admin/StudentRecords?start="+end+"&perpage="+perpage) %>">Next&gt;</a>
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