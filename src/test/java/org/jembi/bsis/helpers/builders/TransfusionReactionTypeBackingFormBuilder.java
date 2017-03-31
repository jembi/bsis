package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.backingform.TransfusionReactionTypeBackingForm;

public class TransfusionReactionTypeBackingFormBuilder extends AbstractBuilder<TransfusionReactionTypeBackingForm> {

  private UUID id;
  private String name;
  private String description;
  private Boolean isDeleted = false;

  public TransfusionReactionTypeBackingFormBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public TransfusionReactionTypeBackingFormBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public TransfusionReactionTypeBackingFormBuilder withDescription(String description) {
    this.description = description;
    return this;
  }

  public TransfusionReactionTypeBackingFormBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }

  public TransfusionReactionTypeBackingFormBuilder withIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
    return this;
  }

  @Override
  public TransfusionReactionTypeBackingForm build() {
    TransfusionReactionTypeBackingForm transfusionReactionType = new TransfusionReactionTypeBackingForm();
    transfusionReactionType.setId(id);
    transfusionReactionType.setName(name);
    transfusionReactionType.setDescription(description);
    transfusionReactionType.setIsDeleted(isDeleted);
    return transfusionReactionType;
  }

  public static TransfusionReactionTypeBackingFormBuilder aTransfusionReactionTypeBackingForm() {
    return new TransfusionReactionTypeBackingFormBuilder();
  }

}
