package repository.bloodtyping;

public enum BloodTypingStatus {
  COMPLETE, PENDING_TESTS, AMBIGUOUS, NO_MATCH;

  public static BloodTypingStatus fromObject(Object status) {
    if (status == null || !(status instanceof BloodTypingStatus))
      return NO_MATCH;
    return (BloodTypingStatus)status;
  }
}
