<%
/* preferences to be displayed on selection of teacher beneath the table,
continuous visibility of all subk select course, branch, class retrieve
all subjects specific to that class from the subject table retrieve all
infrastructures from table infrastructure subject table has the - gr
number of the teacher - the infrastructure id in the venue a table
having 8 slots for each day choose a subject, teacher for every
selection check for infrasructure for that slot check for availability
for the teacher for that slot response text may have infra, teacher,
subject separated by a special character */
%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ITEA - HOD | Time-Table</title>
<jsp:include page="/components/style.jsp" />
<style type="text/css">
.timetable td, .timetable th, .timetable select, .timetable select option{
	font-size:11px;
}
.timetable th{
	margin:0px;
	padding:5px;
/*	background-color:black;
	color:white; */
}
.timetable td{
	margin:0px;
	padding:5px;
	background-color:white;
	border-right:1px solid;
	border-bottom:1px solid;
}
</style>
<script type="text/javascript" src="/ITEA/components/ajax.js"></script>
<script type="text/javascript">
	function showClass(branch_id) {
		document.getElementById("classdiv").style.display = 'block'
		ce = document.getElementById("classdiv")
		sel = ce.innerHTML
		if (document.all)
			sel = sel.substring(0, sel.indexOf("<OPTION"));
		else
			sel = sel.substring(0, sel.indexOf("<option"));
		ce.innerHTML = sel
				+ "<option value=\"-1\">Loading...</option></select>";
		ajaxRequestEscaped("<%=response.encodeURL("/ITEA/hod/TimeTableAdmin?q=class&branch_id=\" + branch_id + \"&rand="
				+ Math.random()) %>", "displayClasses")

	}

	function displayClasses(optList) {
		ce = document.getElementById("classdiv")
		sel = ce.innerHTML
		if (document.all)
			sel = sel.substring(0, sel.indexOf("<OPTION"));
		else
			sel = sel.substring(0, sel.indexOf("<option"));
		ce.innerHTML = sel + "<option value=\"-1\">--Choose One--</option>"
				+ unescape(optList) + "</select>";

	}

	function showOpts() {
		document.getElementById("tablediv").style.display = 'block'
		class_id = document.getElementById("classes")
		class_id = class_id.options[class_id.selectedIndex].value

		ajaxRequest("<%=response.encodeURL("/ITEA/hod/TimeTableAdmin?q=opts&class_id=\" + class_id + \"&rand="
				+ Math.random()) %>", "fillIn")
	}

	function fillIn(optList) {
		for (i = 0; i < 54; i++) {
			optlist1 = optList.substring(0, optList.indexOf("#"));
			optlist2 = optList.substring(optlist1.length, optList
					.lastIndexOf("#"));
			optlist3 = optList.substring(optlist1.length + optlist2.length,
					optList.length);
			var ce = document.getElementById(i);
			var sels=ce.innerHTML;
			var sel1 = null;
			var sel2 = null;
			var sel3 = null;
			
			if (document.all) sel1 = sels.substring(0, sels.indexOf("<OPTION"));
			else sel1 = sels.substring(0, sels.indexOf("<option"));
			
			if (document.all) sel2 = sels.substring(sels.indexOf("</SELECT>")+9,sels.length);
			else sel2 = sels.substring(sels.indexOf("</select>")+9,sels.length);
			if (document.all) sel3 = sel2.substring(sel2.indexOf("</SELECT>")+9,sel2.length);
			else sel3 = sel2.substring(sel2.indexOf("</select>")+9,sel2.length);
			
			if (document.all) sel2 = sel2.substring(0, sel.indexOf("<OPTION"));
			else sel2 = sel2.substring(0, sel2.indexOf("<option"));
			
			if (document.all) sel3 = sel3.substring(0, sel.indexOf("<OPTION"));
			else sel3 = sel3.substring(0, sel3.indexOf("<option"));
			
/*			if (document.all) {
				sel = sel.substring(0, sel.indexOf("<OPTION"));
				sel2 = sel2.substring(sel2.indexOf("<BR>"), sel2.length);
				sel2 = sel2.substring(sel2.indexOf("<BR>"), sel2
						.indexOf("<OPTION"));
				sel3 = sel3.substring(sel3.lastIndexOf("<BR>"), sel3
						.lastIndexOf("<OPTION"));
			} else {
				sel = sel.substring(0, sel.indexOf("<option"));
				sel2 = sel2.substring(sel2.indexOf("<br>"), sel2.length);
				sel2 = sel2.substring(sel2.indexOf("<br>"), sel2
						.indexOf("<option"));
				sel3 = sel3.substring(sel3.lastIndexOf("<br>"), sel3
						.lastIndexOf("<option"));
			}
*/			ce.innerHTML = sel1
					+ "<option value=\"-1\" selected=\"selected\">--Select a Subject--</option>"
					+ optlist1
					+ "</select>" 
					+ sel2
					+ "<option value=\"-1\" selected=\"selected\">--Select a Teacher--</option>"
					+ optlist2
					+ "</select>" 
					+ sel3
					+ "<option value=\"-1\" selected=\"selected\">--Select a Room/Lab--</option>" 
					+ optlist3 
					+ "</select>";
		}

	}
	function getPreferences(elem){
		var gr_no=elem.options[elem.selectedIndex].value;
		var prefStore=document.getElementById("lecturePreferences");
		prefStore.innerHTML="Loading...";
		ajaxRequestEscaped("<%=response.encodeURL("/ITEA/hod/TimeTableAdmin?q=preferences&gr_no=\"+gr_no+\"&rand="+Math.random()) %>","displayPreferences");
	}
	function displayPreferences(response){
		var prefStore=document.getElementById("lecturePreferences");
		prefStore.innerHTML=unescape(response);
	}
	function activate(act) {
		var id = act.id;

		if (document.getElementById(id).checked == true) {
			if (id.charAt(1) != "t") {
				document.getElementById("s" + id.substring(1, 3)).disabled = false;
				document.getElementById("t" + id.substring(1, 3)).disabled = false;
				document.getElementById("i" + id.substring(1, 3)).disabled = false;

			} else {
				document.getElementById("s" + id.substring(1, 4)).disabled = false;
				document.getElementById("t" + id.substring(1, 4)).disabled = false;
				document.getElementById("i" + id.substring(1, 4)).disabled = false;

			}
		} else {
			if (id.charAt(1) != "t") {
				document.getElementById("s" + id.substring(1, 3)).disabled = true;
				document.getElementById("t" + id.substring(1, 3)).disabled = true;
				document.getElementById("i" + id.substring(1, 3)).disabled = true;

			} else {
				document.getElementById("s" + id.substring(1, 4)).disabled = true;
				document.getElementById("t" + id.substring(1, 4)).disabled = true;
				document.getElementById("i" + id.substring(1, 4)).disabled = true;

			}
		}

	}
	function check(op) {
		var subject_id = document.getElementById("s" + op.id.substring(1, op.id.length)).value;
		var teacher_id = document.getElementById("t" + op.id.substring(1, op.id.length)).value;
		var infra_id = document.getElementById("i" + op.id.substring(1, op.id.length)).value;
		var d_id="";
		var slot="";
		if (op.id.charAt(1) == 't') {
			d_id = op.id.substring(1, 3);
			slot = op.id.charAt(3);
		} else {
			d_id = op.id.substring(1, 2);
			slot = op.id.charAt(2);
		}
		ajaxRequestEscaped("<%=response.encodeURL("/ITEA/hod/TimeTableAdmin?q=check&subject_id=\" + subject_id + \"&teacher_id=\" + teacher_id + \"&infra_id=\" + infra_id + \"&d_id=\" + d_id + \"&slot=\" + slot + \"&opId=\"+op.id+\"&rand=" + Math.random()) %>",
				"err_disp")

	}

	function err_disp(content) {
		content=unescape(content);
		var id=content.substring(0,content.indexOf('#'))
		var con=content.substring(id.length+1,content.length)
		document.getElementById("err_c" + id.substring(1,id.length)).style.display = 'block';
		document.getElementById("err_c" + id.substring(1,id.length)).innerHTML = con;
		if (content == "")
			document.getElementById("err_c" + id.substring(1,id.length)).style.display = 'none';
		}
	</script>
</head>
<%
	if(request.getParameter("show")!=null && request.getParameter("show").equals("error")){
%>
<body>
	<jsp:include page="/components/navbar.jsp" />
	Unable to process your request.
	The timetable generation failed. Please try again.
<%
	}else if(request.getParameter("show")!=null && request.getParameter("show").equals("success")){
%>
<body>
	<jsp:include page="/components/navbar.jsp" />
	Update Successful.
	The time table has been created successfully. Students and faculty will be able to view their time tables. 
<%		
	}else{
%>
<body onload="showClass('<%=session.getAttribute("branch_id").toString() %>')">
<jsp:include page="/components/navbar.jsp" />

<div id="tablediv">
<form method="post" name="form1" action="TimeTableAdmin">

<div id="options">
<table>
	<tr>
		<td>Select a Class</td>
		<td id="classdiv" style="display: none">
			<select name="classes" id="classes" size="1" onchange="showOpts()">
				<option value="-1">--Choose One--</option>
			</select>
		</td>
	</tr>
</table>
<table>
	<tr>
		<td>Chosen Faculty's Lecture Time Preferences: </td>
		<td id="lecturePreferences"></td>
	</tr>
</table>
</div>


<input type="submit" value="Finalize Timetable">
<table class="timetable" cellspacing="0" cellpadding="0">
	<caption><strong>Time Table</strong></caption>
	<tr>
		<th class="menu navText" />
		<th class="menu navText">Monday</th>
		<th class="menu navText">Tuesday</th>
		<th class="menu navText">Wednesday</th>
		<th class="menu navText">Thursday</th>
		<th class="menu navText">Friday</th>
		<th class="menu navText">Saturday</th>
	</tr>

<%
	String[] slotTime={"8:00am to 9:00am","9:00am to 10:00am","10:00am to 11:00am","11:00am to 12:00pm","12:00pm to 1:00pm","1:00pm to 2:00pm","2:00pm to 3:00pm","3:00pm to 4:00pm","4:00pm to 5:00pm"};
	for (int i = 1,id = 0; i <= 9; i++) {
%>
	<tr>
		<th class="menu navText">
			Slot <%=i %><br />
			<%=slotTime[i-1] %>
		</th>
		<td id="m<%=i %>">
			<input type="checkbox" id="cm<%=i %>" name="cm<%=i %>" onclick="activate(this)" />
			<br />
			<div id="err_cm<%=i %>" style="display:none"></div>
			<div id="<%=id++ %>">
				<select id="sm<%=i %>" name="sm<%=i %>" onchange="check(this)" disabled="disabled">
					<option value="-1">--Select a Subject--</option>
				</select>
				<br />
				<select id="tm<%=i %>" name="tm<%=i %>" onchange="check(this); getPreferences(this)" disabled="disabled">
					<option value="-1">--Select a Teacher--</option>
				</select>
				<br />
				<select id="im<%=i %>" name="im<%=i %>" onchange="check(this)" disabled="disabled">
					<option value="-1">--Select a Room/Lab--</option>
				</select>
			</div>
		</td>
		<td id="tu<%=i %>">
			<input type="checkbox" id="ctu<%=i %>" name="ctu<%=i %>" onclick="activate(this)" />
			<br />
			<div id="err_ctu<%=i %>" style="display:none"></div>
			<div id="<%=id++ %>">
				<select id="stu<%=i %>" name="stu<%=i %>" onchange="check(this)" disabled="disabled">
					<option value="-1">--Select a Subject--</option>
				</select>
				<br />
				<select id="ttu<%=i %>" name="ttu<%=i %>" onchange="check(this); getPreferences(this)" disabled="disabled">
					<option value="-1">--Select a Teacher--</option>
				</select>
				<br />
				<select id="itu<%=i %>" name="itu<%=i %>" onchange="check(this)" disabled="disabled">
					<option value="-1">--Select a Room/Lab--</option>
				</select>
			</div>
		</td>
		<td id="w<%=i %>">
			<input type="checkbox" id="cw<%=i %>" name="cw<%=i %>" onclick="activate(this)" />
			<br />
			<div id="err_cw<%=i %>" style="display:none"></div>
			<div id="<%=id++ %>">
				<select id="sw<%=i %>" name="sw<%=i %>" onchange="check(this)" disabled="disabled">
					<option value="-1">--Select a Subject--</option>
				</select>
				<br />
				<select id="tw<%=i %>" name="tw<%=i %>" onchange="check(this); getPreferences(this)" disabled="disabled">
					<option value="-1">--Select a Teacher--</option>
				</select>
				<br />
				<select id="iw<%=i %>" name="iw<%=i %>" onchange="check(this)" disabled="disabled">
					<option value="-1">--Select a Room/Lab--</option>
				</select>
			</div>
		</td>
		<td id="th<%=i %>">
			<input type="checkbox" id="cth<%=i %>" name="cth<%=i %>" onclick="activate(this)" />
			<br />
			<div id="err_cth<%=i %>" style="display:none"></div>
			<div id="<%=id++ %>">
				<select id="sth<%=i %>" name="sth<%=i %>" onchange="check(this)" disabled="disabled">
					<option value="-1">--Select a Subject--</option>
				</select>
				<br />
				<select id="tth<%=i %>" name="tth<%=i %>" onchange="check(this); getPreferences(this)" disabled="disabled">
					<option value="-1">--Select a Teacher--</option>
				</select>
				<br />
				<select id="ith<%=i %>" name="ith<%=i %>" onchange="check(this)" disabled="disabled">
					<option value="-1">--Select a Room/Lab--</option>
				</select>
			</div>
		</td>
		<td id="f<%=i %>">
			<input type="checkbox" id="cf<%=i %>" name="cf<%=i %>" onclick="activate(this)" />
			<br />
			<div id="err_cf<%=i %>" style="display:none"></div>
			<div id="<%=id++ %>">
				<select id="sf<%=i %>" name="sf<%=i %>" onchange="check(this)" disabled="disabled">
					<option value="-1">--Select a Subject--</option>
				</select>
				<br />
				<select id="tf<%=i %>" name="tf<%=i %>" onchange="check(this); getPreferences(this)" disabled="disabled">
					<option value="-1">--Select a Teacher--</option>
				</select>
				<br />
				<select id="if<%=i %>" name="if<%=i %>" onchange="check(this)" disabled="disabled">
					<option value="-1">--Select a Room/Lab--</option>
				</select>
			</div>
		</td>
		<td id="s<%=i %>">
			<input type="checkbox" id="cs<%=i %>" name="cs<%=i %>" onclick="activate(this)" />
			<br />
			<div id="err_cs<%=i %>" style="display:none"></div>
			<div id="<%=id++ %>">
				<select id="ss<%=i %>" name="ss<%=i %>" onchange="check(this); getPreferences(this)" disabled="disabled">
					<option value="-1">--Select a Subject--</option>
				</select>
				<br />
				<select id="ts<%=i %>" name="ts<%=i %>" onchange="check(this)" disabled="disabled">
					<option value="-1">--Select a Teacher--</option>
				</select>
				<br />
				<select id="is<%=i %>" name="is<%=i %>" onchange="check(this)" disabled="disabled">
					<option value="-1">--Select a Room/Lab--</option>
				</select>
			</div>
		</td>
	</tr>
<%
	}
%>
</table><br />
</form>
</div>
<%
	}
%>
</body>
</html>