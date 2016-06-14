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
import helpers.builders.ComponentBackingFormBuilder;
import helpers.builders.ComponentBuilder;
import helpers.builders.LocationBackingFormBuilder;
import helpers.builders.LocationBuilder;
import helpers.builders.ReturnFormBackingFormBuilder;
import helpers.builders.ReturnFormBuilder;
import helpers.builders.ReturnFormFullViewModelBuilder;
import helpers.builders.ReturnFormViewModelBuilder;
import helpers.matchers.ReturnFormFullViewModelMatcher;
import helpers.matchers.ReturnFormMatcher;
import helpers.matchers.ReturnFormViewModelMatcher;
import model.component.Component;
import model.location.Location;
import model.returnform.ReturnForm;
import repository.ComponentRepository;
import repository.LocationRepository;
import service.ReturnFormConstraintChecker;
import viewmodel.ComponentViewModel;
import viewmodel.LocationViewModel;
import viewmodel.ReturnFormFullViewModel;
import viewmodel.ReturnFormViewModel;

@RunWith(MockitoJUnitRunner.class)
public class ReturnFormFactoryTests {

  @InjectMocks
  private ReturnFormFactory returnFormFactory;
  @Mock
  private LocationRepository locationRepository;
  @Mock
  private LocationViewModelFactory locationViewModelFactory;
  @Mock
  private ComponentRepository componentRepository;
  @Mock
  private ComponentViewModelFactory componentViewModelFactory;
  @Mock
  private ReturnFormConstraintChecker returnFormConstraintChecker;

  @Test
  public void testConvertBackingFormToReturnFormEntity_shouldReturnExpectedEntity() {
    // Set up data
    Location returnedFrom = LocationBuilder.aUsageSite().withId(1L).build();
    Location returnedTo = LocationBuilder.aDistributionSite().withId(2L).build();
    Component component = ComponentBuilder.aComponent().withId(1L).build();
    Date returnDate = new Date();

    ReturnForm expectedEntity = ReturnFormBuilder.aReturnForm()
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withReturnDate(returnDate)
        .withComponent(component)
        .build();
    
    ReturnFormBackingForm backingForm = ReturnFormBackingFormBuilder.aReturnFormBackingForm()
        .withReturnedFrom(LocationBackingFormBuilder.aUsageSiteBackingForm().withId(1L).build())
        .withReturnedTo(LocationBackingFormBuilder.aDistributionSiteBackingForm().withId(2L).build())
        .withReturnDate(returnDate)
        .withComponent(ComponentBackingFormBuilder.aComponentBackingForm().withId(1L).build())
        .build();

    // Setup mock
    when(locationRepository.getLocation(1l)).thenReturn(returnedFrom);
    when(locationRepository.getLocation(2l)).thenReturn(returnedTo);
    when(componentRepository.findComponent(1L)).thenReturn(component);

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
        .withReturnedFrom(new LocationViewModel(returnedFrom))
        .withReturnedTo(new LocationViewModel(returnedTo))
        .withReturnDate(returnDate)
        .build();

    ReturnForm entity = ReturnFormBuilder.aReturnForm()
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withReturnDate(returnDate)
        .build();
    
    // Setup mocks
    when(locationViewModelFactory.createLocationViewModel(returnedFrom)).thenReturn(new LocationViewModel(returnedFrom));
    when(locationViewModelFactory.createLocationViewModel(returnedTo)).thenReturn(new LocationViewModel(returnedTo));

    // Run test
    ReturnFormViewModel convertedViewModel = returnFormFactory.createViewModel(entity);

    // Verify
    assertThat(convertedViewModel, ReturnFormViewModelMatcher.hasSameStateAsReturnFormViewModel(expectedViewModel));
  }

  @Test
  public void testConvertEntityToReturnFormFullViewModel_shouldReturnExpectedViewModel() {
    // Set up data
    Location returnedFrom = LocationBuilder.aUsageSite().withId(1L).build();
    Location returnedTo = LocationBuilder.aDistributionSite().withId(2L).build();
    Date returnDate = new Date();
    Component component = ComponentBuilder.aComponent().withId(1L).build();

    ReturnFormFullViewModel expectedViewModel = ReturnFormFullViewModelBuilder.aReturnFormFullViewModel()
        .withReturnedFrom(new LocationViewModel(returnedFrom))
        .withReturnedTo(new LocationViewModel(returnedTo))
        .withReturnDate(returnDate)
        .withComponent(new ComponentViewModel(component))
        .withPermission("canEdit", true)
        .withPermission("canReturn", true)
        .build();

    ReturnForm entity = ReturnFormBuilder.aReturnForm()
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withReturnDate(returnDate)
        .withComponent(component)
        .build();

    // Setup mocks
    when(componentViewModelFactory.createComponentViewModels(entity.getComponents())).thenReturn(expectedViewModel.getComponents());
    when(locationViewModelFactory.createLocationViewModel(returnedFrom)).thenReturn(new LocationViewModel(returnedFrom));
    when(locationViewModelFactory.createLocationViewModel(returnedTo)).thenReturn(new LocationViewModel(returnedTo));
    when(returnFormConstraintChecker.canEdit(entity)).thenReturn(true);
    when(returnFormConstraintChecker.canReturn(entity)).thenReturn(true);

    // Run test
    ReturnFormFullViewModel convertedViewModel = returnFormFactory.createFullViewModel(entity);

    // Verify
    assertThat(convertedViewModel, ReturnFormFullViewModelMatcher.hasSameStateAsReturnFormFullViewModel(expectedViewModel));
  }

  
}
