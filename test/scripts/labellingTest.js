var tabSelector ;
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
        test.fail('Login failed'); } , TIMEOUT);
       
    }).viewport(1000, 1000);

casper.run(function() {
    this.echo('Test Successful - Login To BSIS', 'INFO');
       test.done();
   });

});


////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 2 --> Labelling  page  //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Lot Release  Page',5,function(test){

casper.then(function(){
    tabSelector  =     this.evaluate(function(){

          for(var i=1;i<10;i++){
         var  selector =  "#topPanelTabs li:nth-child("+i+") a";
          if(document.querySelector(selector).textContent ==='Lot Release'){
           return selector;
       }
    }
    return null;
 });


     test.assertExists(tabSelector);
     casper.click(tabSelector);
     casper.waitForSelector(CHECK_RESULTS_BUTTON_CLASS, function success(){
        test.pass('Lot Release  page  --> Lot Release Page Loaded Successfully') },function timeout(){
          test.fail('Lot Release page  --> Lot Release page Load timeout')},TIMEOUT);

   });

     casper.then(function(){
             test.assertExists(CLEAR_FORM_BUTTON);
             test.assertExists(CHECK_RESULTS_BUTTON_CLASS);

     });

  casper.then(function(){


   this.sendKeys("input#dinNumber", "1234");
   casper.click(CHECK_RESULTS_BUTTON_CLASS);
    casper.waitForSelector(PRINT_DICARD_BUTTON_CLASS, function success(){
        test.pass('Lot Release  page  --> Results  Successfully') },function timeout(){
          test.fail('Lot Release page  --> Results  Loading timeout')},TIMEOUT);


    });


    casper.run(function() {
    this.echo('Test Successful - Labelling Page', 'INFO');
       test.done();
   });

  });
