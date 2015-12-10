package factory;

import helpers.builders.DonationBatchBuilder;
import model.donationbatch.DonationBatch;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import service.DonationBatchConstraintChecker;
import viewmodel.DonationBatchViewModel;

import java.util.Map;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DonationBatchViewModelFactoryTests {

  @InjectMocks
  private DonationBatchViewModelFactory donationBatchViewModelFactory;
  @Mock
  private DonationBatchConstraintChecker donationBatchConstraintChecker;

  @Test
  public void testCreateDonationBatchViewModel() {
    // set up test data
    Integer donationBatchId = 1;
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

  private void assertPermissionEquals(Map<String, Boolean> permissions, String permissionKey, Boolean permissionValue) {
    Boolean permission = permissions.get(permissionKey);
    Assert.assertNotNull("Permission '" + permissionKey + "' exists", permission);
    Assert.assertEquals("Permission '" + permissionKey + "' is " + permissionValue, permissionValue, permission);
  }
}
