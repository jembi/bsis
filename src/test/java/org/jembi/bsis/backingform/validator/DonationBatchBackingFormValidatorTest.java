package org.jembi.bsis.backingform.validator;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.DonationBatchBackingForm;
import org.jembi.bsis.helpers.builders.DonationBatchBuilder;
import org.jembi.bsis.helpers.builders.FormFieldBuilder;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.model.admin.FormField;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.repository.FormFieldRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.repository.SequenceNumberRepository;
import org.jembi.bsis.service.DateGeneratorService;
import org.jembi.bsis.suites.UnitTestSuite;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

public class DonationBatchBackingFormValidatorTest extends UnitTestSuite {

  private static final UUID DONATION_BATCH_ID = UUID.randomUUID();

  @InjectMocks
  DonationBatchBackingFormValidator donationBatchBackingFormValidator;
  @Mock
  private DonationBatchRepository donationBatchRepository;
  @Mock
  private SequenceNumberRepository sequenceNumberRepository;
  @Mock
  private LocationRepository locationRepository;
  @Mock
  FormFieldRepository formFieldRepository;
  @Mock
  private DateGeneratorService dateGeneratorService;

  @Test
  public void testValid() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();

    Date donationBatchDate = new DateTime().toDate();
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch()
        .withVenue(venue)
        .withDonations(new ArrayList<Donation>())
        .withDonationBatchDate(donationBatchDate)
        .build();

    DonationBatchBackingForm form = new DonationBatchBackingForm();
    form.setDonationBatch(donationBatch);

    List<DonationBatch> donationBatches = new ArrayList<>();

    // set up mocks
    FormField formField = FormFieldBuilder.aFormField().withAutoGenerate(true).build();
    when(formFieldRepository.getFormField("donationBatch", "batchNumber")).thenReturn(formField);
    when(sequenceNumberRepository.getNextBatchNumber()).thenReturn("BATCH1");
    when(donationBatchRepository.findDonationBatchByBatchNumberIncludeDeleted("BATCH1")).thenReturn(null);
    when(donationBatchRepository.findDonationBatches(false, Arrays.asList(new UUID[]{locationId}), null, null)).thenReturn(donationBatches);
    when(formFieldRepository.getRequiredFormFields("donationBatch")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("donationBatch")).thenReturn(new HashMap<String, Integer>());
    when(locationRepository.getLocation(venue.getId())).thenReturn(venue);
    when(dateGeneratorService.generateDate(donationBatchDate)).thenReturn(new LocalDate().toDate());

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donationBatch");
    donationBatchBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testValidUpdateExisting() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();

    Date donationBatchDate = new DateTime().toDate();
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch()
        .withId(DONATION_BATCH_ID)
        .withVenue(venue)
        .withDonations(new ArrayList<Donation>())
        .withBatchNumber("BATCH1")
        .withDonationBatchDate(donationBatchDate)
        .build();

    DonationBatchBackingForm form = new DonationBatchBackingForm();
    form.setDonationBatch(donationBatch);

    List<DonationBatch> donationBatches = new ArrayList<>();

    // set up mocks
    when(donationBatchRepository.findDonationBatchByBatchNumberIncludeDeleted("BATCH1")).thenReturn(donationBatch);
    when(donationBatchRepository.findDonationBatches(false, Arrays.asList(new UUID[]{locationId}), null, null)).thenReturn(donationBatches);
    when(formFieldRepository.getRequiredFormFields("donationBatch")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("donationBatch")).thenReturn(new HashMap<String, Integer>());
    when(locationRepository.getLocation(venue.getId())).thenReturn(venue);
    when(dateGeneratorService.generateDate(donationBatchDate)).thenReturn(new LocalDate().toDate());

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donationBatch");
    donationBatchBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testValidUpdateEmptyBatchNumber() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();

    Date donationBatchDate = new DateTime().toDate();
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch()
        .withId(DONATION_BATCH_ID)
        .withVenue(venue)
        .withDonations(new ArrayList<Donation>())
        .withDonationBatchDate(donationBatchDate)
        .build();

    DonationBatchBackingForm form = new DonationBatchBackingForm();
    form.setDonationBatch(donationBatch);
    form.setBatchNumber("");

    List<DonationBatch> donationBatches = new ArrayList<>();

    // set up mocks
    when(donationBatchRepository.findDonationBatches(false, Arrays.asList(new UUID[]{locationId}), null, null)).thenReturn(donationBatches);
    when(formFieldRepository.getRequiredFormFields("donationBatch")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("donationBatch")).thenReturn(new HashMap<String, Integer>());
    when(locationRepository.getLocation(venue.getId())).thenReturn(venue);
    when(dateGeneratorService.generateDate(donationBatchDate)).thenReturn(new LocalDate().toDate());

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donationBatch");
    donationBatchBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testInvalidDuplicate() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();

    Date donationBatchDate = new DateTime().toDate();
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch()
        .withId(DONATION_BATCH_ID)
        .withVenue(venue)
        .withDonations(new ArrayList<Donation>())
        .withDonationBatchDate(donationBatchDate)
        .build();

    UUID donationBatchId2 = UUID.randomUUID();
    DonationBatch duplicate = DonationBatchBuilder.aDonationBatch().withId(donationBatchId2).build();

    DonationBatchBackingForm form = new DonationBatchBackingForm();
    form.setDonationBatch(donationBatch);
    form.setBatchNumber("BATCH1");

    List<DonationBatch> donationBatches = new ArrayList<>();

    // set up mocks
    when(donationBatchRepository.findDonationBatchByBatchNumberIncludeDeleted("BATCH1")).thenReturn(duplicate);
    when(donationBatchRepository.findDonationBatches(false, Arrays.asList(new UUID[]{locationId}), null, null)).thenReturn(donationBatches);
    when(formFieldRepository.getRequiredFormFields("donationBatch")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("donationBatch")).thenReturn(new HashMap<String, Integer>());
    when(locationRepository.getLocation(venue.getId())).thenReturn(venue);
    when(dateGeneratorService.generateDate(donationBatchDate)).thenReturn(new LocalDate().toDate());

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donationBatch");
    donationBatchBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: duplicate batch number", errors.getFieldError("donationBatch.batchNumber"));
  }

  @Test
  public void testInvalidEmptyVenue() throws Exception {
    // set up data
    Location venue = LocationBuilder.aLocation().build();

    Date donationBatchDate = new DateTime().toDate();
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch()
        .withId(DONATION_BATCH_ID)
        .withVenue(venue)
        .withDonations(new ArrayList<Donation>())
        .withDonationBatchDate(donationBatchDate)
        .build();

    DonationBatchBackingForm form = new DonationBatchBackingForm();
    form.setDonationBatch(donationBatch);
    form.setBatchNumber("BATCH1");

    List<DonationBatch> donationBatches = new ArrayList<>();

    // set up mocks
    when(donationBatchRepository.findDonationBatchByBatchNumberIncludeDeleted("BATCH1")).thenReturn(null);
    when(donationBatchRepository.findDonationBatches(false, Arrays.asList(new UUID[] {UUID.randomUUID()}), null, null))
        .thenReturn(donationBatches);
    when(formFieldRepository.getRequiredFormFields("donationBatch")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("donationBatch")).thenReturn(new HashMap<String, Integer>());
    when(dateGeneratorService.generateDate(donationBatchDate)).thenReturn(new LocalDate().toDate());

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donationBatch");
    donationBatchBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: empty venue", errors.getFieldError("donationBatch.venue"));
  }

  @Test
  public void testInvalidNullVenue() throws Exception {
    // set up data
    Location venue = null;

    Date donationBatchDate = new DateTime().toDate();
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch()
        .withId(DONATION_BATCH_ID)
        .withVenue(venue)
        .withDonations(new ArrayList<Donation>())
        .withDonationBatchDate(donationBatchDate)
        .build();

    DonationBatchBackingForm form = new DonationBatchBackingForm();
    form.setDonationBatch(donationBatch);
    form.setBatchNumber("BATCH1");

    List<DonationBatch> donationBatches = new ArrayList<>();

    // set up mocks
    when(donationBatchRepository.findDonationBatchByBatchNumberIncludeDeleted("BATCH1")).thenReturn(null);
    when(donationBatchRepository.findDonationBatches(false, Arrays.asList(new UUID[] {UUID.randomUUID()}), null, null))
        .thenReturn(donationBatches);
    when(formFieldRepository.getRequiredFormFields("donationBatch")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("donationBatch")).thenReturn(new HashMap<String, Integer>());
    when(dateGeneratorService.generateDate(donationBatchDate)).thenReturn(new LocalDate().toDate());

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donationBatch");
    donationBatchBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: empty venue", errors.getFieldError("donationBatch.venue"));
  }

  @Test
  public void testInvalidVenueNotLocation() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).build();

    Date donationBatchDate = new DateTime().toDate();
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch()
        .withId(DONATION_BATCH_ID)
        .withVenue(venue)
        .withDonations(new ArrayList<Donation>())
        .withDonationBatchDate(donationBatchDate)
        .build();

    DonationBatchBackingForm form = new DonationBatchBackingForm();
    form.setDonationBatch(donationBatch);
    form.setBatchNumber("BATCH1");

    List<DonationBatch> donationBatches = new ArrayList<>();

    // set up mocks
    when(donationBatchRepository.findDonationBatchByBatchNumberIncludeDeleted("BATCH1")).thenReturn(null);
    when(donationBatchRepository.findDonationBatches(false, Arrays.asList(new UUID[]{locationId}), null, null)).thenReturn(donationBatches);
    when(formFieldRepository.getRequiredFormFields("donationBatch")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("donationBatch")).thenReturn(new HashMap<String, Integer>());
    when(locationRepository.getLocation(venue.getId())).thenReturn(venue);
    when(dateGeneratorService.generateDate(donationBatchDate)).thenReturn(new LocalDate().toDate());

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donationBatch");
    donationBatchBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: empty venue", errors.getFieldError("donationBatch.venue"));
  }

  @Test
  public void testInvalidVenueOpenDonationBatches() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();

    Date donationBatchDate = new DateTime().toDate();
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch()
        .withVenue(venue)
        .withDonations(new ArrayList<Donation>())
        .withDonationBatchDate(donationBatchDate)
        .build();

    DonationBatchBackingForm form = new DonationBatchBackingForm();
    form.setDonationBatch(donationBatch);
    form.setBatchNumber("BATCH1");

    List<DonationBatch> donationBatches = Arrays.asList(new DonationBatch[]{DonationBatchBuilder.aDonationBatch().build()});

    // set up mocks
    when(donationBatchRepository.findDonationBatchByBatchNumberIncludeDeleted("BATCH1")).thenReturn(null);
    when(donationBatchRepository.findDonationBatches(false, Arrays.asList(new UUID[]{locationId}), null, null)).thenReturn(donationBatches);
    when(formFieldRepository.getRequiredFormFields("donationBatch")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("donationBatch")).thenReturn(new HashMap<String, Integer>());
    when(locationRepository.getLocation(venue.getId())).thenReturn(venue);
    when(dateGeneratorService.generateDate(donationBatchDate)).thenReturn(new LocalDate().toDate());

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donationBatch");
    donationBatchBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: empty venue", errors.getFieldError("donationBatch.venue"));
  }
  
  @Test
  public void testInvalidNullDonationBatchDate_shouldHaveOneError() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();

    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch().withId(DONATION_BATCH_ID).withVenue(venue)
        .withDonations(new ArrayList<Donation>()).withDonationBatchDate(null).build();

    DonationBatchBackingForm form = new DonationBatchBackingForm();
    form.setDonationBatch(donationBatch);
    form.setBatchNumber("BATCH1");

    List<DonationBatch> donationBatches = new ArrayList<>();

    // set up mocks
    when(donationBatchRepository.findDonationBatchByBatchNumberIncludeDeleted("BATCH1")).thenReturn(null);
    when(donationBatchRepository.findDonationBatches(false, Arrays.asList(new UUID[] {locationId}), null, null))
        .thenReturn(donationBatches);
    when(formFieldRepository.getRequiredFormFields("donationBatch")).thenReturn(Arrays.asList(new String[] {}));
    when(formFieldRepository.getFieldMaxLengths("donationBatch")).thenReturn(new HashMap<String, Integer>());
    when(locationRepository.getLocation(venue.getId())).thenReturn(venue);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donationBatch");
    donationBatchBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: empty donationBatchDate", errors.getFieldError("donationBatch.donationBatchDate"));
  }

  @Test
  public void testInvalidDonationBatchDateInTheFuture_shouldHaveOneError() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();

    Date donationBatchDate = (new DateTime()).plusDays(2).toDate();
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch()
        .withId(DONATION_BATCH_ID)
        .withVenue(venue)
        .withDonations(new ArrayList<Donation>())
        .withDonationBatchDate(donationBatchDate)
        .build();

    DonationBatchBackingForm form = new DonationBatchBackingForm();
    form.setDonationBatch(donationBatch);
    form.setBatchNumber("BATCH1");

    List<DonationBatch> donationBatches = new ArrayList<>();

    // set up mocks
    when(donationBatchRepository.findDonationBatchByBatchNumberIncludeDeleted("BATCH1")).thenReturn(null);
    when(donationBatchRepository.findDonationBatches(false, Arrays.asList(new UUID[]{locationId}), null, null)).thenReturn(donationBatches);
    when(formFieldRepository.getRequiredFormFields("donationBatch")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("donationBatch")).thenReturn(new HashMap<String, Integer>());
    when(locationRepository.getLocation(venue.getId())).thenReturn(venue);
    when(dateGeneratorService.generateDate(donationBatchDate)).thenReturn(new LocalDate().plusDays(2).toDate());

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donationBatch");
    donationBatchBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: donationBatchDate In future", errors.getFieldError("donationBatch.donationBatchDate"));
  }

  @Test
  public void testValidDonationBatchDateCurrentDate_shouldHaveNoError() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();

    Date donationBatchDate = (new DateTime()).toDate();
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch().withId(DONATION_BATCH_ID).withVenue(venue)
        .withDonations(new ArrayList<Donation>()).withDonationBatchDate(donationBatchDate).build();

    DonationBatchBackingForm form = new DonationBatchBackingForm();
    form.setDonationBatch(donationBatch);
    form.setBatchNumber("BATCH1");

    List<DonationBatch> donationBatches = new ArrayList<>();

    // set up mocks
    when(donationBatchRepository.findDonationBatchByBatchNumberIncludeDeleted("BATCH1")).thenReturn(null);
    when(donationBatchRepository.findDonationBatches(false, Arrays.asList(new UUID[] {locationId}), null, null))
        .thenReturn(donationBatches);
    when(formFieldRepository.getRequiredFormFields("donationBatch")).thenReturn(Arrays.asList(new String[] {}));
    when(formFieldRepository.getFieldMaxLengths("donationBatch")).thenReturn(new HashMap<String, Integer>());
    when(locationRepository.getLocation(venue.getId())).thenReturn(venue);
    when(dateGeneratorService.generateDate(donationBatchDate)).thenReturn(new LocalDate().toDate());

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donationBatch");
    donationBatchBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testValidDonationBatchDateInThePast_shouldHaveNoError() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    Location venue = LocationBuilder.aLocation().withId(locationId).thatIsVenue().build();

    Date donationBatchDate = (new DateTime()).minusDays(5).toDate();
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch().withId(DONATION_BATCH_ID).withVenue(venue)
        .withDonations(new ArrayList<Donation>()).withDonationBatchDate(donationBatchDate).build();

    DonationBatchBackingForm form = new DonationBatchBackingForm();
    form.setDonationBatch(donationBatch);
    form.setBatchNumber("BATCH1");

    List<DonationBatch> donationBatches = new ArrayList<>();

    // set up mocks
    when(donationBatchRepository.findDonationBatchByBatchNumberIncludeDeleted("BATCH1")).thenReturn(null);
    when(donationBatchRepository.findDonationBatches(false, Arrays.asList(new UUID[] {locationId}), null, null))
        .thenReturn(donationBatches);
    when(formFieldRepository.getRequiredFormFields("donationBatch")).thenReturn(Arrays.asList(new String[] {}));
    when(formFieldRepository.getFieldMaxLengths("donationBatch")).thenReturn(new HashMap<String, Integer>());
    when(locationRepository.getLocation(venue.getId())).thenReturn(venue);
    when(dateGeneratorService.generateDate(donationBatchDate)).thenReturn(new LocalDate().minusDays(5).toDate());

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donationBatch");
    donationBatchBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }
}
