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

<c:set var="configureBloodBagTypesFormId">configureBloodBagTypes-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  $("#${tabContentId}").find(".addBloodBagTypeButton").button({
    icons : {
      primary : 'ui-icon-plusthick'
    }
  }).click(function() {
    var div = $("#${configureBloodBagTypesFormId}").find(".bloodBagTypeDiv")[0];
    var newDiv = $($(div).clone());
    console.log(newDiv);
    newDiv.find('input[name="id"]').val("");
    newDiv.find('input[name="bloodBagTypeName"]').val("");
    $("#${configureBloodBagTypesFormId}").append(newDiv);
  });

  $("#${tabContentId}").find(".saveBloodBagTypesButton").button({
    icons : {
      primary : 'ui-icon-disk'
    }
  }).click(function() {
    var data = {};
    var bloodBagTypeDivs = $("#${configureBloodBagTypesFormId}").find(".bloodBagTypeDiv");
    for (var index=0; index < bloodBagTypeDivs.length; index++) {
      var div = $(bloodBagTypeDivs[index]);
      var bloodBagType = div.find('input[name="id"]').val();
      var bloodBagTypeName = div.find('input[name="bloodBagTypeName"]').val();
      console.log(bloodBagType);
      if (bloodBagType == undefined || bloodBagType == null || bloodBagType === "")
        bloodBagType = bloodBagTypeName;
      data[bloodBagType] = bloodBagTypeName;
    }

    console.log(JSON.stringify(data));
    $.ajax({
      url: "configureBloodBagTypes.html",
      data: {params: JSON.stringify(data)},
      type: "POST",
      success: function(response) {
                 $("#${tabContentId}").replaceWith(response);
                 showMessage("Blood Bag Types Updated Successfully!");
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

<sec:authorize access="hasRole(T(utils.PermissionConstants).MANAGE_BLOOD_BAG_TYPES)">
<div id="${tabContentId}" class="formDiv">
  <div id="${mainContentId}">
    <b>Configure Blood Bag Types</b>
    <br />
    <br />
    <div class="tipsBox ui-state-highlight">
      <p>
        Modify names of blood bag types. Add new blood bag types. 
      </p>
    </div>
    <form id="${configureBloodBagTypesFormId}">
        <c:forEach var="bloodBagType" items="${model.allBloodBagTypes}">
          <div class="bloodBagTypeDiv">
            <div>
              <input type="hidden" name="id" value="${bloodBagType.id}" />
              <input type="text" name="bloodBagTypeName" value="${bloodBagType.bloodBagType}" />
            </div>
          </div>
      </c:forEach>
    </form>
      <br />
      <div>
        <label>&nbsp;</label>
        <button class="addBloodBagTypeButton">Add new blood bag type</button>
        <button class="saveBloodBagTypesButton">Save</button>
        <button class="cancelButton">Cancel</button>
      </div>

  </div>

  <div id="${childContentId}"></div>

</div>
</sec:authorize>