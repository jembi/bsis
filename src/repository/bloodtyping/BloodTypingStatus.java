package repository.bloodtyping;

public enum BloodTypingStatus {
  INCOMPLETE_INFORMATION, COMPLETE, PENDING_TESTS_ABO, PENDING_TESTS_RH, AMBIGUOUS_OR_NO_MATCH;

  public static BloodTypingStatus fromObject(Object status) {
    if (status == null || !(status instanceof BloodTypingStatus))
      return AMBIGUOUS_OR_NO_MATCH;
    return (BloodTypingStatus)status;
  }
}
