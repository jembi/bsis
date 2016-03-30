package factory;

import static helpers.builders.DonorBuilder.aDonor;
import static helpers.builders.DonorViewModelBuilder.aDonorViewModel;
import static helpers.builders.DuplicateDonorDTOBuilder.aDuplicateDonorDTO;
import static helpers.builders.DuplicateDonorViewModelBuilder.aDuplicateDonorViewModel;
import static helpers.matchers.DonorViewModelMatcher.hasSameStateAsDonorViewModel;
import static helpers.matchers.DuplicateDonorViewModelMatcher.hasSameStateAsDuplicateDonorViewModel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.donor.Donor;
import model.util.Gender;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import service.DonorConstraintChecker;
import viewmodel.DonorViewModel;
import viewmodel.DuplicateDonorViewModel;
import dto.DuplicateDonorDTO;

@RunWith(MockitoJUnitRunner.class)
public class DonorViewModelFactoryTests {

  private static final long IRRELEVANT_DONOR_ID = 865;

  @InjectMocks
  private DonorViewModelFactory donorViewModelFactory;
  @Mock
  private DonorConstraintChecker donorConstraintChecker;

  @Test
  public void testCreateDonorViewModelWithPermissions_shouldReturnViewModelWithCorrectDonorAndPermissions() {

    boolean irrelevantCanDeletePermission = true;

    Donor donor = aDonor().withId(IRRELEVANT_DONOR_ID).build();

    DonorViewModel expectedDonorViewModel = aDonorViewModel()
        .withDonor(donor)
        .withPermission("canDelete", irrelevantCanDeletePermission)
        .build();

    when(donorConstraintChecker.canDeleteDonor(IRRELEVANT_DONOR_ID)).thenReturn(irrelevantCanDeletePermission);

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
    Donor donor1 = aDonor().withId(1L).build();
    donors.add(donor1);
    Donor donor2 = aDonor().withId(2L).build();
    donors.add(donor2);
    
    DonorViewModel expectedDonorViewModel1 = aDonorViewModel().withDonor(donor1).build();
    DonorViewModel expectedDonorViewModel2 = aDonorViewModel().withDonor(donor2).build();
    
    List<DonorViewModel> returnedDonorViewModels = donorViewModelFactory.createDonorViewModels(donors);
    
    assertThat(returnedDonorViewModels.get(0), hasSameStateAsDonorViewModel(expectedDonorViewModel1));
    assertThat(returnedDonorViewModels.get(1), hasSameStateAsDonorViewModel(expectedDonorViewModel2));
  }
}
