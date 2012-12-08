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
  $("#creportsDateCollectedFrom").datepicker(
      {
        changeMonth : true,
        changeYear : true,
        minDate : -36500,
        maxDate : 0,
        dateFormat : "mm/dd/yy",
        yearRange : "c-100:c0",
        onSelect : function(selectedDate) {
          $("#creportsDateCollectedTo").datepicker("option", "minDate",
              selectedDate);
        }
      });

  $("#creportsDateCollectedTo").datepicker(
      {
        changeMonth : true,
        changeYear : true,
        minDate : -36500,
        maxDate : 0,
        dateFormat : "mm/dd/yy",
        yearRange : "c-100:c0",
        onSelect : function(selectedDate) {
          $("#creportsDateCollectedFrom").datepicker("option", "maxDate",
              selectedDate);
        }
      });

  $("#collectionsReportFormAggregationCriteria").multiselect({
    multiple : false,
    selectedList : 1,
    header : false
  });

  $("#generateCollectionsReportButton").button({
    icons : {
      primary : 'ui-icon-print'
    }
  }).click(function() {
    var formData = $("#collectionsReportForm").serialize();
    $.ajax({
      type : "GET",
      url : "getCollectionsReport.html",
      data : formData,
      success : function(data) {
        getTimeChart({
          data : data.numCollections,
          renderDest : "collectionsReportResult",
          title : "Collections Report",
          hoverText : "Collections",
          yAxisTitle : "No. of Collections",
          startTime : data.dateCollectedFromUTC,
          endTime : data.dateCollectedToUTC,
          interval : data.interval
        });
      }
    });
  });
</script>

<form:form method="GET" commandName="collectionsReportForm"
	id="collectionsReportForm">
	Under Construction
	<table>
		<thead>
		</thead>
		<tbody>
			<tr>
				<td>Enter Date Range</td>
			</tr>
			<tr>
				<td><form:input path="dateCollectedFrom"
						id="creportsDateCollectedFrom" />&nbsp;to</td>
				<td><form:input path="dateCollectedTo"
						id="creportsDateCollectedTo" /></td>
			</tr>
			<tr>
				<td />
			</tr>
			<tr>
				<td><form:label path="aggregationCriteria"> Aggregation Criteria </form:label></td>
				<td style="padding-left: 10px;"><form:select
						path="aggregationCriteria"
						id="collectionsReportFormAggregationCriteria">
						<form:option value="daily" label="Daily" selected="selected" />
						<form:option value="monthly" label="Monthly" selected="" />
						<form:option value="yearly" label="Yearly" selected="" />
					</form:select></td>
			</tr>
			<tr>
				<td />
				<td><button type="button" id="generateCollectionsReportButton"
						style="margin-left: 10px">Generate report</button></td>
			</tr>
		</tbody>
	</table>
</form:form>

<div id="collectionsReportResult"></div>
