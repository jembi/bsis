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
    <script src="js/usage.js" type="text/javascript"></script>


</head>
<body>
<div class="mainBody">
    <div class="mainContent">
        <jsp:include page="topPanel.jsp" flush="true"/>
        <div class="leftPanel">
            <ul id="usageTabs" class="leftPanelTabs">
                <li id="addUsageTab"><a href="usageAdd.html">Add</a></li>
                <li id="updateUsageTab" class="selectedTab"><a href="updateUsage.html">Update</a></li>
            </ul>
        </div>
        <div class="centerPanel">
            <div class="centralContent">
                <form:form action="updateSelectedUsage.html" id="updateUsageForm">
                    <div class="message" id="usageErrorMessagePanel">
                    </div>
                    <div id="usageMessagePanel" class="infoMessage">
                        <c:if test="${model.usageAdded==true}">
                            Usage of Product Number ${model.usage.productNumber} added
                        </c:if>

                        <c:if test="${model.usageUpdated==true}">
                            Usage updated successfully
                        </c:if>
                    </div>

                    <div class="inputFieldRow"><label for="productNumber">${model.productNoDisplayName}: </label><input
                            type="text"
                            id="productNumber"
                            req="true"
                            maxlength="32"
                            name="productNumber"
                            <c:if test="${model.hasUsage==true}">value="${model.usage.productNumber}"</c:if>
                            />
                    </div>
                    <c:if test="${model.showusageDate==true}">

                        <div class="inputFieldRow"><label for="usageDate">${model.usageDateDisplayName}: </label><input
                                type="text"
                                dateValid="true"
                                id="usageDate"
                                name="usageDate"
                                <c:if test="${model.hasUsage==true}">value="${model.usage.dateUsedString}"</c:if>
                                />
                        </div>
                    </c:if>
                    <c:if test="${model.showhospital==true}">

                        <div class="inputFieldRow"><label for="hospital">${model.hospitalDisplayName}: </label><input
                                type="text"
                                id="hospital"
                                maxlength="250"
                                name="hospital"
                                <c:if test="${model.hasUsage==true}">value="${model.usage.hospital}"</c:if>
                                />
                        </div>
                    </c:if>
                    <c:if test="${model.showward==true}">

                        <div class="inputFieldRow"><label for="ward">${model.wardDisplayName}: </label><input
                                type="text"
                                id="ward"
                                maxlength="250"
                                name="ward"
                                <c:if test="${model.hasUsage==true}">value="${model.usage.ward}"</c:if>
                                />
                        </div>
                    </c:if>
                    <div class="inputFieldRow"><label for="useIndication">${model.useIndicationDisplayName}: </label>

                        <select id="useIndication"
                                name="useIndication">
                            <option value="used"
                                    <c:if test="${model.hasUsage==true and model.usage.useIndication=='used'}">selected="selected"</c:if>>
                                Used
                            </option>
                            <option value="discarded"
                                    <c:if test="${model.hasUsage==true and model.usage.useIndication=='discarded'}">selected="selected"</c:if>>
                                Discarded
                            </option>
                            <option value="other"
                                    <c:if test="${model.hasUsage==true and model.usage.useIndication=='other'}">selected="selected"</c:if>>
                                Other
                            </option>
                        </select>

                    </div>
                    <c:if test="${model.showcomment==true}">

                        <div class="inputFieldRow"><label for="comment">${model.commentDisplayName}: </label><input
                                type="text"
                                id="comment"
                                maxlength="250"
                                name="comment"
                                <c:if test="${model.hasUsage==true}">value="${model.usage.comment}"</c:if>
                                />
                        </div>
                    </c:if>

                    <div id="actionButtons" class="actionButtonsPanel">
                        <input id="updateUsageButton" type="button" value="Update"/>
                        <input id="deleteUsageButton" type="button" value="Delete"/>
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