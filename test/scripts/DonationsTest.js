

  
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
///////////////////////////  TEST 3 - Find Donation Record  //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Find Donations Record',9,function(test){
  

 casper.then(function(){

       casper.click(DONATION_FIND_TAB);
       casper.waitForSelector('.formFormatClass', function success(){
        test.pass('Donations Page Loaded Successfully') },function timeout(){
          test.fail('Donations page timeout')},TIMEOUT);

       
   

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
     casper.waitForText('print', function success(){
        test.pass('Donations Search -- Donation Results  Loaded Successfully') },function timeout(){
          test.fail('Donations Search -- Donation Results  Loading  timeout')},TIMEOUT);
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
test.assertExists(DONOR_NUMBER_ID);
test.assertExists(DONATION_COLLECTED_ON_ID);
test.assertExists(DONATION_TYPE_CLASS);
test.assertExists(HAEMOGLOBIN_COUNT_ID);
 // test.assertExists('#bloodBagType');
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

  //   this.evaluate(function() {
  //       var document.querySelector('#collectionNumber');
  //       document.getElementById(DONOR_NUMBER_ID).value = DONOR_NUMBER;       
  //       document.getElementById(DONATION_COLLECTED_ON_ID).value = COLLECTED_ON;
  //       document.getElementById(DONATION_CENTER_CLASS).value = DONATION_CENTER;
  //       document.getElementById(DONATION_SITE_CLASS).value = DONATION_SITE;

  // });
   

      this.fill('form.formFormatClass', {  
           collectionNumber : DONOR_NUMBER,
           firstName   : DONOR_FIRST_NAME,
      });

});

casper.then(function(){

  // casper.click(DONATION_ADD_CLASS);
  casper.waitForText('Collection added Successfully', function() {
        test.pass('Donation Added successful.')},
        function() {
        test.fail('Donation Adding Failed '); } , 2000);

});
    
  

casper.run(function() {
    this.echo('Test Successful - Add DOnation Record', 'INFO');
       test.done();
   });
});


