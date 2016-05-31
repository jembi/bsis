package factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import backingform.ReturnFormBackingForm;
import helpers.builders.LocationBackingFormBuilder;
import helpers.builders.LocationBuilder;
import helpers.builders.ReturnFormBackingFormBuilder;
import helpers.builders.ReturnFormBuilder;
import helpers.builders.ReturnFormViewModelBuilder;
import helpers.matchers.ReturnFormMatcher;
import helpers.matchers.ReturnFormViewModelMatcher;
import model.location.Location;
import model.returnform.ReturnForm;
import repository.LocationRepository;
import viewmodel.ReturnFormViewModel;

@RunWith(MockitoJUnitRunner.class)
public class ReturnFormFactoryTests {

  @InjectMocks
  private ReturnFormFactory returnFormFactory;
  @Mock
  private LocationRepository locationRepository;
  @Mock
  private LocationViewModelFactory locationViewModelFactory;

  @Test
  public void testConvertBackingFormToReturnFormEntity_shouldReturnExpectedEntity() {
    // Set up data
    Location returnedFrom = LocationBuilder.aUsageSite().withId(1L).build();
    Location returnedTo = LocationBuilder.aDistributionSite().withId(2L).build();
    Date returnDate = new Date();

    ReturnForm expectedEntity = ReturnFormBuilder.aReturnForm()
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withReturnDate(returnDate)
        .build();
    
    ReturnFormBackingForm backingForm = ReturnFormBackingFormBuilder.aReturnFormBackingForm()
        .withReturnedFrom(LocationBackingFormBuilder.aUsageSiteBackingForm().withId(1L).build())
        .withReturnedTo(LocationBackingFormBuilder.aDistributionSiteBackingForm().withId(2L).build())
        .withReturnDate(returnDate)
        .build();

    // Setup mock
    when(locationRepository.getLocation(1l)).thenReturn(returnedFrom);
    when(locationRepository.getLocation(2l)).thenReturn(returnedTo);

    // Run test
    ReturnForm convertedEntity = returnFormFactory.createEntity(backingForm);
   
    // Verify
    assertThat(convertedEntity, ReturnFormMatcher.hasSameStateAsReturnForm(expectedEntity));
  }
  


  @Test
  public void testConvertEntityToReturnFormViewModel_shouldReturnExpectedViewModel() {
    // Set up data
    Location returnedFrom = LocationBuilder.aUsageSite().withId(1L).build();
    Location returnedTo = LocationBuilder.aDistributionSite().withId(2L).build();
    Date returnDate = new Date();

    ReturnFormViewModel expectedViewModel = ReturnFormViewModelBuilder.aReturnFormViewModel()
        .withReturnedFrom(locationViewModelFactory.createLocationViewModel(returnedFrom))
        .withReturnedTo(locationViewModelFactory.createLocationViewModel(returnedTo))
        .withReturnDate(returnDate)
        .build();

    ReturnForm entity = ReturnFormBuilder.aReturnForm()
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withReturnDate(returnDate)
        .build();

    // Run test
    ReturnFormViewModel convertedViewModel = returnFormFactory.createViewModel(entity);

    // Verify
    assertThat(convertedViewModel, ReturnFormViewModelMatcher.hasSameStateAsReturnFormViewModel(expectedViewModel));
  }

  
}
