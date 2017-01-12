package org.jembi.bsis.viewmodel;

import java.util.HashMap;
import java.util.Map;

public class DivisionFullViewModel extends DivisionViewModel {

  Map<String, Boolean> permissions = new HashMap<>();

  public Map<String, Boolean> getPermissions() {
    return permissions;
  }

  public void setPermissions(Map<String, Boolean> permissions) {
    this.permissions = permissions;
  }

  public void setPermission(String key, boolean value) {
    permissions.put(key, value);
  }

}
