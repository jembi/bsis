package repository;

import static helpers.builders.CollectedDonationValueObjectBuilder.aCollectedDonationValueObject;
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonationTypeBuilder.aDonationType;
import static helpers.builders.DonorBuilder.aDonor;
import static helpers.builders.LocationBuilder.aVenue;
import static helpers.builders.PackTypeBuilder.aPackType;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.donationtype.DonationType;
import model.donor.Donor;
import model.location.Location;
import model.packtype.PackType;
import model.util.Gender;

import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import suites.ContextDependentTestSuite;
import valueobject.CollectedDonationValueObject;

public class DonationRepositoryTests extends ContextDependentTestSuite {
    
    @PersistenceContext
    private EntityManager entityManager;
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
        
        List<CollectedDonationValueObject> expectedValueObjects = Arrays.asList(
            aCollectedDonationValueObject()
                    .withVenue(expectedVenue)
                    .withDonationType(expectedDonationType)
                    .withGender(expectedGender)
                    .withBloodAbo(expectedBloodAbo)
                    .withBloodRh(expectedBloodRh)
                    .withCount(2)
                    .build()
        );
        
        List<CollectedDonationValueObject> returnedValueObjects = donationRepository.findCollectedDonationsReportIndicators(
                irrelevantStartDate, irrelevantEndDate);
        
        assertThat(returnedValueObjects, is(expectedValueObjects));
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

}
