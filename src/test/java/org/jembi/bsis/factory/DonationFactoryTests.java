package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.jembi.bsis.helpers.builders.AdverseEventBackingFormBuilder.anAdverseEventBackingForm;
import static org.jembi.bsis.helpers.builders.AdverseEventBuilder.anAdverseEvent;
import static org.jembi.bsis.helpers.builders.AdverseEventViewModelBuilder.anAdverseEventViewModel;
import static org.jembi.bsis.helpers.builders.DonationBackingFormBuilder.aDonationBackingForm;
import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonationFullViewModelBuilder.aDonationFullViewModel;
import static org.jembi.bsis.helpers.builders.DonationTypeBuilder.aDonationType;
import static org.jembi.bsis.helpers.builders.DonationTypeViewModelBuilder.aDonationTypeViewModel;
import static org.jembi.bsis.helpers.matchers.DonationTypeMatcher.hasSameStateAsDonationType;
import static org.jembi.bsis.helpers.builders.DonationViewModelBuilder.aDonationViewModel;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.matchers.LocationMatcher.hasSameStateAsLocation;
import static org.jembi.bsis.helpers.builders.LocationViewModelBuilder.aLocationViewModel;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.matchers.PackTypeMatcher.hasSameStateAsPackType;
import static org.jembi.bsis.helpers.builders.PackTypeFullViewModelBuilder.aPackTypeViewFullModel;
import static org.jembi.bsis.helpers.builders.PackTypeViewModelBuilder.aPackTypeViewModel;
import static org.jembi.bsis.helpers.matchers.DonationFullViewModelMatcher.hasSameStateAsDonationFullViewModel;
import static org.jembi.bsis.helpers.matchers.DonationViewModelMatcher.hasSameStateAsDonationViewModel;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.AdverseEventBackingForm;
import org.jembi.bsis.backingform.DonationBackingForm;
import org.jembi.bsis.helpers.builders.AdverseEventBuilder;
import org.jembi.bsis.helpers.builders.DonationBuilder;
import org.jembi.bsis.helpers.builders.PackTypeBuilder;
import org.jembi.bsis.helpers.matchers.DonationMatcher;
import org.jembi.bsis.model.adverseevent.AdverseEvent;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.HaemoglobinLevel;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.repository.PackTypeRepository;
import org.jembi.bsis.service.DonationConstraintChecker;
import org.jembi.bsis.service.DonorConstraintChecker;
import org.jembi.bsis.util.RandomTestDate;
import org.jembi.bsis.viewmodel.AdverseEventViewModel;
import org.jembi.bsis.viewmodel.DonationFullViewModel;
import org.jembi.bsis.viewmodel.DonationTypeViewModel;
import org.jembi.bsis.viewmodel.DonationViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.PackTypeFullViewModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DonationFactoryTests {

  private static final UUID IRRELEVANT_DONOR_ID = UUID.randomUUID();
  private static final UUID IRRELEVANT_DONATION_ID = UUID.randomUUID();
  private static final UUID ANOTHER_IRRELEVANT_DONATION_ID = UUID.randomUUID();
  private static final UUID IRRELEVANT_PACKTYPE_ID = UUID.randomUUID();
  private static final UUID IRRELEVANT_DONATIONTYPE_ID = UUID.randomUUID();
  private static final UUID IRRELEVANT_LOCATION_ID = UUID.randomUUID();

  @InjectMocks
  private DonationFactory donationFactory;
  @Mock
  private DonationConstraintChecker donationConstraintChecker;
  @Mock
  private AdverseEventFactory adverseEventFactory;
  @Mock
  private DonorConstraintChecker donorConstraintChecker;
  @Mock
  private LocationFactory locationFactory;
  @Mock
  private PackTypeFactory packTypeFactory;
  @Mock
  private PackTypeRepository packTypeRepository;
  @Mock
  private DonorRepository donorRepository;
  @Mock
  private DonationTypeFactory donationTypeFactory;

  @Test
  public void testCreateEntity_shouldReturnCorrectEntity() {
    // Set up data
    UUID donorId = UUID.randomUUID();
    AdverseEventBackingForm adverseEventForm = anAdverseEventBackingForm().build();
    Donor donor = aDonor().withId(donorId).build();
    PackType packType = PackTypeBuilder.aPackType().withId(IRRELEVANT_PACKTYPE_ID).build();

    DonationBackingForm donationForm = aDonationBackingForm()
        .withAdverseEvent(adverseEventForm)
        .withDonor(donor)
        .withPackType(packType)
        .build();
    
    AdverseEvent adverseEvent = AdverseEventBuilder.anAdverseEvent().build(); 
    Donation expectedEntity = DonationBuilder.aDonation()
        .withAdverseEvent(adverseEvent)
        .withDonor(donor)
        .withPackType(packType)
        .build();

    // Set up mocks
    when(packTypeRepository.getPackTypeById(IRRELEVANT_PACKTYPE_ID)).thenReturn(packType);
    when(donorRepository.findDonorById(donorId)).thenReturn(donor);
    when(adverseEventFactory.createEntity(adverseEventForm)).thenReturn(adverseEvent);

    // Run test
    Donation convertedEntity = donationFactory.createEntity(donationForm);
   
    // Verify
    assertThat(convertedEntity, DonationMatcher.hasSameStateAsDonation(expectedEntity));
  }

  @Test
  public void testCreateDonationFullViewModelWithPermissions_shouldReturnViewModelWithCorrectDonationAndPermissions() {

    boolean irrelevantCanDeletePermission = true;
    boolean irrelevantCanUpdatePermission = true;
    boolean irrelevantCanDonatePermission = true;
    boolean irrelevantIsBackEntryPermission = true;
    boolean irrelevantCanEditPackTypePermission = true;

    UUID irrelevantAdverseEventId = UUID.randomUUID();
    UUID donationTypeId = UUID.randomUUID();
    UUID irrelevantDonorId = UUID.randomUUID();
    
    Date donationDate = new Date();
    String donationIdentificationNumber = "0000001";
    String donorNumber = "000001";
    DonationType donationType = aDonationType().withId(donationTypeId).build();
    PackType packType = aPackType().withId(IRRELEVANT_PACKTYPE_ID).build();
    PackTypeFullViewModel packTypeFullViewModel = aPackTypeViewFullModel().withId(IRRELEVANT_PACKTYPE_ID).build();
    String notes = "some notes";
    TTIStatus ttiStatus = TTIStatus.NOT_DONE;
    String batchNumber = "010100";
    BloodTypingStatus bloodTypingStatus = BloodTypingStatus.NOT_DONE;
    BloodTypingMatchStatus bloodTypingMatchStatus = BloodTypingMatchStatus.NOT_DONE;
    String bloodAbo = "A";
    String bloodRh = "+";
    BigDecimal haemoglobinCount = new BigDecimal(12.02);
    HaemoglobinLevel haemoglobinLevel = HaemoglobinLevel.PASS;
    BigDecimal donorWeight = new BigDecimal(120.6);
    int donorPulse = 80;
    int bloodPressureSystolic = 120;
    int bloodPressureDiastolic = 80;
    Date bleedStartTime = new Date();
    Date bleedEndTime = new Date();
    UUID locationId = UUID.randomUUID();
    Location venue = aVenue().withId(locationId).build();
    AdverseEvent adverseEvent = anAdverseEvent().withId(irrelevantAdverseEventId).build();

    Donation donation = aDonation().withId(IRRELEVANT_DONATION_ID)
        .withDonor(aDonor().withId(irrelevantDonorId).withDonorNumber(donorNumber).build())
        .withDonationBatch(aDonationBatch().thatIsBackEntry().withBatchNumber(batchNumber).build())
        .withAdverseEvent(adverseEvent)
        .withPackType(packType)
        .withDonationDate(donationDate)
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withDonationType(donationType)
        .withNotes(notes)
        .withTTIStatus(ttiStatus)
        .withBloodTypingStatus(bloodTypingStatus)
        .withBloodTypingMatchStatus(bloodTypingMatchStatus)
        .withBloodAbo(bloodAbo)
        .withBloodRh(bloodRh)
        .withHaemoglobinCount(haemoglobinCount)
        .withHaemoglobinLevel(haemoglobinLevel)
        .withDonorWeight(donorWeight)
        .withDonorPulse(donorPulse)
        .withBloodPressureSystolic(bloodPressureSystolic)
        .withBloodPressureDiastolic(bloodPressureDiastolic)
        .withBleedStartTime(bleedStartTime)
        .withBleedEndTime(bleedEndTime)
        .withVenue(venue)
        .thatIsReleased()
        .build();

    AdverseEventViewModel adverseEventViewModel = anAdverseEventViewModel().withId(irrelevantAdverseEventId).build();
    DonationTypeViewModel donationTypeViewModel = aDonationTypeViewModel().withId(donationTypeId).build();
    LocationViewModel venueViewModel = aLocationViewModel().withId(venue.getId()).build();

    DonationFullViewModel expectedDonationFullViewModel = aDonationFullViewModel()
        .withId(IRRELEVANT_DONATION_ID)
        .withPermission("canDelete", irrelevantCanDeletePermission)
        .withPermission("canEditBleedTimes", irrelevantCanUpdatePermission)
        .withPermission("canDonate", irrelevantCanDonatePermission)
        .withPermission("canEditPackType", irrelevantCanEditPackTypePermission)
        .withPermission("isBackEntry", irrelevantIsBackEntryPermission)
        .withAdverseEvent(adverseEventViewModel)
            .withPackType(packTypeFullViewModel)
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withDonationDate(donationDate)
        .withDonationType(donationTypeViewModel)
        .withNotes(notes)
        .withDonorNumber(donorNumber)
        .withTTIStatus(ttiStatus)
        .withDonationBatchNumber(batchNumber)
        .withBloodTypingStatus(bloodTypingStatus)
        .withBloodTypingMatchStatus(bloodTypingMatchStatus)
        .withBloodAbo(bloodAbo)
        .withBloodRh(bloodRh)
        .withHaemoglobinCount(haemoglobinCount)
        .withHaemoglobinLevel(haemoglobinLevel)
        .withDonorWeight(donorWeight)
        .withDonorPulse(donorPulse)
        .withBloodPressureSystolic(bloodPressureSystolic)
        .withBloodPressureDiastolic(bloodPressureDiastolic)
        .withBleedStartTime(bleedStartTime)
        .withBleedEndTime(bleedEndTime)
        .withVenue(venueViewModel)
        .thatIsReleased()
        .build();

    when(donationConstraintChecker.canDeleteDonation(IRRELEVANT_DONATION_ID)).thenReturn(irrelevantCanDeletePermission);
    when(donationConstraintChecker.canEditBleedTimes(IRRELEVANT_DONATION_ID)).thenReturn(irrelevantCanUpdatePermission);
    when(donationConstraintChecker.canEditPackType(donation)).thenReturn(irrelevantCanEditPackTypePermission);
    when(donorConstraintChecker.isDonorEligibleToDonate(irrelevantDonorId)).thenReturn(irrelevantCanDonatePermission);
    when(adverseEventFactory.createAdverseEventViewModel(adverseEvent)).thenReturn(adverseEventViewModel);
    when(locationFactory.createViewModel(venue)).thenReturn(venueViewModel);
    when(packTypeFactory.createFullViewModel(packType)).thenReturn(packTypeFullViewModel);
    when(donationTypeFactory.createViewModel(donationType)).thenReturn(donationTypeViewModel);

    DonationFullViewModel returnedDonationFullViewModel = donationFactory.createDonationFullViewModelWithPermissions(
        donation);

    assertThat(returnedDonationFullViewModel, hasSameStateAsDonationFullViewModel(expectedDonationFullViewModel));
  }

  @Test
  public void testCreateDonationFullViewModelsWithPermissions_shouldReturnViewModelsWithCorrectDonationAndPermissions() {

    UUID irrelevantAdverseEventId = UUID.randomUUID();
    UUID donationTypeId = UUID.randomUUID();
    UUID irrelevantDonorId = UUID.randomUUID();
    UUID anotherIrrelevantDonorId = UUID.randomUUID();

    AdverseEvent adverseEvent = anAdverseEvent().withId(irrelevantAdverseEventId).build();
    PackType packType = aPackType().withId(IRRELEVANT_PACKTYPE_ID).build();
    PackTypeFullViewModel packTypeFullViewModel = aPackTypeViewFullModel().withId(IRRELEVANT_PACKTYPE_ID).build();
    DonationType donationType = aDonationType().withId(donationTypeId).build();
    Donation donation1 = aDonation().withId(IRRELEVANT_DONATION_ID)
        .withDonor(aDonor().withId(irrelevantDonorId).build())
        .withDonationBatch(aDonationBatch().thatIsBackEntry().build())
        .withAdverseEvent(adverseEvent)
        .withPackType(packType)
        .withDonationType(donationType)
        .build();
    Donation donation2 = aDonation().withId(ANOTHER_IRRELEVANT_DONATION_ID)
        .withDonor(aDonor().withId(anotherIrrelevantDonorId).build())
        .withDonationBatch(aDonationBatch().build())
        .withPackType(packType)
        .withDonationType(donationType)
        .build();
    List<Donation> donations = Arrays.asList(new Donation[]{donation1, donation2});

    AdverseEventViewModel adverseEventViewModel = anAdverseEventViewModel().withId(irrelevantAdverseEventId).build();
    DonationTypeViewModel donationTypeViewModel = aDonationTypeViewModel().withId(donationTypeId).build();

    DonationFullViewModel expectedDonationFullViewModel1 = aDonationFullViewModel()
        .withId(IRRELEVANT_DONATION_ID)
        .withPermission("canDelete", true)
        .withPermission("canEditBleedTimes", true)
        .withPermission("canEditPackType", true)
        .withPermission("canDonate", true)
        .withPermission("isBackEntry", true)
        .withAdverseEvent(adverseEventViewModel)
        .withPackType(packTypeFullViewModel)
        .withDonationType(donationTypeViewModel)
        .build();
    DonationFullViewModel expectedDonationFullViewModel2 = aDonationFullViewModel()
        .withId(ANOTHER_IRRELEVANT_DONATION_ID)
        .withPermission("canDelete", true)
        .withPermission("canEditBleedTimes", true)
        .withPermission("canEditPackType", false)
        .withPermission("canDonate", false)
        .withPermission("isBackEntry", false)
        .withPackType(packTypeFullViewModel)
        .withDonationType(donationTypeViewModel)
        .build();

    when(donationConstraintChecker.canDeleteDonation(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canEditBleedTimes(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canEditPackType(donation1)).thenReturn(true);
    when(donorConstraintChecker.isDonorEligibleToDonate(irrelevantDonorId)).thenReturn(true);
    when(donorConstraintChecker.isDonorDeferred(irrelevantDonorId)).thenReturn(false);
    when(adverseEventFactory.createAdverseEventViewModel(adverseEvent)).thenReturn(adverseEventViewModel);
    when(donationConstraintChecker.canDeleteDonation(ANOTHER_IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canEditBleedTimes(ANOTHER_IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canEditPackType(donation2)).thenReturn(false);
    when(donorConstraintChecker.isDonorEligibleToDonate(anotherIrrelevantDonorId)).thenReturn(true);
    when(donorConstraintChecker.isDonorDeferred(anotherIrrelevantDonorId)).thenReturn(true);
    when(packTypeFactory.createFullViewModel(packType)).thenReturn(packTypeFullViewModel);
    when(donationTypeFactory.createViewModel(donationType)).thenReturn(donationTypeViewModel);

    List<DonationFullViewModel> returnedDonationViewModels = donationFactory.createDonationFullViewModelsWithPermissions(donations);

    assertThat(returnedDonationViewModels.get(0), hasSameStateAsDonationFullViewModel(expectedDonationFullViewModel1));
    assertThat(returnedDonationViewModels.get(1), hasSameStateAsDonationFullViewModel(expectedDonationFullViewModel2));
  }

  @Test
  public void testCreateDonationViewModel_shouldReturnCorrectEntity() throws Exception {
    String donorNumber = "123";
    String donationIdentificationNumber = "1234567";
    Date donationDate = new RandomTestDate();
    String bloodAbo = "A";
    String bloodRh = "-";
    Donation donation = aDonation()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonor(aDonor().withId(IRRELEVANT_DONOR_ID).withDonorNumber(donorNumber).build())
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withPackType(aPackType().withId(IRRELEVANT_PACKTYPE_ID).build())
        .withDonationType(aDonationType().withId(IRRELEVANT_DONATIONTYPE_ID).build())
        .withDonationDate(donationDate)
        .withVenue(aVenue().withId(IRRELEVANT_LOCATION_ID).build())
        .withTTIStatus(TTIStatus.SAFE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withBloodAbo(bloodAbo)
        .withBloodRh(bloodRh)
        .thatIsReleased()
        .thatIsNotDeleted()
        .build();

    DonationViewModel expectedViewModel = aDonationViewModel()
        .withId(IRRELEVANT_DONATION_ID)
        .withDonorNumber(donorNumber)
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withPackType(aPackTypeViewModel().withId(IRRELEVANT_PACKTYPE_ID).build())
        .withDonationType(aDonationTypeViewModel().withId(IRRELEVANT_DONATIONTYPE_ID).build())
        .withDonationDate(donationDate)
        .withVenue(aLocationViewModel().withId(IRRELEVANT_LOCATION_ID).build())
        .withTTIStatus(TTIStatus.SAFE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withBloodAbo(bloodAbo)
        .withBloodRh(bloodRh)
        .thatIsReleased()
        .build();

    when(packTypeFactory.createViewModel(argThat(hasSameStateAsPackType(donation.getPackType())))).thenReturn(
        expectedViewModel.getPackType());
    when(donationTypeFactory.createViewModel(argThat(hasSameStateAsDonationType(donation.getDonationType()))))
        .thenReturn(expectedViewModel.getDonationType());
    when(locationFactory.createViewModel(argThat(hasSameStateAsLocation(donation.getVenue())))).thenReturn(
        expectedViewModel.getVenue());

    DonationViewModel actualViewModel = donationFactory.createDonationViewModel(donation);

    assertThat(actualViewModel, hasSameStateAsDonationViewModel(expectedViewModel));
  }

  @Test
  public void testCreateDonationViewModels_shouldReturnList() throws Exception {   
    List<Donation> donations = Arrays.asList(
        aDonation().withId(UUID.randomUUID()).build(),
        aDonation().withId(UUID.randomUUID()).build(),
        aDonation().withId(UUID.randomUUID()).build()
    );

    DonationViewModel expectedViewModel1 = aDonationViewModel().withId(donations.get(0).getId()).build();
    DonationViewModel expectedViewModel2 = aDonationViewModel().withId(donations.get(1).getId()).build();
    DonationViewModel expectedViewModel3 = aDonationViewModel().withId(donations.get(2).getId()).build();

    List<DonationViewModel> actualViewModels = donationFactory.createDonationViewModels(donations);

    assertThat(actualViewModels.size(), is(3));
    assertThat(actualViewModels, hasItem(expectedViewModel1));
    assertThat(actualViewModels, hasItem(expectedViewModel2));
    assertThat(actualViewModels, hasItem(expectedViewModel3));
  }
}
