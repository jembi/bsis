package org.jembi.bsis.helpers.builders;

import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.helpers.persisters.AbstractEntityPersister;
import org.jembi.bsis.helpers.persisters.BloodTestResultPersister;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.user.User;

public class BloodTestResultBuilder extends AbstractEntityBuilder<BloodTestResult> {

  private UUID id;
  private String result;
  private BloodTest bloodTest = aBloodTest().build();
  private Donation donation = aDonation().build();
  private boolean reEntryRequired;
  private boolean isDeleted = false;
  private Date createdDate;
  private User createdBy;
  private Date testedOn;

  public BloodTestResultBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public BloodTestResultBuilder withResult(String result) {
    this.result = result;
    return this;
  }

  public BloodTestResultBuilder withBloodTest(BloodTest bloodTest) {
    this.bloodTest = bloodTest;
    return this;
  }
  
  public BloodTestResultBuilder withDonation(Donation donation) {
    this.donation = donation;
    return this;
  }

  public BloodTestResultBuilder withReEntryRequired(boolean reEntryRequired) {
    this.reEntryRequired = reEntryRequired;
    return this;
  }

  public BloodTestResultBuilder withIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
    return this;
  }
  
  public BloodTestResultBuilder thatIsDeleted() {
    isDeleted = true;
    return this;
  }
  
  public BloodTestResultBuilder withCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
    return this;
  }
  
  public BloodTestResultBuilder withCreatedBy(User createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public BloodTestResultBuilder withTestedOn(Date testedOn) {
    this.testedOn = testedOn;
    return this;
  }

  @Override
  public BloodTestResult build() {
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setId(id);
    bloodTestResult.setResult(result);
    bloodTestResult.setBloodTest(bloodTest);
    bloodTestResult.setDonation(donation);
    bloodTestResult.setReEntryRequired(reEntryRequired);
    bloodTestResult.setIsDeleted(isDeleted);
    bloodTestResult.setCreatedDate(createdDate);
    bloodTestResult.setCreatedBy(createdBy);
    bloodTestResult.setTestedOn(testedOn);
    return bloodTestResult;
  }

  @Override
  public AbstractEntityPersister<BloodTestResult> getPersister() {
    return new BloodTestResultPersister();
  }

  public static BloodTestResultBuilder aBloodTestResult() {
    return new BloodTestResultBuilder();
  }

}
