package org.jembi.bsis.backingform;

import java.util.UUID;

public class TestBatchDonationRangeBackingForm {

  private UUID testBatchId;
  private String fromDIN;
  private String toDIN;

  public UUID getTestBatchId() {
    return testBatchId;
  }

  public void setTestBatchId(UUID testBatchId) {
    this.testBatchId = testBatchId;
  }

  public String getFromDIN() {
    return fromDIN;
  }

  public void setFromDIN(String fromDIN) {
    this.fromDIN = fromDIN;
  }

  public String getToDIN() {
    return toDIN;
  }

  public void setToDIN(String toDIN) {
    this.toDIN = toDIN;
  }
}
