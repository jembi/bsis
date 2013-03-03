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

        $("#${tabContentId}").find(".listIssuedProductsButton").button(
            {
              icons : {
          			primary : 'ui-icon-document'
        			}
            }).click(function() {

            $("#${tabContentId}").bind("listIssuedProductsDone", emptyChildContent);

            showLoadingImage($("#${childContentId}"));
  	        fetchContent("listIssuedProductsForRequest.html",
              					 {requestId: "${model.request.id}"},
              					 $("#${childContentId}")
  	        						);
        });

        $("#${tabContentId}").find(".findMatchingProductsButton").button(
            {
              icons : {
          			primary : 'ui-icon-search'
        			}
            }).click(function() {

            $("#${tabContentId}").bind("productIssueSuccess", productIssueSuccess);
            $("#${tabContentId}").bind("productIssueCancel", emptyChildContent);

            showLoadingImage($("#${childContentId}"));
  	        fetchContent("findMatchingProductsForRequest.html",
              					 {requestId: "${model.request.id}"},
              					 $("#${childContentId}")
  	        						);
        });

        $("#${tabContentId}").find(".addCompatibilityResultButton").button(
            {
              icons : {
          			primary : 'ui-icon-plus'
        			}
            }).click(function() {

            $("#${tabContentId}").bind("editCompatibilityTestSuccess", addCompatibilityResultSuccess);
            $("#${tabContentId}").bind("editCompatibilityTestCancel", emptyChildContent);

            showLoadingImage($("#${childContentId}"));
  	        fetchContent("editCompatibilityTestFormGenerator.html",
              					 {requestId: "${model.request.id}"},
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

        function productIssueSuccess() {
          emptyChildContent();
          refetchContent("${model.refreshUrl}", $("#${tabContentId}"));
        }

        function addCompatibilityResultSuccess() {
          emptyChildContent();
          refetchContent("${model.refreshUrl}", $("#${tabContentId}"));
        }

				function emptyChildContent() {
				  $("#${childContentId}").html("");
				}

				function hideExtraFields() {
				  $("#${mainContentId}").find(".showMoreSection").hide();
				  $("#${mainContentId}").find(".showMoreButton").show();
				  $("#${mainContentId}").find(".showLessButton").hide();
				}

				function showExtraFields() {
				  $("#${mainContentId}").find(".showMoreSection").show();
				  $("#${mainContentId}").find(".showLessButton").show();
				  $("#${mainContentId}").find(".showMoreButton").hide();
				}

				$("#${mainContentId}").find(".showMoreButton").button({icons: {primary: 'ui-icon-plus'}}).click(showExtraFields);
				$("#${mainContentId}").find(".showLessButton").button({icons: {primary: 'ui-icon-minus'}}).click(hideExtraFields);

				hideExtraFields();
      });
</script>

<div id="${tabContentId}">
	<div id="${mainContentId}">
		<div class="summaryPageButtonSection" style="text-align: right;">
			<button type="button" class="cancelButton">
				Done
			</button>
			<button type="button" class="listIssuedProductsButton">
				List issued products
			</button>
			<button type="button" class="findMatchingProductsButton">
				Find matching products
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
			<button type="button" class="addCompatibilityResultButton">
				Add compatibility result
			</button>
		</div>

		<br />
		<br />

		<div class="tipsBox ui-state-highlight">
			<p>
				${model['requests.findpending.requestsummary']}
			</p>
		</div>

		<div class="printableArea">
			<br />
			<div class="formInTabPane">
				<div id="${requestSummaryBarcodeId}"></div>	
				<c:if test="${model.requestFields.requestDate.hidden != true }">
					<div>
						<label>${model.requestFields.requestDate.displayName}</label>
						<label style="width: auto;">${model.request.requestDate}</label>
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
				<c:if test="${model.requestFields.requestType.hidden != true }">
					<div>
						<label>${model.requestFields.requestType.displayName}</label>
						<label>${model.request.requestType.requestType}</label>
					</div>
				</c:if>
				<c:if test="${model.requestFields.numUnitsRequested.hidden != true }">
					<div>
						<label>${model.requestFields.numUnitsRequested.displayName}</label>
						<label>${model.request.numUnitsRequested}</label>
					</div>
				</c:if>
				<c:if test="${model.requestFields.volume.hidden != true }">
					<div>
						<label>${model.requestFields.volume.displayName}</label>
						<label>${model.request.volume} ml</label>
					</div>
				</c:if>
				<c:if test="${model.requestFields.totalVolumeRequested.hidden != true }">
					<div>
						<label>${model.requestFields.totalVolumeRequested.displayName}</label>
						<label>${model.request.totalVolumeRequested} ml</label>
					</div>
				</c:if>
				<c:if test="${model.requestFields.issuedQuantity.hidden != true }">
					<div>
						<label>${model.requestFields.issuedQuantity.displayName}</label>
						<label>${model.request.issuedQuantity}</label>
					</div>
				</c:if>
				<c:if test="${model.requestFields.totalVolumeIssued.hidden != true }">
					<div>
						<label>${model.requestFields.totalVolumeIssued.displayName}</label>
						<label>${model.request.totalVolumeIssued} ml</label>
					</div>
				</c:if>
				<c:if test="${model.requestFields.totalVolumePending.hidden != true }">
					<div>
						<label>${model.requestFields.totalVolumePending.displayName}</label>
						<label>${model.request.totalVolumePending < 0 ? "" + (-model.request.totalVolumePending) : model.request.totalVolumePending} ml
						${model.request.totalVolumePending < 0 ? " Overissued" : ""}</label>
					</div>
				</c:if>
				<c:if test="${model.requestFields.requestSite.hidden != true }">
					<div>
						<label>${model.requestFields.requestSite.displayName}</label>
						<label>${model.request.requestSite.name}</label>
					</div>
				</c:if>
			</div>
			<div class="formInTabPane showMoreSection">
				<c:if test="${model.requestFields.patientNumber.hidden != true }">
					<div>
						<label>${model.requestFields.patientNumber.displayName}</label>
						<label>${model.request.patientNumber}</label>
					</div>
				</c:if>
				<c:if test="${model.requestFields.patientFirstName.hidden != true }">
					<div>
						<label>${model.requestFields.patientFirstName.displayName}</label>
						<label>${model.request.patientFirstName}</label>
					</div>
				</c:if>
				<c:if test="${model.requestFields.patientLastName.hidden != true }">
					<div>
						<label>${model.requestFields.patientLastName.displayName}</label>
						<label>${model.request.patientLastName}</label>
					</div>
				</c:if>
				<c:if test="${model.requestFields.patientBirthDate.hidden != true }">
					<div>
						<label>${model.requestFields.patientBirthDate.displayName}</label>
						<label>${model.request.patientBirthDate}</label>
					</div>
				</c:if>
				<c:if test="${model.requestFields.patientAge.hidden != true }">
					<div>
						<label>${model.requestFields.patientAge.displayName}</label>
						<label>${model.request.patientAge} years</label>
					</div>
				</c:if>
				<c:if test="${model.requestFields.patientDiagnosis.hidden != true }">
					<div>
						<label>${model.requestFields.patientDiagnosis.displayName}</label>
						<label>${model.request.patientDiagnosis}</label>
					</div>
				</c:if>
				<c:if test="${model.requestFields.ward.hidden != true }">
					<div>
						<label>${model.requestFields.ward.displayName}</label>
						<label>${model.request.ward}</label>
					</div>
				</c:if>
				<c:if test="${model.requestFields.department.hidden != true }">
					<div>
						<label>${model.requestFields.department.displayName}</label>
						<label>${model.request.department}</label>
					</div>
				</c:if>
				<c:if test="${model.requestFields.hospital.hidden != true }">
					<div>
						<label>${model.requestFields.hospital.displayName}</label>
						<label>${model.request.hospital}</label>
					</div>
				</c:if>
				<c:if test="${model.requestFields.requestedBy.hidden != true }">
					<div>
						<label>${model.requestFields.requestedBy.displayName}</label>
						<label>${model.request.requestedBy}</label>
					</div>
				</c:if>
				<c:if test="${model.requestFields.notes.hidden != true }">
					<div>
						<label>${model.requestFields.notes.displayName}</label>
						<label>${model.request.notes}</label>
					</div>
				</c:if>
				<div>
					<label>${model.requestFields.lastUpdatedTime.displayName}</label>
					<label style="width: auto;">${model.request.lastUpdated}</label>
				</div>
				<div>
					<label>${model.requestFields.lastUpdatedBy.displayName}</label>
					<label style="width: auto;">${model.request.lastUpdatedBy}</label>
				</div>
			</div>
			<hr />
		</div>

		<button class="showMoreButton">Show more</button>
		<button class="showLessButton">Show less</button>

	</div>

	<br />
	<br />

	<div id="${childContentId}"></div>
</div>

<div id="${deleteConfirmDialogId}" style="display: none;">Are
	you sure you want to delete this Product?</div>
