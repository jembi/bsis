package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.DonorSummaryViewModelBuilder.aDonorSummaryViewModel;
import static org.jembi.bsis.helpers.builders.DonorViewModelBuilder.aDonorViewModel;
import static org.jembi.bsis.helpers.builders.DuplicateDonorDTOBuilder.aDuplicateDonorDTO;
import static org.jembi.bsis.helpers.builders.DuplicateDonorViewModelBuilder.aDuplicateDonorViewModel;
import static org.jembi.bsis.helpers.matchers.DonorSummaryViewModelMatcher.hasSameStateAsDonorSummaryViewModel;
import static org.jembi.bsis.helpers.matchers.DonorViewModelMatcher.hasSameStateAsDonorViewModel;
import static org.jembi.bsis.helpers.matchers.DuplicateDonorViewModelMatcher.hasSameStateAsDuplicateDonorViewModel;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.dto.DuplicateDonorDTO;
import org.jembi.bsis.factory.DonorViewModelFactory;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.service.DonorConstraintChecker;
import org.jembi.bsis.viewmodel.DonorSummaryViewModel;
import org.jembi.bsis.viewmodel.DonorViewModel;
import org.jembi.bsis.viewmodel.DuplicateDonorViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DonorViewModelFactoryTests {

  @InjectMocks
  private DonorViewModelFactory donorViewModelFactory;
  @Mock
  private DonorConstraintChecker donorConstraintChecker;

  @Test
  public void testCreateDonorViewModel_shouldReturnViewModelWithCorrectDonor() {
    UUID irrelevantDonorId = UUID.fromString("11e71397-0000-0000-0000-000000000865");
    Donor donor = aDonor().withId(irrelevantDonorId).build();

    DonorViewModel expectedDonorViewModel = aDonorViewModel()
        .withDonor(donor)
        .build();

    DonorViewModel returnedDonorViewModel = donorViewModelFactory.createDonorViewModel(donor);

    assertThat(returnedDonorViewModel, hasSameStateAsDonorViewModel(expectedDonorViewModel));
  }

  @Test
  public void testCreateDonorViewModelWithPermissions_shouldReturnViewModelWithCorrectDonorAndPermissions() {

    boolean irrelevantCanDeletePermission = true;
    UUID irrelevantDonorId = UUID.randomUUID();

    Donor donor = aDonor().withId(irrelevantDonorId).build();

    DonorViewModel expectedDonorViewModel = aDonorViewModel()
        .withDonor(donor)
        .withPermission("canDelete", irrelevantCanDeletePermission)
        .build();

    when(donorConstraintChecker.canDeleteDonor(irrelevantDonorId)).thenReturn(irrelevantCanDeletePermission);

    DonorViewModel returnedDonorViewModel = donorViewModelFactory.createDonorViewModelWithPermissions(donor);

    assertThat(returnedDonorViewModel, hasSameStateAsDonorViewModel(expectedDonorViewModel));
  }
  
  @Test
  public void testCreateNullDonorViewModels_shouldReturnEmptyViewModels() {
    List<DonorViewModel> returnedDonorViewModels = donorViewModelFactory.createDonorViewModels(null);
    Assert.assertNotNull("Doesn't return a null", returnedDonorViewModels);
    assertThat(returnedDonorViewModels.size(), equalTo(0));
  }

  @Test
  public void testCreateDuplicateDonorViewModels_shouldReturnModelWithExpectedValues() {
    
    Date birthDate = new Date();
    
    List<DuplicateDonorViewModel> expectedDupViewModels = new ArrayList<>();
    DuplicateDonorViewModel expectedDupViewModel = aDuplicateDonorViewModel()
        .withFirstName("Tom")
        .withLastName("Lee")
        .withGender(Gender.female)
        .withCount(1l)
        .withBirthDate(birthDate)
        .withGroupKey("1234567")
        .build();
    expectedDupViewModels.add(expectedDupViewModel);

    List<DuplicateDonorDTO> dupDTOs = new ArrayList<>();
    DuplicateDonorDTO dupDTO = aDuplicateDonorDTO()
        .withFirstName("Tom")
        .withLastName("Lee")
        .withGender(Gender.female)
        .withCount(1l)
        .withBirthDate(birthDate)
        .withGroupKey("1234567")
        .build();
    dupDTOs.add(dupDTO);

    List<DuplicateDonorViewModel> returnedDupViewModels = donorViewModelFactory.createDuplicateDonorViewModels(dupDTOs);
    assertThat(returnedDupViewModels.get(0), hasSameStateAsDuplicateDonorViewModel(expectedDupViewModel));
  }

  @Test
  public void testCreateDonorViewModels_shouldReturnExpectedViewModels() {
    List<Donor> donors = new ArrayList<>();
    UUID donorId1 = UUID.randomUUID();
    UUID donorId2 = UUID.randomUUID();
    
    Donor donor1 = aDonor().withId(donorId1).build();
    donors.add(donor1);
    Donor donor2 = aDonor().withId(donorId2).build();
    donors.add(donor2);
    
    DonorViewModel expectedDonorViewModel1 = aDonorViewModel().withDonor(donor1).build();
    DonorViewModel expectedDonorViewModel2 = aDonorViewModel().withDonor(donor2).build();
    
    List<DonorViewModel> returnedDonorViewModels = donorViewModelFactory.createDonorViewModels(donors);
    
    assertThat(returnedDonorViewModels.get(0), hasSameStateAsDonorViewModel(expectedDonorViewModel1));
    assertThat(returnedDonorViewModels.get(1), hasSameStateAsDonorViewModel(expectedDonorViewModel2));
  }

  @Test
  public void testCreateDonorSummaryViewModels_shouldReturnExpectedViewModels() {
    Location venue = LocationBuilder.aVenue().withName("Venue").build();
    List<Donor> donors = new ArrayList<>();
    UUID donorId1 = UUID.randomUUID();
    UUID donorId2 = UUID.randomUUID();
    
    Donor donor1 = aDonor().withId(donorId1).withVenue(venue).build();
    donors.add(donor1);
    Donor donor2 = aDonor().withId(donorId2).withVenue(venue).build();
    donors.add(donor2);

    DonorSummaryViewModel expectedDonorSummaryViewModel1 = aDonorSummaryViewModel()
        .withBirthDate(donor1.getBirthDate())
        .withFirstName(donor1.getFirstName())
        .withGender(donor1.getGender())
        .withId(donor1.getId())
        .withLastName(donor1.getLastName())
        .withVenueName("Venue")
        .build();
    DonorSummaryViewModel expectedDonorSummaryViewModel2 = aDonorSummaryViewModel()
        .withBirthDate(donor2.getBirthDate())
        .withFirstName(donor2.getFirstName())
        .withGender(donor2.getGender())
        .withId(donor2.getId())
        .withLastName(donor2.getLastName())
        .withVenueName("Venue")
        .build();

    List<DonorSummaryViewModel> returnedDonorSummaryViewModels = donorViewModelFactory.createDonorSummaryViewModels(donors);

    assertThat(returnedDonorSummaryViewModels.get(0),
        hasSameStateAsDonorSummaryViewModel(expectedDonorSummaryViewModel1));
    assertThat(returnedDonorSummaryViewModels.get(1),
        hasSameStateAsDonorSummaryViewModel(expectedDonorSummaryViewModel2));
  }
}
