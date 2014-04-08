 
  
/////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST  1 --> Login Test into BSIS  //////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

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
        test.assertSelectorHasText('a', LOT_RELEASE_TAB);
        test.assertSelectorHasText('a', REPORT_TAB);
      
    
    });

 casper.run(function() {
    casper.echo('Test Successful - Verifying  Tabs', 'INFO');
       test.done();
   });

});











