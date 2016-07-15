package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LocationFactoryTests {

  @InjectMocks
  private LocationFactory locationFactory;

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
  public void testCreateLocationFullViewModels_shouldReturnViewModelWithTheCorrectState() {
    List<Location> locations = new ArrayList<Location>();
    locations.add(LocationBuilder.aLocation().build());
    locations.add(LocationBuilder.aLocation().build());
    List<LocationFullViewModel> venueViewModels = locationFactory.createFullViewModels(locations);
    Assert.assertNotNull("venue view models were created", venueViewModels);
    Assert.assertEquals("venue view models were created", 2, venueViewModels.size());
  }
}
