package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.DivisionBackingFormBuilder.aDivisionBackingForm;
import static org.jembi.bsis.helpers.builders.DivisionBuilder.aDivision;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aVenueBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.matchers.DivisionViewModelMatcher.hasSameStateAsDivisionViewModel;
import static org.jembi.bsis.helpers.matchers.LocationManagementViewModelMatcher.hasSameStateAsLocationManagementViewModel
;
import static org.jembi.bsis.helpers.matchers.LocationMatcher.hasSameStateAsLocation;
import static org.jembi.bsis.helpers.builders.LocationManagementViewModelBuilder.aLocationManagementViewModelBuilder;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
  public void testCreateLocationManagementViewModel_shouldReturnViewModelWithTheCorrectState() {
   // data setUp
    Long id = 3L;
    String name = "Location";
    Division divisionLevel1 = aDivision().withId(1L).withName("Level 1").build();
    Division divisionLevel2 = aDivision().withId(2L).withName("Level 2").withParent(divisionLevel1).build();
    Division divisionLevel3 = aDivision().withId(3L).withName("Level 3").withParent(divisionLevel2).build();
    
    Location location = LocationBuilder.aLocation()
        .withId(id)
        .withName(name)
        .withDivisionLevel3(divisionLevel3)
        .build();
    
    LocationManagementViewModel expectedViewModel = aLocationManagementViewModelBuilder()
        .withId(id)
        .withName(name)
        .withDivisionLevel3Name(divisionLevel3.getName())
        .build();
    
    //Test
    LocationManagementViewModel viewModel = locationFactory.createLocationViewModel(location);
    
    assertThat(viewModel, hasSameStateAsLocationManagementViewModel(expectedViewModel));
  }
  
  @Test
  public void testCreateLocationManagementViewModels_shouldReturnViewModelWithTheCorrectState() {
    Division divisionLevel1 = aDivision().withId(1L).withName("Level 1").build();
    Division divisionLevel2 = aDivision().withId(2L).withName("Level 2").withParent(divisionLevel1).build();
    Division divisionLevel3 = aDivision().withId(3L).withName("Level 3").withParent(divisionLevel2).build();
    
    List<Location> locations = Arrays.asList(
        LocationBuilder.aLocation().withId(1l).withName("Level 1").withDivisionLevel3(divisionLevel3).build(),
        LocationBuilder.aLocation().withId(2l).withName("Level 2").withDivisionLevel3(divisionLevel3).build(),
        LocationBuilder.aLocation().withId(3l).withName("Level 3").withDivisionLevel3(divisionLevel3).build()); 
    
    List<LocationManagementViewModel> expectedLocations = Arrays.asList(
        aLocationManagementViewModelBuilder().withId(1l).withName("Level 1").withDivisionLevel3Name(divisionLevel3.getName()).build(),
        aLocationManagementViewModelBuilder().withId(2l).withName("Level 2").withDivisionLevel3Name(divisionLevel3.getName()).build(),
        aLocationManagementViewModelBuilder().withId(3l).withName("Level 3").withDivisionLevel3Name(divisionLevel3.getName()).build());
    
    //Test
    List<LocationManagementViewModel> viewModels = locationFactory.createLocationViewModels(locations);
  
    //Assertions
    assertThat("View models are created", viewModels.size() == 3);
    assertThat(viewModels.get(0), hasSameStateAsLocationManagementViewModel(expectedLocations.get(0)));
    assertThat(viewModels.get(1), hasSameStateAsLocationManagementViewModel(expectedLocations.get(1)));
    assertThat(viewModels.get(2), hasSameStateAsLocationManagementViewModel(expectedLocations.get(2)));
  }
}
