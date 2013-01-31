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

  $("#${mainContentId}").find(".printWorksheetButton").button({
    icons : {
      primary : 'ui-icon-print'
    }
  }).click(function() { 
    $("#${mainContentId}").find(".printableArea").printArea();
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

			<button class="printWorksheetButton">Print worksheet</button>

			<div class="printableArea">

				<div style="margin-top: 20px; margin-bottom: 20px; font-size: 18pt;">Worksheet ID: ${model.worksheetBatchId}</div>
			
				<table class="dataTable worksheetTable">
					<thead>
						<tr>
								<c:if test="${model.worksheetConfig['collectionNumber'] == 'true'}">
									<th style="height: ${model.worksheetConfig.rowHeight}px; width: ${model.worksheetConfig.columnWidth}px;">
										Collection Number
									</th>
								</c:if>

							  <c:if test="${model.worksheetConfig['testedOn'] == 'true'}">
									<th style="height: ${model.worksheetConfig.rowHeight}px; width: ${model.worksheetConfig.columnWidth}px;">
										Tested On
									</th>
								</c:if>

								<c:forEach var="bloodTest" items="${model.bloodTests}">
								  <c:if test="${model.worksheetConfig[bloodTest.name] == 'true'}">
										<th style="height: ${model.worksheetConfig.rowHeight}px; width: ${model.worksheetConfig.columnWidth}px;">
											${bloodTest.name}
										</th>
									</c:if>
								</c:forEach>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="collectedSample" items="${model.allCollectedSamples}">
							<tr>
							  <c:if test="${model.worksheetConfig['collectionNumber'] == 'true'}">
									<td style="height: ${model.worksheetConfig.rowHeight}px; width: ${model.worksheetConfig.columnWidth}px;">
										${collectedSample.collectionNumber}
									</td>
								</c:if>
							  <c:if test="${model.worksheetConfig['testedOn'] == 'true'}">
									<td style="height: ${model.worksheetConfig.rowHeight}px; width: ${model.worksheetConfig.columnWidth}px;"></td>
							  </c:if>
								<c:forEach var="bloodTest" items="${model.bloodTests}">
								  <c:if test="${model.worksheetConfig[bloodTest.name] == 'true'}">
										<td style="height: ${model.worksheetConfig.rowHeight}px; width: ${model.worksheetConfig.columnWidth}px;"></td>
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