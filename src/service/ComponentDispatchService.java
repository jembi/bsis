package service;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.component.Component;
import model.component.ComponentStatus;
import model.componentmovement.ComponentStatusChange;
import model.componentmovement.ComponentStatusChangeReason;
import model.componentmovement.ComponentStatusChangeReasonCategory;
import model.componentmovement.ComponentStatusChangeType;
import model.inventory.InventoryStatus;
import model.location.Location;
import repository.ComponentStatusChangeReasonRepository;
import utils.SecurityUtils;

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
