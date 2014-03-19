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

<script>
$(document).ready(function() {
  showBarcode($("#${tabContentId}").find(".donorBarcode"), "${donor.donorNumber}");

  
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

  $("#${tabContentId}").find(".doneButton").button({
    icons : {
      primary : 'ui-icon-check'
    }
  }).click(function() {
    refetchContent("${addAnotherDonorUrl}", $("#${tabContentId}"));
  });

  $("#${tabContentId}").find(".addAnotherDonorButton").button({
    icons : {
      primary : 'ui-icon-plusthick'
    }
  }).click(function() {
    refetchContent("${addAnotherDonorUrl}", $("#${tabContentId}"));
  });
  
  function hideMainContent() {
	    $("#${mainContentId}").remove();
  }

  function editDonorDone() {
      emptyChildContent();
      refetchContent("${refreshUrl}", $("#${tabContentId}"));
      $("#${addDonorContentId}").show();
  }
  
  function emptyChildContent() {
      $("#${childContentId}").remove();
  }
  
});
</script>
<sec:authorize access="hasRole(T(utils.PermissionConstants).ADD_DONOR)">
<div id="${tabContentId}">

  <div id="${mainContentId}">
    <div class="successBox ui-state-highlight">
      <img src="images/check_icon.png"
           style="height: 30px; padding-left: 10px; padding-right: 10px;" />
      <span class="successText">
        Donor added Successfully.
      </span>
    </div>
    <div>
      <div class="summaryPageButtonSection" style="text-align: right;">
      	<sec:authorize access="hasRole('Edit Donor')">
	      <button type="button" class="editButton">
	        Edit
	      </button>
	    </sec:authorize>
        <button type="button" class="doneButton">
          Done
        </button>
        <button type="button" class="addAnotherDonorButton">
          Add another donor
        </button>
        <button type="button" class="printButton">
          Print
        </button>
      </div>
  
      <jsp:include page="donorDetails.jsp" />
    </div>
  </div>

  <div id="${childContentId}">
  </div>

</div>
</sec:authorize>