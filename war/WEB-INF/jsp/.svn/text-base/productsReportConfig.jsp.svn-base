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
                <li id="collectionsTab"><a href="admin-collectionsReportFieldsConfig.html">Collections</a>
                <li id="testResultsTab"><a href="admin-testResultsReportFieldsConfig.html">Test Results</a>
                <li id="productsTab" class="selectedTab"><a href="admin-productsReportFieldsConfig.html">Products</a>
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
                <div id="productsConfigPanel" class="tabbedPanel">

                    <form:form action="admin-saveProductsReportConfig.html">


                    <div id="productsConfigOptions" style="clear:both;margin-top: 10px;">
                        <div class="inputFieldRow">
                            <input type="checkbox" id="productNo" name="productNo" value="true"
                                   <c:if test="${model.productNoFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="productNo" class="checkboxLabel">Product No.</label>
                        </div>
                        <div class="inputFieldRow">
                            <input type="checkbox" id="collectionNo" name="collectionNo" value="true"
                                   <c:if test="${model.collectionNoFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="collectionNo" class="checkboxLabel">Collection No.</label>
                        </div>
                        <div class="inputFieldRow">
                            <input type="checkbox" id="dateCollected" name="dateCollected" value="true"
                                   <c:if test="${model.dateCollectedFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="dateCollected" class="checkboxLabel">Date Collected</label>
                        </div>
                        <div class="inputFieldRow">
                            <input type="checkbox" id="productType" name="productType" value="true"
                                   <c:if test="${model.productTypeFieldName=='true'}">checked="checked"</c:if>
                                    />
                            <label for="productType" class="checkboxLabel">Product Type</label>
                        </div>

                        <div class="actionButtonsPanel" id="collectionsReportConfigButtonPanel"><input
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

        <p>Select fields to be displayed on report</p>
    </div>
        <jsp:include page="bottomPanel.jsp" flush="true"/>

    </div>
</div>
</body>
</html>