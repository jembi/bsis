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
                <form:form action="findUsage.html" id="findUsageForm">

                    <c:if test="${model.usageNotFound==true}">
                        <div class="infoMessage">
                            Usage of Product Number ${model.productNumber} not found
                        </div>
                    </c:if>

                    <div class="inputFieldRow"><label for="productNumber">${model.productNoDisplayName}: </label><input
                            type="text"
                            id="productNumber"
                            name="productNumber"
                            />
                    </div>


                    <div id="actionButtons" class="actionButtonsPanel">
                        <input id="findUsageButton" type="submit" value="Find"/>
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