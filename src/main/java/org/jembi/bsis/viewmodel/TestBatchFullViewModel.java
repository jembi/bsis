package org.jembi.bsis.viewmodel;

import org.jembi.bsis.model.testbatch.TestBatchStatus;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TestBatchFullViewModel extends TestBatchViewModel {

  private int readyForReleaseCount;
  private final Map<String, Boolean> permissions = new HashMap<>();
  private final SortedSet<DonationViewModel> donations = new TreeSet<>();
  private final SortedSet<String> dinsWithoutTestSamples = new TreeSet<>();
  private final SortedSet<String> dinsInOtherTestBatches = new TreeSet<>();
  private final SortedSet<String> dinsInOpenDonationanBatch = new TreeSet<>();

  @SuppressWarnings("unused")
  @Builder(builderMethodName = "builderFull")
  public TestBatchFullViewModel(
      UUID id, Date testBatchDate, Date lastUpdated, TestBatchStatus status, String batchNumber, String notes,
      Integer numberOfSamples, LocationViewModel location, boolean backEntry, int readyForReleaseCount,
      @Singular Map<String, Boolean> permissions, @Singular Collection<DonationViewModel> donations,
      @Singular Collection<String> dinsWithoutTestSamples, @Singular Collection<String> dinsInOtherTestBatches,
      @Singular("dinInOpenDonationanBatch") Collection<String> dinsInOpenDonationanBatch) {
    super(id, testBatchDate, lastUpdated, status, batchNumber, notes, numberOfSamples, location, backEntry);
    this.readyForReleaseCount = readyForReleaseCount;
    this.permissions.putAll(permissions);
    this.donations.addAll(donations);
    this.dinsWithoutTestSamples.addAll(dinsWithoutTestSamples);
    this.dinsInOtherTestBatches.addAll(dinsInOtherTestBatches);
    this.dinsInOpenDonationanBatch.addAll(dinsInOpenDonationanBatch);
  }

  public void putAllPermissions(Map<String, Boolean> permissionMap) {
    this.permissions.putAll(permissionMap);
  }

  public void addAllDonations(Collection<DonationViewModel> donations) {
    this.donations.addAll(donations);
  }

  public void addAllDonationIdsWithoutTestSamples(Collection<String> dins) {
    this.dinsWithoutTestSamples.addAll(dins);
  }

  public void addAllDonationIdsInOtherTestBatches(Collection<String> dins) {
    this.dinsInOtherTestBatches.addAll(dins);
  }

  public void addAllDonationIdsInOpenDonationBatch(Collection<String> dins) {
    this.dinsInOpenDonationanBatch.addAll(dins);
  }
}
