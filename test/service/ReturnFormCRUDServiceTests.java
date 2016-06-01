package service;

import static org.mockito.Mockito.verify;

import java.util.Date;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import helpers.builders.LocationBuilder;
import helpers.builders.ReturnFormBuilder;
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
  }

}
