package service;

import static helpers.builders.ComponentBuilder.aComponent;
import static helpers.builders.LocationBuilder.aDistributionSite;
import static helpers.builders.LocationBuilder.aUsageSite;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import helpers.builders.LocationBuilder;
import helpers.builders.ReturnFormBuilder;
import model.component.Component;
import model.location.Location;
import model.returnform.ReturnForm;
import model.returnform.ReturnStatus;
import repository.ReturnFormRepository;
import suites.UnitTestSuite;

public class ReturnFormCRUDServiceTests extends UnitTestSuite {
  
  @InjectMocks
  private ReturnFormCRUDService returnFormCRUDService;
  
  @Mock
  private ReturnFormRepository returnFormRepository;

  @Mock
  private ComponentReturnService componentReturnService;

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
    when(returnFormRepository.update(updatedReturnForm)).thenReturn(updatedReturnForm);
    
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
    when(returnFormRepository.update(updatedReturnForm)).thenReturn(updatedReturnForm);
    
    // Run test
    ReturnForm mergedReturnForm = returnFormCRUDService.updateReturnForm(updatedReturnForm);
    
    // Verify
    assertThat(mergedReturnForm, is(updatedReturnForm));
    verify(componentReturnService).returnComponent(component, returnedTo);
    verify(returnFormRepository).update(updatedReturnForm);
  }

}
