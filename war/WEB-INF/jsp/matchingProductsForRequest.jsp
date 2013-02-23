<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tableContent-${unique_page_id}</c:set>
<c:set var="table_id">productsTable-${unique_page_id}</c:set>
<c:set var="productsTableEditRowDivId">productsTableEditRowDiv-${unique_page_id}</c:set>
<c:set var="confirmIssueProductsDialogId">confirmIssueProductsDialog-${unique_page_id}</c:set>

<script>
$(document).ready(
    function() {

			var productVolumeSelection = {};
      var selected_products = [];
      
      var selectedRowId = null;
      var productsTable = $("#${table_id}").dataTable({
        "bJQueryUI" : true,
        "sDom" : 'C<"H"lfrT>t<"F"ip>T',
        "aaSorting" : [],
        "oTableTools" : {
          "sRowSelect" : "multi",
          "aButtons" : [ "print" ],
          "fnRowSelected" : function(node) {
											        var elements = $(node).children();
											        if (elements[0].getAttribute("class") === "dataTables_empty") {
											          return;
											        }
											        selectedRowId = elements[0].innerHTML;
											        var checkbox = $(node).find(":checkbox");
										        	selected_products.push(selectedRowId);
										        	checkbox.attr("checked", true);
											        console.log(selected_products);
 													  },
				"fnRowDeselected" : function(node) {
														  var elements = $(node).children();
											        if (elements[0].getAttribute("class") === "dataTables_empty") {
											          return;
											        }
											        selectedRowId = elements[0].innerHTML;
											        var checkbox = $(node).find(":checkbox");
											        selected_products.splice(selected_products.indexOf(selectedRowId), 1);
											        checkbox.attr('checked', false);
											        console.log(selected_products);
														},
        },
        "oColVis" : {
         	"aiExclude": [0,1],
        }
      });

      $("#${tabContentId}").find(".issueSelectedProductsButton").button({
        icons : {
          primary : 'ui-icon-check'
        }
      }).click(issueSelectedProducts);

      $("#${tabContentId}").find(".cancelButton").button({
        icons : {
          primary : 'ui-icon-closethick'
        }
      }).click(function() {
        $("#${tabContentId}").trigger("productIssueCancel");
      });


      function issueSelectedProducts() {
        console.log(selected_products);
        if (selected_products.length == 0) {
          showMessage("You must select at least one product to issue.");
          return;
        }
        showConfirmIssueDialog();
      }

      function confirmIssue() {
        var data = {requestId : "${model.request.id}",
            				productsToIssue : JSON.stringify(productVolumeSelection)
            			 };
        $.ajax({
          url: "issueSelectedProducts.html",
          type: "POST",
          data: data,
          success: function() {
            				 showMessage("Products Issued Successfully!");
            				 $("#${tabContentId}").parent().trigger("productIssueSuccess");
          				 },
         	error:   function() {
										 showErrorMessage("Something went wrong while issuing your request. Please try again.");         	  
         					 }
        });
      }

      function showConfirmIssueDialog() {
        $.ajax({
          type : "GET",
          url : "confirmIssueProductsDialog.html",
          data : {requestId: "${model.request.id}", productsToIssue: JSON.stringify(selected_products)},
          success : function (response) {
					  					$("#${confirmIssueProductsDialogId}").find(".dialogContent").html(response);
					  					$("#${confirmIssueProductsDialogId}").bind("updateProductVolumeSelection", updateProductVolumeSelection);
					  					$("#${confirmIssueProductsDialogId}").dialog({
            					  modal: true,
            					  width: 800,
            					  height: 600,
            					  buttons: {
            					    "Confirm Issue" : function() {	confirmIssue(); $(this).dialog("close"); },
            					    "Cancel" : function(event, ui) { $(this).dialog("close"); }
            					  }
            					});
          				  },
          error :   function () {
            				  showErrorMessage("Something went wrong. Please try again.");
          				  }
        });
      }

      $("#${table_id}_filter").find("label").find("input").keyup(function() {
        var searchBox = $("#${table_id}_filter").find("label").find("input");
        $("#${table_id}").removeHighlight();
        if (searchBox.val() != "")
          $("#${table_id}").find("td").highlight(searchBox.val());
      });

      function updateProductVolumeSelection(event, productVolumes) {
        productVolumeSelection = productVolumes;
        console.log(productVolumeSelection);
      }
    });
</script>

<div id="${tabContentId}">

	<c:choose>

		<c:when test="${fn:length(model.allProducts) eq 0}">
			<span
				style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
				Sorry no matching products found for this request </span>
		</c:when>

		<c:otherwise>

			<div class="tipsBox ui-state-highlight">
				<p>
					${model['requests.findpending.findmatchingproducts']}
				</p>
			</div>

			<br />
			<button class="issueSelectedProductsButton">Issue Selected Products</button>
			<button class="cancelButton">Cancel</button>
			<br />
			<br />
			<table id="${table_id}" class="dataTable productsTable">
				<thead>
					<tr>
						<th style="display:none;"></th>
						<th></th>
						<c:if test="${model.productFields.productNumber.hidden != true}">
							<th>${model.productFields.productNumber.displayName}</th>
						</c:if>
						<c:if test="${model.productFields.bloodGroup.hidden != true}">
							<th>${model.productFields.bloodGroup.displayName}</th>
						</c:if>
						<c:if test="${model.productFields.productType.hidden != true}">
							<th>${model.productFields.productType.displayName}</th>
						</c:if>
						<c:if test="${model.productFields.createdOn.hidden != true}">
							<th>${model.productFields.createdOn.displayName}</th>
						</c:if>
						<c:if test="${model.productFields.age.hidden != true}">
							<th>${model.productFields.age.displayName} (in days)</th>
						</c:if>
						<c:if test="${model.productFields.expiresOn.hidden != true}">
							<th>${model.productFields.expiresOn.displayName}</th>
						</c:if>
							<th>${model.crossmatchTestFields.compatibilityResult.displayName}</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="product" items="${model.allProducts}">
						<c:set var="rowColor" value="${product.isCompatible == 'COMPATIBLE' ? 'green' : '' }" />
						<c:set var="rowFontWeight" value="${product.isCompatible == 'COMPATIBLE' ? 'bold' : '' }" />
						<tr style="color: ${rowColor}; font-weight: ${rowFontWeight};">
							<td style="display: none;">${product.id}</td>
							<td>
								<input type="checkbox" />
							</td>
							<c:if test="${model.productFields.productNumber.hidden != true}">
								<td>${product.productNumber}</td>
							</c:if>
							<c:if test="${model.productFields.bloodGroup.hidden != true}">
								<td>${product.bloodGroup}</td>
							</c:if>
							<c:if test="${model.productFields.productType.hidden != true}">
								<td>${product.productType.productType}</td>
							</c:if>
							<c:if test="${model.productFields.createdOn.hidden != true}">
								<td>${product.createdOn}</td>
							</c:if>
							<c:if test="${model.productFields.age.hidden != true}">
								<td>${product.age}</td>
							</c:if>
							<c:if test="${model.productFields.expiresOn.hidden != true}">
								<td>${product.expiresOn}</td>
							</c:if>
							<td>${product.isCompatible}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

		</c:otherwise>
	</c:choose>

</div>

<div id="${confirmIssueProductsDialogId}" title="Confirm Issue?" style="display: none;">
	<div class="dialogContent"></div>
</div>