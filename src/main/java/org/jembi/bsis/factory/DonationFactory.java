package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jembi.bsis.backingform.DonationBackingForm;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.repository.PackTypeRepository;
import org.jembi.bsis.service.DonationConstraintChecker;
import org.jembi.bsis.service.DonorConstraintChecker;
import org.jembi.bsis.viewmodel.AdverseEventViewModel;
import org.jembi.bsis.viewmodel.DonationFullViewModel;
import org.jembi.bsis.viewmodel.DonationViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DonationFactory {

  @Autowired
  private DonationConstraintChecker donationConstraintChecker;
  @Autowired
  private AdverseEventFactory adverseEventFactory;
  @Autowired
  private DonorConstraintChecker donorConstraintChecker;
  @Autowired
  private LocationFactory locationFactory;
  @Autowired
  private PackTypeFactory packTypeFactory;
  @Autowired
  private DonorRepository donorRepository;
  @Autowired
  private PackTypeRepository packTypeRepository;
  @Autowired
  private DonationTypeFactory donationTypeFactory;

  public Donation createEntity(DonationBackingForm form) {
    Donation donation = form.getDonation();
    donation.setDonor(donorRepository.findDonorById(donation.getDonor().getId()));
    donation.setPackType(packTypeRepository.getPackTypeById(donation.getPackType().getId()));
    donation.setAdverseEvent(adverseEventFactory.createEntity(form.getAdverseEvent()));
    return donation;
  }

  public List<DonationViewModel> createDonationViewModels(Collection<Donation> donations) {
    List<DonationViewModel> viewModels = new ArrayList<>();
    for (Donation donation : donations) {
      viewModels.add(createDonationViewModel(donation));
    }
    return viewModels;
  }

  public DonationViewModel createDonationViewModel(Donation donation) {
    DonationViewModel donationViewModel = new DonationViewModel();
    donationViewModel.setId(donation.getId());
    donationViewModel.setDonorNumber(donation.getDonorNumber());
    donationViewModel.setDonationIdentificationNumber(donation.getDonationIdentificationNumber());
    donationViewModel.setPackType(packTypeFactory.createViewModel(donation.getPackType()));
    donationViewModel.setDonationType(donationTypeFactory.createViewModel(donation.getDonationType()));
    donationViewModel.setDonationDate(donation.getDonationDate());
    donationViewModel.setVenue(locationFactory.createViewModel(donation.getVenue()));
    donationViewModel.setReleased(donation.isReleased());
    donationViewModel.setTTIStatus(donation.getTTIStatus());
    donationViewModel.setBloodTypingMatchStatus(donation.getBloodTypingMatchStatus());
    donationViewModel.setBloodTypingStatus(donation.getBloodTypingStatus());
    donationViewModel.setBloodAbo(donation.getBloodAbo());
    donationViewModel.setBloodRh(donation.getBloodRh());
    return donationViewModel;
  }

  public List<DonationFullViewModel> createDonationFullViewModelsWithPermissions(List<Donation> donations) {
    List<DonationFullViewModel> donationFullViewModels = new ArrayList<>();
    for (Donation donation : donations) {
      donationFullViewModels.add(createDonationFullViewModelWithPermissions(donation));
    }
    return donationFullViewModels;
  }

  public List<DonationFullViewModel> createDonationFullViewModelsWithoutPermissions(List<Donation> donations) {
    List<DonationFullViewModel> donationFullViewModels = new ArrayList<>();
    for (Donation donation : donations) {
      donationFullViewModels.add(createDonationFullViewModelWithoutPermissions(donation));
    }
    return donationFullViewModels;
  }

  public DonationFullViewModel createDonationFullViewModelWithPermissions(Donation donation) {
    DonationFullViewModel donationFullViewModel = createDonationFullViewModelWithoutPermissions(donation);

    boolean canDonate = !donorConstraintChecker.isDonorDeferred(donation.getDonor().getId());
    boolean isBackEntry = donation.getDonationBatch().isBackEntry();

    // Populate permissions
    Map<String, Boolean> permissions = new HashMap<>();
    permissions.put("canDelete", donationConstraintChecker.canDeleteDonation(donation.getId()));
    permissions.put("canEditBleedTimes", donationConstraintChecker.canEditBleedTimes(donation.getId()));
    permissions.put("canDonate", canDonate);
    permissions.put("canEditPackType", donationConstraintChecker.canEditPackType(donation));
    permissions.put("isBackEntry", isBackEntry);
    donationFullViewModel.setPermissions(permissions);

    return donationFullViewModel;
  }

  public DonationFullViewModel createDonationFullViewModelWithoutPermissions(Donation donation) {
    DonationFullViewModel donationFullViewModel = new DonationFullViewModel();
    donationFullViewModel.setId(donation.getId());
    donationFullViewModel.setDonationDate(donation.getDonationDate());
    donationFullViewModel.setDonationIdentificationNumber(donation.getDonationIdentificationNumber());
    donationFullViewModel.setDonationType(donationTypeFactory.createViewModel(donation.getDonationType()));
    donationFullViewModel.setPackType(packTypeFactory.createFullViewModel(donation.getPackType()));
    donationFullViewModel.setNotes(donation.getNotes());
    donationFullViewModel.setDonorNumber(donation.getDonorNumber());
    donationFullViewModel.setLastUpdated(donation.getLastUpdated());
    donationFullViewModel.setCreatedDate(donation.getCreatedDate());
    donationFullViewModel.setTTIStatus(donation.getTTIStatus());
    donationFullViewModel.setDonationBatchNumber(donation.getDonationBatchNumber());
    donationFullViewModel.setBloodTypingStatus(donation.getBloodTypingStatus());
    donationFullViewModel.setBloodTypingMatchStatus(donation.getBloodTypingMatchStatus());
    donationFullViewModel.setBloodAbo(donation.getBloodAbo());
    donationFullViewModel.setBloodRh(donation.getBloodRh());
    donationFullViewModel.setHaemoglobinCount(donation.getHaemoglobinCount());
    donationFullViewModel.setHaemoglobinLevel(donation.getHaemoglobinLevel());
    donationFullViewModel.setDonorWeight(donation.getDonorWeight());
    donationFullViewModel.setDonorPulse(donation.getDonorPulse());
    donationFullViewModel.setBloodPressureSystolic(donation.getBloodPressureSystolic());
    donationFullViewModel.setBloodPressureDiastolic(donation.getBloodPressureDiastolic());
    donationFullViewModel.setBleedStartTime(donation.getBleedStartTime());
    donationFullViewModel.setBleedEndTime(donation.getBleedEndTime());
    donationFullViewModel.setVenue(locationFactory.createViewModel(donation.getVenue()));
    donationFullViewModel.setReleased(donation.isReleased());

    if (donation.getAdverseEvent() != null) {
      AdverseEventViewModel adverseEventViewModel =
          adverseEventFactory.createAdverseEventViewModel(donation.getAdverseEvent());
      donationFullViewModel.setAdverseEvent(adverseEventViewModel);
    }

    return donationFullViewModel;
  }

}
