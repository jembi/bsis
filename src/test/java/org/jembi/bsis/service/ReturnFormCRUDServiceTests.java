package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aDistributionSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aUsageSite;
import static org.jembi.bsis.helpers.builders.ReturnFormBuilder.aReturnForm;
import static org.jembi.bsis.helpers.matchers.ReturnFormMatcher.hasSameStateAsReturnForm;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.helpers.builders.ReturnFormBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.returnform.ReturnForm;
import org.jembi.bsis.model.returnform.ReturnStatus;
import org.jembi.bsis.repository.ReturnFormRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ReturnFormCRUDServiceTests extends UnitTestSuite {
  
  @InjectMocks
  private ReturnFormCRUDService returnFormCRUDService;
  
  @Mock
  private ReturnFormRepository returnFormRepository;

  @Mock
  private ComponentCRUDService componentCRUDService;
  
  @Mock
  private ReturnFormConstraintChecker returnFormConstraintChecker;

  @Test
  public void testCreateReturnForm_shouldPersist() {
    // Set up
    Location returnedFrom = LocationBuilder.aUsageSite().withId(UUID.randomUUID()).build();
    Location returnedTo = LocationBuilder.aDistributionSite().withId(UUID.randomUUID()).build();
    Date returnDate = new Date();

    ReturnForm returnForm = ReturnFormBuilder.aReturnForm()
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withReturnDate(returnDate)
        .withReturnStatus(null)
        .build();
    
    ReturnForm returnFormToPersist = returnForm;
    returnFormToPersist.setStatus(ReturnStatus.CREATED);
    
    // Run test
    returnFormCRUDService.createReturnForm(returnForm);

    // Verify
    verify(returnFormRepository).save(returnFormToPersist);
    verifyZeroInteractions(componentCRUDService);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testUpdateReturnFormWhenCannotEdit_shouldThrow() {
    UUID id = UUID.randomUUID();
    // Set up data
    ReturnForm existingReturnForm = aReturnForm().withId(id).withReturnStatus(ReturnStatus.RETURNED).build();
    ReturnForm updatedReturnForm = aReturnForm().withId(id).withReturnStatus(ReturnStatus.RETURNED).build();
    
    // Set up mocks
    when(returnFormRepository.findById(id)).thenReturn(existingReturnForm);
    when(returnFormConstraintChecker.canEdit(argThat(hasSameStateAsReturnForm(existingReturnForm)))).thenReturn(false);
    
    // Run test
    returnFormCRUDService.updateReturnForm(updatedReturnForm);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testUpdateReturnFormWhenCannotReturn_shouldThrow() {
    UUID id = UUID.randomUUID();
    // Set up data
    ReturnForm existingReturnForm = aReturnForm().withId(id).withReturnStatus(ReturnStatus.CREATED).build();
    ReturnForm updatedReturnForm = aReturnForm()
        .withId(id)
        .withReturnStatus(ReturnStatus.RETURNED)
        .withComponents(Collections.<Component>emptyList())
        .build();
    
    // Set up mocks
    when(returnFormRepository.findById(id)).thenReturn(existingReturnForm);
    when(returnFormConstraintChecker.canEdit(argThat(hasSameStateAsReturnForm(existingReturnForm)))).thenReturn(true);
    when(returnFormConstraintChecker.canReturn(argThat(hasSameStateAsReturnForm(existingReturnForm)))).thenReturn(false);
    
    // Run test
    returnFormCRUDService.updateReturnForm(updatedReturnForm);
  }
  
  @Test
  public void testAddComponentsToReturnForm_shouldUpdateFieldsCorrectly() {
    // Setup data
    Location returnedFrom = aDistributionSite().build();
    Location returnedTo = aUsageSite().build();
    Component component = aComponent().build();
    UUID id = UUID.randomUUID();
    
    ReturnForm existingReturnForm = ReturnFormBuilder.aReturnForm()
        .withId(id)
        .withReturnStatus(ReturnStatus.CREATED)
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .build();
    ReturnForm updatedReturnForm = ReturnFormBuilder.aReturnForm()
        .withId(id)
        .withReturnStatus(ReturnStatus.CREATED)
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withComponent(component)
        .build();

    // Setup mocks
    when(returnFormRepository.findById(id)).thenReturn(existingReturnForm);
    when(returnFormConstraintChecker.canEdit(argThat(hasSameStateAsReturnForm(existingReturnForm)))).thenReturn(true);
    when(returnFormRepository.update(argThat(hasSameStateAsReturnForm(updatedReturnForm)))).thenReturn(updatedReturnForm);
    
    // Run test
    ReturnForm mergedReturnForm = returnFormCRUDService.updateReturnForm(updatedReturnForm);
    
    // Verify
    assertThat(mergedReturnForm, is(updatedReturnForm));
    verifyZeroInteractions(componentCRUDService);
    verify(returnFormRepository).update(updatedReturnForm);
  }
  
  @Test
  public void testUpdateReturnFormToReturned_shouldUpdateFieldsCorrectly() {
    // Setup data
    Location returnedFrom = aDistributionSite().build();
    Location returnedTo = aUsageSite().build();
    Component component = aComponent().build();
    UUID id = UUID.randomUUID();
    
    ReturnForm existingReturnForm = ReturnFormBuilder.aReturnForm()
        .withId(id)
        .withReturnStatus(ReturnStatus.CREATED)
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .build();
    ReturnForm updatedReturnForm = ReturnFormBuilder.aReturnForm()
        .withId(id)
        .withReturnStatus(ReturnStatus.RETURNED)
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withComponent(component)
        .build();

    // Setup mocks
    when(returnFormRepository.findById(id)).thenReturn(existingReturnForm);
    when(returnFormConstraintChecker.canEdit(argThat(hasSameStateAsReturnForm(existingReturnForm)))).thenReturn(true);
    when(returnFormConstraintChecker.canReturn(argThat(hasSameStateAsReturnForm(existingReturnForm)))).thenReturn(true);
    when(returnFormRepository.update(argThat(hasSameStateAsReturnForm(updatedReturnForm)))).thenReturn(updatedReturnForm);
    
    // Run test
    ReturnForm mergedReturnForm = returnFormCRUDService.updateReturnForm(updatedReturnForm);
    
    // Verify
    assertThat(mergedReturnForm, is(updatedReturnForm));
    verify(componentCRUDService).returnComponent(component, returnedTo);
    verify(returnFormRepository).update(updatedReturnForm);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testDeleteReturnFormThatCannotBeDeleted_shouldThrow() {
    // Set up fixture
    UUID returnFormId = UUID.randomUUID();
    ReturnForm existingReturnForm = aReturnForm().withId(returnFormId).build();
    
    // Set up expectations
    when(returnFormRepository.findById(returnFormId)).thenReturn(existingReturnForm);
    when(returnFormConstraintChecker.canDelete(argThat(hasSameStateAsReturnForm(existingReturnForm)))).thenReturn(false);
    
    // Exercise SUT
    returnFormCRUDService.deleteReturnForm(returnFormId);
  }
  
  @Test
  public void testDeleteReturnForm_shouldUpdateReturnFormStatusToDeleted() {
    // Set up fixture
    UUID returnFormId = UUID.randomUUID();
    Location returnedFrom = aDistributionSite().withId(UUID.randomUUID()).build();
    Location returnedTo = aUsageSite().withId(UUID.randomUUID()).build();
    Date returnDate = new Date();

    ReturnForm existingReturnForm = aReturnForm()
        .withId(returnFormId)
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withReturnDate(returnDate)
        .build();
    ReturnForm expectedReturnForm = aReturnForm()
        .withId(returnFormId)
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withReturnDate(returnDate)
        .withIsDeleted(true)
        .build();
    
    // Set up expectations
    when(returnFormRepository.findById(returnFormId)).thenReturn(existingReturnForm);
    when(returnFormConstraintChecker.canDelete(argThat(hasSameStateAsReturnForm(existingReturnForm)))).thenReturn(true);
    
    // Exercise SUT
    returnFormCRUDService.deleteReturnForm(returnFormId);
    
    // Verify
    verify(returnFormRepository).update(argThat(hasSameStateAsReturnForm(expectedReturnForm)));
  }

}