package backingform.validator;

import static org.mockito.Mockito.when;
import helpers.builders.DonationBatchBuilder;
import helpers.builders.DonationBuilder;
import helpers.builders.DonationTypeBuilder;
import helpers.builders.DonorBuilder;
import helpers.builders.FormFieldBuilder;
import helpers.builders.LocationBuilder;
import helpers.builders.PackTypeBuilder;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import model.donation.Donation;
import model.donation.HaemoglobinLevel;
import model.donationbatch.DonationBatch;
import model.donationtype.DonationType;
import model.donor.Donor;
import model.location.Location;
import model.packtype.PackType;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import repository.DonationBatchRepository;
import repository.DonationRepository;
import repository.DonorRepository;
import repository.FormFieldRepository;
import repository.SequenceNumberRepository;
import service.GeneralConfigAccessorService;
import backingform.DonationBackingForm;

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
  
  @Test
  public void testValidInsertNewDonationWithSpecifiedDINAndDonationDate() throws Exception {
    // set up data
    DonationBackingForm form = createBasicBackingForm();
    Date donationDate = form.getDonationDate();

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
    Assert.assertEquals("DIN wasn't updated", "DIN123", form.getDonationIdentificationNumber());
    Assert.assertEquals("DonationDate wasn't updated", donationDate, form.getDonationDate());
  }
  
  @Test
  public void testValidInsertNewDonationWithoutSpecifiedDINAndDonationDate() throws Exception {
    // set up data
    DonationBackingForm form = createBasicBackingForm();
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
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
    Assert.assertEquals("DIN was generated", "DIN234", form.getDonationIdentificationNumber());
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Assert.assertEquals("DonationDate was set to today", sdf.format(new Date()), sdf.format(form.getDonationDate()));
  }
  
  @Test
  public void testValidInsertNewDonationWithoutSpecifiedDINAndDonationDateDontGenerate() throws Exception {
    // set up data
    DonationBackingForm form = createBasicBackingForm();
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
  public void testInvalidDINAlreadyExists() throws Exception {
    // set up data
    DonationBackingForm form = createBasicBackingForm();
    Donation otherDonation = DonationBuilder.aDonation().withId(2L).build();

    // set up mocks
    when(donationRepository.findDonationByDonationIdentificationNumberIncludeDeleted("DIN123")).thenReturn(otherDonation);
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
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
    DonationBackingForm form = createBasicBackingForm();
    form.setBleedStartTime(null);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
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
    DonationBackingForm form = createBasicBackingForm();
    form.setBleedEndTime(null);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
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
    DonationBackingForm form = createBasicBackingForm();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    form.setBleedStartTime(sdf.parse("2016-01-01 15:00"));
    form.setBleedEndTime(sdf.parse("2016-01-01 13:00"));

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
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
    DonationBackingForm form = createBasicBackingForm();
    form.setDonorNumber(null);

    // set up mocks
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
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
    DonationBackingForm form = createBasicBackingForm();

    // set up mocks
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
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
    DonationBackingForm form = createBasicBackingForm();
    form.getDonationBatch().setBatchNumber(null);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
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
    DonationBackingForm form = createBasicBackingForm();

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field donationBatch", errors.getFieldError("donation.donationBatch"));
  }
  
  @Test
  public void testInvalidVenueEmpty() throws Exception {
    // set up data
    DonationBackingForm form = createBasicBackingForm();
    form.setVenue(null);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
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
    DonationBackingForm form = createBasicBackingForm();
    form.getDonation().getVenue().setIsVenue(false);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
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
    DonationBackingForm form = createBasicBackingForm();
    form.setPackType(null);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
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
    DonationBackingForm form = createBasicBackingForm();
    form.setDonationType(null);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field donationType", errors.getFieldError("donation.donationType"));
  }
  
  @Test
  public void testInvalidBloodPressureAboveMax() throws Exception {
    // set up data
    DonationBackingForm form = createBasicBackingForm();
    form.setBloodPressureDiastolic(101);
    form.setBloodPressureSystolic(191);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
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
    DonationBackingForm form = createBasicBackingForm();
    form.setBloodPressureDiastolic(39);
    form.setBloodPressureSystolic(69);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
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
    DonationBackingForm form = createBasicBackingForm();
    form.setHaemoglobinCount(BigDecimal.valueOf(0));

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
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
    DonationBackingForm form = createBasicBackingForm();
    form.setHaemoglobinCount(BigDecimal.valueOf(26));

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
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
    DonationBackingForm form = createBasicBackingForm();
    form.setDonorWeight(BigDecimal.valueOf(400));

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
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
    DonationBackingForm form = createBasicBackingForm();
    form.setDonorWeight(BigDecimal.valueOf(20));

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
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
    DonationBackingForm form = createBasicBackingForm();
    form.setDonorPulse(20);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
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
    DonationBackingForm form = createBasicBackingForm();
    form.setDonorPulse(300);

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
    mockGeneralConfigAndFormFields();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field donorPulse", errors.getFieldError("donation.donorPulse"));
  }

  @Test
  public void testValidUpdateExistingDonation() throws Exception {
    // set up data
    DonationBackingForm form = createBasicBackingForm();
    Donation donation = form.getDonation();
    donation.setId(1L);

    // set up mocks
    when(donationRepository.findDonationByDonationIdentificationNumberIncludeDeleted("DIN123")).thenReturn(form.getDonation());
    when(donorRepository.findDonorByDonorNumber("DN123", false)).thenReturn(form.getDonor());
    when(donationBatchRepository.findDonationBatchByBatchNumber("DB123")).thenReturn(form.getDonationBatch());
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
    DonationBackingForm form = createBasicBackingForm();
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
    mockGeneralConfigAndFormFields();    

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donation");
    donationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
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
    when(formFieldRepository.getRequiredFormFields("donation")).thenReturn(new ArrayList<String>());
    when(formFieldRepository.getFieldMaxLengths("donation")).thenReturn(new HashMap<String, Integer>());    
  }
  
  private DonationBackingForm createBasicBackingForm() throws Exception {
    Location venue = LocationBuilder.aLocation().withId(1L).thatIsVenue().build();
    PackType packType = PackTypeBuilder.aPackType().withId(1L).withPackType("Single").build();
    DonationType donationType = DonationTypeBuilder.aDonationType().withId(1L).withName("Voluntary").build();

    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch()
        .withBatchNumber("DB123")
        .withVenue(venue)
        .withDonations(new ArrayList<Donation>())
        .build();
    
    Donor donor = DonorBuilder.aDonor().withId(1L).withFirstName("David").withLastName("Smith").withDonorNumber("DN123").build();
    
    DonationBackingForm form = new DonationBackingForm();
    form.setDonorNumber("DN123");
    form.setDonationBatch(donationBatch);
    form.setDonor(donor);
    form.setDonationIdentificationNumber("DIN123");
    form.setPackType(packType);
    form.setDonationType(donationType);
    Date donationDate = new SimpleDateFormat("yyyy-MM-dd").parse("2016-01-01");
    form.setDonationDate(donationDate);
    form.setBleedStartTime(new Date());
    form.setBleedEndTime(new Date());
    form.setDonationBatchNumber("DB123");
    form.setVenue(venue);
    form.setBloodPressureDiastolic(80);
    form.setBloodPressureSystolic(100);
    form.setHaemoglobinCount(BigDecimal.valueOf(12));
    form.setHaemoglobinLevel(HaemoglobinLevel.PASS);
    form.setDonorWeight(BigDecimal.valueOf(80));
    form.setDonorPulse(100);

    return form;
  }
}
