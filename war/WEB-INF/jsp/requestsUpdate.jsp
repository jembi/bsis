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
    <jsp:include page="commonHeadIncludes.jsp" flush="true"/>
    <script src="js/requests.js" type="text/javascript"></script>


</head>
<body>
<div class="mainBody">
    <div class="mainContent">
        <jsp:include page="topPanel.jsp" flush="true"/>
        <div class="leftPanel">
            <ul id="requestsTabs" class="leftPanelTabs">
                <li id="addRequestsTab"><a href="requestsAdd.html">Add</a></li>
                <li id="updateRequestsTab" class="selectedTab"><a href="viewAllRequests.html">View</a></li>
            </ul>
        </div>
        <div class="centerPanel">
            <div class="centralContent">
                <div class="infoMessage" id="requestMessagePanel">
                    <c:if test="${model.requestAdded==true}">Request added successfully</c:if>
                    <c:if test="${model.requestUpdated==true}">Request updated successfully</c:if>
                    <c:if test="${model.requestDeleted==true}">Request ${model.deletedRequestNumber} deleted</c:if>
                </div>
                <form:form action="updateExistingRequest.html" id="updateRequestsForm">
                    <div class="message" id="requestsErrorMessagePanel">
                    </div>
                    <input type="hidden" id="requestId" name="requestId"
                           <c:if test="${model.hasRequest==true}">value="${model.request.requestId}"</c:if>>

                    <div class="inputFieldRow"><label for="requestNumber">${model.requestNoDisplayName}: </label><input
                            type="text"
                            id="requestNumber"
                            req="true"
                            maxlength="32"
                            name="requestNumber"
                            <c:if test="${model.hasRequest==true}">value="${model.request.requestNumber}"</c:if>
                            />
                    </div>
                    <c:if test="${model.showrequestDate==true}">
                        <div class="inputFieldRow"><label
                                for="requestDate">${model.requestDateDisplayName}: </label><input
                                type="text"
                                req="true"
                                dateValid="true"
                                id="requestDate"
                                name="requestDate"
                                <c:if test="${model.hasRequest==true}">value="${model.request.dateRequested}"</c:if>
                                />
                        </div>
                    </c:if>
                    <c:if test="${model.showrequiredDate==true}">

                        <div class="inputFieldRow"><label
                                for="requiredDate">${model.requiredDateDisplayName}: </label><input
                                type="text"
                                dateValid="true"
                                id="requiredDate"
                                name="requiredDate"
                                <c:if test="${model.hasRequest==true}">value="${model.request.dateRequired}"</c:if>
                                />
                        </div>
                    </c:if>
                    <div class="inputFieldRow"><label for="site">${model.siteDisplayName}: </label><select
                            id="site"
                            name="site">

                        <c:forEach var="site" items="${model.sites}">
                            <option value="${site.locationId}"
                                    <c:if test="${model.hasRequest==true and model.request.siteId==site.locationId}">selected="selected"</c:if>>
                                    ${site.name}</option>
                        </c:forEach>

                    </select>

                    </div>

                    <div class="inputFieldRow"><label for="productType">${model.productTypeDisplayName}: </label>

                        <div id="productType" class="radioButtonsList">
                            <input type="radio" id="wholeBlood" name="productType" value="wholeBlood"
                                   <c:if test="${model.hasRequest==true and model.request.productType=='wholeBlood'}">checked="checked"</c:if>
                                   <c:if test="${!model.hasRequest==true}">checked="checked"</c:if>
                                    />
                            <label for="wholeBlood" class="radioLabel">Whole Blood</label>
                            <input type="radio" id="rcc" name="productType" value="rcc"
                                   <c:if test="${model.hasRequest==true and model.request.productType=='rcc'}">checked="checked"</c:if>
                                    />
                            <label for="rcc" class="radioLabel">RCC</label>
                            <input type="radio" id="ffp" name="productType" value="ffp"
                                   <c:if test="${model.hasRequest==true and model.request.productType=='ffp'}">checked="checked"</c:if>
                                    />
                            <label for="ffp" class="radioLabel">FFP</label>

                            <input type="radio" id="platelets" name="productType" value="platelets"
                                   <c:if test="${model.hasRequest==true and model.request.productType=='platelets'}">checked="checked"</c:if>
                                    />
                            <label for="platelets" class="radioLabel">Platelets</label>

                        </div>
                    </div>

                    <div class="inputFieldRow"><label for="abo">${model.aboDisplayName}: </label>

                        <div id="abo" class="radioButtonsList">
                            <input type="radio" id="aboA" name="abo" value="A"
                                   <c:if test="${model.hasRequest==true and model.request.abo=='A'}">checked="checked"</c:if>
                                   <c:if test="${!model.hasRequest==true}">checked="checked"</c:if>
                                    />
                            <label for="aboA" class="radioLabel">A</label>
                            <input type="radio" id="aboB" name="abo" value="B"
                                   <c:if test="${model.hasRequest==true and model.request.abo=='B'}">checked="checked"</c:if>
                                    />
                            <label for="aboB" class="radioLabel">B</label>
                            <input type="radio" id="aboAB" name="abo" value="AB"
                                   <c:if test="${model.hasRequest==true and model.request.abo=='AB'}">checked="checked"</c:if>
                                    />
                            <label for="aboAB" class="radioLabel">AB</label>
                            <input type="radio" id="aboO" name="abo" value="O"
                                   <c:if test="${model.hasRequest==true and model.request.abo=='O'}">checked="checked"</c:if>
                                    />
                            <label for="aboO" class="radioLabel">O</label>


                        </div>
                    </div>

                    <div class="inputFieldRow"><label for="rhd">${model.rhdDisplayName}: </label>

                        <div id="rhd" class="radioButtonsList">
                            <input type="radio" id="rhDPositive" name="rhd" value="positive"
                                   <c:if test="${model.hasRequest==true and model.request.rhd=='positive'}">checked="checked"</c:if>
                                   <c:if test="${!model.hasRequest==true}">checked="checked"</c:if>
                                    />
                            <label for="rhDPositive" class="radioLabel">+ Positive</label>
                            <input type="radio" id="rhDNegative" name="rhd" value="negative"
                                   <c:if test="${model.hasRequest==true and model.request.rhd=='negative'}">checked="checked"</c:if>
                                    />
                            <label for="rhDNegative" class="radioLabel">- Negative</label>

                        </div>
                    </div>

                    <div class="inputFieldRow"><label for="quantity">${model.quantityDisplayName} :</label><input
                            type="text"
                            id="quantity"
                            maxlength="10"
                            req="true"
                            numOnly="true"
                            name="quantity"
                            <c:if test="${model.hasRequest==true}">value="${model.request.quantity}"</c:if>
                            />
                    </div>
                    <c:if test="${model.showcomment==true}">

                        <div class="inputFieldRow"><label for="comment">${model.commentDisplayName} :</label><input
                                type="text"
                                id="comment"
                                maxlength="250"
                                name="comment"
                                <c:if test="${model.hasRequest==true}">value="${model.request.comment}"</c:if>
                                />
                        </div>
                    </c:if>

                    <div class="inputFieldRow"><label for="status">${model.statusDisplayName}: </label>

                        <select id="status"
                                name="status">
                            <option value="pending"
                                    <c:if test="${model.hasRequest==true and model.request.status=='pending'}">selected="selected"</c:if>>
                                Pending
                            </option>
                            <option value="partiallyFulfilled"
                                    <c:if test="${model.hasRequest==true and model.request.status=='partiallyFulfilled'}">selected="selected"</c:if>>
                                Partially Fulfilled
                            </option>
                            <option value="fulfilled"
                                    <c:if test="${model.hasRequest==true and model.request.status=='fulfilled'}">selected="selected"</c:if>>
                                Fulfilled
                            </option>
                        </select>

                    </div>

                    <div id="actionButtons" class="actionButtonsPanel">
                        <input id="updateRequestButton" type="button" value="Update"/>
                        <input id="deleteRequestButton" type="button" value="Delete"/>
                    </div>
                </form:form>
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