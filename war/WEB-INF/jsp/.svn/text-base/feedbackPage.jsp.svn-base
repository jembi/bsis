<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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
        <jsp:include page="topPanel.jsp" flush="true"/>

        <div class="leftAlignedPanel">

            <div id="messagePanel" class="infoMessage">
                <c:if test="${model.feedbackSaved==true}">
                    <p>We have saved your comments.</p>

                    <p>Thank you!</p>
                </c:if>
            </div>
            <form:form action="submitFeedback.html" id="feedbackAction">
                <div class="inputFieldRow"><label for="comments">Comments: </label>
                    <textarea rows="10" cols="30" id="comments" name="comments"></textarea>

                    <div class="actionButtonsPanel">
                        <input style="margin-left:165px;" type="submit" value="Save"/>
                    </div>
                </div>
            </form:form>
        </div>
        <div class="rightColumn" style="margin-top: 90px;float: none;width:90%;">
            <div class="infoMessage">
                You can also contact us by email at
                <p>Prof. Santosh Vempala : <a class="link" href="mailto:vempala@cc.gatech.edu">vempala@cc.gatech.edu</a>
                </p>

                <p>Surabhi Potnis : <a class="link"
                                       href="mailto:surabhi.potnis@gatech.edu">surabhi.potnis@gatech.edu</a>
                </p>
            </div>
        </div>
    </div>
</div>
</body>
</html>
