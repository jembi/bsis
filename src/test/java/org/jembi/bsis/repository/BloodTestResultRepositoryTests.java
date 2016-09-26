package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestResultBuilder.aBloodTestResult;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.UserBuilder.aUser;
import static org.jembi.bsis.helpers.matchers.SameDayMatcher.isSameDayAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.jembi.bsis.dto.BloodTestResultExportDTO;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.suites.SecurityContextDependentTestSuite;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BloodTestResultRepositoryTests extends SecurityContextDependentTestSuite {
  
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
  
  @Test
  public void testFindBloodTestResultsForExport_shouldReturnBloodTestResultExportDTOsWithTheCorrectState() {
    // Set up fixture
    String donationIdentificationNumber = "555668";
    String createdByUsername = "created.by";
    Date createdDate = new DateTime().minusDays(29).toDate();
    String result = "POS";
    String testNameShort = "BloodTest";
    
    // Expected
    aBloodTestResult()
        .withDonation(aDonation().withDonationIdentificationNumber(donationIdentificationNumber).build())
        .withCreatedBy(aUser().withUsername(createdByUsername).build())
        .withCreatedDate(createdDate)
        .withResult(result)
        .withBloodTest(aBloodTest().withTestNameShort(testNameShort).build())
        .buildAndPersist(entityManager);
    
    // Excluded because deleted
    aBloodTestResult().thatIsDeleted().buildAndPersist(entityManager);
    
    // Exercise SUT
    List<BloodTestResultExportDTO> returnedDTOs = bloodTestResultRepository.findBloodTestResultsForExport();
    
    // Verify
    assertThat(returnedDTOs.size(), is(1));

    // Verify DTO state
    BloodTestResultExportDTO returnedDTO = returnedDTOs.get(0);
    assertThat(returnedDTO.getDonationIdentificationNumber(), is(donationIdentificationNumber));
    assertThat(returnedDTO.getCreatedBy(), is(createdByUsername));
    assertThat(returnedDTO.getCreatedDate(), isSameDayAs(createdDate));
    assertThat(returnedDTO.getLastUpdatedBy(), is(USERNAME));
    assertThat(returnedDTO.getLastUpdated(), isSameDayAs(new Date()));
    assertThat(returnedDTO.getResult(), is(result));
    assertThat(returnedDTO.getTestName(), is(testNameShort));
  }
  
  @Test
  public void testFindBloodTestResultsForExport_shouldOrderResultsByCreatedDate() {
    // Set up fixture
    String firstTestName = "Number one";
    String secondTestName = "Number two";
    
    aBloodTestResult()
        .withBloodTest(aBloodTest().withTestNameShort(secondTestName).build())
        .withCreatedDate(new DateTime().minusDays(3).toDate())
        .buildAndPersist(entityManager);
    aBloodTestResult()
        .withBloodTest(aBloodTest().withTestNameShort(firstTestName).build())
        .withCreatedDate(new DateTime().minusDays(7).toDate())
        .buildAndPersist(entityManager);
    
    // Exercise SUT
    List<BloodTestResultExportDTO> returnedDTOs = bloodTestResultRepository.findBloodTestResultsForExport();
    
    // Verify
    assertThat(returnedDTOs.size(), is(2));
    assertThat(returnedDTOs.get(0).getTestName(), is(firstTestName));
    assertThat(returnedDTOs.get(1).getTestName(), is(secondTestName));
  }

}
