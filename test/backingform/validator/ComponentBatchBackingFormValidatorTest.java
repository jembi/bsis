package backingform.validator;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import repository.DonationBatchRepository;
import repository.FormFieldRepository;
import repository.LocationRepository;
import backingform.BloodTransportBoxBackingForm;
import backingform.ComponentBatchBackingForm;
import backingform.DonationBatchBackingForm;
import backingform.LocationBackingForm;

@RunWith(MockitoJUnitRunner.class)
public class ComponentBatchBackingFormValidatorTest {

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
    ComponentBatchBackingForm form = new ComponentBatchBackingForm();
    form.setDonationBatch(new DonationBatchBackingForm());
    form.getDonationBatch().setId(1L);
    form.setDeliveryDate(new Date());
    form.setBloodTransportBoxes(new ArrayList<BloodTransportBoxBackingForm>());
    form.getBloodTransportBoxes().add(new BloodTransportBoxBackingForm());
    LocationBackingForm locationForm = new LocationBackingForm();
    locationForm.setId(1L);
    form.setLocation(locationForm);
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentBatch");
    
    // set up mocks
    when(donationBatchRepository.verifyDonationBatchExists(1L)).thenReturn(true);
    when(locationRepository.verifyLocationExists(1L)).thenReturn(true);
    
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
    locationForm.setId(1L);
    form.setLocation(locationForm);
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentBatch");
    
    // set up mocks
    when(locationRepository.verifyLocationExists(1L)).thenReturn(true);
    
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
    locationForm.setId(1L);
    form.setLocation(locationForm);
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentBatch");
    
    // set up mocks
    when(locationRepository.verifyLocationExists(1L)).thenReturn(true);
    
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
    form.getDonationBatch().setId(1L);
    LocationBackingForm locationForm = new LocationBackingForm();
    locationForm.setId(1L);
    form.setLocation(locationForm);
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentBatch");
    
    // set up mocks
    when(donationBatchRepository.verifyDonationBatchExists(1L)).thenReturn(false);
    when(locationRepository.verifyLocationExists(1L)).thenReturn(true);
    
    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: No Donation Batch", errors.getFieldError("componentBatch.donationBatch"));
  }
  
  @Test
  public void testValidate_hasNoLocation() throws Exception {
    // set up data
    ComponentBatchBackingForm form = new ComponentBatchBackingForm();
    form.setDonationBatch(new DonationBatchBackingForm());
    form.getDonationBatch().setId(1L);
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentBatch");
    
    // set up mocks
    when(donationBatchRepository.verifyDonationBatchExists(1L)).thenReturn(true);
    when(locationRepository.verifyLocationExists(1L)).thenReturn(true);
    
    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: No Location", errors.getFieldError("componentBatch.location"));
  }
  
  @Test
  public void testValidate_hasLocationNoId() throws Exception {
    // set up data
    ComponentBatchBackingForm form = new ComponentBatchBackingForm();
    form.setDonationBatch(new DonationBatchBackingForm());
    form.getDonationBatch().setId(1L);
    LocationBackingForm locationForm = new LocationBackingForm();
    form.setLocation(locationForm);
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentBatch");
    
    // set up mocks
    when(donationBatchRepository.verifyDonationBatchExists(1L)).thenReturn(true);
    when(locationRepository.verifyLocationExists(1L)).thenReturn(true);
    
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
    form.getDonationBatch().setId(1L);
    LocationBackingForm locationForm = new LocationBackingForm();
    locationForm.setId(1L);
    form.setLocation(locationForm);
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentBatch");
    
    // set up mocks
    when(donationBatchRepository.verifyDonationBatchExists(1L)).thenReturn(true);
    when(locationRepository.verifyLocationExists(1L)).thenReturn(false);
    
    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: No Location", errors.getFieldError("componentBatch.location"));
  }
}
