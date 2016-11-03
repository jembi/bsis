package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.AdverseEventBuilder.anAdverseEvent;
import static org.jembi.bsis.helpers.builders.AdverseEventTypeBuilder.anAdverseEventType;
import static org.jembi.bsis.helpers.builders.CollectedDonationDTOBuilder.aCollectedDonationDTO;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonationTypeBuilder.aDonationType;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.builders.UserBuilder.aUser;
import static org.jembi.bsis.helpers.matchers.SameDayMatcher.isSameDayAs;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.dto.CollectedDonationDTO;
import org.jembi.bsis.dto.DonationExportDTO;
import org.jembi.bsis.model.bloodtesting.TTIStatus;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.HaemoglobinLevel;
import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.suites.SecurityContextDependentTestSuite;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DonationRepositoryTests extends SecurityContextDependentTestSuite {

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
  
  @Test
  public void testFindDonationsForExport_shouldReturnDonationExportDTOsWithTheCorrectState() {
    // Set up fixture
    String donorNumber = "123321";
    String donationIdentificationNumber = "4569871";
    String createdByUsername = "created.by";
    Date createdDate = new DateTime().minusDays(29).toDate();
    String packType = "Blood";
    Date donationDate = new DateTime().minusDays(30).toDate();
    BloodTypingStatus bloodTypingStatus = BloodTypingStatus.NOT_DONE;
    BloodTypingMatchStatus bloodTypingMatchStatus = BloodTypingMatchStatus.NOT_DONE;
    TTIStatus ttiStatus = TTIStatus.NOT_DONE;
    Date bleedStartTime = new DateTime().minusDays(30).plusMinutes(10).toDate();
    Date bleedEndTime = new DateTime().minusDays(30).plusMinutes(20).toDate();
    BigDecimal donorWeight = new BigDecimal(90);
    int bloodPressureSystolic = 120;
    int bloodPressureDiastolic = 80;
    int donorPulse = 60;
    BigDecimal haemoglobinCount = new BigDecimal(12);
    HaemoglobinLevel haemoglobinLevel = HaemoglobinLevel.PASS;
    String bloodAbo = "AB";
    String bloodRh = "+";
    boolean ineliligibleDonor = true;
    String notes = "NOOOTES";
    String adverseEventType = "Fainting";
    String adverseEventComment = "Passed out";

    // Expected donation
    aDonation()
        .withDonor(aDonor().withDonorNumber(donorNumber).build())
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withCreatedBy(aUser().withUsername(createdByUsername).build())
        .withCreatedDate(createdDate)
        .withPackType(aPackType().withPackType(packType).build())
        .withDonationDate(donationDate)
        .withBloodTypingStatus(bloodTypingStatus)
        .withBloodTypingMatchStatus(bloodTypingMatchStatus)
        .withTTIStatus(ttiStatus)
        .withBleedStartTime(bleedStartTime)
        .withBleedEndTime(bleedEndTime)
        .withDonorWeight(donorWeight)
        .withBloodPressureSystolic(bloodPressureSystolic)
        .withBloodPressureDiastolic(bloodPressureDiastolic)
        .withDonorPulse(donorPulse)
        .withHaemoglobinCount(haemoglobinCount)
        .withHaemoglobinLevel(haemoglobinLevel)
        .withAdverseEvent(anAdverseEvent()
            .withType(anAdverseEventType().withName(adverseEventType).build())
            .withComment(adverseEventComment)
            .build())
        .withBloodAbo(bloodAbo)
        .withBloodRh(bloodRh)
        .thatIsReleased()
        .withIneligibleDonor(ineliligibleDonor)
        .withNotes(notes)
        .buildAndPersist(entityManager);
    
    // Excluded deleted donation
    aDonation().thatIsDeleted().buildAndPersist(entityManager);
    
    // Exercise SUT
    List<DonationExportDTO> returnedDTOs = donationRepository.findDonationsForExport();
    
    // Verify
    assertThat(returnedDTOs.size(), is(1));
    
    // Verify DTO state
    DonationExportDTO returnedDTO = returnedDTOs.get(0);
    assertThat(returnedDTO.getDonorNumber(), is(donorNumber));
    assertThat(returnedDTO.getDonationIdentificationNumber(), is(donationIdentificationNumber));
    assertThat(returnedDTO.getCreatedBy(), is(createdByUsername));
    assertThat(returnedDTO.getCreatedDate(), isSameDayAs(createdDate));
    assertThat(returnedDTO.getLastUpdatedBy(), is(USERNAME));
    assertThat(returnedDTO.getLastUpdated(), isSameDayAs(new Date()));
    assertThat(returnedDTO.getPackType(), is(packType));
    assertThat(returnedDTO.getDonationDate(), isSameDayAs(donationDate));
    assertThat(returnedDTO.getBloodTypingStatus(), is(bloodTypingStatus));
    assertThat(returnedDTO.getBloodTypingMatchStatus(), is(bloodTypingMatchStatus));
    assertThat(returnedDTO.getTtiStatus(), is(ttiStatus));
    assertThat(returnedDTO.getBleedStartTime(), isSameDayAs(bleedStartTime));
    assertThat(returnedDTO.getBleedEndTime(), isSameDayAs(bleedEndTime));
    assertThat(returnedDTO.getDonorWeight().compareTo(donorWeight), is(0));
    assertThat(returnedDTO.getBloodPressureSystolic(), is(bloodPressureSystolic));
    assertThat(returnedDTO.getBloodPressureDiastolic(), is(bloodPressureDiastolic));
    assertThat(returnedDTO.getDonorPulse(), is(donorPulse));
    assertThat(returnedDTO.getHaemoglobinCount().compareTo(haemoglobinCount), is(0));
    assertThat(returnedDTO.getHaemoglobinLevel(), is(haemoglobinLevel));
    assertThat(returnedDTO.getAdverseEventType(), is(adverseEventType));
    assertThat(returnedDTO.getAdverseEventComment(), is(adverseEventComment));
    assertThat(returnedDTO.getBloodAbo(), is(bloodAbo));
    assertThat(returnedDTO.getBloodRh(), is(bloodRh));
    assertThat(returnedDTO.isReleased(), is(true));
    assertThat(returnedDTO.isIneligbleDonor(), is(ineliligibleDonor));
    assertThat(returnedDTO.getNotes(), is(notes));
  }
  
  @Test
  public void testFindDonationsForExportWithNoAdverseEvent_shouldReturnDonation() {
    // Set up fixture
    aDonation().withAdverseEvent(null).buildAndPersist(entityManager);
    
    // Exercise SUT
    List<DonationExportDTO> returnedDTOs = donationRepository.findDonationsForExport();
    
    // Verify
    assertThat(returnedDTOs.size(), is(1));
  }

  @Test
  public void testFindDonationsForExport_shouldReturnDonationExportDTOsOrderedByCreatedDate() {
    // Set up fixture
    String firstDonationIdentificationNumber = "1111111";
    String secondDonationIdentificationNumber = "2222222";

    aDonation()
        .withCreatedDate(new DateTime().minusDays(3).toDate())
        .withDonationIdentificationNumber(secondDonationIdentificationNumber)
        .buildAndPersist(entityManager);
    aDonation()
        .withCreatedDate(new DateTime().minusDays(7).toDate())
        .withDonationIdentificationNumber(firstDonationIdentificationNumber)
        .buildAndPersist(entityManager);
    
    // Exercise SUT
    List<DonationExportDTO> returnedDTOs = donationRepository.findDonationsForExport();
    
    // Verify
    assertThat(returnedDTOs.size(), is(2));
    assertThat(returnedDTOs.get(0).getDonationIdentificationNumber(), is(firstDonationIdentificationNumber));
    assertThat(returnedDTOs.get(1).getDonationIdentificationNumber(), is(secondDonationIdentificationNumber));
  }

}
