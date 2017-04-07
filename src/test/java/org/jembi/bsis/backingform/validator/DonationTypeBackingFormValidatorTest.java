package org.jembi.bsis.backingform.validator;

import static org.mockito.Mockito.when;
import static org.jembi.bsis.helpers.builders.DonationTypeBackingFormBuilder.aDonationTypeBackingForm;

import java.util.HashMap;
import java.util.UUID;

import org.jembi.bsis.backingform.DonationTypeBackingForm;
import org.jembi.bsis.backingform.validator.DonationTypeBackingFormValidator;
import org.jembi.bsis.helpers.builders.DonationTypeBuilder;
import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.repository.DonationTypeRepository;
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
    DonationTypeBackingForm form = aDonationTypeBackingForm().withDonationType("DONATIONTYPE").build();

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
    UUID donationTypeId = UUID.randomUUID();
    DonationType donationType = DonationTypeBuilder.aDonationType()
        .withId(donationTypeId)
        .withName("DONATIONTYPE")
        .build();

    DonationTypeBackingForm form = aDonationTypeBackingForm().withId(donationTypeId).withDonationType("DONATIONTYPE").build();

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
    UUID donationTypeId = UUID.randomUUID();
    DonationTypeBackingForm form = aDonationTypeBackingForm().withId(donationTypeId).withDonationType("").build();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donationType");
    donationTypeBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testInvalidDuplicate() throws Exception {
    // set up data
    UUID donationTypeId1 = UUID.randomUUID();
    UUID donationTypeId2 = UUID.randomUUID();
    DonationType duplicate = DonationTypeBuilder.aDonationType()
        .withId(donationTypeId2)
        .build();

    DonationTypeBackingForm form = aDonationTypeBackingForm().withId(donationTypeId1).withDonationType("DONATIONTYPE").build();

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
