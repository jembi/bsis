package org.jembi.bsis.order;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.jembi.bsis.model.order.OrderType;
import org.junit.Test;

public class OrderTypeTests {

  @Test
  public void testIsIssueWithIssueAndPatientRequestOrderTpes_shouldReturnTrue() {
    assertThat(OrderType.isIssue(OrderType.ISSUE), is(true));
    assertThat(OrderType.isIssue(OrderType.PATIENT_REQUEST), is(true));
  }

  @Test
  public void testIsIssueWithTransferOrderType_shouldReturnFalse() {
    assertThat(OrderType.isIssue(OrderType.TRANSFER), is(false));
  }
}
