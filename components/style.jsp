<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
	if(session.getAttribute("theme")!=null){
		String theme=session.getAttribute("theme").toString();
		if(theme!=null){
%>
<link rel="stylesheet" type="text/css" href="/ITEA/components/style<%=theme %>.css" />
<%
		}
	}else{
%>
<link rel="stylesheet" type="text/css" href="/ITEA/components/style0.css" />
<%
	}
%>