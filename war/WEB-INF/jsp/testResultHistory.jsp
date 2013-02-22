<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tableContent-${unique_page_id}</c:set>

<script>
$(document).ready(function(){
  $("#${tabContentId}").find(".doneButton").button({ icons: {primary : "ui-icon-check"} }).click(function() {
    $("#${tabContentId}").parent().trigger("viewTestResultHistoryDone");
  });
});
</script>

<div id="${tabContentId}">
	<button class="doneButton">Done</button>
	<br />
	<br />
	<jsp:include page="testResultsTable.jsp" />
</div>