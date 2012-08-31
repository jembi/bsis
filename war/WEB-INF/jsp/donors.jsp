<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	pageContext.setAttribute("newLineChar", "\n");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>V2V</title>
<jsp:include page="commonHeadIncludes.jsp" flush="true" />
<script src="js/donor.js" type="text/javascript"></script>
<link type="text/css" rel="stylesheet" href="css/donor.css" />

</head>
<body>
	<div class="mainBody">
		<div class="mainContent">
			<jsp:include page="topPanel.jsp" flush="true" />

			<div class="leftPanel">
				<ul id="donorsTabs" class="leftPanelTabs">
					<li id="addDonorTab" class="selectedTab"><a href="donors.html">Find/Add</a></li>
					<li id="viewDonorsTab"><a href="viewDonors.html">View</a></li>
				</ul>
			</div>
			<div class="centerPanel">
				<div class="centralContent">
					<div id="donor">

						<div id="donorPanel">
							<input id="donorScreenType" type="hidden"
								<c:choose>
            <c:when test="${model.donorNotFound==true}">
                value="createDonor"
            </c:when>
            <c:when test="${model.donorCreated==true or model.donorUpdated==true or model.singleDonorFound==true}">
                value="updateDonor"
            </c:when>
            <c:when test="${model.multipleDonorsFound==true}">
                value="multipleDonors"
            </c:when>
            <c:otherwise>
                value="init"
            </c:otherwise>
        </c:choose> />

							<c:if test="${model.donorNotFound==true}">
								<div id="donorErrorMessagePanel" class="message">This
									donor is not present in records. Please create a new entry.</div>
							</c:if>
							<div id="donorValidationErrorMessagePanel" class="message">
							</div>
							<div id="donorMessagePanel" class="infoMessage">
								<c:if test="${model.donorCreated==true}">
        Donor Created
    </c:if>
								<c:if test="${model.donorUpdated==true}">
        Donor Updated
    </c:if>
								<c:if test="${model.donorDeleted==true}">
        Donor No. ${model.donorNumberDeleted} deleted
    </c:if>
							</div>


							<form:form action="findDonor.html" id="donorAction">
								<div id="donorDetails">
									<c:if test="${model.hasDonor==true}">
										<input id="donorId" name="donorId" type="hidden"
											value="${model.donor.donorId}" />
									</c:if>

									<div class="inputFieldRow">
										<label for="donorNumber">${model.donorIDDisplayName}:
										</label><input req="true" type="text" maxlength="32" id="donorNumber"
											name="donorNumber"
											<c:if test="${model.displayDonorNumber!=null and model.displayDonorNumber!=''}">
                    value="${model.displayDonorNumber}"
                </c:if> />
									</div>
									<c:if test="${model.showfirstName==true}">

										<div class="inputFieldRow">
											<label for="firstName">${model.firstNameDisplayName}:
											</label><input req="true" type="text" maxlength="32" id="firstName"
												name="firstName"
												<c:if test="${model.displayFirstName!=null and model.displayFirstName!=''}">
                        value="${model.displayFirstName}"
                    </c:if> />
										</div>
									</c:if>
									<c:if test="${model.showlastName==true}">

										<div class="inputFieldRow">
											<label for="lastName">${model.lastNameDisplayName}: </label><input
												type="text" id="lastName" maxlength="32" name="lastName"
												<c:if test="${model.displayLastName!=null and model.displayLastName!=''}">
                        value="${model.displayLastName}"
                    </c:if> />
										</div>
									</c:if>
									<c:if test="${model.showgender==true}">

										<div class="createDonorField inputFieldRow">
											<label for="gender">${model.genderDisplayName}: </label>

											<div id="gender" class="radioButtonsList">
												<input type="radio" id="genderM" name="gender" value="male"
													<c:if test="${model.hasDonor==true and model.donor.gender=='male'}">checked="checked"</c:if>
													<c:if test="${not model.hasDonor==true}">checked="checked"</c:if> />
												<label for="genderM" class="radioLabel">Male</label> <input
													type="radio" id="genderF" name="gender" value="female"
													<c:if test="${model.hasDonor==true and model.donor.gender=='female'}">checked="checked"</c:if> />
												<label for="genderF" class="radioLabel">Female</label>

											</div>
										</div>
									</c:if>
									<c:if test="${model.showbloodType==true}">

										<div class="createDonorField inputFieldRow">
											<label for="bloodType">${model.bloodTypeDisplayName}:
											</label> <select id="bloodType" name="bloodType">
												<option value=""
													<c:if test="${model.hasDonor==true and model.donor.bloodType==''}">selected="selected"</c:if>>
												</option>
												<option value="A+"
													<c:if test="${model.hasDonor==true and model.donor.bloodType=='A+'}">selected="selected"</c:if>>
													A+</option>
												<option value="A-"
													<c:if test="${model.hasDonor==true and model.donor.bloodType=='A-'}">selected="selected"</c:if>>
													A-</option>
												<option value="B+"
													<c:if test="${model.hasDonor==true and model.donor.bloodType=='B+'}">selected="selected"</c:if>>
													B+</option>
												<option value="B-"
													<c:if test="${model.hasDonor==true and model.donor.bloodType=='B-'}">selected="selected"</c:if>>
													B-</option>
												<option value="AB+"
													<c:if test="${model.hasDonor==true and model.donor.bloodType=='AB+'}">selected="selected"</c:if>>
													AB+</option>
												<option value="AB-"
													<c:if test="${model.hasDonor==true and model.donor.bloodType=='AB-'}">selected="selected"</c:if>>
													AB-</option>
												<option value="O+"
													<c:if test="${model.hasDonor==true and model.donor.bloodType=='O+'}">selected="selected"</c:if>>
													O+</option>
												<option value="O-"
													<c:if test="${model.hasDonor==true and model.donor.bloodType=='O-'}">selected="selected"</c:if>>
													O-</option>

											</select>

										</div>
									</c:if>
									<c:if test="${model.showdateOfBirth==true}">

										<div class="createDonorField inputFieldRow">
											<label for="dobmonth">${model.dobDisplayName}: </label> <input
												type="text" maxlength="2" style="width: 30px;" id="dobmonth"
												name="dobmonth"
												<c:if test="${model.hasDonor==true}">value="${model.donor.birthDateMonth}"</c:if> />
											/ <input type="text" maxlength="2" style="width: 30px;"
												id="dobday" name="dobday"
												<c:if test="${model.hasDonor==true}">value="${model.donor.birthDateDay}"</c:if> />
											/ <input type="text" maxlength="4" style="width: 50px;"
												id="dobyear" name="dobyear"
												<c:if test="${model.hasDonor==true}">value="${model.donor.birthDateYear}"</c:if> />

											<div class="infoMessage" style="margin: 0px; padding: 0px;">
												<label style="width: 40px; margin: 2px 0 0 3px;"> mm
													/ </label> <label style="width: 30px; margin: 2px 0 0 17px;">
													dd / </label> <label style="width: 50px; margin: 2px 0 0 25px;">
													yyyy </label>
											</div>
										</div>
										<div class="createDonorField inputFieldRow"
											style="clear: both; padding-bottom: 20px;">
											<label>OR</label>
										</div>
									</c:if>
									<c:if test="${model.showage==true}">

										<div class="createDonorField inputFieldRow">
											<label for="age">${model.ageDisplayName}: </label><input
												type="text" numOnly="true" id="age" maxlength="2" name="age"
												<c:if test="${model.hasDonor==true}">value="${model.donor.age}"</c:if> />
										</div>
									</c:if>
									<c:if test="${model.showaddress==true}">

										<div class="createDonorField inputFieldRow">
											<label for="address">${model.addressDisplayName}: </label><input
												type="text" id="address" maxlength="64" name="address"
												<c:if test="${model.hasDonor==true}">value="${model.donor.address}"</c:if> />
										</div>
									</c:if>
								</div>

								<div id="findDonorButtonPanel" class="actionButtonsPanel">
									<input id="findDonorButton" type="submit" value="Find Donor" />
								</div>

								<div class="actionButtonsPanel" id="createDonorButtonPanel">
									<input id="createDonorButton" type="button"
										value="Create Donor" /> <a class="link backToFindDonor">Back</a>
								</div>


								<div class="actionButtonsPanel" id="updateDonorButtonPanel">
									<input id="updateDonorButton" type="button"
										value="Update Donor" /> <input id="deleteDonorButton"
										type="button" value="Delete Donor" /> <a
										class="link backToFindDonor">Back</a>
								</div>


							</form:form>
							<c:if test="${model.multipleDonorsFound==true}">
								<div id="donorChoices">
									<form:form action="selectDonor.html" id="donorTableAction">
										<div class="subHeading" style="margin-top: 30px;">
											<p>Possible Donors</p>
										</div>
										<table id="donorsTable">
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
												<th></th>
											</tr>
											<c:forEach var="donor" items="${model.donors}">
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
													<td><a
														href="selectDonor.html?selectedDonorId=${donor.donorId}">edit/delete</a>
													</td>
												</tr>
											</c:forEach>
										</table>

										<div class="actionButtonsPanel" style="margin-top: 20px;">
											<a class="link backToFindDonor">Back</a>
										</div>
									</form:form>

								</div>

							</c:if>

							<c:if test="${model.donorHistory!=null}">
								<div id="donorHistory">
									<div id="donorHistoryHeadingPanel"
										class="collapsibleHeadingPanel"
										title="Click to expand/collapse">
										<p id="expandButton" class="expandButton">
											<img id="donorHistoryPanelButton" src="images/plus.png" />
										</p>

										<p class="collapsibleHeadingPanelText">Donor History</p>
									</div>

									<table id="donorHistoryTable">
										<tr>
											<th>${model.collectionNoDisplayName}</th>
											<c:if test="${model.showcenter==true}">
												<th>${model.centerDisplayName}</th>
											</c:if>
											<c:if test="${model.showsite==true}">
												<th>${model.siteDisplayName}</th>
											</c:if>

											<th>${model.dateCollectedDisplayName}</th>
											<c:if test="${model.showsampleNo==true}">

												<th>${model.sampleNoDisplayName}</th>
											</c:if>
											<c:if test="${model.showshippingNo==true}">
												<th>${model.shippingNoDisplayName}</th>
											</c:if>
											<c:if test="${model.showdonorNo==true}">
												<th>${model.donorNoDisplayName}</th>
											</c:if>
											<c:if test="${model.showdonorType==true}">
												<th>${model.donorTypeDisplayName}</th>
											</c:if>
											<c:if test="${model.showcomment==true}">
												<th>${model.commentDisplayName}</th>
											</c:if>
											<c:if test="${model.showbloodGroup==true}">
												<th>${model.bloodGroupDisplayName}</th>
											</c:if>
											<c:if test="${model.showrhd==true}">
												<th>${model.rhdDisplayName}</th>
											</c:if>
										</tr>
										<c:forEach var="collection" items="${model.donorHistory}">
											<tr>
												<td>${collection.collectionNumber}</td>
												<c:if test="${model.showcenter==true}">
													<td>${collection.centerName}</td>
												</c:if>
												<c:if test="${model.showsite==true}">
													<td>${collection.siteName}</td>
												</c:if>
												<td>${collection.dateCollected}</td>
												<c:if test="${model.showsampleNo==true}">
													<td>${collection.sampleNumber}</td>
												</c:if>
												<c:if test="${model.showshippingNo==true}">
													<td>${collection.shippingNumber}</td>
												</c:if>
												<c:if test="${model.showdonorNo==true}">
													<td>${collection.donorNumber}</td>
												</c:if>
												<c:if test="${model.showdonorType==true}">
													<td>${collection.donorType}</td>
												</c:if>
												<c:if test="${model.showcomment==true}">
													<td>${collection.comment}</td>
												</c:if>
												<c:if test="${model.showbloodGroup==true}">
													<td>${collection.abo}</td>
												</c:if>
												<c:if test="${model.showrhd==true}">
													<td>${collection.rhd}</td>
												</c:if>
											</tr>
										</c:forEach>
									</table>
								</div>
							</c:if>
						</div>
					</div>
				</div>

			</div>
			<c:if test="${fn:length(model.tipsDisplayName)>0}">
				<div id="showTips" class="link showTips">show tips</div>
				<div class="rightPanel">
					<div id="hideTips" class="link hideTips">hide tips</div>
					<p class="tipsTitle">Tips</p>

					<p>${fn:replace(model.tipsDisplayName,newLineChar,"<br/>")}</p>
				</div>
			</c:if>
			<jsp:include page="bottomPanel.jsp" flush="true" />
		</div>
	</div>
</body>
</html>
