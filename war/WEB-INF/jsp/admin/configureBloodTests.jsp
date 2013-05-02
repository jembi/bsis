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

<c:set var="newBloodTestDialogId">newBloodTestDialog-${unique_page_id}</c:set>

<c:set var="bloodTestCategorySelectorId">bloodTestCategorySelector-${unique_page_id}</c:set>
<c:set var="bloodTestWorksheetTypeSelectorId">bloodTestWorksheetTypeSelector-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  var bloodTestsTable = $("#${mainContentId}").find(".bloodTestsTable").dataTable({
    "bJQueryUI" : true,
    "sDom" : '<"H"lfrCT>t<"F"i>',
    "bPaginate" : false,
    "oTableTools" : {
      "sRowSelect" : "single",
      "aButtons" : [ "print" ],
      "fnRowSelected" : function(node) {

                          var elements = $(node).children();
                          if (elements[0].getAttribute("class") === "dataTables_empty") {
                            return;
                          }

                          $("#${mainContentId}").find(".bloodTestSummarySection").html("");
                          var selectedRowId = elements[0].innerHTML;

                          $.ajax({
                            url: "bloodTestSummary.html",
                            type: "GET",
                            data: {bloodTestId : selectedRowId},
                            success: function(response) {
                                       var bloodTestSummarySection = $("#${mainContentId}").find(".bloodTestSummarySection");
                                       animatedScrollTo(bloodTestSummarySection);
                                       bloodTestSummarySection.html(response);
                                     },
                            error: function(response) {
                                     showErrorMessage("Something went wrong. Please try again.");
                                   }
                          });
                        },
    "fnRowDeselected" : function(node) {
                          var elements = $(node).children();
                          if (elements[0].getAttribute("class") === "dataTables_empty") {
                            return;
                          }
                          var selectedRowId = elements[0].innerHTML;
                          var bloodTestSummarySection = $("#${mainContentId}").find(".bloodTestSummarySection");
                          bloodTestSummarySection.html("");
                        },
    },
    "oColVis" : {
       "aiExclude": [0,1],
    }
  });

  $("#${newBloodTestDialogId}").find(".worksheetTypeSelector").multiselect({
    noneSelectedText: 'None Selected',
    selectedText: function(numSelected, numTotal, selectedValues) {
                    var checkedValues = $.map(selectedValues, function(input) { return input.title; });
                    return checkedValues.length ? checkedValues.join(', ') : 'None';
                  }
  });
  $("#${newBloodTestDialogId}").find(".worksheetTypeSelector")
                               .multiselect("uncheckAll");

  $("#${newBloodTestDialogId}").find(".bloodTestCategorySelector").multiselect({
    multiple : false,
    selectedList : 1,
    header : false
  });

  $("#${tabContentId}").bind("bloodTestEditDone", refetchBloodTests);
  $("#${tabContentId}").bind("bloodTestCancel", function() {
    // deselect all rows in the table
    var oTableTools = TableTools.fnGetInstance($("#${mainContentId}").find("table")[0]);
    oTableTools.fnSelectNone();
    $("#${mainContentId}").find(".bloodTestSummarySection").html("");
  });

  function refetchBloodTests() {
    showLoadingImage($("#${tabContentId}"));
    refetchContent("${refreshUrl}", $("#${tabContentId}"));
  }

  $("#${mainContentId}").find(".newBloodTestButton")
                        .button({icons: {primary: 'ui-icon-plusthick'}})
                        .click(function() {
                          showNewBloodTestDialog();
                        });

  function showNewBloodTestDialog() {
    $("#${newBloodTestDialogId}").dialog({
      modal: true,
      title: "New Blood Test",
      width: 700,
      height: 400,
      maxHeight: 400,
      buttons: {
        "Create" : function() {
                     var data = getNewBloodTestData();
                     saveNewBloodTest(data);
                     $(this).dialog("close");
                   },
        "Cancel" : function() {
                     $(this).dialog("close");
                   }
      }
    });

    function getNewBloodTestData() {
      var data = {};
      var newBloodTestForm = $("#${newBloodTestDialogId}");
      data.testName = newBloodTestForm.find('input[name="testName"]').val();
      data.testNameShort = newBloodTestForm.find('input[name="testNameShort"]').val();
      data.category = newBloodTestForm.find(".bloodTestCategorySelector").val();
      var worksheetTypesSelector = newBloodTestForm.find(".worksheetTypeSelector");
      data.worksheetTypeIds = worksheetTypesSelector.multiselect("getChecked").map(function() {
        return this.value;
      }).get().join(",");
      console.log(data);
      return data;
    }

    function saveNewBloodTest(data) {
      $.ajax({
        url: "saveNewBloodTest.html",
        type: "POST",
        data: {bloodTest : JSON.stringify(data)},
        success: function(response) {
                   showMessage("New blood test successfully created.");
                   $("#${tabContentId}").trigger("bloodTestEditDone");
                 },
        error:   function() {
                   showErrorMessage("Unable to create new blood test.");
                 }
      });
    }

  }

});
</script>

<div id="${tabContentId}">

  <div id="${mainContentId}">

    <div style="margin-left: 20px; margin-right: 20px; margin-top: 50px; border-radius: 5px;">

      <div style="font-weight: bold; margin: 15px;">Blood tests</div>

      <div class="tipsBox ui-state-highlight">
        <p>
          The following blood tests have been configured for Blood typing/TTI.
          You can disable/enable tests based on your workflow. You can also create
          new blood tests and add them to worksheets.
        </p>
      </div>

      <div>
        <button class="newBloodTestButton">New blood test</button>
      </div>

      <br />

      <table class="bloodTestsTable">  
        <thead>
          <tr style="height: 30px;">
            <th>Blood test #</th>
            <th>Blood test name</th>
            <th>Category</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="bloodTest" items="${bloodTests}">
            <tr>
              <td>${bloodTest.id}</td>
              <td>${bloodTest.testNameShort}</td>
              <td>${bloodTest.category}</td>
              <c:set var="cellColor" value="${bloodTest.isActive ? '#154A16': '#A2361A'}" /> 
              <td style="color: ${cellColor}">${bloodTest.isActive ? 'ENABLED' : 'DISABLED'}</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>

      <div class="bloodTestSummarySection">
      </div>

    </div>

  </div>

  <div id="${childContentId}">
  </div>

  <div id="${newBloodTestDialogId}" style="display: none;">
    <form class="formFormatClass">
      <div>
        <label>Blood test name</label>
        <input name="testName" />
      </div>
      <div>
        <label>Blood test short name</label>
        <input name="testNameShort" />
      </div>
      <div>
        <label>Blood test category</label>
        <select id="${bloodTestCategorySelectorId}"
                name="bloodTestCategory" class="bloodTestCategorySelector">
          <option value="BLOODTYPING">Blood typing</option>
          <option value="TTI">TTI</option>
        </select>
      </div>
      <div>
        <label>Add to worksheets</label>
        <select id="${bloodTestWorksheetTypeSelectorId}"
                name="worksheetType" class="worksheetTypeSelector">
          <c:forEach var="worksheetType" items="${worksheetTypes}">
            <option value="${worksheetType.id}">${worksheetType.worksheetType}</option>
          </c:forEach>
        </select>
      </div>
    </form>
  </div>

</div>