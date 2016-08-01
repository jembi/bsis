package org.jembi.bsis.helpers.builders;

import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.aComponentStatusChangeReason;

import java.util.Date;

import org.jembi.bsis.model.componentmovement.ComponentStatusChange;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;

public class ComponentStatusChangeBuilder extends AbstractBuilder<ComponentStatusChange> {
  
  private Long id;
  private Date statusChangedOn;
  private ComponentStatusChangeReason statusChangeReason = aComponentStatusChangeReason().build();
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
  
  public ComponentStatusChangeBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }

  @Override
  public ComponentStatusChange build() {
    ComponentStatusChange entity = new ComponentStatusChange();
    entity.setId(id);
    entity.setStatusChangedOn(statusChangedOn);
    entity.setStatusChangeReason(statusChangeReason);
    entity.setIsDeleted(isDeleted);
    return entity;
  }

  public static ComponentStatusChangeBuilder aComponentStatusChange() {
    return new ComponentStatusChangeBuilder();
  }
}
