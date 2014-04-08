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


//Tab Constants

 DONOR_TAB = 'Donors';
DONATION_TAB = 'Donations'
PRODUCT_TAB = 'Products';
ADMIN_TAB= 'Admin';
REQUEST_TAB = 'Requests';
REPORT_TAB = 'Reports';
USAGE_TAB= 'Usage';
HOME_TAB = 'Home';
LOT_RELEASE_TAB = 'Lot Release';
TEST_RESULTS_TAB = 'Test Results';


//Donor Constants
DONOR_NUMBER = '000001';
DONOR_FIRST_NAME = 'malyala';
DONOR_LAST_NAME = '';
DONOR_ANY_BLOOD_GROUP = true;
DONOR_DUE_TO_DONATE = false;
DONOR_DAY_OF_MONTH = '13';
DONOR_YEAR  = '1985';
DONOR_MONTH = '01';
DONOR_GENDER = 'male';


//Donor Selectors
DONOR_NUMBER_ID  = '#donorNumber';
DONOR_FIRST_NAME_ID = '#firstName';
DONOR_LAST_NAME_ID  = '#lastName';
DONOR_ANY_BLOOD_GROUP_ID = '#anyBloodGroup';
DONOR_DUE_TO_DONATE_ID= '#dueToDonate1';
DONOR_FIND_BUTTON_CLASS = 'button.findDonorButton';
DONOR_ADD_BUTTON_CLASS  = 'button.addDonorButton';
DONOR_EDIT_CLASS = '.editButton' ;
DONOR_DELETE_CLASS = '.deleteButton';
DONOR_TABLE_CLASS = '.odd';
DONOR_GENDER_ID = '#gender';
DONOR_SAVE_CLASS = '.saveDonorButton';
DONOR_DONE_CLASS = '.doneButton';
DONOR_DAY_OF_MONTH_ID  = '#dayOfMonth';
DONOR_YEAR_ID  = '#year';
DONOR_MONTH_ID = '#month';
DONOR_CLEAR_BUTTON_CLASS = '.clearFormButton ';

//Donation Constants
DONATION_CENTER = 'Lusaka';
DONATION_SITE = 'Ndola';
COLLECTED_ON = '08/04/2014';
DONATION_IDENTIFICATION_NUMBER = '12345'


//DOnation Selectors 
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





//URl Constants
DONOR_SUMMARY_URL ='http://localhost:8080/bsis/donorSummary.html?donorId=3';





//var donor={number : '0002' , name ; 'JOHN'};    [This strategy is not working ]




