<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  pageContext.setAttribute("newLineChar", "\n");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>


<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>

<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>
<c:set var="childContentId">childContent-${unique_page_id}</c:set>

<c:set var="findWorksheetFormId">findWorksheetForm-${unique_page_id}</c:set>
<c:set var="findWorksheetFormWorksheetTypeSelectorId">findWorksheetFormWorksheetTypeSelectorId-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  $("#${tabContentId}").find(".findWorksheetButton").button({
    icons : {
      primary : 'ui-icon-search'
    }
  }).click(function() {
    var findWorksheetFormData = $("#${findWorksheetFormId}").serialize();
    var resultsDiv = $("#${mainContentId}").find(".findWorksheetResults");
    //showLoadingImage(resultsDiv);
    $.ajax({
      type : "GET",
      url : "findWorksheet.html",
      data : findWorksheetFormData,
      success: function(data) {
        				 animatedScrollTo(resultsDiv);
				         resultsDiv.html(data);
      				 },
      error: function(data) {
							 showErrorMessage("Something went wrong. Please try again later.");        
      			 }
    });
  });

  $("#${tabContentId}").find(".clearFindFormButton")
  										 .button()
  										 .click(clearFindForm);
  
  function clearFindForm() {
    refetchContent("${refreshUrl}", $("#${tabContentId}"));
    $("#${childContentId}").html("");
  }

	$("#${findWorksheetFormId}").find(".worksheetTypeSelector").multiselect({
	  position : {
	    my : 'left top',
	    at : 'right center'
	  },
	  noneSelectedText: 'None Selected',
	  selectedText: function(numSelected, numTotal, selectedValues) {
										if (numSelected == numTotal) {
										  return "Any Worksheet Type";
										}
										else {
										  var checkedValues = $.map(selectedValues, function(input) { return input.title; });
										  return checkedValues.length ? checkedValues.join(', ') : 'Any Worksheet Type';
										}
	  							}
	});

	$("#${findWorksheetFormId}").find(".worksheetTypeSelector")
															.multiselect("checkAll");

  $("#${tabContentId}").bind("worksheetSummaryView",
      function(event, content) {
    		$("#${mainContentId}").hide();
    		$("#${childContentId}").html(content);
  		});

  $("#${tabContentId}").bind("worksheetSummarySuccess",
      function(event, content) {
    		$("#${mainContentId}").show();
    		$("#${childContentId}").html("");
    		$("#${tabContentId}").find(".worksheetsTable").trigger("refreshResults");
  		});

});
</script>

<div id="${tabContentId}">

	<div id="${mainContentId}">

		<form:form method="GET" commandName="findWorksheetForm" id="${findWorksheetFormId}"
			class="formInTabPane">
			<div>
				<label style="width: auto;"><b>Find collections worksheet</b></label>
			</div>
	
			<div>
				<form:label path="worksheetNumber">Worksheet number</form:label>
				<form:input path="worksheetNumber" placeholder="Worksheet Number" />
			</div>
	
			<div>
				<form:label path="worksheetTypes">Worksheet Type</form:label>
				<form:select path="worksheetTypes"
										 id="${findWorksheetFormWorksheetTypeSelectorId}"
										 class="worksheetTypeSelector">
					<c:forEach var="worksheetType" items="${worksheetTypes}">
						<form:option value="${worksheetType.id}" label="${worksheetType.worksheetType}" />
					</c:forEach>
				</form:select>
			</div>

			<form:hidden path="worksheetResultClickUrl" value="${worksheetResultClickUrl}" />

			<div>
				<label></label>
				<button type="button" class="findWorksheetButton">
					Find worksheets
				</button>
				<button type="button" class="clearFindFormButton">
					Clear form
				</button>
			</div>
			</form:form>

		<div class="findWorksheetResults"></div>

	</div>

	<div id="${childContentId}">
	</div>

</div>