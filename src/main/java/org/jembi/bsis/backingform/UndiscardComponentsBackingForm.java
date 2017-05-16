package org.jembi.bsis.backingform;

import java.util.List;
import java.util.UUID;

public class UndiscardComponentsBackingForm {
  
  private List<UUID> componentIds;

  public List<UUID> getComponentIds() {
    return componentIds;
  }

  public void setComponentIds(List<UUID> componentIds) {
    this.componentIds = componentIds;
  }

}
