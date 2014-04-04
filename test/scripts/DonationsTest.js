

  
/////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST  1 --> Login Test into BSIS  //////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

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


////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Find Donation Record  //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Find Donations Record',8,function(test){
  casper.start('http://localhost:8080/bsis/welcomePage.html').viewport(1000,1000);

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

        casper.click(tabSelector);
       casper.waitForSelector('.formFormatClass', function success(){
        test.pass('Donors Page Loaded Successfully') },function timeout(){
          test.fail('Donors page timeout')},TIMEOUT);

       casper.wait('5000');
   

});

casper.then(function(){

  test.assertExists('#collectionNumber');
  test.assertExists('.collectionCenterSelector');
  test.assertExists('.dateCollectedTo');
  test.assertExists('.collectionSiteSelector');
  test.assertExists('.bloodBagTypeSelector');
  test.assertExists('.dateCollectedFrom');
  test.assertExists('.findCollectionButton');

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

casper.test.begin('Add Donations Record',14,function(test){

 casper.then(function(){

casper.click('#addCollectionsContent a');
  casper.waitForSelector('#donorWeight', function success(){
        test.pass('Donors Page Loaded Successfully') },function timeout(){
          test.fail('Donors page timeout')},TIMEOUT);

       
 });

casper.then(function(){
//Asserting Input fields
test.assertExists('#collectionNumber');
test.assertExists('#collectionBatchNumber');
test.assertExists('#donorNumber');
test.assertExists('#collectedOn');
test.assertExists('.addCollectionFormDonationType');
 // test.assertExists('#bloodBagType');
test.assertExists('.addCollectionFormCenters');
test.assertExists('#donorWeight');
test.assertExists('#donorPulse');
test.assertExists('#bloodPressureSystolic');
test.assertExists('#bloodPressureDiastolic');
test.assertExists('#notes');

// Asserting buttons
test.assertExists('.addCollectionButton');
test.assertExists('.clearFormButton');

});

casper.run(function() {
    this.echo('Test Successful - Add DOnation Record', 'INFO');
       test.done();
   });
});


////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 4- Add  Donation Batch  //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Add Donations Record',6,function(test){

 casper.then(function(){

casper.click('#findCollectionBatchContent a');
  casper.waitForText('Find Collection Batches', function success(){
        test.pass('Find Collection Batches Page Loaded Successfully') },function timeout(){
          test.fail('Find Collection Batches page timeout')},TIMEOUT);

       
 });

casper.then(function(){

//Asserting fields
test.assertExists('#batchNumber');
test.assertExists('.collectionCenterSelector');
test.assertExists('.collectionSiteSelector');

//asserting buttons
test.assertExists('.findCollectionBatchButton');
test.assertExists('.clearFindFormButton ');

});

casper.run(function() {
    this.echo('Test Successful -  Find Donation Batche', 'INFO');
       test.done();
   });
});

////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 5- Add  Donation Batch  //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('New Donations Record',6,function(test){

 casper.then(function(){

casper.click('#createCollectionBatch a');
  casper.waitForSelector('.addCollectionBatchFormCenters', function success(){
        test.pass('New Collection Batches Page Loaded Successfully') },function timeout(){
          test.fail('New Collection Batches page timeout')},TIMEOUT);

       
 });

casper.then(function(){

//Asserting fields
test.assertExists('.addCollectionBatchFormCenters');
test.assertExists('.addCollectionBatchFormSites');
test.assertExists('#notes');

//asserting buttons
test.assertExists('.addCollectionBatchButton');
test.assertExists('.clearFormButton');

});

casper.run(function() {
    this.echo('Test Successful -  New Donation Batche', 'INFO');
       test.done();
   });
});





