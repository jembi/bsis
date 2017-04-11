package org.jembi.bsis.helpers.builders;

import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.aComponentStatusChangeReason;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.helpers.persisters.AbstractEntityPersister;
import org.jembi.bsis.helpers.persisters.ComponentStatusChangePersister;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componentmovement.ComponentStatusChange;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;

public class ComponentStatusChangeBuilder extends AbstractEntityBuilder<ComponentStatusChange> {
  
  private UUID id;
  private Date statusChangedOn;
  private ComponentStatusChangeReason statusChangeReason = aComponentStatusChangeReason().build();
  private ComponentStatus newStatus = ComponentStatus.AVAILABLE;
  private Component component;
  private boolean isDeleted = false;
  private String statusChangeReasonText;
  
  public ComponentStatusChangeBuilder withId(UUID id) {
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
  
  public ComponentStatusChangeBuilder withStatusChangeReasonText(String statusChangeReasonText) {
    this.statusChangeReasonText = statusChangeReasonText;
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
    entity.setStatusChangeReasonText(statusChangeReasonText);
    return entity;
  }

  @Override
  public AbstractEntityPersister<ComponentStatusChange> getPersister() {
    return new ComponentStatusChangePersister();
  }

  public static ComponentStatusChangeBuilder aComponentStatusChange() {
    return new ComponentStatusChangeBuilder();
  }
}
