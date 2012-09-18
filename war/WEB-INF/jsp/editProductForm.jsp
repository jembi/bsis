<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>
<c:set var="formId"><%=getCurrentTime()%></c:set>


<script>
  $(".addProductButton").button();
  function updateTestResult() {
    addNewTestResult($("#editProductForm-" + '<c:out value="${formId}"/>')[0]);
    $("#editProductForm-" + '<c:out value="${formId}"/>')[0].reset();
  }
  $("#editProductFormTypes-" + '<c:out value="${formId}"/>').multiselect({
    multiple : false,
    selectedList : 1,
    header : false
  });
  function updateProduct() {
    addNewProduct($("#editProductForm-" + '<c:out value="${formId}"/>')[0]);
    $("#editProductForm-" + '<c:out value="${formId}"/>')[0].reset();
  }
</script>

<form:form method="POST" commandName="editProductForm"
	id="editProductForm-${formId}">
	<table>
		<thead>
			<tr>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><form:label path="productNumber">${model.productNoDisplayName}</form:label></td>
				<td><form:input path="productNumber" /></td>
			</tr>
			<tr>
				<td><form:label path="collectionNumber">${model.collectionNoDisplayName}</form:label></td>
				<td><form:input path="collectionNumber" /></td>
			</tr>
			<tr>
				<td><form:label path="types">${model.productTypeDisplayName}</form:label></td>
				<td><form:select path="types"
						id="editProductFormTypes-${formId}" class="editProductFormTypes">
						<form:option value="wholeBlood" label="Whole Blood"
							selected="selected" />
						<form:option value="rcc" label="RCC" />
						<form:option value="ffp" label="ffp" />
						<form:option value="platelets" label="Platelets" />
						<form:option value="partialPlatelets" label="Partial Platelets" />
					</form:select></td>
			</tr>
			<c:if test="${model.isDialog != 'yes' }">
				<tr>
					<td />
					<td><input type="button" value="Add Product"
						class="addProductButton" onclick="updateProduct();" /></td>
				</tr>
			</c:if>
		</tbody>
	</table>
</form:form>
