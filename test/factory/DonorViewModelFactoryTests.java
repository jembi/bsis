package factory;

import static helpers.builders.DonorBuilder.aDonor;
import static helpers.builders.DonorViewModelBuilder.aDonorViewModel;
import static helpers.matchers.DonorViewModelMatcher.hasSameStateAsDonorViewModel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import model.donor.Donor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import service.DonorConstraintChecker;
import viewmodel.DonorViewModel;

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

}
