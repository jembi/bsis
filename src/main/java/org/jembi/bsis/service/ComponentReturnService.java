package org.jembi.bsis.service;

import java.util.Date;

import javax.transaction.Transactional;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componentmovement.ComponentStatusChange;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonCategory;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeType;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.ComponentStatusChangeReasonRepository;
import org.jembi.bsis.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ComponentReturnService {
  
  @Autowired
  private ComponentCRUDService componentCRUDService;
  @Autowired
  private DateGeneratorService dateGeneratorService;
  @Autowired
  private ComponentStatusChangeReasonRepository componentStatusChangeReasonRepository;

  public Component returnComponent(Component component, Location returnedTo) {
    if (!returnedTo.getIsDistributionSite()) {
      throw new IllegalArgumentException(
          "Can't return a component to a location which is not a distribution site: " + returnedTo);
    }

    component.setStatus(ComponentStatus.AVAILABLE);
    component.setInventoryStatus(InventoryStatus.IN_STOCK);
    component.setLocation(returnedTo);

    // Create a component status change for the component
    Date now = dateGeneratorService.generateDate();
    ComponentStatusChange statusChange = new ComponentStatusChange();
    statusChange.setStatusChangeType(ComponentStatusChangeType.RETURNED);
    statusChange.setNewStatus(component.getStatus());
    statusChange.setStatusChangedOn(now);
    ComponentStatusChangeReason statusChangeReason = componentStatusChangeReasonRepository
        .findFirstComponentStatusChangeReasonForCategory(ComponentStatusChangeReasonCategory.RETURNED);
    statusChange.setStatusChangeReason(statusChangeReason);
    statusChange.setChangedBy(SecurityUtils.getCurrentUser());
    component.addStatusChange(statusChange);

    return componentCRUDService.updateComponent(component);
  }

}
