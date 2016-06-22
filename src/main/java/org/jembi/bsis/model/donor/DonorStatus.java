package org.jembi.bsis.model.donor;

public enum DonorStatus {
  /**
   * Initial status for all donors
   */
  NORMAL,
  /**
   * Currently unused
   */
  DORMANT,
  /**
   * Indicates that the donor is a duplicate of another donor and has been replaced with a new
   * merged donor record
   */
  MERGED,
  /**
   * Replaced with DonorDeferrals, so this is no longer managed through a status
   */
  @Deprecated
  POSITIVE_TTI,
  /**
   * Currently unused
   */
  @Deprecated
  BLOOD_GROUP_MISMATCH
}
