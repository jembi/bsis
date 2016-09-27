package org.jembi.bsis.viewmodel;

import org.jembi.bsis.model.componenttype.ComponentTypeTimeUnits;

public class ComponentTypeSearchViewModel extends ComponentTypeViewModel {

  private Integer expiresAfter;
  private boolean canBeIssued;
  private boolean isDeleted;
  private ComponentTypeTimeUnits expiresAfterUnits = ComponentTypeTimeUnits.DAYS;
  private boolean containsPlasma;

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

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public ComponentTypeTimeUnits getExpiresAfterUnits() {
    return expiresAfterUnits;
  }

  public void setExpiresAfterUnits(ComponentTypeTimeUnits expiresAfterUnits) {
    this.expiresAfterUnits = expiresAfterUnits;
  }

  public boolean getContainsPlasma() {
    return containsPlasma;
  }

  public void setContainsPlasma(boolean containsPlasma) {
    this.containsPlasma = containsPlasma;
  }
}
