<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="beans.MessageBean,java.util.Calendar"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>ITEA | Messages</title>
<jsp:include page="/components/style.jsp" />
<style type="text/css">
.alert{ position:relative; float:right; padding:2em 3em;}
#messagesContainer table{ position:relative; float:left; padding:0 1em 0 1em; }
</style>
<script type="text/javascript" src="/ITEA/components/ajax.js"></script>
<script type="text/javascript" src="/ITEA/components/validation.js"></script>
<script type="text/javascript">
var msg_typeGlobal;
function confirmDeleteRecord(id){
	if(id!=null && id!=""){
		document.getElementById("deleteConfirmationDiv").style.display="";
		document.getElementById("idStore").value=id;
	}else
		document.getElementById("deleteConfirmationDiv").style.display="none";
}
function deleteRecord(){
	id=document.getElementById("idStore").value;
	ajaxRequest("<%=response.encodeURL("/ITEA/components/Messages?ajax=true&do=delete&id=\"+id+\"&rand="+Math.random()) %>","reloadPage");
}
function reloadPage(){
	location.reload(location.href);
}
function checkLength(elemId,length){
	var elem=document.getElementById(elemId);
	if(elem.value.length>length)
		elem.value=elem.value.substring(0,length);
}
function escapeContent(contentElemId){
	var contentElem=document.getElementById(contentElemId);
	content=escape(contentElem.value);
	contentElem.value=content;
}
function showMessage(elemId,msgId){
	var msgStore=new Object();
	msgStore=document.getElementById("messageContent" + elemId);
	msgStore.innerHTML="Loading...";
	ajaxRequest("<%=response.encodeURL("/ITEA/components/Messages?ajax=true&do=show&id=\"+msgId+\"&store=\"+elemId+\"&rand=\"+Math.random()+\"") %>","displayContent");
}
function displayContent(content){
	var elemId=content.substring(0,content.indexOf("##"));
	var msgStore=document.getElementById("messageContent"+elemId);
	content=content.substring(content.indexOf("##")+2,content.length);
	content=content.replace(/%0A/gi,"<br />");
	msgStore.innerHTML=unescape(content);
}
function makeOption(value,text,selected){
	var optElem=document.createElement("option");
	optElem.setAttribute("value",value);
	if(selected!="") optElem.setAttribute("selected",selected);
	optElem.appendChild(document.createTextNode(text));
	return optElem;
}
function getRecType(msg_type){
	subjectElem=document.getElementById("subject");
	msg_typeGlobal=msg_type;
	rte=document.getElementById("rec_type");
	rte.innerHTML="";
	var opt_2=makeOption("-2","--Choose the Message Type--","selected");
	if(msg_type=="-1"){
		rte.appendChild(opt_2);
		rte.disabled="disabled";
		return;
	}

	var opt_1=makeOption("-1","--Choose One--","selected");
	var opt0=makeOption("0","Individual Recipient","");
	var opt1=makeOption("1","Entire Class Students","");
	var opt2=makeOption("2","Entire Branch Students","");
	var opt3=makeOption("3","Entire Branch Faculty","");
	var opt4=makeOption("4","Entire Course Students","");
	var opt5=makeOption("5","All Students","");
	var opt6=makeOption("6","All Faculty","");
	var opt7=makeOption("7","Entire College","");
	var opt8=makeOption("8","Administrator","");

	rte.appendChild(opt_1);
	if(msg_type=="0" || msg_type=="1"){
		for(i=0;i<=8;i++) eval("rte.appendChild(opt"+i+")");
		subjectElem.value="";
		subjectElem.disabled="";
	}
	if(msg_type=="2" || msg_type=="3"){
		rte.appendChild(opt0);
		subjectElem.value="Feedback";
		subjectElem.disabled="disabled";
	}
	rte.disabled="";
}
function getRecipients(rec_type){
	container1=document.getElementById("recipientDiv1");
	container2=document.getElementById("recipientDiv2");
	container3=document.getElementById("recipientDiv3");
	container1.innerHTML="";
	container2.innerHTML="";
	container3.innerHTML="";
	if(rec_type=="0"){
		/* Display choice of student and faculty, list of either, and filtering mechanism - to allow selecting individual recipient */
		if(msg_typeGlobal=="2")	// faculty feedback
			setRecipientSOrF("Faculty");
		else if(msg_typeGlobal=="3")	// student feedback
			setRecipientSOrF("Student");
		else container1.innerHTML="Send to: <br />"
								+ "<input type='radio' name='recipient_sorf' id='recipient_s' value='Student' onclick='setRecipientSOrF(this.value)' onchange='setRecipientSOrF(this.value)' />"
								+ "<label for='recipient_s'>Student</label><br />"
								+ "<input type='radio' name='recipient_sorf' id='recipient_f' value='Faculty' onclick='setRecipientSOrF(this.value)' onchange='setRecipientSOrF(this.value)' />"
								+ "<label for='recipient_f'>Faculty</label><br />";
	}else if(rec_type=="1"){
		/* Class-wide student recipients - display class list via course list and branch list */
		container1.innerHTML="Select a course, branch and class: "
							+ "<table><tr>"
							+ "<td>Course: </td>"
							+ "<td><div id='course'><select name='course' onchange='getBranches(this.options[this.selectedIndex].value)'>"
							+ "<option value='-1'>--Choose One--</option>"
							+ "</select></div></td>"
							+ "</tr><tr>"
							+ "<td>Branch: </td>"
							+ "<td><div id='branch'><select name='branch' onchange='getClasses(this.options[this.selectedIndex].value)'>"
							+ "<option value='-1'>--Choose a Course--</option>"
							+ "</select></div></td>"
							+ "</tr><tr>"
							+ "<td>Class: </td>"
							+ "<td><div id='class'><select name='recipient'>"
							+ "<option value='-1'>--Choose a Course and Branch--</option>"
							+ "</select></div></td>"
							+ "</tr></table>"; 
		getCourses();
	}else if(rec_type=="2" || rec_type=="3"){
		/* Branch-wide recipients - display branch list via course list */
		container1.innerHTML="Select a course and branch: "
							+ "<table><tr>"
							+ "<td>Course: </td>"
							+ "<td><div id='course'><select name='course' onchange='getBranches(this.options[this.selectedIndex].value)'>"
							+ "<option value='-1'>--Choose One--</option>"
							+ "</select></div></td>"
							+ "</tr><tr>"
							+ "<td>Branch: </td>"
							+ "<td><div id='branch'><select name='recipient'>"
							+ "<option value='-1'>--Choose a Course--</option>"
							+ "</select></div></td>"
							+ "</tr></table>";
		getCourses();
	}else if(rec_type=="4"){
		/* Course-wide student recipients - display course list */
		container1.innerHTML="Select a course: "
							+ "<table><tr>"
							+ "<td><div id='course'><select name='recipient'>"
							+ "<option value='-1'>--Choose One--</option>"
							+ "</select></div></td>"
							+ "</tr></table>";
		getCourses();
	}else if(rec_type=="5"){
		/* All students */
		container1.innerHTML="<select name='recipient'>"
							+ "<option value='0'>All Students</option>"
							+ "</select>";
	}else if(rec_type=="6"){
		/* All faculty */
		container1.innerHTML="<select name='recipient'>"
							+ "<option value='0'>All Faculty</option>"
							+ "</select>";
	}else if(rec_type=="7"){
		/* College-wide recipients */
		container1.innerHTML="<select name='recipient'>"
							+ "<option value='0'>Entire College</option>"
							+ "</select>";
	}else if(rec_type=="8"){
		/* Administrator recipient */
		container1.innerHTML="<select name='recipient'>"
							+ "<option value='0'>Administrator</option>"
							+ "</select>";
	}
}
function setRecipientSOrF(value){
	container2=document.getElementById("recipientDiv2");
	container3=document.getElementById("recipientDiv3");
	container2.innerHTML="";
	container3.innerHTML="";
	if(value=="Student"){
		/* Display filtering options */
		container2.innerHTML+="Filter the list by choosing a course, branch and class: <br />"
							+ "<table><tr>"
							+ "<td>Course: </td>"
							+ "<td><div id='course'><select name='course' onchange='getBranches(this.options[this.selectedIndex].value)'>"
							+ "<option value='-1' selected='selected'>--Choose One--</option>"
							+ "</select></div></td>"
							+ "</tr><tr>"
							+ "<td>Branch: </td>"
							+ "<td><div id='branch'><select name='branch' onchange='getClasses(this.options[this.selectedIndex].value)'>"
							+ "<option value='-1' selected='selected'>--Choose a Course--</option>"
							+ "</select></div></td>"
							+ "</tr><tr>"
							+ "<td>Class: </td>"
							+ "<td><div id='class'><select name='class' onchange='getStudents(this.options[this.selectedIndex].value)'>"
							+ "<option value='-1' selected='selected'>--Choose a Course and Branch--</option>"
							+ "</select></div></td>"
							+ "</tr></table>";
		getCourses();
		/* Display recipients list */ 
		container3.innerHTML+="Select your recipient(s): <div id='recipient'><select name='recipient'>"
							+ "<option value=\"-1\">--Choose One--</option>"
							+ "</select></div><br />";
		getStudents(""); // get list of all students
	}else if(value=="Faculty"){
		container2=document.getElementById("recipientDiv2");
		container3=document.getElementById("recipientDiv3");
		container2.innerHTML="";
		container3.innerHTML="";
		/* Display filtering options */
		container2.innerHTML+="Filter the list by choosing a course and branch: <br />"
							+ "<table><tr>"
							+ "<td>Course: </td>"
							+ "<td><div id='course'><select name='course' onchange='getBranches(this.options[this.selectedIndex].value)'>"
							+ "<option value='-1' selected='selected'>--Choose One--</option>"
							+ "</select></div></td>"
							+ "</tr><tr>"
							+ "<td>Branch: </td>"
							+ "<td><div id='branch'><select name='branch' onchange='getFaculty(this.options[this.selectedIndex].value)'>"
							+ "<option value='-1' selected='selected'>--Choose a Course--</option>"
							+ "</select></div></td>"
							+ "</tr></table>";
		getCourses();
		/* Display recipients list */
		container3.innerHTML+="Select your recipient(s): <div id='recipient'><select name='recipient'>"
							+ "<option value=\"-1\">--Choose One--</option>"
							+ "</select></div><br />";
		getFaculty("");	// get list of all faculty
	}
}
function getCourses(){
	var ce=document.getElementById("course");	
	var sel=ce.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION")); // IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	ce.innerHTML=sel+"<option value=\"-1\">Loading...</option></select>";
	ajaxRequest("<%=response.encodeURL("/ITEA/components/Messages?ajax=true&list=course&rand=\"+Math.random()+\"") %>","displayCourses");
}
function displayCourses(optList){
	var ce=document.getElementById("course");
	var sel=ce.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION")); // IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	ce.innerHTML=sel+"<option value=\"-1\">--Choose One--</option>"+optList+"</select>";
}
function getBranches(course_id){
	var be=document.getElementById("branch");
	var sel=be.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION")); // IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	if(course_id=="-1"){
		be.innerHTML=sel+"<option value=\"id\">--Choose a Course--</option></select>";
		return;
	}
	be.innerHTML=sel+"<option value=\"-1\">Loading...</option></select>";
	ajaxRequest("<%=response.encodeURL("/ITEA/components/Messages?ajax=true&list=branch&course_id=\"+course_id+\"&rand=\"+Math.random()+\"") %>","displayBranches");
}
function displayBranches(optList){
	var be=document.getElementById("branch");
	var sel=be.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION")); // IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	be.innerHTML=sel+"<option value=\"-1\">--Choose One--</option>"+optList+"</select>";
}
function getClasses(branch_id){
	var le=document.getElementById("class");
	var sel=le.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION")); // IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	if(branch_id=="-1"){
		le.innerHTML=sel+"<option value=\"-1\">--Choose a Course and Branch--</option></select>";
		return;
	}
	le.innerHTML=sel+"<option value=\"-1\">Loading...</option></select>";
	ajaxRequest("<%=response.encodeURL("/ITEA/components/Messages?ajax=true&list=class&branch_id=\"+branch_id+\"&rand=\"+Math.random()+\"") %>","displayClasses");
}
function displayClasses(optList){
	var le=document.getElementById("class");
	var sel=le.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION")); // IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	le.innerHTML=sel+"<option value=\"-1\">--Choose One--</option>"+optList+"</select>";
}
function getStudents(class_id){
	var url="";
	if(class_id!=null && class_id!="" && class_id!="-1")
		url="<%=response.encodeURL("/ITEA/components/Messages?ajax=true&list=student&class_id=\"+class_id+\"&rand=\"+Math.random()+\"") %>";
	else url="<%=response.encodeURL("/ITEA/components/Messages?ajax=true&list=student&rand=\"+Math.random()+\"") %>";
	se=document.getElementById("recipient");
	var sel=se.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION")); // IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	se.innerHTML=sel+"<option value=\"-1\">Loading...</option></select>";
	ajaxRequest(url,"displayStudents");
}
function displayStudents(optList){
	se=document.getElementById("recipient");
	var sel=se.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION")); // IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	se.innerHTML=sel+"<option value=\"-1\">--Choose One--</option>"+optList+"</select>";
}
function getFaculty(branch_id){
	var url="";
	if(branch_id!=null && branch_id!="" && branch_id!="-1")
		url="<%=response.encodeURL("/ITEA/components/Messages?ajax=true&list=faculty&branch_id=\"+branch_id+\"&rand=\"+Math.random()+\"") %>";
	else url="<%=response.encodeURL("/ITEA/components/Messages?ajax=true&list=faculty&rand=\"+Math.random()+\"") %>";
	fe=document.getElementById("recipient");
	var sel=fe.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION")); // IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	fe.innerHTML=sel+"<option value=\"-1\">Loading...</option></select>";
	ajaxRequest(url,"displayFaculty");
}
function displayFaculty(optList){
	fe=document.getElementById("recipient");
	var sel=fe.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION")); // IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	fe.innerHTML=sel+"<option value=\"-1\">--Choose One--</option>"+optList+"</select>";
}
</script>
</head>
<body>
<jsp:include page="/components/navbar.jsp" />
<%
	/* View Messages only */
	if(request.getParameter("interface").equals("view")){
%>

<h3>View Messages</h3>
<p>Would you like to send a message? <a href="<%=response.encodeURL("/ITEA/components/Messages?interface=send&rand="+Math.random()) %>">Send Messages</a></p>
<br />

<%! int i=0; %>
<%
	/* Get MessageBean instances */
	MessageBean msgb[]=(MessageBean[])request.getAttribute("msgb");
	int noOfMsgs=(Integer)request.getAttribute("msgb_no");
%>
<center>
	<div id="deleteConfirmationDiv" style="display:none">
		<fieldset>
		Are you sure you want to delete the record? <br />
		<input type="hidden" id="idStore" value="" /> <br />
		<input type="button" value="Yes" onclick="deleteRecord()" /><input type="button" value="No" onclick="confirmDeleteRecord()" />
		</fieldset>
	</div>
</center>
<div id="messagesContainer">
<table width="40%">
	<tr>
		<th colspan="5">Messages</th>
	</tr>
	<tr>
		<th>S No.</th>
		<th>Sender</th>
		<th>Subject</th>
		<th>Date</th>
		<th />
	</tr>
<%
	for(i=0;i<noOfMsgs;i++){
		if(msgb[i].getMsg_type()==0){	// normal messages
%>
	<tr>
		<td />
		<td><%=msgb[i].getSender()%></td>
		<td><%=msgb[i].getSubject()%></td>
		<td><%=msgb[i].getDate().getDd()+"."+msgb[i].getDate().getMm()+"."+msgb[i].getDate().getYyyy()%></td>
		<td><input type="button" value="Delete" onclick="confirmDeleteRecord('<%=msgb[i].getId() %>');" /></td>
	</tr>
	<tr>
		<td colspan="5" id="messageContent<%=i+1%>"  class="message menu navText"><a href="javascript:showMessage('<%=i+1%>','<%=msgb[i].getId()%>')">View message</a></td>
	</tr>
<%	
		}
	}
%>
</table>
<table width="40%" class="alert">
	<tr>
		<th colspan="5">Alerts</th>
	</tr>
	<tr>
		<th>S No.</th>
		<th>Sender</th>
		<th>Subject</th>
		<th>Date</th>
		<th />
	</tr>
<%
	for(i=0;i<noOfMsgs;i++){
		if(msgb[i].getMsg_type()==1){	// alerts only
%>
	<tr>
		<td />
		<td><%=msgb[i].getSender()%></td>
		<td><%=msgb[i].getSubject()%></td>
		<td><%=msgb[i].getDate().getDd()+"."+msgb[i].getDate().getMm()+"."+msgb[i].getDate().getYyyy()%></td>
		<td><input type="button" value="Delete" onclick="confirmDeleteRecord('<%=msgb[i].getId() %>');" /></td>
	</tr>
	<tr>
		<td colspan="5" id="messageContent<%=i+1%>"  class="message menu navText"><a href="javascript:showMessage('<%=i+1%>','<%=msgb[i].getId()%>')">View message</a></td>
	</tr>
<%	
		}
	}
%>
</table>
</div>
<%
	/* Send Messages Only */
	}else if(request.getParameter("interface").equals("send")){
%>

<h3>Send Message(s)</h3>
<p>Would you like to view messages you have received? <a href="<%=response.encodeURL("/ITEA/components/Messages?interface=view&rand="+Math.random()) %>">View Messages</a></p>
<form id="sendMessageForm" name="sendMessageForm" method="post" action="<%=response.encodeURL("/ITEA/components/Messages?interface=save&rand="+Math.random()) %>" 
onsubmit="escapeContent('content'); checkLength('content',3000); return validateThese('INDIVIDUAL','subject','content')">
<input type="hidden" name="sender" value="<%=session.getAttribute("user_id")%>" />
	<table>
		<tr>
			<td>Message Type: </td>
			<td>
				<select name="msg_type" id="msg_type" onchange="getRecType(this.options[this.selectedIndex].value)">
					<option value="-1" selected="selected">--Choose One--</option>
					<option value="0">Normal Message</option>
					<option value="1">High Priority Notice (Alert)</option>
					<%
						String user_type=session.getAttribute("user_type").toString();
						if(!user_type.equals("admin") && !user_type.equals("faculty")){
					%>
					<option value="2">Faculty Feedback</option>
					<%
						}else if(!user_type.equals("admin") && !user_type.equals("student")){
					%>
					<option value="3">Student Feedback</option>
					<%
						}
					%>
				</select>
			</td>
		</tr>
		<tr>
			<td>Category of Receivers: </td>
			<td>
				<select name="rec_type" disabled="disabled" id="rec_type" onchange="getRecipients(this.options[this.selectedIndex].value)">
					<option value="-1" selected="selected">--Choose the Message Type--</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>Add Recipients: </td>
			<td id="recipientDiv">
				<table width="100%">
					<tr>
						<td id="recipientDiv1"></td>
						<td id="recipientDiv2"></td>
					</tr>
					<tr>
						<td id="recipientDiv3" colspan="2"></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>Subject (100 characters maximum): </td>
			<td><input type="text" name="subject" size="40" id="subject" maxlength="100" /></td>
		</tr>
		<tr>
			<td></td>
			<td class="error" id="subject_error">Cannot be empty.</td>
		</tr>
		<tr>
			<td>Message (1000 characters maximum): </td>
			<td>
				<textarea name="content" cols="40" rows="10" id="content" onchange="checkLength(this.id,1000)" onkeypress="checkLength(this.id,1000)" ></textarea>
			</td>
		</tr>
		<tr>
			<td></td>
			<td class="error" id="content_error">Cannot by empty.</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="Send Message" /></td>
		</tr>
	</table>
</form>
<%
	/* Message Saved */
	}else if(request.getParameter("interface").equals("save")){
%>
<h3>Send Message(s)</h3>
<p>Your message has been sent.</p>
<p>Would you like to send another message?</p>
<a href="<%=response.encodeURL("/ITEA/components/Messages?interface=send&rand="+Math.random()) %>">Send Messages</a>
<p>Would you like to view messages you have received?</p>
<a href="<%=response.encodeURL("/ITEA/components/Messages?interface=view&rand="+Math.random()) %>">View Messages</a>

<%
	}
%>
</body>
</html>
<%
	/* Remove Request Attributes */
	request.removeAttribute("msgb");
	request.removeAttribute("msgb_no");
	request.removeAttribute("interface");
%>