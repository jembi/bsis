<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>V2V</title>
    <jsp:include page="commonHeadIncludes.jsp" flush="true"/>
    <script src="js/addUser.js" type="text/javascript"></script>


</head>
<body>
<div class="mainBody">
    <div class="mainContent">
        <jsp:include page="adminTopPanel.jsp" flush="true"/>
        <div class="leftPanel">
            <ul id="userTabs" class="leftPanelTabs">
                <li id="addUserTab" class="selectedTab"><a href="admin-addUser.html">Add</a></li>
                <li id="viewUserTab"><a href="admin-userTable.html">View</a></li>
            </ul>
        </div>

        <div class="centerPanel">
            <div class="centralContent">
                <c:if test="${model.userExists==true}">
                    <div class="infoMessage">User ${model.username} already exists. Choose a different username</div>
                </c:if>
                <c:if test="${model.userNotFound==true}">
                    <div class="message">User not found. Please create user.</div>
                </c:if>
                <div id="userValidationErrorMessagePanel" class="message">
                </div>
                <form:form action="admin-createNewUser.html" id="userAction">

                    <div class="inputFieldRow"><label for="username">Username: </label><input type="text"
                                                                                              id="username"
                                                                                              maxlength="32"
                                                                                              req="true"
                                                                                              name="username"
                                                                                              <c:if test="${model.hasUserDetails==true}">value="${model.userDetails.username}"</c:if>
                            <c:if test="${model.userNotFound==true}">
                                value="${model.username}"
                            </c:if>
                            />
                    </div>
                    <div class="inputFieldRow"><label for="password">Password: </label><input type="password"
                                                                                              id="password"
                                                                                              req="true"
                                                                                              maxlength="32"
                                                                                              name="password"
                                                                                              <c:if test="${model.hasUserDetails==true}">value="${model.userDetails.password}"</c:if>
                            />
                        <input type="checkbox" id="showPassword"/> <label for="showPassword"
                                                                          style="float:none;display:inline;margin-left:0px;">Show
                            Password</label>

                    </div>

                    <div class="inputFieldRow"><label for="type">Type: </label>

                        <div id="type" class="radioButtonsList">
                            <input type="radio" id="technician" name="type" value="technician"
                                   <c:if test="${model.hasUserDetails==true and model.userDetails.type=='technician'}">checked="checked" </c:if>

                                   <c:if test="${not model.hasUserDetails==true}">checked="checked" </c:if>
                                    />
                            <label for="technician" class="radioLabel">Technician</label>
                            <input type="radio" id="admin" name="type" value="admin"
                                   <c:if test="${model.hasUserDetails==true and model.userDetails.type=='admin'}">checked="checked" </c:if>

                                    />
                            <label for="admin" class="radioLabel">Admin</label>
                        </div>
                    </div>

                    <div class="inputFieldRow"><label for="name">Name: </label><input type="text"
                                                                                      id="name"
                                                                                      maxlength="250"
                                                                                      name="name"
                                                                                      <c:if test="${model.hasUserDetails==true}">value="${model.userDetails.name}"</c:if>
                            />
                    </div>

                    <div class="inputFieldRow"><label for="contactNumber">Contact No.: </label><input
                            type="text"
                            id="contactNumber"
                            maxlength="50"
                            name="contactNumber"
                            <c:if test="${model.hasUserDetails==true}">value="${model.userDetails.contactNumber}"</c:if>
                            />
                    </div>
                    <div class="inputFieldRow"><label for="emailId">Email Id: </label><input
                            type="text"
                            id="emailId"
                            maxlength="50"
                            name="emailId"
                            <c:if test="${model.hasUserDetails==true}">value="${model.userDetails.emailId}"</c:if>
                            />
                    </div>

                    <div class="actionButtonsPanel"><input id="createUserButton" type="button"
                                                           value="Create"/>
                    </div>
                </form:form>

            </div>
        </div>
        <jsp:include page="bottomPanel.jsp" flush="true"/>

    </div>
</div>
</body>
</html>