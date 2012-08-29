<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>V2V</title>
    <jsp:include page="commonHeadIncludes.jsp" flush="true"/>
    <script src="js/testResultsReport.js" type="text/javascript"></script>
    <link type="text/css" rel="stylesheet" href="css/report.css"/>


</head>
<body>
<div class="mainBody">
<div class="mainContent">
<jsp:include page="topPanel.jsp" flush="true"/>
<div class="leftPanel">
    <ul id="reportsTabs" class="leftPanelTabs">
        <li id="inventorySummaryTab"><a href="inventorySummary.html">Inventory Summary</a></li>
        <li id="inventoryDetailsTab"><a href="inventoryDetails.html">Inventory Details</a></li>
        <li id="collectionsReportTab"><a href="collectionReport.html">Collections</a></li>
        <li id="testResultsReportTab" class="selectedTab"><a href="testResultReport.html">Test Results</a></li>
        <li id="productsReportTab"><a href="productReport.html">Products</a></li>
    </ul>
</div>

<div class="centerPanel">
<div class="centralContent">
<div id="testResultsReportPanel" class="tabbedPanel">
<div id="testResultsErrorMessagePanel" class="message"></div>

<form:form action="getTestResultReport.html" id="testResultsReportForm">
<div class="inputFieldRow"><label for="collectionFromDate">From Date: </label><input
        type="text"
        id="collectionFromDate"
        name="collectionFromDate"
        <c:if test="${model.hasTestResultsDetails==true}">value="${model.fromDate}"</c:if>
        />
</div>
<div class="inputFieldRow"><label for="collectionToDate">To Date: </label><input
        type="text"
        id="collectionToDate"
        name="collectionToDate"
        <c:if test="${model.hastestResultsDetails==true}">value="${model.toDate}"</c:if>
        />
</div>

<div class="inputFieldRow">
<label for="testResultsTestResultFilters">Test Results: </label>

<div id="testResultsTestResultFilters" class="checkboxList">
    <input type="checkbox" id="allTestResults" name="allTestResults" value="true"
           <c:if test="${model.hasTestResultsDetails==true and model.allTestResults=='true'}">checked="checked"</c:if>
           <c:if test="${not model.hasTestResultsDetails==true}">checked="checked"</c:if>

            />
    <label for="allTestResults" class="checkboxLabel">All</label>
    <input type="checkbox" class="indicatorOption" id="hiv" name="hiv" value="true"
           <c:if test="${model.hasTestResultsDetails==true and model.hiv=='true'}">checked="checked"</c:if>
            />
    <label for="hiv" class="checkboxLabel">HIV</label>
    <input type="checkbox" class="indicatorOption" id="hbv" name="hbv" value="true"
           <c:if test="${model.hasTestResultsDetails==true and model.hbv=='true'}">checked="checked"</c:if>
            />
    <label for="hbv" class="checkboxLabel">HBV</label>
    <input type="checkbox" class="indicatorOption" id="hcv" name="hcv" value="true"
           <c:if test="${model.hasTestResultsDetails==true and model.hcv=='true'}">checked="checked"</c:if>
            />
    <label for="hcv" class="checkboxLabel">HCV</label>
    <input type="checkbox" class="indicatorOption" id="syphilis" name="syphilis" value="true"
           <c:if test="${model.hasTestResultsDetails==true and model.syphilis=='true'}">checked="checked"</c:if>
            />
    <label for="syphilis" class="checkboxLabel">Syphilis</label>
    <input type="checkbox" class="indicatorOption" id="noIndicator" name="noIndicator" value="true"
           <c:if test="${model.hasTestResultsDetails==true and model.noIndicator=='true'}">checked="checked"</c:if>
            />
    <label for=noIndicator class="checkboxLabel">None</label>

</div>


<div class=" inputFieldRow"><label for="testResultsAggregateType">Aggregate: </label>

    <select id="testResultsAggregateType"
            name="testResultsAggregateType">
        <option value=""
                <c:if test="${model.hasTestResultsDetails==true and model.testResultsAggregateType==''}">selected="selected"</c:if>>
        </option>
        <option value="daily"
                <c:if test="${model.hasTestResultsDetails==true and model.testResultsAggregateType=='daily'}">selected="selected"</c:if>>
            Daily
        </option>
        <option value="monthly"
                <c:if test="${model.hasTestResultsDetails==true and model.testResultsAggregateType=='monthly'}">selected="selected"</c:if>>
            Monthly
        </option>
        <option value="yearly"
                <c:if test="${model.hasTestResultsDetails==true and model.testResultsAggregateType=='yearly'}">selected="selected"</c:if>>
            Yearly
        </option>

    </select>

</div>
<div class="actionButtonsPanel" id="testResultsReportButtonPanel"><input
        id="testResultsReportButton"
        type="button"
        value="View"/>
</div>
</form:form>

<div class="infoMessage">
    <c:if test="${model.noTestResultsFound==true}">No Test Results Found</c:if>
</div>
<%--<c:if test="${model.hasTestResultsGraph==true}">--%>
<%--<img src="charts/${model.chartName}.png" alt="Error in generating graph. Please retry.">--%>
<%--</c:if>--%>

<c:if test="${model.hasTestResultsReport==true}">
    <div class="tableContainer">
        <table id="testResultsReportTable">
            <tr>
                <c:if test="${model.collectionNoFieldName==true}">
                    <th>${model.collectionNoDisplayName}</th>
                </c:if>
                <c:if test="${model.dateCollectedFieldName==true}">

                    <th>${model.dateCollectedDisplayName}</th>
                </c:if>

                <c:if test="${model.dateTestedFieldName==true}">

                    <th>${model.dateTestedDisplayName}</th>
                </c:if>

                <c:if test="${model.bloodGroupFieldName==true}">

                    <th>${model.bloodGroupDisplayName}</th>
                </c:if>

                <c:if test="${model.hivFieldName==true}">

                    <th>${model.hivDisplayName}</th>
                </c:if>

                <c:if test="${model.hbvFieldName==true}">

                    <th>${model.hbvDisplayName}</th>
                </c:if>

                <c:if test="${model.hcvFieldName==true}">

                    <th>${model.hcvDisplayName}</th>
                </c:if>

                <c:if test="${model.syphilisFieldName==true}">

                    <th>${model.syphilisDisplayName}</th>
                </c:if>

                <c:if test="${model.commentsFieldName==true}">

                    <th>${model.commentDisplayName}</th>
                </c:if>

            </tr>
            <c:forEach var="result" items="${model.reportTestResults}">
                <tr>
                    <c:if test="${model.collectionNoFieldName==true}">

                        <td>${result.collectionNumber}</td>
                    </c:if>
                    <c:if test="${model.dateCollectedFieldName==true}">

                        <td>${result.dateCollected}</td>
                    </c:if>

                    <c:if test="${model.dateTestedFieldName==true}">
                        <td>${result.dateTested}</td>
                    </c:if>

                    <c:if test="${model.bloodGroupFieldName==true}">
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
                    </c:if>

                    <c:if test="${model.hivFieldName==true}">

                        <td>${result.hiv}</td>
                    </c:if>

                    <c:if test="${model.hbvFieldName==true}">
                        <td>${result.hbv}</td>
                    </c:if>

                    <c:if test="${model.hcvFieldName==true}">
                        <td>${result.hcv}</td>
                    </c:if>

                    <c:if test="${model.syphilisFieldName==true}">

                        <td>${result.syphilis}</td>
                    </c:if>

                    <c:if test="${model.commentsFieldName==true}">
                        <td>${result.comment}</td>
                    </c:if>

                </tr>
            </c:forEach>
        </table>
    </div>
</c:if>

<c:if test="${model.hasDailyCollectionReport==true}">
    <div class="tableContainer">

        <table id="dailyAggregateCollectionReportTable" class="aggregateTable">
            <tr>
                <th>Date</th>
                <th>Total Collections</th>
            </tr>
            <c:forEach var="entry" items="${model.dailyCollectionAggregates}">
                <tr>
                    <td>${entry.key}</td>
                    <td>${entry.value}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
</c:if>

<c:if test="${model.hasMonthlyCollectionReport==true}">
    <div class="tableContainer">

        <table id="monthlyAggregateCollectionReportTable" class="aggregateTable">
            <tr>
                <th>Month/Year</th>
                <th>Total Collections</th>
            </tr>
            <c:forEach var="entry" items="${model.monthlyCollectionAggregates}">
                <tr>
                    <td>${entry.key}</td>
                    <td>${entry.value}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
</c:if>

<c:if test="${model.hasYearlyCollectionReport==true}">
    <div class="tableContainer">

        <table id="yearlyAggregateCollectionReportTable" class="aggregateTable">
            <tr>
                <th>Year</th>
                <th>Total Collections</th>
            </tr>
            <c:forEach var="entry" items="${model.yearlyCollectionAggregates}">
                <tr>
                    <td>${entry.key}</td>
                    <td>${entry.value}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
</c:if>
</div>
</div>
</div>
</div>
<c:if test="${fn:length(model.testResultsTipsDisplayName)>0}">
   <div id="showTips" class="link showTips">show tips</div>
    <div class="rightPanel">
        <div id="hideTips" class="link hideTips">hide tips</div>
        <p class="tipsTitle">Tips</p>

        <p>${fn:replace(model.testResultsTipsDisplayName,newLineChar,"<br/>")}</p>
    </div>
</c:if>
<jsp:include page="bottomPanel.jsp" flush="true"/>

</div>

</div>
</body>
</html>