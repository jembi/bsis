package org.jembi.bsis.viewmodel;

import java.util.Objects;

public class TransfusionReactionTypeFullViewModel extends TransfusionReactionTypeViewModel {

  private String description;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object other) {

    if (!(other instanceof TransfusionReactionTypeFullViewModel)) {
      return false;
    }
    // FIXME: use super method to remove duplicate code.
    TransfusionReactionTypeFullViewModel otherViewModel = (TransfusionReactionTypeFullViewModel) other;
    return Objects.equals(otherViewModel.getId(), getId()) 
        && Objects.equals(otherViewModel.getName(), getName())
        && Objects.equals(otherViewModel.getIsDeleted(), getIsDeleted())
        && Objects.equals(otherViewModel.getDescription(), getDescription());
  }
}
