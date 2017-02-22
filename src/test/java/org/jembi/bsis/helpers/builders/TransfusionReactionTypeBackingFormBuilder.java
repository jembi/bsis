package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.backingform.TransfusionReactionTypeBackingForm;

public class TransfusionReactionTypeBackingFormBuilder extends AbstractBuilder<TransfusionReactionTypeBackingForm> {

  private Long id;
  private String name;
  private String description;
  private boolean isDeleted = false;

  public TransfusionReactionTypeBackingFormBuilder withId(long id) {
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

  public TransfusionReactionTypeBackingFormBuilder withIsDeleted(boolean isDeleted) {
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
