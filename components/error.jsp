<!-- Component: Error Page
	Global error page for the site.
	Pass parameter "type" to display specific error messages.
	Values for "type": generic, unauthorized, server, database, form
	Call examples:
		request.getRequestDispatcher(response.encodeURL("error.jsp?type=server")).forward(request,response);
		
	Provides the following error reports:
	generic: Simple error report.
	unauthorized: Error report for access without logging in.
	server: Error on the server, some exception, for example.
	database: DBMS error, possibly in SQL queries.
	form: Form values entered produce an error.
	duplicate: Generated Grno is duplicate	
	
-->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ITEA | Error</title>
<jsp:include page="/components/style.jsp" />
</head>
<body>
<jsp:include page="/components/navbar.jsp" />
<%
	if(request.getParameter("type")==null || request.getParameter("type").equals("generic")){
%>
<!-- Generic Error -->
<h2>Error</h2>
<p>An error occurred during the operation. If you were filling forms, try going back and entering different values.</p>
<p>You may also simply try again.</p>
<%
	}else if(request.getParameter("type").equals("unauthorized")){
%>
<!-- Unauthorized Access Error -->
<h2>Unauthorized Access</h2>
You need to be logged in to access this section of the website.
<a href="/ITEA/components/Home">Login</a>
<%
	}else if(request.getParameter("type").equals("server")){
%>
<!-- Server Error -->
<h2>Server Error</h2>
An error seems to have occurred on the server. 
Please try again later, or contact the administrator about the problem.
<a href="<%=response.encodeURL("/ITEA/components/Messages?interface=send") %>">Send Message to Administrator</a>
<%
	}else if(request.getParameter("type").equals("form")){
%>
<!-- Form Error -->
<h2>Error</h2>
The data supplied is inconsistent or invalid. Please make sure all data entered is valid.
<%
	}else if(request.getParameter("type").equals("duplicate")){
%>
<!-- Gr No Error -->
<h2>Duplicate Entry</h2>
The student/faculty entered is duplicate. If you want to modify details, edit them directly via the 'View' interface.
<%
	}else if(request.getParameter("type").equals("database")){
%>
<!-- Database Error -->
<h2>Database Error</h2>
An error seems to have occurred in the database. Ensure that you are entering correct values where required.
Please try again later, or contact the administrator about the problem.
<a href="<%=response.encodeURL("/ITEA/components/Messages?interface=send") %>">Send Message to Administrator</a>
<%
	}
%>
<br /><br />
<a href="javascript:void(0)" onclick="history.back()" />&lt; Back</a> | <a href="<%=response.encodeURL("/ITEA/components/Home") %>">Home &gt;</a>
</body>
</html>