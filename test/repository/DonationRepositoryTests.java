package repository;

import static helpers.builders.CollectedDonationDTOBuilder.aCollectedDonationDTO;
import static helpers.builders.ComponentBatchBuilder.aComponentBatch;
import static helpers.builders.DataTypeBuilder.aBooleanDataType;
import static helpers.builders.DonationBatchBuilder.aDonationBatch;
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonationTypeBuilder.aDonationType;
import static helpers.builders.DonorBuilder.aDonor;
import static helpers.builders.GeneralConfigBuilder.aGeneralConfig;
import static helpers.builders.LocationBuilder.aProcessingSite;
import static helpers.builders.LocationBuilder.aVenue;
import static helpers.builders.PackTypeBuilder.aPackType;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import constant.GeneralConfigConstants;
import dto.CollectedDonationDTO;
import model.component.Component;
import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.donationtype.DonationType;
import model.donor.Donor;
import model.location.Location;
import model.packtype.PackType;
import model.util.Gender;
import suites.ContextDependentTestSuite;

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
        .withDonor(aDonor().withGender(expectedGender).withVenue(expectedVenue).build())
        .withDonationType(expectedDonationType)
        .withBloodAbo(expectedBloodAbo)
        .withBloodRh(expectedBloodRh)
        .buildAndPersist(entityManager);

    // Expected
    aDonation()
        .thatIsNotDeleted()
        .withDonationDate(irrelevantEndDate)
        .withDonor(aDonor().withGender(expectedGender).withVenue(expectedVenue).build())
        .withDonationType(expectedDonationType)
        .withBloodAbo(expectedBloodAbo)
        .withBloodRh(expectedBloodRh)
        .buildAndPersist(entityManager);

    // Excluded by date
    aDonation()
        .thatIsNotDeleted()
        .withDonationDate(new Date())
        .withDonor(aDonor().withGender(expectedGender).withVenue(expectedVenue).build())
        .withDonationType(expectedDonationType)
        .withBloodAbo(expectedBloodAbo)
        .withBloodRh(expectedBloodRh)
        .buildAndPersist(entityManager);

    // Excluded by deleted
    aDonation()
        .thatIsDeleted()
        .withDonationDate(irrelevantStartDate)
        .withDonor(aDonor().withGender(expectedGender).withVenue(expectedVenue).build())
        .withDonationType(expectedDonationType)
        .withBloodAbo(expectedBloodAbo)
        .withBloodRh(expectedBloodRh)
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
  public void testAddDonationToDonationBatchWithoutComponentBatch_shouldSetComponentLocationToVenue() {
    // Set up fixture
    insertConfigToCreateInitialComponents();
    PackType packTypeThatCountsAsDonation = aPackType().withCountAsDonation(true).buildAndPersist(entityManager);
    Location donationBatchVenue = aVenue().build();
    DonationBatch donationBatchWithoutComponentBatch = aDonationBatch()
        .withVenue(donationBatchVenue)
        .withComponentBatch(null)
        .buildAndPersist(entityManager);
    Donor existingDonor = aDonor().buildAndPersist(entityManager);
    
    Donation donation = aDonation()
        .withPackType(packTypeThatCountsAsDonation)
        .withDonationBatch(donationBatchWithoutComponentBatch)
        .withDonor(existingDonor)
        .withDonationDate(new Date())
        .withBleedStartTime(new Date())
        .withBleedEndTime(new Date())
        .build();
    
    // Call the method being tested
    Donation addedDonation = donationRepository.addDonation(donation);
    
    // Verify results
    List<Component> components = addedDonation.getComponents();
    assertThat(components.size(), is(1));
    assertThat(components.get(0).getLocation(), is(donationBatchVenue));
  }
  
  @Test
  public void testAddDonationToDonationBatchWithComponentBatch_shouldSetComponentLocationToProcessingSite() {
    // Set up fixture
    insertConfigToCreateInitialComponents();
    PackType packTypeThatCountsAsDonation = aPackType().withCountAsDonation(true).buildAndPersist(entityManager);
    Location componentBatchProcessingSite = aProcessingSite().build();
    DonationBatch donationBatchWithComponentBatch = aDonationBatch()
        .withComponentBatch(aComponentBatch().withLocation(componentBatchProcessingSite).build())
        .buildAndPersist(entityManager);
    Donor existingDonor = aDonor().buildAndPersist(entityManager);
    
    Donation donation = aDonation()
        .withPackType(packTypeThatCountsAsDonation)
        .withDonationBatch(donationBatchWithComponentBatch)
        .withDonor(existingDonor)
        .withDonationDate(new Date())
        .withBleedStartTime(new Date())
        .withBleedEndTime(new Date())
        .build();
    
    // Call the method being tested
    Donation addedDonation = donationRepository.addDonation(donation);
    
    // Verify results
    List<Component> components = addedDonation.getComponents();
    assertThat(components.size(), is(1));
    assertThat(components.get(0).getLocation(), is(componentBatchProcessingSite));
  }
  
  private void insertConfigToCreateInitialComponents() {
    // General config to enable creation of initial components
    aGeneralConfig()
        .withName(GeneralConfigConstants.CREATE_INITIAL_COMPONENTS)
        .withDataType(aBooleanDataType().build())
        .withValue("true")
        .buildAndPersist(entityManager);
  }

}
