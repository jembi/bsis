package service;

import static helpers.builders.ComponentBuilder.aComponent;
import static helpers.builders.LocationBuilder.aDistributionSite;
import static helpers.builders.LocationBuilder.aUsageSite;
import static helpers.matchers.ComponentMatcher.hasSameStateAsComponent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import model.component.Component;
import model.component.ComponentStatus;
import model.inventory.InventoryStatus;
import model.location.Location;
import repository.ComponentStatusChangeReasonRepository;
import suites.UnitTestSuite;

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
        .withInventoryStatus(InventoryStatus.REMOVED).build();
    Location returnedTo = aDistributionSite().build();
    
    Component expectedComponent = aComponent()
        .withLocation(returnedTo)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.IN_STOCK).build();
    
    // Set up mocks
    when(componentCRUDService.updateComponent(argThat(hasSameStateAsComponent(expectedComponent)))).thenReturn(expectedComponent);
    
    // Run test
    Component returnedComponent = componentReturnService.returnComponent(componentToReturn, returnedTo);
    
    // Verify
    assertThat(returnedComponent, hasSameStateAsComponent(expectedComponent));
  }
  
}
