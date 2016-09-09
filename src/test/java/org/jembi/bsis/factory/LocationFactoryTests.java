package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.DivisionBackingFormBuilder.aDivisionBackingForm;
import static org.jembi.bsis.helpers.builders.DivisionBuilder.aDivision;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aVenueBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.builders.LocationManagementViewModelBuilder.aLocationManagementViewModel;
import static org.jembi.bsis.helpers.matchers.DivisionViewModelMatcher.hasSameStateAsDivisionViewModel;
import static org.jembi.bsis.helpers.matchers.LocationManagementViewModelMatcher.hasSameStateAsLocationManagementViewModel;
import static org.jembi.bsis.helpers.matchers.LocationMatcher.hasSameStateAsLocation;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.helpers.builders.DivisionBuilder;
import org.jembi.bsis.helpers.builders.DivisionViewModelBuilder;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.DivisionRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.DivisionViewModel;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.jembi.bsis.viewmodel.LocationManagementViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class LocationFactoryTests extends UnitTestSuite {

  @InjectMocks
  private LocationFactory locationFactory;
  @Mock
  private DivisionFactory divisionFactory;
  @Mock
  private DivisionRepository divisionRepository;

  @Test
  public void testCreateLocationFullViewModel_shouldReturnViewModelWithTheCorrectState() {
    Long venueId = 1L;
    String venueName = "location";
    Location venue = LocationBuilder.aLocation().withId(venueId).withName(venueName).thatIsVenue().build();
    LocationFullViewModel venueViewModel = locationFactory.createFullViewModel(venue);
    Assert.assertNotNull("venue view model was created", venueViewModel);
    Assert.assertEquals("isVenue is correct", true, venueViewModel.getIsVenue());
    Assert.assertEquals("name is correct", venueName, venueViewModel.getName());
    Assert.assertEquals("id is correct", venueId, venueViewModel.getId());
  }

  @Test
  public void testCreateLocationFullViewModelWithDivisions_shouldReturnViewModelWithTheCorrectState() {
    // Set up data
    Division divisionLevel1 = DivisionBuilder.aDivision().withId(1l).build();
    Division divisionLevel2 = DivisionBuilder.aDivision().withId(2l).build();
    Division divisionLevel3 = DivisionBuilder.aDivision().withId(3l).build();
    Long venueId = 1L;
    String venueName = "location";
    Location venue = LocationBuilder.aLocation().withId(venueId).withName(venueName).thatIsVenue()
        .withDivisionLevel1(divisionLevel1)
        .withDivisionLevel2(divisionLevel2)
        .withDivisionLevel3(divisionLevel3)
        .build();

    DivisionViewModel divisionLevel1ViewModel = DivisionViewModelBuilder.aDivisionViewModel().withId(1l).build();
    DivisionViewModel divisionLevel2ViewModel = DivisionViewModelBuilder.aDivisionViewModel().withId(2l).build();
    DivisionViewModel divisionLevel3ViewModel = DivisionViewModelBuilder.aDivisionViewModel().withId(3l).build();

    // Set up mocks
    when(divisionFactory.createDivisionViewModel(divisionLevel1)).thenReturn(divisionLevel1ViewModel);
    when(divisionFactory.createDivisionViewModel(divisionLevel2)).thenReturn(divisionLevel2ViewModel);
    when(divisionFactory.createDivisionViewModel(divisionLevel3)).thenReturn(divisionLevel3ViewModel);

    // Run test
    LocationFullViewModel venueViewModel = locationFactory.createFullViewModel(venue);

    // Verify
    Assert.assertNotNull("venue view model was created", venueViewModel);
    Assert.assertEquals("isVenue is correct", true, venueViewModel.getIsVenue());
    Assert.assertEquals("name is correct", venueName, venueViewModel.getName());
    Assert.assertEquals("id is correct", venueId, venueViewModel.getId());
    assertThat(venueViewModel.getDivisionLevel1(), hasSameStateAsDivisionViewModel(divisionLevel1ViewModel));
    assertThat(venueViewModel.getDivisionLevel2(), hasSameStateAsDivisionViewModel(divisionLevel2ViewModel));
    assertThat(venueViewModel.getDivisionLevel3(), hasSameStateAsDivisionViewModel(divisionLevel3ViewModel));
  }

  @Test
  public void testCreateLocationFullViewModels_shouldReturnViewModelWithTheCorrectState() {
    List<Location> locations = new ArrayList<Location>();
    locations.add(LocationBuilder.aLocation().build());
    locations.add(LocationBuilder.aLocation().build());
    List<LocationFullViewModel> venueViewModels = locationFactory.createFullViewModels(locations);
    Assert.assertNotNull("venue view models were created", venueViewModels);
    Assert.assertEquals("venue view models were created", 2, venueViewModels.size());
  }

  @Test
  public void testCreateLocationViewModel_shouldReturnViewModelWithTheCorrectState() {
    Long id = 1L;
    String name = "location";
    Location location = LocationBuilder.aLocation().withId(id).withName(name).thatIsDeleted().build();
    LocationViewModel venueViewModel = locationFactory.createViewModel(location);
    Assert.assertNotNull("location view model was created", venueViewModel);
    Assert.assertEquals("isDeleted is true", true, venueViewModel.getIsDeleted());
    Assert.assertEquals("name is correct", name, venueViewModel.getName());
    Assert.assertEquals("id is correct", id, venueViewModel.getId());
  }

  @Test
  public void testCreateLocationViewModels_shouldReturnViewModelWithTheCorrectState() {
    List<Location> locations = new ArrayList<Location>();
    locations.add(LocationBuilder.aLocation().build());
    locations.add(LocationBuilder.aLocation().build());
    List<LocationViewModel> locationViewModels = locationFactory.createViewModels(locations);
    Assert.assertNotNull("location view models were created", locationViewModels);
    Assert.assertEquals("there are 2 location view models", 2, locationViewModels.size());
  }
  
  @Test
  public void testCreateEntity_shouldReturnEntityWithTheCorrectState() {
    // Set up fixture
    LocationBackingForm backingForm = aVenueBackingForm()
        .withId(1L)
        .withName("Location")
        .withDivisionLevel3(aDivisionBackingForm().withId(3L).build())
        .build();
    
    Division divisionLevel1 = aDivision().withId(1L).withName("Level 1").build();
    Division divisionLevel2 = aDivision().withId(2L).withName("Level 2").withParent(divisionLevel1).build();
    Division divisionLevel3 = aDivision().withId(3L).withName("Level 3").withParent(divisionLevel2).build();
    
    // Set up expectations
    Location expectedLocation = aVenue()
        .withId(1L)
        .withName("Location")
        .withDivisionLevel1(divisionLevel1)
        .withDivisionLevel2(divisionLevel2)
        .withDivisionLevel3(divisionLevel3)
        .build();
    
    when(divisionRepository.findDivisionById(3L)).thenReturn(divisionLevel3);
    
    // Exercise SUT
    Location returnedLocation = locationFactory.createEntity(backingForm);
    
    // Verify
    assertThat(returnedLocation, hasSameStateAsLocation(expectedLocation));
  }
  
  @Test
  public void testCreateManagementViewModel_shouldReturnViewModelWithTheCorrectState() {
   // data setUp
    long locationId = 1L;
    long divisionId = 3l;
    String divisionName = "aDivision";
    String locationName = "aLocation";
    Division divisionLevel3 = aDivision().withId(divisionId).withName(divisionName).build();
    
    DivisionViewModel divisionLevel3ViewModel = DivisionViewModelBuilder.aDivisionViewModel()
        .withId(divisionId)
        .withName(divisionName)
        .build();  
    
    Location location = LocationBuilder.aLocation()
        .withId(locationId)
        .withName(locationName)
        .withDivisionLevel3(divisionLevel3)
        .build();
    
    LocationManagementViewModel expectedViewModel = aLocationManagementViewModel()
        .withId(locationId)
        .withName(locationName)
        .isDeleted(false)
        .withDivisionLevel3(divisionLevel3ViewModel)
        .build();
   
    when(divisionFactory.createDivisionViewModel(divisionLevel3, false)).thenReturn(divisionLevel3ViewModel);
    
    //Test
    LocationManagementViewModel viewModel = locationFactory.createManagementViewModel(location);
    
    assertThat(viewModel, hasSameStateAsLocationManagementViewModel(expectedViewModel));
  }
  
  @Test
  public void testCreateManagementViewModels_shouldReturnViewModelsWithTheCorrectStates() {
    long divisionId = 3L;
    String divisionName = "aDivision";
    Division divisionLevel3 = aDivision()
        .withId(divisionId)
        .withName(divisionName)
        .build();
    
    DivisionViewModel divisionLevel3ViewModel = DivisionViewModelBuilder.aDivisionViewModel()
        .withId(divisionId)
        .withName(divisionName)
        .build();  
    
    String locationName1 = "aLocation1";
    String locationName2 = "aLocation2";
    String locationName3 = "aLocation3";
    
    List<Location> locations = Arrays.asList(
        LocationBuilder.aLocation().withId(1L).withName(locationName1).withDivisionLevel3(divisionLevel3).build(),
        LocationBuilder.aLocation().withId(2L).withName(locationName2).withDivisionLevel3(divisionLevel3).build(),
        LocationBuilder.aLocation().withId(3L).withName(locationName3).withDivisionLevel3(divisionLevel3).build()); 
    
    List<LocationManagementViewModel> expectedLocations = Arrays.asList(
        aLocationManagementViewModel().withId(1L).withName(locationName1).withDivisionLevel3(divisionLevel3ViewModel).build(),
        aLocationManagementViewModel().withId(2L).withName(locationName2).withDivisionLevel3(divisionLevel3ViewModel).build(),
        aLocationManagementViewModel().withId(3L).withName(locationName3).withDivisionLevel3(divisionLevel3ViewModel).build());
    
    when(divisionFactory.createDivisionViewModel(divisionLevel3, false)).thenReturn(divisionLevel3ViewModel);
    //Test
    List<LocationManagementViewModel> viewModels = locationFactory.createManagementViewModels(locations);
  
    //Assertion
    assertThat(viewModels, Matchers.notNullValue());
    assertThat(viewModels.get(0), hasSameStateAsLocationManagementViewModel(expectedLocations.get(0)));    
    assertThat(viewModels.get(1), hasSameStateAsLocationManagementViewModel(expectedLocations.get(1)));   
    assertThat(viewModels.get(2), hasSameStateAsLocationManagementViewModel(expectedLocations.get(2)));
  }
}
