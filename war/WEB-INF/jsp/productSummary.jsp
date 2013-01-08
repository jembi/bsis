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
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>
<c:set var="childContentId">childContent-${unique_page_id}</c:set>
<c:set var="productSummaryBarcodeId">donorSummaryBarcode-${unique_page_id}</c:set>
<c:set var="deleteConfirmDialogId">deleteConfirmDialog-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        $("#${productSummaryBarcodeId}").barcode(
					  "${model.product.productNumber}",
						"code128",
						{barWidth: 2, barHeight: 50, fontSize: 15, output: "css"});

        function notifyParentDone() {
          $("#${tabContentId}").parent().trigger("productSummarySuccess");
        }

        $("#${tabContentId}").find(".editButton").button(
            {
              icons : {
          			primary : 'ui-icon-pencil'
        			}
            }).click(function() {

            $("#${tabContentId}").bind("editProductSuccess", editProductSuccess);
            $("#${tabContentId}").bind("editProductCancel", emptyChildContent);

  	        fetchContent("editProductFormGenerator.html",
              					 {productId: "${model.product.id}"},
              					 $("#${childContentId}")
  	        						);
        });

        $("#${tabContentId}").find(".printButton").button({
          icons : {
            primary : 'ui-icon-print'
          }
        }).click(function() {
          $("#${mainContentId}").printArea();
        });

        $("#${tabContentId}").find(".doneButton").button({
          icons : {
            primary : 'ui-icon-check'
          }
        }).click(function() {
          notifyParentDone();
        });

        $("#${tabContentId}").find(".deleteButton").button({
          icons : {
            primary : 'ui-icon-trash'
          }
        }).click(function() {
          $("#${deleteConfirmDialogId}").dialog(
              {
                modal : true,
                title : "Confirm Delete",
                buttons : {
                  "Delete" : function() {
                    deleteProduct("${model.product.id}", notifyParentDone);
                    $(this).dialog("close");
                  },
                  "Cancel" : function() {
                    $(this).dialog("close");
                  }
                }
              });
        });

        function editProductSuccess() {
          emptyChildContent();
          refetchContent("${model.refreshUrl}", $("#${tabContentId}"));
        }

        function editCollectionSuccess() {
          emptyChildContent();
        }

				function emptyChildContent() {
				  $("#${childContentId}").html("");
				}

      });
</script>

<div id="${tabContentId}">
	<div id="${mainContentId}" class="formInTabPane">

		<div class="summaryPageButtonSection" style="text-align: right;">
			<button type="button" class="doneButton">
				Done
			</button>
			<button type="button" class="editButton">
				Edit
			</button>
			<!-- button type="button" class="productLabelButton">
				Product Label
			</button-->
			<button type="button" class="deleteButton">
				Delete
			</button>
			<button type="button" class="printButton">
				Print
			</button>
		</div>

		<br />
		<br />

		<div id="${productSummaryBarcodeId}"></div>

		<c:if test="${model.productFields.productNumber.hidden != true }">
			<div>
				<label>${model.productFields.productNumber.displayName}</label>
				<label>${model.product.productNumber}</label>
			</div>
		</c:if>
		<c:if test="${model.productFields.collectionNumber.hidden != true }">
			<div>
				<label>${model.productFields.collectionNumber.displayName}</label>
				<label>${model.product.collectionNumber}</label>
			</div>
		</c:if>
		<c:if test="${model.productFields.createdOn.hidden != true }">
			<div>
				<label>${model.productFields.createdOn.displayName}</label>
				<label>${model.product.createdOn}</label>
			</div>
		</c:if>
		<c:if test="${model.productFields.expiresOn.hidden != true }">
			<div>
				<label>${model.productFields.expiresOn.displayName}</label>
				<label>${model.product.expiresOn}</label>
			</div>
		</c:if>
		<c:if test="${model.productFields.productType.hidden != true }">
			<div>
				<label>${model.productFields.productType.displayName}</label>
				<label>${model.product.productType.productTypeName}</label>
			</div>
		</c:if>
		<c:if test="${model.productFields.isAvailable.hidden != true }">
			<div>
				<label>${model.productFields.isAvailable.displayName}</label>
				<label>${model.product.isAvailable ? "Yes" : "No"}</label>
			</div>
		</c:if>
	</div>

	<hr />
	<br />
	<br />

	<div id="${childContentId}"></div>
</div>

<div id="${deleteConfirmDialogId}" style="display: none;">Are
	you sure you want to delete this Product?</div>
