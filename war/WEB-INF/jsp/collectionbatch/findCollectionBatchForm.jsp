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

<c:set var="findCollectionBatchFormCollectionCentersSelectorId">findCollectionBatchFormCollectionCentersSelectorId-${unique_page_id}</c:set>
<c:set var="findCollectionBatchFormCollectionSitesSelectorId">findCollectionBatchFormCollectionSitesSelectorId-${unique_page_id}</c:set>

<c:set var="findCollectionBatchFormId">findCollectionBatchForm-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  $("#${tabContentId}").find(".findCollectionBatchButton").button({
    icons : {
      primary : 'ui-icon-search'
    }
  }).click(function() {
    var findCollectionBatchFormData = $("#${findCollectionBatchFormId}").serialize();
    var resultsDiv = $("#${mainContentId}").find(".findCollectionBatchResults");
    $.ajax({
      type : "GET",
      url : "findCollectionBatch.html",
      data : findCollectionBatchFormData,
      success: function(data) {
        				 animatedScrollTo(resultsDiv);
				         resultsDiv.html(data);
      				 },
      error: function(data) {
							 showErrorMessage("Something went wrong. Please try again later.");        
      			 }
    });
  });

  $("#${tabContentId}").find(".clearFindFormButton").button({
    icons : {
      
    }
  }).click(clearFindForm);

  function clearFindForm() {
    refetchContent("${refreshUrl}", $("#${tabContentId}"));
    $("#${childContentId}").html("");
  }

	$("#${findCollectionBatchFormId}").find(".collectionCenterSelector").multiselect({
	  position : {
	    my : 'left top',
	    at : 'right center'
	  },
	  noneSelectedText: 'None Selected',
	  //header: false,
		 // uncomment the following if you wish to disallow selection of no options in the
		 // selection menu
	  //click: function(e) {
			 //if( $(this).multiselect("widget").find("input:checked").length == 0 ){
       // return false;
			 //}
		 //},
	  selectedText: function(numSelected, numTotal, selectedValues) {
										if (numSelected == numTotal) {
										  return "Any Center";
										}
										else {
										  var checkedValues = $.map(selectedValues, function(input) { return input.title; });
										  return checkedValues.length ? checkedValues.join(', ') : 'Any Center';
										}
	  							}
	});
	$("#${findCollectionBatchFormId}").find(".collectionCenterSelector").multiselect("checkAll");

	$("#${findCollectionBatchFormId}").find(".collectionSiteSelector").multiselect({
	  position : {
	    my : 'left top',
	    at : 'right center'
	  },
	  noneSelectedText: 'None Selected',
	  //header: false,
		 // uncomment the following if you wish to disallow selection of no options in the
		 // selection menu
	  //click: function(e) {
      			 //if( $(this).multiselect("widget").find("input:checked").length == 0 ){
             //  return false;
      			 //}
	  			 //},
	  selectedText: function(numSelected, numTotal, selectedValues) {
										if (numSelected == numTotal) {
										  return "Any Site";
										}
										else {
										  var checkedValues = $.map(selectedValues, function(input) { return input.title; });
										  return checkedValues.length ? checkedValues.join(', ') : 'Any Site';
										}
	  							}
	});
	$("#${findCollectionBatchFormId}").find(".collectionSiteSelector").multiselect("checkAll");

  $("#${tabContentId}").bind("collectionBatchSummaryView",
      function(event, content) {
    		$("#${mainContentId}").hide();
    		$("#${childContentId}").html(content);
  		});

  $("#${tabContentId}").bind("collectionBatchSummarySuccess",
      function(event, content) {
    		$("#${mainContentId}").show();
    		$("#${childContentId}").html("");
    		$("#${tabContentId}").find(".collectionsTable").trigger("refreshResults");
  		});
});
</script>

<div id="${tabContentId}" class="formDiv">
	<div id="${mainContentId}">
		<b>Find Collection Batches</b>
		<div class="tipsBox ui-state-highlight">
			<p>
				${tips['collectionbatch.find']}
			</p>
		</div>
		<form:form method="GET" commandName="findCollectionBatchForm" id="${findCollectionBatchFormId}"
			class="formInTabPane">
	
			<div>
				<form:label path="batchNumber">Batch number</form:label>
				<form:input path="batchNumber" placeholder="Batch Number" />
			</div>
	
			<div>
					<form:label path="collectionCenters">Collection center</form:label>
					<form:select path="collectionCenters"
											 id="${findCollectionBatchFormCollectionCentersSelectorId}"
											 class="collectionCenterSelector">
						<c:forEach var="center" items="${centers}">
							<form:option value="${center.id}">${center.name}</form:option>
						</c:forEach>
					</form:select>
			</div>
	
			<div>
					<form:label path="collectionSites">Collection site</form:label>
					<form:select path="collectionSites"
											 id="${findCollectionBatchFormCollectionSitesSelectorId}"
											 class="collectionSiteSelector">
						<c:forEach var="site" items="${sites}">
							<form:option value="${site.id}">${site.name}</form:option>
						</c:forEach>
					</form:select>
			</div>

			<br />
			<br />
	
			<div>
				<label></label>
				<button type="button" class="findCollectionBatchButton">
					Find collection batches
				</button>
				<button type="button" class="clearFindFormButton">
					Clear form
				</button>
			</div>
	
		</form:form>
	<div class="findCollectionBatchResults"></div>	
</div>
	<div id="${childContentId}"></div>
</div>
