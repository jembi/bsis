<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>V2V</title>
    <jsp:include page="commonHeadIncludes.jsp" flush="true"/>
    <script src="js/locations.js" type="text/javascript"></script>
    <link type="text/css" rel="stylesheet" href="css/locations.css"/>
    <%--<link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.16.custom.css"/>--%>


</head>
<body>
<div class="mainBody">
    <div class="mainContent">
        <jsp:include page="adminTopPanel.jsp" flush="true"/>
        <div class="leftPanel">
            <ul id="locationsTabs" class="leftPanelTabs">
                <li id="addLocationsTab" class="selectedTab"><a href="admin-locations.html">Add</a></li>
                <li id="updateLocationsTab"><a href="admin-locations.html?view=true">View</a></li>
            </ul>
        </div>
        <div class="centerPanel">
            <div class="centralContent">
                <div id=locations>
                    <c:if test="${model.locationAdded==true}">
                        <div class="infoMessage">Location added successfully</div>
                    </c:if>
                    <c:if test="${model.locationUpdated==true}">
                        <div class="infoMessage">Location updated successfully</div>
                    </c:if>
                    <c:if test="${model.locationDeleted==true}">
                        <div class="infoMessage">Location ${model.locationNameDeleted} deleted successfully</div>
                    </c:if>
                    <form:form action="admin-addLocation.html" id="locationAction">

                        <c:if test="${model.hasLocation==true}">
                            <input type="hidden" name="locationId" id="locationId" value="${model.location.locationId}">
                        </c:if>
                        <div class="inputFieldRow"><label for="name">Location: </label><input type="text"
                                                                                              id="name"
                                                                                              maxlength="32"
                                                                                              name="name"
                                                                                              <c:if test="${model.hasLocation==true}">value="${model.location.name}"</c:if>
                                />
                        </div>
                        <div class="inputFieldRow"><label for="locationType">Location Type: </label><select
                                id="locationType"
                                name="locationType">
                            <option value=""
                                    <c:if test="${model.hasLocation==true and model.location.typeName==''}">selected="selected"</c:if>></option>

                            <c:forEach var="locationType" items="${model.locationTypes}">
                                <option value="${locationType.locationTypeId}"
                                        <c:if test="${model.hasLocation==true and model.location.type==locationType.locationTypeId}">selected="selected"</c:if>>
                                        ${locationType.name}</option>
                            </c:forEach>

                        </select>

                        </div>

                        <div class="inputFieldRow"><label for="center">Is Center: </label>

                            <div id="center" class="radioButtonsList">
                                <input type="radio" id="isCenterYes" name="center" value="true"
                                       <c:if test="${model.hasLocation==true and model.location.center=='true'}">checked="checked" </c:if>

                                        />
                                <label for="isCenterYes" class="radioLabel">Yes</label>
                                <input type="radio" id="isCenterNo" name="center" value="false"
                                       <c:if test="${model.hasLocation==true and model.location.center=='false'}">checked="checked" </c:if>
                                       <c:if test="${not model.hasLocation==true}">checked="checked" </c:if>

                                        />
                                <label for="isCenterNo" class="radioLabel">No</label>
                            </div>
                        </div>

                        <div class="inputFieldRow"><label for="collectionSite">Is Collection Site: </label>

                            <div id="collectionSite" class="radioButtonsList">
                                <input type="radio" id="isCollectionSiteYes" name="collectionSite" value="true"
                                       <c:if test="${model.hasLocation==true and model.location.collectionSite=='true'}">checked="checked" </c:if>

                                        />
                                <label for="isCollectionSiteYes" class="radioLabel">Yes</label>
                                <input type="radio" id="isCollectionSiteNo" name="collectionSite" value="false"
                                       <c:if test="${model.hasLocation==true and model.location.collectionSite=='false'}">checked="checked" </c:if>
                                       <c:if test="${not model.hasLocation==true}">checked="checked" </c:if>


                                        />
                                <label for="isCollectionSiteNo" class="radioLabel">No</label>
                            </div>
                        </div>

                        <div class="inputFieldRow"><label for="usageSite">Is Usage Site: </label>

                            <div id="usageSite" class="radioButtonsList">
                                <input type="radio" id="isUsageSiteYes" name="usageSite" value="true"
                                       <c:if test="${model.hasLocation==true and model.location.usageSite=='true'}">checked="checked" </c:if>

                                        />
                                <label for="isUsageSiteYes" class="radioLabel">Yes</label>
                                <input type="radio" id="isUsageSiteNo" name="usageSite" value="false"
                                       <c:if test="${model.hasLocation==true and model.location.usageSite=='false'}">checked="checked" </c:if>
                                       <c:if test="${not model.hasLocation==true}">checked="checked" </c:if>

                                        />
                                <label for="isUsageSiteNo" class="radioLabel">No</label>
                            </div>
                        </div>

                        <div class="inputFieldRow"><label for="mobileSite">Is Mobile Site: </label>

                            <div id="mobileSite" class="radioButtonsList">
                                <input type="radio" id="isMobileSiteYes" name="mobileSite" value="true"
                                       <c:if test="${model.hasLocation==true and model.location.mobileSite=='true'}">checked="checked" </c:if>
                                        />
                                <label for="isMobileSiteYes" class="radioLabel">Yes</label>
                                <input type="radio" id="isMobileSiteNo" name="mobileSite" value="false"
                                       <c:if test="${model.hasLocation==true and model.location.mobileSite=='false'}">checked="checked" </c:if>
                                       <c:if test="${not model.hasLocation==true}">checked="checked" </c:if>

                                        />
                                <label for="isMobileSiteNo" class="radioLabel">No</label>
                            </div>
                        </div>
                        <div class="actionButtonsPanel">
                            <div><input id="createLocationButton" type="submit" value="Create"/>
                                <input id="updateLocationButton" type="button"
                                       value="Update"/>
                                <input id="deleteLocationButton" type="button"
                                       value="Delete"/>
                                <input id="locationResetButton" type="button" value="Clear"/>
                            </div>
                        </div>
                    </form:form>
                </div>


                <div id="allLocations">
                    <c:if test="${empty model.allLocations}">
                        <div class="infoMessage">No Locations</div>
                    </c:if>
                    <c:if test="${not empty model.allLocations}">

                        <table id="locationsTable">
                            <tr>
                                <th>Location</th>
                                <th>Type</th>
                                <th>Is Center</th>
                                <th>Is Collection Site</th>
                                <th>Is Usage Site</th>
                                <th>Is Mobile Site</th>
                                <th></th>
                            </tr>
                            <c:forEach var="location" items="${model.allLocations}">
                                <tr>
                                    <td>${location.name}</td>
                                    <td>${location.typeName}</td>
                                    <td>
                                        <c:if test="${location.center == true}">yes</c:if>
                                        <c:if test="${location.center == false}">no</c:if>
                                    </td>
                                    <td>
                                        <c:if test="${location.collectionSite == true}">yes</c:if>
                                        <c:if test="${location.collectionSite == false}">no</c:if>
                                    <td>
                                        <c:if test="${location.usageSite == true}">yes</c:if>
                                        <c:if test="${location.usageSite == false}">no</c:if>
                                    </td>
                                    <td>
                                        <c:if test="${location.mobileSite == true}">yes</c:if>
                                        <c:if test="${location.mobileSite == false}">no</c:if>
                                    </td>
                                    <td>
                                        <a href="admin-selectLocation.html?selectedLocationId=${location.locationId}">edit/delete</a>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:if>
                </div>
            </div>
        </div>
        <jsp:include page="bottomPanel.jsp" flush="true"/>
    </div>
</div>
</body>
</html>