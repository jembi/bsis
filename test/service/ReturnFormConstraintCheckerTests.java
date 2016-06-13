package service;

import static helpers.builders.ReturnFormBuilder.aReturnForm;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.mockito.InjectMocks;

import model.returnform.ReturnForm;
import model.returnform.ReturnStatus;
import suites.UnitTestSuite;

public class ReturnFormConstraintCheckerTests extends UnitTestSuite {
  
  @InjectMocks
  private ReturnFormConstraintChecker returnFormConstraintChecker;
  
  @Test
  public void testCanEditWithCreatedReturnForm_shouldReturnTrue() {
    // Set up
    ReturnForm returnForm = aReturnForm().withReturnStatus(ReturnStatus.CREATED).build();
    
    // Test
    boolean canEdit = returnFormConstraintChecker.canEdit(returnForm);
    
    // Verify
    assertThat(canEdit, is(true));
  }
  
  @Test
  public void testCanEditWithReturnedReturnForm_shouldReturnFalse() {
    // Set up
    ReturnForm returnForm = aReturnForm().withReturnStatus(ReturnStatus.RETURNED).build();
    
    // Test
    boolean canEdit = returnFormConstraintChecker.canEdit(returnForm);
    
    // Verify
    assertThat(canEdit, is(false));
  }

}
