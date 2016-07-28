package org.jembi.bsis.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReturnFormFullViewModel extends ReturnFormViewModel {

  private List<ComponentFullViewModel> components = new ArrayList<>();
  private Map<String, Boolean> permissions = new HashMap<>();

  public List<ComponentFullViewModel> getComponents() {
    return components;
  }

  public void setComponents(List<ComponentFullViewModel> components) {
    this.components = components;
  }

  public Map<String, Boolean> getPermissions() {
    return permissions;
  }

  public void setPermissions(Map<String, Boolean> permissions) {
    this.permissions = permissions;
  }

}
