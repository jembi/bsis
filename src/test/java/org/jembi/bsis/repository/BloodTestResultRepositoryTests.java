package org.jembi.bsis.repository;

import static org.jembi.bsis.helpers.builders.BloodTestResultBuilder.aBloodTestResult;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BloodTestResultRepositoryTests extends ContextDependentTestSuite {
  
  @Autowired
  private BloodTestResultRepository bloodTestResultRepository;

  @Test
  public void testGetTestOutcomes_shouldReturnCorrectOutcomes() {
    // Set up data
    Donation donation = aDonation().buildAndPersist(entityManager);
    Donation excludedDonation = aDonation().buildAndPersist(entityManager);

    BloodTestResult testOutcome1 = aBloodTestResult().withDonation(donation).buildAndPersist(entityManager);
    BloodTestResult testOutcome2 = aBloodTestResult().withDonation(donation).buildAndPersist(entityManager);
    // Test outcome excluded because it's deleted
    aBloodTestResult().withDonation(donation).withIsDeleted(true).buildAndPersist(entityManager);
    // Test outcome excluded because it belongs to excludedDonation
    aBloodTestResult().withDonation(excludedDonation).buildAndPersist(entityManager);

    // Test
    List<BloodTestResult> testOutcomes = bloodTestResultRepository.getTestOutcomes(donation);

    // Verify
    assertEquals("There's 2 outcomes returned", 2, testOutcomes.size());
    assertTrue("testOutcome1 is returned", testOutcomes.contains(testOutcome1));
    assertTrue("testOutcome2 is returned", testOutcomes.contains(testOutcome2));

  }

}
