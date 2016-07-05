package org.jembi.bsis.helpers.builders;

import java.util.Date;

import org.jembi.bsis.model.componentmovement.ComponentStatusChange;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeType;

public class ComponentStatusChangeBuilder extends AbstractBuilder<ComponentStatusChange> {
  
  private Long id;
  private ComponentStatusChangeType statusChangeType;
  private Date statusChangedOn;
  
  public ComponentStatusChangeBuilder withId(Long id) {
    this.id = id;
    return this;
  }
  
  public ComponentStatusChangeBuilder withStatusChangeType(ComponentStatusChangeType statusChangeType) {
    this.statusChangeType = statusChangeType;
    return this;
  }
  
  public ComponentStatusChangeBuilder withStatusChangedOn(Date statusChangedOn) {
    this.statusChangedOn = statusChangedOn;
    return this;
  }

  @Override
  public ComponentStatusChange build() {
    ComponentStatusChange entity = new ComponentStatusChange();
    entity.setId(id);
    entity.setStatusChangedOn(statusChangedOn);
    entity.setStatusChangeType(statusChangeType);
    return entity;
  }

  public static ComponentStatusChangeBuilder aComponentStatusChange() {
    return new ComponentStatusChangeBuilder();
  }

  public static ComponentStatusChangeBuilder aDiscardedStatusChange() {
    return new ComponentStatusChangeBuilder()
        .withStatusChangeType(ComponentStatusChangeType.DISCARDED);
  }
  
  public static ComponentStatusChangeBuilder aReturnedStatusChange() {
    return new ComponentStatusChangeBuilder()
        .withStatusChangeType(ComponentStatusChangeType.RETURNED);
  }
}
