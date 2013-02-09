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
    "bJQueryUI" : true,
    "sDom" : '<"H"lr>t<"F"irp>',
    "bServerSide" : true,
    "aLengthMenu": [1, 5, 10, 15, 25, 50, 100],
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
      								 },
    	"fnDrawCallback" : addSaveButtonToWorksheet
  });

  function addSaveButtonToWorksheet() {
	  $("#${mainContentId}").find(".worksheetSaveAndNextButton").button(
	      {
	        icons : {
	          primary: "ui-icon-disk"
	        }
	      }
	      ).click(saveAndNextButtonClicked);
	 }

  var modified_cells = {};

  function saveAndNextButtonClicked() {
  }
  
  function makeRowsEditable(data) {
		for (var index in data) {
		  var row = data[index];
		  var collectedSampleId = row[0];
		  row[1] = getEditableCollectionNumber(row[1], collectedSampleId);
		  row[2] = getEditableTestedOn(row[2], collectedSampleId);
		  row[3] = getEditableBloodABOSelector(row[3], collectedSampleId);
		  row[4] = getEditableBloodRhSelector(row[4], collectedSampleId);
		  row[5] = getEditableHBVSelector(row[5], collectedSampleId);
		  row[6] = getEditableHCVSelector(row[6], collectedSampleId);
		  row[7] = getEditableHIVSelector(row[7], collectedSampleId);
		  row[8] = getEditableSyphilisSelector(row[8], collectedSampleId);
		}
  }

  function clearRadioButtonSelection(eventObj) {
    $(eventObj.target).closest('tr').find('input[type="radio"]').prop('checked', false);
  }

  function getRowFromRadioButton(radioButton) {
    return radioButton.closest("tr");
  }

  function registerRadioButtonCallbacks() {
    getWorksheetForTestResultsTable().find('input[type="radio"]').click(
        function(eventObj) {
          var radioButton = $(eventObj.target);
      		var cell = radioButton.closest("td");
      		cell.css("background-color", "#c4d9e7");
      		var row = radioButton.closest("tr");
      		var rowCells = row.children();
      		var collectedSampleId = $(rowCells[0]).html();
      		var cellInput = cell.find("input:checked");
      		console.log(modified_cells);
      		modified_cells[collectedSampleId] = modified_cells[collectedSampleId] || {};
      		var testname = cellInput.data("testname");
      		var testvalue = cellInput.val();
      		modified_cells[collectedSampleId][testname] = testvalue;
      		console.log(modified_cells);
    		});
  }

  function getEditableCollectionNumber(cell, collectedSampleId) {
    return '<div style="height: ${model.worksheetConfig.rowHeight}px; margin: 5px;">' + cell +
    					'<br /> <br />' +
    					'<span class="link clearSelection">Clear test results</span>' +
    			 '</div>';
  }

  function getEditableTestedOn(cell, collectedSampleId) {
    var inputElement = '<input class="testedOn inlineInput" value="' + cell + '" />';
    var rowContents = '<div class="editableField testedOnEditableField">' + inputElement + '</div>';
		return rowContents;
  }

  function getEditableBloodABOSelector(cell, collectedSampleId) {
    var rowContents = $("#${editableFieldsForTableId}").find(".editableBloodABOField")[0].outerHTML;
    rowContents = rowContents.replace(/collectedSampleId/g, collectedSampleId);
		return rowContents;
  }
  
  function getEditableBloodRhSelector(cell, collectedSampleId) {
    var rowContents = $("#${editableFieldsForTableId}").find(".editableBloodRhField")[0].outerHTML;
    rowContents = rowContents.replace(/collectedSampleId/g, collectedSampleId);
		return rowContents;
  }

  function getEditableHBVSelector(cell, collectedSampleId) {
    var rowContents = $("#${editableFieldsForTableId}").find(".editableHBVField")[0].outerHTML;
    rowContents = rowContents.replace(/collectedSampleId/g, collectedSampleId);
		return rowContents;
  }

  function getEditableHCVSelector(cell, collectedSampleId) {
    var rowContents = $("#${editableFieldsForTableId}").find(".editableHCVField")[0].outerHTML;
    rowContents = rowContents.replace(/collectedSampleId/g, collectedSampleId);
		return rowContents;
  }

  function getEditableHIVSelector(cell, collectedSampleId) {
    var rowContents = $("#${editableFieldsForTableId}").find(".editableHIVField")[0].outerHTML;
    rowContents = rowContents.replace(/collectedSampleId/g, collectedSampleId);
		return rowContents;
  }

  function getEditableSyphilisSelector(cell, collectedSampleId) {
    var rowContents = $("#${editableFieldsForTableId}").find(".editableSyphilisField")[0].outerHTML;
    rowContents = rowContents.replace(/collectedSampleId/g, collectedSampleId);
		return rowContents;
  }

  function rowDeselectDisableEdit(node) {
    getWorksheetForTestResultsTable().find(".editableField").hide();
    rowContents = rowContents.replace(/collectedSampleId/g, collectedSampleId);
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
					<tbody style="font-size: 8pt;">
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
					<tfoot>
						<tr>
						<td colspan="9" align="right">
							<button class="worksheetSaveAndNextButton">Save and continue to next page</button>
						</td>
						</tr>
					</tfoot>
				</table>
			</div>
		</c:if>
	</div>
	<div id="${childContentId}"></div>
</div>

<div id="${noResultsFoundDivId}" style="display: none;">
	<span
		style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
		No worksheet found. </span>
</div>

<div id="${editableFieldsForTableId}" style="display: none;">
	<c:forEach var="bloodTest" items="${model.bloodTests}">
		<div class="editable${fn:replace(bloodTest.name, ' ', '')}Field">
			<c:set var="uniqueInputName" value="${fn:replace(bloodTest.name,' ','')}-collectedSampleId" />
			<c:forEach var="allowedResult" items="${bloodTest.allowedResults}">
				<div>
					<!-- using collected sample id as the name should be unique across multiple inputs.
							 otherwise selecting one radio button will change another radio button with the
							 same name.
					 -->

					<!-- nesting input element inside label element allows selection of input radiobutton by clicking on the label text.
							 this is nice from usability point of view.
					  -->
	 			  <label style="width: 60px; margin-left: 0;
	 			 			   margin-right: 10px; cursor: pointer;">
	 			 		<input type="radio"
				 		   	   name="${uniqueInputName}" value="${allowedResult}"
				 				   data-testname="${bloodTest.name}"
				 				   style="width: 20px;" />
	 			 			   ${allowedResult}
	 			 	</label>

			  </div>
			</c:forEach>
			<br />
		</div>
	</c:forEach>
</div>
