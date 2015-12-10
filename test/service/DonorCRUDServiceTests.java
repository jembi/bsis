package service;

import model.donor.Donor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import repository.DonorRepository;

import static helpers.builders.DonorBuilder.aDonor;
import static helpers.matchers.DonorMatcher.hasSameStateAsDonor;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DonorCRUDServiceTests {

  private static final Long IRRELEVANT_DONOR_ID = 99L;

  @InjectMocks
  private DonorCRUDService donorCRUDService;
  @Mock
  private DonorRepository donorRepository;
  @Mock
  private DonorConstraintChecker donorConstraintChecker;

  @Test(expected = IllegalStateException.class)
  public void testDeleteDonorWithDonorWithConstraints_shouldThrow() {

    when(donorConstraintChecker.canDeleteDonor(IRRELEVANT_DONOR_ID)).thenReturn(false);

    donorCRUDService.deleteDonor(IRRELEVANT_DONOR_ID);

    verify(donorConstraintChecker).canDeleteDonor(IRRELEVANT_DONOR_ID);
    verifyNoMoreInteractions(donorConstraintChecker, donorRepository);
  }

  @Test
  public void testDeleteDonor_shouldSoftDeleteDonor() {

    Donor existingDonor = aDonor().withNotes("").build();
    Donor expectedDonor = aDonor().thatIsDeleted().withNotes("").build();

    when(donorConstraintChecker.canDeleteDonor(IRRELEVANT_DONOR_ID)).thenReturn(true);
    when(donorRepository.findDonorById(IRRELEVANT_DONOR_ID)).thenReturn(existingDonor);

    donorCRUDService.deleteDonor(IRRELEVANT_DONOR_ID);

    verify(donorConstraintChecker).canDeleteDonor(IRRELEVANT_DONOR_ID);
    verify(donorRepository).findDonorById(IRRELEVANT_DONOR_ID);
    verify(donorRepository).updateDonor(argThat(hasSameStateAsDonor(expectedDonor)));
    verifyNoMoreInteractions(donorConstraintChecker, donorRepository);
  }

}
