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

<c:set var="crossmatchConfirmDialogId">crossmatchConfirmDialog-${unique_page_id}</c:set>

<script>
$(document).ready(
    function() {

      var selected_products = [];
      var compatible_notknown = {};
      var compatible = {};
      
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

											        var collectionId = elements[0].innerHTML;
											        var collectionNumber = elements[2].innerHTML;
											        switch ($(node).data('iscompatible')) {
											        case 'OTHER':
											        case 'NOT_KNOWN': compatible_notknown[collectionId] = collectionNumber; 
											        									break;
											        case 'COMPATIBLE': compatible[collectionId] = collectionNumber;
											        									 break;
											        }

											        var selectedRowId = elements[0].innerHTML;
											        var checkbox = $(node).find(":checkbox");
										        	selected_products.push(selectedRowId);
										        	checkbox.attr("checked", true);
 													  },
					"fnRowDeselected" : function(node) {
														  var elements = $(node).children();
											        if (elements[0].getAttribute("class") === "dataTables_empty") {
											          return;
											        }

											        var selectedRowId = elements[0].innerHTML;

											        if (selectedRowId in compatible)
											          delete compatible[selectedRowId];
											        if (selectedRowId in compatible_notknown)
											          delete compatible_notknown[selectedRowId];

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
      }).click(function() {
        				issueSelectedProducts()
      				 });

      $("#${tabContentId}").find(".cancelButton").button({
        icons : {
          primary : 'ui-icon-closethick'
        }
      }).click(function() {
        $("#${tabContentId}").trigger("productIssueCancel");
      });


      function issueSelectedProducts(skipCrossmatch) {
        console.log(selected_products);
        if (selected_products.length == 0) {
          showMessage("You must select at least one product to issue.");
          return;
        }

        if (skipCrossmatch === undefined)
          skipCrossmatch = false;
        
        if (Object.keys(compatible_notknown).length > 0 && skipCrossmatch === false) {
          showCrossmatchConfirmDialog();
          return;
        }
        
        var data = {requestId : "${requestId}",
            				productsToIssue : JSON.stringify(selected_products)
            			 };
        $.ajax({
          url: "issueSelectedProducts.html",
          type: "POST",
          data: data,
          success: function() {
            				 showMessage("Products issued successfully!");
            				 $("#${tabContentId}").parent().trigger("productIssueSuccess");
          				 },
         	error:   function() {
										 showErrorMessage("Something went wrong while issuing your request. Please try again.");         	  
         					 }
        });
      }

      $("#${table_id}_filter").find("label").find("input").keyup(function() {
        var searchBox = $("#${table_id}_filter").find("label").find("input");
        $("#${table_id}").removeHighlight();
        if (searchBox.val() != "")
          $("#${table_id}").find("td").highlight(searchBox.val());
      });

      function showCrossmatchConfirmDialog() {
        // remove existing products from list
        $("#${crossmatchConfirmDialogId}").find("ul").children().remove();

        for (var collectionId in compatible_notknown) {
          $("#${crossmatchConfirmDialogId}").find("ul").append("<li>" + compatible_notknown[collectionId] + "</li>");
        }

        $("#${crossmatchConfirmDialogId}").dialog({
          modal: true,
          title: "Crossmatch not done",
          width: 800,
          height: 400,
          buttons: {
            "Yes Issue these products": function() {
																					$(this).dialog("close");
																					issueSelectedProducts(true);
            														},
            "No I want to add crossmatch tests": function() {
											 														 $(this).dialog("close");
            				 														 }
          }
        });
      }

    });
</script>

<div id="${tabContentId}">

	<c:choose>

		<c:when test="${fn:length(allProducts) eq 0}">
			<span
				style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
				Sorry no matching products found for this request </span>
		</c:when>

		<c:otherwise>

			<div class="tipsBox ui-state-highlight">
				<p>
					${tips['requests.findpending.findmatchingproducts']}
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
						<c:if test="${productFields.collectionNumber.hidden != true}">
							<th>${productFields.collectionNumber.displayName}</th>
						</c:if>
						<c:if test="${productFields.bloodGroup.hidden != true}">
							<th>${productFields.bloodGroup.displayName}</th>
						</c:if>
						<c:if test="${productFields.productType.hidden != true}">
							<th>${productFields.productType.displayName}</th>
						</c:if>
						<c:if test="${productFields.createdOn.hidden != true}">
							<th>${productFields.createdOn.displayName}</th>
						</c:if>
						<c:if test="${productFields.age.hidden != true}">
							<th>${productFields.age.displayName} (in days)</th>
						</c:if>
						<c:if test="${productFields.expiresOn.hidden != true}">
							<th>${productFields.expiresOn.displayName}</th>
						</c:if>
						<th>${compatibilityTestFields.compatibilityResult.displayName}</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="product" items="${allProducts}">
						<c:set var="rowColor" value="${product.isCompatible == 'COMPATIBLE' ? 'green' : '' }" />
						<c:set var="rowFontWeight" value="${product.isCompatible == 'COMPATIBLE' ? 'bold' : '' }" />
						<tr style="color: ${rowColor}; font-weight: ${rowFontWeight};" data-iscompatible="${product.isCompatible}">
							<td style="display: none;">${product.id}</td>
							<td>
								<input type="checkbox" />
							</td>
							<c:if test="${productFields.collectionNumber.hidden != true}">
								<td>${product.collectionNumber}</td>
							</c:if>
							<c:if test="${productFields.collectionNumber.hidden != true}">
								<td>${product.bloodGroup}</td>
							</c:if>
							<c:if test="${productFields.productType.hidden != true}">
								<td>${product.productType.productType}</td>
							</c:if>
							<c:if test="${productFields.createdOn.hidden != true}">
								<td>${product.createdOn}</td>
							</c:if>
							<c:if test="${productFields.age.hidden != true}">
								<td>${product.age}</td>
							</c:if>
							<c:if test="${productFields.expiresOn.hidden != true}">
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

<div id="${crossmatchConfirmDialogId}" style="display: none;">
	<div>
		<p>
			Crossmatch testing has not been done for the following products.
		</p>
		<p>
			Do you want to skip crossmatch tests and issue the selected products?
		</p>
		<ul class="productsListWithoutCrossmatch">
		</ul>
	</div>
</div>
