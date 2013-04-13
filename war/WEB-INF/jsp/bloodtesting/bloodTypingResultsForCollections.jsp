<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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

<script>
$(document).ready(function() {
  var selectedCollectionId;
	$("#${mainContentId}").find(".bloodTypingPrimaryTestsDoneSummaryTable")
												.dataTable({
											    "bJQueryUI" : true,
											    "sDom" : '<"H"T>t<"F"i>',
											    "bSort" : false,	// disable sorting of columns as user should see collections in the same order as on the plate
											    "bPaginate" : false,	// not many collections on a plate so no need for pagination
											    "oTableTools" : {
											      "sRowSelect" : "single",
											      "aButtons" : [],
											      "fnRowSelected" : function(node) {
																				        var elements = $(node).children();
																				        if (elements[0].getAttribute("class") === "dataTables_empty") {
																				          return;
																				        }
																				        $("#${tabContentId}").find(".doneButton")
																				        										 .show();
																				        var collectionId = elements[0].innerHTML;
																				        selectedCollectionId = collectionId;
																				        $("#${mainContentId}").html("");
																				        var collectionSummaryUrl = "showCollectionSummaryForTesting.html?" + $.param({collectionId : collectionId});
																				        $("#${childContentId}").find(".collectionSummarySection")
																				        											 .load(collectionSummaryUrl);
																				        var bloodTypingUrl = "showBloodTypingResultsForCollection.html?" + $.param({collectionId : collectionId});
																				        $("#${childContentId}").find(".bloodTypingSection")
																				        											 .load(bloodTypingUrl);
																							},
							 							"fnRowDeselected" : function(node) {
																								}
													}
											   });

	$("#${tabContentId}").find(".doneButton")
											 .button({icons: {primary: 'ui-icon-check'}})
											 .click(doneButtonClicked);

	function doneButtonClicked() {
	  $("#${tabContentId}").trigger("collectionBloodTypingUpdated");
		$("#${tabContentId}").remove();
	}

	$("#${tabContentId}").find(".doneButton")
											 .hide();

	$("#${childContentId}").bind("testResultsUpdated", function() {
    var collectionSummaryUrl = "showCollectionSummaryForTesting.html?" + $.param({collectionId : selectedCollectionId});
    $("#${childContentId}").find(".collectionSummarySection")
    											 .load(collectionSummaryUrl);
    var bloodTypingUrl = "showBloodTypingResultsForCollection.html?" + $.param({collectionId : selectedCollectionId});
    $("#${childContentId}").find(".bloodTypingSection")
    											 .load(bloodTypingUrl);
	});
});
</script>

<div id="${tabContentId}">
		<div>
			<button class="doneButton" style="margin-left: 20px;">Return to previous screen</button>
		</div>
		<br />
		<br />

	<div id="${mainContentId}">

		<div style="width: 87%; margin-left: 20px;">
			<table class="bloodTypingPrimaryTestsDoneSummaryTable">
				<thead>
					<tr>
						<th style="display: none;"/>
						<th>${collectionFields.collectionNumber.displayName}</th>
						<th>Blood Typing Status</th>
						<th>Blood ABO</th>
						<th>Blood Rh</th>
						<th>Extra information</th>
						<th>Pending tests</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="collectionEntry" items="${collections}">
						<c:set var="collection" value="${collectionEntry.value}" />
						<c:set var="bloodTypingOutputForCollection" value="${bloodTypingOutput[collection.id]}" />
						<tr>
							<td style="display: none;">
								${collection.id}
							</td>
							<td style="width: 140px;">
								${collection.collectionNumber}
							</td>
							<td style="text-align: center; width: 140px;">
								${collection.bloodTypingStatus}
							</td>
							<td style="text-align: center; width: 140px;">
								${collection.bloodAbo}
							</td>
							<td style="text-align: center; width: 140px;">
								${collection.bloodRh}
							</td>
							<td>
								${collection.extraBloodTypeInformation}
							</td>
							<td style="width: auto;">
								<ul>
									<c:forEach var="pendingTestId" items="${bloodTypingOutputForCollection.pendingBloodTypingTestsIds}">
										<c:set var="pendingTest" value="${allBloodTypingTests[pendingTestId]}" />
										<li>
											${pendingTest.testName}
										</li>
									</c:forEach>
								</ul>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

	<div id="${childContentId}">
		<div class="collectionSummarySection">
		</div>
		<div class="bloodTypingSection">
		</div>
	</div>

</div>