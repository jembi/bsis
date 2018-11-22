package org.jembi.bsis.model.testbatch;

import java.util.HashSet;
import java.util.Set;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public final class DonationAdditionResult {

  private final TestBatch testBatch;
  private final Set<String> dinsWithoutTestSamples = new HashSet<>();
  private final Set<String> dinsInOtherTestBatches = new HashSet<>();
  private final Set<String> dinsInOpenDonationBatch = new HashSet<>();

  public static DonationAdditionResult from(TestBatch testBatch) {
    return new DonationAdditionResult(testBatch);
  }

  public DonationAdditionResult addDinWithoutTestSample(String donationIdentificationNumber) {
    this.dinsWithoutTestSamples.add(donationIdentificationNumber);
    return this;
  }

  public DonationAdditionResult addDinInAnotherTestBatch(String donationIdentificationNumber) {
    this.dinsInOtherTestBatches.add(donationIdentificationNumber);
    return this;
  }

  public DonationAdditionResult addDinInOpenDonationBatch(String donationIdentificationNumber) {
    this.dinsInOpenDonationBatch.add(donationIdentificationNumber);
    return this;
  }
}
