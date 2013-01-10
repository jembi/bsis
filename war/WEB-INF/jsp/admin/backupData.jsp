<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  pageContext.setAttribute("newLineChar", "\n");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>


<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>
<c:set var="childContentId">childContent-${unique_page_id}</c:set>

<script>
$(document).ready(function() {
  $("#${tabContentId}").find(".backupDataButton").button().click(
      function() {
        window.open("backupData.zip");
      });
});
</script>

<div id="${tabContentId}" class="formDiv">
	<div id="${mainContentId}">
		<!-- div class="tipsBox ui-state-highlight">
			<p>
				Backup the V2V database by clicking on the button below.
			</p>
		</div-->
		<br />
		<br />
		<button class="backupDataButton">Backup the V2V Database</button>
	</div>
	<div id="${childContentId}">
	</div>
</div>