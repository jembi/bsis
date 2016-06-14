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
