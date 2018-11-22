package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.DeferralReasonBuilder.aDeferralReason;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.DonorDeferralBuilder.aDonorDeferral;
import static org.jembi.bsis.helpers.matchers.DonorDeferralMatcher.hasSameStateAsDonorDeferral;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.helpers.builders.DeferralReasonBuilder;
import org.jembi.bsis.helpers.builders.DonorBuilder;
import org.jembi.bsis.helpers.builders.DonorDeferralBuilder;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.donordeferral.DeferralReasonType;
import org.jembi.bsis.model.donordeferral.DonorDeferral;
import org.jembi.bsis.model.donordeferral.DurationType;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.DeferralReasonRepository;
import org.jembi.bsis.repository.DonorDeferralRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class DonorDeferralCRUDServiceTests extends UnitTestSuite {

  private static final UUID DONOR_DEFERRAL_ID = UUID.randomUUID();

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

    UUID locationId = UUID.randomUUID();
    Location irrelevantVenue = LocationBuilder.aLocation().withId(locationId).withName("Test Location").build();
    DeferralReasonType irrelevantDeferralReasonType = DeferralReasonType.AUTOMATED_TTI_UNSAFE;
    Donor donor = aDonor().build();
    DeferralReason deferralReason = aDeferralReason()
        .withType(irrelevantDeferralReasonType)
        .withDurationType(DurationType.PERMANENT)
        .build();

    DonorDeferral expectedDonorDeferral = aDonorDeferral()
        .withDeferredDonor(donor)
        .withDeferralReason(deferralReason)
        .withDeferralDate(null)
        .withVenue(irrelevantVenue)
        .withDeferredUntil(DonorDeferralCRUDService.PERMANENT_DEFERRAL_DATE)
        .build();

    when(deferralReasonRepository.findDeferralReasonByType(irrelevantDeferralReasonType))
        .thenReturn(deferralReason);
    when(donorDeferralRepository.findDonorDeferralsForDonorByDeferralReason(donor, deferralReason))
        .thenReturn(Collections.<DonorDeferral>emptyList());

    DonorDeferral returnedDonorDeferral = donorDeferralCRUDService.createDeferralForDonorWithVenueAndDeferralReasonType(
        donor, irrelevantVenue, irrelevantDeferralReasonType);

    verify(donorDeferralRepository).save(argThat(hasSameStateAsDonorDeferral(expectedDonorDeferral)));
    assertThat(returnedDonorDeferral, hasSameStateAsDonorDeferral(expectedDonorDeferral));
  }

  @Test
  public void testCreateDeferralForDonorWithDeferralReasonTypeWithPermanentDeferralReasonAndExistingDeferral_shouldReturnExistingDonorDeferral() {

    UUID locationId = UUID.randomUUID();
    Location irrelevantVenue = LocationBuilder.aLocation().withId(locationId).withName("Test Location").build();
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

    DonorDeferral returnedDonorDeferral = donorDeferralCRUDService.createDeferralForDonorWithVenueAndDeferralReasonType(
        donor, irrelevantVenue, irrelevantDeferralReasonType);

    verify(donorDeferralRepository, never()).save(any(DonorDeferral.class));
    assertThat(returnedDonorDeferral, hasSameStateAsDonorDeferral(expectedDonorDeferral));
  }

  @Test
  public void testCreateDeferralForDonorWithDeferralReasonTypeWithTemporaryDeferralReason_shouldCreateAndPersistDonorDeferral() {

    UUID locationId = UUID.randomUUID();
    Location irrelevantVenue = LocationBuilder.aLocation().withId(locationId).withName("Test Location").build();
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
        .withDeferralDate(now)
        .withVenue(irrelevantVenue)
        .withDeferralReason(deferralReason)
        .withDeferredUntil(new DateTime(now).plusDays(irrelevantDuration).toDate())
        .build();

    when(deferralReasonRepository.findDeferralReasonByType(irrelevantDeferralReasonType))
        .thenReturn(deferralReason);
    when(dateGeneratorService.generateDate()).thenReturn(now);

    DonorDeferral returnedDonorDeferral = donorDeferralCRUDService.createDeferralForDonorWithVenueAndDeferralReasonType(
        donor, irrelevantVenue, irrelevantDeferralReasonType);

    verify(donorDeferralRepository).save(argThat(hasSameStateAsDonorDeferral(expectedDonorDeferral)));
    assertThat(returnedDonorDeferral, hasSameStateAsDonorDeferral(expectedDonorDeferral));
  }

  @Test
  public void testFindDeferralById() throws Exception {
    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(UUID.randomUUID()).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.TEMPORARY)
        .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral =
        DonorDeferralBuilder.aDonorDeferral().withId(DONOR_DEFERRAL_ID).withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferredUntil(new Date()).build();

    // set up mocks
    when(donorDeferralRepository.findDonorDeferralById(DONOR_DEFERRAL_ID)).thenReturn(donorDeferral);

    // run tests
    DonorDeferral returnedDonorDeferral = donorDeferralCRUDService.findDeferralById(DONOR_DEFERRAL_ID);

    // asserts
    Assert.assertNotNull("Deferral returned", returnedDonorDeferral);
    Assert.assertEquals("Deferral matches", DONOR_DEFERRAL_ID, returnedDonorDeferral.getId());
  }

  @Test
  public void testDeleteDeferral() throws Exception {
    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(UUID.randomUUID()).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.TEMPORARY)
        .withType(DeferralReasonType.NORMAL).build();
    DonorDeferral donorDeferral =
        DonorDeferralBuilder.aDonorDeferral().withId(DONOR_DEFERRAL_ID).withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferredUntil(new Date()).build();

    // set up mocks
    when(donorDeferralRepository.findDonorDeferralById(DONOR_DEFERRAL_ID)).thenReturn(donorDeferral);

    // run tests
    donorDeferralCRUDService.deleteDeferral(DONOR_DEFERRAL_ID);

    // asserts
    Assert.assertTrue("Deferral was deleted", donorDeferral.getIsVoided());
    Assert.assertEquals("Deferral was deleted by", loggedInUser.getUsername(), donorDeferral.getVoidedBy().getUsername());
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Assert.assertEquals("Deferral was deleted when", sdf.format(new Date()), sdf.format(donorDeferral.getVoidedDate()));
  }

  @Test(expected = IllegalStateException.class)
  public void testUpdateDeferralWithEditingConstraints_shouldThrow() throws Exception {
    // create test data
    DeferralReason permanentDeferralReason = DeferralReasonBuilder.aDeferralReason()
        .withDurationType(DurationType.PERMANENT)
        .build();
    DonorDeferral permanentDonorDeferral = DonorDeferralBuilder.aDonorDeferral()
        .withId(DONOR_DEFERRAL_ID)
        .withDeferralReason(permanentDeferralReason)
        .build();

    // set up mocks
    when(donorDeferralRepository.findDonorDeferralById(DONOR_DEFERRAL_ID)).thenReturn(permanentDonorDeferral);
    when(deferralConstraintChecker.canEditDonorDeferral(DONOR_DEFERRAL_ID)).thenReturn(false);

    // run tests
    donorDeferralCRUDService.updateDeferral(permanentDonorDeferral);
  }
  
  @Test
  public void testUpdateDeferral_shouldUpdateCorrectly() throws Exception {
    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(UUID.randomUUID()).withFirstName("Sample").withLastName("Donor").build();
    DeferralReason originalDeferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.TEMPORARY)
        .withType(DeferralReasonType.NORMAL).withReason("test1").build();
    Date createdDate = new Date();
    DonorDeferral originalDonorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(DONOR_DEFERRAL_ID)
        .withDeferralDate(createdDate)
        .withDeferredDonor(deferredDonor).withDeferralReason(originalDeferralReason).withDeferredUntil(new Date()).build();

    Date newDeferredUntil = new SimpleDateFormat("yyyy-MM-dd").parse("2020-10-20");
    DeferralReason updatedDeferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.TEMPORARY)
        .withType(DeferralReasonType.NORMAL).withReason("test2").build();
    DonorDeferral updatedDonorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(DONOR_DEFERRAL_ID)
        .withDeferralDate(createdDate)
        .withDeferredDonor(deferredDonor).withDeferralReason(updatedDeferralReason).withDeferralReasonText("hello").withDeferredUntil(newDeferredUntil).build();

    // set up mocks
    when(donorDeferralRepository.findDonorDeferralById(DONOR_DEFERRAL_ID)).thenReturn(originalDonorDeferral);
    when(donorDeferralRepository.update(originalDonorDeferral)).thenReturn(updatedDonorDeferral);
    when(deferralConstraintChecker.canEditDonorDeferral(DONOR_DEFERRAL_ID)).thenReturn(true);

    // run tests
    DonorDeferral savedDonorDeferral = donorDeferralCRUDService.updateDeferral(updatedDonorDeferral);

    // asserts - note: due to the use of mocks, there is not much that can be asserted usefully.
    Assert.assertNotNull("Saved DonorDeferral returned", savedDonorDeferral);
  }

  @Test
  public void testEndDeferral() throws Exception {
    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(UUID.randomUUID()).withFirstName("Sample").withLastName("Donor").build();
    Date newDeferredUntil = new SimpleDateFormat("yyyy-MM-dd").parse("2020-10-20");
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.PERMANENT)
        .withType(DeferralReasonType.NORMAL).withReason("test2").build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(DONOR_DEFERRAL_ID)
        .withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferralReasonText("hello").withDeferredUntil(newDeferredUntil).build();

    // set up mocks
    when(deferralConstraintChecker.canEndDonorDeferral(DONOR_DEFERRAL_ID)).thenReturn(true);
    when(donorDeferralRepository.findDonorDeferralById(DONOR_DEFERRAL_ID)).thenReturn(donorDeferral);
    when(donorDeferralRepository.update(donorDeferral)).thenReturn(donorDeferral);

    // run tests
    DonorDeferral endedDonorDeferral = donorDeferralCRUDService.endDeferral(DONOR_DEFERRAL_ID, "the end");

    // asserts - note: due to the use of mocks, there is not much that can be asserted usefully.
    Assert.assertNotNull("Saved DonorDeferral returned", endedDonorDeferral);
  }

  @Test(expected = java.lang.IllegalStateException.class)
  public void testEndDeferralWithConstraints() throws Exception {
    // create test data
    Donor deferredDonor = DonorBuilder.aDonor().withId(UUID.randomUUID()).withFirstName("Sample").withLastName("Donor").build();
    Date newDeferredUntil = new SimpleDateFormat("yyyy-MM-dd").parse("2020-10-20");
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDurationType(DurationType.PERMANENT)
        .withType(DeferralReasonType.NORMAL).withReason("test2").build();
    DonorDeferral donorDeferral = DonorDeferralBuilder.aDonorDeferral().withId(DONOR_DEFERRAL_ID)
        .withDeferralDate(new Date())
        .withDeferredDonor(deferredDonor).withDeferralReason(deferralReason).withDeferralReasonText("hello").withDeferredUntil(newDeferredUntil).build();

    // set up mocks
    when(donorDeferralRepository.findDonorDeferralById(DONOR_DEFERRAL_ID)).thenReturn(donorDeferral);
    when(donorDeferralRepository.update(donorDeferral)).thenReturn(donorDeferral);

    // run tests
    donorDeferralCRUDService.endDeferral(DONOR_DEFERRAL_ID, "the end");
  }

  @Test
  public void testAppendCommentEmpty() throws Exception {
    DonorDeferral deferral =
        DonorDeferralBuilder.aDonorDeferral().withId(DONOR_DEFERRAL_ID).withDeferralReasonText("").build();
    donorDeferralCRUDService.appendComment(deferral, "test");
    Assert.assertEquals("Comment updated", "test", deferral.getDeferralReasonText());
  }

  @Test
  public void testAppendCommentNull() throws Exception {
    DonorDeferral deferral =
        DonorDeferralBuilder.aDonorDeferral().withId(DONOR_DEFERRAL_ID).withDeferralReasonText(null).build();
    donorDeferralCRUDService.appendComment(deferral, "test");
    Assert.assertEquals("Comment updated", "test", deferral.getDeferralReasonText());
  }

  @Test
  public void testAppendComment() throws Exception {
    DonorDeferral deferral =
        DonorDeferralBuilder.aDonorDeferral().withId(DONOR_DEFERRAL_ID).withDeferralReasonText("hello").build();
    donorDeferralCRUDService.appendComment(deferral, "world");
    Assert.assertEquals("Comment updated", "hello. world", deferral.getDeferralReasonText());
  }

  @Test
  public void testAppendCommentPeriod() throws Exception {
    DonorDeferral deferral =
        DonorDeferralBuilder.aDonorDeferral().withId(DONOR_DEFERRAL_ID).withDeferralReasonText("hello. ").build();
    donorDeferralCRUDService.appendComment(deferral, "world");
    Assert.assertEquals("Comment updated", "hello. world", deferral.getDeferralReasonText());
  }
}
