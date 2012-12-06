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
<c:set var="donorSummaryBarcodeId">donorSummaryBarcode-${unique_page_id}</c:set>
<c:set var="deleteConfirmDialogId">deleteConfirmDialog-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        $("#${donorSummaryBarcodeId}").barcode(
					  "${model.donor.donorNumber}",
						"code128",
						{barWidth: 2, barHeight: 50, fontSize: 15, output: "css"});

        function notifyParentDone() {
          $("#${tabContentId}").parent().trigger("donorSummarySuccess");
        }

        $("#${tabContentId}").find(".editButton").button(
            {
              icons : {
          			primary : 'ui-icon-pencil'
        			}
            }).click(function() {

            $("#${tabContentId}").bind("editDonorSuccess", editDonorSuccess);
            $("#${tabContentId}").bind("editDonorCancel", emptyChildContent);

  	        fetchContent("editDonorFormGenerator.html",
              					 {donorId: "${model.donor.id}"},
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

        $("#${tabContentId}").find(".createCollectionButton").button({
          icons : {
            primary : 'ui-icon-disk'
          }
        }).click(function() {
          $("#${tabContentId}").bind("editCollectionSuccess", editCollectionSuccess);
          $("#${tabContentId}").bind("editCollectionCancel", emptyChildContent);

	        fetchContent("addCollectionFormForDonorGenerator.html",
            					 {donorId: "${model.donor.id}"},
            					 $("#${childContentId}")
	        						);
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
                    deleteDonor("${model.donor.id}", notifyParentDone);
                    $(this).dialog("close");
                  },
                  "Cancel" : function() {
                    $(this).dialog("close");
                  }
                }
              });
        });

        function editDonorSuccess() {
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
			<button type="button" class="cancelButton">
				Done
			</button>
			<button type="button" class="editButton">
				Edit
			</button>
			<button type="button" class="createCollectionButton">
				Add Collection
			</button>
			<button type="button" class="deleteButton">
				Delete
			</button>
			<button type="button" class="printButton">
				Print
			</button>
		</div>

		<div id="${donorSummaryBarcodeId}"></div>

		<c:if test="${model.donorFields.donorNumber.hidden != true }">
			<div>
				<label>${model.donorFields.donorNumber.displayName}</label>
				<label>${model.donor.donorNumber}</label>
			</div>
		</c:if>
		<c:if test="${model.donorFields.firstName.hidden != true }">
			<div>
				<label>${model.donorFields.firstName.displayName}</label>
				<label>${model.donor.firstName}</label>
			</div>
		</c:if>
		<c:if test="${model.donorFields.middleName.hidden != true }">
			<div>
				<label>${model.donorFields.middleName.displayName}</label>
				<label>${model.donor.middleName}</label>
			</div>
		</c:if>
		<c:if test="${model.donorFields.lastName.hidden != true }">
			<div>
				<label>${model.donorFields.lastName.displayName}</label>
				<label>${model.donor.lastName}</label>
			</div>
		</c:if>
		<c:if test="${model.donorFields.birthDate.hidden != true }">
			<div>
				<label>${model.donorFields.birthDate.displayName}</label>
				<label>${model.donor.birthDate}</label>
			</div>
		</c:if>
		<c:if test="${model.donorFields.gender.hidden != true }">
			<div>
				<label>${model.donorFields.gender.displayName}</label>
				<label>${model.donor.gender}</label>
			</div>
		</c:if>
		<c:if test="${model.donorFields.bloodGroup.hidden != true }">
			<div>
				<label>${model.donorFields.bloodGroup.displayName}</label>
				<label>${model.donor.bloodGroup}</label>
			</div>
		</c:if>
	</div>

	<hr />
	<br />
	<br />

	<div id="${childContentId}"></div>
</div>

<div id="${deleteConfirmDialogId}" style="display: none;">Are
	you sure you want to delete this Donor?</div>
