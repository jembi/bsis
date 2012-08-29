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
                <li id="productsTab" class="selectedTab"><a href="admin-productsDisplayNamesConfig.html">Products</a>
                </li>
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
                <div id="productsConfigPanel" class="tabbedPanel">

                    <form:form action="admin-saveProductsDisplayNamesConfig.html">


                    <div id="productsConfigOptions" style="clear:both;margin-top: 10px;">
                        <div class="leftColumn">
                            <div class="inputFieldRow"><label for="productNo">Product No.: </label><input
                                    type="text"
                                    maxlength="30"
                                    id="productNo"
                                    name="productNo"
                                    <c:if test="${model.hasNames==true}">value="${model.productNoDisplayName}"</c:if>
                                    />
                            </div>
                            <div class="inputFieldRow"><label for="collectionNo">Collection No.: </label><input
                                    type="text"
                                    id="collectionNo"
                                    maxlength="30"
                                    name="collectionNo"
                                    <c:if test="${model.hasNames==true}">value="${model.collectionNoDisplayName}"</c:if>
                                    />
                            </div>
                            <div class="inputFieldRow"><label for="dateCollected">Date Collected: </label><input
                                    type="text"
                                    id="dateCollected"
                                    maxlength="30"
                                    name="dateCollected"
                                    <c:if test="${model.hasNames==true}">value="${model.dateCollectedDisplayName}"</c:if>
                                    />

                            </div>
                            <div class="inputFieldRow"><label for="productType">Product Type: </label><input
                                    type="text"
                                    id="productType"
                                    maxlength="30"
                                    name="productType"
                                    <c:if test="${model.hasNames==true}">value="${model.productTypeDisplayName}"</c:if>
                                    />

                            </div>

                            <div class="inputFieldRow"><label for="productType">Blood Group: </label><input
                                    type="text"
                                    id="bloodGroup"
                                    maxlength="30"
                                    name="bloodGroup"
                                    <c:if test="${model.hasNames==true}">value="${model.bloodGroupDisplayName}"</c:if>
                                    />

                            </div>
                        </div>
                        <div class="rightColumn">
                            <div class="inputFieldRow"><label for="tips">Tips: </label><textarea cols="25" rows="5" type="text"
                                                                                                 id="tips"
                                                                                                 maxlength="500"
                                                                                                 name="tips"><c:if
                                    test="${model.hasNames==true}">${fn:escapeXml(model.tipsDisplayName)}</c:if></textarea>
                            </div>
                        </div>
                        <div class="actionButtonsPanel" id="productsConfigButtonPanel"><input
                                type="submit"
                                value="Save"/>
                        </div>
                        </form:form>

                    </div>

                </div>
            </div>

        </div>
        <jsp:include page="bottomPanel.jsp" flush="true"/>

    </div>
</div>
</body>
</html>