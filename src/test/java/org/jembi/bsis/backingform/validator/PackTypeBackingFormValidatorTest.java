package org.jembi.bsis.backingform.validator;

import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.jembi.bsis.backingform.PackTypeBackingForm;
import org.jembi.bsis.backingform.validator.PackTypeBackingFormValidator;
import org.jembi.bsis.helpers.builders.PackTypeBuilder;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.repository.FormFieldRepository;
import org.jembi.bsis.repository.PackTypeRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

@RunWith(MockitoJUnitRunner.class)
public class PackTypeBackingFormValidatorTest {

  @InjectMocks
  PackTypeBackingFormValidator packTypeBackingFormValidator;
  @Mock
  private PackTypeRepository packTypeRepository;
  @Mock
  FormFieldRepository formFieldRepository;

  @Test
  public void testValid() throws Exception {
    // set up data
    PackType packType = PackTypeBuilder.aPackType()
        .withPackType("PACKTYPE")
        .withCountAsDonation(true)
        .withTestSampleProduced(true)
        .build();

    PackTypeBackingForm form = new PackTypeBackingForm();
    form.setType(packType);

    // set up mocks
    when(packTypeRepository.findPackTypeByName("PACKTYPE")).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "packType");
    packTypeBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testValidUpdate() throws Exception {
    // set up data
    PackType packType = PackTypeBuilder.aPackType()
        .withId(1l)
        .withPackType("PACKTYPE")
        .withCountAsDonation(true)
        .withTestSampleProduced(true)
        .build();

    PackTypeBackingForm form = new PackTypeBackingForm();
    form.setType(packType);

    // set up mocks
    when(packTypeRepository.findPackTypeByName("PACKTYPE")).thenReturn(packType);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "packType");
    packTypeBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testValidBlankPackType() throws Exception {
    // set up data
    PackType packType = PackTypeBuilder.aPackType()
        .withId(1l)
        .withPackType("")
        .withCountAsDonation(true)
        .withTestSampleProduced(true)
        .build();

    PackTypeBackingForm form = new PackTypeBackingForm();
    form.setType(packType);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "packType");
    packTypeBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testInvalidDuplicate() throws Exception {
    // set up data
    PackType packType = PackTypeBuilder.aPackType()
        .withId(1l)
        .withPackType("PACKTYPE")
        .withCountAsDonation(true)
        .withTestSampleProduced(true)
        .build();

    PackType duplicate = PackTypeBuilder.aPackType()
        .withId(2l)
        .build();

    PackTypeBackingForm form = new PackTypeBackingForm();
    form.setType(packType);

    // set up mocks
    when(packTypeRepository.findPackTypeByName("PACKTYPE")).thenReturn(duplicate);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "packType");
    packTypeBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: packtype exists", errors.getFieldError("type.packType"));
  }

  @Test
  public void testInvalidNoTestSampleDonation() throws Exception {
    // set up data
    PackType packType = PackTypeBuilder.aPackType()
        .withId(1l)
        .withPackType("PACKTYPE")
        .withCountAsDonation(true)
        .withTestSampleProduced(false)
        .build();

    PackTypeBackingForm form = new PackTypeBackingForm();
    form.setType(packType);

    // set up mocks
    when(packTypeRepository.findPackTypeByName("PACKTYPE")).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "packType");
    packTypeBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: can't count as donation if no test sample produced", errors.getFieldError("type.countAsDonation"));
  }
}
