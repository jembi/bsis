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


</head>
<body>
<div class="mainBody">
    <div class="mainContent">
        <jsp:include page="topPanel.jsp" flush="true"/>
        <div class="leftPanel">
            <ul id="testResultsTabs" class="leftPanelTabs">
                <li id="addTestResultsTab"><a href="testResultsAdd.html">Add</a></li>
                <li id="updateTestResultsTab" class="selectedTab"><a href="testResultsView.html">View</a>
                </li>
            </ul>
        </div>
        <div class="centerPanel">
            <div class="centralContent">
                <div id="testResultsMessagePanel" class="infoMessage">
                    <c:if test="${model.collectionNotFound==true}">
                        <p>Collection ${model.collectionNumber} not found. Click<a class="link"
                                                                                   href="collections.html?collectionNumber=${model.collectionNumber}">
                            here</a> to create collection</p>
                    </c:if>
                </div>
                <form:form action="findAllTestResultsByCollection.html" id="findTestResultsForm">
                    <div class="inputFieldRow"><label
                            for="collectionNumber">${model.collectionNoDisplayName}: </label><input
                            type="text"
                            id="collectionNumber"
                            name="collectionNumber"
                            />
                    </div>
                    <div id="actionButtons" class="actionButtonsPanel">
                        <input id="findTestResultButton" type="submit" value="Find All Test Results"/>
                    </div>
                </form:form>

                <c:if test="${model.noResultsFound==true}">
                    <div class="infoMessage">
                        No test results found for collection number ${model.collectionNumber}.
                    </div>
                </c:if>

                <c:if test="${not empty model.allTestResults}">
                    <div id="allTestResults">

                        <table id="testResultsTable">
                            <tr>
                                <th>${model.collectionNoDisplayName}</th>
                                <th>${model.dateCollectedDisplayName}</th>
                                <th>${model.dateTestedDisplayName}</th>
                                <th>${model.bloodGroupDisplayName}</th>
                                <th>${model.hivDisplayName}</th>
                                <th>${model.hbvDisplayName}</th>
                                <th>${model.hcvDisplayName}</th>
                                <th>${model.syphilisDisplayName}</th>
                                <th>${model.commentDisplayName}</th>
                                <th></th>
                            </tr>
                            <c:forEach var="result" items="${model.allTestResults}">
                                <tr>
                                    <td>${result.collectionNumber}</td>
                                    <td>${result.dateCollected}</td>
                                    <td>${result.dateTested}</td>
                                    <td>${result.abo}
                                        <c:choose>
                                            <c:when test="${result.rhd=='positive'}">
                                                +
                                            </c:when>
                                            <c:when test="${result.rhd=='negative'}">
                                                -
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td>${result.hiv}</td>
                                    <td>${result.hbv}</td>
                                    <td>${result.hcv}</td>
                                    <td>${result.syphilis}</td>
                                    <td>${result.comment}</td>

                                    <td>
                                        <a href="selectTestResult.html?selectedTestResultId=${result.testResultId}">edit/delete</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                </c:if>
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