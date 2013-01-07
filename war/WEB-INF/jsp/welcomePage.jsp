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
</head>
<body style="margin-top: 2px;">
	<div class="mainBody">
		<div class="mainContent">
			<jsp:include page="topPanel.jsp" flush="true" />
		</div>
		<div class="bottomPanel">
				<span
				class="ui-icon ui-icon-mail-closed" style="display: inline-block; margin-left: 20px;"></span><a
				href="mailto:rohit.banga@gatech.edu, vempala@cc.gatech.edu"
				target="_blank">Contact Us</a>
		</div>
	</div>
	<div id="preloader">
		<div style="text-align: center; -webkit-transition: 4s ease-out; -webkit-transition-delay: 2s; height: 400px;">
			<img src="images/content_loading.gif" />
			<div>
				<span style="font-size: 21pt;">Loading... </span>
			</div>
		</div>
	</div>
</body>
</html>
