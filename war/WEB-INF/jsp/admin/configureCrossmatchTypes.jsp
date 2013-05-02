<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
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

<c:set var="configureCrossmatchTypesFormId">configureCrossmatchTypes-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  $("#${tabContentId}").find(".addCrossmatchTypeButton").button({
    icons : {
      primary : 'ui-icon-plusthick'
    }
  }).click(function() {
    var div = $("#${configureCrossmatchTypesFormId}").find(".crossmatchTypeDiv")[0];
    var newDiv = $($(div).clone());
    console.log(newDiv);
    newDiv.find('input[name="id"]').val("");
    newDiv.find('input[name="crossmatchType"]').val("");
    $("#${configureCrossmatchTypesFormId}").append(newDiv);
  });

  $("#${tabContentId}").find(".saveCrossmatchTypesButton").button({
    icons : {
      primary : 'ui-icon-disk'
    }
  }).click(function() {
    var data = {};
    var crossmatchTypeDivs = $("#${configureCrossmatchTypesFormId}").find(".crossmatchTypeDiv");
    for (var index=0; index < crossmatchTypeDivs.length; index++) {
      var div = $(crossmatchTypeDivs[index]);
      var id = div.find('input[name="id"]').val();
      var crossmatchType = div.find('input[name="crossmatchType"]').val();
      console.log(crossmatchType);
      if (id == undefined || id == null || id === "")
        id = crossmatchType;
      data[id] = crossmatchType;
    }

    console.log(JSON.stringify(data));
    $.ajax({
      url: "configureCrossmatchTypes.html",
      data: {params: JSON.stringify(data)},
      type: "POST",
      success: function(response) {
                 $("#${tabContentId}").replaceWith(response);
                 showMessage("Crossmatch Types Updated Successfully!");
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
    <b>Configure Crossmatch Types</b>
    <br />
    <br />
    <div class="tipsBox ui-state-highlight">
      <p>
        Modify names of crossmatch types. Add new crossmatch types. 
      </p>
    </div>
    <form id="${configureCrossmatchTypesFormId}">
        <c:forEach var="crossmatchType" items="${model.allCrossmatchTypes}">
          <div class="crossmatchTypeDiv">
            <div>
              <input type="hidden" name="id" value="${crossmatchType.id}" />
              <input type="text" name="crossmatchType" value="${crossmatchType.crossmatchType}" />
            </div>
          </div>
      </c:forEach>
    </form>
      <br />
      <div>
        <label>&nbsp;</label>
        <button class="addCrossmatchTypeButton">Add new crossmatch type</button>
        <button class="saveCrossmatchTypesButton">Save</button>
        <button class="cancelButton">Cancel</button>
      </div>

  </div>

  <div id="${childContentId}"></div>

</div>
