package org.jembi.bsis.controllerservice;

import org.jembi.bsis.factory.DonationViewModelFactory;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.viewmodel.DonationViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class DonationControllerService {
  
  @Autowired
  private DonationRepository donationRepository;
  @Autowired
  private DonationViewModelFactory donationViewModelFactory;
  
  public DonationViewModel findDonationById(long donationId) {
    Donation donation = donationRepository.findDonationById(donationId);
    return donationViewModelFactory.createDonationViewModelWithPermissions(donation);
  }

}
