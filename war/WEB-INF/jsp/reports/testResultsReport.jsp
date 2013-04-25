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

<c:set var="ttiTestsSelectorId">ttiTestsSelectorId-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
  $("#testResultReportTests").multiselect({
    position : {
      my : 'left top',
      at : 'right center'
    }
  });

  $("#trreportsDateTestedFrom").datepicker(
      {
        changeMonth : true,
        changeYear : true,
        minDate : -36500,
        maxDate : 0,
        dateFormat : "dd/mm/yy",
        yearRange : "c-100:c0",
        onSelect : function(selectedDate) {
          $("#trreportsDateTestedTo").datepicker("option", "minDate",
              selectedDate);
        }
      });

  var firstDayOfYear = new Date();
	firstDayOfYear.setDate(1);
	firstDayOfYear.setMonth(0);
  $("#trreportsDateTestedFrom").datepicker("setDate", firstDayOfYear);

  $("#trreportsDateTestedTo").datepicker(
      {
        changeMonth : true,
        changeYear : true,
        minDate : -36500,
        maxDate : 0,
        dateFormat : "dd/mm/yy",
        yearRange : "c-100:c0",
        onSelect : function(selectedDate) {
          $("#trreportsDateTestedFrom").datepicker("option", "maxDate",
              selectedDate);
        }
      });

  $("#testResultsReportFormAggregationCriteria").multiselect({
    multiple : false,
    selectedList : 1,
    header : false
  });

  $("#generateTestResultsReportButton").button({
    icons : {
      primary : 'ui-icon-print'
    }
  }).click(function() {
    var formData = $("#testResultsReportForm").serialize();
    $.ajax({
      type : "GET",
      url : "getTestResultsReport.html",
      data : formData,
      success : function(data) {
        getTestResultsChart({
          data : data.numTestResults,
          renderDest : "testResultsReportResult",
          title : "Test Results Report",
          hoverText : "Test Results",
          yAxisTitle : "No. of Test Results",
          startTime : data.dateTestedFromUTC,
          endTime : data.dateTestedToUTC,
          interval : data.interval
        });
      },
      error: function(data) {
        			 showErrorMessage("Something went wrong. Please try again later.");
      			 }
    });
  });
  
  $("#clearTestResultsReportButton").button({
    icons: {
      primary: 'ui-icon-grip-solid-horizontal'
    }
  }).click(function() {
    $("#testResultsReportResult").html("");
  });

  $("#testResultsReportForm").find(".collectionCenterSelector").multiselect({
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
									  var checkedValues = $.map(selectedValues, function(input) { return input.title; });
									  return checkedValues.length ? checkedValues.join(', ') : 'Any Center';
									}
	  }
	});

  $("#testResultsReportForm").find(".collectionCenterSelector").multiselect("checkAll");

  $("#testResultsReportForm").find(".collectionSiteSelector").multiselect({
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

  $("#testResultsReportForm").find(".collectionSiteSelector").multiselect("checkAll");

  $("#${ttiTestsSelectorId}").multiselect({
	  position : {
	    my : 'left top',
	    at : 'right center'
	  },
	  noneSelectedText: 'None Selected',
	  selectedText: function(numSelected, numTotal, selectedValues) {
										if (numSelected == numTotal) {
										  return "All Tests";
										}
										else {
										  var checkedValues = $.map(selectedValues, function(input) { return input.title; });
										  return checkedValues.length ? checkedValues.join(', ') : 'All Tests';
										}
	  							}
	});

  $("#${ttiTestsSelectorId}").multiselect("checkAll");

});
</script>

<form:form method="GET" commandName="testResultsReportForm"
	id="testResultsReportForm">

		<br/>
		<div class="tipsBox ui-state-highlight">
			<p>
				${model['report.collections.testresultsreport']}
			</p>
		</div>

	<table>
		<thead>
		</thead>
		<tbody>
			<tr>
				<td>
					<form:label path="ttiTests">TTI Tests to compare</form:label>
				</td>
				<td>
					<form:select path="ttiTests" id="${ttiTestsSelectorId}">
						<c:forEach var="ttiTest" items="${ttiTests}">
							<form:option value="${ttiTest.id}" label="${ttiTest.testNameShort}" />
						</c:forEach>
					</form:select>
				</td>
			</tr>
			<tr>
				<td>Enter Date Range</td>
			</tr>
			<tr>
				<td><form:input path="dateTestedFrom"
						id="trreportsDateTestedFrom" placeholder="From Date" />&nbsp;to</td>
				<td><form:input path="dateTestedTo" id="trreportsDateTestedTo" placeholder="To Date" /></td>
			</tr>
			<tr>
				<td><form:label path="centers">Collection Centers</form:label></td>
				<td style="padding-left: 10px;"><form:select path="centers" class="collectionCenterSelector">
					<c:forEach var="center" items="${model.centers}">
						<form:option value="${center.id}" label="${center.name}" />
					</c:forEach>
				</form:select>
				</td>
			</tr>
			<tr>
				<td>
					<form:label path="sites">Collection Sites</form:label>
				</td>
				<td style="padding-left: 10px;">
					<form:select path="sites" class="collectionSiteSelector">
						<c:forEach var="site" items="${model.sites}">
							<form:option value="${site.id}" label="${site.name}" />
						</c:forEach>
					</form:select>
				</td>
			</tr>
			<tr>
				<td />
			</tr>
			<tr>
				<td><form:label path="aggregationCriteria"> Aggregation Criteria </form:label></td>
				<td style="padding-left: 10px;"><form:select
						path="aggregationCriteria"
						id="testResultsReportFormAggregationCriteria">
						<form:option value="daily" label="Daily" selected="" />
						<form:option value="monthly" label="Monthly" selected="selected" />
						<form:option value="yearly" label="Yearly" selected="" />
					</form:select></td>
			</tr>
			<tr>
				<td />
				<td><button type="button" id="generateTestResultsReportButton"
						style="margin-left: 10px">Generate report</button>
						<button type="button" id="clearTestResultsReportButton"
						style="margin-left: 10px">Clear report</button>
						
						</td>
			</tr>
		</tbody>
	</table>
</form:form>

<div id="testResultsReportResult" style="margin-right: 10px; margin-left: 10px; width: 90%;"></div>
