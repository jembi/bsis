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
                <li id="issueTab"><a href="admin-issueDisplayNamesConfig.html">Issue</a></li>
                <li id="usageTab"><a href="admin-usageDisplayNamesConfig.html">Usage</a></li>
                <li id="reportsTab" class="selectedTab"><a href="admin-reportsDisplayNamesConfig.html">Reports</a></li>

            </ul>
        </div>
        <div class="centerPanel">
            <div class="centralContent">
                <div id="messagePanel" class="infoMessage">
                    <c:if test="${model.configSaved==true}">
                        Configuration Saved
                    </c:if>
                </div>
                <div id="reportsConfigPanel" class="tabbedPanel">

                    <form:form action="admin-saveReportsDisplayNamesConfig.html">


                    <div id="reportsConfigOptions" style="clear:both;margin-top: 10px;">
                        <div>
                            <div class="inputFieldRow"><label for="inventorySummaryTips">Inventory Summary
                                Tips: </label><textarea cols="25" rows="5"
                                    id="inventorySummaryTips"
                                    maxlength="500"
                                    name="inventorySummaryTips"><c:if test="${model.hasNames==true}">${fn:escapeXml(model.inventorySummaryTipsDisplayName)}</c:if></textarea>

                            </div>
                            <div class="inputFieldRow"><label for="inventoryDetailsTips">Inventory Details
                                Tips: </label><textarea cols="25" rows="5"
                                    id="inventoryDetailsTips"
                                    maxlength="500"
                                    name="inventoryDetailsTips"><c:if test="${model.hasNames==true}">${fn:escapeXml(model.inventoryDetailsTipsDisplayName)}</c:if></textarea>


                            </div>
                            <div class="inputFieldRow"><label for="collectionsTips">Collections Tips: </label><textarea cols="25" rows="5"
                                    type="text"
                                    id="collectionsTips"
                                    maxlength="500"
                                    name="collectionsTips"><c:if test="${model.hasNames==true}">${fn:escapeXml(model.collectionsTipsDisplayName)}</c:if></textarea>


                            </div>

                            <div class="inputFieldRow"><label for="testResultsTips">Test Results Tips: </label><textarea cols="25" rows="5"
                                    id="testResultsTips"
                                    maxlength="500"
                                    name="testResultsTips"><c:if test="${model.hasNames==true}">${fn:escapeXml(model.testResultsTipsDisplayName)}</c:if></textarea>

                            </div>

                            <div class="inputFieldRow"><label for="productsTips">Products Tips: </label><textarea cols="25" rows="5"
                                    id="productsTips"
                                    maxlength="500"
                                    name="productsTips"><c:if test="${model.hasNames==true}">${fn:escapeXml(model.productsTipsDisplayName)}</c:if></textarea>

                            </div>
                        </div>

                    </div>
                    <div class="actionButtonsPanel" id="reportsDisplayNamesConfigButtonPanel"><input
                            type="submit"
                            value="Save"/>
                    </div>

                </div>
                </form:form>

            </div>
        </div>
        <jsp:include page="bottomPanel.jsp" flush="true"/>

    </div>
</div>
</body>
</html>