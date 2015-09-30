package repository;

import static helpers.builders.CollectedDonationValueObjectBuilder.aCollectedDonationValueObject;
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonationTypeBuilder.aDonationType;
import static helpers.builders.DonorBuilder.aDonor;
import static helpers.builders.LocationBuilder.aDonorPanel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.donationtype.DonationType;
import model.location.Location;
import model.util.Gender;
import org.joda.time.DateTime;
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
        
        Location expectedVenue = aDonorPanel().build();
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

}
