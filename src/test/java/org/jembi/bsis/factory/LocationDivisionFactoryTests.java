package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.LocationDivisionBuilder.aLocationDivision;
import static org.jembi.bsis.helpers.builders.LocationDivisionViewModelBuilder.aLocationDivisionViewModel;
import static org.jembi.bsis.helpers.matchers.LocationDivisionViewModelMatcher.hasSameStateAsLocationDivisionViewModel;
import static org.mockito.Mockito.doReturn;

import java.util.Arrays;
import java.util.List;

import org.jembi.bsis.model.location.LocationDivision;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.LocationDivisionViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;

public class LocationDivisionFactoryTests extends UnitTestSuite {
  
  @Spy
  @InjectMocks
  private LocationDivisionFactory locationDivisionFactory;
  
  @Test
  public void testCreateLocationDivisionViewModel_shouldReturnViewModelWithTheCorrectState() {
    // Set up fixture
    long id = 769L;
    String name = "Some Location Division";
    int level = 1;
    
    LocationDivision locationDivision = aLocationDivision()
        .withId(id)
        .withName(name)
        .withLevel(level)
        .build();
    
    // Set up expectations
    LocationDivisionViewModel expectedViewModel = aLocationDivisionViewModel()
        .withId(id)
        .withName(name)
        .withLevel(level)
        .build();
    
    // Exercise SUT
    LocationDivisionViewModel returnedViewModel = locationDivisionFactory.createLocationDivisionViewModel(locationDivision);
    
    // Verify
    assertThat(returnedViewModel, hasSameStateAsLocationDivisionViewModel(expectedViewModel));
  }
  
  @Test
  public void testCreateLocationDivisionViewModels_shouldReturnExpectedViewModels() {
    // Set up fixture
    LocationDivision firstLocationDivision = aLocationDivision().withId(1L).withName("First").withLevel(1).build();
    LocationDivision secondLocationDivision = aLocationDivision().withId(3L).withName("Second").withLevel(2).build();
    List<LocationDivision> locationDivisions = Arrays.asList(firstLocationDivision, secondLocationDivision);
    
    // Set up expectations
    LocationDivisionViewModel firstViewModel = aLocationDivisionViewModel()
        .withId(1L)
        .withName("First")
        .withLevel(1)
        .build();
    LocationDivisionViewModel secondViewModel = aLocationDivisionViewModel()
        .withId(3L)
        .withName("Second")
        .withLevel(2)
        .build();
    List<LocationDivisionViewModel> expectedViewModels = Arrays.asList(firstViewModel, secondViewModel);
    
    doReturn(firstViewModel).when(locationDivisionFactory).createLocationDivisionViewModel(firstLocationDivision);
    doReturn(secondViewModel).when(locationDivisionFactory).createLocationDivisionViewModel(secondLocationDivision);
    
    // Exercise SUT
    List<LocationDivisionViewModel> returnedViewModels = locationDivisionFactory.createLocationDivisionViewModels(
        locationDivisions);
    
    // Verify
    assertThat(returnedViewModels, is(expectedViewModels));
  }

}
