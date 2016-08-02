package org.jembi.bsis.service;

import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;

import org.jembi.bsis.helpers.builders.DeferralReasonBuilder;
import org.jembi.bsis.helpers.builders.DonorBuilder;
import org.jembi.bsis.helpers.builders.DonorDeferralBuilder;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.donordeferral.DeferralReasonType;
import org.jembi.bsis.model.donordeferral.DonorDeferral;
import org.jembi.bsis.model.donordeferral.DurationType;
import org.jembi.bsis.repository.DonorDeferralRepository;
import org.jembi.bsis.service.DeferralConstraintChecker;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DeferralConstraintCheckerTests {

  @InjectMocks
  private DeferralConstraintChecker deferralConstraintChecker;

  @Mock
  private DonorDeferralRepository deferralRepository;

  @Test
  public void testCanDeleteDonorDeferral() {
    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(1l).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.PERMANENT)
        .withType(DeferralReasonType.AUTOMATED_TTI_UNSAFE).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferredUntil(new Date()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(1l)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canDeleteDonorDeferral(1l);

    // do asserts
    Assert.assertTrue("Can delete an automatic deferral", result);
  }

  @Test
  public void testCanDeleteDonorDeferralTemporary() {
    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(1l).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.TEMPORARY)
        .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferredUntil(new Date()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(1l)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canDeleteDonorDeferral(1l);

    // do asserts
    Assert.assertTrue("Can delete this temporary deferral", result);
  }

  @Test
  public void testCanDeleteDonorDeferralPermanent() {
    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(1l).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.PERMANENT)
        .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferredUntil(new Date()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(1l)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canDeleteDonorDeferral(1l);

    // do asserts
    Assert.assertTrue("Can delete this permanent deferral", result);
  }

  @Test
  public void testCanEditDonorDeferral() {
    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(1l).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.PERMANENT)
        .withType(DeferralReasonType.AUTOMATED_TTI_UNSAFE).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferredUntil(new Date()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(1l)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canEditDonorDeferral(1l);

    // do asserts
    Assert.assertFalse("Can not edit an automatic deferral", result);
  }

  @Test
  public void testCanEditDonorDeferralTemporary() {
    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(1l).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.TEMPORARY)
        .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferredUntil(new Date()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(1l)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canEditDonorDeferral(1l);

    // do asserts
    Assert.assertTrue("Can edit this temporary deferral", result);
  }

  @Test
  public void testCanEditDonorDeferralPermanent() {
    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(1l).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.PERMANENT)
        .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferredUntil(new Date()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(1l)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canEditDonorDeferral(1l);

    // do asserts
    Assert.assertTrue("Can edit this permanent deferral", result);
  }

  @Test
  public void testCanEndDonorDeferral() {
    // create test data
    Calendar deferralEndDate = Calendar.getInstance();
    deferralEndDate.add(Calendar.DAY_OF_YEAR, 90);
    Donor deferredDonor = DonorBuilder.aDonor().withId(1l).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.PERMANENT)
        .withType(DeferralReasonType.AUTOMATED_TTI_UNSAFE).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason)
        .withDeferredUntil(deferralEndDate.getTime()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(1l)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canEndDonorDeferral(1l);

    // do asserts
    Assert.assertFalse("Can not end an automatic deferral", result);
  }

  @Test
  public void testCanEndDonorDeferralTemporary() {
    // create test data
    Calendar deferralEndDateCal = Calendar.getInstance();
    deferralEndDateCal.add(Calendar.DAY_OF_YEAR, 90);
    Donor deferredDonor = DonorBuilder.aDonor().withId(1l).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.TEMPORARY)
        .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason)
        .withDeferredUntil(deferralEndDateCal.getTime()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(1l)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canEndDonorDeferral(1l);

    // do asserts
    Assert.assertTrue("Can end this temporary deferral", result);
  }

  @Test
  public void testCanEndDonorDeferralTemporaryThatsOver() {
    // create test data
    Calendar deferralEndDate = Calendar.getInstance();
    deferralEndDate.add(Calendar.DAY_OF_YEAR, -90);
    Donor deferredDonor = DonorBuilder.aDonor().withId(1l).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.TEMPORARY)
        .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason)
        .withDeferredUntil(deferralEndDate.getTime()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(1l)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canEndDonorDeferral(1l);

    // do asserts
    Assert.assertFalse("Cannot end a deferral that's over", result);
  }

  @Test
  public void testCanEndDonorDeferralPermanent() {
    // create test data
    Calendar deferralEndDate = Calendar.getInstance();
    deferralEndDate.add(Calendar.DAY_OF_YEAR, 90);
    Donor deferredDonor = DonorBuilder.aDonor().withId(1l).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.PERMANENT)
        .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason)
        .withDeferredUntil(deferralEndDate.getTime()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(1l)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canEndDonorDeferral(1l);

    // do asserts
    Assert.assertTrue("Can end this permanent deferral", result);
  }
}
