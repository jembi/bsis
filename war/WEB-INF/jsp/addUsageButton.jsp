<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%!public long getCurrentTime() {
		return System.nanoTime();
	}
	Long button_id = getCurrentTime();%>

<c:set var="button_id"><%=getCurrentTime()%></c:set>

<script>
	$(".addUsageButton").button();

	function bootup() {
		generateEditForm("editUsageFormGenerator.html", {isDialog : "yes"},
		    						 addNewUsage,
		    						 "Add New Usage",
		    						 "addUsageButtonEdit-" + "<%=button_id%>",
		    						 function(){}, 550, 575);
	}
</script>

<button id="addUsageButton-${button_id}" onclick="bootup(${button_id});"
	class="addUsageButton">Click here to Add New Usage</button>
