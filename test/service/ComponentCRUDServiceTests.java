package service;

import model.component.ComponentStatus;
import model.donation.Donation;
import model.donor.Donor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import repository.ComponentRepository;

import java.util.Arrays;

import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonorBuilder.aDonor;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ComponentCRUDServiceTests {

  @InjectMocks
  private ComponentCRUDService componentCRUDService;
  @Mock
  private ComponentRepository componentRepository;

  @Test
  public void testMarkComponentsBelongingToDonorAsUnsafe_shouldDelegateToRepositoryWithCorrectParameters() {

    Donor donor = aDonor().build();

    componentCRUDService.markComponentsBelongingToDonorAsUnsafe(donor);

    verify(componentRepository).updateComponentStatusesForDonor(
            Arrays.asList(ComponentStatus.AVAILABLE, ComponentStatus.QUARANTINED), ComponentStatus.UNSAFE, donor);
  }

  @Test
  public void testMarkComponentsBelongingToDonationAsUnsafe_shouldDelegateToRepositoryWithCorrectParameters() {

    Donation donation = aDonation().build();

    componentCRUDService.markComponentsBelongingToDonationAsUnsafe(donation);

    verify(componentRepository).updateComponentStatusForDonation(
            Arrays.asList(ComponentStatus.AVAILABLE, ComponentStatus.QUARANTINED), ComponentStatus.UNSAFE,
            donation);
  }

}
