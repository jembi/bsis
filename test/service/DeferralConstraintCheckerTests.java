package service;

import helpers.builders.DeferralReasonBuilder;
import helpers.builders.DonorBuilder;
import helpers.builders.DonorDeferralBuilder;
import model.donor.Donor;
import model.donordeferral.DeferralReason;
import model.donordeferral.DeferralReasonType;
import model.donordeferral.DonorDeferral;
import model.donordeferral.DurationType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import repository.DonorDeferralRepository;

import java.util.Calendar;
import java.util.Date;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeferralConstraintCheckerTests {

  @InjectMocks
  private DeferralConstraintChecker deferralConstraintChecker;

  @Mock
  private DonorDeferralRepository deferralRepository;

  @Test
  public void testCanDeleteDonorDeferral() {
    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(1L).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.PERMANENT)
            .withType(DeferralReasonType.AUTOMATED_TTI_UNSAFE).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1L).withCreatedDate(new Date())
            .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferredUntil(new Date()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(1L)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canDeleteDonorDeferral(1L);

    // do asserts
    Assert.assertFalse("Can not delete an automatic deferral", result);
  }

  @Test
  public void testCanDeleteDonorDeferralTemporary() {
    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(1L).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.TEMPORARY)
            .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1L).withCreatedDate(new Date())
            .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferredUntil(new Date()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(1L)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canDeleteDonorDeferral(1L);

    // do asserts
    Assert.assertTrue("Can delete this temporary deferral", result);
  }

  @Test
  public void testCanDeleteDonorDeferralPermanent() {
    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(1L).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.PERMANENT)
            .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1L).withCreatedDate(new Date())
            .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferredUntil(new Date()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(1L)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canDeleteDonorDeferral(1L);

    // do asserts
    Assert.assertTrue("Can delete this permanent deferral", result);
  }

  @Test
  public void testCanEditDonorDeferral() {
    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(1L).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.PERMANENT)
            .withType(DeferralReasonType.AUTOMATED_TTI_UNSAFE).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1L).withCreatedDate(new Date())
            .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferredUntil(new Date()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(1L)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canEditDonorDeferral(1L);

    // do asserts
    Assert.assertFalse("Can not edit an automatic deferral", result);
  }

  @Test
  public void testCanEditDonorDeferralTemporary() {
    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(1L).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.TEMPORARY)
            .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1L).withCreatedDate(new Date())
            .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferredUntil(new Date()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(1L)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canEditDonorDeferral(1L);

    // do asserts
    Assert.assertTrue("Can edit this temporary deferral", result);
  }

  @Test
  public void testCanEditDonorDeferralPermanent() {
    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(1L).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.PERMANENT)
            .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1L).withCreatedDate(new Date())
            .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferredUntil(new Date()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(1L)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canEditDonorDeferral(1L);

    // do asserts
    Assert.assertTrue("Can edit this permanent deferral", result);
  }

  @Test
  public void testCanEndDonorDeferral() {
    // create test data
    Calendar deferralEndDate = Calendar.getInstance();
    deferralEndDate.add(Calendar.DAY_OF_YEAR, 90);
    Donor deferredDonor = DonorBuilder.aDonor().withId(1L).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.PERMANENT)
            .withType(DeferralReasonType.AUTOMATED_TTI_UNSAFE).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1L).withCreatedDate(new Date())
            .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason)
            .withDeferredUntil(deferralEndDate.getTime()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(1L)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canEndDonorDeferral(1L);

    // do asserts
    Assert.assertFalse("Can not end an automatic deferral", result);
  }

  @Test
  public void testCanEndDonorDeferralTemporary() {
    // create test data
    Calendar deferralEndDateCal = Calendar.getInstance();
    deferralEndDateCal.add(Calendar.DAY_OF_YEAR, 90);
    Donor deferredDonor = DonorBuilder.aDonor().withId(1L).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.TEMPORARY)
            .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1L).withCreatedDate(new Date())
            .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason)
            .withDeferredUntil(deferralEndDateCal.getTime()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(1L)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canEndDonorDeferral(1L);

    // do asserts
    Assert.assertTrue("Can end this temporary deferral", result);
  }

  @Test
  public void testCanEndDonorDeferralTemporaryThatsOver() {
    // create test data
    Calendar deferralEndDate = Calendar.getInstance();
    deferralEndDate.add(Calendar.DAY_OF_YEAR, -90);
    Donor deferredDonor = DonorBuilder.aDonor().withId(1L).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.TEMPORARY)
            .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1L).withCreatedDate(new Date())
            .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason)
            .withDeferredUntil(deferralEndDate.getTime()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(1L)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canEndDonorDeferral(1L);

    // do asserts
    Assert.assertFalse("Cannot end a deferral that's over", result);
  }

  @Test
  public void testCanEndDonorDeferralPermanent() {
    // create test data
    Calendar deferralEndDate = Calendar.getInstance();
    deferralEndDate.add(Calendar.DAY_OF_YEAR, 90);
    Donor deferredDonor = DonorBuilder.aDonor().withId(1L).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.PERMANENT)
            .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1L).withCreatedDate(new Date())
            .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason)
            .withDeferredUntil(deferralEndDate.getTime()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(1L)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canEndDonorDeferral(1L);

    // do asserts
    Assert.assertTrue("Can end this permanent deferral", result);
  }
}
