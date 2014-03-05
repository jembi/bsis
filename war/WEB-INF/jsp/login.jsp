<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BSIS</title>
<jsp:include page="commonHeadIncludes.jsp" flush="true" />
<link type="text/css" rel="stylesheet" href="css/login.css" />
</head>
<body>
<!-- Prompt IE users to install Chrome Frame.
       chromium.org/developers/how-tos/chrome-frame-getting-started -->
<!--[if lt IE 10]>
<p class=chromeframe>Your browser is not supported. <a href="http://browsehappy.com/" target="_blank">Upgrade to a different browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true" target="_blank">install Google Chrome Frame</a> to experience this site.</p>
<![endif]-->
  <script>
    $(document).ready(function() {
      $('#username').val('');
      $('#password').val('');
      $('#username').focus();
      $('#loginButton').button({
        icons : {
          primary : "ui-icon-locked"
        }
      });
    });
  </script>
  <div class="mainBody">
    <div class="mainContent">
      <div id="v2vHeading">
        <a href="/bsis">Blood Safety Information System</a>
      </div>
      <div class="loginPanel">
        <div class="centralContent">
          <div id="login">
            <c:if test="${not empty param.error}">
              <jsp:include page="common/errorBox.jsp">
                <jsp:param name="errorMessage" value="Your login attempt was not successful, try again." />
              </jsp:include>
            </c:if>

          <form action="<c:url value='j_spring_security_check' />"
              id="loginAction" method="POST">
              <input type="hidden" name="targetUrl" value="${targetUrl}">

              <div class="inputFieldRow">
                <label for="username">Username </label>
                <input type="text" id="username" name="j_username" />
              </div>
              <div class="inputFieldRow">
                <label for="password">Password </label>
                <input type="password" id="password" name="j_password" />
              </div>

              <div>
                <button id="loginButton" name="submit" type="submit" class="loginButton">Log In</button>
              </div>
            </form>

          </div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>
