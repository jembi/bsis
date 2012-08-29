<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>V2V</title>
    <jsp:include page="commonHeadIncludes.jsp" flush="true"/>
    <script src="js/product.js" type="text/javascript"></script>


</head>
<body>
<div class="mainBody">
<div class="mainContent">
<jsp:include page="topPanel.jsp" flush="true"/>
<div class="leftPanel">
    <ul id="productsTabs" class="leftPanelTabs">
        <li id="addProductsTab">Add</li>
        <li id="updateProductsTab">Update</li>
    </ul>
</div>

<div class="centerPanel">
<div class="centralContent">
    <div id="products">
        <div id="productSearch">

            <div id="productErrorMessagePanel" class="message"></div>
            <div id="productMessagePanel" class="infoMessage">

                <c:if
                        test="${model.productFound==true}">Product found</c:if>
                <c:if
                        test="${model.productNotFound==true}">Product not found. Please create new product</c:if>
                <c:if
                        test="${model.productUpdated==true}">Product updated successfully</c:if>
                <c:if
                        test="${model.productNotUpdated==true}">Product was NOT updated</c:if>
                <c:if
                        test="${model.productCreated==true}">Product created successfully</c:if>
                <c:if
                        test="${model.productDeleted==true}">Product No. ${model.productNumberDeleted} deleted</c:if>
                <c:if test="${model.collectionNotFound==true}">
                    <p>Collection ${model.collectionNumber} not found. Click<a class="link"
                                                                               href="collections.html?collectionNumber=${model.collectionNumber}">
                        here</a> to create collection</p>
                </c:if>

            </div>
            <c:if
                    test="${model.collectionUnsafe==true}">
                <div class="message" id="quarantineMsg">Collection ${model.quarantineCollectionNo} is in Quarantine.
                    Product cannot be created/updated with this collection.
                </div>
            </c:if>
            <div id="addProductsPanel" class="tabbedPanel">
                <form:form action="addProduct.html" id="addProductAction">

                <div class="inputFieldRow"><label
                        for="collectionNumber">${model.collectionNoDisplayName}: </label><input
                        type="text"
                        req="true"
                        id="collectionNumber"
                        maxlength="32"
                        name="collectionNumber"
                        <c:if test="${model.hasProduct==true}">value="${model.product.collectionNumber}"</c:if>
                        />
                </div>
                <div class="inputFieldRow"><label
                        for="productNumber">${model.productNoDisplayName}: </label><input
                        type="text"
                        req="true"
                        id="productNumber"
                        maxlength="32"
                        name="productNumber"
                        <c:choose>
                            <c:when test="${model.hasProduct==true}">value="${model.product.productNumber}"</c:when>
                            <c:when test="${model.productNotFound==true}">value="${model.productNumber}"</c:when>
                        </c:choose>
                        />

                </div>

                <div class="inputFieldRow"><label
                        for="productType">${model.productTypeDisplayName}: </label>

                    <div id="productType" class="radioButtonsList">
                        <input type="radio" id="wholeBlood" name="productType" value="wholeBlood"
                               <c:if test="${model.hasProduct==true and model.product.type=='wholeBlood'}">checked="checked"</c:if>
                               <c:if test="${!model.hasProduct==true}">checked="checked"</c:if>

                                />
                        <label for="wholeBlood" class="radioLabel">Whole Blood</label>
                        <input type="radio" id="rcc" name="productType" value="rcc"
                               <c:if test="${model.hasProduct==true and model.product.type=='rcc'}">checked="checked"</c:if>
                                />
                        <label for="rcc" class="radioLabel">RCC</label>
                        <input type="radio" id="ffp" name="productType" value="ffp"
                               <c:if test="${model.hasProduct==true and model.product.type=='ffp'}">checked="checked"</c:if>
                                />
                        <label for="ffp" class="radioLabel">FFP</label>

                        <input type="radio" id="platelets" name="productType" value="platelets"
                               <c:if test="${model.hasProduct==true and model.product.type=='platelets'}">checked="checked"</c:if>
                                />
                        <label for="platelets" class="radioLabel">Platelets</label>
                                        <span style="display:block;">

                                        <input type="radio" id="partialPlatelets" name="productType"
                                               value="partialPlatelets"
                                               <c:if test="${model.hasProduct==true and model.product.type=='partialPlatelets'}">checked="checked"</c:if>
                                                />
                                        <label for="partialPlatelets" class="radioLabel">Partial Platelets</label>
                                        </span>
                    </div>
                </div>
                <div class="actionButtonsPanel">
                    <div><input id="productAddButton" type="button" value="Create"/>
                        <input id="addProductResetButton" type="button" value="Clear"/>
                    </div>
                </div>

            </div>

            </form:form>
        </div>
        <div id="updateProductsPanel" class="tabbedPanel">
            <form:form action="findProduct.html" id="updateProductAction">
                <input
                        type="hidden"
                        id="updateProductId"
                        name="updateProductId"
                        <c:if test="${model.hasProduct==true}">value="${model.product.productId}"</c:if>
                        />

                <div class="inputFieldRow" id="findProductNumberInputRow"
                     <c:if test="${model.hasProduct==true}">style="display: none;" </c:if>


                        ><label
                        for="findProductNumber">${model.productNoDisplayName}: </label><input
                        type="text"
                        req="true"
                        id="findProductNumber"
                        maxlength="32"
                        name="findProductNumber"
                        />
                </div>

                <c:if test="${model.hasProduct==true}"><input id="isUpdateProduct" type="hidden"
                                                              value="updateProduct"/>


                    <div class="productUpdateField inputFieldRow"><label
                            for="updateCollectionNumber">${model.collectionNoDisplayName}: </label><input
                            type="text"
                            req="true"
                            id="updateCollectionNumber"
                            maxlength="32"
                            name="updateCollectionNumber"
                            <c:if test="${model.hasProduct==true}">value="${model.product.collectionNumber}"</c:if>
                            />

                    </div>

                    <div class="productUpdateField inputFieldRow"><label
                            for="updateProductNumber">${model.productNoDisplayName}: </label><input
                            type="text"
                            req="true"
                            id="updateProductNumber"
                            maxlength="32"
                            name="updateProductNumber"
                            <c:if test="${model.hasProduct==true}">value="${model.product.productNumber}"</c:if>
                            />

                    </div>


                    <div class="productUpdateField inputFieldRow"><label
                            for="productType">${model.productTypeDisplayName}: </label>

                        <div id="updateProductType" class="radioButtonsList">
                            <input type="radio" id="updateWholeBlood" name="updateProductType"
                                   value="wholeBlood"
                                   <c:if test="${model.hasProduct==true and model.product.type=='wholeBlood'}">checked="checked"</c:if>
                                    />
                            <label for="updateWholeBlood" class="radioLabel">Whole Blood</label>
                            <input type="radio" id="updateRcc" name="updateProductType" value="rcc"
                                   <c:if test="${model.hasProduct==true and model.product.type=='rcc'}">checked="checked"</c:if>
                                    />
                            <label for="updateRcc" class="radioLabel">RCC</label>
                            <input type="radio" id="updateFfp" name="updateProductType" value="ffp"
                                   <c:if test="${model.hasProduct==true and model.product.type=='ffp'}">checked="checked"</c:if>
                                    />
                            <label for="updateFfp" class="radioLabel">FFP</label>

                            <input type="radio" id="updatePlatelets" name="updateProductType"
                                   value="platelets"
                                   <c:if test="${model.hasProduct==true and model.product.type=='platelets'}">checked="checked"</c:if>
                                    />
                            <label for="updatePlatelets" class="radioLabel">Platelets</label>
                                        <span style="display:block;">
                                        <input type="radio" id="updatePartialPlatelets" name="updateProductType"
                                               value="partialPlatelets"
                                               <c:if test="${model.hasProduct==true and model.product.type=='partialPlatelets'}">checked="checked"</c:if>
                                                />
                                        <label for="updatePartialPlatelets" class="radioLabel">Partial Platelets</label>
                                        </span>
                        </div>
                    </div>
                </c:if>
                <div class="actionButtonsPanel">
                    <input id="productUpdateButton" class="productUpdateField" type="button"
                           value="Update"/>

                    <input id="productDeleteButton" class="productUpdateField" type="button"
                           value="Delete"/>

                    <div id="productSearchButton"><input type="submit" value="Find"/>
                    </div>
                </div>
            </form:form>
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
<jsp:include page="bottomPanel.jsp" flush="true"/>

</div>
</div>
</body>
</html>