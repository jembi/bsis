package org.jembi.bsis.factory;

import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.viewmodel.TestSampleViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestSampleFactory {

  @Autowired
  private BloodTestResultFactory bloodTestResultFactory;

  public TestSampleViewModel createViewModel(Donation donation, List<BloodTestResult> testOutcomes) {
    TestSampleViewModel viewModel = new TestSampleViewModel();
    viewModel.setDin(donation.getDonationIdentificationNumber());
    viewModel.setVenue(donation.getVenue().getName());
    viewModel.setDonationDate(donation.getDonationDate());
    viewModel.setBloodGroup(BloodTypingStatus.COMPLETE != donation.getBloodTypingStatus()
        ? "" : donation.getBloodAbo() + donation.getBloodRh());
    viewModel.setPackType(donation.getPackType().getPackType());
    viewModel.setTtiStatus(donation.getTTIStatus());
    viewModel.setBloodTypingStatus(donation.getBloodTypingStatus());
    viewModel.setBloodTypingMatchStatus(donation.getBloodTypingMatchStatus());
    if (donation.getTestBatch() != null) {
      viewModel.setTestingSite(donation.getTestBatch().getLocation().getName());
      viewModel.setTestingDate(donation.getTestBatch().getTestBatchDate());
    }
    viewModel.setTestOutcomes(bloodTestResultFactory.createViewModels(testOutcomes));
    return viewModel;
  }
}
