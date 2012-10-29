<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tableContent-${unique_page_id}</c:set>
<c:set var="table_id">productsTable-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        var fnRowSelected = function(node) {
          var elements = $(node).children();
          if (elements[0].getAttribute("class") === "dataTables_empty") {
            return;
          }
          replaceContent("${tabContentId}", "${model.requestUrl}",
              "editProductFormGenerator.html", {
                productNumber : elements[0].innerHTML
              });
        }

        var productsTable = $("#${table_id}").dataTable({
          "bJQueryUI" : true,
          "sDom" : '<"H"lfrT>t<"F"ip>T',
          "oTableTools" : {
            "sRowSelect" : "single",
            "aButtons" : [ "print" ],
            "fnRowSelected" : fnRowSelected
          }
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
					<td><c:if test="${product.isIssued == 'no'}">&#10003;</c:if> <c:if
							test="${product.isIssued == 'yes'}">&#10007;</c:if></td>
				</c:if>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
