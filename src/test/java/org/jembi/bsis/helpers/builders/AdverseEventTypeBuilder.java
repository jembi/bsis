package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.model.adverseevent.AdverseEventType;

public class AdverseEventTypeBuilder extends AbstractEntityBuilder<AdverseEventType> {

  private static int index = 0;

  private UUID id;
  private String name = "adverse.event.type." + index++;
  private String description;
  private boolean deleted;

  public AdverseEventTypeBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public AdverseEventTypeBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public AdverseEventTypeBuilder withDescription(String description) {
    this.description = description;
    return this;
  }

  public AdverseEventTypeBuilder thatIsDeleted() {
    deleted = true;
    return this;
  }

  public AdverseEventTypeBuilder thatIsNotDeleted() {
    deleted = false;
    return this;
  }

  @Override
  public AdverseEventType build() {
    AdverseEventType adverseEventType = new AdverseEventType();
    adverseEventType.setId(id);
    adverseEventType.setName(name);
    adverseEventType.setDescription(description);
    adverseEventType.setDeleted(deleted);
    return adverseEventType;
  }

  public static AdverseEventTypeBuilder anAdverseEventType() {
    return new AdverseEventTypeBuilder();
  }

}
