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

<script>
$(document).ready(function() {
  $("#${mainContentId}").find(".findCheckResultButton")
                        .button({icons: {primary: 'ui-icon-search'}})
                        .click(findIssuedProductsForRequest);

  function findIssuedProductsForRequest() {
    var dinNumber = $("#${mainContentId}").find('input[name="dinNumber"]').val();
    $.ajax({
      url: "findlotRelease.html",
      type: "GET",
      data: {dinNumber : dinNumber},
      success: function(response) {
                 /* $("#${mainContentId}").find(".findIssuedProductsResults")
                                        .html(response); */
    	  $("#${tabContentId}").replaceWith(response);
               },
      error: function(response) {
    	  howErrorMessage("Something went wrong. Please try again.");
             }
    });
  }

  $("#${mainContentId}").find(".clearFormButton")
                        .button()
                        .click(refetchForm);

  $("#${tabContentId}").find(".printLabelButton").button({
      icons : {
        primary : 'ui-icon-print'
      }
    }).click(function() {
  	  window.open("printLabel.html?"+ $.param({dinNumber : "${dinNumber}"}));
    });
  
  $("#${tabContentId}").find(".printDiscardButton").button({
      icons : {
        primary : 'ui-icon-print'
      }
    }).click(function() {
  	  window.open("printDiscard.html?"+ $.param({dinNumber : "${dinNumber}"}));
    });
  
  function refetchForm() {
    $.ajax({
      url: "lotReleaseFormGenerator.html",
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

});
</script>

<sec:authorize access="hasRole(T(utils.PermissionConstants).ISSUE_COMPONENT)">
<div id="${tabContentId}">

  <div id="${mainContentId}">
  <c:if test="${!empty success && !success && !discard}">
        <jsp:include page="../common/errorBox.jsp">
          <jsp:param name="errorMessage" value="${errorMessage}" />
        </jsp:include>
   </c:if>
   <c:if test="${success}">
        <div class="tipsBox ui-state-highlight">
      		<p>Donation suitable for labelling. <button class="printLabelButton">Print Label</button> </p>
    	</div>
    </c:if>
    <c:if test="${discard}">
        <div class="tipsBox ui-state-highlight">
      		<p>Product has been discarded. <button class="printDiscardButton">Print Discard Label</button> </p>
    	</div>
    </c:if>
   
  <br>&nbsp;&nbsp;
  	<b>Print Pack Labels</b>
    <div class="formFormatClass">
      <div>
        <label style="width:215px;">Donation Identification Number</label>
        <input name="dinNumber" />
      </div>
      <div style="padding-left: 245px;">
        <button class="findCheckResultButton autoWidthButton">
          Check Results
        </button>
        <button class="clearFormButton">
          Clear form
        </button> 
      </div>
    </div>
  </div>

</div>
</sec:authorize>
