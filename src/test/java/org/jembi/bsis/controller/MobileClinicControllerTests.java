package org.jembi.bsis.controller;

import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jembi.bsis.controller.MobileClinicController;
import org.jembi.bsis.dto.MobileClinicDonorDTO;
import org.jembi.bsis.factory.MobileClinicDonorViewModelFactory;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.helpers.builders.MobileClinicDonorBuilder;
import org.jembi.bsis.model.donor.DonorStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.location.LocationType;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.viewmodel.MobileClinicLookUpDonorViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class MobileClinicControllerTests {

  @InjectMocks
  private MobileClinicController mobileClinicController;
  @Mock
  private LocationRepository locationRepository;
  @Mock
  private DonorRepository donorRepository;
  @Mock
  private MobileClinicDonorViewModelFactory mobileClinicDonorViewModelFactory;

  @Test
  public void testMobileClinicLookUpFormGenerator() {

    List<Location> venues = new ArrayList<>();
    Location venue1 = LocationBuilder.aLocation().withId(1L).withName("test").build();
    venues.add(venue1);

    when(locationRepository.getLocationsByType(LocationType.VENUE)).thenReturn(venues);

    Map<String, Object> map = mobileClinicController.mobileClinicLookUpFormGenerator();
    Assert.assertNotNull("map is returned", map);
    Object venuesValue = map.get("venues");
    Assert.assertNotNull("map has venues", venuesValue);
    Assert.assertEquals("venues are correct", venues, venuesValue);
  }

  @Test
  public void testMobileClinicLookUp() throws Exception {
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

    when(donorRepository.findMobileClinicDonorsByVenue(1L)).thenReturn(clinicDonorDTOs);
    when(mobileClinicDonorViewModelFactory.createMobileClinicDonorViewModels(clinicDonorDTOs, clinicDate)).thenReturn(clinicDonorsViewModels);

    ResponseEntity<Map<String, Object>> response = mobileClinicController.mobileClinicLookUp(1L, clinicDate);
    Map<String, Object> map = response.getBody();
    Assert.assertNotNull("map is returned", map);
    Object donorsValue = map.get("donors");
    Assert.assertNotNull("map has donors", donorsValue);
    Assert.assertEquals("donors are correct", clinicDonorsViewModels, donorsValue);
  }
}
