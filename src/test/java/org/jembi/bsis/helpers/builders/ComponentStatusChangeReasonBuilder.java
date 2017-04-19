package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonCategory;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonType;

public class ComponentStatusChangeReasonBuilder extends AbstractEntityBuilder<ComponentStatusChangeReason> {

  private UUID id;
  private String statusChangeReason;
  private ComponentStatusChangeReasonCategory category;
  private Boolean isDeleted = false;
  private ComponentStatusChangeReasonType type = ComponentStatusChangeReasonType.INVALID_WEIGHT;

  public ComponentStatusChangeReasonBuilder withId(UUID id) {
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

  public ComponentStatusChangeReasonBuilder withComponentStatusChangeReasonType(ComponentStatusChangeReasonType type) {
    this.type = type;
    return this;
  }

  public ComponentStatusChangeReasonBuilder thatIsDeleted() {
    this.isDeleted = Boolean.TRUE;
    return this;
  }

  public ComponentStatusChangeReasonBuilder thatIsNotDeleted() {
    this.isDeleted = Boolean.FALSE;
    return this;
  }

  @Override
  public ComponentStatusChangeReason build() {
    ComponentStatusChangeReason entity = new ComponentStatusChangeReason();
    entity.setId(id);
    entity.setCategory(category);
    entity.setIsDeleted(isDeleted);
    entity.setStatusChangeReason(statusChangeReason);
    entity.setType(type);
    return entity;
  }

  public static ComponentStatusChangeReasonBuilder aComponentStatusChangeReason() {
    return new ComponentStatusChangeReasonBuilder();
  }

  public static ComponentStatusChangeReasonBuilder aDiscardReason() {
    return new ComponentStatusChangeReasonBuilder()
        .withComponentStatusChangeReasonCategory(ComponentStatusChangeReasonCategory.DISCARDED)
        .withComponentStatusChangeReasonType(null);
  }
  
  public static ComponentStatusChangeReasonBuilder anIssuedReason() {
    return new ComponentStatusChangeReasonBuilder()
        .withComponentStatusChangeReasonCategory(ComponentStatusChangeReasonCategory.ISSUED)
        .withComponentStatusChangeReasonType(null);
  }

  public static ComponentStatusChangeReasonBuilder aReturnReason() {
    return new ComponentStatusChangeReasonBuilder()
        .withComponentStatusChangeReasonCategory(ComponentStatusChangeReasonCategory.RETURNED)
        .withComponentStatusChangeReasonType(null);
  }

  public static ComponentStatusChangeReasonBuilder anUnsafeReason() {
    return new ComponentStatusChangeReasonBuilder()
        .withComponentStatusChangeReasonCategory(ComponentStatusChangeReasonCategory.UNSAFE);
  }

}
