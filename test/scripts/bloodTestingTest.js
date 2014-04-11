
/////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST  1 --> Login Test into BSIS  //////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

loginTest(SUPER_USER_NAME,SUPER_USER_PASSWORD);

////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Assert Donor  page  ////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////

assertTabExistsByName(TEST_RESULTS_TAB);


////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Assert Left Panel Tabs     //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Asserting Left Panel Tabs Test',2,function(test){

casper.then(function(){
             test.assertExists(BLOOD_TYPING_TEST_RESULTS_TAB_ID);
             test.assertExists(TEST_RESULTS_FIND_TAB_ID);


     });


casper.run(function() {
    this.echo('Test Successful - Asserting Left Panel Tabs .', 'INFO');
       test.done();
   });

});




////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Search Test  Results     //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Search Test Results Test',8,function(test){

  casper.then(function(){
          
          casper.click(TEST_RESULTS_FIND_TAB_ID);
           casper.waitForSelector(TEST_RESULTS_FIND_BUTTON_CLASS, function success(){
             test.pass('Search Product --> Find Products  Page Loaded Successfully') },function timeout(){
                test.fail('Search Products --> FInd Products page loading timeout')},TIMEOUT);


  });

casper.then(function(){

   test.assertExists(DONATION_NUMBER_ID);
   test.assertExists(TEST_RESULTS_FIND_BUTTON_CLASS);
   test.assertExists(TEST_RESULTS_CLEAR_BUTTON_CLASS);

});


  casper.then(function(){

     casper.fill(TEST_RESULTS_FIND_FORM, {
     collectionNumber : '1234'
    });

});

casper.then(function(){

  casper.click(TEST_RESULTS_FIND_BUTTON_CLASS);
           casper.waitForSelector(TTI_RESULTS_CLASS, function success(){
             test.pass('Search Test Results --> Test Results  Summary Loaded Successfully') },function timeout(){
                test.fail('Search Test Results -->Test Results  Summary  loading timeout')},TIMEOUT);

});

casper.then(function(){

   test.assertExists(COLLECTION_RESULTS_CLASS);
   test.assertExists(BLOOD_TYPING_RESULTS_CLASS);
   test.assertExists(TTI_RESULTS_CLASS);

});


casper.run(function() {
    this.echo('Test Successful - Search  Test results .', 'INFO');
       test.done();
   });

});



////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Add Blood Typing  Results     //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Add Blood  Typing Results Test',9,function(test){

  casper.then(function(){
          
          casper.click(BLOOD_TYPING_TEST_RESULTS_TAB_ID);
           casper.waitForSelector(BLOOD_TYPING_COLLECTION_ADD_BUTTON_CLASS, function success(){
             test.pass('Add Blood Typing Results --> Add Blood Typing results for collection  Page Loaded Successfully') },function timeout(){
                test.fail('Add Blood Typing Results --> Add Blood Typing results for collection  Page page loading timeout')},TIMEOUT);


  });

casper.then(function(){

    test.assertElementCount('form[name="bloodTypingResultsForm"] .collectionNumberInput', 12);
    test.assertExists(BLOOD_TYPING_COLLECTION_ADD_BUTTON_CLASS);
   test.assertExists(CLEAR_FORM_BUTTON);

});


  casper.then(function(){

     casper.fill(BLOOD_TYPING_RESULTS_ADD_FORM, {
     collectionNumber_1 : '1234'
    });

});

casper.then(function(){

  casper.click(BLOOD_TYPING_COLLECTION_ADD_BUTTON_CLASS);
           casper.waitForSelector(BLOOD_TYPING_COLLECTION_CHANGE_BUTTON_CLASS, function success(){
             test.pass('Add Blood Typing Results--> Blood Typing Wells form  Loaded Successfully') },function timeout(){
                test.fail(' Add Blood Typing Results-->Blood Typing Wells form  loading timeout')},TIMEOUT);

});

casper.then(function(){

   test.assertExists(COLLECTION_RESULTS_CLASS);
   test.assertExists(BLOOD_TYPING_RESULTS_CLASS);
   test.assertExists(TTI_RESULTS_CLASS);

});

casper.then(function(){

   this.sendKeys("input#well1", "-");
   this.sendKeys("input#well2", "-");
   this.sendKeys("input#well3", "-");
   this.sendKeys("input#well4", "+");
   this.sendKeys("input#well5", "+");
   this.sendKeys("input#well6", "+");
   casper.wait('5000');
   
});

casper.then(function(){

 casper.click(BLOOD_TYPING_RESULTS_SaVE_BUTTON_CLASS);
           casper.waitForSelector(BLOOD_TYPING_RESULTS_FOR_MORE_COLLECTIONS_BUTTON_CLASS, function success(){
             test.pass('Add Blood Typing Results--> Blood Typing Results   Added Successfully') },function timeout(){
                test.fail(' Add Blood Typing Results-->Blood Typing Results   adding timeout')},TIMEOUT);

});




casper.run(function() {
    this.echo('Test Successful - Blood Typing .', 'INFO');
       test.done();
   });

});


