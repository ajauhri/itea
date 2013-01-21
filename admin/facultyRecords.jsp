<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="beans.FacultyBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ITEA - Admin | Faculty Records</title>
<jsp:include page="/components/style.jsp" />
<script type="text/javascript" src="/ITEA/components/ajax.js"></script>
<script type="text/javascript" src="/ITEA/components/validation.js"></script>
<script type="text/javascript">
function validate(){
	var form=document.getElementById("addFacultyForm");
	var valNum=true;
	var numElems=new Array("year_of_joining","dd","mm","yyyy","phone_no");
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
	return validateAllExcept("INDIVIDUAL","addFacultyForm","mname","fac_type");
}
function modify(elem,gr_no,field,list){
	if(list){
		makeChanges(gr_no,field,elem.value);
		return;
	}
	elem.onclick="";
	var editor="<form name='editForm' onsubmit='makeChanges(\""+gr_no+"\",\""+field+"\",this.editField.value); return false;'>";
	editor+="<input type='text' value='"+elem.innerHTML+"' name='editField' onblur='makeChanges(\""+gr_no+"\",\""+field+"\",this.value); return false;' />"
	editor+="</form>";
	elem.innerHTML=editor;
	elem.form.editField.focus();
}
function makeChanges(gr_no,field,value){
	/* Highly specific validation */
	var e=0;
	var ge=document.getElementById("global_error");
	if(field=="fac_type" && value!="0" && value!="1" && value!="2" && value!="3") e=1;
	if(field=="branch_id" && isNaN(value)) e=1;
	if(field=="year_of_joining" && (isNaN(value) || value.length!=4)) e=1;
	if(field=="fname" && value.length>30) e=1;
	if(field=="mname" && value.length>30) e=1;
	if(field=="lname" && value.length>30) e=1;
	if(field=="house_no" && value.length>30) e=1;
	if(field=="street" && value.length>30) e=1;
	if(field=="city" && value.length>30) e=1;
	if(field=="pincode" && value.length>30) e=1;
	if(field=="state" && value.length>30) e=1;
	if(field=="phone_no" && isNaN(value)) e=1;
	if(field=="dd" && (isNaN(value) || value.length!=2)) e=1;
	if(field=="mm" && (isNaN(value) || value.length!=2)) e=1;
	if(field=="yyyy" && (isNaN(value) || value.length!=4)) e=1;
	if(field=="email" && !validateEmailDirect(value)) e=1;
	if(e==1) ge.style.display="inline";
	else ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/FacultyRecords?ajax=true&do=edit&gr_no=\"+gr_no+\"&field=\"+field+\"&newContent=\"+value+\"&rand="+Math.random()) %>","reloadPage");
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
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/FacultyRecords?ajax=true&do=delete&gr_no=\"+id+\"&rand="+Math.random()) %>","reloadPage");
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
//	be.innerHTML="<option value=\"-1\">Loading...</option>";
	ajaxRequestEscaped("<%=response.encodeURL("/ITEA/admin/FacultyRecords?ajax=true&list=branch&course_id=\"+course_id+\"&rand="+Math.random()) %>","displayBranches");
}
function displayBranches(optList){
	be=document.getElementById("branch");
	var sel="<select name=\"branch_id\">";
	sel+="<OPTION value=\"0\" selected=\"selected\">--Choose One--</option>"+unescape(optList);
	sel+="</select>";
	be.innerHTML=sel;
}
function selectBranch(branchStore,branch_id){
	var be=document.getElementById(branchStore);
	var branches=be.options;
	for(var i=0;i<branches.length;i++)
		if(branches[i].value==branch_id){
			branches[i].selected="selected";
			be.selectedIndex=i;
		}
}
</script>
</head>
<body>
<jsp:include page="/components/navbar.jsp" />
<%
	if(request.getParameter("do")!=null && request.getParameter("do").equals("save")){
		/* Faculty Record Saved */
%>
<h2>Record Saved.</h2>
The new record was added.
Would you like to add another record?
<a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?do=add") %>">Add New Record</a>
Would you like to view facutly records?
<a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start=0&perpage=10") %>">View Records</a>
<%
	}else if(request.getParameter("do")!=null && request.getParameter("do").equals("add")){
		/* Add Faculty Interface */
%>
<form name="addFacultyForm" id="addFacultyForm" method="post" action="<%=response.encodeURL("/ITEA/admin/FacultyRecords?do=save") %>"
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
			<td>Faculty Type: </td>
			<td>
				<select name="fac_type" id="fac_type" onchange="with(document.getElementById('branchStore')) this.value=='3'?style.display='none':style.display='block'">
					<option value="0" selected="selected">Internal Faculty</option>
					<option value="1">Visiting Faculty</option>
					<option value="2">Head of Department</option>
					<option value="3">Dean</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>Branch Id: </td>
			<td>
				<table id="branchStore">
					<tr>
						<td>Select a Course: </td>
						<td>
							<div id="course">
								<select name="course" id="course" onchange="getBranches(this.options[this.selectedIndex].value)">
									<option value="-1" selected="selected">--Choose One--</option>
									<jsp:include page="/components/CourseList" />
								</select>
							</div>
						</td>
						<td class="error" id="course_error" />
					</tr>
					<tr>
						<td>Select a Branch: </td>
						<td>
							<div id="branch">
								<select name="branch_id" id="branch_id">
									<option value="-1" selected="selected">--Choose a Class--</option>
								</select>
							</div>
						</td>
						<td class="error" id="branch_id_error" />
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>Year of Joining: </td>
			<td><input type="text" name="year_of_joining" id="year_of_joining" maxlength="4" /></td>
			<td>
				<div id="year_of_joining_error" class="error">Cannot be empty.</div>
				<div id="year_of_joining_number_error" class="error">Numeric value required.</div>
			</td>
		</tr>
		<tr>
			<td>First Name: </td>
			<td><input type="text" name="fname" id="fname" /></td>
			<td id="fname_error" class="error">Cannot be empty.</td>
		</tr>
		<tr>
			<td>Middle Name: </td>
			<td><input type="text" name="mname" /></td>
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
						<td><input type="text" name="dd" id="dd" size="2" maxlength="2" /></td>
						<td> / </td>
						<td><input type="text" name="mm" id="mm" size="2" maxlength="2" /></td>
						<td> / </td>
						<td><input type="text" name="yyyy" id="yyyy" size="4" maxlength="4" /></td>
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
				<div id="phone_no_number_error" class="error">Numeric value required.</div>
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
		/* Display Faculty Records */

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

		FacultyBean fb[]=(FacultyBean[])request.getAttribute("fb");
		int noOfFaculty=(Integer)request.getAttribute("fb_no");

		int end=start+perpage;
		if(noOfFaculty<end) end=noOfFaculty;
%>

<!-- Chart for entire faculty database -->
<div class="options">
	<!-- Displays per page -->
	<center>
		<input type="button" value="Add Faculty" onclick="location.href='<%=response.encodeURL("/ITEA/admin/FacultyRecords?do=add") %>'" /><br />
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
	Records per page: <a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start="+start+"&perpage=10") %>">10</a> | <a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start="+start+"&perpage=50") %>">50</a> | <a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start="+start+"&perpage=100") %>">100</a>
	<br />
<%
		if(start==0){
%>
	<span style="color:#777">&lt;Previous</span> | <a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start="+end+"&perpage="+perpage) %>">Next&gt;</a>
<%
		}else if(end==noOfFaculty){
			int prevStart=start-perpage;
%>
	<a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start="+prevStart+"&perpage="+perpage) %>">&lt;Previous</a> | <span style="color:#777">Next&gt;</span>
<%
		}else{
			int prevStart=start-perpage;
%>
	<a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start="+prevStart+"&perpage="+perpage) %>">&lt;Previous</a> | <a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start="+end+"&perpage="+perpage) %>">Next&gt;</a>
<%
		}
%>
</div>
<table class="chart">
	<tr>
		<th rowspan="2"><a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start=0&perpage="+perpage+"&order_by=gr_no") %>">GR No.</a></th>
		<th rowspan="2"><a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start=0&perpage="+perpage+"&order_by=fac_type") %>">Faculty Type</a></th>
		<th rowspan="2"><a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start=0&perpage="+perpage+"&order_by=branch_id") %>">Branch</a></th>
		<th rowspan="2"><a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start=0&perpage="+perpage+"&order_by=year_of_joining") %>">Year of Joining</a></th>
		<th colspan="3">Name</th>
		<th colspan="5">Address</th>
		<th colspan="2">Contact</th>
		<th colspan="3">Date of Birth</th>
		<th></th>
	</tr>
	<tr>
		<th><a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start=0&perpage="+perpage+"&order_by=fname") %>">First Name</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start=0&perpage="+perpage+"&order_by=mname") %>">Middle Name</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start=0&perpage="+perpage+"&order_by=lname") %>">Last Name</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start=0&perpage="+perpage+"&order_by=house_no") %>">House No.</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start=0&perpage="+perpage+"&order_by=street") %>">Street</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start=0&perpage="+perpage+"&order_by=city") %>">City</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start=0&perpage="+perpage+"&order_by=pincode") %>">Pincode</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start=0&perpage="+perpage+"&order_by=state") %>">State</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start=0&perpage="+perpage+"&order_by=phone_no") %>">Phone No.</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start=0&perpage="+perpage+"&order_by=email") %>">Email</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start=0&perpage="+perpage+"&order_by=dd") %>">dd</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start=0&perpage="+perpage+"&order_by=mm") %>">mm</a></th>
		<th><a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start=0&perpage="+perpage+"&order_by=yyyy") %>">yyyy</a></th>
		<th></th>
	</tr>
	<%
		/* List All Faculty Records */
		for(int i=start;i<end;i++){
			%>
				<tr>
					<td onclick="modify(this,'<%=fb[i].getGr_no() %>','gr_no')"><%=fb[i].getGr_no() %></td>
					<td>
						<select id="fac_type<%=fb[i].getGr_no() %>" onchange="modify(this,'<%=fb[i].getGr_no() %>','fac_type',true)">
							<option value="0" <%=(fb[i].getFac_type()==0)?"selected=\"selected\"":"" %>>Internal Faculty</option>
							<option value="1" <%=(fb[i].getFac_type()==1)?"selected=\"selected\"":"" %>>Visiting Faculty</option>
							<option value="2" <%=(fb[i].getFac_type()==2)?"selected=\"selected\"":"" %>>Head of Department</option>
							<option value="3" <%=(fb[i].getFac_type()==3)?"selected=\"selected\"":"" %>>Dean</option>
						</select>
					</td>
					<td>
					<% 
						if(fb[i].getFac_type()!=3){
					%>
						<select id="branch<%=fb[i].getGr_no() %>" onchange="modify(this,'<%=fb[i].getGr_no() %>','branch_id',true)"><%=fb[i].getBranch_id() %>
							<jsp:include page="/components/BranchList" />
						</select>
						<script type="text/javascript">
						selectBranch("branch<%=fb[i].getGr_no() %>","<%=fb[i].getBranch_id() %>");
						</script>
					<%
						}
					%>
					</td>
					<td onclick="modify(this,'<%=fb[i].getGr_no() %>','year_of_joining')"><%=fb[i].getYear_of_joining() %></td>
					<td onclick="modify(this,'<%=fb[i].getGr_no() %>','fname')"><%=fb[i].getPersonal_details().getFname() %></td>
					<td onclick="modify(this,'<%=fb[i].getGr_no() %>','mname')"><%=fb[i].getPersonal_details().getMname() %></td>
					<td onclick="modify(this,'<%=fb[i].getGr_no() %>','lname')"><%=fb[i].getPersonal_details().getLname() %></td>
					<td onclick="modify(this,'<%=fb[i].getGr_no() %>','house_no')"><%=fb[i].getPersonal_details().getHouse_no() %></td>
					<td onclick="modify(this,'<%=fb[i].getGr_no() %>','street')"><%=fb[i].getPersonal_details().getStreet() %></td>
					<td onclick="modify(this,'<%=fb[i].getGr_no() %>','city')"><%=fb[i].getPersonal_details().getCity() %></td>
					<td onclick="modify(this,'<%=fb[i].getGr_no() %>','pincode')"><%=fb[i].getPersonal_details().getPincode() %></td>
					<td onclick="modify(this,'<%=fb[i].getGr_no() %>','state')"><%=fb[i].getPersonal_details().getState() %></td>
					<td onclick="modify(this,'<%=fb[i].getGr_no() %>','phone_no')"><%=fb[i].getPersonal_details().getPhone_no() %></td>
					<td onclick="modify(this,'<%=fb[i].getGr_no() %>','email')"><%=fb[i].getPersonal_details().getEmail() %></td>
					<td onclick="modify(this,'<%=fb[i].getGr_no() %>','dd')"><%=fb[i].getPersonal_details().getDob().getDd() %></td>
					<td onclick="modify(this,'<%=fb[i].getGr_no() %>','mm')"><%=fb[i].getPersonal_details().getDob().getMm() %></td>
					<td onclick="modify(this,'<%=fb[i].getGr_no() %>','yyyy')"><%=fb[i].getPersonal_details().getDob().getYyyy() %></td>
					<th><input type="button" value="Delete" onclick="confirmDeleteRecord('<%=fb[i].getGr_no() %>');" /></th>
				</tr>
			<%
		}
	%>
</table>
<div class="options">
<%
		if(start==0){
%>
<span style="color:#777">&lt;Previous</span> | <a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start="+end+"&perpage="+perpage) %>">Next&gt;</a>
<%
		}else if(end==noOfFaculty){
			int prevStart=start-perpage;
%>
<a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start="+prevStart+"&perpage="+perpage) %>">&lt;Previous</a> | <span style="color:#777">Next&gt;</span>
<%
		}else{
			int prevStart=start-perpage;
%>
<a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start="+prevStart+"&perpage="+perpage) %>">&lt;Previous</a> | <a href="<%=response.encodeURL("/ITEA/admin/FacultyRecords?start="+end+"&perpage="+perpage) %>">Next&gt;</a>
<%
		}
	}
%>
</div>
</body>
</html>
<%
	/* Remove Request Attributes */
	request.removeAttribute("fb");
	request.removeAttribute("fb_no");
	request.removeAttribute("perpage");
	request.removeAttribute("start");
%>