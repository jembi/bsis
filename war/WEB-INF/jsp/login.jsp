<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>V2V</title>
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
        <a href="/v2v">VEIN-TO-VEIN</a>
      </div>
      <div class="loginPanel">
        <div class="centralContent">
          <div id="login">
 
          <form name='f' action="<c:url value='j_spring_security_check' />"
                method='POST'>
            <table>
              <tr>
                <td>User:</td>
                <td><input type='text' name='j_username' value=''>
                </td>
              </tr>
              <tr>
                <td>Password:</td>
                <td><input type='password' name='j_password' />
                </td>
              </tr>
              <tr>
                <td colspan='2'><input name="submit" type="submit"
                  value="submit" />
                </td>
              </tr>
              <tr>
                <td colspan='2'><input name="reset" type="reset" />
                </td>
              </tr>
            </table>
         </form>

          <form:form action="<c:url value='j_spring_security_check' />" id="loginAction" method="POST">
              <c:choose>
                <c:when test="${model.loginFailed==true}">
                  <script>
                    var options = {
                      backgroundColor : 'red',
                      delay : 1500,
                      speed : 300,
                    };
                    $.showMessage("Login Failed", options);
                    setTimeout(function() {
                      window.location = "/v2v";
                    }, 2000);
                  </script>
                </c:when>
              </c:choose>
              <input type="hidden" name="targetUrl" value="${targetUrl}">

              <div class="inputFieldRow">
                <label for="username">Username </label><input type="text"
                  id="username" name="username" />
              </div>
              <div class="inputFieldRow">
                <label for="password">Password </label><input type="password"
                  id="password" name="password" />
              </div>

              <div>
                <button id="loginButton" class="loginButton">Log In</button>
              </div>
            </form:form>
          </div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>
