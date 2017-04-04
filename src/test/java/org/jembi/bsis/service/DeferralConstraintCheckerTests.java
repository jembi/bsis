package org.jembi.bsis.service;

import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.helpers.builders.DeferralReasonBuilder;
import org.jembi.bsis.helpers.builders.DonorBuilder;
import org.jembi.bsis.helpers.builders.DonorDeferralBuilder;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.donordeferral.DeferralReasonType;
import org.jembi.bsis.model.donordeferral.DonorDeferral;
import org.jembi.bsis.model.donordeferral.DurationType;
import org.jembi.bsis.repository.DonorDeferralRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class DeferralConstraintCheckerTests extends UnitTestSuite {

  private static final UUID DONOR_DEFERRAL_ID = UUID.fromString("11e71397-acc9-b7da-8cc5-34e6d7870681");

  @InjectMocks
  private DeferralConstraintChecker deferralConstraintChecker;

  @Mock
  private DonorDeferralRepository deferralRepository;

  @Test
  public void testCanEditDonorDeferral() {
    // create test data
    UUID donorId = UUID.randomUUID();
    Donor deferredDonor = DonorBuilder.aDonor().withId(donorId).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.PERMANENT)
        .withType(DeferralReasonType.AUTOMATED_TTI_UNSAFE).build();
    DonorDeferral donorDeferral =
        DonorDeferralBuilder.aDonorDeferral().withId(DONOR_DEFERRAL_ID).withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferredUntil(new Date()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(DONOR_DEFERRAL_ID)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canEditDonorDeferral(DONOR_DEFERRAL_ID);

    // do asserts
    Assert.assertFalse("Can not edit an automatic deferral", result);
  }

  @Test
  public void testCanEditDonorDeferralTemporary() {
    // create test data
    UUID donorId = UUID.randomUUID();
    Donor deferredDonor = DonorBuilder.aDonor().withId(donorId).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.TEMPORARY)
        .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral =
        DonorDeferralBuilder.aDonorDeferral().withId(DONOR_DEFERRAL_ID).withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferredUntil(new Date()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(DONOR_DEFERRAL_ID)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canEditDonorDeferral(DONOR_DEFERRAL_ID);

    // do asserts
    Assert.assertTrue("Can edit this temporary deferral", result);
  }

  @Test
  public void testCanEditDonorDeferralPermanent() {
    // create test data
    UUID donorId = UUID.randomUUID();
    Donor deferredDonor = DonorBuilder.aDonor().withId(donorId).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.PERMANENT)
        .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral =
        DonorDeferralBuilder.aDonorDeferral().withId(DONOR_DEFERRAL_ID).withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferredUntil(new Date()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(DONOR_DEFERRAL_ID)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canEditDonorDeferral(DONOR_DEFERRAL_ID);

    // do asserts
    Assert.assertTrue("Can edit this permanent deferral", result);
  }

  @Test
  public void testCanEndDonorDeferral() {
    // create test data
    Calendar deferralEndDate = Calendar.getInstance();
    UUID donorId = UUID.randomUUID();
    deferralEndDate.add(Calendar.DAY_OF_YEAR, 90);
    Donor deferredDonor = DonorBuilder.aDonor().withId(donorId).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.PERMANENT)
        .withType(DeferralReasonType.AUTOMATED_TTI_UNSAFE).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(DONOR_DEFERRAL_ID)
        .withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason)
        .withDeferredUntil(deferralEndDate.getTime()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(DONOR_DEFERRAL_ID)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canEndDonorDeferral(DONOR_DEFERRAL_ID);

    // do asserts
    Assert.assertFalse("Can not end an automatic deferral", result);
  }

  @Test
  public void testCanEndDonorDeferralTemporary() {
    // create test data
    Calendar deferralEndDateCal = Calendar.getInstance();
    UUID donorId = UUID.randomUUID();
    deferralEndDateCal.add(Calendar.DAY_OF_YEAR, 90);
    Donor deferredDonor = DonorBuilder.aDonor().withId(donorId).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.TEMPORARY)
        .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(DONOR_DEFERRAL_ID)
        .withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason)
        .withDeferredUntil(deferralEndDateCal.getTime()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(DONOR_DEFERRAL_ID)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canEndDonorDeferral(DONOR_DEFERRAL_ID);

    // do asserts
    Assert.assertTrue("Can end this temporary deferral", result);
  }

  @Test
  public void testCanEndDonorDeferralTemporaryThatsOver() {
    // create test data
    Calendar deferralEndDate = Calendar.getInstance();
    UUID donorId = UUID.randomUUID();
    deferralEndDate.add(Calendar.DAY_OF_YEAR, -90);
    Donor deferredDonor = DonorBuilder.aDonor().withId(donorId).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.TEMPORARY)
        .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(DONOR_DEFERRAL_ID)
        .withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason)
        .withDeferredUntil(deferralEndDate.getTime()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(DONOR_DEFERRAL_ID)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canEndDonorDeferral(DONOR_DEFERRAL_ID);

    // do asserts
    Assert.assertFalse("Cannot end a deferral that's over", result);
  }

  @Test
  public void testCanEndDonorDeferralPermanent() {
    // create test data
    Calendar deferralEndDate = Calendar.getInstance();
    deferralEndDate.add(Calendar.DAY_OF_YEAR, 90);
    UUID donorId = UUID.randomUUID();
    
    Donor deferredDonor = DonorBuilder.aDonor().withId(donorId).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.PERMANENT)
        .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(DONOR_DEFERRAL_ID)
        .withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason)
        .withDeferredUntil(deferralEndDate.getTime()).build();

    // set up mocks
    when(deferralRepository.findDonorDeferralById(DONOR_DEFERRAL_ID)).thenReturn(donorDeferral);

    // run test
    boolean result = deferralConstraintChecker.canEndDonorDeferral(DONOR_DEFERRAL_ID);

    // do asserts
    Assert.assertTrue("Can end this permanent deferral", result);
  }
}
