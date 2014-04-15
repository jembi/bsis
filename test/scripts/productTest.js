
/////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST  1 --> Login Test into BSIS  //////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

loginTest(USERNAME,PASSWORD);


////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 2 --> View Components  page  //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

assertTabExistsByName(PRODUCT_TAB,VIEW_COMPONENT_INFORMATION, COMPONENT_RECORD_TAB_SELECTOR);

////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Record Component  Test   //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Record Component Test',8,function(test){

  casper.then(function(){
          
          casper.click(COMPONENT_RECORD_TAB_SELECTOR);
           casper.waitForText(COMPONENT_RECORD_TEXT, function success(){
             test.pass('Record Component --> Record Components  Page Loaded Successfully') },function timeout(){
                test.fail('Record Components --> Record Components page loading timeout')},TIMEOUT);


  });

casper.then(function(){

if(ADD_COMPONENT)
  test.assertExists(DONATION_NUMBER_ID);
else
  test.assertDoesntExist(DONATION_NUMBER_ID);

if(ADD_COMPONENT)
  test.assertExists(COMPONENT_VIEW_BUTTON_CLASS);
else
  test.assertDoesntExist(DONATION_NUMBER_ID);

if(ADD_COMPONENT)
  test.assertExists(COMPONENT_CLEAR_BUTTON_CLASS);
else
  test.assertDoesntExist(DONATION_NUMBER_ID);

});


 casper.then(function(){

// Need to improve logic on clicking products table 
 
 if(ADD_COMPONENT){
     casper.fill(COMPONENT_RECORD_FORM, {
      collectionNumber : DONATION_NUMBER
 },false);

     casper.click(COMPONENT_VIEW_BUTTON_CLASS);
     casper.waitForSelector(TABLE_CLASS, function success(){
        test.pass('Record Component --> Component Results  Loaded Successfully') },function timeout(){
          test.fail('Record Component --> Component Results  Loading  timeout')},TIMEOUT);

   test.assertExists(TABLE_CLASS);
    casper.click('.ui-tabs-panel.ui-widget-content.ui-corner-bottom .dataTable.productsTable.DTTT_selectable tr:nth-child(1) td:nth-child(1)');
     casper.waitForSelector(RECORD_NEW_COMPONENT_CLASS, function success(){
        test.pass('Record Component  --> Record Component form  Loaded Successfully') },function timeout(){
          test.fail('Record Component  --> Record Component form  Loading  timeout')},TIMEOUT);
   }else
      test.pass('Record Component --> No permission To View Record Component');
  });

  casper.then(function(){

 if(ADD_COMPONENT){

   casper.fill(COMPONENT_RECORD_FORM, {
     collectionNumber : DONATION_NUMBER
  });


     casper.click(COMPONENT_FIND_BUTTON_CLASS);
     casper.waitForSelector(TABLE_CLASS, function success(){
        test.pass('Record Component --> Component Results  Loaded Successfully') },function timeout(){
          test.fail('Record Component --> Component Results  Loading  timeout')},TIMEOUT);
   }else
      test.pass('Record Component --> No permission To View Component Results');


  });


casper.run(function() {
    this.echo('Test Successful - Record Component.', 'INFO');
       test.done();
   });



});


////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Search Component    //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Search Component Test',16,function(test){

  casper.then(function(){
        
      if(VIEW_COMPONENT){
          casper.click(COMPONENT_FIND_TAB_SELECTOR);
           casper.waitForText(COMPONENT_FIND_TEXT, function success(){
             test.pass('Search Component --> Find Components  Page Loaded Successfully') },function timeout(){
                test.fail('Search Components --> FInd Components page loading timeout')},TIMEOUT);
      }
      else
        test.pass('Search Component --> No permission To View Components Page') ;

  });

casper.then(function(){

  if(VIEW_COMPONENT)
   test.assertExists(DONATION_NUMBER_ID);
 else
   test.assertDoesntExist(DONATION_NUMBER_ID);

  if(VIEW_COMPONENT)
   test.assertExists(COMPONENT_SEARCH_BY_CLASS);
 else
   test.assertDoesntExist(COMPONENT_SEARCH_BY_CLASS);

  if(VIEW_COMPONENT)
   test.assertExists(COMPONENT_STATUS_CLASS);
 else
   test.assertDoesntExist(COMPONENT_STATUS_CLASS);

  if(VIEW_COMPONENT)
   test.assertExists(COMPONENT_FIND_BUTTON_CLASS);
 else
   test.assertDoesntExist(COMPONENT_FIND_BUTTON_CLASS);

  if(VIEW_COMPONENT)
   test.assertExists(COMPONENT_CLEAR_BUTTON_CLASS);
 else
   test.assertDoesntExist(COMPONENT_CLEAR_BUTTON_CLASS);



});


 casper.then(function(){

if(VIEW_COMPONENT){
     casper.fill(COMPONENT_FIND_FORM, {
     collectionNumber : DONATION_NUMBER
 });

     casper.click(COMPONENT_FIND_BUTTON_CLASS);
     casper.waitForSelector(TABLE_CLASS, function success(){
        test.pass('Search Component --> Component Results  Loaded Successfully') },function timeout(){
          test.fail('Search Component --> Component Results  Loading  timeout')},TIMEOUT);


   test.assertExists('.ui-tabs-panel.ui-widget-content.ui-corner-bottom .dataTable.productsTable.DTTT_selectable td:nth-child(3)');
    this.click('.ui-tabs-panel.ui-widget-content.ui-corner-bottom .dataTable.productsTable.DTTT_selectable td:nth-child(3)');
    
    casper.waitForSelector(COMPONENT_DONE_BUTTON_CLASS, function success(){
       test.pass('Search Component  --> Component summary page Loaded Successfully') },function timeout(){
         test.fail('Search Component  --> Component summary page  Loading  timeout')},TIMEOUT);
  }else
     test.pass('Search Component  --> No Permission To view Component Summary Page') ;

  });


casper.then(function(){

if(VIEW_COMPONENT)
   test.assertExists(COMPONENT_DONE_BUTTON_CLASS);
 else
   test.assertDoesntExist(COMPONENT_DONE_BUTTON_CLASS);

 if(EDIT_COMPONENT)
   test.assertExists(COMPONENT_EDIT_BUTTON_CLASS);
 else
   test.assertDoesntExist(COMPONENT_EDIT_BUTTON_CLASS);

 if(VOID_COMPONENT)
   test.assertExists(COMPONENT_DELETE_BUTTON_CLASS);
 else
   test.assertDoesntExist(COMPONENT_DELETE_BUTTON_CLASS);

 if(VIEW_COMPONENT)
   test.assertExists(COMPONENT_PRINT_BUTTON_CLASS);
 else
   test.assertDoesntExist(COMPONENT_PRINT_BUTTON_CLASS);

 if(DISCARD_COMPONENT)
   test.assertExists(COMPONENT_DISCARD_BUTTON_CLASS);
 else
   test.assertDoesntExist(COMPONENT_DISCARD_BUTTON_CLASS);

 if(VIEW_COMPONENT)
   test.assertExists(COMPONENT_RETURN_BUTTON_CLASS);
 else
   test.assertDoesntExist(COMPONENT_RETURN_BUTTON_CLASS);

 if(VIEW_COMPONENT)
   test.assertExists(COMPONENT_PRINT_BUTTON_CLASS);
 else
   test.assertDoesntExist(COMPONENT_RETURN_BUTTON_CLASS);


});

casper.run(function() {
    this.echo('Test Successful - Search  Component .', 'INFO');
       test.done();
   });

});



////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Delete Component   //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Delete Component Test',2,function(test){


casper.then(function(){

  if(VOID_COMPONENT){
   test.assertExists(COMPONENT_DELETE_BUTTON_CLASS);
     casper.click(COMPONENT_DELETE_BUTTON_CLASS);
       casper.setFilter(PAGE_CONFIRM, function() {
    return true;
  });
     casper.waitForText(COMPONENT_FIND_TEXT, function success(){
             test.pass('Search Component --> Find Components  Page Loaded Successfully') },function timeout(){
                test.fail('Delete Components --> FInd Components page loading timeout')},TIMEOUT);

  }else
  test.pass('Search Component Test--> No Permissin To View Components Page');

  });
      
 

casper.run(function() {
    this.echo('Test Successful - Delete  Component .', 'INFO');
       test.done();
   });

});



