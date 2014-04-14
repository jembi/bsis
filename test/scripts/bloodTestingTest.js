
/////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST  1 --> Login Test into BSIS  //////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

loginTest(USERNAME,PASSWORD);

////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Assert Donor  page  ////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////

assertTabExistsByName(TEST_RESULTS_TAB,VIEW_TESTING_INFORMATION,BLOOD_TYPING_TEST_RESULTS_TAB_ID);


////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Assert Left Panel Tabs     //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Asserting Left Panel Tabs Test',2,function(test){

casper.then(function(){
          
          if(VIEW_TESTING_INFORMATION)
             test.assertExists(BLOOD_TYPING_TEST_RESULTS_TAB_ID);
           else
             test.assertDoesntExist(BLOOD_TYPING_TEST_RESULTS_TAB_ID);



            if(VIEW_TESTING_INFORMATION)
              test.assertExists(TEST_RESULTS_FIND_TAB_ID);
            else
              test.assertDoesntExist(TEST_RESULTS_FIND_TAB_ID);


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
        
        if(VIEW_TEST_OUTCOME){
          casper.click(TEST_RESULTS_FIND_TAB_ID);
           casper.waitForSelector(TEST_RESULTS_FIND_BUTTON_CLASS, function success(){
             test.pass('Search Product --> Find Products  Page Loaded Successfully') },function timeout(){
                test.fail('Search Products --> FInd Products page loading timeout')},TIMEOUT);
         }
         else
          test.pass('Search Test Results --> No Permission TO View Find Test Results page');



  });

casper.then(function(){

  if(VIEW_TESTING_INFORMATION)
   test.assertExists(DONATION_NUMBER_ID);
 else
   test.assertDoesntExist(DONATION_NUMBER_ID);

 if(VIEW_TESTING_INFORMATION)
   test.assertExists(TEST_RESULTS_FIND_BUTTON_CLASS);
 else
   test.assertDoesntExist(TEST_RESULTS_FIND_BUTTON_CLASS);
   
  if(VIEW_TESTING_INFORMATION)
   test.assertExists(TEST_RESULTS_CLEAR_BUTTON_CLASS);
 else
   test.assertDoesntExist(TEST_RESULTS_CLEAR_BUTTON_CLASS);
  

});

if(VIEW_TEST_OUTCOME)
  casper.then(function(){
     casper.fill(TEST_RESULTS_FIND_FORM, {
     collectionNumber : '1234'
    });

});

casper.then(function(){

 if(VIEW_TEST_OUTCOME){
  casper.click(TEST_RESULTS_FIND_BUTTON_CLASS);
           casper.waitForSelector(TTI_RESULTS_CLASS, function success(){
             test.pass('Search Test Results --> Test Results  Summary Loaded Successfully') },function timeout(){
                test.fail('Search Test Results -->Test Results  Summary  loading timeout')},TIMEOUT);
  }
  else
     test.pass('Search Test Results  --> No Permission To view Test Summary ');

});

casper.then(function(){

  if(VIEW_TEST_OUTCOME)
   test.assertExists(COLLECTION_RESULTS_CLASS);
  else
   test.assertDoesntExist(COLLECTION_RESULTS_CLASS);


  if(VIEW_TEST_OUTCOME)
   test.assertExists(BLOOD_TYPING_RESULTS_CLASS);
  else
   test.assertDoesntExist(COLLECTION_RESULTS_CLASS);


 if(VIEW_TEST_OUTCOME)
   test.assertExists(TTI_RESULTS_CLASS);
  else
   test.assertDoesntExist(COLLECTION_RESULTS_CLASS);
 

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
        
        if(ADD_BLOOD_TYPING_OUTCOME){
          casper.click(BLOOD_TYPING_TEST_RESULTS_TAB_ID);
           casper.waitForSelector(BLOOD_TYPING_COLLECTION_ADD_BUTTON_CLASS, function success(){
             test.pass('Add Blood Typing Results --> Add Blood Typing results for collection  Page Loaded Successfully') },function timeout(){
                test.fail('Add Blood Typing Results --> Add Blood Typing results for collection  Page page loading timeout')},TIMEOUT);
         }
         else
           test.pass('Add Blood Typing Results --> No Permission To View Add Blood Typing Results page ')


  });

casper.then(function(){

   if(ADD_BLOOD_TYPING_OUTCOME)
    test.assertElementCount('form[name="bloodTypingResultsForm"] .collectionNumberInput', 12);
  else
    test.assertDoesntExist('form[name="bloodTypingResultsForm"] .collectionNumberInput', 12);


  if(ADD_BLOOD_TYPING_OUTCOME)
    test.assertExists(BLOOD_TYPING_COLLECTION_ADD_BUTTON_CLASS);
  else
    test.assertExists(BLOOD_TYPING_COLLECTION_ADD_BUTTON_CLASS);

 if(ADD_BLOOD_TYPING_OUTCOME)
   test.assertExists(CLEAR_FORM_BUTTON);
 else
   test.assertExists(CLEAR_FORM_BUTTON);


});


  casper.then(function(){

     casper.fill(BLOOD_TYPING_RESULTS_ADD_FORM, {
     collectionNumber_1 : DONATION_NUMBER
    });

});

casper.then(function(){

 if(ADD_BLOOD_TYPING_OUTCOME){
  casper.click(BLOOD_TYPING_COLLECTION_ADD_BUTTON_CLASS);
           casper.waitForSelector(BLOOD_TYPING_COLLECTION_CHANGE_BUTTON_CLASS, function success(){
             test.pass('Add Blood Typing Results--> Blood Typing Wells form  Loaded Successfully') },function timeout(){
                test.fail(' Add Blood Typing Results-->Blood Typing Wells form  loading timeout')},TIMEOUT);
}
else
           test.pass('Add Blood Typing Results --> No Permission To Add Blood Typing Outcomes page');

});

casper.then(function(){

if(ADD_BLOOD_TYPING_OUTCOME)
   test.assertExists(COLLECTION_RESULTS_CLASS);
 else
   test.assertDoesntExist(COLLECTION_RESULTS_CLASS);

 if(ADD_BLOOD_TYPING_OUTCOME)
   test.assertExists(BLOOD_TYPING_RESULTS_CLASS);
 else
   test.assertDoesntExist(COLLECTION_RESULTS_CLASS);

 if(ADD_BLOOD_TYPING_OUTCOME)
   test.assertExists(TTI_RESULTS_CLASS);
 else
   test.assertDoesntExist(COLLECTION_RESULTS_CLASS);


});

casper.then(function(){

   this.sendKeys("input#well1", "-");
   this.sendKeys("input#well2", "-");
   this.sendKeys("input#well3", "-");
   this.sendKeys("input#well4", "+");
   this.sendKeys("input#well5", "+");
   this.sendKeys("input#well6", "+");
   
});

casper.then(function(){

 if(ADD_BLOOD_TYPING_OUTCOME){
 casper.click(BLOOD_TYPING_RESULTS_SaVE_BUTTON_CLASS);
           casper.waitForSelector(BLOOD_TYPING_RESULTS_FOR_MORE_COLLECTIONS_BUTTON_CLASS, function success(){
             test.pass('Add Blood Typing Results--> Blood Typing Results   Added Successfully') },function timeout(){
                test.fail(' Add Blood Typing Results-->Blood Typing Results   adding timeout')},TIMEOUT);
         }
  else
           test.pass('Add Blood Typing Results --> No Permission To Add Blood Typing Outcomes');


});




casper.run(function() {
    this.echo('Test Successful - Blood Typing .', 'INFO');
       test.done();
   });

});


