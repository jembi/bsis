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
public class ComponentDispatchService {
  
  @Autowired
  private ComponentCRUDService componentCRUDService;
  @Autowired
  private DateGeneratorService dateGeneratorService;
  @Autowired
  private ComponentStatusChangeReasonRepository componentStatusChangeReasonRepository;
  
  public Component issueComponent(Component component, Location issueTo) {
    if (!issueTo.getIsUsageSite()) {
      throw new IllegalArgumentException("Can't issue a component to a location which is not a usage site: " + issueTo);
    }
    
    Date issuedDate = dateGeneratorService.generateDate();
    
    component.setInventoryStatus(InventoryStatus.REMOVED);
    component.setStatus(ComponentStatus.ISSUED);
    component.setIssuedOn(issuedDate);
    component.setLocation(issueTo);

    // Create a component status change for the component
    ComponentStatusChange statusChange = new ComponentStatusChange();
    statusChange.setStatusChangeType(ComponentStatusChangeType.ISSUED);
    statusChange.setNewStatus(ComponentStatus.ISSUED);
    statusChange.setStatusChangedOn(issuedDate);
    ComponentStatusChangeReason statusChangeReason = componentStatusChangeReasonRepository
        .findFirstComponentStatusChangeReasonForCategory(ComponentStatusChangeReasonCategory.ISSUED);
    statusChange.setStatusChangeReason(statusChangeReason);
    statusChange.setChangedBy(SecurityUtils.getCurrentUser());
    component.addStatusChange(statusChange);

    return componentCRUDService.updateComponent(component);
  }
  
  public Component transferComponent(Component component, Location transferTo) {
    if (!transferTo.getIsDistributionSite()) {
      throw new IllegalArgumentException("Can't transfer a component to a location which is not a distribution site: " + transferTo);
    }
    component.setLocation(transferTo);
    return componentCRUDService.updateComponent(component);
  }

}
