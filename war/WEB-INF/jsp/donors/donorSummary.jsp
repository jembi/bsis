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
					  "${donor.donorNumber}",
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

            hideMainContent();
            $("#${tabContentId}").bind("editDonorSuccess", editDonorDone);
            $("#${tabContentId}").bind("editDonorCancel", editDonorDone);

  	        fetchContent("editDonorFormGenerator.html",
              					 {donorId: "${donor.id}"},
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

        $("#${tabContentId}").find(".createCollectionButton").button({
          icons : {
            primary : 'ui-icon-disk'
          }
        }).click(function() {
          $("#${tabContentId}").bind("editCollectionSuccess", editCollectionDone);
          $("#${tabContentId}").bind("editCollectionCancel", editCollectionDone);
					//hideMainContent();
	        fetchContent("addCollectionFormForDonorGenerator.html",
            					 {donorId: "${donor.id}"},
            					 $("#${childContentId}")
	        						);
        });

        $("#${tabContentId}").find(".donorHistoryButton").button({
          icons : {
            primary : 'ui-icon-disk'
          }
        }).click(function() {
          //hideMainContent();
          $("#${tabContentId}").bind("donorHistoryDone", editDonorDone);
	        fetchContent("viewDonorHistory.html",
            					 {donorId: "${donor.id}"},
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
                    deleteDonor("${donor.id}", notifyParentDone);
                    $(this).dialog("close");
                  },
                  "Cancel" : function() {
                    $(this).dialog("close");
                  }
                }
              });
        });

        function editDonorDone() {
          emptyChildContent();
          refetchContent("${refreshUrl}", $("#${tabContentId}"));
        }

        function editCollectionDone() {
          emptyChildContent();
          refetchContent("${refreshUrl}", $("#${tabContentId}"));
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
			<button type="button" class="donorHistoryButton">
				View Donation History
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

		<br />
		<br />

		<div class="tipsBox ui-state-highlight">
			<p>
				${tips['donors.finddonor.donorsummary']}
			</p>
		</div>

		<div class="formInTabPane printableArea">
			<br />
			<div id="${donorSummaryBarcodeId}"></div>
			<c:if test="${donorFields.donorNumber.hidden != true }">
				<div>
					<label>${donorFields.donorNumber.displayName}</label>
					<label>${donor.donorNumber}</label>
				</div>
			</c:if>
			<c:if test="${donorFields.firstName.hidden != true }">
				<div>
					<label>${donorFields.firstName.displayName}</label>
					<label>${donor.firstName}</label>
				</div>
			</c:if>
			<c:if test="${donorFields.middleName.hidden != true }">
				<div>
					<label>${donorFields.middleName.displayName}</label>
					<label>${donor.middleName}</label>
				</div>
			</c:if>
			<c:if test="${donorFields.lastName.hidden != true }">
				<div>
					<label>${donorFields.lastName.displayName}</label>
					<label>${donor.lastName}</label>
				</div>
			</c:if>
			<c:if test="${donorFields.birthDate.hidden != true }">
				<div>
					<label>${donorFields.birthDate.displayName}</label>
					<label>${donor.birthDate}</label>
				</div>
			</c:if>
			<c:if test="${donorFields.gender.hidden != true }">
				<div>
					<label>${donorFields.gender.displayName}</label>
					<label>${donor.gender}</label>
				</div>
			</c:if>
			<c:if test="${donorFields.bloodGroup.hidden != true }">
				<div>
					<label>${donorFields.bloodGroup.displayName}</label>
					<label>${donor.bloodGroup}</label>
				</div>
			</c:if>
			<c:if test="${donorFields.address.hidden != true }">
				<div>
					<label>${donorFields.address.displayName}</label>
					<label>${donor.address}</label>
				</div>
			</c:if>
			<c:if test="${donorFields.city.hidden != true }">
				<div>
					<label>${donorFields.city.displayName}</label>
					<label>${donor.city}</label>
				</div>
			</c:if>
			<c:if test="${donorFields.province.hidden != true }">
				<div>
					<label>${donorFields.province.displayName}</label>
					<label>${donor.province}</label>
				</div>
			</c:if>
			<c:if test="${donorFields.district.hidden != true }">
				<div>
					<label>${donorFields.district.displayName}</label>
					<label>${donor.district}</label>
				</div>
			</c:if>
			<c:if test="${donorFields.state.hidden != true }">
				<div>
					<label>${donorFields.state.displayName}</label>
					<label>${donor.state}</label>
				</div>
			</c:if>
			<c:if test="${donorFields.country.hidden != true }">
				<div>
					<label>${donorFields.country.displayName}</label>
					<label>${donor.country}</label>
				</div>
			</c:if>
			<c:if test="${donorFields.zipcode.hidden != true }">
				<div>
					<label>${donorFields.zipcode.displayName}</label>
					<label>${donor.zipcode}</label>
				</div>
			</c:if>
			<c:if test="${donorFields.notes.hidden != true }">
				<div>
					<label>${donorFields.notes.displayName}</label>
					<label>${donor.notes}</label>
				</div>
			</c:if>
			<br />
			<div>
				<label>${donorFields.lastUpdatedTime.displayName}</label>
				<label style="width: auto;">${donor.lastUpdated}</label>
			</div>
			<div>
				<label>${donorFields.lastUpdatedBy.displayName}</label>
				<label style="width: auto;">${donor.lastUpdatedBy}</label>
			</div>
			<hr />
		</div>
	</div>

	<br />
	<br />

	<div id="${childContentId}"></div>
</div>

<div id="${deleteConfirmDialogId}" style="display: none;">Are
	you sure you want to delete this Donor?</div>
