<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  pageContext.setAttribute("newLineChar", "\n");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<link type="text/css" rel="stylesheet" href="css/admin.css" />
<script type="text/javascript" src="js/admin.js"></script>

<div id="adminTab" class="leftPanel">
	<ul>
		<li id="configureForms">
			<a href="configureForms.html">Configure Forms</a>
		</li>
		<li id="locationForm">
			<a href="configureLocationsFormGenerator.html">Centers/Sites</a>
		</li>
		<li id="productTypesForm">
			<a href="configureProductTypesFormGenerator.html">Product Types</a>
		</li>
		<li id="bloodBagTypesForm">
			<a href="configureBloodBagTypesFormGenerator.html">Blood Bag Types</a>
		</li>
		<li id="configureTipsForm">
			<a href="configureTipsFormGenerator.html">Tips</a>
		</li>
		<li id="createSampleData">
			<a href="createSampleDataFormGenerator.html">Sample Data</a>
		</li>
	</ul>
</div>
