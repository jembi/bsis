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
    <script src="js/productReport.js" type="text/javascript"></script>
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
        <li id="testResultsReportTab"><a href="testResultReport.html">Test Results</a></li>
        <li id="productsReportTab" class="selectedTab"><a href="productReport.html">Products</a></li>
    </ul>
</div>

<div class="centerPanel">
    <div class="centralContent">

        <div id="productsReportPanel" class="tabbedPanel">
            <div id="productsErrorMessagePanel" class="message"></div>

            <form:form action="getProductReport.html" id="productsReportForm">

                <div class="inputFieldRow"><label for="collectionFromDate">From Date: </label><input
                        type="text"
                        id="collectionFromDate"
                        name="collectionFromDate"
                        <c:if test="${model.hasProductDetails==true}">value="${model.fromDate}"</c:if>
                        />
                </div>
                <div class="inputFieldRow"><label for="collectionToDate">To Date: </label><input
                        type="text"
                        id="collectionToDate"
                        name="collectionToDate"
                        <c:if test="${model.hasProductDetails==true}">value="${model.toDate}"</c:if>
                        />
                </div>

                <div class="inputFieldRow">
                    <label for="productTypeFilters">Product Types: </label>

                    <div id="productTypeFilters" class="checkboxList">
                        <input type="checkbox" id="allProducts" name="allProducts" value="true"
                               <c:if test="${model.hasProductDetails==true and model.allProducts=='true'}">checked="checked"</c:if>
                               <c:if test="${not model.hasProductDetails==true}">checked="checked"</c:if>

                                />
                        <label for="allProducts" class="checkboxLabel">All</label>
                        <input type="checkbox" class="productTypeOption" id="wholeBlood" name="wholeBlood"
                               value="true"
                               <c:if test="${model.hasProductDetails==true and model.wholeBlood=='true'}">checked="checked"</c:if>
                                />
                        <label for="wholeBlood" class="checkboxLabel">Whole Blood</label>

                        <input type="checkbox" class="productTypeOption" id="rcc" name="rcc" value="true"
                               <c:if test="${model.hasProductDetails==true and model.rcc=='true'}">checked="checked"</c:if>
                                />
                        <label for="rcc" class="checkboxLabel">RCC</label>

                        <input type="checkbox" class="productTypeOption" id="ffp" name="ffp" value="true"
                               <c:if test="${model.hasProductDetails==true and model.ffp=='true'}">checked="checked"</c:if>
                                />
                        <label for="ffp" class="checkboxLabel">FFP</label>

                        <input type="checkbox" class="productTypeOption" id="platelets" name="platelets"
                               value="true"
                               <c:if test="${model.hasProductDetails==true and model.platelets=='true'}">checked="checked"</c:if>
                                />
                        <label for="platelets" class="checkboxLabel">Platelets</label>
                        <span style="display:block;">

                        <input type="checkbox" class="productTypeOption" id="partialPlatelets" name="partialPlatelets"
                               value="true"
                               <c:if test="${model.hasProductDetails==true and model.partialPlatelets=='true'}">checked="checked"</c:if>
                                />
                        <label for="partialPlatelets" class="checkboxLabel">Partial Platelets</label>

                         </span>
                    </div>


                </div>


                <div class=" inputFieldRow"><label for="productAggregateType">Aggregate: </label>

                    <select id="productAggregateType"
                            name="productAggregateType">
                        <option value=""
                                <c:if test="${model.hasProductDetails==true and model.productAggregateType==''}">selected="selected"</c:if>>
                        </option>
                        <option value="daily"
                                <c:if test="${model.hasProductDetails==true and model.productAggregateType=='daily'}">selected="selected"</c:if>>
                            Daily
                        </option>
                        <option value="monthly"
                                <c:if test="${model.hasProductDetails==true and model.productAggregateType=='monthly'}">selected="selected"</c:if>>
                            Monthly
                        </option>
                        <option value="yearly"
                                <c:if test="${model.hasProductDetails==true and model.productAggregateType=='yearly'}">selected="selected"</c:if>>
                            Yearly
                        </option>

                    </select>

                </div>
                <div class="actionButtonsPanel" id="productsReportButtonPanel"><input
                        id="productsReportButton"
                        type="button"
                        value="View"/>
                </div>
            </form:form>


            <div class="infoMessage">
                <c:if test="${model.noProductsFound==true}">No Products Found</c:if>
            </div>
            <%--<c:if test="${model.hasProductReportGraph==true}">--%>
            <%--<img src="charts/${model.productReportChartName}.png"--%>
            <%--alt="Error in generating graph. Please retry.">--%>
            <%--</c:if>--%>
            <c:if test="${model.hasProductReport==true}">
                <div class="tableContainer">

                    <table id="productReportTable">
                        <tr>
                            <c:if test="${model.productNoFieldName==true}">
                                <th>${model.productNoDisplayName}</th>
                            </c:if>
                            <c:if test="${model.collectionNoFieldName==true}">
                                <th>${model.collectionNoDisplayName}</th>
                            </c:if>
                            <c:if test="${model.dateCollectedFieldName==true}">
                                <th>${model.dateCollectedDisplayName}</th>
                            </c:if>
                            <c:if test="${model.productTypeFieldName==true}">
                                <th>${model.productTypeDisplayName}</th>
                            </c:if>
                        </tr>
                        <c:forEach var="product" items="${model.products}">
                            <tr>
                                <c:if test="${model.productNoFieldName==true}">

                                    <td>${product.productNumber}</td>
                                </c:if>
                                <c:if test="${model.collectionNoFieldName==true}">

                                    <td>${product.collectionNumber}</td>
                                </c:if>

                                <c:if test="${model.dateCollectedFieldName==true}">

                                    <td>${product.dateCollectedString}</td>
                                </c:if>

                                <c:if test="${model.productTypeFieldName==true}">

                                    <td>${product.type}</td>
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
<c:if test="${fn:length(model.productsTipsDisplayName)>0}">
   <div id="showTips" class="link showTips">show tips</div>
    <div class="rightPanel">
        <div id="hideTips" class="link hideTips">hide tips</div>
        <p class="tipsTitle">Tips</p>

        <p>${fn:replace(model.productsTipsDisplayName,newLineChar,"<br/>")}</p>
    </div>
</c:if>
<jsp:include page="bottomPanel.jsp" flush="true"/>

</div>
</div>

</body>
</html>