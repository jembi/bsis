<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>V2V</title>
    <jsp:include page="commonHeadIncludes.jsp" flush="true"/>


</head>
<body>
<div class="mainBody">
    <div class="mainContent">
        <jsp:include page="adminTopPanel.jsp" flush="true"/>
        <div class="leftPanel">
            <ul id="displayFieldsTabs" class="leftPanelTabs">
                <li id="collectionsTab"><a href="admin-collectionsDisplayFieldsConfig.html">Collections</a>
                </li>
                <li id="donorsTab"><a href="admin-donorsDisplayFieldsConfig.html">Donors</a></li>
                <li id="requestsTab" class="selectedTab"><a href="admin-requestsDisplayFieldsConfig.html">Requests</a>
                </li>
                <li id="usageTab"><a href="admin-usageDisplayFieldsConfig.html">Usage</a>
                </li>

            </ul>
        </div>
        <div class="centerPanel">
            <div class="centralContent">
                <div id="messagePanel" class="infoMessage">
                    <c:if test="${model.configSaved==true}">
                        Configuration Saved
                    </c:if>
                </div>
                <div id="displayFieldsConfigPanel" class="tabbedPanel">

                    <form:form action="admin-saveRequestsDisplayFieldsConfig.html">


                    <div id="requestConfigOptions" style="clear:both;margin-top: 10px;">

                        <div class="inputFieldRow">
                            <input type="checkbox" id="requestDate" name="requestDate" value="true"
                                   <c:if test="${model.showrequestDate=='true'}">checked="checked"</c:if>
                                    />
                            <label for="requestDate" class="checkboxLabel">Request Date</label>
                        </div>

                        <div class="inputFieldRow">
                            <input type="checkbox" id="requiredDate" name="requiredDate" value="true"
                                   <c:if test="${model.showrequiredDate=='true'}">checked="checked"</c:if>
                                    />
                            <label for="requiredDate" class="checkboxLabel">Required Date</label>
                        </div>
                        <div class="inputFieldRow">
                            <input type="checkbox" id="comment" name="comment" value="true"
                                   <c:if test="${model.showcomment=='true'}">checked="checked"</c:if>
                                    />
                            <label for="comment" class="checkboxLabel">Comment</label>
                        </div>


                        <div class="actionButtonsPanel" id="requestsConfigButtonPanel"><input
                                type="submit"
                                value="Save"/>
                        </div>
                        </form:form>

                    </div>
                </div>
				    </div>
        </div>
		<div id="showTips" class="link showTips">show tips</div>
    <div class="rightPanel">
        <div id="hideTips" class="link hideTips">hide tips</div>
        <p class="tipsTitle">Tips</p>

        <p>Select fields to be displayed on form</p>
    </div>
                <jsp:include page="bottomPanel.jsp" flush="true"/>

        
    </div>
</div>
</body>
</html>