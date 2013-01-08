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

<c:set var="tabContentId">findProductFormDiv-${unique_page_id}</c:set>
<c:set var="findProductFormId">findProductForm-${unique_page_id}</c:set>
<c:set var="findProductFormSearchById">findProductFormSearchBySelector-${unique_page_id}</c:set>
<c:set var="findProductFormResultId">findProductFormResult-${unique_page_id}</c:set>
<c:set var="searchByInputDivId">searchByInputDivId-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
  $("#${tabContentId}").find(".findProductButton").button({
    icons : {
      primary : 'ui-icon-search'
    }
  }).click(function() {
    var findProductFormData = $("#${findProductFormId}").serialize();
    var resultsDiv = $("#${mainContentId}").find(".findProductResults");
    showLoadingImage(resultsDiv);
    $.ajax({
      type : "GET",
      url : "findProduct.html",
      data : findProductFormData,
      success: function(data) {
				         resultsDiv.html(data);
        				 window.scrollTo(0, document.body.scrollHeight);
      				 },
      error: function(data) {
							 showErrorMessage("Something went wrong. Please try again later.");        
      			 }
    });
  });

  $("#${tabContentId}").find(".clearFindFormButton").button({
    icons : {
      primary : 'ui-icon-grip-solid-horizontal'
    }
  }).click(clearFindForm);
  
  function clearFindForm() {
    refetchContent("${model.refreshUrl}", $("#${tabContentId}"));
    $("#${childContentId}").html("");
		// show the appropriate input based on default search by
	  $("#${findProductFormId}").find(".searchBy").trigger("change");
  }

  console.log($("#${findProductFormId}").find(".searchBy"));
  $("#${findProductFormId}").find(".searchBy").change(toggleSearchBy);
  $("#${findProductFormId}").find(".searchBy").multiselect({
    selectedList: 1,
    multiple: false
  });
  // first time trigger change event so that the input box is shown
  $("#${findProductFormId}").find(".searchBy").trigger("change");

  function toggleSearchBy() {

		var form = $("#${findProductFormId}");
		var searchBy = form.find(".searchBy").val();
		// jquery ui multiselect must be destroyed before hiding the select options
		destroyProductTypeMultiSelect();
		form.find(".hidableDiv").each(function() {
		  $(this).hide();
		});

		console.log(searchBy);
		switch(searchBy) {
			case "productNumber": form.find(".productNumberInput").show();
															 break;
			case "collectionNumber":   form.find(".collectionNumberInput").show();
															 break;
			case "productType": form.find(".productTypeInput").show();
								  			 createProductTypeMultiSelect();
			 									 break;
		}
  }

  function createProductTypeMultiSelect() {
		$("#${findProductFormId}").find(".productTypeSelector").multiselect({
		  position : {
		    my : 'left top',
		    at : 'right center'
		  },
		  noneSelectedText: 'None Selected',
		  selectedText: function(numSelected, numTotal, selectedValues) {
											if (numSelected == numTotal) {
											  return "Any Product Type";
											}
											else {
											  console.log(selectedValues);
											  var checkedValues = $.map(selectedValues, function(input) { return input.title; });
											  return checkedValues.length ? checkedValues.join(', ') : 'Any Product Type';
											}
		  							}
		});
		$("#${findProductFormId}").find(".productTypeSelector").multiselect("checkAll");
  }

  function destroyProductTypeMultiSelect() {
    $("#${findProductFormId}").find(".productTypeInput").multiselect("destroy");
  }

  function getDateExpiresFromInput() {
    return $("#${findProductFormId}").find(".dateExpiresFrom");  
  }

  function getDateExpiresToInput() {
    return $("#${findProductFormId}").find(".dateExpiresTo");  
  }

  getDateExpiresFromInput().datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 365,
    dateFormat : "mm/dd/yy",
    yearRange : "c-100:c+1",
    onSelect : function(selectedDate) {
      getDateExpiresToInput().datepicker("option", "minDate", selectedDate);
    }
  });

  getDateExpiresToInput().datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 365,
    dateFormat : "mm/dd/yy",
    yearRange : "c-100:c+1",
    onSelect : function(selectedDate) {
      getDateExpiresFromInput().datepicker("option", "maxDate", selectedDate);
    }
  });

  // child div shows donor information. bind this div to productSummaryView event
  $("#${tabContentId}").bind("productSummaryView",
      function(event, content) {
    		$("#${mainContentId}").hide();
    		$("#${childContentId}").html(content);
  		});

  $("#${tabContentId}").bind("productSummarySuccess",
      function(event, content) {
    		$("#${mainContentId}").show();
    		$("#${childContentId}").html("");
    		$("#${tabContentId}").find(".productsTable").trigger("refreshResults");
  		});
});
</script>

<div id="${tabContentId}" class="formDiv">
	<div id="${mainContentId}">
	<b>Find Products</b>
		<div class="tipsBox ui-state-highlight">
			<p>
				${model['products.find']}
			</p>
		</div>
	<form:form method="GET" commandName="findProductForm" id="${findProductFormId}"
		class="formInTabPane">
		<div>
			<form:label path="searchBy">Find Product by </form:label>
			<form:select path="searchBy" class="searchBy">
				<form:option value="collectionNumber" label="${model.productFields.collectionNumber.displayName}" />
				<form:option value="productNumber" label="${model.productFields.productNumber.displayName}" />
				<form:option value="productType" label="${model.productFields.productType.displayName}" />
			</form:select>

			<div class="productNumberInput hidableDiv" style="display:none">
				<form:label path="productNumber"></form:label>
				<!-- Spring supports dynamic attributes so placeholder can be added -->
				<form:input path="productNumber" placeholder="Product Number"/>
			</div>
	
			<div class="collectionNumberInput hidableDiv" style="display:none">
				<form:input path="collectionNumber" placeholder="Collection Number" />
			</div>
	
			<div class="productTypeInput hidableDiv" style="display:none">
				<form:select path="productTypes" class="productTypeSelector">
					<c:forEach var="productType" items="${model.productTypes}">
						<form:option value="${productType.productType}" label="${productType.productTypeName}" />
					</c:forEach>
				</form:select>
			</div>

		</div>

		<br />
		<br />

		<div>
			<span style="margin-left: 15px;"> Date of expiry between </span>
		</div>
		<div>
			<form:input path="dateExpiresFrom" class="dateExpiresFrom" placeholder="Any Date"/>
				and
			<form:input path="dateExpiresTo" class="dateExpiresTo" placeholder="Any Date"/>
		</div>

		<div>
			<label></label>
			<button type="button" class="findProductButton">
				Find product
			</button>
			<button type="button" class="clearFindFormButton">
				Clear form
			</button>
		</div>
	</form:form>
	<div class="findProductResults"></div>	
</div>
	<div id="${childContentId}"></div>
</div>
