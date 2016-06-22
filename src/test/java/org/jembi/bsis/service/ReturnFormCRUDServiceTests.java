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

import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.helpers.builders.ReturnFormBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.returnform.ReturnForm;
import org.jembi.bsis.model.returnform.ReturnStatus;
import org.jembi.bsis.repository.ReturnFormRepository;
import org.jembi.bsis.service.ComponentReturnService;
import org.jembi.bsis.service.ReturnFormCRUDService;
import org.jembi.bsis.service.ReturnFormConstraintChecker;
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
  private ComponentReturnService componentReturnService;
  
  @Mock
  private ReturnFormConstraintChecker returnFormConstraintChecker;

  @Test
  public void testCreateReturnForm_shouldPersist() {
    // Set up
    Location returnedFrom = LocationBuilder.aUsageSite().withId(1L).build();
    Location returnedTo = LocationBuilder.aDistributionSite().withId(2L).build();
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
    verifyZeroInteractions(componentReturnService);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testUpdateReturnFormWhenCannotEdit_shouldThrow() {
    // Set up data
    ReturnForm existingReturnForm = aReturnForm().withId(1L).withReturnStatus(ReturnStatus.RETURNED).build();
    ReturnForm updatedReturnForm = aReturnForm().withId(1L).withReturnStatus(ReturnStatus.RETURNED).build();
    
    // Set up mocks
    when(returnFormRepository.findById(1L)).thenReturn(existingReturnForm);
    when(returnFormConstraintChecker.canEdit(argThat(hasSameStateAsReturnForm(existingReturnForm)))).thenReturn(false);
    
    // Run test
    returnFormCRUDService.updateReturnForm(updatedReturnForm);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testUpdateReturnFormWhenCannotReturn_shouldThrow() {
    // Set up data
    ReturnForm existingReturnForm = aReturnForm().withId(1L).withReturnStatus(ReturnStatus.CREATED).build();
    ReturnForm updatedReturnForm = aReturnForm()
        .withId(1L)
        .withReturnStatus(ReturnStatus.RETURNED)
        .withComponents(Collections.<Component>emptyList())
        .build();
    
    // Set up mocks
    when(returnFormRepository.findById(1L)).thenReturn(existingReturnForm);
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
    
    ReturnForm existingReturnForm = ReturnFormBuilder.aReturnForm()
        .withId(1L)
        .withReturnStatus(ReturnStatus.CREATED)
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .build();
    ReturnForm updatedReturnForm = ReturnFormBuilder.aReturnForm()
        .withId(1L)
        .withReturnStatus(ReturnStatus.CREATED)
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withComponent(component)
        .build();

    // Setup mocks
    when(returnFormRepository.findById(1L)).thenReturn(existingReturnForm);
    when(returnFormConstraintChecker.canEdit(argThat(hasSameStateAsReturnForm(existingReturnForm)))).thenReturn(true);
    when(returnFormRepository.update(argThat(hasSameStateAsReturnForm(updatedReturnForm)))).thenReturn(updatedReturnForm);
    
    // Run test
    ReturnForm mergedReturnForm = returnFormCRUDService.updateReturnForm(updatedReturnForm);
    
    // Verify
    assertThat(mergedReturnForm, is(updatedReturnForm));
    verifyZeroInteractions(componentReturnService);
    verify(returnFormRepository).update(updatedReturnForm);
  }
  
  @Test
  public void testUpdateReturnFormToReturned_shouldUpdateFieldsCorrectly() {
    // Setup data
    Location returnedFrom = aDistributionSite().build();
    Location returnedTo = aUsageSite().build();
    Component component = aComponent().build();
    
    ReturnForm existingReturnForm = ReturnFormBuilder.aReturnForm()
        .withId(1L)
        .withReturnStatus(ReturnStatus.CREATED)
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .build();
    ReturnForm updatedReturnForm = ReturnFormBuilder.aReturnForm()
        .withId(1L)
        .withReturnStatus(ReturnStatus.RETURNED)
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withComponent(component)
        .build();

    // Setup mocks
    when(returnFormRepository.findById(1L)).thenReturn(existingReturnForm);
    when(returnFormConstraintChecker.canEdit(argThat(hasSameStateAsReturnForm(existingReturnForm)))).thenReturn(true);
    when(returnFormConstraintChecker.canReturn(argThat(hasSameStateAsReturnForm(existingReturnForm)))).thenReturn(true);
    when(returnFormRepository.update(argThat(hasSameStateAsReturnForm(updatedReturnForm)))).thenReturn(updatedReturnForm);
    
    // Run test
    ReturnForm mergedReturnForm = returnFormCRUDService.updateReturnForm(updatedReturnForm);
    
    // Verify
    assertThat(mergedReturnForm, is(updatedReturnForm));
    verify(componentReturnService).returnComponent(component, returnedTo);
    verify(returnFormRepository).update(updatedReturnForm);
  }

}
