package factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import model.component.Component;
import model.location.Location;
import model.returnform.ReturnForm;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import repository.ComponentRepository;
import repository.LocationRepository;
import service.ReturnFormConstraintChecker;
import viewmodel.ComponentViewModel;
import viewmodel.LocationViewModel;
import viewmodel.ReturnFormFullViewModel;
import viewmodel.ReturnFormViewModel;
import backingform.ReturnFormBackingForm;

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

  @Test
  public void testConvertNullToReturnFormViewModels_shouldReturnEmptyList() {
    // Run test
    List<ReturnFormViewModel> convertedViewModels = returnFormFactory.createViewModels(null);

    // Verify
    assertThat(convertedViewModels, Matchers.notNullValue());
  }
  
  @Test
  public void testConvertEntitiesToReturnFormViewModels_shouldReturnList() {
    // Set up data
    Location returnedFrom1 = LocationBuilder.aUsageSite().withId(1L).build();
    Location returnedTo1 = LocationBuilder.aDistributionSite().withId(2L).build();
    Date returnDate1 = new Date();
    ReturnFormViewModel expectedViewModel1 = ReturnFormViewModelBuilder.aReturnFormViewModel()
        .withReturnedFrom(new LocationViewModel(returnedFrom1))
        .withReturnedTo(new LocationViewModel(returnedTo1))
        .withReturnDate(returnDate1)
        .build();
    ReturnForm entity1 = ReturnFormBuilder.aReturnForm()
        .withReturnedFrom(returnedFrom1)
        .withReturnedTo(returnedTo1)
        .withReturnDate(returnDate1)
        .build();
    
    Location returnedFrom2 = LocationBuilder.aUsageSite().withId(1L).build();
    Location returnedTo2 = LocationBuilder.aDistributionSite().withId(2L).build();
    Date returnDate2 = new Date();
    ReturnFormViewModel expectedViewModel2 = ReturnFormViewModelBuilder.aReturnFormViewModel()
        .withReturnedFrom(new LocationViewModel(returnedFrom2))
        .withReturnedTo(new LocationViewModel(returnedTo2))
        .withReturnDate(returnDate2)
        .build();
    ReturnForm entity2 = ReturnFormBuilder.aReturnForm()
        .withReturnedFrom(returnedFrom2)
        .withReturnedTo(returnedTo2)
        .withReturnDate(returnDate2)
        .build();
    
    // Setup mocks
    when(locationViewModelFactory.createLocationViewModel(returnedFrom1)).thenReturn(new LocationViewModel(returnedFrom1));
    when(locationViewModelFactory.createLocationViewModel(returnedFrom2)).thenReturn(new LocationViewModel(returnedFrom2));
    when(locationViewModelFactory.createLocationViewModel(returnedTo1)).thenReturn(new LocationViewModel(returnedTo1));
    when(locationViewModelFactory.createLocationViewModel(returnedTo2)).thenReturn(new LocationViewModel(returnedTo2));

    // Run test
    List<ReturnFormViewModel> convertedViewModels = returnFormFactory.createViewModels(Arrays.asList(entity1, entity2));

    // Verify
    assertThat(convertedViewModels, Matchers.notNullValue());
    assertThat(convertedViewModels.get(0), ReturnFormViewModelMatcher.hasSameStateAsReturnFormViewModel(expectedViewModel1));
    assertThat(convertedViewModels.get(1), ReturnFormViewModelMatcher.hasSameStateAsReturnFormViewModel(expectedViewModel2));
  }
}
