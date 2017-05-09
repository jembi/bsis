package org.jembi.bsis.model.donation;

public enum TTIStatus {
  NOT_DONE, SAFE, UNSAFE, INDETERMINATE;

  public static boolean makesComponentsUnsafe(TTIStatus status) {

    if (status == UNSAFE || status == INDETERMINATE) {
      return true;
    }

    return false;
  }
}
