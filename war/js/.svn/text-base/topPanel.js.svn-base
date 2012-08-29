$(document).ready(function() {
    var removeAllSelectedTabs = function () {
        $('#topTabs li').removeClass('selectedTab');
    };
    var donorTabUrls = ["/v2v/donorsLandingPage.html","/v2v/viewDonors.html","/v2v/donors.html","/v2v/findDonor.html","/v2v/selectDonor.html","/v2v/createDonor.html","/v2v/updateDonor.html","/v2v/deleteDonor.html"];
    var collectionTabUrls = ["/v2v/collections.html","/v2v/addCollection.html","/v2v/deleteCollection.html","/v2v/updateCollection.html","/v2v/findCollection.html","/v2v/collectionsLandingPage.html"];
    var testResultsTabUrls = ["/v2v/testResultsLandingPage.html","/v2v/testResultsAdd.html","/v2v/addNewTestResults.html","/v2v/updateExistingTestResults.html","/v2v/deleteExistingTestResult.html","/v2v/testResultsView.html","/v2v/findAllTestResultsByCollection.html","/v2v/selectTestResult.html"];
    var productsTabUrls = ["/v2v/productsLandingPage.html","/v2v/products.html","/v2v/products.html","/v2v/addProduct.html","/v2v/deleteProduct.html","/v2v/findProduct.html","/v2v/updateProduct.html"];
    var requestsTabUrls = ["/v2v/requestsLandingPage.html","/v2v/requestsAdd.html","/v2v/requestsUpdate.html","/v2v/addNewRequest.html","/v2v/updateExistingRequest.html","/v2v/deleteExistingRequest.html","/v2v/viewAllRequests.html"];
    var issueTabUrls = ["/v2v/issueLandingPage.html","/v2v/issueViewRequests.html","/v2v/issueProduct.html","/v2v/issueAnyProduct.html","/v2v/issueSelectedProducts.html"];
    var usageTabUrls = ["/v2v/usageLandingPage.html","/v2v/usageAdd.html","/v2v/updateUsage.html","/v2v/addNewUsage.html","/v2v/updateSelectedUsage.html","/v2v/findUsage.html","/v2v/deleteExistingUsage.html"];
    var reportsTabUrls = ["/v2v/reports.html","/v2v/collectionReport.html","/v2v/testResultReport.html","/v2v/productReport.html","/v2v/getCollectionReport.html","/v2v/getTestResultReport.html","/v2v/getProductReport.html","/v2v/inventorySummary.html","/v2v/inventoryDetails.html"];
    var locationsTabUrls = ["/v2v/admin-locationsLandingPage.html","/v2v/admin-locations.html","/v2v/admin-addLocation.html","/v2v/admin-selectLocation.html","/v2v/admin-updateLocation.html","/v2v/admin-deleteLocation.html"];
    var locationTypesTabUrls = ["/v2v/admin-locationTypesLandingPage.html","/v2v/admin-locationTypes.html","/v2v/admin-addLocationType.html","/v2v/admin-selectLocationType.html","/v2v/admin-updateLocationType.html","/v2v/admin-deleteLocationType.html"];
    var reportConfigTabUrls = ["/v2v/admin-reportConfigLandingPage.html","/v2v/admin-collectionsReportFieldsConfig.html","/v2v/admin-saveCollectionsReportConfig.html","/v2v/admin-testResultsReportFieldsConfig.html","/v2v/admin-saveTestResultsReportConfig.html","/v2v/admin-productsReportFieldsConfig.html","/v2v/admin-saveProductsReportConfig.html"];
    var displayNamesConfigTabUrls = ["/v2v/admin-displayNamesConfigLandingPage.html","/v2v/admin-donorsDisplayNamesConfig.html","/v2v/admin-saveDonorsDisplayNamesConfig.html","/v2v/admin-collectionsDisplayNamesConfig.html","/v2v/admin-saveCollectionsDisplayNamesConfig.html","/v2v/admin-testResultsDisplayNamesConfig.html","/v2v/admin-saveTestResultsDisplayNamesConfig.html","/v2v/admin-productsDisplayNamesConfig.html","/v2v/admin-saveProductsDisplayNamesConfig.html","/v2v/admin-requestsDisplayNamesConfig.html","/v2v/admin-saveRequestsDisplayNamesConfig.html","/v2v/admin-issueDisplayNamesConfig.html","/v2v/admin-saveIssueDisplayNamesConfig.html","/v2v/admin-usageDisplayNamesConfig.html","/v2v/admin-saveUsageDisplayNamesConfig.html","/v2v/admin-reportsDisplayNamesConfig.html","/v2v/admin-saveReportsDisplayNamesConfig.html"];
    var displayFieldsConfigTabUrls = ["/v2v/admin-displayFieldsConfigLandingPage.html","/v2v/admin-collectionsDisplayFieldsConfig.html","/v2v/admin-saveCollectionsDisplayFieldsConfig.html","/v2v/admin-donorsDisplayFieldsConfig.html","/v2v/admin-saveDonorsDisplayFieldsConfig.html"];
    var usersTabUrls = ["/v2v/admin-userLandingPage.html","/v2v/admin-addUser.html","/v2v/admin-updateUser.html","/v2v/admin-createNewUser.html","/v2v/admin-updateExistingUser.html","/v2v/admin-deleteUser.html","/v2v/admin-userTable.html","/v2v/admin-findUser.html"];
    var createDateTabUrls = ["/v2v/admin-createData.html","/v2v/admin-createDummyData.html","/v2v/admin-deleteDummyData.html"];


    var url = $(location).attr('href');
    removeAllSelectedTabs();

    var startIndex = url.indexOf("/v2v");
    var endIndex = url.indexOf("?");
    if (endIndex != -1) {
        url = url.substr(startIndex, endIndex - startIndex);
    }
    else {
        url = url.substr(startIndex);
    }

    var tabUrlMapping = {
        "donorTabOption":donorTabUrls,
        "collectionTabOption":collectionTabUrls,
        "testResultsTabOption":testResultsTabUrls,
        "productsTabOption":productsTabUrls,
        "requestsTabOption":requestsTabUrls,
        "issueTabOption":issueTabUrls,
        "usageTabOption":usageTabUrls,
        "reportsTabOption":reportsTabUrls,
        "locationsTabOption":locationsTabUrls,
        "locationTypesTabOption":locationTypesTabUrls,
        "reportConfigTabOption":reportConfigTabUrls,
        "displayNamesConfigTabOption":displayNamesConfigTabUrls,
        "displayFieldsConfigTabOption":displayFieldsConfigTabUrls,
        "createDataTabOption":createDateTabUrls,
        "usersTabOption":usersTabUrls
    }

    $.each(tabUrlMapping, function(key, value) {
        if (value != null) {
            if ($.inArray(url, value) != -1) {
                $("#" + key).addClass('selectedTab');
            }
        }
    })

});