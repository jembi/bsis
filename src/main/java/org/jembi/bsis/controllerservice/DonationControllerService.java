package org.jembi.bsis.controllerservice;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jembi.bsis.backingform.DonationBackingForm;
import org.jembi.bsis.factory.DonationFactory;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.service.DonationCRUDService;
import org.jembi.bsis.viewmodel.DonationFullViewModel;
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
    if (donation.getDonationBatch() == null || donation.getTestBatch() == null) {
      return null;
    }
    return donation.getTestBatch().getStatus();
  }

  public DonationViewModel findByDIN(String din) {
    Donation donation = donationRepository.findDonationByDonationIdentificationNumber(din);
    return donationFactory.createDonationViewModel(donation);
  }

  public Collection<DonationViewModel> findByVenueAndPackTypeInRange(UUID venueId, UUID packTypeId, Date startDate,
                                                                     Date endDate) {
    List<Donation> donations = donationRepository.findByVenueAndPackTypeInRange(venueId, packTypeId, startDate,
        endDate);
    return donations.stream()
        .map(donation -> donationFactory.createDonationViewModel(donation))
        .collect(Collectors.toList());
  }
}
