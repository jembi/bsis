package model.component;

public enum ComponentStatus {

  QUARANTINED, AVAILABLE, EXPIRED, ISSUED, SPLIT, USED, UNSAFE, DISCARDED, PROCESSED;

  /**
   * Alternative to valueOf when we want to assign a default status instead
   * of throwing an exception when an invalid string is passed.
   *
   * @param statusStr
   * @return
   */
  public static ComponentStatus lookup(String statusStr) {
    ComponentStatus status = null;
    try {
      status = ComponentStatus.valueOf(statusStr);
    } catch (IllegalArgumentException ex) {
      ex.printStackTrace();
      status = ComponentStatus.QUARANTINED;
    }
    return status;
  }
}
