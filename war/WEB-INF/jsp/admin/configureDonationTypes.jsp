<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  pageContext.setAttribute("newLineChar", "\n");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
    return System.nanoTime();
  }%>


<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>
<c:set var="childContentId">childContent-${unique_page_id}</c:set>

<c:set var="configureDonationTypesFormId">configureDonationTypes-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  $("#${tabContentId}").find(".addDonationTypeButton").button({
    icons : {
      primary : 'ui-icon-plusthick'
    }
  }).click(function() {
    var div = $("#${configureDonationTypesFormId}").find(".donationTypeDiv")[0];
    var newDiv = $($(div).clone());
    console.log(newDiv);
    newDiv.find('input[name="id"]').val("");
    newDiv.find('input[name="donationTypeName"]').val("");
    $("#${configureDonationTypesFormId}").append(newDiv);
  });

  $("#${tabContentId}").find(".saveDonationTypesButton").button({
    icons : {
      primary : 'ui-icon-disk'
    }
  }).click(function() {
    var data = {};
    var donationTypeDivs = $("#${configureDonationTypesFormId}").find(".donationTypeDiv");
    for (var index=0; index < donationTypeDivs.length; index++) {
      var div = $(donationTypeDivs[index]);
      var donationType = div.find('input[name="id"]').val();
      var donationTypeName = div.find('input[name="donationTypeName"]').val();
      console.log(donationType);
      if (donationType == undefined || donationType == null || donationType === "")
        donationType = donationTypeName;
      data[donationType] = donationTypeName;
    }

    console.log(JSON.stringify(data));
    $.ajax({
      url: "configureDonationTypes.html",
      data: {params: JSON.stringify(data)},
      type: "POST",
      success: function(response) {
                 $("#${tabContentId}").replaceWith(response);
                 showMessage("Donation Types Updated Successfully!");
               },
      error:    function(response) {
                 showErrorMessage("Something went wrong. Please try again later");
                 console.log(response);
               },
    });
    return false;
  });

  $("#${tabContentId}").find(".cancelButton").button({
    icons : {
      
    }
  }).click(refetchForm);
  
  function refetchForm() {
    refetchContent("${model.refreshUrl}", $("#${tabContentId}"));
    $("#${childContentId}").html("");
  }

});
</script>

  <sec:authorize access="hasRole(T(utils.PermissionConstants).MANAGE_DONATION_TYPES)">
<div id="${tabContentId}" class="formDiv">
  <div id="${mainContentId}">
    <b>Configure Donation Types</b>
    <br />
    <br />
    <div class="tipsBox ui-state-highlight">
      <p>
        Modify names of donation types. Add new donation types. 
      </p>
    </div>
    <form id="${configureDonationTypesFormId}">
        <c:forEach var="donationType" items="${model.allDonationTypes}">
          <div class="donationTypeDiv">
            <div>
              <input type="hidden" name="id" value="${donationType.id}" />
              <input type="text" name="donationTypeName" value="${donationType.donationType}" />
            </div>
          </div>
      </c:forEach>
    </form>
      <br />
      <div>
        <label>&nbsp;</label>
        <button class="addDonationTypeButton">Add new donation type</button>
        <button class="saveDonationTypesButton">Save</button>
        <button class="cancelButton">Cancel</button>
      </div>

  </div>

  <div id="${childContentId}"></div>

</div>
</sec:authorize>
