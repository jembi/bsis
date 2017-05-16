package org.jembi.bsis.viewmodel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LabellingViewModel extends BaseViewModel<UUID> {

  private String componentName;
  private String componentCode;
  private Map<String, Boolean> permissions = new HashMap<>();
  private boolean hasComponentBatch;

  public boolean getHasComponentBatch () {
    return hasComponentBatch;
  }

  public void setHasComponentBatch (boolean hasComponentBatch) {
    this.hasComponentBatch = hasComponentBatch;
  }

  public String getComponentName() {
    return componentName;
  }

  public void setComponentName(String componentName) {
    this.componentName = componentName;
  }

  public String getComponentCode() {
    return componentCode;
  }

  public void setComponentCode(String componentCode) {
    this.componentCode = componentCode;
  }

  public Map<String, Boolean> getPermissions() {
    return permissions;
  }

  public void setPermissions(Map<String, Boolean> permissions) {
    this.permissions = permissions;
  }

}
