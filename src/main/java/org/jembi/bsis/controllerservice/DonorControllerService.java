package org.jembi.bsis.controllerservice;

import java.util.List;
import java.util.UUID;

import org.jembi.bsis.factory.DonationFactory;
import org.jembi.bsis.factory.DonorDeferralFactory;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.donordeferral.DonorDeferral;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.viewmodel.DonationFullViewModel;
import org.jembi.bsis.viewmodel.DonorDeferralViewModel;
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
  @Autowired
  private DonorDeferralFactory donorDeferralFactory;
  
  public List<DonationFullViewModel> findDonationsForDonor(UUID donorId) {
    Donor donor = donorRepository.findDonorById(donorId);
    return donationFactory.createDonationFullViewModelsWithPermissions(donor.getDonations());
  }

  public DonorDeferralViewModel getLastDeferral(UUID donorId) {
    DonorDeferral lastDonorDeferral = donorRepository.getLastDonorDeferral(donorId);
    if (lastDonorDeferral != null) {
      return donorDeferralFactory.createDonorDeferralViewModel(lastDonorDeferral);
    }
    return null;
  }
}