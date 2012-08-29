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
                <li id="collectionsTab" class="selectedTab"><a href="admin-collectionsDisplayNamesConfig.html">Collections</a>
                </li>
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
                <div id="collectionsConfigPanel" class="tabbedPanel">

                    <form:form action="admin-saveCollectionsDisplayNamesConfig.html">

                        <div class="leftColumn">
                            <div id="collectionConfigOptions" style="clear:both;margin-top: 10px;">
                                <div class="inputFieldRow"><label for="collectionNumber">Collection No.: </label><input
                                        type="text"
                                        id="collectionNumber"
                                        maxlength="30"
                                        name="collectionNo"
                                        <c:if test="${model.hasCollection==true}">value="${model.collectionNoDisplayName}"</c:if>
                                        />
                                </div>
                                <div class="inputFieldRow"><label for="collectionDonorNumber">Donor No.: </label><input
                                        type="text"
                                        id="collectionDonorNumber"
                                        maxlength="30"
                                        name="donorNo"
                                        <c:if test="${model.hasCollection==true}">value="${model.donorNoDisplayName}"</c:if>
                                        />

                                </div>

                                <div class="inputFieldRow"><label for="collectionDonorType">Donor Type: </label><input
                                        type="text"
                                        id="collectionDonorType"
                                        maxlength="30"
                                        name="donorType"
                                        <c:if test="${model.hasCollection==true}">value="${model.donorTypeDisplayName}"</c:if>
                                        />

                                </div>

                                <div class="inputFieldRow"><label for="collectionCenter">Center: </label><input
                                        type="text"
                                        id="collectionCenter"
                                        maxlength="30"
                                        name="center"
                                        <c:if test="${model.hasCollection==true}">value="${model.centerDisplayName}"</c:if>
                                        />
                                </div>
                                <div class="inputFieldRow"><label for="collectionSite">Site: </label><input
                                        id="collectionSite"
                                        name="site"
                                        type="text"
                                        maxlength="30"
                                        <c:if test="${model.hasCollection==true}">value="${model.siteDisplayName}"</c:if>
                                        />

                                </div>
                                <div class="inputFieldRow"><label for="collectionDate">Date: </label><input type="text"
                                                                                                            id="collectionDate"
                                                                                                            name="dateCollected"
                                                                                                            maxlength="30"
                                                                                                            <c:if test="${model.hasCollection==true}">value="${model.dateCollectedDisplayName}"</c:if>
                                        />
                                </div>

                                <div class="inputFieldRow"><label for="sampleNumber">Sample No.: </label><input
                                        type="text"
                                        maxlength="30"
                                        id="sampleNumber"
                                        name="sampleNo"
                                        <c:if test="${model.hasCollection==true}">value="${model.sampleNoDisplayName}"</c:if>

                                        />
                                </div>
                                <div class="inputFieldRow"><label for="shippingNumber">Shipping No.: </label><input
                                        type="text"
                                        id="shippingNumber"
                                        maxlength="30"
                                        name="shippingNo"
                                        <c:if test="${model.hasCollection==true}">value="${model.shippingNoDisplayName}"</c:if>

                                        />
                                </div>
                                <div class="inputFieldRow"><label for="collectionComment">Comment: </label><input
                                        type="text"
                                        id="collectionComment"
                                        maxlength="30"
                                        name="comment"
                                        <c:if test="${model.hasCollection==true}">value="${model.commentDisplayName}"</c:if>

                                        />
                                </div>

                                <div class="inputFieldRow"><label for="collectionBloodGroup">Blood Group: </label><input
                                        type="text"
                                        id="collectionBloodGroup"
                                        maxlength="30"
                                        name="bloodGroup"
                                        <c:if test="${model.hasCollection==true}">value="${model.bloodGroupDisplayName}"</c:if>

                                        />
                                </div>

                                <div class="inputFieldRow"><label for="collectionRhd">RhD: </label><input type="text"
                                                                                                          id="collectionRhd"
                                                                                                          maxlength="30"
                                                                                                          name="rhd"
                                                                                                          <c:if test="${model.hasCollection==true}">value="${model.rhdDisplayName}"</c:if>

                                        />
                                </div>
                            </div>
                        </div>
                        <div class="rightColumn">
                            <div class="inputFieldRow"><label for="collectionTips">Tips: </label><textarea cols="25" rows="5"
                                    id="collectionTips"
                                    maxlength="500"
                                    name="tips"><c:if
                                    test="${model.hasCollection==true}">${fn:escapeXml(model.tipsDisplayName)}</c:if></textarea>
                            </div>
                        </div>
                        <div class="actionButtonsPanel" id="collectionsReportConfigButtonPanel"><input
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
</div>
</body>
</html>