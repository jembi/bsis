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
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
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
        .withDonor(aDonor().withGender(expectedGender).build()).buildAndPersist(entityManager);
    Donation expectedDonationEndDate = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(irrelevantEndDate)
        .withDonor(aDonor().withGender(expectedGender).build()).buildAndPersist(entityManager);
    Donation expectedDonationCurrentDate = aDonation().withVenue(expectedVenue).thatIsNotDeleted().withDonationDate(new Date())
        .withDonor(aDonor().withGender(expectedGender).build()).buildAndPersist(entityManager);
    Donation deletedDonation = aDonation().withVenue(expectedVenue).thatIsDeleted().withDonationDate(irrelevantStartDate)
        .withDonor(aDonor().withGender(expectedGender).build()).buildAndPersist(entityManager);
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
        .withDonation(expectedDonationCurrentDate)
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

}
