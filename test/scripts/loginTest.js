
  
/////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST  1 --> Login Test into BSIS  //////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
var colorizer = require('colorizer').create('Colorizer');
casper.test.begin('Login Test to BSIS',1,function(test){
casper.start(LOGIN_URL, function() {
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
casper.start(WELCOME_URL);
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


/////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Donor Search Test   //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Search For  Donor Record',2,function(test){
casper.start(WELCOME_URL, function() {
     casper.waitForText('Donors', function() {
       test.pass('Donors page Loaded Successful.')},function() {
       test.fail('Donors Page Not Found'); } , TIMEOUT);
   
       }, true);


 casper.then(function(){  


        this.fill('form.formFormatClass:nth-of-type(1)', {
          donorNumber :  DONOR_NUMBER,
          firstName :    DONOR_FIRST_NAME,     
          lastName  : DONOR_LAST_NAME,
          anyBloodGroup : DONOR_ANY_BLOOD_GROUP,
         dueToDonate :  DONOR_DUE_TO_DONATE}, true);

          casper.click('button.findDonorButton');

        casper.waitForText(DONOR_NUMBER, function success() {
             test.pass('Donor Search Result found')   },function fail() {
              test.fail('Donor Search Result Not found')           } , TIMEOUT);    

   });

   casper.run(function() {
    this.echo('Test Successful - Search Donor Record.', 'INFO');
       test.done();
   });

});



/////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Add Donor   //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Add Donor Record',3,function(test){
casper.start(WELCOME_URL, function() {
     casper.waitForText('Donors', function() {
       test.pass('Donors page Loaded Successful.')},function() {
       test.fail('Donors Page Not Found'); } , TIMEOUT);
   
       }, true);

casper.waitForSelector('.formFormatClass:nth-of-type(2)', function success() {
             test.pass('Add Donor Form found')   },function fail() {
              test.fail('Add Donor Form Not found')           } , TIMEOUT);    

 casper.then(function(){  

         this.fill('form.formFormatClass:nth-of-type(2)', {  // working if the form is in same IDV
          firstName:    DONOR_FIRST_NAME,     
          lastName  : DONOR_LAST_NAME,
          'gender' : DONOR_GENDER,
          month    : DONOR_BIRTH_DAY_OF_MONTH,
          year     : DONOR_BIRTH_YEAR,
           dayOfMonth :  DONOR_BIRTH_MONTH}, true);


          casper.click('button.addDonorButton');

        casper.waitForText(DONOR_NUMBER, function success() {
             test.pass('Donor Added Successfully')   },function fail() {
              test.fail('Failed TO Add Donor')           } , TIMEOUT);    

   });



   casper.run(function() {
       this.echo('Test Successful - Delete Donor.', 'GREEN_BAR');
         test.assertExists('.deleteButton');
       test.done();
   });


});


/////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Deleting Donor   //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Delete  Donor Record',3,function(test){
casper.start(WELCOME_URL, function() {
     casper.waitForText('Donors', function() {
       test.pass('Donors page Loaded Successful.')},function() {
       test.fail('Donors Page Not Found'); } , TIMEOUT);
   
       }, true);
   

 casper.then(function(){  
         

        this.fill('', {
          donorNumber :  DONOR_NUMBER,
          firstName :    DONOR_FIRST_NAME,     
          lastName  : DONOR_LAST_NAME,
          anyBloodGroup : DONOR_ANY_BLOOD_GROUP,
         dueToDonate :  DONOR_DUE_TO_DONATE}, true);

          casper.click('button.findDonorButton');
           

        casper.waitForSelector('.findDonorResults', function() {
             test.pass('Donor Search Result found');

               },function() {
              test.fail('Donor Search Result Not found')           } , TIMEOUT);    



       });
 
         // Donor table row click functionality test is to be added  

   casper.run(function() {

    this.echo('Test Successful - Delete Donor.', 'GREEN_BAR');
         test.assertExists('.deleteButton');
       test.done();
   });


});





