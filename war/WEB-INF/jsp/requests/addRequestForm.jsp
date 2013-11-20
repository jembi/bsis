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
<c:set var="addRequestFormId">addRequestForm-${unique_page_id}</c:set>

<c:set var="addRequestFormBloodAboSelectorId">addRequestFormBloodAboSelectorId-${unique_page_id}</c:set>
<c:set var="addRequestFormBloodRhSelectorId">addRequestFormBloodRhSelectorId-${unique_page_id}</c:set>
<c:set var="addRequestFormProductTypeSelectorId">addRequestFormProductTypeSelectorId-${unique_page_id}</c:set>
<c:set var="addRequestFormRequestTypeSelectorId">addRequestFormRequestTypeSelectorId-${unique_page_id}</c:set>
<c:set var="addRequestFormRequestSiteSelectorId">addRequestFormRequestSiteSelectorId-${unique_page_id}</c:set>
<c:set var="addRequestFormPatientGenderSelectorId">addRequestFormPatientGenderSelectorId-${unique_page_id}</c:set>
<c:set var="addRequestFormCrossmatchTypeSelectorId">addRequestFormCrossmatchTypeSelectorId-${unique_page_id}</c:set>
<c:set var="addRequestcompatbilityResultSelectorId">addRequestcompatbilityResultSelectorId-${unique_page_id}</c:set>
<c:set var="table_id">requestsTable-${unique_page_id}</c:set>

<script>
$(document).ready(
    function() {
		
      function notifyParentSuccess() {
        // let the parent know we are done
        $("#${tabContentId}").parent().trigger("editRequestSuccess");
      }

      function notifyParentCancel() {
      // let the parent know we are done
      $("#${tabContentId}").parent().trigger("editRequestCancel");
      }

      $("#${cancelButtonId}").button({
        icons : {
          primary : 'ui-icon-closethick'
        }
      }).click(
          function() {
             notifyParentCancel();
      });

      $("#${mainContentId}").find(".addRequestButton").button({
        icons : {
          primary : 'ui-icon-plusthick'
        }
      }).click(
          function() {
            addNewRequest($("#${addRequestFormId}")[0],
                             "${tabContentId}", notifyParentSuccess);
          });

      $("#${addRequestFormId}").find(".bloodAbo").multiselect({
        multiple : false,
        selectedList : 1,
        header : false
      });

      $("#${addRequestFormId}").find(".bloodRh").multiselect({
        multiple : false,
        selectedList : 1,
        header : false
      });

      $("#${addRequestFormId}").find(".productType").multiselect({
        multiple : false,
        selectedList : 1,
        header : false
      });

      $("#${addRequestFormId}").find(".requestType").multiselect({
        multiple : false,
        selectedList : 1,
        header : false
      });

      $("#${addRequestFormId}").find(".crossmatchType").multiselect({
        multiple : false,
        selectedList : 1,
        header : false
      });
      
      $("#${addRequestFormId}").find(".compatbilityResult").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });
      
      $("#${addRequestFormId}").find(".bloodAbo").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });
      
      $("#${addRequestFormId}").find(".requestSites").multiselect({
          multiple : false,
          selectedList : 1,
          header : false
        });

      $("#${addRequestFormId}").find(".requestDate").datetimepicker({
        changeMonth : true,
        changeYear : true,
        minDate : -36500,
        maxDate : 1,
        dateFormat : "dd/mm/yy",
        timeFormat : "hh:mm:ss tt",
        yearRange : "c-100:c0",
        onSelect : function(selectedDate) {
          //$("#${addRequestFormId}").find(".requiredDate").datetimepicker("option", "minDate", selectedDate);
        },
      });
      
      

      $("#${addRequestFormId}").find(".dispatchDate").datetimepicker({
        changeMonth : true,
        changeYear : true,
        minDate : -36500,
        maxDate : 60,
        dateFormat : "dd/mm/yy",
        timeFormat : "hh:mm:ss tt",
        yearRange : "c-100:c+1",
        onSelect : function(selectedDate) {
          //$("#${addRequestFormId}").find(".requestDate").datetimepicker("option", "maxDate", selectedDate);
        },
      });
      
      
      $("#${addRequestFormId}").find(".compatbilityTestDate").datepicker({
          changeMonth : true,
          changeYear : true,
          minDate : -36500,
          maxDate : 60,
          dateFormat : "dd/mm/yy",
          yearRange : "c-100:c+1",
          onSelect : function(selectedDate) {
            //$("#${addRequestFormId}").find(".requestDate").datetimepicker("option", "maxDate", selectedDate);
          },
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
      
      
      $("#${tabContentId}").find(".removedRequestedComponentsButton").button({
    	    icons : {
    	      primary : 'ui-icon-disk'
    	    }
    	  }).click(function() {
    	    var data = {};
    	    data.requestedComponent=this.id;

    	    console.log(JSON.stringify(data));
    	    $.ajax({
    	      url: "removeRequestedComponents.html",
    	      data: {params: JSON.stringify(data)},
    	      type: "GET",
    	      success: function(response) {
    	    	  refetchForm() ;
    	               },
    	      error:    function(response) {
    	                 showErrorMessage("Something went wrong. Please try again later");
    	                 console.log(response);
    	               },
    	    });
    	    return false;
    	  });
      
      
      $("#${tabContentId}").find(".addRequestedComponentsButton").button({
  	    icons : {
  	      primary : 'ui-icon-disk'
  	    }
  	  }).click(function() {
  	    var data = {};
  	    data.requestedComponent=$("#${tabContentId}").find(".productType").val();
		data.bloodABO = $("#${tabContentId}").find(".bloodAbo").val();
		data.bloodRh = $("#${tabContentId}").find(".bloodRh").val();
		data.numUnitsRequested = $("#${tabContentId}").find(".numUnitsRequested").val();
  	    
  	    console.log(JSON.stringify(data));
  	    $.ajax({
  	      url: "addRequestedComponents.html",
  	      data: {params: JSON.stringify(data)},
  	      type: "GET",
  	      success: function(response) {
  	    	refetchForm() ;
  	               },
  	      error:    function(response) {
  	                 showErrorMessage("Something went wrong. Please try again later");
  	                 console.log(response);
  	               },
  	    });
  	    return false;
  	  });
           
      $("#${tabContentId}").find(".addDINComponentsButton").button({
    	    icons : {
    	      primary : 'ui-icon-search'
    	    }
    	  }).click(function() {
    	    var data = {};
    	    
    	   data.din = $("#${mainContentId}").find('input[name="din"]').val();
    	   data.compatbilityTestDate = $("#${mainContentId}").find(".compatbilityTestDate").val(); 
    	   data.crossmatchType = $("#${mainContentId}").find(".crossmatchType").val(); 
    	   data.compatbilityResult = $("#${mainContentId}").find(".compatbilityResult").val();
    	   
    	    console.log(JSON.stringify(data));
    	    $.ajax({
    	      url: "findComponent.html",
    	      data: {params: JSON.stringify(data)},
    	      type: "GET",
    	      success: function(response) {
    	    	  var $response = $(response);
    	    	  var $page = $response.find('.dinComponents').html();
    	    	  	//$("#${mainContentId}").find(".dinComponents").html($page);
    	    	  	$("#${mainContentId}").find(".dinComponents").html(response);
    	    	  
    	               },
    	      error:    function(response) {
    	                 showErrorMessage("Something went wrong. Please try again later");
    	                 console.log(response);
    	               },
    	    });
    	    return false;
    	  }); 
      
      getGenderSelector().multiselect({
        multiple : false,
        selectedList : 1,
        header : false
      });

      if ("${firstTimeRender}" == "true") {
        $("#${tabContentId}").find('textarea[name="notes"]').html("${requestFields.notes.defaultValue}");
        setDefaultValueForSelector(getBloodAboSelector(), "${requestFields.patientBloodAbo.defaultValue}");
        setDefaultValueForSelector(getBloodRhSelector(), "${requestFields.patientBloodRh.defaultValue}");
        setDefaultValueForSelector(getProductTypeSelector(), "${requestFields.productType.defaultValue}");
        setDefaultValueForSelector(getRequestTypeSelector(), "${requestFields.requestType.defaultValue}");
        setDefaultValueForSelector(getRequestSiteSelector(), "${requestFields.requestSite.defaultValue}");
        setDefaultValueForSelector(getGenderSelector(), "${requestFields.patientGender.defaultValue}");
      }

      function getGenderSelector() {
        return $("#${mainContentId}").find('select[name="patientGender"]').multiselect();
      }

      function getBloodAboSelector() {
        return $("#${tabContentId}").find('select[name="bloodAbo"]').multiselect();
      }
      
      function getBloodRhSelector() {
        return $("#${tabContentId}").find('select[name="bloodRh"]').multiselect();
      }
      
      function getProductTypeSelector() {
        return $("#${tabContentId}").find('select[name="productType"]').multiselect();
      }

      function getRequestTypeSelector() {
        return $("#${tabContentId}").find('select[name="requestType"]').multiselect();
      }

      function getRequestSiteSelector() {
        return $("#${tabContentId}").find('select[name="requestSite"]').multiselect();
      }
      
      function updateBarcode(val) {
        if (val === null || val === undefined || val === "")
          val = "-";
       $("#${addRequestFormId}").find(".barcodeContainer").barcode(
          val,
          "code128",
          {barWidth: 2, barHeight: 50, fontSize: 15, output: "css"});
      }
      updateBarcode("${addRequestForm.request.requestNumber}");

      $("#${addRequestFormId}").find('input[name="requestNumber"]').keyup(function() {
        updateBarcode($(this).val());
      });
      

    });
</script>

<div id="${tabContentId}">

  <div id="${mainContentId}">
	<b>Add Requests</b>
    <c:if test="${!empty success && !success}">
      <jsp:include page="../common/errorBox.jsp">
        <jsp:param name="errorMessage" value="${errorMessage}" />
      </jsp:include>
    </c:if>

    <form:form method="POST" commandName="addRequestForm"
      class="formFormatClass" id="${addRequestFormId}">
      <%-- <c:if test="${!requestFields.requestNumber.autoGenerate}">--%>    
        <c:if test="${requestFields.requestNumber.hidden != true }">
          <div>
            <form:label path="requestNumber">${requestFields.requestNumber.displayName}</form:label>
            <form:input path="requestNumber" value="${firstTimeRender ?  requestFields.requestNumber.defaultValue : ''}" />
            <form:errors class="formError"
              path="request.requestNumber" delimiter=", "></form:errors>
          </div>
         </c:if>
      <%--</c:if>--%>
       <c:if test="${requestFields.requestSite.hidden != true }">
        <div>
          <form:label path="requestSite">${requestFields.requestSite.displayName}</form:label>
          <form:select path="requestSite"
            id="${addRequestFormRequestSiteSelectorId}"
            class="requestSites">
            
            <c:forEach var="site" items="${sites}">
              <form:option value="${site.id}">${site.name}</form:option>
            </c:forEach>
          </form:select>
          <form:errors class="formError" path="request.requestSite" delimiter=", "></form:errors>
        </div>
      </c:if>
      <c:if test="${requestFields.requestType.hidden != true }">
        <div>
          <form:label path="requestType">${requestFields.requestType.displayName}</form:label>
          <form:select path="requestType"
                       id="${addRequestFormRequestTypeSelectorId}"
                       class="requestType">
            
            <c:forEach var="requestType" items="${requestTypes}">
              <form:option value="${requestType.id}">${requestType.requestType}</form:option>
            </c:forEach>
          </form:select>
          <form:errors class="formError" path="requestType"
            delimiter=", "></form:errors>
          <form:errors class="formError" path="request.requestType"
            delimiter=", "></form:errors>
        </div>
      </c:if>
      
      <%-- BulkTranfer flag is false --%>
      <c:if test="${bulkTransferStatus != true }">
	      <c:if test="${requestFields.requestedBy.hidden != true }">
	        <div>
	          <form:label path="requestedBy">${requestFields.requestedBy.displayName}</form:label>
	          <form:input path="requestedBy" value="${firstTimeRender ?  requestFields.requestedBy.defaultValue : ''}" />
	          <form:errors class="formError" path="request.requestedBy" delimiter=", "></form:errors>
	        </div>
	      </c:if>
      </c:if>
      
      <%-- BulkTranfer flag is true --%>
      <c:if test="${bulkTransferStatus == true }">
	       <b>Patient Information</b>
	       <c:if test="${requestFields.patientNumber.hidden != true }">
	        <div>
	          <form:label path="patientNumber">${requestFields.patientNumber.displayName}</form:label>
	          <form:input path="patientNumber" value="${firstTimeRender ?  requestFields.patientNumber.defaultValue : ''}" />
	          <form:errors class="formError" path="request.patientNumber" delimiter=", "></form:errors>
	        </div>
	      </c:if>
	       <c:if test="${requestFields.patientFirstName.hidden != true }">
	        <div>
	          <form:label path="patientFirstName">${requestFields.patientFirstName.displayName}</form:label>
	          <form:input path="patientFirstName" value="${firstTimeRender ?  requestFields.patientFirstName.defaultValue : ''}" />
	          <form:errors class="formError" path="request.patientFirstName" delimiter=", "></form:errors>
	        </div>
	      </c:if>
	      <c:if test="${requestFields.patientLastName.hidden != true }">
	        <div>
	          <form:label path="patientLastName">${requestFields.patientLastName.displayName}</form:label>
	          <form:input path="patientLastName" value="${firstTimeRender ?  requestFields.patientFirstName.defaultValue : ''}" />
	          <form:errors class="formError" path="request.patientFirstName" delimiter=", "></form:errors>
	        </div>
	      </c:if>
	      <c:if test="${requestFields.patientGender.hidden != true}">
	        <div>
	          <form:label path="patientGender">${requestFields.patientGender.displayName}</form:label>
	          <form:select path="patientGender" id="${addRequestFormPatientGenderSelectorId}">            
	            <form:option value="male" label="Male" />
	            <form:option value="female" label="Female" />
	          </form:select>
	          <form:errors class="formError" path="request.patientGender" delimiter=", "></form:errors>
	        </div>
	      </c:if>
	       <c:if test="${requestFields.patientAge.hidden != true }">
	        <div>
	          <form:label path="patientAge">${requestFields.patientAge.displayName}</form:label>
	          <form:input path="patientAge" value="${firstTimeRender ?  requestFields.patientAge.defaultValue : ''}"
	                      type="number" min="0" max="120" />
	            years
	          <form:errors class="formError" path="request.patientAge" delimiter=", "></form:errors>
	        </div>
	      </c:if>
	      
	      <b>Facility Information</b>
	      
	      <c:if test="${requestFields.hospital.hidden != true }">
	        <div>
	          <form:label path="hospital">${requestFields.hospital.displayName}</form:label>
	          <form:input path="hospital" value="${firstTimeRender ?  requestFields.hospital.defaultValue : ''}" />
	          <form:errors class="formError" path="request.hospital" delimiter=", "></form:errors>
	        </div>
	      </c:if>
	       <c:if test="${requestFields.department.hidden != true }">
	        <div>
	          <form:label path="department">${requestFields.department.displayName}</form:label>
	          <form:input path="department" value="${firstTimeRender ?  requestFields.department.defaultValue : ''}" />
	          <form:errors class="formError" path="request.department" delimiter=", "></form:errors>
	        </div>
	      </c:if>
	      <c:if test="${requestFields.ward.hidden != true }">
	        <div>
	          <form:label path="ward">${requestFields.ward.displayName}</form:label>
	          <form:input path="ward" value="${firstTimeRender ?  requestFields.ward.defaultValue : ''}" />
	          <form:errors class="formError" path="request.ward" delimiter=", "></form:errors>
	        </div>
	      </c:if>
	      
	      <b>Request Information</b>
	      
	      <c:if test="${requestFields.requestedBy.hidden != true }">
	        <div>
	          <form:label path="requestedBy">${requestFields.requestedBy.displayName}</form:label>
	          <form:input path="requestedBy" value="${firstTimeRender ?  requestFields.requestedBy.defaultValue : ''}" />
	          <form:errors class="formError" path="request.requestedBy" delimiter=", "></form:errors>
	        </div>
	      </c:if>
	      <c:if test="${requestFields.requestDate.hidden != true }">
	          <div>
	            <form:label path="requestDate">${requestFields.requestDate.displayName}</form:label>
	            <form:input path="requestDate" class="requestDate" value="${firstTimeRender ?  requestFields.requestDate.defaultValue : ''}" />
	            <form:errors class="formError" path="request.requestDate"
	              delimiter=", "></form:errors>
	          </div>
	       </c:if>
	      <c:if test="${requestFields.patientDiagnosis.hidden != true }">
	        <div>
	          <form:label path="patientDiagnosis">${requestFields.patientDiagnosis.displayName}</form:label>
	          <form:input path="patientDiagnosis" value="${firstTimeRender ?  requestFields.patientDiagnosis.defaultValue : ''}" />
	          <form:errors class="formError" path="request.patientDiagnosis" delimiter=", "></form:errors>
	        </div>
      	</c:if>
      	<div>
	          <label>Patient Blood Group</label>
	          <form:select path="patientBloodAbo"
				                       id="${addRequestFormBloodAboSelectorId}"
				                       class="bloodAbo">
				            <form:option value="" label="" />
				            <form:option value="A" label="A" />
				            <form:option value="B" label="B" />
				            <form:option value="AB" label="AB" />
				            <form:option value="O" label="O" />
				          </form:select>
				          <form:errors class="formError" path="request.patientBloodAbo" delimiter=", "></form:errors>
				 <form:select path="patientBloodRh"
				                       id="${addRequestFormBloodRhSelectorId}"
				                       class="bloodRh">
				            <form:option value="" label="" />
				            <form:option value="+" label="POS" />
				            <form:option value="-" label="NEG" />
				          </form:select>
				          <form:errors class="formError" path="request.patientBloodRh" delimiter=", "></form:errors>
	        </div>	
      	
      </c:if>
      
      
      
      <b>Requested Components</b>
      <div class="requestedComponents">
      	<table border="1" class="requestedComponentsTable">
      		<tr>	
      			<th>Component Type</th>
      			<c:if test="${bulkTransferStatus != true }"><th>Blood ABO</th></c:if>
      			<c:if test="${bulkTransferStatus != true }"><th>Blood Rh</th></c:if>
      			<th>Num. Units</th>
      			<th></th>
      		</tr>
      		<c:forEach var="requestedComponents" items="${requestedComponents}">
      			<tr>
      				<td align="left">${requestedComponents.productType.productType}</td>
      				<c:if test="${bulkTransferStatus != true }"><td align="center">${requestedComponents.bloodABO}</td></c:if>
      				<c:if test="${bulkTransferStatus != true }"><td align="center">${requestedComponents.bloodRh}</td></c:if>
      				<td align="center">${requestedComponents.numUnits}</td>
      				<td><button type="button" id="${requestedComponents.id}" class="removedRequestedComponentsButton">Remove</button></td>
      			</tr>
      		</c:forEach>
      		<tr>
      			<td>
      				<c:if test="${requestFields.productType.hidden != true }">
        			<div>
          				<form:label path="productType">${requestFields.productType.displayName}</form:label>
          				<form:select path="productType" id="${addRequestFormProductTypeSelectorId}" class="productType">
            				<form:option value="">&nbsp;</form:option>
            				<c:forEach var="productType" items="${productTypes}">
              					<form:option value="${productType.id}">${productType.productType}</form:option>
            				</c:forEach>
          				</form:select>
			          <form:errors class="formError" path="request.productType"
			            delimiter=", "></form:errors>
			          <form:errors class="formError" path="productType"
			            delimiter=", "></form:errors>
        			</div>
      			</c:if>
      			</td>
      			<c:if test="${bulkTransferStatus != true }">
      			<td>
      				<c:if test="${requestFields.patientBloodAbo.hidden != true }">
				        <div>
				          <form:label path="patientBloodAbo">${requestFields.patientBloodAbo.displayName}</form:label>
				          <form:select path="patientBloodAbo"
				                       id="${addRequestFormBloodAboSelectorId}"
				                       class="bloodAbo">
				            <form:option value="" label="" />
				            <form:option value="A" label="A" />
				            <form:option value="B" label="B" />
				            <form:option value="AB" label="AB" />
				            <form:option value="O" label="O" />
				          </form:select>
				          <form:errors class="formError" path="request.patientBloodAbo" delimiter=", "></form:errors>
				        </div>
      				</c:if>
      			</td>
      			<td>
      				<c:if test="${requestFields.patientBloodRh.hidden != true }">
				        <div>
				          <form:label path="patientBloodRh">${requestFields.patientBloodRh.displayName}</form:label>
				          <form:select path="patientBloodRh"
				                       id="${addRequestFormBloodRhSelectorId}"
				                       class="bloodRh">
				            <form:option value="" label="" />
				            <form:option value="+" label="POS" />
				            <form:option value="-" label="NEG" />
				          </form:select>
				          <form:errors class="formError" path="request.patientBloodRh" delimiter=", "></form:errors>
				        </div>
      				</c:if>
      			</td>
      			</c:if>
      			<td>
      				 <c:if test="${requestFields.numUnitsRequested.hidden != true }">
				        <div>
				          <form:label path="numUnitsRequested">${requestFields.numUnitsRequested.displayName}</form:label>
				          <form:input path="numUnitsRequested" class="numUnitsRequested" value="${firstTimeRender ?  requestFields.numUnitsRequested.defaultValue : ''}" />
				          <form:errors class="formError" path="request.numUnitsRequested" delimiter=", "></form:errors>
				        </div>
      				</c:if>
      			</td>
      			<td>
      				<button type="button" class="addRequestedComponentsButton">Add</button>
      			</td>
      		</tr>
      	</table>
      </div>
      <br>
      <b>Issued Components</b>
      	<div>
      		<label style="width:10%">DIN</label>
      		<form:input id="din" name="din" path="din"></form:input>
      	</div>
      	
      	
      	<c:if test="${bulkTransferStatus == true }">
      		<div>
      			<label style="width:10%">Compatbility Test Date</label>
      			 <form:input path="compatbilityTestDate" id="compatbilityTestDate" class="compatbilityTestDate"/>
      		</div>
      		<div>
      			<label style="width:10%">Crossmatch Type</label>
      			<form:select path="crossmatchType" id="${addRequestFormCrossmatchTypeSelectorId}" class="crossmatchType">
            				<form:option value="">&nbsp;</form:option>
            				<c:forEach var="crossmatchType" items="${crossmatchType}">
              					<form:option value="${crossmatchType.id}">${crossmatchType.crossmatchType}</form:option>
            				</c:forEach>
          				</form:select>
      		</div>
      		<div>
      			<label style="width:10%">Compatbility Result</label>
      			 <form:select path="compatbilityResult"
				                       id="${addRequestcompatbilityResultSelectorId}"
				                       class="compatbilityResult">
				            <form:option value="" label="" />
				            <form:option value="COMPATIBLE" label="COMPATIBLE" />
				            <form:option value="NOT_COMPATIBLE" label="NOT_COMPATIBLE" />
				            <form:option value="NOT_KNOWN" label="NOT_KNOWN" />
				            <form:option value="OTHER" label="OTHER" />
				          </form:select>
      		</div>
      	</c:if>
      
      <div style="padding-left: 195px;"><button type="button" class="addDINComponentsButton">Add Component</button></div>
      
      <div id="dinComponents" class="dinComponents">
      	  
	      </div>
	     
	     <c:if test="${requestFields.dispatchDate.hidden != true }">
	        <div>
	          <form:label path="dispatchDate" style="width:10%">${requestFields.dispatchDate.displayName}</form:label>
	          <form:input path="dispatchDate" class="dispatchDate" value="${firstTimeRender ?  requestFields.dispatchDate.defaultValue : ''}" />
	          <form:errors class="formError" path="request.dispatchDate"
	            delimiter=", "></form:errors>
	        </div>
      	</c:if>
	     
	    <c:if test="${requestFields.notes.hidden != true }">
	        <div>
	          <form:label path="notes" class="labelForTextArea" style="width:10%">${requestFields.notes.displayName}</form:label>
	          <form:textarea path="notes" />
	          <form:errors class="formError" path="request.notes"
	            delimiter=", "></form:errors>
	        </div>
      </c:if>
    </form:form>

    <div style="margin-left: 215px;">
      <c:if test="${bulkTransferStatus != true }">	
	      <label></label>
	      <button type="button" class="addRequestButton autoWidthButton">
	        Add Request
	      </button>
	      <button type="button" class="clearFormButton autoWidthButton">
	        Cancel
	      </button>
	   </c:if>
	   <c:if test="${bulkTransferStatus == true }">
	   		<label></label>
	      <button type="button" class="addRequestButton autoWidthButton">
	        Submit Request
	      </button>
	   </c:if>        
    </div>
  </div>
</div>
