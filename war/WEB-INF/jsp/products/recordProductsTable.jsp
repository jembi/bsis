<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%!public long getCurrentTime() {
    return System.nanoTime();
  }%>

<c:set var="unique_page_id"><%=getCurrentTime()%></c:set>
<c:set var="tabContentId">tableContent-${unique_page_id}</c:set>
<c:set var="table_id">productsTable-${unique_page_id}</c:set>
<c:set var="noResultsFoundDivId">noResultsFoundDiv-${unique_page_id}</c:set>
<c:set var="addProductFormId">addProductForm-${unique_page_id}</c:set>
<c:set var="findProductFormId">addProductForm-${unique_page_id}</c:set>

<script>
$(document).ready(
    function() {

      var productsTable = $("#${table_id}").dataTable({
        "bJQueryUI" : true,
        "sDom" : '<"H"lrT>t<"F"ip>T',
        "bServerSide" : true,
        "sAjaxSource" : "${nextPageUrl}",
        "sPaginationType" : "full_numbers",
        "aoColumnDefs" : [{ "sClass" : "hide_class", "aTargets": [0]},{ "sClass" : "hide_class", "aTargets": [1]}
                         ],
        "fnServerData" : function (sSource, aoData, fnCallback, oSettings) {
                           oSettings.jqXHR = $.ajax({
                             "datatype": "json",
                             "type": "GET",
                             "url": sSource,
                             "data": aoData,
                             "success": function(jsonResponse) {
                                           if (jsonResponse.iTotalRecords == 0) {
                                             $("#${tabContentId}").html($("#${noResultsFoundDivId}").html());
                                           }
                                           fnCallback(jsonResponse);
                                         }
                             });
                           },
        "oTableTools" : {
          "sRowSelect" : "single",
          "aButtons" : [  ],
          "fnRowSelected" : function(node) {
        	  				  var elements = $(node).children();
                              if (elements[0].getAttribute("class") === "dataTables_empty") {
                                return;
                              }
                              $(".productID").val(elements[0].innerHTML);
                              $(".collectedSampleID").val(elements[1].innerHTML);
                              $(".hiddenPackNumber").val(elements[3].innerHTML);
                              $(".status").val(elements[6].innerHTML);
                              $(".productTypes1").empty();
                              $(".productTypes1").val(elements[2].innerHTML);
                              
                              var findProductFormData = $("#${findProductFormId}").serialize();
                              var donationIdentificationNumber = elements[3].innerHTML.split('-');
                              if(donationIdentificationNumber.length == 3){
                            	  showErrorMessage("This product cannot be processed further.");      
                            	  return;
                              }	
                              if(elements[6].innerHTML == 'PROCESSED'){
                            	  showErrorMessage("Product is already Processed.");      
                            	  return;
                              }
                             
                              $.ajax({
                        	      type : "GET",
                        	      url : "getRecordNewProductComponents.html",
                        	      data : findProductFormData,
                        	      success: function(data) {
                        	    	  	$("#${tabContentId}").replaceWith(data);
                        	    	  	if(elements[2].innerHTML == "Whole Blood"){
          								  $("#noOfUnits").attr('disabled', 'disabled');
          								  $(".firstSeperation").val("1");
          								}
                        	    	  	$("#newProductComponent").show();
                        	    	  	
                        	               },
                        	      error: function(data) {
                        	    	       showErrorMessage("Something went wrong. Please try again later.");        
                        	             }
                        	    });
                             
//                               $(".productID").val(elements[0].innerHTML);
//                               $(".collectedSampleID").val(elements[1].innerHTML);
//                               $(".hiddenPackNumber").val(elements[3].innerHTML);
//                               $(".status").val(elements[6].innerHTML);
                              
                             },
        "fnRowDeselected" : function(node) {
                            },
        },
        "oColVis" : {
           "aiExclude": [0,1],
        }
      });
      
      function refreshResults() {
        showLoadingImage($("#${tabContentId}"));
        $.ajax({url: "${refreshUrl}",
                data: {},
                type: "GET",
                success: function(response) {
                           $("#${tabContentId}").html(response);
                         }
        });
      }

      $("#${tabContentId}").find(".productsTable").bind("refreshResults", refreshResults);
	
      $("#${addProductFormId}").find(".productType").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });
      
      $("#${tabContentId}").find(".addNewRecord").button().click(function() {
    	    var findProductFormData = $("#${findProductFormId}").serialize();
    	    var resultsDiv = $("#${mainContentId}").find(".tabContentId");
    	    $.ajax({
    	      type : "GET",
    	      url : "findProductByPackNumber.html",
    	      data : findProductFormData,
    	      success: function(data) {
    	                 animatedScrollTo(resultsDiv);
    	                 resultsDiv.html(data);
    	               },
    	      error: function(data) {
    	               showErrorMessage("Something went wrong. Please try again later.");        
    	             }
    	    });
    	  });
      
      $("#${tabContentId}").find(".recordNewProduct").button().click(function() {
  	    var findProductFormData = $("#${findProductFormId}").serialize();
        var resultsDiv = $("#${mainContentId}").find(".tabContentId");
    	var selectedProductType = $(".selectedProductType").val();
    	var numberRegex = /^\d+$/;
    	var str = $('#noOfUnits').val();
   	 
    	if(selectedProductType != ''){
	    	if(numberRegex.test(str)) {
	    		if(selectedProductType == 'Platelets concentrate' && $("#noOfUnits").attr('disabled') !="disabled" && ($("#noOfUnits").val() > 6 || $("#noOfUnits").val() < 2)){
	 	   			  showErrorMessage("Num. Units should be between 2 and 6.");   
		   	   	      return;
	 	   		}
	 	   		if($(".productID").val() != 1 && $("#noOfUnits").attr('disabled') !="disabled" && ($("#noOfUnits").val() > 5 || $("#noOfUnits").val() < 2)){
		   	   	  showErrorMessage("Num. Units should be between 2 and 5.");   
		   	   	  return;
		   	   	}
		   	   	else{
		   	   		if(selectedProductType == 'Whole Blood Pedi' && ($("#noOfUnits").val() > 5 || $("#noOfUnits").val() < 2)){
				    	  showErrorMessage("Num. Units should be between 2 and 5.");   
				   	   	  return;
	      			}
		   	   		else{
				  	    $.ajax({
				  	      type : "GET",
				  	      url : "recordNewProductComponents.html",
				  	      data : findProductFormData,
				  	      success: function(data) {
				  	               $("#${tabContentId}").replaceWith(data);
				  	               },
				  	      error: function(data) {
				  	               showErrorMessage("Something went wrong. Please try again later.");        
				  	             }
				  	    });
		   	   		}
		      	}
	    	}
	    	else{
	    		showErrorMessage("Num. Units should be numeric.");
	    	}
       }
    	else{
    		showErrorMessage("Please select Product Type.");
    	}
  	  });
      
      $("#${tabContentId}").find(".productType").change(function() {
    	  var selectedValue = getProductTypeSelector().val();
    	  var selectedOption = $(this).find('option[value="' + selectedValue + '"]');
    	  $(".selectedProductType").val(selectedOption.attr( "title" ));
    	  
    	  if(selectedOption.attr( "title" ) === "Whole Blood Pedi"){
    		  $("#noOfUnits").removeAttr("disabled");
    	  }
    	  
    	  if($(".firstSeperation").val() == "1" && selectedOption.attr( "title" ) != "Whole Blood Pedi"){
    		  $("#noOfUnits").attr('disabled', 'disabled');
    		  $("#noOfUnits").val(0);
    	  }
    	  
          var dateExpiresFrom = $("#${addProductFormId}").find(".dateExpiresFrom");
          if (dateExpiresFrom.val() === undefined ||
        		  dateExpiresFrom.val().trim() === "") {
        	  dateExpiresFrom.datetimepicker("setDate", new Date());
          }
          var createdOnVal = dateExpiresFrom.datetimepicker("getDate");
          console.log(selectedOption.data("expiryintervalminutes")*60*1000);
          var expiryDate = new Date(createdOnVal.getTime() + selectedOption.data("expiryintervalminutes")*60*1000);
          console.log(expiryDate);
          $("#${addProductFormId}").find(".dateExpiresTo")
                                   .datetimepicker('setDate', expiryDate);
        });
      
      
      $("#${tabContentId}").find(".clearFindFormButton").button({
    	    icons : {
    	      
    	    }
    	  }).click(clearFindForm);
    	  
    	  function clearFindForm() {
    	    refetchContent("${refreshUrl}", $("#${tabContentId}"));
    	    $("#${childContentId}").html("");
    	    // show the appropriate input based on default search by
    	    $("#${findProductFormId}").find(".searchBy").trigger("change");
    	  }
      
      function getProductTypeSelector() {
    	  return $("#${addProductFormId}").find('select[name="productTypes"]').multiselect();
        }
      
      $("#${addProductFormId}").find(".dateExpiresFrom").datetimepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -365,
          maxDate : 1,
          dateFormat : "dd/mm/yy",
          timeFormat : "hh:mm:ss tt",
          yearRange : "c-100:c0",
        });

        $("#${addProductFormId}").find(".dateExpiresTo").datetimepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -1,
          maxDate : 365,
          dateFormat : "dd/mm/yy",
          timeFormat : "hh:mm:ss tt",
          yearRange : "c-100:c+1",
        });
      
    });
</script>

<sec:authorize access="hasRole(T(utils.PermissionConstants).ADD_COMPONENT)">
<div id="${tabContentId}">
	<c:choose>

		<c:when test="${fn:length(allProducts) eq -1}">
			<span
				style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
				Sorry no results found matching your search request </span>
		</c:when>

		<c:otherwise>

			<table id="${table_id}" class="dataTable productsTable">
				<thead>
					<tr>
						<th style="display: none"></th>
						<th style="display: none"></th>
						<c:if test="${productFields.productType.hidden != true}">
							<th>${productFields.productType.displayName}</th>
						</c:if>
						<c:if test="${productFields.donationIdentificationNumber.hidden != true}">
							<th>${productFields.donationIdentificationNumber.displayName}</th>
						</c:if>
						<c:if test="${productFields.createdOn.hidden != true}">
							<th>${productFields.createdOn.displayName}</th>
						</c:if>
						<c:if test="${productFields.expiresOn.hidden != true}">
							<th>${productFields.expiresOn.displayName}</th>
						</c:if>
						<c:if test="${productFields.status.hidden != true}">
							<th>${productFields.status.displayName}</th>
						</c:if>
						<c:if test="${productFields.createdBy.hidden != true}">
							<th>${productFields.createdBy.displayName}</th>
						</c:if>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="product" items="${allProducts}">
						<tr>
							<td style="display: none">${product.id}</td>
							<td style="display: none">${product.collectedSample.id}</td>
							<c:if test="${productFields.productType.hidden != true}">
								<td>${product.productType.productTypeNameShort}</td>
							</c:if>
							<c:if test="${productFields.donationIdentificationNumber.hidden != true}">
								<th>${product.donationIdentificationNumber}</th>
							</c:if>
							<c:if test="${productFields.createdOn.hidden != true}">
								<td>${product.createdOn}</td>
							</c:if>
							<c:if test="${productFields.expiresOn.hidden != true}">
								<td>${product.expiresOn}</td>
							</c:if>
							<c:if test="${productFields.status.hidden != true}">
								<td>${product.status}</td>
							</c:if>
							<c:if test="${productFields.createdBy.hidden != true}">
								<td>${product.createdBy}</td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>

		</c:otherwise>
	</c:choose>

	<br>
	<div id="newProductComponent" style="display: none;">
		<form:form method="POST" commandName="addProductForm"
			class="formFormatClass" id="${addProductFormId}">
			<b>Record New Product Components</b>
			<c:if test="${productFields.productType.hidden != true }">
				<div>

					<form:label path="productTypes">${productFields.productType.displayName}</form:label>

					<form:select path="productTypes" class="productType"
						id="productType">
						<form:option value="">&nbsp;</form:option>
						<c:forEach var="productType" items="${productTypes}">
							<form:option value="${productType.id}"
								title="${productType.productType}"
								data-expiryintervalminutes="${productType.expiryIntervalMinutes}">
			              ${productType.productType}
			            </form:option>
						</c:forEach>

					</form:select>
				</div>
			</c:if>
			<form:hidden path="productTypes" class="productTypes1" />
			<form:hidden path="collectionNumber" class="hiddenPackNumber" />
			<form:hidden path="collectedSampleID" class="collectedSampleID" />
			<form:hidden path="dateExpiresFrom" class="dateExpiresFrom" />
			<form:hidden path="dateExpiresTo" class="dateExpiresTo" />
			<form:hidden path="status" class="status" />
			<form:hidden path="productID" class="productID" />
			<input type="hidden" id="firstSeperation" name="firstSeperation"
				class="firstSeperation" />
			<input type="hidden" id="selectedProductType" name="selectedProductType"
				class="selectedProductType" />	
			<div class="noOfUnits">
				<form:label path="noOfUnits">${productFields.numUnits.displayName}</form:label>
				<form:input path="noOfUnits" id="noOfUnits" size="1" />
			</div>

		</form:form>

		<div class="formFormatClass">
			<div>
				<label></label>
				<button type="button" class="recordNewProduct">Record New
					Product</button>
				<button type="button" class="clearFindFormButton">Done</button>

			</div>
		</div>

		<div class="findProductResults1"></div>

	</div>

	<div id="${noResultsFoundDivId}" style="display: none;">
		<span
			style="font-style: italic; font-size: 14pt; margin-top: 30px; display: block;">
			Sorry no results found matching your search request </span>
	</div>

</div>
</sec:authorize>