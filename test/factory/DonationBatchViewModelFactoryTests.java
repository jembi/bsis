package factory;

import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonationViewModelBuilder.aDonationViewModel;
import static helpers.builders.PackTypeBuilder.aPackType;
import static helpers.matchers.DonationViewModelMatcher.hasSameStateAsDonationViewModel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import helpers.builders.DonationBatchBuilder;
import helpers.builders.DonationBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import model.donation.Donation;
import model.donationbatch.DonationBatch;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import service.DonationBatchConstraintChecker;
import viewmodel.DonationBatchViewModel;
import viewmodel.DonationViewModel;

@RunWith(MockitoJUnitRunner.class)
public class DonationBatchViewModelFactoryTests {

    @InjectMocks
    private DonationBatchViewModelFactory donationBatchViewModelFactory;
    @Mock
    private DonationBatchConstraintChecker donationBatchConstraintChecker;
    @Mock
    private DonationViewModelFactory donationViewModelFactory;
    
    
    @Test
    public void testCreateDonationBatchViewModel() {
    	// set up test data
    	Long donationBatchId = 1L;
    	DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).build();
    	
    	// set up mocks
    	when(donationBatchConstraintChecker.canDeleteDonationBatch(donationBatchId)).thenReturn(false);
    	when(donationBatchConstraintChecker.canCloseDonationBatch(donationBatchId)).thenReturn(false);
    	when(donationBatchConstraintChecker.canReopenDonationBatch(donationBatchId)).thenReturn(false);
    	when(donationBatchConstraintChecker.canEditDonationBatch(donationBatchId)).thenReturn(false);
    	when(donationBatchConstraintChecker.canEditDonationBatchDate(donationBatchId)).thenReturn(false);
    	
    	// run tests
    	DonationBatchViewModel viewModel = donationBatchViewModelFactory.createDonationBatchViewModel(donationBatch);

    	// do asserts
    	Assert.assertNotNull("view model was created", viewModel);
    	Assert.assertNotNull("permissions were created", viewModel.getPermissions());
    	Assert.assertEquals("permissions correct", 5, viewModel.getPermissions().size());
    	assertPermissionEquals(viewModel.getPermissions(), "canDelete", false);
    	assertPermissionEquals(viewModel.getPermissions(), "canClose", false);
    	assertPermissionEquals(viewModel.getPermissions(), "canReopen", false);
    	assertPermissionEquals(viewModel.getPermissions(), "canEdit", false);
    	assertPermissionEquals(viewModel.getPermissions(), "canEditDate", false);
    }
    
  @Test
  public void testCreateDonationBatchViewModelWithDonations() {
    // set up test data
    Long donationId = new Long(1);
    Donation donation = new DonationBuilder().withId(donationId).build();
    Long donationBatchId = new Long(1);
    DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).withDonation(donation).build();
    
    // expected data
    DonationViewModel expectedDonationViewModel = aDonationViewModel()
        .withDonation(donation)
        .build();

    // set up mocks
    when(donationViewModelFactory.createDonationViewModelWithPermissions(donation)).thenReturn(expectedDonationViewModel);
    when(donationBatchConstraintChecker.canDeleteDonationBatch(donationBatchId)).thenReturn(false);
    when(donationBatchConstraintChecker.canCloseDonationBatch(donationBatchId)).thenReturn(false);
    when(donationBatchConstraintChecker.canReopenDonationBatch(donationBatchId)).thenReturn(false);
    when(donationBatchConstraintChecker.canEditDonationBatch(donationBatchId)).thenReturn(false);
    when(donationBatchConstraintChecker.canEditDonationBatchDate(donationBatchId))
        .thenReturn(false);

    // run tests
    DonationBatchViewModel returnedDonationBatchViewModel = donationBatchViewModelFactory.createDonationBatchViewModel(donationBatch);

    // do asserts
    Assert.assertNotNull("view model was created", returnedDonationBatchViewModel);
    Assert.assertNotNull("donations were created", returnedDonationBatchViewModel.getDonations());
    Assert.assertEquals("donations correct", 1, returnedDonationBatchViewModel.getDonations().size());
    assertThat(returnedDonationBatchViewModel.getDonations().get(0), hasSameStateAsDonationViewModel(expectedDonationViewModel));
  }
  
  @Test
  public void testCreateDonationBatchViewModelWithDonationsThatHaveNoSampleProduced() {
    // set up test data
    Long donation1Id = new Long(1);
    Donation donation1 = aDonation().withId(donation1Id)
        .withPackType(aPackType().withTestSampleProduced(true).build())
        .build();
    Long donation2Id = new Long(2);
    Donation donation2 = aDonation().withId(donation2Id)
        .withPackType(aPackType().withTestSampleProduced(false).build())
        .build();
    List<Donation> donations = Arrays.asList(new Donation[] { donation1, donation2 });
    
    Long donationBatchId = new Long(1);
    DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).withDonations(donations).build();
    
    // expected data
    DonationViewModel expectedDonation1ViewModel = aDonationViewModel()
        .withDonation(donation1)
        .withPermission("canDelete", true)
        .withPermission("canUpdateDonationFields", true)
        .withPermission("canDonate", true)
        .withPermission("isBackEntry", true)
        .build();

    // set up mocks
    when(donationViewModelFactory.createDonationViewModelWithPermissions(donation1)).thenReturn(expectedDonation1ViewModel);
    when(donationBatchConstraintChecker.canDeleteDonationBatch(donationBatchId)).thenReturn(false);
    when(donationBatchConstraintChecker.canCloseDonationBatch(donationBatchId)).thenReturn(false);
    when(donationBatchConstraintChecker.canReopenDonationBatch(donationBatchId)).thenReturn(false);
    when(donationBatchConstraintChecker.canEditDonationBatch(donationBatchId)).thenReturn(false);
    when(donationBatchConstraintChecker.canEditDonationBatchDate(donationBatchId))
        .thenReturn(false);

    // run tests
    DonationBatchViewModel returnedDonationBatchViewModel = donationBatchViewModelFactory.createDonationBatchViewModel(donationBatch, true);

    // do asserts
    Assert.assertNotNull("view model was created", returnedDonationBatchViewModel);
    Assert.assertNotNull("donations were created", returnedDonationBatchViewModel.getDonations());
    Assert.assertEquals("donations correct", 1, returnedDonationBatchViewModel.getDonations().size());
    DonationViewModel returnedDonation = returnedDonationBatchViewModel.getDonations().get(0);
    assertThat(returnedDonation, hasSameStateAsDonationViewModel(expectedDonation1ViewModel));
  }
  
  @Test
  public void testCreateDonationBatchViewModelWithDonationsButNoDonationPermissions() {
    // set up test data
    Long donationId = new Long(1);
    Donation donation = new DonationBuilder().withId(donationId)
        .withPackType(aPackType().withTestSampleProduced(true).build())
        .build();
    Long donationBatchId = new Long(1);
    DonationBatch donationBatch = new DonationBatchBuilder().withId(donationBatchId).withDonation(donation).build();
    
    // expected data
    DonationViewModel expectedDonationViewModel = aDonationViewModel()
        .withDonation(donation)
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
    DonationBatchViewModel returnedDonationBatchViewModel = donationBatchViewModelFactory.createDonationBatchViewModelWithoutDonationPermissions(donationBatch, true);

    // do asserts
    Assert.assertNotNull("view model was created", returnedDonationBatchViewModel);
    Assert.assertNotNull("donations were created", returnedDonationBatchViewModel.getDonations());
    Assert.assertEquals("donations correct", 1, returnedDonationBatchViewModel.getDonations().size());
    DonationViewModel returnedDonation = returnedDonationBatchViewModel.getDonations().get(0);
    assertThat(returnedDonation, hasSameStateAsDonationViewModel(expectedDonationViewModel));
  }

    private void assertPermissionEquals(Map<String, Boolean> permissions, String permissionKey, Boolean permissionValue) {
    	Boolean permission = permissions.get(permissionKey);
    	Assert.assertNotNull("Permission '"+permissionKey+"' exists", permission);
    	Assert.assertEquals("Permission '"+permissionKey+"' is "+permissionValue, permissionValue, permission);
    }
}
