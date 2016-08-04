package org.jembi.bsis.helpers.builders;

import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.aComponentStatusChangeReason;

import java.util.Date;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componentmovement.ComponentStatusChange;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;

public class ComponentStatusChangeBuilder extends AbstractBuilder<ComponentStatusChange> {
  
  private Long id;
  private Date statusChangedOn;
  private ComponentStatusChangeReason statusChangeReason = aComponentStatusChangeReason().build();
  private ComponentStatus newStatus = ComponentStatus.AVAILABLE;
  private Component component;
  private boolean isDeleted = false;
  
  public ComponentStatusChangeBuilder withId(Long id) {
    this.id = id;
    return this;
  }
  
  public ComponentStatusChangeBuilder withStatusChangedOn(Date statusChangedOn) {
    this.statusChangedOn = statusChangedOn;
    return this;
  }
  
  public ComponentStatusChangeBuilder withStatusChangeReason(ComponentStatusChangeReason statusChangeReason) {
    this.statusChangeReason = statusChangeReason;
    return this;
  }
  
  public ComponentStatusChangeBuilder withComponent(Component component) {
    this.component = component;
    return this;
  }
  
  public ComponentStatusChangeBuilder withNewStatus(ComponentStatus newStatus) {
    this.newStatus = newStatus;
    return this;
  }
  
  public ComponentStatusChangeBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }

  @Override
  public ComponentStatusChange build() {
    ComponentStatusChange entity = new ComponentStatusChange();
    entity.setId(id);
    entity.setNewStatus(newStatus);
    entity.setStatusChangedOn(statusChangedOn);
    entity.setStatusChangeReason(statusChangeReason);
    entity.setComponent(component);
    entity.setIsDeleted(isDeleted);
    return entity;
  }

  public static ComponentStatusChangeBuilder aComponentStatusChange() {
    return new ComponentStatusChangeBuilder();
  }
}
