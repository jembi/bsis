package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.factory.DonationSummaryViewModelFactory;
import org.jembi.bsis.helpers.builders.DonationBatchBuilder;
import org.jembi.bsis.helpers.builders.DonationBuilder;
import org.jembi.bsis.helpers.builders.TestBatchBuilder;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.viewmodel.DonationSummaryViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DonationSummaryViewModelFactoryTests {

  @InjectMocks
  private DonationSummaryViewModelFactory donationSummaryViewModelFactory;

  @Test
  public void testCreateDonationSummaryViewModels_fromTestBatch_onlyAmbiguous() {
    
    Donation d1 = DonationBuilder.aDonation().withBloodTypingMatchStatus(BloodTypingMatchStatus.AMBIGUOUS).build();
    Donation d2 = DonationBuilder.aDonation().withBloodTypingMatchStatus(BloodTypingMatchStatus.RESOLVED).build();
    Donation d3 = DonationBuilder.aDonation().withBloodTypingMatchStatus(BloodTypingMatchStatus.AMBIGUOUS).build();
    Donation d4 = DonationBuilder.aDonation().withBloodTypingMatchStatus(BloodTypingMatchStatus.RESOLVED).build();
    ArrayList<Donation> donations1 = new ArrayList<Donation>();
    donations1.add(d1);
    donations1.add(d2);
    ArrayList<Donation> donations2 = new ArrayList<Donation>();
    donations2.add(d3);
    donations2.add(d4);
    DonationBatch donationBatch1 = DonationBatchBuilder.aDonationBatch().withDonations(donations1).build();
    DonationBatch donationBatch2 = DonationBatchBuilder.aDonationBatch().withDonations(donations2).build();
    TestBatch testBatch =
        TestBatchBuilder.aTestBatch().withDonationBatch(donationBatch1).withDonationBatch(donationBatch2).build();
    List<DonationSummaryViewModel> donationModels =
        donationSummaryViewModelFactory.createDonationSummaryViewModels(testBatch, BloodTypingMatchStatus.AMBIGUOUS);

    Assert.assertTrue("2 ambiguous donations on the test batch", donationModels.size() == 2);

  }

  @Test
  public void testCreateDonationSummaryViewModels_fromDonations() {

    Donation d1 = DonationBuilder.aDonation().withBloodTypingMatchStatus(BloodTypingMatchStatus.AMBIGUOUS).build();
    Donation d2 = DonationBuilder.aDonation().withBloodTypingMatchStatus(BloodTypingMatchStatus.RESOLVED).build();
    Donation d3 = DonationBuilder.aDonation().withBloodTypingMatchStatus(BloodTypingMatchStatus.AMBIGUOUS).build();
    Donation d4 = DonationBuilder.aDonation().withBloodTypingMatchStatus(BloodTypingMatchStatus.RESOLVED).build();
    ArrayList<Donation> donations = new ArrayList<Donation>();
    donations.add(d1);
    donations.add(d2);
    donations.add(d3);
    donations.add(d4);
    List<DonationSummaryViewModel> donationModels =
        donationSummaryViewModelFactory.createFullDonationSummaryViewModels(donations);

    Assert.assertTrue("4 donations", donationModels.size() == 4);
    
  }

}
