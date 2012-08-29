<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>V2V</title>
    <jsp:include page="commonHeadIncludes.jsp" flush="true"/>
    <script src="js/collections.js" type="text/javascript"></script>

</head>
<body>
<div class="mainBody">
<div class="mainContent">
<jsp:include page="topPanel.jsp" flush="true"/>
<div class="leftPanel">
    <ul id="collectionsTabs" class="leftPanelTabs">
        <li id="addCollectionsTab">Add</li>
        <li id="updateCollectionsTab">Update</li>
    </ul>
</div>
<div class="centerPanel">
<div class="centralContent">
<div class="infoMessage">
    <c:if test="${model.addedCollection==true}">

        Collection added with Collection Number
        :${model.collection.collectionNumber}
    </c:if>
    <c:if test="${model.collectionNotFound==true}">
        Collection not found. Please create a new collection
    </c:if>
    <c:if test="${model.deletedCollection==true}">
        Collection No. ${model.collectionIdDeleted} was deleted
    </c:if>
</div>
<div id="collectionErrorMessagePanel" class="message">
</div>
<div id="addCollectionsPanel">
    <form:form action="addCollection.html" id="addCollectionAction">


        <div class="inputFieldRow"><label for="collectionNumber">${model.collectionNoDisplayName}: </label><input
                type="text"
                req="true"
                id="collectionNumber"
                maxlength="32"
                name="collectionNumber"
                <c:if test="${model.hasCollection==true}">value="${model.collection.collectionNumber}"</c:if>
                <c:if test="${model.collectionNotFound==true}">value="${model.collectionNumber}"</c:if>
                />
        </div>


        <c:if test="${model.showdonorNo==true}">

            <div class="inputFieldRow"><label for="collectionDonorNumber">${model.donorNoDisplayName}: </label><input
                    type="text"
                    id="collectionDonorNumber"
                    maxlength="32"
                    name="collectionDonorNumber"
                    <c:choose>
                        <c:when test="${model.hasCollection==true}">value="${model.collection.donorNumber}"</c:when>
                        <c:when test="${model.hasDonor==true}">value="${model.donor.donorNumber}"</c:when>
                    </c:choose>
                    />

            </div>
        </c:if>

        <c:if test="${model.showdonorType==true}">

            <div class="inputFieldRow"><label for="donorType">${model.donorTypeDisplayName}: </label>

                <div id="donorType" class="radioButtonsList">
                    <input type="radio" id="voluntaryDonorType" name="donorType" value="voluntary"
                           <c:if test="${model.hasCollection==true and model.collection.donorType=='voluntary'}">checked="checked"</c:if>
                           <c:if test="${not model.hasCollection==true}">checked="checked"</c:if>
                            />
                    <label for="voluntaryDonorType" class="radioLabel">Voluntary</label>
                    <input type="radio" id="familyDonorType" name="donorType" value="family"
                           <c:if test="${model.hasCollection==true and model.collection.donorType=='family'}">checked="checked"</c:if>
                            />
                    <label for="familyDonorType" class="radioLabel">Family</label>


                    <input type="radio" id="autologousDonorType" name="donorType" value="autologous"
                           <c:if test="${model.hasCollection==true and model.collection.donorType=='autologous'}">checked="checked"</c:if>
                            />
                    <label for="autologousDonorType" class="radioLabel">Autologous</label>

                    <div>
                        <input type="radio" id="therapeuticDonorType" name="donorType" value="therapeutic"
                               <c:if test="${model.hasCollection==true and model.collection.donorType=='therapeutic'}">checked="checked"</c:if>
                                />
                        <label for="therapeuticDonorType" class="radioLabel">Therapeutic</label>

                        <input type="radio" id="otherDonorType" name="donorType" value="other"
                               <c:if test="${model.hasCollection==true and model.collection.donorType=='other'}">checked="checked"</c:if>
                                />
                        <label for="otherDonorType" class="radioLabel">Other</label>
                    </div>
                </div>
            </div>
        </c:if>
        <c:if test="${model.showcenter==true}">

            <div class="inputFieldRow"><label for="collectionCenter">${model.centerDisplayName}: </label><select
                    id="collectionCenter"
                    name="collectionCenter">
                <option value=""
                        <c:if test="${model.hasCollection==true and model.collection.centerId==''}">selected="selected"</c:if>></option>
                <c:forEach var="center" items="${model.centers}">
                    <option value="${center.locationId}"
                            <c:if test="${model.hasCollection==true and model.collection.centerId==center.locationId}">selected="selected"</c:if>>${center.name}</option>
                </c:forEach>

            </select>
            </div>
        </c:if>
        <c:if test="${model.showsite==true}">

            <div class="inputFieldRow"><label for="collectionSite">${model.siteDisplayName}: </label><select
                    id="collectionSite"
                    name="collectionSite">
                <option value=""
                        <c:if test="${model.hasCollection==true and model.collection.siteId==''}">selected="selected"</c:if>></option>

                <c:forEach var="site" items="${model.collectionSites}">
                    <option value="${site.locationId}"
                            <c:if test="${model.hasCollection==true and model.collection.siteId==site.locationId}">selected="selected"</c:if>>
                            ${site.name}</option>
                </c:forEach>

            </select>
            </div>
        </c:if>

        <div class="inputFieldRow"><label for="collectionDate">${model.dateCollectedDisplayName}: </label><input
                type="text"
                dateValid="true"
                req="true"
                id="collectionDate"
                name="collectionDate"
                <c:if test="${model.hasCollection==true}">value="${model.collection.dateCollected}"</c:if>
                />
        </div>
        <c:if test="${model.showsampleNo==true}">

            <div class="inputFieldRow"><label for="sampleNumber">${model.sampleNoDisplayName}: </label><input
                    type="text"
                    numOnly="true"
                    id="sampleNumber"
                    maxlength="20"
                    name="sampleNumber"
                    <c:if test="${model.hasCollection==true}">value="${model.collection.sampleNumber}"</c:if>

                    />
            </div>
        </c:if>
        <c:if test="${model.showshippingNo==true}">

            <div class="inputFieldRow"><label for="shippingNumber">${model.shippingNoDisplayName}: </label><input
                    type="text"
                    numOnly="true"
                    id="shippingNumber"
                    maxlength="20"
                    name="shippingNumber"
                    <c:if test="${model.hasCollection==true}">value="${model.collection.shippingNumber}"</c:if>

                    />
            </div>
        </c:if>
        <c:if test="${model.showcomment==true}">

            <div class="inputFieldRow"><label for="collectionComment">${model.commentDisplayName}: </label><input
                    type="text"
                    id="collectionComment"
                    maxlength="250"
                    name="collectionComment"
                    <c:if test="${model.hasCollection==true}">value="${model.collection.comment}"</c:if>

                    />
            </div>
        </c:if>
        <div class="actionButtonsPanel">
            <div><input id="createCollectionButton" type="button" value="Add"/>
                <input id="addCollectionResetButton" type="button" value="Clear"/>
            </div>
        </div>
    </form:form>
</div>
<div id="updateCollectionsPanel">
    <form:form action="updateCollection.html" id="updateCollectionsForm">
        <c:if test="${model.updatedCollection==true}">
            <div id="updateCollectionMessagePanel" class="infoMessage">Collection updated successfully</div>
        </c:if>
        <input
                type="hidden"
                id="updateCollectionId"
                name="updateCollectionId"
                <c:if test="${model.hasCollection==true}">value="${model.collection.collectionId}"</c:if>
                />

        <div class="inputFieldRow"><label for="updateCollectionNumber">${model.collectionNoDisplayName}: </label><input
                type="text"
                req="true"
                id="updateCollectionNumber"
                maxlength="32"
                name="updateCollectionNumber"
                <c:if test="${model.hasCollection==true}">value="${model.collection.collectionNumber}"</c:if>
                />
        </div>
        <c:if test="${model.hasCollection==true}"><input id="isUpdateCollection" type="hidden"
                                                         value="updateCollection"/>

            <c:if test="${model.showdonorNo==true}">

                <div class="collectionUpdateField inputFieldRow"><label
                        for="collectionDonorNumber">${model.donorNoDisplayName}: </label><input
                        type="text"
                        maxlength="32"
                        id="updateCollectionDonorNumber"
                        name="updateCollectionDonorNumber"
                        <c:choose>
                            <c:when test="${model.hasCollection==true}">value="${model.collection.donorNumber}"</c:when>
                            <c:when test="${model.hasDonor==true}">value="${model.donor.donorNumber}"</c:when>
                        </c:choose>
                        />

                </div>
            </c:if>
            <c:if test="${model.showdonorType==true}">

                <div class="collectionUpdateField inputFieldRow"><label
                        for="updateDonorType">${model.donorTypeDisplayName}: </label>

                    <div id="updateDonorType" class="radioButtonsList">
                        <input type="radio" id="updateVoluntaryDonorType" name="updateDonorType" value="voluntary"
                               <c:if test="${model.hasCollection==true and model.collection.donorType=='voluntary'}">checked="checked"</c:if>
                                />
                        <label for="updateVoluntaryDonorType" class="radioLabel">Voluntary</label>
                        <input type="radio" id="updateFamilyDonorType" name="updateDonorType" value="family"
                               <c:if test="${model.hasCollection==true and model.collection.donorType=='family'}">checked="checked"</c:if>
                                />
                        <label for="updateFamilyDonorType" class="radioLabel">Family</label>
                        <input type="radio" id="updateAutologousDonorType" name="updateDonorType" value="autologous"
                               <c:if test="${model.hasCollection==true and model.collection.donorType=='autologous'}">checked="checked"</c:if>
                                />
                        <label for="updateAutologousDonorType" class="radioLabel">Autologous</label>

                        <div>
                            <input type="radio" id="updateTherapeuticDonorType" name="updateDonorType"
                                   value="therapeutic"
                                   <c:if test="${model.hasCollection==true and model.collection.donorType=='therapeutic'}">checked="checked"</c:if>
                                    />
                            <label for="updateTherapeuticDonorType" class="radioLabel">Therapeutic</label>


                            <input type="radio" id="updateOtherDonorType" name="updateDonorType" value="other"
                                   <c:if test="${model.hasCollection==true and model.collection.donorType=='other'}">checked="checked"</c:if>
                                    />
                            <label for="updateOtherDonorType" class="radioLabel">Other</label>

                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${model.showcenter==true}">

                <div class="collectionUpdateField inputFieldRow"><label
                        for="updateCollectionCenter">${model.centerDisplayName}: </label><select
                        id="updateCollectionCenter"
                        name="updateCollectionCenter">
                    <option value=""
                            <c:if test="${model.hasCollection==true and model.collection.centerId==''}">selected="selected"</c:if>></option>

                    <c:forEach var="center" items="${model.centers}">
                        <option value="${center.locationId}"
                                <c:if test="${model.hasCollection==true and model.collection.centerId==center.locationId}">selected="selected"</c:if>>${center.name}</option>
                    </c:forEach>

                </select>
                </div>
            </c:if>
            <c:if test="${model.showsite==true}">

                <div class="collectionUpdateField inputFieldRow"><label
                        for="updateCollectionSite">${model.siteDisplayName}: </label><select
                        id="updateCollectionSite"
                        name="updateCollectionSite">
                    <option value=""
                            <c:if test="${model.hasCollection==true and model.collection.siteId==''}">selected="selected"</c:if>></option>

                    <c:forEach var="site" items="${model.collectionSites}">
                        <option value="${site.locationId}"
                                <c:if test="${model.hasCollection==true and model.collection.siteId==site.locationId}">selected="selected"</c:if>>
                                ${site.name}</option>
                    </c:forEach>

                </select>

                </div>
            </c:if>
            <div class="collectionUpdateField inputFieldRow"><label
                    for="updateCollectionDate">${model.dateCollectedDisplayName}: </label><input
                    type="text"
                    dateValid="true"
                    req="true"
                    id="updateCollectionDate"
                    name="updateCollectionDate"
                    <c:if test="${model.hasCollection==true}">value="${model.collection.dateCollected}"</c:if>

                    />
            </div>
            <c:if test="${model.showsampleNo==true}">

                <div class="collectionUpdateField inputFieldRow"><label
                        for="updateSampleNumber">${model.sampleNoDisplayName}: </label><input
                        type="text"
                        numOnly="true"
                        maxlength="10"
                        id="updateSampleNumber"
                        name="updateSampleNumber"
                        <c:if test="${model.hasCollection==true}">value="${model.collection.sampleNumber}"</c:if>

                        />
                </div>
            </c:if>
            <c:if test="${model.showshippingNo==true}">

                <div class="collectionUpdateField inputFieldRow"><label
                        for="updateShippingNumber">${model.shippingNoDisplayName}: </label><input
                        type="text" id="updateShippingNumber"
                        maxlength="10"
                        numOnly="true"
                        name="updateShippingNumber"
                        <c:if test="${model.hasCollection==true}">value="${model.collection.shippingNumber}"</c:if>

                        /></div>
            </c:if>
            <c:if test="${model.showcomment==true}">

                <div class="collectionUpdateField inputFieldRow"><label
                        for="updateCollectionComment">${model.commentDisplayName}: </label><input
                        type="text"
                        id="updateCollectionComment"
                        maxlength="250"
                        name="updateCollectionComment"
                        <c:if test="${model.hasCollection==true}">value="${model.collection.comment}"</c:if>

                        />
                </div>
            </c:if>
        </c:if>
        <div class="actionButtonsPanel">
            <input id="collectionUpdateButton" class="collectionUpdateField" type="button"
                   value="Update"/>

            <input id="collectionDeleteButton" class="collectionUpdateField" type="button"
                   value="Delete"/>

            <div id="collectionSearchButton"><input type="submit" value="Find"/></div>
        </div>
    </form:form>
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
<jsp:include page="bottomPanel.jsp" flush="true"/>
</div>
</div>
</body>
</html>
