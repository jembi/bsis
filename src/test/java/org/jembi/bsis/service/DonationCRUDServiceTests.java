package org.jembi.bsis.service;

import static org.jembi.bsis.helpers.matchers.DonationTestStatusesMatcher.testStatusesAreReset;

import org.jembi.bsis.backingform.BloodTypingResolutionBackingForm;
import org.jembi.bsis.helpers.builders.BloodTestResultBuilder;
import org.jembi.bsis.helpers.builders.ComponentBuilder;
import org.jembi.bsis.helpers.builders.DonationBatchBuilder;
import org.jembi.bsis.helpers.builders.DonationBuilder;
import org.jembi.bsis.model.adverseevent.AdverseEvent;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.HaemoglobinLevel;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.BloodTestResultRepository;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.repository.PackTypeRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.AdverseEventBuilder.anAdverseEvent;
import static org.jembi.bsis.helpers.builders.AdverseEventTypeBuilder.anAdverseEventType;
import static org.jembi.bsis.helpers.builders.BloodTypingResolutionBackingFormBuilder.aBloodTypingResolutionBackingForm;
import static org.jembi.bsis.helpers.builders.BloodTypingResolutionsBackingFormBuilder.aBloodTypingResolutionsBackingForm;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aReleasedTestBatch;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;
import static org.jembi.bsis.helpers.matchers.ComponentMatcher.hasSameStateAsComponent;
import static org.jembi.bsis.helpers.matchers.DonationMatcher.hasSameStateAsDonation;
import static org.jembi.bsis.helpers.matchers.DonationTestStatusesMatcher.testStatusesAreReset;
import static org.jembi.bsis.helpers.matchers.DonorMatcher.hasSameStateAsDonor;
import static org.jembi.bsis.model.testbatch.TestBatchStatus.CLOSED;
import static org.jembi.bsis.model.testbatch.TestBatchStatus.OPEN;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DonationCRUDServiceTests extends UnitTestSuite {

  private static final UUID IRRELEVANT_DONATION_ID = UUID.randomUUID();
  private static final UUID IRRELEVANT_COMPONENT_ID = UUID.randomUUID();
  private static final UUID IRRELEVANT_DONATION_BATCH_ID = UUID.randomUUID();
  private static final UUID IRRELEVANT_TEST_BATCH_ID = UUID.randomUUID();
  private static final UUID IRRELEVANT_PACK_TYPE_ID = UUID.randomUUID();
  private static final Date IRRELEVANT_DATE_OF_FIRST_DONATION = new DateTime().minusDays(7).toDate();
  private static final Date IRRELEVANT_DATE_OF_LAST_DONATION = new DateTime().minusDays(2).toDate();
  private static final Date IRRELEVANT_CURRENT_DATE = new DateTime().toDate();
  private static final UUID COMPONENT_ID_1 = UUID.randomUUID();
  private static final UUID COMPONENT_ID_2 = UUID.randomUUID();
  private static final UUID FIRST_BLOOD_TEST_RESULT_ID = UUID.randomUUID();
  private static final UUID SECOND_BLOOD_TEST_RESULT_ID = UUID.randomUUID();

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
  @Mock
  private ComponentRepository componentRepository;
  @Mock
  private CheckCharacterService checkCharacterService;
  @Mock
  private DateGeneratorService dateGeneratorService;
  @Mock
  private TestBatchConstraintChecker testBatchConstraintChecker;

  @Test(expected = IllegalStateException.class)
  public void testDeleteDonationWithConstraints_shouldThrow() {

    when(donationConstraintChecker.canDeleteDonation(IRRELEVANT_DONATION_ID)).thenReturn(false);

    donationCRUDService.deleteDonation(IRRELEVANT_DONATION_ID);
  }

  @Test
  public void testDeleteDonationWithFirstDonation_shouldSoftDeleteDonationAndUpdateDonorFirstDonationDate() {

    // Set up fixture
    UUID irrelevantDonorId = UUID.randomUUID();
    PackType packType = aPackType().withId(IRRELEVANT_PACK_TYPE_ID).build();
    Donor existingDonor = aDonor()
        .withId(irrelevantDonorId)
        .withDateOfFirstDonation(IRRELEVANT_DATE_OF_FIRST_DONATION)
        .withDateOfLastDonation(IRRELEVANT_DATE_OF_LAST_DONATION)
        .build();
    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(existingDonor)
        .withDonationDate(IRRELEVANT_DATE_OF_FIRST_DONATION)
        .withPackType(packType)
        .build();

    // Set up expectations
    Donation expectedDonation = aDonation()
        .thatIsDeleted()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(existingDonor)
        .withDonationDate(IRRELEVANT_DATE_OF_FIRST_DONATION)
        .withPackType(packType)
        .build();
    Date expectedDateOfFirstDonation = new Date();
    Donor expectedDonor = aDonor()
        .withId(irrelevantDonorId)
        .withDateOfFirstDonation(expectedDateOfFirstDonation)
        .withDateOfLastDonation(IRRELEVANT_DATE_OF_LAST_DONATION)
        .build();

    when(donationConstraintChecker.canDeleteDonation(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationRepository.findDateOfFirstDonationForDonor(irrelevantDonorId)).thenReturn(expectedDateOfFirstDonation);

    // Exercise SUT
    donationCRUDService.deleteDonation(IRRELEVANT_DONATION_ID);

    // Verify
    verify(donationRepository).update(argThat(hasSameStateAsDonation(expectedDonation)));
    verify(donorRepository).updateDonor(argThat(hasSameStateAsDonor(expectedDonor)));
    verify(donorService).setDonorDueToDonate(argThat(hasSameStateAsDonor(expectedDonor)));
  }

  @Test
  public void testDeleteDonationWithComponents_shouldAlsoDeleteRelatedComponents() {
    // Set up fixture
    UUID irrelevantDonorId = UUID.randomUUID();
    PackType packType = aPackType().withId(IRRELEVANT_PACK_TYPE_ID).build();
    Donor existingDonor = aDonor()
        .withId(irrelevantDonorId)
        .withDateOfFirstDonation(IRRELEVANT_DATE_OF_FIRST_DONATION)
        .withDateOfLastDonation(IRRELEVANT_DATE_OF_LAST_DONATION)
        .build();
    Component existingComponent1 = aComponent().withId(COMPONENT_ID_1).build();
    Component existingComponent2 = aComponent().withId(COMPONENT_ID_2).build();
    Component deletedComponent1 = aComponent().withId(COMPONENT_ID_1).thatIsDeleted().build();
    Component deletedComponent2 = aComponent().withId(COMPONENT_ID_2).thatIsDeleted().build();
    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(existingDonor)
        .withDonationDate(IRRELEVANT_CURRENT_DATE)
        .withPackType(packType)
        .withComponents(Arrays.asList(existingComponent1, existingComponent2))
        .build();

    // Set up expectations
    Donation expectedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .thatIsDeleted()
        .withDonor(existingDonor)
        .withDonationDate(IRRELEVANT_CURRENT_DATE)
        .withPackType(packType)
        .withComponents(Arrays.asList(existingComponent1, existingComponent2))
        .build();

    when(donationConstraintChecker.canDeleteDonation(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(componentCRUDService.deleteComponent(COMPONENT_ID_1)).thenReturn(deletedComponent1);
    when(componentCRUDService.deleteComponent(COMPONENT_ID_2)).thenReturn(deletedComponent2);

    // Exercise SUT
    donationCRUDService.deleteDonation(IRRELEVANT_DONATION_ID);

    // Verify
    verify(donationRepository).update(argThat(hasSameStateAsDonation(expectedDonation)));
    verify(componentCRUDService).deleteComponent(COMPONENT_ID_1);
    verify(componentCRUDService).deleteComponent(COMPONENT_ID_2);
  }

  @Test
  public void testDeleteDonationWithLastDonation_shouldSoftDeleteDonationAndUpdateDonorLastDonationDate() {

    // Set up fixture
    UUID irrelevantDonorId = UUID.randomUUID();
    PackType packType = aPackType().withId(IRRELEVANT_PACK_TYPE_ID).build();
    Donor existingDonor = aDonor()
        .withId(irrelevantDonorId)
        .withDateOfFirstDonation(IRRELEVANT_DATE_OF_FIRST_DONATION)
        .withDateOfLastDonation(IRRELEVANT_DATE_OF_LAST_DONATION)
        .build();
    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(existingDonor)
        .withDonationDate(IRRELEVANT_DATE_OF_LAST_DONATION)
        .withPackType(packType)
        .build();

    // Set up expectations
    Donation expectedDonation = aDonation()
        .thatIsDeleted()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(existingDonor)
        .withDonationDate(IRRELEVANT_DATE_OF_LAST_DONATION)
        .withPackType(packType)
        .build();
    Date expectedDateOfLastDonation = new Date();
    Donor expectedDonor = aDonor()
        .withId(irrelevantDonorId)
        .withDateOfFirstDonation(IRRELEVANT_DATE_OF_FIRST_DONATION)
        .withDateOfLastDonation(expectedDateOfLastDonation)
        .build();

    when(donationConstraintChecker.canDeleteDonation(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationRepository.findDateOfLastDonationForDonor(irrelevantDonorId)).thenReturn(expectedDateOfLastDonation);

    // Exercise SUT
    donationCRUDService.deleteDonation(IRRELEVANT_DONATION_ID);

    // Verify
    verify(donationRepository).update(argThat(hasSameStateAsDonation(expectedDonation)));
    verify(donorRepository).updateDonor(argThat(hasSameStateAsDonor(expectedDonor)));
    verify(donorService).setDonorDueToDonate(argThat(hasSameStateAsDonor(expectedDonor)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateDonationWithUpdatedBleedStartTimeAndCannotUpdate_shouldThrow() {

    // Set up fixture
    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withBleedEndTime(IRRELEVANT_DATE_OF_FIRST_DONATION)
        .withBleedStartTime(IRRELEVANT_DATE_OF_FIRST_DONATION)
        .build();
    Donation updatedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withBleedStartTime(new Date())
        .withBleedEndTime(IRRELEVANT_DATE_OF_FIRST_DONATION)
        .build();

    // Set up expectations
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationConstraintChecker.canEditBleedTimes(IRRELEVANT_DONATION_ID)).thenReturn(false);

    // Exercise SUT
    donationCRUDService.updateDonation(updatedDonation);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateDonationWithUpdatedBleedEndTimeAndCannotUpdate_shouldThrow() {

    // Set up fixture
    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withBleedEndTime(IRRELEVANT_DATE_OF_FIRST_DONATION)
        .withBleedStartTime(IRRELEVANT_DATE_OF_FIRST_DONATION)
        .build();
    Donation updatedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withBleedEndTime(new Date())
        .withBleedStartTime(IRRELEVANT_DATE_OF_FIRST_DONATION)
        .build();

    // Set up expectations
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationConstraintChecker.canEditBleedTimes(IRRELEVANT_DONATION_ID)).thenReturn(false);

    // Exercise SUT
    donationCRUDService.updateDonation(updatedDonation);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateDonationWithDifferentPackTypeThatCantEditPackType_shouldThrow() {
    // Set up fixture 
    UUID newPackTypeId = UUID.randomUUID();
    UUID oldPackTypeId = UUID.randomUUID();
    PackType newPackType = aPackType().withId(newPackTypeId).build();
    PackType oldPackType = aPackType().withId(oldPackTypeId).build();
    Date irrelevantBleedStartTime = new DateTime().minusMinutes(30).toDate();
    Date irrelevantBleedEndTime = new DateTime().minusMinutes(5).toDate();

    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withPackType(oldPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();
    Donation updatedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();

    // Set up expectations
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationConstraintChecker.canEditBleedTimes(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canEditPackType(existingDonation)).thenReturn(false);

    // Exercise SUT
    donationCRUDService.updateDonation(updatedDonation);
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
    Donation updatedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
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
    when(donationRepository.update(existingDonation)).thenAnswer(returnsFirstArg());

    // Exercise SUT
    Donation returnedDonation = donationCRUDService.updateDonation(updatedDonation);

    // Verify
    verify(donationConstraintChecker, never()).canEditPackType(existingDonation);
    assertThat(returnedDonation, hasSameStateAsDonation(expectedDonation));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateDonationWithDifferentPackTypeThatCantEditToNewPackType_shouldThrow() {
    // Set up fixture 
    UUID newPackTypeId = UUID.randomUUID();
    UUID oldPackTypeId = UUID.randomUUID();
    PackType newPackType = aPackType().withId(newPackTypeId).build();
    PackType oldPackType = aPackType().withId(oldPackTypeId).build();
    Date irrelevantBleedStartTime = new DateTime().minusMinutes(30).toDate();
    Date irrelevantBleedEndTime = new DateTime().minusMinutes(5).toDate();

    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withPackType(oldPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();
    Donation updatedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();

    // Set up expectations
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationConstraintChecker.canEditBleedTimes(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canEditPackType(existingDonation)).thenReturn(true);
    when(donationConstraintChecker.canEditToNewPackType(existingDonation, newPackType)).thenReturn(false);

    // Exercise SUT
    donationCRUDService.updateDonation(updatedDonation);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateDonationWithDifferentPackTypeAndDeferredDonor_shouldThrow() {
    // Set up fixture 
    UUID irrelevantDonorId = UUID.randomUUID();
    Donor donor = aDonor().withId(irrelevantDonorId).build();
    UUID newPackTypeId = UUID.randomUUID();
    UUID oldPackTypeId = UUID.randomUUID();
    PackType newPackType = aPackType().withId(newPackTypeId).build();
    PackType oldPackType = aPackType().withId(oldPackTypeId).build();
    Date irrelevantBleedStartTime = new DateTime().minusMinutes(30).toDate();
    Date irrelevantBleedEndTime = new DateTime().minusMinutes(5).toDate();

    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(oldPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();
    Donation updatedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();

    // Set up expectations
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationConstraintChecker.canEditBleedTimes(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canEditPackType(existingDonation)).thenReturn(true);
    when(donorConstraintChecker.isDonorDeferred(irrelevantDonorId)).thenReturn(true);
    when(donationRepository.update(existingDonation)).thenAnswer(returnsFirstArg());

    // Exercise SUT
    donationCRUDService.updateDonation(updatedDonation);
  }

  @Test
  // Regression test for BSIS-1534
  public void testUpdateDonationWithSamePackType_shouldNotCheckDonorDeferralStatus() {
    // Set up fixture 
    UUID irrelevantDonorId = UUID.randomUUID();
    Donor donor = aDonor().withId(irrelevantDonorId).build();
    PackType packType = aPackType().withId(IRRELEVANT_PACK_TYPE_ID).build();
    Date irrelevantBleedStartTime = new DateTime().minusMinutes(30).toDate();
    Date irrelevantBleedEndTime = new DateTime().minusMinutes(5).toDate();

    Donation donation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(packType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();

    // Set up expectations

    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(donation);
    when(donationRepository.update(donation)).thenAnswer(returnsFirstArg());

    // Exercise SUT
    Donation returnedDonation = donationCRUDService.updateDonation(donation);

    // Verify
    verify(donorConstraintChecker, never()).isDonorDeferred(irrelevantDonorId);
    assertThat(returnedDonation, hasSameStateAsDonation(donation));
  }

  @Test
  public void testUpdateDonationWithDifferentPackTypeThatDoesntProduceTestSamples_shouldDeleteExistingTestOutcomes() {
    // Set up fixture 
    UUID irrelevantDonorId = UUID.randomUUID();
    Donor donor = aDonor().withId(irrelevantDonorId).build();
    UUID newPackTypeId = UUID.randomUUID();
    UUID oldPackTypeId = UUID.randomUUID();
    PackType newPackType = aPackType().withId(newPackTypeId).withTestSampleProduced(false).withCountAsDonation(false).build();
    PackType oldPackType = aPackType().withId(oldPackTypeId).withTestSampleProduced(false).withCountAsDonation(false).build();
    Date irrelevantBleedStartTime = new DateTime().minusMinutes(30).toDate();
    Date irrelevantBleedEndTime = new DateTime().minusMinutes(5).toDate();

    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(oldPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withTTIStatus(TTIStatus.SAFE)
        .withBloodAbo("A")
        .withBloodRh("+")
        .build();
    Donation updatedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();

    List<BloodTestResult> bloodTestResultList = new ArrayList<>();
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(FIRST_BLOOD_TEST_RESULT_ID).withDonation(existingDonation).build());
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(SECOND_BLOOD_TEST_RESULT_ID).withDonation(existingDonation).build());

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
    when(donationConstraintChecker.canEditBleedTimes(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canEditPackType(existingDonation)).thenReturn(true);
    when(donationConstraintChecker.canEditToNewPackType(existingDonation, newPackType)).thenReturn(true);
    when(donorConstraintChecker.isDonorDeferred(irrelevantDonorId)).thenReturn(false);
    when(donationRepository.update(argThat(hasSameStateAsDonation(expectedDonation)))).thenAnswer(returnsFirstArg());
    when(bloodTestResultRepository.getTestOutcomes(existingDonation)).thenReturn(bloodTestResultList);

    // Exercise SUT
    Donation returnedDonation = donationCRUDService.updateDonation(updatedDonation);

    // Verify
    verify(bloodTestsService).setTestOutcomesAsDeleted(existingDonation);
    assertThat(returnedDonation, hasSameStateAsDonation(expectedDonation));
  }

  @Test
  public void testUpdateDonation_shouldRetrieveAndUpdateDonation() {
    // Set up fixture
    UUID irrelevantDonorId = UUID.randomUUID();
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
    UUID irrelevantAdverseEventTypeId = UUID.randomUUID();
    UUID irrelevantAdverseEventId = UUID.randomUUID();
    AdverseEvent irrelevantAdverseEvent = anAdverseEvent()
        .withId(irrelevantAdverseEventId)
        .withType(anAdverseEventType().withId(irrelevantAdverseEventTypeId).build())
        .build();

    Donor expectedDonor = aDonor().withId(irrelevantDonorId).build();

    Component component = ComponentBuilder.aComponent().build();
    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withPackType(irrelevantPackType)
        .withDonor(expectedDonor)
        .withAdverseEvent(irrelevantAdverseEvent)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();

    Donation updatedDonation = aDonation()
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
    when(donationConstraintChecker.canEditBleedTimes(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canEditPackType(existingDonation)).thenReturn(true);
    when(donationRepository.update(argThat(hasSameStateAsDonation(expectedDonation)))).thenReturn(expectedDonation);
    when(donorConstraintChecker.isDonorDeferred(irrelevantDonorId)).thenReturn(false);
    when(donorRepository.findDonorById(irrelevantDonorId)).thenReturn(expectedDonor);
    when(componentCRUDService.createInitialComponent(existingDonation)).thenReturn(component);

    // Exercise SUT
    Donation returnedDonation = donationCRUDService.updateDonation(updatedDonation);

    // Verify
    verify(donationRepository).update(argThat(hasSameStateAsDonation(expectedDonation)));
    assertThat(returnedDonation, is(expectedDonation));
  }

  @Test
  public void testCreateDonationWithDonationWithEligibleDonor_shouldAddDonation() {

    UUID donorId = UUID.randomUUID();
    PackType packTypeThatCountsAsDonation = aPackType().withId(IRRELEVANT_PACK_TYPE_ID).withCountAsDonation(true).build();
    Donation donation = aDonation()
        .withDonationDate(new Date())
        .withDonor(aDonor().withId(donorId).build())
        .withDonationIdentificationNumber("3000505")
        .withPackType(packTypeThatCountsAsDonation)
        .build();

    when(donorConstraintChecker.isDonorEligibleToDonate(donorId)).thenReturn(true);
    when(checkCharacterService.calculateFlagCharacters(donation.getDonationIdentificationNumber())).thenReturn("11");

    Donation returnedDonation = donationCRUDService.createDonation(donation);

    verify(donationRepository).save(donation);
    verify(componentCRUDService, never()).markComponentsBelongingToDonationAsUnsafe(donation);
    assertThat(returnedDonation, is(donation));
  }

  @Test
  public void testCreateDonationWithDonationWithPackTypeThatDoesNotCountAsDonation_shouldAddDonation() {

    UUID donorId = UUID.randomUUID();
    PackType packTypeThatDoesNotCountAsDonation = aPackType().withCountAsDonation(false).build();

    Donation donation = aDonation()
        .withDonationDate(new Date())
        .withDonor(aDonor().withId(donorId).build())
        .withPackType(packTypeThatDoesNotCountAsDonation)
        .withDonationIdentificationNumber("3000505")
        .build();

    when(checkCharacterService.calculateFlagCharacters(donation.getDonationIdentificationNumber())).thenReturn("11");

    Donation returnedDonation = donationCRUDService.createDonation(donation);

    verify(donationRepository).save(donation);
    verify(componentCRUDService, never()).markComponentsBelongingToDonationAsUnsafe(donation);
    assertThat(returnedDonation, is(donation));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateDonationWithDonationWithIneligibleDonorAndNotBackEntry_shouldThrow() {

    UUID donorId = UUID.randomUUID();
    String donationBatchNumber = "000001";
    DonationBatch donationBatch = aDonationBatch().withBatchNumber(donationBatchNumber).build();

    Donation donation = aDonation()
        .withDonor(aDonor().withId(donorId).build())
        .withDonationBatch(donationBatch)
        .withDonationIdentificationNumber("3000505")
        .withPackType(aPackType().withId(IRRELEVANT_PACK_TYPE_ID).build())
        .withDonationBatch(donationBatch)
        .build();

    when(donorConstraintChecker.isDonorEligibleToDonate(donorId)).thenReturn(false);
    when(donationBatchRepository.findDonationBatchByBatchNumber(donationBatchNumber)).thenReturn(donationBatch);

    donationCRUDService.createDonation(donation);

    verify(donationRepository, never()).save(donation);
    verify(componentCRUDService, never()).markComponentsBelongingToDonationAsUnsafe(donation);
  }

  @Test
  public void testCreateDonationWithDonationWithIneligibleDonorAndBackEntry_shouldAddDonationAndDiscardComponents() {

    UUID donorId = UUID.randomUUID();
    String donationBatchNumber = "000001";
    DonationBatch donationBatch = aDonationBatch().withBatchNumber(donationBatchNumber).thatIsBackEntry().build();
    PackType packTypeThatCountsAsDonation = aPackType().withCountAsDonation(true).build();

    Donation donation = aDonation()
        .withDonationDate(new Date())
        .withDonor(aDonor().withId(donorId).build())
        .withDonationBatch(donationBatch)
        .withDonationIdentificationNumber("3000505")
        .withPackType(packTypeThatCountsAsDonation)
        .build();

    when(donorConstraintChecker.isDonorEligibleToDonate(donorId)).thenReturn(false);
    when(donationBatchRepository.findDonationBatchByBatchNumber(donationBatchNumber)).thenReturn(donationBatch);
    when(checkCharacterService.calculateFlagCharacters(donation.getDonationIdentificationNumber())).thenReturn("11");

    Donation returnedDonation = donationCRUDService.createDonation(donation);

    verify(donationRepository).save(donation);
    verify(componentCRUDService).markComponentsBelongingToDonationAsUnsafe(donation);
    verify(postDonationCounsellingCRUDService).createPostDonationCounsellingForDonation(donation);
    assertThat(returnedDonation, is(donation));
  }

  @Test
  public void testUpdateDonationsBloodTypingResolutions_withResolvedStatus() {
    PackType packType = aPackType().withId(IRRELEVANT_PACK_TYPE_ID).build();
    BloodTypingResolutionBackingForm bloodTypingResolutionBackingForm = aBloodTypingResolutionBackingForm()
        .withDonationId(IRRELEVANT_DONATION_ID)
        .withStatus(BloodTypingMatchStatus.RESOLVED)
        .withBloodAbo("A")
        .withBloodRh("POS")
        .build();
    TestBatch testBatch = aReleasedTestBatch().build();
    DonationBatch donationBatch = aDonationBatch().build();
    Donation donation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.AMBIGUOUS)
        .withBloodAbo("B")
        .withBloodRh("NEG")
        .withDonationBatch(donationBatch)
        .withTestBatch(testBatch)
        .withPackType(packType)
        .build();
    Donation expectedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withBloodAbo("A")
        .withBloodRh("POS")
        .withDonationBatch(donationBatch)
        .withTestBatch(testBatch)
        .withPackType(packType)
        .build();

    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(donation);
    when(donationRepository.update(donation)).thenReturn(donation);

    donationCRUDService.updateDonationsBloodTypingResolutions(aBloodTypingResolutionsBackingForm()
        .withBloodTypingResolution(bloodTypingResolutionBackingForm)
        .build());

    verify(donationRepository).update(argThat(hasSameStateAsDonation(expectedDonation)));
    verify(testBatchStatusChangeService).handleRelease(donation);
  }

  @Test
  public void testUpdateDonationsBloodTypingResolutions_withNoTypeDeterminedStatus() {
    BloodTypingResolutionBackingForm bloodTypingResolutionBackingForm = aBloodTypingResolutionBackingForm()
        .withDonationId(IRRELEVANT_DONATION_ID)
        .withStatus(BloodTypingMatchStatus.NO_TYPE_DETERMINED)
        .build();
    DonationBatch donationBatch = aDonationBatch().withId(IRRELEVANT_DONATION_BATCH_ID).build();
    TestBatch testBatch = aTestBatch().withId(IRRELEVANT_TEST_BATCH_ID).build();
    PackType packType = aPackType().withId(IRRELEVANT_PACK_TYPE_ID).build();
    Donation donation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.AMBIGUOUS)
        .withDonationBatch(donationBatch)
        .withTestBatch(testBatch)
        .withPackType(packType)
        .build();
    Donation expectedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NO_TYPE_DETERMINED)
        .withDonationBatch(donationBatch)
        .withTestBatch(testBatch)
        .withPackType(packType)
        .build();

    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(donation);
    when(donationRepository.update(donation)).thenReturn(donation);

    donationCRUDService.updateDonationsBloodTypingResolutions(aBloodTypingResolutionsBackingForm()
        .withBloodTypingResolution(bloodTypingResolutionBackingForm)
        .build());

    verify(donationRepository).update(argThat(hasSameStateAsDonation(expectedDonation)));
  }

  @Test
  public void testCreateDonationWithPackTypeThatProducesComponents_shouldCreateNewInitialComponent() {
    // Set up fixture 
    UUID irrelevantDonorId = UUID.randomUUID();
    Donor donor = aDonor().withId(irrelevantDonorId).build();
    PackType packType = aPackType().withCountAsDonation(true).build();
    String donationBatchNumber = "000001";
    Donation donation = aDonation()
        .withDonationDate(new Date())
        .withDonor(donor)
        .withPackType(packType)
        .withDonationIdentificationNumber("3000505")
        .withDonationBatch(DonationBatchBuilder.aDonationBatch().withBatchNumber(donationBatchNumber).build())
        .build();

    // Set up expectations
    when(donationBatchRepository.findDonationBatchByBatchNumber(donationBatchNumber)).thenReturn(donation.getDonationBatch());
    when(donorConstraintChecker.isDonorEligibleToDonate(irrelevantDonorId)).thenReturn(true);
    when(componentCRUDService.createInitialComponent(donation)).thenReturn(ComponentBuilder.aComponent().build());
    when(checkCharacterService.calculateFlagCharacters(donation.getDonationIdentificationNumber())).thenReturn("11");


    // Exercise SUT
    donationCRUDService.createDonation(donation);

    // Verify
    verify(componentCRUDService).createInitialComponent(donation);
    assertThat("initial components were created", !donation.getComponents().isEmpty());
  }

  @Test
  public void testCreateDonationWithPackTypeThatDoesntProduceComponents_shouldNotAddComponent() {
    // Set up fixture 
    UUID irrelevantDonorId = UUID.randomUUID();
    Donor donor = aDonor().withId(irrelevantDonorId).build();
    PackType packType = aPackType().withCountAsDonation(false).build();
    String donationBatchNumber = "000001";
    Donation donation = aDonation()
        .withDonationDate(new Date())
        .withDonor(donor)
        .withPackType(packType)
        .withDonationIdentificationNumber("3000505")
        .withDonationBatch(DonationBatchBuilder.aDonationBatch().withBatchNumber(donationBatchNumber).build())
        .build();

    // Exercise SUT
    when(donorConstraintChecker.isDonorEligibleToDonate(irrelevantDonorId)).thenReturn(true);
    when(donationBatchRepository.findDonationBatchByBatchNumber(donationBatchNumber)).thenReturn(donation.getDonationBatch());
    when(componentCRUDService.createInitialComponent(donation)).thenReturn(null);
    when(checkCharacterService.calculateFlagCharacters(donation.getDonationIdentificationNumber())).thenReturn("11");

    // Test
    donationCRUDService.createDonation(donation);

    // Verify
    assertThat("No initial components were created", donation.getComponents().isEmpty());
  }

  @Test
  public void testUpdateDonationWithNewPackTypeThatDoesntCountAsDonation_shouldDeleteInitialComponent() {
    // Set up fixture
    UUID irrelevantDonorId = UUID.randomUUID();
    Donor donor = aDonor().withId(irrelevantDonorId).build();
    Date irrelevantBleedStartTime = new DateTime().minusMinutes(30).toDate();
    Date irrelevantBleedEndTime = new DateTime().minusMinutes(5).toDate();

    UUID oldPackTypeId = UUID.randomUUID();
    PackType oldPackType = aPackType()
        .withId(oldPackTypeId)
        .withTestSampleProduced(true)
        .withCountAsDonation(true)
        .build();

    UUID newPackTypeId = UUID.randomUUID();
    PackType newPackType = aPackType()
        .withId(newPackTypeId)
        .withTestSampleProduced(true)
        .withCountAsDonation(false)
        .build();

    Component initialComponent = ComponentBuilder.aComponent().build();
    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(oldPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withComponent(initialComponent)
        .thatIsNotReleased()
        .build();

    Donation updatedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .thatIsNotReleased()
        .build();

    Donation expectedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .thatIsNotReleased()
        .withComponent(initialComponent)
        .build();

    // Exercise SUT
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationConstraintChecker.canEditBleedTimes(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canEditPackType(existingDonation)).thenReturn(true);
    when(donationConstraintChecker.canEditToNewPackType(existingDonation, newPackType)).thenReturn(true);
    when(donationRepository.update(argThat(hasSameStateAsDonation(expectedDonation)))).thenReturn(expectedDonation);

    // Test
    Donation returnedDonation = donationCRUDService.updateDonation(updatedDonation);

    // assertion
    verify(donationRepository).update(argThat(hasSameStateAsDonation(expectedDonation)));
    assertThat(returnedDonation.getComponents().get(0).getIsDeleted(), is(true));
    assertThat(returnedDonation, hasSameStateAsDonation(expectedDonation));
  }

  @Test
  public void testUpdateDonationWithNewPackTypeThatCountsAsDonationAndNoInitialComponent_shouldCreateInitialComponent() {
    // Set up fixture
    Date irrelevantBleedStartTime = new DateTime().minusMinutes(30).toDate();
    Date irrelevantBleedEndTime = new DateTime().minusMinutes(5).toDate();
    UUID irrelevantDonorId = UUID.randomUUID();
    Donor donor = aDonor().withId(irrelevantDonorId).build();
    Component initialComponent = ComponentBuilder.aComponent().build();

    UUID oldPackTypeId = UUID.randomUUID();
    PackType oldPackType = aPackType()
        .withId(oldPackTypeId)
        .withTestSampleProduced(true)
        .withCountAsDonation(false)
        .build();

    UUID newPackTypeId = UUID.randomUUID();
    PackType newPackType = aPackType()
        .withId(newPackTypeId)
        .withTestSampleProduced(true)
        .withCountAsDonation(true)
        .build();

    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(oldPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();

    Donation updatedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .build();

    Donation expectedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withComponents(Arrays.asList(initialComponent))
        .build();

    // Exercise SUT
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationConstraintChecker.canEditBleedTimes(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canEditPackType(existingDonation)).thenReturn(true);
    when(donationConstraintChecker.canEditToNewPackType(existingDonation, newPackType)).thenReturn(true);
    when(donorConstraintChecker.isDonorDeferred(irrelevantDonorId)).thenReturn(false);
    when(componentCRUDService.createInitialComponent(updatedDonation)).thenReturn(initialComponent);
    when(donationRepository.update(argThat(hasSameStateAsDonation(expectedDonation))))
        .thenAnswer(returnsFirstArg());

    // Test
    Donation returnedDonation = donationCRUDService.updateDonation(updatedDonation);

    //verify
    verify(componentCRUDService).createInitialComponent(existingDonation);

    // assertions
    assertThat(existingDonation.getComponents().size(), is(1));
    assertThat(returnedDonation, hasSameStateAsDonation(expectedDonation));
  }

  @Test
  public void testUpdateDonationInAReleasedTestBatchWithNewPackTypeThatCountsAsDonationAndNoInitialComponent_shouldPerformRelease() {
    // Set up fixture
    Date irrelevantBleedStartTime = new DateTime().minusMinutes(30).toDate();
    Date irrelevantBleedEndTime = new DateTime().minusMinutes(5).toDate();
    UUID irrelevantDonorId = UUID.randomUUID();
    Donor donor = aDonor().withId(irrelevantDonorId).build();
    Component initialComponent = ComponentBuilder.aComponent().build();
    DonationBatch donationBatch = aDonationBatch().withId(IRRELEVANT_DONATION_BATCH_ID).build();
    TestBatch testBatch = aTestBatch().withId(IRRELEVANT_TEST_BATCH_ID).withStatus(TestBatchStatus.RELEASED).build();

    UUID oldPackTypeId = UUID.randomUUID();
    PackType oldPackType = aPackType()
        .withId(oldPackTypeId)
        .withTestSampleProduced(true)
        .withCountAsDonation(false)
        .build();

    UUID newPackTypeId = UUID.randomUUID();
    PackType newPackType = aPackType()
        .withId(newPackTypeId)
        .withTestSampleProduced(true)
        .withCountAsDonation(true)
        .build();

    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(oldPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withDonationBatch(donationBatch)
        .withTestBatch(testBatch)
        .build();

    Donation updatedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withDonationBatch(donationBatch)
        .withTestBatch(testBatch)
        .build();

    Donation expectedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withComponents(Arrays.asList(initialComponent))
        .withDonationBatch(donationBatch)
        .withTestBatch(testBatch)
        .build();

    // Exercise SUT
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationConstraintChecker.canEditBleedTimes(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canEditPackType(existingDonation)).thenReturn(true);
    when(donationConstraintChecker.canEditToNewPackType(existingDonation, newPackType)).thenReturn(true);
    when(donorConstraintChecker.isDonorDeferred(irrelevantDonorId)).thenReturn(false);
    when(componentCRUDService.createInitialComponent(updatedDonation)).thenReturn(initialComponent);
    when(donationRepository.update(argThat(hasSameStateAsDonation(expectedDonation)))).thenAnswer(returnsFirstArg());

    // Test
    donationCRUDService.updateDonation(updatedDonation);

    //verify
    verify(donationRepository).update(argThat(hasSameStateAsDonation(expectedDonation)));
    verify(testBatchStatusChangeService).handleRelease(argThat(hasSameStateAsDonation(expectedDonation)));
  }

  @Test
  public void testUpdateDonationInAClosedTestBatchWithNewPackTypeThatCountsAsDonationAndNoInitialComponent_shouldPerformRelease() {
    // Set up fixture
    Date irrelevantBleedStartTime = new DateTime().minusMinutes(30).toDate();
    Date irrelevantBleedEndTime = new DateTime().minusMinutes(5).toDate();
    UUID irrelevantDonorId = UUID.randomUUID();
    Donor donor = aDonor().withId(irrelevantDonorId).build();
    Component initialComponent = ComponentBuilder.aComponent().build();
    DonationBatch donationBatch = aDonationBatch().withId(IRRELEVANT_DONATION_BATCH_ID).build();
    TestBatch testBatch = aTestBatch().withId(IRRELEVANT_TEST_BATCH_ID).withStatus(CLOSED).build();

    UUID oldPackTypeId = UUID.randomUUID();
    PackType oldPackType = aPackType()
        .withId(oldPackTypeId)
        .withTestSampleProduced(true)
        .withCountAsDonation(false)
        .build();

    UUID newPackTypeId = UUID.randomUUID();
    PackType newPackType = aPackType()
        .withId(newPackTypeId)
        .withTestSampleProduced(true)
        .withCountAsDonation(true)
        .build();

    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(oldPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withDonationBatch(donationBatch)
        .withTestBatch(testBatch)
        .build();

    Donation updatedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withDonationBatch(donationBatch)
        .withTestBatch(testBatch)
        .build();

    Donation expectedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withComponents(Arrays.asList(initialComponent))
        .withDonationBatch(donationBatch)
        .withTestBatch(testBatch)
        .build();

    // Exercise SUT
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationConstraintChecker.canEditBleedTimes(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canEditPackType(existingDonation)).thenReturn(true);
    when(donationConstraintChecker.canEditToNewPackType(existingDonation, newPackType)).thenReturn(true);
    when(donorConstraintChecker.isDonorDeferred(irrelevantDonorId)).thenReturn(false);
    when(componentCRUDService.createInitialComponent(updatedDonation)).thenReturn(initialComponent);
    when(donationRepository.update(argThat(hasSameStateAsDonation(expectedDonation)))).thenAnswer(returnsFirstArg());

    // Test
    donationCRUDService.updateDonation(updatedDonation);

    //verify
    verify(donationRepository).update(argThat(hasSameStateAsDonation(expectedDonation)));
    verify(testBatchStatusChangeService).handleRelease(argThat(hasSameStateAsDonation(expectedDonation)));
  }

  @Test
  public void testUpdateDonationNotInATestBatchWithNewPackTypeThatCountsAsDonationAndNoInitialComponent_shouldNotPerformRelease() {
    // Set up fixture
    Date irrelevantBleedStartTime = new DateTime().minusMinutes(30).toDate();
    Date irrelevantBleedEndTime = new DateTime().minusMinutes(5).toDate();
    UUID irrelevantDonorId = UUID.randomUUID();
    Donor donor = aDonor().withId(irrelevantDonorId).build();
    Component initialComponent = ComponentBuilder.aComponent().build();
    DonationBatch donationBatch = aDonationBatch().withId(IRRELEVANT_DONATION_BATCH_ID).build();

    UUID oldPackTypeId = UUID.randomUUID();
    PackType oldPackType = aPackType()
        .withId(oldPackTypeId)
        .withTestSampleProduced(true)
        .withCountAsDonation(false)
        .build();

    UUID newPackTypeId = UUID.randomUUID();
    PackType newPackType = aPackType()
        .withId(newPackTypeId)
        .withTestSampleProduced(true)
        .withCountAsDonation(true)
        .build();

    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(oldPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withDonationBatch(donationBatch)
        .build();

    Donation updatedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withDonationBatch(donationBatch)
        .build();

    Donation expectedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withComponents(Arrays.asList(initialComponent))
        .withDonationBatch(donationBatch)
        .build();

    // Exercise SUT
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationConstraintChecker.canEditBleedTimes(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canEditPackType(existingDonation)).thenReturn(true);
    when(donationConstraintChecker.canEditToNewPackType(existingDonation, newPackType)).thenReturn(true);
    when(donorConstraintChecker.isDonorDeferred(irrelevantDonorId)).thenReturn(false);
    when(componentCRUDService.createInitialComponent(updatedDonation)).thenReturn(initialComponent);
    when(donationRepository.update(argThat(hasSameStateAsDonation(expectedDonation)))).thenAnswer(returnsFirstArg());

    // Test
    donationCRUDService.updateDonation(updatedDonation);

    //verify
    verify(donationRepository).update(argThat(hasSameStateAsDonation(expectedDonation)));
    verify(testBatchStatusChangeService, never()).handleRelease(any(Donation.class));
  }

  @Test
  public void testUpdateDonationInOpenTestBatchWithNewPackTypeThatCountsAsDonationAndNoInitialComponent_shouldNotPerformRelease() {
    // Set up fixture
    Date irrelevantBleedStartTime = new DateTime().minusMinutes(30).toDate();
    Date irrelevantBleedEndTime = new DateTime().minusMinutes(5).toDate();
    UUID irrelevantDonorId = UUID.randomUUID();
    Donor donor = aDonor().withId(irrelevantDonorId).build();
    Component initialComponent = ComponentBuilder.aComponent().build();
    DonationBatch donationBatch = aDonationBatch().withId(IRRELEVANT_DONATION_BATCH_ID).build();
    TestBatch testBatch = aTestBatch()
        .withId(IRRELEVANT_TEST_BATCH_ID)
        .withStatus(OPEN)
        .build();

    UUID oldPackTypeId = UUID.randomUUID();
    PackType oldPackType = aPackType()
        .withId(oldPackTypeId)
        .withTestSampleProduced(true)
        .withCountAsDonation(false)
        .build();

    UUID newPackTypeId = UUID.randomUUID();
    PackType newPackType = aPackType()
        .withId(newPackTypeId)
        .withTestSampleProduced(true)
        .withCountAsDonation(true)
        .build();

    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(oldPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withDonationBatch(donationBatch)
        .withTestBatch(testBatch)
        .build();

    Donation updatedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withDonationBatch(donationBatch)
        .withTestBatch(testBatch)
        .build();

    Donation expectedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withComponents(Arrays.asList(initialComponent))
        .withDonationBatch(donationBatch)
        .withTestBatch(testBatch)
        .build();

    // Exercise SUT
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationConstraintChecker.canEditBleedTimes(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canEditPackType(existingDonation)).thenReturn(true);
    when(donationConstraintChecker.canEditToNewPackType(existingDonation, newPackType)).thenReturn(true);
    when(donorConstraintChecker.isDonorDeferred(irrelevantDonorId)).thenReturn(false);
    when(componentCRUDService.createInitialComponent(updatedDonation)).thenReturn(initialComponent);
    when(donationRepository.update(argThat(hasSameStateAsDonation(expectedDonation)))).thenAnswer(returnsFirstArg());

    // Test
    donationCRUDService.updateDonation(updatedDonation);

    //verify
    verify(donationRepository).update(argThat(hasSameStateAsDonation(expectedDonation)));
    verify(testBatchStatusChangeService, never()).handleRelease(any(Donation.class));
  }

  @Test
  public void testUpdateDonationWithNewPackTypeThatCountsAsDonation_shouldUpdateExistingComponent() {
    UUID irrelevantDonorId = UUID.randomUUID();
    Donor donor = aDonor().withId(irrelevantDonorId).build();
    Date irrelevantBleedStartTime = new DateTime().minusMinutes(30).toDate();
    Date irrelevantBleedEndTime = new DateTime().minusMinutes(5).toDate();
    Date expiresOn = new DateTime().plusMonths(1).toDate();
    UUID oldPackTypeId = UUID.randomUUID();
    PackType oldPackType = aPackType()
        .withId(oldPackTypeId)
        .withTestSampleProduced(true)
        .withCountAsDonation(true)
        .build();
    UUID newPackTypeId = UUID.randomUUID();
    PackType newPackType = aPackType()
        .withId(newPackTypeId)
        .withTestSampleProduced(true)
        .withCountAsDonation(true)
        .build();

    ComponentType componentType = aComponentType().build();
    Component existingComponent = ComponentBuilder.aComponent()
        .withComponentCode("0011")
        .withComponentType(componentType)
        .withExpiresOn(expiresOn)
        .build();

    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(donor)
        .withPackType(oldPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withComponent(existingComponent)
        .build();

    Component updatedComponent = ComponentBuilder.aComponent()
        .withComponentCode("00122")
        .withComponentType(componentType)
        .withExpiresOn(new DateTime().plusMonths(3).toDate())
        .build();

    Donation expectedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withPackType(newPackType)
        .withBleedStartTime(irrelevantBleedStartTime)
        .withBleedEndTime(irrelevantBleedEndTime)
        .withComponent(updatedComponent)
        .build();

    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationConstraintChecker.canEditBleedTimes(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canEditPackType(existingDonation)).thenReturn(true);
    when(donationConstraintChecker.canEditToNewPackType(existingDonation, newPackType)).thenReturn(true);
    when(donorConstraintChecker.isDonorDeferred(irrelevantDonorId)).thenReturn(false);
    when(componentCRUDService.updateComponentWithNewPackType(existingComponent, newPackType)).thenReturn(updatedComponent);
    when(donationRepository.update(expectedDonation)).thenReturn(expectedDonation);

    // Run test
    Donation returnedDonation = donationCRUDService.updateDonation(expectedDonation);

    // Verify
    verify(componentCRUDService).updateComponentWithNewPackType(existingDonation.getComponents().get(0), newPackType);
    assertThat(returnedDonation, hasSameStateAsDonation(expectedDonation));
  }

  @Test
  public void testUpdateDonationWithDifferentBleedTime_shouldUpdateComponentCreatedOn() {
    DateTime donationDate = new DateTime().minusDays(2);
    DateTime createdOn = donationDate;
    DateTime newBleedStartTime = new DateTime().minusMinutes(130);
    DateTime newBleedEndTime = new DateTime().minusMinutes(120);
    DateTime expectedCreatedOn = new DateTime(donationDate.getYear(), donationDate.getMonthOfYear(), donationDate.getDayOfMonth(),
        newBleedStartTime.getHourOfDay(), newBleedStartTime.getMinuteOfHour());
    UUID irrelevantDonorId = UUID.randomUUID();

    ComponentType irrelevantComponentType = aComponentType().build();
    Component initialComponent = aComponent()
        .withId(IRRELEVANT_COMPONENT_ID)
        .withComponentType(irrelevantComponentType)
        .withCreatedOn(donationDate.toDate())
        .build();
    Component expectedInitialComponent = aComponent()
        .withId(IRRELEVANT_COMPONENT_ID)
        .withComponentType(irrelevantComponentType)
        .withCreatedOn(createdOn.toDate())
        .build();

    Donor irrelevantDonor = aDonor().withId(irrelevantDonorId).build();
    PackType irrelevantPackType = aPackType().build();
    Donation existingDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(irrelevantDonor)
        .withPackType(irrelevantPackType)
        .withDonationDate(donationDate.toDate())
        .withBleedStartTime(new Date())
        .withBleedEndTime(new Date())
        .withComponent(initialComponent)
        .build();
    Donation expectedDonation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(irrelevantDonor)
        .withPackType(irrelevantPackType)
        .withDonationDate(donationDate.toDate())
        .withBleedStartTime(newBleedStartTime.toDate())
        .withBleedEndTime(newBleedEndTime.toDate())
        .withComponent(expectedInitialComponent)
        .build();

    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
    when(donationConstraintChecker.canEditBleedTimes(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donorConstraintChecker.isDonorDeferred(irrelevantDonorId)).thenReturn(false);
    when(donationRepository.update(expectedDonation)).thenReturn(expectedDonation);
    when(dateGeneratorService.generateDateTime(createdOn.toDate(), newBleedStartTime.toDate())).thenReturn(expectedCreatedOn.toDate());

    // Run test
    Donation returnedDonation = donationCRUDService.updateDonation(expectedDonation);

    // Verify
    verify(dateGeneratorService).generateDateTime(createdOn.toDate(), newBleedStartTime.toDate());
    assertThat(returnedDonation.getInitialComponent(), hasSameStateAsComponent(expectedDonation.getInitialComponent()));
  }

  @Test
  public void testClearTestOutcomes_shouldResetStatusesAndDelegateToBloodTestService() {
    Donation donation = aDonation().withBloodAbo("A").withBloodRh("-")
        .withTTIStatus(TTIStatus.SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH).build();

    donationCRUDService.clearTestOutcomes(donation);

    assertThat(donation, testStatusesAreReset());
    verify(bloodTestsService).setTestOutcomesAsDeleted(argThat(hasSameStateAsDonation(donation)));
  }
}
