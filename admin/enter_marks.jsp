<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ITEA - Admin | Enter Marks</title>
<jsp:include page="/components/style.jsp" />
<script type="text/javascript" src="/ITEA/components/ajax.js"></script>
<script type="text/javascript">
function getStudents()
{
	
	var se=document.getElementById("subject");
	var cl=document.getElementById("class_id");
	if(cl.value == -1 || cl.value =="-1"  )
		return;
	if(se.value == -1 || se.value =="-1"  )
		return;
	var subject_id=se.options[se.selectedIndex].value;
	var chartStore=document.getElementById("chart");
	chartStore.innerHTML+="<tr><td colspan='3'>Loading...</td></tr>";
	ajaxRequestEscaped("<%=response.encodeURL("EnterMarks?ajax=true&list=students&subject_id=\"+se.value+\"&class_id=\"+cl.value+\"&rand="+Math.random()) %>","displayStudents");
}
function displayStudents(content){
	var chartStore=document.getElementById("chart");
	chartStore.style.display="block";
	chartStore.innerHTML=unescape(content);
}
function getSubjects(class_id)
{
	if(class_id == -1 || class_id=="-1")
		return;
	var se = document.getElementById("subjectlist");   ///
	var sel=se.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	se.innerHTML=sel+"<option value=\"-1\">Loading...</option></select>";
	ajaxRequestEscaped("<%=response.encodeURL("EnterMarks?ajax=true&list=subject&class_id=\"+class_id+\"&rand="+Math.random()) %>","displaySubjects");
}
function displaySubjects(optList)
{
	var se=document.getElementById("subjectlist"); ///
	var sel=se.innerHTML;
	if(document.all) sel=sel.substring(0,sel.indexOf("<OPTION"));	// IE hack
	else sel=sel.substring(0,sel.indexOf("<option"));
	se.innerHTML=sel+"<option value=\"-1\">--Choose One--</option>"+unescape(optList)+"</select>";
}
function validate()
{
	var se=document.getElementById("subject");
	var cl=document.getElementById("class_id");
	if(cl.value == -1 || cl.value =="-1"  )
	{	 return false ;}
	if(se.value == -1 || se.value =="-1"  )
	{	 return false ;}
	else
		return true ;

}
</script>
</head>
<body>


<jsp:include page="/components/navbar.jsp" />
Enter marks obtained in examination for students.
Please select a class.

<form method="post"  action="<%=response.encodeURL("/ITEA/admin/EnterMarks") %>" onsubmit="return validate()">

<jsp:include page="/components/ClassSem" />

<br/>
Please select a Subject: 
<div id=subjectlist>
<select name="subject" id="subject" onchange="getStudents()">
	<option value="-1">--Choose One--</option>
</select>
</div>
<table id="chart">
	<tr>
 		<th>GrNo</th>
		<th>Name</th>
		<th>Marks</th>
	</tr>
</table>

<input type="submit" value="Submit" />
</form>
</body>
</html>