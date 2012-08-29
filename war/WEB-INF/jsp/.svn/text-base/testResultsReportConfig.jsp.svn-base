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
                <li id="collectionsTab"><a href="admin-collectionsReportFieldsConfig.html">Collections</a>
                <li id="testResultsTab" class="selectedTab"><a href="admin-testResultsReportFieldsConfig.html">Test Results</a>
                <li id="productsTab" ><a href="admin-productsReportFieldsConfig.html">Products</a>
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
                <div id="testResultsConfigPanel" class="tabbedPanel">

                    <form:form action="admin-saveTestResultsReportConfig.html">


                    <div id="testResultsConfigOptions" style="clear:both;margin-top: 10px;">
                        <div class="inputFieldRow">
                            <input type="checkbox" id="collectionNo" name="collectionNo" value="true"
                                   <c:if test="${model.collectionNoFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="collectionNo" class="checkboxLabel">Collection No.</label>
                        </div>
                        <div class="inputFieldRow">
                            <input type="checkbox" id="dateCollected" name="dateCollected" value="true"
                                   <c:if test="${model.dateCollectedFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="dateCollected" class="checkboxLabel">Date Collected</label>
                        </div>
                        <div class="inputFieldRow">
                            <input type="checkbox" id="dateTested" name="dateTested" value="true"
                                   <c:if test="${model.dateTestedFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="dateTested" class="checkboxLabel">Date Tested</label>
                        </div>
                        <div class="inputFieldRow">
                            <input type="checkbox" id="bloodGroup" name="bloodGroup" value="true"
                                   <c:if test="${model.bloodGroupFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="bloodGroup" class="checkboxLabel">Blood Group</label>
                        </div>

                        <div class="inputFieldRow">
                            <input type="checkbox" id="hiv" name="hiv" value="true"
                                   <c:if test="${model.hivFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="hiv" class="checkboxLabel">HIV</label>
                        </div>

                        <div class="inputFieldRow">
                            <input type="checkbox" id="hbv" name="hbv" value="true"
                                   <c:if test="${model.hbvFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="hbv" class="checkboxLabel">HBV</label>
                        </div>

                        <div class="inputFieldRow">
                            <input type="checkbox" id="hcv" name="hcv" value="true"
                                   <c:if test="${model.hcvFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="hcv" class="checkboxLabel">HCV</label>
                        </div>

                        <div class="inputFieldRow">
                            <input type="checkbox" id="syphilis" name="syphilis" value="true"
                                   <c:if test="${model.syphilisFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="syphilis" class="checkboxLabel">Syphilis</label>
                        </div>

                        <div class="inputFieldRow">
                            <input type="checkbox" id="comment" name="comment" value="true"
                                   <c:if test="${model.commentFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="comment" class="checkboxLabel">Comment</label>
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