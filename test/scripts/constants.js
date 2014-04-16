LOGIN_URL = "http://localhost:8080/bsis/login.html";
WELCOME_URL = 'http://localhost:8080/bsis/welcomePage.html';
TIMEOUT = '7000';
LOGIN_USERNAME_ID = "j_username" ;
LOGIN_PASSWORD_ID =  "j_password";
LOGIN_FORM_ID = 'form#loginAction';
FORM_CLASS = 'form.formFormatClass';

//Page Selectors
HOME_PAGE_SELECTOR  = 'a[href="#homeLandingPageContent"]';
DONATION_PAGE_SELECTOR = 'a[href="#collectionsLandingPageContent"]';
DONOR_PAGE_SELECTOR = 'a[href="#donorsLandingPageContent"]';
COMPONENT_PAGE_SELECTOR = 'a[href="#productsLandingPageContent"]';
TESTING_PAGE_SELECTOR = 'a[href="#productsLandingPageContent"]';
REQUEST_PAGE_SELECTOR = 'a[href="#requestsLandingPageContent"]';
USAGE_PAGE_SELECTOR = 'a[href="#productsLandingPageContent"]';
ADMIN_PAGE_SELECTOR = 'a[href="#adminLandingPageContent"]';
REPORT_PAGE_SELECTOR = 'a[href="#reportsLandingPageContent"]';
LABELLING_PAGE_SELECTOR = 'a[href="#lotReleasePageContent"]';



//Tab COnstants

HOME_TAB   = 'Home';
DONATION_TAB = 'Donations';
DONOR_TAB = 'Donors';
TEST_RESULTS_TAB = 'Test Results'
REQUEST_TAB = 'Requests';
USAGE_TAB  = 'Usage';
ADMIN_TAB ='Admin';
PRODUCT_TAB = "Products";
LABELLING_TAB = "Lot Release";
REPORT_TAB = "Reports";


//Others
TABLE_CLASS = '.odd';
DONE_BUTTON = '.doneButton';
PRINT_TEXT = 'print';
PAGE_CONFIRM = 'page.confirm';
CLEAR_FORM_BUTTON = '.clearFormButton ';
CHECK_RESULTS_BUTTON_CLASS = '.findCheckResultButton';
PRINT_DICARD_BUTTON_CLASS = '.printDiscardButton';
LABELLING_DIN_ID = '#dinNumber';

//Tab Constant selectors
COMPONENT_RECORD_TAB_SELECTOR = '#recordProductsContent a';
COMPONENT_FIND_TAB_SELECTOR = '#findOrAddProductsContent a';
TEST_RESULTS_FIND_TAB_ID = '#findOrAddTestResultsContent a';
BLOOD_TYPING_TEST_RESULTS_TAB_ID= '#bloodTypingTestResults a';


//Donor Constants
DONOR_NUMBER = '000004';
DONOR_FIRST_NAME = 'malyala';
DONOR_LAST_NAME = 'rak';
DONOR_ANY_BLOOD_GROUP = true;
DONOR_DUE_TO_DONATE = false;
DONOR_DAY_OF_MONTH = '13';
DONOR_YEAR  = '1985';
DONOR_MONTH = '01';
DONOR_GENDER = 'male';
DONOR_FIND_TEXT ='Find Donors';




//Donor Selectors
DONOR_NUMBER_ID  = '#donorNumber';
DONOR_FIRST_NAME_ID = '#firstName';
DONOR_LAST_NAME_ID  = '#lastName';
DONOR_ANY_BLOOD_GROUP_ID = '#anyBloodGroup';
DONOR_DUE_TO_DONATE_ID= '#dueToDonate1';
DONOR_EDIT_TEXT = 'Edit donor';
DONOR_FIND_BUTTON_CLASS = 'button.findDonorButton';
DONOR_ADD_BUTTON_CLASS  = 'button.addDonorButton';
DONOR_EDIT_BUTTON_CLASS = '.summaryPageButtonSection .editButton' ;
DONOR_DELETE_BUTTON_CLASS = '.summaryPageButtonSection .deleteButton';
DONOR_DEFER_BUTTON_CLASS = '.deferDonorButton';
DONOR_HISTORY_BUTTON_CLASS = '.donorHistoryButton';
DONOR_SHOW_DEFERRALS_BUTTON_CLASS = '.donorDeferralsButton';
DONOR_PRINT_BARCODE_BUTTON_CLASS = '.printBarcode';
DONOR_DONE_BUTTON_CLASS = '.doneButton';
DONOR_CLEAR_BUTTON_CLASS = '.clearFormButton ';
DONATION_PRINT_BUTTON_CLASS = '.printButton';

DONOR_GENDER_NAME= 'select[name="gender"]';
DONOR_SAVE_CLASS = '.saveDonorButton';
DONOR_DONE_CLASS = '.doneButton';
DONOR_DAY_OF_MONTH_ID  = '#dayOfMonth';
DONOR_YEAR_ID  = '#year';
DONOR_MONTH_ID = '#month';
DONOR_ADD_ANOTHER_BUTTON_CLASS = '.addAnotherDonorButton';

//Form Constants
DONOR_ADD_FORM = 'form[name="addDonorForm"]'
COMPONENT_FIND_FORM = 'form[name="findComponentForm"]';
COMPONENT_RECORD_FORM = 'form[name="recordProductForm"]';
DONATION_EDIT_FORM= 'form[name="editCollectionForm"]';
DONATION_ADD_FORM = 'form[name="addDonationForm"]';
TEST_RESULTS_FIND_FORM = 'form[name="findTestResultsForm"]';
BLOOD_TYPING_RESULTS_ADD_FORM = 'form[name="bloodTypingResultsForm"]';

//Donation Constants
DONATION_CENTER = '2';
DONATION_SITE = '2';
COLLECTED_ON = '09/04/2014 12:00:00 AM';
DONATION_IDENTIFICATION_NUMBER = Math.round(+new Date()/1000);
DONATION_NUMBER = '1234';


//Donation Selectors 
DONATION_CENTER_CLASS= '.collectionCenterSelector';
DONATION_SITE_CLASS = '.collectionSiteSelector';
DONATION_FORM_CLEAR_CLASS = '.clearFormButton';
DONATION_TYPE='addCollectionFormDonationType'
DONOR_WEIGHT_ID = '#donorWeight';
DONOR_PULSE_ID = '#donorPulse';
BLOOD_BAG_TYPE = 'select[name="bloodBagType"]';
BLOOD_PRESSURE_SYSTOLIC_ID = '#bloodPressureSystolic';
BLOOD_PRESSURE_DIASTOLIC_ID = '#bloodPressureDiastolic';
DONATION_NUMBER_ID = '#collectionNumber';
DONATION_TYPE_CLASS = '.addCollectionFormDonationType';
DONATION_COLLECTED_FROM_ID = '.dateCollectedFrom';
DONATION_COLLECTED_TO_ID = '.dateCollectedTo';
HAEMOGLOBIN_COUNT_ID = '#haemoglobinCount';
NOTES_ID = '#notes';
DONATION_COLLECTED_ON_ID = '#collectedOn';
DONATION_ADD_CLASS= '.addCollectionButton';
BLOOD_BAG_CLASS = '.bloodBagTypeSelector';
DONATION_FIND_BUTTON_CLASS ='.findCollectionButton';
DONATION_CLEAR_FORM_CLASS = '.clearFormButton ';
DONATION_EDIT_BUTTON_CLASS  = '.editButton';
DONATION_DONE_BUTTON_CLASS  = '.cancelButton';
DONATION_DELETE_BUTTON_CLASS = '.deleteButton';
DONATION_SAVE_BUTTON_CLASS = '.saveCollectionButton';
DONATION_ADD_TAB =  '#addCollectionsContent a';
DONATION_FIND_TAB = '#findOrAddCollectionsContent a';
DONATION_ADD_ANOTHER_CLASS='.addAnotherCollectionButton';



//DOnation Texts
DONATION_FIND_TEXT ='Find Collections';

//Product Text Constants
COMPONENT_RECORD_TEXT = 'Record Product';
COMPONENT_FIND_TEXT = 'Find Products';
COMPONENT_DETAILS_TEXT = 'Product details';

//Product Selectors
COMPONENT_FIND_BUTTON_CLASS = '.findProductButton';
COMPONENT_CLEAR_BUTTON_CLASS = '.clearFindFormButton';
RECORD_NEW_COMPONENT_CLASS = '.recordNewProduct ';
COMPONENT_DONE_BUTTON_CLASS   = '.doneButton';
COMPONENT_EDIT_BUTTON_CLASS = '.editButton';
COMPONENT_DELETE_BUTTON_CLASS = '.deleteButton';
COMPONENT_PRINT_BUTTON_CLASS = '.printButton'; 
COMPONENT_DISCARD_BUTTON_CLASS = '.discardButton';
COMPONENT_RETURN_BUTTON_CLASS  = '.returnButton';
COMPONENT_MOVEMENT_BUTTON_CLASS =  '.productHistoryButton';
COMPONENT_SEARCH_BY_CLASS = '.searchBy';
COMPONENT_STATUS_CLASS = '.productStatusSelector';
COMPONENT_VIEW_BUTTON_CLASS = '.viewFormButton';


//Test Results Selectors
TEST_RESULTS_FIND_BUTTON_CLASS = '.findTestResultButton';
TEST_RESULTS_CLEAR_BUTTON_CLASS = '.clearFindFormButton';
COLLECTION_RESULTS_CLASS = '.collectionSummaryForTestingSection ';
BLOOD_TYPING_RESULTS_CLASS = '.bloodTypingForCollectionSection';
TTI_RESULTS_CLASS = '.ttiForCollectionSection'; 

//Blood typing Selectors

BLOOD_TYPING_COLLECTION_ADD_BUTTON_CLASS = '.addCollectionsToPlate';
BLOOD_TYPING_COLLECTION_CHANGE_BUTTON_CLASS = '.changeCollectionsButton';
BLOOD_TYPING_RESULTS_SaVE_BUTTON_CLASS = '.saveButton';
BLOOD_TYPING_RESULTS_FOR_MORE_COLLECTIONS_BUTTON_CLASS = '.enterTestResultsForCollectionsButton';


//Global Methods

function loginTest (USERNAME , PASSWORD){
casper.test.begin('Login Test to BSIS',1,function(test){
casper.start(LOGIN_URL).viewport(1600,1000);
casper.then( function() {
    this.fillSelectors('form#loginAction', {
        'input[name="j_username"]':    USERNAME,
        'input[name="j_password"]':    PASSWORD
       }, true);
    
    casper.waitForText(USERNAME, function() {
        test.pass('Login successful.')},function() {
        test.fail('Login failed'); } , 2000);
       
    });


casper.run(function() {
    this.echo('Test Successful - Login To BSIS', 'INFO');
       test.done();
   });

});

}

///Asserting Tabs
var tabSelector;
function assertTabExistsByName (TAB_NAME , PERMISSION, SELECTOR){
casper.test.begin('View '+TAB_NAME+' Page',2,function(test){
casper.then(function(){
    
    tabSelector  =   this.evaluate(function(TAB){

       for(var i=1;i<10;i++){

         var  selector =  "#topPanelTabs li:nth-child("+i+") a";
          if(document.querySelector(selector).textContent == TAB){
           return selector;
       }
    }
    return null;
 },{  TAB : TAB_NAME
 });

  if(PERMISSION)
     test.assertExists(tabSelector);
   else
     test.assertDoesntExist(tabSelector);

  if(PERMISSION){
     casper.click(tabSelector);
     casper.waitForSelector(SELECTOR, function success(){
        test.pass( TAB_NAME +' Page  --  Page Loaded Successfully') },function timeout(){
          test.fail( TAB_NAME + '  Page  --  page Loading Timeout')},TIMEOUT);
      }else
        test.pass('No permission To View -->' +TAB_NAME);



   });

    casper.run(function() {
    this.echo('Test Successful - View '+TAB_NAME + ' Page', 'INFO');
       test.done();
   });

  });
}



