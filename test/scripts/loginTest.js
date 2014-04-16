 
  
/////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST  1 --> Login Test into BSIS  //////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

loginTest(USERNAME,PASSWORD);


/////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 2 --> Verify the Tabs /////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////



casper.test.begin('Test For Verifying Tabs',10,function(test){



test.assertExists(HOME_PAGE_SELECTOR);


 casper.then(function(){
  if(VIEW_DONOR_INFORMATION)
        test.assertExists(DONOR_PAGE_SELECTOR);
    else
         test.assertDoesntExist(DONOR_PAGE_SELECTOR);

 });

  casper.then(function(){
  if(VIEW_DONATION_INFORMATION)
        test.assertExists(DONATION_PAGE_SELECTOR);
    else
        test.assertDoesntExist(DONATION_PAGE_SELECTOR);
});

   casper.then(function(){
     if(VIEW_TESTING_INFORMATION)
        test.assertExists(TESTING_PAGE_SELECTOR);
    else
        test.assertDoesntExist(TESTING_PAGE_SELECTOR);
});


 casper.then(function(){
     if(VIEW_COMPONENT_INFORMATION)
        test.assertExists(COMPONENT_PAGE_SELECTOR);
    else
        test.assertDoesntExist(COMPONENT_PAGE_SELECTOR);
});

    casper.then(function(){
     if(VIEW_INVENTORY_INFORMATION)
        test.assertExists(REQUEST_PAGE_SELECTOR);
    else
        test.assertDoesntExist(REQUEST_PAGE_SELECTOR);
});

     casper.then(function(){
     if(VIEW_INVENTORY_INFORMATION)
         test.assertExists(USAGE_PAGE_SELECTOR);
     else
        test.assertDoesntExist(USAGE_PAGE_SELECTOR);
});



  casper.then(function(){
 if(VIEW_ADMIN_INFORMATION)
        test.assertExists(ADMIN_PAGE_SELECTOR);
    else
        test.assertDoesntExist(ADMIN_PAGE_SELECTOR);
});

 
 casper.then(function(){
     if(LABELLING_PAGE_SELECTOR)
         test.assertExists(LABELLING_PAGE_SELECTOR);
     else
         test.assertDoesntExist(LABELLING_PAGE_SELECTOR);
 });

 casper.then(function(){
     if(VIEW_REPORTING_INFORMATION)
       test.assertExists(REPORT_PAGE_SELECTOR);
   else
       test.assertDoesntExist(REPORT_PAGE_SELECTOR);

});

    
 casper.run(function() {
    casper.echo('Test Successful - Verifying  Tabs', 'INFO');
       test.done();
   });

});











