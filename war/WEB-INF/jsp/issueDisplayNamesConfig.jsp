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
                <li id="donorsTab"><a href="admin-donorsDisplayNamesConfig.html">Donors</a></li>
                <li id="collectionsTab"><a href="admin-collectionsDisplayNamesConfig.html">Collections</a></li>
                <li id="testResultsTab"><a href="admin-testResultsDisplayNamesConfig.html">Test Results</a></li>
                <li id="productsTab"><a href="admin-productsDisplayNamesConfig.html">Products</a></li>
                <li id="requestsTab"><a href="admin-requestsDisplayNamesConfig.html">Requests</a></li>
                <li id="issueTab" class="selectedTab"><a href="admin-issueDisplayNamesConfig.html">Issue</a></li>
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
                <div id="issueConfigPanel" class="tabbedPanel">

                    <form:form action="admin-saveIssueDisplayNamesConfig.html">


                        <div id="issueConfigOptions" style="clear:both;margin-top: 10px;">
                            <div class="leftColumn">
                                <div class="inputFieldRow"><label for="issueDate">Issue Date: </label><input
                                        type="text"
                                        maxlength="30"
                                        id="issueDate"
                                        name="issueDate"
                                        <c:if test="${model.hasNames==true}">value="${model.issueDateDisplayName}"</c:if>
                                        />
                                </div>
                                <div class="inputFieldRow"><label for="issue">Issue: </label><input
                                        type="text"
                                        id="issue"
                                        maxlength="30"
                                        name="issue"
                                        <c:if test="${model.hasNames==true}">value="${model.issueDisplayName}"</c:if>
                                        />

                                </div>
                            </div>
                            <div class="rightColumn" style="margin-top: 0px;">
                                <div class="inputFieldRow" style="margin:3px 0px;"><label for="tips">Tips: </label>
                                    <textarea cols="25" rows="5"
                                            id="tips"
                                            maxlength="500"
                                            name="issueTips"><c:if test="${model.hasNames==true}">${fn:escapeXml(model.issueTipsDisplayName)}</c:if></textarea>

                                </div>
                            </div>
                            <div class="actionButtonsPanel" id="issueConfigButtonPanel"><input
                                    type="submit"
                                    value="Save"/>
                            </div>

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