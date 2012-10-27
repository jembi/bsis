<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<c:set var="table_id">${model.tableName}</c:set>

<script>
  $(function() {
    var requestsTable = $("#${table_id}").dataTable({
      "bJQueryUI" : true,
      "sDom": '<"H"lfrT>t<"F"ip>T',
      "oTableTools" : {"aButtons": ["print"]}
    });

    $("#${table_id}_filter").find("label").find("input").keyup(function() {
      var searchBox = $("#${table_id}_filter").find("label").find("input");
      $("#${table_id}").removeHighlight();
      if (searchBox.val() != "")
        $("#${table_id}").find("td").highlight(searchBox.val());
    });

    // we need to invoke the live function here in order for click event to be
    // registered across pages of table
    // http://stackoverflow.com/questions/5985884/jquery-datatables-row-click-not-registering-on-pages-other-than-first
    $(".${table_id}Edit").die("click");
    $(".${table_id}Edit").live(
        "click",
        function(event) {

          // remove row_selected class everywhere
          $(requestsTable.fnSettings().aoData).each(function() {
            $(this.nTr).removeClass('row_selected');
          });

          // add row_selected class to the current row
          $(event.target.parentNode.parentNode).addClass('row_selected');

          var elements = $(event.target.parentNode.parentNode).children();
          if (elements[0].getAttribute("class") === "dataTables_empty") {
            return;
          }

          var requestId = elements[0].innerHTML;

          generateEditForm("editRequestFormGenerator.html", {
            requestNumber : requestId,
            isDialog : "yes"
          }, updateExistingRequest, "Edit Request: " + elements[1].innerHTML
              + " " + elements[2].innerHTML, 'requestsTable',
              decorateEditRequestDialog, 550, 575);
        });

    // we need to invoke the live function here in order for click event to be
    // registered across pages of table
    // http://stackoverflow.com/questions/5985884/jquery-datatables-row-click-not-registering-on-pages-other-than-first
    $(".${table_id}Delete").die("click");
    $(".${table_id}Delete")
        .live(
            "click",
            function(event) {
              // remove row_selected class everywhere
              $(requestsTable.fnSettings().aoData).each(function() {
                $(this.nTr).removeClass('row_selected');
              });

              // add row_selected class to the current row
              $(event.target.parentNode.parentNode).addClass('row_selected');

              var elements = $(event.target.parentNode.parentNode).children();
              if (elements[0].getAttribute("class") === "dataTables_empty") {
                return;
              }

              var requestId = elements[0].innerHTML;
              $(
                  "<div id='deleteRequestDialog'> Are you sure you want to delete Request with Number: "
                      + requestId + "</div>").dialog({
                autoOpen : false,
                height : 150,
                width : 400,
                modal : true,
                title : "Confirm Delete",
                buttons : {
                  "Delete" : function() {
                    deleteRequest(requestId);
                    $(this).dialog("close");
                  },
                  "Cancel" : function() {
                    $(this).dialog("close");
                  }
                },
                close : function() {
                  $("#deleteRequestDialog").remove();
                }
              });
              $("#deleteRequestDialog").dialog("open");

            });

    $(".${table_id}Issue").die("click");
    $(".${table_id}Issue").live("click", function(event) {

      // remove row_selected class everywhere
      $(requestsTable.fnSettings().aoData).each(function() {
        $(this.nTr).removeClass('row_selected');
      });

      // add row_selected class to the current row
      $(event.target.parentNode.parentNode).addClass('row_selected');

      var elements = $(event.target.parentNode.parentNode).children();
      if (elements[0].getAttribute("class") === "dataTables_empty") {
        return;
      }

      var requestId = elements[0].innerHTML;
      showIssueRequestDialog(requestId);
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

<jsp:include page="addRequestButton.jsp" flush="true" />
<br />
<br />

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
			<th></th>
			<th>Actions</th>
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
				<td><c:if test="${request.status != 'fulfilled'}">
						<span class="${table_id}Issue"
							style="display: inline-block; text-color: blue; text-decoration: underline;"
							title="Issue">Issue</span>
					</c:if></td>
				<td><span class="ui-icon ui-icon-pencil ${table_id}Edit"
					style="display: inline-block;" title="Edit"></span> <span
					class="ui-icon ui-icon-trash ${table_id}Delete"
					style="display: inline-block; margin-left: 10px;" title="Delete"></span>
				</td>

			</tr>
		</c:forEach>
	</tbody>
</table>
