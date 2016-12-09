package org.jembi.bsis.model.testbatch;

public enum TestBatchStatus {
  OPEN, RELEASED, CLOSED;

  /**
   * Determines if the specified TestBatchStatus indicates that the Test Batch has been released previously. 
   *
   * @param status TestBatchStatus to check
   * @return true if status is released, or after release
   */
  public static boolean hasBeenReleased(TestBatchStatus status) {
    if (status == RELEASED || status == CLOSED) {
      return true;
    }
    return false;
  }
}