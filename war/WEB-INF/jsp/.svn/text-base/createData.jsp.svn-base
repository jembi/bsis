<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>V2V</title>
    <jsp:include page="commonHeadIncludes.jsp" flush="true"/>
    <script src="js/createData.js" type="text/javascript"></script>

</head>
<body>
<div class="mainBody">
    <div class="mainContent">
        <jsp:include page="adminTopPanel.jsp" flush="true"/>
        <div class="leftAlignedPanel">
            <div class="centralContent">
                <div id="messagePanel" class="infoMessage">
                    <c:if test="${model.dataCreated==true}">
                        Data Created
                    </c:if>

                    <c:if test="${model.dataDeleted==true}">
                        Data Deleted
                    </c:if>
                </div>
                <div>
                    <form:form action="admin-createDummyData.html">
                        <div class="inputFieldRow"><label for="donorNumber">Number of Donors </label><input
                                type="text"
                                id="donorNumber"
                                name="donorNumber"
                                style="margin-top: 10px;"
                                <c:if test="${model.hasDetails==true}">value="${model.donorNumber}"</c:if>
                                />
                        </div>
                        <div class="inputFieldRow"><label for="collectionNumber">Number of Collections </label><input
                                type="text"
                                id="collectionNumber"
                                name="collectionNumber"
                                style="margin-top: 10px;"
                                <c:if test="${model.hasDetails==true}">value="${model.collectionNumber}"</c:if>
                                />
                        </div>

                        <div class="inputFieldRow"><label for="productNumber">Number of Products </label><input
                                type="text"
                                id="productNumber"
                                name="productNumber"
                                style="margin-top: 10px;"
                                <c:if test="${model.hasDetails==true}">value="${model.productNumber}"</c:if>
                                />
                        </div>

                        <div class="inputFieldRow"><label for="requestNumber">Number of Requests </label><input
                                type="text"
                                id="requestNumber"
                                name="requestNumber"
                                style="margin-top: 10px;"
                                <c:if test="${model.hasDetails==true}">value="${model.requestNumber}"</c:if>
                                />
                        </div>

                        <div class="inputFieldRow"><input id="createButton" type="submit"
                                                          value="Create test data"/></div>
                    </form:form>
                </div>
                <div>
                    <form:form action="admin-deleteDummyData.html">
                </div>
                <div class="inputFieldRow"><input type="submit" value="Delete test data"/></div>
                </form:form>
            </div>
        </div>

       <div id="showTips" class="link showTips">show tips</div>
    <div class="rightPanel">
        <div id="hideTips" class="link hideTips">hide tips</div>
            <p class="tipsTitle">Tips</p>

            <p>Enter number of records of each type to be randomly generated. </p>

            <p>"Delete Data" will delete <b>ALL</b> records.</p>
        </div>
        <jsp:include page="bottomPanel.jsp" flush="true"/>

    </div>
</div>
</body>
</html>