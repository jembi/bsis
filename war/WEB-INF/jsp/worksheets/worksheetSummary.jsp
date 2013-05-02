<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>

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

        showBarcode($("#${mainContentId}").find(".worksheetBarcode"),
                    "${worksheet.worksheetNumber}");

        function notifyParentDone() {
          $("#${tabContentId}").parent().trigger("worksheetSummarySuccess");
        }

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
                    deleteWorksheet("${worksheet.id}", notifyParentDone);
                    $(this).dialog("close");
                  },
                  "Cancel" : function() {
                    $(this).dialog("close");
                  }
                }
              });
        });

        $("#${tabContentId}").bind("refreshWorksheet", refreshWorksheet);
        function refreshWorksheet() {
          emptyChildContent();
          refetchContent("${refreshUrl}", $("#${tabContentId}"));
        }

        function emptyChildContent() {
          $("#${childContentId}").html("");
        }

        $("#${mainContentId}").find(".showHideButton")
                              .button({icons: {primary : 'ui-icon-plusthick'}})
                              .click(showHideToggle);

        function showHideToggle() {
          var showHideButton = $(this);
          var showHideSection = $("#${mainContentId}").find(".worksheetDetails")
                                                      .find(".inputCollectionsSection");
          var currentlyVisible = showHideSection.is(":visible");
          if (currentlyVisible) {
            $("#${mainContentId}").find(".availableTestResultInput")
                                   .each(function() {
                                          $(this).prop("type", "hidden");
                                        });
            $("#${mainContentId}").find(".availableTestResultLabel")
                                  .each(function() {
                                          $(this).show();
                                        });
            showHideButton.button("option", "label", "Add collections");
            showHideButton.button("option", "icons", {primary : "ui-icon-plusthick"});
            showHideSection.hide();
          }
          else {
            showHideButton.button("option", "label", "Show less");
            showHideButton.button("option", "icons", {primary : "ui-icon-minusthick"});
            showHideSection.show();
          }
        }

        showHideToggle();
      });
</script>

<div id="${tabContentId}">

  <div id="${mainContentId}">
      <div class="summaryPageButtonSection" style="text-align: right;">
      <button type="button" class="cancelButton">
        Done
      </button>
      <button type="button" class="deleteButton">
        Delete
      </button>
    </div>
    <div class="worksheetDetails">
      <jsp:include page="worksheetDetail.jsp" />
    </div>
    <button class="showHideButton">Add collections</button>

    <div class="worksheetSection">
      <jsp:include page="printableWorksheet.jsp" />
    </div>


  </div>

  <div id="${childContentId}"></div>
</div>

<div id="${deleteConfirmDialogId}" style="display: none;">
  Are  you sure you want to delete this worksheet?
</div>
