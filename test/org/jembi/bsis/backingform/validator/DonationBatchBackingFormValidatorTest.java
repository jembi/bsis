package org.jembi.bsis.backingform.validator;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jembi.bsis.backingform.DonationBatchBackingForm;
import org.jembi.bsis.backingform.validator.DonationBatchBackingFormValidator;
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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

@RunWith(MockitoJUnitRunner.class)
public class DonationBatchBackingFormValidatorTest {

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

  @Test
  public void testValid() throws Exception {
    // set up data
    Location venue = LocationBuilder.aLocation().withId(1l).thatIsVenue().build();

    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch()
        .withVenue(venue)
        .withDonations(new ArrayList<Donation>())
        .build();

    DonationBatchBackingForm form = new DonationBatchBackingForm();
    form.setDonationBatch(donationBatch);

    List<DonationBatch> donationBatches = new ArrayList<>();

    // set up mocks
    FormField formField = FormFieldBuilder.aFormField().withAutoGenerate(true).build();
    when(formFieldRepository.getFormField("donationBatch", "batchNumber")).thenReturn(formField);
    when(sequenceNumberRepository.getNextBatchNumber()).thenReturn("BATCH1");
    when(donationBatchRepository.findDonationBatchByBatchNumberIncludeDeleted("BATCH1")).thenReturn(null);
    when(donationBatchRepository.findDonationBatches(false, Arrays.asList(new Long[]{1l}), null, null)).thenReturn(donationBatches);
    when(formFieldRepository.getRequiredFormFields("donationBatch")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("donationBatch")).thenReturn(new HashMap<String, Integer>());
    when(locationRepository.getLocation(venue.getId())).thenReturn(venue);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donationBatch");
    donationBatchBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testValidUpdateExisting() throws Exception {
    // set up data
    Location venue = LocationBuilder.aLocation().withId(1l).thatIsVenue().build();

    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch()
        .withId(1l)
        .withVenue(venue)
        .withDonations(new ArrayList<Donation>())
        .withBatchNumber("BATCH1")
        .build();

    DonationBatchBackingForm form = new DonationBatchBackingForm();
    form.setDonationBatch(donationBatch);

    List<DonationBatch> donationBatches = new ArrayList<>();

    // set up mocks
    when(donationBatchRepository.findDonationBatchByBatchNumberIncludeDeleted("BATCH1")).thenReturn(donationBatch);
    when(donationBatchRepository.findDonationBatches(false, Arrays.asList(new Long[]{1l}), null, null)).thenReturn(donationBatches);
    when(formFieldRepository.getRequiredFormFields("donationBatch")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("donationBatch")).thenReturn(new HashMap<String, Integer>());
    when(locationRepository.getLocation(venue.getId())).thenReturn(venue);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donationBatch");
    donationBatchBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testValidUpdateEmptyBatchNumber() throws Exception {
    // set up data
    Location venue = LocationBuilder.aLocation().withId(1l).thatIsVenue().build();

    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch()
        .withId(1l)
        .withVenue(venue)
        .withDonations(new ArrayList<Donation>())
        .build();

    DonationBatchBackingForm form = new DonationBatchBackingForm();
    form.setDonationBatch(donationBatch);
    form.setBatchNumber("");

    List<DonationBatch> donationBatches = new ArrayList<>();

    // set up mocks
    when(donationBatchRepository.findDonationBatches(false, Arrays.asList(new Long[]{1l}), null, null)).thenReturn(donationBatches);
    when(formFieldRepository.getRequiredFormFields("donationBatch")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("donationBatch")).thenReturn(new HashMap<String, Integer>());
    when(locationRepository.getLocation(venue.getId())).thenReturn(venue);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donationBatch");
    donationBatchBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testInvalidDuplicate() throws Exception {
    // set up data
    Location venue = LocationBuilder.aLocation().withId(1l).thatIsVenue().build();

    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch()
        .withId(1l)
        .withVenue(venue)
        .withDonations(new ArrayList<Donation>())
        .build();

    DonationBatch duplicate = DonationBatchBuilder.aDonationBatch().withId(2l).build();

    DonationBatchBackingForm form = new DonationBatchBackingForm();
    form.setDonationBatch(donationBatch);
    form.setBatchNumber("BATCH1");

    List<DonationBatch> donationBatches = new ArrayList<>();

    // set up mocks
    when(donationBatchRepository.findDonationBatchByBatchNumberIncludeDeleted("BATCH1")).thenReturn(duplicate);
    when(donationBatchRepository.findDonationBatches(false, Arrays.asList(new Long[]{1l}), null, null)).thenReturn(donationBatches);
    when(formFieldRepository.getRequiredFormFields("donationBatch")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("donationBatch")).thenReturn(new HashMap<String, Integer>());
    when(locationRepository.getLocation(venue.getId())).thenReturn(venue);

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

    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch()
        .withId(1l)
        .withVenue(venue)
        .withDonations(new ArrayList<Donation>())
        .build();

    DonationBatchBackingForm form = new DonationBatchBackingForm();
    form.setDonationBatch(donationBatch);
    form.setBatchNumber("BATCH1");

    List<DonationBatch> donationBatches = new ArrayList<>();

    // set up mocks
    when(donationBatchRepository.findDonationBatchByBatchNumberIncludeDeleted("BATCH1")).thenReturn(null);
    when(donationBatchRepository.findDonationBatches(false, Arrays.asList(new Long[]{1l}), null, null)).thenReturn(donationBatches);
    when(formFieldRepository.getRequiredFormFields("donationBatch")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("donationBatch")).thenReturn(new HashMap<String, Integer>());

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

    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch()
        .withId(1l)
        .withVenue(venue)
        .withDonations(new ArrayList<Donation>())
        .build();

    DonationBatchBackingForm form = new DonationBatchBackingForm();
    form.setDonationBatch(donationBatch);
    form.setBatchNumber("BATCH1");

    List<DonationBatch> donationBatches = new ArrayList<>();

    // set up mocks
    when(donationBatchRepository.findDonationBatchByBatchNumberIncludeDeleted("BATCH1")).thenReturn(null);
    when(donationBatchRepository.findDonationBatches(false, Arrays.asList(new Long[]{1l}), null, null)).thenReturn(donationBatches);
    when(formFieldRepository.getRequiredFormFields("donationBatch")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("donationBatch")).thenReturn(new HashMap<String, Integer>());

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
    Location venue = LocationBuilder.aLocation().withId(1l).build();

    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch()
        .withId(1l)
        .withVenue(venue)
        .withDonations(new ArrayList<Donation>())
        .build();

    DonationBatchBackingForm form = new DonationBatchBackingForm();
    form.setDonationBatch(donationBatch);
    form.setBatchNumber("BATCH1");

    List<DonationBatch> donationBatches = new ArrayList<>();

    // set up mocks
    when(donationBatchRepository.findDonationBatchByBatchNumberIncludeDeleted("BATCH1")).thenReturn(null);
    when(donationBatchRepository.findDonationBatches(false, Arrays.asList(new Long[]{1l}), null, null)).thenReturn(donationBatches);
    when(formFieldRepository.getRequiredFormFields("donationBatch")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("donationBatch")).thenReturn(new HashMap<String, Integer>());
    when(locationRepository.getLocation(venue.getId())).thenReturn(venue);

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
    Location venue = LocationBuilder.aLocation().withId(1l).thatIsVenue().build();

    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch()
        .withVenue(venue)
        .withDonations(new ArrayList<Donation>())
        .build();

    DonationBatchBackingForm form = new DonationBatchBackingForm();
    form.setDonationBatch(donationBatch);
    form.setBatchNumber("BATCH1");

    List<DonationBatch> donationBatches = Arrays.asList(new DonationBatch[]{DonationBatchBuilder.aDonationBatch().build()});

    // set up mocks
    when(donationBatchRepository.findDonationBatchByBatchNumberIncludeDeleted("BATCH1")).thenReturn(null);
    when(donationBatchRepository.findDonationBatches(false, Arrays.asList(new Long[]{1l}), null, null)).thenReturn(donationBatches);
    when(formFieldRepository.getRequiredFormFields("donationBatch")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("donationBatch")).thenReturn(new HashMap<String, Integer>());
    when(locationRepository.getLocation(venue.getId())).thenReturn(venue);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donationBatch");
    donationBatchBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: empty venue", errors.getFieldError("donationBatch.venue"));
  }
}
