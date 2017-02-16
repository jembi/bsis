package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.builders.PostDonationCounsellingBuilder.aPostDonationCounselling;
import static org.jembi.bsis.helpers.builders.UserBuilder.aUser;
import static org.jembi.bsis.helpers.matchers.SameDayMatcher.isSameDayAs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;

import org.jembi.bsis.dto.PostDonationCounsellingExportDTO;
import org.jembi.bsis.model.counselling.CounsellingStatus;
import org.jembi.bsis.model.counselling.PostDonationCounselling;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.suites.SecurityContextDependentTestSuite;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PostDonationCounsellingRepositoryTests extends SecurityContextDependentTestSuite {

  private static final Date NO_START_DATE = null;
  private static final Date NO_END_DATE = null;
  private static final Set<Long> NO_VENUES = null;
  private static final CounsellingStatus NO_COUNSELLING_STATUS = null; 
  private static final Boolean NO_REFERRED = null;

  @Autowired
  private PostDonationCounsellingRepository postDonationCounsellingRepository;

  @Test
  public void testFindPostDonationCounsellingWithNoParametersAndFlagedForCounselling_shouldReturnAllActivePostDonationCounsellingRecords() {

    PostDonationCounselling firstExpectedPostDonationCounselling = aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(aDonation().build())
        .buildAndPersist(entityManager);

    PostDonationCounselling secondExpectedPostDonationCounselling = aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(aDonation().build())
        .buildAndPersist(entityManager);

    List<PostDonationCounselling> expectedPostDonationCounsellingList = Arrays.asList(firstExpectedPostDonationCounselling, secondExpectedPostDonationCounselling);

    List<PostDonationCounselling> returnedPostDonationCounsellingList = postDonationCounsellingRepository.findPostDonationCounselling(
        NO_START_DATE, NO_END_DATE, NO_VENUES, NO_COUNSELLING_STATUS, NO_REFERRED, true);
    
    assertThat(returnedPostDonationCounsellingList, is(expectedPostDonationCounsellingList));
  }

  @Test
  public void testFindPostDonationCounsellingWithVenues_shouldReturnPostDonationCounsellingsWithDonationVenues() {

    Location firstVenue = aVenue().buildAndPersist(entityManager);
    Location secondVenue = aVenue().buildAndPersist(entityManager);
    List<Long> venues = Arrays.asList(firstVenue.getId(), secondVenue.getId());
    
    PostDonationCounselling one = aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(aDonation().withVenue(firstVenue).build())
        .buildAndPersist(entityManager);

    PostDonationCounselling two = aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(aDonation().withVenue(secondVenue).build())
        .buildAndPersist(entityManager);

    List<PostDonationCounselling> expectedPostDonationCounsellingList = Arrays.asList(one, two);

    // Excluded by venue
    aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(aDonation()
            .withVenue(aVenue().build())
            .build())
        .buildAndPersist(entityManager);

    List<PostDonationCounselling> returnedDonations = postDonationCounsellingRepository.findPostDonationCounselling(
        NO_START_DATE, NO_END_DATE, new HashSet<>(venues), NO_COUNSELLING_STATUS, NO_REFERRED, true);

    assertThat(returnedDonations, is(expectedPostDonationCounsellingList));
  }

  @Test
  public void testFindPostDonationCounsellingWithStartDate_shouldReturnPostDonationCounsellingsWithDonationsAferStartDate() {
    DateTime startDate = new DateTime().minusDays(7);

    // Donation on start date
    PostDonationCounselling postDonationCounsellingWithDonationOnStartDate = aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(aDonation().withDonationDate(startDate.toDate()).build())
        .buildAndPersist(entityManager);

    // Donation after start date
    PostDonationCounselling postDonationCounsellingWithDonationAfterStartDate = aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(aDonation().withDonationDate(startDate.plusDays(3).toDate()).build())
        .buildAndPersist(entityManager);
    
    List<PostDonationCounselling> expectedPostDonationCounsellingList = new ArrayList<>(Arrays.asList(postDonationCounsellingWithDonationOnStartDate, postDonationCounsellingWithDonationAfterStartDate));

    // Excluded by donation before start date
    aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(aDonation()
            .withDonor(aDonor().build())
            .withDonationDate(startDate.minusDays(3).toDate())
            .build())
        .buildAndPersist(entityManager);

    List<PostDonationCounselling> returnedPostDonationCounsellingList = postDonationCounsellingRepository.findPostDonationCounselling(
        startDate.toDate(), NO_END_DATE, NO_VENUES, NO_COUNSELLING_STATUS, NO_REFERRED, true);

    assertThat(returnedPostDonationCounsellingList, is(expectedPostDonationCounsellingList));
  }

  @Test
  public void testFindPostDonationCounsellingWithDonorsFlaggedForCounsellingWithEndDate_shouldReturnPostDonationCounsellingWithDonationsBeforeEndDate() {
    DateTime endDate = new DateTime().minusDays(7);

    // Donation on end date
    PostDonationCounselling postDonationCounsellingWithDonationOnEndDate = aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(aDonation().withDonationDate(endDate.toDate()).build())
        .buildAndPersist(entityManager);

    // Donation before end date
    PostDonationCounselling postDonationCounsellingWithDonationbeforeEndDate = aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(aDonation().withDonationDate(endDate.minusDays(3).toDate()).build())
        .buildAndPersist(entityManager);
    
    List<PostDonationCounselling> expectedPostDonationCounsellingList = new ArrayList<>(Arrays.asList(postDonationCounsellingWithDonationOnEndDate, postDonationCounsellingWithDonationbeforeEndDate));

    // Excluded by donation after end date
    aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(aDonation()
            .withDonationDate(endDate.plusDays(3).toDate())
            .build())
        .buildAndPersist(entityManager);

    List<PostDonationCounselling> returnedPostDonationCounsellingList = postDonationCounsellingRepository.findPostDonationCounselling(
        NO_START_DATE, endDate.toDate(), NO_VENUES, NO_COUNSELLING_STATUS, NO_REFERRED, true);

    assertThat(returnedPostDonationCounsellingList, is(expectedPostDonationCounsellingList));
  }

  @Test
  public void testFindPostDonationCounsellingWithDonorFlaggedForCounsellingWithDates_shouldReturnPostDonationCounsellingWithDonationsInDateRange() {
    DateTime startDate = new DateTime().minusDays(14);
    DateTime endDate = new DateTime().minusDays(7);

    // Donation in date range
    aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(aDonation().withDonationDate(startDate.plusDays(1).toDate()).build())
        .buildAndPersist(entityManager);

    // Excluded by isDeleted
    aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsDeleted()
        .withDonation(aDonation().withDonationDate(startDate.plusDays(2).toDate()).build())
        .buildAndPersist(entityManager);

    // Excluded by donation before start date
    aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(aDonation()
            .withDonationDate(startDate.minusDays(1).toDate())
            .build())
        .buildAndPersist(entityManager);

    // Excluded by donation after end date
    aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(aDonation()
            .withDonationDate(endDate.plusDays(1).toDate())
            .build())
        .buildAndPersist(entityManager);

    List<PostDonationCounselling> returnedPostDonationCounsellingList = postDonationCounsellingRepository.findPostDonationCounselling(
        startDate.toDate(), endDate.toDate(), NO_VENUES, NO_COUNSELLING_STATUS, NO_REFERRED, true);

    assertThat(returnedPostDonationCounsellingList.size(), is(1));
  }
  
  @Test
  public void testFindPostDonationCounsellingWithDonorFlaggedForCounsellingWithCounsellingStatus_shouldReturnPostDonationCounsellingWithExpectedCounsellingStatus() {
    
    CounsellingStatus expectedCounsellingStatus = CounsellingStatus.RECEIVED_COUNSELLING;
    CounsellingStatus filteredCounsellingStatus = CounsellingStatus.DID_NOT_RECEIVE_COUNSELLING;
    
    // Donation with expected counselling status
    PostDonationCounselling expectedPostDonationCounselling = aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withCounsellingStatus(expectedCounsellingStatus)
        .buildAndPersist(entityManager);

    // Excluded by counselling status
    aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withCounsellingStatus(filteredCounsellingStatus)
        .buildAndPersist(entityManager);

    // Excluded by counselling status
    aPostDonationCounselling()
    .thatIsFlaggedForCounselling()
    .thatIsNotDeleted()
    .withCounsellingStatus(filteredCounsellingStatus)
    .buildAndPersist(entityManager);
    
    List<PostDonationCounselling> expectedPostDonationCounsellingList = new ArrayList<>(Arrays.asList(expectedPostDonationCounselling));

    List<PostDonationCounselling> returnedPostDonationCounsellingList = postDonationCounsellingRepository.findPostDonationCounselling(
        NO_START_DATE, NO_END_DATE, NO_VENUES, expectedCounsellingStatus, NO_REFERRED, true);

    assertThat(returnedPostDonationCounsellingList, is(expectedPostDonationCounsellingList));
  }
  
  @Test
  public void testFindPostDonationCounsellingWithDonorFlaggedForCounsellingWithReferred_shouldReturnPostDonationCounsellingWithReferredTrue() {
    
    // PostDonationCounselling referred true
    PostDonationCounselling expectedPostDonationCounselling = aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withReferred(Boolean.TRUE)
        .buildAndPersist(entityManager);

    // Excluded by referred false
    aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withReferred(Boolean.FALSE)
        .buildAndPersist(entityManager);

    // Excluded by referred false
    aPostDonationCounselling()
    .thatIsFlaggedForCounselling()
    .thatIsNotDeleted()
    .withReferred(Boolean.FALSE)
    .buildAndPersist(entityManager);
    
    List<PostDonationCounselling> expectedPostDonationCounsellingList = new ArrayList<>(Arrays.asList(expectedPostDonationCounselling));

    List<PostDonationCounselling> returnedPostDonationCounsellingList = postDonationCounsellingRepository.findPostDonationCounselling(
        NO_START_DATE, NO_END_DATE, NO_VENUES, NO_COUNSELLING_STATUS, Boolean.TRUE, true);

    assertThat(returnedPostDonationCounsellingList, is(expectedPostDonationCounsellingList));
  }

  @Test(expected = NoResultException.class)
  public void testFindFlaggedPostDonationCounsellingForDonorWithNoPostDonationCounselling_shouldThrow() {

    Donor donor = aDonor().buildAndPersist(entityManager);

    postDonationCounsellingRepository.findPostDonationCounsellingForDonor(donor.getId());
  }

  @Test
  public void testFindFlaggedPostDonationCounsellingForDonor_shouldReturnFirstFlaggedPostDonationCounsellingForDonor() {

    Donor donor = aDonor().build();

    // Excluded by date
    aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(aDonation()
            .withDonor(donor)
            .withDonationDate(new DateTime().minusDays(3).toDate())
            .build())
        .buildAndPersist(entityManager);

    // Excluded by donor
    aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(aDonation()
            .withDonor(aDonor().build())
            .withDonationDate(new DateTime().minusDays(7).toDate())
            .build())
        .buildAndPersist(entityManager);

    // Excluded by isDeleted
    aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsDeleted()
        .withDonation(aDonation()
            .withDonor(donor)
            .withDonationDate(new DateTime().minusDays(5).toDate())
            .build())
        .buildAndPersist(entityManager);

    PostDonationCounselling expectedPostDonationCounselling = aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(aDonation()
            .withDonor(donor)
            .withDonationDate(new DateTime().minusDays(5).toDate())
            .build())
        .buildAndPersist(entityManager);

    PostDonationCounselling returnedPostDonationCounselling = postDonationCounsellingRepository
        .findPostDonationCounsellingForDonor(donor.getId());

    assertThat(returnedPostDonationCounselling, is(expectedPostDonationCounselling));
  }

  @Test
  public void testCountFlaggedPostDonationCounsellingsForDonorWithNoPostDonationCounsellings_shouldReturnZero() {

    Donor donor = aDonor().buildAndPersist(entityManager);

    int returnedCount = postDonationCounsellingRepository.countFlaggedPostDonationCounsellingsForDonor(donor.getId());

    assertThat(returnedCount, is(0));
  }

  @Test
  public void testCountFlaggedPostDonationCounsellingsForDonor_shouldReturnCorrectCount() {

    Donor donor = aDonor().build();

    // Excluded by donor
    aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(aDonation()
            .withDonor(aDonor().build())
            .build())
        .buildAndPersist(entityManager);

    // Excluded by flag
    aPostDonationCounselling()
        .thatIsNotFlaggedForCounselling()
        .withDonation(aDonation().withDonor(donor).build())
        .buildAndPersist(entityManager);

    // Excluded by isDeleted
    aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsDeleted()
        .withDonation(aDonation().withDonor(donor).build())
        .buildAndPersist(entityManager);

    // Expected
    aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(aDonation().withDonor(donor).build())
        .buildAndPersist(entityManager);
    aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(aDonation().withDonor(donor).build())
        .buildAndPersist(entityManager);

    int returnedCount = postDonationCounsellingRepository.countFlaggedPostDonationCounsellingsForDonor(donor.getId());

    assertThat(returnedCount, is(2));
  }

  @Test
  public void testCountNotFlaggedPostDonationCounsellingsForDonor_shouldReturnCorrectCount() {
    Donor donor = aDonor().build();

    // Excluded by donor
    aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(aDonation()
            .withDonor(aDonor().build())
            .build())
        .buildAndPersist(entityManager);

    // Excluded by flag
    aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .withDonation(aDonation().withDonor(donor).build())
        .buildAndPersist(entityManager);

    // Excluded by isDeleted
    aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsDeleted()
        .withDonation(aDonation().withDonor(donor).build())
        .buildAndPersist(entityManager);

    // Expected
    aPostDonationCounselling()
        .thatIsNotFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(aDonation().withDonor(donor).build())
        .buildAndPersist(entityManager);

    int returnedCount = postDonationCounsellingRepository.countNotFlaggedPostDonationCounsellingsForDonor(donor.getId());

    assertThat(returnedCount, is(1));

  }

  @Test
  public void testFindPostDonationCounsellingForDonationWithNoExistingCounselling_shouldReturnNull() {

    Donation donation = aDonation().buildAndPersist(entityManager);

    PostDonationCounselling returnedCounselling = postDonationCounsellingRepository.findPostDonationCounsellingForDonation(
        donation);

    assertThat(returnedCounselling, is(nullValue()));
  }

  @Test
  public void testFindPostDonationCounsellingForDonation_shouldReturnCorrectCounselling() {

    Donation donation = aDonation().buildAndPersist(entityManager);

    // Excluded by donation - persisted before expected counselling to check order
    aPostDonationCounselling().withDonation(aDonation().build()).buildAndPersist(entityManager);

    PostDonationCounselling expectedCounselling = aPostDonationCounselling()
        .withDonation(donation)
        .buildAndPersist(entityManager);

    // Excluded by donation - persisted after expected counselling to check order
    aPostDonationCounselling().withDonation(aDonation().build()).buildAndPersist(entityManager);

    PostDonationCounselling returnedCounselling = postDonationCounsellingRepository.findPostDonationCounsellingForDonation(
        donation);

    assertThat(returnedCounselling, is(expectedCounselling));
  }
  
  @Test
  public void testFindPostDonationCounsellingsForExports_shouldReturnPostDonationCounsellingExportDTOsWithTheCorrectState() {
    // Set up fixture
    String donationIdentificationNumber = "1233219";
    String createdByUsername = "created.by";
    Date createdDate = new DateTime().minusDays(7).toDate();
    Date counsellingDate = new Date();

    // Expected
    aPostDonationCounselling()
        .withCreatedBy(aUser().withUsername(createdByUsername).build())
        .withCreatedDate(createdDate)
        .withDonation(aDonation().withDonationIdentificationNumber(donationIdentificationNumber).build())
        .withCounsellingDate(counsellingDate)
        .buildAndPersist(entityManager);

    // Excluded by deleted
    aPostDonationCounselling().thatIsDeleted().buildAndPersist(entityManager);

    // Exercise SUT
    List<PostDonationCounsellingExportDTO> returnedDTOs =
        postDonationCounsellingRepository.findPostDonationCounsellingsForExport();
    
    // Verify
    assertThat(returnedDTOs.size(), is(1));
    
    // Verify DTO state
    PostDonationCounsellingExportDTO returnedDTO = returnedDTOs.get(0);
    assertThat(returnedDTO.getDonationIdentificationNumber(), is(donationIdentificationNumber));
    assertThat(returnedDTO.getCreatedBy(), is(createdByUsername));
    assertThat(returnedDTO.getCreatedDate(), isSameDayAs(createdDate));
    assertThat(returnedDTO.getLastUpdatedBy(), is(USERNAME));
    assertThat(returnedDTO.getLastUpdated(), isSameDayAs(new Date()));
    assertThat(returnedDTO.getCounsellingDate(), isSameDayAs(counsellingDate));
  }

  @Test
  public void testFindPostDonationCounsellingsForExports_shouldReturnPostDonationCounsellingExportDTOsOrderedByCreatedDate() {
    // Set up fixture
    String firstDonationIdentificationNumber = "1233219";
    String secondDonationIdentificationNumber = "99887765";
    
    aPostDonationCounselling()
        .withDonation(aDonation().withDonationIdentificationNumber(secondDonationIdentificationNumber).build())
        .withCreatedDate(new DateTime().minusDays(30).toDate())
        .buildAndPersist(entityManager);
    aPostDonationCounselling()
        .withDonation(aDonation().withDonationIdentificationNumber(firstDonationIdentificationNumber).build())
        .withCreatedDate(new DateTime().minusDays(91).toDate())
        .buildAndPersist(entityManager);
    
    // Exercise SUT
    List<PostDonationCounsellingExportDTO> returnedDTOs =
        postDonationCounsellingRepository.findPostDonationCounsellingsForExport();
    
    // Verify
    assertThat(returnedDTOs.size(), is(2));
    assertThat(returnedDTOs.get(0).getDonationIdentificationNumber(), is(firstDonationIdentificationNumber));
    assertThat(returnedDTOs.get(1).getDonationIdentificationNumber(), is(secondDonationIdentificationNumber));
  }
}
