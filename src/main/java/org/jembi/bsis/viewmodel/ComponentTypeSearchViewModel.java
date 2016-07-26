package org.jembi.bsis.viewmodel;

import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.componenttype.ComponentTypeTimeUnits;

public class ComponentTypeSearchViewModel extends ComponentTypeViewModel {

  public ComponentTypeSearchViewModel(ComponentType componentType) {
    super(componentType);
  }

  public Integer getExpiresAfter() {
    return componentType.getExpiresAfter();
  }
  
  public ComponentTypeTimeUnits getExpiresAfterUnits() {
    return componentType.getExpiresAfterUnits();
  }
  
  public boolean getCanBeIssued() {
    return componentType.getCanBeIssued();
  }
  
  public boolean getIsDeleted() {
    return componentType.getIsDeleted();
  }
}
