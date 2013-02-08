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
<c:set var="editableFieldsForTableId">editableFieldsForTableId-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  function getWorksheetForTestResultsTable() {
    return $("#${mainContentId}").find(".worksheetForTestResultsTable");
  }

  console.log("${model.nextPageUrl}");

  var testResultsTable = getWorksheetForTestResultsTable().dataTable({
    "bJQueryUI" : false,
    "sDom" : '<rp>t<"F"ip>',
    "bServerSide" : true,
    "iDisplayLength" : 5,
    "bSort" : false,
    "sPaginationType" : "full_numbers",
    "sAjaxSource" : "${model.nextPageUrl}",
    "aoColumnDefs" : [{ "sClass" : "hide_class", "aTargets": [0]},
                      { "sClass" : "white_bkg_class", "aTargets": [1,2,3,4,5,6]}
    								 ],
    "fnServerData" : function (sSource, aoData, fnCallback, oSettings) {
											 console.log("here");
      								 oSettings.jqXHR = $.ajax({
      								   "datatype": "json",
      								   "type": "GET",
      								   "url": sSource,
      								   "data": aoData,
      								   "success": function(jsonResponse) {
      								     						makeRowsEditable(jsonResponse.aaData);
      								     						fnCallback(jsonResponse);
      								     						getWorksheetForTestResultsTable().find(".link").click(clearRadioButtonSelection);
      								     						registerRadioButtonCallbacks();
      								     						if (jsonResponse.iTotalRecords == 0) {
      								     						  $("#${mainContentId}").html($("#${noResultsFoundDivId}").html());
      								     						}
      								   						}
      								   });
      								 }
  });
  
  function makeRowsEditable(data) {
		for (var index in data) {
		  var row = data[index];
		  row[1] = getEditableCollectionNumber(row[1]);
		  row[2] = getEditableTestedOn(row[2]);
		  row[3] = getEditableBloodABOSelector(row[3]);
		  row[4] = getEditableBloodRhSelector(row[4]);
		  row[5] = getEditableHBVSelector(row[5]);
		  row[6] = getEditableHCVSelector(row[6]);
		  row[7] = getEditableHIVSelector(row[7]);
		  row[8] = getEditableSyphilisSelector(row[8]);
		}
  }

  function clearRadioButtonSelection(eventObj) {
    $(eventObj.target).parent().find('input[type="radio"]').prop('checked', false);
  }

  function getRowFromRadioButton(radioButton) {
    return radioButton.closest("tr");
  }

  function registerRadioButtonCallbacks() {
    getWorksheetForTestResultsTable().find('input[type="radio"]').click(
        function(eventObj) {
          var radioButton = $(eventObj.target);
      		var row = getRowFromRadioButton(radioButton);
      		var rowCells = $(row).find("td");
      		console.log($(rowCells[0]).html());
      		$(rowCells[1]).find(".savedStatusText").html("Unsaved");
    		});
  }

  function getEditableCollectionNumber(cell) {
    return '<div style="height: ${model.worksheetConfig.rowHeight}px; margin: 5px;">' + cell +
    					'<br /> <br />' +
    					'<span class="savedStatusText">Saved</span>'
    			 '</div>';
  }

  function getEditableTestedOn(cell) {
    var inputElement = '<input class="testedOn inlineInput" value="' + cell + '" />';
    var rowContents = '<div class="editableField testedOnEditableField">' + inputElement + '</div>';
		return rowContents;
  }

  function getEditableBloodABOSelector(cell) {
    var selectElement = $("#${editableFieldsForTableId}").find(".editableBloodABOField")[0].outerHTML;
    var rowContents = '<div class="editableField bloodABOEditableField">' + selectElement + '</div>';
		return rowContents;
  }
  
  function getEditableBloodRhSelector(cell) {
    var selectElement = $("#${editableFieldsForTableId}").find(".editableBloodRhField")[0].outerHTML;
    var rowContents = '<div class="editableField bloodABOEditableField">' + selectElement + '</div>';
		return rowContents;
  }

  function getEditableHBVSelector(cell) {
    var selectElement = $("#${editableFieldsForTableId}").find(".editableHBVField")[0].outerHTML;
    var rowContents = '<div class="editableField hbvEditableField">' + selectElement + '</div>';
		return rowContents;
  }

  function getEditableHCVSelector(cell) {
    var selectElement = $("#${editableFieldsForTableId}").find(".editableHCVField")[0].outerHTML;
    var rowContents = '<div class="editableField hcvEditableField">' + selectElement + '</div>';
		return rowContents;
  }

  function getEditableHIVSelector(cell) {
    var selectElement = $("#${editableFieldsForTableId}").find(".editableHIVField")[0].outerHTML;
    var rowContents = '<div class="editableField hivEditableField">' + selectElement + '</div>';
		return rowContents;
  }

  function getEditableSyphilisSelector(cell) {
    var selectElement = $("#${editableFieldsForTableId}").find(".editableSyphilisField")[0].outerHTML;
    var rowContents = '<div class="editableField syphilisEditableField">' + selectElement + '</div>';
		return rowContents;
  }

  function rowDeselectDisableEdit(node) {
    getWorksheetForTestResultsTable().find(".editableField").hide();
    $(node).find(".viewableField").show();
  }
  
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
			
				<table class="dataTable worksheetForTestResultsTable noHighlight">
					<thead>
						<tr>
								<th style="display: none"></th>
								<c:if test="${model.worksheetConfig['collectionNumber'] == 'true'}">
									<th style="width: 150px;">
										Collection Number
									</th>
								</c:if>

							  <c:if test="${model.worksheetConfig['testedOn'] == 'true'}">
									<th style="width: 100px;">
										Tested On
									</th>
								</c:if>

								<c:forEach var="bloodTest" items="${model.bloodTests}">
								  <c:if test="${model.worksheetConfig[bloodTest.name] == 'true'}">
										<th style="width: 170px;">
											${bloodTest.name}
										</th>
									</c:if>
								</c:forEach>

						</tr>
					</thead>
					<tbody style="font-size: 9pt;">
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

<div id="${editableFieldsForTableId}" style="display: none;">
	<c:forEach var="bloodTest" items="${model.bloodTests}">
		<div class="editable${fn:replace(bloodTest.name, ' ', '')}Field inlineTestResultSelection">
			<c:forEach var="allowedResult" items="${bloodTest.allowedResults}">
				<div>
					<input id="result-${bloodTest.name}${allowedResult}" type="radio"
				 				 name="Test${bloodTest.name}" value="${allowedResult}"
				 				 style="width: 20px;"></input>
	 			  <label for="result-${bloodTest.name}${allowedResult}"
	 			 	 		   style="width: 60px; margin-left: 0;
	 			 			   margin-right: 10px; cursor: pointer;">${allowedResult}</label>
			  </div>
			</c:forEach>
			<br />
			<span class="link clearSelection">Clear</span>
		</div>
	</c:forEach>
</div>
