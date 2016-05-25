package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backingform.RecordComponentBackingForm;
import factory.ComponentViewModelFactory;
import model.component.Component;
import model.component.ComponentStatus;
import model.componentmovement.ComponentStatusChange;
import model.componentmovement.ComponentStatusChangeReason;
import model.componentmovement.ComponentStatusChangeType;
import model.componenttype.ComponentType;
import model.componenttype.ComponentTypeTimeUnits;
import model.donation.Donation;
import model.donor.Donor;
import model.inventory.InventoryStatus;
import repository.ComponentRepository;
import utils.SecurityUtils;
import viewmodel.ComponentViewModel;

@Transactional
@Service
public class ComponentCRUDService {

  private static final Logger LOGGER = Logger.getLogger(ComponentCRUDService.class);
  private static final List<ComponentStatus> UPDATABLE_STATUSES = Arrays.asList(ComponentStatus.AVAILABLE,
      ComponentStatus.QUARANTINED);

  @Autowired
  private ComponentRepository componentRepository;
  @Autowired
  private ComponentViewModelFactory componentViewModelFactory;

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

  public Component processComponent(RecordComponentBackingForm recordComponentForm) {
    Component parentComponent =
        componentRepository.findComponentById(Long.valueOf(recordComponentForm.getParentComponentId()));
    Donation donation = parentComponent.getDonation();
    ComponentStatus parentStatus = parentComponent.getStatus();
    long componentId = Long.valueOf(recordComponentForm.getParentComponentId());

    // map of new components, storing component type and num. of units
    Map<ComponentType, Integer> newComponents = new HashMap<ComponentType, Integer>();

    // iterate over components in combination, adding them to the new components map, along with the
    // num. of units of each component
    for (ComponentType pt : recordComponentForm.getComponentTypeCombination().getComponentTypes()) {
      boolean check = false;
      for (ComponentType ptm : newComponents.keySet()) {
        if (pt.getId() == ptm.getId()) {
          Integer count = newComponents.get(ptm) + 1;
          newComponents.put(ptm, count);
          check = true;
          break;
        }
      }
      if (!check) {
        newComponents.put(pt, 1);
      }
    }

    // If the parent is unsafe then set new components to unsafe as well
    ComponentStatus initialComponentStatus =
        parentStatus == ComponentStatus.UNSAFE ? ComponentStatus.UNSAFE : ComponentStatus.QUARANTINED;

    for (ComponentType pt : newComponents.keySet()) {

      String componentTypeCode = pt.getComponentTypeCode();
      int noOfUnits = newComponents.get(pt);

      // Add New component
      if (!parentStatus.equals(ComponentStatus.PROCESSED) && !parentStatus.equals(ComponentStatus.DISCARDED)) {

        for (int i = 1; i <= noOfUnits; i++) {
          Component component = new Component();
          component.setIsDeleted(false);

          // if there is more than one unit of the component, append unit number suffix
          if (noOfUnits > 1) {
            component.setComponentCode(componentTypeCode + "-0" + i);
          } else {
            component.setComponentCode(componentTypeCode);
          }
          component.setComponentType(pt);
          component.setDonation(donation);
          component.setParentComponent(parentComponent);
          component.setStatus(initialComponentStatus);
          component.setCreatedOn(donation.getDonationDate());
          component.setLocation(parentComponent.getLocation());

          Calendar cal = Calendar.getInstance();
          Date createdOn = cal.getTime();
          cal.setTime(component.getCreatedOn());

          // set component expiry date
          if (pt.getExpiresAfterUnits() == ComponentTypeTimeUnits.DAYS)
            cal.add(Calendar.DAY_OF_YEAR, pt.getExpiresAfter());
          else if (pt.getExpiresAfterUnits() == ComponentTypeTimeUnits.HOURS)
            cal.add(Calendar.HOUR, pt.getExpiresAfter());
          else if (pt.getExpiresAfterUnits() == ComponentTypeTimeUnits.YEARS)
            cal.add(Calendar.YEAR, pt.getExpiresAfter());

          Date expiresOn = cal.getTime();
          component.setCreatedOn(createdOn);
          component.setExpiresOn(expiresOn);

          componentRepository.addComponent(component);

          // Set source component status to PROCESSED
          componentRepository.setComponentStatusToProcessed(componentId);
        }
      }
    }

    return parentComponent;
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
  
  public ComponentViewModel findComponentByCodeAndDIN(String componentCode, String donationIdentificationNumber) {
    Component component = componentRepository.findComponentByCodeAndDIN(componentCode, donationIdentificationNumber);
    return componentViewModelFactory.createComponentViewModel(component);
  }
}
