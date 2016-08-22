package org.jembi.bsis.controllerservice;

import org.jembi.bsis.backingform.DonationBackingForm;
import org.jembi.bsis.factory.DonationViewModelFactory;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.service.DonationCRUDService;
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
  @Autowired
  private DonationCRUDService donationCRUDService;
  
  public DonationViewModel createDonation(DonationBackingForm backingForm) {
    Donation donation = donationCRUDService.createDonation(backingForm);
    return donationViewModelFactory.createDonationViewModelWithPermissions(donation);
  }
  
  public DonationViewModel updateDonation(DonationBackingForm backingForm) {
    Donation donation = donationCRUDService.updateDonation(backingForm.getId(), backingForm);
    return donationViewModelFactory.createDonationViewModelWithPermissions(donation);
  }
  
  public DonationViewModel findDonationById(long donationId) {
    Donation donation = donationRepository.findDonationById(donationId);
    return donationViewModelFactory.createDonationViewModelWithPermissions(donation);
  }

}
