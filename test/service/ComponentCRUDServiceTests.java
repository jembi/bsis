package service;

import static helpers.builders.ComponentBuilder.aComponent;
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonorBuilder.aDonor;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import model.component.Component;
import model.component.ComponentStatus;
import model.componentmovement.ComponentStatusChange;
import model.componentmovement.ComponentStatusChangeReason;
import model.componentmovement.ComponentStatusChangeType;
import model.donation.Donation;
import model.donor.Donor;
import model.inventory.InventoryStatus;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import repository.ComponentRepository;
import suites.UnitTestSuite;

@RunWith(MockitoJUnitRunner.class)
public class ComponentCRUDServiceTests extends UnitTestSuite {

  @InjectMocks
  private ComponentCRUDService componentCRUDService;
  @Mock
  private ComponentRepository componentRepository;

  @Test
  public void testMarkComponentsBelongingToDonorAsUnsafe_shouldDelegateToRepositoryWithCorrectParameters() {

    Donor donor = aDonor().build();

    componentCRUDService.markComponentsBelongingToDonorAsUnsafe(donor);

    verify(componentRepository).updateComponentStatusesForDonor(
        Arrays.asList(ComponentStatus.AVAILABLE, ComponentStatus.QUARANTINED), ComponentStatus.UNSAFE, donor);
  }

  @Test
  public void testMarkComponentsBelongingToDonationAsUnsafe_shouldDelegateToRepositoryWithCorrectParameters() {

    Donation donation = aDonation().build();

    componentCRUDService.markComponentsBelongingToDonationAsUnsafe(donation);

    verify(componentRepository).updateComponentStatusForDonation(
        Arrays.asList(ComponentStatus.AVAILABLE, ComponentStatus.QUARANTINED), ComponentStatus.UNSAFE,
        donation);
  }

  @Test
  public void testDiscardComponent() throws Exception {
    // set up data
    Component component = aComponent().build();
    ComponentStatusChangeReason discardReason = new ComponentStatusChangeReason();
    discardReason.setId(1L);
    String reasonText = "junit";
    
    // set up mocks
    when(componentRepository.findComponentById(1L)).thenReturn(component);
    when(componentRepository.updateComponent(component)).thenReturn(component);
    
    // run test
    componentCRUDService.discardComponent(1L, discardReason, reasonText);
    
    // check asserts
    Assert.assertNotNull("Status change has been set", component.getStatusChanges());
    Assert.assertEquals("Status change has been set", 1, component.getStatusChanges().size());
    ComponentStatusChange statusChange = component.getStatusChanges().get(0);
    Assert.assertEquals("Status change is correct", ComponentStatusChangeType.DISCARDED, statusChange.getStatusChangeType());
    Assert.assertEquals("Status change is correct", ComponentStatus.DISCARDED, statusChange.getNewStatus());
    Assert.assertEquals("Status change is correct", discardReason, statusChange.getStatusChangeReason());
    Assert.assertEquals("Status change is correct", reasonText, statusChange.getStatusChangeReasonText());
    Assert.assertEquals("Status change is correct", loggedInUser, statusChange.getChangedBy());
  }
  
  @Test
  public void testDiscardInStockComponent() throws Exception {
    // set up data
    Component component = aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).build();
    ComponentStatusChangeReason discardReason = new ComponentStatusChangeReason();
    discardReason.setId(1L);
    String reasonText = "junit";
    
    // set up mocks
    when(componentRepository.findComponentById(1L)).thenReturn(component);
    when(componentRepository.updateComponent(component)).thenReturn(component);
    
    // run test
    componentCRUDService.discardComponent(1L, discardReason, reasonText);
    
    // check asserts
    Assert.assertEquals("Component is now removed from stock", InventoryStatus.REMOVED, component.getInventoryStatus());
  }
}
