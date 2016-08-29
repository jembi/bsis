package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.CollectedDonationDTOBuilder.aCollectedDonationDTO;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonationTypeBuilder.aDonationType;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.dto.CollectedDonationDTO;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DonationRepositoryTests extends ContextDependentTestSuite {

  @Autowired
  private DonationRepository donationRepository;

  @Test
  public void testFindCollectedDonationsReportIndicators_shouldReturnAggregatedIndicators() {
    Date irrelevantStartDate = new DateTime().minusDays(7).toDate();
    Date irrelevantEndDate = new DateTime().minusDays(2).toDate();

    Location expectedVenue = aVenue().build();
    DonationType expectedDonationType = aDonationType().build();
    String expectedBloodAbo = "A";
    String expectedBloodRh = "+";
    Gender expectedGender = Gender.male;

    // Expected
    aDonation()
        .thatIsNotDeleted()
        .withDonationDate(irrelevantStartDate)
        .withDonor(aDonor().withGender(expectedGender).build())
        .withDonationType(expectedDonationType)
        .withBloodAbo(expectedBloodAbo)
        .withBloodRh(expectedBloodRh)
        .withVenue(expectedVenue)
        .buildAndPersist(entityManager);

    // Expected
    aDonation()
        .thatIsNotDeleted()
        .withDonationDate(irrelevantEndDate)
        .withDonor(aDonor().withGender(expectedGender).build())
        .withDonationType(expectedDonationType)
        .withBloodAbo(expectedBloodAbo)
        .withBloodRh(expectedBloodRh)
        .withVenue(expectedVenue)
        .buildAndPersist(entityManager);

    // Excluded by date
    aDonation()
        .thatIsNotDeleted()
        .withDonationDate(new Date())
        .withDonor(aDonor().withGender(expectedGender).build())
        .withDonationType(expectedDonationType)
        .withBloodAbo(expectedBloodAbo)
        .withBloodRh(expectedBloodRh)
        .withVenue(expectedVenue)
        .buildAndPersist(entityManager);

    // Excluded by deleted
    aDonation()
        .thatIsDeleted()
        .withDonationDate(irrelevantStartDate)
        .withDonor(aDonor().withGender(expectedGender).build())
        .withDonationType(expectedDonationType)
        .withBloodAbo(expectedBloodAbo)
        .withBloodRh(expectedBloodRh)
        .withVenue(expectedVenue)
        .buildAndPersist(entityManager);

    List<CollectedDonationDTO> expectedDtos = Arrays.asList(
        aCollectedDonationDTO()
            .withVenue(expectedVenue)
            .withDonationType(expectedDonationType)
            .withGender(expectedGender)
            .withBloodAbo(expectedBloodAbo)
            .withBloodRh(expectedBloodRh)
            .withCount(2)
            .build()
    );

    List<CollectedDonationDTO> returnedDtos = donationRepository.findCollectedDonationsReportIndicators(
        irrelevantStartDate, irrelevantEndDate);

    assertThat(returnedDtos, is(expectedDtos));
  }

  @Test
  @Ignore("Can't get interval to work with HSQLDB")
  public void testFindLatestDueToDonateDateForDonor_shouldReturnLatestDate() {

    int shortPeriodBetweenDonations = 30;
    int longPeriodBetweenDonations = 120;

    PackType shortPeriodPackType = aPackType().withPeriodBetweenDonations(shortPeriodBetweenDonations).build();
    PackType longPeriodPackType = aPackType().withPeriodBetweenDonations(longPeriodBetweenDonations).build();

    Date donationDate = new Date();
    Date earlierDonationDate = new DateTime(donationDate).minusDays(1).toDate();
    Date laterDonationDate = new DateTime(donationDate).plusDays(1).toDate();
    Date expectedDueToDonateDate = new DateTime(donationDate).plusDays(longPeriodBetweenDonations).toDate();

    Donor donor = aDonor().buildAndPersist(entityManager);

    // Expected: donationDate + longPeriodBetweenDonations
    aDonation()
        .thatIsNotDeleted()
        .withDonor(donor)
        .withDonationDate(donationDate)
        .withPackType(longPeriodPackType)
        .buildAndPersist(entityManager);

    // Excluded by earlier due to donate date
    aDonation()
        .thatIsNotDeleted()
        .withDonor(donor)
        .withDonationDate(earlierDonationDate)
        .withPackType(longPeriodPackType)
        .buildAndPersist(entityManager);

    // Excluded by earlier due to donate date
    aDonation()
        .thatIsNotDeleted()
        .withDonor(donor)
        .withDonationDate(laterDonationDate)
        .withPackType(shortPeriodPackType)
        .buildAndPersist(entityManager);

    // Excluded by deleted
    aDonation()
        .thatIsDeleted()
        .withDonor(donor)
        .withDonationDate(laterDonationDate)
        .withPackType(longPeriodPackType)
        .buildAndPersist(entityManager);

    // Excluded by donor
    aDonation()
        .thatIsNotDeleted()
        .withDonor(aDonor().build())
        .withDonationDate(laterDonationDate)
        .withPackType(longPeriodPackType)
        .buildAndPersist(entityManager);

    Date returnedDueToDonateDate = donationRepository.findLatestDueToDonateDateForDonor(donor.getId());

    assertThat(returnedDueToDonateDate, is(expectedDueToDonateDate));
  }
  
  @Test
  public void testFindLastDonationsByDonorVenueAndDonationDate_shouldReturnCorrectDonations() {
    // Set up fixture
    Location venue = aVenue().buildAndPersist(entityManager);
    Date startDate = new DateTime().minusDays(90).toDate();
    Date endDate = new DateTime().minusDays(60).toDate();
    Date outOfRangeDate = new DateTime().minusDays(30).toDate();
    
    Donor donorWithLastDonationOnEndDate = aDonor().withDateOfLastDonation(endDate).withVenue(venue).build();
    
    // Excluded by last donation date
    aDonation()
        .withDonor(donorWithLastDonationOnEndDate)
        .withDonationDate(startDate)
        .buildAndPersist(entityManager);

    // Excluded by venue
    aDonation()
        .withDonor(aDonor().withDateOfLastDonation(startDate).withVenue(aVenue().build()).build())
        .withDonationDate(startDate)
        .buildAndPersist(entityManager);

    // Excluded by donation date
    aDonation()
        .withDonor(aDonor().withDateOfLastDonation(outOfRangeDate).withVenue(venue).build())
        .withDonationDate(outOfRangeDate)
        .buildAndPersist(entityManager);

    // Excluded by deleted flag
    aDonation()
        .thatIsDeleted()
        .withDonor(aDonor().withDateOfLastDonation(endDate).withVenue(venue).build())
        .withDonationDate(endDate)
        .buildAndPersist(entityManager);
    
    List<Donation> expectedDonations = Arrays.asList(
        aDonation()
            .withDonor(aDonor().withDateOfLastDonation(startDate).withVenue(venue).build())
            .withDonationDate(startDate)
            .buildAndPersist(entityManager),
        aDonation()
            .withDonor(donorWithLastDonationOnEndDate)
            .withDonationDate(endDate)
            .buildAndPersist(entityManager)
    );
    
    // Exercise SUT
    List<Donation> returnedDonations = donationRepository.findLastDonationsByDonorVenueAndDonationDate(venue, startDate,
        endDate);
    
    // Verify
    assertThat(returnedDonations, is(expectedDonations));
  }

}
