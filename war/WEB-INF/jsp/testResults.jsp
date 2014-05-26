<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  pageContext.setAttribute("newLineChar", "\n");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<link type="text/css" rel="stylesheet" href="css/testResults.css" media="all" />
<script type="text/javascript" src="js/testResults.js"></script>

<div id="testResultsTab" class="leftPanel tabs">
  <ul>
    <sec:authorize access="hasRole(T(utils.PermissionConstants).VIEW_TEST_OUTCOME)">
    <li id="findOrAddTestResultsContent">
      <a href="findTestResultFormGenerator.html">Find Test Results</a>
    </li>
    </sec:authorize>
    <c:if test="${labsetup['bloodTypingElisa'] == 'true' }">
    <sec:authorize access="hasRole(T(utils.PermissionConstants).ADD_BLOOD_TYPING_OUTCOME)">
      <li id="bloodTypingTestResults">
        <a href="bloodTypingWorksheetGenerator.html">Blood Typing (ELISA)</a>
      </li>
    </sec:authorize>
    </c:if>
    <c:if test="${labsetup['ttiElisa'] == 'true' }">
    <sec:authorize access="hasRole(T(utils.PermissionConstants).ADD_TTI_OUTCOME)">
      <li id="ttiTestResultsWells">
        <a href="ttiWellsWorksheetFormGenerator.html">TTI Results (ELISA)</a>
      </li>
    </sec:authorize>
    </c:if>
    <c:if test="${labsetup['ttiUploadResult'] == 'true' }">
    <sec:authorize access="hasRole(T(utils.PermissionConstants).ADD_TTI_OUTCOME)">
      <li id="UploadTTIResults">
        <a href="uploadTTIResultsFormGenerator.html">Upload TTI Results</a>
      </li>
    </sec:authorize>
    </c:if>
    <!-- li id="ttiResults">
      <a href="ttiFormGenerator.html">TTI Results</a>
    </li-->
    <c:if test="${labsetup['useWorksheets'] == 'true' }">
    <sec:authorize access="hasRole(T(utils.PermissionConstants).ADD_TEST_OUTCOME)">
      <li id="addTestResultsForWorksheet">
        <a href="worksheetForTestResultsFormGenerator.html">Worksheets</a>
      </li>
    </sec:authorize>
    </c:if>
  </ul>
</div>