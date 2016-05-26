package service;

import static helpers.builders.ComponentBuilder.aComponent;
import static helpers.builders.LocationBuilder.aDistributionSite;
import static helpers.builders.LocationBuilder.aUsageSite;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import model.component.Component;
import model.componentmovement.ComponentStatusChangeReason;
import model.componentmovement.ComponentStatusChangeReasonCategory;
import model.location.Location;
import repository.ComponentStatusChangeReasonRepository;
import suites.UnitTestSuite;

public class ComponentDispatchServiceTests extends UnitTestSuite {
  
  @InjectMocks
  private ComponentDispatchService componentDispatchService;
  @Mock
  private ComponentCRUDService componentCRUDService;
  @Mock
  private DateGeneratorService dateGeneratorService;
  @Mock
  private ComponentStatusChangeReasonRepository componentStatusChangeReasonRepository;
  
  @Test(expected = IllegalArgumentException.class)
  public void testTransferComponentWithNonDistributionSite_shouldThrow() {
    // Set up fixture
    Component componentToTransfer = aComponent().withLocation(aDistributionSite().build()).build();
    Location transferTo = aUsageSite().build();

    // Call test method
    componentDispatchService.transferComponent(componentToTransfer, transferTo);
  }
  
  @Test
  public void testTransferComponent_shouldUpdateComponentLocation() {
    // Set up fixture
    Component componentToTransfer = aComponent().withLocation(aDistributionSite().build()).build();
    Location transferTo = aDistributionSite().build();
    
    // Set up expectations
    when(componentCRUDService.updateComponent(componentToTransfer)).thenReturn(componentToTransfer);
    
    // Call test method
    Component returnedComponent = componentDispatchService.transferComponent(componentToTransfer, transferTo);
    
    // Verify
    assertThat(returnedComponent, is(componentToTransfer));
    assertThat(returnedComponent.getLocation(), is(transferTo));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testIssueComponentWithNonUsageSite_shouldThrow() {
    // Set up fixture
    Component componentToIssue = aComponent().withLocation(aDistributionSite().build()).build();
    Location issueTo = aDistributionSite().build();
    
    // Call test method
    componentDispatchService.issueComponent(componentToIssue, issueTo);
  }
  
  @Test
  public void testIssueComponent_shouldUpdateComponentFields() {
    // Set up fixture
    Component componentToIssue = aComponent().withLocation(aDistributionSite().build()).build();
    Location issueTo = aUsageSite().build();
    Date issuedDate = new Date();
    
    // Set up expectations
    when(dateGeneratorService.generateDate()).thenReturn(issuedDate);
    when(componentStatusChangeReasonRepository.findFirstComponentStatusChangeReasonForCategory(
        ComponentStatusChangeReasonCategory.ISSUED)).thenReturn(new ComponentStatusChangeReason());
    when(componentCRUDService.updateComponent(componentToIssue)).thenReturn(componentToIssue);
    
    // Call test method
    Component returnedComponent = componentDispatchService.issueComponent(componentToIssue, issueTo);
    
    // Verify
    assertThat(returnedComponent, is(componentToIssue));
    assertThat(returnedComponent.getLocation(), is(issueTo));
  }

}
