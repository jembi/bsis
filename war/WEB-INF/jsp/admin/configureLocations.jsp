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

<c:set var="configureLocationsFormId">configureLocations-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  $("#${tabContentId}").find(".addLocationButton").button({
    icons : {
      primary : 'ui-icon-plusthick'
    }
  }).click(function() {
    var div = $("#${configureLocationsFormId}").find(".locationDiv")[0];
    var newDiv = $($(div).clone());
    console.log(newDiv);
    newDiv.find('input[name="id"]').val("newLocation-" + new Date().getTime());
    newDiv.find('input[name="locationName"]').val("");
    newDiv.find('input[name="isCollectionCenter"]').removeAttr('checked');
    newDiv.find('input[name="isCollectionSite"]').removeAttr('checked');
    newDiv.find('input[name="isUsageSite"]').removeAttr('checked');
    $("#${configureLocationsFormId}").append(newDiv);
  });

  $("#${tabContentId}").find(".saveLocationsButton").button({
    icons : {
      primary : 'ui-icon-disk'
    }
  }).click(function() {
     
    var data = {};
    var locationDivs = $("#${configureLocationsFormId}").find(".locationDiv");
    for (var index=0; index < locationDivs.length; index++) {
      var div = $(locationDivs[index]);
      var locationId = div.find('input[name="id"]').val();
      var locationName = div.find('input[name="locationName"]').val();
      var isCollectionCenter = div.find('input[name="isCollectionCenter"]').is(":checked");
      var isCollectionSite = div.find('input[name="isCollectionSite"]').is(":checked");
      var isUsageSite = div.find('input[name="isUsageSite"]').is(":checked");
      var isDonorPanel = div.find('input[name="isDonorPanel"]').is(":checked");
      var isCurrentLocation = div.find('input[name="isCurrentLocation"]').is(":checked");
      data[locationId] = {name: locationName,
                          isCollectionCenter: isCollectionCenter,
                          isCollectionSite: isCollectionSite,
                          isUsageSite: isUsageSite,
                          isDonorPanel: isDonorPanel,
                          isCurrentLocation: isCurrentLocation
                         };
    }

    $.ajax({
      url: "configureLocations.html",
      data: {params: JSON.stringify(data)},
      type: "POST",
      success: function(response) {
                 $("#${tabContentId}").replaceWith(response);
                 showMessage("Centers and Sites Updated Successfully!");
               },
      error:    function(response) {
                 $("#${tabContentId}").replaceWith(response.responseText);
               },
    });
    return false;
  });

  $("#${tabContentId}").find(".cancelButton").button().click(refetchForm);
  
  function refetchForm() {
    refetchContent("${model.refreshUrl}", $("#${tabContentId}"));
    $("#${childContentId}").html("");
  }

});
</script>

<sec:authorize access="hasRole(T(utils.PermissionConstants).MANAGE_DONATION_SITES)">
<div id="${tabContentId}" class="formDiv">
   
    <c:if test="${!empty success && !success}">
        <c:forEach items="${errors}" var="error">
            <jsp:include page="../common/errorBox.jsp">
                <jsp:param name="errorMessage" value="${error}" />
            </jsp:include>
        </c:forEach>
    </c:if>

  <div id="${mainContentId}" class="formFormatClass">
    <b>Configure Locations</b>
    <br />
    <br />
    <div class="tipsBox ui-state-highlight">
      <p>
        Edit/Create centers and sites. 
      </p>
    </div>
    <form:errors />
    <form id="${configureLocationsFormId}" >
        <c:forEach var="location" items="${model.allLocations}">

          <div class="locationDiv">
            <div>
              <input type="hidden" name="id" value="${location.id}" />
              <input type="text" name="locationName" value="${location.name}" />
        
              <label for="isCollectionCenter">Center</label>
              <c:if test="${location.isCollectionCenter}">
                <input type="checkbox" name="isCollectionCenter" checked="checked"/>
              </c:if>
              <c:if test="${!location.isCollectionCenter}">
                <input type="checkbox" name="isCollectionCenter" />
              </c:if>

              <label for="isCollectionSite" style="margin-left: 10px;">Collection Site</label>
              <c:if test="${location.isCollectionSite}">
                <input type="checkbox" name="isCollectionSite" checked="checked"/>
              </c:if>
              <c:if test="${!location.isCollectionSite}">
                <input type="checkbox" name="isCollectionSite" />
              </c:if>

              <label for="isUsageSite" style="margin-left: 10px;">Request/Usage Site</label>
              <c:if test="${location.isUsageSite}">
                <input type="checkbox" name="isUsageSite" checked="checked"/>
              </c:if>
              <c:if test="${!location.isUsageSite}">
                <input type="checkbox" name="isUsageSite" />
              </c:if>

              <label for="isDonorPanel" style="margin-left: 10px;">Donor Panel</label>
              <c:if test="${location.isDonorPanel}">
                <input type="checkbox" name="isDonorPanel" checked="checked"/>
              </c:if>
              <c:if test="${!location.isDonorPanel}">
                <input type="checkbox" name="isDonorPanel" />
              </c:if>
                
              <label for="isCurrentLocation" style="margin-left: 10px;">Current Location</label>
              <c:if test="${location.isCurrentLocation}">
                <input type="checkbox" name="isCurrentLocation"  checked="checked"/>
              </c:if>
              <c:if test="${!location.isCurrentLocation}">
                <input type="checkbox" name="isCurrentLocation" />
              </c:if>

            </div>
          </div>

      </c:forEach>
    </form>
      <br />
      <div>
        <label>&nbsp;</label>
        <button class="addLocationButton">Add new Center/Site</button>
        <button class="saveLocationsButton">Save</button>
        <button class="cancelButton">Cancel</button>
      </div>

  </div>

  <div id="${childContentId}"></div>

</div>
</sec:authorize>