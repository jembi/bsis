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
<c:set var="discardConfirmDialogId">discardConfirmDialog-${unique_page_id}</c:set>

<script>
  $(document).ready(
      function() {

        function notifyParentDone() {
          $("#${tabContentId}").parent().trigger("productSummarySuccess");
        }

        $("#${tabContentId}").find(".editButton").button(
            {
              icons : {
          			primary : 'ui-icon-pencil'
        			}
            }).click(function() {

            $("#${tabContentId}").bind("editProductSuccess", editProductSuccess);
            $("#${tabContentId}").bind("editProductCancel", emptyChildContent);

  	        fetchContent("editProductFormGenerator.html",
              					 {productId: "${product.id}"},
              					 $("#${childContentId}")
  	        						);
        });

        $("#${tabContentId}").find(".testResultsForProductButton").button(
            {
              icons : {
          			primary : 'ui-icon-bookmark'
        			}
            }).click(function() {

            $("#${tabContentId}").bind("testResultsHistorySuccess", emptyChildContent);

  	        fetchContent("testResultsForProduct.html",
              					 {productId: "${product.id}"},
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

        $("#${tabContentId}").find(".doneButton").button({
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
                    deleteProduct("${product.id}", notifyParentDone);
                    $(this).dialog("close");
                  },
                  "Cancel" : function() {
                    $(this).dialog("close");
                  }
                }
              });
        });

        $("#${mainContentId}").find(".productHistoryButton")
        										  .button({icons: {primary : 'ui-icon-document'}})
        											.click(showProductHistory);

       	function showProductHistory() {
          $("#${tabContentId}").bind("productHistoryDone", productHistoryDone);
	        fetchContent("viewProductHistory.html",
            					 {productId: "${product.id}"},
            					 $("#${childContentId}")
	        						);
       	}
        
        $("#${mainContentId}").find(".discardButton")
                              .button({icons: {primary : 'ui-icon-closethick'}})
									            .click(generateDiscardProductDialog);


  			function generateDiscardProductDialog() {
          $("<div>").dialog({
            modal : true,
            open : function() {
              // extra parameters passed as string otherwise POST request sent
              $(this).load("discardProductFormGenerator.html?" + $.param({
                productId : "${product.id}"
              }));
            },
            close : function(event, ui) {
              $(this).remove();
            },
            title : "Discard Product",
            height : 400,
            width : 600,
            buttons : {
              "Discard Product" : function() {
                $(this).dialog("close");
                discardProduct($(this).find(".discardProductForm"), discardProductDone);
              },
              "Cancel" : function() {
                $(this).dialog("close");
              }
            }
          });
        }

        $("#${mainContentId}").find(".returnButton")
											        .button({icons: {primary : 'ui-icon-arrowreturnthick-1-w'}})
        											.click(generateReturnProductDialog);

				function generateReturnProductDialog() {
					$("<div>").dialog({
                modal : true,
                open : function() {
                  // extra parameters passed as string otherwise POST request sent
                  $(this).load("returnProductFormGenerator.html?" + $.param({
                    productId : "${product.id}"
                  }));
                },
                close : function(event, ui) {
                  $(this).remove();
                },
                title : "Return Product",
                height : 400,
                width : 600,
                buttons : {
                  "Return Product" : function() {
                    $(this).dialog("close");
                    returnProduct($(this).find(".returnProductForm"),
                        returnProductDone);
                  },
                  "Cancel" : function() {
                    $(this).dialog("close");
                  }
                }
              });
        }

        function discardProductDone() {
          refetchContent("${refreshUrl}", $("#${tabContentId}"));
        }

        function returnProductDone() {
          refetchContent("${refreshUrl}", $("#${tabContentId}"));
        }

        function productHistoryDone() {
          emptyChildContent();
          refetchContent("${refreshUrl}", $("#${tabContentId}"));
        }

        function editProductSuccess() {
          emptyChildContent();
          refetchContent("${refreshUrl}", $("#${tabContentId}"));
        }

        function editCollectionSuccess() {
          emptyChildContent();
        }

        function emptyChildContent() {
          $("#${childContentId}").html("");
        }

      });
</script>

<div id="${tabContentId}">
	<div id="${mainContentId}">

		<div class="summaryPageButtonSection" style="text-align: right;">
			<button type="button" class="doneButton">
				Done
			</button>
			<c:if test="${permissions['editInformation'] eq 'allowed'}">
			<button type="button" class="editButton">
				Edit
			</button>
			</c:if>
			<!-- button type="button" class="testResultsForProductButton">
				Test results for product
			</button-->
			<!-- button type="button" class="productLabelButton">
				Product Label
			</button-->
			<button type="button" class="discardButton">
				Discard product
			</button>
			<button type="button" class="returnButton">
				Return product
			</button>
			<button type="button" class="productHistoryButton">
				Show product movement details
			</button>
			<c:if test="${permissions['editInformation'] eq 'allowed'}">
			<button type="button" class="deleteButton">
				Delete
			</button>
			</c:if>
			<button type="button" class="printButton">
				Print
			</button>
		</div>

		<div class="tipsBox ui-state-highlight">
			<p>
				${tips['products.findproduct.productsummary']}
			</p>
		</div>

		<jsp:include page="productDetail.jsp" />

	</div>

	<br />
	<br />

	<div id="${childContentId}"></div>
</div>

<div id="${deleteConfirmDialogId}" style="display: none;">Are
	you sure you want to delete this Product?</div>
