<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  pageContext.setAttribute("newLineChar", "\n");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<link type="text/css" rel="stylesheet" href="css/testResults.css" media="all" />
<script type="text/javascript" src="js/testResults.js"></script>

<div id="testResultsTab" class="leftPanel tabs">
	<ul>
		<li id="findOrAddTestResultsContent">
			<a href="findTestResultFormGenerator.html">Find/Edit Tests</a>
		</li>
		<li id="addTestResultsContent">
			<a href="addAllTestResultsFormGenerator.html">Add Test Results</a>
		</li>
		<li id="addTestResultsForWorksheet">
			<a href="worksheetForTestResultsFormGenerator.html">Edit Worksheet</a>
		</li>
	</ul>
</div>
