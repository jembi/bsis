package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonationFullViewModelBuilder.aDonationFullViewModel;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.matchers.DonationFullViewModelMatcher.hasSameStateAsDonationFullViewModel;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.helpers.builders.DonationBuilder;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.service.DonationBatchConstraintChecker;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.DonationBatchFullViewModel;
import org.jembi.bsis.viewmodel.DonationBatchViewModel;
import org.jembi.bsis.viewmodel.DonationFullViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class DonationBatchViewModelFactoryTests extends UnitTestSuite {

  @InjectMocks
  private DonationBatchViewModelFactory donationBatchViewModelFactory;
  @Mock
  private DonationBatchConstraintChecker donationBatchConstraintChecker;
  @Mock
  private DonationFactory donationFactory;


  @Test
  public void testCreateDonationBatchBasicViewModel() {
    // set up test data
    UUID donationBatchId = UUID.randomUUID();
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
    UUID donationBatchId1 = UUID.randomUUID();
    UUID donationBatchId2 = UUID.randomUUID();
    List<DonationBatch> donationBatches = new ArrayList<DonationBatch>();
    donationBatches.add(aDonationBatch()
        .withId(donationBatchId1).withDonation(DonationBuilder.aDonation().build()).build());
    donationBatches.add(aDonationBatch()
        .withId(donationBatchId2).withDonation(DonationBuilder.aDonation().build()).build());

    // run tests
    List<DonationBatchViewModel> viewModels = donationBatchViewModelFactory.createDonationBatchBasicViewModels(donationBatches);

    // do asserts
    Assert.assertNotNull("view models were created", viewModels);
    Assert.assertEquals("correct number of view models created", 2, viewModels.size());
  }

  @Test
  public void testCreateDonationBatchFullViewModel() {
    // set up test data
    UUID donationId = UUID.randomUUID();
    Donation donation = aDonation().withId(donationId).build();
    UUID donationBatchId = UUID.randomUUID();
    DonationBatch donationBatch = aDonationBatch().withId(donationBatchId).withDonation(donation).build();

    // expected data
    DonationFullViewModel expectedDonationFullViewModel = aDonationFullViewModel()
        .withId(donationId)
        .build();

    // set up mocks
    when(donationFactory.createDonationFullViewModelWithoutPermissions(donation)).thenReturn(expectedDonationFullViewModel);
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
    assertThat(returnedDonationBatchViewModel.getDonations().get(0), hasSameStateAsDonationFullViewModel(expectedDonationFullViewModel));

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
    UUID donation1Id = UUID.randomUUID();
    Donation donation1 = aDonation().withId(donation1Id)
        .withPackType(aPackType().withTestSampleProduced(true).build())
        .build();
    UUID donation2Id = UUID.randomUUID();
    Donation donation2 = aDonation().withId(donation2Id)
        .withPackType(aPackType().withTestSampleProduced(false).build())
        .build();
    List<Donation> donations = Arrays.asList(donation1, donation2);

    UUID donationBatchId = UUID.randomUUID();
    DonationBatch donationBatch = aDonationBatch().withId(donationBatchId).withDonations(donations).build();

    // expected data (no permissions)
    DonationFullViewModel expectedDonationFullViewModel = aDonationFullViewModel()
        .withId(donation1Id)
        .build();

    // set up mocks
    when(donationFactory.createDonationFullViewModelWithoutPermissions(donation1)).thenReturn(expectedDonationFullViewModel);

    // run tests
    DonationBatchFullViewModel returnedDonationBatchViewModel =
        donationBatchViewModelFactory.createDonationBatchViewModelWithTestSamples(donationBatch);

    // do asserts
    Assert.assertNotNull("view model was created", returnedDonationBatchViewModel);
    Assert.assertNotNull("donations were created", returnedDonationBatchViewModel.getDonations());
    Assert.assertEquals("donations correct", 1, returnedDonationBatchViewModel.getDonations().size());
    assertThat(returnedDonationBatchViewModel.getDonations().get(0), hasSameStateAsDonationFullViewModel(expectedDonationFullViewModel));
  }

  private void assertPermissionEquals(Map<String, Boolean> permissions, String permissionKey, Boolean permissionValue) {
    Boolean permission = permissions.get(permissionKey);
    Assert.assertNotNull("Permission '" + permissionKey + "' exists", permission);
    Assert.assertEquals("Permission '" + permissionKey + "' is " + permissionValue, permissionValue, permission);
  }
}
