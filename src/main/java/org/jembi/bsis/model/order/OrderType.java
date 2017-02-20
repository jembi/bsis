package org.jembi.bsis.model.order;

import java.util.Arrays;
import java.util.List;

public enum OrderType {
  TRANSFER, ISSUE, PATIENT_REQUEST;
  
  public static boolean isIssue(OrderType orderType) {
    if (OrderType.getIssueOrderTypes().contains(orderType)) {
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

  public static boolean isPatientRequest(OrderType orderType) {
    if (orderType == OrderType.PATIENT_REQUEST) {
      return true;
    }

    return false;
  }

  public static List<OrderType> getIssueOrderTypes() {
    return Arrays.asList(OrderType.ISSUE, OrderType.PATIENT_REQUEST);
  }
}