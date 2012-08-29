<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>V2V</title>
    <jsp:include page="commonHeadIncludes.jsp" flush="true"/>
    <link type="text/css" rel="stylesheet" href="css/login.css"/>
    <script src="js/login.js" type="text/javascript"></script>
    <script src="js/jquery-ui-1.8.16.custom.min.js" type="text/javascript"></script>
    <link type="text/css" rel="stylesheet" href="css/jquery-ui-1.8.16.custom.css"/>

</head>
<body>
<div class="mainBody">
    <div class="mainContent">
        <div id="v2vHeading"><a href="/v2v">Vein-to-Vein</a></div>
        <div class="loginPanel">
            <div class="centralContent">
                <div id="login">
                    <form:form action="loginUser.html" id="loginAction">
                        <c:if test="${model.loginFailed==true}">
                            <div class="message">
                                Login failed
                            </div>
                        </c:if>
                        <input type="hidden" name="targetUrl" value="${targetUrl}">

                        <div class="inputFieldRow"><label for="username">Username: </label><input type="text"
                                                                                                  id="username"
                                                                                                  name="username"/>
                        </div>
                        <div class="inputFieldRow"><label for="password">Password: </label><input type="password"
                                                                                                  id="password"
                                                                                                  name="password"/>
                        </div>

                        <div><input class="loginButton" id="loginButton" type="submit" value="Login"/></div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
