package controllerservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import factory.DonationViewModelFactory;
import model.donor.Donor;
import repository.DonorRepository;
import viewmodel.DonationViewModel;

@Service
@Transactional
public class DonorControllerService {
  
  @Autowired
  private DonorRepository donorRepository;
  @Autowired
  private DonationViewModelFactory donationViewModelFactory;
  
  public List<DonationViewModel> findDonationsForDonor(long donorId) {
    Donor donor = donorRepository.findDonorById(donorId);
    return donationViewModelFactory.createDonationViewModelsWithPermissions(donor.getDonations());
  }

}
