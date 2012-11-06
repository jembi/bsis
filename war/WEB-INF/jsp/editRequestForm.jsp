<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="editRequestFormDivId">editRequestFormDiv-${unique_page_id}</c:set>
<c:set var="editRequestFormId">editRequestForm-${unique_page_id}</c:set>
<c:set var="deleteRequestConfirmDialogId">deleteRequestConfirmDialog-${unique_page_id}</c:set>
<c:set var="editRequestFormProductTypesId">editRequestFormProductTypesId-${unique_page_id}</c:set>
<c:set var="editRequestFormSitesId">editRequestFormSitesId-${unique_page_id}</c:set>
<c:set var="editRequestFormDateRequestedId">editRequestFormDateRequestedId-${unique_page_id}</c:set>
<c:set var="editRequestFormDateRequiredId">editRequestFormDateRequiredId-${unique_page_id}</c:set>
<c:set var="updateRequestButtonId">updateRequestButton-${unique_page_id}</c:set>
<c:set var="deleteRequestButtonId">deleteRequestButton-${unique_page_id}</c:set>
<c:set var="findProductsButtonId">findProductsButton-${unique_page_id}</c:set>
<c:set var="goBackButtonId">goBackButton-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {
        $("#${updateRequestButtonId}").button({
          icons : {
            primary : 'ui-icon-plusthick'
          }
        }).click(function() {
          updateExistingRequest($("#${editRequestFormId}")[0]);
        });

        $("#${deleteRequestButtonId}").button({
          icons : {
            primary : 'ui-icon-minusthick'
          }
        }).click(
            function() {
              $("#${deleteRequestConfirmDialogId}").dialog(
                  {
                    modal : true,
                    title : "Confirm Delete",
                    buttons : {
                      "Delete" : function() {
                        var requestNumber = $("#${editRequestFormId}").find(
                            "[name='requestNumber']").val();
                        deleteRequest(requestNumber);
                        $(this).dialog("close");
                      },
                      "Cancel" : function() {
                        $(this).dialog("close");
                      }
                    }
                  });
            });

        $("#${findProductsButtonId}").button({
          icons : {
            primary : 'ui-icon-search'
          }
        }).click(
            function() {
              var parentDivId = $("#${editRequestFormDivId}").parent().attr(
                  "id");
              console.log(parentDivId);
              replaceContent(parentDivId, "${model.requestUrl}",
                  "findMatchingProductsForRequest.html", {
                    requestNumber : "${model.requestNumber}"
                  });
              $("html, body").animate({
                scrollTop : $(document).height()
              }, "slow");
            });

        $("#${goBackButtonId}").button({
          icons : {
            primary : 'ui-icon-circle-arrow-w'
          }
        }).click(function() {
          window.history.back();
          return false;
        });

        $("#${editRequestFormDateRequestedId}").datepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -36500,
          maxDate : 0,
          dateFormat : "mm/dd/yy",
          yearRange : "c-100:c0",
        });

        $("#${editRequestFormDateRequiredId}").datepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -36500,
          maxDate : 365,
          dateFormat : "mm/dd/yy",
          yearRange : "c-100:c2",
        });

        $("#${editRequestFormSitesId}").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${editRequestFormProductTypesId}").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });
      });
</script>


<div id="${editRequestFormDivId}" class="editFormDiv">
	<form:form method="POST" commandName="editRequestForm"
		id="${editRequestFormId}">
		<table>
			<thead>
				<tr>
					<td><b>Request</b></td>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><form:label path="requestNumber">${model.requestNoDisplayName}</form:label></td>
					<td><form:input path="requestNumber" /></td>
				</tr>
				<tr>
					<td><form:label path="dateRequested">${model.requestDateDisplayName}</form:label></td>
					<td><form:input path="dateRequested"
							id="${editRequestFormDateRequestedId}" /></td>
				</tr>
				<tr>
					<td><form:label path="dateRequired">${model.requiredDateDisplayName}</form:label></td>
					<td><form:input path="dateRequired"
							id="${editRequestFormDateRequiredId}" /></td>
				</tr>
				<tr>
					<td><form:label path="quantity">Requested Quantity</form:label></td>
					<td><form:input path="quantity"></form:input></td>
				</tr>
				<tr>
					<td><form:label path="abo">Blood ABO</form:label></td>
					<td><form:radiobutton path="abo" value="A" label="A"
							class="radioWithToggle" /> <form:radiobutton path="abo"
							value="B" label="B" class="radioWithToggle" /> <form:radiobutton
							path="abo" value="AB" label="AB" class="radioWithToggle" /> <form:radiobutton
							path="abo" value="O" label="O" class="radioWithToggle" /></td>
				</tr>
				<tr>
					<td><form:label path="rhd">Rh</form:label></td>
					<td><form:radiobutton path="rhd" value="negative"
							label="negative" class="radioWithToggle" /> <form:radiobutton
							path="rhd" value="positive" label="positive"
							class="radioWithToggle" /></td>
				</tr>
				<tr>
					<td><form:label path="status">${model.statusDisplayName}</form:label></td>
					<td><form:radiobutton path="status" value="partiallyFulfilled"
							label="Partially Fulfilled" /> <form:radiobutton path="status"
							value="pending" label="Pending" /> <form:radiobutton
							path="status" value="fulfilled" label="Fulfilled" /></td>
				</tr>
				<tr>
					<td><form:label path="sites">${model.siteDisplayName}</form:label></td>
					<td style="padding-left: 10px;"><form:select path="sites"
							id="${editRequestFormSitesId}" class="editRequestFormSites">
							<c:forEach var="site" items="${model.sites}">
								<form:option value="${site}" label="${site}"
									selected="${site == model.selectedSite ? 'selected' : ''}" />
							</c:forEach>
						</form:select></td>
				</tr>
				<tr>
					<td><form:label path="productType">${model.productTypeDisplayName}</form:label></td>
					<td style="padding-left: 10px;"><form:select
							path="productType" id="${editRequestFormProductTypesId}"
							class="editRequestFormTypes">
							<form:option value="wholeBlood" label="Whole Blood"
								selected="${model.selectedProductType == 'wholeBlood' ? 'selected' : ''}" />
							<form:option value="rcc" label="RCC"
								selected="${model.selectedProductType == 'rcc' ? 'selected' : ''}" />
							<form:option value="ffp" label="ffp"
								selected="${model.selectedProductType == 'ffp' ? 'selected' : ''}" />
							<form:option value="platelets" label="Platelets"
								selected="${model.selectedProductType == 'platelets' ? 'selected' : ''}" />
							<form:option value="partialPlatelets" label="Partial Platelets"
								selected="${model.selectedProductType == 'partialPlatelets' ? 'selected' : ''}" />
						</form:select></td>
				</tr>
				<tr>
					<td><form:label path="patientName">Patient Name</form:label></td>
					<td><form:input path="patientName" /></td>
				</tr>
				<tr>
					<td><form:label path="comments">${model.commentsDisplayName}</form:label></td>
					<td><form:textarea path="comments" class="commentsInputBox"
							maxlength="255" /></td>
				</tr>
				<c:if test="${model.isDialog != 'yes' }">
					<tr>
						<td />
						<td><button type="button" id="${updateRequestButtonId}"
								style="margin-left: 10px">Save</button>
							<button type="button" id="${goBackButtonId}"
								style="margin-left: 10px">Go Back</button>
							<button type="button" id="${findProductsButtonId}"
								style="margin-left: 10px">Find Products to Issue</button>
							<button type="button" id="${deleteRequestButtonId}"
								style="margin-left: 10px">Delete</button></td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</form:form>
</div>

<div id="${deleteRequestConfirmDialogId}" style="display: none">Are
	you sure you want to delete this Request?</div>
