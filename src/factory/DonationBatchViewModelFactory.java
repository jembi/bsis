package factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.user.User;
import service.DonationBatchConstraintChecker;
import viewmodel.DonationBatchFullViewModel;
import viewmodel.DonationBatchViewModel;
import viewmodel.DonationViewModel;
import viewmodel.LocationViewModel;

@Service
public class DonationBatchViewModelFactory {

  @Autowired
  private DonationViewModelFactory donationViewModelFactory;

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
   * Create a full view model for the given donation batch. Includes donation list, donation batch
   * permissions and donation permissions.
   *
   * @param donationBatch the donation batch
   * @return the donation batch with donations view model
   */
  public DonationBatchFullViewModel createDonationBatchFullViewModel(DonationBatch donationBatch) {
    DonationBatchFullViewModel donationBatchViewModel = new DonationBatchFullViewModel();
    populateFullViewModel(donationBatch, donationBatchViewModel, false, false);
    return donationBatchViewModel;
  }

  /**
   * Create a full view model for the given donation batch that excludes donation permissions, and
   * optionally excludes donations without test samples.
   *
   * @param donationBatch The donation batch.
   * @param excludeDonationsWithoutTestSamples Whether or not to exclude donations without test
   *        samples.
   * @return The populated view model.
   */
  public DonationBatchFullViewModel createDonationBatchFullViewModelWithoutDonationPermissions(
      DonationBatch donationBatch, boolean excludeDonationsWithoutTestSamples) {
    DonationBatchFullViewModel donationBatchViewModel = new DonationBatchFullViewModel();
    populateFullViewModel(donationBatch, donationBatchViewModel, excludeDonationsWithoutTestSamples, true);
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
    donationBatchViewModel.setVenue(new LocationViewModel(donationBatch.getVenue()));
    donationBatchViewModel.setNotes(donationBatch.getNotes());
    donationBatchViewModel.setBackEntry(donationBatch.isBackEntry());
    donationBatchViewModel.setNumDonations(donationBatch.getDonations().size());

    // Audit fields
    User createdBy = donationBatch.getCreatedBy();
    User lastUpdatedBy = donationBatch.getLastUpdatedBy();
    donationBatchViewModel.setCreatedDate(donationBatch.getCreatedDate());
    donationBatchViewModel.setCreatedBy(createdBy == null ? "" : createdBy.getUsername());
    donationBatchViewModel.setUpdatedDate(donationBatch.getLastUpdated());
    donationBatchViewModel.setLastUpdatedBy(lastUpdatedBy == null ? "" : lastUpdatedBy.getUsername());

    return donationBatchViewModel;
  }
  

  /**
   * Populate full view model.
   *
   * @param donationBatch the donation batch
   * @param donationBatchViewModel the donation batch view model
   * @param excludeDonationsWithoutTestSamples the exclude donations without test samples
   * @param withDonationPermissions the with donation permissions
   */
  private void populateFullViewModel(DonationBatch donationBatch, DonationBatchFullViewModel donationBatchViewModel,
      boolean excludeDonationsWithoutTestSamples, boolean excludeDonationPermissions) {

    populateBasicViewModel(donationBatch, donationBatchViewModel);
    
    donationBatchViewModel.setDonations(
        createDonationViewModels(donationBatch, excludeDonationsWithoutTestSamples, excludeDonationPermissions));

    Map<String, Boolean> permissions = new HashMap<>();
    permissions.put("canDelete", donationBatchConstraintChecker.canDeleteDonationBatch(donationBatch.getId()));
    permissions.put("canClose", donationBatchConstraintChecker.canCloseDonationBatch(donationBatch.getId()));
    permissions.put("canReopen", donationBatchConstraintChecker.canReopenDonationBatch(donationBatch.getId()));
    permissions.put("canEdit", donationBatchConstraintChecker.canEditDonationBatch(donationBatch.getId()));
    permissions.put("canEditDate", donationBatchConstraintChecker.canEditDonationBatchDate(donationBatch.getId()));
    donationBatchViewModel.setPermissions(permissions);
  }


  /**
   * Create a list of view models for the given donation batch. Optionally excludes donations
   * without test samples, and donation permissions.
   *
   * @param donationBatch the donation batch
   * @param excludeDonationsWithoutTestSamples the exclude donations without test samples
   * @param withDonationPermissions the with donation permissions
   * @return the list< donation view model>
   */
  private List<DonationViewModel> createDonationViewModels(DonationBatch donationBatch,
      boolean excludeDonationsWithoutTestSamples, boolean excludeDonationPermissions) {
    List<DonationViewModel> donationViewModels = new ArrayList<>();
    if (donationBatch.getDonations() != null) {
      for (Donation donation : donationBatch.getDonations()) {
        if (excludeDonationsWithoutTestSamples && !donation.getPackType().getTestSampleProduced()) {
          // This donation did not produce a test sample so skip it
          continue;
        }
        if (excludeDonationPermissions) {
          donationViewModels.add(donationViewModelFactory.createDonationViewModelWithoutPermissions(donation));
        } else {
          donationViewModels.add(donationViewModelFactory.createDonationViewModelWithPermissions(donation));
        }
      }
    }
    return donationViewModels;
  }
}
