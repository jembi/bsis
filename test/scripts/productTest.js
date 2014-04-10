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
///////////////////////////  TEST 2 --> View Products  page  //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('View Products Page',2,function(test){

casper.then(function(){
    tabSelector  =     this.evaluate(function(){

          for(var i=1;i<10;i++){
         var  selector =  "#topPanelTabs li:nth-child("+i+") a";
          if(document.querySelector(selector).textContent ==='Products'){
           return selector;
       }
    }
    return null;
 });


     test.assertExists(tabSelector);
     casper.click(tabSelector);
     casper.waitForText(COMPONENT_FIND_TEXT, function success(){
        test.pass('product page  --> Products Page Loaded Successfully') },function timeout(){
          test.fail('product page  --> Products page timeout')},TIMEOUT);

   });

    casper.run(function() {
    this.echo('Test Successful - View Products Page', 'INFO');
       test.done();
   });

  });


////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Record Product  Test   //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Record Product Test',8,function(test){

  casper.then(function(){
          
          casper.click(COMPONENT_RECORD_TAB_SELECTOR);
           casper.waitForText(COMPONENT_RECORD_TEXT, function success(){
             test.pass('Record Product --> Record Products  Page Loaded Successfully') },function timeout(){
                test.fail('Record Products --> Record Products page loading timeout')},TIMEOUT);


  });

casper.then(function(){

  test.assertExists(DONATION_NUMBER_ID);
  test.assertExists(COMPONENT_VIEW_BUTTON_CLASS);
  test.assertExists(COMPONENT_CLEAR_BUTTON_CLASS);
});


 casper.then(function(){

     casper.fill(COMPONENT_RECORD_FORM, {
      collectionNumber : '123'
 },false);

});

casper.then(function(){

     casper.click(COMPONENT_VIEW_BUTTON_CLASS);
     casper.waitForSelector(TABLE_CLASS, function success(){
        test.pass('Record Product --> Product Results  Loaded Successfully') },function timeout(){
          test.fail('Record Product --> Product Results  Loading  timeout')},TIMEOUT);

  });

 casper.then(function(){
   test.assertExists(TABLE_CLASS);
   require('utils').dump(this.getHTML('.ui-tabs-panel.ui-widget-content.ui-corner-bottom .dataTable.productsTable.DTTT_selectable tr:nth-child(1) td:nth-child(3)'));
    casper.click('.ui-tabs-panel.ui-widget-content.ui-corner-bottom .dataTable.productsTable.DTTT_selectable tr:nth-child(1) td:nth-child(1)');
     casper.waitForSelector(RECORD_NEW_COMPONENT_CLASS, function success(){
        test.pass('Record Product  --> Record Product form  Loaded Successfully') },function timeout(){
          test.fail('Record Product  --> Record Product form  Loading  timeout')},TIMEOUT);
  });

  casper.then(function(){

     casper.fill(COMPONENT_RECORD_FORM, {
     collectionNumber : '123'
    });
   });
casper.then(function(){

     casper.click(COMPONENT_FIND_BUTTON_CLASS);
     casper.waitForSelector(TABLE_CLASS, function success(){
        test.pass('Search Product --> Product Results  Loaded Successfully') },function timeout(){
          test.fail('Search Product --> Product Results  Loading  timeout')},TIMEOUT);

  });


casper.run(function() {
    this.echo('Test Successful - Record Product.', 'INFO');
       test.done();
   });

});


////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Search Product    //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Search Product Test',16,function(test){

  casper.then(function(){
          
          casper.click(COMPONENT_ADD_TAB_SELECTOR);
           casper.waitForText(COMPONENT_RECORD_TEXT, function success(){
             test.pass('Search Product --> Find Products  Page Loaded Successfully') },function timeout(){
                test.fail('Search Products --> FInd Products page loading timeout')},TIMEOUT);


  });

casper.then(function(){

   test.assertExists(DONATION_NUMBER_ID);
   test.assertExists(COMPONENT_SEARCH_BY_CLASS);
   test.assertExists(COMPONENT_STATUS_CLASS);
   test.assertExists(COMPONENT_FIND_BUTTON_CLASS);
   test.assertExists(COMPONENT_CLEAR_BUTTON_CLASS);
});


 casper.then(function(){

     casper.fill(COMPONENT_FIND_FORM, {
     collectionNumber : '123'
 });

});

casper.then(function(){

     casper.click(COMPONENT_FIND_BUTTON_CLASS);
     casper.waitForSelector(TABLE_CLASS, function success(){
        test.pass('Search Product --> Product Results  Loaded Successfully') },function timeout(){
          test.fail('Search Product --> Product Results  Loading  timeout')},TIMEOUT);

  });

 casper.then(function(){
  require('utils').dump(this.getHTML('.ui-tabs-panel.ui-widget-content.ui-corner-bottom .dataTable.productsTable.DTTT_selectable tr:nth-child(2) td:nth-child(3)'));
   test.assertExists('.ui-tabs-panel.ui-widget-content.ui-corner-bottom .dataTable.productsTable.DTTT_selectable .odd.DTTT_selected td:nth-child(3)');
    this.click('.ui-tabs-panel.ui-widget-content.ui-corner-bottom .dataTable.productsTable.DTTT_selectable .odd.DTTT_selected td:nth-child(3)');
    
    casper.waitForText(COMPONENT_DONE_BUTTON_CLASS, function success(){
       test.pass('Search Product  --> Product summary page Loaded Successfully') },function timeout(){
         test.fail('Search Product  --> Product summary page  Loading  timeout')},TIMEOUT);
  });


casper.then(function(){

   test.assertExists(COMPONENT_DONE_BUTTON_CLASS);
   test.assertExists(COMPONENT_EDIT_BUTTON_CLASS);
   test.assertExists(COMPONENT_DELETE_BUTTON_CLASS);
   test.assertExists(COMPONENT_PRINT_BUTTON_CLASS);
   test.assertExists(COMPONENT_DISCARD_BUTTON_CLASS);
   test.assertExists(COMPONENT_RETURN_BUTTON_CLASS);
   test.assertExists(COMPONENT_PRINT_BUTTON_CLASS);

});

casper.run(function() {
    this.echo('Test Successful - Search  Product .', 'INFO');
       test.done();
   });

});





