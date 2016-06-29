package org.jembi.bsis.service;

import java.util.Arrays;
import java.util.List;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.inventory.InventoryStatus;
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
    return CAN_DISCARD_OR_PROCESS_OR_RECORD_WEIGHT_STATUSES.contains(component.getStatus());
  }

  public boolean canRecordWeight(Component component) {
    if (component.getParentComponent() != null) {
      return false;
    }
    return CAN_DISCARD_OR_PROCESS_OR_RECORD_WEIGHT_STATUSES.contains(component.getStatus());
  }

  public boolean canProcess(Component component) {
    if (component.getParentComponent() == null && component.getWeight() == null) {
      // Can't process initial components with no recorded weight
      return false;
    }
    if (component.getComponentType().getProducedComponentTypeCombinations() == null
        || component.getComponentType().getProducedComponentTypeCombinations().size() == 0) {
      // There must be component type combinations for that component type to be able to process it
      return false;
    }
    return CAN_DISCARD_OR_PROCESS_OR_RECORD_WEIGHT_STATUSES.contains(component.getStatus());
  }

  public boolean canUnprocess(Component parentComponent) {
    
    if (!parentComponent.getStatus().equals(ComponentStatus.PROCESSED)) {
      return false;
    }

    List<Component> components = componentRepository.findComponentsByDonationIdentificationNumber(parentComponent.getDonationIdentificationNumber());
    for (Component component : components) {
      // check that status is available and it hasn't been labelled (not in stock) for all child components
      if (component.getId() != parentComponent.getId()) {
        if (!(component.getStatus().equals(ComponentStatus.AVAILABLE) && component.getInventoryStatus().equals(InventoryStatus.NOT_IN_STOCK))) {
          return false;
        }
      }
    }

    return true;
  }

}
