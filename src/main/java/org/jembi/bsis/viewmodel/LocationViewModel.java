package org.jembi.bsis.viewmodel;

public class LocationViewModel extends BaseViewModel {

  private String name;
  private boolean isDeleted;

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
