package org.jembi.bsis.viewmodel;

import org.jembi.bsis.model.componenttype.ComponentTypeTimeUnits;

public class ComponentTypeSearchViewModel extends ComponentTypeViewModel {

  private int expiresAfter;
  private boolean canBeIssued;
  private boolean isDeleted;
  private ComponentTypeTimeUnits expiresAfterUnits = ComponentTypeTimeUnits.DAYS;

  public Integer getExpiresAfter() {
    return expiresAfter;
  }

  public void setExpiresAfter(Integer expiresAfter) {
    this.expiresAfter = expiresAfter;
  }

  public boolean getCanBeIssued() {
    return canBeIssued;
  }

  public void setCanBeIssued(boolean canBeIssued) {
    this.canBeIssued = canBeIssued;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public ComponentTypeTimeUnits getExpiresAfterUnits() {
    return expiresAfterUnits;
  }

  public void setExpiresAfterUnits(ComponentTypeTimeUnits expiresAfterUnits) {
    this.expiresAfterUnits = expiresAfterUnits;
  }
}
