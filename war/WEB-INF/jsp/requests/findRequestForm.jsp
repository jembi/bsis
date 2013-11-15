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
<c:set var="addRequestFormId">addRequestForm-${unique_page_id}</c:set>

<c:set var="findRequestFormProductTypeSelectorId">findRequestFormProductTypeSelectorId-${unique_page_id}</c:set>
<c:set var="findRequestFormRequestSiteSelectorId">findRequestFormRequestSiteSelectorId-${unique_page_id}</c:set>
<c:set var="addRequestFormRequestSiteSelectorId">addRequestFormRequestSiteSelectorId-${unique_page_id}</c:set>

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
  
  $("#${tabContentId}").find(".addRequestButton").button({
	    icons : {
	      primary : 'ui-icon-search'
	    }
	  }).click(function() {
		  var addRequestFormData = $("#${addRequestFormId}").serialize();
		    var resultsDiv = $("#${mainContentId}").find(".addRequestResults");
	    //showLoadingImage(resultsDiv);
	    $.ajax({
	      type : "GET",
	      url : "addRequestFormGenerator.html",
	      data : addRequestFormData,
	      success : function(data) {
	    	  $("#${tabContentId}").find(".addRequestForm").hide();
	    	  $("#${tabContentId}").find(".findRequestForm").hide();
	    	  resultsDiv.html(data);
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
  
  $("#${addRequestFormId}").find(".requestSites").multiselect({
      multiple : false,
      selectedList : 1,
      header : false
    });
  
  $("#${addRequestFormId}").find(".requestType").multiselect({
      multiple : false,
      selectedList : 1,
      header : false
    });

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
  <div class="findRequestForm">
    <b>Find Requests</b>
    <div class="tipsBox ui-state-highlight">
      <p>
        ${tips['requests.findpending']}
      </p>
    </div>
    <form:form method="GET" commandName="findRequestForm" id="${findRequestFormId}"
      class="formFormatClass">

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

    <div class="formFormatClass">
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
     <div class="addRequestResults"></div>
  </div>

  <div id="${childContentId}"></div>
  
 <div class="addRequestForm">
  <b>Add Requests</b>
  
   <form:form method="GET" commandName="addRequestForm" id="${addRequestFormId}"
      class="formFormatClass">

      <div>
        <form:label path="requestNumber">${requestFields.requestNumber.displayName}</form:label>
        <form:input path="requestNumber" />
      </div>
      <div>
        <form:label path="requestSite">${requestFields.requestSite.displayName}</form:label>
       	<form:select path="requestSite"
            id="${addRequestFormRequestSiteSelectorId}"
            class="requestSites">
            <form:option value="" selected="selected">&nbsp;</form:option>
            <c:forEach var="site" items="${sites}">
              <form:option value="${site.id}">${site.name}</form:option>
            </c:forEach>
          </form:select>
      </div>
      <div>
          <form:label path="requestType">${requestFields.requestType.displayName}</form:label>
          <form:select path="requestType"
                       id="${addRequestFormRequestTypeSelectorId}"
                       class="requestType">
            <form:option value="">&nbsp;</form:option>
            <c:forEach var="requestType" items="${requestTypes}">
              <form:option value="${requestType.id}">${requestType.requestType}</form:option>
            </c:forEach>
          </form:select>
        </div>
	</form:form>
	
	<div class="formFormatClass">
      <div>
        <label></label>
        <button type="button" class="addRequestButton autoWidthButton">
          Add requests
        </button>
      </div>
    </div>
   </div>
</div>
