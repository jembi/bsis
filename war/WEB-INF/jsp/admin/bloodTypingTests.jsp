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
	$("#${mainContentId}").find(".bloodTypingTestsTable").dataTable({
    "bJQueryUI" : true,
    "sDom" : '<"H"lrT>t<"F"i>T',
    "aaSorting" : [],
    "bPaginate" : false,
    "bSort" : false,
    "oTableTools" : {
      "sRowSelect" : "single",
      "aButtons" : [],
      "fnRowSelected" : function(node) {
									        var elements = $(node).children();
									        if (elements[0].getAttribute("class") === "dataTables_empty") {
									          return;
									        }
									        var selectedRowId = elements[0].innerHTML;
									        console.log("row selected");
												},
			"fnRowDeselected" : function(node) {
													  var elements = $(node).children();
										        if (elements[0].getAttribute("class") === "dataTables_empty") {
										          return;
										        }
										        var selectedRowId = elements[0].innerHTML;
										        console.log("row deselected");
													},
	    }
	});
});
</script>

<div id="${tabContentId}">

	<div id="${mainContentId}">


		<div style="width: 700px; margin-left: 50px; margin-top: 50px; border: thin solid #1075A1; border-radius: 5px;">
			<div style="font-weight: bold; margin: 30px;">Blood Typing tests</div>
			<table class="bloodTypingTestsTable">	
				<thead>
					<tr>
						<th style="display: none;"></th>
						<th style="width: 300px;">Pattern</th>
						<th style="width: 100px;">
							Result
						</th>
						<th style="width: 100px;">
							Pending tests
						</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="bloodTypingRule" items="${bloodTypingRules}">
						<tr style="cursor: pointer;">
							<td style="display: none;">${bloodTypingRule.id}</td>
							<td style="text-align: center;">
								<c:forEach var="bloodTypingTest" items="${bloodTypingTests}">
									<c:if test="${not empty bloodTypingRule.patternMap[bloodTypingTest.id]}">
										<div>
											<label style="width: 130px; display: inline-block; text-align: left; cursor: pointer;">${bloodTypingTest.testNameShort}</label>
											<label style="width: 130px; display: inline-block; text-align: left; cursor: pointer;">${bloodTypingRule.patternMap[bloodTypingTest.id]}</label>
										</div>
									</c:if>
								</c:forEach>
							</td>
							<td style="text-align: center;">
								${bloodTypingRule.newInformation}
							</td>
							<td style="width: auto; text-align: center;">
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

	</div>

	<div id="${childContentId}">
	</div>

</div>