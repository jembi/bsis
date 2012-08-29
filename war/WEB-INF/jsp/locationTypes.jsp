<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>V2V</title>
    <jsp:include page="commonHeadIncludes.jsp" flush="true"/>
    <script src="js/locationTypes.js" type="text/javascript"></script>
    <link type="text/css" rel="stylesheet" href="css/locationTypes.css"/>


</head>
<body>
<div class="mainBody">
    <div class="mainContent">
        <jsp:include page="adminTopPanel.jsp" flush="true"/>
        <div class="leftPanel">
            <ul id="locationTypesTabs" class="leftPanelTabs">
                <li id="addLocationTypesTab" class="selectedTab"><a href="admin-locationTypes.html">Add</a></li>
                <li id="updateLocationTypesTab"><a href="admin-locationTypes.html?view=true">View</a></li>
            </ul>
        </div>
        <div class="centerPanel">
            <div class="centralContent">
                <div id=locationTypes>
                    <c:if test="${model.locationTypeAdded==true}">
                        <div class="infoMessage">Location Type added successfully</div>
                    </c:if>
                    <c:if test="${model.locationTypeUpdated==true}">
                        <div class="infoMessage">Location Type updated successfully</div>
                    </c:if>
                    <c:if test="${model.locationTypeDeleted==true}">
                        <div class="infoMessage">Location Type ${model.locationTypeNameDeleted} deleted successfully
                        </div>
                    </c:if>
                    <form:form action="admin-addLocationType.html" id="locationTypeAction">

                        <c:if test="${model.hasLocationType==true}">
                            <input type="hidden" name="locationTypeId" id="locationTypeId"
                                   value="${model.locationType.locationTypeId}">
                        </c:if>
                        <div class="inputFieldRow"><label for="name">Location Type: </label><input type="text"
                                                                                                   id="name"
                                                                                                   maxlength="32"
                                                                                                   name="name"
                                                                                                   <c:if test="${model.hasLocationType==true}">value="${model.locationType.name}"</c:if>
                                />
                        </div>
                        <div class="actionButtonsPanel">
                            <div><input id="createLocationTypeButton" type="submit" value="Create"/>
                                <input id="updateLocationTypeButton" type="button"
                                       value="Update"/>

                                <input id="deleteLocationTypeButton" type="button"
                                       value="Delete"/>
                                <input id="locationTypeResetButton" type="button" value="Clear"/>
                            </div>
                        </div>
                    </form:form>
                </div>

                <div id="allLocationTypes">

                    <c:if test="${empty model.allLocationTypes}">
                        <div class="infoMessage">No Location Types</div>
                    </c:if>
                    <c:if test="${not empty model.allLocationTypes}">


                        <table id="locationTypesTable">

                            <c:forEach var="locationType" items="${model.allLocationTypes}">
                                <tr>
                                    <td>${locationType.name}</td>
                                    <td>
                                        <a href="admin-selectLocationType.html?selectedLocationTypeId=${locationType.locationTypeId}">edit/delete</a>
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