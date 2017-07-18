package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.service.DonationBatchConstraintChecker;
import org.jembi.bsis.viewmodel.DonationBatchFullViewModel;
import org.jembi.bsis.viewmodel.DonationBatchViewModel;
import org.jembi.bsis.viewmodel.DonationFullViewModel;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DonationBatchViewModelFactory {

  @Autowired
  private DonationFactory donationFactory;

  @Autowired
  private DonationBatchConstraintChecker donationBatchConstraintChecker;

  /**
   * Create a basic view model for the given donation batch.
   *
   * @param donationBatch DonationBatch to convert
   * @return DonationBatchViewModel representation of the given DonationBatch
   */
  public DonationBatchViewModel createDonationBatchBasicViewModel(DonationBatch donationBatch) {
    DonationBatchViewModel donationBatchViewModel = new DonationBatchViewModel();
    populateBasicViewModel(donationBatch, donationBatchViewModel);
    return donationBatchViewModel;
  }
  
  /**
   * Create a list basic view model for the given donation batches.
   *
   * @param donationBatches List of DonationBatches to convert
   * @return List<DonationBatchViewModel> representation of the given DonationBatches
   */
  public List<DonationBatchViewModel> createDonationBatchBasicViewModels(List<DonationBatch> donationBatches) {
    List<DonationBatchViewModel> donationBatchViewModels = new ArrayList<DonationBatchViewModel>();
    if (donationBatches != null) {
      for (DonationBatch donationBatch : donationBatches) {
        donationBatchViewModels.add(createDonationBatchBasicViewModel(donationBatch));
      }
    }
    return donationBatchViewModels;
  }
  
  /**
   * Create a full view model for the given donation batch. Includes donation list, donation batch
   * permissions and donation permissions.
   *
   * @param donationBatch the donation batch
   * @return the donation batch with donations view model
   */
  public DonationBatchFullViewModel createDonationBatchFullViewModel(DonationBatch donationBatch) {
    DonationBatchFullViewModel donationBatchViewModel = new DonationBatchFullViewModel();
    populateFullViewModel(donationBatch, donationBatchViewModel);
    return donationBatchViewModel;
  }

  /**
   * Create a full view model for the given donation batch that excludes donation permissions and
   * donations without test samples.
   *
   * @param donationBatch The donation batch.
   * @return The populated view model.
   */
  public DonationBatchFullViewModel createDonationBatchViewModelWithTestSamples(DonationBatch donationBatch) {
    DonationBatchFullViewModel donationBatchViewModel = new DonationBatchFullViewModel();
    populateBasicViewModel(donationBatch, donationBatchViewModel);
    donationBatchViewModel.setDonations(createDonationFullViewModels(donationBatch, true));
    return donationBatchViewModel;
  }


  /**
   * Populate basic view model.
   *
   * @param donationBatch the donation batch
   * @param donationBatchViewModel the donation batch view model
   * @return the donation batch view model
   */
  private DonationBatchViewModel populateBasicViewModel(DonationBatch donationBatch,
      DonationBatchViewModel donationBatchViewModel) {
    donationBatchViewModel.setId(donationBatch.getId());
    donationBatchViewModel.setBatchNumber(donationBatch.getBatchNumber());
    donationBatchViewModel.setIsClosed(donationBatch.getIsClosed());
    donationBatchViewModel.setVenue(new LocationFullViewModel(donationBatch.getVenue()));
    donationBatchViewModel.setNotes(donationBatch.getNotes());
    donationBatchViewModel.setBackEntry(donationBatch.isBackEntry());
    donationBatchViewModel.setNumDonations(donationBatch.getDonations().size());

    // Audit fields
    User createdBy = donationBatch.getCreatedBy();
    User lastUpdatedBy = donationBatch.getLastUpdatedBy();
    donationBatchViewModel.setCreatedBy(createdBy == null ? "" : createdBy.getUsername());
    donationBatchViewModel.setUpdatedDate(donationBatch.getLastUpdated());
    donationBatchViewModel.setLastUpdatedBy(lastUpdatedBy == null ? "" : lastUpdatedBy.getUsername());
    donationBatchViewModel.setDonationBatchDate(donationBatch.getDonationBatchDate());

    return donationBatchViewModel;
  }
  

  /**
   * Populate full view model.
   *
   * @param donationBatch the donation batch
   * @param donationBatchViewModel the donation batch view model
   * @param withDonationPermissions the with donation permissions
   */
  private void populateFullViewModel(DonationBatch donationBatch, DonationBatchFullViewModel donationBatchViewModel) {

    populateBasicViewModel(donationBatch, donationBatchViewModel);
    
    donationBatchViewModel.setDonations(createDonationFullViewModels(donationBatch));

    Map<String, Boolean> permissions = new HashMap<>();
    permissions.put("canDelete", donationBatchConstraintChecker.canDeleteDonationBatch(donationBatch.getId()));
    permissions.put("canClose", donationBatchConstraintChecker.canCloseDonationBatch(donationBatch.getId()));
    permissions.put("canReopen", donationBatchConstraintChecker.canReopenDonationBatch(donationBatch.getId()));
    permissions.put("canEdit", donationBatchConstraintChecker.canEditDonationBatch(donationBatch.getId()));
    permissions.put("canEditDate", donationBatchConstraintChecker.canEditDonationBatchDate(donationBatch.getId()));
    donationBatchViewModel.setPermissions(permissions);
  }

  private List<DonationFullViewModel> createDonationFullViewModels(DonationBatch donationBatch) {
    return createDonationFullViewModels(donationBatch, false);
  }

  /**
   * Create a list of full view models for the given donation batch. Optionally excludes donations
   * without test samples.
   *
   * @param donationBatch the donation batch
   * @param excludeDonationsWithoutTestSamples the exclude donations without test samples
   * @return the list< donation full view model>
   */
  private List<DonationFullViewModel> createDonationFullViewModels(DonationBatch donationBatch,
      boolean excludeDonationsWithoutTestSamples) {
    List<DonationFullViewModel> donationFullViewModels = new ArrayList<>();
    if (donationBatch.getDonations() != null) {
      for (Donation donation : donationBatch.getDonations()) {
        if (excludeDonationsWithoutTestSamples && !donation.getPackType().getTestSampleProduced()) {
          // This donation did not produce a test sample so skip it
          continue;
        }
        donationFullViewModels.add(donationFactory.createDonationFullViewModelWithoutPermissions(donation));
      }
    }
    return donationFullViewModels;
  }
}
