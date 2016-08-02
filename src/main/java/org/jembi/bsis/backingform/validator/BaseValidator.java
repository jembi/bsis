package org.jembi.bsis.backingform.validator;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.jembi.bsis.model.admin.FormField;
import org.jembi.bsis.repository.FormFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Superclass for all validators used to check the form data and determine if the data entered into
 * the system is valid.
 *
 * Extend this class and implement the abstract method validateForm in order to add custom
 * validation.
 *
 * @param <T> class definition of the form Javabean
 */
@Component
public abstract class BaseValidator<T> implements Validator {

  private static final Logger LOGGER = Logger.getLogger(BaseValidator.class);

  @Autowired
  private FormFieldRepository formFieldRepository;

  @Override
  public boolean supports(Class<?> clazz) {
    Class<?> thisClazz = GenericTypeResolver.resolveTypeArgument(getClass(), BaseValidator.class);
    return thisClazz.isAssignableFrom(clazz);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void validate(Object target, Errors errors) {
    LOGGER.trace("Start validation for " + getFormName());

    if (target == null) {
      return;
    }

    T backingForm = (T) target;
    validateForm(backingForm, errors);

    LOGGER.trace("End validator for " + getFormName() + " with errors " + errors);
  }

  /**
   * Method to override in order to use BaseValidator and add custom validations. This method is
   * called by the BaseValidator.
   *
   * @param form   T JavaBean with form data
   * @param errors Errors containing any validator errors
   */
  public abstract void validateForm(T form, Errors errors);

  /**
   * Specifies the name of the form which is used to retrieve Form configuration
   *
   * @return String name of the form
   */
  public abstract String getFormName();

  /**
   * Specifies if the form has a base entity. This will be used when generating the error keys. By
   * default is true.
   *
   * @return true, if successful
   */
  protected boolean formHasBaseEntity() {
    return true;
  }

  /**
   * Performs the common field checks which are: 1) checking that the required fields have been
   * filled in 2) checking that the length of the data is within the configured maximums
   *
   * @param form   Object JavaBean containing the form data
   * @param errors Errors containing all the errors in the form data
   * @throws BaseValidatorRuntimeException if there are exceptions that occur while inspecting the
   *                                       form properties.
   */
  protected void commonFieldChecks(T form, Errors errors) {
    try {
      checkRequiredFields(form, errors);
      checkFieldLengths(form, errors);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      throw new BaseValidatorRuntimeException("Exception while validating form: "
          + ReflectionToStringBuilder.toString(form), errors, e);
    }
  }

  /**
   * Checks if the length of the data in the specified form complies to the configured max lengths
   * for each field.
   *
   * @param form   Object JavaBean containing the form data
   * @param errors Errors containing all the errors in the form data
   * @throws IllegalAccessException    if the form has properties that cannot be accessed due to
   *                                   permissions
   * @throws InvocationTargetException if the form property accessor methods throw an exception
   * @throws NoSuchMethodException     if a form accessor method for a property cannot be found
   */
  protected void checkFieldLengths(T form, Errors errors)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    @SuppressWarnings("unchecked")
    Map<String, Object> properties = BeanUtils.describe(form);
    Map<String, Integer> maxLengths = formFieldRepository.getFieldMaxLengths(getFormName());
    for (String field : maxLengths.keySet()) {
      if (properties.containsKey(field)) {
        Object fieldValue = properties.get(field);
        Integer maxLength = maxLengths.get(field);
        if (fieldValue != null && maxLength > 0
            && (fieldValue instanceof String && ((String) fieldValue).length() > maxLength)) {
          String fieldName = field;
          if (formHasBaseEntity()) {
            fieldName = getFormName() + "." + field;
          }
          errors.rejectValue(fieldName, "fieldLength.error",
              "Maximum length for this field is " + maxLength);
        }
      }
    }
  }

  /**
   * Checks if all the fields configured as required have been captured in the form
   *
   * @param form   Object JavaBean containing the form data
   * @param errors Errors containing all the errors in the form data
   * @throws IllegalAccessException    if the form has properties that cannot be accessed due to
   *                                   permissions
   * @throws InvocationTargetException if the form property accessor methods throw an exception
   * @throws NoSuchMethodException     if a form accessor method for a property cannot be found
   */
  @SuppressWarnings("unchecked")
  protected void checkRequiredFields(T form, Errors errors)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    Map<String, Object> properties = BeanUtils.describe(form);
    List<String> requiredFields = formFieldRepository.getRequiredFormFields(getFormName());
    for (String requiredField : requiredFields) {
      if (properties.containsKey(requiredField)) {
        Object fieldValue = properties.get(requiredField);
        if (fieldValue == null || (fieldValue instanceof String && StringUtils.isBlank((String) fieldValue))) {
          String fieldName = requiredField;
          if (formHasBaseEntity()) {
            fieldName = getFormName() + "." + requiredField;
          }
          errors.rejectValue(fieldName, "requiredField.error", "This information is required");
        }
      }
    }
  }

  /**
   * Checks the form configuration and determines if the field is auto generated or not
   *
   * @param formName  String name of the form
   * @param fieldName String name of the field
   * @return boolean true if the field value is generated automatically, false otherwise
   */
  protected boolean isFieldAutoGenerated(String fieldName) {
    FormField formField = formFieldRepository.getFormField(getFormName(), fieldName);
    if (formField == null) {
      return false;
    }
    return formField.getAutoGenerate();
  }

  /**
   * Determines if a field uses the current time for not
   *
   * @param formName  String name of the form
   * @param fieldName String name of the field
   * @return boolean true if the field value uses the current time, false otherwise
   */
  protected boolean doesFieldUseCurrentTime(String fieldName) {
    FormField formField = formFieldRepository.getFormField(getFormName(), fieldName);
    if (formField == null) {
      return false;
    }
    return formField.getUseCurrentTime();
  }

  /**
   * Determines if the specified date is in the future or not
   *
   * @param date Date to validate
   * @return boolean if date is after the current timestamp, false is not
   */
  protected boolean isFutureDate(Date date) {
    Date today = new Date();
    if (date.after(today)) {
      return true;
    } else {
      return false;
    }
  }
}
