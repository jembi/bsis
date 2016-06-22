package org.jembi.bsis.model.donordeferral;

/**
 * The type of deferral reason. This is used to distinguish the deferral reason to be used for
 * automated deferrals.
 *
 * Exactly one non-deleted deferral reason should exist for each type other than NORMAL so that it
 * can be looked up by this field.
 */
public enum DeferralReasonType {

  NORMAL,
  AUTOMATED_TTI_UNSAFE;

  /**
   * Determines if a Deferral is automated or not.
   *
   * @return boolean true if this DeferralReasonType is automated, false otherwise
   */
  public boolean isAutomatedDeferral() {
    if (this == AUTOMATED_TTI_UNSAFE) {
      return true;
    }
    return false;
  }
}
