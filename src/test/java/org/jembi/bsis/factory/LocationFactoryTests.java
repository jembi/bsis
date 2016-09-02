package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.matchers.DivisionViewModelMatcher.hasSameStateAsDivisionViewModel;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.helpers.builders.DivisionBuilder;
import org.jembi.bsis.helpers.builders.DivisionViewModelBuilder;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.viewmodel.DivisionViewModel;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LocationFactoryTests {

  @InjectMocks
  private LocationFactory locationFactory;
  @Mock
  private DivisionFactory divisionFactory;

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
}
