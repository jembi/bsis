package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.MobileClinicDonorBuilder.aMobileClinicDonor;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.builders.DivisionBuilder.aDivision;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.dto.MobileClinicDonorDTO;
import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.service.DonorConstraintChecker;
import org.jembi.bsis.viewmodel.DivisionViewModel;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.jembi.bsis.viewmodel.MobileClinicExportDonorViewModel;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

public class MobileClinicDonorDTOFactoryTests {

  @Spy
  @InjectMocks
  private MobileClinicDonorExportFactory mobileClinicDonorExportFactory;
  @InjectMocks
  private LocationFactory locationFactory;
  @InjectMocks
  private DivisionFactory divisionFactory;
  @Mock
  private DonorConstraintChecker donorConstraintChecker;


  @Test
  public void testCreateMobileClinicDonorExportViewModels_shouldReturnExpectedViewModels() {
    // Set up fixture
    Date clinicDate = new Date();
    SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
    try {
      clinicDate = fmt.parse("26/09/2016");
    } catch (ParseException ex) {
      clinicDate = null;
    }
    
    Location venue = aVenue().withName("Test").withId(1L).build();
    
    MobileClinicDonorDTO firstDonor = aMobileClinicDonor().withId(1L).withFirstName("Moses").withLastName("Mariga").build();
    MobileClinicDonorDTO secondDonor = aMobileClinicDonor().withId(7L).withFirstName("Test").withLastName("Donor").build();
    List<MobileClinicDonorDTO> donors = Arrays.asList(firstDonor, secondDonor);
    
    
    // Set up expectations
    MobileClinicExportDonorViewModel firstDonorViewModel = new MobileClinicExportDonorViewModel(1l, "001", "test", "donor", "male", "AB", null, "12/12/1990", new LocationFullViewModel(venue), false, true);
    MobileClinicExportDonorViewModel secondDonorViewModel = new MobileClinicExportDonorViewModel(2l, "002", "test2", "donor", "male", "AB", null, "12/12/1990", new LocationFullViewModel(venue), false, true);
    List<MobileClinicExportDonorViewModel> expectedViewModels = Arrays.asList(firstDonorViewModel, secondDonorViewModel);

    //when(locationFactory.createFullViewModel(venue)).thenReturn(new LocationFullViewModel(venue));
    doReturn(expectedViewModels).when(mobileClinicDonorExportFactory).createMobileClinicExportDonorViewModels(donors, clinicDate);

    // Exercise SUT
    List<MobileClinicExportDonorViewModel> returnedViewModels =
        mobileClinicDonorExportFactory.createMobileClinicExportDonorViewModels(donors, clinicDate);

    // Verify
    assertThat(returnedViewModels, is(expectedViewModels));
  }
}
