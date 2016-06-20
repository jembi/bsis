package controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import model.component.Component;
import model.component.ComponentStatus;

@Transactional(readOnly = true)
@Service
public class ComponentConstraintChecker {
  
  // Statuses for which a component can be discarded
  private static final List<ComponentStatus> CAN_DISCARD_STATUSES = Arrays.asList(
      ComponentStatus.QUARANTINED,
      ComponentStatus.AVAILABLE,
      ComponentStatus.UNSAFE,
      ComponentStatus.EXPIRED);
  
  public boolean canDiscard(Component component) {
    return CAN_DISCARD_STATUSES.contains(component.getStatus());
  }

}
