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
  $("#generateCollectionsReportButton").button().click(function() {
    var formData = $("#collectionsReportForm").serialize();
    console.log(formData);
    $.ajax({
      type : "GET",
      url : "getCollectionsReport.html",
      data : formData,
      success : function(data) {
        console.log(data);
        var chart = getTimeChart({
          data : data.numCollections,
          renderDest : "collectionsReportResult",
          title : "Collections Report",
          hoverText : "Collections",
          yAxisTitle : "No. of Collections",
          startTime : data.dateCollectedFromUTC,
          endTime : data.dateCollectedToUTC,
          interval : 24 * 3600 * 1000
        });
      }
    });
  });
</script>

<form:form method="GET" commandName="collectionsReportForm"
	id="collectionsReportForm">
	<table>
		<thead>
		</thead>
		<tbody>
			<tr>
				<td>Enter Date Range</td>
			</tr>
			<tr>
				<td><form:input path="dateCollectedFrom"
						id="creportsDateCollectedFrom" />&nbsp;and</td>
				<td><form:input path="dateCollectedTo"
						id="creportsDateCollectedTo" /></td>
			</tr>
			<tr>
				<td><input type="button" value="Generate Report"
					id="generateCollectionsReportButton" /></td>
			</tr>
		</tbody>
	</table>
</form:form>

<div id="collectionsReportResult"></div>
