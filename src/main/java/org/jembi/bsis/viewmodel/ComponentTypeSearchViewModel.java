package org.jembi.bsis.viewmodel;

import org.jembi.bsis.model.componenttype.ComponentTypeTimeUnits;

public class ComponentTypeSearchViewModel extends ComponentTypeViewModel {

  private Integer expiresAfter;
  private Boolean canBeIssued;
  private Boolean isDeleted;
  private ComponentTypeTimeUnits expiresAfterUnits = ComponentTypeTimeUnits.DAYS;
  private boolean containsPlasma;;

  public Integer getExpiresAfter() {
    return expiresAfter;
  }

  public void setExpiresAfter(Integer expiresAfter) {
    this.expiresAfter = expiresAfter;
  }

  public Boolean getCanBeIssued() {
    return canBeIssued;
  }

  public void setCanBeIssued(Boolean canBeIssued) {
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

  public boolean getIsContainsPlasma() {
    return containsPlasma;
  }

  public void setIsContainsPlasma(boolean containsPlasma) {
    this.containsPlasma = containsPlasma;
  }
}
