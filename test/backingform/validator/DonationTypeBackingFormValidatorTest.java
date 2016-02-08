package backingform.validator;

import static org.mockito.Mockito.when;

import helpers.builders.DonationTypeBuilder;

import java.util.HashMap;

import model.donationtype.DonationType;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import repository.DonationTypeRepository;
import repository.FormFieldRepository;
import backingform.DonationTypeBackingForm;

@RunWith(MockitoJUnitRunner.class)
public class DonationTypeBackingFormValidatorTest {

  @InjectMocks
  DonationTypeBackingFormValidator donationTypeBackingFormValidator;
  @Mock
  DonationTypeRepository donationTypeRepository;
  @Mock
  FormFieldRepository formFieldRepository;

  @Test
  public void testValid() throws Exception {
    // set up data
    DonationType donationType = DonationTypeBuilder.aDonationType()
        .withName("DONATIONTYPE")
        .build();

    DonationTypeBackingForm form = new DonationTypeBackingForm();
    form.setDonationType(donationType);

    // set up mocks
    when(donationTypeRepository.getDonationType("DONATIONTYPE")).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donationType");
    donationTypeBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testValidUpdate() throws Exception {
    // set up data
    DonationType donationType = DonationTypeBuilder.aDonationType()
        .withId(1l)
        .withName("DONATIONTYPE")
        .build();

    DonationTypeBackingForm form = new DonationTypeBackingForm();
    form.setDonationType(donationType);

    // set up mocks
    when(donationTypeRepository.getDonationType("DONATIONTYPE")).thenReturn(donationType);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donationType");
    donationTypeBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testValidBlankType() throws Exception {
    // set up data
    DonationType donationType = DonationTypeBuilder.aDonationType()
        .withId(1l)
        .withName("")
        .build();

    DonationTypeBackingForm form = new DonationTypeBackingForm();
    form.setDonationType(donationType);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donationType");
    donationTypeBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testInvalidDuplicate() throws Exception {
    // set up data
    DonationType donationType = DonationTypeBuilder.aDonationType()
        .withId(1l)
        .withName("DONATIONTYPE")
        .build();

    DonationType duplicate = DonationTypeBuilder.aDonationType()
        .withId(2l)
        .build();

    DonationTypeBackingForm form = new DonationTypeBackingForm();
    form.setDonationType(donationType);

    // set up mocks
    when(donationTypeRepository.getDonationType("DONATIONTYPE")).thenReturn(duplicate);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donationType");
    donationTypeBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: donationType exists", errors.getFieldError("type"));
  }
}
