<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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

<c:set var="findDonationBatchFormDonationCentersSelectorId">findDonationBatchFormDonationCentersSelectorId-${unique_page_id}</c:set>
<c:set var="findDonationBatchFormDonationSitesSelectorId">findDonationBatchFormDonationSitesSelectorId-${unique_page_id}</c:set>

<c:set var="findDonationBatchFormId">findDonationBatchForm-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
  $( "#exactDate" ).datepicker();
  $("#${tabContentId}").find(".findDonationBatchButton").button({
    icons : {
      primary : 'ui-icon-search'
    }
  }).click(function() {
    var findDonationBatchFormData = $("#${findDonationBatchFormId}").serialize();
    var resultsDiv = $("#${mainContentId}").find(".findDonationBatchResults");
    $.ajax({
      type : "GET",
      url : "findDonationBatch.html",
      data : findDonationBatchFormData,
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

  $("#${findDonationBatchFormId}").find(".donationCenterSelector").multiselect({
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
  $("#${findDonationBatchFormId}").find(".donationCenterSelector").multiselect("checkAll");

  $("#${findDonationBatchFormId}").find(".donationSiteSelector").multiselect({
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
  $("#${findDonationBatchFormId}").find(".donationSiteSelector").multiselect("checkAll");

  $("#${tabContentId}").bind("donationBatchSummaryView",
      function(event, content) {
        $("#${mainContentId}").hide();
        $("#${childContentId}").html(content);
      });

  $("#${tabContentId}").bind("donationBatchSummarySuccess",
      function(event, content) {
        $("#${mainContentId}").show();
        $("#${childContentId}").html("");
        $("#${tabContentId}").find(".donationsBatchTable").trigger("refreshResults");
      });

  $("#${findDonationBatchFormId}").submit(function(event) {
    event.preventDefault();
  });
});
</script>
<sec:authorize access="hasRole(T(utils.PermissionConstants).VIEW_DONATION_BATCH)">
<div id="${tabContentId}" class="formDiv">
  <div id="${mainContentId}">
    <b>Find Donation Batches</b>
    <div class="tipsBox ui-state-highlight">
      <p>
        ${tips['donationbatch.find']}
      </p>
    </div>
    <form:form method="GET" commandName="findDonationBatchForm" id="${findDonationBatchFormId}"
      class="formFormatClass">
  
      <div>
        <form:label path="din">Donation Identification Number</form:label>
        <form:input path="din" placeholder="Donation Identifica Number" />
      </div>
  
      <div>
          <form:label path="donationCenters">Donation center</form:label>
          <form:select path="donationCenters"
                       id="${findDonationBatchFormDonationCentersSelectorId}"
                       class="donationCenterSelector">
            <c:forEach var="center" items="${centers}">
              <form:option value="${center.id}">${center.name}</form:option>
            </c:forEach>
          </form:select>
      </div>
  
      <div>
          <form:label path="donationSites">Donation site</form:label>
          <form:select path="donationSites"
                       id="${findDonationBatchFormDonationSitesSelectorId}"
                       class="donationSiteSelector">
            <c:forEach var="site" items="${sites}">
              <form:option value="${site.id}">${site.name}</form:option>
            </c:forEach>
          </form:select>
      </div>
          
      <div>
        <form:label path="exactDate">Date</form:label>
        <form:input path="exactDate" placeholder="Date"/>
        <form:select path="period">
            <form:option value="1">Exact Day</form:option>
            <form:option value="2">1 Day</form:option>
            <form:option value="8">1 Week</form:option>
            <form:option value="32">1 Month</form:option>
        </form:select>
      </div>

      <br />
      <br />

    </form:form>

    <div class="formFormatClass">
      <div>
        <label></label>
        <button type="button" class="findDonationBatchButton">
          Find donation batches
        </button>
        <button type="button" class="clearFindFormButton">
          Clear form
        </button>
      </div>
    </div>

  <div class="findDonationBatchResults"></div>  
</div>
  <div id="${childContentId}"></div>
</div>
</sec:authorize>