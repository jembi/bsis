<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>V2V</title>
    <jsp:include page="commonHeadIncludes.jsp" flush="true"/>
    <script src="js/updateUser.js" type="text/javascript"></script>


</head>
<body>
<div class="mainBody">
    <div class="mainContent">
        <c:if test="${model.selfUser==true}">
            <jsp:include page="topPanel.jsp" flush="true"/>
            <input type="hidden" value="true" id="selfUser"/>
        </c:if>
        <c:if test="${not model.selfUser==true}">
            <jsp:include page="adminTopPanel.jsp" flush="true"/>
            <div class="leftPanel">
                <ul id="userTabs" class="leftPanelTabs">
                    <li id="addUserTab"><a href="admin-addUser.html">Add</a></li>
                    <li id="viewUserTab" class="selectedTab"><a href="admin-userTable.html">View</a></li>

                </ul>
            </div>
        </c:if>
        <div class="
             <c:choose>
                 <c:when test="${not model.selfUser==true}">
                         centerPanel
                </c:when>
                <c:when test="${model.selfUser==true}">
                    leftAlignedPanel
                </c:when>
            </c:choose>
        ">
            <div class="centralContent">
                <c:if test="${model.userUpdated==true}">
                    <div class="infoMessage">User updated successfully</div>
                </c:if>
                <c:if test="${model.userAdded==true}">
                    <div class="infoMessage">User created successfully</div>
                </c:if>
                <c:if test="${model.userDeleted==true}">
                    <div class="infoMessage">User ${model.usernameDeleted} deleted</div>
                </c:if>
                 <div id="userValidationErrorMessagePanel" class="message">
                </div>
                <form:form action="admin-findUser.html" id="userAction">

                    <div class="inputFieldRow"><label for="username">Username: </label>
                        <div><label style="margin-left:3px;"><c:if
                                test="${model.hasUserDetails==true}">${model.userDetails.username}</c:if></label></div>
                        <input type="hidden" id="username" name="username"
                               <c:if test="${model.hasUserDetails==true}">value="${model.userDetails.username}"</c:if>
                                />
                    </div>
                    <div class="inputFieldRow" style="clear:both;padding-top:20px;"><label for="password">Password: </label><input type="password"
                                                                                              id="password"
                                                                                              req="true"
                                                                                              maxlength="32"
                                                                                              name="password"
                                                                                              <c:if test="${model.hasUserDetails==true}">value="${model.userDetails.password}"</c:if>
                            />
                        <input type="checkbox" id="showPassword"/> <label for="showPassword" style="float:none;display:inline;margin-left:0px;">Show Password</label>
                    </div>
                    <c:if test="${not model.selfUser==true}">

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
                    </c:if>
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

                    <div class="actionButtonsPanel"><input id="updateUserButton" type="button"
                                                           value="Update"/>
                        <c:if test="${not model.selfUser==true}">

                            <input id="deleteUserButton" type="button"
                                   value="Delete"/>
                        </c:if>
                    </div>
                </form:form>

            </div>
        </div>
        <jsp:include page="bottomPanel.jsp" flush="true"/>

    </div>
</div>
</body>
</html>