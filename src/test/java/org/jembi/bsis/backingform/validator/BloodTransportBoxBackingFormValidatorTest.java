package org.jembi.bsis.backingform.validator;

import java.util.HashMap;

import org.jembi.bsis.backingform.BloodTransportBoxBackingForm;
import org.jembi.bsis.backingform.validator.BloodTransportBoxBackingFormValidator;
import org.jembi.bsis.repository.FormFieldRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

@RunWith(MockitoJUnitRunner.class)
public class BloodTransportBoxBackingFormValidatorTest {

  @InjectMocks
  private BloodTransportBoxBackingFormValidator validator;
  
  @Mock
  FormFieldRepository formFieldRepository;
  
  @Test
  public void testValidate_hasNoErrors() throws Exception {
    // set up data
    BloodTransportBoxBackingForm form = new BloodTransportBoxBackingForm();
    form.setTemperature(0.0);
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTransportBox");
    
    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }
}
