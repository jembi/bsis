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
<c:set var="deleteConfirmDialogId">deleteConfirmDialog-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
  showBarcode($("#${tabContentId}").find(".requestBarcode"), "${request.requestNumber}");
  
  $("#${tabContentId}").find(".deleteButton").button({
      icons : {
        primary : 'ui-icon-trash'
      }
    }).click(function() {
    	alert(1);
      $("#${deleteConfirmDialogId}").dialog(
          {
            modal : true,
            title : "Confirm Delete",
            buttons : {
              "Delete" : function() {
            	  alert(2);
                deleteRequest("${request.id}", notifyParentDone);
                $(this).dialog("close");
              },
              "Cancel" : function() {
                $(this).dialog("close");
              }
            }
          });
    });
  
  function notifyParentDone() {
	  refetchContent("${addAnotherRequestUrl}", $("#${tabContentId}"));
    }

  $("#${tabContentId}").find(".printButton").button({
    icons : {
      primary : 'ui-icon-print'
    }
  }).click(function() {
    $("#${mainContentId}").find(".printableArea").printArea();
  });

  $("#${tabContentId}").find(".doneButton").button({
    icons : {
      primary : 'ui-icon-check'
    }
  }).click(function() {
    refetchContent("${addAnotherRequestUrl}", $("#${tabContentId}"));
  });

  $("#${tabContentId}").find(".addAnotherRequestButton").button({
    icons : {
      primary : 'ui-icon-plusthick'
    }
  }).click(function() {
    refetchContent("${addAnotherRequestUrl}", $("#${tabContentId}"));
  });

  function editCollectionDone() {
    refetchContent("${refreshUrl}", $("#${tabContentId}"));
  }

});
</script>

<div id="${tabContentId}">

  <div id="${mainContentId}">
    <div class="successBox ui-state-highlight">
      <img src="images/check_icon.png"
           style="height: 30px; padding-left: 10px; padding-right: 10px;" />
      <span class="successText">
        <c:if test="${bulkTransferStatus == true }">
        	Request has been fulfilled.
        </c:if>
        <c:if test="${bulkTransferStatus != true }">
        	Request has been partially fulfilled.
        </c:if>
      </span>
    </div>
    <div>
      <div class="summaryPageButtonSection" style="text-align: left;">
        <button type="button" class="doneButton">
          Done
        </button>
        <button type="button" class="editButton">
          Edit 
        </button>
        <button type="button" class="deleteButton">
          Delete
        </button>
       <!--  <button type="button" class="addAnotherRequestButton">
          Add another request
        </button> -->
        <button type="button" class="printButton">
          Print
        </button>
      </div>
  
      <jsp:include page="requestDetails.jsp" />
    </div>
  </div>

  <div id="${childContentId}">
  </div>

</div>
<div id="${deleteConfirmDialogId}" style="display: none;">
  Are  you sure you want to delete this Collection?
</div>