package repository;

import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonorBuilder.aDonor;
import static helpers.builders.LocationBuilder.aDonorPanel;
import static helpers.builders.PostDonationCounsellingBuilder.aPostDonationCounselling;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import model.counselling.PostDonationCounselling;
import model.donor.Donor;
import model.location.Location;

import org.joda.time.DateTime;
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
    private static final Long NO_DONOR_PANEL = null;
    
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private PostDonationCounsellingRepository postDonationCounsellingRepository;
    
    @Test
    public void testFindDonorsFlaggedForCounsellingWithNoDates_shouldReturnDonorsFlaggedForCounselling() {

        Donor firstExpectedDonor = aDonor().build();
        Donor secondExpectedDonor = aDonor().build();
        
        List<Donor> expectedDonors = Arrays.asList(firstExpectedDonor, secondExpectedDonor);

        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonor(firstExpectedDonor)
                        .build())
                .buildAndPersist(entityManager);

        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonor(secondExpectedDonor)
                        .build())
                .buildAndPersist(entityManager);

        // Duplicate to test distinct donors
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonor(firstExpectedDonor)
                        .build())
                .buildAndPersist(entityManager);
        
        // Excluded by flag
        aPostDonationCounselling()
                .thatIsNotFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonor(aDonor().build())
                        .build())
                .buildAndPersist(entityManager);
        
        List<Donor> returnedDonors = postDonationCounsellingRepository.findDonorsFlaggedForCounselling(
                NO_START_DATE, NO_END_DATE, NO_DONOR_PANEL);
        
        assertThat(returnedDonors, is(expectedDonors));
    }
    
    @Test
    public void testFindDonorsFlaggedForCounseliingWithDonorPanel_shouldReturnDonorsWithDonationsForDonorPanel() {

        Donor expectedDonor = aDonor().build();
        List<Donor> expectedDonors = Arrays.asList(expectedDonor);
        
        Location donorPanel = aDonorPanel().buildAndPersist(entityManager);
        
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonor(expectedDonor)
                        .withDonorPanel(donorPanel)
                        .build())
                .buildAndPersist(entityManager);
        
        // Excluded by donor panel
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonor(aDonor().build())
                        .withDonorPanel(aDonorPanel().build())
                        .build())
                .buildAndPersist(entityManager);
        
        List<Donor> returnedDonors = postDonationCounsellingRepository.findDonorsFlaggedForCounselling(
                NO_START_DATE, NO_END_DATE, donorPanel.getId());
        
        assertThat(returnedDonors, is(expectedDonors));
    }

    @Test
    public void testFindDonorsFlaggedForCounsellingWithStartDate_shouldReturnDonorsWithDonationsAferStartDate() {
        DateTime startDate = new DateTime().minusDays(7);

        Donor firstExpectedDonor = aDonor().build();
        Donor secondExpectedDonor = aDonor().build();
        
        List<Donor> expectedDonors = Arrays.asList(firstExpectedDonor, secondExpectedDonor);

        // Donation on start date
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonor(firstExpectedDonor)
                        .withDonationDate(startDate.toDate())
                        .build())
                .buildAndPersist(entityManager);

        // Donation after start date
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonor(secondExpectedDonor)
                        .withDonationDate(startDate.plusDays(3).toDate())
                        .build())
                .buildAndPersist(entityManager);

        // Excluded by donation before start date
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonor(aDonor().build())
                        .withDonationDate(startDate.minusDays(3).toDate())
                        .build())
                .buildAndPersist(entityManager);
        
        List<Donor> returnedDonors = postDonationCounsellingRepository.findDonorsFlaggedForCounselling(
                startDate.toDate(), NO_END_DATE, NO_DONOR_PANEL);
        
        assertThat(returnedDonors, is(expectedDonors));
    }

    @Test
    public void testFindDonorsFlaggedForCounsellingWithEndDate_shouldReturnDonorsWithDonationsBeforeEndDate() {
        DateTime endDate = new DateTime().minusDays(7);

        Donor firstExpectedDonor = aDonor().build();
        Donor secondExpectedDonor = aDonor().build();
        
        List<Donor> expectedDonors = Arrays.asList(firstExpectedDonor, secondExpectedDonor);

        // Donation on end date
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonor(firstExpectedDonor)
                        .withDonationDate(endDate.toDate())
                        .build())
                .buildAndPersist(entityManager);

        // Donation before end date
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonor(secondExpectedDonor)
                        .withDonationDate(endDate.minusDays(3).toDate())
                        .build())
                .buildAndPersist(entityManager);

        // Excluded by donation after end date
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonor(aDonor().build())
                        .withDonationDate(endDate.plusDays(3).toDate())
                        .build())
                .buildAndPersist(entityManager);
        
        List<Donor> returnedDonors = postDonationCounsellingRepository.findDonorsFlaggedForCounselling(
                NO_START_DATE, endDate.toDate(), NO_DONOR_PANEL);
        
        assertThat(returnedDonors, is(expectedDonors));
    }

    @Test
    public void testFindDonorsFlaggedForCounsellingWithDates_shouldReturnDonorsWithDonationsInDateRange() {
        DateTime startDate = new DateTime().minusDays(14);
        DateTime endDate = new DateTime().minusDays(7);

        Donor firstExpectedDonor = aDonor().build();
        
        List<Donor> expectedDonors = Arrays.asList(firstExpectedDonor);

        // Donation in date range
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonor(firstExpectedDonor)
                        .withDonationDate(startDate.plusDays(1).toDate())
                        .build())
                .buildAndPersist(entityManager);

        // Excluded by donation before start date
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonor(aDonor().build())
                        .withDonationDate(startDate.minusDays(1).toDate())
                        .build())
                .buildAndPersist(entityManager);

        // Excluded by donation after end date
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonor(aDonor().build())
                        .withDonationDate(endDate.plusDays(1).toDate())
                        .build())
                .buildAndPersist(entityManager);
        
        List<Donor> returnedDonors = postDonationCounsellingRepository.findDonorsFlaggedForCounselling(
                startDate.toDate(), endDate.toDate(), NO_DONOR_PANEL);
        
        assertThat(returnedDonors, is(expectedDonors));
    }
    
    @Test
    public void testFindFlaggedPostDonationCounsellingForDonorWithNoPostDonationCounselling_shouldReturnNull() {
        
        Donor donor = aDonor().buildAndPersist(entityManager);

        PostDonationCounselling returnedPostDonationCounselling = postDonationCounsellingRepository
                .findFlaggedPostDonationCounsellingForDonor(donor);

        assertThat(returnedPostDonationCounselling, is(nullValue()));
    }
    
    @Test
    public void testFindFlaggedPostDonationCounsellingForDonor_shouldReturnFirstFlaggedPostDonationCounsellingForDonor() {
        
        Donor donor = aDonor().build();

        // Excluded by date
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonor(donor)
                        .withDonationDate(new DateTime().minusDays(3).toDate())
                        .build())
                .buildAndPersist(entityManager);

        // Excluded by flag
        aPostDonationCounselling()
                .thatIsNotFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonor(donor)
                        .withDonationDate(new DateTime().minusDays(7).toDate())
                        .build())
                .buildAndPersist(entityManager);

        // Excluded by donor
        aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonor(aDonor().build())
                        .withDonationDate(new DateTime().minusDays(7).toDate())
                        .build())
                .buildAndPersist(entityManager);

        PostDonationCounselling expectedPostDonationCounselling = aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .withDonation(aDonation()
                        .withDonor(donor)
                        .withDonationDate(new DateTime().minusDays(5).toDate())
                        .build())
                .buildAndPersist(entityManager);
        
        PostDonationCounselling returnedPostDonationCounselling = postDonationCounsellingRepository
                .findFlaggedPostDonationCounsellingForDonor(donor);

        assertThat(returnedPostDonationCounselling, is(expectedPostDonationCounselling));
    }

}
