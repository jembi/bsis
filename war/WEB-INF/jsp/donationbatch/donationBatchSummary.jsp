<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
<c:set var="deleteConfirmDialogId">deleteConfirmDialog-${unique_page_id}</c:set>
<c:set var="addCollectionBatchToWorksheetDialogId">addCollectionBatchToWorksheet-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        function notifyParentDone() {
          $("#${tabContentId}").parent().trigger("donationBatchSummarySuccess");
        }

        $("#${tabContentId}").find(".editButton").button(
            {
              icons : {
                primary : 'ui-icon-pencil'
              }
            }).click(function() {

            hideMainContent();
            $("#${tabContentId}").bind("editCollectionBatchSuccess", editCollectionBatchDone);
            $("#${tabContentId}").bind("editCollectionBatchCancel", editCollectionBatchDone);

            fetchContent("editDonationBatchFormGenerator.html",
                         {donationBatchId: "${donationBatch.id}"},
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

         // child div shows donor information. bind this div to collectionSummaryView event
      $("#${tabContentId}").bind("collectionSummaryView",
      function(event, content) {
        $("#${mainContentId}").hide();
        $("#${childContentId}").html(content);
      });
      
        $("#${tabContentId}").find(".cancelButton").button({
          icons : {
            primary : 'ui-icon-check'
          }
        }).click(function() {
          notifyParentDone();
        });

        $("#${mainContentId}").find(".addCollectionBatchToWorksheetButton")
                              .button({icons: {primary : 'ui-icon-plusthick'}})
                              .click(addCollectionBatchToWorksheet);

        function addCollectionBatchToWorksheet() {
          $("#${addCollectionBatchToWorksheetDialogId}").find(".findWorksheetFormSection")
                                                        .load("findWorksheetToAddDonationBatchFormGenerator.html?"
                                                            + $.param({donationBatchId: "${donationBatch.id}"}));

          var addToWorksheetDialog = $("#${addCollectionBatchToWorksheetDialogId}").dialog({
            modal: true,
            title: "Select worksheet",
            height: 600,
            width: 800,
            buttons: {
              "Close" : function() {
                          $(this).dialog("close");
                        }
            }
          });
          console.log(addToWorksheetDialog);
        }
        
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
                    deleteCollectionBatch("${donationBatch.id}", notifyParentDone);
                    $(this).dialog("close");
                  },
                  "Cancel" : function() {
                    $(this).dialog("close");
                  }
                }
              });
        });

        function editCollectionBatchDone() {
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
<sec:authorize access="hasRole(T(utils.PermissionConstants).VIEW_DONATION_BATCH)">
<div id="${tabContentId}">
  <div id="${mainContentId}">

    <div class="summaryPageButtonSection" style="text-align: right;">
      <button type="button" class="cancelButton">
        Done
      </button>
      <sec:authorize access="hasRole(T(utils.PermissionConstants).ADD_DONATION_BATCH)">
      <button type="button" class="addCollectionBatchToWorksheetButton">
        Add collections in batch to worksheet
      </button>
      </sec:authorize>
      <!-- button type="button" class="editButton">
        Edit
      </button-->
      <sec:authorize access="hasRole(T(utils.PermissionConstants).VOID_DONATION_BATCH)">
      <button type="button" class="deleteButton">
        Delete
      </button>
      </sec:authorize>
      <button type="button" class="printButton">
        Print
      </button>
    </div>

    <br />
    <br />

    <div class="tipsBox ui-state-highlight">
      <p>
        ${tips['collectionbatches.findcollectionbatch.collectionbatchsummary']}
      </p>
    </div>

    <jsp:include page="donationBatchDetail.jsp" />
    <jsp:include page="../collections/collectionsTable.jsp" />

  </div>

  <br />
  <br />

  <div id="${childContentId}"></div>
</div>

<div id="${deleteConfirmDialogId}" style="display: none;">
  Are  you sure you want to delete this Collection Batch?
</div>

<div id="${addCollectionBatchToWorksheetDialogId}" style="display: none;">
  Find and select the worksheet you want to add this collection batch to
  <div class="findWorksheetFormSection"></div> 
</div>
</sec:authorize>