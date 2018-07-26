package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.helpers.persisters.AbstractEntityPersister;
import org.jembi.bsis.helpers.persisters.TestBatchPersister;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TestBatchBuilder extends AbstractEntityBuilder<TestBatch> {

  private UUID id;
  private TestBatchStatus status;
  private Set<Donation> donations;
  private String batchNumber;
  private Date testBatchDate;
  private Date lastUpdatedDate;
  private String notes;
  private Location location = LocationBuilder.aTestingSite().build();
  private boolean backEntry;

  public TestBatchBuilder withId(UUID id) {
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

  public TestBatchBuilder withTestBatchDate(Date testBatchDate) {
    this.testBatchDate = testBatchDate;
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

  public TestBatchBuilder withDonations(Set<Donation> donations) {
    this.donations = donations;
    return this;
  }

  public TestBatchBuilder withLocation(Location location) {
    this.location = location;
    return this;
  }

  public TestBatchBuilder withDonation(Donation donation) {
    if (donations == null) {
      donations = new HashSet<>();
    }
    donations.add(donation);
    return this;
  }

  public TestBatchBuilder withBackEntry(boolean backEntry) {
    this.backEntry = backEntry;
    return this;
  }

  @Override
  public TestBatch build() {
    TestBatch testBatch = new TestBatch();
    testBatch.setId(id);
    testBatch.setStatus(status);
    testBatch.setBatchNumber(batchNumber);
    testBatch.setTestBatchDate(testBatchDate);
    testBatch.setLastUpdated(lastUpdatedDate);
    testBatch.setNotes(notes);
    testBatch.setLocation(location);
    testBatch.setDonations(donations == null ? new HashSet<>() : donations);
    testBatch.setBackEntry(backEntry);
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

  @Override
  public AbstractEntityPersister<TestBatch> getPersister() {
    return new TestBatchPersister();
  }
}
