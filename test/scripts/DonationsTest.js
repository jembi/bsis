

  
/////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST  1 --> Login Test into BSIS  //////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Login Test to BSIS',1,function(test){
casper.start(LOGIN_URL, function() {
    this.fillSelectors(LOGIN_FORM_ID, {
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
   }).viewport(1000,1000);

});


////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - View Donations page  //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('View Donations Page',2,function(test){
  casper.start(WELCOME_URL).viewport(1000,1000);

casper.then(function(){
    var tabSelector  =     this.evaluate(function(){

          for(var i=1;i<10;i++){
         var  selector =  "#topPanelTabs li:nth-child("+i+") a";
          if(document.querySelector(selector).textContent ==='Donations'){
           return selector;
       }
    }
    return null;
 });

     test.assertExists(tabSelector);
     casper.click(tabSelector);
     casper.waitForText('Find Collections', function success(){
        test.pass('Donations Page Loaded Successfully') },function timeout(){
          test.fail('Donations page timeout')},TIMEOUT);

   });

    casper.run(function() {
    this.echo('Test Successful - View Donations Page', 'INFO');
       test.done();
   });

  });


////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Add  Donation Record  //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Add Donations Record',15,function(test){

 casper.then(function(){

casper.click(DONATION_ADD_TAB);
  casper.waitForSelector(DONOR_WEIGHT_ID, function success(){
        test.pass('Add Donation --> Donations Loaded Successfully') },function timeout(){
          test.fail('Add Donation --> Donations page timeout')},TIMEOUT);

       
 });


casper.then(function(){
//Asserting Input fields
test.assertExists(DONATION_NUMBER_ID);
test.assertExists(DONOR_NUMBER_ID);
test.assertExists(DONATION_COLLECTED_ON_ID);
test.assertExists(DONATION_TYPE_CLASS);
test.assertExists(HAEMOGLOBIN_COUNT_ID);
test.assertExists('#bloodBagType'); //unable to locate -- ID is dynamic and no class specified
test.assertExists(DONATION_CENTER_CLASS);
test.assertExists(DONATION_SITE_CLASS);
test.assertExists(DONOR_WEIGHT_ID);
test.assertExists(DONOR_PULSE_ID);
test.assertExists(BLOOD_PRESSURE_SYSTOLIC_ID);
test.assertExists(BLOOD_PRESSURE_DIASTOLIC_ID);
test.assertExists(NOTES_ID);

// Asserting buttons
test.assertExists(DONATION_ADD_CLASS);
test.assertExists(DONATION_CLEAR_FORM_CLASS);

});

casper.then(function() {
   
      this.fill(DONATION_ADD_FORM, {  
           collectionNumber :  DONATION_IDENTIFICATION_NUMBER,
           donorNumber      :  DONOR_NUMBER,
           collectedOn      :  COLLECTED_ON,
           collectionCenter :  DONATION_CENTER,
           collectionSite   :  DONATION_SITE
      });

});



casper.then(function(){

  casper.click(DONATION_ADD_CLASS);
  casper.waitForSelector(DONATION_ADD_ANOTHER_CLASS, function() {
        test.pass('Add Donation --> Add Donation  successful.')},
        function() {
        test.fail('Add DOnation -->Add Donation Failed '); } , TIMEOUT);

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

       casper.click(DONATION_FIND_TAB);
       casper.waitForSelector('.formFormatClass', function success(){
        test.pass('Search Donation -- >Donations Page Loaded Successfully') },function timeout(){
          test.fail('Search Donation -->Donations page timeout')},TIMEOUT);

       
   

});

casper.then(function(){

  test.assertExists(DONATION_NUMBER_ID);
  test.assertExists(DONATION_CENTER_CLASS);
  test.assertExists(DONATION_COLLECTED_TO_ID);
  test.assertExists(DONATION_SITE_CLASS);
  test.assertExists(BLOOD_BAG_CLASS);
  test.assertExists(DONATION_COLLECTED_FROM_ID);
  test.assertExists(DONATION_FIND_CLASS);

});


  casper.then(function(){

    casper.click(DONATION_FIND_CLASS);
     casper.waitForSelector(TABLE_CLASS, function success(){
        test.pass('Search Donation --> Donation Results  Loaded Successfully') },function timeout(){
          test.fail('Search Donation --> Donation Results  Loading  timeout')},TIMEOUT);
  });


 casper.then(function(){
   test.assertExists(TABLE_CLASS);
    casper.click(TABLE_CLASS);
     casper.waitForSelector(DONATION_EDIT_BUTTON_CLASS, function success(){
        test.pass('Search Donation --> Donation Summary page  Loaded Successfully') },function timeout(){
          test.fail('Edit Donation --> Donation Summary page  Loading  timeout')},TIMEOUT);
  });



casper.then(function(){

  test.assertExists(DONATION_DONE_BUTTON_CLASS);
  test.assertExists(DONATION_EDIT_BUTTON_CLASS);
  test.assertExists(DONATION_DELETE_BUTTON_CLASS);
  test.assertExists(DONATION_PRINT_BUTTON_CLASS);
  

});

casper.then(function(){
 
  casper.waitForSelector(DONATION_FIND_CLASS, function success(){
     test.pass('Search Donation -- >Donations Page Loaded Successfully') },function timeout(){
          test.fail('Search Donation -->Donations page timeout')},TIMEOUT);
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
  test.assertExists(DONATION_FIND_CLASS);
   casper.click(DONATION_FIND_TAB);
     casper.waitForSelector(TABLE_CLASS, function success(){
        test.pass('Edit Donation -- Donation Results  Loaded Successfully') },function timeout(){
          test.fail('Edit Donation -- Donation Results  Loading  timeout')},TIMEOUT);
  
    
  });

 
  casper.then(function(){
   test.assertExists(TABLE_CLASS);
    casper.click(TABLE_CLASS);
     casper.waitForSelector(DONATION_EDIT_BUTTON_CLASS, function success(){
        test.pass('Edit Donation -- Donation Summary page  Loaded Successfully') },function timeout(){
          test.fail('Edit Donation -- Donation Summary page  Loading  timeout')},TIMEOUT);
  });
 
 
  casper.then(function(){

   test.assertExists(DONATION_EDIT_BUTTON_CLASS);
    casper.click(DONATION_EDIT_BUTTON_CLASS);
     casper.waitForSelector(DONATION_SAVE_BUTTON_CLASS, function success(){
        test.pass('Edit Donation -- Edit Donation page  Loaded Successfully') },function timeout(){
          test.fail('Edit Donation -- Edit Donation page   Loading  timeout')},TIMEOUT);
  });

casper.then(function() {
   
      this.fill(DONATION_EDIT_FORM, {  
           collectionCenter :  DONATION_CENTER,  
           collectionSite   :  DONATION_SITE
      });

});
casper.then(function(){
   test.assertExists(DONATION_SAVE_BUTTON_CLASS);
    casper.click(DONATION_SAVE_BUTTON_CLASS);
     casper.waitForSelector(DONATION_DONE_BUTTON_CLASS, function success(){
        test.pass('Edit Donation --> Donation Summary  page  Loaded Successfully') },function timeout(){
          test.fail('Edit Donation --> Donation Summary page   Loading  timeout')},TIMEOUT);
  });

 casper.then(function(){
   test.assertExists(DONATION_DONE_BUTTON_CLASS);
    casper.click(DONATION_DONE_BUTTON_CLASS);
     casper.waitForSelector(DONATION_FIND_CLASS, function success(){
        test.pass('Edit Donation --> FInd Donation  page  Loaded Successfully') },function timeout(){
          test.fail('Edit Donation --> Find Donation page   Loading  timeout')},TIMEOUT);
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
   test.assertExists(DONATION_FIND_CLASS);
    casper.click(DONATION_FIND_CLASS);
     casper.waitForSelector(TABLE_CLASS, function success(){
        test.pass('Delete Donation -- Donation Results  Loaded Successfully') },function timeout(){
          test.fail('Delete Donation -- Donation Results  Loading  timeout')},TIMEOUT);
    
  });

 
  casper.then(function(){
    test.assertExists(TABLE_CLASS);
    casper.click(TABLE_CLASS);
     casper.waitForSelector(TABLE_CLASS, function success(){
        test.pass('Delete Donation -- Donation Summmary Page  Loaded Successfully') },function timeout(){
          test.fail('Delete Donation -- Donation Summary page  Loading  timeout')},TIMEOUT);
     
  });
 
 
  casper.then(function(){
   test.assertExists(DONATION_DELETE_BUTTON_CLASS);
    casper.click(DONATION_DELETE_BUTTON_CLASS);
    casper.setFilter(PAGE_CONFIRM, function() {
    return true;
  });
      casper.waitForSelector(DONATION_FIND_CLASS, function success(){
        test.pass('Delete Donation --> Donation  Deleted Successfully') },function timeout(){
          test.fail('Delete Donation -->  Deleting Donation Failed')},TIMEOUT);
     
  });

casper.then(function(){


});


casper.run(function() {
    this.echo('Test Successful - Delete DOnation Record', 'INFO');
       test.done();
   });


});






