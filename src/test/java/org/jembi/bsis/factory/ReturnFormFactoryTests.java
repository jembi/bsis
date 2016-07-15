package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hamcrest.Matchers;
import org.jembi.bsis.backingform.ReturnFormBackingForm;
import org.jembi.bsis.helpers.builders.ComponentBackingFormBuilder;
import org.jembi.bsis.helpers.builders.ComponentBuilder;
import org.jembi.bsis.helpers.builders.ComponentViewModelBuilder;
import org.jembi.bsis.helpers.builders.LocationBackingFormBuilder;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.helpers.builders.ReturnFormBackingFormBuilder;
import org.jembi.bsis.helpers.builders.ReturnFormBuilder;
import org.jembi.bsis.helpers.builders.ReturnFormFullViewModelBuilder;
import org.jembi.bsis.helpers.builders.ReturnFormViewModelBuilder;
import org.jembi.bsis.helpers.matchers.ReturnFormFullViewModelMatcher;
import org.jembi.bsis.helpers.matchers.ReturnFormMatcher;
import org.jembi.bsis.helpers.matchers.ReturnFormViewModelMatcher;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.returnform.ReturnForm;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.service.ReturnFormConstraintChecker;
import org.jembi.bsis.viewmodel.ComponentViewModel;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.jembi.bsis.viewmodel.ReturnFormFullViewModel;
import org.jembi.bsis.viewmodel.ReturnFormViewModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReturnFormFactoryTests {

  @InjectMocks
  private ReturnFormFactory returnFormFactory;
  @Mock
  private LocationRepository locationRepository;
  @Mock
  private LocationFactory locationFactory;
  @Mock
  private ComponentRepository componentRepository;
  @Mock
  private ComponentFactory componentFactory;
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
        .withReturnedFrom(new LocationFullViewModel(returnedFrom))
        .withReturnedTo(new LocationFullViewModel(returnedTo))
        .withReturnDate(returnDate)
        .build();

    ReturnForm entity = ReturnFormBuilder.aReturnForm()
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withReturnDate(returnDate)
        .build();
    
    // Setup mocks
    when(locationFactory.createFullViewModel(returnedFrom)).thenReturn(new LocationFullViewModel(returnedFrom));
    when(locationFactory.createFullViewModel(returnedTo)).thenReturn(new LocationFullViewModel(returnedTo));

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
    ComponentViewModel componentViewModel = ComponentViewModelBuilder.aComponentViewModel().withId(1L).build();

    ReturnFormFullViewModel expectedViewModel = ReturnFormFullViewModelBuilder.aReturnFormFullViewModel()
        .withReturnedFrom(new LocationFullViewModel(returnedFrom))
        .withReturnedTo(new LocationFullViewModel(returnedTo))
        .withReturnDate(returnDate)
        .withComponent(componentViewModel)
        .withPermission("canEdit", true)
        .withPermission("canReturn", true)
        .withPermission("canDiscard", true)
        .withPermission("canDelete", true)
        .build();

    ReturnForm entity = ReturnFormBuilder.aReturnForm()
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withReturnDate(returnDate)
        .withComponent(component)
        .build();

    // Setup mocks
    when(componentFactory.createComponentViewModels(entity.getComponents())).thenReturn(expectedViewModel.getComponents());
    when(locationFactory.createFullViewModel(returnedFrom)).thenReturn(new LocationFullViewModel(returnedFrom));
    when(locationFactory.createFullViewModel(returnedTo)).thenReturn(new LocationFullViewModel(returnedTo));
    when(returnFormConstraintChecker.canEdit(entity)).thenReturn(true);
    when(returnFormConstraintChecker.canReturn(entity)).thenReturn(true);
    when(returnFormConstraintChecker.canDiscard(entity)).thenReturn(true);
    when(returnFormConstraintChecker.canDelete(entity)).thenReturn(true);

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
        .withReturnedFrom(new LocationFullViewModel(returnedFrom1))
        .withReturnedTo(new LocationFullViewModel(returnedTo1))
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
        .withReturnedFrom(new LocationFullViewModel(returnedFrom2))
        .withReturnedTo(new LocationFullViewModel(returnedTo2))
        .withReturnDate(returnDate2)
        .build();
    ReturnForm entity2 = ReturnFormBuilder.aReturnForm()
        .withReturnedFrom(returnedFrom2)
        .withReturnedTo(returnedTo2)
        .withReturnDate(returnDate2)
        .build();
    
    // Setup mocks
    when(locationFactory.createFullViewModel(returnedFrom1)).thenReturn(new LocationFullViewModel(returnedFrom1));
    when(locationFactory.createFullViewModel(returnedFrom2)).thenReturn(new LocationFullViewModel(returnedFrom2));
    when(locationFactory.createFullViewModel(returnedTo1)).thenReturn(new LocationFullViewModel(returnedTo1));
    when(locationFactory.createFullViewModel(returnedTo2)).thenReturn(new LocationFullViewModel(returnedTo2));

    // Run test
    List<ReturnFormViewModel> convertedViewModels = returnFormFactory.createViewModels(Arrays.asList(entity1, entity2));

    // Verify
    assertThat(convertedViewModels, Matchers.notNullValue());
    assertThat(convertedViewModels.get(0), ReturnFormViewModelMatcher.hasSameStateAsReturnFormViewModel(expectedViewModel1));
    assertThat(convertedViewModels.get(1), ReturnFormViewModelMatcher.hasSameStateAsReturnFormViewModel(expectedViewModel2));
  }
}
