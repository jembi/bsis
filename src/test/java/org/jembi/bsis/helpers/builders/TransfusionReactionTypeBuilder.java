package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.model.transfusion.TransfusionReactionType;

public class TransfusionReactionTypeBuilder extends AbstractEntityBuilder<TransfusionReactionType> {

  private UUID id;
  private String name = "default.transfusionreactiontype.name";
  private String description = "default description";
  private boolean isDeleted = false;

  public TransfusionReactionTypeBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public TransfusionReactionTypeBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public TransfusionReactionTypeBuilder withDescription(String description) {
    this.description = description;
    return this;
  }

  public TransfusionReactionTypeBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }

  public TransfusionReactionTypeBuilder withIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
    return this;
  }

  @Override
  public TransfusionReactionType build() {
    TransfusionReactionType transfusionReactionType = new TransfusionReactionType();
    transfusionReactionType.setId(id);
    transfusionReactionType.setName(name);
    transfusionReactionType.setDescription(description);
    transfusionReactionType.setIsDeleted(isDeleted);
    return transfusionReactionType;
  }

  public static TransfusionReactionTypeBuilder aTransfusionReactionType() {
    return new TransfusionReactionTypeBuilder();
  }

}
