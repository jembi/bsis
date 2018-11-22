package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.jembi.bsis.helpers.builders.DivisionBackingFormBuilder.aDivisionBackingForm;
import static org.jembi.bsis.helpers.builders.DivisionBuilder.aDivision;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aLocationBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aVenueBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aReferralSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.builders.LocationManagementViewModelBuilder.aLocationManagementViewModel;
import static org.jembi.bsis.helpers.matchers.DivisionViewModelMatcher.hasSameStateAsDivisionViewModel;
import static org.jembi.bsis.helpers.matchers.LocationFullViewModelMatcher.hasSameStateAsLocationFullViewModel;
import static org.jembi.bsis.helpers.matchers.LocationManagementViewModelMatcher.hasSameStateAsLocationManagementViewModel;
import static org.jembi.bsis.helpers.matchers.LocationMatcher.hasSameStateAsLocation;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.helpers.builders.DivisionBuilder;
import org.jembi.bsis.helpers.builders.DivisionViewModelBuilder;
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
    UUID venueId = UUID.randomUUID();
    String venueName = "location";
    Location allSiteLocation = aLocation()
        .withId(venueId)
        .withName(venueName)
        .thatIsVenue()
        .thatIsDistributionSite()
        .thatIsMobileSite()
        .thatIsProcessingSite()
        .thatIsReferralSite()
        .thatIsTestingSite()
        .thatIsUsageSite()
        .build();

    Location expectedSiteLocation = aLocation()
        .withId(venueId)
        .withName(venueName)
        .thatIsVenue()
        .thatIsDistributionSite()
        .thatIsMobileSite()
        .thatIsProcessingSite()
        .thatIsReferralSite()
        .thatIsTestingSite()
        .thatIsUsageSite()
        .build();
    LocationFullViewModel expectedLocationFullViewModel = new LocationFullViewModel(expectedSiteLocation);

    LocationFullViewModel allSiteLocationViewModel = locationFactory.createFullViewModel(allSiteLocation);

    assertThat(allSiteLocationViewModel, hasSameStateAsLocationFullViewModel(expectedLocationFullViewModel));
  }
  
  @Test
  public void testCreateLocationReferralSiteFullViewModel_shouldReturnViewModelWithTheCorrectState() {
    UUID locationId = UUID.randomUUID();
    Location referralSite = aReferralSite()
        .withId(locationId)
        .withName("Referral Site")
        .thatIsReferralSite()
        .build();
    LocationFullViewModel referralSiteFullViewModel = locationFactory.createFullViewModel(referralSite);
    assertThat(referralSite.getId(), is(referralSiteFullViewModel.getId()));
    assertThat(referralSite.getName(), is(referralSiteFullViewModel.getName()));
    assertThat(referralSite.getIsReferralSite(), is(referralSiteFullViewModel.getIsReferralSite()));
  }

  @Test
  public void testCreateLocationFullViewModelWithDivisions_shouldReturnViewModelWithTheCorrectState() {
    // Set up data
    UUID divisionId1 = UUID.randomUUID();
    UUID divisionId2 = UUID.randomUUID();
    UUID divisionId3 = UUID.randomUUID();
    Division divisionLevel1 = DivisionBuilder.aDivision().withId(divisionId1).build();
    Division divisionLevel2 = DivisionBuilder.aDivision().withId(divisionId2).build();
    Division divisionLevel3 = DivisionBuilder.aDivision().withId(divisionId3).build();
    UUID venueId = UUID.randomUUID();
    String venueName = "location";
    Location venue = aLocation().withId(venueId).withName(venueName).thatIsVenue()
        .withDivisionLevel1(divisionLevel1)
        .withDivisionLevel2(divisionLevel2)
        .withDivisionLevel3(divisionLevel3)
        .build();

    DivisionViewModel divisionLevel1ViewModel = DivisionViewModelBuilder.aDivisionViewModel().withId(divisionId1).build();
    DivisionViewModel divisionLevel2ViewModel = DivisionViewModelBuilder.aDivisionViewModel().withId(divisionId2).build();
    DivisionViewModel divisionLevel3ViewModel = DivisionViewModelBuilder.aDivisionViewModel().withId(divisionId3).build();

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
    locations.add(aLocation().build());
    locations.add(aLocation().build());
    List<LocationFullViewModel> venueViewModels = locationFactory.createFullViewModels(locations);
    Assert.assertNotNull("venue view models were created", venueViewModels);
    Assert.assertEquals("venue view models were created", 2, venueViewModels.size());
  }

  @Test
  public void testCreateLocationViewModel_shouldReturnViewModelWithTheCorrectState() {
    UUID locationId = UUID.randomUUID();
    String name = "location";
    Location location = aLocation().withId(locationId).withName(name).thatIsDeleted().build();
    LocationViewModel venueViewModel = locationFactory.createViewModel(location);
    Assert.assertNotNull("location view model was created", venueViewModel);
    Assert.assertEquals("isDeleted is true", true, venueViewModel.getIsDeleted());
    Assert.assertEquals("name is correct", name, venueViewModel.getName());
    Assert.assertEquals("id is correct", locationId, venueViewModel.getId());
  }

  @Test
  public void testCreateLocationViewModels_shouldReturnViewModelWithTheCorrectState() {
    List<Location> locations = new ArrayList<Location>();
    locations.add(aLocation().build());
    locations.add(aLocation().build());
    List<LocationViewModel> locationViewModels = locationFactory.createViewModels(locations);
    Assert.assertNotNull("location view models were created", locationViewModels);
    Assert.assertEquals("there are 2 location view models", 2, locationViewModels.size());
  }
  
  @Test
  public void testCreateVenueInDivision_shouldReturnEntityWithTheCorrectState() {
    // Set up fixture
    UUID divisionId1 = UUID.randomUUID();
    UUID divisionId2 = UUID.randomUUID();
    UUID divisionId3 = UUID.randomUUID();

    UUID locationId = UUID.randomUUID();
    LocationBackingForm backingForm = aVenueBackingForm()
        .withId(locationId)
        .withName("Venue")
        .withDivisionLevel3(aDivisionBackingForm().withId(divisionId3).build())
        .build();
    
    Division divisionLevel1 = aDivision().withId(divisionId1).withName("Level 1").build();
    Division divisionLevel2 = aDivision().withId(divisionId2).withName("Level 2").withParent(divisionLevel1).build();
    Division divisionLevel3 = aDivision().withId(divisionId3).withName("Level 3").withParent(divisionLevel2).build();
    
    // Set up expectations
    Location expectedLocation = aVenue()
        .withId(locationId)
        .withName("Venue")
        .withDivisionLevel1(divisionLevel1)
        .withDivisionLevel2(divisionLevel2)
        .withDivisionLevel3(divisionLevel3)
        .build();
    
    when(divisionRepository.findDivisionById(divisionId3)).thenReturn(divisionLevel3);
    
    // Exercise SUT
    Location returnedLocation = locationFactory.createEntity(backingForm);
    
    // Verify
    assertThat(returnedLocation, hasSameStateAsLocation(expectedLocation));
  }

  @Test
  public void testCreateEntity_shouldReturnEntityWithTheCorrectState() {
    // Set up fixture
    UUID divisionId1 = UUID.randomUUID();
    UUID divisionId2 = UUID.randomUUID();
    UUID divisionId3 = UUID.randomUUID();

    UUID locationId = UUID.randomUUID();
    LocationBackingForm backingForm = aLocationBackingForm()
        .withId(locationId)
        .withName("Everything happens here")
        .thatIsVenue()
        .thatIsMobileSite()
        .thatIsReferralSite()
        .thatIsTestingSite()
        .thatIsUsageSite()
        .thatIsDistributionSite()
        .thatIsProcessingSite()
        .withDivisionLevel3(aDivisionBackingForm().withId(divisionId3).build())
        .build();

    Division divisionLevel1 = aDivision().withId(divisionId1).withName("Level 1").build();
    Division divisionLevel2 = aDivision().withId(divisionId2).withName("Level 2").withParent(divisionLevel1).build();
    Division divisionLevel3 = aDivision().withId(divisionId3).withName("Level 3").withParent(divisionLevel2).build();

    // Set up expectations
    Location expectedLocation = aVenue()
        .withId(locationId)
        .withName("Everything happens here")
        .thatIsVenue()
        .thatIsMobileSite()
        .thatIsReferralSite()
        .thatIsTestingSite()
        .thatIsUsageSite()
        .thatIsDistributionSite()
        .thatIsProcessingSite()
        .withDivisionLevel1(divisionLevel1)
        .withDivisionLevel2(divisionLevel2)
        .withDivisionLevel3(divisionLevel3)
        .build();

    when(divisionRepository.findDivisionById(divisionId3)).thenReturn(divisionLevel3);

    // Exercise SUT
    Location returnedLocation = locationFactory.createEntity(backingForm);

    // Verify
    assertThat(returnedLocation, hasSameStateAsLocation(expectedLocation));
  }
  
  @Test
  public void testCreateManagementViewModel_shouldReturnViewModelWithTheCorrectState() {
   // data setUp
    UUID locationId = UUID.randomUUID();
    UUID divisionId = UUID.randomUUID();
    String divisionName = "aDivision";
    String locationName = "aLocation";
    Division divisionLevel3 = aDivision().withId(divisionId).withName(divisionName).build();
    
    DivisionViewModel divisionLevel3ViewModel = DivisionViewModelBuilder.aDivisionViewModel()
        .withId(divisionId)
        .withName(divisionName)
        .build();  
    
    Location location = aLocation()
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
    String divisionName = "aDivision";
    UUID divisionId = UUID.randomUUID();

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
    
    UUID locationId1 = UUID.randomUUID();
    UUID locationId2 = UUID.randomUUID();
    UUID locationId3 = UUID.randomUUID();
    List<Location> locations = Arrays.asList(
        aLocation().withId(locationId1).withName(locationName1).withDivisionLevel3(divisionLevel3).build(),
        aLocation().withId(locationId2).withName(locationName2).withDivisionLevel3(divisionLevel3).build(),
        aLocation().withId(locationId3).withName(locationName3).withDivisionLevel3(divisionLevel3).build());
    
    List<LocationManagementViewModel> expectedLocations = Arrays.asList(
        aLocationManagementViewModel().withId(locationId1).withName(locationName1).withDivisionLevel3(divisionLevel3ViewModel).build(),
        aLocationManagementViewModel().withId(locationId2).withName(locationName2).withDivisionLevel3(divisionLevel3ViewModel).build(),
        aLocationManagementViewModel().withId(locationId3).withName(locationName3).withDivisionLevel3(divisionLevel3ViewModel).build());
    
    when(divisionFactory.createDivisionViewModel(divisionLevel3, false)).thenReturn(divisionLevel3ViewModel);
    //Test
    List<LocationManagementViewModel> viewModels = locationFactory.createManagementViewModels(locations);
  
    //Assertion
    assertThat(viewModels, is(notNullValue()));
    assertThat(viewModels.size(), is(3));
    assertThat(viewModels.get(0), hasSameStateAsLocationManagementViewModel(expectedLocations.get(0)));    
    assertThat(viewModels.get(1), hasSameStateAsLocationManagementViewModel(expectedLocations.get(1)));   
    assertThat(viewModels.get(2), hasSameStateAsLocationManagementViewModel(expectedLocations.get(2)));
  }
}
