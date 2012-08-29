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
    <script src="js/collectionReport.js" type="text/javascript"></script>
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
        <li id="collectionsReportTab" class="selectedTab"><a href="collectionReport.html">Collections</a></li>
        <li id="testResultsReportTab"><a href="testResultReport.html">Test Results</a></li>
        <li id="productsReportTab"><a href="productReport.html">Products</a></li>
    </ul>
</div>

<div class="centerPanel">
<div class="centralContent">
<div id="collectionsReportPanel" class="tabbedPanel">

<div id="collectionErrorMessagePanel" class="message"></div>
<form:form action="getCollectionReport.html" id="collectionsReportForm">
    <div class="inputFieldRow"><label for="collectionFromDate">From Date: </label><input
            type="text"
            id="collectionFromDate"
            name="collectionFromDate"
            <c:if test="${model.hasCollectionDetails==true}">value="${model.fromDate}"</c:if>
            />
    </div>
    <div class="inputFieldRow"><label for="collectionToDate">To Date: </label><input
            type="text"
            id="collectionToDate"
            name="collectionToDate"
            <c:if test="${model.hasCollectionDetails==true}">value="${model.toDate}"</c:if>
            />
    </div>

    <div class="inputFieldRow">
        <label for="collectionBloodTypeFilters">Blood Types: </label>

        <div id="collectionBloodTypeFilters" class="checkboxList">
            <input type="checkbox" id="allBloodTypes" name="allBloodTypes" value="true"
                   <c:if test="${model.hasCollectionDetails==true and model.allBloodTypes=='true'}">checked="checked"</c:if>
                   <c:if test="${not model.hasCollectionDetails==true}">checked="checked"</c:if>
                    />
            <label for="allBloodTypes" class="checkboxLabel">All</label>
            <input type="checkbox" class="bloodGroupOption" id="aPositive" name="aPositive" value="true"
                   <c:if test="${model.hasCollectionDetails==true and model.aPositive=='true'}">checked="checked"</c:if>
                    />
            <label for="aPositive" class="checkboxLabel">A+</label>

            <input type="checkbox" class="bloodGroupOption" id="aNegative" name="aNegative" value="true"
                   <c:if test="${model.hasCollectionDetails==true and model.aNegative=='true'}">checked="checked"</c:if>
                    />
            <label for="aNegative" class="checkboxLabel">A-</label>

            <input type="checkbox" class="bloodGroupOption" id="bPositive" name="bPositive" value="true"
                   <c:if test="${model.hasCollectionDetails==true and model.bPositive=='true'}">checked="checked"</c:if>
                    />
            <label for="bPositive" class="checkboxLabel">B+</label>

            <input type="checkbox" class="bloodGroupOption" id="bNegative" name="bNegative" value="true"
                   <c:if test="${model.hasCollectionDetails==true and model.bNegative=='true'}">checked="checked"</c:if>
                    />
            <label for="bNegative" class="checkboxLabel">B-</label>

            <input type="checkbox" class="bloodGroupOption" id="abPositive" name="abPositive" value="true"
                   <c:if test="${model.hasCollectionDetails==true and model.abPositive=='true'}">checked="checked"</c:if>
                    />
            <label for="abPositive" class="checkboxLabel">AB+</label>

            <input type="checkbox" class="bloodGroupOption" id="abNegative" name="abNegative" value="true"
                   <c:if test="${model.hasCollectionDetails==true and model.abNegative=='true'}">checked="checked"</c:if>
                    />
            <label for="abNegative" class="checkboxLabel">AB-</label>

            <input type="checkbox" class="bloodGroupOption" id="oPositive" name="oPositive" value="true"
                   <c:if test="${model.hasCollectionDetails==true and model.oPositive=='true'}">checked="checked"</c:if>
                    />
            <label for="oPositive" class="checkboxLabel">O+</label>

            <input type="checkbox" class="bloodGroupOption" id="oNegative" name="oNegative" value="true"
                   <c:if test="${model.hasCollectionDetails==true and model.oNegative=='true'}">checked="checked"</c:if>
                    />
            <label for="oNegative" class="checkboxLabel">O-</label>

        </div>


    </div>


    <div class=" inputFieldRow"><label for="collectionAggregateType">Aggregate: </label>

        <select id="collectionAggregateType"
                name="collectionAggregateType">
            <option value=""
                    <c:if test="${model.hasCollectionDetails==true and model.collectionAggregateType==''}">selected="selected"</c:if>>
            </option>
            <option value="daily"
                    <c:if test="${model.hasCollectionDetails==true and model.collectionAggregateType=='daily'}">selected="selected"</c:if>>
                Daily
            </option>
            <option value="monthly"
                    <c:if test="${model.hasCollectionDetails==true and model.collectionAggregateType=='monthly'}">selected="selected"</c:if>>
                Monthly
            </option>
            <option value="yearly"
                    <c:if test="${model.hasCollectionDetails==true and model.collectionAggregateType=='yearly'}">selected="selected"</c:if>>
                Yearly
            </option>

        </select>

    </div>
    <div class="actionButtonsPanel" id="collectionsReportButtonPanel"><input
            id="collectionsReportButton"
            type="button"
            value="View"/>
    </div>
</form:form>


<div class="infoMessage">
    <c:if test="${model.noCollectionsFound==true}">No Collections Found</c:if>
</div>
<%--<c:if test="${model.hasCollectionReportGraph==true}">--%>
<%--<img src="charts/${model.bloodGroupChartName}.png" alt="Error in generating graph. Please retry.">--%>
<%--</c:if>--%>
<%--<c:if test="${model.hasDailyCollectionReportGraph==true}">--%>
<%--<img src="charts/${model.dailyCollectionsChartName}.png" alt="Error in generating graph. Please retry.">--%>
<%--</c:if>--%>
<%--<c:if test="${model.hasMonthlyCollectionReportGraph==true}">--%>
<%--<img src="charts/${model.monthlyCollectionsChartName}.png" alt="Error in generating graph. Please retry.">--%>
<%--</c:if>--%>
<%--<c:if test="${model.hasYearlyCollectionReportGraph==true}">--%>
<%--<img src="charts/${model.yearlyCollectionsChartName}.png" alt="Error in generating graph. Please retry.">--%>
<%--</c:if>--%>
<c:if test="${model.hasCollectionReport==true}">
    <div class="tableContainer">
        <table id="collectionReportTable">
            <tr>
                <c:if test="${model.collectionNoFieldName==true}">
                    <th>${model.collectionNoDisplayName}</th>
                </c:if>
                <c:if test="${model.centerFieldName==true}">
                    <th>${model.centerDisplayName}</th>
                </c:if>
                <c:if test="${model.siteFieldName==true}">
                    <th>${model.siteDisplayName}</th>
                </c:if>
                <c:if test="${model.dateCollectedFieldName==true}">
                    <th>${model.dateCollectedDisplayName}</th>
                </c:if>
                <c:if test="${model.sampleNoFieldName==true}">
                    <th>${model.sampleNoDisplayName}</th>
                </c:if>
                <c:if test="${model.shippingNoFieldName==true}">
                    <th>${model.shippingNoDisplayName}</th>
                </c:if>
                <c:if test="${model.donorNoFieldName==true}">
                    <th>${model.donorNoDisplayName}</th>
                </c:if>
                <c:if test="${model.donorTypeFieldName==true}">
                    <th>${model.donorTypeDisplayName}</th>
                </c:if>
                <c:if test="${model.commentFieldName==true}">
                    <th>${model.commentDisplayName}</th>
                </c:if>
                <c:if test="${model.bloodGroupFieldName==true}">
                    <th>${model.bloodGroupDisplayName}</th>
                </c:if>
                <c:if test="${model.rhdFieldName==true}">
                    <th>${model.rhdDisplayName}</th>
                </c:if>
            </tr>
            <c:forEach var="collection" items="${model.collections}">
                <tr>
                    <c:if test="${model.collectionNoFieldName==true}">

                        <td>${collection.collectionNumber}</td>
                    </c:if>
                    <c:if test="${model.centerFieldName==true}">
                        <td>${collection.centerName}</td>
                    </c:if>
                    <c:if test="${model.siteFieldName==true}">
                        <td>${collection.siteName}</td>
                    </c:if>
                    <c:if test="${model.dateCollectedFieldName==true}">
                        <td>${collection.dateCollected}</td>
                    </c:if>
                    <c:if test="${model.sampleNoFieldName==true}">
                        <td>${collection.sampleNumber}</td>
                    </c:if>
                    <c:if test="${model.shippingNoFieldName==true}">
                        <td>${collection.shippingNumber}</td>
                    </c:if>
                    <c:if test="${model.donorNoFieldName==true}">
                        <td>${collection.donorNumber}</td>
                    </c:if>
                    <c:if test="${model.donorTypeFieldName==true}">
                        <td>${collection.donorType}</td>
                    </c:if>
                    <c:if test="${model.commentFieldName==true}">
                        <td>${collection.comment}</td>
                    </c:if>
                    <c:if test="${model.bloodGroupFieldName==true}">
                        <td>${collection.abo}</td>
                    </c:if>
                    <c:if test="${model.rhdFieldName==true}">
                        <td>${collection.rhd}</td>
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
<c:if test="${fn:length(model.collectionsTipsDisplayName)>0}">
   <div id="showTips" class="link showTips">show tips</div>
    <div class="rightPanel">
        <div id="hideTips" class="link hideTips">hide tips</div>
        <p class="tipsTitle">Tips</p>

        <p>${fn:replace(model.collectionsTipsDisplayName,newLineChar,"<br/>")}</p>
    </div>
</c:if>
<jsp:include page="bottomPanel.jsp" flush="true"/>
</div>
</div>
</body>
</html>