<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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

  if (${collectionFound} === true) {
    fetchBloodTypingResults();
  }

  function fetchBloodTypingResults() {
	  $.ajax({
	    url: "showBloodTypingResultsForCollection.html",
	    type: "GET",
	    data: {collectionId : "${collectionId}", showDoneButton: false},
	    success: function(response) {
	      				 getBloodTypingSection().html(response);
	    				 },
	    error:   function() {
	      			   showErrorMessage("Something went wrong")
	    			   }
	  });
  }

  function getBloodTypingSection() {
    return $("#${mainContentId}").find(".bloodTypingSection");
  }
});
</script>

<div id="${tabContentId}">

	<div id="${mainContentId}">

		<c:if test="${not collectionFound}">
			Collection not found. Please check the collection number.
		</c:if>

		<c:if test="${collectionFound}">

			<br />
			<span style="margin-left: 31px; font-weight: bold;">Blood typing results for collection</span>
			<div class="bloodTypingSection">
			</div>

		</c:if>

	</div>

	<div id="${childContentId}">
	</div>
</div>