package org.jembi.bsis.controllerservice;

import java.util.UUID;

import org.jembi.bsis.backingform.DonationBackingForm;
import org.jembi.bsis.factory.DonationFactory;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.service.DonationCRUDService;
import org.jembi.bsis.viewmodel.DonationFullViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class DonationControllerService {
  
  @Autowired
  private DonationRepository donationRepository;
  @Autowired
  private DonationFactory donationFactory;
  @Autowired
  private DonationCRUDService donationCRUDService;
  
  public DonationFullViewModel createDonation(DonationBackingForm backingForm) {
    Donation donation = donationFactory.createEntity(backingForm);
    donation = donationCRUDService.createDonation(donation);
    return donationFactory.createDonationFullViewModelWithPermissions(donation);
  }
  
  public DonationFullViewModel updateDonation(DonationBackingForm backingForm) {
    Donation donation = donationFactory.createEntity(backingForm);
    donation = donationCRUDService.updateDonation(donation);
    return donationFactory.createDonationFullViewModelWithPermissions(donation);
  }
  
  public DonationFullViewModel findDonationById(UUID donationId) {
    Donation donation = donationRepository.findDonationById(donationId);
    return donationFactory.createDonationFullViewModelWithPermissions(donation);
  }
  
  public TestBatchStatus getTestBatchStatusForDonation(UUID donationId) {
    Donation donation = donationRepository.findDonationById(donationId);
    if (donation.getDonationBatch() == null || donation.getDonationBatch().getTestBatch() == null) {
      return null;
    }
    return donation.getDonationBatch().getTestBatch().getStatus();
  }

}
