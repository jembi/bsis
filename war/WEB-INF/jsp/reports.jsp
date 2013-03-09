<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
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

<div id="reportsTab" class="leftPanel tabs">
	<ul>
		<li id="inventoryReport">
			<a href="inventoryReportFormGenerator.html">Product Inventory</a>
		</li>
		<li id="collectionsReport">
			<a href="collectionsReportFormGenerator.html">Collections</a>
		</li>
		<li id="testResultsReport">
			<a href="testResultsReportFormGenerator.html">Test Results</a>
		</li>
		<li id="requestsReport">
			<a href="requestsReportFormGenerator.html">Requests</a>
		</li>
		<li id="issuedProductsReport">
			<a href="issuedProductsReportFormGenerator.html">Issued products</a>
		</li>
		<li id="discardedProductsReport">
			<a href="discardedProductsReportFormGenerator.html">Discarded products</a>
		</li>
	</ul>
</div>