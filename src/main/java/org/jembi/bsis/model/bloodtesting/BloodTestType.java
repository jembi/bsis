package org.jembi.bsis.model.bloodtesting;

import java.util.Arrays;
import java.util.List;

public enum BloodTestType {
  BASIC_BLOODTYPING, BASIC_TTI, CONFIRMATORY_TTI, REPEAT_BLOODTYPING, REPEAT_TTI;

  public static boolean isPendingTTI(BloodTestType bloodTestType) {
    if (bloodTestType == CONFIRMATORY_TTI || bloodTestType == REPEAT_TTI) {
      return true;
    }
    return false;
  }

  /**
   * Get the matching BloodTestTypes for the specified BloodTestCategory - because only
   * certain combinations of BloodTestType and BloodTestCategory are valid.
   *
   * @param category BloodTestCategory
   * @return List<BloodTestType> of the valid BloodTestTypes
   *
   * @throws IllegalArgumentException if the BloodTestCategory specified is unknown.
   */
  public static List<BloodTestType> getBloodTestTypeForCategory(BloodTestCategory category) {
    switch (category) {
      case BLOODTYPING:
        return Arrays.asList(BASIC_BLOODTYPING, REPEAT_BLOODTYPING);
      case TTI:
        return Arrays.asList(BASIC_TTI, REPEAT_TTI, CONFIRMATORY_TTI);
      default:
        throw new IllegalArgumentException("Invalid BloodTestCategory " + category);
    }
  }
}
