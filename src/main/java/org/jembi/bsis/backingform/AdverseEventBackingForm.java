package org.jembi.bsis.backingform;

import java.util.UUID;

import javax.validation.constraints.NotNull;

public class AdverseEventBackingForm {

  private UUID id;
  private AdverseEventTypeBackingForm typeBackingForm;
  private String comment;

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getId() {
    return id;
  }

  @NotNull
  public void setType(AdverseEventTypeBackingForm typeBackingForm) {
    this.typeBackingForm = typeBackingForm;
  }

  public AdverseEventTypeBackingForm getType() {
    return typeBackingForm;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getComment() {
    return comment;
  }
}
