<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  pageContext.setAttribute("newLineChar", "\n");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<script>
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
        dateFormat : "mm/dd/yy",
        yearRange : "c-100:c0",
        onSelect : function(selectedDate) {
          $("#trreportsDateTestedTo").datepicker("option", "minDate",
              selectedDate);
        }
      });

  $("#trreportsDateTestedTo").datepicker(
      {
        changeMonth : true,
        changeYear : true,
        minDate : -36500,
        maxDate : 0,
        dateFormat : "mm/dd/yy",
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
        getTimeChart({
          data : data.numTestResults,
          renderDest : "testResultsReportResult",
          title : "Test Results Report",
          hoverText : "Test Results",
          yAxisTitle : "No. of Test Results",
          startTime : data.dateTestedFromUTC,
          endTime : data.dateTestedToUTC,
          interval : data.interval
        });
      }
    });
  });
</script>

<form:form method="GET" commandName="testResultsReportForm"
	id="testResultsReportForm">
	Under Construction
	<table>
		<thead>
		</thead>
		<tbody>
			<tr>
				<td><form:label path="tests">Select One or More Tests</form:label></td>
				<td><form:select path="tests" id="testResultReportTests">
						<form:option value="hiv" label="HIV" />
						<form:option value="hbv" label="HBV" />
						<form:option value="hcv" label="HCV" />
						<form:option value="syphilis" label="Syphilis" />
						<form:option value="none" label="None" />
					</form:select></td>
			</tr>
			<tr>
				<td>Enter Date Range</td>
			</tr>
			<tr>
				<td><form:input path="dateTestedFrom"
						id="trreportsDateTestedFrom" />&nbsp;to</td>
				<td><form:input path="dateTestedTo" id="trreportsDateTestedTo" /></td>
			</tr>
			<tr>
				<td />
			</tr>
			<tr>
				<td><form:label path="aggregationCriteria"> Aggregation Criteria </form:label></td>
				<td style="padding-left: 10px;"><form:select
						path="aggregationCriteria"
						id="testResultsReportFormAggregationCriteria">
						<form:option value="daily" label="Daily" selected="selected" />
						<form:option value="monthly" label="Monthly" selected="" />
						<form:option value="yearly" label="Yearly" selected="" />
					</form:select></td>
			</tr>
			<tr>
				<td />
				<td><button type="button" id="generateTestResultsReportButton"
						style="margin-left: 10px">Generate report</button></td>
			</tr>
		</tbody>
	</table>
</form:form>

<div id="testResultsReportResult"></div>
