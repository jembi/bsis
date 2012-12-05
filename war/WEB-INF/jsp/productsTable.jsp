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
<c:set var="deleteProductConfirmDialogId">deleteProductConfirmDialog-${unique_page_id}</c:set>

<script>
$(document).ready(
    function() {

      var selectedRowId = null;
      var productsTable = $("#${table_id}").dataTable({
        "bJQueryUI" : true,
        "sDom" : 'C<"H"lfrT>t<"F"ip>T',
        "oTableTools" : {
          "sRowSelect" : "single",
          "aButtons" : [ "print" ],
          "fnRowSelected" : function(node) {
            									clearEditSection();
            									$(".rowEditButton").button("enable");
            					        var elements = $(node).children();
            					        if (elements[0].getAttribute("class") === "dataTables_empty") {
            					          return;
            					        }
            					        selectedRowId = elements[0].innerHTML;
          									},
          "fnRowDeselected" : function(node) {
            									clearEditSection();
            									$(".rowEditButton").button("disable");
          									},
        },
        "oColVis" : {
         	"aiExclude": [0,1],
        }
      });

      function createEditSection(url, data) {
        $.ajax({
          url: url,
          data: data,
          type: "GET",
          success: function(response) {
            				 $("#${productsTableEditRowDivId}").html(response);
            				 $(".editRowDiv").show();
            				 $('html, body').animate({
            				 		scrollTop: $("#${productsTableEditRowDivId}").offset().top
            				 }, 700);
            			 }
        });
      }

      function clearEditSection() {
        console.log("clear");
        $("#${productsTableEditRowDivId}").html("");
        $(".editRowDiv").hide();
      }

    	$(".closeButton").click(clearEditSection);

      $("#${tabContentId}").find(".editProduct").button(
          {
            disabled: true,
            icons : {
        			primary : 'ui-icon-pencil'
      			}
          }).click(function() {
        $("#${productsTableEditRowDivId}").bind("editProductSuccess", refreshResults);
        createEditSection("editProductFormGenerator.html",
            							{productId: selectedRowId});
      });

      function refreshResults() {
        showLoadingImage("${tabContentId}");
        $.ajax({url: "${model.refreshUrl}",
          			data: {},
          			type: "GET",
          			success: function(response) {
          			  				 $("#${tabContentId}").html(response);
          							 }
        });
      }

      $("#${tabContentId}").find(".refreshResults").button({
        icons : {
          primary : 'ui-icon-arrowrefresh-1-e'
        }
      }).click(refreshResults);

      $("#${tabContentId}").find(".deleteProduct").button(
          {
            disabled: true,
            icons : {
        			primary : 'ui-icon-trash'
      			}
          }).click(function() {
        $("#${deleteProductConfirmDialogId}").dialog(
            {
              modal : true,
              title : "Confirm Delete",
              buttons : {
                "Delete" : function() {
                  deleteProduct(selectedRowId, refreshResults);
                  $(this).dialog("close");
                },
                "Cancel" : function() {
                  $(this).dialog("close");
                }
              }
            });
      });

      $("#${table_id}_filter").find("label").find("input").keyup(function() {
        var searchBox = $("#${table_id}_filter").find("label").find("input");
        $("#${table_id}").removeHighlight();
        if (searchBox.val() != "")
          $("#${table_id}").find("td").highlight(searchBox.val());
      });

    });
</script>

<div id="${tabContentId}">

	<c:choose>

		<c:when test="${fn:length(model.allProducts) eq 0}">
			<span
				style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
				Sorry no results found matching your search request </span>
		</c:when>

		<c:otherwise>

			<button class="refreshResults">
				Refresh
			</button>
			<button class="rowEditButton editProduct">
				Edit Product
			</button>
			<button class="rowEditButton deleteProduct">
				Delete Product
			</button>

			<table id="${table_id}" class="dataTable productsTable">
				<thead>
					<tr>
						<th style="display: none"></th>
						<c:if test="${model.productFields.productNumber.hidden != true}">
							<th>${model.productFields.productNumber.displayName}</th>
						</c:if>
						<c:if test="${model.productFields.collectionNumber.hidden != true}">
							<th>${model.productFields.collectionNumber.displayName}</th>
						</c:if>
						<c:if test="${model.productFields.bloodGroup.hidden != true}">
							<th>${model.productFields.bloodGroup.displayName}</th>
						</c:if>
						<c:if test="${model.productFields.productType.hidden != true}">
							<th>${model.productFields.productType.displayName}</th>
						</c:if>
						<c:if test="${model.productFields.isAvailable.hidden != true}">
							<th>${model.productFields.isAvailable.displayName}</th>
						</c:if>
						<c:if test="${model.productFields.isQuarantined.hidden != true}">
							<th>${model.productFields.isQuarantined.displayName}</th>
						</c:if>
						<c:if test="${model.productFields.createdOn.hidden != true}">
							<th>${model.productFields.createdOn.displayName}</th>
						</c:if>
						<c:if test="${model.productFields.expiresOn.hidden != true}">
							<th>${model.productFields.expiresOn.displayName}</th>
						</c:if>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="product" items="${model.allProducts}">
						<tr>
							<td style="display: none">${product.id}</td>
							<c:if test="${model.productFields.productNumber.hidden != true}">
								<td>${product.productNumber}</td>
							</c:if>
							<c:if test="${model.productFields.collectionNumber.hidden != true}">
								<td>${product.collectionNumber}</td>
							</c:if>
							<c:if test="${model.productFields.bloodGroup.hidden != true}">
								<td>${product.bloodGroup}</td>
							</c:if>
							<c:if test="${model.productFields.productType.hidden != true}">
								<td>${product.productType}</td>
							</c:if>
							<c:if test="${model.productFields.isAvailable.hidden != true}">
								<td>${product.isAvailable}</td>
							</c:if>
							<c:if test="${model.productFields.isQuarantined.hidden != true}">
								<td>${product.isQuarantined}</td>
							</c:if>
							<c:if test="${model.productFields.createdOn.hidden != true}">
								<td>${product.createdOn}</td>
							</c:if>
							<c:if test="${model.productFields.expiresOn.hidden != true}">
								<td>${product.expiresOn}</td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<button class="refreshResults">
				Refresh
			</button>
			<button class="rowEditButton editProduct">
				Edit Product
			</button>
			<button class="rowEditButton deleteProduct">
				Delete Product
			</button>

		</c:otherwise>
	</c:choose>

	<div class="editRowDiv" style="display: none;">
		<span class="closeButton">X</span>
		<div id="${productsTableEditRowDivId}">	
		</div>
	</div>

</div>

<div id="${deleteProductConfirmDialogId}" style="display: none;">Are
	you sure you want to delete this Product?</div>
