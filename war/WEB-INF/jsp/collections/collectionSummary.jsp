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
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>
<c:set var="childContentId">childContent-${unique_page_id}</c:set>
<c:set var="deleteConfirmDialogId">deleteConfirmDialog-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        showBarcode($("#${mainContentId}").find(".collectionBarcode"),
                    "${collectedSample.collectionNumber}");

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
                         {collectionId: "${collectedSample.id}"},
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

        $("#${tabContentId}").find(".testResultsForCollectionButton").button(
            {
              icons : {
                primary : 'ui-icon-bookmark'
              }
            }).click(function() {

            $("#${tabContentId}").bind("testResultsHistorySuccess", emptyChildContent);

            fetchContent("testResultsForCollection.html",
                         {collectedSampleId: "${collectedSample.id}"},
                         $("#${childContentId}")
                        );
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
                    deleteCollection("${collectedSample.id}", notifyParentDone);
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
      <!-- button type="button" class="testResultsForCollectionButton">
        Test results for collection
      </button-->
      <sec:authorize access="hasRole('ROLE_ADMIN')">
      <button type="button" class="editButton">
        Edit
      </button>
      </sec:authorize>
      <sec:authorize access="hasRole('ROLE_ADMIN')">
      <button type="button" class="deleteButton">
        Delete
      </button>
      </sec:authorize>
      <button type="button" class="printButton">
        Print
      </button>
    </div>

    <div class="tipsBox ui-state-highlight">
      <p>
        ${tips['collections.findcollection.collectionsummary']}
      </p>
    </div>

    <jsp:include page="collectionDetail.jsp" />

  </div>

  <br />
  <br />

  <div id="${childContentId}"></div>
</div>

<div id="${deleteConfirmDialogId}" style="display: none;">
  Are  you sure you want to delete this Collection?
</div>
