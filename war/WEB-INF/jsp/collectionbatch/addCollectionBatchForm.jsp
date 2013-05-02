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
<c:set var="addCollectionBatchFormId">addCollectionBatchForm-${unique_page_id}</c:set>
<c:set var="addCollectionBatchFormCentersId">addCollectionBatchFormCenters-${unique_page_id}</c:set>
<c:set var="addCollectionBatchFormSitesId">addCollectionBatchFormSites-${unique_page_id}</c:set>
<c:set var="printButtonId">printButton-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        function notifyParentSuccess() {
						// let the parent know we are done
						$("#${tabContentId}").parent().trigger("editCollectionBatchSuccess");
				}
  
        function notifyParentCancel() {
	        $("#${tabContentId}").parent().trigger("editCollectionBatchCancel");
        }

        $("#${tabContentId}").find(".cancelButton").button({
          icons : {
            primary : 'ui-icon-closethick'
          }
        }).click(notifyParentCancel);

        $("#${tabContentId}").find(".addCollectionBatchButton").button({
          icons : {
            primary : 'ui-icon-plusthick'
          }
        }).click(
            function() {
                addNewCollectionBatch($("#${addCollectionBatchFormId}")[0],
                    									"${tabContentId}", notifyParentSuccess);
            });

        $("#${printButtonId}").button({
          icons : {
            primary : 'ui-icon-print'
          }
        }).click(function() {
          $("#${addCollectionBatchFormId}").printArea();
        });

        $("#${addCollectionBatchFormCentersId}").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${addCollectionBatchFormSitesId}").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

        $("#${tabContentId}").find(".clearFormButton").button({
          icons : {            
          }
        }).click(refetchForm);

        function refetchForm() {
          $.ajax({
            url: "${refreshUrl}",
            data: {},
            type: "GET",
            success: function (response) {
              			 	 $("#${tabContentId}").replaceWith(response);
            				 },
            error:   function (response) {
											 showErrorMessage("Something went wrong. Please try again.");
            				 }
            
          });
        }

        if ("${firstTimeRender}" == "true") {
          // just set the default values for the new collection  
        	$("#${tabContentId}").find('textarea[name="notes"]').html("${collectionBatchFields.notes.defaultValue}");
        	setDefaultValueForSelector(getCollectionCenterSelector(), "${collectionBatchFields.collectionCenter.defaultValue}");
        	setDefaultValueForSelector(getCollectionSiteSelector(), "${collectionBatchFields.collectionSite.defaultValue}");
        }

        function getCollectionCenterSelector() {
          return $("#${tabContentId}").find('select[name="collectionCenter"]').multiselect();
        }

        function getCollectionSiteSelector() {
          return $("#${tabContentId}").find('select[name="collectionSite"]').multiselect();
        }

		});
</script>

<div id="${tabContentId}">

	<c:if test="${!empty success && !success}">
			<jsp:include page="../common/errorBox.jsp">
				<jsp:param name="errorMessage" value="${errorMessage}" />
			</jsp:include>
	</c:if>

	<form:form method="POST" commandName="addCollectionBatchForm"
		class="formFormatClass" id="${addCollectionBatchFormId}">
		<c:if test="${!collectionBatchFields.batchNumber.autoGenerate}">
			<c:if test="${collectionBatchFields.batchNumber.hidden != true }">
				<div>
					<form:label path="batchNumber">${collectionBatchFields.batchNumber.displayName}</form:label>
					<form:input path="batchNumber" value="${firstTimeRender ? collectionBatchFields.batchNumber.defaultValue : ''}" />
					<form:errors class="formError"
						path="collectionBatch.batchNumber" delimiter=", "></form:errors>
				</div>
			</c:if>
		</c:if>
		<c:if test="${collectionBatchFields.collectionCenter.hidden != true }">
			<div>
				<form:label path="collectionCenter">${collectionBatchFields.collectionCenter.displayName}</form:label>
				<form:select path="collectionCenter" id="${addCollectionBatchFormCentersId}" class="addCollectionBatchFormCenters">
					<form:option value="" selected="selected">&nbsp;</form:option>
					<c:forEach var="center" items="${centers}">
						<form:option value="${center.id}">${center.name}</form:option>
					</c:forEach>
				</form:select>
				<form:errors class="formError" path="collectionBatch.collectionCenter" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${collectionBatchFields.collectionSite.hidden != true }">
			<div>
				<form:label path="collectionSite">${collectionBatchFields.collectionSite.displayName}</form:label>
				<form:select path="collectionSite" id="${addCollectionBatchFormSitesId}"
					class="addCollectionBatchFormSites">
					<form:option value="" selected="selected">&nbsp;</form:option>
					<c:forEach var="site" items="${sites}">
						<form:option value="${site.id}">${site.name}</form:option>
					</c:forEach>
				</form:select>
				<form:errors class="formError" path="collectionBatch.collectionSite" delimiter=", "></form:errors>
			</div>
		</c:if>
		<c:if test="${collectionBatchFields.notes.hidden != true }">
			<div>
				<form:label path="notes" class="labelForTextArea">${collectionBatchFields.notes.displayName}</form:label>
				<form:textarea path="notes" />
				<form:errors class="formError" path="collectionBatch.notes"
					delimiter=", "></form:errors>
			</div>
		</c:if>
		</form:form>

		<div style="margin-left: 200px;">
			<label></label>
			<button type="button" class="addCollectionBatchButton autoWidthButton">
				Add Collection Batch
			</button>
			<button type="button" class="clearFormButton autoWidthButton">
				Clear form
			</button>				
		</div>

</div>
