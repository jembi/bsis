package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.backingform.TestBatchDonationRangeBackingForm;

public class TestBatchDonationRangeBackingFormBuilder extends AbstractBuilder<TestBatchDonationRangeBackingForm> {

  private UUID testBatchId;
  private String fromDIN;
  private String toDIN;

  public TestBatchDonationRangeBackingFormBuilder withTestBatchId(UUID testBatchId) {
    this.testBatchId = testBatchId;
    return this;
  }

  public TestBatchDonationRangeBackingFormBuilder withFromDIN(String fromDIN) {
    this.fromDIN = fromDIN;
    return this;
  }

  public TestBatchDonationRangeBackingFormBuilder withToDIN(String toDIN) {
    this.toDIN = toDIN;
    return this;
  }

  @Override
  public TestBatchDonationRangeBackingForm build() {
    TestBatchDonationRangeBackingForm testBatchDonationRangeBackingForm = new TestBatchDonationRangeBackingForm();
    testBatchDonationRangeBackingForm.setTestBatchId(testBatchId);
    testBatchDonationRangeBackingForm.setFromDIN(fromDIN);
    testBatchDonationRangeBackingForm.setToDIN(toDIN);
    return testBatchDonationRangeBackingForm;
  }

  public static TestBatchDonationRangeBackingFormBuilder aTestBatchDonationRangeBackingForm() {
    return new TestBatchDonationRangeBackingFormBuilder();
  }
}
