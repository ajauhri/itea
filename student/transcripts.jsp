<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="beans.TranscriptBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Transcripts</title>
<jsp:include page="/components/style.jsp" />
<script type="text/javascript" src="/ITEA/components/ajax.js"></script>
<script type="text/javascript">
function getClasses(){
	var le=document.getElementById("class");
	var sel=le.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION")); // IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	le.innerHTML=sel+"<option value=\"-1\">Loading...</option></select>";
	ajaxRequest("<%=response.encodeURL("/ITEA/student/Transcripts?ajax=true&list=class&rand="+Math.random()) %>","displayClasses");
}
function displayClasses(optList){
	var le=document.getElementById("class");
	var sel=le.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION")); // IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	le.innerHTML=sel+"<option value=\"-1\">--Choose One--</option>"+optList+"</select>";
}
</script>
</head>
<body onload="getClasses()">
<jsp:include page="/components/navbar.jsp" />
<form method="post" action="<%=response.encodeURL("/ITEA/student/Transcripts?do=get") %>">
	<table>
		<tr>
			<td>Academic Session</td>
			<td id="class">
				<select name="class_id">
					<option value="-1">--Choose One--</option>
				</select>
			</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="Get Transcript" /></td>
		</tr>
	</table>
</form>
<%
	if(request.getParameter("do")!=null && request.getParameter("do").equals("show")){
%>
	<div id="transcript">
		<h2>Transcript for Academic Session: <%=(Integer)request.getAttribute("session") %></h2>
		<table>
			<tr>
				<th colspan="2">Student Details</th>
			</tr>
			<tr>
				<td>GR No: </td>
				<td><%=session.getAttribute("user_id").toString() %></td>
			</tr>
			<tr>
				<td colspan="2"></td>
			</tr>
			<tr>
				<th>Subject</th>
				<th>Marks</th>
			</tr>
<%
		int noOfTranscripts=(Integer)request.getAttribute("tb_no");
		TranscriptBean tb[]=(TranscriptBean[])request.getAttribute("tb");
		
		for(int i=0;i<noOfTranscripts;i++){
%>
			<tr>
				<td><%=tb[i].getSubject() %></td>
				<td><%=tb[i].getMarks() %>%</td>
			</tr>		
<%
		}
%>
		</table>
	</div>
<%
	}
%>
</body>
</html>