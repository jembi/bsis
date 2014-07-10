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
      $("#${tabContentId}").bind("donationSummaryView",
      function(event, content) {
        $("#${mainContentId}").hide();
        $("#${childContentId}").html(content);
        $("#${tabContentId}").find(".donationTable").trigger("refreshResults");
      });
      
     
      
        $("#${tabContentId}").find(".doneButton").button({
          icons : {
            primary : 'ui-icon-check'
          }
        }).click(function() {
            donationBatchSummaryViewDone();
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
                    deleteCollectionBatch("${donationBatch.id}", notifyParentDone);
                    $(this).dialog("close");
                  },
                  "Cancel" : function() {
                    $(this).dialog("close");
                  }
                }
              });
        });

        function donationBatchSummaryViewDone() {
          emptyChildContent();
           $("#${tabContentId}").parent().trigger("donationBatchSummarySuccess");
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
      <button type="button" class="doneButton">
        Done
      </button>
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
    <jsp:include page="./donationBatchDonationsTable.jsp" />
      <div id="${childContentId}">
         
      </div>


  </div>

  <br />
  <br />

</div>

<div id="${deleteConfirmDialogId}" style="display: none;">
  Are  you sure you want to delete this Collection Batch?
</div>


</sec:authorize>