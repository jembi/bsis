package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonationFullViewModelBuilder.aDonationFullViewModel;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.DonorViewModelBuilder.aDonorViewModel;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aReferralSiteBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aReferralSite;
import static org.jembi.bsis.helpers.builders.LocationViewModelBuilder.aLocationViewModel;
import static org.jembi.bsis.helpers.builders.PostDonationCounsellingBackingFormBuilder.aPostDonationCounsellingBackingForm;
import static org.jembi.bsis.helpers.builders.PostDonationCounsellingBuilder.aPostDonationCounselling;
import static org.jembi.bsis.helpers.builders.PostDonationCounsellingSummaryViewModelBuilder.aPostDonationCounsellingSummaryViewModel;
import static org.jembi.bsis.helpers.builders.PostDonationCounsellingViewModelBuilder.aPostDonationCounsellingViewModel;
import static org.jembi.bsis.helpers.matchers.PostDonationCounsellingMatcher.hasSameStateAsPostDonationCounselling;
import static org.jembi.bsis.helpers.matchers.PostDonationCounsellingSummaryViewModelMatcher.hasSameStateAsPostDonationCounsellingSummaryViewModel;
import static org.jembi.bsis.helpers.matchers.PostDonationCounsellingViewModelMatcher.hasSameStateAsPostDonationCounsellingViewModel;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.backingform.PostDonationCounsellingBackingForm;
import org.jembi.bsis.helpers.builders.LocationViewModelBuilder;
import org.jembi.bsis.model.counselling.CounsellingStatus;
import org.jembi.bsis.model.counselling.PostDonationCounselling;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.repository.PostDonationCounsellingRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.DonationFullViewModel;
import org.jembi.bsis.viewmodel.DonorViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.PostDonationCounsellingSummaryViewModel;
import org.jembi.bsis.viewmodel.PostDonationCounsellingViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

public class PostDonationCounsellingFactoryTests extends UnitTestSuite {

  @InjectMocks
  @Spy
  private PostDonationCounsellingFactory postDonationCounsellingFactory;
  @Mock
  private PostDonationCounsellingRepository postDonationCounsellingRepository;
  @Mock
  private DonationFactory donationFactory;
  @Mock
  private DonorViewModelFactory donorFactory;
  @Mock
  private LocationFactory locationFactory;
  @Mock
  private LocationRepository locationRepository;

  @Test
  public void testCreateViewModel_shouldReturnViewModelWithCorrectDonorAndPermissionsTrue() {

    boolean canRemoveStatus = true;
    UUID donorId = UUID.randomUUID();
    UUID donationId = UUID.randomUUID();
    UUID postDonationCounsellingId = UUID.randomUUID();
    UUID referralSiteId = UUID.randomUUID();
    
    Donor donor = aDonor().withId(donorId).build();
    Donation donation = aDonation().withId(donationId).withDonor(donor).build();
    Location referralSite = aReferralSite().withId(referralSiteId).withName("Care").build();

    PostDonationCounselling postDonationCounselling = aPostDonationCounselling()
        .withId(postDonationCounsellingId)
        .withDonation(donation)
        .thatIsFlaggedForCounselling()
        .thatIsReferred()
        .withReferralSite(referralSite)
        .withNotes("notes")
        .build();
    
    DonorViewModel expectedDonorViewModel = aDonorViewModel().withDonor(donor).build();
    DonationFullViewModel expectedDonationFullViewModel = aDonationFullViewModel().withId(donationId).build();
    LocationViewModel expectedReferralSiteViewModel = aLocationViewModel().withId(referralSiteId).build();

    PostDonationCounsellingViewModel expectedPostDonationCounsellingViewModel = aPostDonationCounsellingViewModel()
        .withId(postDonationCounsellingId)
        .withDonation(expectedDonationFullViewModel)
        .withDonor(expectedDonorViewModel)
        .withPermission("canRemoveStatus", canRemoveStatus)
        .thatIsFlaggedForCounselling()
        .thatIsReferred()
        .withReferralSite(expectedReferralSiteViewModel)
        .withNotes("notes")
        .build();

    when(postDonationCounsellingRepository.countNotFlaggedPostDonationCounsellingsForDonor(donorId)).thenReturn(1);
    when(donationFactory.createDonationFullViewModelWithoutPermissions(donation)).thenReturn(expectedDonationFullViewModel);
    when(donorFactory.createDonorViewModel(donor)).thenReturn(expectedDonorViewModel);
    when(locationFactory.createViewModel(referralSite)).thenReturn(expectedReferralSiteViewModel);

    PostDonationCounsellingViewModel returnedPostDonationCounsellingViewModel = postDonationCounsellingFactory
        .createViewModel(postDonationCounselling);

    assertThat(returnedPostDonationCounsellingViewModel, hasSameStateAsPostDonationCounsellingViewModel(expectedPostDonationCounsellingViewModel));

  }

  @Test
  public void testCreateViewModel_shouldReturnViewModelWithCorrectDonorAndPermissionsFalse() {

    boolean canRemoveStatus = false;
    UUID donorId = UUID.randomUUID();
    UUID donationId = UUID.randomUUID();
    UUID postDonationCounsellingId = UUID.randomUUID();
    
    Donor donor = aDonor().withId(donorId).build();
    Donation donation = aDonation().withId(donationId).withDonor(donor).build();

    PostDonationCounselling postDonationCounselling = aPostDonationCounselling()
        .withId(postDonationCounsellingId)
        .withDonation(donation)
        .thatIsFlaggedForCounselling()
        .thatIsNotReferred()
        .build();
    
    DonorViewModel expectedDonorViewModel = aDonorViewModel().withDonor(donor).build();
    DonationFullViewModel expectedDonationFullViewModel = aDonationFullViewModel().withId(donationId).build();

    PostDonationCounsellingViewModel expectedPostDonationCounsellingViewModel = aPostDonationCounsellingViewModel()
        .withId(postDonationCounsellingId)
        .withDonation(expectedDonationFullViewModel)
        .withDonor(expectedDonorViewModel)
        .withPermission("canRemoveStatus", canRemoveStatus)
        .thatIsFlaggedForCounselling()
        .thatIsNotReferred()
        .build();

    when(postDonationCounsellingRepository.countNotFlaggedPostDonationCounsellingsForDonor(donorId)).thenReturn(0);
    when(donationFactory.createDonationFullViewModelWithoutPermissions(donation)).thenReturn(expectedDonationFullViewModel);
    when(donorFactory.createDonorViewModel(donor)).thenReturn(expectedDonorViewModel);

    PostDonationCounsellingViewModel returnedPostDonationCounsellingViewModel = postDonationCounsellingFactory
        .createViewModel(postDonationCounselling);

    assertThat(returnedPostDonationCounsellingViewModel, hasSameStateAsPostDonationCounsellingViewModel(expectedPostDonationCounsellingViewModel));

  }

  @Test
  public void testCreateViewModelWithReceivedCounsellingStatus_shouldReturnCorrectViewModel() {

    UUID donorId = UUID.randomUUID();
    UUID donationId = UUID.randomUUID();
    UUID postDonationCounsellingId = UUID.randomUUID();
    Date counsellingDate = new Date();
    CounsellingStatus counsellingStatus = CounsellingStatus.RECEIVED_COUNSELLING;
    String notes = "Given counselling";

    Donor donor = aDonor().withId(donorId).build();
    Donation donation = aDonation().withId(donationId).withDonor(donor).build();

    PostDonationCounselling postDonationCounselling = aPostDonationCounselling()
        .withId(postDonationCounsellingId)
        .withDonation(donation)
        .thatIsNotFlaggedForCounselling()
        .withCounsellingStatus(counsellingStatus)
        .withCounsellingDate(counsellingDate)
        .thatIsNotReferred()
        .withNotes(notes)
        .build();

    DonorViewModel expectedDonorViewModel = aDonorViewModel().withDonor(donor).build();
    DonationFullViewModel expectedDonationFullViewModel = aDonationFullViewModel().withId(donationId).build();

    PostDonationCounsellingViewModel expectedPostDonationCounsellingViewModel = aPostDonationCounsellingViewModel()
        .withId(postDonationCounsellingId)
        .withDonation(expectedDonationFullViewModel)
        .withDonor(expectedDonorViewModel)
        .withPermission("canRemoveStatus", false)
        .thatIsNotFlaggedForCounselling()
        .thatIsNotReferred()
        .withCounsellingStatus(counsellingStatus)
        .withCounsellingDate(counsellingDate)
        .withNotes(notes)
        .build();

    when(postDonationCounsellingRepository.countNotFlaggedPostDonationCounsellingsForDonor(donorId)).thenReturn(0);
    when(donationFactory.createDonationFullViewModelWithoutPermissions(donation)).thenReturn(expectedDonationFullViewModel);
    when(donorFactory.createDonorViewModel(donor)).thenReturn(expectedDonorViewModel);

    PostDonationCounsellingViewModel returnedPostDonationCounsellingViewModel = postDonationCounsellingFactory
        .createViewModel(postDonationCounselling);

    assertThat(returnedPostDonationCounsellingViewModel, hasSameStateAsPostDonationCounsellingViewModel(expectedPostDonationCounsellingViewModel));
  }

  @Test
  public void testCreateViewModelWithRefusedCounsellingStatus_shouldReturnCorrectViewModel() {

    UUID donorId = UUID.randomUUID();
    UUID donationId = UUID.randomUUID();
    UUID postDonationCounsellingId = UUID.randomUUID();
    CounsellingStatus counsellingStatus = CounsellingStatus.REFUSED_COUNSELLING;
    String notes = "Did not have time to talk to us";

    Donor donor = aDonor().withId(donorId).build();
    Donation donation = aDonation().withId(donationId).withDonor(donor).build();

    PostDonationCounselling postDonationCounselling = aPostDonationCounselling()
        .withId(postDonationCounsellingId)
        .withDonation(donation)
        .thatIsNotFlaggedForCounselling()
        .withCounsellingStatus(counsellingStatus)
        .withCounsellingDate(null)
        .withNotes(notes)
        .withReferred(null)
        .build();

    DonorViewModel expectedDonorViewModel = aDonorViewModel().withDonor(donor).build();
    DonationFullViewModel expectedDonationFullViewModel = aDonationFullViewModel().withId(donationId).build();

    PostDonationCounsellingViewModel expectedPostDonationCounsellingViewModel = aPostDonationCounsellingViewModel()
        .withId(postDonationCounsellingId)
        .withDonation(expectedDonationFullViewModel)
        .withDonor(expectedDonorViewModel)
        .withPermission("canRemoveStatus", false)
        .thatIsNotFlaggedForCounselling()
        .withCounsellingStatus(counsellingStatus)
        .withCounsellingDate(null)
        .withNotes(notes)
        .withReferred(null)
        .build();

    when(postDonationCounsellingRepository.countNotFlaggedPostDonationCounsellingsForDonor(donorId)).thenReturn(0);
    when(donationFactory.createDonationFullViewModelWithoutPermissions(donation)).thenReturn(expectedDonationFullViewModel);
    when(donorFactory.createDonorViewModel(donor)).thenReturn(expectedDonorViewModel);

    PostDonationCounsellingViewModel returnedPostDonationCounsellingViewModel = postDonationCounsellingFactory
        .createViewModel(postDonationCounselling);

    assertThat(returnedPostDonationCounsellingViewModel, hasSameStateAsPostDonationCounsellingViewModel(expectedPostDonationCounsellingViewModel));
  }

  @Test
  public void testCreateEntityThatIsNotReferred_shouldReturnEntityInCorrectState() {
    Date counsellingDate = new Date();
    UUID postDonationCounsellingId = UUID.randomUUID();

    PostDonationCounsellingBackingForm form = aPostDonationCounsellingBackingForm()
        .withId(postDonationCounsellingId)
        .withCounsellingDate(counsellingDate)
        .withCounsellingStatus(CounsellingStatus.RECEIVED_COUNSELLING)
        .thatIsNotFlaggedForCounselling()
        .thatIsNotReferred()
        .withNotes("notes")
        .build();

    PostDonationCounselling expectedEntity = aPostDonationCounselling()
        .withId(postDonationCounsellingId)
        .withCounsellingDate(counsellingDate)
        .withCounsellingStatus(CounsellingStatus.RECEIVED_COUNSELLING)
        .thatIsNotFlaggedForCounselling()
        .withDonation(null) // donation is not mapped in the form, so must be null
        .withNotes("notes")
        .thatIsNotReferred()
        .build();

    PostDonationCounselling returnedEntity = postDonationCounsellingFactory.createEntity(form);

    assertThat(returnedEntity, hasSameStateAsPostDonationCounselling(expectedEntity));
  }

  @Test
  public void testCreateEntityThatIsReferred_shouldReturnEntityInCorrectState() {
    Date counsellingDate = new Date();
    UUID locationId = UUID.randomUUID();
    UUID postDonationCounsellingId = UUID.randomUUID(); 

    LocationBackingForm referralSiteForm = aReferralSiteBackingForm().withId(locationId).withName("Care").build();
    PostDonationCounsellingBackingForm form = aPostDonationCounsellingBackingForm()
        .withId(postDonationCounsellingId)
        .withCounsellingDate(counsellingDate)
        .withCounsellingStatus(CounsellingStatus.RECEIVED_COUNSELLING)
        .thatIsNotFlaggedForCounselling()
        .thatIsReferred()
        .withReferralSite(referralSiteForm)
        .withNotes("notes")
        .build();

    Location referralSite = aReferralSite().withId(locationId).withName("Care").build();
    PostDonationCounselling expectedEntity = aPostDonationCounselling()
        .withId(postDonationCounsellingId)
        .withCounsellingDate(counsellingDate)
        .withCounsellingStatus(CounsellingStatus.RECEIVED_COUNSELLING)
        .thatIsNotFlaggedForCounselling()
        .withDonation(null) // donation is not mapped in the form, so must be null
        .withNotes("notes")
        .thatIsReferred()
        .withReferralSite(referralSite)
        .build();

    when(locationRepository.getLocation(locationId)).thenReturn(referralSite);

    PostDonationCounselling returnedEntity = postDonationCounsellingFactory.createEntity(form);

    assertThat(returnedEntity, hasSameStateAsPostDonationCounselling(expectedEntity));
  }

  @Test
  public void testCreateSummaryViewModels_shouldReturnCorrectViewModels() {
    Donor donor = aDonor().withId(UUID.randomUUID()).build();
    Donation donation = aDonation().withDonor(donor).build();
    PostDonationCounselling entity1 = aPostDonationCounselling().withId(UUID.randomUUID()).withDonation(donation).build();
    PostDonationCounselling entity2 = aPostDonationCounselling().withId(UUID.randomUUID()).withDonation(donation).build();
    PostDonationCounselling entity3 = aPostDonationCounselling().withId(UUID.randomUUID()).withDonation(donation).build();

    List<PostDonationCounselling> entities = Arrays.asList(entity1, entity2, entity3);

    List<PostDonationCounsellingSummaryViewModel> viewModels =
        postDonationCounsellingFactory.createSummaryViewModels(entities);

    assertThat(viewModels.size(), is(3));
    verify(postDonationCounsellingFactory).createSummaryViewModel(entity1);
    verify(postDonationCounsellingFactory).createSummaryViewModel(entity2);
    verify(postDonationCounsellingFactory).createSummaryViewModel(entity3);
  }

  @Test
  public void testCreateSummaryViewModel_shouldReturnCorrectViewModel() {
    UUID postDonationCounsellingId = UUID.randomUUID();
    Donor donor = aDonor()
        .withId(postDonationCounsellingId)
        .withDonorNumber("123456")
        .withBirthDate(new Date())
        .withGender(Gender.female)
        .withFirstName("First")
        .withLastName("Last")
        .build();
    UUID locationId = UUID.randomUUID();
    Donation donation = aDonation()
        .withId(UUID.randomUUID())
        .withDonor(donor)
        .withDonationIdentificationNumber("9000000")
        .withBloodAbo("A")
        .withBloodRh("+")
        .withDonationDate(new Date())
        .withVenue(aLocation().withId(locationId).build())
        .build();

    PostDonationCounselling postDonationCounselling = aPostDonationCounselling()
        .withId(postDonationCounsellingId)
        .withDonation(donation)
        .thatIsNotFlaggedForCounselling()
        .withCounsellingStatus(CounsellingStatus.RECEIVED_COUNSELLING)
        .withCounsellingDate(new Date())
        .withNotes("notes")
        .withReferred(true)
        .build();
    
    LocationViewModel venueViewModel = LocationViewModelBuilder.aLocationViewModel().withId(locationId).build();
    
    PostDonationCounsellingSummaryViewModel expectedSummary = aPostDonationCounsellingSummaryViewModel()
        .withId(postDonationCounsellingId)
        .withBirthDate(donor.getBirthDate())
        .withBloodGroup(donation.getBloodAbo() + donation.getBloodRh())
        .withDonorId(donor.getId())
        .withCounselled("Y")
        .withReferred("Y")
        .withDonorNumber(donor.getDonorNumber())
        .withFirstName(donor.getFirstName())
        .withLastName(donor.getLastName())
        .withDonationDate(donation.getDonationDate())
        .withDonationIdentificationNumber(donation.getDonationIdentificationNumber())
        .withCounsellingDate(postDonationCounselling.getCounsellingDate())
        .withVenue(venueViewModel)
        .withGender(donor.getGender())
        .build();
    
    when(locationFactory.createViewModel(donation.getVenue())).thenReturn(venueViewModel);
    
    PostDonationCounsellingSummaryViewModel summary =
        postDonationCounsellingFactory.createSummaryViewModel(postDonationCounselling);

    assertThat(summary, hasSameStateAsPostDonationCounsellingSummaryViewModel(expectedSummary));
  }
  
  @Test
  public void testCreateSummaryViewModelWhereReferredIsNull_shouldSetReferredToEmpty() {

    Donor donor = aDonor().withId(UUID.randomUUID()).build();
    Donation donation = aDonation().withDonor(donor).build();
    PostDonationCounselling counselling = aPostDonationCounselling()
        .withId(UUID.randomUUID())
        .withDonation(donation)
        .withReferred(null)
        .build();

    PostDonationCounsellingSummaryViewModel summary =
        postDonationCounsellingFactory.createSummaryViewModel(counselling);

    assertThat(summary.getReferred(), isEmptyString());
  }

  @Test
  public void testCreateSummaryViewModelWhereReferredIsTrue_shouldSetReferredToY() {

    Donor donor = aDonor().withId(UUID.randomUUID()).build();
    Donation donation = aDonation().withDonor(donor).build();
    PostDonationCounselling counselling = aPostDonationCounselling()
        .withId(UUID.randomUUID())
        .withDonation(donation)
        .withReferred(true)
        .build();

    PostDonationCounsellingSummaryViewModel summary =
        postDonationCounsellingFactory.createSummaryViewModel(counselling);

    assertThat(summary.getReferred(), is("Y"));
  }
  
  @Test
  public void testCreateSummaryViewModelWhereReferredIsFalse_shouldSetReferredToN() {

    Donor donor = aDonor().withId(UUID.randomUUID()).build();
    Donation donation = aDonation().withDonor(donor).build();
    PostDonationCounselling counselling = aPostDonationCounselling()
        .withId(UUID.randomUUID())
        .withDonation(donation)
        .withReferred(false)
        .build();

    PostDonationCounsellingSummaryViewModel summary =
        postDonationCounsellingFactory.createSummaryViewModel(counselling);

    assertThat(summary.getReferred(), is("N"));
  }
  
  @Test
  public void testCreateSummaryViewModelWhereStatusIsNull_shouldSetCouselledToEmpty() {

    Donor donor = aDonor().withId(UUID.randomUUID()).build();
    Donation donation = aDonation().withDonor(donor).build();
    PostDonationCounselling counselling = aPostDonationCounselling()
        .withId(UUID.randomUUID())
        .withDonation(donation)
        .withCounsellingStatus(null)
        .build();

    PostDonationCounsellingSummaryViewModel summary =
        postDonationCounsellingFactory.createSummaryViewModel(counselling);

    assertThat(summary.getReferred(), isEmptyString());
  }
  
  @Test
  public void testCreateSummaryViewModelWhereStatusIsReceivedCounselling_shouldSetCouselledToY() {

    Donor donor = aDonor().withId(UUID.randomUUID()).build();
    Donation donation = aDonation().withDonor(donor).build();
    PostDonationCounselling counselling = aPostDonationCounselling()
        .withId(UUID.randomUUID())
        .withDonation(donation)
        .withCounsellingStatus(CounsellingStatus.RECEIVED_COUNSELLING)
        .build();

    PostDonationCounsellingSummaryViewModel summary =
        postDonationCounsellingFactory.createSummaryViewModel(counselling);

    assertThat(summary.getCounselled(), is("Y"));
  }
  
  @Test
  public void testCreateSummaryViewModelWhereStatusIsRefusedCounselling_shouldSetCouselledToR() {

    Donor donor = aDonor().withId(UUID.randomUUID()).build();
    Donation donation = aDonation().withDonor(donor).build();
    PostDonationCounselling counselling = aPostDonationCounselling()
        .withId(UUID.randomUUID())
        .withDonation(donation)
        .withCounsellingStatus(CounsellingStatus.REFUSED_COUNSELLING)
        .build();

    PostDonationCounsellingSummaryViewModel summary =
        postDonationCounsellingFactory.createSummaryViewModel(counselling);

    assertThat(summary.getCounselled(), is("R"));
  }
  
  @Test
  public void testCreateSummaryViewModelWhereStatusIsDidNotReceiveCounselling_shouldSetCouselledToN() {

    Donor donor = aDonor().withId(UUID.randomUUID()).build();
    Donation donation = aDonation().withDonor(donor).build();
    PostDonationCounselling counselling = aPostDonationCounselling()
        .withId(UUID.randomUUID())
        .withDonation(donation)
        .withCounsellingStatus(CounsellingStatus.DID_NOT_RECEIVE_COUNSELLING)
        .build();

    PostDonationCounsellingSummaryViewModel summary =
        postDonationCounsellingFactory.createSummaryViewModel(counselling);

    assertThat(summary.getCounselled(), is("N"));
  }
}
