

/////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST  1 --> Login Test into BSIS  //////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

loginTest(USERNAME,PASSWORD);

////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Assert Donor  page  ////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////

assertTabExistsByName(DONOR_TAB,VIEW_DONOR_INFORMATION,DONOR_GENDER_NAME);

////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Add Donor  Test   //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Add   Donor Record',8,function(test){

casper.then(function(){
  
 if(ADD_DONOR) 
  test.assertExists(DONOR_FIRST_NAME_ID);
else
  test.assertDoesntExist(DONOR_FIRST_NAME_ID);

 if(ADD_DONOR) 
  test.assertExists(DONOR_LAST_NAME_ID);
else
  test.assertDoesntExist(DONOR_LAST_NAME_ID);

 if(ADD_DONOR) 
  test.assertExists(DONOR_DAY_OF_MONTH_ID);
else
  test.assertDoesntExist(DONOR_DAY_OF_MONTH_ID);


 if(ADD_DONOR) 
  test.assertExists(DONOR_YEAR_ID); 
else
  test.assertDoesntExist(DONOR_YEAR_ID); 

 if(ADD_DONOR) 
  test.assertExists(DONOR_MONTH_ID);
else
  test.assertDoesntExist(DONOR_MONTH_ID);

 if(ADD_DONOR) 
  test.assertExists(DONOR_GENDER_NAME);
else
  test.assertDoesntExist(DONOR_GENDER_NAME);

 if(ADD_DONOR) 
  test.assertExists(DONOR_ADD_BUTTON_CLASS);
else
  test.assertDoesntExist(DONOR_ADD_BUTTON_CLASS);



});



 casper.then(function(){

if(ADD_DONOR){
      this.fill(DONOR_ADD_FORM, {  
           firstName   : DONOR_FIRST_NAME,
           lastName    : DONOR_LAST_NAME,
           gender      : DONOR_GENDER,
           year        : DONOR_YEAR,
           month       : DONOR_MONTH,
           dayOfMonth  : DONOR_DAY_OF_MONTH,
          
      });

    casper.click(DONOR_ADD_BUTTON_CLASS);
    casper.waitForSelector(DONOR_ADD_ANOTHER_BUTTON_CLASS,

  function success(){
     casper.click(DONOR_DONE_BUTTON_CLASS);
       casper.waitForText(DONOR_FIND_TEXT, function success(){
        test.pass('Add Donor -- Donors Page Loaded Successfully') },function timeout(){
          test.fail('Add Donor -- Donors page timeout')},TIMEOUT);
    
  },

  function fail(){
     casper.click(DONOR_CLEAR_BUTTON_CLASS);
       casper.waitForText(DONOR_FIND_TEXT, function success(){
        test.pass('Add Donor -- Donors Page Loaded Successfully') },function timeout(){
          test.fail('Add Donor -- Donors page timeout')},TIMEOUT);

    },TIMEOUT 
 );
}else
test.pass('Add Donor Test -- > No Permission To Add Donor')

});



casper.run(function() {
    this.echo('Test Successful - Add Donor Record.', 'INFO');
       test.done();
   });

});




////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Search Donor Test   //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Search For  Donor Record',16,function(test){


 casper.then(function(){  

  if(VIEW_DONOR)
     test.assertExists(DONOR_NUMBER_ID);
   else
     test.assertDoesntExist(DONOR_NUMBER_ID);

  if(VIEW_DONOR)
     test.assertExists(DONOR_FIRST_NAME_ID);  
    else
     test.assertDoesntExist(DONOR_FIRST_NAME_ID);  

  if(VIEW_DONOR)
     test.assertExists(DONOR_LAST_NAME_ID);
   else
     test.assertDoesntExist(DONOR_FIRST_NAME_ID);     

   if(VIEW_DONOR)
     test.assertExists(DONOR_ANY_BLOOD_GROUP_ID);  
   else
     test.assertDoesntExist(DONOR_FIRST_NAME_ID);     

   if(VIEW_DONOR)
     test.assertExists(DONOR_DUE_TO_DONATE_ID);
   else
     test.assertDoesntExist(DONOR_FIRST_NAME_ID);     

   if(VIEW_DONOR)
     test.assertExists(DONOR_FIND_BUTTON_CLASS);
   else
     test.assertDoesntExist(DONOR_FIRST_NAME_ID);     



    });



 casper.then(function(){
  if(VIEW_DONOR){
       casper.click(DONOR_FIND_BUTTON_CLASS);
        casper.waitForSelector(TABLE_CLASS, function success(){
        test.pass('Search Donor  -- Donor Results  Loaded Successfully') },function timeout(){
          test.fail('Search Donor  --Donors Reslults  timeout')},TIMEOUT);
   }else
     test.pass('Search Donor  Test  -- No Permission To View Donor Results')
 
    });


casper.then(function(){

    if(VIEW_DONOR){
      test.assertExists(TABLE_CLASS);
      casper.click(TABLE_CLASS);
        casper.waitForSelector(DONOR_DONE_BUTTON_CLASS,

  function success(){
    test.pass('Edit Donor - Donors Summary page Successfully') 
  },

  function fail(){
      test.fail('Edit Donor - Donors Summary page  Loading Failed')
    },TIMEOUT 
 );
}else
     test.pass('Search Donor  Test  -- No Permission To View Donor Summary ')

});



//Donor summary page Asssertions
casper.then(function(){  

  if(EDIT_DONOR)
     test.assertExists(DONOR_EDIT_BUTTON_CLASS);
   else
     test.assertDoesntExist(DONOR_EDIT_BUTTON_CLASS);

  if(VIEW_DONOR)
     test.assertExists(DONOR_HISTORY_BUTTON_CLASS);  
   else
     test.assertDoesntExist(DONOR_HISTORY_BUTTON_CLASS);  

  if(VOID_DONOR)   
     test.assertExists(DONOR_DELETE_BUTTON_CLASS);
   else
     test.assertDoesntExist(DONOR_DELETE_BUTTON_CLASS);

  if(ADD_DEFERRAL)
     test.assertExists(DONOR_DEFER_BUTTON_CLASS); 
   else
     test.assertDoesntExist(DONOR_DEFER_BUTTON_CLASS); 

  if(VIEW_DEFERRAL) 
     test.assertExists(DONOR_SHOW_DEFERRALS_BUTTON_CLASS);
   else
     test.assertDoesntExist(DONOR_SHOW_DEFERRALS_BUTTON_CLASS);

  if(VIEW_DONOR)
     test.assertExists(DONOR_PRINT_BARCODE_BUTTON_CLASS);
   else
     test.assertDoesntExist(DONOR_PRINT_BARCODE_BUTTON_CLASS);

  if(VIEW_DONOR)
     test.assertExists(DONOR_DONE_BUTTON_CLASS);
   else
     test.assertDoesntExist(DONOR_DONE_BUTTON_CLASS);


    });


   casper.run(function() {
    this.echo('Test Successful - Search Donor Record.', 'INFO');
       test.done();
   });

});


////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3- Edit  Donor  Test   //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin(' Edit Donor Record',3,function(test){

casper.then(function(){

if(EDIT_DONOR){
 casper.click(DONOR_EDIT_BUTTON_CLASS);
 casper.waitForSelector(DONOR_SAVE_CLASS,

  function success(){
    test.pass('Edit Donor Test --> Edit Donors page Loaded Successfully');
  },

  function fail(){
      test.fail('Edit Donor Test --> Edit Donors Summary page  Loading Failed')
    },TIMEOUT );
 }else
    test.pass('Edit Donor Test --> Edit Donors page Loaded Successfully');

});


 casper.then(function(){
if(EDIT_DONOR){
   test.assertExists(DONOR_SAVE_CLASS);
   casper.click(DONOR_SAVE_CLASS);
      casper.waitForSelector(DONOR_DONE_CLASS,

  function success(){
    test.pass('Edit Donor --> Donor Edited Successfully') 
  },

  function fail(){
      test.fail('Edit Donor --> Donor Editing  Failed')
    },TIMEOUT 
 );
}
else
    test.pass('Edit Donor Test --> Edit Donor');

  
});



casper.run(function() {
    this.echo('Test Successful - Update Donor Record.', 'INFO');
       test.done();
   });

});



////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Delete  Donor  Test   //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin(' Delete  Donor Record',2,function(test){


casper.then(function(){

if(VOID_DONOR)
 test.assertExists(DONOR_DELETE_BUTTON_CLASS);
else
test.assertDoesntExist(DONOR_DELETE_BUTTON_CLASS);

  if(VOID_DONOR){
 casper.click(DONOR_DELETE_BUTTON_CLASS);
     casper.setFilter(PAGE_CONFIRM, function() {
     return true;
  });

   casper.waitForText(DONOR_FIND_TEXT, function success(){
        test.pass('Delete Donor -- Donor Deleted  Successfully') },function timeout(){
          test.fail('Delete Donor -- Failed  to delete Donor')},TIMEOUT);
   }
else
    test.pass('Delete Donor Test --> No Permission To Delete Donor');

});


casper.run(function() {
    this.echo('Test Successful - Delete Donor Record.', 'INFO');
       test.done();
   });
});












