package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.AdverseEventBackingFormBuilder.anAdverseEventBackingForm;
import static org.jembi.bsis.helpers.builders.AdverseEventBuilder.anAdverseEvent;
import static org.jembi.bsis.helpers.builders.AdverseEventTypeBackingFormBuilder.anAdverseEventTypeBackingForm;
import static org.jembi.bsis.helpers.builders.AdverseEventTypeBuilder.anAdverseEventType;
import static org.jembi.bsis.helpers.builders.BloodTypingResolutionBackingFormBuilder.aBloodTypingResolutionBackingForm;
import static org.jembi.bsis.helpers.builders.BloodTypingResolutionsBackingFormBuilder.aBloodTypingResolutionsBackingForm;
import static org.jembi.bsis.helpers.builders.DonationBackingFormBuilder.aDonationBackingForm;
import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aReleasedTestBatch;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;
import static org.jembi.bsis.helpers.matchers.DonationMatcher.hasSameStateAsDonation;
import static org.jembi.bsis.helpers.matchers.DonorMatcher.hasSameStateAsDonor;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.backingform.AdverseEventBackingForm;
import org.jembi.bsis.backingform.BloodTypingResolutionBackingForm;
import org.jembi.bsis.backingform.DonationBackingForm;
import org.jembi.bsis.helpers.builders.BloodTestResultBuilder;
import org.jembi.bsis.helpers.builders.ComponentBuilder;
import org.jembi.bsis.helpers.builders.DonationBatchBuilder;
import org.jembi.bsis.helpers.builders.TestBatchBuilder;
import org.jembi.bsis.model.adverseevent.AdverseEvent;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.bloodtesting.TTIStatus;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.HaemoglobinLevel;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.repository.BloodTestResultRepository;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.repository.PackTypeRepository;
import org.jembi.bsis.repository.bloodtesting.BloodTypingMatchStatus;
import org.jembi.bsis.suites.UnitTestSuite;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class DonationCRUDServiceTests extends UnitTestSuite {

  private static final long IRRELEVANT_DONATION_ID = 2;
  private static final long IRRELEVANT_DONOR_ID = 7;
  private static final long IRRELEVANT_PACK_TYPE_ID = 5009;
  private static final Date IRRELEVANT_DATE_OF_FIRST_DONATION = new DateTime().minusDays(7).toDate();
  private static final Date IRRELEVANT_DATE_OF_LAST_DONATION = new DateTime().minusDays(2).toDate();

  @InjectMocks
  private DonationCRUDService donationCRUDService;
  @Mock
  private DonationConstraintChecker donationConstraintChecker;
  @Mock
  private DonationRepository donationRepository;
  @Mock
  private DonorRepository donorRepository;
  @Mock
  private DonationBatchRepository donationBatchRepository;
  @Mock
  private ComponentCRUDService componentCRUDService;
  @Mock
  private PackTypeRepository packTypeRepository;
  @Mock
  private DonorConstraintChecker donorConstraintChecker;
  @Mock
  private DonorService donorService;
  @Mock
  private PostDonationCounsellingCRUDService postDonationCounsellingCRUDService;
  @Mock
  private TestBatchStatusChangeService testBatchStatusChangeService;
  @Mock
  private BloodTestResultRepository bloodTestResultRepository;
  @Mock
  private BloodTestsService bloodTestsService;

  @Test(expected = IllegalStateException.class)
  public void testDeleteDonationWithConstraints_shouldThrow() {

    when(donationConstraintChecker.canDeleteDonation(IRRELEVANT_DONATION_ID)).thenReturn(false);

    donationCRUDService.deleteDonation(IRRELEVANT_DONATION_ID);
  }

  @Test
  public void testDeleteDonationWithFirstDonation_shouldSoftDeleteDonationAndUpdateDonorFirstDonationDate() {

    // Set up fixture
    Donor existingDonor = aDonor()
        .withId(IRRELEVANT_DONOR_ID)
        .withDateOfFirstDonation(IRRELEVANT_DATE_OF_FIRST_DONATION)
        .withDateOfLastDonation(IRRELEVANT_DATE_OF_LAST_DONATION)
        .build();
    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(existingDonor)
        .withDonationDate(IRRELEVANT_DATE_OF_FIRST_DONATION)
        .build();

    // Set up expectations
    Donation expectedDonation = aDonation()
        .thatIsDeleted()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(existingDonor)
        .withDonationDate(IRRELEVANT_DATE_OF_FIRST_DONATION)
        .build();
    Date expectedDateOfFirstDonation = new Date();
    Donor expectedDonor = aDonor()
        .withId(IRRELEVANT_DONOR_ID)
        .withDateOfFirstDonation(expectedDateOfFirstDonation)
        .withDateOfLastDonation(IRRELEVANT_DATE_OF_LAST_DONATION)
        .build();

    when(donationConstraintChecker.canDeleteDonation(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationRepository.findDateOfFirstDonationForDonor(IRRELEVANT_DONOR_ID)).thenReturn(expectedDateOfFirstDonation);

    // Exercise SUT
    donationCRUDService.deleteDonation(IRRELEVANT_DONATION_ID);

    // Verify
    verify(donationRepository).updateDonation(argThat(hasSameStateAsDonation(expectedDonation)));
    verify(donorRepository).updateDonor(argThat(hasSameStateAsDonor(expectedDonor)));
    verify(donorService).setDonorDueToDonate(argThat(hasSameStateAsDonor(expectedDonor)));
  }

  @Test
  public void testDeleteDonationWithLastDonation_shouldSoftDeleteDonationAndUpdateDonorLastDonationDate() {

    // Set up fixture
    Donor existingDonor = aDonor()
        .withId(IRRELEVANT_DONOR_ID)
        .withDateOfFirstDonation(IRRELEVANT_DATE_OF_FIRST_DONATION)
        .withDateOfLastDonation(IRRELEVANT_DATE_OF_LAST_DONATION)
        .build();
    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(existingDonor)
        .withDonationDate(IRRELEVANT_DATE_OF_LAST_DONATION)
        .build();

    // Set up expectations
    Donation expectedDonation = aDonation()
        .thatIsDeleted()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(existingDonor)
        .withDonationDate(IRRELEVANT_DATE_OF_LAST_DONATION)
        .build();
    Date expectedDateOfLastDonation = new Date();
    Donor expectedDonor = aDonor()
        .withId(IRRELEVANT_DONOR_ID)
        .withDateOfFirstDonation(IRRELEVANT_DATE_OF_FIRST_DONATION)
        .withDateOfLastDonation(expectedDateOfLastDonation)
        .build();

    when(donationConstraintChecker.canDeleteDonation(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationRepository.findDateOfLastDonationForDonor(IRRELEVANT_DONOR_ID)).thenReturn(expectedDateOfLastDonation);

    // Exercise SUT
    donationCRUDService.deleteDonation(IRRELEVANT_DONATION_ID);

    // Verify
    verify(donationRepository).updateDonation(argThat(hasSameStateAsDonation(expectedDonation)));
    verify(donorRepository).updateDonor(argThat(hasSameStateAsDonor(expectedDonor)));
    verify(donorService).setDonorDueToDonate(argThat(hasSameStateAsDonor(expectedDonor)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateDonationWithUpdatedPackTypeAndCannotUpdate_shouldThrow() {

    // Set up fixture
    Donation existingDonation = aDonation().withId(IRRELEVANT_DONATION_ID).build();
    DonationBackingForm donationBackingForm = aDonationBackingForm()
        .withPackType(aPackType().withId(7l).build())
        .build();

    // Set up expectations
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationConstraintChecker.canUpdateDonationFields(IRRELEVANT_DONATION_ID)).thenReturn(false);

    // Exercise SUT
    donationCRUDService.updateDonation(IRRELEVANT_DONATION_ID, donationBackingForm);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateDonationWithUpdatedBleedStartTimeAndCannotUpdate_shouldThrow() {

    // Set up fixture
    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withBleedEndTime(IRRELEVANT_DATE_OF_FIRST_DONATION)
        .withBleedStartTime(IRRELEVANT_DATE_OF_FIRST_DONATION)
        .build();
    DonationBackingForm donationBackingForm = aDonationBackingForm()
        .withBleedStartTime(new Date())
        .withBleedEndTime(IRRELEVANT_DATE_OF_FIRST_DONATION)
        .build();

    // Set up expectations
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationConstraintChecker.canUpdateDonationFields(IRRELEVANT_DONATION_ID)).thenReturn(false);

    // Exercise SUT
    donationCRUDService.updateDonation(IRRELEVANT_DONATION_ID, donationBackingForm);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateDonationWithUpdatedBleedEndTimeAndCannotUpdate_shouldThrow() {

    // Set up fixture
    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withBleedEndTime(IRRELEVANT_DATE_OF_FIRST_DONATION)
        .withBleedStartTime(IRRELEVANT_DATE_OF_FIRST_DONATION)
        .build();
    DonationBackingForm donationBackingForm = aDonationBackingForm()
        .withBleedEndTime(new Date())
        .withBleedStartTime(IRRELEVANT_DATE_OF_FIRST_DONATION)
        .build();

    // Set up expectations
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationConstraintChecker.canUpdateDonationFields(IRRELEVANT_DONATION_ID)).thenReturn(false);

    // Exercise SUT
    donationCRUDService.updateDonation(IRRELEVANT_DONATION_ID, donationBackingForm);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testUpdateDonationWithDifferentPackTypeThatCantEditPackType_shouldThrow() {
    // Set up fixture 
    PackType newPackType = aPackType().withId(2L).build();
    Date irrelevantBleedStartTime = new DateTime().minusMinutes(30).toDate();
    Date irrelevantBleedEndTime = new DateTime().minusMinutes(5).toDate();

    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withPackType(aPackType().withId(1L).build())
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();
    DonationBackingForm donationBackingForm = aDonationBackingForm()
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();
    
    // Set up expectations
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationConstraintChecker.canUpdateDonationFields(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canEditPackType(existingDonation)).thenReturn(false);
    
    // Exercise SUT
    donationCRUDService.updateDonation(IRRELEVANT_DONATION_ID, donationBackingForm);
  }
  
  @Test
  public void testUpdateDonationWithSamePackType_shouldNotCheckCanEditPackType() {
    // Set up fixture 
    PackType packType = aPackType().withId(IRRELEVANT_PACK_TYPE_ID).build();
    Date irrelevantBleedStartTime = new DateTime().minusMinutes(30).toDate();
    Date irrelevantBleedEndTime = new DateTime().minusMinutes(5).toDate();

    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withPackType(packType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();
    DonationBackingForm donationBackingForm = aDonationBackingForm()
        .withPackType(packType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();
    
    // Set up expectations
    Donation expectedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withPackType(packType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();
    
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(packTypeRepository.getPackTypeById(IRRELEVANT_PACK_TYPE_ID)).thenReturn(packType);
    when(donationRepository.updateDonation(existingDonation)).thenAnswer(returnsFirstArg());
    
    // Exercise SUT
    Donation returnedDonation = donationCRUDService.updateDonation(IRRELEVANT_DONATION_ID, donationBackingForm);
    
    // Verify
    verify(donationConstraintChecker, never()).canEditPackType(existingDonation);
    assertThat(returnedDonation, hasSameStateAsDonation(expectedDonation));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testUpdateDonationWithDifferentPackTypeThatProducesTestSamplesWithNotOpenTestBatch_shouldThrow() {
    // Set up fixture 
    PackType newPackType = aPackType().withId(2L).withTestSampleProduced(true).build();
    Date irrelevantBleedStartTime = new DateTime().minusMinutes(30).toDate();
    Date irrelevantBleedEndTime = new DateTime().minusMinutes(5).toDate();
    TestBatch testBatch = TestBatchBuilder.aReleasedTestBatch().build();
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch().withTestBatch(testBatch).build();

    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withPackType(aPackType().withTestSampleProduced(false).withId(1L).build())
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withDonationBatch(donationBatch)
        .build();
    DonationBackingForm donationBackingForm = aDonationBackingForm()
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();
    
    // Set up expectations
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationConstraintChecker.canUpdateDonationFields(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canEditPackType(existingDonation)).thenReturn(true);
    when(packTypeRepository.getPackTypeById(2L)).thenReturn(newPackType);
    
    // Exercise SUT
    donationCRUDService.updateDonation(IRRELEVANT_DONATION_ID, donationBackingForm);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateDonationWithDifferentPackTypeAndDeferredDonor_shouldThrow() {
    // Set up fixture 
    Donor donor = aDonor().withId(IRRELEVANT_DONOR_ID).build();
    PackType newPackType = aPackType().withId(2L).build();
    Date irrelevantBleedStartTime = new DateTime().minusMinutes(30).toDate();
    Date irrelevantBleedEndTime = new DateTime().minusMinutes(5).toDate();

    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(aPackType().withId(1L).build())
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();
    DonationBackingForm donationBackingForm = aDonationBackingForm()
        .withDonor(donor)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();
    
    // Set up expectations
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationConstraintChecker.canUpdateDonationFields(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canEditPackType(existingDonation)).thenReturn(true);
    when(packTypeRepository.getPackTypeById(2L)).thenReturn(newPackType);
    when(donorConstraintChecker.isDonorDeferred(IRRELEVANT_DONOR_ID)).thenReturn(true);
    when(donationRepository.updateDonation(existingDonation)).thenAnswer(returnsFirstArg());
    
    // Exercise SUT
    donationCRUDService.updateDonation(IRRELEVANT_DONATION_ID, donationBackingForm);
  }

  @Test
  // Regression test for BSIS-1534
  public void testUpdateDonationWithSamePackType_shouldNotCheckDonorDeferralStatus() {
    // Set up fixture 
    Donor donor = aDonor().withId(IRRELEVANT_DONOR_ID).build();
    PackType packType = aPackType().withId(IRRELEVANT_PACK_TYPE_ID).build();
    Date irrelevantBleedStartTime = new DateTime().minusMinutes(30).toDate();
    Date irrelevantBleedEndTime = new DateTime().minusMinutes(5).toDate();

    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(packType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();
    DonationBackingForm donationBackingForm = aDonationBackingForm()
        .withDonor(donor)
        .withPackType(packType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();
    
    // Set up expectations
    Donation expectedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(packType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();
    
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationRepository.updateDonation(existingDonation)).thenAnswer(returnsFirstArg());
    
    // Exercise SUT
    Donation returnedDonation = donationCRUDService.updateDonation(IRRELEVANT_DONATION_ID, donationBackingForm);
    
    // Verify
    verify(donorConstraintChecker, never()).isDonorDeferred(IRRELEVANT_DONOR_ID);
    assertThat(returnedDonation, hasSameStateAsDonation(expectedDonation));
  }
  
  @Test
  public void testUpdateDonationWithDifferentPackTypeThatDoesntCountAsDonation_shouldDeleteComponentsAndOutcomesAndClearStatuses() {
    // Set up fixture 
    Donor donor = aDonor().withId(IRRELEVANT_DONOR_ID).build();
    PackType newPackType = aPackType().withId(IRRELEVANT_PACK_TYPE_ID).withCountAsDonation(false).build();
    Date irrelevantBleedStartTime = new DateTime().minusMinutes(30).toDate();
    Date irrelevantBleedEndTime = new DateTime().minusMinutes(5).toDate();

    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(aPackType().build())
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodAbo("A")
        .withBloodRh("+")
        .build();
    DonationBackingForm donationBackingForm = aDonationBackingForm()
        .withDonor(donor)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();
    
    Component initialComponent = ComponentBuilder.aComponent().withDonation(existingDonation).build();
    existingDonation.setComponents(Arrays.asList(initialComponent));

    List<BloodTestResult> bloodTestResultList = new ArrayList<>();
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(1L).withDonation(existingDonation).build());
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(2L).withDonation(existingDonation).build());

    // Set up expectations
    Donation expectedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withTTIStatus(TTIStatus.NOT_DONE)
        .withBloodAbo(null)
        .withBloodRh(null)
        .build();
    
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationConstraintChecker.canUpdateDonationFields(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canEditPackType(existingDonation)).thenReturn(true);
    when(packTypeRepository.getPackTypeById(IRRELEVANT_PACK_TYPE_ID)).thenReturn(newPackType);
    when(donorConstraintChecker.isDonorDeferred(IRRELEVANT_DONOR_ID)).thenReturn(false);
    when(bloodTestResultRepository.getTestOutcomes(existingDonation)).thenReturn(bloodTestResultList);
    when(donationRepository.updateDonation(existingDonation)).thenAnswer(returnsFirstArg());
    
    // Exercise SUT
    Donation returnedDonation = donationCRUDService.updateDonation(IRRELEVANT_DONATION_ID, donationBackingForm);

    // Verify
    verify(bloodTestsService).setTestOutcomesAsDeleted(existingDonation);
    assertThat(returnedDonation, hasSameStateAsDonation(expectedDonation));
    assertThat(returnedDonation.getComponents().get(0).getIsDeleted(), is(true));
  }
  
  @Test
  public void testUpdateDonationWithDifferentPackTypeThatCountsAsDonation_shouldNotDeleteComponentsAndOutcomesAndClearStatuses() {
    // Set up fixture 
    Donor donor = aDonor().withId(IRRELEVANT_DONOR_ID).build();
    PackType newPackType = aPackType().withId(IRRELEVANT_PACK_TYPE_ID).withCountAsDonation(true).build();
    Date irrelevantBleedStartTime = new DateTime().minusMinutes(30).toDate();
    Date irrelevantBleedEndTime = new DateTime().minusMinutes(5).toDate();

    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(aPackType().build())
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodAbo("A")
        .withBloodRh("+")
        .build();
    DonationBackingForm donationBackingForm = aDonationBackingForm()
        .withDonor(donor)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();
    
    Component initialComponent = ComponentBuilder.aComponent().withDonation(existingDonation).build();
    existingDonation.setComponents(Arrays.asList(initialComponent));

    List<BloodTestResult> bloodTestResultList = new ArrayList<>();
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(1L).withDonation(existingDonation).build());
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(2L).withDonation(existingDonation).build());

    // Set up expectations
    Donation expectedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodAbo("A")
        .withBloodRh("+")
        .build();
    
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationConstraintChecker.canUpdateDonationFields(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canEditPackType(existingDonation)).thenReturn(true);
    when(packTypeRepository.getPackTypeById(IRRELEVANT_PACK_TYPE_ID)).thenReturn(newPackType);
    when(donorConstraintChecker.isDonorDeferred(IRRELEVANT_DONOR_ID)).thenReturn(false);
    when(bloodTestResultRepository.getTestOutcomes(existingDonation)).thenReturn(bloodTestResultList);
    when(donationRepository.updateDonation(existingDonation)).thenAnswer(returnsFirstArg());
    
    // Exercise SUT
    Donation returnedDonation = donationCRUDService.updateDonation(IRRELEVANT_DONATION_ID, donationBackingForm);

    // Verify
    verify(bloodTestsService, never()).setTestOutcomesAsDeleted(existingDonation);
    assertThat(returnedDonation, hasSameStateAsDonation(expectedDonation));
    assertThat(returnedDonation.getComponents().get(0).getIsDeleted(), is(false));
  }
  
  @Test
  public void testUpdateDonationWithDifferentPackTypeThatCountsAsDonationWithNoInitComponent_shouldCreateInitComponent() {
    // Set up fixture 
    Donor donor = aDonor().withId(IRRELEVANT_DONOR_ID).build();
    PackType newPackType = aPackType().withId(IRRELEVANT_PACK_TYPE_ID).withCountAsDonation(true).build();
    Date irrelevantBleedStartTime = new DateTime().minusMinutes(30).toDate();
    Date irrelevantBleedEndTime = new DateTime().minusMinutes(5).toDate();

    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(aPackType().build())
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodAbo("A")
        .withBloodRh("+")
        .build();
    DonationBackingForm donationBackingForm = aDonationBackingForm()
        .withDonor(donor)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();

    List<BloodTestResult> bloodTestResultList = new ArrayList<>();
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(1L).withDonation(existingDonation).build());
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(2L).withDonation(existingDonation).build());

    // Set up expectations
    Donation expectedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodAbo("A")
        .withBloodRh("+")
        .build();
    
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationConstraintChecker.canUpdateDonationFields(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canEditPackType(existingDonation)).thenReturn(true);
    when(packTypeRepository.getPackTypeById(IRRELEVANT_PACK_TYPE_ID)).thenReturn(newPackType);
    when(donorConstraintChecker.isDonorDeferred(IRRELEVANT_DONOR_ID)).thenReturn(false);
    when(bloodTestResultRepository.getTestOutcomes(existingDonation)).thenReturn(bloodTestResultList);
    when(donationRepository.updateDonation(existingDonation)).thenAnswer(returnsFirstArg());
    
    // Exercise SUT
    Donation returnedDonation = donationCRUDService.updateDonation(IRRELEVANT_DONATION_ID, donationBackingForm);

    // Verify
    verify(donationRepository).createInitialComponent(existingDonation);
    assertThat(returnedDonation, hasSameStateAsDonation(expectedDonation));
  }

  @Test
  public void testUpdateDonation_shouldRetrieveAndUpdateDonation() {
    // Set up fixture
    Integer irrelevantDonorPulse = 80;
    BigDecimal irrelevantHaemoglobinCount = new BigDecimal(2);
    HaemoglobinLevel irrelevantHaemoglobinLevel = HaemoglobinLevel.FAIL;
    Integer irrelevantBloodPressureSystolic = 120;
    Integer irrelevantBloodPressureDiastolic = 80;
    BigDecimal irrelevantDonorWeight = new BigDecimal(65);
    String irrelevantNotes = "some notes";
    PackType irrelevantPackType = aPackType().withId(IRRELEVANT_PACK_TYPE_ID).withCountAsDonation(true).build();
    Date irrelevantBleedStartTime = new DateTime().minusMinutes(30).toDate();
    Date irrelevantBleedEndTime = new DateTime().minusMinutes(5).toDate();
    Long irrelevantAdverseEventTypeId = 8L;
    Long irrelevantAdverseEventId = 7L;
    AdverseEvent irrelevantAdverseEvent = anAdverseEvent()
        .withId(irrelevantAdverseEventId)
        .withType(anAdverseEventType().withId(irrelevantAdverseEventTypeId).build())
        .build();
    AdverseEventBackingForm irrelevantAdverseEventBackingForm = anAdverseEventBackingForm()
        .withType(anAdverseEventTypeBackingForm().withId(irrelevantAdverseEventTypeId).build())
        .withId(irrelevantAdverseEventId)
        .build();

    String donationBatchNumber = "000001";

    Donor expectedDonor = aDonor().withId(IRRELEVANT_DONOR_ID).build();

    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withPackType(aPackType().withId(8l).withCountAsDonation(true).build())
        .withDonor(expectedDonor)
        .withAdverseEvent(irrelevantAdverseEvent)
        .build();

    DonationBackingForm donationBackingForm = aDonationBackingForm()
        .withDonor(expectedDonor)
        .withDonorPulse(irrelevantDonorPulse)
        .withHaemoglobinCount(irrelevantHaemoglobinCount)
        .withHaemoglobinLevel(irrelevantHaemoglobinLevel)
        .withBloodPressureSystolic(irrelevantBloodPressureSystolic)
        .withBloodPressureDiastolic(irrelevantBloodPressureDiastolic)
        .withDonorWeight(irrelevantDonorWeight)
        .withNotes(irrelevantNotes)
        .withPackType(irrelevantPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withAdverseEvent(irrelevantAdverseEventBackingForm)
        .withDonationBatchNumber(donationBatchNumber)
        .build();

    // Set up expectations
    Donation expectedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(expectedDonor)
        .withDonorPulse(irrelevantDonorPulse)
        .withHaemoglobinCount(irrelevantHaemoglobinCount)
        .withHaemoglobinLevel(irrelevantHaemoglobinLevel)
        .withBloodPressureSystolic(irrelevantBloodPressureSystolic)
        .withBloodPressureDiastolic(irrelevantBloodPressureDiastolic)
        .withDonorWeight(irrelevantDonorWeight)
        .withNotes(irrelevantNotes)
        .withPackType(irrelevantPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withAdverseEvent(irrelevantAdverseEvent)
        .build();


    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationConstraintChecker.canUpdateDonationFields(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canEditPackType(existingDonation)).thenReturn(true);
    when(donationRepository.updateDonation(argThat(hasSameStateAsDonation(expectedDonation)))).thenReturn(expectedDonation);
    when(packTypeRepository.getPackTypeById(IRRELEVANT_PACK_TYPE_ID)).thenReturn(irrelevantPackType);
    when(donorConstraintChecker.isDonorDeferred(IRRELEVANT_DONOR_ID)).thenReturn(false);
    when(donorRepository.findDonorById(IRRELEVANT_DONOR_ID)).thenReturn(expectedDonor);


    // Exercise SUT
    Donation returnedDonation = donationCRUDService.updateDonation(IRRELEVANT_DONATION_ID, donationBackingForm);

    // Verify
    verify(donationRepository).updateDonation(argThat(hasSameStateAsDonation(expectedDonation)));
    verify(donorService).setDonorDueToDonate(argThat(hasSameStateAsDonor(expectedDonor)));
    assertThat(returnedDonation, is(expectedDonation));
  }

  @Test
  public void testCreateDonationWithDonationWithEligibleDonor_shouldAddDonation() {

    Donation donation = aDonation().build();
    long donorId = 993L;

    DonationBackingForm backingForm = aDonationBackingForm()
        .withDonation(donation)
        .withDonor(aDonor().withId(donorId).build())
        .withPackType(aPackType().withId(IRRELEVANT_PACK_TYPE_ID).build())
        .build();

    PackType packTypeThatCountsAsDonation = aPackType().withCountAsDonation(true).build();

    when(packTypeRepository.getPackTypeById(IRRELEVANT_PACK_TYPE_ID)).thenReturn(packTypeThatCountsAsDonation);
    when(donorConstraintChecker.isDonorEligibleToDonate(donorId)).thenReturn(true);

    Donation returnedDonation = donationCRUDService.createDonation(backingForm);

    verify(donationRepository).addDonation(donation);
    verify(componentCRUDService, never()).markComponentsBelongingToDonationAsUnsafe(donation);
    assertThat(returnedDonation, is(donation));
  }

  @Test
  public void testCreateDonationWithDonationWithPackTypeThatDoesNotCountAsDonation_shouldAddDonation() {

    Donation donation = aDonation().build();
    long donorId = 993L;

    DonationBackingForm backingForm = aDonationBackingForm()
        .withDonation(donation)
        .withDonor(aDonor().withId(donorId).build())
        .withPackType(aPackType().withId(IRRELEVANT_PACK_TYPE_ID).build())
        .build();

    PackType packTypeThatCountsAsDonation = aPackType().withCountAsDonation(false).build();

    when(packTypeRepository.getPackTypeById(IRRELEVANT_PACK_TYPE_ID)).thenReturn(packTypeThatCountsAsDonation);

    Donation returnedDonation = donationCRUDService.createDonation(backingForm);

    verify(donationRepository).addDonation(donation);
    verify(componentCRUDService, never()).markComponentsBelongingToDonationAsUnsafe(donation);
    assertThat(returnedDonation, is(donation));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateDonationWithDonationWithIneligibleDonorAndNotBackEntry_shouldThrow() {

    Donation donation = aDonation().build();
    long donorId = 993L;
    String donationBatchNumber = "000001";

    DonationBackingForm backingForm = aDonationBackingForm()
        .withDonation(donation)
        .withDonor(aDonor().withId(donorId).build())
        .withDonationBatchNumber(donationBatchNumber)
        .withPackType(aPackType().withId(IRRELEVANT_PACK_TYPE_ID).build())
        .build();

    DonationBatch donationBatch = aDonationBatch().build();

    PackType packTypeThatCountsAsDonation = aPackType().withCountAsDonation(true).build();

    when(packTypeRepository.getPackTypeById(IRRELEVANT_PACK_TYPE_ID)).thenReturn(packTypeThatCountsAsDonation);
    when(donorConstraintChecker.isDonorEligibleToDonate(donorId)).thenReturn(false);
    when(donationBatchRepository.findDonationBatchByBatchNumber(donationBatchNumber)).thenReturn(donationBatch);

    donationCRUDService.createDonation(backingForm);

    verify(donationRepository, never()).addDonation(donation);
    verify(componentCRUDService, never()).markComponentsBelongingToDonationAsUnsafe(donation);
  }

  @Test
  public void testCreateDonationWithDonationWithIneligibleDonorAndBackEntry_shouldAddDonationAndDiscardComponents() {

    Donation donation = aDonation().build();
    long donorId = 993L;
    String donationBatchNumber = "000001";

    DonationBackingForm backingForm = aDonationBackingForm()
        .withDonation(donation)
        .withDonor(aDonor().withId(donorId).build())
        .withDonationBatchNumber(donationBatchNumber)
        .withPackType(aPackType().withId(IRRELEVANT_PACK_TYPE_ID).build())
        .build();


    DonationBatch donationBatch = aDonationBatch().thatIsBackEntry().build();

    PackType packTypeThatCountsAsDonation = aPackType().withCountAsDonation(true).build();

    when(packTypeRepository.getPackTypeById(IRRELEVANT_PACK_TYPE_ID)).thenReturn(packTypeThatCountsAsDonation);
    when(donorConstraintChecker.isDonorEligibleToDonate(donorId)).thenReturn(false);
    when(donationBatchRepository.findDonationBatchByBatchNumber(donationBatchNumber)).thenReturn(donationBatch);

    Donation returnedDonation = donationCRUDService.createDonation(backingForm);

    verify(donationRepository).addDonation(donation);
    verify(componentCRUDService).markComponentsBelongingToDonationAsUnsafe(donation);
    verify(postDonationCounsellingCRUDService).createPostDonationCounsellingForDonation(donation);
    assertThat(returnedDonation, is(donation));
  }
  
  @Test
  public void testUpdateDonationsBloodTypingResolutions_withResolvedStatus() {
    BloodTypingResolutionBackingForm bloodTypingResolutionBackingForm = aBloodTypingResolutionBackingForm()
        .withDonationId(IRRELEVANT_DONATION_ID)
        .withStatus(BloodTypingMatchStatus.RESOLVED)
        .withBloodAbo("A")
        .withBloodRh("POS")
        .build();
    DonationBatch donationBatch = aDonationBatch().withTestBatch(aReleasedTestBatch().build()).build();
    Donation donation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.AMBIGUOUS)
        .withBloodAbo("B")
        .withBloodRh("NEG")
        .withDonationBatch(donationBatch)
        .build();
    Donation expectedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withBloodAbo("A")
        .withBloodRh("POS")
        .withDonationBatch(donationBatch)
        .build();
    
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(donation);
    when(donationRepository.updateDonation(donation)).thenReturn(donation);
    
    donationCRUDService.updateDonationsBloodTypingResolutions(aBloodTypingResolutionsBackingForm()
        .withBloodTypingResolution(bloodTypingResolutionBackingForm)
        .build());
    
    verify(donationRepository).updateDonation(argThat(hasSameStateAsDonation(expectedDonation)));
    verify(testBatchStatusChangeService).handleRelease(donation);
  }
  
  @Test
  public void testUpdateDonationsBloodTypingResolutions_withNoTypeDeterminedStatus() {
    BloodTypingResolutionBackingForm bloodTypingResolutionBackingForm = aBloodTypingResolutionBackingForm()
        .withDonationId(IRRELEVANT_DONATION_ID)
        .withStatus(BloodTypingMatchStatus.NO_TYPE_DETERMINED)
        .build();
    DonationBatch donationBatch = aDonationBatch().withTestBatch(aTestBatch().build()).build();
    Donation donation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.AMBIGUOUS)
        .withDonationBatch(donationBatch)
        .build();
    Donation expectedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NO_TYPE_DETERMINED)
        .withDonationBatch(donationBatch)
        .build();
    
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(donation);
    when(donationRepository.updateDonation(donation)).thenReturn(donation);
    
    donationCRUDService.updateDonationsBloodTypingResolutions(aBloodTypingResolutionsBackingForm()
        .withBloodTypingResolution(bloodTypingResolutionBackingForm)
        .build());
    
    verify(donationRepository).updateDonation(argThat(hasSameStateAsDonation(expectedDonation)));
  }

}
