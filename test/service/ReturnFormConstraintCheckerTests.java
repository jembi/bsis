package service;

import static helpers.builders.ComponentBuilder.aComponent;
import static helpers.builders.ReturnFormBuilder.aReturnForm;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Collections;

import org.junit.Test;
import org.mockito.InjectMocks;

import model.component.Component;
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
  
  @Test
  public void testCanReturnWithReturnedReturnForm_shouldReturnFalse() {
    // Set up
    ReturnForm returnForm = aReturnForm()
        .withReturnStatus(ReturnStatus.RETURNED)
        .withComponent(aComponent().build())
        .build();
    
    // Test
    boolean canReturn = returnFormConstraintChecker.canReturn(returnForm);
    
    // Verify
    assertThat(canReturn, is(false));
  }
  
  @Test
  public void testCanReturnWithReturnFormWithoutComponents_shouldReturnFalse() {
    // Set up
    ReturnForm returnForm = aReturnForm()
        .withReturnStatus(ReturnStatus.CREATED)
        .withComponents(Collections.<Component>emptyList())
        .build();
    
    // Test
    boolean canReturn = returnFormConstraintChecker.canReturn(returnForm);
    
    // Verify
    assertThat(canReturn, is(false));
  }
  
  @Test
  public void testCanReturnWithCreatedReturnFormWithComponents_shouldReturnTrue() {
    // Set up
    ReturnForm returnForm = aReturnForm()
        .withReturnStatus(ReturnStatus.CREATED)
        .withComponent(aComponent().build())
        .build();
    
    // Test
    boolean canReturn = returnFormConstraintChecker.canReturn(returnForm);
    
    // Verify
    assertThat(canReturn, is(true));
  }

}
