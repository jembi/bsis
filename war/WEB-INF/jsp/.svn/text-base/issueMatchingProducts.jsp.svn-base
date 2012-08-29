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
    <link type="text/css" rel="stylesheet" href="css/issue.css"/>
    <script src="js/issue.js" type="text/javascript"></script>


</head>
<body>
<div class="mainBody">
    <div class="mainContent">
        <jsp:include page="topPanel.jsp" flush="true"/>
        <div class="leftPanel">
            <ul id="issueTabs" class="leftPanelTabs">
                <li id="viewRequestsTab" class="selectedTab"><a href="issueViewRequests.html">Pending Requests</a></li>
                <li id="allProductsTab"><a href="issueAnyProduct.html"> All Products</a></li>
            </ul>
        </div>
        <div class="centerPanel">
            <div class="centralContent">
                <div class="headingPanel">Matching Products</div>
                <div id="issueMessagePanel" class="infoMessage"></div>
                <div class="requestDetails">${model.siteDisplayName}: ${model.request.siteName}</div>
                <c:if test="${model.showrequestDate==true}">
                <div class="requestDetails">${model.requestDateDisplayName}: ${model.request.dateRequested}</div>
                </c:if>
                <c:if test="${model.showrequiredDate==true}">
                <div class="requestDetails">${model.requiredDateDisplayName} : ${model.request.dateRequired}</div>
                </c:if>
                <div class="requestDetails">${model.productTypeDisplayName} : ${model.request.productType}</div>
                <input id="requestProductType" type="hidden" value="${model.request.productType}"/>
                <div class="requestDetails infoMessage">${model.quantityDisplayName} required
                    : ${model.request.quantity}</div>
                <div class="requestDetails infoMessage">${model.quantityDisplayName} selected : <label
                        id="quantitySelected">0</label></div>
                <div id="issueProductsErrorPanel" class="message">
                </div>
                <div id="products">
                    <form:form action="issueSelectedProducts.html" id="issueSelectedProductsForm">
                        <input type="hidden" name="requestId" value="${model.request.requestId}"/>
                        <input type="hidden" id="quantityIssued" name="quantityIssued" value="0"/>
                        <table id="productsTable">
                            <tr>
                                <th>${model.productNoDisplayName}</th>
                                <th>${model.collectionNoDisplayName}</th>
                                <th>${model.dateCollectedDisplayName}</th>
                                <th>${model.productTypeDisplayName}</th>
                                <th>${model.bloodGroupDisplayName}</th>
                                <th>${model.siteDisplayName}</th>
                                <th>${model.issueDateDisplayName}</th>
                                <th>${model.issueDisplayName}?</th>
                            </tr>
                            <c:forEach var="product" items="${model.products}">
                                <tr>
                                    <td>${product.productNumber}</td>
                                    <td>${product.collectionNumber}</td>
                                    <td>${product.dateCollectedString}</td>
                                    <td class="productType">${product.type}</td>
                                    <td>${product.abo}
                                        <c:choose>
                                            <c:when test="${product.rhd=='positive'}">
                                                +
                                            </c:when>
                                            <c:when test="${product.rhd=='negative'}">
                                                -
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <select
                                                id="site-productId${product.productId}"
                                                name="site-productId${product.productId}">

                                            <c:forEach var="site" items="${model.sites}">
                                                <option value="${site.locationId}"
                                                        <c:if test="${model.siteId!=null and model.siteId==site.locationId}">selected="selected"</c:if>
                                                        >
                                                        ${site.name}
                                                </option>
                                            </c:forEach>

                                        </select>
                                    </td>
                                    <td>
                                        <input type="text" class="issueDate"
                                               id="issueDate-productId${product.productId}"
                                               name="issueDate-productId${product.productId}"/>
                                    </td>
                                    <td><input type="checkbox" class="issueCheckBox"
                                               id="productId-productId${product.productId}"
                                               name="productId-productId${product.productId}"
                                               value="${product.productId}"></td>
                                </tr>
                            </c:forEach>
                        </table>
                        <div class="actionButtonsPanel" id="matchingProductsButtonPanel"><input
                                type="button"
                                id="issueSelectedProductsButton"
                                value="Issue Selected Products"/>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
        <c:if test="${fn:length(model.issueTipsDisplayName)>0}">
           <div id="showTips" class="link showTips">show tips</div>
    <div class="rightPanel">
        <div id="hideTips" class="link hideTips">hide tips</div>
                <p class="tipsTitle">Tips</p>

                <p>${fn:replace(model.issueTipsDisplayName,newLineChar,"<br/>")}</p>
            </div>
        </c:if>
        <jsp:include page="bottomPanel.jsp" flush="true"/>

    </div>
</div>
</body>
</html>