package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;

public class TestBatchBuilder extends AbstractEntityBuilder<TestBatch> {

  private Long id;
  private TestBatchStatus status;
  private Set<DonationBatch> donationBatches;
  private String batchNumber;
  private Date createdDate;
  private Date lastUpdatedDate;
  private String notes;
  private Location location = LocationBuilder.aTestingSite().build();

  public TestBatchBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public TestBatchBuilder withStatus(TestBatchStatus status) {
    this.status = status;
    return this;
  }

  public TestBatchBuilder withBatchNumber(String batchNumber) {
    this.batchNumber = batchNumber;
    return this;
  }

  public TestBatchBuilder withCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
    return this;
  }

  public TestBatchBuilder withLastUpdatedDate(Date lastUpdatedDate) {
    this.lastUpdatedDate = lastUpdatedDate;
    return this;
  }

  public TestBatchBuilder withNotes(String notes) {
    this.notes = notes;
    return this;
  }

  public TestBatchBuilder withDonationBatches(Set<DonationBatch> donationBatches) {
    this.donationBatches = donationBatches;
    return this;
  }

  public TestBatchBuilder withLocation(Location location) {
    this.location = location;
    return this;
  }

  public TestBatchBuilder withDonationBatch(DonationBatch donationBatch) {
    if (donationBatches == null) {
      donationBatches = new HashSet<>();
    }
    donationBatches.add(donationBatch);
    return this;
  }

  @Override
  public TestBatch build() {
    TestBatch testBatch = new TestBatch();
    testBatch.setId(id);
    testBatch.setStatus(status);
    testBatch.setBatchNumber(batchNumber);
    testBatch.setCreatedDate(createdDate);
    testBatch.setLastUpdated(lastUpdatedDate);
    testBatch.setNotes(notes);
    testBatch.setDonationBatches(donationBatches);
    testBatch.setLocation(location);
    return testBatch;
  }

  public static TestBatchBuilder aTestBatch() {
    return new TestBatchBuilder();
  }
  
  public static TestBatchBuilder aReleasedTestBatch() {
    TestBatchBuilder builder = new TestBatchBuilder();
    builder.withStatus(TestBatchStatus.RELEASED);
    return builder;
  }

}
