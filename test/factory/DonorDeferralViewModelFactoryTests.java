package factory;

import helpers.builders.DonorBuilder;
import helpers.builders.DonorDeferralBuilder;
import junit.framework.Assert;
import model.donor.Donor;
import model.donordeferral.DonorDeferral;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import service.DeferralConstraintChecker;
import viewmodel.DonorDeferralViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DonorDeferralViewModelFactoryTests {

  @InjectMocks
  private DonorDeferralViewModelFactory donorDeferralViewModelFactory;

  @Mock
  private DeferralConstraintChecker deferralConstraintChecker;

  @Test
  public void testCreateDonorDeferralViewModel() throws Exception {

    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(1L).withFirstName("Sample").withLastName("Donor").build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1L).withCreatedDate(new Date())
            .withDeferredDonor(deferredDonor).withDeferredUntil(new Date()).build();

    // set up mocks
    when(deferralConstraintChecker.canDeleteDonorDeferral(1L)).thenReturn(true);
    when(deferralConstraintChecker.canEndDonorDeferral(1L)).thenReturn(true);
    when(deferralConstraintChecker.canEditDonorDeferral(1L)).thenReturn(true);

    // run tests
    DonorDeferralViewModel donorDeferralViewModel = donorDeferralViewModelFactory.createDonorDeferralViewModel(donorDeferral);

    // asserts
    Assert.assertNotNull("DonorDeferralViewModel exists", donorDeferralViewModel);
    Assert.assertEquals("DonorDeferral matches", new Long(1), donorDeferralViewModel.getId());
    Assert.assertNotNull("Permissions have been defined", donorDeferralViewModel.getPermissions());
    Assert.assertEquals("Permissions have been defined", 3, donorDeferralViewModel.getPermissions().size());
    Boolean canDelete = donorDeferralViewModel.getPermissions().get("canDelete");
    Assert.assertNotNull("Permissions have been defined", canDelete);
    Assert.assertTrue("Permissions have been defined", canDelete);
    Boolean canEdit = donorDeferralViewModel.getPermissions().get("canEdit");
    Assert.assertNotNull("Permissions have been defined", canEdit);
    Assert.assertTrue("Permissions have been defined", canEdit);
    Boolean canEnd = donorDeferralViewModel.getPermissions().get("canEnd");
    Assert.assertNotNull("Permissions have been defined", canEnd);
    Assert.assertTrue("Permissions have been defined", canEnd);
  }

  @Test
  public void testCreateDonorDeferralViewModels() throws Exception {

    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(1L).withFirstName("Sample").withLastName("Donor").build();
    List<DonorDeferral> donorDeferrals = new ArrayList<>();
    DonorDeferral donorDeferral1 = DonorDeferralBuilder.aDonorDeferral().withId(1L).withCreatedDate(new Date())
            .withDeferredDonor(deferredDonor).withDeferredUntil(new Date()).build();
    donorDeferrals.add(donorDeferral1);
    DonorDeferral donorDeferral2 = DonorDeferralBuilder.aDonorDeferral().withId(2L).withCreatedDate(new Date())
            .withDeferredDonor(deferredDonor).withDeferredUntil(new Date()).build();
    donorDeferrals.add(donorDeferral2);
    DonorDeferral donorDeferral3 = DonorDeferralBuilder.aDonorDeferral().withId(3L).withCreatedDate(new Date())
            .withDeferredDonor(deferredDonor).withDeferredUntil(new Date()).build();
    donorDeferrals.add(donorDeferral3);

    // set up mocks
    when(deferralConstraintChecker.canDeleteDonorDeferral(1L)).thenReturn(true);
    when(deferralConstraintChecker.canEditDonorDeferral(1L)).thenReturn(true);
    when(deferralConstraintChecker.canEndDonorDeferral(1L)).thenReturn(true);
    when(deferralConstraintChecker.canDeleteDonorDeferral(2L)).thenReturn(true);
    when(deferralConstraintChecker.canEditDonorDeferral(2L)).thenReturn(true);
    when(deferralConstraintChecker.canEndDonorDeferral(2L)).thenReturn(true);
    when(deferralConstraintChecker.canDeleteDonorDeferral(3L)).thenReturn(true);
    when(deferralConstraintChecker.canEditDonorDeferral(3L)).thenReturn(true);
    when(deferralConstraintChecker.canEndDonorDeferral(3L)).thenReturn(true);

    // run tests
    List<DonorDeferralViewModel> donorDeferralViewModels = donorDeferralViewModelFactory.createDonorDeferralViewModels(donorDeferrals);

    // asserts
    Assert.assertNotNull("DonorDeferralViewModels returned", donorDeferralViewModels);
    Assert.assertEquals("DonorDeferralViewModels returned", 3, donorDeferralViewModels.size());
    boolean[] matches = {false, false, false};
    for (DonorDeferralViewModel donorDeferralViewModel : donorDeferralViewModels) {
      Assert.assertNotNull("DonorDeferralViewModel exists", donorDeferralViewModel);
      Assert.assertNotNull("DonorDeferralViewModel id defined", donorDeferralViewModel.getId());
      matches[(int) (donorDeferralViewModel.getId() - 1)] = true;
      Assert.assertNotNull("Permissions have been defined", donorDeferralViewModel.getPermissions());
      Assert.assertEquals("Permissions have been defined", 3, donorDeferralViewModel.getPermissions().size());
      Boolean canDelete = donorDeferralViewModel.getPermissions().get("canDelete");
      Assert.assertNotNull("Permissions have been defined", canDelete);
      Assert.assertTrue("Permissions have been defined", canDelete);
    }
    Assert.assertTrue("DonorDeferrals defined", matches[0]);
    Assert.assertTrue("DonorDeferrals defined", matches[1]);
    Assert.assertTrue("DonorDeferrals defined", matches[2]);
  }
}
