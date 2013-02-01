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
<c:set var="noResultsFoundDivId">noResultsFoundDivId-${unique_page_id}</c:set>


<script>
$(document).ready(function() {

  function getWorksheetForTestResultsTable() {
    return $("#${mainContentId}").find(".worksheetForTestResultsTable");
  }

  console.log("${model.nextPageUrl}");

  var testResultsTable = getWorksheetForTestResultsTable().dataTable({
    "bJQueryUI" : true,
    "sDom" : '<"H"lrT>t<"F"ip>T',
    "bServerSide" : true,
    "bSort" : false,
    "sAjaxSource" : "${model.nextPageUrl}",
    "aoColumnDefs" : [{ "sClass" : "hide_class", "aTargets": [0]},
    								 ],
    "fnServerData" : function (sSource, aoData, fnCallback, oSettings) {
      								 oSettings.jqXHR = $.ajax({
      								   "datatype": "json",
      								   "type": "GET",
      								   "url": sSource,
      								   "data": aoData,
      								   "success": function(jsonResponse) {
      								     						if (jsonResponse.iTotalRecords == 0) {
      								     						  $("#${mainContentId}").html($("#${noResultsFoundDivId}").html());
      								     						}
      								     						fnCallback(jsonResponse);
      								   						}
      								   });
      								 },
    "oTableTools" : {
      "sRowSelect" : "single",
      "aButtons" : [],
      "fnRowSelected" : function(node) {
													$("#${mainContentId}").parent().trigger("collectionSummaryView");
									        var elements = $(node).children();
									        if (elements[0].getAttribute("class") === "dataTables_empty") {
									          return;
									        }
									        selectedRowId = elements[0].innerHTML;
												  },
			"fnRowDeselected" : function(node) {
													},
    },
  });

});
</script>

<div id="${tabContentId}">
	<div id="${mainContentId}">
		<c:if test="${!model.worksheetFound}">
			<span style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
					No worksheet found.
			</span>
		</c:if>
		<c:if test="${model.worksheetFound}">
		
			<br />
			<br />

			<div class="printableArea">

				<div style="margin-top: 20px; margin-bottom: 20px; font-size: 18pt;">Worksheet ID: ${model.worksheetBatchId}</div>
			
				<table class="dataTable worksheetForTestResultsTable">
					<thead>
						<tr>
								<th style="display: none"></th>
								<c:if test="${model.worksheetConfig['collectionNumber'] == 'true'}">
									<th>
										Collection Number
									</th>
								</c:if>

							  <c:if test="${model.worksheetConfig['testedOn'] == 'true'}">
									<th>
										Tested On
									</th>
								</c:if>

								<c:forEach var="bloodTest" items="${model.bloodTests}">
								  <c:if test="${model.worksheetConfig[bloodTest.name] == 'true'}">
										<th>
											${bloodTest.name}
										</th>
									</c:if>
								</c:forEach>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="collectedSample" items="${model.allCollectedSamples}">
							<tr>
								<td style="display: none">${collectedSample.id}</td>
							  <c:if test="${model.worksheetConfig['collectionNumber'] == 'true'}">
									<td>
										${collectedSample.collectionNumber}
									</td>
								</c:if>
							  <c:if test="${model.worksheetConfig['testedOn'] == 'true'}">
									<td></td>
							  </c:if>
								<c:forEach var="bloodTest" items="${model.bloodTests}">
								  <c:if test="${model.worksheetConfig[bloodTest.name] == 'true'}">
										<td></td>
									</c:if>
								</c:forEach>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</c:if>
	</div>
	<div id="${childContentId}"></div>
</div>

<div id="${noResultsFoundDivId}" style="display: none;">
	<span
		style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
		Sorry no results found matching your search request </span>
</div>
