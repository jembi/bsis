package service;

import static helpers.builders.DeferralReasonBuilder.aDeferralReason;
import static helpers.builders.DonorBuilder.aDonor;
import static helpers.builders.DonorDeferralBuilder.aDonorDeferral;
import static helpers.matchers.DonorDeferralMatcher.hasSameStateAsDonorDeferral;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import helpers.builders.DeferralReasonBuilder;
import helpers.builders.DonorBuilder;
import helpers.builders.DonorDeferralBuilder;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import model.donor.Donor;
import model.donordeferral.DeferralReason;
import model.donordeferral.DeferralReasonType;
import model.donordeferral.DonorDeferral;
import model.donordeferral.DurationType;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import repository.DeferralReasonRepository;
import repository.DonorDeferralRepository;
import suites.UnitTestSuite;

public class DonorDeferralCRUDServiceTests extends UnitTestSuite {

  @InjectMocks
  private DonorDeferralCRUDService donorDeferralCRUDService;
  @Mock
  private DonorDeferralRepository donorDeferralRepository;
  @Mock
  private DeferralReasonRepository deferralReasonRepository;
  @Mock
  private DateGeneratorService dateGeneratorService;
  @Mock
  private DeferralConstraintChecker deferralConstraintChecker;

  @Test
  public void testCreateDeferralForDonorWithDeferralReasonTypeWithPermanentDeferralReason_shouldCreateAndPersistDonorDeferral() {

    DeferralReasonType irrelevantDeferralReasonType = DeferralReasonType.AUTOMATED_TTI_UNSAFE;
    Donor donor = aDonor().build();
    DeferralReason deferralReason = aDeferralReason()
        .withType(irrelevantDeferralReasonType)
        .withDurationType(DurationType.PERMANENT)
        .build();

    DonorDeferral expectedDonorDeferral = aDonorDeferral()
        .withDeferredDonor(donor)
        .withDeferralReason(deferralReason)
        .withDeferredUntil(DonorDeferralCRUDService.PERMANENT_DEFERRAL_DATE)
        .build();

    when(deferralReasonRepository.findDeferralReasonByType(irrelevantDeferralReasonType))
        .thenReturn(deferralReason);
    when(donorDeferralRepository.findDonorDeferralsForDonorByDeferralReason(donor, deferralReason))
        .thenReturn(Collections.<DonorDeferral>emptyList());

    DonorDeferral returnedDonorDeferral = donorDeferralCRUDService.createDeferralForDonorWithDeferralReasonType(
        donor, irrelevantDeferralReasonType);

    verify(donorDeferralRepository).save(argThat(hasSameStateAsDonorDeferral(expectedDonorDeferral)));
    assertThat(returnedDonorDeferral, hasSameStateAsDonorDeferral(expectedDonorDeferral));
  }

  @Test
  public void testCreateDeferralForDonorWithDeferralReasonTypeWithPermanentDeferralReasonAndExistingDeferral_shouldReturnExistingDonorDeferral() {

    DeferralReasonType irrelevantDeferralReasonType = DeferralReasonType.AUTOMATED_TTI_UNSAFE;
    Donor donor = aDonor().build();
    DeferralReason deferralReason = aDeferralReason()
        .withType(irrelevantDeferralReasonType)
        .withDurationType(DurationType.PERMANENT)
        .build();

    DonorDeferral expectedDonorDeferral = aDonorDeferral()
        .withDeferredDonor(donor)
        .withDeferralReason(deferralReason)
        .withDeferredUntil(DonorDeferralCRUDService.PERMANENT_DEFERRAL_DATE)
        .build();

    when(deferralReasonRepository.findDeferralReasonByType(irrelevantDeferralReasonType))
        .thenReturn(deferralReason);
    when(donorDeferralRepository.findDonorDeferralsForDonorByDeferralReason(donor, deferralReason))
        .thenReturn(Arrays.asList(expectedDonorDeferral));

    DonorDeferral returnedDonorDeferral = donorDeferralCRUDService.createDeferralForDonorWithDeferralReasonType(
        donor, irrelevantDeferralReasonType);

    verify(donorDeferralRepository, never()).save(any(DonorDeferral.class));
    assertThat(returnedDonorDeferral, hasSameStateAsDonorDeferral(expectedDonorDeferral));
  }

  @Test
  public void testCreateDeferralForDonorWithDeferralReasonTypeWithTemporaryDeferralReason_shouldCreateAndPersistDonorDeferral() {

    DeferralReasonType irrelevantDeferralReasonType = DeferralReasonType.AUTOMATED_TTI_UNSAFE;
    int irrelevantDuration = 7;
    Date now = new Date();
    Donor donor = aDonor().build();
    DeferralReason deferralReason = aDeferralReason()
        .withType(irrelevantDeferralReasonType)
        .withDurationType(DurationType.TEMPORARY)
        .withDefaultDuration(irrelevantDuration)
        .build();

    DonorDeferral expectedDonorDeferral = aDonorDeferral()
        .withDeferredDonor(donor)
        .withDeferralReason(deferralReason)
        .withDeferredUntil(new DateTime(now).plusDays(irrelevantDuration).toDate())
        .build();

    when(deferralReasonRepository.findDeferralReasonByType(irrelevantDeferralReasonType))
        .thenReturn(deferralReason);
    when(dateGeneratorService.generateDate()).thenReturn(now);

    DonorDeferral returnedDonorDeferral = donorDeferralCRUDService.createDeferralForDonorWithDeferralReasonType(
        donor, irrelevantDeferralReasonType);

    verify(donorDeferralRepository).save(argThat(hasSameStateAsDonorDeferral(expectedDonorDeferral)));
    assertThat(returnedDonorDeferral, hasSameStateAsDonorDeferral(expectedDonorDeferral));
  }

  @Test
  public void testFindDeferralById() throws Exception {
    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(1l).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.TEMPORARY)
        .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withCreatedDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferredUntil(new Date()).build();

    // set up mocks
    when(donorDeferralRepository.findDonorDeferralById(1l)).thenReturn(donorDeferral);

    // run tests
    DonorDeferral returnedDonorDeferral = donorDeferralCRUDService.findDeferralById(1l);

    // asserts
    Assert.assertNotNull("Deferral returned", returnedDonorDeferral);
    Assert.assertEquals("Deferral matches", new Long(1), returnedDonorDeferral.getId());
  }

  @Test
  public void testDeleteDeferral() throws Exception {
    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(1l).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.TEMPORARY)
        .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withCreatedDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferredUntil(new Date()).build();

    // set up mocks
    when(donorDeferralRepository.findDonorDeferralById(1l)).thenReturn(donorDeferral);
    when(deferralConstraintChecker.canDeleteDonorDeferral(1L)).thenReturn(true);

    // run tests
    donorDeferralCRUDService.deleteDeferral(1l);

    // asserts
    Assert.assertTrue("Deferral was deleted", donorDeferral.getIsVoided());
    Assert.assertEquals("Deferral was deleted by", loggedInUser.getUsername(), donorDeferral.getVoidedBy().getUsername());
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Assert.assertEquals("Deferral was deleted when", sdf.format(new Date()), sdf.format(donorDeferral.getVoidedDate()));
  }

  @Test(expected = java.lang.IllegalStateException.class)
  public void testDeleteDeferralWithConstraints() throws Exception {
    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(1l).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.TEMPORARY)
        .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withCreatedDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferredUntil(new Date()).build();

    // set up mocks
    when(donorDeferralRepository.findDonorDeferralById(1l)).thenReturn(donorDeferral);
    when(deferralConstraintChecker.canDeleteDonorDeferral(1L)).thenReturn(false);

    // run tests
    donorDeferralCRUDService.deleteDeferral(1l);
  }

  @Test
  public void testUpdateDeferral() throws Exception {
    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(1l).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason originalDeferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.TEMPORARY)
        .withType(DeferralReasonType.NORMAL).withReason("test1").build();
    Date createdDate = new Date();
    DonorDeferral originalDonorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withCreatedDate(createdDate)
        .withDeferredDonor(deferredDonor).withDeferralReason(originalDeferralReason).withDeferredUntil(new Date()).build();

    Date newDeferredUntil = new SimpleDateFormat("yyyy-MM-dd").parse("2020-10-20");
    DeferralReason updatedDeferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.PERMANENT)
        .withType(DeferralReasonType.NORMAL).withReason("test2").build();
    DonorDeferral updatedDonorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withCreatedDate(createdDate)
        .withDeferredDonor(deferredDonor).withDeferralReason(updatedDeferralReason).withDeferralReasonText("hello").withDeferredUntil(newDeferredUntil).build();

    // set up mocks
    when(donorDeferralRepository.findDonorDeferralById(1l)).thenReturn(originalDonorDeferral);
    when(deferralConstraintChecker.canDeleteDonorDeferral(1L)).thenReturn(true);
    when(donorDeferralRepository.update(originalDonorDeferral)).thenReturn(updatedDonorDeferral);

    // run tests
    DonorDeferral savedDonorDeferral = donorDeferralCRUDService.updateDeferral(updatedDonorDeferral);

    // asserts - note: due to the use of mocks, there is not much that can be asserted usefully.
    Assert.assertNotNull("Saved DonorDeferral returned", savedDonorDeferral);
  }

  @Test
  public void testEndDeferral() throws Exception {
    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(1l).withFirstName("Sample").withLastName("Donor").build();
    Date newDeferredUntil = new SimpleDateFormat("yyyy-MM-dd").parse("2020-10-20");
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.PERMANENT)
        .withType(DeferralReasonType.NORMAL).withReason("test2").build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withCreatedDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferralReasonText("hello").withDeferredUntil(newDeferredUntil).build();

    // set up mocks
    when(donorDeferralRepository.findDonorDeferralById(1l)).thenReturn(donorDeferral);
    when(deferralConstraintChecker.canDeleteDonorDeferral(1L)).thenReturn(true);
    when(donorDeferralRepository.update(donorDeferral)).thenReturn(donorDeferral);

    // run tests
    DonorDeferral endedDonorDeferral = donorDeferralCRUDService.endDeferral(1l, "the end");

    // asserts - note: due to the use of mocks, there is not much that can be asserted usefully.
    Assert.assertNotNull("Saved DonorDeferral returned", endedDonorDeferral);
  }

  @Test(expected = java.lang.IllegalStateException.class)
  public void testEndDeferralWithConstraints() throws Exception {
    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(1l).withFirstName("Sample").withLastName("Donor").build();
    Date newDeferredUntil = new SimpleDateFormat("yyyy-MM-dd").parse("2020-10-20");
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.PERMANENT)
        .withType(DeferralReasonType.NORMAL).withReason("test2").build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withCreatedDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferralReasonText("hello").withDeferredUntil(newDeferredUntil).build();

    // set up mocks
    when(donorDeferralRepository.findDonorDeferralById(1l)).thenReturn(donorDeferral);
    when(deferralConstraintChecker.canDeleteDonorDeferral(1L)).thenReturn(false);
    when(donorDeferralRepository.update(donorDeferral)).thenReturn(donorDeferral);

    // run tests
    donorDeferralCRUDService.endDeferral(1l, "the end");
  }

  @Test
  public void testAppendCommentEmpty() throws Exception {
    DonorDeferral deferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withDeferralReasonText("").build();
    donorDeferralCRUDService.appendComment(deferral, "test");
    Assert.assertEquals("Comment updated", "test", deferral.getDeferralReasonText());
  }

  @Test
  public void testAppendCommentNull() throws Exception {
    DonorDeferral deferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withDeferralReasonText(null).build();
    donorDeferralCRUDService.appendComment(deferral, "test");
    Assert.assertEquals("Comment updated", "test", deferral.getDeferralReasonText());
  }

  @Test
  public void testAppendComment() throws Exception {
    DonorDeferral deferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withDeferralReasonText("hello").build();
    donorDeferralCRUDService.appendComment(deferral, "world");
    Assert.assertEquals("Comment updated", "hello. world", deferral.getDeferralReasonText());
  }

  @Test
  public void testAppendCommentPeriod() throws Exception {
    DonorDeferral deferral = DonorDeferralBuilder.aDonorDeferral().withId(1l).withDeferralReasonText("hello. ").build();
    donorDeferralCRUDService.appendComment(deferral, "world");
    Assert.assertEquals("Comment updated", "hello. world", deferral.getDeferralReasonText());
  }
}
