package org.jembi.bsis.controllerservice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonorOutcomesViewModelBuilder.aDonorOutcomesViewModel;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.dto.MobileClinicDonorDTO;
import org.jembi.bsis.factory.DonorOutcomesViewModelFactory;
import org.jembi.bsis.factory.MobileClinicDonorFactory;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.helpers.builders.MobileClinicDonorBuilder;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.BloodTestRepository;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.DonorOutcomesViewModel;
import org.jembi.bsis.viewmodel.MobileClinicExportDonorViewModel;
import org.jembi.bsis.viewmodel.MobileClinicLookUpDonorViewModel;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class MobileClinicControllerServiceTests extends UnitTestSuite {

  @InjectMocks
  private MobileClinicControllerService mobileClinicControllerService;
  @Mock
  private LocationRepository locationRepository;
  @Mock
  private DonationRepository donationRepository;
  @Mock
  private DonorOutcomesViewModelFactory donorOutcomesViewModelFactory;
  @Mock
  private BloodTestRepository bloodTestRepository;
  @Mock
  private DonorRepository donorRepository;
  @Mock
  private MobileClinicDonorFactory mobileClinicDonorFactory;

  @Test
  public void testGetDonorOutcomes_shouldReturnCorrectViewModels() {
    // Set up fixture
    UUID venueId = UUID.randomUUID();
    Date startDate = new DateTime().minusDays(30).toDate();
    Date endDate = new DateTime().minusDays(7).toDate();

    Location donorVenue = aVenue().withId(venueId).build();
    List<Donation> donations = Arrays.asList(aDonation().withId(UUID.randomUUID()).build(), aDonation().withId(UUID.randomUUID()).build());

    // Set up expectations
    List<DonorOutcomesViewModel> expectedViewModels = Arrays.asList(
        aDonorOutcomesViewModel().build(),
        aDonorOutcomesViewModel().build()
    );

    when(locationRepository.getLocation(venueId)).thenReturn(donorVenue);
    when(donationRepository.findLastDonationsByDonorVenueAndDonationDate(donorVenue, startDate, endDate)).thenReturn(donations);
    when(donorOutcomesViewModelFactory.createDonorOutcomesViewModels(donations)).thenReturn(expectedViewModels);

    // Exercise SUT
    List<DonorOutcomesViewModel> returnedViewModels = mobileClinicControllerService.getDonorOutcomes(venueId,
        startDate, endDate);

    // Verify
    assertThat(returnedViewModels, is(expectedViewModels));
  }

  @Test
  public void testGetBloodTestNames_shouldReturnCorrectNames() {
    // Set up fixture
    BloodTest firstBasicTtiTest = aBloodTest().withId(UUID.randomUUID()).withTestNameShort("First").build();
    BloodTest secondBasicTtiTest = aBloodTest().withId(UUID.randomUUID()).withTestNameShort("Second").build();
    BloodTest firstRepestTtiTest = aBloodTest().withId(UUID.randomUUID()).withTestNameShort("Third").build();
    BloodTest secondRepeatTtiTest = aBloodTest().withId(UUID.randomUUID()).withTestNameShort("Fourth").build();
    BloodTest firstConfirmatoryTtiTest = aBloodTest().withId(UUID.randomUUID()).withTestNameShort("Fifth").build();

    // Set expectations
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_TTI)).thenReturn(Arrays.asList(
            firstBasicTtiTest, secondBasicTtiTest));
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.REPEAT_TTI)).thenReturn(Arrays.asList(
            firstRepestTtiTest, secondRepeatTtiTest));
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.CONFIRMATORY_TTI)).thenReturn(Arrays.asList(
            firstConfirmatoryTtiTest));

    List<String> expectedBloodTestNames = Arrays.asList("First", "Second", "Third", "Fourth", "Fifth");

    // Exercise SUT
    List<String> returnedBloodTestNames = mobileClinicControllerService.getBloodTestNames();

    // Verify
    assertThat(returnedBloodTestNames, is(expectedBloodTestNames));
  }

  @Test
  public void testGetMobileClinicDonorsByVenue() throws Exception {
    // Set up
    Date clinicDate = new Date();

    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).withName("test").build();
    MobileClinicDonorDTO donor1 = MobileClinicDonorBuilder.aMobileClinicDonor()
        .withVenue(venue)
        .build();
    MobileClinicDonorDTO donor2 = MobileClinicDonorBuilder.aMobileClinicDonor()
        .withVenue(venue)
        .build();
    List<MobileClinicDonorDTO> clinicDonorDTOs = new ArrayList<>();
    clinicDonorDTOs.add(donor1);
    clinicDonorDTOs.add(donor2);

    // Set expectations
    List<MobileClinicLookUpDonorViewModel> expectedClinicDonorsViewModels = new ArrayList<>();
    expectedClinicDonorsViewModels.add(new MobileClinicLookUpDonorViewModel(donor1));
    expectedClinicDonorsViewModels.add(new MobileClinicLookUpDonorViewModel(donor2));

    //Mock
    when(mobileClinicControllerService.getMobileClinicDonorsByVenue(venue.getId(), clinicDate)).thenReturn(expectedClinicDonorsViewModels);
    when(donorRepository.findMobileClinicDonorsByVenues(new HashSet<UUID>(Arrays.asList(venue.getId()))))
        .thenReturn(clinicDonorDTOs);
    when(mobileClinicDonorFactory.createMobileClinicDonorViewModels(clinicDonorDTOs,clinicDate)).thenReturn(expectedClinicDonorsViewModels);

    // Exercise SUT
    List<MobileClinicLookUpDonorViewModel> returnedClinicDonorsViewModels = mobileClinicControllerService.getMobileClinicDonorsByVenue(venue.getId(), clinicDate);

    // Verify
    assertThat(returnedClinicDonorsViewModels, is(expectedClinicDonorsViewModels));
  }

  @Test
  public void testGetMobileClinicDonorsByVenues() throws Exception {
    // Set up
    Date clinicDate = new Date();
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).withName("test").build();
    MobileClinicDonorDTO donor1 = MobileClinicDonorBuilder.aMobileClinicDonor()
        .withVenue(venue)
        .build();
    MobileClinicDonorDTO donor2 = MobileClinicDonorBuilder.aMobileClinicDonor()
        .withVenue(venue)
        .build();
    List<MobileClinicDonorDTO> clinicDonorDTOs = new ArrayList<>();
    clinicDonorDTOs.add(donor1);
    clinicDonorDTOs.add(donor2);

    // Set expectations
    List<MobileClinicExportDonorViewModel> expectedClinicDonorsViewModels = new ArrayList<>();
    expectedClinicDonorsViewModels
        .add(mobileClinicDonorFactory.createMobileClinicExportDonorViewModel(donor1, clinicDate));
    expectedClinicDonorsViewModels
        .add(mobileClinicDonorFactory.createMobileClinicExportDonorViewModel(donor2, clinicDate));

    //Mock
    when(mobileClinicControllerService.getMobileClinicDonorsByVenues(new HashSet<UUID>(Arrays.asList(venue.getId())),
        clinicDate)).thenReturn(expectedClinicDonorsViewModels);
    when(donorRepository.findMobileClinicDonorsByVenues(new HashSet<UUID>(Arrays.asList(venue.getId()))))
        .thenReturn(clinicDonorDTOs);
    when(mobileClinicDonorFactory.createMobileClinicExportDonorViewModels(clinicDonorDTOs, clinicDate))
        .thenReturn(expectedClinicDonorsViewModels);

    // Exercise SUT
    List<MobileClinicExportDonorViewModel> returnedClinicDonorsViewModels = mobileClinicControllerService
        .getMobileClinicDonorsByVenues(new HashSet<UUID>(Arrays.asList(venue.getId())), clinicDate);

    // Verify
    assertThat(returnedClinicDonorsViewModels, is(expectedClinicDonorsViewModels));
  }

}
