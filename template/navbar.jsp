<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="navbar">
	<c:set var="user_type" value="${sessionScope.user_type}" />
	<c:if test="${!empty user_type}">
		<c:set var="student" value="student" />
		<c:set var="faculty" value="faculty" />
		<c:set var="hod" value="hod" />
		<c:set var="dean" value="dean" />
		<c:set var="data_manager" value="data_manager" />
		<c:choose>
			<c:when test="${user_type==student}">
				Home | Student | Evict Faculty | Dangerous Weapons and Sharp Pencils | <a href=<%=response.encodeURL("/pkpk/Messages?interface=view") %>>View Messages</a>
			</c:when>
			<c:when test="${user_type==faculty}">
				Home | Faculty | Rape Students | Torture Instruments
			</c:when>
			<c:when test="${user_type==hod}">
				Home | HOD | Sit | Sulk | Sit | Sulk some more
			</c:when>
			<c:when test="${user_type==dean}">
				Home | Dean | Avoid Management | Devoid Management
			</c:when>
			<c:when test="${user_type==data_manager}">
				Home | Data Manager | Corrupt Records | Reinitialize Data | Delete Dean
			</c:when>
		</c:choose>
	</c:if>
</div>