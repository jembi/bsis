package org.jembi.bsis.model.donation;

public enum BloodTypingStatus {

  /** Default status - Both ABO/Rh tests have not been done */
  NOT_DONE,
  /** A first time donor that has ABO/Rh repeat tests pending */
  PENDING_TESTS,
  /** All the ABO/Rh blood tests have been done */
  COMPLETE;

}