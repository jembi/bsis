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
    <link type="text/css" rel="stylesheet" href="css/requests.css"/>
    <jsp:include page="commonHeadIncludes.jsp" flush="true"/>


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
                <div id="requestsMessagePanel" class="infoMessage">
                    <c:if test="${model.productsIssued==true}">
                        <p>Products issued successfully for Request Number : ${model.requestNumber}</p>
                    </c:if>
                    <c:if test="${model.noRequestsFound==true}">
                        <p>No pending requests found</p>
                    </c:if>
                </div>


                <c:if test="${not empty model.allRequests}">
                    <div id="allRequests">

                        <table id="requestsTable">
                            <tr>
                                <th>${model.requestNoDisplayName}</th>
                                <c:if test="${model.showrequestDate==true}">

                                <th>${model.requestDateDisplayName}</th>
                                </c:if>
                                <c:if test="${model.showrequiredDate==true}">

                                <th>${model.requiredDateDisplayName}</th>
                                </c:if>
                                <th>${model.siteDisplayName}</th>
                                <th>${model.bloodGroupDisplayName}</th>
                                <th>${model.productTypeDisplayName}</th>
                                <th>${model.quantityDisplayName}</th>
                                <c:if test="${model.showcomment==true}">

                                <th>${model.commentDisplayName}</th>
                                </c:if>
                                <th>${model.statusDisplayName}</th>
                                <th></th>
                            </tr>
                            <c:forEach var="request" items="${model.allRequests}">
                                <tr>
                                    <td>${request.requestNumber}</td>
                                     <c:if test="${model.showrequestDate==true}">

                                    <td>${request.dateRequested}</td>
                                    </c:if>
                                    <c:if test="${model.showrequiredDate==true}">

                                    <td>${request.dateRequired}</td>
                                    </c:if>
                                    <td>${request.siteName}</td>
                                    <td>${request.abo}
                                        <c:choose>
                                            <c:when test="${request.rhd=='positive'}">
                                                +
                                            </c:when>
                                            <c:when test="${request.rhd=='negative'}">
                                                -
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td>${request.productType}</td>
                                    <td>${request.quantity}</td>
                                     <c:if test="${model.showcomment==true}">

                                    <td>${request.comment}</td>
                                    </c:if>
                                    <td>${request.status}</td>

                                    <td>
                                        <a href="issueProduct.html?selectedRequestId=${request.requestId}">issue</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                </c:if>
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
</div>
</div>
</body>
</html>