<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
    return System.nanoTime();
  }%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="signoutButtonId">signout-${unique_page_id}</c:set>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BSIS - Configuration Error</title>
<link type="text/css" rel="stylesheet" href="css/common.css" />
<link type="text/css" rel="stylesheet" href="css/topPanel.css" />
<link type="text/css" rel="stylesheet"
  href="css/jquery-ui-1.8.16.custom.css" />
<link type="text/css" href="css/redmond.custom/redmond.custom.css"
  rel="Stylesheet" />

<script type="text/javascript" src="js/jquery-1.8.1.min.js"></script>
<script type="text/javascript"
  src="jquery-ui/js/jquery-ui-1.8.23.custom.min.js"></script>
</head>
<body>

  <script>
    $(document).ready(function() {
      $("#${signoutButtonId}").button().click(function() {
        window.location.replace("logout.html");
      });
    });
  </script>

  <div class="mainBody">
    <div class="mainContent">
      <div id="v2vHeading">
        <a href="/bsis">Blood Safety Information System</a>
      </div>
      <div class="headlink">
        <p class="topPanelUsername">Logged in as ${model.user.username}</p>
        <a href="logout.html"><span class="ui-icon ui-icon-locked"
          style="display: inline-block;"></span>Sign out</a>
      </div>
      <div class="firstTimeConfigDiv">
        An unexpected error occurred.
        Please <a href="mailto:support@jembi.org?subject=BSIS%20Support"
          target="_blank"> Contact Us </a> to report the problem. <br />
        <br /> <br />

        <button type="button" id="${signoutButtonId}"
          style="margin-left: 10px">Sign out</button>
      </div>
    </div>
    <div class="bottomPanel"></div>
  </div>
</body>
</html>
