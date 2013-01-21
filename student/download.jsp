<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" import="beans.ResourceBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ITEA - Student | Downloads</title>
<jsp:include page="/components/style.jsp" />
</head>
<body>
	<jsp:include page="/components/navbar.jsp" />
<%
	if(request.getParameter("show")==null){
%>
	<form action="DownloadList" method="post">
		<jsp:include page="/components/StudentSubList" />
		<input type="submit" value="View Downloads" />
	</form>
<%
	}else if(request.getParameter("show").equals("nofiles")){
%>
	<p>
		No files have been uploaded for this subject.
	</p>
<%
	}else if(request.getParameter("show").equals("files")){
%>
	<h4>Files Uploaded</h4>
	<p>
		To download, right-click on the file and save it ("Save Target As" or "Save Link As").
	</p>
	<table>
		<tr>
			<th>Title</th>
			<th>Description</th>
			<th>File</th>
			<th>Date</th>
		</tr>
<%
		ResourceBean rb[]=(ResourceBean[])request.getAttribute("rb");
		int noOfResources=(Integer)request.getAttribute("rb_no");
		for(int i=0;i<noOfResources;i++){
%>
		<tr>
			<td><%=rb[i].getTitle() %></td>
			<td><%=rb[i].getDescription() %></td>
			<td><a href="/ITEA/uploads/<%=rb[i].getFilename() %>"><%=rb[i].getCl_filename() %></a></td>
			<td><%=rb[i].getDate().toString() %></td>
		</tr>
<%
		}
	}
%>
	</table>
</body>
</html>