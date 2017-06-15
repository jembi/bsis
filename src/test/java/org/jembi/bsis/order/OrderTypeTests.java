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
    assertThat(OrderType.isIssue(null), is(false));
  }

  @Test
  public void testIsTransferWithIssueAndPatientRequestOrderTpes_shouldReturnFalse() {
    assertThat(OrderType.isTransfer(OrderType.ISSUE), is(false));
    assertThat(OrderType.isTransfer(OrderType.PATIENT_REQUEST), is(false));
    assertThat(OrderType.isTransfer(null), is(false));
  }

  @Test
  public void testIsTransferWithTransferOrderType_shouldReturnTrue() {
    assertThat(OrderType.isTransfer(OrderType.TRANSFER), is(true));
  }
}