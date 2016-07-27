package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.service.DonationConstraintChecker;
import org.jembi.bsis.service.DonorConstraintChecker;
import org.jembi.bsis.viewmodel.AdverseEventViewModel;
import org.jembi.bsis.viewmodel.DonationTypeViewModel;
import org.jembi.bsis.viewmodel.DonationViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DonationViewModelFactory {

  @Autowired
  private DonationConstraintChecker donationConstraintChecker;
  @Autowired
  private AdverseEventViewModelFactory adverseEventViewModelFactory;
  @Autowired
  private DonorConstraintChecker donorConstraintChecker;
  @Autowired
  private LocationFactory locationFactory;
  @Autowired
  private PackTypeFactory packTypeFactory;

  public List<DonationViewModel> createDonationViewModelsWithPermissions(List<Donation> donations) {
    List<DonationViewModel> donationViewModels = new ArrayList<>();
    for (Donation donation : donations) {
      donationViewModels.add(createDonationViewModelWithPermissions(donation));
    }
    return donationViewModels;
  }

  public List<DonationViewModel> createDonationViewModelsWithoutPermissions(List<Donation> donations) {
    List<DonationViewModel> donationViewModels = new ArrayList<>();
    for (Donation donation : donations) {
      donationViewModels.add(createDonationViewModelWithoutPermissions(donation));
    }
    return donationViewModels;
  }

  public DonationViewModel createDonationViewModelWithPermissions(Donation donation) {
    DonationViewModel donationViewModel = createDonationViewModelWithoutPermissions(donation);

    boolean canDonate = !donorConstraintChecker.isDonorDeferred(donation.getDonor().getId());
    boolean isBackEntry = donation.getDonationBatch().isBackEntry();

    // Populate permissions
    Map<String, Boolean> permissions = new HashMap<>();
    permissions.put("canDelete", donationConstraintChecker.canDeleteDonation(donation.getId()));
    permissions.put("canUpdateDonationFields", donationConstraintChecker.canUpdateDonationFields(donation.getId()));
    permissions.put("canDonate", canDonate);
    permissions.put("isBackEntry", isBackEntry);
    donationViewModel.setPermissions(permissions);

    return donationViewModel;
  }

  public DonationViewModel createDonationViewModelWithoutPermissions(Donation donation) {
    DonationViewModel donationViewModel = new DonationViewModel();
    donationViewModel.setId(donation.getId());
    donationViewModel.setDonationDate(donation.getDonationDate());
    donationViewModel.setDonationIdentificationNumber(donation.getDonationIdentificationNumber());
    // FIXME: Use donation type factory when it exists
    donationViewModel.setDonationType(new DonationTypeViewModel(donation.getDonationType()));
    donationViewModel.setPackType(packTypeFactory.createFullViewModel(donation.getPackType()));
    donationViewModel.setNotes(donation.getNotes());
    donationViewModel.setDonorNumber(donation.getDonorNumber());
    donationViewModel.setLastUpdated(donation.getLastUpdated());
    donationViewModel.setCreatedDate(donation.getCreatedDate());
    donationViewModel.setTTIStatus(donation.getTTIStatus());
    donationViewModel.setDonationBatchNumber(donation.getDonationBatchNumber());
    donationViewModel.setBloodTypingStatus(donation.getBloodTypingStatus());
    donationViewModel.setBloodTypingMatchStatus(donation.getBloodTypingMatchStatus());
    donationViewModel.setBloodAbo(donation.getBloodAbo());
    donationViewModel.setBloodRh(donation.getBloodRh());
    donationViewModel.setHaemoglobinCount(donation.getHaemoglobinCount());
    donationViewModel.setHaemoglobinLevel(donation.getHaemoglobinLevel());
    donationViewModel.setDonorWeight(donation.getDonorWeight());
    donationViewModel.setDonorPulse(donation.getDonorPulse());
    donationViewModel.setBloodPressureSystolic(donation.getBloodPressureSystolic());
    donationViewModel.setBloodPressureDiastolic(donation.getBloodPressureDiastolic());
    donationViewModel.setBleedStartTime(donation.getBleedStartTime());
    donationViewModel.setBleedEndTime(donation.getBleedEndTime());
    donationViewModel.setVenue(locationFactory.createFullViewModel(donation.getVenue()));
    donationViewModel.setReleased(donation.isReleased());

    if (donation.getAdverseEvent() != null) {
      AdverseEventViewModel adverseEventViewModel =
          adverseEventViewModelFactory.createAdverseEventViewModel(donation.getAdverseEvent());
      donationViewModel.setAdverseEvent(adverseEventViewModel);
    }

    return donationViewModel;
  }

}
