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
<c:set var="requestSummaryBarcodeId">donorSummaryBarcode-${unique_page_id}</c:set>
<c:set var="deleteConfirmDialogId">deleteConfirmDialog-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        $("#${requestSummaryBarcodeId}").barcode(
					  "${model.request.requestNumber}",
						"code128",
						{barWidth: 2, barHeight: 50, fontSize: 15, output: "css"});

        function notifyParentDone() {
          $("#${tabContentId}").parent().trigger("requestSummarySuccess");
        }

        $("#${tabContentId}").find(".editButton").button(
            {
              icons : {
          			primary : 'ui-icon-pencil'
        			}
            }).click(function() {

            $("#${tabContentId}").bind("editRequestSuccess", editRequestSuccess);
            $("#${tabContentId}").bind("editRequestCancel", emptyChildContent);

  	        fetchContent("editRequestFormGenerator.html",
              					 {requestId: "${model.request.id}"},
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

        $("#${tabContentId}").find(".cancelButton").button({
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
                    deleteRequest("${model.request.id}", notifyParentDone);
                    $(this).dialog("close");
                  },
                  "Cancel" : function() {
                    $(this).dialog("close");
                  }
                }
              });
        });

        function editRequestSuccess() {
          emptyChildContent();
          refetchContent("${model.refreshUrl}", $("#${tabContentId}"));
        }

				function emptyChildContent() {
				  $("#${childContentId}").html("");
				}

      });
</script>

<div id="${tabContentId}">
	<div id="${mainContentId}" class="formInTabPane">

		<div class="summaryPageButtonSection" style="text-align: right;">
			<button type="button" class="cancelButton">
				Done
			</button>
			<button type="button" class="editButton">
				Edit
			</button>
			<button type="button" class="deleteButton">
				Delete
			</button>
			<button type="button" class="printButton">
				Print
			</button>
		</div>

		<div id="${requestSummaryBarcodeId}"></div>

		<c:if test="${model.requestFields.requestDate.hidden != true }">
			<div>
				<label>${model.requestFields.requestDate.displayName}</label>
				<label>${model.request.requestDate}</label>
			</div>
		</c:if>
		<c:if test="${model.requestFields.requiredDate.hidden != true }">
			<div>
				<label>${model.requestFields.requiredDate.displayName}</label>
				<label>${model.request.requiredDate}</label>
			</div>
		</c:if>
		<c:if test="${model.requestFields.bloodGroup.hidden != true }">
			<div>
				<label>${model.requestFields.bloodGroup.displayName}</label>
				<label>${model.request.bloodGroup}</label>
			</div>
		</c:if>
		<c:if test="${model.requestFields.productType.hidden != true }">
			<div>
				<label>${model.requestFields.productType.displayName}</label>
				<label>${model.request.productType.productType}</label>
			</div>
		</c:if>
		<c:if test="${model.requestFields.requestedQuantity.hidden != true }">
			<div>
				<label>${model.requestFields.requestedQuantity.displayName}</label>
				<label>${model.request.requestedQuantity}</label>
			</div>
		</c:if>
		<c:if test="${model.requestFields.issuedQuantity.hidden != true }">
			<div>
				<label>${model.requestFields.issuedQuantity.displayName}</label>
				<label>${model.request.issuedQuantity}</label>
			</div>
		</c:if>
		<c:if test="${model.requestFields.requestSite.hidden != true }">
			<div>
				<label>${model.requestFields.requestSite.displayName}</label>
				<label>${model.request.requestSite.name}</label>
			</div>
		</c:if>
		<c:if test="${model.requestFields.patientName.hidden != true }">
			<div>
				<label>${model.requestFields.patientName.displayName}</label>
				<label>${model.request.patientName}</label>
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
