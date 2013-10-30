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

<c:set var="newComponentProcessingDialogId">newComponentProcessingDialog-${unique_page_id}</c:set>
<c:set var="editComponentProcessingDialogId">editComponentProcessingDialog-${unique_page_id}</c:set>
<c:set var="addComponentProcessingFormProductType">addComponentProcessingFormProductType-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  var componentProcessingTable = $("#${mainContentId}").find(".componentProcessingTable").dataTable({
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

                          $("#${mainContentId}").find(".productTypeSummarySection").html("");
                          var selectedRowId = elements[0].innerHTML;

                          $.ajax({
                            url: "componentProcessingSummary.html",
                            type: "GET",
                            data: {productTypeId : selectedRowId},
                            success: function(response) {
                                       var productTypeSummarySection = $("#${mainContentId}").find(".productTypeSummarySection");
                                       animatedScrollTo(productTypeSummarySection);
                                       productTypeSummarySection.html(response);
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
                          var productTypeSummarySection = $("#${mainContentId}").find(".productTypeSummarySection");
                          productTypeSummarySection.html("");
                        },
    },
    "oColVis" : {
       "aiExclude": [0],
    }
  });
  
  $("#${addComponentProcessingFormeId}").multiselect({
      multiple : false,
      selectedList : 1,
      header : false
    });

  $("#${newComponentProcessingDialogId}").find(".addComponentProcessingFormeId").multiselect({
    multiple : false,
    selectedList : 1,
    header : false
  });

  $("#${newComponentProcessingDialogId} .ui-multiselect").css("width", "227px");

  $("#${editComponentProcessingDialogId}").find(".expiresAfterUnitsSelector").multiselect({
    multiple : false,
    selectedList : 1,
    header : false
  });

  $("#${editComponentProcessingDialogId} .ui-multiselect").css("width", "100px");
  
  $("#${tabContentId}").bind("componentProcessingEditDone", refetchComponentProcessing);
  $("#${tabContentId}").bind("componentProcessingCancel", function() {
    // deselect all rows in the table
    var oTableTools = TableTools.fnGetInstance($("#${mainContentId}").find("table")[0]);
    oTableTools.fnSelectNone();
    $("#${mainContentId}").find(".productTypeSummarySection").html("");
  });

  function refetchComponentProcessing() {
    showLoadingImage($("#${tabContentId}"));
    refetchContent("${refreshUrl}", $("#${tabContentId}"));
  }

  $("#${mainContentId}").find(".newComponentProcessingButton")
                        .button({icons: {primary: 'ui-icon-plusthick'}})
                        .click(function() {
                          showNewComponentProcessing();
                        });

  function showNewComponentProcessing() {
    clearComponentProcessingData("${newComponentProcessingDialogId}");
    showEditComponentProcessingDialogGeneric("${newComponentProcessingDialogId}", "New Component Processing", "saveComponentProcessing.html");
  }

  $("#${tabContentId}").bind("editcomponentProcessing", showUpdateComponentProcessingDialog);

  function showUpdateComponentProcessingDialog() {
    setSelectedComponentProcessingData("${editComponentProcessingDialogId}");
    showEditComponentProcessingDialogGeneric("${editComponentProcessingDialogId}", "Edit Component Processing", "updateComponentProcessing.html");
  }

  function showEditComponentProcessingDialogGeneric(formDivId, title, url) {
    $("#" + formDivId).dialog({
      modal: true,
      title: title,
      width: 800,  // dialog width should be sufficient to make sure select option appears on the same line
                  // call to refresh method by setDefaultValueForSelector just ignores the width and the selector ends up on the next line
                  // setDefaultValueForSelector() does not seem to work all the time without call to refresh
      height: 400,
      maxHeight: 400,
      buttons: {
        "Save" : function() {
                     var data = getComponentProcessingData(formDivId);
                     if(data.productType!=null && data.productType!=""){
                    	 $("#p3").html("");
                    	 $("#p4").html("");
                    	 if(data.unitsMax > data.unitsMin){
                        	 saveComponentProcessing(url, data);
                             $(this).dialog("close");	 
                         }else{
                        	 $("#p1").html("UnitMin cannot be greater than Unitmax.");
                        	 $("#p2").html("UnitMin cannot be greater than Unitmax.");
                        	 
                         }
                     }else{
                    	 $("#p3").html("Processed Product cannot be Blank.");
                    	 $("#p4").html("Processed Product cannot be Blank.");
                     }
                    
                     
                   },
        "Cancel" : function() {
                     $(this).dialog("close");
                   }
      }
    });
  }

  function clearComponentProcessingData(formDivId) {
    var newProductTypeForm = $("#" + formDivId);
    $("#p1").html("");
    $("#p2").html("");
    $("#p3").html("");
    $("#p4").html("");
    newProductTypeForm.find('select[name="productTypes"]').val("");
    newProductTypeForm.find('input[name="unitsMin"]').val("");
    newProductTypeForm.find('input[name="unitsMax"]').val("");
  }

  function setSelectedComponentProcessingData(formDivId) {
	var oTableTools = TableTools.fnGetInstance($("#${mainContentId}").find("table")[0]);
	var selectedRow = oTableTools.fnGetSelected()[0];
	var newComponentProcessingForm = $("#" + formDivId);
	newComponentProcessingForm.find('input[name="productTypeId"]').val($(selectedRow).data("processingid"));
    newComponentProcessingForm.find('select[name="productTypes"]').val($(selectedRow).data("productprocesses"));
    newComponentProcessingForm.find('input[name="unitsMin"]').val($(selectedRow).data("unitsmins"));
    newComponentProcessingForm.find('input[name="unitsMax"]').val($(selectedRow).data("unitsmaxs"));
    
  }

  function getComponentProcessingData(formDivId) {
    var data = {};
    var newProductTypeForm = $("#" + formDivId);
    data.id = newProductTypeForm.find('input[name="productTypeId"]').val();
    data.productType = newProductTypeForm.find('select[name="productTypes"]').val();
    data.unitsMin = newProductTypeForm.find('input[name="unitsMin"]').val();
    data.unitsMax = newProductTypeForm.find('input[name="unitsMax"]').val();
    return data;
  }

  function saveComponentProcessing(url, data) {
    $.ajax({
      url: url,
      type: "POST",
      data: {componentProcessingType : JSON.stringify(data)},
      success: function(response) {
                 showMessage("Component Processing successfully saved.");
                 $("#${tabContentId}").trigger("componentProcessingEditDone");
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

      <div style="font-weight: bold; margin: 15px;">Component Processing</div>

      <div class="tipsBox ui-state-highlight">
        <p>
          The following Component Processing can be created.
          You can create new Component Processing based on your requirement.
        </p>
      </div>

      <div>
        <button class="newComponentProcessingButton">New component processing</button>
      </div>

      <br />

      <table class="componentProcessingTable">  
        <thead>
          <tr style="height: 30px;">
            <th style="display: none;"></th>
            <th>Processed Product</th>
            <th>Source Product</th>
            <th>Units Min.</th>
            <th>Units Max.</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
        
          <c:forEach var="componentProcessing" items="${allComponentProcessing}">
            <tr data-processingid="${componentProcessing.id}"
                data-productprocesses="${componentProcessing.productProcessed}"
                data-unitsmins="${componentProcessing.unitsMin}"
                data-unitsmaxs="${componentProcessing.unitsMax}">
              <td style="display: none;">${componentProcessing.id}</td>
              <td>${componentProcessing.productType.productType}</td>
              <td>${componentProcessing.productType.productTypeNameShort}</td>
              <td>${componentProcessing.unitsMin} </td>
              <td>${componentProcessing.unitsMax} </td>
              <c:set var="cellColor" value="${componentProcessing.isDeleted ? '#A2361A':'#154A16'}" />
              <td style="color: ${cellColor};">${componentProcessing.isDeleted ? 'Not in Use' : 'In Use'}</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>

      <div class="productTypeSummarySection" style="height: 500px;">
      </div>

    </div>

  </div>

  <div id="${childContentId}">
  </div>

 <div id="${editComponentProcessingDialogId}" style="display: none;">
    <form class="formFormatClass">
      <input type="hidden" name="productTypeId"/>
      <div>
        <label>Product Processed</label>
       	<form:select path="productTypes"
            id="${addComponentProcessingFormProductType}" name="productTypes"
            class="addComponentProcessingFormeId">
            <form:option value="">&nbsp;</form:option>
            <c:forEach var="productTypes" items="${productTypes}">
              <form:option value="${productTypes.id}">${productTypes.productType}</form:option>
            </c:forEach>
          </form:select><label id="p3" style="width:39%;color:red"></label>
      </div>
     <div>
        <label>Units Min </label>
        <input name="unitsMin" /><label id="p1" style="width:39%;color:red"></label>
      </div>
      <div>
        <label>Units Max </label>
        <input name="unitsMax" />
      </div>
    </form>
  </div>

  <div id="${newComponentProcessingDialogId}" style="display: none;">
    <form class="formFormatClass">
      <input type="hidden" name="productTypeId" value="" />
      <div>
        <label>Product Processed</label>
        <form:select path="productTypes"
            id="${addComponentProcessingFormProductType}" name="productTypes"
            class="addComponentProcessingFormeId">
            <form:option value="">&nbsp;</form:option>
            <c:forEach var="productTypes" items="${productTypes}">
              <form:option value="${productTypes.id}">${productTypes.productType}</form:option>
            </c:forEach>
          </form:select><label id="p4" style="width:39%;color:red"></label>
      </div>
      <div>
        <label>Units Min </label>
        <input name="unitsMin" /><label id="p2" style="width:39%;color:red"></label>
      </div>
      <div>
        <label>Units Max </label>
        <input name="unitsMax" />
      </div>
    </form>
  </div>

</div>