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

  var barcodeDivs = $("#${mainContentId}").find(".collectionNumberBarcode");
  for (var index = 0; index < barcodeDivs.length; ++index) {
    var barcodeDiv = $(barcodeDivs[index]);
    showBarcode(barcodeDiv, barcodeDiv.data("collectionnumber"));
  }

  showBarcode($("#${mainContentId}").find(".worksheetNumberBarcode"), "${worksheet.worksheetNumber}");

});
</script>

<div id="${tabContentId}">
	<div id="${mainContentId}">
		<br />
		<br />

		<button class="printWorksheetButton">Print worksheet</button>

		<div class="printableArea">

			<div style="margin-top: 20px; margin-bottom: 20px; font-size: 18pt;">Worksheet Number: ${worksheet.worksheetNumber}</div>
			<div class="worksheetNumberBarcode"></div>

			<br/>
			<br/>

			<table class="dataTable worksheetTable">
				<thead>
					<tr>
							<c:if test="${worksheetConfig['collectionNumber'] == 'true'}">
								<th style="height: ${worksheetConfig.rowHeight}px; width: ${worksheetConfig.columnWidth}px;">
									Collection Number
								</th>
							</c:if>

							<c:forEach var="bloodTest" items="${bloodTests}">
								<th style="height: ${worksheetConfig.rowHeight}px; width: ${worksheetConfig.columnWidth}px;">
									${bloodTest.testNameShort}
								</th>
							</c:forEach>

					</tr>
				</thead>
				<tbody>
					<c:forEach var="collectedSample" items="${allCollectedSamples}">
						<tr>
						  <c:if test="${worksheetConfig['collectionNumber'] == 'true'}">
								<td style="height: ${worksheetConfig.rowHeight}px; width: ${worksheetConfig.columnWidth}px;">
									<div class="collectionNumberBarcode" data-collectionnumber="${collectedSample.collectionNumber}"></div>
								</td>
							</c:if>
							<c:forEach var="bloodTest" items="${bloodTests}">
								<td style="height: ${worksheetConfig.rowHeight}px; width: ${worksheetConfig.columnWidth}px;"></td>
							</c:forEach>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

	<div id="${childContentId}"></div>

</div>