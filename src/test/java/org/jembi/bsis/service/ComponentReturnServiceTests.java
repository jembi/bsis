package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aDistributionSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aUsageSite;
import static org.jembi.bsis.helpers.matchers.ComponentMatcher.hasSameStateAsComponent;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.ComponentStatusChangeReasonRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ComponentReturnServiceTests extends UnitTestSuite {
  
  @InjectMocks
  private ComponentReturnService componentReturnService;
  @Mock
  private ComponentCRUDService componentCRUDService;
  @Mock
  private DateGeneratorService dateGeneratorService;
  @Mock
  private ComponentStatusChangeReasonRepository componentStatusChangeReasonRepository;
  
  @Test(expected = IllegalArgumentException.class)
  public void testReturnComponentToNonDistributionSite_shouldThrow() {
    // Set up data
    Component componentToReturn = aComponent().withLocation(aUsageSite().build()).build();
    Location returnedTo = aUsageSite().build();

    // Run test
    componentReturnService.returnComponent(componentToReturn, returnedTo);
  }
  
  @Test
  public void testReturnComponent_shouldUpdateComponentCorrectly() {
    // Set up data
    Component componentToReturn = aComponent()
        .withLocation(aUsageSite().build())
        .withStatus(ComponentStatus.ISSUED)
        .withInventoryStatus(InventoryStatus.REMOVED)
        .withDonation(null).build();
    Location returnedTo = aDistributionSite().build();
    
    Component expectedComponent = aComponent()
        .withLocation(returnedTo)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withDonation(null).build();
    
    // Set up mocks
    when(componentCRUDService.updateComponent(argThat(hasSameStateAsComponent(expectedComponent)))).thenReturn(expectedComponent);
    
    // Run test
    Component returnedComponent = componentReturnService.returnComponent(componentToReturn, returnedTo);
    
    // Verify
    assertThat(returnedComponent, hasSameStateAsComponent(expectedComponent));
  }
  
}
