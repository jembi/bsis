<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<c:set var="table_id">${model.tableName}</c:set>

<script>
  var table_id = "${table_id}";
  var requestsTable = $("#" + table_id).dataTable({
    "bJQueryUI" : true
  });

  $("#" + table_id + " tbody").dblclick(
      function(event) {

        // remove row_selected class everywhere
        $(requestsTable.fnSettings().aoData).each(function() {
          $(this.nTr).removeClass('row_selected');
        });

        // add row_selected class to the current row
        $(event.target.parentNode).addClass('row_selected');

        var elements = $(event.target.parentNode).children();
        if (elements[0].getAttribute("class") === "dataTables_empty") {
          return;
        }

        var requestId = elements[0].innerHTML;

        generateEditForm("editRequestFormGenerator.html", {
          requestNumber : requestId,
          isDialog : "yes"
        }, updateExistingRequest, "Edit Request: " + elements[1].innerHTML
            + " " + elements[2].innerHTML, 'requestsTable',
            decorateEditRequestDialog, 550, 500);
      });
</script>

<br />
<jsp:include page="addRequest.jsp" flush="true" />
<br />
<br />

<table id="${table_id}" class="dataTable collectionsTable">
	<thead>
		<tr>
			<th>${model.requestNoDisplayName}</th>
			<c:if test="${model.showdateRequested==true}">
				<th>${model.requestDateDisplayName}</th>
			</c:if>
			<c:if test="${model.showdateRequired==true}">
				<th>${model.requiredDateDisplayName}</th>
			</c:if>
			<c:if test="${model.showsite==true}">
				<th>${model.siteDisplayName}</th>
			</c:if>
			<c:if test="${model.showabo==true}">
				<th>${model.aboDisplayName}</th>
			</c:if>
			<c:if test="${model.showrhd==true}">
				<th>${model.rhdDisplayName}</th>
			</c:if>
			<c:if test="${model.showproductType==true}">
				<th>${model.productTypeDisplayName}</th>
			</c:if>
			<c:if test="${model.showquantity==true}">
				<th>${model.quantityDisplayName}</th>
			</c:if>
			<c:if test="${model.showstatus==true}">
				<th>${model.statusDisplayName}</th>
			</c:if>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="request" items="${model.allRequests}">
			<tr>
				<td>${request.requestNumber}</td>
				<c:if test="${model.showdateRequested==true}">
					<td>${request.dateRequested}</td>
				</c:if>
				<c:if test="${model.showdateRequired==true}">
					<td>${request.dateRequired}</td>
				</c:if>
				<c:if test="${model.showsite == true}">
					<td>${request.site}</td>
				</c:if>
				<c:if test="${model.showabo == true}">
					<td>${request.abo}</td>
				</c:if>
				<c:if test="${model.showrhd == true}">
					<td>${request.rhd}</td>
				</c:if>
				<c:if test="${model.showproductType == true}">
					<td>${request.productType}</td>
				</c:if>
				<c:if test="${model.showquantity == true}">
					<td>${request.quantity}</td>
				</c:if>
				<c:if test="${model.showstatus == true}">
					<td>${request.status}</td>
				</c:if>
			</tr>
		</c:forEach>
	</tbody>
</table>
