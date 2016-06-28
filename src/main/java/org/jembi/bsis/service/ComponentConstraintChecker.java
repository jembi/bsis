package org.jembi.bsis.service;

import java.util.Arrays;
import java.util.List;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ComponentConstraintChecker {
  
  // Statuses for which a component can be discarded or processed
  private static final List<ComponentStatus> CAN_DISCARD_OR_PROCESS_STATUSES = Arrays.asList(
      ComponentStatus.QUARANTINED,
      ComponentStatus.AVAILABLE,
      ComponentStatus.UNSAFE,
      ComponentStatus.EXPIRED);
  
  public boolean canDiscard(Component component) {
    return CAN_DISCARD_OR_PROCESS_STATUSES.contains(component.getStatus());
  }

  public boolean canRecordWeight(Component component) {
    return component.getParentComponent() == null;
  }

  public boolean canProcess(Component component) {
    if (component.getParentComponent() == null && component.getWeight() == null) {
      // Can't process initial components with no recorded weight
      return false;
    }
    return CAN_DISCARD_OR_PROCESS_STATUSES.contains(component.getStatus());
  }

}
