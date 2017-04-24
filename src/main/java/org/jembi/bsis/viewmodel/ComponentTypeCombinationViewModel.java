package org.jembi.bsis.viewmodel;

import java.util.UUID;

public class ComponentTypeCombinationViewModel extends BaseViewModel<UUID> {
  
  private String combinationName;
  private boolean isDeleted;

  public String getCombinationName() {
    return this.combinationName;
  }

  public void setCombinationName(String combinationName) {
    this.combinationName = combinationName;
  }

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
  
}
