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

<c:set var="newProductTypeCombinationDialogId">newProductTypeDialog-${unique_page_id}</c:set>

<c:set var="productTypeCombinationProductTypeSelectorId">productTypeCombinationProductTypeSelector-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  var productTypeCombinationsTable = $("#${mainContentId}").find(".productTypeCombinationTable").dataTable({
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

                          $("#${mainContentId}").find(".productTypeCombinationSummarySection").html("");
                          var selectedRowId = elements[0].innerHTML;

                          $.ajax({
                            url: "productTypeCombinationSummary.html",
                            type: "GET",
                            data: {productTypeCombinationId : selectedRowId},
                            success: function(response) {
                                       var productTypeCombinationSummarySection = $("#${mainContentId}").find(".productTypeCombinationSummarySection");
                                       animatedScrollTo(productTypeCombinationSummarySection);
                                       productTypeCombinationSummarySection.html(response);
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
                          var productTypeCombinationSummarySection = $("#${mainContentId}").find(".productTypeCombinationSummarySection");
                          productTypeCombinationSummarySection.html("");
                        },
    },
    "oColVis" : {
       "aiExclude": [0],
    }
  });

  $("#${newProductTypeCombinationDialogId}").find(".productTypesSelector").multiselect({
    position : {
      my : 'left top',
      at : 'right center'
    },
    noneSelectedText: 'None selected',
    selectedText: function(numSelected, numTotal, selectedValues) {
                    var checkedValues = $.map(selectedValues, function(input) { return input.title; });
                    return checkedValues.length ? checkedValues.join(', ') : 'None selected';
                  }
  });

  $("#${newProductTypeCombinationDialogId}").find(".productTypesSelector").multiselect("uncheckAll");

  $("#${tabContentId}").bind("productTypeCombinationEditDone", refetchProductTypeCombination);
  $("#${tabContentId}").bind("productTypeCombinationCancel", function() {
    // deselect all rows in the table
    var oTableTools = TableTools.fnGetInstance($("#${mainContentId}").find("table")[0]);
    oTableTools.fnSelectNone();
    $("#${mainContentId}").find(".productTypeCombinationSummarySection").html("");
  });

  function refetchProductTypeCombination() {
    showLoadingImage($("#${tabContentId}"));
    refetchContent("${refreshUrl}", $("#${tabContentId}"));
  }

  $("#${mainContentId}").find(".newProductTypeCombinationButton")
                        .button({icons: {primary: 'ui-icon-plusthick'}})
                        .click(function() {
                          showNewProductTypeCombinationDialog();
                        });

  function showNewProductTypeCombinationDialog() {
    clearProductTypeCombinationData();
    showEditProductTypeCombinationDialogGeneric("New Product Type Combination", "saveNewProductTypeCombination.html");
  }

  function showEditProductTypeCombinationDialogGeneric(title, url) {
    $("#${newProductTypeCombinationDialogId}").dialog({
      modal: true,
      title: title,
      width: 800,  // dialog width should be sufficient to make sure select option appears on the same line
                  // call to refresh method by setDefaultValueForSelector just ignores the width and the selector ends up on the next line
                  // setDefaultValueForSelector() does not seem to work all the time without call to refresh
      height: 400,
      maxHeight: 400,
      buttons: {
        "Save" : function() {
                     var data = getProductTypeCombinationData();
                     saveProductTypeCombination(url, data);
                     $(this).dialog("close");
                   },
        "Cancel" : function() {
                     $(this).dialog("close");
                   }
      }
    });
  }

  function clearProductTypeCombinationData() {
    var newProductTypeForm = $("#${newProductTypeCombinationDialogId}");
    newProductTypeForm.find('input[name="productTypeCombinationId"]').val("");
    newProductTypeForm.find('input[name="combinationName"]').val("");
    $("#${newProductTypeCombinationDialogId}").find(".productTypesSelector").multiselect("uncheckAll");
  }

  function getProductTypeCombinationData() {
    var data = {};
    var newProductTypeForm = $("#${newProductTypeCombinationDialogId}");
    data.combinationName = newProductTypeForm.find('input[name="combinationName"]').val();
    var productTypesSelector = $("#${newProductTypeCombinationDialogId}").find(".productTypesSelector");
    data.productTypeIds = productTypesSelector.multiselect("getChecked").map(function() {
      return this.value;
    }).get().join(",");
    return data;
  }

  function saveProductTypeCombination(url, data) {

    if (data.productTypeIds === undefined || data.productTypeIds.length === 0) {
      showErrorMessage("No product types selected");
      return;
    }

    $.ajax({
      url: url,
      type: "POST",
      data: {productTypeCombination : JSON.stringify(data)},
      success: function(response) {
                 showMessage("Product type combination successfully saved.");
                 $("#${tabContentId}").trigger("productTypeCombinationEditDone");
               },
      error:   function() {
                 showErrorMessage("Something went wrong. Please try again.");
               }
    });
  }


});
</script>

<div id="${tabContentId}">

  <div id="${mainContentId}">

    <div style="margin-left: 20px; margin-right: 20px; margin-top: 50px; border-radius: 5px;">

      <div style="font-weight: bold; margin: 15px;">Product types</div>

      <div class="tipsBox ui-state-highlight">
        <p>
          Products can be created with the following product type combinations.
          <br />
          You can create new product type combinations based on your requirement.
        </p>
      </div>

      <div>
        <button class="newProductTypeCombinationButton">New product type combination</button>
      </div>

      <br />

      <table class="productTypeCombinationTable">  
        <thead>
          <tr style="height: 30px;">
            <th style="display: none;"></th>
            <th>Combination name</th>
            <th>Product types in combination</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="productTypeCombination" items="${productTypeCombinations}">
            <tr data-producttypecombinationid="${productTypeCombination.id}"
                data-combinationname="${productTypeCombination.combinationName}"
                data-productTypes="${productTypeCombination.productTypes}"
                >
              <td style="display: none;">${productTypeCombination.id}</td>
              <td>${productTypeCombination.combinationName}</td>

              <td>
                <ul>
                  <c:forEach var="productType" items="${productTypeCombination.productTypes}">
                    <li>${productType.productType}</li>
                  </c:forEach>
                </ul>
              </td>

              <c:set var="cellColor" value="${productTypeCombination.isDeleted ? '#A2361A':'#154A16'}" />
              <td style="color: ${cellColor};">${productTypeCombination.isDeleted ? 'Not in Use' : 'In Use'}</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>

      <div class="productTypeCombinationSummarySection" style="height: 500px;">
      </div>

    </div>

  </div>

  <div id="${childContentId}">
  </div>

  <div id="${newProductTypeCombinationDialogId}" style="display: none;">
    <form class="formFormatClass">
      <div>
        <label>Combination name</label>
        <input name="combinationName" placeholder="Optional" />
      </div>
      <div>
        <label>Product types</label>
        <select name="productTypes"
                class="productTypesSelector"
                 id="${productTypeCombinationProductTypeSelectorId}">
          <c:forEach var="productType" items="${productTypes}">
            <option value="${productType.id}">${productType.productTypeCode}</option>
          </c:forEach>
        </select>
      </div>
    </form>
  </div>

</div>