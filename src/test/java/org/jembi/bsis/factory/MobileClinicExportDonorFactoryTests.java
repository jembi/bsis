package org.jembi.bsis.factory;

import static org.jembi.bsis.helpers.builders.LocationViewModelBuilder.aLocationViewModel;
import static org.jembi.bsis.helpers.builders.MobileClinicDonorBuilder.aMobileClinicDonor;
import static org.jembi.bsis.helpers.builders.MobileClinicExportDonorViewModelBuilder.aMobileClinicExportDonorViewModel;
import static org.jembi.bsis.helpers.matchers.MobileClinicExportViewModelMatcher.hasSameStateAsMobileClinicExportDonorViewModel;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.dto.MobileClinicDonorDTO;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.model.donor.DonorStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.service.DonorConstraintChecker;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.MobileClinicExportDonorViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class MobileClinicExportDonorFactoryTests extends UnitTestSuite {
  
  @InjectMocks
  private MobileClinicExportDonorFactory mobileClinicExportDonorFactory;
  @Mock
  private LocationFactory locationFactory;
  @Mock
  private DonorConstraintChecker donorConstraintChecker;

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
    String venueName="testVenue";
    Location venue = LocationBuilder.aLocation().withName(venueName).build();
     
    MobileClinicDonorDTO mobileClinicDonor = 
        aMobileClinicDonor()
          .withId(id)
          .withDonorNumber(donorNumber)
          .withFirstName(firstName)
          .withLastName(lastName)
          .withGender(Gender.female)
          .withBloodAbo(bloodAbo)
          .withBloodRh(bloodRh)
          .withBirthDate(birthDate)
          .withVenue(venue)
          .withDonorStatus(DonorStatus.NORMAL)
          .thatIsNotDeleted()
          .build();
    
    LocationViewModel venueViewModel = aLocationViewModel().withName(venueName).build();
    MobileClinicExportDonorViewModel expectedMobileClinicExportDonorViewModel = 
        aMobileClinicExportDonorViewModel()
        .withId(id)
        .withDonorNumber(donorNumber)
        .withFirstName(firstName)
        .withLastName(lastName)
        .withGender(Gender.female.name())
        .withBloodType(bloodAbo + bloodRh)
        .withBirthDate(birthDateString)
        .withVenue(venueViewModel)
        .withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted()
        .thatIsEligible()
        .build();

    Date mobileClinicDate = new Date();

    when(donorConstraintChecker.isDonorEligibleToDonateOnDate(mobileClinicDonor.getId(), mobileClinicDate))
        .thenReturn(true);
    when(locationFactory.createViewModel(venue)).thenReturn(venueViewModel);

    MobileClinicExportDonorViewModel returnedViewModel =
        mobileClinicExportDonorFactory.createMobileClinicExportDonorViewModel(mobileClinicDonor, mobileClinicDate);

    Assert.assertThat(returnedViewModel,
        hasSameStateAsMobileClinicExportDonorViewModel(expectedMobileClinicExportDonorViewModel));
  }

  @Test
  public void testCreateMobileClinicDonorExportViewModels_shouldReturnExpectedViewModels() {
    // Set up fixture
    MobileClinicDonorDTO firstDonor = aMobileClinicDonor().withId(1L).withGender(Gender.male).build();
    MobileClinicDonorDTO secondDonor = aMobileClinicDonor().withId(7L).withGender(Gender.male).build();
    
    List<MobileClinicDonorDTO> donors = Arrays.asList(firstDonor, secondDonor);
    
    MobileClinicExportDonorViewModel expectedMobileClinicExportDonorViewModel1 = 
        aMobileClinicExportDonorViewModel()
        .withId(1L)
        .withBloodType("")
        .withBirthDate("")
        .withGender(Gender.male.name())
        .thatIsEligible()
        .build();
    
    MobileClinicExportDonorViewModel expectedMobileClinicExportDonorViewModel2 = 
        aMobileClinicExportDonorViewModel()
        .withId(7L)
        .withBloodType("")
        .withBirthDate("")
        .withGender(Gender.male.name())
        .thatIsEligible()
        .build();

    Date clinicDate = new Date();

    when(donorConstraintChecker.isDonorEligibleToDonateOnDate(firstDonor.getId(), clinicDate))
        .thenReturn(true);
    when(donorConstraintChecker.isDonorEligibleToDonateOnDate(secondDonor.getId(), clinicDate))
        .thenReturn(true);

    // Exercise SUT
    List<MobileClinicExportDonorViewModel> returnedViewModels = mobileClinicExportDonorFactory.createMobileClinicExportDonorViewModels(donors, clinicDate);
    
    // Verify
    Assert.assertThat(returnedViewModels.get(0),
        hasSameStateAsMobileClinicExportDonorViewModel(expectedMobileClinicExportDonorViewModel1));
    Assert.assertThat(returnedViewModels.get(1),
        hasSameStateAsMobileClinicExportDonorViewModel(expectedMobileClinicExportDonorViewModel2));
  }
}