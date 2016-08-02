package org.jembi.bsis.model.component;

public enum ComponentStatus {

  QUARANTINED, AVAILABLE, EXPIRED, ISSUED, SPLIT, USED, UNSAFE, DISCARDED, PROCESSED;
  
  public static boolean isFinalStatus(ComponentStatus status) {

    if (status == ComponentStatus.DISCARDED || status == ComponentStatus.ISSUED || status == ComponentStatus.USED
        || status == ComponentStatus.SPLIT || status == ComponentStatus.PROCESSED || status == ComponentStatus.UNSAFE) {
      return true;
    }

    return false;
  }
}
