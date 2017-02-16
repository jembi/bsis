package org.jembi.bsis.model.order;

public enum OrderType {
  TRANSFER, ISSUE, PATIENT_REQUEST;
  
  public static boolean isIssue(OrderType orderType) {
    if (orderType == OrderType.ISSUE || orderType == OrderType.PATIENT_REQUEST) {
      return true;
    }
    
    return false;
  }

  public static boolean isTransfer(OrderType orderType) {
    if (orderType == OrderType.TRANSFER) {
      return true;
    }

    return false;
  }
}