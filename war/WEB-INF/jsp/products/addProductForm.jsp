<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="addProductFormId">addProductForm-${unique_page_id}</c:set>
<c:set var="addProductFormBarcodeId">addProductFormBarcode-${unique_page_id}</c:set>
<c:set var="addProductFormProductTypesId">addProductFormProductType-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        function notifyParentSuccess() {
						// let the parent know we are done
						$("#${tabContentId}").parent().trigger("editProductSuccess");
				}

        function notifyParentCancel() {
					// let the parent know we are done
					$("#${tabContentId}").parent().trigger("editProductCancel");
				}

        $("#${tabContentId}").find(".addProductButton").button({
          icons : {
            primary : 'ui-icon-plusthick'
          }
        }).click(
            function() {
                addNewProduct($("#${addProductFormId}")[0],
                    									"${tabContentId}", notifyParentSuccess);
            });

        $("#${addProductFormId}").find(".productType").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${addProductFormId}").find(".productType").change(function() {
          console.log("changed");
          var selectedValue = getProductTypeSelector().val();
          var selectedOption = $(this).find('option[value="' + selectedValue + '"]');
          var createdOn = $("#${addProductFormId}").find(".createdOn");
          if (createdOn.val() === undefined ||
              createdOn.val().trim() === "") {
            createdOn.datetimepicker("setDate", new Date());
          }
          var createdOnVal = createdOn.datetimepicker("getDate");
          console.log(selectedOption.data("expiryintervalminutes")*60*1000);
          var expiryDate = new Date(createdOnVal.getTime() + selectedOption.data("expiryintervalminutes")*60*1000);
          console.log(expiryDate);
          $("#${addProductFormId}").find(".expiresOn")
          												 .datetimepicker('setDate', expiryDate);
        });

        $("#${addProductFormId}").find(".createdOn").datetimepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -365,
          maxDate : 1,
          dateFormat : "mm/dd/yy",
          timeFormat : "hh:mm:ss tt",
          yearRange : "c-100:c0",
        });

        $("#${addProductFormId}").find(".expiresOn").datetimepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -1,
          maxDate : 365,
          dateFormat : "mm/dd/yy",
          timeFormat : "hh:mm:ss tt",
          yearRange : "c-100:c+1",
        });

        $("#${tabContentId}").find(".clearFormButton")
        										 .button()
        										 .click(refetchForm);

        function refetchForm() {
          $.ajax({
            url: "${refreshUrl}",
            data: {},
            type: "GET",
            success: function (response) {
              			 	 $("#${tabContentId}").replaceWith(response);
            				 },
            error:   function (response) {
											 showErrorMessage("Something went wrong. Please try again.");
            				 }
            
          });
        }

        if ("${firstTimeRender}" == "true") {
        	$("#${tabContentId}").find('textarea[name="notes"]').html("${productFields.notes.defaultValue}");
        	setDefaultValueForSelector(getProductTypeSelector(), "${productFields.productType.defaultValue}");
        }

        function getProductTypeSelector() {
          return $("#${tabContentId}").find('select[name="productType"]').multiselect();
        }
      });
</script>

<div id="${tabContentId}">

	<c:if test="${!empty success && !success}">
		<jsp:include page="../common/errorBox.jsp">
			<jsp:param name="errorMessage" value="${errorMessage}" />
		</jsp:include>
	</c:if>

	<form:form method="POST" commandName="addProductForm"
		class="formInTabPane" id="${addProductFormId}">
		<c:if test="${productFields.collectionNumber.hidden != true }">
			<div>
				<form:label path="collectionNumber">${productFields.collectionNumber.displayName}</form:label>
				<form:input path="collectionNumber" value="${firstTimeRender ? productFields.collectionNumber.defaultValue : ''}" />
				<form:errors class="formError"
					path="product.collectionNumber" delimiter=", "></form:errors>
				<form:errors class="formError"
					path="product.collectedSample" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${productFields.productType.hidden != true }">
			<div>
				<form:label path="productType">${productFields.productType.displayName}</form:label>
				<form:select path="productType" id="${addProductFormProductTypesId}" class="productType">
					<form:option value="">&nbsp;</form:option>
					<c:forEach var="productType" items="${productTypes}">
						<form:option value="${productType.id}"
							data-expiryintervalminutes="${productType.expiryIntervalMinutes}">
							${productType.productType}
						</form:option>
					</c:forEach>
				</form:select>
				<form:errors class="formError" path="product.productType"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${productFields.createdOn.hidden != true }">
			<div>
				<form:label path="createdOn">${productFields.createdOn.displayName}</form:label>
				<form:input path="createdOn" class="createdOn" value="${firstTimeRender ? productFields.createdOn.defaultValue : ''}" />
				<form:errors class="formError" path="product.createdOn"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${productFields.expiresOn.hidden != true }">
			<div>
				<form:label path="expiresOn">${productFields.expiresOn.displayName}</form:label>
				<form:input path="expiresOn" class="expiresOn" value="${firstTimeRender ? productFields.expiresOn.defaultValue : ''}" />
				<form:errors class="formError" path="product.expiresOn"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${productFields.notes.hidden != true }">
			<div>
				<form:label path="notes" class="labelForTextArea">${productFields.notes.displayName}</form:label>
				<form:textarea path="notes" />
				<form:errors class="formError" path="product.notes"
					delimiter=", "></form:errors>
			</div>
		</c:if>
	</form:form>

	<div style="margin-left: 200px;">
		<label></label>
		<button type="button" class="addProductButton">
			Save
		</button>
		<button type="button" class="clearFormButton">
			Clear form
		</button>
	</div>

</div>
