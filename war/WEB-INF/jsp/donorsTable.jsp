<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	pageContext.setAttribute("newLineChar", "\n");
%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="jquery-ui/js/jquery-ui-1.8.23.custom.min.js"></script>

<script src="js/donorTable.js" type="text/javascript"></script>
<script src="js/jquery-latest.js" type="text/javascript"></script>
<script src="plugins/DataTables/media/js/jquery.dataTables.js"></script>
					<table id="donorsTable" class="dataTable donorTableTest">
						<thead>
							<tr>
								<th>${model.donorIDDisplayName}</th>
								<c:if test="${model.showfirstName==true}">
									<th>${model.firstNameDisplayName}</th>
								</c:if>
								<c:if test="${model.showlastName==true}">
									<th>${model.lastNameDisplayName}</th>
								</c:if>
								<c:if test="${model.showgender==true}">
									<th>${model.genderDisplayName}</th>
								</c:if>
								<c:if test="${model.showbloodType==true}">
									<th>${model.bloodTypeDisplayName}</th>
								</c:if>
								<c:if test="${model.showdateOfBirth==true}">
									<th>${model.dobDisplayName}</th>
								</c:if>
								<c:if test="${model.showaddress==true}">
									<th>${model.addressDisplayName}</th>
								</c:if>
								<c:if test="${model.showcomments==true}">
									<th>${model.commentsDisplayName}</th>
								</c:if>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="donor" items="${model.allDonors}">
								<tr>
									<td>${donor.donorNumber}</td>
									<c:if test="${model.showfirstName==true}">
										<td>${donor.firstName}</td>
									</c:if>
									<c:if test="${model.showlastName==true}">
										<td>${donor.lastName}</td>
									</c:if>
									<c:if test="${model.showgender==true}">
										<td>${donor.gender}</td>
									</c:if>
									<c:if test="${model.showbloodType==true}">
										<td>${donor.bloodType}</td>
									</c:if>
									<c:if test="${model.showdateOfBirth==true}">
										<td>${donor.birthDate}</td>
									</c:if>
									<c:if test="${model.showaddress==true}">
										<td>${donor.address}</td>
									</c:if>
									<c:if test="${model.showcomments==true}">
										<td>${donor.comments}</td>
									</c:if>
									<td><a
										href="selectDonor.html?selectedDonorId=${donor.donorId}">edit/delete</a>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
			<c:if test="${fn:length(model.tipsDisplayName)>0}">
				<div id="showTips" class="link showTips">show tips</div>
				<div class="rightPanel">
					<div id="hideTips" class="link hideTips">hide tips</div>
					<p class="tipsTitle">Tips</p>

					<p>${fn:replace(model.tipsDisplayName,newLineChar,"<br/>")}</p>
				</div>
			</c:if>
			<jsp:include page="bottomPanel.jsp" flush="true" />
