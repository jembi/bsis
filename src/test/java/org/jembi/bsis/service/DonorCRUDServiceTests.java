package org.jembi.bsis.service;

import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.matchers.DonorMatcher.hasSameStateAsDonor;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.service.DonorCRUDService;
import org.jembi.bsis.service.DonorConstraintChecker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DonorCRUDServiceTests {

  @InjectMocks
  private DonorCRUDService donorCRUDService;
  @Mock
  private DonorRepository donorRepository;
  @Mock
  private DonorConstraintChecker donorConstraintChecker;

  @Test(expected = IllegalStateException.class)
  public void testDeleteDonorWithDonorWithConstraints_shouldThrow() {
    UUID irrelavantDonorId = UUID.randomUUID();

    when(donorConstraintChecker.canDeleteDonor(irrelavantDonorId)).thenReturn(false);

    donorCRUDService.deleteDonor(irrelavantDonorId);

    verify(donorConstraintChecker).canDeleteDonor(irrelavantDonorId);
    verifyNoMoreInteractions(donorConstraintChecker, donorRepository);
  }

  @Test
  public void testDeleteDonor_shouldSoftDeleteDonor() {

    UUID irrelaventDonorId = UUID.randomUUID();
    Donor existingDonor = aDonor().withNotes("").build();
    Donor expectedDonor = aDonor().thatIsDeleted().withNotes("").build();

    when(donorConstraintChecker.canDeleteDonor(irrelaventDonorId)).thenReturn(true);
    when(donorRepository.findDonorById(irrelaventDonorId)).thenReturn(existingDonor);

    donorCRUDService.deleteDonor(irrelaventDonorId);

    verify(donorConstraintChecker).canDeleteDonor(irrelaventDonorId);
    verify(donorRepository).findDonorById(irrelaventDonorId);
    verify(donorRepository).updateDonor(argThat(hasSameStateAsDonor(expectedDonor)));
    verifyNoMoreInteractions(donorConstraintChecker, donorRepository);
  }

}
