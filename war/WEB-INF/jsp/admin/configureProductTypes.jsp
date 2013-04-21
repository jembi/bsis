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

<c:set var="productTypeExpiresAfterUnitsSelectorId">productTypeExpiresAfterUnitsSelector-${unique_page_id}</c:set>

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
		$("#${newProductTypeDialogId}").dialog({
		  modal: true,
		  title: "New Product Type",
		  width: 700,
		  height: 400,
		  maxHeight: 400,
		  buttons: {
		    "Create" : function() {
										 var data = getNewProductTypeData();
										 saveNewProductType(data);
										 $(this).dialog("close");
		    					 },
		    "Cancel" : function() {
										 $(this).dialog("close");
		    					 }
		  }
		});

		function getNewProductTypeData() {
		  var data = {};
		  var newProductTypeForm = $("#${newProductTypeDialogId}");
		  data.productTypeName = newProductTypeForm.find('input[name="productTypeName"]').val();
		  data.productTypeNameShort = newProductTypeForm.find('input[name="productTypeNameShort"]').val();
		  data.expiresAfter = newProductTypeForm.find('input[name="expiresAfter"]').val();
		  data.expiresAfterUnits = newProductTypeForm.find('.expiresAfterUnitsSelector').val();
		  console.log(data);
		  return data;
		}

		function saveNewProductType(data) {
			$.ajax({
			  url: "saveNewProductType.html",
			  type: "POST",
			  data: {productType : JSON.stringify(data)},
			  success: function(response) {
			    				 showMessage("New product type successfully created.");
			    				 $("#${tabContentId}").trigger("productTypeEditDone");
			  				 },
			  error:   function() {
			    				 showErrorMessage("Unable to create new product type.");
			  				 }
			});
		}

	}

});
</script>

<div id="${tabContentId}">

	<div id="${mainContentId}">

		<div style="margin-left: 20px; margin-right: 20px; margin-top: 50px; border-radius: 5px;">

			<div style="font-weight: bold; margin: 15px;">Product types</div>

			<div class="tipsBox ui-state-highlight">
				<p>
					The following product types can be created.
					You can create new product types based on your requirement.
				</p>
			</div>

			<div>
				<button class="newProductTypeButton">New product type</button>
			</div>

			<br />

			<table class="productTypesTable">	
				<thead>
					<tr style="height: 30px;">
						<th style="display: none;"></th>
						<th>Short name</th>
						<th>Full name</th>
						<th>Expiry time</th>
						<th>Status</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="productType" items="${productTypes}">
						<tr>
							<td style="display: none;">${productType.id}</td>
							<td>${productType.productTypeNameShort}</td>
							<td>${productType.productType}</td>
							<td>${productType.expiresAfter} ${productType.expiresAfterUnits}</td>
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

	<div id="${newProductTypeDialogId}" style="display: none;">
		<form class="formInTabPane">
			<div>
				<label>Product type name</label>
				<input name="productTypeName" />
			</div>
			<div>
				<label>Product type short name</label>
				<input name="productTypeNameShort" />
			</div>
			<div>
				<label>Expiry interal</label>
				<input name="expiresAfter" type="number" min="1" />
				<select name="expiresAfterUnits"
								class="expiresAfterUnitsSelector"
							 	id="${productTypeExpiresAfterUnitsSelectorId}">
					 <option value="DAYS">Days</option>
					 <option value="HOURS">HOURS</option>
					 <option value="YEARS">YEARS</option>
				</select>
			</div>
		</form>
	</div>

</div>