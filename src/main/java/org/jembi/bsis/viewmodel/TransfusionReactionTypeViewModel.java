package org.jembi.bsis.viewmodel;

import java.util.UUID;

public class TransfusionReactionTypeViewModel {

  private UUID id;
  private String name;
  private boolean isDeleted = false;

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

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
