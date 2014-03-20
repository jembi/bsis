<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
    return System.nanoTime();
  }%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="addProductCombinationFormId">addProductForm-${unique_page_id}</c:set>
<c:set var="addProductFormBarcodeId">addProductFormBarcode-${unique_page_id}</c:set>
<c:set var="addProductFormProductTypeCombinationsId">addProductFormProductType-${unique_page_id}</c:set>

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
              var expiresOn = {};
              var selectedOption = $("#${addProductCombinationFormId}").find('select[name="productTypeCombination"] option:selected');
              var expiryIntervalByProductType = selectedOption.data("expiryintervalbyproducttype");
              var expiresOnInputs = $("#${addProductCombinationFormId}").find(".productTypeExpiresOnInput");

              for (var index = 0; index < expiresOnInputs.length; ++index) {
                var expiresOnInput = $(expiresOnInputs[index]);
                if (expiryIntervalByProductType !== undefined && expiresOnInput.data("producttypeid") in expiryIntervalByProductType) {
                  expiresOn[expiresOnInput.data("producttypeid")] = expiresOnInput.val();
                }
              }

              console.log(expiresOn);

              $("#${addProductCombinationFormId}").find('input[name="expiresOn"]')
                                                  .val(JSON.stringify(expiresOn));
              addNewProductCombination($("#${addProductCombinationFormId}")[0],
                                           "${tabContentId}", notifyParentSuccess);
            });

        $("#${addProductCombinationFormId}").find(".productTypeCombination").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${addProductCombinationFormId}").find(".productTypeCombination").change(function() {

          var selectedValue = getProductTypeCombinationSelector().val();
          var selectedOption = $(this).find('option[value="' + selectedValue + '"]');
          var expiryIntervalByProductType = selectedOption.data("expiryintervalbyproducttype");
          if (expiryIntervalByProductType === undefined)
            return;

          var createdOn = $("#${addProductCombinationFormId}").find(".createdOn");
          createdOn.datetimepicker("setDate", new Date());
          var createdOnVal = createdOn.datetimepicker("getDate");

          var expiresOnInputs = $("#${addProductCombinationFormId}").find(".productTypeExpiresOnInput");

          // show the expires on inputs for only the product types in the current product type combination
          for (var index = 0; index < expiresOnInputs.length; ++index) {
            var expiresOnInput = $(expiresOnInputs[index]);
            if (expiryIntervalByProductType !== undefined && expiresOnInput.data("producttypeid") in expiryIntervalByProductType) {
              expiresOnInput.parent().show();
              var expiryDate = new Date(createdOnVal.getTime() + expiryIntervalByProductType[expiresOnInput.data("producttypeid")]*60*1000);
              expiresOnInput.datetimepicker('setDate', expiryDate);
            }
            else {
              expiresOnInput.parent().hide();
            }
          }
        });

        $("#${addProductCombinationFormId}").find(".createdOn").datetimepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -365,
          maxDate : 1,
          dateFormat : "dd/mm/yy",
          timeFormat : "hh:mm:ss tt",
          yearRange : "c-100:c0",
        });

        $("#${addProductCombinationFormId}").find(".expiresOn").datetimepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -1,
          maxDate : 365,
          dateFormat : "dd/mm/yy",
          timeFormat : "hh:mm:ss tt",
          yearRange : "c-100:c+1",
        });

        $("#${addProductCombinationFormId}").find(".productTypeExpiresOnInput").datetimepicker({
          changeMonth: true,
          changeYear: true,
          minDate: -1,
          maxDate: 365,
          dateFormat: "dd/mm/yy",
          timeFormat: "hh:mm:ss tt",
          yearRange: "c-100:c+1"
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
          setDefaultValueForSelector(getProductTypeCombinationSelector(), "${productFields.productTypeCombination.defaultValue}");
        }

        function getProductTypeCombinationSelector() {
          return $("#${tabContentId}").find('select[name="productTypeCombination"]').multiselect();
        }

        $("#${addProductCombinationFormId}").find(".productTypeCombination").each(function() {
          $(this).change();
        });

      });
</script>

<sec:authorize access="hasRole(T(utils.PermissionConstants).MANAGE_COMPONENT_COMBINATION)">
<div id="${tabContentId}">

  <c:if test="${!empty success && !success}">
    <jsp:include page="../common/errorBox.jsp">
      <jsp:param name="errorMessage" value="${errorMessage}" />
    </jsp:include>
  </c:if>

  <form:form method="POST" commandName="addProductCombinationForm"
    class="formFormatClass" id="${addProductCombinationFormId}">
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
    <c:if test="${productFields.productTypeCombination.hidden != true }">
      <div>
        <form:label path="productTypeCombination">${productFields.productType.displayName}</form:label>
        <form:select path="productTypeCombination" id="${addProductFormProductTypeCombinationsId}" class="productTypeCombination">
          <form:option value="" selected="selected">&nbsp;</form:option>
          <c:forEach var="productTypeCombination" items="${productTypeCombinations}">
            <form:option value="${productTypeCombination.id}"
              data-expiryintervalbyproducttype="${productTypeCombinationsMap[productTypeCombination.id]}">
              ${productTypeCombination.combinationName}
            </form:option>
          </c:forEach>
        </form:select>
        <form:errors class="formError" path="productTypeCombination"
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
        <form:hidden path="expiresOn" class="expiresOn" value="${firstTimeRender ? productFields.expiresOn.defaultValue : ''}" />
        <form:errors class="formError" path="product.expiresOn"
          delimiter=", "></form:errors>
      </div>
      <c:forEach var="productType" items="${productTypes}">
        <div style="display: none;">
          <label style="vertical-align: middle;">${productType.productTypeNameShort} <br />expires on</label>
          <input class="productTypeExpiresOnInput" data-producttypeid="${productType.id}" />
        </div>
      </c:forEach>
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
</sec:authorize>
