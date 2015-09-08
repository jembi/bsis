package factory;

import java.util.ArrayList;
import java.util.List;

import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import viewmodel.DonationBatchViewModel;
import viewmodel.DonationViewModel;
import viewmodel.LocationViewModel;

@Service
public class DonationBatchViewModelFactory {
    
    @Autowired
    private DonationViewModelFactory donationViewModelFactory;

    public DonationBatchViewModel createDonationBatchViewModel(DonationBatch donationBatch) {
        DonationBatchViewModel donationBatchViewModel = new DonationBatchViewModel();
        donationBatchViewModel.setId(donationBatch.getId());
        donationBatchViewModel.setBatchNumber(donationBatch.getBatchNumber());
        donationBatchViewModel.setIsClosed(donationBatch.getIsClosed());
        donationBatchViewModel.setDonorPanel(new LocationViewModel(donationBatch.getDonorPanel()));
        donationBatchViewModel.setNotes(donationBatch.getNotes());

        // Audit fields
        User createdBy = donationBatch.getCreatedBy();
        User lastUpdatedBy = donationBatch.getLastUpdatedBy();
        donationBatchViewModel.setCreatedDate(donationBatch.getCreatedDate());
        donationBatchViewModel.setCreatedBy(createdBy == null ? "" : createdBy.getUsername());
        donationBatchViewModel.setUpdatedDate(donationBatch.getLastUpdated());
        donationBatchViewModel.setLastUpdatedBy(lastUpdatedBy == null ? "" : lastUpdatedBy.getUsername());
        
        // Add all donations
        List<DonationViewModel> donationViewModels = new ArrayList<>();
        if (donationBatch.getDonations() != null) {
            for (Donation donation : donationBatch.getDonations()) {
                donationViewModels.add(donationViewModelFactory.createDonationViewModelWithPermissions(donation));
            }
        }
        donationBatchViewModel.setDonations(donationViewModels);

        return donationBatchViewModel;
    }

}
