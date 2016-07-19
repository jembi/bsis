package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.AdverseEventBuilder.anAdverseEvent;
import static org.jembi.bsis.helpers.builders.AdverseEventViewModelBuilder.anAdverseEventViewModel;
import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonationTypeBuilder.aDonationType;
import static org.jembi.bsis.helpers.builders.DonationViewModelBuilder.aDonationViewModel;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.builders.PackTypeFullViewModelBuilder.aPackTypeViewFullModel;
import static org.jembi.bsis.helpers.matchers.DonationViewModelMatcher.hasSameStateAsDonationViewModel;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.model.adverseevent.AdverseEvent;
import org.jembi.bsis.model.bloodtesting.TTIStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.HaemoglobinLevel;
import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.repository.bloodtesting.BloodTypingMatchStatus;
import org.jembi.bsis.repository.bloodtesting.BloodTypingStatus;
import org.jembi.bsis.service.DonationConstraintChecker;
import org.jembi.bsis.service.DonorConstraintChecker;
import org.jembi.bsis.viewmodel.AdverseEventViewModel;
import org.jembi.bsis.viewmodel.DonationTypeViewModel;
import org.jembi.bsis.viewmodel.DonationViewModel;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.jembi.bsis.viewmodel.PackTypeFullViewModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DonationViewModelFactoryTests {

  private static final long IRRELEVANT_DONATION_ID = 89;
  private static final long ANOTHER_IRRELEVANT_DONATION_ID = 90;
  private static final long IRRELEVANT_DONOR_ID = 89;
  private static final long ANOTHER_IRRELEVANT_DONOR_ID = 90;
  private static final long IRRELEVANT_PACKTYPE_ID = 99;

  @InjectMocks
  private DonationViewModelFactory donationViewModelFactory;
  @Mock
  private DonationConstraintChecker donationConstraintChecker;
  @Mock
  private AdverseEventViewModelFactory adverseEventViewModelFactory;
  @Mock
  private DonorConstraintChecker donorConstraintChecker;
  @Mock
  private LocationFactory locationFactory;
  @Mock
  private PackTypeFactory packTypeFactory;

  @Test
  public void testCreateDonationViewModelWithPermissions_shouldReturnViewModelWithCorrectDonationAndPermissions() {

    boolean irrelevantCanDeletePermission = true;
    boolean irrelevantCanUpdatePermission = true;
    boolean irrelevantCanDonatePermission = true;
    boolean irrelevantIsBackEntryPermission = true;

    Long irrelevantAdverseEventId = 11L;
    Date donationDate = new Date();
    String donationIdentificationNumber = "0000001";
    String donorNumber = "000001";
    DonationType donationType = aDonationType().withId(23L).build();
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
    Location venue = aVenue().withId(79L).build();
    AdverseEvent adverseEvent = anAdverseEvent().withId(irrelevantAdverseEventId).build();

    Donation donation = aDonation().withId(IRRELEVANT_DONATION_ID)
        .withDonor(aDonor().withId(IRRELEVANT_DONOR_ID).withDonorNumber(donorNumber).build())
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

    DonationViewModel expectedDonationViewModel = aDonationViewModel()
        .withId(IRRELEVANT_DONATION_ID)
        .withPermission("canDelete", irrelevantCanDeletePermission)
        .withPermission("canUpdateDonationFields", irrelevantCanUpdatePermission)
        .withPermission("canDonate", irrelevantCanDonatePermission)
        .withPermission("isBackEntry", irrelevantIsBackEntryPermission)
        .withAdverseEvent(adverseEventViewModel)
            .withPackType(packTypeFullViewModel)
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withDonationDate(donationDate)
        .withDonationType(new DonationTypeViewModel(donationType))
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
        .withVenue(new LocationFullViewModel(venue))
        .thatIsReleased()
        .build();

    when(donationConstraintChecker.canDeleteDonation(IRRELEVANT_DONATION_ID)).thenReturn(irrelevantCanDeletePermission);
    when(donationConstraintChecker.canUpdateDonationFields(IRRELEVANT_DONATION_ID)).thenReturn(irrelevantCanUpdatePermission);
    when(donorConstraintChecker.isDonorEligibleToDonate(IRRELEVANT_DONOR_ID)).thenReturn(irrelevantCanDonatePermission);
    when(adverseEventViewModelFactory.createAdverseEventViewModel(adverseEvent)).thenReturn(adverseEventViewModel);
    when(locationFactory.createFullViewModel(venue)).thenReturn(new LocationFullViewModel(venue));
    when(packTypeFactory.createFullViewModel(packType)).thenReturn(packTypeFullViewModel);

    DonationViewModel returnedDonationViewModel = donationViewModelFactory.createDonationViewModelWithPermissions(
        donation);

    assertThat(returnedDonationViewModel, hasSameStateAsDonationViewModel(expectedDonationViewModel));
  }

  @Test
  public void testCreateDonationViewModelsWithPermissions_shouldReturnViewModelsWithCorrectDonationAndPermissions() {

    Long irrelevantAdverseEventId = 11L;
    AdverseEvent adverseEvent = anAdverseEvent().withId(irrelevantAdverseEventId).build();
    PackType packType = aPackType().withId(IRRELEVANT_PACKTYPE_ID).build();
    PackTypeFullViewModel packTypeFullViewModel = aPackTypeViewFullModel().withId(IRRELEVANT_PACKTYPE_ID).build();
    DonationType donationType = aDonationType().withId(23L).build();
    Donation donation1 = aDonation().withId(IRRELEVANT_DONATION_ID)
        .withDonor(aDonor().withId(IRRELEVANT_DONOR_ID).build())
        .withDonationBatch(aDonationBatch().thatIsBackEntry().build())
        .withAdverseEvent(adverseEvent)
        .withPackType(packType)
        .withDonationType(donationType)
        .build();
    Donation donation2 = aDonation().withId(ANOTHER_IRRELEVANT_DONATION_ID)
        .withDonor(aDonor().withId(ANOTHER_IRRELEVANT_DONOR_ID).build())
        .withDonationBatch(aDonationBatch().build())
        .withPackType(packType)
        .withDonationType(donationType)
        .build();
    List<Donation> donations = Arrays.asList(new Donation[]{donation1, donation2});

    AdverseEventViewModel adverseEventViewModel = anAdverseEventViewModel().withId(irrelevantAdverseEventId).build();

    DonationViewModel expectedDonation1ViewModel = aDonationViewModel()
        .withId(IRRELEVANT_DONATION_ID)
        .withPermission("canDelete", true)
        .withPermission("canUpdateDonationFields", true)
        .withPermission("canDonate", true)
        .withPermission("isBackEntry", true)
        .withAdverseEvent(adverseEventViewModel)
        .withPackType(packTypeFullViewModel)
        .withDonationType(new DonationTypeViewModel(donationType))
        .build();
    DonationViewModel expectedDonation2ViewModel = aDonationViewModel()
        .withId(ANOTHER_IRRELEVANT_DONATION_ID)
        .withPermission("canDelete", true)
        .withPermission("canUpdateDonationFields", true)
        .withPermission("canDonate", false)
        .withPermission("isBackEntry", false)
        .withPackType(packTypeFullViewModel)
        .withDonationType(new DonationTypeViewModel(donationType))
        .build();

    when(donationConstraintChecker.canDeleteDonation(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canUpdateDonationFields(IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donorConstraintChecker.isDonorEligibleToDonate(IRRELEVANT_DONOR_ID)).thenReturn(true);
    when(donorConstraintChecker.isDonorDeferred(IRRELEVANT_DONATION_ID)).thenReturn(false);
    when(adverseEventViewModelFactory.createAdverseEventViewModel(adverseEvent)).thenReturn(adverseEventViewModel);
    when(donationConstraintChecker.canDeleteDonation(ANOTHER_IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donationConstraintChecker.canUpdateDonationFields(ANOTHER_IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donorConstraintChecker.isDonorEligibleToDonate(ANOTHER_IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(donorConstraintChecker.isDonorDeferred(ANOTHER_IRRELEVANT_DONATION_ID)).thenReturn(true);
    when(packTypeFactory.createFullViewModel(packType)).thenReturn(packTypeFullViewModel);

    List<DonationViewModel> returnedDonationViewModels = donationViewModelFactory.createDonationViewModelsWithPermissions(donations);

    assertThat(returnedDonationViewModels.get(0), hasSameStateAsDonationViewModel(expectedDonation1ViewModel));
    assertThat(returnedDonationViewModels.get(1), hasSameStateAsDonationViewModel(expectedDonation2ViewModel));
  }
}
