package org.jembi.bsis.repository.bloodtesting;

public enum BloodTypingStatus {

  /** Default status - Both ABO/Rh tests have not been done */
  NOT_DONE,
  /** A first time donor that has ABO/Rh repeat tests pending */
  PENDING_TESTS,
  /** All the ABO/Rh blood tests have been done */
  COMPLETE,
  /** Either ABO or Rh tests have a result of NT (not tested) */
  INDETERMINATE;
  
  public boolean isEndState() {
    if (this == COMPLETE || this == INDETERMINATE) {
      return true;
    }
    return false;
  }
}