<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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

<script>
$(document).ready(function() {
  $("#${mainContentId}").find(".enterTestResultsForCollectionsButton")
                        .button({icons: {primary :  'ui-icon-plusthick'}})
                        .click(fetchBloodTypingForm);

  function fetchBloodTypingForm() {
    showLoadingImage($("#${tabContentId}"));
    $.ajax({
      url: "bloodTypingWorksheetGenerator.html",
      data: {},
      type: "GET",
      success: function (response) {
                  $("#${tabContentId}").replaceWith(response);
               },
      error:   function (response) {
                 showErrorMessage("Something went wrong. Please try again.");
               }
      
    });
  }

  function loadCollectionsStatusTable() {
    $.ajax({
      url : "getBloodTypingStatusForCollections.html",
      type : "GET",
      data :  {collectionIds : "${collectionIds}"},
      success : function(response) {
                  $("#${childContentId}").html(response);      
                },
      error :   function(response) {
                  showErrorMessage("Something went wrong when trying to fetch donation status.");
                }
    });
  }

  // reload table when any blood typing results of any collection are updated
  $("#${childContentId}").bind("collectionBloodTypingUpdated", loadCollectionsStatusTable);

  loadCollectionsStatusTable();
});
</script>

<div id="${tabContentId}">

  <div id="${mainContentId}" style="margin: 10px;">

    <div class="successBox ui-state-highlight">
      <img src="images/check_icon.png"
           style="height: 30px; padding-left: 10px; padding-right: 10px;" />
        <span class="successText">
          Blood Typing tests added successfully for donations.
        </span>
        <div style="margin-left: 55px;">
          Please review results below. Perform confirmatory tests for donations as indicated.
          You may click the donations in the table below to enter secondary test results now.
        </div>
    </div>

    <br />
    <br />

    <button class="enterTestResultsForCollectionsButton" style="margin:10px;">
      Enter blood typing tests for more donations
    </button>

    <br />
    <br />
    <br />
  </div>

  <div id="${childContentId}">
  </div>

</div>
