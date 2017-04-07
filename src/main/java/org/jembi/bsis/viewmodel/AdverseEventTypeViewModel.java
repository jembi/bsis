package org.jembi.bsis.viewmodel;

import java.util.UUID;

public class AdverseEventTypeViewModel extends BaseViewModel<UUID> {
  private String name;
  private String description;
  private boolean isDeleted;

  public AdverseEventTypeViewModel() {
    // Default constructor
  }

  public AdverseEventTypeViewModel(UUID id, String name, String description, boolean isDeleted) {
    this.setId(id);
    this.name = name;
    this.description = description;
    this.isDeleted = isDeleted;
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
