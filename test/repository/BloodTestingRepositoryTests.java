package repository;

import static helpers.builders.BloodTestBuilder.aBloodTest;
import static helpers.builders.BloodTestResultDTOBuilder.aBloodTestResultDTO;
import static helpers.builders.BloodTestResultBuilder.aBloodTestResult;
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonorBuilder.aDonor;
import static helpers.builders.LocationBuilder.aVenue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dto.BloodTestResultDTO;
import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestType;
import model.donation.Donation;
import model.location.Location;
import model.util.Gender;
import repository.bloodtesting.BloodTestingRepository;
import suites.ContextDependentTestSuite;

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
