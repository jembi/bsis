

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
///////////////////////////  TEST 3 - View Donor  page  //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

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
     casper.waitForText('Find Donors', function success(){
        test.pass('Donor Search -- Donors Page Loaded Successfully') },function timeout(){
          test.fail('Donor Search -- Donors page timeout')},TIMEOUT);

   });

    casper.run(function() {
    this.echo('Test Successful - View Donora Page', 'INFO');
       test.done();
   });

  });

////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Donor Search Test   //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin('Search For  Donor Record',7,function(test){



  
 casper.then(function(){  


     test.assertExists(DONOR_NUMBER_ID);
     test.assertExists(DONOR_FIRST_NAME_ID);     
     test.assertExists(DONOR_LAST_NAME_ID);
     test.assertExists(DONOR_ANY_BLOOD_GROUP_ID);  
     test.assertExists(DONOR_DUE_TO_DONATE_ID);
     test.assertExists(DONOR_FIND_BUTTON_CLASS);


    });
 


 // casper.then(function(){

 //      this.fill('form.formFormatClass', {  
 //           donorNumber : DONOR_NUMBER,
 //           firstName   : DONOR_FIRST_NAME,
 //      });

 //    });

 casper.then(function(){

    casper.click(DONOR_FIND_BUTTON_CLASS);
        casper.waitForText('print', function success(){
        test.pass('Donor Search -- Donor Results  Loaded Successfully') },function timeout(){
          test.fail('Donor Search --Donors Reslults  timeout')},TIMEOUT);
   
    });

   casper.run(function() {
    this.echo('Test Successful - Search Donor Record.', 'INFO');
       test.done();
   });

});


////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////  TEST 3 - Update  Donor  Test   //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

casper.test.begin(' Update Donor Record',5,function(test){

  casper.then(function(){

     test.assertExists(tabSelector);
     casper.click(tabSelector);
     casper.waitForText('Find Donors', function success(){
        test.pass('Edit Donor -- Donors Page Loaded Successfully') },function timeout(){
          test.fail('Edit Donor -- Donors page timeout')},TIMEOUT);

     casper.click(DONOR_FIND_BUTTON_CLASS);
        casper.waitForText('print', function success(){
        test.pass('Edit Donor  -- Donor Results  Loaded Successfully') },function timeout(){
          test.fail('Edit Donor  --Donors Reslults  timeout')},TIMEOUT);
   
 
    });

casper.then(function(){

      test.assertExists(DONOR_TABLE_CLASS);
      casper.click(DONOR_TABLE_CLASS);
      
  casper.waitForSelector(DONOR_EDIT_CLASS,

  function success(){
    test.pass('Update Donor - Donors Summary page Successfully') 
  },

  function fail(){
      test.fail('Update Donor - Donors Summary page  Loading Failed')
    },TIMEOUT 
 );


});


casper.then(function(){

 casper.click(DONOR_EDIT_CLASS);
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
    test.pass('Donor Edited Successfully') 
  },

  function fail(){
      test.fail('Donor Editing  Failed')
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

casper.test.begin(' Delete  Donor Record',5,function(test){

casper.then(function(){

     test.assertExists(tabSelector);
     casper.click(tabSelector);
     casper.waitForText('Find Donors', function success(){
        test.pass('Donors Page Loaded Successfully') },function timeout(){
          test.fail('Donors page timeout')},TIMEOUT);
 
    });

 casper.then(function(){

    casper.click(DONOR_FIND_BUTTON_CLASS);
        casper.waitForSelector(DONOR_TABLE_CLASS, function success(){
        test.pass('Delete Donor -- Donors Page Loaded Successfully') },function timeout(){
          test.fail('Delete Donor -- Donors page Loading timeout')},TIMEOUT);

    casper.click(DONOR_TABLE_CLASS);
      casper.waitForSelector(DONOR_DELETE_CLASS,

            function success(){
                test.pass('Delete Donor -- Donors Summary page Successfully') 
              },

            function fail(){
                  test.fail('Delete Donor -- Donors Summary page  Loading Failed')
            },TIMEOUT 
          );
   
    });


casper.then(function(){

      test.assertExists(DONOR_DELETE_CLASS);
      casper.click(DONOR_DELETE_CLASS);



});

casper.run(function() {
    this.echo('Test Successful - Delete Donor Record.', 'INFO');
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
  test.assertExists(DONOR_GENDER_ID);
  test.assertExists(DONOR_ADD_BUTTON_CLASS);



});

 casper.then(function(){

      this.fill('form.formFormatClass', {  
           donorNumber : DONOR_NUMBER,
           firstName   : DONOR_FIRST_NAME,
           year        : DONOR_YEAR,
           month       : DONOR_MONTH,
           dayOfMonth  : DONOR_DAY_OF_MONTH,
          
      });

    });

casper.then(function(){

    casper.click(DONOR_ADD_BUTTON_CLASS);

 });

 casper.then(function(){

  casper.waitForText(' Donor added Successfully',

  function success(){
    test.pass('Donor Added Successfully') 
  },

  function fail(){
      test.fail('Donor Adding Failed')
    },TIMEOUT 
 );
});



casper.run(function() {
    this.echo('Test Successful - Search Donor Record.', 'INFO');
       test.done();
   });

});










