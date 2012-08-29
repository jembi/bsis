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
                <li id="testResultsTab" class="selectedTab"><a href="admin-testResultsDisplayNamesConfig.html">Test
                    Results</a></li>
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
                <div id="testResultsConfigPanel" class="tabbedPanel">

                    <form:form action="admin-saveTestResultsDisplayNamesConfig.html">

                        <div class="leftColumn">
                            <div id="testResultsConfigOptions" style="clear:both;margin-top: 10px;">
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
                                <div class="inputFieldRow"><label for="dateTested">Date Tested: </label><input
                                        type="text"
                                        id="dateTested"
                                        maxlength="30"
                                        name="dateTested"
                                        <c:if test="${model.hasNames==true}">value="${model.dateTestedDisplayName}"</c:if>
                                        />

                                </div>

                                <div class="inputFieldRow"><label for="hiv">HIV: </label><input
                                        type="text"
                                        id="hiv"
                                        maxlength="30"
                                        name="hiv"
                                        <c:if test="${model.hasNames==true}">value="${model.hivDisplayName}"</c:if>
                                        />

                                </div>

                                <div class="inputFieldRow"><label for="hbv">HBV: </label><input
                                        type="text"
                                        id="hbv"
                                        maxlength="30"
                                        name="hbv"
                                        <c:if test="${model.hasNames==true}">value="${model.hbvDisplayName}"</c:if>
                                        />
                                </div>
                                <div class="inputFieldRow"><label for="hcv">HCV: </label><input
                                        id="hcv"
                                        maxlength="30"
                                        name="hcv"
                                        type="text"
                                        <c:if test="${model.hasNames==true}">value="${model.hcvDisplayName}"</c:if>
                                        />

                                </div>
                                <div class="inputFieldRow"><label for="syphilis">Syphilis: </label><input
                                        type="text"
                                        id="syphilis"
                                        maxlength="30"
                                        name="syphilis"
                                        <c:if test="${model.hasNames==true}">value="${model.syphilisDisplayName}"</c:if>
                                        />
                                </div>

                                <div class="inputFieldRow"><label for="abo">ABO: </label><input
                                        type="text"
                                        id="abo"
                                        maxlength="30"
                                        name="abo"
                                        <c:if test="${model.hasNames==true}">value="${model.aboDisplayName}"</c:if>

                                        />
                                </div>

                                <div class="inputFieldRow"><label for="rhd">RhD: </label><input
                                        type="text"
                                        id="rhd"
                                        maxlength="30"
                                        name="rhd"
                                        <c:if test="${model.hasNames==true}">value="${model.rhdDisplayName}"</c:if>

                                        />
                                </div>

                                <div class="inputFieldRow"><label for="bloodGroup">Blood Group: </label><input
                                        type="text"
                                        id="bloodGroup"
                                        maxlength="30"
                                        name="bloodGroup"
                                        <c:if test="${model.hasNames==true}">value="${model.bloodGroupDisplayName}"</c:if>

                                        />
                                </div>

                                <div class="inputFieldRow"><label for="comment">Comment: </label><input
                                        type="text"
                                        id="comment"
                                        maxlength="30"
                                        name="comment"
                                        <c:if test="${model.hasNames==true}">value="${model.commentDisplayName}"</c:if>

                                        />
                                </div>
                            </div>
                        </div>
                        <div class="rightColumn">
                            <div class="inputFieldRow"><label for="tips">Tips: </label><textarea cols="25" rows="5"
                                    id="tips"
                                    maxlength="500"
                                    name="tips"><c:if test="${model.hasNames==true}">${fn:escapeXml(model.tipsDisplayName)}</c:if></textarea>
                            </div>
                        </div>
                        <div class="actionButtonsPanel" id="testResultsConfigButtonPanel"><input
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