package validator;

import static helpers.builders.ComponentTypeBackingFormBuilder.aComponentTypeBackingForm;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Collections;
import java.util.List;

import model.componenttype.ComponentTypeCombination;
import model.componenttype.ComponentTypeTimeUnits;

import org.junit.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import repository.ComponentTypeRepository;
import backingform.ComponentTypeBackingForm;
import backingform.validator.ComponentTypeBackingFormValidator;

public class ComponentTypeBackingFormValidatorTests {
    
    private static final String IRRELEVANT_NAME = "irrelevant.name";
    private static final String IRRELEVANT_NAME_SHORT = "irrelevant.name.short";
    private static final Integer IRRELEVANT_EXPIRES_AFTER = 24;
    private static final String IRRELEVANT_EXPIRES_AFTER_UNITS = ComponentTypeTimeUnits.HOURS.name();
    private static final String IRRELEVANT_DESCRIPTION = "irrelevant.description";
    private static final List<ComponentTypeCombination> IRRELEVANT_COMPONENT_TYPE_COMBINATIONS = Collections.emptyList();

    private ComponentTypeBackingFormValidator validator = new ComponentTypeBackingFormValidator();
    
    @Test
    public void testSupportsWithComponentTypeBackingForm_shouldReturnTrue() {
        boolean result = validator.supports(ComponentTypeBackingForm.class);
        assertThat(result, is(true));
    }
    
    @Test
    public void testSupportsWithUnsupportedClass_shouldReturnFalse() {
        boolean result = validator.supports(ComponentTypeRepository.class);
        assertThat(result, is(false));
    }
    
    @Test
    public void testValidateWithNoComponentTypeName_shouldHaveErrors() {
        ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
                .withComponentTypeName(null)
                .withComponentTypeNameShort(IRRELEVANT_NAME_SHORT)
                .withExpiresAfter(IRRELEVANT_EXPIRES_AFTER)
                .withExpiresAfterUnits(IRRELEVANT_EXPIRES_AFTER_UNITS)
                .withDescription(IRRELEVANT_DESCRIPTION)
                .thatHasBloodGroup()
                .withProducedComponentTypeCombinations(IRRELEVANT_COMPONENT_TYPE_COMBINATIONS)
                .build();
        
        Errors errors = new BindException(backingForm, "componentTypeBackingForm");
        validator.validate(backingForm, errors);
        
        assertThat(errors.getErrorCount(), is(1));
        List<FieldError> fieldErrors = errors.getFieldErrors("componentType.componentTypeName");
        assertThat(fieldErrors.size(), is(1));
        assertThat(fieldErrors.get(0).getDefaultMessage(), is("The componentTypeName is required"));
    }
    
    @Test
    public void testValidateWithNoComponentTypeNameShort_shouldHaveErrors() {
        ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
                .withComponentTypeName(IRRELEVANT_NAME)
                .withComponentTypeNameShort(null)
                .withExpiresAfter(IRRELEVANT_EXPIRES_AFTER)
                .withExpiresAfterUnits(IRRELEVANT_EXPIRES_AFTER_UNITS)
                .withDescription(IRRELEVANT_DESCRIPTION)
                .thatHasBloodGroup()
                .withProducedComponentTypeCombinations(IRRELEVANT_COMPONENT_TYPE_COMBINATIONS)
                .build();
        
        Errors errors = new BindException(backingForm, "componentTypeBackingForm");
        validator.validate(backingForm, errors);
        
        assertThat(errors.getErrorCount(), is(1));
        List<FieldError> fieldErrors = errors.getFieldErrors("componentType.componentTypeNameShort");
        assertThat(fieldErrors.size(), is(1));
        assertThat(fieldErrors.get(0).getDefaultMessage(), is("The componentTypeNameShort is required"));
    }
    
    @Test
    public void testValidateWithNoExpiresAfter_shouldHaveErrors() {
        ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
                .withComponentTypeName(IRRELEVANT_NAME)
                .withComponentTypeNameShort(IRRELEVANT_NAME_SHORT)
                .withExpiresAfter(null)
                .withExpiresAfterUnits(IRRELEVANT_EXPIRES_AFTER_UNITS)
                .withDescription(IRRELEVANT_DESCRIPTION)
                .thatHasBloodGroup()
                .withProducedComponentTypeCombinations(IRRELEVANT_COMPONENT_TYPE_COMBINATIONS)
                .build();
        
        Errors errors = new BindException(backingForm, "componentTypeBackingForm");
        validator.validate(backingForm, errors);
        
        assertThat(errors.getErrorCount(), is(1));
        List<FieldError> fieldErrors = errors.getFieldErrors("componentType.expiresAfter");
        assertThat(fieldErrors.size(), is(1));
        assertThat(fieldErrors.get(0).getDefaultMessage(), is("The expiresAfter value is required"));
    }
    
    @Test
    public void testValidateWithNoExpiresAfterUnits_shouldHaveErrors() {
        ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
                .withComponentTypeName(IRRELEVANT_NAME)
                .withComponentTypeNameShort(IRRELEVANT_NAME_SHORT)
                .withExpiresAfter(IRRELEVANT_EXPIRES_AFTER)
                .withExpiresAfterUnits(null)
                .withDescription(IRRELEVANT_DESCRIPTION)
                .thatHasBloodGroup()
                .withProducedComponentTypeCombinations(IRRELEVANT_COMPONENT_TYPE_COMBINATIONS)
                .build();
        
        Errors errors = new BindException(backingForm, "componentTypeBackingForm");
        validator.validate(backingForm, errors);
        
        assertThat(errors.getErrorCount(), is(1));
        List<FieldError> fieldErrors = errors.getFieldErrors("componentType.expiresAfterUnits");
        assertThat(fieldErrors.size(), is(1));
        assertThat(fieldErrors.get(0).getDefaultMessage(), is("The expiresAfterUnits value is required"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidateWithInvalidExpiresAfterUnits_shouldThrow() {
        ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
                .withComponentTypeName(IRRELEVANT_NAME)
                .withComponentTypeNameShort(IRRELEVANT_NAME_SHORT)
                .withExpiresAfter(IRRELEVANT_EXPIRES_AFTER)
                .withExpiresAfterUnits("INVALID")
                .withDescription(IRRELEVANT_DESCRIPTION)
                .thatHasBloodGroup()
                .withProducedComponentTypeCombinations(IRRELEVANT_COMPONENT_TYPE_COMBINATIONS)
                .build();
        
        Errors errors = new BindException(backingForm, "componentTypeBackingForm");
        validator.validate(backingForm, errors);
    }
    
    @Test
    public void testValidateWithNoDescription_shouldHaveErrors() {
        ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
                .withComponentTypeName(IRRELEVANT_NAME)
                .withComponentTypeNameShort(IRRELEVANT_NAME_SHORT)
                .withExpiresAfter(IRRELEVANT_EXPIRES_AFTER)
                .withExpiresAfterUnits(IRRELEVANT_EXPIRES_AFTER_UNITS)
                .withDescription(null)
                .thatHasBloodGroup()
                .withProducedComponentTypeCombinations(IRRELEVANT_COMPONENT_TYPE_COMBINATIONS)
                .build();
        
        Errors errors = new BindException(backingForm, "componentTypeBackingForm");
        validator.validate(backingForm, errors);
        
        assertThat(errors.getErrorCount(), is(1));
        List<FieldError> fieldErrors = errors.getFieldErrors("componentType.description");
        assertThat(fieldErrors.size(), is(1));
        assertThat(fieldErrors.get(0).getDefaultMessage(), is("The description is required"));
    }
    
    @Test
    public void testValidateWithNoHasBloodGroup_shouldHaveErrors() {
        ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
                .withComponentTypeName(IRRELEVANT_NAME)
                .withComponentTypeNameShort(IRRELEVANT_NAME_SHORT)
                .withExpiresAfter(IRRELEVANT_EXPIRES_AFTER)
                .withExpiresAfterUnits(IRRELEVANT_EXPIRES_AFTER_UNITS)
                .withDescription(IRRELEVANT_DESCRIPTION)
                .withProducedComponentTypeCombinations(IRRELEVANT_COMPONENT_TYPE_COMBINATIONS)
                .build();
        
        Errors errors = new BindException(backingForm, "componentTypeBackingForm");
        validator.validate(backingForm, errors);
        
        assertThat(errors.getErrorCount(), is(1));
        List<FieldError> fieldErrors = errors.getFieldErrors("componentType.hasBloodGroup");
        assertThat(fieldErrors.size(), is(1));
        assertThat(fieldErrors.get(0).getDefaultMessage(), is("The Has Blood Group is required"));
    }
    
    @Test
    public void testValidateWithNoProducedComponentTypeCombinations_shouldHaveErrors() {
        ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
                .withComponentTypeName(IRRELEVANT_NAME)
                .withComponentTypeNameShort(IRRELEVANT_NAME_SHORT)
                .withExpiresAfter(IRRELEVANT_EXPIRES_AFTER)
                .withExpiresAfterUnits(IRRELEVANT_EXPIRES_AFTER_UNITS)
                .withDescription(IRRELEVANT_DESCRIPTION)
                .thatHasBloodGroup()
                .withProducedComponentTypeCombinations(null)
                .build();
        
        Errors errors = new BindException(backingForm, "componentTypeBackingForm");
        validator.validate(backingForm, errors);
        
        assertThat(errors.getErrorCount(), is(1));
        List<FieldError> fieldErrors = errors.getFieldErrors("componentType.producedComponentTypeCombinations");
        assertThat(fieldErrors.size(), is(1));
        assertThat(fieldErrors.get(0).getDefaultMessage(), is("The Produced Component Type Combinations is required"));
    }
    
    @Test
    public void testValidateWithAValidBackingForm_shouldHaveNoErrors() {
        ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
                .withComponentTypeName(IRRELEVANT_NAME)
                .withComponentTypeNameShort(IRRELEVANT_NAME_SHORT)
                .withExpiresAfter(IRRELEVANT_EXPIRES_AFTER)
                .withExpiresAfterUnits(IRRELEVANT_EXPIRES_AFTER_UNITS)
                .withDescription(IRRELEVANT_DESCRIPTION)
                .thatHasBloodGroup()
                .withProducedComponentTypeCombinations(IRRELEVANT_COMPONENT_TYPE_COMBINATIONS)
                .build();
        
        Errors errors = new BindException(backingForm, "componentTypeBackingForm");
        validator.validate(backingForm, errors);
        
        assertThat(errors.getErrorCount(), is(0));
    }

}
