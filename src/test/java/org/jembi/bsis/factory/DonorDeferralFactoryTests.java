package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.DeferralBackingFormBuilder.aDeferralBackingForm;
import static org.jembi.bsis.helpers.builders.DeferralReasonBackingFormBuilder.aDeferralReasonBackingForm;
import static org.jembi.bsis.helpers.builders.DonorBackingFormBuilder.aDonorBackingForm;
import static org.jembi.bsis.helpers.builders.DonorDeferralBuilder.aDonorDeferral;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aVenueBackingForm;
import static org.jembi.bsis.helpers.matchers.DonorDeferralMatcher.hasSameStateAsDonorDeferral;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.backingform.DeferralBackingForm;
import org.jembi.bsis.backingform.DeferralReasonBackingForm;
import org.jembi.bsis.backingform.DonorBackingForm;
import org.jembi.bsis.backingform.LocationBackingForm;
import static org.jembi.bsis.helpers.builders.DeferralReasonBuilder.aDeferralReason;
import org.jembi.bsis.helpers.builders.DonorBuilder;
import org.jembi.bsis.helpers.builders.DonorDeferralBuilder;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.donordeferral.DonorDeferral;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.DeferralReasonRepository;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.service.DeferralConstraintChecker;
import org.jembi.bsis.viewmodel.DonorDeferralViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DonorDeferralFactoryTests {

  @InjectMocks
  private DonorDeferralFactory donorDeferralFactory;

  @Mock
  private DeferralConstraintChecker deferralConstraintChecker;
  
  @Mock
  private DeferralReasonFactory deferralReasonFactory;
  
  @Mock
  private DeferralReasonRepository deferralReasonRepository;
  
  @Mock
  private DonorRepository donorRepository;
  
  @Mock
  private LocationRepository locationRepository;

  @Test
  public void testCreateDonorDeferralViewModel() throws Exception {

    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(1l).withFirstName("Sample").withLastName("Donor").build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferredUntil(new Date()).build();

    // set up mocks
    when(deferralConstraintChecker.canEndDonorDeferral(1L)).thenReturn(true);
    when(deferralConstraintChecker.canEditDonorDeferral(1L)).thenReturn(true);

    // run tests
    DonorDeferralViewModel donorDeferralViewModel = donorDeferralFactory.createDonorDeferralViewModel(donorDeferral);

    // asserts
    Assert.assertNotNull("DonorDeferralViewModel exists", donorDeferralViewModel);
    Assert.assertEquals("DonorDeferral matches", new Long(1), donorDeferralViewModel.getId());
    Assert.assertNotNull("Permissions have been defined", donorDeferralViewModel.getPermissions());
    Assert.assertEquals("Permissions have been defined", 2, donorDeferralViewModel.getPermissions().size());
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
    Donor deferredDonor = DonorBuilder.aDonor().withId(1l).withFirstName("Sample").withLastName("Donor").build();
    List<DonorDeferral> donorDeferrals = new ArrayList<DonorDeferral>();
    DonorDeferral donorDeferral1 = DonorDeferralBuilder.aDonorDeferral().withId(1l).withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferredUntil(new Date()).build();
    donorDeferrals.add(donorDeferral1);
    DonorDeferral donorDeferral2 = DonorDeferralBuilder.aDonorDeferral().withId(2l).withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferredUntil(new Date()).build();
    donorDeferrals.add(donorDeferral2);
    DonorDeferral donorDeferral3 = DonorDeferralBuilder.aDonorDeferral().withId(3l).withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferredUntil(new Date()).build();
    donorDeferrals.add(donorDeferral3);

    // set up mocks
    when(deferralConstraintChecker.canEditDonorDeferral(1L)).thenReturn(true);
    when(deferralConstraintChecker.canEndDonorDeferral(1L)).thenReturn(true);
    when(deferralConstraintChecker.canEditDonorDeferral(2L)).thenReturn(true);
    when(deferralConstraintChecker.canEndDonorDeferral(2L)).thenReturn(true);
    when(deferralConstraintChecker.canEditDonorDeferral(3L)).thenReturn(true);
    when(deferralConstraintChecker.canEndDonorDeferral(3L)).thenReturn(true);

    // run tests
    List<DonorDeferralViewModel> donorDeferralViewModels = donorDeferralFactory.createDonorDeferralViewModels(donorDeferrals);

    // asserts
    Assert.assertNotNull("DonorDeferralViewModels returned", donorDeferralViewModels);
    Assert.assertEquals("DonorDeferralViewModels returned", 3, donorDeferralViewModels.size());
    boolean[] matches = {false, false, false};
    for (DonorDeferralViewModel donorDeferralViewModel : donorDeferralViewModels) {
      Assert.assertNotNull("DonorDeferralViewModel exists", donorDeferralViewModel);
      Assert.assertNotNull("DonorDeferralViewModel id defined", donorDeferralViewModel.getId());
      matches[(int) (donorDeferralViewModel.getId() - 1)] = true;
      Assert.assertNotNull("Permissions have been defined", donorDeferralViewModel.getPermissions());
      Assert.assertEquals("Permissions have been defined", 2, donorDeferralViewModel.getPermissions().size());
    }
    Assert.assertTrue("DonorDeferrals defined", matches[0]);
    Assert.assertTrue("DonorDeferrals defined", matches[1]);
    Assert.assertTrue("DonorDeferrals defined", matches[2]);
  }
  
  @Test
  public void testCreateEntity_shouldCreateEntities() {
    // set up test data
    Date deferralDate = new Date();
    Date deferredUntilDate = new Date();
    String deferralReasonText = "testing123";
    
    DonorBackingForm donorForm = aDonorBackingForm().withId(1L).build();
    LocationBackingForm locationForm = aVenueBackingForm().withId(1L).build();
    DeferralReasonBackingForm deferralReasonForm = aDeferralReasonBackingForm().withId(1L).build();

    DeferralBackingForm deferralForm = aDeferralBackingForm()
        .withDeferralDate(deferralDate)
        .withDeferredUntil(deferredUntilDate)
        .withDeferralReason(deferralReasonForm)
        .withDeferredDonor(donorForm)
        .withVenue(locationForm)
        .withDeferralReasonText(deferralReasonText)
        .build();
    
    Location location = LocationBuilder.aVenue().build();
    Donor donor = DonorBuilder.aDonor().build();
    DeferralReason deferralReason = aDeferralReason().build();

    // set up expected result
    DonorDeferral expectedDonorDeferral = aDonorDeferral()
        .withDeferralDate(deferralDate)
        .withDeferredUntil(deferredUntilDate)
        .withDeferredDonor(donor)
        .withDeferralReason(deferralReason)
        .withVenue(location)
        .build();
    
    // set up mocks
    when(deferralReasonRepository.getDeferralReasonById(1L)).thenReturn(deferralReason);
    when(donorRepository.findDonorById(1L)).thenReturn(donor);
    when(locationRepository.getLocation(1L)).thenReturn(location);
    
    // run test
    DonorDeferral createdEntity = donorDeferralFactory.createEntity(deferralForm);
    
    // check results
    assertThat(createdEntity, hasSameStateAsDonorDeferral(expectedDonorDeferral));
  }
}
