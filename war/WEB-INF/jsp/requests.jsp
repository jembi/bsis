<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  pageContext.setAttribute("newLineChar", "\n");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<link type="text/css" rel="stylesheet" href="css/requests.css" />
<script type="text/javascript" src="js/requests.js"></script>

<div id="requestsTab" class="leftPanel">
	<ul>
		<li id="addRequestsContent"><a
			href="editRequestFormGenerator.html">Add Requests</a></li>
		<li id="findOrAddRequestsContent"><a
			href="findRequestFormGenerator.html">Find Requests</a></li>
	</ul>
</div>
