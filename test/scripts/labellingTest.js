/////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST  1 --> Login Test into BSIS  //////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

loginTest(USERNAME,PASSWORD);


////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 2 --> Labelling  page  //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

assertTabExistsByName(LABELLING_TAB,VIEW_INVENTORY_INFORMATION,CHECK_RESULTS_BUTTON_CLASS);

////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3--> Labelling  page  //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Asserting Lot Release  Page',4,function(test){


    casper.then(function(){
      if(ISSUE_COMPONENT)
          test.assertExists(LABELLING_DIN_ID);
       else
          test.assertDoesntExist(LABELLING_DIN_ID);

     if(ISSUE_COMPONENT)
          test.assertExists(CLEAR_FORM_BUTTON);
      else
          test.assertDoesntExist(CLEAR_FORM_BUTTON);

       if(ISSUE_COMPONENT)
          test.assertExists(CHECK_RESULTS_BUTTON_CLASS);
        else
          test.assertDoesntExist(CHECK_RESULTS_BUTTON_CLASS);


     });

  casper.then(function(){

   if(ISSUE_COMPONENT){
      this.sendKeys(LABELLING_DIN_ID, DONATION_NUMBER);
      casper.click(CHECK_RESULTS_BUTTON_CLASS);
       casper.waitForSelector(PRINT_DICARD_BUTTON_CLASS, function success(){
        test.pass('Lot Release  page  --> Results Loaded  Successfully') },function timeout(){
          test.fail('Lot Release page  --> Results  Loading timeout')},TIMEOUT);
    }else
       test.pass("No Permission to print the pack label")

    });


    casper.run(function() {
    this.echo('Test Successful - Labelling Page', 'INFO');
       test.done();
   });

  });
