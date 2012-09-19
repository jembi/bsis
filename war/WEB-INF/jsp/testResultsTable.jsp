<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<c:set var="table_id">${model.tableName}</c:set>

<script>
  var table_id = "${table_id}";
  var testResultsTable = $("#" + table_id).dataTable({
    "bJQueryUI" : true
  });

  $("#" + table_id + " tbody").dblclick(
      function(event) {

        // remove row_selected class everywhere
        $(testResultsTable.fnSettings().aoData).each(function() {
          $(this.nTr).removeClass('row_selected');
        });

        // add row_selected class to the current row
        $(event.target.parentNode).addClass('row_selected');

        var elements = $(event.target.parentNode).children();
        if (elements[0].getAttribute("class") === "dataTables_empty") {
          return;
        }

        var collectionId = elements[0].innerHTML;

        generateEditForm("editTestResultFormGenerator.html", {
          collectionNumber : collectionId,
          isDialog : "yes"
        }, updateExistingTestResult, "Edit Test Result: "
            + elements[1].innerHTML + " " + elements[2].innerHTML,
            'testResultsTable', decorateEditTestResultDialog, 550, 500);
      });
</script>

<jsp:include page="addTestResultButton.jsp" flush="true" />
<br />
<br />

<table id="${table_id}" class="dataTable collectionsTable">
	<thead>
		<tr>
			<th>${model.collectionNoDisplayName}</th>
			<c:if test="${model.showdateTested==true}">
				<th>${model.dateTestedDisplayName}</th>
			</c:if>
			<c:if test="${model.showhiv==true}">
				<th>${model.hivDisplayName} Reactive</th>
			</c:if>
			<c:if test="${model.showhbv==true}">
				<th>${model.hbvDisplayName} Reactive</th>
			</c:if>
			<c:if test="${model.showhcv==true}">
				<th>${model.hcvDisplayName} Reactive</th>
			</c:if>
			<c:if test="${model.showsyphilis==true}">
				<th>${model.syphilisDisplayName} Reactive</th>
			</c:if>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="testResult" items="${model.allTestResults}">
			<tr>
				<td>${testResult.collectionNumber}</td>
				<c:if test="${model.showdateTested==true}">
					<td>${testResult.dateTested}</td>
				</c:if>
				<c:if test="${model.showhiv == true}">
					<td><c:if test="${testResult.hiv =='reactive'}">
						&#10003;
					</c:if></td>
				</c:if>
				<c:if test="${model.showhbv == true}">
					<td><c:if test="${testResult.hbv =='reactive'}">
						&#10003;
					</c:if></td>
				</c:if>
				<c:if test="${model.showhcv == true}">
					<td><c:if test="${testResult.hcv =='reactive'}">
						&#10003;
					</c:if></td>
				</c:if>
				<c:if test="${model.showsyphilis == true}">
					<td><c:if test="${testResult.syphilis =='reactive'}">
						&#10003;
					</c:if></td>
				</c:if>
			</tr>
		</c:forEach>
	</tbody>
</table>
