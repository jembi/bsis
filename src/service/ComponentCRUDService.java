package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import model.component.Component;
import model.component.ComponentStatus;
import model.componentmovement.ComponentStatusChange;
import model.componentmovement.ComponentStatusChangeReason;
import model.componentmovement.ComponentStatusChangeType;
import model.donation.Donation;
import model.donor.Donor;
import model.inventory.InventoryStatus;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repository.ComponentRepository;
import repository.DonationRepository;
import utils.SecurityUtils;

@Transactional
@Service
public class ComponentCRUDService {

  private static final Logger LOGGER = Logger.getLogger(ComponentCRUDService.class);
  private static final List<ComponentStatus> UPDATABLE_STATUSES = Arrays.asList(ComponentStatus.AVAILABLE,
      ComponentStatus.QUARANTINED);

  @Autowired
  private ComponentRepository componentRepository;
  @Autowired
  private DonationRepository donationRepository;

  /**
   * Change the status of components belonging to the donor from AVAILABLE to UNSAFE.
   */
  public void markComponentsBelongingToDonorAsUnsafe(Donor donor) {

    LOGGER.info("Marking components as unsafe for donor: " + donor);

    componentRepository.updateComponentStatusesForDonor(UPDATABLE_STATUSES, ComponentStatus.UNSAFE, donor);
  }

  /**
   * Change the status of components linked to the donation from AVAILABLE to UNSAFE.
   */
  public void markComponentsBelongingToDonationAsUnsafe(Donation donation) {

    LOGGER.info("Marking components as unsafe for donation: " + donation);

    componentRepository.updateComponentStatusForDonation(UPDATABLE_STATUSES, ComponentStatus.UNSAFE, donation);
  }

  public void updateComponentStatusesForDonation(Donation donation) {

    LOGGER.info("Updating component statuses for donation: " + donation);

    for (Component component : donation.getComponents()) {

      if (!component.getIsDeleted() && componentRepository.updateComponentInternalFields(component)) {
        componentRepository.updateComponent(component);
      }
    }
  }

  public Component discardComponent(Long componentId, Long discardReasonId, String discardReasonText) {
    Component existingComponent = componentRepository.findComponentById(componentId);
    
    // update existing component status
    existingComponent.setStatus(ComponentStatus.DISCARDED);
    existingComponent.setDiscardedOn(new Date());
    
    // create a component status change for the component
    ComponentStatusChange statusChange = new ComponentStatusChange();
    statusChange.setStatusChangeType(ComponentStatusChangeType.DISCARDED);
    statusChange.setNewStatus(ComponentStatus.DISCARDED);
    statusChange.setStatusChangedOn(new Date());
    ComponentStatusChangeReason discardReason = new ComponentStatusChangeReason();
    discardReason.setId(discardReasonId);
    statusChange.setStatusChangeReason(discardReason);
    statusChange.setStatusChangeReasonText(discardReasonText);
    statusChange.setChangedBy(SecurityUtils.getCurrentUser());
    
    if (existingComponent.getStatusChanges() == null) {
      existingComponent.setStatusChanges(new ArrayList<ComponentStatusChange>());
    }
    existingComponent.getStatusChanges().add(statusChange);
    statusChange.setComponent(existingComponent);
    
    // remove component from inventory
    if (existingComponent.getInventoryStatus() == InventoryStatus.IN_STOCK) {
      existingComponent.setInventoryStatus(InventoryStatus.REMOVED);
    }
    
    componentRepository.updateComponent(existingComponent);
    
    return existingComponent;
  }
}
