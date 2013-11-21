package model.collectedsample;


public enum LotReleaseStatus {
	
	NOT_SUITABLE_FOR_RELEASE, SUITABLE_FOR_RELEASE, LABELLED, DISCARDED;

  /**
   * Alternative to valueOf when we want to assign a default LotReleaseStatus instead
   * of throwing an exception when an invalid string is passed.
   * @param statusStr
   * @return
   */
  public static LotReleaseStatus lookup(String statusStr) {
  	LotReleaseStatus status = null;
    try {
       status = LotReleaseStatus.valueOf(statusStr);
    } catch (IllegalArgumentException ex) {
      ex.printStackTrace();
      status = LotReleaseStatus.NOT_SUITABLE_FOR_RELEASE;
    }
    return status;
  }
}
