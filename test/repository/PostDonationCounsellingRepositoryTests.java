package repository;

import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonorBuilder.aDonor;
import static helpers.builders.LocationBuilder.aDonorPanel;
import static helpers.builders.PostDonationCounsellingBuilder.aPostDonationCounselling;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import model.donation.Donation;
import model.location.Location;

import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@WebAppConfiguration
@Transactional
public class PostDonationCounsellingRepositoryTests {
    
    private static final Date NO_START_DATE = null;
    private static final Date NO_END_DATE = null;
    private static final Set<Long> NO_DONOR_PANELS = null;
    
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private PostDonationCounsellingRepository postDonationCounsellingRepository;
    
    @Ignore
    @Test
    public void testFindDonorsFlaggedForCounsellingWithNoDates_shouldReturnDonorsFlaggedForCounselling() {

        Donation firstExpectedDonation = aDonation().build();
        Donation secondExpectedDonation = aDonation().build();

        List<Donation> expectedDonations = Arrays.asList(firstExpectedDonation, secondExpectedDonation);

        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(firstExpectedDonation)
                .buildAndPersist(entityManager);

        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(secondExpectedDonation)
                .buildAndPersist(entityManager);

        // Duplicate to test distinct donations
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(firstExpectedDonation)
                .buildAndPersist(entityManager);
        
        // Excluded by flag
        aPostDonationCounselling()
                .thatIsNotFlaggedForCounselling()
                .withDonation(aDonation().build())
                .buildAndPersist(entityManager);
        
        List<Donation> returnedDonations = postDonationCounsellingRepository.findDonationsFlaggedForCounselling(
                NO_START_DATE, NO_END_DATE, NO_DONOR_PANELS);
        
        assertThat(returnedDonations, is(expectedDonations));
    }
    
    @Ignore
    @Test
    public void testFindDonorsFlaggedForCounsellingWithDonorPanels_shouldReturnDonorsWithDonationsForDonorPanels() {
        
        Location firstDonorPanel = aDonorPanel().buildAndPersist(entityManager);
        Location secondDonorPanel = aDonorPanel().buildAndPersist(entityManager);
        List<Long> donorPanels = Arrays.asList(firstDonorPanel.getId(), secondDonorPanel.getId());
        
        Donation firstExpectedDonation = aDonation().withDonorPanel(firstDonorPanel).build();
        Donation secondExpectedDonation = aDonation().withDonorPanel(secondDonorPanel).build();
        List<Donation> expectedDonations = Arrays.asList(firstExpectedDonation, secondExpectedDonation);
        
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(firstExpectedDonation)
                .buildAndPersist(entityManager);

        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(secondExpectedDonation)
                .buildAndPersist(entityManager);
        
        // Excluded by donor panel
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonorPanel(aDonorPanel().build())
                        .build())
                .buildAndPersist(entityManager);
        
        List<Donation> returnedDonations = postDonationCounsellingRepository.findDonationsFlaggedForCounselling(
                NO_START_DATE, NO_END_DATE, new HashSet<>(donorPanels));
        
        assertThat(returnedDonations, is(expectedDonations));
    }

    @Ignore
    @Test
    public void testFindDonorsFlaggedForCounsellingWithStartDate_shouldReturnDonorsWithDonationsAferStartDate() {
        DateTime startDate = new DateTime().minusDays(7);

        Donation firstExpectedDonation = aDonation().withDonationDate(startDate.toDate()).build();
        Donation secondExpectedDonation = aDonation().withDonationDate(startDate.plusDays(3).toDate()).build();
        List<Donation> expectedDonations = Arrays.asList(firstExpectedDonation, secondExpectedDonation);

        // Donation on start date
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(firstExpectedDonation)
                .buildAndPersist(entityManager);

        // Donation after start date
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(secondExpectedDonation)
                .buildAndPersist(entityManager);

        // Excluded by donation before start date
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonor(aDonor().build())
                        .withDonationDate(startDate.minusDays(3).toDate())
                        .build())
                .buildAndPersist(entityManager);
        
        List<Donation> returnedDonations = postDonationCounsellingRepository.findDonationsFlaggedForCounselling(
                startDate.toDate(), NO_END_DATE, NO_DONOR_PANELS);
        
        assertThat(returnedDonations, is(expectedDonations));
    }

    @Ignore
    @Test
    public void testFindDonorsFlaggedForCounsellingWithEndDate_shouldReturnDonorsWithDonationsBeforeEndDate() {
        DateTime endDate = new DateTime().minusDays(7);

        Donation firstExpectedDonation = aDonation().withDonationDate(endDate.toDate()).build();
        Donation secondExpectedDonation = aDonation().withDonationDate(endDate.minusDays(3).toDate()).build();
        List<Donation> expectedDonations = Arrays.asList(firstExpectedDonation, secondExpectedDonation);

        // Donation on end date
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(firstExpectedDonation)
                .buildAndPersist(entityManager);

        // Donation before end date
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(secondExpectedDonation)
                .buildAndPersist(entityManager);

        // Excluded by donation after end date
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonationDate(endDate.plusDays(3).toDate())
                        .build())
                .buildAndPersist(entityManager);
        
        List<Donation> returnedDonations = postDonationCounsellingRepository.findDonationsFlaggedForCounselling(
                NO_START_DATE, endDate.toDate(), NO_DONOR_PANELS);
        
        assertThat(returnedDonations, is(expectedDonations));
    }

    @Ignore
    @Test
    public void testFindDonorsFlaggedForCounsellingWithDates_shouldReturnDonorsWithDonationsInDateRange() {
        DateTime startDate = new DateTime().minusDays(14);
        DateTime endDate = new DateTime().minusDays(7);

        Donation expectedDonation = aDonation().withDonationDate(startDate.plusDays(1).toDate()).build();
        List<Donation> expectedDonations = Arrays.asList(expectedDonation);

        // Donation in date range
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(expectedDonation)
                .buildAndPersist(entityManager);

        // Excluded by donation before start date
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonationDate(startDate.minusDays(1).toDate())
                        .build())
                .buildAndPersist(entityManager);

        // Excluded by donation after end date
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonationDate(endDate.plusDays(1).toDate())
                        .build())
                .buildAndPersist(entityManager);
        
        List<Donation> returnedDonations = postDonationCounsellingRepository.findDonationsFlaggedForCounselling(
                startDate.toDate(), endDate.toDate(), NO_DONOR_PANELS);
        
        assertThat(returnedDonations, is(expectedDonations));
    }
    
}
