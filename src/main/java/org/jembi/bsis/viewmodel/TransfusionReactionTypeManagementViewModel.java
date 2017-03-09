package org.jembi.bsis.viewmodel;

import java.util.Objects;

public class TransfusionReactionTypeManagementViewModel extends TransfusionReactionTypeViewModel {

  private String description;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object other) {

    if (!(other instanceof TransfusionReactionTypeManagementViewModel)) {
      return false;
    }
    // FIXME: use super method to remove duplicate code.
    TransfusionReactionTypeManagementViewModel otherViewModel = (TransfusionReactionTypeManagementViewModel) other;
    return Objects.equals(otherViewModel.getId(), getId()) 
        && Objects.equals(otherViewModel.getName(), getName())
        && Objects.equals(otherViewModel.getIsDeleted(), getIsDeleted())
        && Objects.equals(otherViewModel.getDescription(), getDescription());
  }
}
