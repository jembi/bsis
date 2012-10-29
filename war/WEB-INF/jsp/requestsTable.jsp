<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%!public long getCurrentTime() {
		return System.nanoTime();
	}%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tableContent-${unique_page_id}</c:set>
<c:set var="table_id">requestsTable-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  var fnRowSelected = function(node) {
    var elements = $(node).children();
    if (elements[0].getAttribute("class") === "dataTables_empty") {
      return;
    }
    replaceContent("${tabContentId}", "${model.requestUrl}", "editRequestFormGenerator.html", {requestNumber: elements[0].innerHTML});
  }

    var requestsTable = $("#${table_id}").dataTable({
      "bJQueryUI" : true,
      "sDom" : '<"H"lfrT>t<"F"ip>T',
      "oTableTools" : {
        "sRowSelect" : "single",
        "aButtons" : [ "print" ],
        "fnRowSelected" : fnRowSelected
      }
    });

    $("#${table_id}_filter").find("label").find("input").keyup(function() {
      var searchBox = $("#${table_id}_filter").find("label").find("input");
      $("#${table_id}").removeHighlight();
      if (searchBox.val() != "")
        $("#${table_id}").find("td").highlight(searchBox.val());
    });


    function showIssueRequestDialog(requestNumber) {

      var issueDialogId = 'issueRequest' + requestNumber;
      $.ajax({
            url : "findAvailableProducts.html",
            contentType : "application/json",
            type : "GET",
            data : {
              requestNumber : requestNumber
            },
            success : function(responseData) {
              var message = "<h3>"
                  + "Select a Product to Issue for Request Number: "
                  + requestNumber + "</h3>";
              $(
                  "<div id='" + issueDialogId + "'>" + message + responseData
                      + "</div>")
                  .dialog(
                      {
                        autoOpen : false,
                        height : 600,
                        width : 718,
                        modal : true,
                        title : "Select a Product to issue for Request Number: "
                            + requestNumber,
                        buttons : {
                          "Issue selected products" : function() {
                            var selectedRows = $("#" + issueDialogId).find(
                                "table").find(".DTTT_selected");
                            console.log(selectedRows);
                            if (selectedRows == undefined
                                || selectedRows.length == 0)
                              return false;
                            var selectedProducts = {};
                            for ( var index = 0; index < selectedRows.length; ++index) {
                              var row = selectedRows[index];
                              var elements = $(row).children();
                              if (elements === undefined
                                  || elements[0] === undefined)
                                continue;
                              if (elements[0].getAttribute("class") === "dataTables_empty")
                                continue;
                              selectedProducts[index] = elements[0].innerHTML;
                            }
                            console.log(selectedProducts);
                            issueProducts(requestNumber, selectedProducts);
                            $(this).dialog("close");
                          },
                          "Cancel" : function() {
                            $(this).dialog("close");
                          }
                        },
                        close : function() {
                          $("#" + issueDialogId).remove();
                        }
                      });
              $('#' + issueDialogId).dialog("open");
            }
          });
    }

    function issueProducts(requestNumber, products) {
      $.ajax({
        type : "POST",
        url : "issueProducts.html",
        data : {products: JSON.stringify(products),
          			requestNumber: requestNumber
        			 },
        success : function(jsonResponse) {
						          if (jsonResponse["success"] === true) {
						            $.showMessage("Products Issued Successfully!");
						          } else {
						            $.showMessage("Something went wrong." + jsonResponse["errMsg"], {
						              backgroundColor : 'red'
						            });
						          }
       	 					},
    		error: function() {
    		  				$.showMessage("Something went wrong", {backgroundColor : 'red'});
    					 }
    		});
    }
  });
</script>

<div id="${tabContentId}">
<table id="${table_id}" class="dataTable collectionsTable">
	<thead>
		<tr>
			<th>${model.requestNoDisplayName}</th>
			<c:if test="${model.showdateRequested==true}">
				<th>${model.requestDateDisplayName}</th>
			</c:if>
			<c:if test="${model.showdateRequired==true}">
				<th>${model.requiredDateDisplayName}</th>
			</c:if>
			<c:if test="${model.showsite==true}">
				<th>${model.siteDisplayName}</th>
			</c:if>
			<c:if test="${model.showabo==true}">
				<th>${model.aboDisplayName}</th>
			</c:if>
			<c:if test="${model.showrhd==true}">
				<th>${model.rhdDisplayName}</th>
			</c:if>
			<c:if test="${model.showproductType==true}">
				<th>${model.productTypeDisplayName}</th>
			</c:if>
			<c:if test="${model.showquantity==true}">
				<th>${model.quantityDisplayName}</th>
			</c:if>
			<c:if test="${model.showstatus==true}">
				<th>${model.statusDisplayName}</th>
			</c:if>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="request" items="${model.allRequests}">
			<tr>
				<td>${request.requestNumber}</td>
				<c:if test="${model.showdateRequested==true}">
					<td>${request.dateRequested}</td>
				</c:if>
				<c:if test="${model.showdateRequired==true}">
					<td>${request.dateRequired}</td>
				</c:if>
				<c:if test="${model.showsite == true}">
					<td>${request.site}</td>
				</c:if>
				<c:if test="${model.showabo == true}">
					<td>${request.abo}</td>
				</c:if>
				<c:if test="${model.showrhd == true}">
					<td>${request.rhd}</td>
				</c:if>
				<c:if test="${model.showproductType == true}">
					<td>${request.productType}</td>
				</c:if>
				<c:if test="${model.showquantity == true}">
					<td>${request.quantity}</td>
				</c:if>
				<c:if test="${model.showstatus == true}">
					<td>${request.status}</td>
				</c:if>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>