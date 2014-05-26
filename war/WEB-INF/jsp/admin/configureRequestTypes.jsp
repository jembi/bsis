<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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

<c:set var="configureRequestTypesFormId">configureRequestTypes-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  $("#${tabContentId}").find(".addRequestTypeButton").button({
    icons : {
      primary : 'ui-icon-plusthick'
    }
  }).click(function() {
    var div = $("#${configureRequestTypesFormId}").find(".requestTypeDiv")[0];
    var newDiv = $($(div).clone());
    console.log(newDiv);
    newDiv.find('input[name="id"]').val("");
    newDiv.find('input[name="requestType"]').val("");
    newDiv.find('input[name="bulkTransfer"]').removeAttr('checked');
    $("#${configureRequestTypesFormId}").append(newDiv);
  });

  $("#${tabContentId}").find(".saveRequestTypesButton").button({
    icons : {
      primary : 'ui-icon-disk'
    }
  }).click(function() {
    var data = {};
    var requestTypeDivs = $("#${configureRequestTypesFormId}").find(".requestTypeDiv");
    for (var index=0; index < requestTypeDivs.length; index++) {
      var div = $(requestTypeDivs[index]);
      var id = div.find('input[name="id"]').val();
      var requestType = div.find('input[name="requestType"]').val();
      var bulkTransfer = div.find('input[name="bulkTransfer"]').is(":checked");
      console.log(requestType);
      if (id == undefined || id == null || id === "")
        id = requestType;
      data[id] = {requestType:requestType,bulkTransfer:bulkTransfer};  
      
    }

    console.log(JSON.stringify(data));
    $.ajax({
      url: "configureRequestTypes.html",
      data: {params: JSON.stringify(data)},
      type: "POST",
      success: function(response) {
                 $("#${tabContentId}").replaceWith(response);
                 showMessage("Request Types Updated Successfully!");
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

<div id="${tabContentId}" class="formDiv">
  <div id="${mainContentId}">
    <b>Configure Request Types</b>
    <br />
    <br />
    <div class="tipsBox ui-state-highlight">
      <p>
        Modify names of request types. Add new request types. 
      </p>
    </div>
    <form id="${configureRequestTypesFormId}">
    
        
       <div class="requestTypeDiv">
       	<c:forEach var="requestType" items="${model.allRequestTypes}">
         <div>
           <input type="hidden" name="id" value="${requestType.id}" />
           <input type="text" name="requestType" value="${requestType.requestType}" />
           <c:choose>
   			 <c:when test="${requestType.bulkTransfer eq true}"> Bulk Transfer <input type="checkbox" name="bulkTransfer" checked="checked"/></c:when>
			 <c:otherwise> Bulk Transfer <input type="checkbox" name="bulkTransfer"/></c:otherwise>
		   </c:choose>
         </div>
         </c:forEach>
       </div>
      
    </form>
      <br />
      <div>
        <label>&nbsp;</label>
        <button class="addRequestTypeButton">Add new request type</button>
        <button class="saveRequestTypesButton">Save</button>
        <button class="cancelButton">Cancel</button>
      </div>

  </div>

  <div id="${childContentId}"></div>

</div>
