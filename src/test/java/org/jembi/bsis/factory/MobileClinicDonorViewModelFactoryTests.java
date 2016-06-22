package org.jembi.bsis.factory;

import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.dto.MobileClinicDonorDTO;
import org.jembi.bsis.factory.MobileClinicDonorViewModelFactory;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.helpers.builders.MobileClinicDonorBuilder;
import org.jembi.bsis.model.donor.DonorStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.service.DonorConstraintChecker;
import org.jembi.bsis.utils.CustomDateFormatter;
import org.jembi.bsis.viewmodel.MobileClinicLookUpDonorViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MobileClinicDonorViewModelFactoryTests {

  @InjectMocks
  private MobileClinicDonorViewModelFactory mobileClinicDonorModelFactory;
  @Mock
  private DonorConstraintChecker donorConstraintChecker;

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
}
