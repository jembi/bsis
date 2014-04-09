LOGIN_URL = "http://localhost:8080/bsis/login.html";
WELCOME_URL = 'http://localhost:8080/bsis/welcomePage.html';
USERNAME= "superuser";
PASSWORD = "superuser";
TIMEOUT = '7000';
LOGIN_USERNAME_ID = "j_username" ;
LOGIN_PASSWORD_ID =  "j_password";
LOGIN_FORM_ID = 'form#loginAction';
FORM_CLASS = 'form.formFormatClass';


//Others
TABLE_CLASS = '.odd';
DONE_BUTTON = '.doneButton';
PRINT_TEXT = 'print';
PAGE_CONFIRM = 'page.confirm';

//Tab Constants

 DONOR_TAB = 'Donors';
DONATION_TAB = 'Donations'
COMPONENT_TAB = 'Products';
ADMIN_TAB= 'Admin';
REQUEST_TAB = 'Requests';
REPORT_TAB = 'Reports';
USAGE_TAB= 'Usage';
HOME_TAB = 'Home';
LOT_RELEASE_TAB = 'Lot Release';
TEST_RESULTS_TAB = 'Test Results';



//Donor Constants
DONOR_NUMBER = '000003';
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

DONOR_FIND_BUTTON_CLASS = 'button.findDonorButton';
DONOR_ADD_BUTTON_CLASS  = 'button.addDonorButton';
DONOR_EDIT_BUTTON_CLASS = '.editButton' ;
DONOR_DELETE_BUTTON_CLASS = '.deleteButton';
DONOR_DEFER_BUTTON_CLASS = '.deferDonorButton';
DONOR_HISTORY_BUTTON_CLASS = '.donorHistoryButton';
DONOR_SHOW_DEFERRALS_BUTTON_CLASS = '.donorDeferralsButton';
DONOR_PRINT_BARCODE_BUTTON_CLASS = '.printBarcode';
DONOR_DONE_BUTTON_CLASS = '.doneButton';
DONOR_CLEAR_BUTTON_CLASS = '.clearFormButton ';
DONATION_PRINT_BUTTON_CLASS = '.printButton';

DONOR_GENDER_ID = '#gender';
DONOR_SAVE_CLASS = '.saveDonorButton';
DONOR_DONE_CLASS = '.doneButton';
DONOR_DAY_OF_MONTH_ID  = '#dayOfMonth';
DONOR_YEAR_ID  = '#year';
DONOR_MONTH_ID = '#month';
DONOR_ADD_ANOTHER_BUTTON_CLASS = '.addAnotherDonorButton';

//Form Constants
DONOR_ADD_FORM = 'form[name="addDonorForm"]'
COMPONENT_FIND_FORM = 'form[name="findProductForm"]';
COMPONENT_RECORD_FORM = 'form[name="recordProductForm"]';
DONATION_EDIT_FORM= 'form[name="editCollectionForm"]';
DONATION_ADD_FORM = 'form[name="addDonationForm"]';

//Donation Constants
DONATION_CENTER = '2';
DONATION_SITE = '2';
COLLECTED_ON = '09/04/2014 12:00:00 AM';
DONATION_IDENTIFICATION_NUMBER = Math.round(+new Date()/1000);


//Donation Selectors 
DONATION_CENTER_CLASS= '.collectionCenterSelector';
DONATION_SITE_CLASS = '.collectionSiteSelector';
DONATION_FORM_CLEAR_CLASS = '.clearFormButton';
DONATION_TYPE='addCollectionFormDonationType'
DONOR_WEIGHT_ID = '#donorWeight';
DONOR_PULSE_ID = '#donorPulse';
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
DONATION_FIND_CLASS ='.findCollectionButton';
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
COMPONENT_RECORD_TAB_SELECTOR = '#recordProductsContent a';
COMPONENT_ADD_TAB_SELECTOR = '#findOrAddProductsContent a';
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


