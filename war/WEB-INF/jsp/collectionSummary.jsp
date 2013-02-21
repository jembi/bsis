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
<c:set var="collectionSummaryBarcodeId">collectionSummaryBarcode-${unique_page_id}</c:set>
<c:set var="deleteConfirmDialogId">deleteConfirmDialog-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        $("#${collectionSummaryBarcodeId}").barcode(
					  "${model.collectedSample.collectionNumber}",
						"code128",
						{barWidth: 2, barHeight: 50, fontSize: 15, output: "css"});

        function notifyParentDone() {
          $("#${tabContentId}").parent().trigger("collectionSummarySuccess");
        }

        $("#${tabContentId}").find(".editButton").button(
            {
              icons : {
          			primary : 'ui-icon-pencil'
        			}
            }).click(function() {

            hideMainContent();
            $("#${tabContentId}").bind("editCollectionSuccess", editCollectionDone);
            $("#${tabContentId}").bind("editCollectionCancel", editCollectionDone);

  	        fetchContent("editCollectionFormGenerator.html",
              					 {collectionId: "${model.collectedSample.id}"},
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
                    deleteCollection("${model.collectedSample.id}", notifyParentDone);
                    $(this).dialog("close");
                  },
                  "Cancel" : function() {
                    $(this).dialog("close");
                  }
                }
              });
        });

        function editCollectionDone() {
          emptyChildContent();
          refetchContent("${model.refreshUrl}", $("#${tabContentId}"));
        }

				function emptyChildContent() {
				  $("#${childContentId}").remove();
				}

				function hideMainContent() {
				  $("#${mainContentId}").remove();
				}
      });
</script>

<div id="${tabContentId}">
	<div id="${mainContentId}">

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

		<br />
		<br />

		<div class="tipsBox ui-state-highlight">
			<p>
				${model['collections.findcollection.collectionsummary']}
			</p>
		</div>

		<div class="formInTabPane printableArea">
			<br />
			<div id="${collectionSummaryBarcodeId}"></div>
	
			<c:if test="${model.collectedSampleFields.collectionNumber.hidden != true }">
				<div>
					<label>${model.collectedSampleFields.collectionNumber.displayName}</label>
					<label>${model.collectedSample.collectionNumber}</label>
				</div>
			</c:if>
			<c:if test="${model.collectedSampleFields.donorNumber.hidden != true }">
				<div>
					<label>${model.collectedSampleFields.donorNumber.displayName}</label>
					<c:if test="${not empty model.collectedSample.donorNumber}">
						<label style="width: auto;">${model.collectedSample.donorNumber} (${model.collectedSample.donor.firstName} ${model.collectedSample.donor.lastName})</label>
					</c:if>
				</div>
			</c:if>
			<c:if test="${model.collectedSampleFields.donorType.hidden != true }">
				<div>
					<label>${model.collectedSampleFields.donorType.displayName}</label>
					<label>${model.collectedSample.donorType}</label>
				</div>
			</c:if>
			<c:if test="${model.collectedSampleFields.bloodBagType.hidden != true }">
				<div>
					<label>${model.collectedSampleFields.bloodBagType.displayName}</label>
					<label>${model.collectedSample.bloodBagType}</label>
				</div>
			</c:if>
			<c:if test="${model.collectedSampleFields.collectedOn.hidden != true }">
				<div>
					<label>${model.collectedSampleFields.collectedOn.displayName}</label>
					<label style="width: auto;">${model.collectedSample.collectedOn}</label>
				</div>
			</c:if>
			<c:if test="${model.collectedSampleFields.collectionCenter.hidden != true }">
				<div>
					<label>${model.collectedSampleFields.collectionCenter.displayName}</label>
					<label>${model.collectedSample.collectionCenter}</label>
				</div>
			</c:if>
			<c:if test="${model.collectedSampleFields.collectionSite.hidden != true }">
				<div>
					<label>${model.collectedSampleFields.collectionSite.displayName}</label>
					<label>${model.collectedSample.collectionSite}</label>
				</div>
			</c:if>
			<c:if test="${model.collectedSampleFields.sampleNumber.hidden != true }">
				<div>
					<label>${model.collectedSampleFields.sampleNumber.displayName}</label>
					<label>${model.collectedSample.sampleNumber}</label>
				</div>
			</c:if>
					<c:if test="${model.collectedSampleFields.shippingNumber.hidden != true }">
				<div>
					<label>${model.collectedSampleFields.shippingNumber.displayName}</label>
					<label>${model.collectedSample.shippingNumber}</label>
				</div>
			</c:if>
			<c:if test="${model.collectedSampleFields.notes.hidden != true }">
				<div>
					<label>${model.collectedSampleFields.notes.displayName}</label>
					<label>${model.collectedSample.notes}</label>
				</div>
			</c:if>
			<div>
				<label>${model.collectedSampleFields.lastUpdatedTime.displayName}</label>
				<label style="width: auto;">${model.collectedSample.lastUpdated}</label>
			</div>
			<div>
				<label>${model.collectedSampleFields.lastUpdatedBy.displayName}</label>
				<label style="width: auto;">${model.collectedSample.lastUpdatedBy}</label>
			</div>
			<hr />
		</div>
	</div>

	<br />
	<br />

	<div id="${childContentId}"></div>
</div>

<div id="${deleteConfirmDialogId}" style="display: none;">Are
	you sure you want to delete this Collection?</div>
