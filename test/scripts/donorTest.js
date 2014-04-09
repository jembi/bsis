

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
        test.fail('Login failed'); } , 2000);
       
    }).viewport(1000, 1000);

casper.run(function() {
    this.echo('Test Successful - Login To BSIS', 'INFO');
       test.done();
   });

});


////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - View Donor  page  ////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('View Donors Page',2,function(test){

casper.then(function(){
    tabSelector  =     this.evaluate(function(){

          for(var i=1;i<10;i++){
         var  selector =  "#topPanelTabs li:nth-child("+i+") a";
          if(document.querySelector(selector).textContent ==='Donors'){
           return selector;
       }
    }
    return null;
 });


     test.assertExists(tabSelector);
     casper.click(tabSelector);
     casper.waitForText(DONOR_FIND_TEXT, function success(){
        test.pass('Donor Search -- Donors Page Loaded Successfully') },function timeout(){
          test.fail('Donor Search -- Donors page timeout')},TIMEOUT);

   });

    casper.run(function() {
    this.echo('Test Successful - View Donora Page', 'INFO');
       test.done();
   });

  });

////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Add Donor  Test   //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Add   Donor Record',2,function(test){

casper.then(function(){

  test.assertExists(DONOR_FIRST_NAME_ID);
  test.assertExists(DONOR_LAST_NAME_ID);
  test.assertExists(DONOR_DAY_OF_MONTH_ID);
  test.assertExists(DONOR_YEAR_ID); 
  test.assertExists(DONOR_MONTH_ID);
  test.assertExists(DONOR_GENDER_ID); //unable to locate -- ID is dynamic and no class specified
  test.assertExists(DONOR_ADD_BUTTON_CLASS);



});

 casper.then(function(){

      this.fill(DONOR_ADD_FORM, {  
           firstName   : DONOR_FIRST_NAME,
           lastName    : DONOR_LAST_NAME,
           gender      : DONOR_GENDER,
           year        : DONOR_YEAR,
           month       : DONOR_MONTH,
           dayOfMonth  : DONOR_DAY_OF_MONTH,
          
      });

    });

casper.then(function(){

    casper.click(DONOR_ADD_BUTTON_CLASS);

 });

 casper.then(function(){

  casper.waitForSelector(DONOR_ADD_ANOTHER_BUTTON_CLASS,

  function success(){
     casper.click(DONOR_DONE_BUTTON_CLASS);
       casper.waitForText(DONOR_FIND_TEXT, function success(){
        test.pass('Add Donor -- Donors Page Loaded Successfully') },function timeout(){
          test.fail('Add Donor -- Donors page timeout')},TIMEOUT);
    test.pass('Add Donor -- Donor Added Successfully') 
  },

  function fail(){
     casper.click(DONOR_CLEAR_BUTTON_CLASS);
       casper.waitForText(DONOR_FIND_TEXT, function success(){
        test.pass('Add Donor -- Donors Page Loaded Successfully') },function timeout(){
          test.fail('Add Donor -- Donors page timeout')},TIMEOUT);

      test.fail('Donor Adding Failed');
     

    },TIMEOUT 
 );
});


casper.run(function() {
    this.echo('Test Successful - Add Donor Record.', 'INFO');
       test.done();
   });

});




////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Search Donor Test   //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Search For  Donor Record',18,function(test){

   casper.then(function(){

     test.assertExists(tabSelector);
     casper.click(tabSelector);
     casper.waitForText(DONOR_FIND_TEXT, function success(){
        test.pass('Search Donor -- Donors Page Loaded Successfully') },function timeout(){
          test.fail('Search Donor -- Donors page timeout')},TIMEOUT);
   });


 casper.then(function(){  


     test.assertExists(DONOR_NUMBER_ID);
     test.assertExists(DONOR_FIRST_NAME_ID);     
     test.assertExists(DONOR_LAST_NAME_ID);
     test.assertExists(DONOR_ANY_BLOOD_GROUP_ID);  
     test.assertExists(DONOR_DUE_TO_DONATE_ID);
     test.assertExists(DONOR_FIND_BUTTON_CLASS);


    });

 casper.then(function(){
        casper.click(DONOR_FIND_BUTTON_CLASS);
        casper.waitForSelector(TABLE_CLASS, function success(){
        test.pass('Search Donor  -- Donor Results  Loaded Successfully') },function timeout(){
          test.fail('Search Donor  --Donors Reslults  timeout')},TIMEOUT);
   
 
    });


casper.then(function(){

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

});

casper.then(function(){  


     test.assertExists(DONOR_EDIT_BUTTON_CLASS);
     test.assertExists(DONOR_HISTORY_BUTTON_CLASS);     
     test.assertExists(DONOR_DELETE_BUTTON_CLASS);
     test.assertExists(DONOR_DEFER_BUTTON_CLASS);  
     test.assertExists(DONOR_SHOW_DEFERRALS_BUTTON_CLASS);
     test.assertExists(DONOR_PRINT_BARCODE_BUTTON_CLASS);
     test.assertExists(DONOR_DONE_BUTTON_CLASS);



    });


   casper.run(function() {
    this.echo('Test Successful - Search Donor Record.', 'INFO');
       test.done();
   });

});


////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Edit  Donor  Test   //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin(' Edit Donor Record',8,function(test){

  casper.then(function(){

     test.assertExists(tabSelector);
     casper.click(tabSelector);
     casper.waitForText(DONOR_FIND_TEXT, function success(){
        test.pass('Edit Donor -- Donors Page Loaded Successfully') },function timeout(){
          test.fail('Edit Donor -- Donors page timeout')},TIMEOUT);

     casper.click(DONOR_FIND_BUTTON_CLASS);
        casper.waitForText(PRINT_TEXT, function success(){
        test.pass('Edit Donor  -- Donor Results  Loaded Successfully') },function timeout(){
          test.fail('Edit Donor  --Donors Reslults  timeout')},TIMEOUT);
   
 
    });

casper.then(function(){

      test.assertExists(TABLE_CLASS);
      casper.click(TABLE_CLASS);
      
  casper.waitForSelector(DONOR_EDIT_BUTTON_CLASS,

  function success(){
    test.pass('Edit Donor - Donors Summary page Successfully') 
  },

  function fail(){
      test.fail('Edit Donor - Donors Summary page  Loading Failed')
    },TIMEOUT 
 );


});


casper.then(function(){

 casper.click(DONOR_EDIT_BUTTON_CLASS);
 casper.waitForText('Edit donor',

  function success(){
    test.pass('Edit Donors page LoadedSuccessfully') 
  },

  function fail(){
      test.fail('Edit Donors Summary page  Loading Failed')
    },TIMEOUT 
 );
 

});


 casper.then(function(){



      test.assertExists(DONOR_SAVE_CLASS);
      casper.click(DONOR_SAVE_CLASS);
      
  casper.waitForSelector(DONOR_DONE_CLASS,

  function success(){
    test.pass('Edit Donor -- Donor Edited Successfully') 
  },

  function fail(){
      test.fail('Edit Donor -- Donor Editing  Failed')
    },TIMEOUT 
 );

  
});



casper.run(function() {
    this.echo('Test Successful - Update Donor Record.', 'INFO');
       test.done();
   });

});



////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Delete  Donor  Test   //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin(' Delete  Donor Record',6,function(test){

casper.then(function(){

     test.assertExists(tabSelector);
     casper.click(tabSelector);
     casper.waitForText(DONOR_FIND_TEXT, function success(){
        test.pass('Donors Page Loaded Successfully') },function timeout(){
          test.fail('Donors page timeout')},TIMEOUT);
 
    });

 casper.then(function(){

    casper.click(DONOR_FIND_BUTTON_CLASS);
        casper.waitForSelector(TABLE_CLASS, function success(){
        test.pass('Delete Donor -- Donors Page Loaded Successfully') },function timeout(){
          test.fail('Delete Donor -- Donors page Loading timeout')},TIMEOUT);

    casper.click(TABLE_CLASS);
      casper.waitForSelector(DONOR_DELETE_BUTTON_CLASS,

            function success(){
                test.pass('Delete Donor -- Donors Summary page Successfully') 
              },

            function fail(){
                  test.fail('Delete Donor -- Donors Summary page  Loading Failed')
            },TIMEOUT 
          );
   
    });


casper.then(function(){

      test.assertExists(DONOR_DELETE_BUTTON_CLASS)
        casper.click(DONOR_DELETE_BUTTON_CLASS);
   casper.setFilter(PAGE_CONFIRM, function() {
    return true;
  });

   casper.waitForText(DONOR_FIND_TEXT, function success(){
        test.pass('Delete Donor -- Donor Deleted  Successfully') },function timeout(){
          test.fail('Delete Donor -- Failed in deleting donor')},TIMEOUT);

   

});


casper.run(function() {
    this.echo('Test Successful - Delete Donor Record.', 'INFO');
       test.done();
   });
});












