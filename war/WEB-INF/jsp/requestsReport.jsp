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
<c:set var="requestsReportBloodGroupSelectorId">requestsReportBloodGroupSelector-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
  $("#requestreportsDateRequestedFrom").datepicker(
      {
        changeMonth : true,
        changeYear : true,
        minDate : -36500,
        maxDate : 0,
        dateFormat : "dd/mm/yy",
        yearRange : "c-100:c0",
        onSelect : function(selectedDate) {
          $("#requestreportsDateRequestedTo").datepicker("option", "minDate",
              selectedDate);
        }
      });

  var lastYear = new Date();
  lastYear.setFullYear(lastYear.getFullYear()-1);
  $("#requestreportsDateRequestedFrom").datepicker("setDate", lastYear);
  
  $("#requestreportsDateRequestedTo").datepicker(
      {
        changeMonth : true,
        changeYear : true,
        minDate : -36500,
        maxDate : 0,
        dateFormat : "dd/mm/yy",
        yearRange : "c-100:c0",
        onSelect : function(selectedDate) {
          $("#requestreportsDateRequestedFrom").datepicker("option", "maxDate",
              selectedDate);
        }
      });

  $("#requestsReportFormAggregationCriteria").multiselect({
    multiple : false,
    selectedList : 1,
    header : false
  });

  function getBloodGroupSelector() {
    return $("#${requestsReportBloodGroupSelectorId}");
  }

  getBloodGroupSelector().multiselect({
	  position : {
	    my : 'left top',
	    at : 'right center'
	  },
	  minWidth: 250,
	  noneSelectedText: 'None selected',
	  selectedText: function(numSelected, numTotal, selectedValues) {
	    							if (numSelected == numTotal) {
	    							  return "All Blood Groups";
	    							}
										  var checkedValues = $.map(selectedValues, function(input) { return input.title; });
										  return checkedValues.length ? checkedValues.join(', ') : 'None Selected';
	  							}
	});

  getBloodGroupSelector().multiselect("checkAll");

  $("#generateRequestsReportButton").button({
    icons : {
      primary : 'ui-icon-print'
    }
  }).click(function() {
    var formData = $("#requestsReportForm").serialize();
    $.ajax({
      type : "GET",
      url : "getRequestsReport.html",
      data : formData,
      success : function(data) {
        getRequestsChart({
          data : data.numRequests,
          renderDest : "requestsReportResult",
          title : "Requests Report",
          hoverText : "Requests",
          yAxisTitle : "No. of Requests",
          startTime : data.dateRequestedFromUTC,
          endTime : data.dateRequestedToUTC,
          interval : data.interval
        });
      }
    });
  });

  $("#clearRequestsReportButton").button({
    icons: {
      primary: 'ui-icon-grip-solid-horizontal'
    }
  }).click(function() {
    $("#requestsReportResult").html("");
  });

  $("#requestsReportForm").find(".collectionCenterSelector").multiselect({
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

  $("#requestsReportForm").find(".collectionCenterSelector").multiselect("checkAll");

  $("#requestsReportForm").find(".collectionSiteSelector").multiselect({
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
									  console.log(selectedValues);
									  var checkedValues = $.map(selectedValues, function(input) { return input.title; });
									  return checkedValues.length ? checkedValues.join(', ') : 'Any Site';
									}
	  }
	});

  $("#requestsReportForm").find(".collectionSiteSelector").multiselect("checkAll");
});
</script>

<form:form method="GET" commandName="requestsReportForm"
	id="requestsReportForm">
		<br/>
		<div class="tipsBox ui-state-highlight">
			<p>
				${model['report.requests.requestsreport']}
			</p>
		</div>
	<table>
		<thead>
		</thead>
		<tbody>
			<tr>
				<td>Enter Date Range</td>
			</tr>
			<tr>
				<td><form:input path="dateRequestedFrom"
						id="requestreportsDateRequestedFrom" placeholder="From Date" />&nbsp;to</td>
				<td><form:input path="dateRequestedTo"
						id="requestreportsDateRequestedTo" placeholder="To Date" /></td>
			</tr>
			<tr>
				<td />
			</tr>
			<tr>
				<td>
					<form:label path="sites">Request Sites</form:label>
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
				<td><form:label path="aggregationCriteria"> Aggregation Criteria </form:label></td>
				<td style="padding-left: 10px;"><form:select
						path="aggregationCriteria"
						id="requestsReportFormAggregationCriteria">
						<form:option value="daily" label="Daily" selected="" />
						<form:option value="monthly" label="Monthly" selected="selected" />
						<form:option value="yearly" label="Yearly" selected="" />
					</form:select></td>
			</tr>
			<tr>
				<td><form:label path="bloodGroups">Blood Groups</form:label></td>
				<td style="padding-left: 10px;">
					<form:select id="${requestsReportBloodGroupSelectorId}" path="bloodGroups">
						<form:option value="A+">A+</form:option>
						<form:option value="B+">B+</form:option>
						<form:option value="AB+">AB+</form:option>
						<form:option value="O+">O+</form:option>
						<form:option value="A-">A-</form:option>
						<form:option value="B-">B-</form:option>
						<form:option value="AB-">AB-</form:option>
						<form:option value="O-">O-</form:option>
					</form:select>
				</td>
			</tr>
			<tr>
				<td />
				<td><button type="button" id="generateRequestsReportButton"
						style="margin-left: 10px">Generate report</button>
						<button type="button" id="clearRequestsReportButton"
						style="margin-left: 10px">Clear report</button>
				</td>
			</tr>
		</tbody>
	</table>
</form:form>

<div id="requestsReportResult" style="margin-right: 10px; margin-left: 10px; width: 90%;"></div>
