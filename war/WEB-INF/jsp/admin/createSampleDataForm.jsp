<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="createSampleDataFormDivId">createSampleDataFormDiv-${unique_page_id}</c:set>
<c:set var="createSampleDataFormId">createSampleDataForm-${unique_page_id}</c:set>
<c:set var="createSampleDataFormButtonId">createSampleDataFormButton-${unique_page_id}</c:set>

<script>
$(document).ready(
  function() {

    $("#${createSampleDataFormButtonId}").button().click(

      function() {

        var numDonors = $("#${createSampleDataFormId}").find('input[name="numDonors"]').val();
        var numCollections = $("#${createSampleDataFormId}").find('input[name="numCollections"]').val();
        var numProducts = $("#${createSampleDataFormId}").find('input[name="numProducts"]').val();
        var numTestResults = $("#${createSampleDataFormId}").find('input[name="numTestResults"]').val();
        var numRequests = $("#${createSampleDataFormId}").find('input[name="numRequests"]').val();
        var numUsages = $("#${createSampleDataFormId}").find('input[name="numUsages"]').val();
        var numIssues = $("#${createSampleDataFormId}").find('input[name="numIssues"]').val();
        $.ajax({
          url : "createSampleData.html",
          data : {
            numDonors: numDonors,
            numCollections: numCollections,
            numProducts: numProducts,
            numTestResults: numTestResults,
            numRequests: numRequests,
            numUsages: numUsages,
            numIssues: numIssues,
          },
          type : "POST",
          success : function(responseData) {
     				          console.log(responseData);
     				          if (responseData.success == true) {
               			   	$.showMessage("Data created successfully!");
              			   	reloadCurrentTab();
     				          }
     				          else {
   					            $.showMessage("Something went wrong." + responseData["errMsg"], {
										      backgroundColor : 'red'
	 											});
     				          }
                		}
        });
      });
  });
</script>

<div id="${createSampleDataFormDivId}" class="editFormDiv">
	<div style="font-weight: bold; font-style: italic; margin-top: 20px; margin-bottom: 20px;">
		Create sample data for testing Vein-to-Vein</div>
	<form id="${createSampleDataFormId}" class="editForm">
		<div>
			<label> No. of donors </label>
			<input name="numDonors" type="number" value="0"
						 min="0" max="10000" step="20" />
		</div>
		<div>
			<label> No. of collections </label>
			<input name="numCollections" type="number" value="0"
						 min="0" max="10000" step="20" />
		</div>
		<div>
			<label> No. of products </label>
			<input name="numProducts" type="number" value="0"
						 min="0" max="10000" step="20" />
		</div>
		<div>
			<label> No. of test results </label>
			<input name="numTestResults" type="number" value="0"
						 min="0" max="10000" step="20" />
		</div>
		<div>
			<label> No. of requests </label>
			<input name="numRequests" type="number" value="0"
						 min="0" max="10000" step="20" />
		</div>
		<div>
			<label> No. of usages </label>
			<input name="numUsages" type="number" value="0"
						 min="0" max="10000" step="20" />
		</div>
		<div>
			<label> No. of issues </label>
			<input name="numIssues" type="number" value="0"
						 min="0" max="10000" step="20" />
		</div>
	</form>
	<div>
		<button id="${createSampleDataFormButtonId}">
			Create sample data
		</button>
	</div>
</div>
