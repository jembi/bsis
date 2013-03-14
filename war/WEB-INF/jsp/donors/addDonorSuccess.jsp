<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>
<c:set var="childContentId">childContent-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
  $.ajax({
    url: "donorSummary.html",
    data: {donorId : "${donorId}"},
    type: "GET",
    success : function (response) {
      					$("#${tabContentId}").find(".donorSummaryContainer").html(response);
    					}
  });
});
</script>

<div id="${tabContentId}">

	<div class="successBox ui-state-highlight">
		<img src="images/check_icon.png" style="height: 30px;" />
		<span class="successText">
			Donor record added Successfully.
			<br />
			You can view the details below and print donor card. Click "Add another donor" to add another donor.
		</span>
	</div>

	<div class="donorSummaryContainer">
	</div>
</div>