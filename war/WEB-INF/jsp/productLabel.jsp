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

<script>
$(document).ready(
  function() {

    function notifyParentSuccess() {
			// let the parent know we are done
			$("#${tabContentId}").parent().trigger("productLabelSuccess");
		}

    $("#${tabContentId}").find(".doneButton").button({
      icons : {
        primary : 'ui-icon-check'
      }
    }).click(function() {
      notifyParentSuccess();
    });

    
    $("#${tabContentId}").find(".printButton").button({
      icons : {
        primary : 'ui-icon-print'
      }
    }).click(function() {
      $("#${tabContentId}").find(".printable").printArea();
    });

    var data = {"bloodGroup" : "${model.bloodGroup}"};
    generateProductLabel("upperRight", $("#${tabContentId}").find(".upperRightQuadrant").find("div"), data);
    
	});
</script>

<div id="${tabContentId}">

		<div class="labelPageButtonSection" style="text-align: right;">
			<button type="button" class="doneButton">
				Done
			</button>
			<button type="button" class="printButton">
				Print label
			</button>
		</div>

		<div class="printable">
			<div class="productLabel">
				<div>
					<div class="productLabelQuadrant upperLeftQuadrant">
						upper left
					</div>
					<div class="productLabelQuadrant upperRightQuadrant">
						<div></div>
					</div>
				</div>
				<div>
					<div class="productLabelQuadrant lowerLeftQuadrant">
						lower left
					</div>
					<div class="productLabelQuadrant lowerRightQuadrant">
						lower right
					</div>
				</div>
			</div>
		</div>

</div>