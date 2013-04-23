<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  pageContext.setAttribute("newLineChar", "\n");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<link type="text/css" rel="stylesheet" href="css/usage.css" />
<script type="text/javascript" src="js/usage.js"></script>

<div id="usageTab" class="leftPanel tabs">
	<ul>
		<!-- li id="findOrAddUsageContent"><a
			href="findUsageFormGenerator.html">Find Usage</a></li-->
		<li id="addUsageByRequestContent">
			<a href="addUsageByRequestFormGenerator.html">Record Usage <br /> by Request</a>
		<!-- li id="addUsageContent">
			<a href="addUsageFormGenerator.html">Add Usage</a>
		</li-->
	</ul>
</div>
