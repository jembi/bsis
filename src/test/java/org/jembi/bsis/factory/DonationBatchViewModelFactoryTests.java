package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonationViewModelBuilder.aDonationViewModel;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.matchers.DonationViewModelMatcher.hasSameStateAsDonationViewModel;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jembi.bsis.factory.DonationBatchViewModelFactory;
import org.jembi.bsis.factory.DonationViewModelFactory;
import org.jembi.bsis.helpers.builders.DonationBuilder;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.service.DonationBatchConstraintChecker;
import org.jembi.bsis.viewmodel.DonationBatchFullViewModel;
import org.jembi.bsis.viewmodel.DonationBatchViewModel;
import org.jembi.bsis.viewmodel.DonationViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DonationBatchViewModelFactoryTests {

  @InjectMocks
  private DonationBatchViewModelFactory donationBatchViewModelFactory;
  @Mock
  private DonationBatchConstraintChecker donationBatchConstraintChecker;
  @Mock
  private DonationViewModelFactory donationViewModelFactory;


  @Test
  public void testCreateDonationBatchBasicViewModel() {
    // set up test data
    Long donationBatchId = new Long(1);
    String batchNumber = "BATCH123";
    Location venue = LocationBuilder.aLocation().withName("Venue").build();
    List<Donation> donations = new ArrayList<>();
    donations.add(DonationBuilder.aDonation().build());
    DonationBatch donationBatch = aDonationBatch()
        .withId(donationBatchId)
        .withBatchNumber(batchNumber)
        .withVenue(venue)
        .withDonations(donations)
        .thatIsClosed()
        .build();
    
    // run tests
    DonationBatchViewModel viewModel = donationBatchViewModelFactory.createDonationBatchBasicViewModel(donationBatch);

    // do asserts
    Assert.assertNotNull("view model was created", viewModel);
    Assert.assertEquals("id was set", donationBatchId, viewModel.getId());
    Assert.assertEquals("numDonations was set", Integer.valueOf(1), viewModel.getNumDonations());
    Assert.assertEquals("batchNumber was set", batchNumber, viewModel.getBatchNumber());
    Assert.assertEquals("venue was set", venue.getName(), viewModel.getVenue().getName());
    Assert.assertEquals("closed is set", true, viewModel.getIsClosed());
    

  }
  
  @Test
  public void testCreateDonationBatchBasicViewModels() {
    // set up test data
    List<DonationBatch> donationBatches = new ArrayList<DonationBatch>();
    donationBatches.add(aDonationBatch().withId(1L).withDonation(DonationBuilder.aDonation().build()).build());
    donationBatches.add(aDonationBatch().withId(2L).withDonation(DonationBuilder.aDonation().build()).build());

    // run tests
    List<DonationBatchViewModel> viewModels = donationBatchViewModelFactory.createDonationBatchBasicViewModels(donationBatches);

    // do asserts
    Assert.assertNotNull("view models were created", viewModels);
    Assert.assertEquals("correct number of view models created", 2, viewModels.size());
  }

  @Test
  public void testCreateDonationBatchFullViewModel() {
    // set up test data
    Long donationId = new Long(1);
    Donation donation = aDonation().withId(donationId).build();
    Long donationBatchId = new Long(1);
    DonationBatch donationBatch = aDonationBatch().withId(donationBatchId).withDonation(donation).build();

    // expected data
    DonationViewModel expectedDonationViewModel = aDonationViewModel()
        .withId(donationId)
        .build();

    // set up mocks
    when(donationViewModelFactory.createDonationViewModelWithoutPermissions(donation)).thenReturn(expectedDonationViewModel);
    when(donationBatchConstraintChecker.canDeleteDonationBatch(donationBatchId)).thenReturn(false);
    when(donationBatchConstraintChecker.canCloseDonationBatch(donationBatchId)).thenReturn(false);
    when(donationBatchConstraintChecker.canReopenDonationBatch(donationBatchId)).thenReturn(false);
    when(donationBatchConstraintChecker.canEditDonationBatch(donationBatchId)).thenReturn(false);
    when(donationBatchConstraintChecker.canEditDonationBatchDate(donationBatchId))
        .thenReturn(false);

    // run tests
    DonationBatchFullViewModel returnedDonationBatchViewModel =
        donationBatchViewModelFactory.createDonationBatchFullViewModel(donationBatch);

    // do asserts
    Assert.assertNotNull("view model was created", returnedDonationBatchViewModel);
    Assert.assertNotNull("donations were created", returnedDonationBatchViewModel.getDonations());
    Assert.assertEquals("donations correct", 1, returnedDonationBatchViewModel.getDonations().size());
    assertThat(returnedDonationBatchViewModel.getDonations().get(0), hasSameStateAsDonationViewModel(expectedDonationViewModel));

    // assert permissions
    Assert.assertNotNull("permissions were created", returnedDonationBatchViewModel.getPermissions());
    Assert.assertEquals("permissions correct", 5, returnedDonationBatchViewModel.getPermissions().size());
    assertPermissionEquals(returnedDonationBatchViewModel.getPermissions(), "canDelete", false);
    assertPermissionEquals(returnedDonationBatchViewModel.getPermissions(), "canClose", false);
    assertPermissionEquals(returnedDonationBatchViewModel.getPermissions(), "canReopen", false);
    assertPermissionEquals(returnedDonationBatchViewModel.getPermissions(), "canEdit", false);
    assertPermissionEquals(returnedDonationBatchViewModel.getPermissions(), "canEditDate", false);
  }

  @Test
  public void testCreateDonationBatchFullViewModelExcludingDonationsWithoutTestSamples() {
    // set up test data
    Long donation1Id = new Long(1);
    Donation donation1 = aDonation().withId(donation1Id)
        .withPackType(aPackType().withTestSampleProduced(true).build())
        .build();
    Long donation2Id = new Long(2);
    Donation donation2 = aDonation().withId(donation2Id)
        .withPackType(aPackType().withTestSampleProduced(false).build())
        .build();
    List<Donation> donations = Arrays.asList(donation1, donation2);

    Long donationBatchId = new Long(1);
    DonationBatch donationBatch = aDonationBatch().withId(donationBatchId).withDonations(donations).build();

    // expected data (no permissions)
    DonationViewModel expectedDonation1ViewModel = aDonationViewModel()
        .withId(donation1Id)
        .build();

    // set up mocks
    when(donationViewModelFactory.createDonationViewModelWithoutPermissions(donation1)).thenReturn(expectedDonation1ViewModel);

    // run tests
    DonationBatchFullViewModel returnedDonationBatchViewModel =
        donationBatchViewModelFactory.createDonationBatchViewModelWithTestSamples(donationBatch);

    // do asserts
    Assert.assertNotNull("view model was created", returnedDonationBatchViewModel);
    Assert.assertNotNull("donations were created", returnedDonationBatchViewModel.getDonations());
    Assert.assertEquals("donations correct", 1, returnedDonationBatchViewModel.getDonations().size());
    assertThat(returnedDonationBatchViewModel.getDonations().get(0), hasSameStateAsDonationViewModel(expectedDonation1ViewModel));
  }

  private void assertPermissionEquals(Map<String, Boolean> permissions, String permissionKey, Boolean permissionValue) {
    Boolean permission = permissions.get(permissionKey);
    Assert.assertNotNull("Permission '" + permissionKey + "' exists", permission);
    Assert.assertEquals("Permission '" + permissionKey + "' is " + permissionValue, permissionValue, permission);
  }
}