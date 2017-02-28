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
    // There must be a componentBatch attached to the component
    if (!component.hasComponentBatch()){
      return false;
    }
    
    // Check component status is allowed to discard
    return CAN_DISCARD_OR_PROCESS_OR_RECORD_WEIGHT_STATUSES.contains(component.getStatus());
  }
  
  public boolean canUndiscard(Component component) {
    // Check component status is allowed to undiscard
    return component.getStatus() == ComponentStatus.DISCARDED;
  }

  public boolean canPreProcess(Component component) {
    // There must be a componentBatch attached to the component
    if (!component.hasComponentBatch()){
      return false;
    }
    
    // Only initial components can record weight
    if (!component.isInitialComponent()) {
      return false;
    }
    // Check component status is allowed to record weight
    return CAN_DISCARD_OR_PROCESS_OR_RECORD_WEIGHT_STATUSES.contains(component.getStatus());
  }

  public boolean canProcess(Component component) {
    // There must be a componentBatch attached to the component
    if (!component.hasComponentBatch()){
      return false;
    }

    // There must be component type combinations for that component type to be able to process it
    if (component.getComponentType().getProducedComponentTypeCombinations() == null
        || component.getComponentType().getProducedComponentTypeCombinations().size() == 0) {
      return false;
    }
    // Check component status is allowed to process
    return CAN_DISCARD_OR_PROCESS_OR_RECORD_WEIGHT_STATUSES.contains(component.getStatus());
  }

  public boolean canRecordChildComponentWeight(Component component) {
    // Check that is a child component
    if (component.getParentComponent() == null) {
      return false;
    }

    // Check that parent's weight has been recorded
    if (component.getParentComponent().getWeight() == null) {
      return false;
    }

    // Check that parent's status is processed
    if (!component.getParentComponent().getStatus().equals(ComponentStatus.PROCESSED)) {
      return false;
    }

    // Check that is not in stock
    if (component.getInventoryStatus().equals(InventoryStatus.IN_STOCK)) {
      return false;
    }

    // Check component status is allowed to process
    return CAN_DISCARD_OR_PROCESS_OR_RECORD_WEIGHT_STATUSES.contains(component.getStatus());
  }

  public boolean canUnprocess(Component parentComponent) {

    if (!parentComponent.getStatus().equals(ComponentStatus.PROCESSED)) {
      return false;
    }

    // Get child components
    List<Component> components = componentRepository.findChildComponents(parentComponent);
    for (Component component : components) {
      // Check that status is correct and it hasn't been labelled (not in stock)
      if (!(CAN_DISCARD_OR_PROCESS_OR_RECORD_WEIGHT_STATUSES.contains(component.getStatus())
          && component.getInventoryStatus().equals(InventoryStatus.NOT_IN_STOCK))) {
        return false;
      }
    }

    return true;
  }

  public boolean canTransfuse(Component component) {
    if (component.getStatus().equals(ComponentStatus.ISSUED)) {
      // component should be issued before it can be transfused
      return true;
    }
    return false;
  }
}