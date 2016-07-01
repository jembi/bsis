package org.jembi.bsis.service;

import java.util.Arrays;
import java.util.List;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.repository.ComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ComponentConstraintChecker {

  @Autowired
  private ComponentRepository componentRepository;	

  // Statuses for which a component can be discarded or processed or weight can be recorded for
  private static final List<ComponentStatus> CAN_DISCARD_OR_PROCESS_OR_RECORD_WEIGHT_STATUSES = Arrays.asList(
      ComponentStatus.QUARANTINED,
      ComponentStatus.AVAILABLE,
      ComponentStatus.UNSAFE,
      ComponentStatus.EXPIRED);
  
  public boolean canDiscard(Component component) {
    // Check component status is allowed to discard
    return CAN_DISCARD_OR_PROCESS_OR_RECORD_WEIGHT_STATUSES.contains(component.getStatus());

  }

  public boolean canRecordWeight(Component component) {
    // Only initial components can record weight
    if (component.getParentComponent() != null) {
      return false;
    }
    // Check component status is allowed to record weight
    return CAN_DISCARD_OR_PROCESS_OR_RECORD_WEIGHT_STATUSES.contains(component.getStatus());
  }

  public boolean canProcess(Component component) {
    // If it's an initial component, check that weight is valid
    if (component.getParentComponent() == null) {
      if (!isWeightValid(component)) {
        return false;
      }
    }
    // There must be component type combinations for that component type to be able to process it
    if (component.getComponentType().getProducedComponentTypeCombinations() == null
        || component.getComponentType().getProducedComponentTypeCombinations().size() == 0) {
      return false;
    }
    // Check component status is allowed to process
    return CAN_DISCARD_OR_PROCESS_OR_RECORD_WEIGHT_STATUSES.contains(component.getStatus());
  }

  public boolean canUnprocess(Component parentComponent) {

    if (!parentComponent.getStatus().equals(ComponentStatus.PROCESSED)) {
      return false;
    }

    List<Component> components = componentRepository.findComponentsByDonationIdentificationNumber(parentComponent.getDonationIdentificationNumber());
    for (Component component : components) {
      // check that status is correct and it hasn't been labelled (not in stock) for all child components
      if (component.getId() != parentComponent.getId()) {
        if (!(CAN_DISCARD_OR_PROCESS_OR_RECORD_WEIGHT_STATUSES.contains(component.getStatus())
            && component.getInventoryStatus().equals(InventoryStatus.NOT_IN_STOCK))) {
          return false;
        }
      }
    }

    return true;
  }

  private boolean isWeightValid(Component component) {
    if (component.getWeight() != null) {
      PackType packType = component.getDonation().getPackType();
      Integer weight = component.getWeight();
      if (weight <= packType.getMaxWeight() && weight >= packType.getMinWeight()) {
        return true;
      }
    }
    return false;
  }

}
