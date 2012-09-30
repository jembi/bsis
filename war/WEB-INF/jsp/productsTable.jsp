<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<c:set var="table_id">${model.tableName}</c:set>

<script>
  var table_id = "${table_id}";
  var productsTable = $("#" + table_id).dataTable({
    "bJQueryUI" : true
  });

  $("#${table_id}_filter").find("label").find("input").keyup(function() {
    var searchBox = $("#${table_id}_filter").find("label").find("input");
    $("#" + table_id).removeHighlight();
    if (searchBox.val() != "")
    	$("#" + table_id).find("td").highlight(searchBox.val());
  });
  
  $("#" + table_id + " tbody").dblclick(
      function(event) {

        // remove row_selected class everywhere
        $(productsTable.fnSettings().aoData).each(function() {
          $(this.nTr).removeClass('row_selected');
        });

        // add row_selected class to the current row
        $(event.target.parentNode).addClass('row_selected');

        var elements = $(event.target.parentNode).children();
        if (elements[0].getAttribute("class") === "dataTables_empty") {
          return;
        }

        var productId = elements[0].innerHTML;

        generateEditForm("editProductFormGenerator.html", {
          productNumber : productId,
          isDialog : "yes"
        }, updateExistingProduct, "Edit Product: " + elements[1].innerHTML
            + " " + elements[2].innerHTML, 'productsTable',
            decorateEditProductDialog, 550, 500);
      });
</script>

<c:if
	test="${empty model.showAddProductButton || model.showAddProductButton == true}">
	<jsp:include page="addProductButton.jsp" flush="true" />
	<br />
	<br />
</c:if>

<table id="${table_id}" class="dataTable collectionsTable">
	<thead>
		<tr>
			<th>${model.productNoDisplayName}</th>
			<th>${model.collectionNoDisplayName}</th>
			<c:if test="${model.showproductType==true}">
				<th>${model.productTypeDisplayName}</th>
			</c:if>
			<c:if test="${model.showisIssued==true}">
				<th>${model.isIssuedDisplayName}</th>
			</c:if>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="product" items="${model.allProducts}">
			<tr>
				<td>${product.productNumber}</td>
				<td>${product.collectionNumber}</td>
				<c:if test="${model.showproductType}">
					<td>${product.type}</td>
				</c:if>
				<c:if test="${model.showisIssued==true}">
					<td><c:if test="${product.isIssued == 'no'}">&#10003;</c:if></td>
				</c:if>
			</tr>
		</c:forEach>
	</tbody>
</table>
