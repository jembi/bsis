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
    <link type="text/css" rel="stylesheet" href="css/inventory.css"/>


</head>
<body>
<div class="mainBody">
    <div class="mainContent">
        <jsp:include page="topPanel.jsp" flush="true"/>
        <div class="leftPanel">
            <ul id="reportsTabs" class="leftPanelTabs">
                <li id="inventorySummaryTab" class="selectedTab"><a href="inventorySummary.html">Inventory Summary</a>
                </li>
                <li id="inventoryDetailsTab"><a href="inventoryDetails.html">Inventory Details</a>
                </li>
                <li id="collectionsReportTab"><a href="collectionReport.html">Collections</a></li>
                <li id="testResultsReportTab"><a href="testResultReport.html">Test Results</a></li>
                <li id="productsReportTab"><a href="productReport.html">Products</a></li>
            </ul>
        </div>
        <div class="centerPanel">
            <div class="centralContent">
                <div class="headingPanel">Product Inventory</div>
                <div id="inventory">
                    <%--<c:if test="${model.hasInventoryReportGraph==true}">--%>
                    <%--<img src="charts/${model.inventoryChartName}.png"--%>
                    <%--alt="Error in generating graph. Please retry.">--%>
                    <%--</c:if>--%>
                    <p>Total Products : ${model.totalProducts}</p>

                    <p>Total Whole Blood : ${model.totalWholeBlood}</p>

                    <p>Total RCC : ${model.totalRCC}</p>

                    <p>Total FFP : ${model.totalFFP}</p>

                    <p>Total Platelets : ${model.totalPlatelets}</p>

                   <p>Total Partial Platelets : ${model.totalPartialPlatelets}</p>
                </div>
            </div>
        </div>
        <c:if test="${fn:length(model.inventorySummaryTipsDisplayName)>0}">
           <div id="showTips" class="link showTips">show tips</div>
    <div class="rightPanel">
        <div id="hideTips" class="link hideTips">hide tips</div>
                <p class="tipsTitle">Tips</p>

                <p>${fn:replace(model.inventorySummaryTipsDisplayName,newLineChar,"<br/>")}</p>
            </div>
        </c:if>
        <jsp:include page="bottomPanel.jsp" flush="true"/>

    </div>
</div>
</body>
</html>