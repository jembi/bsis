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
<c:set var="collectionsReportBloodGroupSelectorId">collectionsReportBloodGroupSelector-${unique_page_id}</c:set>


<script>
$(document).ready(function() {
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

  var firstDayOfYear = new Date();
	firstDayOfYear.setDate(1);
	firstDayOfYear.setMonth(0);
  $("#creportsDateCollectedFrom").datepicker("setDate", firstDayOfYear);
  
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

  function getBloodGroupSelector() {
    return $("#${collectionsReportBloodGroupSelectorId}");
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
        getCollectionsChart({
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

  $("#clearCollectionsReportButton").button({
    icons: {
      primary: 'ui-icon-grip-solid-horizontal'
    }
  }).click(function() {
    $("#collectionsReportResult").html("");
  });

  $("#collectionsReportForm").find(".collectionCenterSelector").multiselect({
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

  $("#collectionsReportForm").find(".collectionCenterSelector").multiselect("checkAll");

  $("#collectionsReportForm").find(".collectionSiteSelector").multiselect({
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

  $("#collectionsReportForm").find(".collectionSiteSelector").multiselect("checkAll");
});
</script>

<form:form method="GET" commandName="collectionsReportForm"
	id="collectionsReportForm">
		<br/>
		<div class="tipsBox ui-state-highlight">
			<p>
				${model['report.collections.collectionsreport']}
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
				<td><form:input path="dateCollectedFrom"
						id="creportsDateCollectedFrom" placeholder="From Date" />&nbsp;to</td>
				<td><form:input path="dateCollectedTo"
						id="creportsDateCollectedTo" placeholder="To Date" /></td>
			</tr>
			<tr>
				<td />
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
				<td><form:label path="bloodGroups">Blood Groups</form:label></td>
				<td style="padding-left: 10px;">
					<form:select id="${collectionsReportBloodGroupSelectorId}" path="bloodGroups">
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
				<td><button type="button" id="generateCollectionsReportButton"
						style="margin-left: 10px">Generate report</button>
						<button type="button" id="clearCollectionsReportButton"
						style="margin-left: 10px">Clear report</button>
				</td>
			</tr>
		</tbody>
	</table>
</form:form>

<div id="collectionsReportResult" style="margin-right: 10px; margin-left: 10px; width: 90%;"></div>
