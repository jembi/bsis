

  
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
   });

});


////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Find Donation Record  //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Find Donations Record',8,function(test){
  casper.start(WELCOME_URL).viewport(1000,1000);

 casper.then(function(){

        casper.click(DONATION_FIND_TAB);
       casper.waitForSelector('.formFormatClass', function success(){
        test.pass('Donors Page Loaded Successfully') },function timeout(){
          test.fail('Donors page timeout')},TIMEOUT);

       
   

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

    casper.click('button.findCollectionButton');
  });


casper.run(function() {
    this.echo('Test Successful - Find DOnation Record', 'INFO');
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
        test.pass('Donations Loaded Successfully') },function timeout(){
          test.fail('Donations page timeout')},TIMEOUT);

       
 });

casper.then(function(){
//Asserting Input fields
test.assertExists(DONATION_NUMBER_ID);
test.assertExists(DONATION_BATCH_NUMBER_ID);
test.assertExists(DONOR_NUMBER);
test.assertExists(DONATION_COLLECTED_ON_ID);
test.assertExists(DONATION_TYPE_CLASS);
test.assertExists(HAEMOGLOBIN_COUNT_ID);
 // test.assertExists('#bloodBagType');
test.assertExists(DONATION_CENTER_CLASS);
test.assertExists(DONOR_WEIGHT_ID);
test.assertExists(DONOR_PULSE_ID);
test.assertExists(BLOOD_PRESSURE_SYSTOLIC_ID);
test.assertExists(BLOOD_PRESSURE_DIASTOLIC_ID);
test.assertExists(NOTES_ID);

// Asserting buttons
test.assertExists(DONATION_ADD_CLASS);
test.assertExists(DONATION_CLEAR_FORM_CLASS);

});

casper.run(function() {
    this.echo('Test Successful - Add DOnation Record', 'INFO');
       test.done();
   });
});


////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 4- Find  Donation Batch  //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Add Donations Record',6,function(test){

 casper.then(function(){

casper.click(DONATION_FIND_BATCH_TAB);
  casper.waitForText('Find Collection Batches', function success(){
        test.pass('Find Collection Batches Page Loaded Successfully') },function timeout(){
          test.fail('Find Collection Batches page timeout')},TIMEOUT);

       
 });

casper.then(function(){

//Asserting fields
test.assertExists(DONATION_BATCH_NUMBER_ID);
test.assertExists(DONATION_BATCH_CENTER_CLASS);
test.assertExists(DONATION_BATCH_SITE_CLASS);

//asserting buttons
test.assertExists(DONATION_BATCH_FIND_CLASS);
test.assertExists(DONATION_BATCH_FORM_CLEAR_CLASS);

});

casper.run(function() {
    this.echo('Test Successful -  Find Donation Batche', 'INFO');
       test.done();
   });
});

////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 5- Add Donation Batch  //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('New Donations Record',6,function(test){

 casper.then(function(){

casper.click(DONATION_ADD_BATCH_TAB);
  casper.waitForSelector(DONATION_CENTER_CLASS, function success(){
        test.pass('New Collection Batches Page Loaded Successfully') },function timeout(){
          test.fail('New Collection Batches page timeout')},TIMEOUT);

       });

casper.then(function(){

//Asserting fields
test.assertExists(DONATION_BATCH_CENTER_CLASS);
test.assertExists(DONATION_BATCH_SITE_CLASS);
test.assertExists('#notes');

//asserting buttons
test.assertExists(DONATION_BATCH_ADD_CLASS);
test.assertExists(DONATION_FORM_CLEAR_CLASS);

});


casper.run(function() {
    this.echo('Test Successful -  Add Donation Batch', 'INFO');
       test.done();
   });
});


////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 5- Add  Worksheet//////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('New Donations Record',6,function(test){

 casper.then(function(){

casper.click('#addWorksheet a');
  casper.waitForSelector(WORKSHEET_ADD_CLASS, function success(){
        test.pass('Add Worksheet Page Loaded Successfully') },function timeout(){
          test.fail('Add Worksheet page timeout')},TIMEOUT);

       });

casper.then(function(){

//Asserting fields
test.assertExists(WORKSHEET_TYPE_CLASS);
test.assertExists(NOTES_ID);


//asserting buttons
test.assertExists(WORKSHEET_ADD_CLASS);
test.assertExists(WORKSHEET_CLEAR_CLASS);

});


casper.run(function() {
    this.echo('Test Successful -  Add Worksheet', 'INFO');
       test.done();
   });
});

////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 5- Find Worksheets //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Find Worksheets',5,function(test){

 casper.then(function(){

casper.click(WORKSHEET_FIND_TAB);
  casper.waitForText('Find collections worksheet', function success(){
        test.pass(' Find Worksheet Page Loaded Successfully') },function timeout(){
          test.fail('Find Worksheets page timeout')},TIMEOUT);

       });

casper.then(function(){

//Asserting fields
test.assertExists(WORKSHEET_NUMBER_ID);
test.assertExists(WORKSHEET_TYPE_CLASS);


//asserting buttons
test.assertExists(WORKSHEET_FIND_CLASS);
test.assertExists(WORKSHEET_CLEAR_FIND_CLASS);

});


casper.run(function() {
    this.echo('Test Successful -  Finding Worksheets', 'INFO');
       test.done();
   });
});
