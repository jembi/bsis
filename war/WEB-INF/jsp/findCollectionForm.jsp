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

<c:set var="findCollectionFormId">findCollectionForm-${unique_page_id}</c:set>
<c:set var="findCollectionFormSearchById">findCollectionFormSearchBySelector-${unique_page_id}</c:set>
<c:set var="findCollectionFormResultId">findCollectionFormResult-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
  $("#${tabContentId}").find(".findCollectionButton").button({
    icons : {
      primary : 'ui-icon-search'
    }
  }).click(function() {
    var findCollectionFormData = $("#${findCollectionFormId}").serialize();
    var resultsDiv = $("#${mainContentId}").find(".findCollectionResults");
    //showLoadingImage(resultsDiv);
    $.ajax({
      type : "GET",
      url : "findCollection.html",
      data : findCollectionFormData,
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
    refetchContent("${model.refreshUrl}", $("#${tabContentId}"));
    $("#${childContentId}").html("");
  }

	$("#${findCollectionFormId}").find(".bloodBagTypeSelector").multiselect({
	  position : {
	    my : 'left top',
	    at : 'right center'
	  },
	  noneSelectedText: 'None Selected',
	  header: false,
		 // uncomment the following if you wish to disallow selection of no options in the
		 // selection menu
	  //click: function(e) {
			 //if($(this).multiselect("widget").find("input:checked").length == 0 ){
       // return false;
			 //}
		 //},
	  selectedText: function(numSelected, numTotal, selectedValues) {
										if (numSelected == numTotal) {
										  return "Any Blood Bag Type";
										}
										else {
										  var checkedValues = $.map(selectedValues, function(input) { return input.title; });
										  return checkedValues.length ? checkedValues.join(', ') : 'Any Blood Bag Type';
										}
	  							}
	});
	$("#${findCollectionFormId}").find(".bloodBagTypeSelector").multiselect("checkAll");

	$("#${findCollectionFormId}").find(".collectionCenterSelector").multiselect({
	  position : {
	    my : 'left top',
	    at : 'right center'
	  },
	  noneSelectedText: 'None Selected',
	  header: false,
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
	$("#${findCollectionFormId}").find(".collectionCenterSelector").multiselect("checkAll");

	$("#${findCollectionFormId}").find(".collectionSiteSelector").multiselect({
	  position : {
	    my : 'left top',
	    at : 'right center'
	  },
	  noneSelectedText: 'None Selected',
	  header: false,
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
	$("#${findCollectionFormId}").find(".collectionSiteSelector").multiselect("checkAll");

  function getDateOfCollectionFromInput() {
    return $("#${findCollectionFormId}").find(".dateCollectedFrom");  
  }

  function getDateOfCollectionToInput() {
    return $("#${findCollectionFormId}").find(".dateCollectedTo");  
  }

  getDateOfCollectionFromInput().datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 365,
    dateFormat : "mm/dd/yy",
    yearRange : "c-100:c+1",
    onSelect : function(selectedDate) {
      getDateOfCollectionToInput().datepicker("option", "minDate", selectedDate);
    }
  });

  getDateOfCollectionToInput().datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 365,
    dateFormat : "mm/dd/yy",
    yearRange : "c-100:c+1",
    onSelect : function(selectedDate) {
      getDateOfCollectionFromInput().datepicker("option", "maxDate", selectedDate);
    }
  });

  // child div shows donor information. bind this div to collectionSummaryView event
  $("#${tabContentId}").bind("collectionSummaryView",
      function(event, content) {
    		$("#${mainContentId}").hide();
    		$("#${childContentId}").html(content);
  		});

  $("#${tabContentId}").bind("collectionSummarySuccess",
      function(event, content) {
    		$("#${mainContentId}").show();
    		$("#${childContentId}").html("");
    		$("#${tabContentId}").find(".collectionsTable").trigger("refreshResults");
  		});
});
</script>

<div id="${tabContentId}" class="formDiv">
	<div id="${mainContentId}">
		<b>Find Collections</b>
		<div class="tipsBox ui-state-highlight">
			<p>
				${model['collectedSamples.find']}
			</p>
		</div>
		<form:form method="GET" commandName="findCollectedSampleForm" id="${findCollectionFormId}"
			class="formInTabPane">
	
			<div>
				<form:label path="collectionNumber">Collection number</form:label>
				<form:input path="collectionNumber" placeholder="Collection Number" />
			</div>
	
			<div>
				<form:label path="bloodBagTypes">Blood Bag Type</form:label>
				<form:select id="${tabContentId}-bloodBagTypes" path="bloodBagTypes" class="bloodBagTypeSelector">
					<c:forEach var="bloodBagType" items="${model.bloodBagTypes}">
						<form:option value="${bloodBagType.id}" label="${bloodBagType.bloodBagType}" />
					</c:forEach>
				</form:select>
			</div>
	
			<div>
					<form:label path="collectionCenters">Collection center</form:label>
					<form:select id="${tabContentId}-collectionCenters" path="collectionCenters" class="collectionCenterSelector">
						<c:forEach var="center" items="${model.centers}">
							<form:option value="${center.id}">${center.name}</form:option>
						</c:forEach>
					</form:select>
			</div>
	
			<div>
					<form:label path="collectionSites">Collection site</form:label>
					<form:select id="${tabContentId}-collectionSites" path="collectionSites" class="collectionSiteSelector">
						<c:forEach var="site" items="${model.sites}">
							<form:option value="${site.id}">${site.name}</form:option>
						</c:forEach>
					</form:select>
			</div>

			<div>
				<label style="width: auto;">Date collected between</label>
				<form:input path="dateCollectedFrom" class="dateCollectedFrom" placeholder="From"/>
				and
				<form:input path="dateCollectedTo" class="dateCollectedTo" placeholder="To"/>
			</div>
			<br />
			<br />
	
			<div>
				<label></label>
				<button type="button" class="findCollectionButton">
					Find collections
				</button>
				<button type="button" class="clearFindFormButton">
					Clear form
				</button>
			</div>
	
		</form:form>
	<div class="findCollectionResults"></div>	
</div>
	<div id="${childContentId}"></div>
</div>
