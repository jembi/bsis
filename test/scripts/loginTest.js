 
  
/////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST  1 --> Login Test into BSIS  //////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

loginTest(SUPER_USER_NAME,SUPER_USER_PASSWORD);


/////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 2 --> Verify the Tabs /////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Test For Verifying Tabs',10,function(test){

 casper.then(function(){
        
       
        test.assertSelectorHasText('a', HOME_TAB);
        test.assertSelectorHasText('a', DONATION_TAB);
        test.assertSelectorHasText('a', DONOR_TAB);
        test.assertSelectorHasText('a', TEST_RESULTS_TAB);
        test.assertSelectorHasText('a', REQUEST_TAB);
        test.assertSelectorHasText('a', USAGE_TAB);
        test.assertSelectorHasText('a', ADMIN_TAB);
        test.assertSelectorHasText('a', PRODUCT_TAB);
        test.assertSelectorHasText('a', LABELLING_TAB);
        test.assertSelectorHasText('a', REPORT_TAB);
      
    
    });

 casper.run(function() {
    casper.echo('Test Successful - Verifying  Tabs', 'INFO');
       test.done();
   });

});











