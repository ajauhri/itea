<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	String user_type="";
	String user_type_formatted="";
	if(session.getAttribute("user_type")!=null){
		user_type=session.getAttribute("user_type").toString();
		if(user_type.equals("student")) user_type_formatted="Student";
		else if(user_type.equals("faculty")) user_type_formatted="Faculty";
		else if(user_type.equals("hod")) user_type_formatted="H.O.D.";
		else if(user_type.equals("dean")) user_type_formatted="Dean";
		else if(user_type.equals("admin")) user_type_formatted="Data Manager (Admin)";
	}else{
		user_type_formatted="";
		user_type="none";
	}
%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr class="header">
		<td width="50" nowrap="nowrap">&nbsp;</td>
		<td height="70" colspan="5" nowrap="nowrap" class="logo style1">I.T.E.A.|<span
			class="tagline"><%=user_type_formatted %> Home</span></td>
	</tr>
	<tr class="menu">
		<td width="50" nowrap="nowrap">&nbsp;</td>
		<td colspan="5" height="24">
		<table border="0" cellpadding="0" cellspacing="0" id="navigation">
<%
		if(user_type.equals("student")){
%>
						<tr>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/components/Home") %>">Home</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/components/Messages") %>">Messages/Feedback</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/student/DownloadList") %>">Downloads</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/student/TimeTableView") %>">Time Table</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/student/Transcripts") %>">Transcripts</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/components/ManageAccount") %>">Manage Account</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/components/Logout") %>">Logout</a></td>
						</tr>
<%
		}else if(user_type.equals("faculty")){
%>
						<tr>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/components/Home") %>">Home</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/components/Messages") %>">Messages</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/faculty/ManageLectures") %>">Manage Lectures</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/faculty/TimeTablePersonal") %>">Time Table</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/faculty/Upload") %>">Upload Files</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/components/ManageAccount") %>">Manage Account</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/components/Logout") %>">Logout</a></td>
						</tr>
<%
		}else if(user_type.equals("hod")){
%>
						<tr>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/components/Home") %>">Home</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/components/Messages") %>">Message</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/Reports/Reports") %>">Reports</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/hod/TimeTableAdmin") %>">Time Table</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/components/ManageAccount") %>">Manage Account</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/components/Logout") %>">Logout</a></td>
						</tr>
<%
		}else if(user_type.equals("dean")){
%>
						<tr>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/components/Home") %>">Home</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/components/Messages") %>">Message</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/Reports/Reports") %>">Reports</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/dean/ResetCourse") %>">Declare End of Session</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/components/ManageAccount") %>">Manage Account</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/components/Logout") %>">Logout</a></td>
						</tr>
<%
		}else if(user_type.equals("admin")){
%>
						<tr>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/components/Home") %>">Home</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/components/Messages") %>">Messages</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/admin/ManageDatabase") %>">Manage Database</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/components/ManageAccount") %>">Manage Account</a></td>
							<td class="navText" align="center" nowrap="nowrap"><a
								href="<%=response.encodeURL("/ITEA/components/Logout") %>">Logout</a></td>
						</tr>
<%
		}
%>
		</table>
		</td>
	</tr>
</table>
<br />
<br />