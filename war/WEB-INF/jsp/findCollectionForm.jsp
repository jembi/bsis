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
<c:set var="findCollectionFormDivId">findCollectionFormDiv-${unique_page_id}</c:set>
<c:set var="findCollectionFormId">findCollectionForm-${unique_page_id}</c:set>
<c:set var="findCollectionFormSearchById">findCollectionFormSearchBySelector-${unique_page_id}</c:set>
<c:set var="findCollectionFormResultId">findCollectionFormResult-${unique_page_id}</c:set>
<c:set var="searchByInputDivId">searchByInputDivId-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
  $("#${findCollectionFormDivId}").find(".findCollectionButton").button({
    icons : {
      primary : 'ui-icon-search'
    }
  }).click(function() {
    var findCollectionFormData = $("#${findCollectionFormId}").serialize();
    showLoadingImage('${findCollectionFormResultId}');
    $.ajax({
      type : "GET",
      url : "findCollection.html",
      data : findCollectionFormData,
      success: function(data) {
      				   $('#${findCollectionFormResultId}').html(data);
        				 window.scrollTo(0, document.body.scrollHeight);
      				 },
      error: function(data) {
							 showErrorMessage("Something went wrong. Please try again later.");        
      			 }
    });
  });

  $("#${findCollectionFormDivId}").find(".clearFindFormButton").button({
    icons : {
      primary : 'ui-icon-grip-solid-horizontal'
    }
  }).click(clearFindForm);
  
  function clearFindForm() {
		$("#${findCollectionFormId}").each(function() {
		  this.reset();
		});
		$("#${findCollectionFormResultId}").html("");
		// show the appropriate input based on default search by
	  $("#${findCollectionFormId}").find(".searchBy").trigger("change");
  }

  $("#${findCollectionFormId}").find(".searchBy").change(toggleSearchBy);
  $("#${findCollectionFormId}").find(".searchBy").multiselect({
    selectedList: 1,
    multiple: false
  });
  // first time trigger change event so that the input box is shown
  $("#${findCollectionFormId}").find(".searchBy").trigger("change");

  function toggleSearchBy() {

		var form = $("#${findCollectionFormId}");
		var searchBy = form.find(".searchBy").val();
		// jquery ui multiselect must be destroyed before hiding the select options
		destroyCollectionCenterMultiSelect();
		form.find(".hidableDiv").each(function() {
		  $(this).hide();
		});

		switch(searchBy) {
			case "collectionNumber": form.find(".collectionNumberInput").show();
															 break;
			case "shippingNumber":   form.find(".shippingNumberInput").show();
															 break;
			case "sampleNumber":     form.find(".sampleNumberInput").show();
			 										     break;
			case "collectionCenter": form.find(".collectionCenterInput").show();
															 createCollectionCenterMultiSelect();
	     												 break;
		}
  }

  function createCollectionCenterMultiSelect() {
		$("#${findCollectionFormId}").find(".collectionCenterInput").multiselect({
		  position : {
		    my : 'left top',
		    at : 'right center'
		  },
		  noneSelectedText: 'None Selected',
		  selectedText: function(numSelected, numTotal, selectedValues) {
										if (numSelected == numTotal) {
										  return "Any Center";
										}
										else {
										  console.log(selectedValues);
										  var checkedValues = $.map(selectedValues, function(input) { return input.title; });
										  return checkedValues.length ? checkedValues.join(', ') : 'Any Center';
										}
		  							}
		});
  }

  function destroyCollectionCenterMultiSelect() {
    $("#${findCollectionFormId}").find(".collectionCenterInput").multiselect("destroy");
  }

  function getDateCollectedFromInput() {
    return $("#${findCollectionFormId}").find(".dateCollectedFrom");  
  }

  function getDateCollectedToInput() {
    return $("#${findCollectionFormId}").find(".dateCollectedTo");  
  }

  getDateCollectedFromInput().datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 0,
    dateFormat : "mm/dd/yy",
    yearRange : "c-100:c0",
    onSelect : function(selectedDate) {
      getDateCollectedToInput().datepicker("option", "minDate", selectedDate);
    }
  });

  getDateCollectedToInput().datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 0,
    dateFormat : "mm/dd/yy",
    yearRange : "c-100:c0",
    onSelect : function(selectedDate) {
      getDateCollectedFromInput().datepicker("option", "maxDate", selectedDate);
    }
  });

});
</script>

<div id="${findCollectionFormDivId}" class="formDiv">
	<b>Find Collections</b>
	<form:form method="GET" commandName="findCollectedSampleForm" id="${findCollectionFormId}"
		class="formInTabPane">
		<div>
			<form:label path="searchBy">Find Collection by </form:label>
			<!-- need to set id searchBy selector otherwise the
      		 search by selector in collections page will get the same id  -->
			<form:select path="searchBy" id="${tabContentId}-findCollectionsSearchBy" class="searchBy">
				<form:option value="collectionNumber" label="${model.collectedSampleFields.collectionNumber.displayName}" />
				<form:option value="shippingNumber" label="${model.collectedSampleFields.shippingNumber.displayName}" />
				<form:option value="sampleNumber" label="${model.collectedSampleFields.sampleNumber.displayName}" />
				<form:option value="collectionCenter" label="${model.collectedSampleFields.collectionCenter.displayName}" />
			</form:select>

			<div class="collectionNumberInput hidableDiv" style="display:none">
				<form:label path="collectionNumber"></form:label>
				<!-- Spring supports dynamic attributes so placeholder can be added -->
				<form:input path="collectionNumber" placeholder="Collection Number"/>
			</div>
	
			<div class="shippingNumberInput hidableDiv" style="display:none">
				<form:input path="shippingNumber" placeholder="Shipping Number" />
			</div>
	
			<div class="sampleNumberInput hidableDiv" style="display:none">
				<form:input path="sampleNumber" placeholder="Sample Number" />
			</div>
	
			<div class="collectionCenterInput hidableDiv" style="display:none">
				<form:select path="centers" class="collectionCenterSelector">
					<c:forEach var="center" items="${model.centers}">
						<form:option value="${center.id}" label="${center.name}" />
					</c:forEach>
				</form:select>
			</div>

		</div>

		<br />
		<br />

		<div>
			<span style="margin-left: 15px; font-style: italic;"> Date of collection between (optional)</span>
		</div>
		<div>
			<form:input path="dateCollectedFrom" class="dateCollectedFrom" placeholder="Any Date"/>
				and
			<form:input path="dateCollectedTo" class="dateCollectedTo" placeholder="Any Date"/>
		</div>

		<div>
			<label></label>
			<button type="button" class="findCollectionButton">
				Find collection
			</button>
			<button type="button" class="clearFindFormButton">
				Clear form
			</button>
		</div>
	</form:form>
</div>

<div id="${findCollectionFormResultId}"></div>