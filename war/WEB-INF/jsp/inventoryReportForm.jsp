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
  $(document).ready(
      function() {
        $("#${tabContentId}").find(".generateInventoryReportButton").button().click(function() {

          showLoadingImage($("#${childContentId}"));
          $.ajax({
            url: "generateInventoryReport.html",
            type: "GET",
            data: {},
            success: function(responseData) {
              				 console.log(responseData);
              				 showMessage("Inventory Report successfully generated");
              	        generateInventoryChart({
              	          data : responseData,
              	          renderDest : "${childContentId}",
              	        });

            				 },
            error: 	 function(responseData) {
              				 console.log(responseData);
              				 showErrorMessage("Something went wrong while generating inventory report");
            				 },
          });
          
        });
        
        $("#${tabContentId}").find(".clearReportButton").button().click(function() {
          $("#${childContentId}").html("");
        });
        
      });
</script>

<div id="${tabContentId}">
	<div id="${mainContentId}" class="reportMessage">
		<div>
			Click the "Generate Inventory Report" button below to generate a report of your products.
		</div>
		<div style="margin-top: 10px;">
			<button class="generateInventoryReportButton">Generate Inventory Report</button>
			<button class="clearReportButton">Clear Report</button>
		</div>
	</div>

	<hr />
	<br />
	<br />

	<div id="${childContentId}"></div>
</div>
