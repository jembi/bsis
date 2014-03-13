<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>
<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tabContent-${unique_page_id}</c:set>
<c:set var="mainContentId">mainContent-${unique_page_id}</c:set>
<c:set var="childContentId">childContent-${unique_page_id}</c:set>
<c:set var="ttiTestSelectorId">ttiTestSelectorId-${unique_page_id}</c:set>
<script>
	$(document)
			.ready(
					function() {
						$("#${mainContentId}").find(".doneTtiResultsButton")
								.button().click(uploadTtiResults);
						function hideMainContent() {
							$("#${mainContentId}").remove();
						}

						function uploadTtiResults() {
							hideMainContent();
							$
									.ajax({
										url : "uploadTTIResultsFormGenerator.html",
										//data: {tsvFile : tsvFile},
										success : function(response) {
											notifyParentDone();
										},
										error : function(response) {
											showErrorMessage("Something went wrong. Please try again.");
										}
									});
						}

						function notifyParentDone() {
							$("#${tabContentId}").parent().trigger(
									"ttiResultSuccess");
						}

						function refetchForm() {
							$("#${tabContentId}").load("${refreshUrl}");
						}

						function emptyChildContent() {
							$("#${childContentId}").html("");
						}
					});
</script>
<div id="${tabContentId}">
	<div id="${mainContentId}">
		<form class="formFormatClass">
			<div>
				<label style="width: auto;"> <b>Import TTI Results</b>
				</label>
			</div>
			<div class="tipsBox ui-state-highlight">
				<p>TTI Results Imported into BSIS</p>
			</div>
			<br>
			<div>${SuccessRows}</div>
			<div>${FailedRows}</div>
		</form>
		<div class="formFormatClass">
			<div>
				<button class="doneTtiResultsButton">Done</button>
			</div>
		</div>
	</div>
	<div id="${childContentId}"></div>
</div>