package org.jembi.bsis.controller;

import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;
import static org.jembi.bsis.helpers.builders.LocationViewModelBuilder.aLocationViewModel;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jembi.bsis.controllerservice.TestBatchControllerService;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

public class TestBatchControllerTests extends UnitTestSuite {

  @Mock
  private TestBatchControllerService testBatchControllerService;
  @Mock
  private LocationFactory locationFactory;
  @InjectMocks
  private TestBatchController testBatchController;

  @Test
  //set up data
  public void testFindTestingSites() throws Exception{
    Location location1 = aLocation().withName("test 1").build();
    Location location2 = aLocation().withName("test 2").build();
    LocationViewModel locationViewModel1 = aLocationViewModel().withName(location1.getName()).build();
    LocationViewModel locationViewModel2 = aLocationViewModel().withName(location2.getName()).build();
    List<LocationViewModel> expectedLocationViewModels = new ArrayList<>();
    expectedLocationViewModels.add(locationViewModel1);
    expectedLocationViewModels.add(locationViewModel2);
    
    //set up mocks
    when(locationFactory.createViewModel(location1)).thenReturn(locationViewModel1);
    when(locationFactory.createViewModel(location2)).thenReturn(locationViewModel2);
    when(testBatchControllerService.getTestingSites()).thenReturn(expectedLocationViewModels);
    
    //SUT
    ResponseEntity<Map<String, Object>> response = testBatchController.findTestingSites();
    Map<String, Object> map = response.getBody();
    
    //assertions
    Assert.assertNotNull("map is returned", map);
    Object testingSitesValue = map.get("testingSites");
    Assert.assertNotNull("map has sites", testingSitesValue);
    Assert.assertEquals("sites are correct", expectedLocationViewModels, testingSitesValue);
  }
}
