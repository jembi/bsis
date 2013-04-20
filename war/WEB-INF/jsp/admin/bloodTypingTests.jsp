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

<script>
$(document).ready(function() {

  var selectedRowId = undefined;
  
	var bloodTypingTable = $("#${mainContentId}").find(".bloodTypingTestsTable").dataTable({
    "bJQueryUI" : true,
    "sDom" : '<"H"lrC>t<"F"i>',
    "aaSorting" : [],
    "sScrollX": "100%",
    "bPaginate" : false,
    "bSort" : false,
      "oColVis" : {
       	"aiExclude": [0,1],
      }
	});

	new FixedColumns(bloodTypingTable, {"iLeftColumns" : 1, "iRightColumns" : 1});

	$("#${mainContentId}").find(".bloodTypingTestsTable")
												.find("tbody tr")
												.click(bloodTypingRowClicked);

	function bloodTypingRowClicked() {
	  var ruleIdElement = $(this).find(".bloodTypingRuleId");
	  if (ruleIdElement === undefined) {
	    return;
	  }

	  if (selectedRowId !== undefined) {
	    $(this).closest("table")
	    			 .find("tr")
	    			 .each(function() {
	      						 $(this).css("background", "");
	      					 });
	  }

	  var clickedRowId = ruleIdElement[0].innerHTML;
	  if (clickedRowId === selectedRowId) {
	    $("#${mainContentId}").find(".ruleSummarySection").html("");
	    selectedRowId = undefined;
	    return;
	  }

	  $(this).css("background", "#9BB5AA");
	  selectedRowId = clickedRowId;
	  $.ajax({
	    url: "bloodTypingRuleSummary.html",
	    type: "GET",
	    data: {bloodTypingRuleId : selectedRowId},
	    success: function(response) {
	      				 var ruleSummarySection = $("#${mainContentId}").find(".ruleSummarySection");
	      			   animatedScrollTo(ruleSummarySection);
	      			   ruleSummarySection.html(response);
	    				 },
	    error: function(response) {
	      			 showErrorMessage("Something went wrong. Please try again.");
	    			 }
	  })
	}

	$("#${tabContentId}").bind("bloodTypingRuleEditDone", refetchContent);
	$("#${tabContentId}").bind("bloodTypingRuleEditError", refetchContent);

	function refetchContent() {
		$("#${tabContentId}").load("${refreshUrl}");
	}
});
</script>

<div id="${tabContentId}">

	<div id="${mainContentId}">

		<div style="margin-left: 20px; margin-right: 20px; margin-top: 50px; border-radius: 5px;">

			<div style="font-weight: bold; margin: 15px;">Blood Typing tests</div>

			<table class="bloodTypingTestsTable">	
				<thead>
					<tr style="height: 30px;">
						<th>Rule #</th>
						<!--  column for rule id should be hidden. It cannot be part of the fixed columns as
									fixed columns form a separate table.
						  -->
						<th style="display: none;"></th>
						<c:forEach var="bloodTypingTest" items="${bloodTypingTests}">
							<th style="width: 20px;">${bloodTypingTest.testName}</th>
						</c:forEach>
						<th style="width: 300px;">
							Result
						</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="bloodTypingRule" items="${bloodTypingRules}">
						<tr style="cursor: pointer; height: 30px;">
							<td style="text-align: center;">${bloodTypingRule.id}</td>
							<td style="display: none;" class="bloodTypingRuleId">${bloodTypingRule.id}</td>
							<c:set var="ruleNum" value="${ruleNum + 1}" />
							<c:forEach var="bloodTypingTest" items="${bloodTypingTests}">
								<td style="text-align: center; width: 20px;">
								<c:if test="${not empty bloodTypingRule.patternMap[bloodTypingTest.id]}">
									${bloodTypingRule.patternMap[bloodTypingTest.id]}
								</c:if>
								</td>
							</c:forEach>
							<td style="width: 300px; text-align: center;">
								<c:if test="${not empty bloodTypingRule.newInformation}">
									<div>
										<span style="text-align:left; display: inline-block; width: 45%; margin-left: 10px;">
											${bloodTypingRule.collectionFieldChanged}
										</span>
										<span style="text-align: right; display: inline-block; width: 45%; margin-right: 10px;">
											${bloodTypingRule.newInformation}
										</span>
									</div>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<div class="ruleSummarySection">
			</div>

		</div>

	</div>

	<div id="${childContentId}">
	</div>

</div>