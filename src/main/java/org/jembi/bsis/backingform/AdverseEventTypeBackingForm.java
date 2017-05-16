package org.jembi.bsis.backingform;

import java.util.UUID;

import javax.validation.constraints.NotNull;

public class AdverseEventTypeBackingForm {

  private UUID id;
  @NotNull
  private String name;
  private String description;
  private boolean isDeleted;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

}
