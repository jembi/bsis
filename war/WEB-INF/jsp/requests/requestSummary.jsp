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
<c:set var="deleteConfirmDialogId">deleteConfirmDialog-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        showBarcode($("#${tabContentId}").find(".requestBarcode"), "${request.requestNumber}");

        function notifyParentDone() {
          $("#${tabContentId}").parent().trigger("requestSummarySuccess");
        }

        $("#${tabContentId}").find(".editButton").button(
            {
              icons : {
          			primary : 'ui-icon-pencil'
        			}
            }).click(function() {

            $("#${tabContentId}").bind("editRequestSuccess", editRequestSuccess);
            $("#${tabContentId}").bind("editRequestCancel", emptyChildContent);

  	        fetchContent("editRequestFormGenerator.html",
              					 {requestId: "${request.id}"},
              					 $("#${childContentId}")
  	        						);
        });

        $("#${tabContentId}").find(".listIssuedProductsButton").button(
            {
              icons : {
          			primary : 'ui-icon-document'
        			}
            }).click(function() {

            $("#${tabContentId}").bind("listIssuedProductsDone", emptyChildContent);

            showLoadingImage($("#${childContentId}"));
  	        fetchContent("listIssuedProductsForRequest.html",
              					 {requestId: "${request.id}"},
              					 $("#${childContentId}")
  	        						);
        });

        $("#${tabContentId}").find(".findMatchingProductsButton").button(
            {
              icons : {
          			primary : 'ui-icon-search'
        			}
            }).click(function() {

            $("#${tabContentId}").bind("productIssueSuccess", productIssueSuccess);
            $("#${tabContentId}").bind("productIssueCancel", emptyChildContent);

            showLoadingImage($("#${childContentId}"));
  	        fetchContent("findMatchingProductsForRequest.html",
              					 {requestId: "${request.id}"},
              					 $("#${childContentId}")
  	        						);
        });

        $("#${tabContentId}").find(".addCompatibilityResultButton").button(
            {
              icons : {
          			primary : 'ui-icon-plus'
        			}
            }).click(function() {

            $("#${tabContentId}").bind("editCompatibilityTestSuccess", addCompatibilityResultSuccess);
            $("#${tabContentId}").bind("editCompatibilityTestCancel", emptyChildContent);

            showLoadingImage($("#${childContentId}"));
  	        fetchContent("editCompatibilityTestFormGenerator.html",
              					 {requestId: "${request.id}"},
              					 $("#${childContentId}")
  	        						);
        });

        $("#${tabContentId}").find(".printButton").button({
          icons : {
            primary : 'ui-icon-print'
          }
        }).click(function() {
          $("#${mainContentId}").find(".printableArea").printArea();
        });

        $("#${tabContentId}").find(".cancelButton").button({
          icons : {
            primary : 'ui-icon-check'
          }
        }).click(function() {
          notifyParentDone();
        });

        $("#${tabContentId}").find(".deleteButton").button({
          icons : {
            primary : 'ui-icon-trash'
          }
        }).click(function() {
          $("#${deleteConfirmDialogId}").dialog(
              {
                modal : true,
                title : "Confirm Delete",
                buttons : {
                  "Delete" : function() {
                    deleteRequest("${request.id}", notifyParentDone);
                    $(this).dialog("close");
                  },
                  "Cancel" : function() {
                    $(this).dialog("close");
                  }
                }
              });
        });

        function editRequestSuccess() {
          emptyChildContent();
          refetchContent("${refreshUrl}", $("#${tabContentId}"));
        }

        function productIssueSuccess() {
          emptyChildContent();
          refetchContent("${refreshUrl}", $("#${tabContentId}"));
        }

        function addCompatibilityResultSuccess() {
          emptyChildContent();
          refetchContent("${refreshUrl}", $("#${tabContentId}"));
        }

				function emptyChildContent() {
				  $("#${childContentId}").html("");
				}

				function hideExtraFields() {
				  $("#${mainContentId}").find(".showMoreSection").hide();
				  $("#${mainContentId}").find(".showMoreButton").show();
				  $("#${mainContentId}").find(".showLessButton").hide();
				}

				function showExtraFields() {
				  $("#${mainContentId}").find(".showMoreSection").show();
				  $("#${mainContentId}").find(".showLessButton").show();
				  $("#${mainContentId}").find(".showMoreButton").hide();
				}

				$("#${mainContentId}").find(".showMoreButton").button({icons: {primary: 'ui-icon-plus'}}).click(showExtraFields);
				$("#${mainContentId}").find(".showLessButton").button({icons: {primary: 'ui-icon-minus'}}).click(hideExtraFields);

				hideExtraFields();
      });
</script>

<div id="${tabContentId}">
	<div id="${mainContentId}">
		<div class="summaryPageButtonSection" style="text-align: right;">
			<button type="button" class="cancelButton">
				Done
			</button>
			<button type="button" class="listIssuedProductsButton">
				List issued products
			</button>
			<button type="button" class="findMatchingProductsButton">
				Find matching products
			</button>
			<button type="button" class="editButton">
				Edit
			</button>
			<button type="button" class="deleteButton">
				Delete
			</button>
			<button type="button" class="printButton">
				Print
			</button>
			<button type="button" class="addCompatibilityResultButton">
				Add compatibility result
			</button>
		</div>

		<br />
		<br />

		<div class="tipsBox ui-state-highlight">
			<p>
				${tips['requests.findpending.requestsummary']}
			</p>
		</div>

		<jsp:include page="requestDetails.jsp" />

	</div>

	<br />
	<br />

	<div id="${childContentId}"></div>
</div>

<div id="${deleteConfirmDialogId}" style="display: none;">Are
	you sure you want to delete this Product?</div>
