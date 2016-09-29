package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.matchers.MobileClinicExportViewModelMatcher.hasSameStateAsMobileClinicExportDonorViewModel;
import static org.jembi.bsis.helpers.builders.MobileClinicDonorBuilder.aMobileClinicDonor;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.dto.MobileClinicDonorDTO;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.service.DonorConstraintChecker;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.jembi.bsis.viewmodel.MobileClinicExportDonorViewModel;
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
    MobileClinicDonorDTO firstDonor = aMobileClinicDonor().withId(1L).withFirstName("Moses").withLastName("Mariga").withVenue(venue).build();
    MobileClinicDonorDTO secondDonor = aMobileClinicDonor().withId(7L).withFirstName("Test").withLastName("Donor").withVenue(venue).build();
    List<MobileClinicDonorDTO> donors = Arrays.asList(firstDonor, secondDonor);
    
    // Set up expectations
    when(locationFactory.createFullViewModel(venue)).thenReturn(new LocationFullViewModel(venue));
    when(donorConstraintChecker.isDonorEligibleToDonateOnDate(firstDonor.getId(), clinicDate)).thenReturn(true);
    when(donorConstraintChecker.isDonorEligibleToDonateOnDate(secondDonor.getId(), clinicDate)).thenReturn(true);
    MobileClinicExportDonorViewModel firstDonorViewModel = mobileClinicExportDonorFactory.createMobileClinicExportDonorViewModel(firstDonor, clinicDate);
    MobileClinicExportDonorViewModel secondDonorViewModel = mobileClinicExportDonorFactory.createMobileClinicExportDonorViewModel(secondDonor, clinicDate);
    
    // Exercise SUT
    List<MobileClinicExportDonorViewModel> returnedViewModels = mobileClinicExportDonorFactory.createMobileClinicExportDonorViewModels(donors, clinicDate);
    
    // Verify
    assertThat(returnedViewModels.get(0), hasSameStateAsMobileClinicExportDonorViewModel(firstDonorViewModel));
    assertThat(returnedViewModels.get(1), hasSameStateAsMobileClinicExportDonorViewModel(secondDonorViewModel));
  }
}