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
                <li id="collectionsTab" class="selectedTab"><a href="admin-collectionsReportFieldsConfig.html">Collections</a>
                <li id="testResultsTab"><a href="admin-testResultsReportFieldsConfig.html">Test Results</a>
                <li id="productsTab"><a href="admin-productsReportFieldsConfig.html">Products</a>
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
                <div id="collectionsConfigPanel" class="tabbedPanel">

                    <form:form action="admin-saveCollectionsReportConfig.html">


                    <div id="collectionConfigOptions" style="clear:both;margin-top: 10px;">
                        <div class="inputFieldRow">
                            <input type="checkbox" id="collectionNo" name="collectionNo" value="true"
                                   <c:if test="${ model.collectionNoFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="collectionNo" class="checkboxLabel">Collection No.</label>
                        </div>
                        <div class="inputFieldRow">
                            <input type="checkbox" id="center" name="center" value="true"
                                   <c:if test="${model.centerFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="center" class="checkboxLabel">Center</label>
                        </div>
                        <div class="inputFieldRow">
                            <input type="checkbox" id="site" name="site" value="true"
                                   <c:if test="${model.siteFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="site" class="checkboxLabel">Site</label>
                        </div>
                        <div class="inputFieldRow">
                            <input type="checkbox" id="dateCollected" name="dateCollected" value="true"
                                   <c:if test="${model.dateCollectedFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="dateCollected" class="checkboxLabel">Date Collected</label>
                        </div>
                        <div class="inputFieldRow">
                            <input type="checkbox" id="sampleNo" name="sampleNo" value="true"
                                   <c:if test="${model.sampleNoFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="sampleNo" class="checkboxLabel">Sample No.</label>
                        </div>

                        <div class="inputFieldRow">
                            <input type="checkbox" id="shippingNo" name="shippingNo" value="true"
                                   <c:if test="${model.shippingNoFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="shippingNo" class="checkboxLabel">Shipping No.</label>
                        </div>

                        <div class="inputFieldRow">
                            <input type="checkbox" id="donorNo" name="donorNo" value="true"
                                   <c:if test="${model.donorNoFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="donorNo" class="checkboxLabel">Donor No.</label>
                        </div>
                        <div class="inputFieldRow">
                            <input type="checkbox" id="donorType" name="donorType" value="true"
                                   <c:if test="${model.donorTypeFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="donorType" class="checkboxLabel">Donor Type.</label>
                        </div>
                        <div class="inputFieldRow">
                            <input type="checkbox" id="comment" name="comment" value="true"
                                   <c:if test="${model.commentFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="comment" class="checkboxLabel">Comment</label>
                        </div>
                        <div class="inputFieldRow">
                            <input type="checkbox" id="bloodGroup" name="bloodGroup" value="true"
                                   <c:if test="${model.bloodGroupFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="bloodGroup" class="checkboxLabel">Blood Group</label>
                        </div>
                        <div class="inputFieldRow">
                            <input type="checkbox" id="rhd" name="rhd" value="true"
                                   <c:if test="${model.rhdFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="rhd" class="checkboxLabel">Rhd</label>
                        </div>

                        <div class="actionButtonsPanel" id="collectionsReportConfigButtonPanel"><input
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

        <p>Select fields to be displayed on report</p>
    </div>
        <jsp:include page="bottomPanel.jsp" flush="true"/>

    </div>
</div>
</body>
</html>