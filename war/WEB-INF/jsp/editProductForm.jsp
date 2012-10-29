<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="editProductFormId">editProductForm-${unique_page_id}</c:set>
<c:set var="deleteProductConfirmDialogId">deleteProductConfirmDialog-${unique_page_id}</c:set>
<c:set var="editProductFormProductTypesId">editProductFormProductTypes-${unique_page_id}</c:set>
<c:set var="updateProductButtonId">updateProductButton-${unique_page_id}</c:set>
<c:set var="deleteProductButtonId">deleteProductButton-${unique_page_id}</c:set>
<c:set var="goBackButtonId">goBackButton-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {
        $("#${updateProductButtonId}").button({
          icons : {
            primary : 'ui-icon-plusthick'
          }
        }).click(function() {
          updateExistingProduct($("#${editProductFormId}")[0]);
        });

        $("#${deleteProductButtonId}").button({
          icons : {
            primary : 'ui-icon-minusthick'
          }
        }).click(
            function() {
              $("#${deleteProductConfirmDialogId}").dialog(
                  {
                    modal : true,
                    title : "Confirm Delete",
                    buttons : {
                      "Delete" : function() {
                        var productNumber = $("#${editProductFormId}").find(
                            "[name='productNumber']").val();
                        deleteProduct(productNumber);
                        $(this).dialog("close");
                      },
                      "Cancel" : function() {
                        $(this).dialog("close");
                      }
                    }
                  });
            });

        $("#${goBackButtonId}").button({
          icons : {
            primary : 'ui-icon-circle-arrow-w'
          }
        }).click(function() {
          window.history.back();
          return false;
        });

        $("#${editProductFormProductTypesId}").multiselect(
            {
              multiple : false,
              selectedList : 1,
              header : false
            });
      });
</script>

<div class="editFormDiv">
<form:form method="POST" commandName="editProductForm"
	id="${editProductFormId}">
	<table>
		<thead>
				<tr>
					<td><b>Product</b></td>
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
				<td><form:label path="type">${model.productTypeDisplayName}</form:label></td>
				<td style="padding-left: 10px;"><form:select path="type"
						id="${editProductFormProductTypesId}">
						<form:option value="wholeBlood" label="Whole Blood"
							selected="${model.selectedType == 'wholeBlood' ? 'selected' : ''}" />
						<form:option value="rcc" label="RCC"
							selected="${model.selectedType == 'rcc' ? 'selected' : ''}" />
						<form:option value="ffp" label="ffp"
							selected="${model.selectedType == 'ffp' ? 'selected' : ''}" />
						<form:option value="platelets" label="Platelets"
							selected="${model.selectedType == 'platelets' ? 'selected' : ''}" />
						<form:option value="partialPlatelets" label="Partial Platelets"
							selected="${model.selectedType == 'partialPlatelets' ? 'selected' : ''}" />
					</form:select></td>
			</tr>
			<c:if test="${model.isDialog != 'yes' }">
				<tr>
					<td />
					<td><button type="button" id="${updateProductButtonId}"
							style="margin-left: 10px">Save changes</button>
						<button type="button" id="${deleteProductButtonId}"
							style="margin-left: 10px">Delete</button>
						<button type="button" id="${goBackButtonId}"
							style="margin-left: 10px">Go Back</button></td>
				</tr>
			</c:if>
		</tbody>
	</table>
</form:form>
</div>

<div id="${deleteProductConfirmDialogId}" style="display: none">Are
	you sure you want to delete this Product?</div>