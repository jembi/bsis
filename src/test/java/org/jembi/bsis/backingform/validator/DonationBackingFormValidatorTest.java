package org.jembi.bsis.backingform.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonationTypeBackingFormBuilder.aDonationTypeBackingForm;
import static org.jembi.bsis.helpers.builders.DonationTypeBuilder.aDonationType;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aLocationBackingForm;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.persistence.NoResultException;

import org.jembi.bsis.backingform.DonationBackingForm;
import org.jembi.bsis.backingform.DonationTypeBackingForm;
import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.helpers.builders.DonationBatchBuilder;
import org.jembi.bsis.helpers.builders.DonationBuilder;
import org.jembi.bsis.helpers.builders.DonorBuilder;
import org.jembi.bsis.helpers.builders.FormFieldBuilder;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.helpers.builders.PackTypeBuilder;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.HaemoglobinLevel;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.repository.FormFieldRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.repository.SequenceNumberRepository;
import org.jembi.bsis.service.DonorDeferralStatusCalculator;
import org.jembi.bsis.service.GeneralConfigAccessorService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

@RunWith(MockitoJUnitRunner.class)
public class DonationBackingFormValidatorTest {

  @InjectMocks
  DonationBackingFormValidator donationBackingFormValidator;
  @Mock
  private DonationRepository donationRepository;
  @Mock
  private DonorRepository donorRepository;
  @Mock
  private DonationBatchRepository donationBatchRepository;
  @Mock
  FormFieldRepository formFieldRepository;
  @Mock
  private GeneralConfigAccessorService generalConfigAccessorService;
  @Mock
  private AdverseEventBackingFormValidator adverseEventBackingFormValidator;
  @Mock
  private SequenceNumberRepository sequenceNumberRepository;
  @Mock
  private LocationRepository locationRepository;
  @Mock
  private DonorDeferralStatusCalculator donorDeferralStatusCalculator;
  
  @Test
  public void testValidInsertNewDonationWithSpecifiedDINAndDonationDate() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    Date donationDate = form.getDonationDate();

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
    Assert.assertEquals("DIN wasn't updated", "DIN1234", form.getDonationIdentificationNumber());
    Assert.assertEquals("DonationDate wasn't updated", donationDate, form.getDonationDate());
  }
  
  @Test
  public void testValidInsertNewDonationWithoutSpecifiedDINAndDonationDate() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.setDonationDate(null);
    form.setDonationIdentificationNumber(null);

    // set up mocks
    when(formFieldRepository.getFormField("donation", "donationIdentificationNumber")).thenReturn(FormFieldBuilder.aFormField()
        .withAutoGenerate(true).build());
    when(sequenceNumberRepository.getNextDonationIdentificationNumber()).thenReturn("DIN234");
    when(formFieldRepository.getFormField("donation", "donationDate")).thenReturn(FormFieldBuilder.aFormField()
        .withUseCurrentTime(true).build());
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertEquals("DIN was generated", "DIN234", form.getDonationIdentificationNumber());
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Assert.assertEquals("DonationDate was set to today", sdf.format(new Date()), sdf.format(form.getDonationDate()));
  }
  
  @Test
  public void testValidInsertNewDonationWithoutSpecifiedDINAndDonationDateDontGenerate() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.setDonationDate(null);
    form.setDonationIdentificationNumber(null);

    // set up mocks
    when(formFieldRepository.getFormField("donation", "donationIdentificationNumber")).thenReturn(FormFieldBuilder.aFormField()
        .withAutoGenerate(false).build());
    when(sequenceNumberRepository.getNextDonationIdentificationNumber()).thenReturn("DIN234");
    when(formFieldRepository.getFormField("donation", "donationDate")).thenReturn(FormFieldBuilder.aFormField()
        .withUseCurrentTime(false).build());
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
    Assert.assertEquals("DIN was not generated", null, form.getDonationIdentificationNumber());
    Assert.assertEquals("DonationDate was not set", null, form.getDonationDate());
  }
  
  @Test
  public void testValidUpdateDonationAfterConfigDINLengthPropertyChange() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.setId(UUID.randomUUID());
    form.setDonationIdentificationNumber("DIN5247");

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    mockGeneralConfigAndFormFields();
    // edit form and change DIN config value
    form.setLastUpdated(new Date());
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(10);
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    assertThat("Validation error count is zero", errors.getErrorCount() == 0);   
  }
  
  @Test
  public void testInvalidDINAlreadyExists() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    Donation otherDonation = DonationBuilder.aDonation().withId(UUID.randomUUID()).build();

    // set up mocks
    when(donationRepository.findDonationByDonationIdentificationNumberIncludeDeleted("DIN1234")).thenReturn(otherDonation);
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field donationIdentificationNumber", errors.getFieldError("donation.donationIdentificationNumber"));
  }
  
  @Test
  public void testInvalidNullBleedStartTime() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.setBleedStartTime(null);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field bleedStartTime", errors.getFieldError("donation.bleedStartTime"));
  }
  
  @Test
  public void testInvalidNullBleedEndTime() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.setBleedEndTime(null);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field bleedEndTime", errors.getFieldError("donation.bleedEndTime"));
  }
  
  @Test
  public void testInvalidBleedStartTimeAfterEndTime() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    form.setBleedStartTime(sdf.parse("2016-01-01 15:00"));
    form.setBleedEndTime(sdf.parse("2016-01-01 13:00"));

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field bleedEndTime", errors.getFieldError("donation"));
  }

  @Test
  public void testInvalidDonorNull() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.setDonorNumber(null);

    // set up mocks
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field donor", errors.getFieldError("donation.donor"));
  }
  
  @Test
  public void testInvalidDonorNotFound() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenThrow(new NoResultException());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field donor", errors.getFieldError("donation.donor"));
  }
  
  @Test
  public void testInvalidDonationBatchNull() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.getDonationBatch().setBatchNumber(null);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field donationBatch", errors.getFieldError("donation.donationBatch"));
  }
  
  @Test
  public void testInvalidDonationBatchNotFound() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenThrow(new NoResultException());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field donationBatch", errors.getFieldError("donation.donationBatch"));
  }
  
  @Test
  public void testInvalidVenueNull() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.setVenue(null);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field venue", errors.getFieldError("donation.venue"));
  }

  @Test
  public void testInvalidVenueNullId() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    venue.setId(null);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field venue", errors.getFieldError("donation.venue"));
  }
  
  @Test
  public void testInvalidVenue() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    venue.setIsVenue(false);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field venue", errors.getFieldError("donation.venue"));
  }

  @Test
  public void testInvalidLocation() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenThrow(new NoResultException());
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field venue", errors.getFieldError("donation.venue"));
  }
  
  @Test
  public void testInvalidPackTypeEmpty() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.setPackType(null);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field packType", errors.getFieldError("donation.packType"));
  }
  
  @Test
  public void testInvalidDonationTypeEmpty() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.setDonationType(null);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field donationType", errors.getFieldError("donation.donationType"));
  }

  @Test
  public void testInvalidDonationBeforeNextAllowedDate() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.getPackType().setPeriodBetweenDonations(20);
    form.getPackType().setCountAsDonation(Boolean.TRUE);
    Donation donation = aDonation()
        .withPackType(form.getPackType())
        .withDonationDate(new Date())
        .withBleedStartTime(new Date())
        .withBleedEndTime(new Date())
        .withDonationBatch(aDonationBatch().withId(UUID.randomUUID()).build())
        .withVenue(venue)
        .withDonationType(aDonationType().withId(UUID.randomUUID()).build())
        .withDonor(form.getDonor())
        .build();
    form.getDonor().setDonations(Arrays.asList(donation));

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Error exist", 1, errors.getErrorCount());
    Assert.assertEquals(errors.getFieldError("donation.donor").getCode(), "errors.invalid.donationBeforeNextAllowedDate");
    Assert.assertEquals(errors.getFieldError("donation.donor").getDefaultMessage(), "Selected donation Date is before donor's next allowed donation date");
  }

  @Test
  public void testUpdateOfExistingDonationBeforeNextAllowedDate_shouldNotHaveErrors() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.getPackType().setPeriodBetweenDonations(20);
    form.getPackType().setCountAsDonation(Boolean.TRUE);
    Donation donation = aDonation()
        .withPackType(form.getPackType())
        .withDonationDate(new Date())
        .withBleedStartTime(new Date())
        .withBleedEndTime(new Date())
        .withDonationBatch(aDonationBatch().withId(UUID.randomUUID()).build())
        .withVenue(venue)
        .withDonationType(aDonationType().withId(UUID.randomUUID()).build())
        .withDonor(form.getDonor())
        .build();
    form.getDonor().setDonations(Arrays.asList(donation));
    //A donation with an existing UUID is not a new one. Donor validation should then not execute on updating
    form.getDonation().setId(UUID.randomUUID());

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Error exist", 0, errors.getErrorCount());
  }

  @Test
  public void testHistoricalDonationBatchDonationBeforeNextAllowedDate() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.getDonationBatch().setBackEntry(true);
    form.getPackType().setPeriodBetweenDonations(20);
    form.getPackType().setCountAsDonation(Boolean.TRUE);
    Donation donation = aDonation()
        .withPackType(form.getPackType())
        .withDonationDate(new Date())
        .withBleedStartTime(new Date())
        .withBleedEndTime(new Date())
        .withDonationBatch(aDonationBatch().withId(UUID.randomUUID()).build())
        .withVenue(venue)
        .withDonationType(aDonationType().withId(UUID.randomUUID()).build())
        .withDonor(form.getDonor())
        .build();
    form.getDonor().setDonations(Arrays.asList(donation));

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No error exist", 0, errors.getErrorCount());
  }

  @Test
  public void testDonationBeforeNextAllowedDateWithPackTypeNotCountingAsDonation() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.getPackType().setPeriodBetweenDonations(20);
    form.getPackType().setCountAsDonation(Boolean.FALSE);
    Donation donation = aDonation()
        .withPackType(form.getPackType())
        .withDonationDate(new Date())
        .withBleedStartTime(new Date())
        .withBleedEndTime(new Date())
        .withDonationBatch(aDonationBatch().withId(UUID.randomUUID()).build())
        .withVenue(venue)
        .withDonationType(aDonationType().withId(UUID.randomUUID()).build())
        .withDonor(form.getDonor())
        .build();
    form.getDonor().setDonations(Arrays.asList(donation));

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testDonationAfterNextAllowedDate() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.getPackType().setPeriodBetweenDonations(20);
    form.getPackType().setCountAsDonation(Boolean.TRUE);
    Donation donation = aDonation()
        .withPackType(form.getPackType())
        .withDonationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2016-01-01"))
        .withBleedStartTime(new Date())
        .withBleedEndTime(new Date())
        .withDonationBatch(aDonationBatch().withId(UUID.randomUUID()).build())
        .withVenue(venue)
        .withDonationType(aDonationType().withId(UUID.randomUUID()).build())
        .withDonor(form.getDonor())
        .build();
    form.getDonor().setDonations(Arrays.asList(donation));

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testInvalidDonorDeferred() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.TRUE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Error exist", 1, errors.getErrorCount());
    Assert.assertEquals(errors.getFieldError("donation.donor").getCode(), "errors.invalid.donorDeferred");
    Assert.assertEquals(errors.getFieldError("donation.donor").getDefaultMessage(), "Donor is currently deferred");
  }

  @Test
  public void testUpdateOfExistingDonationOfDeferredDonor_shouldNotHaveErrors() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    //A donation with an existing UUID is not a new one. Donor validation should then not execute on updating
    form.getDonation().setId(UUID.randomUUID());

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.TRUE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Error exist", 0, errors.getErrorCount());
  }

  @Test
  public void testHistoricalDonationBatchDonorDeferred() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.getDonationBatch().setBackEntry(true);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.TRUE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No error exist", 0, errors.getErrorCount());
  }

  @Test
  public void testInvalidBloodPressureAboveMax() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.setBloodPressureDiastolic(101);
    form.setBloodPressureSystolic(191);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 2, errors.getErrorCount());
    Assert.assertNotNull("Error on field bloodPressureSystolic", errors.getFieldError("donation.bloodPressureSystolic"));
    Assert.assertNotNull("Error on field bloodPressureDiastolic", errors.getFieldError("donation.bloodPressureDiastolic"));
  }
  
  @Test
  public void testInvalidBloodPressureBelowMin() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.setBloodPressureDiastolic(39);
    form.setBloodPressureSystolic(69);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 2, errors.getErrorCount());
    Assert.assertNotNull("Error on field bloodPressureSystolic", errors.getFieldError("donation.bloodPressureSystolic"));
    Assert.assertNotNull("Error on field bloodPressureDiastolic", errors.getFieldError("donation.bloodPressureDiastolic"));
  }
  
  @Test
  public void testInvalidHaemoglobinCountBelowMin() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.setHaemoglobinCount(BigDecimal.valueOf(0));

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field haemoglobinCount", errors.getFieldError("donation.haemoglobinCount"));
  }
  
  @Test
  public void testInvalidHaemoglobinCountAboveMax() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.setHaemoglobinCount(BigDecimal.valueOf(26));

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field haemoglobinCount", errors.getFieldError("donation.haemoglobinCount"));
  }
  
  @Test
  public void testInvalidDonorWeightAbove() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.setDonorWeight(BigDecimal.valueOf(400));

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field donorWeight", errors.getFieldError("donation.donorWeight"));
  }
  
  @Test
  public void testInvalidDonorWeightBelow() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.setDonorWeight(BigDecimal.valueOf(20));

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field donorWeight", errors.getFieldError("donation.donorWeight"));
  }
  
  @Test
  public void testInvalidDonorPulseBelow() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.setDonorPulse(20);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field donorPulse", errors.getFieldError("donation.donorPulse"));
  }
  
  @Test
  public void testInvalidDonorPulseAbove() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.setDonorPulse(300);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field donorPulse", errors.getFieldError("donation.donorPulse"));
  }

  @Test
  public void testInvalidDinAboveMaximum() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.setDonationIdentificationNumber("DIN111111111111111111111111111111111111");

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
  }

  @Test
  public void testInvalidDinBelowMinimum() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.setDonationIdentificationNumber("DIN12");

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
  }

  @Test
  public void testInvalidDinLenthConfiguration_shouldDefaultTo20() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.setDonationIdentificationNumber("DIN12345678901234567"); // DIN of 20 characters

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);

    when(generalConfigAccessorService.getGeneralConfigValueByName("donation.donor.bpSystolicMin")).thenReturn("70");
    when(generalConfigAccessorService.getGeneralConfigValueByName("donation.donor.bpSystolicMax")).thenReturn("190");
    when(generalConfigAccessorService.getGeneralConfigValueByName("donation.donor.bpDiastolicMin")).thenReturn("40");
    when(generalConfigAccessorService.getGeneralConfigValueByName("donation.donor.bpDiastolicMax")).thenReturn("100");
    when(generalConfigAccessorService.getGeneralConfigValueByName("donation.donor.hbMin")).thenReturn("1");
    when(generalConfigAccessorService.getGeneralConfigValueByName("donation.donor.hbMax")).thenReturn("25");
    when(generalConfigAccessorService.getGeneralConfigValueByName("donation.donor.weightMin")).thenReturn("30");
    when(generalConfigAccessorService.getGeneralConfigValueByName("donation.donor.weightMax")).thenReturn("300");
    when(generalConfigAccessorService.getGeneralConfigValueByName("donation.donor.pulseMin")).thenReturn("30");
    when(generalConfigAccessorService.getGeneralConfigValueByName("donation.donor.pulseMax")).thenReturn("200");
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(35);
    when(formFieldRepository.getRequiredFormFields("donation")).thenReturn(Arrays.asList(new String[] {"packType", "donationType"}));
    when(formFieldRepository.getFieldMaxLengths("donation")).thenReturn(new HashMap<String, Integer>());
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testValidUpdateExistingDonation() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    Donation donation = form.getDonation();
    donation.setId(UUID.randomUUID());

    // set up mocks
    when(donationRepository.findDonationByDonationIdentificationNumberIncludeDeleted("DIN1234")).thenReturn(form.getDonation());
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }
  
  @Test
  public void testValidInsertMinimumDataRequired() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();
    DonationBackingForm form = createBasicBackingForm(venue);
    form.setDonationDate(null);
    form.setDonationIdentificationNumber(null);
    form.setDonorWeight(null);
    form.setDonorPulse(null);
    form.setBloodPressureDiastolic(null);
    form.setBloodPressureSystolic(null);
    form.setBloodPressureDiastolic(null);
    form.setBloodPressureSystolic(null);
    form.setHaemoglobinCount(null);
    form.setHaemoglobinLevel(null);

    // set up mocks
    when(formFieldRepository.getFormField("donation", "donationIdentificationNumber")).thenReturn(FormFieldBuilder.aFormField()
        .withAutoGenerate(true).build());
    when(sequenceNumberRepository.getNextDonationIdentificationNumber()).thenReturn("DIN234");
    when(formFieldRepository.getFormField("donation", "donationDate")).thenReturn(FormFieldBuilder.aFormField()
        .withUseCurrentTime(true).build());
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    when(locationRepository.getLocation(locationId)).thenReturn(venue);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(any(UUID.class))).thenReturn(Boolean.FALSE);
    mockGeneralConfigAndFormFields();    

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertEquals("DIN was generated", "DIN234", form.getDonationIdentificationNumber());
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Assert.assertEquals("DonationDate was set to today", sdf.format(new Date()), sdf.format(form.getDonationDate()));
  }

  private void mockGeneralConfigAndFormFields() {
    when(generalConfigAccessorService.getGeneralConfigValueByName("donation.donor.bpSystolicMin")).thenReturn("70");
    when(generalConfigAccessorService.getGeneralConfigValueByName("donation.donor.bpSystolicMax")).thenReturn("190");
    when(generalConfigAccessorService.getGeneralConfigValueByName("donation.donor.bpDiastolicMin")).thenReturn("40");
    when(generalConfigAccessorService.getGeneralConfigValueByName("donation.donor.bpDiastolicMax")).thenReturn("100");
    when(generalConfigAccessorService.getGeneralConfigValueByName("donation.donor.hbMin")).thenReturn("1");
    when(generalConfigAccessorService.getGeneralConfigValueByName("donation.donor.hbMax")).thenReturn("25");
    when(generalConfigAccessorService.getGeneralConfigValueByName("donation.donor.weightMin")).thenReturn("30");
    when(generalConfigAccessorService.getGeneralConfigValueByName("donation.donor.weightMax")).thenReturn("300");
    when(generalConfigAccessorService.getGeneralConfigValueByName("donation.donor.pulseMin")).thenReturn("30");
    when(generalConfigAccessorService.getGeneralConfigValueByName("donation.donor.pulseMax")).thenReturn("200");
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(7);
    when(formFieldRepository.getRequiredFormFields("donation")).thenReturn(Arrays.asList(new String[] {"packType", "donationType"}));
    when(formFieldRepository.getFieldMaxLengths("donation")).thenReturn(new HashMap<String, Integer>());    
  }
  
  private DonationBackingForm createBasicBackingForm(Location venue) throws Exception {
    UUID packTypeId = UUID.randomUUID();
    PackType packType = PackTypeBuilder.aPackType().withId(packTypeId).withPackType("Single").build();
    DonationTypeBackingForm donationType = aDonationTypeBackingForm().withId(UUID.randomUUID()).withDonationType("Voluntary").build();

    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch()
        .withBatchNumber("DB123")
        .withVenue(venue)
        .withDonations(new ArrayList<Donation>())
        .build();
    
    Donor donor = DonorBuilder.aDonor().withId(UUID.randomUUID()).withFirstName("David").withLastName("Smith").withDonorNumber("DN123").build();
    
    DonationBackingForm form = new DonationBackingForm();
    form.setDonorNumber("DN123");
    form.setDonationBatch(donationBatch);
    form.setDonor(donor);
    form.setDonationIdentificationNumber("DIN1234");
    form.setPackType(packType);
    form.setDonationType(donationType);
    Date donationDate = new SimpleDateFormat("yyyy-MM-dd").parse("2016-01-01");
    form.setDonationDate(donationDate);
    form.setBleedStartTime(new Date());
    form.setBleedEndTime(new Date());
    form.setDonationBatchNumber("DB123");
    form.setVenue(aLocationBackingForm().withId(venue.getId()).thatIsVenue().build());
    form.setBloodPressureDiastolic(80);
    form.setBloodPressureSystolic(100);
    form.setHaemoglobinCount(BigDecimal.valueOf(12));
    form.setHaemoglobinLevel(HaemoglobinLevel.PASS);
    form.setDonorWeight(BigDecimal.valueOf(80));
    form.setDonorPulse(100);

    return form;
  }
}
