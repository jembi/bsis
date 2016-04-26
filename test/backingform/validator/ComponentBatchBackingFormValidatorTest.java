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
import backingform.BloodTransportBoxBackingForm;
import backingform.ComponentBatchBackingForm;
import backingform.DonationBatchBackingForm;

@RunWith(MockitoJUnitRunner.class)
public class ComponentBatchBackingFormValidatorTest {

  @InjectMocks
  private ComponentBatchBackingFormValidator validator;
  
  @Mock
  FormFieldRepository formFieldRepository;
  
  @Mock
  private DonationBatchRepository donationBatchRepository;
  
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
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentBatch");
    
    // set up mocks
    when(donationBatchRepository.verifyDonationBatchExists(1L)).thenReturn(true);
    
    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }
  
  @Test
  public void testValidate_hasNoDonationBatch() throws Exception {
    // set up data
    ComponentBatchBackingForm form = new ComponentBatchBackingForm();
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentBatch");
    
    // set up mocks
    
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
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentBatch");
    
    // set up mocks
    
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
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentBatch");
    
    // set up mocks
    when(donationBatchRepository.verifyDonationBatchExists(1L)).thenReturn(false);
    
    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: No Donation Batch", errors.getFieldError("componentBatch.donationBatch"));
  }
}
