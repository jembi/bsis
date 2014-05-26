<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  pageContext.setAttribute("newLineChar", "\n");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<link type="text/css" rel="stylesheet" href="css/reports.css" media="all" />
<script type="text/javascript" src="js/reports.js"></script>
<script type="text/javascript" src="plugins/highcharts/js/highcharts.js"></script>
<script type="text/javascript"
  src="plugins/highcharts/js/modules/exporting.js"></script>

<sec:authorize access="hasRole(T(utils.PermissionConstants).VIEW_REPORTING_INFORMATION)">
<div id="reportsTab" class="leftPanel tabs">
  <ul>
  <sec:authorize access="hasRole(T(utils.PermissionConstants).VIEW_INVENTORY_INFORMATION)">
    <li id="inventoryReport">
      <a href="inventoryReportFormGenerator.html">Product Inventory</a>
    </li>
    </sec:authorize>
    <sec:authorize access="hasRole(T(utils.PermissionConstants).DONATIONS_REPORTING)">
    <li id="collectionsReport">
      <a href="collectionsReportFormGenerator.html">Donations</a>
    </li>
    </sec:authorize>
    <sec:authorize access="hasRole(T(utils.PermissionConstants).TTI_REPORTING)">
    <li id="ttiReport">
      <a href="ttiReportFormGenerator.html">TTI Reports</a>
    </li>
    </sec:authorize>
    <sec:authorize access="hasRole(T(utils.PermissionConstants).REQUESTS_REPORTING)">
    <li id="requestsReport">
      <a href="requestsReportFormGenerator.html">Requests</a>
    </li>
    </sec:authorize>
    <sec:authorize access="hasRole(T(utils.PermissionConstants).COMPONENTS_ISSUED_REPORTING)">
    <li id="issuedProductsReport">
      <a href="issuedProductsReportFormGenerator.html">Issued products</a>
    </li>
    </sec:authorize>
    <sec:authorize access="hasRole(T(utils.PermissionConstants).COMPONENTS_DISCARDED_REPORTING)">
    <li id="discardedProductsReport">
      <a href="discardedProductsReportFormGenerator.html">Discarded products</a>
    </li>
    </sec:authorize>
  </ul>
</div>
</sec:authorize>