<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="header">
	<h1>ITEA</h1>
	<h3>IT-Enabled Academia</h3>
	<div id="userInfo">
		<c:set var="user_type" value="${sessionScope.user_type}" />
		<c:set var="student" value="student" />
		<c:set var="faculty" value="faculty" />
		<c:set var="hod" value="hod" />
		<c:set var="dean" value="dean" />
		<c:set var="data_manager" value="data_manager" />
		<c:if test="${!empty user_type}">
			<c:choose>
				<c:when test="${user_type==student}">
					<c:out value="Student Home" />
				</c:when>
				<c:when test="${user_type==student}">
					<c:out value="Faculty Home" />
				</c:when>
				<c:when test="${user_type==student}">
					<c:out value="H.O.D. Home" />
				</c:when>
				<c:when test="${user_type==student}">
					<c:out value="Dean Home" />
				</c:when>
				<c:when test="${user_type==student}">
					<c:out value="Data Manager Home" />
				</c:when>
			</c:choose>
		</c:if>
	</div>
</div>