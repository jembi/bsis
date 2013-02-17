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

<c:set var="configureProductVolumesFormId">configureProductVolumes-${unique_page_id}</c:set>

<script>
$(document).ready(function() {

  $("#${tabContentId}").find(".addProductVolumeButton").button({
    icons : {
      primary : 'ui-icon-plusthick'
    }
  }).click(function() {
    var div = $("#${configureProductVolumesFormId}").find(".productVolumeDiv")[0];
    var newDiv = $($(div).clone());
    console.log(newDiv);
    newDiv.find('input[name="id"]').val("");
    newDiv.find('input[name="productVolume"]').val("");
    $("#${configureProductVolumesFormId}").append(newDiv);
  });

  $("#${tabContentId}").find(".saveProductVolumesButton").button({
    icons : {
      primary : 'ui-icon-disk'
    }
  }).click(function() {
    var data = {};
    var productVolumeDivs = $("#${configureProductVolumesFormId}").find(".productVolumeDiv");
    for (var index=0; index < productVolumeDivs.length; index++) {
      var div = $(productVolumeDivs[index]);
      var id = div.find('input[name="id"]').val();
      var productVolume = div.find('input[name="productVolume"]').val();
      console.log(productVolume);
      if (id == undefined || id == null || id === "")
        id = productVolume;
      data[id] = productVolume;
    }

    console.log(JSON.stringify(data));
    $.ajax({
      url: "configureProductVolumes.html",
      data: {params: JSON.stringify(data)},
      type: "POST",
      success: function(response) {
        				 $("#${tabContentId}").replaceWith(response);
        				 showMessage("Product Volumes Updated Successfully!");
      				 },
      error: 	 function(response) {
        				 showErrorMessage("Something went wrong. Please try again later");
        				 console.log(response);
      				 },
    });
    return false;
  });

  $("#${tabContentId}").find(".cancelButton").button({
    icons : {
      
    }
  }).click(refetchForm);
  
  function refetchForm() {
    refetchContent("${model.refreshUrl}", $("#${tabContentId}"));
    $("#${childContentId}").html("");
  }

});
</script>

<div id="${tabContentId}" class="formDiv">
	<div id="${mainContentId}">
		<b>Configure Product Volumes</b>
		<br />
		<br />
		<div class="tipsBox ui-state-highlight">
			<p>
				Modify the list of product volumes (in ml). 
			</p>
		</div>
		<form id="${configureProductVolumesFormId}">
				<c:forEach var="productVolume" items="${model.allProductVolumes}">
					<div class="productVolumeDiv">
						<div>
							<input type="hidden" name="id" value="${productVolume.id}" />
							<input type="text" name="productVolume" value="${productVolume.volume}" />
						</div>
					</div>
			</c:forEach>
		</form>
			<br />
			<div>
				<label>&nbsp;</label>
				<button class="addProductVolumeButton">Add new product volume</button>
				<button class="saveProductVolumesButton">Save</button>
				<button class="cancelButton">Cancel</button>
			</div>

	</div>

	<div id="${childContentId}"></div>

</div>
