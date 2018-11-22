package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestResultBuilder.aBloodTestResult;
import static org.jembi.bsis.helpers.builders.BloodTestResultDTOBuilder.aBloodTestResultDTO;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.builders.UserBuilder.aUser;
import static org.jembi.bsis.helpers.matchers.SameDayMatcher.isSameDayAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.jembi.bsis.dto.BloodTestResultDTO;
import org.jembi.bsis.dto.BloodTestResultExportDTO;
import org.jembi.bsis.dto.BloodTestTotalDTO;
import org.jembi.bsis.helpers.builders.BloodTestTotalDTOBuilder;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.suites.SecurityContextDependentTestSuite;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BloodTestResultRepositoryTests extends SecurityContextDependentTestSuite {
  
  @Autowired
  private BloodTestResultRepository bloodTestResultRepository;

  @Test
  public void testCountBloodTestResultsForDonation() {
    // Set up data
    Donation donation = aDonation().buildAndPersist(entityManager);
    Donation excludedDonation = aDonation().buildAndPersist(entityManager);

    aBloodTestResult().withDonation(donation).buildAndPersist(entityManager);
    aBloodTestResult().withDonation(donation).buildAndPersist(entityManager);
    // Test outcome excluded because it's deleted
    aBloodTestResult().withDonation(donation).withIsDeleted(true).buildAndPersist(entityManager);
    // Test outcome excluded because it belongs to excludedDonation
    aBloodTestResult().withDonation(excludedDonation).buildAndPersist(entityManager);

    // Test
    int count = bloodTestResultRepository.countBloodTestResultsForDonation(donation.getId());

    // Verify
    assertThat(count, is(2));
  }

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
        .withBloodTest(aBloodTest().withTestNameShort(testNameShort).withTestName(testNameShort).build())
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
        .withBloodTest(aBloodTest().withTestNameShort(secondTestName).withTestName(secondTestName).build())
        .withCreatedDate(new DateTime().minusDays(3).toDate())
        .buildAndPersist(entityManager);
    aBloodTestResult()
        .withBloodTest(aBloodTest().withTestNameShort(firstTestName).withTestName(firstTestName).build())
        .withCreatedDate(new DateTime().minusDays(7).toDate())
        .buildAndPersist(entityManager);
    
    // Exercise SUT
    List<BloodTestResultExportDTO> returnedDTOs = bloodTestResultRepository.findBloodTestResultsForExport();
    
    // Verify
    assertThat(returnedDTOs.size(), is(2));
    assertThat(returnedDTOs.get(0).getTestName(), is(firstTestName));
    assertThat(returnedDTOs.get(1).getTestName(), is(secondTestName));
  }

  @Test
  public void testFindTTIPrevalenceReportIndicators_shouldReturnAggregatedIndicators() {
    Date irrelevantStartDate = new DateTime().minusDays(7).toDate();
    Date irrelevantEndDate = new DateTime().minusDays(2).toDate();

    Location expectedVenue = aVenue().build();
    Gender expectedGender = Gender.male;
    Donation expectedDonationStartDate = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(irrelevantStartDate)
            .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).buildAndPersist(entityManager);
    Donation expectedDonationEndDate = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(irrelevantEndDate)
            .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).buildAndPersist(entityManager);

    Donation outOfRangeDateDonation = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(new Date())
            .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).buildAndPersist(entityManager);
    Donation deletedDonation = aDonation().withVenue(expectedVenue).thatIsDeleted().withDonationDate(irrelevantStartDate)
            .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).buildAndPersist(entityManager);
    Donation notReleasedDonation = aDonation().withVenue(expectedVenue).thatIsDeleted().withDonationDate(irrelevantStartDate)
            .thatIsNotReleased().withDonor(aDonor().withGender(expectedGender).build()).buildAndPersist(entityManager);
    Donation doesntCountAsDonationDonation = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(irrelevantStartDate)
            .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build())
            .withPackType(aPackType().withCountAsDonation(false).build()).buildAndPersist(entityManager);
    BloodTest expectedBloodTest = aBloodTest().withBloodTestType(BloodTestType.BASIC_TTI)
            .withTestName("test1").withTestNameShort("t1").buildAndPersist(entityManager);
    BloodTest unexpectedBloodTest = aBloodTest().withBloodTestType(BloodTestType.BASIC_BLOODTYPING)
            .withTestName("test2").withTestNameShort("t2").buildAndPersist(entityManager);

    // Expected
    aBloodTestResult()
            .withDonation(expectedDonationStartDate)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    // Expected
    aBloodTestResult()
            .withDonation(expectedDonationEndDate)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    // Excluded by date
    aBloodTestResult()
            .withDonation(outOfRangeDateDonation)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    // Excluded - donation deleted
    aBloodTestResult()
            .withDonation(deletedDonation)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    // Excluded by blood test type
    aBloodTestResult()
            .withDonation(expectedDonationStartDate)
            .withBloodTest(unexpectedBloodTest)
            .buildAndPersist(entityManager);

    // Excluded by not being released
    aBloodTestResult()
            .withDonation(notReleasedDonation)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    // Excluded by not counting as donation
    aBloodTestResult()
            .withDonation(doesntCountAsDonationDonation)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    List<BloodTestResultDTO> expectedDtos = Arrays.asList(
            aBloodTestResultDTO()
                    .withBloodTest(expectedBloodTest)
                    .withVenue(expectedVenue)
                    .withGender(expectedGender)
                    .withCount(2)
                    .build()
    );

    List<BloodTestResultDTO> returnedDtos = bloodTestResultRepository.findTTIPrevalenceReportIndicators(
            irrelevantStartDate, irrelevantEndDate);

    assertThat(returnedDtos, is(expectedDtos));
  }

  @Test
  public void testFindTTIPrevalenceReportIndicatorsWithInactiveAndDeletedBloodTests_shouldReturnAggregatedIndicators() {
    Date irrelevantStartDate = new DateTime().minusDays(7).toDate();
    Date irrelevantEndDate = new DateTime().minusDays(2).toDate();

    Location expectedVenue = aVenue().build();
    Gender expectedGender = Gender.male;
    Donation expectedDonation = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(irrelevantStartDate)
            .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).buildAndPersist(entityManager);
    BloodTest expectedBloodTest = aBloodTest().withBloodTestType(BloodTestType.BASIC_TTI)
            .withTestName("test1").withTestNameShort("t1").thatIsInActive().buildAndPersist(entityManager);
    BloodTest deletedBloodTest = aBloodTest().withBloodTestType(BloodTestType.BASIC_TTI)
        .withTestName("test3").withTestNameShort("t3").thatIsDeleted().buildAndPersist(entityManager);

    // Expected even though the BloodTest is inactive
    aBloodTestResult()
            .withDonation(expectedDonation)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    // Excluded because the BloodTest is deleted
    aBloodTestResult()
            .withDonation(expectedDonation)
            .withBloodTest(deletedBloodTest)
            .buildAndPersist(entityManager);

    List<BloodTestResultDTO> expectedDtos = Arrays.asList(
            aBloodTestResultDTO()
                    .withBloodTest(expectedBloodTest)
                    .withVenue(expectedVenue)
                    .withGender(expectedGender)
                    .withCount(1)
                    .build()
    );

    List<BloodTestResultDTO> returnedDtos = bloodTestResultRepository.findTTIPrevalenceReportIndicators(
            irrelevantStartDate, irrelevantEndDate);

    assertThat(returnedDtos, is(expectedDtos));
  }


  @Test
  public void testFindTTIPrevalenceTotalUnitsTested_shouldReturnCorrectTotals() {
    Date irrelevantStartDate = new DateTime().minusDays(7).toDate();
    Date irrelevantEndDate = new DateTime().minusDays(2).toDate();

    Location expectedVenue = aVenue().build();
    Gender expectedGender = Gender.male;
    Donation expectedDonationStartDate = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(irrelevantStartDate)
            .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).buildAndPersist(entityManager);
    Donation expectedDonationEndDate = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(irrelevantEndDate)
            .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).buildAndPersist(entityManager);
    Donation expectedDonationSafe = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(irrelevantStartDate)
            .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).withTTIStatus(TTIStatus.SAFE).buildAndPersist(entityManager);
    Donation expectedDonationUnsafe = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(irrelevantStartDate)
            .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).withTTIStatus(TTIStatus.UNSAFE).buildAndPersist(entityManager);

    Donation outOfRangeDateDonation = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(new Date())
            .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).buildAndPersist(entityManager);
    Donation deletedDonation = aDonation().withVenue(expectedVenue).thatIsDeleted().withDonationDate(irrelevantStartDate)
            .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).buildAndPersist(entityManager);
    Donation notReleasedDonation = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(irrelevantStartDate)
            .thatIsNotReleased().withDonor(aDonor().withGender(expectedGender).build()).buildAndPersist(entityManager);
    Donation doesntCountAsDonationDonation = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(irrelevantStartDate)
            .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build())
            .withPackType(aPackType().withCountAsDonation(false).build()).buildAndPersist(entityManager);

    BloodTest expectedBloodTest = aBloodTest().withBloodTestType(BloodTestType.BASIC_TTI)
            .withTestName("test1").withTestNameShort("t1").buildAndPersist(entityManager);
    BloodTest unexpectedBloodTest = aBloodTest().withBloodTestType(BloodTestType.BASIC_BLOODTYPING)
            .withTestName("test2").withTestNameShort("t2").buildAndPersist(entityManager);

    // Expected in count
    aBloodTestResult()
            .withDonation(expectedDonationStartDate)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    // Expected in count
    aBloodTestResult()
            .withDonation(expectedDonationEndDate)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    // Expected in count
    aBloodTestResult()
            .withDonation(expectedDonationSafe)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    // Expected in count
    aBloodTestResult()
            .withDonation(expectedDonationUnsafe)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    // Excluded from count as donation was counted already
    aBloodTestResult()
            .withDonation(expectedDonationStartDate)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    // Excluded by date
    aBloodTestResult()
            .withDonation(outOfRangeDateDonation)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    // Excluded by being deleted
    aBloodTestResult()
            .withDonation(deletedDonation)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    // Excluded by blood test type
    aBloodTestResult()
            .withDonation(expectedDonationStartDate)
            .withBloodTest(unexpectedBloodTest)
            .buildAndPersist(entityManager);

    // Excluded by not being released
    aBloodTestResult()
            .withDonation(notReleasedDonation)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    // Excluded by not counting as donation
    aBloodTestResult()
            .withDonation(doesntCountAsDonationDonation)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    List<BloodTestTotalDTO> expectedDtos = Arrays.asList(
            BloodTestTotalDTOBuilder.aBloodTestTotalDTO()
                    .withVenue(expectedVenue)
                    .withGender(expectedGender)
                    .withTotal(4)
                    .build()
    );

    List<BloodTestTotalDTO> returnedDtos = bloodTestResultRepository.findTTIPrevalenceReportTotalUnitsTested(
            irrelevantStartDate, irrelevantEndDate);

    assertThat(returnedDtos, is(expectedDtos));
  }

  @Test
  public void testFindTTIPrevalenceTotalUnsafeUnitsTested_shouldReturnCorrectTotals() {
    Date irrelevantStartDate = new DateTime().minusDays(7).toDate();
    Date irrelevantEndDate = new DateTime().minusDays(2).toDate();

    Location expectedVenue = aVenue().build();
    Gender expectedGender = Gender.male;
    Donation expectedUnsafeDonationStartDate = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(irrelevantStartDate)
            .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).withTTIStatus(TTIStatus.UNSAFE).buildAndPersist(entityManager);
    Donation expectedUnsafeDonationEndDate = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(irrelevantEndDate)
            .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).withTTIStatus(TTIStatus.UNSAFE).buildAndPersist(entityManager);

    Donation safeDonation = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(irrelevantStartDate)
            .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).withTTIStatus(TTIStatus.SAFE).buildAndPersist(entityManager);
    Donation deletedDonation = aDonation().withVenue(expectedVenue).thatIsDeleted().withDonationDate(irrelevantStartDate)
            .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).withTTIStatus(TTIStatus.UNSAFE).buildAndPersist(entityManager);
    Donation outOfRangeDateDonation = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(new Date())
            .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).withTTIStatus(TTIStatus.UNSAFE).buildAndPersist(entityManager);
    Donation notReleasedDonation = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(irrelevantStartDate)
            .thatIsNotReleased().withDonor(aDonor().withGender(expectedGender).build()).withTTIStatus(TTIStatus.UNSAFE).buildAndPersist(entityManager);
    Donation doesntCountAsDonationDonation = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(irrelevantStartDate)
            .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).withTTIStatus(TTIStatus.UNSAFE)
            .withPackType(aPackType().withCountAsDonation(false).build()).buildAndPersist(entityManager);

    BloodTest expectedBloodTest = aBloodTest().withBloodTestType(BloodTestType.BASIC_TTI)
            .withTestName("test1").withTestNameShort("t1").buildAndPersist(entityManager);
    BloodTest unexpectedBloodTest = aBloodTest().withBloodTestType(BloodTestType.BASIC_BLOODTYPING)
            .withTestName("test2").withTestNameShort("t2").buildAndPersist(entityManager);

    // Expected in count
    aBloodTestResult()
            .withDonation(expectedUnsafeDonationStartDate)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    // Expected in count
    aBloodTestResult()
            .withDonation(expectedUnsafeDonationEndDate)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    // Excluded from count as donation was counted already
    aBloodTestResult()
            .withDonation(expectedUnsafeDonationStartDate)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    // Excluded by tti status
    aBloodTestResult()
            .withDonation(safeDonation)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    // Excluded by date
    aBloodTestResult()
            .withDonation(outOfRangeDateDonation)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    // Excluded by being deleted
    aBloodTestResult()
            .withDonation(deletedDonation)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    // Excluded by blood test type
    aBloodTestResult()
            .withDonation(expectedUnsafeDonationStartDate)
            .withBloodTest(unexpectedBloodTest)
            .buildAndPersist(entityManager);

    // Excluded by not being released
    aBloodTestResult()
            .withDonation(notReleasedDonation)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    // Excluded by not counting as donation
    aBloodTestResult()
            .withDonation(doesntCountAsDonationDonation)
            .withBloodTest(expectedBloodTest)
            .buildAndPersist(entityManager);

    List<BloodTestTotalDTO> expectedDtos = Arrays.asList(
            BloodTestTotalDTOBuilder.aBloodTestTotalDTO()
                    .withVenue(expectedVenue)
                    .withGender(expectedGender)
                    .withTotal(2)
                    .build()
    );

    List<BloodTestTotalDTO> returnedDtos = bloodTestResultRepository.findTTIPrevalenceReportTotalUnsafeUnitsTested(
            irrelevantStartDate, irrelevantEndDate);

    assertThat(returnedDtos, is(expectedDtos));
  }
}
