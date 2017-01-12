package org.jembi.bsis.model.donation;

public enum BloodTypingMatchStatus {

  /** Default - ABO/Rh test outcomes have not been captured */
  NOT_DONE,
  /** For first time donors, there is a missing ABO/Rh outcome */
  NO_MATCH,
  /** For first time donors, the repeat ABO/Rh outcomes did not match the initial outcomes
   * For repeat donors, the ABO/Rh outcomes do no match the donor's ABO/Rh */
  AMBIGUOUS,
  /** For first time donors, the repeat ABO/Rh outcomes matched the initial outcomes. 
   * For repeat donors, the ABO/Rh outcomes matched the donor's ABO/Rh */
  MATCH,
  /** ABO/Rh discrepancies (ambiguous outcomes) have been manually resolved */
  RESOLVED,
  /** ABO/Rh discrepancies cannot be resolved */
  NO_TYPE_DETERMINED,
  /** Either ABO or Rh tests have a result of NT (not tested) */
  INDETERMINATE;

  public static boolean isEndState(BloodTypingMatchStatus status) {
    if (status == MATCH || status == RESOLVED || status == NO_TYPE_DETERMINED || status == INDETERMINATE) {
      return true;
    }
    return false;
  }
  
  public static boolean isResolvedState(BloodTypingMatchStatus status) {
    if (status == RESOLVED || status == NO_TYPE_DETERMINED) {
      return true;
    }
    return false;
  }
  
  public static boolean isBloodGroupConfirmed(BloodTypingMatchStatus status) {
    if (status == MATCH || status == RESOLVED) {
      return true;
    }
    return false;
  }
}
