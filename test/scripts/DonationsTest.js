

  
/////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST  1 --> Login Test into BSIS  //////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

loginTest(USERNAME,PASSWORD);


////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Assert Donations page  //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

assertTabExistsByName(DONATION_TAB , VIEW_DONATION_INFORMATION , DONATION_SITE_CLASS);

////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Add  Donation Record  //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Add Donations Record',17,function(test){

 casper.then(function(){

if(ADD_DONATION){
casper.click(DONATION_ADD_TAB);
  casper.waitForSelector(DONOR_WEIGHT_ID, function success(){
        test.pass('Add Donation --> Donations Loaded Successfully') },function timeout(){
          test.fail('Add Donation --> Donations page timeout')},TIMEOUT);

  }else
        test.pass('Add Donation -- No Permission to view Add Donations Page');

});



casper.then(function(){
//Asserting Input fields

if(ADD_DONATION)
test.assertExists(DONATION_NUMBER_ID);
else
test.assertDoesntExist(DONATION_NUMBER_ID);

if(ADD_DONATION)
test.assertExists(DONOR_NUMBER_ID);
else
test.assertDoesntExist(DONOR_NUMBER_ID);

if(ADD_DONATION)
test.assertExists(DONATION_COLLECTED_ON_ID);
else
test.assertDoesntExist(DONATION_COLLECTED_ON_ID);

if(ADD_DONATION)
test.assertExists(DONATION_TYPE_CLASS);
else
test.assertDoesntExist(DONATION_TYPE_CLASS);

if(ADD_DONATION)
test.assertExists(HAEMOGLOBIN_COUNT_ID);
else
test.assertDoesntExist(HAEMOGLOBIN_COUNT_ID);

if(ADD_DONATION)
test.assertExists(BLOOD_BAG_TYPE); 
else
test.assertDoesntExist(BLOOD_BAG_TYPE); 

if(ADD_DONATION)
test.assertExists(DONATION_CENTER_CLASS);
else
test.assertDoesntExist(DONATION_CENTER_CLASS);

if(ADD_DONATION)
test.assertExists(DONATION_SITE_CLASS);
else
test.assertDoesntExist(DONATION_SITE_CLASS);

if(ADD_DONATION)
test.assertExists(DONOR_WEIGHT_ID);
else
test.assertDoesntExist(DONOR_WEIGHT_ID);

if(ADD_DONATION)
test.assertExists(DONOR_PULSE_ID);
else
test.assertDoesntExist(DONOR_PULSE_ID);

if(ADD_DONATION)
test.assertExists(BLOOD_PRESSURE_SYSTOLIC_ID);
else
test.assertDoesntExist(BLOOD_PRESSURE_SYSTOLIC_ID);

if(ADD_DONATION)
test.assertExists(BLOOD_PRESSURE_DIASTOLIC_ID);
else
test.assertDoesntExist(BLOOD_PRESSURE_DIASTOLIC_ID);

if(ADD_DONATION)
test.assertExists(NOTES_ID);
else
test.assertDoesntExist(NOTES_ID);

// Asserting buttons

if(ADD_DONATION)
test.assertExists(DONATION_ADD_CLASS);
esle
test.assertDoesntExist(DONATION_ADD_CLASS);

if(ADD_DONATION)
test.assertExists(DONATION_CLEAR_FORM_CLASS);
else
test.assertDoesntExist(DONATION_CLEAR_FORM_CLASS);


});



casper.then(function() {
if(ADD_DONATION){
   
      this.fill(DONATION_ADD_FORM, {  
           collectionNumber :  DONATION_IDENTIFICATION_NUMBER,
           donorNumber      :  DONOR_NUMBER,
           collectedOn      :  COLLECTED_ON,
           collectionCenter :  DONATION_CENTER,
           collectionSite   :  DONATION_SITE
      });
  }
});



casper.then(function(){

if(ADD_DONATION){
  casper.click(DONATION_ADD_CLASS);
  casper.waitForSelector(DONATION_ADD_ANOTHER_CLASS, function() {
        test.pass('Add Donation --> Add Donation  successful.')},
        function() {
        test.fail('Add DOnation -->Add Donation Failed '); } , TIMEOUT);
}else
        test.pass('Add Donation --> No Permission to Add results');

});

  
  

casper.run(function() {
    this.echo('Test Successful - Add DOnation Record', 'INFO');
       test.done();
   });
});




////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Search Donation Record  //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Search  Donation Records',16,function(test){
  

 casper.then(function(){

      
    if(VIEW_DONATION){
       casper.click(DONATION_FIND_TAB);
       casper.waitForSelector('.formFormatClass', function success(){
        test.pass('Search Donation -- >Donations Page Loaded Successfully') },function timeout(){
          test.fail('Search Donation -->Donations page timeout')},TIMEOUT); 
  }else
      test.pass('Search Donation -- >No Permission To View Donations Page') ; 


});

casper.then(function(){

if(VIEW_DONATION)
  test.assertExists(DONATION_NUMBER_ID);
else
  test.assertDoesntExist(DONATION_NUMBER_ID);
 

if(VIEW_DONATION)
  test.assertExists(DONATION_CENTER_CLASS);
else
  test.assertDoesntExist(DONATION_CENTER_CLASS);


if(VIEW_DONATION)
  test.assertExists(DONATION_COLLECTED_TO_ID);
else
  test.assertDoesntExist(DONATION_COLLECTED_TO_ID);

if(VIEW_DONATION)
  test.assertExists(DONATION_SITE_CLASS);
else
  test.assertDoesntExist(DONATION_SITE_CLASS);

if(VIEW_DONATION)
  test.assertExists(BLOOD_BAG_CLASS);
else
  test.assertDoesntExist(BLOOD_BAG_CLASS);

if(VIEW_DONATION)
  test.assertExists(DONATION_COLLECTED_FROM_ID);
else
  test.assertDoesntExist(DONATION_COLLECTED_FROM_ID);

if(VIEW_DONATION)
  test.assertExists(DONATION_FIND_BUTTON_CLASS);
else
  test.assertDoesntExist(DONATION_FIND_BUTTON_CLASS);


});


  casper.then(function(){
if(VIEW_DONATION){
    casper.click(DONATION_FIND_BUTTON_CLASS);
     casper.waitForSelector(TABLE_CLASS, function success(){
        test.pass('Search Donation --> Donation Results  Loaded Successfully') },function timeout(){
          test.fail('Search Donation --> Donation Results  Loading  timeout')},TIMEOUT);
     }else
     test.pass('Search Donation --> No Permission To View Donation results');
  });


 casper.then(function(){

  if(VIEW_DONATION){
   test.assertExists(TABLE_CLASS);
    casper.click(TABLE_CLASS);
     casper.waitForSelector(DONATION_EDIT_BUTTON_CLASS, function success(){
        test.pass('Search Donation --> Donation Summary page  Loaded Successfully') },function timeout(){
          test.fail('Edit Donation --> Donation Summary page  Loading  timeout')},TIMEOUT);
   }else
     test.pass('Search Donation --> No Permission To View Donation Summary Page');

  });


casper.then(function(){

 if(VIEW_DONATION)
  test.assertExists(DONATION_DONE_BUTTON_CLASS);
else
  test.assertDoesntExist(DONATION_DONE_BUTTON_CLASS);


if(VIEW_DONATION)
  test.assertExists(DONATION_EDIT_BUTTON_CLASS);
else
  test.assertDoesntExist(DONATION_DONE_BUTTON_CLASS);


if(VIEW_DONATION)
  test.assertExists(DONATION_DELETE_BUTTON_CLASS);
else
  test.assertDoesntExist(DONATION_DONE_BUTTON_CLASS);


if(VIEW_DONATION)
  test.assertExists(DONATION_PRINT_BUTTON_CLASS);
else
  test.assertDoesntExist(DONATION_DONE_BUTTON_CLASS);

  

});



casper.run(function() {
    this.echo('Test Successful - Search DOnation Record', 'INFO');
       test.done();
   });



});




////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Edit  Donation Record  //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Edit Donations Record',10,function(test){
  

 
  casper.then(function(){
if(EDIT_DONATION){
   test.assertExists(DONATION_EDIT_BUTTON_CLASS);
    casper.click(DONATION_EDIT_BUTTON_CLASS);
     casper.waitForSelector(DONATION_SAVE_BUTTON_CLASS, function success(){
        test.pass('Edit Donation --> Edit Donation page  Loaded Successfully') },function timeout(){
          test.fail('Edit Donation --> Edit Donation page   Loading  timeout')},TIMEOUT);
   }else
   test.pass('Edit Donation --> No Permission TO view Edit Donation page');
  });

casper.then(function() {
      if(EDIT_DONATION)
      this.fill(DONATION_EDIT_FORM, {  
           collectionCenter :  DONATION_CENTER,  
           collectionSite   :  DONATION_SITE
      });

});
casper.then(function(){
  if(EDIT_DONATION){
   test.assertExists(DONATION_SAVE_BUTTON_CLASS);
    casper.click(DONATION_SAVE_BUTTON_CLASS);
     casper.waitForSelector(DONATION_DONE_BUTTON_CLASS, function success(){
        test.pass('Edit Donation --> Donation Summary  page  Loaded Successfully') },function timeout(){
          test.fail('Edit Donation --> Donation Summary page   Loading  timeout')},TIMEOUT);
   }else
   test.pass('Edit Donation --> No Permission TO  Edit Donation ');

  });

casper.run(function() {
    this.echo('Test Successful - Edit DOnation Record', 'INFO');
       test.done();
   });


});

////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Delete  Donation Record  //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Delete  Donations Record',6,function(test){
  
  
  casper.then(function(){
  if(VOID_DONATION){
   test.assertExists(DONATION_DELETE_BUTTON_CLASS);
    casper.click(DONATION_DELETE_BUTTON_CLASS);
    casper.setFilter(PAGE_CONFIRM, function() {
    return true;
  });

      casper.waitForSelector(DONATION_FIND_BUTTON_CLASS, function success(){
        test.pass('Delete Donation --> Donation  Deleted Successfully') },function timeout(){
          test.fail('Delete Donation -->  Deleting Donation Failed')},TIMEOUT);
     }else
      test.fail('Delete Donation -->  No Permission To Delete Donation')
  });



casper.run(function() {
    this.echo('Test Successful - Delete DOnation Record', 'INFO');
       test.done();
   });


});






