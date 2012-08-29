<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
                <li id="collectionsTab"><a href="admin-collectionsDisplayFieldsConfig.html">Collections</a></li>
                <li id="donorsTab" class="selectedTab"><a href="admin-donorsDisplayFieldsConfig.html">Donors</a></li>
                <li id="requestsTab"><a href="admin-requestsDisplayFieldsConfig.html">Requests</a>
                </li>
                <li id="usageTab"><a href="admin-usageDisplayFieldsConfig.html">Usage</a>
                </li>
            </ul>
        </div>
        <div class="centerPanel">
            <div class="centralContent">
                <div id="messagePanel" class="infoMessage">
                    <c:if test="${model.configSaved==true}">
                        Configuration Saved
                    </c:if>
                </div>
                <div id="displayFieldsConfigPanel" class="tabbedPanel">

                    <form:form action="admin-saveDonorsDisplayFieldsConfig.html">


                    <div id="donorConfigOptions" style="clear:both;margin-top: 10px;">

                        <div class="inputFieldRow">
                            <input type="checkbox" id="firstName" name="firstName" value="true"
                                   <c:if test="${model.showfirstName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="firstName" class="checkboxLabel">First Name</label>
                        </div>
                        <div class="inputFieldRow">
                            <input type="checkbox" id="lastName" name="lastName" value="true"
                                   <c:if test="${model.showlastName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="lastName" class="checkboxLabel">Last Name</label>
                        </div>

                        <div class="inputFieldRow">
                            <input type="checkbox" id="gender" name="gender" value="true"
                                   <c:if test="${model.showgender=='true'}">checked="checked"</c:if>
                                    />
                            <label for="gender" class="checkboxLabel">Gender</label>
                        </div>

                        <div class="inputFieldRow">
                            <input type="checkbox" id="bloodType" name="bloodType" value="true"
                                   <c:if test="${model.showbloodType=='true'}">checked="checked"</c:if>
                                    />
                            <label for="bloodType" class="checkboxLabel">Blood Type</label>
                        </div>

                        <div class="inputFieldRow">
                            <input type="checkbox" id="dateOfBirth" name="dateOfBirth" value="true"
                                   <c:if test="${model.showdateOfBirth=='true'}">checked="checked"</c:if>
                                    />
                            <label for="dateOfBirth" class="checkboxLabel">Date of Birth</label>
                        </div>
                        <div class="inputFieldRow">
                            <input type="checkbox" id="age" name="age" value="true"
                                   <c:if test="${model.showage=='true'}">checked="checked"</c:if>
                                    />
                            <label for="age" class="checkboxLabel">Age</label>
                        </div>
                        <div class="inputFieldRow">
                            <input type="checkbox" id="address" name="address" value="true"
                                   <c:if test="${model.showaddress=='true'}">checked="checked"</c:if>
                                    />
                            <label for="address" class="checkboxLabel">Address</label>
                        </div>

                        <div class="actionButtonsPanel" id="donorsConfigButtonPanel"><input
                                type="submit"
                                value="Save"/>
                        </div>
                        </form:form>

                    </div>
                </div>
            </div>
        </div>
		<div id="showTips" class="link showTips">show tips</div>
    <div class="rightPanel">
        <div id="hideTips" class="link hideTips">hide tips</div>
        <p class="tipsTitle">Tips</p>

        <p>Select fields to be displayed on form</p>
    </div>
        <jsp:include page="bottomPanel.jsp" flush="true"/>

    </div>
</div>
</body>
</html>