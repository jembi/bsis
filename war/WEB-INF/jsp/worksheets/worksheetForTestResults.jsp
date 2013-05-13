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

<c:set var="UninterpretableResultsDialogId">uninterpretableResultsDialog-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  var modified_cells = {};
  var original_data = {};
  var testids = "${testIdsCommaSeparated}".split(",");
  var buttonsAlreadyAdded = false;

  function getWorksheetForTestResultsTable() {
    return $("#${mainContentId}").find(".worksheetForTestResultsTable");
  }

  function isWorksheetModified() {
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
    "aLengthMenu": [1, 2, 5, 10, 50, 100],  // number of collections on each page
    "iDisplayLength" : 2,
    "bSort" : false,
    "sPaginationType" : "full_numbers",
    "sAjaxSource" : "${nextPageUrl}",
    "aoColumnDefs" : [{ "sClass" : "hide_class", "aTargets": [0]}],
    "fnServerData" : function (sSource, aoData, fnCallback, oSettings) {

                       if (isWorksheetModified()) {
                         // if unsaved changes then prompt user to save changes
                         showUnsavedChangesDialog();
                         return;
                       }
                       oSettings.jqXHR = $.ajax({
                         "datatype": "json",
                         "type": "GET",
                         "url": sSource,
                         "data": aoData,
                         "success": function(jsonResponse) {
                                      console.log("${collectionsMap}");                                       
                                       resetWorksheetCurrentPageData();
                                       makeRowsEditable(jsonResponse.aaData);
                                       fnCallback(jsonResponse);
                                       getWorksheetForTestResultsTable().find(".link").click(clearRadioButtonSelection);
                                       registerRadioButtonCallbacks();
                                       setOriginalColorForAllInputs();
                                       if (jsonResponse.iTotalRecords == 0) {
                                         //$("#${mainContentId}").html($("#${noResultsFoundDivId}").html());
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
        { icons : { primary: "ui-icon-disk" } }).click(saveTestResultsOnWorksheetPage);
    $("#${mainContentId}").find(".worksheetUndoChangesOnPageButton").button(
        {
          icons : {
            primary: "ui-icon-arrowreturnthick-1-w"
          }
        }
        ).click(undoChanges);
    buttonsAlreadyAdded = true;
   }

    function showUninterpretableConfirmationDialog() {
     $("#${UninterpretableResultsDialogId}").dialog({
       modal: true,
       height: 200,
       width: 600,
       title: "Confirm save",
       buttons: {
         "Save uninterpretable results" : function(event) {
                                             $(this).dialog("close");
                                             saveTestResultsOnWorksheetPage(event, true);
                                           },
         "Cancel and review results" : function() {
                                           $(this).dialog("close");
                                        }
       }
     });
   }

  function saveTestResultsOnWorksheetPage(event, saveUninterpretableResults) {
    if (!isWorksheetModified())
      return;

    if (saveUninterpretableResults === undefined)
      saveUninterpretableResults = false;

    $.ajax({
      "url"  : "saveAllTestResults.html", 
      "data" : {saveTestsData: JSON.stringify(modified_cells),
                saveUninterpretableResults: saveUninterpretableResults},
      "type" : "POST",
      "success" : function() {
                    showMessage("Test results saved successfully");
                    resetWorksheetCurrentPageData();
                    testResultsTable.fnStandingRedraw();
                  },
      "error" : function() {
                  showUninterpretableConfirmationDialog();
                }
    });
  }

  function undoChanges() {
    modified_cells = {};
    testResultsTable.fnStandingRedraw();
  }

  // modify the text data passed by the server to show cells with input radio buttons
  function makeRowsEditable(data) {
    original_data = {};
    for (var index in data) { // one row at a time
      var row = data[index];
      var collectionId = row[0];  // each row has a hidden collectedsample id column

      original_data[collectionId] = {
          "collectionNumber" : row[1]
      };

      for (var testidIndex = 0; testidIndex < testids.length; ++testidIndex) {
        var testid = testids[testidIndex];
         // first two columns skipped for collection id and collection number
        original_data[collectionId][testid] = row[testidIndex+2];
      }

      row[1] = getEditableCollectionInformation(row[1]);
      // server returns the value of the cells in order but we have replace the
      // value by the relevant input elements. These DOM elements are generated below.
      for (var testidIndex = 0; testidIndex < testids.length; ++testidIndex) {
        var testid = testids[testidIndex];
        // first two columns skipped for collection id and collection number
        row[testidIndex+2] = getEditableTestSelector(row[testidIndex+2], collectionId, testid);
      }
    }
  }

  function clearRadioButtonSelection(eventObj) {
    // uncheck all radio buttons in the same row
    var row = $(eventObj.target).closest('tr');
    row.find('input[type="radio"]').prop('checked', false);

    var collectionId = $(row.find("td")[0]).html();
    // recolor all cells in the row to make sure modified cells
    // are distinguishable from the original ones
    for (var index in testids) {
      var testid = testids[index];
      var original_value = original_data[collectionId][testid];

      // just find the first radio button within the row for the test name
      var firstRadioButtonForTest = row.find('input[data-testid="' + testid + '"]:first');
      // find the table cell containing this radio button
      var tableCell = firstRadioButtonForTest.closest("td");

      if (original_value === undefined || original_value === "") {
        // same as original, exclude from change set
        if (modified_cells[collectionId] !== undefined && modified_cells[collectionId][testid] !== undefined)
          delete modified_cells[collectionId][testid];
        setOriginalColor(collectionId, testid, tableCell);
      } else {
        // value changed
        modified_cells[collectionId] = modified_cells[collectionId] || {};
        modified_cells[collectionId][testid] = "";
        setModifiedColor(tableCell);
      }
    }
  }

  function setModifiedColor(cell) {
    cell.css("background-color", "#c4d9e7");
    cell.css("color", "black");
  }

  function setOriginalColor(collectionId, testId, cell) {
    if (original_data[collectionId][testId] !== "") {
      cell.css("background", "#00642C");
      cell.css("color", "white");
    }
    else {
      cell.css("background", "#ffffff");
      cell.css("color", "black");
    }
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
          // collection id modified
          var collectionId = $(rowCells[0]).html();
          var cellInput = cell.find("input:checked");
          modified_cells[collectionId] = modified_cells[collectionId] || {};
          var testid = cellInput.data("testid");
          var testvalue = cellInput.val();
          if (testvalue === original_data[collectionId][testid]) {
            // same as original, exclude from change set
            // this is how a modified cell can go to unmodified state
            delete modified_cells[collectionId][testid];
            setOriginalColor(collectionId, testid, cell);
          } else {
            // value changed
            // mark cell as modified
            cell.css("background-color", "#c4d9e7");
            cell.css("color", "black");
            modified_cells[collectionId][testid] = testvalue;
          }
        });
  }

  function setOriginalColorForAllInputs() {
    getWorksheetForTestResultsTable().find('input[type="radio"]')
                                     .each(function() {
                                       setOriginalColor($(this).data("rowid"), $(this).data("testid"), $(this).closest("td"));
                                     });
  }

  function getEditableCollectionInformation(cell) {
    var collectionInfo = JSON.parse(cell);
    var cellContents = '<div style="margin-left: 2px; font-size: 1.2em;">' + collectionInfo.collectionNumber +
              '<br /> <br/>';
    if (collectionInfo.bloodAbo !== undefined) {
      cellContents = cellContents + '<span style="font-size: 11px; color: #7A7A7A;">Blood ABO: ' + collectionInfo.bloodAbo + '</span><br />';
    }
    if (collectionInfo.bloodRh !== undefined) {
      cellContents = cellContents + '<span style="font-size: 11px; color: #7A7A7A;">Blood Rh: ' + collectionInfo.bloodRh + '</span><br />';
    }
    if (collectionInfo.ttiStatus !== undefined) {
      cellContents = cellContents + '<span style="font-size: 11px; color: #7A7A7A;">TTI: ' + collectionInfo.ttiStatus + '</span><br />';
    }
    // not showing clear selection link for now
    //'<span class="link clearSelection">Clear</span>'
    cellContents = cellContents + '<br/> </div>';
    return cellContents;
  }

  function getEditableTestSelector(cell, collectionId, testid) {
    var rowContents = $("#${editableFieldsForTableId}").find(".editable" + testid + "Field");
    rowContents = rowContents[0].outerHTML.replace(/collectionId/g, collectionId);
    rowContents = $(rowContents);
    if (cell !== null && cell !== undefined && cell !== "") {
      // check the appropriate radio button if the test result is already available
      var radioButton = rowContents.find('input[data-testid="' + testid + '"]' +
          '[data-rowid="' + collectionId + '"]' +
          '[data-validresult="' + cell + '"]');
      radioButton.attr("checked", "checked");
    }
    return rowContents[0].outerHTML;
  }
  
  function rowDeselectDisableEdit(node) {
    getWorksheetForTestResultsTable().find(".editableField").hide();
    rowContents = rowContents.replace(/collectionId/g, collectionId);
    $(node).find(".viewableField").show();
  }

  $("#${mainContentId}").find(".returnToFindWorksheetButton")
                        .button({icons: {primary : 'ui-icon-arrowreturn-1-w'}})
                        .click(function() {
                          if (isWorksheetModified()) {
                            showUnsavedChangesDialog();
                            return;
                          }
                          $("#${tabContentId}").trigger("worksheetSummarySuccess");
                        });
});
</script>

<div id="${tabContentId}">
  <div id="${mainContentId}">

    <br />
    <br />

    <button class="returnToFindWorksheetButton">Return to find worksheet screen</button>

    <br />

    <div class="printableArea">

      <div style="margin-top: 20px; margin-bottom: 20px; font-size: 18pt;">Worksheet Number: ${worksheetNumber}</div>
        <table class="dataTable worksheetForTestResultsTable noHighlight">
          <thead>
            <tr>
              <th style="display: none"></th>
              <c:if test="${worksheetConfig['collectionNumber'] == 'true'}">
                <th style="width: 150px;">
                  Collection Number
                </th>
              </c:if>

              <c:forEach var="bloodTest" items="${bloodTests}">
                <th style="width: 170px;">
                  ${bloodTest.testNameShort}
                </th>
              </c:forEach>
            </tr>
          </thead>
          <tbody style="font-size: 11pt;">
          </tbody>
          <tfoot>
            <tr>
            <!-- colspan must be set in order for buttons to appear at the appropriate position -->
            <td colspan="${fn:length(bloodTests) + 2}" align="right">
              <button class="worksheetSaveAndNextButton">Save</button>
              <button class="worksheetUndoChangesOnPageButton">Undo changes on this page</button>
            </td>
            </tr>
            </tfoot>
        </table>
      </div>
    </div>
  <div id="${childContentId}"></div>
</div>

<div id="${noResultsFoundDivId}" style="display: none;">
  <span
    style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
    No worksheet found. </span>
</div>

<div id="${editableFieldsForTableId}" style="display: none;">
  <c:forEach var="bloodTest" items="${bloodTests}">
    <div class="editable${fn:replace(bloodTest.id, ' ', '')}Field editableField">
      <c:set var="uniqueInputName" value="${fn:replace(bloodTest.testNameShort,' ','')}-collectionId" />
      <c:forEach var="validResult" items="${bloodTest.validResults}">
        <div>
          <!-- using collected sample id as the name should be unique across multiple inputs.
               otherwise selecting one radio button will change another radio button with the
               same name.
           -->

          <!-- nesting input element inside label element allows selection of input radiobutton by clicking on the label text.
               this is nice from usability point of view.
            -->
           <label style="margin-left: 2px;
                   margin-right: 0; cursor: pointer; font-size: 20px;">
              <input type="radio"
                     name="${uniqueInputName}" value="${validResult}"
                    data-testname="${bloodTest.testNameShort}"
                    data-testid="${bloodTest.id}"
                    data-validresult="${validResult}"
                    data-rowid="collectionId"
                    style="width: 15px; margin-left: 0; margin-right: 0;" />
                   ${validResult}
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
    There are unsaved changes on the worksheet. Please save your changes or click on undo to cancel your changes.
  </p>
</div>

<div id="${UninterpretableResultsDialogId}" style="display: none">
  <br />
  Results for the collection are uninterpretable. Are you sure you want to save the results?
  <br />
</div>
