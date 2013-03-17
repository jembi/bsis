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

<c:set var="wellPrefixId">wellPrefixId-${unique_page_id}</c:set>

<div id="${tabContentId}">
	<div id="${mainContentId}" class="formInTabPane">
		<div class="bloodTypingPlate">
			<c:forEach var="rowNum" begin="1" end="${plate.numRows}">
				<label>${bloodTestsOnPlate[rowNum-1].testNameShort}</label>
				<c:forEach var="colNum" begin="1" end="${plate.numColumns}">
					<input
						style="width: ${bloodTypingConfig['titerWellRadius']}px; 
									 height: ${bloodTypingConfig['titerWellRadius']}px;
									 border-radius: ${bloodTypingConfig['titerWellRadius']}px;
									 text-align: center;
									 padding: 0;
									 "/>
				</c:forEach>
				<br />
			</c:forEach>
		</div>
	</div>
</div>