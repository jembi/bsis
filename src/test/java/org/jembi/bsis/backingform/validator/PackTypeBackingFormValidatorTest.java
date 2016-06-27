package org.jembi.bsis.backingform.validator;

import static org.jembi.bsis.helpers.builders.PackTypeBackingFormBuilder.aPackTypeBackingForm;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.jembi.bsis.backingform.PackTypeBackingForm;
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
  private PackTypeBackingFormValidator packTypeBackingFormValidator;
  @Mock
  private PackTypeRepository packTypeRepository;
  @Mock
  private FormFieldRepository formFieldRepository;

  @Test
  public void testValidCountAsDonation() throws Exception {
    // set up data
    String packTypeName = "PACKTYPE";

    PackTypeBackingForm form = aPackTypeBackingForm()
        .withPackType(packTypeName)
        .withCountAsDonation(true)
        .withTestSampleProduced(true)
        .withMaxWeight(100)
        .withMinWeight(10)
        .withLowVolumeWeight(25)
        .build();

    // set up mocks
    when(packTypeRepository.findPackTypeByName(packTypeName)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<>(), "packType");
    packTypeBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }
  
  @Test
  public void testValidTestSampleOnly() throws Exception {
    // set up data
    String packTypeName = "PACKTYPE";

    PackTypeBackingForm form = aPackTypeBackingForm()
        .withPackType(packTypeName)
        .withCountAsDonation(false)
        .withTestSampleProduced(true)
        .build();

    // set up mocks
    when(packTypeRepository.findPackTypeByName(packTypeName)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<>(), "packType");
    packTypeBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testValidUpdate() throws Exception {
    // set up data
    Long packTypeId = 1L;
    String packTypeName = "PACKTYPE";

    PackType packType = aPackType()
        .withId(packTypeId)
        .withPackType(packTypeName)
        .withCountAsDonation(false)
        .withTestSampleProduced(true)
        .build();

    PackTypeBackingForm form = aPackTypeBackingForm()
        .withId(packTypeId)
        .withPackType(packTypeName)
        .withCountAsDonation(false)
        .withTestSampleProduced(true)
        .build();

    // set up mocks
    when(packTypeRepository.findPackTypeByName(packTypeName)).thenReturn(packType);

    // run test
    Errors errors = new MapBindingResult(new HashMap<>(), "packType");
    packTypeBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testValidBlankPackType() throws Exception {
    // set up data
    PackTypeBackingForm form = aPackTypeBackingForm()
        .withId(1L)
        .withPackType("")
        .withCountAsDonation(false)
        .withTestSampleProduced(true)
        .build();

    // run test
    Errors errors = new MapBindingResult(new HashMap<>(), "packType");
    packTypeBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testInvalidDuplicate() throws Exception {
    // set up data
    String packTypeName = "PACKTYPE";

    PackTypeBackingForm form = aPackTypeBackingForm()
        .withId(1L)
        .withPackType(packTypeName)
        .withCountAsDonation(false)
        .withTestSampleProduced(true)
        .build();

    PackType duplicatePackType = aPackType().withId(2l).withPackType(packTypeName).build();

    // set up mocks
    when(packTypeRepository.findPackTypeByName(packTypeName)).thenReturn(duplicatePackType);

    // run test
    Errors errors = new MapBindingResult(new HashMap<>(), "packType");
    packTypeBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: packtype exists", errors.getFieldError("packType"));
  }

  @Test
  public void testInvalidNoTestSampleDonation() throws Exception {
    // set up data
    String packTypeName = "PACKTYPE";

    PackTypeBackingForm form = aPackTypeBackingForm()
        .withId(1L)
        .withPackType(packTypeName)
        .withCountAsDonation(true)
        .withTestSampleProduced(false)
        .withMaxWeight(100)
        .withMinWeight(10)
        .withLowVolumeWeight(25)
        .build();

    // run test
    Errors errors = new MapBindingResult(new HashMap<>(), "packType");
    packTypeBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: can't count as donation if no test sample produced", errors.getFieldError("countAsDonation"));
  }
  
  @Test
  public void testInvalidWeights() throws Exception {
    // set up data
    String packTypeName = "PACKTYPE";

    PackTypeBackingForm form = aPackTypeBackingForm()
        .withPackType(packTypeName)
        .withCountAsDonation(true)
        .withTestSampleProduced(true)
        .build();

    // set up mocks
    when(packTypeRepository.findPackTypeByName(packTypeName)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<>(), "packType");
    packTypeBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 3, errors.getErrorCount());
    Assert.assertNotNull("Error: minWeight", errors.getFieldError("minWeight"));
    Assert.assertNotNull("Error: maxWeight", errors.getFieldError("maxWeight"));
    Assert.assertNotNull("Error: lowVolumeWeight", errors.getFieldError("lowVolumeWeight"));
  }
}
