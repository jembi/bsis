package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestResultBuilder.aBloodTestResult;
import static org.jembi.bsis.helpers.builders.BloodTestResultDTOBuilder.aBloodTestResultDTO;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.dto.BloodTestResultDTO;
import org.jembi.bsis.dto.BloodTestTotalDTO;
import org.jembi.bsis.helpers.builders.BloodTestTotalDTOBuilder;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.model.bloodtesting.TTIStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.repository.bloodtesting.BloodTestingRepository;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BloodTestingRepositoryTests extends ContextDependentTestSuite{
  
  @Autowired
  private BloodTestingRepository bloodTestingRepository;
  
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
    BloodTest expectedBloodTest = aBloodTest().withBloodTestType(BloodTestType.BASIC_TTI).buildAndPersist(entityManager);
    BloodTest unexpectedBloodTest = aBloodTest().withBloodTestType(BloodTestType.BASIC_BLOODTYPING).buildAndPersist(entityManager);
    
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
    
    List<BloodTestResultDTO> expectedDtos = Arrays.asList(
        aBloodTestResultDTO()
            .withBloodTest(expectedBloodTest)
            .withVenue(expectedVenue)
            .withGender(expectedGender)
            .withCount(2)
            .build()
    );

    List<BloodTestResultDTO> returnedDtos = bloodTestingRepository.findTTIPrevalenceReportIndicators(
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
        .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).withTTIStatus(TTIStatus.TTI_SAFE).buildAndPersist(entityManager);
    Donation expectedDonationUnsafe = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(irrelevantStartDate)
        .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).withTTIStatus(TTIStatus.TTI_UNSAFE).buildAndPersist(entityManager);
    
    Donation outOfRangeDateDonation = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(new Date())
        .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).buildAndPersist(entityManager);
    Donation deletedDonation = aDonation().withVenue(expectedVenue).thatIsDeleted().withDonationDate(irrelevantStartDate)
        .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).buildAndPersist(entityManager);
    Donation notReleasedDonation = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(irrelevantStartDate)
        .thatIsNotReleased().withDonor(aDonor().withGender(expectedGender).build()).buildAndPersist(entityManager);
    
    BloodTest expectedBloodTest = aBloodTest().withBloodTestType(BloodTestType.BASIC_TTI).buildAndPersist(entityManager);
    BloodTest unexpectedBloodTest = aBloodTest().withBloodTestType(BloodTestType.BASIC_BLOODTYPING).buildAndPersist(entityManager);
    
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
     
    
    List<BloodTestTotalDTO> expectedDtos = Arrays.asList(
        BloodTestTotalDTOBuilder.aBloodTestTotalDTO()
            .withVenue(expectedVenue)
            .withGender(expectedGender)
            .withTotal(4)
            .build()
    );

    List<BloodTestTotalDTO> returnedDtos = bloodTestingRepository.findTTIPrevalenceReportTotalUnitsTested(
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
        .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).withTTIStatus(TTIStatus.TTI_UNSAFE).buildAndPersist(entityManager);
    Donation expectedUnsafeDonationEndDate = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(irrelevantEndDate)
        .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).withTTIStatus(TTIStatus.TTI_UNSAFE).buildAndPersist(entityManager);
    
    Donation safeDonation = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(irrelevantStartDate)
        .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).withTTIStatus(TTIStatus.TTI_SAFE).buildAndPersist(entityManager);
    Donation deletedDonation = aDonation().withVenue(expectedVenue).thatIsDeleted().withDonationDate(irrelevantStartDate)
        .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).withTTIStatus(TTIStatus.TTI_UNSAFE).buildAndPersist(entityManager);
    Donation outOfRangeDateDonation = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(new Date())
        .thatIsReleased().withDonor(aDonor().withGender(expectedGender).build()).withTTIStatus(TTIStatus.TTI_UNSAFE).buildAndPersist(entityManager);
    Donation notReleasedDonation = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(irrelevantStartDate)
        .thatIsNotReleased().withDonor(aDonor().withGender(expectedGender).build()).withTTIStatus(TTIStatus.TTI_UNSAFE).buildAndPersist(entityManager);
    
    BloodTest expectedBloodTest = aBloodTest().withBloodTestType(BloodTestType.BASIC_TTI).buildAndPersist(entityManager);
    BloodTest unexpectedBloodTest = aBloodTest().withBloodTestType(BloodTestType.BASIC_BLOODTYPING).buildAndPersist(entityManager);
    
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
     
    List<BloodTestTotalDTO> expectedDtos = Arrays.asList(
        BloodTestTotalDTOBuilder.aBloodTestTotalDTO()
            .withVenue(expectedVenue)
            .withGender(expectedGender)
            .withTotal(2)
            .build()
    );

    List<BloodTestTotalDTO> returnedDtos = bloodTestingRepository.findTTIPrevalenceReportTotalUnsafeUnitsTested(
        irrelevantStartDate, irrelevantEndDate);

    assertThat(returnedDtos, is(expectedDtos));
    
  }

}
