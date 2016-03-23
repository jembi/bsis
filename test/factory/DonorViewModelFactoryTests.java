package factory;

import static helpers.builders.DonorBuilder.aDonor;
import static helpers.builders.DonorViewModelBuilder.aDonorViewModel;
import static helpers.builders.DuplicateDonorDTOBuilder.aDuplicateDonorDTO;
import static helpers.builders.DuplicateDonorViewModelBuilder.aDuplicateDonorViewModel;
import static helpers.matchers.DonorViewModelMatcher.hasSameStateAsDonorViewModel;
import static helpers.matchers.DuplicateDonorViewModelMatcher.hasSameStateAsDuplicateDonorViewModel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import dto.DuplicateDonorDTO;
import model.donor.Donor;
import model.util.Gender;
import service.DonorConstraintChecker;
import viewmodel.DonorViewModel;
import viewmodel.DuplicateDonorViewModel;

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
}
