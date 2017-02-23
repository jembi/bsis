package org.jembi.bsis.viewmodel;

import java.util.Objects;

public class TransfusionReactionTypeViewModel {

  private long id;
  private String name;
  private boolean isDeleted = false;

  public long getId() {
    return id;
  }

  public void setId(long id) {
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

  @Override
  public boolean equals(Object other) {

    if (!(other instanceof TransfusionReactionTypeViewModel)) {
      return false;
    }

    TransfusionReactionTypeViewModel otherViewModel = (TransfusionReactionTypeViewModel) other;
    return Objects.equals(otherViewModel.getId(), getId()) 
        && Objects.equals(otherViewModel.getName(), getName())
        && Objects.equals(otherViewModel.getIsDeleted(), getIsDeleted());
  }
}
