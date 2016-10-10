package org.jembi.bsis.model.bloodtesting;

public enum BloodTestType {
  // TODO: Investigate if ADVANCED_BLOODTYPING is being used at all
  BASIC_BLOODTYPING, ADVANCED_BLOODTYPING, BASIC_TTI, CONFIRMATORY_TTI, REPEAT_BLOODTYPING, REPEAT_TTI;

  public static boolean isPendingTTI(BloodTestType bloodTestType) {
    if (bloodTestType == CONFIRMATORY_TTI || bloodTestType == REPEAT_TTI) {
      return true;
    }
    return false;
  }
}
