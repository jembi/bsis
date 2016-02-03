package backingform.validator;

import static org.mockito.Mockito.when;

import helpers.builders.ComponentBuilder;
import helpers.builders.DonationBuilder;

import java.util.HashMap;

import javax.persistence.NoResultException;

import model.component.Component;
import model.donation.Donation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import repository.DonationRepository;
import repository.FormFieldRepository;
import backingform.ComponentCombinationBackingForm;

@RunWith(MockitoJUnitRunner.class)
public class ComponentCombinationBackingFormValidatorTest {

  @InjectMocks
  ComponentCombinationBackingFormValidator componentCombinationBackingFormValidator;

  @Mock
  DonationRepository donationRepository;

  @Mock
  FormFieldRepository formFieldRepository;

  @Test
  public void testValidBackingForm() throws Exception {
    Donation donation = DonationBuilder.aDonation().withDonationIdentificationNumber("DIN123").build();
    Component component = ComponentBuilder.aComponent().withDonation(donation).build();
    ComponentCombinationBackingForm form = new ComponentCombinationBackingForm();
    form.setComponent(component);
    form.setComponentTypeCombination("1");
    form.setExpiresOn("{ \"1\": \"2016-01-27T10:10:10.000+02:00\" }");

    // set up mocks
    when(donationRepository.findDonationByDonationIdentificationNumber("DIN123")).thenReturn(donation);
    when(formFieldRepository.getFormField("donor", "donorNumber")).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "componentCombination");
    componentCombinationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testInvalidEmptyDIN() throws Exception {
    Donation donation = DonationBuilder.aDonation().withDonationIdentificationNumber("").build();
    Component component = ComponentBuilder.aComponent().withDonation(donation).build();
    ComponentCombinationBackingForm form = new ComponentCombinationBackingForm();
    form.setComponent(component);
    form.setComponentTypeCombination("1");
    form.setExpiresOn("{ \"1\": \"2016-01-27T10:10:10.000+02:00\" }");

    // set up mocks
    when(formFieldRepository.getFormField("donor", "donorNumber")).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "componentCombination");
    componentCombinationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: component.donationIdentificationNumber", errors.getFieldError("component.donationIdentificationNumber"));
  }

  @Test
  public void testInvalidNoSuchDIN() throws Exception {
    Donation donation = DonationBuilder.aDonation().withDonationIdentificationNumber("DIN123").build();
    Component component = ComponentBuilder.aComponent().withDonation(donation).build();
    ComponentCombinationBackingForm form = new ComponentCombinationBackingForm();
    form.setComponent(component);
    form.setComponentTypeCombination("1");
    form.setExpiresOn("{ \"1\": \"2016-01-27T10:10:10.000+02:00\" }");

    // set up mocks
    when(donationRepository.findDonationByDonationIdentificationNumber("DIN123")).thenThrow(new NoResultException());
    when(formFieldRepository.getFormField("donor", "donorNumber")).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "componentCombination");
    componentCombinationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: component.donationIdentificationNumber", errors.getFieldError("component.donationIdentificationNumber"));
  }

  @Test
  public void testInvalidExpiresOnDateFormat() throws Exception {
    Donation donation = DonationBuilder.aDonation().withDonationIdentificationNumber("DIN123").build();
    Component component = ComponentBuilder.aComponent().withDonation(donation).build();
    ComponentCombinationBackingForm form = new ComponentCombinationBackingForm();
    form.setComponent(component);
    form.setComponentTypeCombination("1");
    form.setExpiresOn("{ \"1\": \"2016-01-27\" }");

    // set up mocks
    when(donationRepository.findDonationByDonationIdentificationNumber("DIN123")).thenReturn(donation);
    when(formFieldRepository.getFormField("donor", "donorNumber")).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "componentCombination");
    componentCombinationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: component.expiresOn", errors.getFieldError("component.expiresOn"));
  }

  @Test
  public void testInvalidExpiresOnHashMap() throws Exception {
    Donation donation = DonationBuilder.aDonation().withDonationIdentificationNumber("DIN123").build();
    Component component = ComponentBuilder.aComponent().withDonation(donation).build();
    ComponentCombinationBackingForm form = new ComponentCombinationBackingForm();
    form.setComponent(component);
    form.setComponentTypeCombination("1");
    form.setExpiresOn("TEST");

    // set up mocks
    when(donationRepository.findDonationByDonationIdentificationNumber("DIN123")).thenReturn(donation);
    when(formFieldRepository.getFormField("donor", "donorNumber")).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "componentCombination");
    componentCombinationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: component.expiresOn", errors.getFieldError("component.expiresOn"));
  }
}
