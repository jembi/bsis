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

<c:set var="newProductTypeDialogId">newProductTypeDialog-${unique_page_id}</c:set>
<c:set var="editProductTypeDialogId">editProductTypeDialog-${unique_page_id}</c:set>

<c:set var="productTypeExpiresAfterUnitsSelectorId">productTypeExpiresAfterUnitsSelector-${unique_page_id}</c:set>
<c:set var="editBledProductCheckboxId">editBledProductCheckboxId-${unique_page_id}</c:set>
<c:set var="addBledProductCheckboxId">addBledProductCheckboxId-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  var productTypesTable = $("#${mainContentId}").find(".productTypesTable").dataTable({
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
                            url: "productTypeSummary.html",
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

  $("#${newProductTypeDialogId}").find(".expiresAfterUnitsSelector").multiselect({
    multiple : false,
    selectedList : 1,
    header : false
  });

  $("#${newProductTypeDialogId} .ui-multiselect").css("width", "100px");

  $("#${editProductTypeDialogId}").find(".expiresAfterUnitsSelector").multiselect({
    multiple : false,
    selectedList : 1,
    header : false
  });

  $("#${editProductTypeDialogId} .ui-multiselect").css("width", "100px");
  
  $("#${tabContentId}").bind("productTypeEditDone", refetchProductTypes);
  $("#${tabContentId}").bind("productTypeCancel", function() {
    // deselect all rows in the table
    var oTableTools = TableTools.fnGetInstance($("#${mainContentId}").find("table")[0]);
    oTableTools.fnSelectNone();
    $("#${mainContentId}").find(".productTypeSummarySection").html("");
  });

  function refetchProductTypes() {
    showLoadingImage($("#${tabContentId}"));
    refetchContent("${refreshUrl}", $("#${tabContentId}"));
  }

  $("#${mainContentId}").find(".newProductTypeButton")
                        .button({icons: {primary: 'ui-icon-plusthick'}})
                        .click(function() {
                          showNewProductTypeDialog();
                        });

  function showNewProductTypeDialog() {
    clearProductTypeData("${newProductTypeDialogId}");
    showEditProductTypeDialogGeneric("${newProductTypeDialogId}", "New Product Type", "saveNewProductType.html");
  }

  $("#${tabContentId}").bind("editProductType", showUpdateProductTypeDialog);

  function showUpdateProductTypeDialog() {
	setSelectedProductTypeData("${editProductTypeDialogId}");
    showEditProductTypeDialogGeneric("${editProductTypeDialogId}", "Edit Product Type", "updateProductType.html");
  }

  function showEditProductTypeDialogGeneric(formDivId, title, url) {
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
                     var data = getProductTypeData(formDivId);
                     var numberRegex = /^[1-9][0-9]*$/;
                     var str = data.expiresAfter;
                     
                     $("div#"+formDivId+" #productTypeNameErrorMessage").html("");
                     $("div#"+formDivId+" #productTypeCodeErrorMessage").html("");
                     $("div#"+formDivId+" #expiresAfterErrorMessage").html("");
                     $("div#"+formDivId+" #productTypeNameErrorMessage1").html("");
                     $("div#"+formDivId+" #productTypeCodeErrorMessage1").html("");
                     $("div#"+formDivId+" #expiresAfterErrorMessage1").html("");
                     
                     if(data.productTypeName == "" || data.productTypeCode == "" || data.expiresAfter == "" || !numberRegex.test(str)){
                    	 if(data.productTypeName == ""){
                    		 $("div#"+formDivId+" #productTypeNameErrorMessage").html("Product Type Name cannot be blank.");
                    		 $("div#"+formDivId+" #productTypeNameErrorMessage1").html("Product Type Name cannot be blank.");
                    	 }
                    	 if(data.productTypeCode == ""){
                    		 $("div#"+formDivId+" #productTypeCodeErrorMessage").html("Product Type Code cannot be blank.");
                    		 $("div#"+formDivId+" #productTypeCodeErrorMessage1").html("Product Type Code cannot be blank.");
                    	 }
                    	 if(data.expiresAfter == ""){
                    	 	$("div#"+formDivId+" #expiresAfterErrorMessage").html("Expires interval cannot be blank.");
                    	 	$("div#"+formDivId+" #expiresAfterErrorMessage1").html("Expires interval cannot be blank.");
                    	 }
                    	 if(data.expiresAfter != "" && !numberRegex.test(str)){
                    	 	 $("div#"+formDivId+" #expiresAfterErrorMessage").html("Expires interval must have numeric value.");	 
                    		 $("div#"+formDivId+" #expiresAfterErrorMessage1").html("Expires interval must have numeric value.");
                    	 }
                     }
                    
                     else{
                    	 	 saveProductType(url, data);
                             $(this).dialog("close");
                     }
                     
                   },
        "Cancel" : function() {
                     $(this).dialog("close");
                   }
      }
    });
  }
  
  function clearProductTypeData(formDivId) {
	var newProductTypeForm = $("#" + formDivId);
    newProductTypeForm.find('input[name="productTypeId"]').val("");
    newProductTypeForm.find('input[name="productTypeName"]').val("");
    newProductTypeForm.find('input[name="productTypeCode"]').val("");
    newProductTypeForm.find('input[name="expiresAfter"]').val("");
    newProductTypeForm.find('input[name="bledProduct"]').val("");
    $("div#"+formDivId+" #productTypeNameErrorMessage").html("");
    $("div#"+formDivId+" #productTypeCodeErrorMessage").html("");
    $("div#"+formDivId+" #expiresAfterErrorMessage").html("");
    $("div#"+formDivId+" #productTypeNameErrorMessage1").html("");
    $("div#"+formDivId+" #productTypeCodeErrorMessage1").html("");
    $("div#"+formDivId+" #expiresAfterErrorMessage1").html("");
    setDefaultValueForSelector(newProductTypeForm.find('select[name="expiresAfterUnits"]').multiselect(), "DAYS");
  }

  function setSelectedProductTypeData(formDivId) {
	var oTableTools = TableTools.fnGetInstance($("#${mainContentId}").find(".productTypesTable")[0]);
    var selectedRow = oTableTools.fnGetSelected()[0];
    var newProductTypeForm = $("#" + formDivId);
    $("div#"+formDivId+" #productTypeNameErrorMessage").html("");
    $("div#"+formDivId+" #productTypeCodeErrorMessage").html("");
    $("div#"+formDivId+" #expiresAfterErrorMessage").html("");
    $("div#"+formDivId+" #productTypeNameErrorMessage1").html("");
    $("div#"+formDivId+" #productTypeCodeErrorMessage1").html("");
    $("div#"+formDivId+" #expiresAfterErrorMessage1").html("");
    newProductTypeForm.find('input[name="productTypeId"]').val($(selectedRow).data("producttypeid"));
    newProductTypeForm.find('input[name="productTypeName"]').val($(selectedRow).data("producttypename"));
    newProductTypeForm.find('input[name="productTypeCode"]').val($(selectedRow).data("producttypecode"));
    newProductTypeForm.find('input[name="expiresAfter"]').val($(selectedRow).data("expiresafter"));
    newProductTypeForm.find('input[name="bledProduct"]').val($(selectedRow).data("bledproduct"));
    newProductTypeForm.find('input[name="bledProduct"]').attr('checked', $(selectedRow).data("bledproduct"));
    setDefaultValueForSelector(newProductTypeForm.find('select[name="expiresAfterUnits"]').multiselect(), $(selectedRow).data("expiresafterunits"));
  }

  function getProductTypeData(formDivId) {
	var splitedFormDivId = formDivId.split("-");
	var data = {};
    var newProductTypeForm = $("#" + formDivId);
    var isChecked;
    data.id = newProductTypeForm.find('input[name="productTypeId"]').val();
    data.productTypeName = newProductTypeForm.find('input[name="productTypeName"]').val();
    data.productTypeCode = newProductTypeForm.find('input[name="productTypeCode"]').val();
    data.expiresAfter = newProductTypeForm.find('input[name="expiresAfter"]').val();
    data.expiresAfterUnits = newProductTypeForm.find('.expiresAfterUnitsSelector').val();
    
    if(splitedFormDivId[0]=="editProductTypeDialog"){
    	var isChecked = $("#${editBledProductCheckboxId}").is(":checked");
    }else{
    	isChecked = $("#${addBledProductCheckboxId}").is(":checked");
    }
    if(isChecked){
    	data.bledProduct = "true";
    }else{
    	data.bledProduct = "false";
    }
    
    var createCheckBox = newProductTypeForm.find(".createPediProductType");
    if (newProductTypeForm.find(".createPediProductType").length != 0) {
      if (newProductTypeForm.find(".createPediProductType").is(":checked")) {
        data.createPediProductType = "true";
      }
      else {
        data.createPediProductType = "false";
      }
    }
    return data;
  }

  function saveProductType(url, data) {
	$.ajax({
      url: url,
      type: "POST",
      data: {productType : JSON.stringify(data)},
      success: function(response) {
                 showMessage("Product type successfully saved.");
                 $("#${tabContentId}").trigger("productTypeEditDone");
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

      <div style="font-weight: bold; margin: 15px;">${ConfigureProductTypes.productTypes.displayName}</div>

      <div class="tipsBox ui-state-highlight">
        <p>
          The following product types can be created.
          You can create new product types based on your requirement.
        </p>
      </div>

      <div>
        <button class="newProductTypeButton">${ConfigureProductTypes.newProductType.displayName}</button>
      </div>

      <br />

      <table class="productTypesTable">  
        <thead>
          <tr style="height: 30px;">
            <th style="display: none;"></th>
            <th>${ConfigureProductTypes.productType.displayName}</th>
            <th>${ConfigureProductTypes.productCode.displayName}</th>
            <th>${ConfigureProductTypes.expiryTime.displayName}</th>
            <th>${ConfigureProductTypes.bledProduct.displayName}</th>
            <th>${ConfigureProductTypes.status.displayName}</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="productType" items="${productTypes}">
            <tr data-producttypeid="${productType.id}"
                data-producttypename="${productType.productType}"
                data-productTypeCode="${productType.productTypeCode}"
                data-expiresafter="${productType.expiresAfter}"
                data-expiresafterunits="${productType.expiresAfterUnits}"
                data-bledproduct="${productType.bledProduct}"
                >
              <td style="display: none;">${productType.id}</td>
              <td>${productType.productType}</td>
              <td>${productType.productTypeCode}</td>
              <td>${productType.expiresAfter} ${productType.expiresAfterUnits}</td>
              <td>
              <c:if test="${productType.bledProduct != true }">
              	<input type="checkbox" name="bledProduct" disabled="true" id="bledProduct" />
              </c:if>
              <c:if test="${productType.bledProduct != false }">
              	<input type="checkbox" id="bledProduct" name="bledProduct" disabled="true" checked />
              </c:if>
              </td>
			  <c:set var="cellColor" value="${productType.isDeleted ? '#A2361A':'#154A16'}" />
              <td style="color: ${cellColor};">${productType.isDeleted ? 'Not in Use' : 'In Use'}</td>
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

  <div id="${editProductTypeDialogId}" style="display: none;">
    <form class="formFormatClass">
      <input type="hidden" name="productTypeId" value="" />
      <div>
        <label>${ConfigureProductTypes.productTypeName.displayName}</label>
        <input name="productTypeName" /><label id="productTypeNameErrorMessage1" style="width:39%;color:red"></label>
      </div>
      <div>
        <label>${ConfigureProductTypes.productCodeName.displayName}</label>
        <input name="productTypeCode" /><label id="productTypeCodeErrorMessage1" style="width:39%;color:red"></label>
      </div>
      <div>
        <label>${ConfigureProductTypes.expiryInterval.displayName}</label>
        <input name="expiresAfter" type="number" min="1" />
        <select name="expiresAfterUnits"
                class="expiresAfterUnitsSelector"
                 id="${productTypeExpiresAfterUnitsSelectorId}">
           <option value="DAYS">${ConfigureProductTypes.days.displayName}</option>
           <option value="HOURS">${ConfigureProductTypes.hours.displayName}</option>
           <option value="YEARS">${ConfigureProductTypes.years.displayName}</option>
        </select><label id="expiresAfterErrorMessage1" style="width:39%;color:red"></label>
      </div>
      <div>
        <label>${ConfigureProductTypes.bledProduct.displayName}</label>
         <input type="checkbox" id="${editBledProductCheckboxId}" name="bledProduct" />
      </div>
    </form>
  </div>

  <div id="${newProductTypeDialogId}" style="display: none;">
    <form class="formFormatClass">
      <input type="hidden" name="productTypeId" value="" />
      <div>
        <label>${ConfigureProductTypes.productTypeName.displayName}</label>
        <input name="productTypeName" /><label id="productTypeNameErrorMessage" style="width:39%;color:red"></label>
      </div>
      <div>
        <label>${ConfigureProductTypes.productCodeName.displayName}</label>
        <input name="productTypeCode" /><label id="productTypeCodeErrorMessage" style="width:39%;color:red"></label>
      </div>
      <div>
        <label>${ConfigureProductTypes.expiryInterval.displayName}</label>
        <input name="expiresAfter" type="number" min="1" />
        <select name="expiresAfterUnits"
                class="expiresAfterUnitsSelector"
                 id="${productTypeExpiresAfterUnitsSelectorId}">
           <option value="DAYS">${ConfigureProductTypes.days.displayName}</option>
           <option value="HOURS">${ConfigureProductTypes.hours.displayName}</option>
           <option value="YEARS">${ConfigureProductTypes.years.displayName}</option>
        </select><label id="expiresAfterErrorMessage" style="width:39%;color:red"></label>
      </div>
      <div>
        <label style="width: auto;">Create corresponding pedi product type</label>
        <input type="checkbox" name="createPediProductType" class="createPediProductType" />
      </div>
      <div>
        <label>${ConfigureProductTypes.bledProduct.displayName}</label>
            <input type="checkbox" id="${addBledProductCheckboxId}" name="bledProduct"/>
      </div>
    </form>
  </div>

</div>