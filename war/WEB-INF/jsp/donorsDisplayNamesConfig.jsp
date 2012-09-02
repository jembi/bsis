<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>V2V</title>
    <jsp:include page="commonHeadIncludes.jsp" flush="true"/>


</head>
<body>
<div class="mainBody">
    <div class="mainContent">
        <jsp:include page="adminTopPanel.jsp" flush="true"/>
        <div class="leftPanel">
            <ul id="displayFieldsTabs" class="leftPanelTabs">
                <li id="donorsTab" class="selectedTab"><a href="admin-donorsDisplayNamesConfig.html">Donors</a></li>
                <li id="collectionsTab"><a href="admin-collectionsDisplayNamesConfig.html">Collections</a></li>
                <li id="testResultsTab"><a href="admin-testResultsDisplayNamesConfig.html">Test Results</a></li>
                <li id="productsTab"><a href="admin-productsDisplayNamesConfig.html">Products</a></li>
                <li id="requestsTab"><a href="admin-requestsDisplayNamesConfig.html">Requests</a></li>
                <li id="issueTab"><a href="admin-issueDisplayNamesConfig.html">Issue</a></li>
                <li id="usageTab"><a href="admin-usageDisplayNamesConfig.html">Usage</a></li>
                <li id="reportsTab"><a href="admin-reportsDisplayNamesConfig.html">Reports</a></li>


            </ul>
        </div>
        <div class="centerPanel">
            <div class="centralContent">
                <div id="messagePanel" class="infoMessage">
                    <c:if test="${model.configSaved==true}">
                        Configuration Saved
                    </c:if>
                </div>
                <div id="donorsConfigPanel" class="tabbedPanel">

                    <form:form action="admin-saveDonorsDisplayNamesConfig.html">

                        <div class="leftColumn">
                            <div id="donorConfigOptions" style="clear:both;margin-top: 10px;">
                                <div class="inputFieldRow"><label for="donorID">Donor ID: </label><input
                                        type="text"
                                        id="donorID"
                                        maxlength="30"
                                        name="donorID"
                                        <c:if test="${model.hasNames==true}">value="${model.donorIDDisplayName}"</c:if>
                                        />
                                </div>
                                <div class="inputFieldRow"><label for="firstName">First Name: </label><input
                                        type="text"
                                        id="firstName"
                                        maxlength="30"
                                        name="firstName"
                                        <c:if test="${model.hasNames==true}">value="${model.firstNameDisplayName}"</c:if>
                                        />

                                </div>

                                <div class="inputFieldRow"><label for="lastName">Last Name: </label><input
                                        type="text"
                                        id="lastName"
                                        maxlength="30"
                                        name="lastName"
                                        <c:if test="${model.hasNames==true}">value="${model.lastNameDisplayName}"</c:if>
                                        />

                                </div>

                                <div class="inputFieldRow"><label for="gender">Gender: </label><input
                                        type="text"
                                        id="gender"
                                        maxlength="30"
                                        name="gender"
                                        <c:if test="${model.hasNames==true}">value="${model.genderDisplayName}"</c:if>
                                        />
                                </div>
                                <div class="inputFieldRow"><label for="bloodType">Site: </label><input
                                        id="bloodType"
                                        name="bloodType"
                                        maxlength="30"
                                        type="text"
                                        <c:if test="${model.hasNames==true}">value="${model.bloodTypeDisplayName}"</c:if>
                                        />

                                </div>
                                <div class="inputFieldRow"><label for="dob">Date of Birth: </label><input
                                        type="text"
                                        id="dob"
                                        maxlength="30"
                                        name="dob"
                                        <c:if test="${model.hasNames==true}">value="${model.dobDisplayName}"</c:if>
                                        />
                                </div>

                                <div class="inputFieldRow"><label for="age">Age: </label><input
                                        type="text"
                                        id="age"
                                        maxlength="30"
                                        name="age"
                                        <c:if test="${model.hasNames==true}">value="${model.ageDisplayName}"</c:if>
                                        />
                                </div>

                                <div class="inputFieldRow"><label for="address">Address: </label><input
                                        type="text"
                                        id="address"
                                        maxlength="30"
                                        name="address"
                                        <c:if test="${model.hasNames==true}">value="${model.addressDisplayName}"</c:if>

                                        />
                                </div>
                            </div>
                        </div>
                        <div class="rightColumn">
                            <div class="inputFieldRow"><label for="tips">Tips: </label>
                                <textarea cols="25" rows="5"
                                          id="tips"
                                          name="tips"><c:if test="${model.hasNames==true}">${fn:escapeXml(model.tipsDisplayName)}</c:if></textarea>
                            </div>
                        </div>
                        <div class="actionButtonsPanel" id="donorsConfigButtonPanel"><input
                                type="submit"
                                value="Save"/>
                        </div>
                    </form:form>

                </div>

            </div>
        </div>
        <jsp:include page="bottomPanel.jsp" flush="true"/>

    </div>
</div>
</body>
</html>