package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.jembi.bsis.helpers.builders.AdverseEventBuilder.anAdverseEvent;
import static org.jembi.bsis.helpers.builders.AdverseEventTypeBuilder.anAdverseEventType;
import static org.jembi.bsis.helpers.builders.CollectedDonationDTOBuilder.aCollectedDonationDTO;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonationTypeBuilder.aDonationType;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.builders.UserBuilder.aUser;
import static org.jembi.bsis.helpers.matchers.SameDayMatcher.isSameDayAs;
import static org.jembi.bsis.helpers.matchers.DonationMatcher.hasSameStateAsDonation;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.dto.CollectedDonationDTO;
import org.jembi.bsis.dto.DonationExportDTO;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.HaemoglobinLevel;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.suites.SecurityContextDependentTestSuite;
import org.jembi.bsis.util.RandomTestDate;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DonationRepositoryTests extends SecurityContextDependentTestSuite {

  @Autowired
  private DonationRepository donationRepository;

  @Test
  public void testFindByDonationIdentificationNumber_shouldReturnMatchingDonation() {
    String din = "din";
    Donation expected = aDonation().withDonationIdentificationNumber(din).buildAndPersist(entityManager);

    Donation actual = donationRepository.findDonationByDonationIdentificationNumber(din);

    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void testFindCollectedDonationsReportIndicators_shouldReturnAggregatedIndicators() {
    Date irrelevantStartDate = new DateTime().minusDays(7).toDate();
    Date irrelevantEndDate = new DateTime().minusDays(2).toDate();

    Location expectedVenue = aVenue().build();
    DonationType expectedDonationType = aDonationType().thatIsNotDeleted().build();
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

    // Excluded by deleted donation
    aDonation()
        .thatIsDeleted()
        .withDonationDate(irrelevantStartDate)
        .withDonor(aDonor().withGender(expectedGender).build())
        .withDonationType(expectedDonationType)
        .withBloodAbo(expectedBloodAbo)
        .withBloodRh(expectedBloodRh)
        .withVenue(expectedVenue)
        .buildAndPersist(entityManager);

    // Excluded by deleted donationType
    aDonation()
        .thatIsNotDeleted()
        .withDonationDate(irrelevantStartDate)
        .withDonor(aDonor().withGender(expectedGender).build())
        .withDonationType(aDonationType().thatIsDeleted().build())
        .withBloodAbo(expectedBloodAbo)
        .withBloodRh(expectedBloodRh)
        .withVenue(expectedVenue)
        .buildAndPersist(entityManager);

    // Excluded by not counting as donation
    aDonation()
        .thatIsNotDeleted()
        .withDonationDate(irrelevantStartDate)
        .withDonor(aDonor().withGender(expectedGender).build())
        .withDonationType(expectedDonationType)
        .withBloodAbo(expectedBloodAbo)
        .withBloodRh(expectedBloodRh)
        .withVenue(expectedVenue)
        .withPackType(aPackType().withCountAsDonation(false).build())
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
  public void testFindDonationsBetweenTwoDins_shouldReturnDonations() {
    Date irrelevantStartDate = new RandomTestDate();

    // Expected
    Donation donation1 = aDonation()
        .thatIsNotDeleted()
        .withDonationIdentificationNumber("2000003")
        .withDonationDate(irrelevantStartDate)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);

    // Expected
    Donation donation2 = aDonation()
        .thatIsNotDeleted()
        .withDonationIdentificationNumber("2000004")
        .withDonationDate(irrelevantStartDate)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);

    // Expected
    Donation donation3 = aDonation()
        .thatIsNotDeleted()
        .withDonationIdentificationNumber("2000005")
        .withDonationDate(irrelevantStartDate)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);

    // Excluded: din before range
    aDonation()
        .thatIsNotDeleted()
        .withDonationIdentificationNumber("1000003")
        .withDonationDate(irrelevantStartDate)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);

    // Excluded: din after range
    aDonation()
        .thatIsNotDeleted()
        .withDonationIdentificationNumber("3000003")
        .withDonationDate(irrelevantStartDate)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);

    // Excluded: donation deleted
    aDonation()
        .thatIsNotDeleted()
        .withDonationIdentificationNumber("2000006")
        .withDonationDate(irrelevantStartDate)
        .thatIsDeleted()
        .buildAndPersist(entityManager);

    List<Donation> returnedDonations = donationRepository.findDonationsBetweenTwoDins(
        "2000003", "2000010");

    assertThat(returnedDonations.size(), is(3));
    assertThat(returnedDonations, hasItem(hasSameStateAsDonation(donation1)));
    assertThat(returnedDonations, hasItem(hasSameStateAsDonation(donation2)));
    assertThat(returnedDonations, hasItem(hasSameStateAsDonation(donation3)));
  }

  @Test
  public void testFindDonationsBetweenTwoDinsWithTheSameDin_shouldReturnOneDonation() {
    Date irrelevantStartDate = new RandomTestDate();

    // Expected
    Donation donation = aDonation()
        .thatIsNotDeleted()
        .withDonationIdentificationNumber("2000003")
        .withDonationDate(irrelevantStartDate)
        .buildAndPersist(entityManager);

    // Excluded: din before range
    aDonation()
        .thatIsNotDeleted()
        .withDonationIdentificationNumber("2000002")
        .withDonationDate(irrelevantStartDate)
        .buildAndPersist(entityManager);

    // Excluded: din after range
    aDonation()
        .thatIsNotDeleted()
        .withDonationIdentificationNumber("2000004")
        .withDonationDate(irrelevantStartDate)
        .buildAndPersist(entityManager);

    List<Donation> returnedDonations = donationRepository.findDonationsBetweenTwoDins(
        "2000003", "2000003");

    assertThat(returnedDonations.size(), is(1));
    assertThat(returnedDonations.get(0), hasSameStateAsDonation(donation));

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
  public void testFindDonationByDonationIdentificationNumberWithFlagCharacters_shouldReturnCorrectDonation() {
    // Set up fixture
    String dinWithFlagCharacters = "123456725";

    aDonation()
      .withDonationIdentificationNumber("1234567")
      .withFlagCharacters("25")
      .buildAndPersist(entityManager);

    // Exercise SUT
    Donation returnedDonation = donationRepository.findDonationByDonationIdentificationNumber(dinWithFlagCharacters);

    // Verify
    assertThat(returnedDonation.getDonationIdentificationNumber(), is("1234567"));
    assertThat(returnedDonation.getFlagCharacters(), is("25"));
  }

  @Test
  public void testFindDonationByDonationIdentificationNumberIncludeDeletedWithFlagCharacters_shouldReturnCorrectDonation() {
    // Set up fixture
    String dinWithFlagCharacters = "123456725";

    aDonation()
      .withDonationIdentificationNumber("1234567")
      .withFlagCharacters("25")
      .buildAndPersist(entityManager);

    // Exercise SUT
    Donation returnedDonation = donationRepository.findDonationByDonationIdentificationNumberIncludeDeleted(dinWithFlagCharacters);

    // Verify
    assertThat(returnedDonation.getDonationIdentificationNumber(), is("1234567"));
    assertThat(returnedDonation.getFlagCharacters(), is("25"));
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

  @Test
  public void testFindByVenueAndPackTypeInRangeWithDatesOnly_shouldReturnDonationsInRange() {
    Instant instant = Instant.now();
    Date nowMinusOne = Date.from(instant.minusMillis(1));
    Date now = Date.from(instant);
    Date nowPlusOne = Date.from(instant.plusMillis(1));
    Date nowPlusTwo = Date.from(instant.plusMillis(2));
    Date nowPlusThree = Date.from(instant.plusMillis(3));

    PackType producesTestSample = aPackType().withTestSampleProduced(true).build();
    PackType doesNotProduceTestSample = aPackType().withTestSampleProduced(false).build();

    Donation donationOutsideStart = aDonation().withPackType(producesTestSample).withBleedEndTime(nowMinusOne)
        .buildAndPersist(entityManager);
    Donation donationAtStart = aDonation().withPackType(producesTestSample).withBleedEndTime(now)
        .buildAndPersist(entityManager);
    Donation donationInRange = aDonation().withPackType(producesTestSample).withBleedEndTime(nowPlusOne)
        .buildAndPersist(entityManager);
    Donation donationInRangeWithoutTestSample = aDonation().withPackType(doesNotProduceTestSample)
        .withBleedEndTime(nowPlusOne).buildAndPersist(entityManager);
    Donation donationAtEnd = aDonation().withPackType(producesTestSample).withBleedEndTime(nowPlusTwo)
        .buildAndPersist(entityManager);
    Donation donationOutsideEnd = aDonation().withPackType(producesTestSample).withBleedEndTime(nowPlusThree)
        .buildAndPersist(entityManager);
    Donation deletedDonation = aDonation().withPackType(producesTestSample).withBleedEndTime(nowPlusOne)
        .thatIsDeleted().buildAndPersist(entityManager);

    List<Donation> actual = donationRepository.findInRange(now, nowPlusTwo);

    assertThat(actual, hasItems(donationAtStart, donationInRange, donationAtEnd));
    assertThat(actual, not(hasItem(donationOutsideStart)));
    assertThat(actual, not(hasItem(donationOutsideEnd)));
    assertThat(actual, not(hasItem(deletedDonation)));
    assertThat(actual, not(hasItem(donationInRangeWithoutTestSample)));
  }

  @Test
  public void testFindByVenueAndPackTypeInRangeWithVenueIdAndDates_shouldReturnDonationsFromVenueInRange() {
    Instant instant = Instant.now();
    Date now = Date.from(instant);
    Date nowPlusOne = Date.from(instant.plusMillis(1));
    Date nowPlusTwo = Date.from(instant.plusMillis(2));

    Location queryVenue = aLocation().build();

    PackType producesTestSample = aPackType().withTestSampleProduced(true).build();
    PackType doesNotProduceTestSample = aPackType().withTestSampleProduced(false).build();

    Donation donationFromQueryVenue = aDonation().withPackType(producesTestSample).withVenue(queryVenue)
        .withBleedEndTime(nowPlusOne).buildAndPersist(entityManager);
    Donation donationFromQueryVenueWithoutTestSample = aDonation().withPackType(doesNotProduceTestSample)
        .withVenue(queryVenue).withBleedEndTime(nowPlusOne).buildAndPersist(entityManager);
    Donation donationFromAnotherVenue = aDonation().withPackType(producesTestSample).withVenue(aLocation().build())
        .withBleedEndTime(nowPlusOne).buildAndPersist(entityManager);
    Donation deletedDonation = aDonation().withPackType(producesTestSample).withBleedEndTime(nowPlusOne)
        .withVenue(queryVenue).thatIsDeleted().buildAndPersist(entityManager);

    List<Donation> actual = donationRepository
        .findByVenueAndPackTypeInRange(queryVenue.getId(), null, now, nowPlusTwo);

    assertThat(actual, hasItem(donationFromQueryVenue));
    assertThat(actual, not(hasItem(donationFromAnotherVenue)));
    assertThat(actual, not(hasItem(deletedDonation)));
    assertThat(actual, not(hasItem(donationFromQueryVenueWithoutTestSample)));
  }

  @Test
  public void testFindByVenueAndPackTypeInRangeWithPackTypeIdAndDates_shouldReturnDonationsWithPackTypeInRange() {
    Instant instant = Instant.now();
    Date now = Date.from(instant);
    Date nowPlusOne = Date.from(instant.plusMillis(1));
    Date nowPlusTwo = Date.from(instant.plusMillis(2));

    PackType queryPackType = aPackType().withTestSampleProduced(true).build();
    PackType doesNotProduceTestSample = aPackType().withTestSampleProduced(false).build();

    Donation donationFromQueryPackType = aDonation().withPackType(queryPackType).withBleedEndTime(nowPlusOne)
        .buildAndPersist(entityManager);
    Donation donationWithAnotherPackType = aDonation().withPackType(aPackType().build()).withBleedEndTime(nowPlusOne)
        .buildAndPersist(entityManager);
    Donation donationWithoutTestSample = aDonation().withPackType(doesNotProduceTestSample).withBleedEndTime(nowPlusOne)
        .buildAndPersist(entityManager);
    Donation deletedDonation = aDonation().withBleedEndTime(nowPlusOne).withPackType(queryPackType).thatIsDeleted()
        .buildAndPersist(entityManager);

    List<Donation> actual = donationRepository
        .findByVenueAndPackTypeInRange(null, queryPackType.getId(), now, nowPlusTwo);

    assertThat(actual, hasItem(donationFromQueryPackType));
    assertThat(actual, not(hasItem(donationWithAnotherPackType)));
    assertThat(actual, not(hasItem(deletedDonation)));
    assertThat(actual, not(hasItem(donationWithoutTestSample)));
  }

  @Test
  public void testFindByVenueAndPackTypeInRangeWithVenueIdAndPackTypeIdAndDates_shouldReturnDonationsFromVenueWithPackTypeInRange() {
    Instant instant = Instant.now();
    Date nowMinusOne = Date.from(instant.minusMillis(1));
    Date now = Date.from(instant);
    Date nowPlusOne = Date.from(instant.plusMillis(1));
    Date nowPlusTwo = Date.from(instant.plusMillis(2));
    Date nowPlusThree = Date.from(instant.plusMillis(3));

    Location queryVenue = aLocation().build();
    PackType queryPackType = aPackType().withTestSampleProduced(true).build();
    PackType doesNotProduceTestSample = aPackType().withTestSampleProduced(false).build();

    Donation donationMatchingFilters = aDonation().withVenue(queryVenue).withPackType(queryPackType)
        .withBleedEndTime(nowPlusOne).buildAndPersist(entityManager);
    Donation donationOutsideStart = aDonation().withVenue(queryVenue).withPackType(queryPackType)
        .withBleedEndTime(nowMinusOne).buildAndPersist(entityManager);
    Donation donationOutsideEnd = aDonation().withVenue(queryVenue).withPackType(queryPackType)
        .withBleedEndTime(nowPlusThree).buildAndPersist(entityManager);
    Donation donationFromAnotherVenue = aDonation().withVenue(aLocation().build()).withPackType(queryPackType)
        .withBleedEndTime(nowPlusOne).buildAndPersist(entityManager);
    Donation donationWithAnotherPackType = aDonation().withVenue(queryVenue).withPackType(aPackType().build())
        .withBleedEndTime(nowPlusOne).buildAndPersist(entityManager);
    Donation donationWithoutTestSample = aDonation().withVenue(queryVenue).withPackType(doesNotProduceTestSample)
        .withBleedEndTime(nowPlusOne).buildAndPersist(entityManager);
    Donation deletedDonation = aDonation().withVenue(queryVenue).withPackType(queryPackType)
        .withBleedEndTime(nowPlusOne).thatIsDeleted().buildAndPersist(entityManager);

    List<Donation> actual = donationRepository
        .findByVenueAndPackTypeInRange(queryVenue.getId(), queryPackType.getId(), now, nowPlusTwo);

    assertThat(actual, hasItem(donationMatchingFilters));
    assertThat(actual, not(hasItem(donationOutsideStart)));
    assertThat(actual, not(hasItem(donationOutsideEnd)));
    assertThat(actual, not(hasItem(donationFromAnotherVenue)));
    assertThat(actual, not(hasItem(donationWithAnotherPackType)));
    assertThat(actual, not(hasItem(deletedDonation)));
    assertThat(actual, not(hasItem(donationWithoutTestSample)));
  }
}
