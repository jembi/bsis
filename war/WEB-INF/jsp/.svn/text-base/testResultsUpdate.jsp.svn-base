<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>V2V</title>
    <link type="text/css" rel="stylesheet" href="css/testResults.css"/>
    <jsp:include page="commonHeadIncludes.jsp" flush="true"/>
    <script src="js/testResults.js" type="text/javascript"></script>


</head>
<body>
<div class="mainBody">
    <div class="mainContent">
        <jsp:include page="topPanel.jsp" flush="true"/>
        <div class="leftPanel">
            <ul id="testResultsTabs" class="leftPanelTabs">
                <li id="addTestResultsTab"><a href="testResultsAdd.html">Add</a></li>
                <li id="updateTestResultsTab" class="selectedTab"><a href="testResultsView.html">View</a></li>
            </ul>
        </div>
        <div class="centerPanel">
            <div class="centralContent">
                <div id="testResults">
                    <div id="testResultsMessagePanel" class="infoMessage">

                        <c:if test="${model.testResultAdded==true}">
                            <p>Test Result Added Successfully</p>
                        </c:if>
                        <c:if test="${model.testResultUpdated==true}">
                            <p>Test Result Updated Successfully</p>
                        </c:if>
                        <c:if test="${model.createNewProduct==true}">
                            <p><a class="link"
                                  href="products.html?collectionNumber=${model.collectionNumber}">Click
                                here to create product</a></p>
                        </c:if>
                        <c:if test="${model.testResultUpdated==false}">
                            <p>Test Result Was NOT Updated</p>
                        </c:if>
                        <c:if test="${model.collectionNotFound==true}">
                            <p>Collection Number ${model.collectionNumber} not found. Click<a class="link"
                                                                                              href="collections.html?collectionNumber=${model.collectionNumber}">
                                here</a> to create collection</p>
                        </c:if>
                    </div>
                    <div id="testResultsErrorMessagePanel" class="message"></div>

                    <form:form action="updateExistingTestResults.html" id="updateTestResultsForm">
                        <input
                                type="hidden"
                                id="existingTestResultId"
                                name="existingTestResultId"
                                <c:if test="${model.hasTestResult==true}">value="${model.testResult.testResultId}"</c:if>
                                />

                        <div class="inputFieldRow"><label
                                for="collectionNumber">${model.collectionNoDisplayName}: </label><input
                                type="text"
                                req="true"
                                id="collectionNumber"
                                maxlength="32"
                                name="collectionNumber"
                                <c:if test="${model.hasTestResult==true}">value="${model.testResult.collectionNumber}"</c:if>
                                />
                        </div>

                        <div class="inputFieldRow"><label
                                for="testResultDate">${model.dateTestedDisplayName}: </label><input
                                type="text"
                                req="true"
                                dateValid="true"
                                id="testResultDate"
                                name="testResultDate"
                                <c:if test="${model.hasTestResult==true}">value="${model.testResult.dateTested}"</c:if>
                                />
                        </div>

                        <div id="subProductDetails">

                            <div id="allTestResults">
                                <div class="testResultsRow"><label for="hiv" class="paddedLabel">${model.hivDisplayName}: </label>

                                    <div id="hiv" class="testResultOptions">
                                        <input type="radio" id="hivReactive" name="hiv" value="reactive"
                                               <c:if test="${model.hasTestResult==true and model.testResult.hiv=='reactive'}">checked="checked"</c:if>

                                                />
                                        <label for="hivReactive" class="radioLabel">Reactive</label>


                                        <input type="radio" id="hivNegative" name="hiv" value="negative"
                                               <c:if test="${model.hasTestResult==true and model.testResult.hiv=='negative'}">checked="checked"</c:if>
                                                />
                                        <label for="hivNegative" class="radioLabel">Negative</label>

                                    </div>
                                </div>

                                <div class="testResultsRow"><label for="hbv" class="paddedLabel">${model.hbvDisplayName}: </label>

                                    <div id="hbv" class="testResultOptions">
                                        <input type="radio" id="hbvReactive" name="hbv" value="reactive"
                                               <c:if test="${model.hasTestResult==true and model.testResult.hbv=='reactive'}">checked="checked"</c:if>

                                                />
                                        <label for="hbvReactive" class="radioLabel">Reactive</label>
                                        <input type="radio" id="hbvNegative" name="hbv" value="negative"
                                               <c:if test="${model.hasTestResult==true and model.testResult.hbv=='negative'}">checked="checked"</c:if>

                                                />
                                        <label for="hbvNegative" class="radioLabel">Negative</label>

                                    </div>
                                </div>

                                <div class="testResultsRow"><label for="hcv" class="paddedLabel">${model.hcvDisplayName}: </label>

                                    <div id="hcv" class="testResultOptions">
                                        <input type="radio" id="hcvReactive" name="hcv" value="reactive"
                                               <c:if test="${model.hasTestResult==true and model.testResult.hcv=='reactive'}">checked="checked"</c:if>
                                                />
                                        <label for="hcvReactive" class="radioLabel">Reactive</label>
                                        <input type="radio" id="hcvNegative" name="hcv" value="negative"
                                               <c:if test="${model.hasTestResult==true and model.testResult.hcv=='negative'}">checked="checked"</c:if>
                                                />
                                        <label for="hcvNegative" class="radioLabel">Negative</label>

                                    </div>
                                </div>

                                <div class="testResultsRow"><label for="syphilis"
                                                                   class="paddedLabel">${model.syphilisDisplayName}: </label>

                                    <div id="syphilis" class="testResultOptions">
                                        <input type="radio" id="syphilisReactive" name="syphilis" value="reactive"
                                               <c:if test="${model.hasTestResult==true and model.testResult.syphilis=='reactive'}">checked="checked"</c:if>
                                                />
                                        <label for="syphilisReactive" class="radioLabel">Reactive</label>
                                        <input type="radio" id="syphilisNegative" name="syphilis" value="negative"
                                               <c:if test="${model.hasTestResult==true and model.testResult.syphilis=='negative'}">checked="checked"</c:if>
                                                />
                                        <label for="syphilisNegative" class="radioLabel">Negative</label>

                                    </div>
                                </div>

                                <div class="testResultsRow"><label for="abo" class="paddedLabel">${model.aboDisplayName}: </label>

                                    <div id="abo" class="testResultOptions">
                                        <input type="radio" id="aboA" name="abo" value="A"
                                               <c:if test="${model.hasTestResult==true and model.testResult.abo=='A'}">checked="checked"</c:if>
                                                />
                                        <label for="aboA" class="radioLabel">A</label>
                                        <input type="radio" id="aboB" name="abo" value="B"
                                               <c:if test="${model.hasTestResult==true and model.testResult.abo=='B'}">checked="checked"</c:if>
                                                />
                                        <label for="aboB" class="radioLabel">B</label>
                                        <input type="radio" id="aboAB" name="abo" value="AB"
                                               <c:if test="${model.hasTestResult==true and model.testResult.abo=='AB'}">checked="checked"</c:if>
                                                />
                                        <label for="aboAB" class="radioLabel">AB</label>
                                        <input type="radio" id="aboO" name="abo" value="O"
                                               <c:if test="${model.hasTestResult==true and model.testResult.abo=='O'}">checked="checked"</c:if>
                                                />
                                        <label for="aboO" class="radioLabel">O</label>


                                    </div>
                                </div>

                                <div class="testResultsRow"><label for="rhd" class="paddedLabel">${model.rhdDisplayName}: </label>

                                    <div id="rhd" class="testResultOptions">
                                        <input type="radio" id="rhDPositive" name="rhd" value="positive"
                                               <c:if test="${model.hasTestResult==true and model.testResult.rhd=='positive'}">checked="checked"</c:if>
                                                />
                                        <label for="rhDPositive" class="radioLabel">+ Positive</label>
                                        <input type="radio" id="rhDNegative" name="rhd" value="negative"
                                               <c:if test="${model.hasTestResult==true and model.testResult.rhd=='negative'}">checked="checked"</c:if>
                                                />
                                        <label for="rhDNegative" class="radioLabel">- Negative</label>

                                    </div>
                                </div>
                                <div class="inputFieldRow"><label
                                        for="comment">${model.commentDisplayName}: </label><input
                                        type="text"
                                        id="comment"
                                        maxlength="250"
                                        name="comment"
                                        <c:if test="${model.hasTestResult==true}">value="${model.testResult.comment}"</c:if>

                                        />
                                </div>
                            </div>
                        </div>

                        <div id="actionButtons" class="actionButtonsPanel">
                            <input id="updateTestResultButton" type="button" value="Update"/>
                            <input id="deleteTestResultButton" type="button" value="Delete"/>
                        </div>
                    </form:form>

                </div>
            </div>
        </div>
        <c:if test="${fn:length(model.tipsDisplayName)>0}">
           <div id="showTips" class="link showTips">show tips</div>
    <div class="rightPanel">
        <div id="hideTips" class="link hideTips">hide tips</div>
                <p class="tipsTitle">Tips</p>

                <p>${fn:replace(model.tipsDisplayName,newLineChar,"<br/>")}</p>
            </div>
        </c:if>
        <jsp:include page="bottomPanel.jsp" flush="true"/>

    </div>
</div>
</body>
</html>