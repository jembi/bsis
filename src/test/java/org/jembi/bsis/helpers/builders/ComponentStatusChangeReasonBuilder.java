package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonCategory;

public class ComponentStatusChangeReasonBuilder extends AbstractEntityBuilder<ComponentStatusChangeReason> {

  private Long id;
  private String statusChangeReason;
  private ComponentStatusChangeReasonCategory category;
  private Boolean isDeleted = false;

  public ComponentStatusChangeReasonBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public ComponentStatusChangeReasonBuilder withStatusChangeReason(String statusChangeReason) {
    this.statusChangeReason = statusChangeReason;
    return this;
  }
  
  
  public ComponentStatusChangeReasonBuilder withComponentStatusChangeReasonCategory(
      ComponentStatusChangeReasonCategory category) {
    this.category = category;
    return this;
  }

  @Override
  public ComponentStatusChangeReason build() {
    ComponentStatusChangeReason entity = new ComponentStatusChangeReason();
    entity.setId(id);
    entity.setCategory(category);
    entity.setIsDeleted(isDeleted);
    entity.setStatusChangeReason(statusChangeReason);
    return entity;
  }

  public static ComponentStatusChangeReasonBuilder aComponentStatusChangeReason() {
    return new ComponentStatusChangeReasonBuilder();
  }

  public static ComponentStatusChangeReasonBuilder aDiscardReason() {
    return new ComponentStatusChangeReasonBuilder()
        .withComponentStatusChangeReasonCategory(ComponentStatusChangeReasonCategory.DISCARDED);
  }

  public static ComponentStatusChangeReasonBuilder aReturnReason() {
    return new ComponentStatusChangeReasonBuilder()
        .withComponentStatusChangeReasonCategory(ComponentStatusChangeReasonCategory.RETURNED);
  }

}
