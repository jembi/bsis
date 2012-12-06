<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="editProductFormDivId">editProductFormDiv-${unique_page_id}</c:set>
<c:set var="editProductFormId">editProductForm-${unique_page_id}</c:set>
<c:set var="editProductFormBarcodeId">editProductFormBarcode-${unique_page_id}</c:set>
<c:set var="editProductFormDonorId">editProductFormDonor-${unique_page_id}</c:set>
<c:set var="editProductFormDonorHiddenId">editProductFormDonorHidden-${unique_page_id}</c:set>
<c:set var="editProductFormCentersId">editProductFormCenters-${unique_page_id}</c:set>
<c:set var="editProductFormSitesId">editProductFormSites-${unique_page_id}</c:set>
<c:set var="editProductFormProductTypeId">editProductFormProductType-${unique_page_id}</c:set>
<c:set var="editProductFormDonorTypeId">editProductFormDonorType-${unique_page_id}</c:set>
<c:set var="updateProductButtonId">updateProductButton-${unique_page_id}</c:set>
<c:set var="deleteProductButtonId">deleteProductButton-${unique_page_id}</c:set>
<c:set var="printButtonId">printButton-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        function notifyParent() {
						// let the parent know we are done
						$("#${editProductFormDivId}").parent().trigger("editProductSuccess");
				}

        $("#${updateProductButtonId}").button({
          icons : {
            primary : 'ui-icon-plusthick'
          }
        }).click(
            function() {
              if ("${model.existingProduct}" == "true")
                updateExistingProduct($("#${editProductFormId}")[0],
                  														"${editProductFormDivId}",
                  														notifyParent);
              else
                addNewProduct($("#${editProductFormId}")[0],
                    									"${editProductFormDivId}", notifyParent);
            });

        $("#${printButtonId}").button({
          icons : {
            primary : 'ui-icon-print'
          }
        }).click(function() {
          $("#${editProductFormId}").printArea();
        });

        $("#${editProductFormId}").find(".bloodGroup").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${editProductFormId}").find(".productType").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${editProductFormId}").find(".createdOn").datepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -36500,
          maxDate : 0,
          dateFormat : "mm/dd/yy",
          yearRange : "c-100:c0",
        });

        $("#${editProductFormId}").find(".expiresOn").datepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -36500,
          maxDate : 60,
          dateFormat : "mm/dd/yy",
          yearRange : "c-100:c+1",
        });

        // second condition required for the case where the form is returned with errors
        var createdOnDatePicker = $("#${editProductFormId}").find(".createdOn");
        if ("${model.existingProduct}" == "false" && createdOnDatePicker.val() == "") {
          createdOnDatePicker.datepicker('setDate', new Date());
        }

        var expiresOnDatePicker = $("#${editProductFormId}").find(".expiresOn");
        if ("${model.existingProduct}" == "false" && expiresOnDatePicker.val() == "") {
          var today = new Date();
          var later = new Date();
          later.setDate(today.getDate() + 30);
          expiresOnDatePicker.datepicker('setDate', later);
        }

        // set the checkboxes if required
        var isAvailableCheckbox = $("#${editProductFormId}").find(".isAvailable");
        if ("${model.existingProduct}" == "false") {
          isAvailableCheckbox.prop("checked", true);
        }

        var isQuarantinedCheckbox = $("#${editProductFormId}").find(".isQuarantined");
        if ("${model.existingProduct}" == "false") {
          isQuarantinedCheckbox.prop("checked", true);
        }

        $("#${editProductFormId}").find(".clearFormButton").button({
          icons : {
            primary : 'ui-icon-grip-solid-horizontal'
          }
        }).click(refetchForm);

        function refetchForm() {
          $.ajax({
            url: "${model.refreshUrl}",
            data: {},
            type: "GET",
            success: function (response) {
              			 	 $("#${editProductFormDivId}").replaceWith(response);
            				 },
            error:   function (response) {
											 showErrorMessage("Something went wrong. Please try again.");
            				 }
            
          });
        }

        $("#${editProductFormBarcodeId}").barcode(
					  "${editProductForm.product.productNumber}-${editProductForm.product.id}",
						"code128",
						{barWidth: 2, barHeight: 50, fontSize: 15, output: "css"});

        copyMirroredFields("${editProductFormId}", JSON.parse('${model.productFields.mirroredFields}'));

      });
</script>

<div id="${editProductFormDivId}">

	<form:form method="POST" commandName="editProductForm"
		class="formInTabPane" id="${editProductFormId}">
		<c:if test="${model.existingProduct}">
			<div id="${editProductFormBarcodeId}"></div>
		</c:if>
		<form:hidden path="id" />
		<c:if test="${model.productFields.productNumber.hidden != true }">
			<div>
				<form:label path="productNumber">${model.productFields.productNumber.displayName}</form:label>
				<form:input path="productNumber" />
				<form:errors class="formError"
					path="product.productNumber" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.productFields.collectionNumber.hidden != true }">
			<div>
				<form:label path="collectionNumber">${model.productFields.collectionNumber.displayName}</form:label>
				<form:input path="collectionNumber" />
				<form:errors class="formError"
					path="product.collectedSample" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.productFields.bloodGroup.hidden != true }">
			<div>
				<form:label path="bloodGroup">${model.productFields.bloodGroup.displayName}</form:label>
				<form:select path="bloodGroup" class="bloodGroup">
					<form:option value="Unknown" label="Unknown" />
					<form:option value="A+" label="A+" />
					<form:option value="A-" label="A-" />
					<form:option value="B+" label="B+" />
					<form:option value="B-" label="B-" />
					<form:option value="AB+" label="AB+" />
					<form:option value="AB-" label="AB-" />
					<form:option value="O+" label="O+" />
					<form:option value="O-" label="O-" />
				</form:select>
				<form:errors class="formError" path="bloodGroup" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.productFields.createdOn.hidden != true }">
			<div>
				<form:label path="createdOn">${model.productFields.createdOn.displayName}</form:label>
				<form:input path="createdOn" class="createdOn" />
				<form:errors class="formError" path="product.createdOn"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.productFields.expiresOn.hidden != true }">
			<div>
				<form:label path="expiresOn">${model.productFields.expiresOn.displayName}</form:label>
				<form:input path="expiresOn" class="expiresOn" />
				<form:errors class="formError" path="product.expiresOn"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.productFields.productType.hidden != true }">
			<div>
				<form:label path="productType">${model.productFields.productType.displayName}</form:label>
				<form:select path="productType" class="productType">
					<form:option value="">&nbsp;</form:option>
					<c:forEach var="productType" items="${model.productTypes}">
						<form:option value="${productType}">${productType}</form:option>
					</c:forEach>
				</form:select>
				<form:errors class="formError" path="product.productType"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${model.productFields.notes.hidden != true }">
			<div>
				<form:label path="notes" class="labelForTextArea">${model.productFields.notes.displayName}</form:label>
				<form:textarea path="notes" maxlength="255" />
				<form:errors class="formError" path="product.notes"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<div>
			<label></label>
			<c:if test="${!(model.existingProduct)}">
				<button type="button" id="${updateProductButtonId}">
					Save and add another
				</button>
				<button type="button" class="clearFormButton">
					Clear form
				</button>
			</c:if>
			<c:if test="${model.existingProduct}">
				<button type="button" id="${updateProductButtonId}"
								class="autoWidthButton">Save</button>
			</c:if>

			<button type="button" id="${printButtonId}">
				Print
			</button>
		</div>
	</form:form>
</div>
