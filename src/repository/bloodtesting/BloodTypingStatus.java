package repository.bloodtesting;

public enum BloodTypingStatus {
  COMPLETE, PENDING_TESTS, AMBIGUOUS, NO_MATCH, NOT_DONE;

  public static BloodTypingStatus fromObject(Object status) {
    if (status == null || !(status instanceof BloodTypingStatus))
      return NO_MATCH;
    return (BloodTypingStatus)status;
  }
}
