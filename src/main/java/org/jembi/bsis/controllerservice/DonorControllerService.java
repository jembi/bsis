package org.jembi.bsis.controllerservice;

import java.util.List;

import org.jembi.bsis.factory.DonationFactory;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.viewmodel.DonationViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DonorControllerService {
  
  @Autowired
  private DonorRepository donorRepository;
  @Autowired
  private DonationFactory donationFactory;
  
  public List<DonationViewModel> findDonationsForDonor(long donorId) {
    Donor donor = donorRepository.findDonorById(donorId);
    return donationFactory.createDonationViewModelsWithPermissions(donor.getDonations());
  }

}
