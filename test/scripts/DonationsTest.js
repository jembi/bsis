

  
/////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST  1 --> Login Test into BSIS  //////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
casper.options.waitTimeout = {width: '1600', height: '1600'};
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
///////////////////////////  TEST 3 - Add Donation Record  //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////




casper.test.begin('Add  Donation Record',9,function(test){
  casper.start('http://localhost:8080/bsis/welcomePage.html').viewport(1600,1000);

casper.then(function(){
  var tabSelector  =     this.evaluate(function(){

         for(var i=1;i<10;i++){
        var  selector =  "#topPanelTabs li:nth-child("+i+")";
         if(document.querySelector(selector).textContent ==='Donations'){
          return selector;
      }
   }
   return null;
});
  this.echo(tabSelector);
  casper.click(tabSelector);
});


casper.then(function(){
      casper.waitForSelector('.formDiv', function success(){
        test.pass('Donations Page Loaded Successfully') },function timeout(){
          test.fail('Donations page timeout')},TIMEOUT);
   
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
    this.echo('Test Successful - Add DOnation Record', 'INFO');
       test.done();
   });


});







