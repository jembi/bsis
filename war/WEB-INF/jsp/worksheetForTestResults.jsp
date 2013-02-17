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
<c:set var="saveConfirmDialogId">saveConfirmDialogId-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  var modified_cells = {};
  var original_data = {};
  var testnames = ["Blood ABO", "Blood Rh", "HBV", "HCV", "HIV", "Syphilis"];
  var buttonsAlreadyAdded = false;

  function getWorksheetForTestResultsTable() {
    return $("#${mainContentId}").find(".worksheetForTestResultsTable");
  }

  function isWorksheetModified() {
    console.log(modified_cells);
    for (var index in modified_cells) {
      var modifications = modified_cells[index];
      var modified = false;
      for (var index in modifications) {
        // at least one test was modified
        modified = true;
        break;
      }
			if (modified)
			  return true;
    }
    return false;
  }
  
  var testResultsTable = getWorksheetForTestResultsTable().dataTable({
    "bJQueryUI" : true,
    "sDom" : '<"H"lr>t<"F"irp>',
    "bServerSide" : true,
    "aLengthMenu": [1, 2, 5, 10, 50, 100],
    "iDisplayLength" : 2,
    "bSort" : false,
    "sPaginationType" : "full_numbers",
    "sAjaxSource" : "${model.nextPageUrl}",
    "aoColumnDefs" : [{ "sClass" : "hide_class", "aTargets": [0]},
                      { "sClass" : "white_bkg_class", "aTargets": [1,2,3,4,5,6]}
    								 ],
    "fnServerData" : function (sSource, aoData, fnCallback, oSettings) {

      								 if (isWorksheetModified()) {
      								   showUnsavedChangesDialog();
      								   return;
      								 }

      								 oSettings.jqXHR = $.ajax({
      								   "datatype": "json",
      								   "type": "GET",
      								   "url": sSource,
      								   "data": aoData,
      								   "success": function(jsonResponse) {
											     						resetWorksheetCurrentPageData();
      								     						makeRowsEditable(jsonResponse.aaData);
      								     						fnCallback(jsonResponse);
      								     						getWorksheetForTestResultsTable().find(".link").click(clearRadioButtonSelection);
      								     						registerRadioButtonCallbacks();
      								     						if (jsonResponse.iTotalRecords == 0) {
      								     						  $("#${mainContentId}").html($("#${noResultsFoundDivId}").html());
      								     						}
      								   						},
      								   	"error" : function(jsonResponse) {
																			showErrorMessage("Something went wrong. Please try again.");      								   	  	
      								   						}
      								   });
      								 },
    	"fnDrawCallback" : addButtonsToWorksheet
  });

  function resetWorksheetCurrentPageData() {
    modified_cells = {};
    original_data = {};
  }

  function addButtonsToWorksheet() {
    if (buttonsAlreadyAdded === true)
      return;

	  $("#${mainContentId}").find(".worksheetSaveAndNextButton").button(
	      { icons : { primary: "ui-icon-disk" } }).click(saveAndNextButtonClicked);
	  $("#${mainContentId}").find(".worksheetUndoChangesOnPageButton").button(
	      {
	        icons : {
	          primary: "ui-icon-arrowreturnthick-1-w"
	        }
	      }
	      ).click(undoChanges);
	  buttonsAlreadyAdded = true;
	 }

  function saveAndNextButtonClicked() {
    if (!isWorksheetModified())
      return;

    $.ajax({
      "url"  : "saveWorksheetTestResults.html", 
      "data" : {params: JSON.stringify(modified_cells), worksheetBatchId : "${model.worksheetBatchId}"},
      "type" : "POST",
      "success" : function() {
										showMessage("Test results saved successfully");
										resetWorksheetCurrentPageData();
										testResultsTable.fnStandingRedraw();
      					  },
    	"error" : function() {
    	  					showErrorMessage("Something went wrong. Please try again.");
    						}
    });
  }

  function undoChanges() {
    modified_cells = {};
		testResultsTable.fnStandingRedraw();
  }
  
  function makeRowsEditable(data) {
    original_data = {};
		for (var index in data) { // one row at a time
		  var row = data[index];
			original_data[row[0]] = {
			    "collectionNumber" : row[1],
			    "testedOn" : row[2],
			    "Blood ABO" : row[3],
			    "Blood Rh" : row[4],
			    "HBV" : row[5],
			    "HCV" : row[6],
			    "HIV" : row[7],
			    "Syphilis" : row[8]
			};
		  var collectedSampleId = row[0];	// each row has a hidden collectedsample id column
		  // server returns the value of the cells in order but we have replace the
		  // value by the relevant input elements. These DOM elements are generated below.
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
    var row = $(eventObj.target).closest('tr');
    row.find('input[type="radio"]').prop('checked', false);
    var collectedSampleId = $(row.find("td")[0]).html();

    console.log(modified_cells);
    for (var index in testnames) {
      var testname = testnames[index];
      var original_value = original_data[collectedSampleId][testname];

      // just find the first radio button within the row for the test name
		  var firstRadioButtonForTest = row.find('input[data-testname="' + testname + '"]:first');
      // find the table cell containing this radio button
      var tableCell = firstRadioButtonForTest.closest("td");

  		if (original_value === null || original_value === "") {
  		  // same as original, exclude from change set
  		  if (modified_cells[collectedSampleId] !== undefined && modified_cells[collectedSampleId][testname] !== undefined)
  		  	delete modified_cells[collectedSampleId][testname];
  			setOriginalColor(tableCell);
  		} else {
  		  // value changed
  			modified_cells[collectedSampleId] = modified_cells[collectedSampleId] || {};
  			modified_cells[collectedSampleId][testname] = "";
  			setModifiedColor(tableCell);
  		}
    }
    console.log(modified_cells);
  }

  function setModifiedColor(cell) {
    cell.css("background-color", "#c4d9e7");
  }

  function setOriginalColor(cell) {
    cell.css("background-color", "#ffffff");
  }
  
  function getRowFromRadioButton(radioButton) {
    return radioButton.closest("tr");
  }

  function showUnsavedChangesDialog() {
    $("#${saveConfirmDialogId}").dialog({
      resizable: false,
      height: 220,
      width: 500,
      title: "Unsaved changes",
      modal: true,
      buttons: {
        "Close" : function() {
										$(this).dialog("close");          
        					}
      }
    });
  }

  function registerRadioButtonCallbacks() {
    getWorksheetForTestResultsTable().find('input[type="radio"]').click(
        function(eventObj) {
          var radioButton = $(eventObj.target);
      		var cell = radioButton.closest("td");
      		var row = radioButton.closest("tr");
      		var rowCells = row.children();
      		var collectedSampleId = $(rowCells[0]).html();
      		var cellInput = cell.find("input:checked");
      		console.log(modified_cells);
      		modified_cells[collectedSampleId] = modified_cells[collectedSampleId] || {};
      		var testname = cellInput.data("testname");
      		var testvalue = cellInput.val();
      		if (testvalue === original_data[collectedSampleId][testname]) {
      		  // same as original, exclude from change set
      		  delete modified_cells[collectedSampleId][testname];
						cell.css("background-color", "#ffffff");
      		} else {
      		  // value changed
      			cell.css("background-color", "#c4d9e7");
      			modified_cells[collectedSampleId][testname] = testvalue;
      		}
      		console.log(modified_cells);
    		});
  }

  function getEditableCollectionNumber(cell, collectedSampleId) {
    return '<div style="height: ${model.worksheetConfig.rowHeight}px; margin: 5px;">' + cell +
    					'<br /> <br />' +
    					'<span class="link clearSelection">Clear</span>' +
    			 '</div>';
  }

  function getEditableTestedOn(cell, collectedSampleId) {
    // use current date for wokrsheet by default
    var testedOn = $.datepicker.formatDate("mm/dd/yy", new Date());
    if (cell !== null && cell !== undefined && cell !== "")
      testedOn = cell;
    var inputElement = '<input class="testedOn inlineInput" value="' + testedOn + '" />';
    var rowContents = '<div class="editableField testedOnEditableField">' + inputElement + '</div>';
		return rowContents;
  }

  function getEditableBloodABOSelector(cell, collectedSampleId) {
    var rowContents = $("#${editableFieldsForTableId}").find(".editableBloodABOField");
    rowContents = rowContents[0].outerHTML.replace(/collectedSampleId/g, collectedSampleId);
    rowContents = $(rowContents);
    if (cell !== null && cell !== undefined && cell !== "") {
      var radioButton = rowContents.find('input[data-testname="Blood ABO"][data-rowid="' + collectedSampleId + '"][data-allowedresult=' + cell + ']');
      radioButton.attr("checked", "checked");
    }
		return rowContents[0].outerHTML;
  }
  
  function getEditableBloodRhSelector(cell, collectedSampleId) {
    var rowContents = $("#${editableFieldsForTableId}").find(".editableBloodRhField");
    rowContents = rowContents[0].outerHTML.replace(/collectedSampleId/g, collectedSampleId);
    rowContents = $(rowContents);
    if (cell !== null && cell !== undefined && cell !== "") {
      var radioButton = rowContents.find('input[data-testname="Blood Rh"][data-rowid="' + collectedSampleId + '"][data-allowedresult=' + cell + ']');
      radioButton.attr("checked", "checked");
    }
		return rowContents[0].outerHTML;
  }

  function getEditableHBVSelector(cell, collectedSampleId) {
    var rowContents = $("#${editableFieldsForTableId}").find(".editableHBVField");
    rowContents = rowContents[0].outerHTML.replace(/collectedSampleId/g, collectedSampleId);
    rowContents = $(rowContents);
    if (cell !== null && cell !== undefined && cell !== "") {
      var radioButton = rowContents.find('input[data-testname="HBV"][data-rowid="' + collectedSampleId + '"][data-allowedresult=' + cell + ']');
      radioButton.attr("checked", "checked");
    }
		return rowContents[0].outerHTML;
  }

  function getEditableHCVSelector(cell, collectedSampleId) {
    var rowContents = $("#${editableFieldsForTableId}").find(".editableHCVField");
    rowContents = rowContents[0].outerHTML.replace(/collectedSampleId/g, collectedSampleId);
    rowContents = $(rowContents);
    if (cell !== null && cell !== undefined && cell !== "") {
      var radioButton = rowContents.find('input[data-testname="HCV"][data-rowid="' + collectedSampleId + '"][data-allowedresult=' + cell + ']');
      radioButton.attr("checked", "checked");
    }
		return rowContents[0].outerHTML;
  }

  function getEditableHIVSelector(cell, collectedSampleId) {
    var rowContents = $("#${editableFieldsForTableId}").find(".editableHIVField");
    rowContents = rowContents[0].outerHTML.replace(/collectedSampleId/g, collectedSampleId);
    rowContents = $(rowContents);
    if (cell !== null && cell !== undefined && cell !== "") {
      var radioButton = rowContents.find('input[data-testname="HIV"][data-rowid="' + collectedSampleId + '"][data-allowedresult=' + cell + ']');
      radioButton.attr("checked", "checked");
    }
		return rowContents[0].outerHTML;
  }

  function getEditableSyphilisSelector(cell, collectedSampleId) {
    var rowContents = $("#${editableFieldsForTableId}").find(".editableSyphilisField");
    rowContents = rowContents[0].outerHTML.replace(/collectedSampleId/g, collectedSampleId);
    rowContents = $(rowContents);
    if (cell !== null && cell !== undefined && cell !== "") {
      var radioButton = rowContents.find('input[data-testname="Syphilis"][data-rowid="' + collectedSampleId + '"][data-allowedresult=' + cell + ']');
      radioButton.attr("checked", "checked");
    }
		return rowContents[0].outerHTML;
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
						<tbody style="font-size: 11pt;">
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
								<button class="worksheetSaveAndNextButton">Save</button>
								<button class="worksheetUndoChangesOnPageButton">Undo changes on this page</button>
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
		<div class="editable${fn:replace(bloodTest.name, ' ', '')}Field editableField">
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
	 			  <label style="margin-left: 2px;
	 			 			   margin-right: 0; cursor: pointer;">
	 			 		<input type="radio"
				 		   	   name="${uniqueInputName}" value="${allowedResult}"
				 				   data-testname="${bloodTest.name}"
				 				   data-allowedresult="${allowedResult}"
				 				   data-rowid="collectedSampleId"
				 				   style="width: 10px; margin-left: 0; margin-right: 0;" />
	 			 			   ${allowedResult}
	 			 	</label>

			  </div>
			</c:forEach>
			<br />
		</div>
	</c:forEach>
</div>

<div id="${saveConfirmDialogId}" style="display: none;">
  <p>
  	<span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>
  	There are unsaved changes on the worksheet. Please save this page before continuing to the next page.
  </p>
</div>
