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

<script>
$(document).ready(function() {

  $("#${mainContentId}").find(".saveButton").button({
    icons : {
      primary : 'ui-icon-plusthick'
    }
  }).click(function() {
    var worksheetConfigFormData = $("#${mainContentId}").find(".worksheetConfig").serialize();
    $.ajax({
      type : "GET",
      url : "configureWorksheets.html",
      data : worksheetConfigFormData,
      success: function(data) {
                 showMessage("Worksheet configuration successfully updated.");
                 refetchForm();
               },
      error: function(data) {
               showErrorMessage("Something went wrong. Please try again.");
               refetchForm();
             }
    });
  });

  $("#${mainContentId}").find(".cancelButton").button({
    icons : {
      primary : 'ui-icon-closethick'
    }
  }).click(refetchForm);

  function refetchForm() {
    $.ajax({
      url: "${model.refreshUrl}",
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

 <sec:authorize access="hasRole(T(utils.PermissionConstants).MANAGE_TIPS)">
<div id="${tabContentId}">
  <div id="${mainContentId}">
    <div class="formDiv">
      <b>Configure worksheet properties</b>
      <form class="worksheetConfig formFormatClass">
        <div>
          <label>Row height</label>
          <input name="rowHeight" type="number" value="${model.worksheetConfig.rowHeight}" min="10" max="200" />
        </div>
        <div>
          <label>Column width</label>
          <input name="columnWidth" type="number" value="${model.worksheetConfig.columnWidth}" min="10" max="600" />
        </div>

        <div>
            <c:if test="${model.worksheetConfig['collectionNumber'] == 'true'}">
              <input name="collectionNumber" type="checkbox" value="true" style="width: auto;" checked />
            </c:if>
            <c:if test="${model.worksheetConfig['collectionNumber'] != 'true'}">
              <input name="collectionNumber" type="checkbox" value="true" style="width: auto;" />
            </c:if>
          <label for="collectionNumber" style="width: auto;">Show column for Collection Number</label>
        </div>

        <div>
            <c:if test="${model.worksheetConfig['testedOn'] == 'true'}">
              <input name="testedOn" type="checkbox" value="true" style="width: auto;" checked />
            </c:if>
            <c:if test="${model.worksheetConfig['testedOn'] != 'true'}">
              <input name="testedOn" type="checkbox" value="true" style="width: auto;" />
            </c:if>
          <label for="testedOn" style="width: auto;">Show column for Date of Testing</label>
        </div>

        <c:forEach var="bloodTest" items="${model.bloodTests}">
          <div>
            <c:if test="${model.worksheetConfig[bloodTest.name] == 'true'}">
              <input name="${bloodTest.name}" type="checkbox" value="true" style="width: auto;" checked />
            </c:if>
            <c:if test="${model.worksheetConfig[bloodTest.name] != 'true'}">
              <input name="${bloodTest.name}" type="checkbox" value="true" style="width: auto;" />
            </c:if>
            <label for="${bloodTest.name}" style="width: auto;">Show column for ${bloodTest.name}</label>
          </div>
        </c:forEach>

      </form>
      <div>
        <label></label>
        <button class="saveButton">Save</button>
        <button class="cancelButton">Cancel</button>
      </div>
    </div>
  </div>

  <div id="${childContentId}"></div>
</div>
</sec:authorize>