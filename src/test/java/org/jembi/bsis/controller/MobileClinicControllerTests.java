package org.jembi.bsis.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jembi.bsis.controllerservice.MobileClinicControllerService;
import org.jembi.bsis.dto.MobileClinicDonorDTO;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.helpers.builders.LocationViewModelBuilder;
import org.jembi.bsis.helpers.builders.MobileClinicDonorBuilder;
import org.jembi.bsis.helpers.builders.MobileClinicExportDonorViewModelBuilder;
import org.jembi.bsis.model.donor.DonorStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.MobileClinicExportDonorViewModel;
import org.jembi.bsis.viewmodel.MobileClinicLookUpDonorViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

public class MobileClinicControllerTests extends UnitTestSuite {

  @InjectMocks
  private MobileClinicController mobileClinicController;
  @Mock
  private MobileClinicControllerService mobileClinicControllerService;

  @Test
  public void testGetMobileClinicFormFields() {

    List<LocationViewModel> venues = new ArrayList<>();
    UUID locationId = UUID.randomUUID();
    LocationViewModel venue1 = LocationViewModelBuilder.aLocationViewModel().withId(locationId).withName("test").build();
    venues.add(venue1);

    when(mobileClinicControllerService.getMobileVenues()).thenReturn(venues);

    Map<String, Object> map = mobileClinicController.getMobileClinicFormFields();
    Assert.assertNotNull("map is returned", map);
    Object venuesValue = map.get("venues");
    Assert.assertNotNull("map has venues", venuesValue);
    Assert.assertEquals("venues are correct", venues, venuesValue);
  }

  @Test
  public void testGetMobileClinicDonors() throws Exception {
    Date clinicDate = new Date();
    Location venue = LocationBuilder.aLocation().withName("test").build();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    MobileClinicDonorDTO donor1 = MobileClinicDonorBuilder.aMobileClinicDonor()
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
        .withDonorNumber("D2")
        .withFirstName("Test")
        .withLastName("DonorTwo")
        .withBirthDate(sdf.parse("02/12/1982"))
        .withGender(Gender.male)
        .withDonorStatus(DonorStatus.NORMAL)
        .withVenue(venue)
        .thatIsNotDeleted()
        .build();
    List<MobileClinicDonorDTO> clinicDonorDTOs = new ArrayList<>();
    clinicDonorDTOs.add(donor1);
    clinicDonorDTOs.add(donor2);

    List<MobileClinicLookUpDonorViewModel> clinicDonorsViewModels = new ArrayList<>();
    clinicDonorsViewModels.add(new MobileClinicLookUpDonorViewModel(donor1));
    clinicDonorsViewModels.add(new MobileClinicLookUpDonorViewModel(donor2));

    UUID location1 = UUID.randomUUID();
    when(mobileClinicControllerService.getMobileClinicDonorsByVenue(location1, clinicDate))
        .thenReturn(clinicDonorsViewModels);

    ResponseEntity<Map<String, Object>> response = mobileClinicController.getMobileClinicDonors(location1, clinicDate);
    Map<String, Object> map = response.getBody();
    Assert.assertNotNull("map is returned", map);
    Object donorsValue = map.get("donors");
    Assert.assertNotNull("map has donors", donorsValue);
    Assert.assertEquals("donors are correct", clinicDonorsViewModels, donorsValue);
  }
  
  @Test
  @SuppressWarnings("unchecked")
  public void testGetMobileClinicDonorsByVenues() throws Exception {
    Date clinicDate = new Date();
    MobileClinicExportDonorViewModel mobileClinicDonorViewModelD1 =
        MobileClinicExportDonorViewModelBuilder.aMobileClinicExportDonorViewModel()
          .withDonorNumber("D1")
          .withFirstName("D1")
          .withLastName("Test")
          .thatIsNotDeleted()
          .build();
      
    MobileClinicExportDonorViewModel mobileClinicDonorViewModelD2 =
        MobileClinicExportDonorViewModelBuilder.aMobileClinicExportDonorViewModel()
          .withDonorNumber("D2")
          .withFirstName("D1")
          .withLastName("Test")
          .thatIsNotDeleted()
          .build();
    List<MobileClinicExportDonorViewModel> clinicDonorsViewModels = new ArrayList<>();
    clinicDonorsViewModels.add(mobileClinicDonorViewModelD1);
    clinicDonorsViewModels.add(mobileClinicDonorViewModelD2);
    Set<UUID> venueIDs = new HashSet<>();
    venueIDs.add(UUID.randomUUID());
    when(mobileClinicControllerService.getMobileClinicDonorsByVenues(venueIDs, clinicDate))
        .thenReturn(clinicDonorsViewModels);
    ResponseEntity<Map<String, Object>> response =
        mobileClinicController.getMobileClinicDonorsByVenues(venueIDs, clinicDate);
    Map<String, Object> map = response.getBody();

    // Verify  
    assertThat((List<MobileClinicExportDonorViewModel>) map.get("donors"), equalTo(clinicDonorsViewModels));
  }
}
