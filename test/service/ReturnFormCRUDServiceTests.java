package service;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;

import helpers.builders.ReturnFormBuilder;
import model.returnform.ReturnForm;
import suites.UnitTestSuite;

public class ReturnFormCRUDServiceTests extends UnitTestSuite {
  
  private static final Long ORDER_FORM_ID = 17L;
  
  @InjectMocks
  private ReturnFormCRUDService returnFormCRUDService;
  
  @Test
  public void testCreateReturnForm_shouldPersist() {
    // Set up
    ReturnForm returnForm = ReturnFormBuilder.aReturnForm().withId(null).build();

    // Run test
    returnFormCRUDService.createReturnForm(returnForm);
    
    // Verify
    Assert.assertNotNull("Return form has been created", returnForm.getId());

  }
  


}
