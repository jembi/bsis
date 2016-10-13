package org.jembi.bsis.viewmodel;

public class ComponentTypeCombinationSearchViewModel extends BaseViewModel {
  
  private String combinationName;
  private boolean isDeleted;

  public String getCombinationName() {
    return this.combinationName;
  }

  public void setCombinationName(String combinationName) {
    this.combinationName = combinationName;
  }

  public boolean isDeleted() {
    return isDeleted;
  }

  public void setDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
  
}
