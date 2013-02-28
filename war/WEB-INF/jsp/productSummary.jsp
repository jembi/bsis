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
<c:set var="discardConfirmDialogId">discardConfirmDialog-${unique_page_id}</c:set>

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

        $("#${tabContentId}").find(".testResultsForProductButton").button(
            {
              icons : {
          			primary : 'ui-icon-bookmark'
        			}
            }).click(function() {

            $("#${tabContentId}").bind("testResultsHistorySuccess", emptyChildContent);

  	        fetchContent("testResultsForProduct.html",
              					 {productId: "${model.product.id}"},
              					 $("#${childContentId}")
  	        						);
        });

        $("#${tabContentId}").find(".printButton").button({
          icons : {
            primary : 'ui-icon-print'
          }
        }).click(function() {
          $("#${mainContentId}").find(".printableArea").printArea();
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

        $("#${tabContentId}").find(".discardButton").button({
          icons : {
            primary : 'ui-icon-notice'
          }
        }).click(function() {
          $("#${discardConfirmDialogId}").dialog(
              {
                modal : true,
                title : "Confirm Discard",
                buttons : {
                  "Discard" : function() {
                    discardProduct("${model.product.id}", notifyParentDone);
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
	<div id="${mainContentId}">

		<div class="summaryPageButtonSection" style="text-align: right;">
			<button type="button" class="doneButton">
				Done
			</button>
			<button type="button" class="editButton">
				Edit
			</button>
			<button type="button" class="testResultsForProductButton">
				Test results for product
			</button>
			<!-- button type="button" class="productLabelButton">
				Product Label
			</button-->
			<button type="button" class="discardButton">
				Discard product
			</button>
			<button type="button" class="deleteButton">
				Delete
			</button>
			<button type="button" class="printButton">
				Print
			</button>
		</div>

		<br />
		<br />

		<div class="formInTabPane printableArea">
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
					<label style="width: auto;">${model.product.createdOn}</label>
				</div>
			</c:if>
			<c:if test="${model.productFields.expiresOn.hidden != true }">
				<div>
					<label>${model.productFields.expiresOn.displayName}</label>
					<label style="width: auto;">${model.product.expiresOn} (${product.expiryStatus})</label>
				</div>
			</c:if>
			<c:if test="${model.productFields.productType.hidden != true }">
				<div>
					<label>${model.productFields.productType.displayName}</label>
					<label>${model.product.productType.productType}</label>
				</div>
			</c:if>
			<c:if test="${model.productFields.status.hidden != true }">
				<div>
					<label>${model.productFields.status.displayName}</label>
					<label>${model.product.status}</label>
				</div>
			</c:if>
			<c:if test="${model.productFields.bloodGroup.hidden != true }">
				<div>
					<label>${model.productFields.bloodGroup.displayName}</label>
					<label>${model.product.bloodGroup}</label>
				</div>
			</c:if>
			<c:if test="${model.productFields.bloodGroup.hidden != true }">
				<div>
					<label>${model.productFields.age.displayName}</label>
					<label>${model.product.age}</label>
				</div>
			</c:if>
			<c:if test="${model.productFields.productVolume.hidden != true }">
				<div>
					<label>${model.productFields.productVolume.displayName}</label>
					<label>${model.product.productVolume} ml</label>
				</div>
			</c:if>
			<c:if test="${model.productFields.notes.hidden != true }">
				<div>
					<label>${model.productFields.notes.displayName}</label>
					<label>${model.product.notes}</label>
				</div>
			</c:if>
			<div>
				<label>${model.productFields.lastUpdatedTime.displayName}</label>
				<label style="width: auto;">${model.product.lastUpdated}</label>
			</div>
			<div>
				<label>${model.productFields.lastUpdatedBy.displayName}</label>
				<label style="width: auto;">${model.product.lastUpdatedBy}</label>
			</div>
			<hr />
		</div>
	</div>

	<br />
	<br />

	<div id="${childContentId}"></div>
</div>

<div id="${deleteConfirmDialogId}" style="display: none;">Are
	you sure you want to delete this Product?</div>

<div id="${discardConfirmDialogId}" style="display: none;">Are
	you sure you want to discard this Product?</div>
	