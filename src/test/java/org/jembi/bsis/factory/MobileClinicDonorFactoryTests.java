package org.jembi.bsis.factory;

import static org.jembi.bsis.helpers.builders.LocationViewModelBuilder.aLocationViewModel;
import static org.jembi.bsis.helpers.builders.MobileClinicDonorBuilder.aMobileClinicDonor;
import static org.jembi.bsis.helpers.builders.MobileClinicExportDonorViewModelBuilder.aMobileClinicExportDonorViewModel;
import static org.jembi.bsis.helpers.matchers.MobileClinicExportViewModelMatcher.hasSameStateAsMobileClinicExportDonorViewModel;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.dto.MobileClinicDonorDTO;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.helpers.builders.MobileClinicDonorBuilder;
import org.jembi.bsis.model.donor.DonorStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.service.DonorConstraintChecker;
import org.jembi.bsis.utils.CustomDateFormatter;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.MobileClinicExportDonorViewModel;
import org.jembi.bsis.viewmodel.MobileClinicLookUpDonorViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MobileClinicDonorFactoryTests {

  @InjectMocks
  private MobileClinicDonorFactory mobileClinicDonorModelFactory;
  @Mock
  private DonorConstraintChecker donorConstraintChecker;
  @Mock
  private LocationFactory locationFactory;

  @Test
  public void testCreateMobileClinicDonor() throws Exception {
    Location venue = LocationBuilder.aLocation().withName("test").build();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    MobileClinicDonorDTO donor = MobileClinicDonorBuilder.aMobileClinicDonor()
        .withId(1L)
        .withDonorNumber("D1")
        .withFirstName("Test")
        .withLastName("DonorOne")
        .withBirthDate(sdf.parse("20/02/1975"))
        .withGender(Gender.female)
        .withDonorStatus(DonorStatus.NORMAL)
        .withVenue(venue)
        .thatIsNotDeleted()
        .build();

    Date mobileClinicDate = new Date();

    when(donorConstraintChecker.isDonorEligibleToDonateOnDate(donor.getId(), mobileClinicDate)).thenReturn(true);

    MobileClinicLookUpDonorViewModel returnedViewModel = mobileClinicDonorModelFactory.createMobileClinicDonorViewModel(donor, mobileClinicDate);

    Assert.assertNotNull("view model returned", returnedViewModel);
    Assert.assertEquals("Donor number", "D1", returnedViewModel.getDonorNumber());
    Assert.assertEquals("Donor first name", "Test", returnedViewModel.getFirstName());
    Assert.assertEquals("Donor last name", "DonorOne", returnedViewModel.getLastName());
    Assert.assertEquals("Donor birth date", CustomDateFormatter.getDateString(donor.getBirthDate()), returnedViewModel.getBirthDate());
    Assert.assertEquals("Donor gender", "female", returnedViewModel.getGender());
    Assert.assertTrue("Donor eligible", returnedViewModel.getEligibility());
  }

  @Test
  public void testCreateMobileClinicDonors() throws Exception {
    Location venue = LocationBuilder.aLocation().withName("test").build();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    MobileClinicDonorDTO donor1 = MobileClinicDonorBuilder.aMobileClinicDonor()
        .withId(1L)
        .withDonorNumber("D1")
        .withFirstName("Test")
        .withLastName("DonorOne")
        .withBirthDate(sdf.parse("20/02/1975"))
        .withGender(Gender.female)
        .withDonorStatus(DonorStatus.NORMAL)
        .withVenue(venue)
        .thatIsNotDeleted()
        .build();
    MobileClinicDonorDTO donor2 = MobileClinicDonorBuilder.aMobileClinicDonor()
        .withId(2L)
        .withDonorNumber("D2")
        .withFirstName("Test")
        .withLastName("DonorTwo")
        .withBirthDate(sdf.parse("02/12/1982"))
        .withGender(Gender.male)
        .withDonorStatus(DonorStatus.NORMAL)
        .withVenue(venue)
        .thatIsNotDeleted()
        .build();

    List<MobileClinicDonorDTO> donors = new ArrayList<>();
    donors.add(donor1);
    donors.add(donor2);

    Date mobileClinicDate = new Date();

    when(donorConstraintChecker.isDonorEligibleToDonateOnDate(1L, mobileClinicDate)).thenReturn(true);
    when(donorConstraintChecker.isDonorEligibleToDonateOnDate(2L, mobileClinicDate)).thenReturn(true);

    List<MobileClinicLookUpDonorViewModel> returnedViewModels = mobileClinicDonorModelFactory.createMobileClinicDonorViewModels(donors, mobileClinicDate);

    Assert.assertNotNull("view models returned", returnedViewModels);
    Assert.assertEquals("correct number of view models", 2, returnedViewModels.size());
  }

  @Test
  public void testCreateMobileClinicDonorsNull() throws Exception {
    List<MobileClinicLookUpDonorViewModel> returnedViewModels = mobileClinicDonorModelFactory.createMobileClinicDonorViewModels(null, new Date());
    Assert.assertNotNull("view models returned", returnedViewModels);
    Assert.assertEquals("correct number of view models", 0, returnedViewModels.size());
  }

  @Test
  public void testCreateMobileClinicExportDonorViewModel_shouldReturnViewModelWithTheCorrectState() throws Exception {

    Long id = -1L;
    String donorNumber = "12345";
    String firstName = "aFirstName";
    String lastName = "aLastName";
    String bloodAbo = "0";
    String bloodRh = "-";
    String birthDateString = "1975-02-20";
    Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(birthDateString);
    String venueName = "testVenue";
    Location venue = LocationBuilder.aLocation().withName(venueName).build();

    MobileClinicDonorDTO mobileClinicDonor =
        aMobileClinicDonor().withId(id).withDonorNumber(donorNumber).withFirstName(firstName).withLastName(lastName)
            .withGender(Gender.female).withBloodAbo(bloodAbo).withBloodRh(bloodRh).withBirthDate(birthDate)
            .withVenue(venue).withDonorStatus(DonorStatus.NORMAL).thatIsNotDeleted().build();

    LocationViewModel venueViewModel = aLocationViewModel().withName(venueName).build();
    MobileClinicExportDonorViewModel expectedMobileClinicExportDonorViewModel = aMobileClinicExportDonorViewModel()
        .withId(id).withDonorNumber(donorNumber).withFirstName(firstName).withLastName(lastName)
        .withGender(Gender.female.name()).withBloodType(bloodAbo + bloodRh).withBirthDate(birthDateString)
        .withVenue(venueViewModel).withDonorStatus(DonorStatus.NORMAL).thatIsNotDeleted().thatIsEligible().build();

    Date mobileClinicDate = new Date();

    when(donorConstraintChecker.isDonorEligibleToDonateOnDate(mobileClinicDonor.getId(), mobileClinicDate))
        .thenReturn(true);
    when(locationFactory.createViewModel(venue)).thenReturn(venueViewModel);

    MobileClinicExportDonorViewModel returnedViewModel =
        mobileClinicDonorModelFactory.createMobileClinicExportDonorViewModel(mobileClinicDonor, mobileClinicDate);

    Assert.assertThat(returnedViewModel,
        hasSameStateAsMobileClinicExportDonorViewModel(expectedMobileClinicExportDonorViewModel));
  }

  @Test
  public void testCreateMobileClinicDonorExportViewModels_shouldReturnExpectedViewModels() {
    // Set up fixture
    MobileClinicDonorDTO firstDonor = aMobileClinicDonor().withId(1L).withGender(Gender.male).build();
    MobileClinicDonorDTO secondDonor = aMobileClinicDonor().withId(7L).withGender(Gender.male).build();

    List<MobileClinicDonorDTO> donors = Arrays.asList(firstDonor, secondDonor);

    MobileClinicExportDonorViewModel expectedMobileClinicExportDonorViewModel1 = aMobileClinicExportDonorViewModel()
        .withId(1L).withBloodType("").withBirthDate("").withGender(Gender.male.name()).thatIsEligible().build();

    MobileClinicExportDonorViewModel expectedMobileClinicExportDonorViewModel2 = aMobileClinicExportDonorViewModel()
        .withId(7L).withBloodType("").withBirthDate("").withGender(Gender.male.name()).thatIsEligible().build();

    Date clinicDate = new Date();

    when(donorConstraintChecker.isDonorEligibleToDonateOnDate(firstDonor.getId(), clinicDate)).thenReturn(true);
    when(donorConstraintChecker.isDonorEligibleToDonateOnDate(secondDonor.getId(), clinicDate)).thenReturn(true);

    // Exercise SUT
    List<MobileClinicExportDonorViewModel> returnedViewModels =
        mobileClinicDonorModelFactory.createMobileClinicExportDonorViewModels(donors, clinicDate);

    // Verify
    Assert.assertThat(returnedViewModels.get(0),
        hasSameStateAsMobileClinicExportDonorViewModel(expectedMobileClinicExportDonorViewModel1));
    Assert.assertThat(returnedViewModels.get(1),
        hasSameStateAsMobileClinicExportDonorViewModel(expectedMobileClinicExportDonorViewModel2));
  }
}
