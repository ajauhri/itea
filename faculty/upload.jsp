<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>ITEA - Faculty | Upload</title>
<jsp:include page="/components/style.jsp" />
<script type="text/javascript" src="/ITEA/components/ajax.js"></script>
<script type="text/javascript">
function getSubjects(){
	var se=document.getElementById("subjectContainer");
	var sel=se.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	se.innerHTML=sel+"<option value=\"-1\">Loading...</option></select>";
	ajaxRequest("<%=response.encodeURL("/ITEA/faculty/Attendance?ajax=true&list=subject&rand="+Math.random()) %>","displaySubjects");
}
function displaySubjects(optList){
	var se=document.getElementById("subjectContainer");
	var sel=se.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	se.innerHTML=sel+"<option value=\"-1\">--Choose One--</option>"+optList+"</select>";
}
</script>
</head>
<%
	if(request.getParameter("show")!=null && request.getParameter("show").equals("form")){
%>
<body onload="getSubjects()">
	<jsp:include page="/components/navbar.jsp" />
		<p>
			Size Limit: 200 MB.
		</p>
		<br />
		<form method="post" action="Upload" enctype="multipart/form-data">
			<table>
				<tr>
					<td>Select a subject: </td>
					<td>
						<div id="subjectContainer">
							<select name="subject_id" id="subject" onchange="getStudents()">
								<option value="-1">Choose One</option>
							</select>
						</div>
					</td>
				</tr>
				<tr>	
					<td>Title: </td>
					<td><input type="text" name="title" size="30" /></td>
				</tr>
				<tr>		
					<td>Description: </td>
					<td><input type="text" name="description" size="30" /></td>
				</tr>
				<tr>
					<td colspan=2><input type="file" name="uploadfile" size="30" /></td>
				</tr>
				<tr>
					<td colspan=2>
						<center>
							<input  type="submit" name="Upload" value="Upload" />
							<input type="reset" name="Reset" value="Reset" />
						</center>
					</td>
				</tr>
			</table>
		</form>
		<% 
			if(request.getParameter("upload")!=null && request.getParameter("upload").equals("done")){
					%>
						<p style="background-color:#ccf;">The file(s) was uploaded successfully.</p>
					<%
			}
		%>
</body>
<%
	}else if(request.getParameter("show")!=null && request.getParameter("show").equals("conf")){
%>
<body>
	<jsp:include page="/components/navbar.jsp" />
	<p>
		The file has been uploaded.
		File name: <%=request.getParameter("filename") %>
	</p>
</body>
<%
	}
%>
</html>