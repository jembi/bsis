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

<c:set var="findRequestFormId">findRequestForm-${unique_page_id}</c:set>

<c:set var="findRequestFormProductTypeSelectorId">findRequestFormProductTypeSelectorId-${unique_page_id}</c:set>
<c:set var="findRequestFormRequestSiteSelectorId">findRequestFormRequestSiteSelectorId-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  $("#${tabContentId}").find(".findRequestButton").button({
    icons : {
      primary : 'ui-icon-search'
    }
  }).click(function() {
    var findRequestFormData = $("#${findRequestFormId}").serialize();
    var resultsDiv = $("#${mainContentId}").find(".findRequestResults");
    showLoadingImage(resultsDiv);
    $.ajax({
      type : "GET",
      url : "findRequest.html",
      data : findRequestFormData,
      success : function(data) {
        resultsDiv.html(data);
        window.scrollTo(0, document.body.scrollHeight);
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

  $("#${tabContentId}").find(".productTypeSelector").multiselect({
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
										  var checkedValues = $.map(selectedValues, function(input) { return input.title; });
										  return checkedValues.length ? checkedValues.join(', ') : 'Any Product Type';
										}
										  
    							}
  });
  $("#${tabContentId}").find(".productTypeSelector").multiselect("checkAll");

  $("#${tabContentId}").find(".requestSiteSelector").multiselect({
    position : {
      my : 'left top',
      at : 'right center'
    },
    noneSelectedText: 'None Selected',
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
  $("#${tabContentId}").find(".requestSiteSelector").multiselect("checkAll");

  $("#${tabContentId}").find(".statusSelector").multiselect({
    multiple : false,
    selectedList : 1,
    header : false
  });

  // child div shows request information. bind this div to requestView event
  $("#${tabContentId}").bind("requestSummaryView",
      function(event, content) {
    		$("#${mainContentId}").hide();
    		$("#${childContentId}").html(content);
  		});

  $("#${tabContentId}").bind("requestSummarySuccess",
      function(event, content) {
    		$("#${mainContentId}").show();
    		$("#${childContentId}").html("");
    		$("#${tabContentId}").find(".requestsTable").trigger("refreshResults");
  		});

  $("#${tabContentId}").find(".requestedAfter").datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 0,
    dateFormat : "dd/mm/yy",
    yearRange : "c-100:c0",
  });

  $("#${tabContentId}").find(".requiredBy").datepicker({
    changeMonth : true,
    changeYear : true,
    minDate : -36500,
    maxDate : 365,
    dateFormat : "dd/mm/yy",
    yearRange : "c-100:c+1",
  });

});
</script>

<div id="${tabContentId}" class="formDiv">
	<div id="${mainContentId}">
		<b>Find Requests</b>
		<div class="tipsBox ui-state-highlight">
			<p>
				${tips['requests.findpending']}
			</p>
		</div>
		<form:form method="GET" commandName="findRequestForm" id="${findRequestFormId}"
			class="formInTabPane">

			<div>
				<form:label path="requestNumber">Request number</form:label>
				<form:input path="requestNumber" placeholder="Request Number" />
			</div>

			<div>
				<form:label path="productTypes">Product Type</form:label>
				<form:select id="${findRequestFormProductTypeSelectorId}"
										 path="productTypes"
										 class="productTypeSelector">
					<c:forEach var="productType" items="${productTypes}">
						<form:option value="${productType.id}" label="${productType.productType}" />
					</c:forEach>
				</form:select>
			</div>

			<div>
				<form:label path="requestSites">Requested by site</form:label>
				<form:select id="${findRequestFormRequestSiteSelectorId}"
										 path="requestSites" class="requestSiteSelector">
					<c:forEach var="requestSite" items="${sites}">
						<form:option value="${requestSite.id}" label="${requestSite.name}" />
					</c:forEach>
				</form:select>
			</div>

			<div>
				<form:label path="requestedAfter">Requested after </form:label>
				<form:input path="requestedAfter" class="requestedAfter" placeholder="Request Date"/>
			</div>
			<div>
				<form:label path="requiredBy">Required by </form:label>
				<form:input path="requiredBy" class="requiredBy" placeholder="Required By Date" />
			</div>

			<div>
				<form:label path="includeSatisfiedRequests" style="width: auto;">Include satisfied requests in results</form:label>
				<form:checkbox path="includeSatisfiedRequests" style="width: auto; position: relative; top: 2px;"/>
			</div>

		</form:form>

		<div class="formInTabPane">
			<div>
				<label></label>
				<button type="button" class="findRequestButton">
					Find requests
				</button>
				<button type="button" class="clearFindFormButton">
					Clear form
				</button>
			</div>
		</div>

		<div class="findRequestResults"></div>
	</div>

	<div id="${childContentId}"></div>

</div>
