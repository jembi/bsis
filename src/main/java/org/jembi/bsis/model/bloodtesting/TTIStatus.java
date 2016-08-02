package org.jembi.bsis.model.bloodtesting;

public enum TTIStatus {
  NOT_DONE, TTI_SAFE, TTI_UNSAFE, INDETERMINATE;
  
  public static boolean makesComponentsUnsafe(TTIStatus status) {

    if (status == TTI_UNSAFE || status == INDETERMINATE) {
      return true;
    }

    return false;
  }
}
