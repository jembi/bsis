package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ReturnFormBuilder.aReturnForm;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.UUID;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.returnform.ReturnForm;
import org.jembi.bsis.model.returnform.ReturnStatus;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ReturnFormConstraintCheckerTests extends UnitTestSuite {
  
  @InjectMocks
  private ReturnFormConstraintChecker returnFormConstraintChecker;
  @Mock
  private ComponentConstraintChecker componentConstraintChecker;
  
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
  
  @Test
  public void testCanDiscardWithCreatedReturnForm_shouldReturnFalse() {
    // Set up
    ReturnForm returnForm = aReturnForm()
        .withReturnStatus(ReturnStatus.CREATED)
        .withComponent(aComponent().build())
        .build();
    
    // Test
    boolean canDiscardComponents = returnFormConstraintChecker.canDiscard(returnForm);
    
    // Verify
    assertThat(canDiscardComponents, is(false));
  }
  
  @Test
  public void testCanDiscardWithReturnedReturnFormWithDiscardedComponents_shouldReturnFalse() {
    // Set up
    ReturnForm returnForm = aReturnForm()
        .withReturnStatus(ReturnStatus.RETURNED)
        .withComponent(aComponent().withStatus(ComponentStatus.DISCARDED).build())
        .withComponent(aComponent().withStatus(ComponentStatus.DISCARDED).build())
        .build();
    
    // Test
    boolean canDiscardComponents = returnFormConstraintChecker.canDiscard(returnForm);
    
    // Verify
    assertThat(canDiscardComponents, is(false));
  }
  
  @Test
  public void testCanDiscardWithReturnedReturnFormWithDiscardableComponents_shouldReturnTrue() {
    // Set up
    UUID componentId = UUID.randomUUID();
    Component componentThatCanBeDiscarded = aComponent().withId(componentId).withStatus(ComponentStatus.AVAILABLE).build();
    
    ReturnForm returnForm = aReturnForm()
        .withReturnStatus(ReturnStatus.RETURNED)
        .withComponent(aComponent().withId(componentId).withStatus(ComponentStatus.DISCARDED).build())
        .withComponent(componentThatCanBeDiscarded)
        .build();
    
    when(componentConstraintChecker.canDiscard(componentThatCanBeDiscarded)).thenReturn(true);
    
    // Test
    boolean canDiscardComponents = returnFormConstraintChecker.canDiscard(returnForm);
    
    // Verify
    assertThat(canDiscardComponents, is(true));
  }

  @Test
  public void testCanDeleteWithCreatedReturnForm_shouldReturnTrue() {
    // Set up
    ReturnForm returnForm = aReturnForm().withReturnStatus(ReturnStatus.CREATED).build();

    // Test
    boolean canDelete = returnFormConstraintChecker.canDelete(returnForm);

    // Verify
    assertThat(canDelete, is(true));
  }

  @Test
  public void testCanDeleteWithReturnedReturnForm_shouldReturnFalse() {
    // Set up
    ReturnForm returnForm = aReturnForm().withReturnStatus(ReturnStatus.RETURNED).build();

    // Test
    boolean canDelete = returnFormConstraintChecker.canDelete(returnForm);

    // Verify
    assertThat(canDelete, is(false));
  }

}
