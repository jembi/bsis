package org.jembi.bsis.backingform.validator;

import static org.jembi.bsis.helpers.builders.LocationBuilder.aProcessingSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.jembi.bsis.backingform.BloodTransportBoxBackingForm;
import org.jembi.bsis.backingform.ComponentBatchBackingForm;
import org.jembi.bsis.backingform.DonationBatchBackingForm;
import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.repository.FormFieldRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

public class ComponentBatchBackingFormValidatorTest extends UnitTestSuite {

  private static final UUID DONATION_BATCH_ID = UUID.randomUUID();

  @InjectMocks
  private ComponentBatchBackingFormValidator validator;
  
  @Mock
  FormFieldRepository formFieldRepository;
  
  @Mock
  private DonationBatchRepository donationBatchRepository;
  
  @Mock
  private LocationRepository locationRepository;
  
  @Mock
  private BloodTransportBoxBackingFormValidator bloodTransportBoxBackingFormValidator;
  
  @Test
  public void testValidate_hasNoErrors() throws Exception {
    // set up data
    UUID donationBatchId = UUID.randomUUID();
    UUID locationId = UUID.randomUUID();

    ComponentBatchBackingForm form = new ComponentBatchBackingForm();
    form.setDonationBatch(new DonationBatchBackingForm());
    form.getDonationBatch().setId(donationBatchId);
    form.setDeliveryDate(new Date());
    form.setBloodTransportBoxes(new ArrayList<BloodTransportBoxBackingForm>());
    form.getBloodTransportBoxes().add(new BloodTransportBoxBackingForm());
    LocationBackingForm locationForm = new LocationBackingForm();
    locationForm.setId(locationId);
    form.setLocation(locationForm);
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentBatch");
    
    // set up mocks
    when(donationBatchRepository.verifyDonationBatchExists(donationBatchId)).thenReturn(true);
    when(locationRepository.getLocation(locationId)).thenReturn(aProcessingSite().withId(locationId).build());
    
    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }
  
  @Test
  public void testValidate_hasNoDonationBatch() throws Exception {
    // set up data
    ComponentBatchBackingForm form = new ComponentBatchBackingForm();
    LocationBackingForm locationForm = new LocationBackingForm();
    UUID locationId = UUID.randomUUID();
    locationForm.setId(locationId);
    form.setLocation(locationForm);
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentBatch");
    
    // set up mocks
    when(locationRepository.getLocation(locationId)).thenReturn(aProcessingSite().withId(locationId).build());
    
    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: No Donation Batch", errors.getFieldError("componentBatch.donationBatch"));
  }
  
  @Test
  public void testValidate_hasDonationBatchNoId() throws Exception {
    // set up data
    ComponentBatchBackingForm form = new ComponentBatchBackingForm();
    form.setDonationBatch(new DonationBatchBackingForm());
    LocationBackingForm locationForm = new LocationBackingForm();
    UUID locationId = UUID.randomUUID();
    locationForm.setId(locationId);
    form.setLocation(locationForm);
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentBatch");
    
    // set up mocks
    when(locationRepository.getLocation(locationId)).thenReturn(aProcessingSite().withId(locationId).build());
    
    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: No Donation Batch", errors.getFieldError("componentBatch.donationBatch"));
  }

  @Test
  public void testValidate_donationBatchDoesntExist() throws Exception {
    // set up data
    ComponentBatchBackingForm form = new ComponentBatchBackingForm();
    form.setDonationBatch(new DonationBatchBackingForm());
    form.getDonationBatch().setId(DONATION_BATCH_ID);
    LocationBackingForm locationForm = new LocationBackingForm();
    UUID locationId = UUID.randomUUID();
    locationForm.setId(locationId);
    form.setLocation(locationForm);
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentBatch");
    
    // set up mocks
    when(donationBatchRepository.verifyDonationBatchExists(DONATION_BATCH_ID)).thenReturn(false);
    when(locationRepository.getLocation(locationId)).thenReturn(aProcessingSite().withId(locationId).build());
    
    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: No Donation Batch", errors.getFieldError("componentBatch.donationBatch"));
  }
  
  @Test
  public void testValidate_hasNoLocation() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    ComponentBatchBackingForm form = new ComponentBatchBackingForm();
    form.setDonationBatch(new DonationBatchBackingForm());
    form.getDonationBatch().setId(DONATION_BATCH_ID);
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentBatch");
    
    // set up mocks
    when(donationBatchRepository.verifyDonationBatchExists(DONATION_BATCH_ID)).thenReturn(true);
    when(locationRepository.getLocation(locationId)).thenReturn(aProcessingSite().withId(locationId).build());
    
    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: No Location", errors.getFieldError("componentBatch.location"));
  }
  
  @Test
  public void testValidate_hasLocationNoId() throws Exception {
    // set up data
    UUID locationId = UUID.randomUUID();
    ComponentBatchBackingForm form = new ComponentBatchBackingForm();
    form.setDonationBatch(new DonationBatchBackingForm());
    form.getDonationBatch().setId(DONATION_BATCH_ID);
    LocationBackingForm locationForm = new LocationBackingForm();
    form.setLocation(locationForm);
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentBatch");
    
    // set up mocks
    when(donationBatchRepository.verifyDonationBatchExists(DONATION_BATCH_ID)).thenReturn(true);
    when(locationRepository.getLocation(locationId)).thenReturn(aProcessingSite().withId(locationId).build());
    
    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: No Location", errors.getFieldError("componentBatch.location"));
  }
  
  @Test
  public void testValidate_locationDoesntExist() throws Exception {
    // set up data
    ComponentBatchBackingForm form = new ComponentBatchBackingForm();
    form.setDonationBatch(new DonationBatchBackingForm());
    form.getDonationBatch().setId(DONATION_BATCH_ID);
    LocationBackingForm locationForm = new LocationBackingForm();
    UUID locationId = UUID.randomUUID();
    locationForm.setId(locationId);
    form.setLocation(locationForm);
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentBatch");
    
    // set up mocks
    when(donationBatchRepository.verifyDonationBatchExists(DONATION_BATCH_ID)).thenReturn(true);
    when(locationRepository.getLocation(locationId)).thenReturn(null);
    
    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: No Location", errors.getFieldError("componentBatch.location"));
  }
  
  @Test
  public void testValidate_locationIsDeleted() throws Exception {
    // set up data
    ComponentBatchBackingForm form = new ComponentBatchBackingForm();
    form.setDonationBatch(new DonationBatchBackingForm());
    form.getDonationBatch().setId(DONATION_BATCH_ID);
    LocationBackingForm locationForm = new LocationBackingForm();
    UUID locationId = UUID.randomUUID();
    locationForm.setId(locationId);
    form.setLocation(locationForm);
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentBatch");
    
    // set up mocks
    when(donationBatchRepository.verifyDonationBatchExists(DONATION_BATCH_ID)).thenReturn(true);
    when(locationRepository.getLocation(locationId))
        .thenReturn(aProcessingSite().thatIsDeleted().withId(locationId).build());
    
    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: No Location", errors.getFieldError("componentBatch.location"));
  }
  
  @Test
  public void testValidate_locationIsNotAProcessingSite() throws Exception {
    // set up data
    ComponentBatchBackingForm form = new ComponentBatchBackingForm();
    form.setDonationBatch(new DonationBatchBackingForm());
    form.getDonationBatch().setId(DONATION_BATCH_ID);
    LocationBackingForm locationForm = new LocationBackingForm();
    UUID locationId = UUID.randomUUID();
    locationForm.setId(locationId);
    form.setLocation(locationForm);
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentBatch");
    
    // set up mocks
    when(donationBatchRepository.verifyDonationBatchExists(DONATION_BATCH_ID)).thenReturn(true);
    when(locationRepository.getLocation(locationId)).thenReturn(aVenue().withId(locationId).build());
    
    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: No Location", errors.getFieldError("componentBatch.location"));
  }
}
