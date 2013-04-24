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
			<a href="findTestResultFormGenerator.html">Find Test Results</a>
		</li>

		<c:if test="${model.labsetup['bloodTypingElisa'] == 'true' }">
			<li id="bloodTypingTestResults">
				<a href="bloodTypingWorksheetGenerator.html">Blood Typing (ELISA)</a>
			</li>
		</c:if>
		<c:if test="${model.labsetup['ttiElisa'] == 'true' }">
			<li id="ttiTestResultsWells">
				<a href="ttiWellsWorksheetFormGenerator.html">TTI results (ELISA)</a>
			</li>
		</c:if>

		<!-- li id="ttiResults">
			<a href="ttiFormGenerator.html">TTI Results</a>
		</li-->

		<c:if test="${model.labsetup['useWorksheets'] == 'true' }">
			<li id="addTestResultsForWorksheet">
				<a href="worksheetForTestResultsFormGenerator.html">Worksheets</a>
			</li>
		</c:if>

	</ul>

</div>
