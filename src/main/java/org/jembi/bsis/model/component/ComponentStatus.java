package org.jembi.bsis.model.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ComponentStatus {

  QUARANTINED, AVAILABLE, EXPIRED, ISSUED, TRANSFUSED, UNSAFE, DISCARDED, PROCESSED;
  
  public static boolean isFinalStatus(ComponentStatus status) {

    if (status == ComponentStatus.DISCARDED || status == ComponentStatus.ISSUED || status == ComponentStatus.TRANSFUSED
       || status == ComponentStatus.PROCESSED || status == ComponentStatus.UNSAFE) {
      return true;
    }

    return false;
  }
  
  public static boolean isUsedStatus(ComponentStatus status) {
    if (status == ComponentStatus.TRANSFUSED) {
      return true;
    }
    return false;
  }

  /**
   * Returns only list of component statuses related to components (as opposed to e.g. inventory status)
   * AVAILABLE, QUARANTINED, UNSAFE
   *
   * @return
   */
  public static List<ComponentStatus> getComponentRelatedStatuses() {
    return  new ArrayList<>(Arrays.asList(AVAILABLE, QUARANTINED, UNSAFE));
  }
}
